package com.evolutionandgames.repeatedgames.evolution;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HistoryAnalyzer {

	//Enconding alphabet
	private static char R = 'R';
	private static char S = 'S';
	private static char T = 'T';
	private static char P = 'P';
	
	
	private static Pattern patternALeniency = Pattern.compile("R{1,}S");
	private static Pattern patternBLeniency = Pattern.compile("R{1,}T");
	private static Pattern patternCLeniency = Pattern.compile("R{1,}S[RS]{1,}");
	private static Pattern patternDLeniency = Pattern.compile("R{1,}T[RT]{1,}");
	
	
	private static Pattern patternAForgiveness = Pattern.compile("R{1,}T[PS]");
	private static Pattern patternBForgiveness = Pattern.compile("R{1,}S[PT]");
	private static Pattern patternCForgiveness = Pattern.compile("R{1,}T[PS][RT]");
	private static Pattern patternDForgiveness = Pattern.compile("R{1,}S[PT][RS]");
	
	private static Pattern patternTotalCooperation = Pattern.compile("^R{1,}$");
	
	
	
	private static int count(Pattern pattern, String history) {
		Matcher matcher = pattern.matcher(history);
		int count = 0;
		while (matcher.find()) {
			count++;
		}return count;
	}
	
	
	
	public static int totalCooperation(String encodedInteraction){
		if (patternTotalCooperation.matcher(encodedInteraction).matches()) return 1;
		return 0;
	}
	
	public static String encodeHistories(List<Action> historyOfPlayer1, List<Action> historyOfPlayer2){
		int size = historyOfPlayer1.size();
		StringBuffer ans = new StringBuffer();
		for (int i = 0; i < size; i++) {
			ans.append(encodeStage(historyOfPlayer1.get(i), historyOfPlayer2.get(i)));
		}
		return ans.toString();
	}
	
	
	public static Double computeLeniencyScore(String encodedInteraction){
		double countA = count(patternALeniency, encodedInteraction);
		double countB = count(patternBLeniency, encodedInteraction);
		double countC = count(patternCLeniency, encodedInteraction);
		double countD = count(patternDLeniency, encodedInteraction);
		if (countA + countB == 0.0 ) {
			//if the index cannot be computed returns null
			return null;
		}
		return (countC + countD)/(countA + countB);
	}
	
	public static Double computeForgivenessScore(String encodedInteraction){
		double countA = count(patternAForgiveness, encodedInteraction);
		double countB = count(patternBForgiveness, encodedInteraction);
		double countC = count(patternCForgiveness, encodedInteraction);
		double countD = count(patternDForgiveness, encodedInteraction);
		if (countA + countB == 0.0 ) {
			//if the index cannot be computed returns null
			return null;
		}
		return (countC + countD)/(countA + countB);
	}
	
	
	


	private static char encodeStage(Action action, Action action2) {
		if (action == Action.COOPERATE && action2 == Action.COOPERATE){
			return R;
		}
		if (action == Action.COOPERATE && action2 == Action.DEFECT){
			return S;
		}
		if (action == Action.DEFECT && action2 == Action.COOPERATE){
			return T;
		}
		if (action == Action.DEFECT && action2 == Action.DEFECT){
			return P;
		}
		throw new IllegalStateException("Error enconding actions!");
	}
	
	
	
	
}
