package com.mathpar.Graphic2D.paintElements._interfaces_not_implemented.Androshchuk;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Circle;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;

import static com.mathpar.Graphic2D.paintElements.EngineImpl.helper.FileWriterHelper.writeFormulaToFile;

public class TesterCircle {
    public static void main(String[] args) {
        String fileName = "circleClass.txt";
        Circle circle = new Circle(10);
        Circle circle2 = new Circle(5, new Point(4, 5));
        String circleFormula = circle.draw();
        String circleFormula2 = circle2.draw();
        writeFormulaToFile(circleFormula + "\n" + circleFormula2, fileName);
        System.out.println(circle.draw());
        System.out.println(circle2.draw());

    }
}
