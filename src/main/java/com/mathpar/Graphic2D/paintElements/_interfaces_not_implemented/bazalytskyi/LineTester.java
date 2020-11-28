package com.mathpar.Graphic2D.paintElements._interfaces_not_implemented.bazalytskyi;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Line;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;
import com.mathpar.Graphic2D.paintElements.EngineImpl.helper.FileWriterHelper;

public class LineTester {
    public static void main(String[] args) {
        Point point1 = new Point(1, 0);
        Point point2 = new Point(0, 8);
        Line line = new Line(point1, point2);
        String lineFormula = line.draw();
        FileWriterHelper.writeFormulaToFile(lineFormula, "lineFormula.txt");
    }
}
