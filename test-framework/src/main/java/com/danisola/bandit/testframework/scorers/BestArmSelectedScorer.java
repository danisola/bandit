package com.danisola.bandit.testframework.scorers;

import com.danisola.bandit.testframework.arms.Arm;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class BestArmSelectedScorer implements Scorer {

    private final List<Arm> arms;
    private int selectedBestCount = 0;

    public BestArmSelectedScorer(List<Arm> arms) {
        checkArgument(arms != null && arms.size() > 1, "There must be at least two arms");
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
        //arms.stream().max((a1, a2) -> Double.compare(a1.getExpectedValue(), a2.getExpectedValue())).;
        List<Integer> bestArms = new ArrayList<>(arms.size());
        bestArms.add(0);
        double bestP = arms.get(0).getExpectedValue();
        for (int i = 1; i < arms.size(); i++) {
            Arm arm = arms.get(i);
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
