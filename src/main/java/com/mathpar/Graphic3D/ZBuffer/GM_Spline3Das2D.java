/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.ZBuffer;

import com.mathpar.splines.spline3D;

/**
 *
 * @author yuri
 */
public class GM_Spline3Das2D extends GM_Figure {

    public GM_Spline3Das2D(GM_Figure Parent, byte type, GM_Color color, double[][][] f, double level) {
        super(Parent, type, color);
        spline3D sp3d = new spline3D(f);

        int maxx = f[0].length - 1;
        int maxy = f[0][0].length - 1;
        int M = 1+maxy * 30;
        int N = 1+maxx * 30;
        double[] X = new double[N];
        double[] Y = new double[M];
        double[][] values = new double[N][M];
        for (int i = 0; i < N; i++) {
            X[i] = (maxx * i) / (double) (N-1);
        }
        for (int i = 0; i < M; i++) {
            Y[i] = (maxy * i) / (double) (M - 1);
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                values[i][j] = sp3d.calc(level, X[i], Y[j]);
            }
        }

        Triangles = new GM_Triangle[2 * (N - 1) * (M - 1)];
        int t = 0;
        for (int i = 0; i < N - 1; i++) {
            for (int j = 0; j < M - 1; j++) {
                GM_Vector v11 = new GM_Vector(X[i], values[i][j], Y[j]);
                GM_Vector v12 = new GM_Vector(X[i], values[i][j + 1], Y[j + 1]);
                GM_Vector v21 = new GM_Vector(X[i + 1], values[i + 1][j], Y[j]);
                GM_Vector v22 = new GM_Vector(X[i + 1], values[i + 1][j + 1], Y[j + 1]);
                GM_Triangle t1 = new GM_Triangle(v11, v12, v22);
                GM_Triangle t2 = new GM_Triangle(v11, v21, v22);
                Triangles[t++] = t1;
                Triangles[t++] = t2;
            }
        }
    }
}
