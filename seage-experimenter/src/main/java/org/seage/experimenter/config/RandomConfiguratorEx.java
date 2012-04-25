package org.seage.experimenter.config;

import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;

public class RandomConfiguratorEx extends Configurator {
	
	// Statistical data on previous run
	private DataNode _statistics;
	
	public RandomConfiguratorEx(DataNode statistics) {
		super();
		statistics = _statistics;
	}

	@Override
	public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo,
			String algID, int numConfigs) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
