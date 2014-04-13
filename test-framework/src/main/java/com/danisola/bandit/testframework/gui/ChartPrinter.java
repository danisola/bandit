package com.danisola.bandit.testframework.gui;

import com.danisola.bandit.testframework.scorers.Scorer;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class ChartPrinter {

    private final Scorer scorer;
    private final List<XYChart.Data<Number, Number>> data;
    private final XYChart.Series<Number, Number> series;

    public ChartPrinter(int numDraws, Scorer scorer, XYChart.Series<Number, Number> series) {
        this.data = new ArrayList<>(numDraws);
        this.scorer = scorer;
        this.series = series;
    }

    public void update(int draw, int selectedArm, double reward) {
        double score = scorer.updateScore(draw, selectedArm, reward);
        data.add(new XYChart.Data<>(draw, score));
    }

    public void print() {
        Platform.runLater(() -> series.getData().addAll(data));
    }
}
