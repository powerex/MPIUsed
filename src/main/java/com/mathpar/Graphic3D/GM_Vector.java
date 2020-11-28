/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D;

/**
 * Класс создает радус-ветор точки и хранит ее атрибуты
 * @author JuliaGrishina
 */
public class GM_Vector {
/**
 * экранная координата точки
 */
    public double sx, sy, sz;
    /**
     * координата точки в пространстве
     */
    public double x, y, z;
    /**
     * тип объекта
     */
    public int type;

    /**
     * Радиус-вектор
     * @param x double
     * @param y double
     * @param z double
     */
    public GM_Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Радиус-вектор
     * @param x double
     * @param y double
     * @param z double
     * @param  type int тип объекта
     */
    public GM_Vector(double x, double y, double z, int type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }

    /**
     * сложение векторов
     * @param p вектор с которым производится суммирование
     * @return новый вектор суммы
     */
    public GM_Vector sum(GM_Vector p) {
        return new GM_Vector(this.x + p.x, this.y + p.y, this.z + p.z);
    }

    /**
     * разность векторов
     * @param p GM_Vector вычитаемый вектор
     * @return GM_Vector новый вектор разности
     */
    public GM_Vector sub(GM_Vector p) {
        return new GM_Vector(this.x - p.x, this.y - p.y, this.z - p.z);
    }

/**
 * Умножение вектора на матрицу m
 * @param m матрица
 * @return GM_Vector
 */
    public GM_Vector multOnTransformMatrix(double[][] m) {
        double[] v = new double[4];
        for (int i = 0; i < 4; i++) {
            v[i] = m[i][0] * x
                    + m[i][1] * y
                    + m[i][2] * z
                    + m[i][3];
        }
        return new GM_Vector(v[0], v[1], v[2]);
    }

    /**
     * Умножение вектора на число
     * @param number double
     */
    public GM_Vector mult(double number) {
        return new GM_Vector(this.x * number, this.y * number, this.z * number);
    }

    /**
     * Расстояние между радиус-векторами
     * @param p  GM_Vector
     * @return double
     */
    public double getDistance(GM_Vector p) {
        return (double) Math.abs(Math.sqrt(getDistanceSqr(p)));
    }

    /**
     * Квадрат расстояния между радиус-векторами
     * @param p GM_Vector
     * @return double
     */
    public double getDistanceSqr(GM_Vector p) {
        double dx = x - p.x,
                dy = y - p.y,
                dz = z - p.z;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * длина вектора
     * @return double
     */
    public double getLength() {
        return Math.sqrt(x * x + y * y + z * z);
    }
}
