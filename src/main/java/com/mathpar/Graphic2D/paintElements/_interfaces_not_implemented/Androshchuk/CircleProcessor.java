package com.mathpar.Graphic2D.paintElements._interfaces_not_implemented.Androshchuk;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;

public class CircleProcessor {

    static String getFormulaForCircleAndTriangle(Point a, Point b, Point c) {
        double[] bisector1 = perpendicularBisectorFromLine(a, b);
        double[] bisector2 = perpendicularBisectorFromLine(b, c);

        Point center = lineIntersection(bisector1, bisector2);
        double radius = distanceBetweenPoints(center, a);

        System.out.println(center);
        System.out.println(radius);

        double x1 = a.getX(), x2 = b.getX(), x3 = c.getX();
        double y1 = a.getY(), y2 = b.getY(), y3 = c.getY();

        String triangleMathParFormula = "\\showPlots(" +
                "[\\tablePlot([[" + x1 + "," + y1 + "],[" + x2 + "," + y2 + "]])," +
                "\\tablePlot([[" + x2 + "," + y2 + "],[" + x3 + "," + y3 + "]])," +
                "\\tablePlot([[" + x3 + "," + y3 + "],[" + x1 + "," + y1 + "]])]" +
                ")";

        System.out.println(triangleMathParFormula);

        String circleMathParFormula = "\\paramPlot([((" + center.getX() + " + " + radius + " * 1 * \\cos(x))- 2 * 0 * \\sin(x)),((" + center.getY() + " + " + radius + " * 1 * \\sin(x))+ 5 * 0 * \\cos(x))],[0,2 * \\pi])";

        System.out.println(circleMathParFormula);
        String triangleMathParFormula2 = "\\showPlots(" +
                "[\\tablePlot([[" + x1 + "," + y1 + "],[" + x2 + "," + y2 + "]])," +
                "\\tablePlot([[" + x2 + "," + y2 + "],[" + x3 + "," + y3 + "]])," +
                "\\tablePlot([[" + x3 + "," + y3 + "],[" + x1 + "," + y1 + "]])," +
                circleMathParFormula + "]" +
                ")";

        System.out.println(triangleMathParFormula2);
        return triangleMathParFormula2;
    }

    public static Point lineIntersection(double[] bisector1, double[] bisector2) {

        double a1 = bisector1[0];
        double b1 = bisector1[1];
        double c1 = bisector1[2];

        double a2 = bisector2[0];
        double b2 = bisector2[1];
        double c2 = bisector2[2];

        double determinant = a1 * b2 - a2 * b1;

        if (determinant == 0) {
            return new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        } else {
            double x = (b2 * c1 - b1 * c2) / determinant;
            double y = (a1 * c2 - a2 * c1) / determinant;

            return new Point(x, y);
        }
    }

    public static double distanceBetweenPoints(Point pointA, Point pointB) {
        double x1 = pointA.x;
        double x2 = pointB.x;
        double y1 = pointA.y;
        double y2 = pointB.y;
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static double[] perpendicularBisectorFromLine(Point pointA, Point pointB) {
        double x = (pointA.x + pointB.x) / 2;
        double y = (pointA.y + pointB.y) / 2;
        double[] parameters = lineFromPoints(pointA, pointB);
        double a = parameters[0];
        double b = parameters[1];
        double c = parameters[2];
        double d = -b * x + a * y;
        return new double[]{-b, a, d};
    }

    static double[] lineFromPoints(Point P, Point Q) {
        double a = Q.y - P.y;
        double b = P.x - Q.x;
        double c = a * (P.x) + b * (P.y);
        return new double[]{a, b, c};
    }
}
