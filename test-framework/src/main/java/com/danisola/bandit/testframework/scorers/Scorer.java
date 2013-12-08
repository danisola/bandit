package com.danisola.bandit.testframework.scorers;

public interface Scorer {

    double updateScore(int draw, int chosenArm, double reward);
}
