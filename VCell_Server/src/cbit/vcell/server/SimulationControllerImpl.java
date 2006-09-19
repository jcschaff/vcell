package cbit.vcell.server;
import cbit.util.ConfigurationException;
import cbit.util.DataAccessException;
import cbit.util.SessionLog;
import cbit.util.User;
/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.io.File;
import java.rmi.*;

import cbit.vcell.server.solvers.LocalSolverController;
import cbit.vcell.solver.*;
import cbit.vcell.solvers.*;
import cbit.vcell.messaging.db.SimulationJobStatus;
import cbit.vcell.messaging.db.UpdateSynchronizationException;
import cbit.vcell.messaging.db.VCellServerID;
import cbit.gui.PropertyLoader;
import cbit.rmi.event.*;
import javax.swing.event.EventListenerList;
import cbit.vcell.messaging.server.DispatcherDbManager;
import cbit.vcell.messaging.server.LocalDispatcherDbManager;

/**
 * Insert the type's description here.
 * Creation date: (6/28/01 12:50:36 PM)
 * @author: Jim Schaff
 */
public class SimulationControllerImpl implements SimulationJobStatusSender, WorkerEventListener {
	private java.util.Hashtable solverProxyHash = new java.util.Hashtable();
	private SessionLog adminSessionLog = null;
	private cbit.vcell.server.LocalVCellServer fieldLocalVCellServer = null;
	private AdminDatabaseServer adminDbServer = null;
	private EventListenerList listenerList = new javax.swing.event.EventListenerList();

	private DispatcherDbManager dispatcherDbManager = new LocalDispatcherDbManager();

/**
 * SimulationControllerImpl constructor comment.
 */
public SimulationControllerImpl(SessionLog argAdminSessionLog, AdminDatabaseServer argAdminDbServer, LocalVCellServer argLocalVCellServer) {
	super();
	adminSessionLog = argAdminSessionLog;
	fieldLocalVCellServer = argLocalVCellServer;
	adminDbServer = argAdminDbServer;
}

/**
 * addSimulationStatusEventListener method comment.
 */
public void addSimulationJobStatusListener(cbit.rmi.event.SimulationJobStatusListener listener) {
	listenerList.add(SimulationJobStatusListener.class, listener);
}


/**
 * Insert the method's description here.
 * Creation date: (6/28/01 1:19:54 PM)
 * @return cbit.vcell.solvers.SolverController
 * @param simulation cbit.vcell.solver.Simulation
 */
private SolverController createNewSolverController(User user, SimulationJob simulationJob, SessionLog userSessionLog) throws RemoteException, SimExecutionException, SolverException {
	Simulation simulation = simulationJob.getWorkingSim();
	if (getLocalVCellServer().isPrimaryServer()){
		ComputeHost[] allActiveHosts = getLocalVCellServer().getConnectionPoolStatus().getActiveHosts();
		// now limit to only those appopriate to the simulation type
		ComputeHost[] activeHosts = null;
		if (allActiveHosts != null) {
			java.util.Vector v = new java.util.Vector();
			int simType;
			if (simulation.getIsSpatial()) {
				simType = ComputeHost.PDEComputeHost;
			} else {
				simType = ComputeHost.ODEComputeHost;
			}
			for (int i=0;i<allActiveHosts.length;i++){
				if (allActiveHosts[i].getType() == simType) {
					v.addElement(allActiveHosts[i]);
				}
			}
			if (v.size() > 0) {
				activeHosts = new ComputeHost[v.size()];
				v.copyInto(activeHosts);
			}
		}
		// now select one
		int numActiveHosts = (activeHosts==null)?(0):(activeHosts.length);
		VCellServer vcellServers[] = new VCellServer[numActiveHosts];
		ProcessStatus processStatus[] = new ProcessStatus[numActiveHosts];
		for (int i=0;i<numActiveHosts;i++){
			try {
				vcellServers[i] = getLocalVCellServer().getSlaveVCellServer(activeHosts[i].getHostName());
				//
				// try to connection MessageHandlers to VCellConnection on this host
				//
				VCellConnection vcConn = null;
				LocalVCellConnection localVCellConnection = (LocalVCellConnection)getLocalVCellServer().getVCellConnection(user);
				String password = localVCellConnection.getPassword();
				VCellConnectionFactory vcConnFactory = new RMIVCellConnectionFactory(activeHosts[i].getHostName(),user.getName(),password);
				VCellConnection remoteVCellConnection = vcConnFactory.createVCellConnection();
				if (remoteVCellConnection!=null && !localVCellConnection.getRemoteMessageHandler().isConnected(remoteVCellConnection.getRemoteMessageHandler())){
					cbit.rmi.event.RemoteMessageHandler localMessageHandler = localVCellConnection.getRemoteMessageHandler();
					cbit.rmi.event.RemoteMessageHandler remoteMessageHandler = remoteVCellConnection.getRemoteMessageHandler();
					localMessageHandler.addRemoteMessageListener(remoteMessageHandler, remoteMessageHandler.getRemoteMesssageListenerID());
					remoteMessageHandler.addRemoteMessageListener(localMessageHandler, localMessageHandler.getRemoteMesssageListenerID());
				}
				//
				// get Slave Server metrics
				//
				processStatus[i] = vcellServers[i].getProcessStatus();
				userSessionLog.print("server("+activeHosts[i]+") status="+processStatus[i]);
			}catch (AuthenticationException e){
				userSessionLog.print("authentication failure contacting server("+activeHosts[i]+"): "+e.getMessage());
				adminSessionLog.exception(e);
			}catch (ConnectionException e){
				userSessionLog.print("connection failure contacting server("+activeHosts[i]+"): "+e.getMessage());
				adminSessionLog.exception(e);
			}catch (DataAccessException e){
				userSessionLog.print("data access failure contacting server("+activeHosts[i]+"): "+e.getMessage());
				adminSessionLog.exception(e);
			}catch (Throwable e){
				userSessionLog.print("unexpected failure contacting server("+activeHosts[i]+"): "+e.getMessage());
				adminSessionLog.exception(e);
			}
		}
		int bestServerIndex = -1;
		for (int i=0;i<numActiveHosts;i++){
			if (processStatus[i]!=null){
				if (bestServerIndex==-1){
					bestServerIndex=i;
				}else{
					//
					// see if this server is more appropriate
					//
					double cpuLoad = Math.max(1.0-processStatus[i].getFractionFreeCPU(), processStatus[i].getNumJobsRunning()/(double)processStatus[i].getNumProcessors());
					double cpuLoadBest = Math.max(1.0-processStatus[bestServerIndex].getFractionFreeCPU(), processStatus[bestServerIndex].getNumJobsRunning()/(double)processStatus[bestServerIndex].getNumProcessors());
					long memoryBytes = 0;
					long memoryBytesBest = 0;
					if (simulation.getSolverTaskDescription().getSolverDescription().isInterpretedSolver()){
						//
						// runs within JVM (just care about memory ... don't want to kill servers)
						//
						memoryBytes = processStatus[i].getAvaillableJavaMemoryBytes();
						memoryBytesBest = processStatus[bestServerIndex].getAvaillableJavaMemoryBytes();
					}else{
						//
						// runs outside JVM (numJobs has priority, then free non-java memory)
						//
						memoryBytes = processStatus[i].getFreeMemoryBytes();
						memoryBytesBest = processStatus[bestServerIndex].getFreeMemoryBytes();
					}
					
					//
					// scheduling criteria (CPU most important, Memory discriminates between equally loaded nodes)
					//
					if (cpuLoad < cpuLoadBest){
						bestServerIndex = i;
					}else if (cpuLoad == cpuLoadBest && memoryBytes > memoryBytesBest){
						bestServerIndex = i;
					}
				}
			}
		}
		if (bestServerIndex>-1){
			userSessionLog.alert("returning remote SolverController("+activeHosts[bestServerIndex]+") for "+simulationJob.getSimulationJobID());
			return vcellServers[bestServerIndex].createSolverController(user,simulationJob);
		}else{
			ComputeHost[] potentialHosts = getLocalVCellServer().getConnectionPoolStatus().getPotentialHosts();
			if (potentialHosts != null && potentialHosts.length > 0){
				//
				// this is a primary server with computeServers registered, don't run locally, fail instead
				// 
				throw new SimExecutionException("Failed to dispatch simulation, couldn't contact compute servers");
			}
		}
	}
	//
	// either no appropriate slave server or THIS IS A SLAVE SERVER (can't pass the buck).
	//
	LocalVCellConnection localVCellConnection = (LocalVCellConnection)getLocalVCellServer().getVCellConnection(user);
	LocalSolverController localSolverController = new LocalSolverController(
		localVCellConnection,
		userSessionLog,
		simulationJob,
		getUserSimulationDirectory(user, PropertyLoader.getRequiredProperty(PropertyLoader.localSimDataDirProperty)),
		getUserSimulationDirectory(user, PropertyLoader.getRequiredProperty(PropertyLoader.serverSimDataDirProperty))
		);

	if (getLocalVCellServer().isPrimaryServer()) {
		localSolverController.addWorkerEventListener(this);
	} else {
		localSolverController.addWorkerEventListener(localVCellConnection.getMessageService().getMessageCollector());
	}
	//localSolverController.addJobCompletedListener(localVCellConnection.getMessageService().getMessageCollector());
	//localSolverController.addJobDataListener(localVCellConnection.getMessageService().getMessageCollector());
	//localSolverController.addJobFailureListener(localVCellConnection.getMessageService().getMessageCollector());
	//localSolverController.addJobProgressListener(localVCellConnection.getMessageService().getMessageCollector());
	//localSolverController.addJobStartingListener(localVCellConnection.getMessageService().getMessageCollector());
	userSessionLog.alert("returning local SolverController for "+simulationJob.getSimulationJobID());
	return localSolverController;
}


/**
 * Insert the method's description here.
 * Creation date: (11/17/2000 11:43:22 AM)
 * @param event cbit.rmi.event.JobCompletedEvent
 */
protected void fireSimulationJobStatusEvent(SimulationJobStatusEvent event) {
	// Guaranteed to return a non-null array
	Object[] listeners = listenerList.getListenerList();
	// Reset the source to allow proper wiring
	event.setSource(this);
	// Process the listeners last to first, notifying
	// those that are interested in this event
	for (int i = listeners.length-2; i>=0; i-=2) {
	    if (listeners[i]==SimulationJobStatusListener.class) {
		  ((SimulationJobStatusListener)listeners[i+1]).simulationJobStatusChanged(event);
	    }	       
	}
}


/**
 * Insert the method's description here.
 * Creation date: (6/28/01 4:33:49 PM)
 * @return cbit.vcell.server.LocalVCellServer
 */
public cbit.vcell.server.LocalVCellServer getLocalVCellServer() {
	return fieldLocalVCellServer;
}


/**
 * Insert the method's description here.
 * Creation date: (6/28/01 2:11:05 PM)
 * @return cbit.vcell.solvers.SolverControllerInfo[]
 */
public SolverControllerInfo[] getSolverControllerInfos() {
	System.out.println("SimulationControllerImpl.getSolverControllerInfos()");
	java.util.Vector scList = new java.util.Vector();
	java.util.Enumeration solverProxyEnum = solverProxyHash.elements();
	while (solverProxyEnum.hasMoreElements()){
		SolverProxy solverProxy = (SolverProxy)solverProxyEnum.nextElement();
		scList.add(new SolverControllerInfo(solverProxy));
	}
	return (SolverControllerInfo[])cbit.util.BeanUtils.getArray(scList,SolverControllerInfo.class);
}


/**
 * This method was created by a SmartGuide.
 * @exception java.rmi.RemoteException The exception description.
 */
SolverProxy getSolverProxy(User user, SimulationJob simulationJob, SessionLog userSessionLog) throws RemoteException, SimExecutionException, SolverException, PermissionException, DataAccessException {
	VCSimulationIdentifier vcSimID = simulationJob.getWorkingSim().getSimulationInfo().getAuthoritativeVCSimulationIdentifier();
	if (vcSimID == null){
		throw new IllegalArgumentException("cannot run an unsaved simulation");
	}
	if (!simulationJob.getWorkingSim().getVersion().getOwner().equals(user)){
		throw new PermissionException("insufficient privilege: startSimulation()");
	}
	SolverProxy solverProxy = (SolverProxy)solverProxyHash.get(simulationJob.getSimulationJobID());
	if (solverProxy==null){
		solverProxy = new SolverProxy(userSessionLog, simulationJob);
		solverProxy.setSolverController(createNewSolverController(user,simulationJob,userSessionLog));
		solverProxyHash.put(simulationJob.getSimulationJobID(),solverProxy);
	}
	return solverProxy;
}

/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 * @exception java.rmi.RemoteException The exception description.
 */
public SolverStatus getSolverStatus(User user, SimulationInfo simulationInfo, int jobIndex) throws RemoteException, PermissionException, DataAccessException {
	SolverProxy solverProxy = (SolverProxy)solverProxyHash.get(SimulationJob.createSimulationJobID(Simulation.createSimulationID(simulationInfo.getAuthoritativeVCSimulationIdentifier().getSimulationKey()),jobIndex));
	if (solverProxy==null){
		return new SolverStatus(SolverStatus.SOLVER_READY);
	}
	return solverProxy.getSolverStatus();
}


private File getUserSimulationDirectory(User user, String simDataRoot) {
	File directory = new File(new File(simDataRoot), user.getName());
	if (!directory.exists()){
		if (!directory.mkdirs()){
			String msg = "could not create directory "+directory;
			adminSessionLog.alert(msg);
			throw new ConfigurationException(msg);
		}
	}		 
	return directory;
}


/**
 * Insert the method's description here.
 * Creation date: (2/11/2004 11:26:21 AM)
 * @param ex java.lang.Exception
 */
private void handleException(VCSimulationIdentifier vcSimulationIdentifier, int jobIndex, Exception ex) {
	VCellServerID serverID = VCellServerID.getSystemServerID();
	try {
		SimulationJobStatus oldJobStatus = adminDbServer.getSimulationJobStatus(vcSimulationIdentifier.getSimulationKey(), jobIndex);
		if (oldJobStatus != null) {
			serverID = oldJobStatus.getServerID();
		}
		SimulationJobStatus newJobStatus = updateFailedJobStatus(oldJobStatus, vcSimulationIdentifier, jobIndex, ex.getMessage());
		if (newJobStatus == null) {
			newJobStatus = new SimulationJobStatus(serverID, vcSimulationIdentifier, jobIndex, null, SimulationJobStatus.SCHEDULERSTATUS_FAILED, -1, ex.getMessage(), null, null);
		}
		
		SimulationJobStatusEvent event = new SimulationJobStatusEvent(this, Simulation.createSimulationID(vcSimulationIdentifier.getSimulationKey()), newJobStatus, null, null);
		fireSimulationJobStatusEvent(event);
	} catch (DataAccessException e) {
		SimulationJobStatus newJobStatus = new SimulationJobStatus(serverID, vcSimulationIdentifier, jobIndex, null, SimulationJobStatus.SCHEDULERSTATUS_FAILED, -1, e.getMessage(), null, null);
		SimulationJobStatusEvent event = new SimulationJobStatusEvent(this, Simulation.createSimulationID(vcSimulationIdentifier.getSimulationKey()), newJobStatus, null, null);
		fireSimulationJobStatusEvent(event);
	} catch (RemoteException e) {
		SimulationJobStatus newJobStatus = new SimulationJobStatus(serverID, vcSimulationIdentifier, jobIndex, null, SimulationJobStatus.SCHEDULERSTATUS_FAILED, -1, e.getMessage(), null, null);
		SimulationJobStatusEvent event = new SimulationJobStatusEvent(this, Simulation.createSimulationID(vcSimulationIdentifier.getSimulationKey()), newJobStatus, null, null);
		fireSimulationJobStatusEvent(event);
	}	
}


/**
 * Insert the method's description here.
 * Creation date: (3/11/2004 10:44:18 AM)
 * @param newJobStatus cbit.vcell.messaging.db.SimulationJobStatus
 * @param progress java.lang.Double
 * @param timePoint java.lang.Double
 */
public void onWorkerEvent(cbit.rmi.event.WorkerEvent workerEvent) {	
	try {
		VCSimulationIdentifier vcSimulationIdentifier = workerEvent.getVCSimulationDataIdentifier().getVcSimID();
		int jobIndex = workerEvent.getJobIndex();
		SimulationJobStatus oldJobStatus = adminDbServer.getSimulationJobStatus(vcSimulationIdentifier.getSimulationKey(), jobIndex);
		SimulationJobStatus newJobStatus = null;

		if (oldJobStatus == null || oldJobStatus.isDone()) {
			return;
		}
		
		if (workerEvent.isCompletedEvent()) {
			newJobStatus = updateCompletedJobStatus(oldJobStatus, vcSimulationIdentifier, jobIndex);			
			
		} else if (workerEvent.isFailedEvent()) {
			newJobStatus = updateFailedJobStatus(oldJobStatus, vcSimulationIdentifier, jobIndex, workerEvent.getEventMessage());			
			
		} else if (workerEvent.isNewDataEvent()) {
			newJobStatus = updateRunningJobStatus(oldJobStatus, vcSimulationIdentifier, jobIndex, true, null);
			
		} else if (workerEvent.isProgressEvent()) {
			newJobStatus = updateRunningJobStatus(oldJobStatus, vcSimulationIdentifier, jobIndex, false, null);
			
		} else if (workerEvent.isStartingEvent()) {
			newJobStatus = updateRunningJobStatus(oldJobStatus, vcSimulationIdentifier, jobIndex, false, workerEvent.getEventMessage());			
		}
		if (newJobStatus != null && (!newJobStatus.compareEqual(oldJobStatus) || workerEvent.isProgressEvent() || workerEvent.isNewDataEvent())) {
			Double progress = workerEvent.getProgress();
			Double timepoint = workerEvent.getTimePoint();
			SimulationJobStatusEvent newEvent = new SimulationJobStatusEvent(this, Simulation.createSimulationID(vcSimulationIdentifier.getSimulationKey()), newJobStatus, progress, timepoint);
			fireSimulationJobStatusEvent(newEvent);
		}	
	} catch (DataAccessException ex) {
		adminSessionLog.exception(ex);
	} catch (RemoteException ex) {
		adminSessionLog.exception(ex);
	}
}


/**
 * removeSimulationStatusEventListener method comment.
 */
public void removeSimulationJobStatusListener(cbit.rmi.event.SimulationJobStatusListener listener) {
	listenerList.remove(SimulationJobStatusListener.class, listener);
}


/**
 * This method was created by a SmartGuide.
 * @exception java.rmi.RemoteException The exception description.
 */
public void startSimulation(User user, Simulation simulation, SessionLog userSessionLog) throws RemoteException {
	LocalVCellConnection localVCellConnection = (LocalVCellConnection)getLocalVCellServer().getVCellConnection(user);
	removeSimulationJobStatusListener(localVCellConnection.getMessageService().getMessageCollector());
	addSimulationJobStatusListener(localVCellConnection.getMessageService().getMessageCollector());
	localVCellConnection.getMessageService().getMessageDispatcher().removeWorkerEventListener(this);
	localVCellConnection.getMessageService().getMessageDispatcher().addWorkerEventListener(this);
	for (int i = 0; i < simulation.getScanCount(); i++){
		SimulationJob simJob = new SimulationJob(simulation,i);
		VCSimulationIdentifier vcSimID = simJob.getVCDataIdentifier().getVcSimID();
		try {
			SolverProxy solverProxy = getSolverProxy(user,simJob,userSessionLog);
				
			SimulationJobStatus oldJobStatus = adminDbServer.getSimulationJobStatus(simulation.getKey(),i);	
			SimulationJobStatus newJobStatus = updateDispatchedJobStatus(oldJobStatus, vcSimID, i);
			
			if (newJobStatus != null) {
				SimulationJobStatusEvent event = new SimulationJobStatusEvent(this, simulation.getSimulationID(), newJobStatus, null, null);
				fireSimulationJobStatusEvent(event);
			}

			solverProxy.startSimulationJob(); // can only start after updating the database is done

		} catch (Exception ex) {
			handleException(vcSimID,i,ex);
		}	
	}
}

/**
 * This method was created by a SmartGuide.
 * @exception java.rmi.RemoteException The exception description.
 */
public void stopSimulation(User user, Simulation simulation) {	
	LocalVCellConnection localVCellConnection = (LocalVCellConnection)getLocalVCellServer().getVCellConnection(user);
	removeSimulationJobStatusListener(localVCellConnection.getMessageService().getMessageCollector());
	addSimulationJobStatusListener(localVCellConnection.getMessageService().getMessageCollector());
	for (int i = 0; i < simulation.getScanCount(); i++){
		VCSimulationIdentifier vcSimID = new VCSimulationIdentifier(simulation.getKey(),simulation.getVersion().getOwner());
		try {
			if (!vcSimID.getOwner().equals(user)){
				throw new PermissionException("insufficient privilege: stopSimulation()");
			}
			SimulationJobStatus oldJobStatus = adminDbServer.getSimulationJobStatus(vcSimID.getSimulationKey(), i);	
			SimulationJobStatus newJobStatus = updateStoppedJobStatus(oldJobStatus, vcSimID, i);
			if (newJobStatus != null) {
				SimulationJobStatusEvent event = new SimulationJobStatusEvent(this, simulation.getSimulationID(), newJobStatus, null, null);
				fireSimulationJobStatusEvent(event);
			}
				
			SolverProxy solverProxy = (SolverProxy)solverProxyHash.get(SimulationJob.createSimulationJobID(Simulation.createSimulationID(vcSimID.getSimulationKey()), i));
			if (solverProxy != null){
				solverProxy.stopSimulationJob();
			}
		} catch (Exception ex) {
			handleException(vcSimID,i,ex);
		}
	}
}


/**
 * This method was created by a SmartGuide.
 * @exception java.rmi.RemoteException The exception description.
 */
public void stopSimulation(User user, VCSimulationIdentifier vcSimID, int jobIndex) {	
	LocalVCellConnection localVCellConnection = (LocalVCellConnection)getLocalVCellServer().getVCellConnection(user);
	removeSimulationJobStatusListener(localVCellConnection.getMessageService().getMessageCollector());
	addSimulationJobStatusListener(localVCellConnection.getMessageService().getMessageCollector());
	try {
		if (!vcSimID.getOwner().equals(user)){
			throw new PermissionException("insufficient privilege: stopSimulation()");
		}
		SimulationJobStatus oldJobStatus = adminDbServer.getSimulationJobStatus(vcSimID.getSimulationKey(),jobIndex);	
		SimulationJobStatus newJobStatus = updateStoppedJobStatus(oldJobStatus, vcSimID, jobIndex);
		if (newJobStatus != null) {
			SimulationJobStatusEvent event = new SimulationJobStatusEvent(this, Simulation.createSimulationID(vcSimID.getSimulationKey()), newJobStatus, null, null);
			fireSimulationJobStatusEvent(event);
		}
			
		SolverProxy solverProxy = (SolverProxy)solverProxyHash.get(SimulationJob.createSimulationJobID(Simulation.createSimulationID(vcSimID.getSimulationKey()), jobIndex));
		if (solverProxy != null){
			solverProxy.stopSimulationJob();
		}
	} catch (Exception ex) {
		handleException(vcSimID,jobIndex,ex);
	}
}


/**
 * Insert the method's description here.
 * Creation date: (11/5/2003 11:50:34 AM)
 * @param sim cbit.vcell.solver.Simulation
 * @param jobStatus cbit.vcell.messaging.db.SimulationJobStatus
 */
private SimulationJobStatus updateCompletedJobStatus(SimulationJobStatus oldJobStatus, VCSimulationIdentifier vcSimulationIdentifier, int jobIndex) throws DataAccessException, RemoteException {
	SolverProxy solverProxy = (SolverProxy)solverProxyHash.get(SimulationJob.createSimulationJobID(Simulation.createSimulationID(vcSimulationIdentifier.getSimulationKey()), jobIndex));
	if (solverProxy == null) {
		return null;
	}

	synchronized (solverProxy) {
		String host = (solverProxy != null) ? solverProxy.getHost() : null;
		
		return dispatcherDbManager.updateEndStatus(oldJobStatus, adminDbServer, vcSimulationIdentifier, jobIndex, host, SimulationJobStatus.SCHEDULERSTATUS_COMPLETED, null);		
	}
}


/**
 * Insert the method's description here.
 * Creation date: (5/28/2003 3:39:37 PM)
 * @param simKey cbit.sql.KeyValue
 */
private SimulationJobStatus updateDispatchedJobStatus(SimulationJobStatus oldJobStatus, VCSimulationIdentifier vcSimulationIdentifier, int jobIndex) throws RemoteException, DataAccessException, UpdateSynchronizationException {
	SolverProxy solverProxy = (SolverProxy)solverProxyHash.get(SimulationJob.createSimulationJobID(Simulation.createSimulationID(vcSimulationIdentifier.getSimulationKey()), jobIndex));
	if (solverProxy == null) {
		return null;
	}

	synchronized (solverProxy) {	
		String host = (solverProxy != null) ? solverProxy.getHost() : null;
		
		return dispatcherDbManager.updateDispatchedStatus(oldJobStatus, adminDbServer, host, vcSimulationIdentifier, jobIndex, null);
	}
}


/**
 * Insert the method's description here.
 * Creation date: (11/5/2003 11:50:34 AM)
 * @param sim cbit.vcell.solver.Simulation
 * @param jobStatus cbit.vcell.messaging.db.SimulationJobStatus
 */
private SimulationJobStatus updateFailedJobStatus(SimulationJobStatus oldJobStatus, VCSimulationIdentifier vcSimulationIdentifier, int jobIndex, String solverMsg) throws DataAccessException, RemoteException {
	SolverProxy solverProxy = (SolverProxy)solverProxyHash.get(SimulationJob.createSimulationJobID(Simulation.createSimulationID(vcSimulationIdentifier.getSimulationKey()), jobIndex));
	if (solverProxy == null) {
		return null;
	}
	
	synchronized (solverProxy) {		
		String host = (solverProxy != null) ? solverProxy.getHost() : null;
		
		return dispatcherDbManager.updateEndStatus(oldJobStatus, adminDbServer, vcSimulationIdentifier, jobIndex, host, SimulationJobStatus.SCHEDULERSTATUS_FAILED, solverMsg);
	}
}


/**
 * Insert the method's description here.
 * Creation date: (11/5/2003 11:50:34 AM)
 * @param sim cbit.vcell.solver.Simulation
 * @param jobStatus cbit.vcell.messaging.db.SimulationJobStatus
 */
private SimulationJobStatus updateRunningJobStatus(SimulationJobStatus oldJobStatus, VCSimulationIdentifier vcSimulationIdentifier, int jobIndex, boolean hasData, String solverMsg) throws DataAccessException, RemoteException {
	SolverProxy solverProxy = (SolverProxy)solverProxyHash.get(SimulationJob.createSimulationJobID(Simulation.createSimulationID(vcSimulationIdentifier.getSimulationKey()), jobIndex));
	if (solverProxy == null) {
		return null;
	}

	synchronized (solverProxy) {
		String host = (solverProxy != null) ? solverProxy.getHost() : null;
		
		return dispatcherDbManager.updateRunningStatus(oldJobStatus, adminDbServer, host, vcSimulationIdentifier, jobIndex, hasData, solverMsg);
	}
}


/**
 * Insert the method's description here.
 * Creation date: (11/5/2003 11:50:34 AM)
 * @param sim cbit.vcell.solver.Simulation
 * @param jobStatus cbit.vcell.messaging.db.SimulationJobStatus
 */
private SimulationJobStatus updateStoppedJobStatus(SimulationJobStatus oldJobStatus, VCSimulationIdentifier vcSimID, int jobIndex) throws DataAccessException, RemoteException {	
	SolverProxy solverProxy = (SolverProxy)solverProxyHash.get(SimulationJob.createSimulationJobID(Simulation.createSimulationID(vcSimID.getSimulationKey()), jobIndex));
	if (solverProxy != null) {
		synchronized (solverProxy) {
			return dispatcherDbManager.updateEndStatus(oldJobStatus, adminDbServer, vcSimID, jobIndex, null, SimulationJobStatus.SCHEDULERSTATUS_STOPPED, null);			
		}
	} else {
		return dispatcherDbManager.updateEndStatus(oldJobStatus, adminDbServer, vcSimID, jobIndex, null, SimulationJobStatus.SCHEDULERSTATUS_STOPPED, null);
	}	
}
}