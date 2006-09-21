package cbit.vcell.messaging.server;
import cbit.util.DataAccessException;
import cbit.vcell.server.AdminDatabaseServerXA;
import cbit.vcell.server.AdminDatabaseServer;
import cbit.vcell.simulation.SimulationInfo;
import cbit.vcell.simulation.VCSimulationIdentifier;
import cbit.vcell.solvers.SimulationJobStatus;
import cbit.vcell.messaging.db.UpdateSynchronizationException;

/**
 * Insert the type's description here.
 * Creation date: (2/23/2004 10:42:53 AM)
 * @author: Fei Gao
 */
public interface MessagingDispatcherDbManager extends DispatcherDbManager {
	SimulationJobStatus updateDispatchedStatus(SimulationJobStatus oldJobStatus, AdminDatabaseServerXA adminDbXA, java.sql.Connection con, String computeHost, VCSimulationIdentifier vcSimID, int jobIndex, String startMsg) throws DataAccessException, UpdateSynchronizationException;


SimulationJobStatus updateEndStatus(SimulationJobStatus oldJobStatus, AdminDatabaseServerXA adminDbXA, java.sql.Connection con, VCSimulationIdentifier vcSimID, int jobIndex, String hostName, int status, String solverMsg) throws DataAccessException, UpdateSynchronizationException;


	void updateLatestUpdateDate(SimulationJobStatus oldJobStatus, AdminDatabaseServerXA adminDbXA, java.sql.Connection con, VCSimulationIdentifier vcSimID, int jobIndex) throws DataAccessException, UpdateSynchronizationException;


SimulationJobStatus updateQueueStatus(SimulationJobStatus oldJobStatus, AdminDatabaseServerXA adminDb, java.sql.Connection con, VCSimulationIdentifier vcSimID, int jobIndex, int queueID, int taskID, boolean firstSubmit) throws DataAccessException, UpdateSynchronizationException;


	SimulationJobStatus updateRunningStatus(SimulationJobStatus oldJobStatus, AdminDatabaseServerXA adminDbXA, java.sql.Connection con, String hostName, VCSimulationIdentifier vcSimID, int jobIndex, boolean hasData, String solverMsg)	throws DataAccessException, UpdateSynchronizationException;
}