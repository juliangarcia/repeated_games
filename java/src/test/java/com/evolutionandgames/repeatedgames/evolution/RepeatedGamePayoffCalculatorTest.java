package com.evolutionandgames.repeatedgames.evolution;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentBasedPopulation;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulation;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulationImpl;
import com.evolutionandgames.jevodyn.utils.Random;

public class RepeatedGamePayoffCalculatorTest {

	

	@Before
	public void setUp() throws Exception {
		Random.seed();
	}

	@Test
	public void testNoMatchingNoise() {
		
		RepeatedGame repeatedGame = new RepeatedGame(1.0, 1.0, 1.0, 1.0, 0.0);
		RepeatedGamePayoffCalculator calculator = new RepeatedGamePayoffCalculator(repeatedGame, 0.0, false, false);
		Agent[] agentArray = getDummyArray(Random.nextInt(50));
		AgentBasedPopulation population = new ExtensivePopulationImpl(agentArray);
		calculator.calculatePayoffs(population);
		for (int i = 0; i < population.getSize(); i++) {
			double payoff = ((ExtensivePopulation)population).getPayoffOfAgent(i);
			assertEquals(1.0, payoff, 0.0);
		}
	}
	
	
	@Test
	public void testWithMatchingNoise() {
		
		RepeatedGame repeatedGame = new RepeatedGame(1.0, 1.0, 1.0, 1.0, 0.0);
		RepeatedGamePayoffCalculator calculator = new RepeatedGamePayoffCalculator(repeatedGame, 0.0, false, true);
		int evenSize = 3;
		while(evenSize%2 != 0){
			evenSize = Random.nextInt(50);
		}
		Agent[] agentArray = getDummyArray(evenSize);
		AgentBasedPopulation population = new ExtensivePopulationImpl(agentArray);
		calculator.calculatePayoffs(population);
		for (int i = 0; i < population.getSize(); i++) {
			double payoff = ((ExtensivePopulation)population).getPayoffOfAgent(i);
			assertEquals(1.0, payoff, 0.0);
		}
	}
	

	private Agent[] getDummyArray(int size) {
		Agent[] ans = new Agent[size];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = new DummyAgent();
		}
		return ans;
	}

}
