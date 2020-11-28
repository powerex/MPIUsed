/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.RayTracing;

/**
 *
 * @author yuri
 */
public final class RayTracer {

    public static int width;
    public static int height;
    private static int cameraDistance = 1000;
    private int[] iimg;
    private Ray[] primeRays;
    private TransformationMatrix transformationMatrix;

    public RayTracer(int width, int height) {
        setSize(width, height);
    }

    public void setSize(int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
        double w2 = -width / 2;
        double h2 = -height / 2;
        int arraySize = width * height;
        iimg = new int[arraySize];
        primeRays = new Ray[arraySize];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = width * y + x;
                iimg[i] = 200 * 256 + x + y;
                primeRays[i] = new Ray(0, 0, -cameraDistance, x + w2, y + h2, 0);
            }
        }
        transformationMatrix = new TransformationMatrix();
    }

    public void setPosition(double x, double y, double z) {
    }

    public void setAngles(double alpha, double betta, double gamma) {
    }

    public void setCameraDistance(int distance) {
        cameraDistance = distance;
    }

    public void rotate(double dalpha, double dbetta, double dgamma) {
        transformationMatrix.rotate(dalpha, dbetta, dgamma);
    }

    public void move(double dx, double dy, double dz) {
        transformationMatrix.move(dx, dy, dz);
    }

    public void zoom(double k) {
        transformationMatrix.zoom(k);
    }
// Алгоритм трассировки лучей (Будет выполнятся на сервере, хранящем часть мира)
    World world = new World();//заглушки
    Light light = new Light();

    ColorExt RayTrace(Ray ray) {
        ColorExt color = new ColorExt();
        Hit hit = world.getHit(ray);
        if (hit != null) {

            color.add(light.getLighting(hit));

            if (hit.material.reflection > 0) {
                Ray reflRay = hit.getReflectRay();
                color.add(RayTrace(reflRay).mult(hit.material.reflection));
            }

            if (hit.material.refraction > 0) {
                Ray refrRay = hit.getRefractRay();
                color.add(RayTrace(refrRay).mult(hit.material.refraction));
            }
        }
        return color;
    }

    // Эта функция будет выполняться на сервере, который отвечает за формирование части изображения.
    private void calcPixelColor(int x, int y) {
        int i = width * x + y;
        Ray r = transformationMatrix.transformRay(primeRays[i]);
        iimg[i] = RayTrace(r).getRGB();
    }

    //Эту функцию надо заменинть на отправку и получение частей экрана с сервера графики.
    public void nextFrame() {
        transformationMatrix.transformLight(light);
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                calcPixelColor(x, y);
            }
        }
    }

    public int[] getImageArray() {
        return iimg;
    }
}
