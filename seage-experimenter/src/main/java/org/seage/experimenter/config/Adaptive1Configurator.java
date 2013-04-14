package org.seage.experimenter.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;

import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.example.set.SimpleExampleSet;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.UserError;
import com.rapidminer.repository.RepositoryException;
import com.rapidminer.repository.RepositoryManager;
import com.rapidminer.repository.local.LocalRepository;

public class Adaptive1Configurator extends Configurator
{
	private final String RAPIDMINER_LOCAL_REPOSITORY_NAME = "seage";
	private final String RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH = "repository";
	
	@Override
	public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String algID, DataNode instanceInfo, int numConfigs) throws Exception
	{
//		try {
//			
//			//String rapidMinerHome = "/home/rick/Temp/rm-test";
//			//System.setProperty("rapidminer.home", rapidMinerHome);
//			//System.setProperty(RapidMiner..PROPERTY_CONNECTIONS_FILE_XML, "false");
//			System.setProperty(RapidMiner.PROPERTY_RAPIDMINER_INIT_PLUGINS, "false");
//			//System.setProperty(RapidMiner, "false");
//			System.setProperty("rapidminer.home", ".");
//			RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
//			RepositoryManager.getInstance(null).addRepository(new LocalRepository( RAPIDMINER_LOCAL_REPOSITORY_NAME , new File( RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH )));
//			RapidMiner.init();
//			
//			RepositoryManager rm = RepositoryManager.getInstance(null);
//			try
//	        {
//	            rm.getRepository(RAPIDMINER_LOCAL_REPOSITORY_NAME);
//	        }
//	        catch(RepositoryException re)
//	        {
//	            rm.addRepository(new LocalRepository( RAPIDMINER_LOCAL_REPOSITORY_NAME , new File( RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH )));
//	            rm.save();
//	        }
//			
//
//			Process process = new Process(new File(RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH+"/processes/base/BestAlgParamsPerAlgorithm-AttsRemoved.rmp"));			
//			try{
//				IOContainer res = process.run();
//			}
//			catch(UserError err){}
////			IOContainer op= process.getRootOperator().getResults();
////			SimpleExampleSet ses = (SimpleExampleSet)op.getElementAt(0);
////			ses.getExample(0);
//			//op.getElementAt(0)
//			int a = 0;
//			//Process process2 = new Process(new File());
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		List<ProblemConfig> results = new ArrayList<ProblemConfig>();
		return results.toArray(new ProblemConfig[0]);
	}

	@Override
	public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String algID, int numConfigs) throws Exception
	{		
		return null;
	}
}
