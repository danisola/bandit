package com.danisola.bandit;

public class Ucb1Algorithm extends AbstractBanditAlgorithm {

    public Ucb1Algorithm(int numArms, UpdateStrategy updateStrategy) {
        super(numArms, updateStrategy);
    }

    @Override
    public int selectArm() {
        int totalCount = 0;
        for (int i = 0; i < counts.length; i++) {
            int count = counts[i];
            if (count == 0) {
                return i;
            }
            totalCount += count;
        }

        double[] ucbValues = new double[numArms];
        for (int i = 0; i < numArms; i++) {
            double bonus = Math.sqrt(2 * Math.log(totalCount / counts[i]));
            ucbValues[i] = values[i] + bonus;
        }

        int maxIndex = 0;
        for (int i = 1; i < ucbValues.length; i++) {
            double newValue = ucbValues[i];
            if (newValue > ucbValues[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    @Override
    public String toString() {
        return "UCB1 {}";
    }
}
