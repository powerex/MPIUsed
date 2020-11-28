/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.RayTracing;

/**
 *
 * @author yuri
 */
public class Light {

    public double xo = -3000, yo = -3000, zo = -3000;
    public double xl = xo, yl = yo, zl = zo;

    public ColorExt getLighting(Hit hit) {

        double x1 = hit.normal.xn - hit.normal.xo;
        double y1 = hit.normal.yn - hit.normal.yo;
        double z1 = hit.normal.zn - hit.normal.zo;

        double x2 = xl - hit.normal.xo;
        double y2 = yl - hit.normal.yo;
        double z2 = zl - hit.normal.zo;

        double cos = (x1 * x2 + y1 * y2 + z1 * z2) / Math.sqrt((x1 * x1 + y1 * y1 + z1 * z1) * (x2 * x2 + y2 * y2 + z2 * z2));

        if (cos < 0) {
            cos = 0;
        }

        double If = 0.1;
        double Id = If + (1.0 - If) * cos * 1.0;

        return new ColorExt(hit.material.color).mult(Id);
        /*  for (int i=0;i<NumLights;i++)

        if (Visible(hit_point, lights[i]))

        color += Shade(hit, lights[i]);
         */
    }
}
