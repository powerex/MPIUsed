/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic2D;

import java.awt.*;

/**
 *Класс реализует прорисовку осей координат и подписей единицизмерения
 *
 */
public class Axes {

    public static int h, l, x, y, measure;
    public static int edx, edy;
    public static int i;
    public static Graphics g;
/**метод построения осей координат и подписи значений */
    public static void axes(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        l = ToolBar.DEFAULT_WIDTH / 2;
        h = ToolBar.DEFAULT_HEIGHT / 2;

        g2.setColor(Color.BLACK);
        g2.drawLine(l, 0, l, 2 * h);
        g2.drawLine(0, h, 2 * l, h);
        x = 0;
        edx = (l / measure);
        for (i = -edx; i <= edx + 1; i++) {
            g2.drawLine(x, h - 3, x, h + 3);
            x = l + measure * i;

            if (i % 5 == 0 && i >= 0) {
                g2.drawString("" + i, x - 4, h + 12);
            }
            if ((edx + i) % 5 == 0 && i <= 0 && i > -edx) {
                g2.drawString("-" + (edx + i), l - x + 4, h + 12);
            }


        }
        y = 0;
        edy = (h / measure);
        for (i = -edy; i < edy + 2; i++) {
            g2.drawLine(l - 3, y, l + 3, y);
            y = h + measure * i;
            if (i % 5 == 0 && i < 0 && i > -edy) {
                g2.drawString("" + Math.abs(i), l + 8, y + 4);
            }
            if (i % 5 == 0 && i > 0) {
                g2.drawString("-" + i, l + 5, y + 4);
            }
        }
    }
}
