package com.danisola.bandit.testframework;

import com.danisola.bandit.BanditAlgorithm;
import com.danisola.bandit.testframework.arms.Arm;
import com.danisola.bandit.testframework.gui.ChartPrinter;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class TestRunner implements Runnable {

    private final Arm[] arms;
    private final BanditAlgorithm algorithm;
    private final List<ChartPrinter> printers;
    private final int horizon;

    public TestRunner(Arm[] arms, int horizon, BanditAlgorithm algorithm, List<ChartPrinter> printers) {
        this.arms = checkNotNull(arms);
        this.horizon = horizon;
        this.algorithm = checkNotNull(algorithm);
        this.printers = checkNotNull(printers);
    }

    @Override
    public void run() {
        for (int draw = 0; draw < horizon; draw++) {
            int selectedArm = algorithm.selectArm();
            double reward = arms[selectedArm].draw();
            algorithm.update(selectedArm, reward);

            for (ChartPrinter printer : printers) {
                printer.update(draw + 1, selectedArm, reward);
            }
        }
    }
}
