package com.evolutionandgames.repeatedgames.evolution;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.evolutionandgames.agentbased.AgentBasedPayoffCalculator;
import com.evolutionandgames.agentbased.AgentBasedPopulation;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulation;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulationImpl;

public class RepeatedGamePayoffCalculator implements
		AgentBasedPayoffCalculator {

	private RepeatedGame repeatedGame;
	private double mistakeProbability;
	private boolean keepExtraMeasures;
	
	
	
	SummaryStatistics leniencyMeasure = new SummaryStatistics();
	SummaryStatistics forgivenessMeasure = new SummaryStatistics();
	SummaryStatistics totalCoopMeasure = new SummaryStatistics();
	
	
	public void calculatePayoffs(AgentBasedPopulation population) {
		if (population.getSize()%2 !=0) throw new IllegalArgumentException("This class asumes that the population size is even");
		for (int i = 0; i < population.getSize()-1; i=i+2) {
			//i is focal, we set the fitness of i and i+1
			RepeatedStrategy focal = (RepeatedStrategy)((ExtensivePopulationImpl) population).getAgent(i);
			RepeatedStrategy other = (RepeatedStrategy)((ExtensivePopulationImpl) population).getAgent(i+1);
			double[] result = repeatedGame.playOnce(focal, other, mistakeProbability);
			((ExtensivePopulation) population).setPayoffOfAgent(i, result[0]);
			((ExtensivePopulation) population).setPayoffOfAgent(i+1, result[1]);
			if (keepExtraMeasures) {
				leniencyMeasure.clear();
				forgivenessMeasure.clear();
				totalCoopMeasure.clear();
				String encodedEncounter = HistoryAnalyzer.encodeHistories(repeatedGame.getActionsPlayerOne(), repeatedGame.getActionsPlayerTwo());
				Double forgiveness = HistoryAnalyzer.computeForgivenessScore(encodedEncounter);
				Double leniency = HistoryAnalyzer.computeLeniencyScore(encodedEncounter);
				if (forgiveness != null) {
					forgivenessMeasure.addValue(forgiveness);
				}
				if (leniency != null) {
					leniencyMeasure.addValue(leniency);
				}
				totalCoopMeasure.addValue(HistoryAnalyzer.totalCooperation(encodedEncounter));
				SummaryStatistics[] extraInfo = {leniencyMeasure, forgivenessMeasure, totalCoopMeasure};
				((ExtensivePopulationImpl)population).setExtraInfo(extraInfo);
			}
		}
	}

	public RepeatedGamePayoffCalculator(RepeatedGame repeatedGame, double mistakeProbability, boolean keepExtraMeasures) {
		super();
		this.repeatedGame = repeatedGame;
		this.mistakeProbability = mistakeProbability;
		this.keepExtraMeasures = keepExtraMeasures;
	}
}
