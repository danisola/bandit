package com.danisola.bandit.testframework.arms;

import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

public class BernoulliArm implements Arm {

    private final Random random = new Random();
    private final double p;

    public BernoulliArm(double p) {
        checkArgument(p >= 0 && p <= 1, "p must be between 0 and 1");
        this.p = p;
    }

    @Override
    public double draw() {
        if (random.nextDouble() > p) {
            return 0;
        }
        return 1;
    }

    @Override
    public double getExpectedValue() {
        return p;
    }
}
