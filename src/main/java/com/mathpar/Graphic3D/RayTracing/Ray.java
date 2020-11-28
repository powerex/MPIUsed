/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.RayTracing;

/**
 *
 * @author yuri
 */
public final class Ray {

    public double xo;
    public double yo;
    public double zo;
    public double xn;
    public double yn;
    public double zn;

    public Ray() {
    }

    public Ray(double xo, double yo, double zo, double xn, double yn, double zn) {
        setParams(xo, yo, zo, xn, yn, zn);
    }

    public void setParams(double xo, double yo, double zo, double xn, double yn, double zn) {
        this.xo = xo;
        this.yo = yo;
        this.zo = zo;
        this.xn = xn;
        this.yn = yn;
        this.zn = zn;
    }

    public boolean inCircle(double r) {
        return xn*xn + yn*yn < r*r;
    }
}
