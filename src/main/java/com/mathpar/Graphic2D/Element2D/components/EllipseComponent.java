package com.mathpar.Graphic2D.Element2D.components;

import com.mathpar.Graphic2D.ReadFromFile;
import com.mathpar.func.F;
import com.mathpar.number.Element;
import com.mathpar.polynom.Polynom;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static com.mathpar.Graphic2D.Element2D.PlanimetriaUtils.*;

public class EllipseComponent implements PlanimetriaComponent {
    @Override
    public String getName() {
        return "ЭЛЛИПС";
    }

    @Override
    public UnaryOperator<String> parseExpression() {
        return expr -> {
            List<Element> plots = new ArrayList<>();

            double x = ReadFromFile.Xvis;
            double y = ReadFromFile.Yvis;
            double radX = ReadFromFile.RadXvis;
            double radY = ReadFromFile.RadYvis;
            String label = ReadFromFile.Label;
            double rotate = ReadFromFile.RotationAngle;

            double tanDeg = ReadFromFile.Avis;
            double tanLen = ReadFromFile.Lvis;
            double secA = ReadFromFile.startArc;
            double secB = ReadFromFile.Arc;

            if (secA != secB) {
                plots.add(createSector(x, y, radX, radY, secA, secB, rotate));
            }
            plots.add(createEllipsis(x, y, radX, radY, rotate));
            plots.add(createPointF(x, y, label));

            String result = plots.toString();
            return result.substring(1, result.length() - 1);
        };
    }

    /**
     * Отрисовка елипса. Является обберткой параметрической функции:
     * # g = a * cos(t) * cos(theta) - b * sin(t) * sin(theta) + x0;
     * # k = b * sin(t) * cos(theta) + a * cos(t) * sin(theta) + y0;
     * \paramPlot([g, k], [0, 2 * \pi])
     *
     * @param x        координата Х центра
     * @param y        координата Y центра
     * @param a        первая полуось
     * @param b        вторая полуось
     * @param rotation угол вращения елипса относительно центра в градусах
     * @return объект paramPlot
     */
    public static F createEllipsis(double x, double y, double a, double b, double rotation) {
        double theta = Math.toRadians(rotation);
        Polynom sinTheta = r64(Math.sin(theta));
        Polynom cosTheta = r64(Math.cos(theta));

        F funcG = subtract(add(r64(x),
                multiply(r64(a), multiply(cosTheta, cosX()))),
                multiply(r64(b), multiply(sinTheta, sinX()))
        );
        F funcK = add(add(r64(y),
                multiply(r64(b), multiply(cosTheta, sinX()))),
                multiply(r64(a), multiply(sinTheta, cosX()))
        );

        return paramPlot(
                vector(func("g", funcG), func("k", funcK)),
                vector(new Polynom(new int[0], new Element[0]),
                        multiply(r64(2), func("\\pi"))
                )
        );
    }

    /**
     * Построить сектор елипса.
     *
     * @param x      координата Х центра
     * @param y      координата Y центра
     * @param a      первая полуось
     * @param b      вторая полуось
     * @param start  угол начала сектора
     * @param end    угол конца сектора
     * @param rotate угол поворота сектора в грудусах
     * @return
     */
    public static F createSector(double x, double y, double a, double b, double start, double end, double rotate) {
        double theta1 = Math.toRadians(start);
        double theta2 = Math.toRadians(end);
        double thetaRotate = Math.toRadians(rotate);

        double xStart = a * Math.cos(theta1) + x;
        double yStart = b * Math.sin(theta1) + y;
        double xEnd = a * Math.cos(theta2) + x;
        double yEnd = b * Math.sin(theta2) + y;

        double[] rotatedStart = rotatePoint(x, y, xStart, yStart, thetaRotate);
        double[] rotatedEnd = rotatePoint(x, y, xEnd, yEnd, thetaRotate);

        F xs = vector(rotatedStart[0], x, rotatedEnd[0]);
        F ys = vector(rotatedStart[1], y, rotatedEnd[1]);

        return tablePlot(vector(xs, ys));
    }

    /**
     * Получить новые координаты точки (x y) после смещения на угол @theta относитеьно точки (x0 y0)
     *
     * @param x0    X точки центра вращения
     * @param y0    Y точки центра вращения
     * @param x     X точки вращения
     * @param y     Y точки вращения
     * @param theta угол вращения в грудусах
     * @return
     */
    private static double[] rotatePoint(double x0, double y0, double x, double y, double theta) {
        double xNorm = x - x0;
        double yNorm = y - y0;
        double xEnd = xNorm * Math.cos(theta) - yNorm * Math.sin(theta);
        double yEnd = yNorm * Math.cos(theta) + xNorm * Math.sin(theta);
        return new double[]{xEnd + x0, yEnd + y0};
    }
}
