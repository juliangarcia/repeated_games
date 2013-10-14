package com.evolutionandgames.repeatedgames.utils;

import java.util.Comparator;
import java.util.Map;

import com.evolutionandgames.agentbased.Agent;

//Helper comparator for ordering the stationary distribution
public class MapBasedValueComparator implements Comparator<Agent> {
    Map<Agent, Double> base;
    
    public MapBasedValueComparator(Map<Agent, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(Agent a, Agent b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}