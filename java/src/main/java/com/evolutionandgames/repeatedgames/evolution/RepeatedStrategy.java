package com.evolutionandgames.repeatedgames.evolution;


public interface RepeatedStrategy {

	public void reset();

	public Action currentAction();

	public void next(Action actualPlayPlayer1, Action actualPlayPlayer2);

}
