package com.danisola.bandit;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractBanditAlgorithm implements BanditAlgorithm {

    protected final int numArms;
    protected final int[] counts;
    protected final double[] values;
    protected final UpdateStrategy updateStrategy;

    public AbstractBanditAlgorithm(int numArms, UpdateStrategy updateStrategy) {
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

    public abstract String toString();
}
