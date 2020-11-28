package com.mathpar.Graphic2D.Element2D.components;

import com.mathpar.Graphic2D.ReadFromFile;
import com.mathpar.func.F;

import java.util.function.UnaryOperator;

import static com.mathpar.Graphic2D.Element2D.PlanimetriaUtils.*;

public class RectangleComponent implements PlanimetriaComponent {
    @Override
    public String getName() {
        return "ПРЯМОУГОЛЬНИК";
    }

    @Override
    public UnaryOperator<String> parseExpression() {
        return expr -> {
            double[] x = ReadFromFile.pX;
            double[] y = ReadFromFile.pY;
            String result = createRectangleF(x, y).toString();
            return result.substring(1, result.length() - 1);
        };
    }

    private F createRectangleF(double[] x, double[] y) {

        F fx1 = vector(x[1], x[2]);
        F fy1 = vector(y[1], y[2]);
        F ff1 = vector(fx1, fy1);
        F t1 = tablePlot(ff1);

        F fx2 = vector(x[0], x[1]);
        F fy2 = vector(y[0], y[1]);
        F ff2 = vector(fx2, fy2);
        F t2 = tablePlot(ff2);

        F fx3 = vector(x[2], x[3]);
        F fy3 = vector(y[2], y[3]);
        F ff3 = vector(fx3, fy3);
        F t3 = tablePlot(ff3);

        F fx4 = vector(x[0], x[3]);
        F fy4 = vector(y[0], y[3]);
        F ff4 = vector(fx4, fy4);
        F t4 = tablePlot(ff4);
        return vector(t1, t2, t3, t4);
    }
}
