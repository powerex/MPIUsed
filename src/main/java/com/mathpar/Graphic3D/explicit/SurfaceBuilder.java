/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.explicit;

import com.mathpar.Graphic3D.implicit.*;
import com.mathpar.func.F;
import com.mathpar.number.Element;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.crypto.Mac;

public class SurfaceBuilder {
    private final Ring ring;
    private final F func;
    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;
    private final double zMin;
    private final double zMax;
    private final double lightX;
    private final double lightY;
    private final double lightZ;
    private final int color;
    private final int gridSize;

    public static final double DEFAULT_X_MIN = -10.0;
    public static final double DEFAULT_X_MAX = 10.0;
    public static final double DEFAULT_Y_MIN = -10.0;
    public static final double DEFAULT_Y_MAX = 10.0;
    public static final double DEFAULT_Z_MIN = -10.0;
    public static final double DEFAULT_Z_MAX = 10.0;
    public static final double DEFAULT_LIGHT_X = 0;
    public static final double DEFAULT_LIGHT_Y = 12.0;
    public static final double DEFAULT_LIGHT_Z = 0;
    public static final int DEFAULT_COLOR = 0xbb3366;
    
    public static final int DEFAULT_GRID_SIZE = 50;

    public SurfaceBuilder(Ring ring, F func,
                         double xMin, double xMax,
                         double yMin, double yMax,
                         double zMin, double zMax,
                         int gridSize) {
        this.ring = ring;
        this.func = func;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;
        this.lightX = DEFAULT_LIGHT_X;
        this.lightY = DEFAULT_LIGHT_Y;
        this.lightZ = DEFAULT_LIGHT_Z;
        this.color = DEFAULT_COLOR;
        this.gridSize = gridSize;
    }

    public List<double[]> generateVertices() {
        final double xRange = xMax - xMin;
        final double yRange = yMax - yMin;

        List<double[]> points = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        List<double[]> vertices = new ArrayList<>();
        vertices.add(new double[] {xMin, xMax, yMin, yMax, zMin, zMax, gridSize, lightX, lightY, lightZ, color});
      
        for (int i = 0; i <= gridSize; i++) {
            double y = yMin + yRange * i / gridSize;
            for (int j = 0; j <= gridSize; j++) {
                double x = xMin + xRange * j / gridSize;
                Element g = func.valueOf(new NumberR64[]{new NumberR64(x), new NumberR64(y)}, ring);
                double z = g.doubleValue();
                double[] vertice = {x, y, z};
                vertices.add(vertice);
            }
        }
        return vertices;
    }
}
