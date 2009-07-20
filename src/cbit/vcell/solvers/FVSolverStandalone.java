package cbit.vcell.solvers;
import cbit.vcell.solver.*;
import java.io.*;

import org.vcell.util.PropertyLoader;
import org.vcell.util.SessionLog;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/

/**
 * This interface was generated by a SmartGuide.
 * 
 */
public class FVSolverStandalone extends FVSolver implements Solver {
	private boolean bMessaging = true; 
/**
 * This method was created by a SmartGuide.
 * @param mathDesc cbit.vcell.math.MathDescription
 * @param platform cbit.vcell.solvers.Platform
 * @param directory java.lang.String
 * @param simID java.lang.String
 * @param clientProxy cbit.vcell.solvers.ClientProxy
 */
public FVSolverStandalone (SimulationJob argSimulationJob, File dir, SessionLog sessionLog) throws SolverException {
	this(argSimulationJob, dir, sessionLog, true);
}
	
public FVSolverStandalone (SimulationJob argSimulationJob, File dir, SessionLog sessionLog, boolean arg_bMessaging) throws SolverException {
	super(argSimulationJob, dir, sessionLog);
	bMessaging = arg_bMessaging;
}

/**
 * This method was created by a SmartGuide.
 */
protected void initialize() throws SolverException {
	try {
		if (getSimulation().getSolverTaskDescription().getSolverDescription().equals(SolverDescription.SundialsPDE)) {
			if (getSimulation().getMathDescription().hasFastSystems()) {
				throw new SolverException(SolverDescription.SundialsPDE.getDisplayLabel() + " does not support models containing fast system. Please change the solver.");
			}
			if (getSimulation().getMathDescription().hasPeriodicBoundaryCondition()) {
				throw new SolverException(SolverDescription.SundialsPDE.getDisplayLabel() + " does not support models containing Periodic Boundary Condition. Please change the solver.");
			}
		}
		initStep1();	
	
		setSolverStatus(new SolverStatus(SolverStatus.SOLVER_RUNNING, SimulationMessage.MESSAGE_SOLVER_RUNNING_INIT));
		fireSolverStarting(SimulationMessage.MESSAGE_SOLVEREVENT_STARTING_INIT);
	
		setSolverStatus(new SolverStatus(SolverStatus.SOLVER_RUNNING, SimulationMessage.MESSAGE_SOLVER_RUNNING_INPUT_FILE));
			
		File fvinputFile = new File(getSaveDirectory(), cppCoderVCell.getBaseFilename()+".fvinput");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(fvinputFile));
			new FiniteVolumeFileWriter(pw, getSimulationJob(), getResampledGeometry(), getSaveDirectory(), bMessaging).write();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	
		String executableName = PropertyLoader.getRequiredProperty(PropertyLoader.finiteVolumeExecutableProperty);
		setMathExecutable(new MathExecutable(new String[] {executableName, fvinputFile.getAbsolutePath()}));
	} catch (Exception ex) {
		ex.printStackTrace(System.out);
		throw new SolverException(ex.getMessage());
	}
}
}