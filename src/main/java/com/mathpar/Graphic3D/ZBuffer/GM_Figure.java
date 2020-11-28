/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.ZBuffer;

import java.util.Vector;

public class GM_Figure {

    public GM_Figure Parent = null;
    public Vector<GM_Figure> Children = new Vector();
    public GM_Vector Object[][] = null;
    public GM_Triangle Triangles[] = null;
    public double u_size;
    public double v_size;
    public int u_count;
    public int v_count;
    public byte type;
    public GM_Color color;
    public static final byte points = 0;
    public static final byte lines = 1;
    public static final byte triangles = 2;
    public boolean visible = true;

    protected GM_Figure() {
    }

    public GM_Figure(GM_Figure Parent, byte type, GM_Color color) {
        if (Parent != null) {
            Parent.add(this);
        }
        this.type = type;
        this.color = color;
    }

    public GM_Figure(GM_Figure Parent, byte type, GM_Color color, double u_size, int u_count, double v_size, int v_count) {
        if (Parent != null) {
            Parent.add(this);
        }
        this.type = type;
        this.color = color;
        this.u_size = u_size;
        this.u_count = u_count;
        this.v_size = v_size;
        this.v_count = v_count;

        Object = new GM_Vector[u_count + 1][v_count + 1];
    }

    public void add(GM_Figure figure) {
        if (figure.Parent != null) {
            figure.Parent.Children.remove(figure);
        }
        figure.Parent = this;
        Children.add(figure);
        figure.m = m;
        figure.am = am;
    }
    public double m[][] = { // Матрица повората и перемещения точек
        {1, 0, 0, 0},
        {0, 1, 0, 0},
        {0, 0, 1, 0},
        {0, 0, 0, 1}};
    public double am[][] = m;
    private boolean changed = true; // true, если объект был перемещён или повёрнут

    public void rotateAndMove(double alpha, double betta, double gamma, double dx, double dy, double dz) {
        rotateAndMove(alpha, betta, gamma, dx, dy, dz, 0);
    }

    public void rotateAndMove(double alpha, double betta, double gamma, double dx, double dy, double dz, double dk) {
        for (GM_Figure cur : Children) {
            cur.rotateAndMove(alpha, betta, gamma, dx, dy, dz, dk);
        }
        changed = true;
        if (alpha != 0) {
            double cos = Math.cos(alpha);
            double sin = Math.sin(alpha);
            double[][] n = {{1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}};
            matrixMul(n);
        }
        if (betta != 0) {
            double cos = Math.cos(betta);
            double sin = Math.sin(betta);
            double[][] n = {{cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}};
            matrixMul(n);
        }
        if (gamma != 0) {
            double cos = Math.cos(gamma);
            double sin = Math.sin(gamma);
            double[][] n = {{cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
            matrixMul(n);
        }
        if (alpha != 0) {
            double cos = Math.cos(alpha);
            double sin = -Math.sin(alpha);
            double[][] n = {{1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}};
            amatrixMul(n);
        }
        if (betta != 0) {
            double cos = Math.cos(betta);
            double sin = -Math.sin(betta);
            double[][] n = {{cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}};
            amatrixMul(n);
        }
        if (gamma != 0) {
            double cos = Math.cos(gamma);
            double sin = -Math.sin(gamma);
            double[][] n = {{cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
            amatrixMul(n);
        }

        if (dx != 0 || dy != 0 || dz != 0) {
            double[][] n = {{1, 0, 0, dx},
                {0, 1, 0, dy},
                {0, 0, 1, dz},
                {0, 0, 0, 1}};
            matrixMul(n);
        }
        if (dx != 0 || dy != 0 || dz != 0) {
            double[][] n = {{1, 0, 0, -dx},
                {0, 1, 0, -dy},
                {0, 0, 1, -dz},
                {0, 0, 0, 1}};
            amatrixMul(n);
        }
        if (dk != 0) {
            double[][] n = {{1 + dk, 0, 0, 0},
                {0, 1 + dk, 0, 0},
                {0, 0, 1 + dk, 0},
                {0, 0, 0, 1}};
            matrixMul(n);
        }
    }
    double cutLevel = -300;

    public void setCutLevel(double cutLevel) {
        this.cutLevel = cutLevel;
        changed = true;
        for (GM_Figure cur : Children) {
            cur.setCutLevel(cutLevel);
        }
    }

    public void applyTransformation(double xSize2, double ySize2, double dist) {
        if (!visible) {
            return;
        }
        for (GM_Figure cur : Children) {
            cur.applyTransformation(xSize2, ySize2, dist);
        }
        // отсечение
        GM_Vector o = new GM_Vector(0, 0, cutLevel).multOnTransformMatrix(am);
        GM_Vector n = new GM_Vector(0, 0, cutLevel + 1).multOnTransformMatrix(am).sub(o);
        // конец отсечения
        double[][] old = m;
        double[][] proj = {// Матрица проекции
            {dist, 0, xSize2, xSize2 * dist},
            {0, dist, ySize2, ySize2 * dist},
            {0, 0, 1, 0},
            {0, 0, 1, dist}
        };
        matrixMul(proj);
        if (changed && Object != null) {
            for (int u = 0; u <= u_count; u++) {
                for (int v = 0; v <= v_count; v++) {
                    vectorProjection(Object[u][v]);
                }
            }
        }
        if (changed && Triangles != null) {
            for (int u = 0; u < Triangles.length; u++) {
                Triangles[u].visible = (Triangles[u].vertex[0].sub(o).prod(n) >= 0
                        && Triangles[u].vertex[1].sub(o).prod(n) >= 0
                        && Triangles[u].vertex[2].sub(o).prod(n) >= 0);
                if (Triangles[u].visible) {
                    for (int v = 0; v < 3; v++) {
                        vectorProjection(Triangles[u].vertex[v]);
                    }
                }
            }
        }
        changed = false;
        m = old;
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

    private void vectorProjection(GM_Vector p) {
        double[] x = new double[4];
        for (int i = 0; i < 4; i++) {
            x[i] = m[i][0] * p.x
                    + m[i][1] * p.y
                    + m[i][2] * p.z
                    + m[i][3];
        }
        p.sx = x[0] / x[3];
        p.sy = (int) (x[1] / x[3]);
        p.sz = x[2];
    }

    public void setMyMatrixesToChildren() {
        for (GM_Figure cur : Children) {
            cur.m = m;
            cur.am = am;
            cur.setMyMatrixesToChildren();
        }
    }
}
