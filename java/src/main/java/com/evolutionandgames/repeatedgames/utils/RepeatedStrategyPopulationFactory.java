package com.evolutionandgames.repeatedgames.utils;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentBasedPopulation;
import com.evolutionandgames.agentbased.AgentBasedPopulationFactory;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulationImpl;

public class RepeatedStrategyPopulationFactory implements AgentBasedPopulationFactory {

	int size;
	Agent strategy;
	
	
	
	public AgentBasedPopulation createPopulation() {
		Agent[] agentArray = new Agent[size];
		for (int i = 0; i < agentArray.length; i++) {
			agentArray[i] = strategy;
		}
		return new ExtensivePopulationImpl(agentArray);
	}



	public RepeatedStrategyPopulationFactory(int size, Agent strategy) {
		super();
		this.size = size;
		this.strategy = strategy;
	}

}
