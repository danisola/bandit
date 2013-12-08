package com.danisola.bandit.testframework.gui;

import java.awt.*;

public class Colors {

    private static Color[] colors = new Color[]{
        new Color(226, 107, 103),
        new Color(139, 187, 107),
        new Color(99, 156, 208),
        new Color(240, 169, 100),
        new Color(149, 112, 170),
        new Color(195, 119, 94)
    };

    public static Color getColor(Integer num) {
        return colors[num % colors.length];
    }
}
