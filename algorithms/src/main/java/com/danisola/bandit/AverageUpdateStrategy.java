package com.danisola.bandit;

public class AverageUpdateStrategy implements UpdateStrategy {

    @Override
    public double update(int countsArm, double value, double reward) {
        return ((countsArm - 1) / (double) countsArm) * value + (1 / (double) countsArm) * reward;
    }
}
