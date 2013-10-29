package com.evolutionandgames.repeatedgames.evolution;

import java.util.ArrayList;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import com.evolutionandgames.jevodyn.utils.Random;

public class RepeatedGame {

	private RealMatrix stageGame;
	private double continuationProbability;

	// keeping the last histories
	private ArrayList<Action> actionsPlayerOne = new ArrayList<Action>();
	private ArrayList<Action> actionsPlayerTwo = new ArrayList<Action>();

	public RepeatedGame(double reward, double sucker, double temptation,
			double punishment, double continuationProbability) {
		super();
		double[][] raw = { { reward, sucker }, { temptation, punishment } };
		this.stageGame = new Array2DRowRealMatrix(raw);
		this.continuationProbability = continuationProbability;
	}

	public double getReward() {
		return this.stageGame.getEntry(0, 0);
	}

	public double getSucker() {
		return this.stageGame.getEntry(0, 1);
	}

	public double getTemptation() {
		return this.stageGame.getEntry(1, 0);
	}

	public double getPunishment() {
		return this.stageGame.getEntry(1, 1);
	}
	
	public double getContinuationProbability() {
		return this.continuationProbability;
	}
	
	

	/**
	 * Payoff with a lot of noise, just one interaction takes place.
	 * 
	 * @param playerOne
	 * @param playerTwo
	 * @return
	 */
	public double[] playOnce(RepeatedStrategy playerOne,
			RepeatedStrategy playerTwo) {
		return playOnce(playerOne, playerTwo, 0.0);
	}

	/**
	 * Payoff with a lot of noise, just one interaction takes place.
	 * 
	 * @param playerOne
	 * @param playerTwo
	 * @param mistakeProbability
	 * @return
	 */
	public double[] playOnce(RepeatedStrategy playerOne,
			RepeatedStrategy playerTwo, double mistakeProbability) {
		// reset the machines
		playerOne.reset();
		playerTwo.reset();

		double payoffOne = 0.0;
		double payoffTwo = 0.0;
		// We start with empty histories
		actionsPlayerOne.clear();
		actionsPlayerTwo.clear();

		// STEP
		// first action happens for sure
		double[] stageResult = stage(playerOne, playerTwo, actionsPlayerOne,
				actionsPlayerTwo, mistakeProbability);
		payoffOne = payoffOne + stageResult[0];
		payoffTwo = payoffTwo + stageResult[1];
		
		int rounds = Random.simulateGeometricDistribution(1.0-continuationProbability);
		for (int i = 0; i < rounds; i++) {
			stageResult = stage(playerOne, playerTwo, actionsPlayerOne,
					actionsPlayerTwo, mistakeProbability);
			payoffOne = payoffOne + stageResult[0];
			payoffTwo = payoffTwo + stageResult[1];
		}

		double[] ans = new double[2];
		ans[0] = (1.0 - continuationProbability) * payoffOne;
		ans[1] = (1.0 - continuationProbability) * payoffTwo;
		return ans;
	}

	/**
	 * Looks up payoff combinations in the matrix of the game. Cooperate has
	 * index 0, Defect index 1.
	 * 
	 * @param player1Action
	 * @param player2Action
	 * @return payoff value
	 */
	private double[] payoff(Action player1Action, Action player2Action) {
		if (player1Action == Action.COOPERATE
				&& player2Action == Action.COOPERATE) {
			double[] ans = { this.stageGame.getEntry(0, 0),
					this.stageGame.getEntry(0, 0) };
			return ans;
		} else if (player1Action == Action.COOPERATE
				&& player2Action == Action.DEFECT) {
			double[] ans = { this.stageGame.getEntry(0, 1),
					this.stageGame.getEntry(1, 0) };
			return ans;
		} else if (player1Action == Action.DEFECT
				&& player2Action == Action.COOPERATE) {
			double[] ans = { this.stageGame.getEntry(1, 0),
					this.stageGame.getEntry(0, 1) };
			return ans;
		} else {
			// DEFECT DEFECT
			double[] ans = { this.stageGame.getEntry(1, 1),
					this.stageGame.getEntry(1, 1) };
			return ans;
		}
	}

	/**
	 * Plays a stage game including a probability of mistakes, updating the
	 * history, the state of the automata and returning the payoffs from the
	 * stage.
	 * 
	 * @param playerOne
	 * @param playerTwo
	 * @param actionsPlayerOne
	 * @param actionsPlayerTwo
	 * @param mistakeProbability
	 * @return
	 */
	private double[] stage(RepeatedStrategy playerOne,
			RepeatedStrategy playerTwo, ArrayList<Action> actionsPlayerOne,
			ArrayList<Action> actionsPlayerTwo, double mistakeProbability) {
		if (playerOne.currentAction() == Action.COOPERATE
				&& playerTwo.currentAction() == Action.COOPERATE) {

			Action actualPlayPlayer1 = Action.COOPERATE;
			Action actualPlayPlayer2 = Action.COOPERATE;

			if (Random.bernoulliTrial(mistakeProbability)) {
				actualPlayPlayer1 = Action.DEFECT;
			}
			if (Random.bernoulliTrial(mistakeProbability)) {
				actualPlayPlayer2 = Action.DEFECT;
			}

			// history tracking
			actionsPlayerOne.add(actualPlayPlayer1);
			actionsPlayerTwo.add(actualPlayPlayer2);
			// move automata
			playerOne.next(actualPlayPlayer1, actualPlayPlayer2);
			playerTwo.next(actualPlayPlayer2, actualPlayPlayer1);
			// payoff tracking
			double[] ans = payoff(actualPlayPlayer1, actualPlayPlayer2);
			return ans;

		} else if (playerOne.currentAction() == Action.DEFECT
				&& playerTwo.currentAction() == Action.DEFECT) {
			Action actualPlayPlayer1 = Action.DEFECT;
			Action actualPlayPlayer2 = Action.DEFECT;

			if (Random.bernoulliTrial(mistakeProbability)) {
				actualPlayPlayer1 = Action.COOPERATE;
			}
			if (Random.bernoulliTrial(mistakeProbability)) {
				actualPlayPlayer2 = Action.COOPERATE;
			}

			// history tracking
			actionsPlayerOne.add(actualPlayPlayer1);
			actionsPlayerTwo.add(actualPlayPlayer2);
			// move automata
			playerOne.next(actualPlayPlayer1, actualPlayPlayer2);
			playerTwo.next(actualPlayPlayer2, actualPlayPlayer1);
			// payoff tracking
			double[] ans = payoff(actualPlayPlayer1, actualPlayPlayer2);
			return ans;

		} else if (playerOne.currentAction() == Action.COOPERATE
				&& playerTwo.currentAction() == Action.DEFECT) {
			Action actualPlayPlayer1 = Action.COOPERATE;
			Action actualPlayPlayer2 = Action.DEFECT;

			if (Random.bernoulliTrial(mistakeProbability)) {
				actualPlayPlayer1 = Action.DEFECT;
			}
			if (Random.bernoulliTrial(mistakeProbability)) {
				actualPlayPlayer2 = Action.COOPERATE;
			}

			// history tracking
			actionsPlayerOne.add(actualPlayPlayer1);
			actionsPlayerTwo.add(actualPlayPlayer2);
			// move automata
			playerOne.next(actualPlayPlayer1, actualPlayPlayer2);
			playerTwo.next(actualPlayPlayer2, actualPlayPlayer1);
			// payoff tracking
			double[] ans = payoff(actualPlayPlayer1, actualPlayPlayer2);
			return ans;

		} else {
			// DEFECT COOPERATE
			Action actualPlayPlayer1 = Action.DEFECT;
			Action actualPlayPlayer2 = Action.COOPERATE;

			if (Random.bernoulliTrial(mistakeProbability)) {
				actualPlayPlayer1 = Action.COOPERATE;
			}
			if (Random.bernoulliTrial(mistakeProbability)) {
				actualPlayPlayer2 = Action.DEFECT;
			}

			// history tracking
			actionsPlayerOne.add(actualPlayPlayer1);
			actionsPlayerTwo.add(actualPlayPlayer2);
			// move automata
			playerOne.next(actualPlayPlayer1, actualPlayPlayer2);
			playerTwo.next(actualPlayPlayer2, actualPlayPlayer1);
			// payoff tracking
			double[] ans = payoff(actualPlayPlayer1, actualPlayPlayer2);
			return ans;

		}
	}

	public ArrayList<Action> getActionsPlayerOne() {
		return actionsPlayerOne;
	}

	public ArrayList<Action> getActionsPlayerTwo() {
		return actionsPlayerTwo;
	}

	/**
	 * Payoff with less noise, number of interactions is a parameter
	 * 
	 * @param playerOne
	 * @param playerTwo
	 * @param numberOfEstimates
	 * @return
	 */
	/*
	 * public double[] playManyTimes(ExplicitAutomaton playerOne,
	 * ExplicitAutomaton playerTwo, int numberOfEstimates){ SummaryStatistics
	 * player1Count = new SummaryStatistics(); SummaryStatistics player2Count =
	 * new SummaryStatistics(); for (int i = 0; i < numberOfEstimates; i++) {
	 * double[] samples = playOnce(playerOne, playerTwo);
	 * player1Count.addValue(samples[0]); player2Count.addValue(samples[0]); }
	 * double[] ans = {player1Count.getMean(), player2Count.getMean()}; return
	 * ans; }
	 */

	/**
	 * Payoff including mistake probability, less noise because number of
	 * interactions is a parameter
	 * 
	 * @param playerOne
	 * @param playerTwo
	 * @param numberOfEstimates
	 * @param mistakeProbability
	 * @return
	 */
	/*
	 * public double[] playManyTimes(ExplicitAutomaton playerOne,
	 * ExplicitAutomaton playerTwo, int numberOfEstimates, double
	 * mistakeProbability){ SummaryStatistics player1Count = new
	 * SummaryStatistics(); SummaryStatistics player2Count = new
	 * SummaryStatistics(); for (int i = 0; i < numberOfEstimates; i++) {
	 * double[] samples = playOnce(playerOne, playerTwo, mistakeProbability);
	 * player1Count.addValue(samples[0]); player2Count.addValue(samples[0]); }
	 * double[] ans = {player1Count.getMean(), player2Count.getMean()}; return
	 * ans; }
	 */

}
