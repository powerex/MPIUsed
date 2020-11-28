package com.mathpar.Graphic2D.Element2D.components;

import com.mathpar.Graphic2D.ReadFromFile;

import java.util.function.UnaryOperator;

import static com.mathpar.Graphic2D.Element2D.PlanimetriaUtils.createIntervalF;
import static com.mathpar.Graphic2D.Element2D.PlanimetriaUtils.tablePlot;

public class LineSegmentComponent implements PlanimetriaComponent {
    @Override
    public String getName() {
        return "ОТРЕЗОК";
    }

    @Override
    public UnaryOperator<String> parseExpression() {
        return expr -> {
            double x1 = ReadFromFile.Xvis;
            double x2 = ReadFromFile.Xvis1;
            double y1 = ReadFromFile.Yvis;
            double y2 = ReadFromFile.Yvis1;
            return tablePlot(createIntervalF(x1, x2, y1, y2)).toString();
        };
    }
}
