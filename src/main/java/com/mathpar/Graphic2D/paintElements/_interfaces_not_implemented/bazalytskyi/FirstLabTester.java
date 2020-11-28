package com.mathpar.Graphic2D.paintElements._interfaces_not_implemented.bazalytskyi;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Triangle;
import com.mathpar.Graphic2D.paintElements.EngineImpl.helper.FileWriterHelper;

public class FirstLabTester {

    public static void main(String[] args) {
        Point a = new Point(0, 1.5);
        Point b = new Point(1.5, 6);
        Point c = new Point(6, 0);

        Triangle triangle = new Triangle(a, b, c);
        String triangleAndInscribedCircleFormula = triangle.getTriangleAndInscribedCircleFormula();
        FileWriterHelper.writeFormulaToFile(triangleAndInscribedCircleFormula, "triangleAndInscribedCircleFormula.txt");
    }
}
