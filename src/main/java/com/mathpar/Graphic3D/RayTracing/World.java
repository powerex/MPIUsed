/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.RayTracing;

import com.mathpar.Graphic3D.RayTracing.Objects.AbstractObject;
import com.mathpar.Graphic3D.RayTracing.Objects.Func3D;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author yuri
 */
public class World {

//    spline3D spline;
    ArrayList<AbstractObject> objects;

    public World() {
        objects = new ArrayList<AbstractObject>();

        Material material = new Material();
        material.color = new ColorExt(Color.GREEN);
        Func3D func3D = new Func3D(material);
        objects.add(func3D);

/*
        Material material = new Material();
        material.color = new ColorExt(Color.GREEN);
        Sphere sphere = new Sphere(-10, 0, 0, 20.9, material);
        objects.add(sphere);

        material = new Material();
        material.color = new ColorExt(Color.RED);
        Sphere sphere2 = new Sphere(50, 50, 0, 70, material);
        objects.add(sphere2);

        material = new Material();
        material.color = new ColorExt(Color.BLUE);
        Sphere sphere3 = new Sphere(-50, -50, 20, 30, material);
        objects.add(sphere3);

        for (int i = 0; i < 3; i++) {
            material = new Material();
            material.color = new ColorExt((int) (Math.random() * 0xFFFFFF) | 0xFF000000);
            Sphere sphereR = new Sphere(//
                    Math.random() * 100.0 - 50,
                    Math.random() * 100.0 - 50,
                    Math.random() * 100.0 - 50,
                    Math.random() * 40.0, material);
            objects.add(sphereR);
        }
 // */
//        double f[][][] = {{{0, 1, 2}, {1, 0, 2}, {1, 0, 2}}, {{1, 0, 2}, {0, 1, 2}, {1, 0, 2}}, {{1, 0, 2}, {0, 1, 2}, {1, 0, 2}}};

//        spline = new spline3D();
//        spline.buildTriCubic(f);

    }

    Hit getHit(Ray ray) {
        Hit resultHit = null;
        for (AbstractObject obj : objects) {
            Hit hit;
            if ((hit = obj.getHit(ray)) != null && (resultHit == null || hit.t < resultHit.t)) {
                resultHit = hit;
            }
        }
        return resultHit;
    }
}
