package com.evolutionandgames.repeatedgames.evolution;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.evolutionandgames.agentbased.AgentBasedPayoffCalculator;
import com.evolutionandgames.agentbased.AgentBasedPopulation;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulation;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulationImpl;

public class RepeatedGamePayoffCalculator implements AgentBasedPayoffCalculator {

	private RepeatedGame repeatedGame;
	private double mistakeProbability;
	private boolean keepExtraMeasures;
	private boolean matchingNoise;

	SummaryStatistics leniencyMeasure = new SummaryStatistics();
	SummaryStatistics forgivenessMeasure = new SummaryStatistics();
	SummaryStatistics totalCoopMeasure = new SummaryStatistics();

	public void calculatePayoffs(AgentBasedPopulation population) {
		if (matchingNoise) {
			calculatePayoffsWithMatchingNoise(population);
		} else {
			calculatePayoffsWithoutMatchingNoise(population);
		}
	}

	private void calculatePayoffsWithoutMatchingNoise(
			AgentBasedPopulation population) {
		int popSize = population.getSize();
		for (int i = 0; i < popSize - 1; i = i + 1) {
			for (int j = i + 1; j < population.getSize(); j++) {
				// i is focal, we set the fitness of i and i+1
				RepeatedStrategy focal = (RepeatedStrategy) ((ExtensivePopulationImpl) population)
						.getAgent(i);
				RepeatedStrategy other = (RepeatedStrategy) ((ExtensivePopulationImpl) population)
						.getAgent(j);
				double[] result = repeatedGame.playOnce(focal, other,
						mistakeProbability);
				double focalPrevious = ((ExtensivePopulation) population)
						.getPayoffOfAgent(i);
				double otherPrevious = ((ExtensivePopulation) population)
						.getPayoffOfAgent(j);
				((ExtensivePopulation) population).setPayoffOfAgent(i,
						result[0] + focalPrevious);
				((ExtensivePopulation) population).setPayoffOfAgent(j,
						result[1] + otherPrevious);
				if (keepExtraMeasures) {
					this.registerExtraMeasures(population);
				}

			}

		}
		double scale = (double) popSize -1.0;
		for (int i = 0; i < popSize; i++) {
			double focalPrevious = ((ExtensivePopulation) population)
					.getPayoffOfAgent(i);
			((ExtensivePopulation) population).setPayoffOfAgent(i,
					focalPrevious/scale);
		}

	}

	private void registerExtraMeasures(AgentBasedPopulation population) {
		leniencyMeasure.clear();
		forgivenessMeasure.clear();
		totalCoopMeasure.clear();
		String encodedEncounter = HistoryAnalyzer.encodeHistories(
				repeatedGame.getActionsPlayerOne(),
				repeatedGame.getActionsPlayerTwo());
		Double forgiveness = HistoryAnalyzer
				.computeForgivenessScore(encodedEncounter);
		Double leniency = HistoryAnalyzer
				.computeLeniencyScore(encodedEncounter);
		if (forgiveness != null) {
			forgivenessMeasure.addValue(forgiveness);
		}
		if (leniency != null) {
			leniencyMeasure.addValue(leniency);
		}
		totalCoopMeasure.addValue(HistoryAnalyzer
				.totalCooperation(encodedEncounter));
		SummaryStatistics[] extraInfo = { leniencyMeasure, forgivenessMeasure,
				totalCoopMeasure };
		((ExtensivePopulationImpl) population).setExtraInfo(extraInfo);

	}

	private void calculatePayoffsWithMatchingNoise(
			AgentBasedPopulation population) {
		if (population.getSize() % 2 != 0)
			throw new IllegalArgumentException(
					"This class asumes that the population size is even");
		for (int i = 0; i < population.getSize() - 1; i = i + 2) {
			// i is focal, we set the fitness of i and i+1
			RepeatedStrategy focal = (RepeatedStrategy) ((ExtensivePopulationImpl) population)
					.getAgent(i);
			RepeatedStrategy other = (RepeatedStrategy) ((ExtensivePopulationImpl) population)
					.getAgent(i + 1);
			double[] result = repeatedGame.playOnce(focal, other,
					mistakeProbability);
			((ExtensivePopulation) population).setPayoffOfAgent(i, result[0]);
			((ExtensivePopulation) population).setPayoffOfAgent(i + 1,
					result[1]);
			if (keepExtraMeasures) {
				this.registerExtraMeasures(population);
			}
		}

	}

	public RepeatedGamePayoffCalculator(RepeatedGame repeatedGame,
			double mistakeProbability, boolean keepExtraMeasures) {
		super();
		this.repeatedGame = repeatedGame;
		this.mistakeProbability = mistakeProbability;
		this.keepExtraMeasures = keepExtraMeasures;
		this.matchingNoise = true;
	}

	public RepeatedGamePayoffCalculator(RepeatedGame repeatedGame,
			double mistakeProbability, boolean keepExtraMeasures,
			boolean matchingNoise) {
		super();
		this.repeatedGame = repeatedGame;
		this.mistakeProbability = mistakeProbability;
		this.keepExtraMeasures = keepExtraMeasures;
		this.matchingNoise = matchingNoise;
	}
}
