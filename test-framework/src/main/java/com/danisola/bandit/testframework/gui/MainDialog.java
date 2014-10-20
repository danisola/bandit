package com.danisola.bandit.testframework.gui;

import com.danisola.bandit.BanditAlgorithm;
import com.danisola.bandit.testframework.arms.Arm;
import com.danisola.bandit.testframework.scorers.AverageRewardScorer;
import com.danisola.bandit.testframework.scorers.BestArmSelectedScorer;
import com.danisola.bandit.testframework.scorers.CumulativeRewardScorer;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.danisola.bandit.testframework.gui.Colors.getRgbColor;
import static javafx.scene.paint.Color.rgb;

public class MainDialog implements Initializable, EventHandler<WindowEvent> {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final List<Arm> arms;
    private final List<BanditAlgorithm> algorithms;

    @FXML private LineChart<Number, Number> averageRewardChart;
    @FXML private LineChart<Number, Number> bestArmChart;
    @FXML private LineChart<Number, Number> cumulativeRewardChart;
    @FXML private ComboBox<Integer> horizonComboBox;
    @FXML private Label armsLabel;
    @FXML private GridPane algorithmsPane;

    public MainDialog(Arm[] arms, BanditAlgorithm[] algorithms) {
        this.arms = Lists.newArrayList(arms);
        this.algorithms = Lists.newArrayList(algorithms);
    }

    @FXML
    protected void startSimulation(ActionEvent actionEvent) {
        int numDraws = horizonComboBox.getValue();
        resetTest(numDraws, Lists.newArrayList(averageRewardChart, bestArmChart, cumulativeRewardChart));
        for (int i = 0; i < algorithms.size(); i++) {
            BanditAlgorithm algorithm = algorithms.get(i);
            Color color = Colors.getColor(i);

            XYChart.Series<Number, Number> bestArmSeries = series(bestArmChart, color);
            XYChart.Series<Number, Number> averageRewardSeries = series(averageRewardChart, color);
            XYChart.Series<Number, Number> cumulativeSeries = series(cumulativeRewardChart, color);

            executor.execute(() -> {
                List<ChartPrinter> printers = Lists.newArrayList(
                        new ChartPrinter(numDraws, new AverageRewardScorer(), averageRewardSeries),
                        new ChartPrinter(numDraws, new BestArmSelectedScorer(arms), bestArmSeries),
                        new ChartPrinter(numDraws, new CumulativeRewardScorer(), cumulativeSeries));

                for (int draw = 0; draw < numDraws; draw++) {
                    int selectedArm = algorithm.selectArm();
                    double reward = arms.get(selectedArm).draw();
                    algorithm.update(selectedArm, reward);

                    for (ChartPrinter printer : printers) {
                        printer.update(draw + 1, selectedArm, reward);
                    }
                }

                printers.forEach(ChartPrinter::print);
            });
        }
    }

    private XYChart.Series<Number, Number> series(LineChart<Number, Number> chart, Color color) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        chart.getData().add(series);
        series.nodeProperty().get().setStyle("-fx-stroke-width: 1px; -fx-stroke: #" + getRgbColor(color));
        return series;
    }

    private void resetTest(int numDraws, List<LineChart<Number, Number>> charts) {
        for (LineChart<Number, Number> chart : charts) {
            chart.getData().clear();
            NumberAxis xAxis = (NumberAxis) chart.getXAxis();
            xAxis.setUpperBound(numDraws);
            xAxis.setTickUnit(numDraws / 10);
        }

        algorithms.forEach(BanditAlgorithm::reset);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        armsLabel.setText(Joiner.on(" - ").join(Lists.transform(arms, Arm::getExpectedValue)));

        for (int i = 0; i < algorithms.size(); i++) {
            BanditAlgorithm algorithm = algorithms.get(i);
            Pane colorPanel = new Pane();
            colorPanel.setMinSize(10, 10);
            colorPanel.setMaxSize(10, 10);
            Color color = Colors.getColor(i);
            Rectangle colorRectangle = new Rectangle(10, 10, rgb(color.getRed(), color.getGreen(), color.getBlue()));

            algorithmsPane.add(colorRectangle, 0, i);

            Label label = new Label(algorithm.toString());
            algorithmsPane.add(label, 1, i);
        }
    }

    @Override
    public void handle(WindowEvent windowEvent) {
        executor.shutdown();
    }
}
