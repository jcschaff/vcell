package cbit.vcell.document;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;

import cbit.vcell.math.OutputFunctionContext;
import cbit.vcell.solver.Simulation;
/**
 * Insert the type's description here.
 * Creation date: (6/4/2004 1:56:12 AM)
 * @author: Ion Moraru
 */
public interface SimulationOwner {

	Simulation addNewSimulation() throws PropertyVetoException;

	void addPropertyChangeListener(PropertyChangeListener listener);

	Simulation copySimulation(Simulation simulation) throws PropertyVetoException;

	Simulation[] getSimulations();

	void removePropertyChangeListener(PropertyChangeListener listener);

	void removeSimulation(Simulation simulation) throws PropertyVetoException;
	
	OutputFunctionContext getOutputFunctionContext();
}

