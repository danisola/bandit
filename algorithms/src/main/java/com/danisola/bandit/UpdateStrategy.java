package com.danisola.bandit;

public interface UpdateStrategy {

    public double update(int numCounts, double value, double reward);
}
