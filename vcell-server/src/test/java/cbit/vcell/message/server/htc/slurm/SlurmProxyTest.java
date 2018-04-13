package cbit.vcell.message.server.htc.slurm;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.vcell.util.exe.ExecutableException;

import cbit.vcell.message.server.cmd.CommandServiceSshNative;
import cbit.vcell.message.server.htc.HtcJobStatus;
import cbit.vcell.message.server.htc.HtcProxy.JobInfoAndStatus;
import cbit.vcell.message.server.htc.HtcProxy.PartitionStatistics;
import cbit.vcell.mongodb.VCMongoMessage;
import cbit.vcell.resource.PropertyLoader;
import cbit.vcell.server.HtcJobID;

@Ignore
public class SlurmProxyTest {

    @BeforeClass
    public static void setLogger() throws MalformedURLException
    {
//        System.setProperty("log4j.configurationFile","/Users/schaff/Documents/workspace-modular/vcell/docker/trace.log4j2.xml");
    }
    
	@Test
	public void testSLURM() throws IOException, ExecutableException {
		System.setProperty("log4j2.trace","true");
		System.setProperty(PropertyLoader.vcellServerIDProperty, "Test2");
		System.setProperty(PropertyLoader.htcLogDirExternal, "/Volumes/vcell/htclogs");
		VCMongoMessage.enabled=false;
		String partitions[] = new String[] { "vcell", "vcell2" };
		System.setProperty(PropertyLoader.slurm_partition, partitions[0]);
		
		CommandServiceSshNative cmd = null;
		try {
			cmd = new CommandServiceSshNative("vcell-service.cam.uchc.edu", "vcell", new File("/Users/schaff/.ssh/schaff_rsa"));
			SlurmProxy slurmProxy = new SlurmProxy(cmd, "vcell");
			Map<HtcJobID, JobInfoAndStatus> runningJobs = slurmProxy.getRunningJobs();
			for (HtcJobID job : runningJobs.keySet()) {
				HtcJobStatus jobStatus = runningJobs.get(job).status;
				System.out.println("job "+job.toString()+", status="+jobStatus.toString());
			}
			for (String partition : partitions) {
				System.setProperty(PropertyLoader.slurm_partition, partition);
				PartitionStatistics partitionStatistics = slurmProxy.getPartitionStatistics();
				System.out.println("partition statistics for partition "+partition+": "+partitionStatistics);
				System.out.println("number of cpus allocated = "+partitionStatistics.numCpusAllocated);
				System.out.println("load = "+partitionStatistics.load);
				System.out.println("number of cpus total = "+partitionStatistics.numCpusTotal);
			}
		}catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}finally {
			if (cmd != null) {
				cmd.close();
			}
		}
	}

	

}
