/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.web.executor;

import com.mathpar.Graphic3D.implicit.MarchingCubes;
import com.mathpar.func.F;
import com.mathpar.func.Fname;
import com.mathpar.func.Page;
import com.mathpar.number.Element;
import com.mathpar.number.Ring;
import com.mathpar.web.exceptions.MathparException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

public class ImplicitPlot3dCallable implements Callable<List<double[]>> {
    private static final Logger LOG = LoggerFactory.getLogger(ImplicitPlot3dCallable.class);

    private final Page page;
    private final String task;
    private final int sectionId;

    public ImplicitPlot3dCallable(Page page, String task, int sectionId) {
        this.page = page;
        this.task = task;
        this.sectionId = sectionId;
    }

    @Override
    public List<double[]> call() throws Exception {
        String ignore = page.execution(task, sectionId);
        // TODO: better function detection - it could be not the last.
        final Element expr = page.expr.get(page.expr.size() - 1);
        if (expr instanceof Fname) {
            Element[] exprArgs = ((Fname) expr).X;
            if (exprArgs.length == 1 && exprArgs[0] instanceof F
                    && ((F) exprArgs[0]).name == F.IMPLICIT_PLOT3D) {
                final F implPlot3d = (F) exprArgs[0];
                  F funcToPlot = (F) implPlot3d.X[0];
                final int argsLen = implPlot3d.X.length;
                double xMin = page.xMin3D;
                double xMax = page.xMax3D;
                double yMin = page.yMin3D;
                double yMax = page.yMax3D;
                double zMin = page.zMin3D;
                double zMax = page.zMax3D;
                double lightX = MarchingCubes.DEFAULT_LIGHT_X;
                double lightY = MarchingCubes.DEFAULT_LIGHT_Y;
                double lightZ = MarchingCubes.DEFAULT_LIGHT_Z;
                int color = MarchingCubes.DEFAULT_COLOR;
                int gridSize = page.step3D;

//                F f,
//                double xMin, double xMax,
//                double yMin, double yMax,
//                double zMin, double zMax,
//                double lightX, double lightY, double lightZ,
//                int color, int gridSize

                if (argsLen >= 7) {
                    xMin = implPlot3d.X[1].doubleValue();
                    xMax = implPlot3d.X[2].doubleValue();
                    yMin = implPlot3d.X[3].doubleValue();
                    yMax = implPlot3d.X[4].doubleValue();
                    zMin = implPlot3d.X[5].doubleValue();
                    zMax = implPlot3d.X[6].doubleValue();
                }
                if (argsLen >= 10) {
                    lightX = implPlot3d.X[7].doubleValue();
                    lightY = implPlot3d.X[8].doubleValue();
                    lightZ = implPlot3d.X[9].doubleValue();
                }
                if (argsLen >= 11)   color = (implPlot3d.X[10].value(new Element[0], page.ring)).intValue();
                if (argsLen > 7) gridSize = implPlot3d.X[argsLen-1].intValue();
                


                if (xMax <= xMin) {
                    throw new MathparException(String.format(
                            "xMax (%f) must be > xMin (%f) for implicitPlot3d", xMax, xMin));
                }
                if (yMax <= yMin) {
                    throw new MathparException(String.format(
                            "yMax (%f) must be > yMin (%f) for implicitPlot3d", yMax, yMin));
                }
                if (zMax <= zMin) {
                    throw new MathparException(String.format(
                            "zMax (%f) must be > zMin (%f) for implicitPlot3d", zMax, zMin));
                }

                if (gridSize < 3) {
                    throw new MathparException(String.format(
                            "gridSize (%d) for implicitPlot3d must be >= 3", gridSize));
                }
              Element  funcToPlotEl= funcToPlot.ExpandFnameOrId();
              funcToPlot=(funcToPlotEl instanceof F)? (F)funcToPlotEl: new F(funcToPlotEl);
              return new MarchingCubes(page.ring(), funcToPlot,
                        xMin, xMax, yMin,
                        yMax, zMin, zMax,
                        lightX, lightY, lightZ,
                        color, gridSize)
                        .generateVertices();
            }
        }
        LOG.warn("Can't get parameters for {} function. Return null.", F.FUNC_NAMES[F.IMPLICIT_PLOT3D]);
        return null;
    }
}
