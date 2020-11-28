/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.RayTracing.Objects;

import com.mathpar.Graphic3D.RayTracing.Hit;
import com.mathpar.Graphic3D.RayTracing.Material;
import com.mathpar.Graphic3D.RayTracing.Ray;

/**
 *
 * @author yuri
 */
public class Func3D extends AbstractObject {

    public Func3D(Material material) {
        this.material = material;
    }

    private double f(double x, double y, double z) {
        return x * x + y * y + z * z - 500 * 500;
    }
    private Ray ray;

    private double f(double t) {
        return f(//
                ray.xo + ray.xn * t,
                ray.yo + ray.yn * t,
                ray.zo + ray.zn * t);
    }

    @Override
    public Hit getHit(Ray ray) {
        this.ray = ray;

        double old_t = 0;
        double step = 30.0;
        double t;
        for (t = step; t < 30.0 * step; t += step) {
            if (f(t) * f(old_t) <= 0) {
                break;
            }
            old_t = t;
        }
        if (t < 25.0 * step) {
            double x = ray.xo + ray.xn * t,
                    y = ray.yo + ray.yn * t,
                    z = ray.zo + ray.zn * t;
            double fxyz = f(x, y, z);
            double D = 0.1;
            Ray normal = new Ray(//
                    x,
                    y,
                    z,
                    x + fxyz - f(x + D, y, z),
                    y + fxyz - f(x, y + D, z),
                    z + fxyz - f(x, y, z + D));

            Hit hit = new Hit();
            hit.material = material;
            hit.normal = normal;
            hit.t = t;
            return hit;
        }
        return null;
    }
}
