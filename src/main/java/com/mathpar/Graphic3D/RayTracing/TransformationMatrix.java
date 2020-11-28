/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.RayTracing;

/**
 *
 * @author yuri
 */
public class TransformationMatrix {

    private double m[][];
    private double am[][];

    public TransformationMatrix() {
        m = am = new double[][]{
                    {1, 0, 0, 0},
                    {0, 1, 0, 0},
                    {0, 0, 1, 0},
                    {0, 0, 0, 1}
                };
    }

    private void matrixMul(double[][] n) {
        double[][] M = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    M[i][j] += m[k][j] * n[i][k];
                }
            }
        }
        m = M;
    }

    private void amatrixMul(double[][] n) {
        double[][] M = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    M[i][j] += n[k][j] * am[i][k];
                }
            }
        }
        am = M;
    }

    public void rotate(double dalpha, double dbetta, double dgamma) {
        if (dalpha != 0) {
            double cos = Math.cos(dalpha);
            double sin = Math.sin(dalpha);
            double[][] n = {//
                {1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}};
            matrixMul(n);
        }

        if (dbetta != 0) {
            double cos = Math.cos(dbetta);
            double sin = Math.sin(dbetta);
            double[][] n = {//
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}};
            matrixMul(n);
        }

        if (dgamma != 0) {
            double cos = Math.cos(dgamma);
            double sin = Math.sin(dgamma);
            double[][] n = {//
                {cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
            matrixMul(n);
        }

        if (dalpha != 0) {
            double cos = Math.cos(dalpha);
            double sin = -Math.sin(dalpha);
            double[][] n = {//
                {1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}};
            amatrixMul(n);
        }

        if (dbetta != 0) {
            double cos = Math.cos(dbetta);
            double sin = -Math.sin(dbetta);
            double[][] n = {//
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}};
            amatrixMul(n);
        }

        if (dgamma != 0) {
            double cos = Math.cos(dgamma);
            double sin = -Math.sin(dgamma);
            double[][] n = {//
                {cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
            amatrixMul(n);
        }
    }

    public void move(double dx, double dy, double dz) {
        if (dx != 0 || dy != 0 || dz != 0) {
            double[][] n = {//
                {1, 0, 0, dx},
                {0, 1, 0, dy},
                {0, 0, 1, dz},
                {0, 0, 0, 1}};
            matrixMul(n);
        }
        if (dx != 0 || dy != 0 || dz != 0) {
            double[][] n = {//
                {1, 0, 0, -dx},
                {0, 1, 0, -dy},
                {0, 0, 1, -dz},
                {0, 0, 0, 1}};
            amatrixMul(n);
        }
    }

    public void zoom(double k) {
        if (k != 0) {
            double[][] n = {//
                {k, 0, 0, 0},
                {0, k, 0, 0},
                {0, 0, k, 0},
                {0, 0, 0, 1}};
            matrixMul(n);
        }
        k = 1 / k;
        if (k != 0) {
            double[][] n = {//
                {k, 0, 0, 0},
                {0, k, 0, 0},
                {0, 0, k, 0},
                {0, 0, 0, 1}};
            amatrixMul(n);
        }
    }

    public Ray transformRay(Ray ray) {
        double[] vo = new double[3];
        for (int i = 0; i < 3; i++) {
            vo[i] = m[i][0] * ray.xo
                    + m[i][1] * ray.yo
                    + m[i][2] * ray.zo
                    + m[i][3];
        }
        double[] vn = new double[3];
        for (int i = 0; i < 3; i++) {
            vn[i] = m[i][0] * ray.xn
                    + m[i][1] * ray.yn
                    + m[i][2] * ray.zn
                    + m[i][3];
        }
        return new Ray(vo[0], vo[1], vo[2], vn[0], vn[1], vn[2]);
    }

    public void transformLight(Light light) {
        double[] v = new double[3];
        for (int i = 0; i < 3; i++) {
            v[i] = m[i][0] * light.xo
                    + m[i][1] * light.yo
                    + m[i][2] * light.zo
                    + m[i][3];
        }
        light.xl = v[0];
        light.yl = v[1];
        light.zl = v[2];
    }
}
