package com.danisola.bandit;

public interface BanditAlgorithm {

    public int selectArm();

    public void update(int arm, double reward);

    public void reset();
}
