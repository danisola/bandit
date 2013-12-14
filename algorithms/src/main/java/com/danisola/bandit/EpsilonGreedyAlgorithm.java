package com.danisola.bandit;

import java.util.Random;

/**
 * http://en.wikipedia.org/wiki/Multi-armed_bandit#Semi-uniform_strategies
 */
public class EpsilonGreedyAlgorithm extends AbstractBanditAlgorithm {

    private final double epsilon;
    private final Random random = new Random();

    public EpsilonGreedyAlgorithm(int numArms, double epsilon) {
        super(numArms);
        this.epsilon = epsilon;
    }

    @Override
    public int selectArm() {
        if (random.nextDouble() > epsilon) {
            return getArmWithHigherValue();
        }

        return random.nextInt(counts.length);
    }

    private int getArmWithHigherValue() {
        int maxArm = 0;
        double maxValue = values[0];
        for (int i = 1; i < values.length; i++) {
            double value = values[i];
            if (value > maxValue) {
                maxArm = i;
                maxValue = value;
            }
        }
        return maxArm;
    }

    @Override
    public String toString() {
        return "EpsilonGreedy {epsilon=" + epsilon + "}";
    }
}
