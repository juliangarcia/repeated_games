package com.evolutionandgames.repeatedgames.evolution;

import com.evolutionandgames.agentbased.Agent;

public class DummyAgent implements RepeatedStrategy, Agent{

	public void reset() {
		
	}

	public Action currentAction() {
		return Action.COOPERATE;
	}

	public void next(Action actualPlayPlayer1, Action actualPlayPlayer2) {
		
	}

	public DummyAgent() {
		super();
		
	}
	
	

}
