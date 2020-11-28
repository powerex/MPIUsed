package com.mathpar.Graphic2D.paintElements._interfaces_not_implemented.Zhyrkova;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;

public class Runner {
    public static void main(String[] args) {
        Point p1 = new Point(1, 1);
        Point p2 = new Point(1, 5);
        Point p3 = new Point(8, 1);

        double length1 = 4;
        double length2 = 5;
        double length3 = 3;

        Triangle t1 = new Triangle();
        System.out.println(t1.getMathparCode(p1, p2, p3));

        Triangle t2 = new Triangle();
        System.out.println(t2.getMathparCode(length1, length2, length3));
        System.out.println(t2.getMathparCode(length3, length3, length3));
    }
}
