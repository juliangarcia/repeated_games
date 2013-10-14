package com.evolutionandgames.repeatedgames.utils;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.evolutionandgames.agentbased.AgentBasedPopulation;
import com.evolutionandgames.agentbased.ExtraColumnsProcessor;

public class ExtraMeasuresProcessor implements ExtraColumnsProcessor {

	public int getNumberOfExtraColumns() {
		return 3;
	}

	public String[] getColumnHeaders() {
		final String[] headers = {"Leniency","Forgiveness", "TotalCoop"};
		return headers;
	}

	public Object[] compute(AgentBasedPopulation population) {
		//This is a specific format
		SummaryStatistics[] arrayOfSummary =  (SummaryStatistics[]) population.getExtraInfo();
		if (arrayOfSummary != null){
			Object[] ans = new Object[3];
			ans[0] = arrayOfSummary[0].getMean();
			ans[1] = arrayOfSummary[1].getMean(); 
			ans[2] = arrayOfSummary[2].getMean();
			return ans;
		}
		else{
			//if no fitness has been computed this is what should be returned
			String[] array = {"NaN", "NaN", "0.0"};
			return array;
		}
	}

}
