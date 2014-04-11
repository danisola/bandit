package com.danisola.bandit.testframework.gui;

import com.danisola.bandit.BanditAlgorithm;
import com.danisola.bandit.testframework.arms.Arm;
import com.danisola.bandit.testframework.scorers.AverageRewardScorer;
import com.danisola.bandit.testframework.scorers.BestArmSelectedScorer;
import com.danisola.bandit.testframework.scorers.CumulativeRewardScorer;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainDialog {

    private final Executor executor;
    private final List<Arm> arms;
    private final List<BanditAlgorithm> algorithms;

    private JPanel root;
    private Chart2D averageChart;
    private Chart2D bestChart;
    private Chart2D cumulativeChart;
    private JButton startButton;
    private JTextPane horizonTextField;
    private JLabel armsLabel;
    private JPanel algorithmsPanel;

    public MainDialog(Arm[] arms, BanditAlgorithm[] algorithms) {
        this.arms = Lists.newArrayList(arms);
        this.algorithms = Lists.newArrayList(algorithms);
        this.executor = Executors.newFixedThreadPool(algorithms.length);

        printArms();
        printAlgorithms();

        configureChart(averageChart);
        configureChart(bestChart);
        configureChart(cumulativeChart);

        startButton.addActionListener(e -> runSimulation());
    }

    public JPanel getRoot() {
        return root;
    }

    private void printArms() {
        armsLabel.setText(Joiner.on(" - ").join(Lists.transform(arms, Arm::getExpectedValue)));
    }

    private void printAlgorithms() {
        algorithmsPanel.setLayout(new BoxLayout(algorithmsPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < algorithms.size(); i++) {
            BanditAlgorithm algorithm = algorithms.get(i);
            JPanel colorPanel = new JPanel();
            colorPanel.setMinimumSize(new Dimension(10, 10));
            colorPanel.setMaximumSize(new Dimension(10, 10));
            colorPanel.setBackground(Colors.getColor(i));

            JLabel label = new JLabel(algorithm.toString());
            Box algorithmPanel = Box.createHorizontalBox();
            algorithmPanel.add(colorPanel);
            algorithmPanel.add(label);
            algorithmPanel.add(Box.createHorizontalGlue());
            algorithmsPanel.add(algorithmPanel);
        }
    }

    private void runSimulation() {
        resetTest();

        int numDraws = Integer.parseInt(horizonTextField.getText());

        for (int i = 0; i < algorithms.size(); i++) {
            BanditAlgorithm algorithm = algorithms.get(i);
            Color color = Colors.getColor(i);

            ITrace2D bestArmTrace = newTrace(bestChart, color);
            ITrace2D averageRewardTrace = newTrace(averageChart, color);
            ITrace2D cumulativeTrace = newTrace(cumulativeChart, color);

            List<ChartPrinter> printers = Lists.newArrayList(
                    new ChartPrinter(averageRewardTrace, new AverageRewardScorer()),
                    new ChartPrinter(bestArmTrace, new BestArmSelectedScorer(arms)),
                    new ChartPrinter(cumulativeTrace, new CumulativeRewardScorer()));

            executor.execute(() -> {
                for (int draw = 0; draw < numDraws; draw++) {
                    int selectedArm = algorithm.selectArm();
                    double reward = arms.get(selectedArm).draw();
                    algorithm.update(selectedArm, reward);

                    for (ChartPrinter printer : printers) {
                        printer.update(draw + 1, selectedArm, reward);
                    }
                }
            });
        }
    }

    private void resetTest() {
        averageChart.removeAllTraces();
        bestChart.removeAllTraces();
        cumulativeChart.removeAllTraces();
        algorithms.forEach(BanditAlgorithm::reset);
    }

    private ITrace2D newTrace(Chart2D chart, Color color) {
        ITrace2D trace = new Trace2DSimple();
        trace.setColor(color);
        chart.addTrace(trace);
        return trace;
    }

    private void configureChart(Chart2D chart) {
        chart.setToolTipType(Chart2D.ToolTipType.VALUE_SNAP_TO_TRACEPOINTS);
    }
}
