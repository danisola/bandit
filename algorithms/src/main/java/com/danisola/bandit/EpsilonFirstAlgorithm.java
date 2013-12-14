package com.danisola.bandit;

import java.util.ArrayList;
import java.util.Random;

import static com.google.common.base.Preconditions.checkState;

/**
 * {@link com.danisola.bandit.BanditAlgorithm} that chooses a random lever during the first epsilon draws
 * (exploration phase), and the lever with the highest estimated mean after that (exploitation phase).
 *
 * http://en.wikipedia.org/wiki/Multi-armed_bandit#Semi-uniform_strategies
 */
public class EpsilonFirstAlgorithm extends AbstractBanditAlgorithm {

    private final Random random = new Random();
    private final int epsilon;
    private int currentDraw;

    public EpsilonFirstAlgorithm(int numArms, int epsilon) {
        super(numArms);

        checkState(epsilon >= 0, "Epsilon must be equals or larger than 0");
        this.epsilon = epsilon;
    }

    @Override
    public int selectArm() {
        currentDraw++;

        if (currentDraw <= epsilon) {
            return random.nextInt(counts.length);
        }

        return getArmWithHigherValue();
    }

    private int getArmWithHigherValue() {
        ArrayList<Integer> bestArms = new ArrayList<>(values.length);
        bestArms.add(0);

        double maxValue = values[0];
        for (int i = 1; i < values.length; i++) {
            double value = values[i];
            if (value > maxValue) {
                bestArms.clear();
                bestArms.add(i);
                maxValue = value;
            } else if (value == maxValue) {
                bestArms.add(i);
            }
        }

        return bestArms.get(random.nextInt(bestArms.size()));
    }

    @Override
    public String toString() {
        return "EpsilonFirst {epsilon=" + epsilon + "}";
    }
}
