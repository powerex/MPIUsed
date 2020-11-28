package com.mathpar.Graphic2D.paintElements._interfaces_not_implemented.Androshchuk;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;

import static com.mathpar.Graphic2D.paintElements.EngineImpl.helper.FileWriterHelper.writeFormulaToFile;

public class Tester {

    public static void main(String[] args) {
        Point a = new Point(8, 0);
        Point b = new Point(0, 0);
        Point c = new Point(0, 8);


        String triangleMathParFormula2 = CircleProcessor.getFormulaForCircleAndTriangle(a, b, c);
        writeFormulaToFile(triangleMathParFormula2, "circle.txt");
    }
}
