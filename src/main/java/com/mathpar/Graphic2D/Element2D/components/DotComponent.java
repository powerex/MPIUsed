package com.mathpar.Graphic2D.Element2D.components;

import com.mathpar.Graphic2D.ReadFromFile;
import com.mathpar.func.F;

import java.util.function.UnaryOperator;

import static com.mathpar.Graphic2D.Element2D.PlanimetriaUtils.*;

public class DotComponent implements PlanimetriaComponent {
    @Override
    public String getName() {
        return "ТОЧКА";
    }

    @Override
    public UnaryOperator<String> parseExpression() {
        return expr -> {
            double x = ReadFromFile.Xvis;
            double y = ReadFromFile.Yvis;
            return createPointF(x, y).toString();
        };
    }

    private F createPointF(double x, double y) {
        F fx = vector(x);
        F fy = vector(y);
        F point = vector(fx, fy);

        return pointsPlot(point, vector(""));
    }
}
