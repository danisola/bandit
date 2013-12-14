package com.danisola.bandit;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractBanditAlgorithm implements BanditAlgorithm {

    protected final int numArms;
    protected final int[] counts;
    protected final double[] values;
    protected UpdateStrategy updateStrategy = new AverageUpdateStrategy();

    public AbstractBanditAlgorithm(int numArms) {
        this.numArms = numArms;
        this.counts = new int[numArms];
        this.values = new double[numArms];
        this.updateStrategy = checkNotNull(updateStrategy);
    }

    @Override
    public void update(int arm, double reward) {
        counts[arm]++;
        values[arm] = updateStrategy.update(counts[arm], values[arm], reward);;
    }

    @Override
    public void reset() {
        for (int i = 0; i < counts.length; i++) {
            counts[i] = 0;
            values[i] = 0;
        }
    }

    public void setUpdateStrategy(UpdateStrategy updateStrategy) {
        this.updateStrategy = checkNotNull(updateStrategy);
    }

    public abstract String toString();
}
