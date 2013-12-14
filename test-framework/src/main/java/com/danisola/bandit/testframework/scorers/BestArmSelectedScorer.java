package com.danisola.bandit.testframework.scorers;

import com.danisola.bandit.testframework.arms.Arm;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class BestArmSelectedScorer implements Scorer {

    private final Arm[] arms;
    private int selectedBestCount = 0;

    public BestArmSelectedScorer(Arm[] arms) {
        checkArgument(arms != null && arms.length > 1, "There must be at least two arms");
        this.arms = arms;
    }

    @Override
    public double updateScore(int draw, int chosenArm, double reward) {
        List<Integer> bestArms = getBestArms();
        if (bestArms.contains(chosenArm)) {
            selectedBestCount++;
        }

        return selectedBestCount / (double) draw;
    }

    private List<Integer> getBestArms() {
        List<Integer> bestArms = new ArrayList<>(arms.length);
        bestArms.add(0);
        double bestP = arms[0].getExpectedValue();
        for (int i = 1; i < arms.length; i++) {
            Arm arm = arms[i];
            if (arm.getExpectedValue() == bestP) {
                bestArms.add(i);
            } else if (arm.getExpectedValue() > bestP) {
                bestArms.clear();
                bestArms.add(i);
            }
        }
        return bestArms;
    }
}
