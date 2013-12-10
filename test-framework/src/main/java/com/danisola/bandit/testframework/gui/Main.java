package com.danisola.bandit.testframework.gui;

import com.danisola.bandit.*;
import com.danisola.bandit.testframework.arms.Arm;
import com.danisola.bandit.testframework.arms.BernoulliArm;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Arm[] arms = new Arm[]{
                new BernoulliArm(0.01),
                new BernoulliArm(0.02),
                new BernoulliArm(0.01)
        };

        int numArms = arms.length;
        AverageUpdateStrategy updateStrategy = new AverageUpdateStrategy();
        BanditAlgorithm[] algorithms = new BanditAlgorithm[]{
                new EpsilonGreedyAlgorithm(numArms, 0.01, updateStrategy),
                new EpsilonGreedyAlgorithm(numArms, 0.1, updateStrategy),
                new SoftmaxAlgorithm(numArms, 0.1, updateStrategy),
                new Ucb1Algorithm(numArms, updateStrategy)
        };

        JFrame frame = new JFrame("Bandit Algorithms - Test framework");
        frame.setContentPane(new MainDialog(arms, algorithms).getRoot());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
