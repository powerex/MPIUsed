package com.mathpar.Graphic2D.paintElements._interfaces_not_implemented.bazalytskyi;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;
import com.mathpar.Graphic2D.paintElements.EngineImpl.helper.FileWriterHelper;

public class PointTester {
    public static void main(String[] args) {
        Point point = new Point(1, 1);
        String pointFormula = point.draw();
        FileWriterHelper.writeFormulaToFile(pointFormula, "pointFormula.txt");
    }
}
