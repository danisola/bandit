package com.danisola.bandit.testframework.scorers;

public class CumulativeRewardScorer implements Scorer {

    private double accumulatedScore = 0;

    @Override
    public double updateScore(int draw, int chosenArm, double reward) {
        accumulatedScore += reward;
        return accumulatedScore;
    }
}
