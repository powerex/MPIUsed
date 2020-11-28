/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.ZBuffer;

import com.mathpar.splines.spline1D;

/**
 *
 * @author yuri
 */
public class GM_Spline1D extends GM_Figure {

    public GM_Spline1D(GM_Figure Parent, byte type, GM_Color color, double[] y, spline1D.approxType aType) {
        super(Parent, type, color);
        spline1D sp1d = new spline1D(y, aType);
        int maxx = y.length - 1;
        int N = 1+maxx*100;
        double[] X = new double[N];
        double[] values = new double[N];
        for (int i = 0; i < N; i++) {
            X[i] = (maxx * i) / (double) (N-1);
            values[i] = sp1d.calc(X[i]);
        }
        double zWidth = 2;
        Triangles = new GM_Triangle[2 * (N - 1)];
        int t = 0;
        for (int i = 0; i < N - 1; i++) {
            GM_Vector v11 = new GM_Vector(X[i], values[i], -zWidth);
            GM_Vector v12 = new GM_Vector(X[i], values[i], zWidth);
            GM_Vector v21 = new GM_Vector(X[i + 1], values[i + 1], -zWidth);
            GM_Vector v22 = new GM_Vector(X[i + 1], values[i + 1], zWidth);
            GM_Triangle t1 = new GM_Triangle(v11, v12, v22);
            GM_Triangle t2 = new GM_Triangle(v11, v21, v22);
            Triangles[t++] = t1;
            Triangles[t++] = t2;
        }
    }
}
