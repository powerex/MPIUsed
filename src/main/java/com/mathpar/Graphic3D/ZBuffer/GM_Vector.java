/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.ZBuffer;

/**
 * <p>����� ������������ ��� �������� ���������� ����� </p>
 * @author not attributable
 * @version 1.0
 */
public class GM_Vector {

    /**
     * ���������� v,y,z ���������� ����� � ���������� ������������
     */
    public double x,  y,  z,  sx,  sy,  sz;

    /**
     * ����������� ��� �������� ����� � ���������������� ������������
     * @param v double
     * @param y double
     * @param z double
     */
    public GM_Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GM_Vector sum(GM_Vector p) {
        return new GM_Vector(this.x + p.x, this.y + p.y, this.z + p.z);
    }

    public GM_Vector sub(GM_Vector p) {
        return new GM_Vector(this.x - p.x, this.y - p.y, this.z - p.z);
    }

    public double prod(GM_Vector p) {
        return this.x * p.x + this.y * p.y + this.z * p.z;
    }

    public GM_Vector multOnTransformMatrix(double[][] m) {
        double[] v = new double[4];
        for (int i = 0; i < 4; i++) {
            v[i] = m[i][0] * x +
                    m[i][1] * y +
                    m[i][2] * z +
                    m[i][3];
        }
        return new GM_Vector(v[0], v[1], v[2]);
    }

    /**
     * @param number double
     */
    public GM_Vector mult(double number) {
        return new GM_Vector(this.x * number, this.y * number, this.z * number);
    }

    /**
     * @param p Point
     * @return double
     */
    public double getDistance(GM_Vector p) {
        return (double) Math.sqrt(getDistanceSqr(p));
    }

    /**
     * @param p Point
     * @return double
     */
    public double getDistanceSqr(GM_Vector p) {
        double dx = x - p.x,
                dy = y - p.y,
                dz = z - p.z;
        return dx * dx + dy * dy + dz * dz;
    }
}
