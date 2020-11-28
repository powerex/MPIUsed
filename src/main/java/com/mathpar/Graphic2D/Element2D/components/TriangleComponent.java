package com.mathpar.Graphic2D.Element2D.components;

import com.mathpar.Graphic2D.ReadFromFile;
import com.mathpar.func.F;

import java.util.function.UnaryOperator;

import static com.mathpar.Graphic2D.Element2D.PlanimetriaUtils.tablePlot;
import static com.mathpar.Graphic2D.Element2D.PlanimetriaUtils.vector;

public class TriangleComponent implements PlanimetriaComponent {
    @Override
    public String getName() {
        return "ТРЕУГОЛЬНИК";
    }

    @Override
    public UnaryOperator<String> parseExpression() {
        return expr -> {
            double[] x = ReadFromFile.pX;
            double[] y = ReadFromFile.pY;
            String result = this.createTriangleF(x, y).toString();
            return result.substring(1, result.length() - 1);
        };
    }

    private F createTriangleF(double[] x, double[] y) {
        //1 line
        F fx1 = vector(x[0], x[1]);
        F fy1 = vector(y[0], y[1]);
        F ff1 = vector(fx1, fy1);
        F t1 = tablePlot(ff1);
        //2 line
        F fx2 = vector(x[1], x[2]);
        F fy2 = vector(y[1], y[2]);
        F ff2 = vector(fx2, fy2);
        F t2 = tablePlot(ff2);
        //3 line
        F fx3 = vector(x[2], x[0]);
        F fy3 = vector(y[2], y[0]);
        F ff3 = vector(fx3, fy3);
        F t3 = tablePlot(ff3);
        return vector(t1, t2, t3);
    }
}
