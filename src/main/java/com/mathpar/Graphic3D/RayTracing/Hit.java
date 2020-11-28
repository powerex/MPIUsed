/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.RayTracing;

/**
 *
 * @author yuri
 */
public class Hit {

    public Material material;
    public Ray normal;
    public double t;

    public Ray getReflectRay() {
        return null;
    }

    public Ray getRefractRay() {
        return null;
    }
}
