package com.danisola.bandit.testframework.gui;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bandit Algorithms - Test framework");
        frame.setContentPane(new MainDialog().getRoot());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
