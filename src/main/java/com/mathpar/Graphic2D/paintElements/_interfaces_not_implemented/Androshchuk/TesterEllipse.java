package com.mathpar.Graphic2D.paintElements._interfaces_not_implemented.Androshchuk;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Ellipse;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;

import static com.mathpar.Graphic2D.paintElements.EngineImpl.helper.FileWriterHelper.writeFormulaToFile;

public class TesterEllipse {
    public static void main(String[] args) {
        String fileName = "ellipseClass.txt";
        Ellipse ellipse = new Ellipse(1, 5, new Point(3, 1));
        String ellipseFormula = ellipse.toUiFormula();
        writeFormulaToFile(ellipseFormula, fileName);
        System.out.println(ellipse.toUiFormula());

    }
}
