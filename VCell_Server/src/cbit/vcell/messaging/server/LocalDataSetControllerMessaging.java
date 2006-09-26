package cbit.vcell.messaging.server;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import cbit.gui.PropertyLoader;
import cbit.plot.PlotData;
import cbit.rmi.event.ExportEvent;
import cbit.util.CoordinateIndex;
import cbit.util.DataAccessException;
import cbit.util.SessionLog;
import cbit.util.User;
import cbit.util.VCDataIdentifier;
import cbit.vcell.export.ExportSpecs;
import cbit.vcell.math.DataIdentifier;
import cbit.vcell.mesh.CartesianMesh;
import cbit.vcell.server.DataSetController;
import cbit.vcell.simdata.ParticleDataBlock;
import cbit.vcell.simdata.SimDataBlock;
import cbit.vcell.simdata.SpatialSelection;

/**
 * This interface was generated by a SmartGuide.
 * 
 */
public class LocalDataSetControllerMessaging extends UnicastRemoteObject implements DataSetController {
    private RpcDataServerProxy dataServerProxy = null;
    private User user = null;
    private SessionLog sessionLog = null;

/**
 * This method was created by a SmartGuide.
 */
public LocalDataSetControllerMessaging (SessionLog sLog, User argUser, cbit.vcell.messaging.JmsClientMessaging clientMessaging) throws RemoteException, DataAccessException {
	super(PropertyLoader.getIntProperty(PropertyLoader.rmiPortDataSetController,0));
	this.sessionLog = sLog;
	this.user = argUser;
	try {
		this.dataServerProxy = new RpcDataServerProxy(user, clientMessaging, sessionLog);
	} catch (javax.jms.JMSException e){
		e.printStackTrace(System.out);
		throw new RuntimeException("JMS exception creating DataServerProxy: "+e.getMessage());
	}
}


/**
 * Insert the method's description here.
 * Creation date: (2/26/2004 1:05:01 PM)
 * @param function cbit.vcell.math.Function
 * @exception DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public void addFunction(VCDataIdentifier vcdataID, cbit.vcell.math.AnnotatedFunction function) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.addFunction(vcdataID=" + vcdataID + ", function=" + function.toString() + ")");
	try {
		dataServerProxy.addFunction(vcdataID,function);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * Insert the method's description here.
 * Creation date: (2/26/2004 1:05:01 PM)
 * @param function cbit.vcell.math.Function[]
 * @exception DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public void addFunctions(VCDataIdentifier vcdID, cbit.vcell.math.AnnotatedFunction[] function) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.addFunctions(vcdID=" + vcdID + ", functions=" + function + ")");
	try {
		dataServerProxy.addFunctions(vcdID, function);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String[]
 */
public DataIdentifier[] getDataIdentifiers(VCDataIdentifier vcdID) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.getDataIdentifiers(vcdID=" + vcdID + ")");
	try {
		return dataServerProxy.getDataIdentifiers(vcdID);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * This method was created by a SmartGuide.
 * @return double[]
 */
public double[] getDataSetTimes(VCDataIdentifier vcdID) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.getDataSetTimes(vcdID=" + vcdID + ")");
	try {
		return dataServerProxy.getDataSetTimes(vcdID);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * Insert the method's description here.
 * Creation date: (2/26/2004 1:05:01 PM)
 * @param function cbit.vcell.math.Function
 * @exception DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public cbit.vcell.math.AnnotatedFunction[] getFunctions(VCDataIdentifier vcdataID) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.getFunctions(vcdataID=" + vcdataID + ")");
	try {
		return dataServerProxy.getFunctions(vcdataID);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * This method was created by a SmartGuide.
 * @return boolean
 */
public boolean getIsODEData(VCDataIdentifier vcdID) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.getIsODEData(vcdID=" + vcdID + ")");
	try {
		return dataServerProxy.getIsODEData(vcdID);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * This method was created by a SmartGuide.
 * @return cbit.plot.PlotData
 * @param varName java.lang.String
 * @param begin cbit.vcell.math.CoordinateIndex
 * @param end cbit.vcell.math.CoordinateIndex
 */
public PlotData getLineScan(VCDataIdentifier vcdID, String varName, double time, CoordinateIndex begin, CoordinateIndex end) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.getLineScan(vcdID=" + vcdID + ", " + varName + ", " + time + ", from " + begin + " to " + end + ")");
	try {
		return dataServerProxy.getLineScan(vcdID, varName, time, begin, end);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * This method was created by a SmartGuide.
 * @return cbit.plot.PlotData
 * @param varName java.lang.String
 * @param spatialSelection cbit.vcell.simdata.gui.SpatialSelection
 */
public PlotData getLineScan(VCDataIdentifier vcdID, String varName, double time, SpatialSelection spatialSelection) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.getLineScan(vcdID=" + vcdID + ", " + varName + ", " + time + ", at " + spatialSelection+")");
	try {
		return dataServerProxy.getLineScan(vcdID, varName, time, spatialSelection);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * This method was created by a SmartGuide.
 * @return int[]
 */
public CartesianMesh getMesh(VCDataIdentifier vcdID) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.getMesh(vcdID=" + vcdID + ")");
	try {
		return dataServerProxy.getMesh(vcdID);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * Insert the method's description here.
 * Creation date: (1/14/00 11:20:51 AM)
 * @return cbit.vcell.export.data.ODESimData
 * @exception DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public cbit.vcell.simdata.ODESimData getODEData(VCDataIdentifier vcdID) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.getODEData(vcdID=" + vcdID + ")");
	try {
		return dataServerProxy.getODEData(vcdID);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * This method was created by a SmartGuide.
 * @return double[]
 * @param varName java.lang.String
 * @param time double
 */
public ParticleDataBlock getParticleDataBlock(VCDataIdentifier vcdID, double time) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.getParticleDataBlock(vcdID=" + vcdID + ",time=" + time + ")");
	try {
		return dataServerProxy.getParticleDataBlock(vcdID,time);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * This method was created by a SmartGuide.
 * @return boolean
 */
public boolean getParticleDataExists(VCDataIdentifier vcdID) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.getParticleDataExists(vcdID=" + vcdID + ")");
	try {
		return dataServerProxy.getParticleDataExists(vcdID);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * This method was created by a SmartGuide.
 * @return double[]
 * @param varName java.lang.String
 * @param time double
 */
public SimDataBlock getSimDataBlock(VCDataIdentifier vcdID, String varName, double time) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.getSimDataBlock(vcdID=" + vcdID + ", varName=" + varName + ", time=" + time + ")");
	try {
		return dataServerProxy.getSimDataBlock(vcdID,varName,time);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * This method was created by a SmartGuide.
 * @return double[]
 * @param varName java.lang.String
 * @param index int
 */
public cbit.util.TimeSeriesJobResults getTimeSeriesValues(VCDataIdentifier vcdID,cbit.util.TimeSeriesJobSpec timeSeriesJobSpec) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.getTimeSeriesValues(vcdID=" + vcdID + ", " + timeSeriesJobSpec + ")");
	try {
		return dataServerProxy.getTimeSeriesValues(vcdID,timeSeriesJobSpec);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * This method was created in VisualAge.
 * @param simInfo cbit.vcell.solver.SimulationInfo
 * @exception DataAccessException The exception description.
 */
public ExportEvent makeRemoteFile(ExportSpecs exportSpecs) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.makeRemoteFile(vcdID=" + exportSpecs.getVCDataIdentifier() + ")");
	try {
		return dataServerProxy.makeRemoteFile(exportSpecs);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * Insert the method's description here.
 * Creation date: (2/26/2004 1:05:01 PM)
 * @param function cbit.vcell.math.Function
 * @exception DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public void removeFunction(VCDataIdentifier vcdataID, cbit.vcell.math.AnnotatedFunction function) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerMessaging.removeFunction(vcdataID=" + vcdataID + ", function=" + function + ")");
	try {
		dataServerProxy.removeFunction(vcdataID, function);
	} catch (DataAccessException e){
		sessionLog.exception(e);
		throw e;
	} catch (Throwable e){
		sessionLog.exception(e);
		throw new RuntimeException(e.getMessage());
	}

}
}