/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.web.executor;

import com.mathpar.Graphic3D.explicit.SurfaceBuilder;
import com.mathpar.func.F;
import com.mathpar.func.Fname;
import com.mathpar.func.Page;
import com.mathpar.number.Element;
import com.mathpar.web.exceptions.MathparException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

public class ExplicitPlot3dCallable implements Callable<List<double[]>> {
    private static final Logger LOG = LoggerFactory.getLogger(ExplicitPlot3dCallable.class);

    private final Page page;
    private final String task;
    private final int sectionId;

    public ExplicitPlot3dCallable(Page page, String task, int sectionId) {
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
                    && ((F) exprArgs[0]).name == F.EXPLICIT_PLOT3D) {
                final F explPlot3d = (F) exprArgs[0];
                  F funcToPlot = (F) explPlot3d.X[0];
                final int argsLen = explPlot3d.X.length;
                double xMin = page.xMin3D;
                double xMax = page.xMax3D;
                double yMin = page.yMin3D;
                double yMax = page.yMax3D;
                double zMin = page.zMin3D;
                double zMax = page.zMax3D;
                int gridSize = page.step3D;

                if (argsLen == 7) {
                    xMin = explPlot3d.X[1].doubleValue();
                    xMax = explPlot3d.X[2].doubleValue();
                    yMin = explPlot3d.X[3].doubleValue();
                    yMax = explPlot3d.X[4].doubleValue();
                    zMin = explPlot3d.X[5].doubleValue();
                    zMax = explPlot3d.X[6].doubleValue();
                }
                
                if (argsLen > 7) {
                    gridSize = explPlot3d.X[7].intValue();
                }

                if (xMax <= xMin) {
                    throw new MathparException(String.format(
                            "xMax (%f) must be > xMin (%f) for explicitPlot3d", xMax, xMin));
                }
                if (yMax <= yMin) {
                    throw new MathparException(String.format(
                            "yMax (%f) must be > yMin (%f) for explicitPlot3d", yMax, yMin));
                }
                if (zMax <= zMin) {
                    throw new MathparException(String.format(
                            "zMax (%f) must be > zMin (%f) for explicitPlot3d", zMax, zMin));
                }

                if (gridSize < 3) {
                    throw new MathparException(String.format(
                            "gridSize (%d) for explicitPlot3d must be >= 3", gridSize));
                }

                Element  funcToPlotEl= funcToPlot.ExpandFnameOrId();
                funcToPlot=(funcToPlotEl instanceof F)? (F)funcToPlotEl: new F(funcToPlotEl);
                SurfaceBuilder surface = new SurfaceBuilder(page.ring(), funcToPlot,
                        xMin, xMax, yMin,
                        yMax, zMin, zMax,
                        gridSize);
                return surface.generateVertices();
            }
        }
        LOG.warn("Can't get parameters for {} function. Return null.", F.FUNC_NAMES[F.IMPLICIT_PLOT3D]);
        return null;
    }
}
