package com.danisola.bandit.testframework.gui;

import com.danisola.bandit.BanditAlgorithm;
import com.danisola.bandit.testframework.TestRunner;
import com.danisola.bandit.testframework.arms.Arm;
import com.danisola.bandit.testframework.scorers.AverageRewardScorer;
import com.danisola.bandit.testframework.scorers.BestArmSelectedScorer;
import com.danisola.bandit.testframework.scorers.CumulativeRewardScorer;
import com.google.common.collect.Lists;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainDialog {

    private final Executor executor = Executors.newCachedThreadPool();
    private final Arm[] arms;
    private final BanditAlgorithm[] algorithms;

    private JPanel root;
    private Chart2D averageChart;
    private Chart2D bestChart;
    private Chart2D cumulativeChart;
    private JButton startButton;
    private JTextPane horizonTextField;
    private JLabel armsLabel;
    private JPanel algorithmsPanel;

    public MainDialog(Arm[] arms, BanditAlgorithm[] algorithms) {
        this.arms = arms;
        this.algorithms = algorithms;

        printArms();
        printAlgorithms();

        configureChart(averageChart);
        configureChart(bestChart);
        configureChart(cumulativeChart);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runSimulation();
            }
        });
    }

    public JPanel getRoot() {
        return root;
    }

    private void printArms() {
        StringBuilder sb = new StringBuilder();
        sb.append(arms[0].getExpectedValue());
        for (int i = 1; i < arms.length; i++) {
            sb.append(" - ");
            sb.append(arms[i].getExpectedValue());
        }
        armsLabel.setText(sb.toString());
    }

    private void printAlgorithms() {
        algorithmsPanel.setLayout(new BoxLayout(algorithmsPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < algorithms.length; i++) {
            BanditAlgorithm algorithm = algorithms[i];
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
        averageChart.removeAllTraces();
        bestChart.removeAllTraces();
        cumulativeChart.removeAllTraces();
        resetAlgorithms();

        int numDraws = Integer.parseInt(horizonTextField.getText());

        for (int i = 0; i < algorithms.length; i++) {
            BanditAlgorithm algorithm = algorithms[i];
            Color color = Colors.getColor(i);

            ITrace2D bestArmTrace = newTrace(bestChart, color);
            ITrace2D averageRewardTrace = newTrace(averageChart, color);
            ITrace2D cumulativeTrace = newTrace(cumulativeChart, color);

            List<ChartPrinter> printers = Lists.newArrayList(
                    new ChartPrinter(averageRewardTrace, new AverageRewardScorer()),
                    new ChartPrinter(bestArmTrace, new BestArmSelectedScorer(arms)),
                    new ChartPrinter(cumulativeTrace, new CumulativeRewardScorer())
            );
            executor.execute(new TestRunner(arms, numDraws, algorithm, printers));
        }
    }

    private void resetAlgorithms() {
        for (BanditAlgorithm algorithm : algorithms) {
            algorithm.reset();
        }
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
