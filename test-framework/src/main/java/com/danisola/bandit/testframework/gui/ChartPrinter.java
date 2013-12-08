package com.danisola.bandit.testframework.gui;

import com.danisola.bandit.testframework.scorers.Scorer;
import info.monitorenter.gui.chart.ITrace2D;

public class ChartPrinter {

    private final ITrace2D trace;
    private final Scorer scorer;

    public ChartPrinter(ITrace2D trace, Scorer scorer) {
        this.trace = trace;
        this.scorer = scorer;
    }

    public void update(int draw, int selectedArm, double reward) {
        double score = scorer.updateScore(draw, selectedArm, reward);
        trace.addPoint(draw, score);
    }
}
