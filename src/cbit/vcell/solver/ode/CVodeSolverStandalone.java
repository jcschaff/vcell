package cbit.vcell.solver.ode;

import java.io.PrintWriter;

import org.vcell.util.PropertyLoader;

import cbit.vcell.solver.SimulationMessage;
import cbit.vcell.solver.SolverStatus;
import cbit.vcell.solver.SolverException;
/**
 * Insert the type's description here.
 * Creation date: (10/23/2004 8:07:49 AM)
 * @author: Jim Schaff
 */
public class CVodeSolverStandalone extends SundialsSolver {
/**
 * IDASolverStandalone constructor comment.
 * @param simulation cbit.vcell.solver.Simulation
 * @param directory java.io.File
 * @param sessionLog cbit.vcell.server.SessionLog
 * @exception cbit.vcell.solver.SolverException The exception description.
 */
public CVodeSolverStandalone(cbit.vcell.solver.SimulationJob simulationJob, java.io.File directory, org.vcell.util.SessionLog sessionLog) throws cbit.vcell.solver.SolverException {
	super(simulationJob, directory, sessionLog);
}
/**
 *  This method takes the place of the old runUnsteady()...
 */
protected void initialize() throws cbit.vcell.solver.SolverException {	
	fireSolverStarting(SimulationMessage.MESSAGE_SOLVEREVENT_STARTING_INIT);
	super.initialize();

	String inputFilename = getBaseName() + CVODEINPUT_DATA_EXTENSION;
	String outputFilename = getBaseName() + IDA_DATA_EXTENSION;

	setSolverStatus(new SolverStatus(SolverStatus.SOLVER_RUNNING, SimulationMessage.MESSAGE_SOLVER_RUNNING_INPUT_FILE));
	fireSolverStarting(SimulationMessage.MESSAGE_SOLVEREVENT_STARTING_INPUT_FILE);

	PrintWriter pw = null;
	try {
		pw = new java.io.PrintWriter(inputFilename);
		CVodeFileWriter cvodeFileWriter = new CVodeFileWriter(pw, getSimulation(), getJobIndex(), true);
		cvodeFileWriter.write();
	} catch (Exception e) {
		setSolverStatus(new SolverStatus(SolverStatus.SOLVER_ABORTED, SimulationMessage.solverAborted("CVODE solver could not generate input file: " + e.getMessage())));
		e.printStackTrace(System.out);
		throw new SolverException("CVODE solver could not generate input file: " + e.getMessage());
	} finally {
		if (pw != null) {
			pw.close();
		}
	}

	setSolverStatus(new SolverStatus(SolverStatus.SOLVER_RUNNING,SimulationMessage.MESSAGE_SOLVER_RUNNING_START));	
	
	String executableName = PropertyLoader.getRequiredProperty(PropertyLoader.sundialsSolverExecutableProperty);
	setMathExecutable(new cbit.vcell.solvers.MathExecutable(new String[] {executableName, inputFilename, outputFilename}));
}
}
