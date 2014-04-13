package com.danisola.bandit.testframework;

import com.danisola.bandit.*;
import com.danisola.bandit.testframework.arms.Arm;
import com.danisola.bandit.testframework.arms.BernoulliArm;
import com.danisola.bandit.testframework.gui.MainDialog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Arm[] arms = new Arm[]{
                new BernoulliArm(0.015),
                new BernoulliArm(0.02),
                new BernoulliArm(0.01)
        };

        int numArms = arms.length;
        BanditAlgorithm[] algorithms = new BanditAlgorithm[]{
                new EpsilonGreedyAlgorithm(numArms, 0.1),
                new EpsilonFirstAlgorithm(numArms, 1000),
                new SoftmaxAlgorithm(numArms, 0.1),
                new Ucb1Algorithm(numArms)
        };

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main_dialog.fxml"));
        MainDialog mainDialog = new MainDialog(arms, algorithms);
        fxmlLoader.setController(mainDialog);

        int minWidth = 800;
        int minHeight = 600;

        primaryStage.setTitle("Bandit Algorithms - Test framework");
        primaryStage.setMinWidth(minWidth);
        primaryStage.setMinHeight(minHeight);
        primaryStage.setOnCloseRequest(mainDialog::handle);
        primaryStage.setScene(new Scene(fxmlLoader.load(), minWidth, minHeight));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
