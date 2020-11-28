/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D;

import java.awt.*;

/**
 * <p>Класс предназначен для задания цвета фигуры,помещенной на сцену  </p>
 * @ Екатерина Гордеева
 * @version 1.0
 */
public class GM_Color {

    /**
     * переменная r хранит красную  составляющую света
     */
    public int r;
    /**
     * переменная g хранит  зеленую составляющую света
     */
    public int g;
    /**
     * переменная b хранит  синую составляющую света
     */
    public int b;

    public double alpha;

    /**
     * конструктор для задания цвета фигуры по трем составляющим RGB,
     * если одно из значений первышает допустимые пределы, то оно обнуляется
     * @param r int
     * @param g int
     * @param b int
     */
    public GM_Color(int r, int g, int b) {
        if (r > 255 || g > 255 || b > 255) {
            r = 0;
            g = 0;
            b = 0;
        } else {
            this.r = r;
            this.g = g;
            this.b = b;
        }
        alpha = 100;
    }

    public GM_Color(int r, int g, int b, int alpha) {
        if (r > 255 || g > 255 || b > 255) {
            r = 0;
            g = 0;
            b = 0;
        } else {
            this.r = r;
            this.g = g;
            this.b = b;
        }
        this.alpha = alpha;
    }

    public GM_Color(Color color) {
        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();
        alpha = 100;
    }

    private int multLighti(double coeff) {
        int newR, newG, newB;
        newR = (int) (r * coeff);
        newG = (int) (g * coeff);
        newB = (int) (b * coeff);
        if (newR < 0) {
            newR = 0;
        }
        if (newR > 255) {
            newR = 255;
        }
        if (newG < 0) {
            newG = 0;
        }
        if (newG > 255) {
            newG = 255;
        }
        if (newB < 0) {
            newB = 0;
        }
        if (newB > 255) {
            newB = 255;
        }
        return ((newR << 16) | (newG << 8) | (newB));
    }


    /**
     * определяет цвет треугольника, в зависимости от вычисленного коэффициента
     * интенсивности отраженного света
     * вершины освещаемого треугольника
     * @param p0 GM_Vector
     * @param p1 GM_Vector
     * @param p2 GM_Vector
     * @param viewer GM_Vector - позиция наблюдателя
     * @param light Light-источник света
     * @return Color
     */

    public int getColorI(GM_Vector p0, GM_Vector p1, GM_Vector p2,
            GM_Light light) {
        double I = light.Light(p0, p1, p2);
        int color = multLighti(I) | (((int) alpha) << 24);

        return color;

    }

 }
