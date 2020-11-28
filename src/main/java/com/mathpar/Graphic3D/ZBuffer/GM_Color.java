/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.ZBuffer;

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
    public double[] frgbvalue;
    public double alpha;

    public GM_Color(double r, double g, double b, double alpha) {
        this.frgbvalue = new double[]{r, g, b};
        this.alpha = alpha;
    }

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
        alpha = 30;
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
        alpha = 30;//color.getTransparency();
    }

    /**
     * Умножает все  составляющие на <i>coeff</i> значение.
     * <br>Если конечные значения выходят за пределы [0..255], то они усекаются
     * @param coeff double
     * @return Color
     */
    private Color multLighte(double coeff) {
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
        return new Color(newR, newG, newB);
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
    /*
    private GM_Color multLight(double coeff) {
    double newR, newG, newB;
    newR = (frgbvalue[0] * coeff);
    newG = (frgbvalue[1] * coeff);
    newB = (frgbvalue[2] * coeff);
    return new GM_Color(newR, newG, newB);
    }
     */

    /**
     * определяет цвет отрезка, в зависимости от вычисленного коэффициента
     * интенсивности отраженного света
     * @param p0 Point-граничные точки,
     * @param p1 Point---определяющие освещаемый отрезок
     * @param light Light-источник света
     * @param viewer Point - позиция наблюдателя
     * @return Color
     */
    public Color getColorLine(GM_Vector p0, GM_Vector p1, GM_Light light,
            GM_Vector viewer /*,double distance*/) {
        double I = light.IlluminationLine(p0, p1, viewer /*,distance*/);
        Color color = multLighte(I);
        return color;
    }

    /**
     * определяет цвет треугольника, в зависимости от вычисленного коэффициента
     * интенсивности отраженного света
     * вершины освещаемого треугольника
     * @param p0 Point
     * @param p1 Point
     * @param p2 Point
     * @param viewer Point - позиция наблюдателя
     * @param light Light-источник света
     * @return Color
     */
    /*    public Color getColorN(GM_Vector p0, GM_Vector p1, GM_Vector p2,
    GM_Vector viewer, GM_Light light , GM_Vector dist) {
    double I = light.Light(p0, p1, p2, viewer , dist);
    Color color = multLighte(I);
    return color;

    }
     */
    public int getColorI(GM_Vector p0, GM_Vector p1, GM_Vector p2,
            GM_Light light, GM_Vector dist) {
        double I = light.Light(p0, p1, p2, dist);
        int color = multLighti(I) | (((int) alpha) << 24);
        return color;

    }

    /*  public GM_Color getColor(GM_Vector p0, GM_Vector p1, GM_Vector p2,
    GM_Vector viewer, GM_Light light , GM_Vector dist) {
    double I = light.Light(p0, p1, p2, viewer , dist);
    GM_Color color = multLight(I);
    return color;

    }*/
}
