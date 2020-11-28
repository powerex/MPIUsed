package com.mathpar.Graphic2D.Element2D.components;

import com.mathpar.Graphic2D.ReadFromFile;
import com.mathpar.func.F;

import java.util.function.UnaryOperator;

import static com.mathpar.Graphic2D.Element2D.PlanimetriaUtils.tablePlot;
import static com.mathpar.Graphic2D.Element2D.PlanimetriaUtils.vector;

public class MedianComponent implements PlanimetriaComponent {

    @Override
    public String getName() {
        return "МЕДИАНА";
    }

    @Override
    public UnaryOperator<String> parseExpression() {
        return expr -> {
            double[] x = ReadFromFile.pX;
            double[] y = ReadFromFile.pY;
            double[] point = ReadFromFile.pointFrom;
            String result = showMedian(x, y, point).toString();
            return result.substring(1, result.length() - 1);

        };
    }

    private F showMedian(double[] x, double[] y, double[] pointFrom) {
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
        // median
        double[][] median = getMedianCoords(x, y, pointFrom);
        F fx4 = vector(median[0][0], median[1][0]);
        F fy4 = vector(median[0][1], median[1][1]);
        F ff4 = vector(fx4, fy4);
        F t4 = tablePlot(ff4);

        return vector(t1, t2, t3, t4);
    }

    private static double[][] getMedianCoords(double[] x, double[] y, double[] pointFrom) {
        System.out.println("Calculate median coords");
        double[][] segment = new double[2][2];
        int index = 0;
        for (int i = 0; i < x.length; ++i) {
            if (x[i] == pointFrom[0] && y[i] == pointFrom[1]) continue;
            segment[index][0] = x[i];
            segment[index][1] = y[i];
            ++index;
        }

        double[] pointTo = new double[2];
        pointTo[0] = (segment[0][0] + segment[1][0]) / 2;
        pointTo[1] = (segment[0][1] + segment[1][1]) / 2;
        double[][] res = new double[2][2];
        res[0] = pointFrom;
        res[1] = pointTo;
        return res;
    }
}
