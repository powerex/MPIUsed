package com.mathpar.web.executor;

import com.mathpar.func.F;
import com.mathpar.func.Fname;
import com.mathpar.func.Page;
import com.mathpar.number.Element;
import com.mathpar.web.entity.Plots3DCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlotCollection3DCallable implements Callable<Plots3DCollection> {

    private static final Logger LOG = LoggerFactory.getLogger(ParametricPlot3dCallable.class);

    private final Page page;
    private final String task;
    private final int sectionId;

    public PlotCollection3DCallable(Page page, String task, int sectionId) {
        this.page = page;
        this.task = task;
        this.sectionId = sectionId;
    }

    @Override
    public Plots3DCollection call() throws Exception {
        String ignore = page.execution(task, sectionId);
        // TODO: better function detection - it could be not the last.
        final Element expr = page.expr.get(page.expr.size() - 1);
        if (expr instanceof Fname) {
            Element[] exprArgs = ((Fname) expr).X;
            if (exprArgs.length == 1 && exprArgs[0] instanceof F
                    && ((F) exprArgs[0]).name == F.SHOW_PLOTS_3D) {
                Element[] plotFunctions = ((F) exprArgs[0]).X;
                Plots3DCollection results = new Plots3DCollection();
                for (Element plot : plotFunctions) {
                    if (plot.toString().contains(F.FUNC_NAMES[F.IMPLICIT_PLOT3D])) {
                        results.appendImplicit(this.getDataForImplicitPlot(plot.toString()));
                    } else if (plot.toString().contains(F.FUNC_NAMES[F.EXPLICIT_PLOT3D])) {
                        results.appendExplicit(this.getDataForExplicitPlot(plot.toString()));
                    } else if (plot.toString().contains(F.FUNC_NAMES[F.PARAMETRIC_PLOT3D])) {
                        results.appendParametric(this.getDataForParametricPlot(plot.toString()));
                    }
                }
                return results;
            }
        }
        LOG.warn("Can't get parameters for {} function. Return null.", F.FUNC_NAMES[F.SHOW_PLOTS_3D]);
        return null;
    }

    private List<double[]> getDataForImplicitPlot(String task) {
        return new ImplicitPlot3dTimeoutRunner().run(this.page, task, this.sectionId);
    }

    private List<double[]> getDataForExplicitPlot(String task) {
        return new ExplicitPlot3dTimeoutRunner().run(this.page, task, this.sectionId);
    }

    private List<double[]> getDataForParametricPlot(String task) {
        return new ParametricPlot3dTimeoutRunner().run(this.page, task, this.sectionId);
    }
}
