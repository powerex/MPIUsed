package com.mathpar.Graphic2D.Element2D.components;

import com.mathpar.Graphic2D.ReadFromFile;
import com.mathpar.func.F;
import com.mathpar.number.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static com.mathpar.Graphic2D.Element2D.PlanimetriaUtils.*;

public class CircleComponent implements PlanimetriaComponent {
    public String getName() {
        return "ОКРУЖНОСТЬ";
    }

    public UnaryOperator<String> parseExpression() {
        return expr -> {
            List<Element> plots = new ArrayList<>();

            double x = ReadFromFile.Xvis;
            double y = ReadFromFile.Yvis;
            double radius = ReadFromFile.RadXvis;
            String label = ReadFromFile.Label;
            double tanDeg = ReadFromFile.Avis;
            double tanLen = ReadFromFile.Lvis;
            double secA = ReadFromFile.startArc;
            double secB = ReadFromFile.Arc;
            if (tanDeg > 0) {
                plots.add(createTangent(x, y, radius, tanDeg, tanLen));
            }
            if (secA != secB) {
                plots.add(EllipseComponent.createSector(x, y, radius, radius, secA, secB, 0));
            }
            plots.add(EllipseComponent.createEllipsis(x, y, radius, radius, 0));
            plots.add(createPointF(x, y, label));

            String result = plots.toString();
            return result.substring(1, result.length() - 1);
        };
    }

    /**
     * Построение касательной к окружости.
     *
     * @param x      Х центра окружности
     * @param y      Y центра окружности
     * @param r      радиус окружости
     * @param deg    угол нормали касательной в градусах
     * @param length длина касательной
     * @return объект tablePlot
     */
    public static F createTangent(double x, double y, double r, double deg, double length) {
        double xTan = r * Math.cos(Math.toRadians(deg)) + x;
        double yTan = r * Math.sin(Math.toRadians(deg)) + y;
        double k = (yTan - y) / (xTan - x);
        double kTan = -(1.0 / k);

        double x1 = (length / 2) * Math.cos(Math.atan(kTan)) + xTan;
        double x2 = (length / 2) * Math.cos(Math.atan(kTan) + Math.PI) + xTan;
        double y1 = (length / 2) * Math.sin(Math.atan(kTan)) + yTan;
        double y2 = (length / 2) * Math.sin(Math.atan(kTan) + Math.PI) + yTan;

        return tablePlot(createIntervalF(x1, x2, y1, y2));
    }
}
