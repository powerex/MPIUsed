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
public class Sphere extends AbstractObject {

    double xo = 30;
    double yo = 0;
    double zo = 0;
    double R = 15;


    public Sphere(double xo, double yo, double zo, double R, Material material) {
        setParams(xo, yo, zo, R, material);
    }

    public final void setParams(double xo, double yo, double zo, double R, Material material) {
        this.xo = xo;
        this.yo = yo;
        this.zo = zo;
        this.R = R;
        this.material = material;
    }

    @Override
    public Hit getHit(Ray ray) {
        double X0 = ray.xo - xo;
        double Y0 = ray.yo - yo;
        double Z0 = ray.zo - zo;

        double Dx = ray.xn - ray.xo;
        double Dy = ray.yn - ray.yo;
        double Dz = ray.zn - ray.zo;

        double a = Dx * Dx + Dy * Dy + Dz * Dz;
        double b = 2 * (X0 * Dx + Y0 * Dy + Z0 * Dz);
        double c = X0 * X0 + Y0 * Y0 + Z0 * Z0 - R * R;

        double D = b * b - 4 * a * c;
        if (D >= 0) {
            D = Math.sqrt(D);
            double t1 = (-b - D) / (2 * a);
            double t2 = (-b + D) / (2 * a);
            double t = -1;
            if (t1 > 0) {
                t = t1;
            } else if (t2 > 0) {
                t = t2;
            }
            if (t > 0) {
                Ray normal = new Ray(xo, yo, zo, (ray.xo + Dx * t) * 2 - xo, (ray.yo + Dy * t) * 2 - yo, (ray.zo + Dz * t) * 2 - zo);

                Hit hit = new Hit();
                hit.material = material;
                hit.normal = normal;
                hit.t = t;
                return hit;
            }
        }
        return null;
    }
}
