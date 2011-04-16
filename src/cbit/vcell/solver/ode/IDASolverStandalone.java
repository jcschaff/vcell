package cbit.vcell.solver.ode;

import java.io.File;
import java.io.PrintWriter;

import org.vcell.util.PropertyLoader;
import org.vcell.util.SessionLog;

import cbit.vcell.solver.SimulationJob;
import cbit.vcell.solver.SimulationMessage;
import cbit.vcell.solver.SolverException;
import cbit.vcell.solver.SolverStatus;
import cbit.vcell.solvers.MathExecutable;
/**
 * Insert the type's description here.
 * Creation date: (10/23/2004 8:07:49 AM)
 * @author: Jim Schaff
 */
public class IDASolverStandalone extends SundialsSolver {
/**
 * IDASolverStandalone constructor comment.
 * @param simulation cbit.vcell.solver.Simulation
 * @param directory java.io.File
 * @param sessionLog cbit.vcell.server.SessionLog
 * @exception cbit.vcell.solver.SolverException The exception description.
 */
public IDASolverStandalone(SimulationJob simulationJob, File directory, SessionLog sessionLog, boolean bMessaging) throws SolverException {
	super(simulationJob, directory, sessionLog, bMessaging);
}
/**
 *  This method takes the place of the old runUnsteady()...
 */
protected void initialize() throws SolverException {

	fireSolverStarting(SimulationMessage.MESSAGE_SOLVEREVENT_STARTING_INIT);
	super.initialize();
	
	String inputFilename = getBaseName() + IDAINPUT_DATA_EXTENSION;
	String outputFilename = getBaseName() + IDA_DATA_EXTENSION;
	setSolverStatus(new SolverStatus(SolverStatus.SOLVER_RUNNING, SimulationMessage.MESSAGE_SOLVER_RUNNING_INPUT_FILE));
	fireSolverStarting(SimulationMessage.MESSAGE_SOLVEREVENT_STARTING_INPUT_FILE);

	PrintWriter pw = null;
	try {
		pw = new PrintWriter(inputFilename);
		IDAFileWriter idaFileWriter = new IDAFileWriter(pw, simulationJob, bUseMessaging);
		idaFileWriter.write();
	} catch (Exception e) {
		setSolverStatus(new SolverStatus(SolverStatus.SOLVER_ABORTED, SimulationMessage.solverAborted("Could not generate input file: " + e.getMessage())));
		e.printStackTrace(System.out);
		throw new SolverException("IDA solver could not generate input file: " + e.getMessage());
	} finally {
		if (pw != null) {
			pw.close();
		}
	}

	setSolverStatus(new SolverStatus(SolverStatus.SOLVER_RUNNING,SimulationMessage.MESSAGE_SOLVER_RUNNING_START));	
	
	String executableName = PropertyLoader.getRequiredProperty(PropertyLoader.sundialsSolverExecutableProperty);
	setMathExecutable(new MathExecutable(new String[] {executableName, inputFilename, outputFilename}));
}
}
