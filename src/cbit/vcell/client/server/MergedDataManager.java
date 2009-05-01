package cbit.vcell.client.server;
import org.vcell.util.VCDataIdentifier;

import cbit.vcell.client.data.NewClientPDEDataContext;
import cbit.vcell.simdata.MergedDataInfo;
import cbit.vcell.solver.DataProcessingOutput;
/**
 * Insert the type's description here.
 * Creation date: (11/30/2005 5:37:29 PM)
 * @author: Anuradha Lakshminarayana
 */


public class MergedDataManager implements cbit.vcell.desktop.controls.DataManager {
	private VCDataManager vcDataManager = null;
	private VCDataIdentifier vcDataIdentifier = null;
	private NewClientPDEDataContext newClientPDEDataContext = null;

/**
 * MergedDataManager constructor comment.
 */
public MergedDataManager(VCDataManager argVCDataManager, org.vcell.util.VCDataIdentifier argVCDataId) {
	super();
	setVcDataManager(argVCDataManager);
	setVcDataIdentifier(argVCDataId);
}


/**
 * adds an array of named <code>Function</code>s to the list of variables that are availlable for this Simulation.
 * 
 * @param functions represent named expressions that are to be bound to dataset and whose names are added to variable list.
 * 
 * @throws org.vcell.util.DataAccessException if Functions cannot be bound to this dataset or SimulationInfo not found.
 */
public void addFunctions(cbit.vcell.math.AnnotatedFunction[] functions,boolean[] bReplaceArr) throws org.vcell.util.DataAccessException {
	getVcDataManager().addFunctions(getVcDataIdentifier(), functions,bReplaceArr);
}


/**
 * retrieves a list of data names (state variables and functions) defined for this Simulation.
 * 
 * @param simulationInfo simulation database reference
 * 
 * @returns array of availlable data names.
 * 
 * @throws org.vcell.util.DataAccessException if SimulationInfo not found.
 */
public cbit.vcell.simdata.DataIdentifier[] getDataIdentifiers() throws org.vcell.util.DataAccessException {
	return getVcDataManager().getDataIdentifiers(getVcDataIdentifier());
}


/**
 * gets all times at which simulation result data is availlable for this Simulation.
 * 
 * @returns double array of times of availlable data, or null if no data.
 * 
 * @throws org.vcell.util.DataAccessException if SimulationInfo not found.
 */
public double[] getDataSetTimes() throws org.vcell.util.DataAccessException {
	return getVcDataManager().getDataSetTimes(getVcDataIdentifier());
}


/**
 * Insert the method's description here.
 * Creation date: (6/11/2004 3:53:33 PM)
 * @return cbit.vcell.server.VCDataIdentifier
 */
private org.vcell.util.VCDataIdentifier getFirstVcDataIdentifier() {
	MergedDataInfo mergedDataInfo = null;
	if (getVcDataIdentifier() instanceof cbit.vcell.simdata.MergedDataInfo) {
		mergedDataInfo = (cbit.vcell.simdata.MergedDataInfo)getVcDataIdentifier();
	}

	return mergedDataInfo.getDataIDs()[0];
}


/**
 * gets list of named Functions defined for the resultSet for this Simulation.
 * 
 * @returns array of functions, or null if no functions.
 * 
 * @throws org.vcell.util.DataAccessException if SimulationInfo not found.
 * 
 * @see Function
 */
public cbit.vcell.math.AnnotatedFunction[] getFunctions() throws org.vcell.util.DataAccessException {
	return getVcDataManager().getFunctions(getVcDataIdentifier());
}


/**
 * tests if resultSet contains ODE data for the specified simulation.
 * 
 * @returns <i>true</i> if results are of type ODE, <i>false</i> otherwise.
 * 
 * @throws org.vcell.util.DataAccessException if SimulationInfo not found.
 * 
 * @see Function
 */
public boolean getIsODEData() throws org.vcell.util.DataAccessException {
	return getVcDataManager().getIsODEData(getFirstVcDataIdentifier());
}


/**
 * retrieves a line scan (data sampled along a line in space) for the specified simulation.
 * 
 * @param variable name of variable to be sampled
 * @param time simulation time which is to be sampled.
 * @param begin i,j,k of start of line.
 * @param end i,j,k coordinate of end of line.
 * 
 * @returns annotated array of 'concentration vs. distance' in a plot ready format.
 * 
 * @throws org.vcell.util.DataAccessException if SimulationInfo not found.
 * 
 * @see PlotData
 */
public cbit.plot.PlotData getLineScan(String variable, double time, org.vcell.util.CoordinateIndex begin, org.vcell.util.CoordinateIndex end) throws org.vcell.util.DataAccessException {
	return getVcDataManager().getLineScan(getVcDataIdentifier(), variable, time, begin, end);
}


/**
 * retrieves a line scan (data sampled along a curve in space) for the specified simulation.
 * 
 * @param variable name of variable to be sampled
 * @param time simulation time which is to be sampled.
 * @param spatialSelection spatial curve.
 * 
 * @returns annotated array of 'concentration vs. distance' in a plot ready format.
 * 
 * @throws org.vcell.util.DataAccessException if SimulationInfo not found.
 * 
 * @see PlotData
 */
public cbit.plot.PlotData getLineScan(String variable, double time, cbit.vcell.simdata.gui.SpatialSelection spatialSelection) throws org.vcell.util.DataAccessException {
	return getVcDataManager().getLineScan(getVcDataIdentifier(), variable, time, spatialSelection);
}


/**
 * retrieves the Mesh object for this Simulation.
 * 
 * @returns mesh associated with this data (allows spatial interpretation of indexed data).
 * 
 * @throws org.vcell.util.DataAccessException if SimulationInfo not found.
 * 
 * @see CartesianMesh
 */
public cbit.vcell.solvers.CartesianMesh getMesh() throws org.vcell.util.DataAccessException {
	return getVcDataManager().getMesh(getFirstVcDataIdentifier());
}


/**
 * retrieves the non-spatial (ODE) results for this Simulation.  This is assumed not to change over the life
 * of the simulation
 * 
 * @returns non-spatial (ODE) data.
 * 
 * @throws org.vcell.util.DataAccessException if SimulationInfo not found.
 */
public cbit.vcell.solver.ode.ODESolverResultSet getODESolverResultSet() throws org.vcell.util.DataAccessException {
	return getVcDataManager().getODEData(getVcDataIdentifier());
}


/**
 * retrieves the particle data for this Simulation.
 * 
 * @returns particle data for this result set.
 * 
 * @throws org.vcell.util.DataAccessException if SimulationInfo not found, or if no particle data.
 * 
 * @see ParticleDataBlock
 */
public cbit.vcell.simdata.ParticleDataBlock getParticleDataBlock(double time) throws org.vcell.util.DataAccessException {
	return getVcDataManager().getParticleDataBlock(getVcDataIdentifier(), time);
}

public DataProcessingOutput getDataProcessingOutput() throws org.vcell.util.DataAccessException {
	return getVcDataManager().getDataProcessingOutput(getVcDataIdentifier());
}


/**
 * determines if the result set for this Simulation contains particle data.
 * 
 * @returns <i>true</i> if there is particle data availlable.
 * 
 * @throws org.vcell.util.DataAccessException if SimulationInfo not found.
 */
public boolean getParticleDataExists() throws org.vcell.util.DataAccessException {
	return getVcDataManager().getParticleDataExists(getVcDataIdentifier());
}


/**
 * Insert the method's description here.
 * Creation date: (11/30/2005 5:37:29 PM)
 * @return cbit.vcell.simdata.PDEDataContext
 */
public cbit.vcell.simdata.PDEDataContext getPDEDataContext() {
	boolean isODEData = true;
	try {
		isODEData = getIsODEData();
	} catch (org.vcell.util.DataAccessException e) {
		e.printStackTrace(System.out);
		return null;
	}
	if (!isODEData) {
		if (newClientPDEDataContext == null) {
			newClientPDEDataContext = new NewClientPDEDataContext(this);
		}
		return newClientPDEDataContext;
	} else {
		throw new RuntimeException("No PDEDataContext for ODE data!");
	}
}


/**
 * retrieves the spatial (PDE) data for this Simulation, Variable, and Time.
 * 
 * @param varName name of dataSet (state variable or function).
 * @param time simulation time of data.
 * 
 * @returns spatial (PDE) data for this result set associated with the specified variable name and time, 
 *          or <i>null</i> if no data availlable.
 * 
 * @throws org.vcell.util.DataAccessException if SimulationInfo not found.
 */
public cbit.vcell.simdata.SimDataBlock getSimDataBlock(String varName, double time) throws org.vcell.util.DataAccessException {
	return getVcDataManager().getSimDataBlock(getVcDataIdentifier(), varName, time);
}


/**
 * retrieves a time series (single point as a function of time) of a specified spatial data set.
 * 
 * @param variable name of variable to be sampled
 * @param index identifies index into data array.
 * 
 * @returns annotated array of 'concentration vs. time' in a plot ready format.
 * 
 * @throws org.vcell.util.DataAccessException if SimulationInfo not found.
 * 
 * @see CartesianMesh for transformation between indices and coordinates.
 */
public org.vcell.util.TimeSeriesJobResults getTimeSeriesValues(org.vcell.util.TimeSeriesJobSpec timeSeriesJobSpec) throws org.vcell.util.DataAccessException {
	if (getIsODEData()) {
		throw new RuntimeException("Not Implemented");
	} else {
		return getVcDataManager().getTimeSeriesValues(getVcDataIdentifier(), timeSeriesJobSpec);
	}
}


/**
 * Insert the method's description here.
 * Creation date: (6/11/2004 3:53:33 PM)
 * @return cbit.vcell.server.VCDataIdentifier
 */
private org.vcell.util.VCDataIdentifier getVcDataIdentifier() {
	return vcDataIdentifier;
}


/**
 * Gets the simulationInfo property (cbit.vcell.solver.SimulationInfo) value.
 * @return The simulationInfo property value.
 */
public org.vcell.util.VCDataIdentifier getVCDataIdentifier() {
	return getVcDataIdentifier();
}


/**
 * Insert the method's description here.
 * Creation date: (6/11/2004 3:53:33 PM)
 * @return cbit.vcell.client.server.VCDataManager
 */
private VCDataManager getVcDataManager() {
	return vcDataManager;
}


/**
 * removes the specified <i>function</i> from this Simulation.
 * 
 * @param function function to be removed.
 * 
 * @throws org.vcell.util.DataAccessException if SimulationInfo not found.
 * @throws org.vcell.util.PermissionException if not the owner of this dataset.
 */
public void removeFunction(cbit.vcell.math.AnnotatedFunction function) throws org.vcell.util.DataAccessException, org.vcell.util.PermissionException {
	getVcDataManager().removeFunction(function, getVcDataIdentifier());
}


/**
 * Insert the method's description here.
 * Creation date: (6/11/2004 3:53:33 PM)
 * @param newVcDataIdentifier cbit.vcell.server.VCDataIdentifier
 */
private void setVcDataIdentifier(org.vcell.util.VCDataIdentifier newVcDataIdentifier) {
	vcDataIdentifier = newVcDataIdentifier;
}


/**
 * Insert the method's description here.
 * Creation date: (6/11/2004 3:53:33 PM)
 * @param newVcDataManager cbit.vcell.client.server.VCDataManager
 */
private void setVcDataManager(VCDataManager newVcDataManager) {
	vcDataManager = newVcDataManager;
}
}