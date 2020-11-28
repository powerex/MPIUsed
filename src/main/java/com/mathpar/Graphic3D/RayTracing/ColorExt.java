/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.RayTracing;

import java.awt.Color;

/**
 *
 * @author yuri
 */
public class ColorExt {

    private int value = 0;

    public ColorExt() {
    }

    public ColorExt(Color color) {
        value = color.getRGB();
    }

    public ColorExt(ColorExt color) {
        value = color.getRGB();
    }

    ColorExt(int value) {
        this.value = value;
    }

    public ColorExt add(ColorExt color) {
        int c1 = value;
        int c2 = color.getRGB();
        int r = (c1 & 0xFF) + (c2 & 0xFF);
        int g = (c1 & 0xFF00) + (c2 & 0xFF00);
        int b = (c1 & 0xFF0000) + (c2 & 0xFF0000);
        r = r > 0xFF ? 0xFF : r;
        g = g > 0xFF00 ? 0xFF00 : g;
        b = b > 0xFF0000 ? 0xFF0000 : b;

        value = r | g | b | 0xFF000000;
        return this;
    }

    public ColorExt mult(double k) {
        if (k < 1) {
            int c1 = value;
            int r = (c1 & 0xFF) >> 0;
            int g = (c1 & 0xFF00) >> 8;
            int b = (c1 & 0xFF0000) >> 16;
            r = ((int) ((double) r * k)) << 0;
            g = ((int) ((double) g * k)) << 8;
            b = ((int) ((double) b * k)) << 16;

            value = r | g | b | 0xFF000000;
        }
        return this;
    }

    public int getRGB() {
        return value;
    }
}
