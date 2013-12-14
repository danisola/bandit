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
        BanditAlgorithm[] algorithms = new BanditAlgorithm[]{
                new EpsilonGreedyAlgorithm(numArms, 0.01),
                new EpsilonGreedyAlgorithm(numArms, 0.1),
                new SoftmaxAlgorithm(numArms, 0.1),
                new Ucb1Algorithm(numArms)
        };

        JFrame frame = new JFrame("Bandit Algorithms - Test framework");
        frame.setContentPane(new MainDialog(arms, algorithms).getRoot());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
