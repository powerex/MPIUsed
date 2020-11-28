package com.mathpar.Graphic2D.paintElements.EngineImpl.component;

import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import com.mathpar.Graphic2D.paintElements._interfaces_not_implemented.Androshchuk.CircleProcessor;
import lombok.Getter;
import lombok.Setter;

public class Triangle implements GeometryVar {
    private Point vertexA;
    private Point vertexB;
    private Point vertexC;

    @Setter
    @Getter
    private boolean displayed;

    public Triangle(Point vertexA, Point vertexB, Point vertexC) {
        this.vertexA = vertexA;
        this.vertexB = vertexB;
        this.vertexC = vertexC;
    }

    private double getLengthOfSide(Point vertex1, Point vertex2) {
        double x1 = vertex1.getX();
        double x2 = vertex2.getX();
        double y1 = vertex1.getY();
        double y2 = vertex2.getY();
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    private Point getIncenterOfInscribed() {
        double x1 = this.vertexA.getX(), x2 = this.vertexB.getX(), x3 = this.vertexC.getX();
        double y1 = this.vertexA.getY(), y2 = this.vertexB.getY(), y3 = this.vertexC.getY();
        double a = getLengthOfSide(vertexB, vertexC), b = getLengthOfSide(vertexA, vertexC), c = getLengthOfSide(vertexA, vertexB);

        // Formula to calculate in-center
        double x
                = (a * x1 + b * x2 + c * x3) / (a + b + c);
        double y
                = (a * y1 + b * y2 + c * y3) / (a + b + c);

        return new Point(x, y);
    }

    private double getRadiusOfInscribedCircle() {
        double a = getLengthOfSide(vertexA, vertexB), b = getLengthOfSide(vertexB, vertexC), c = getLengthOfSide(vertexC, vertexA);
        double p = (a + b + c) / 2;
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));

        return 2 * s / (a + b + c);
    }

    @Override
    public String draw() {
        double x1 = this.vertexA.getX(), x2 = this.vertexB.getX(), x3 = this.vertexC.getX();
        double y1 = this.vertexA.getY(), y2 = this.vertexB.getY(), y3 = this.vertexC.getY();

        return "\\showPlots(" +
                "[\\tablePlot([[" + x1 + "," + y1 + "],[" + x2 + "," + y2 + "]])," +
                "\\tablePlot([[" + x2 + "," + y2 + "],[" + x3 + "," + y3 + "]])," +
                "\\tablePlot([[" + x3 + "," + y3 + "],[" + x1 + "," + y1 + "]])" +
                "])";
    }

    public Circle getInscribedCircle() {
        return new Circle(getRadiusOfInscribedCircle(), getIncenterOfInscribed());
    }

    public Circle getCircumscribedCircle() {
        Point center = getCenterOfCircumscribedCircle();
        return new Circle(getRadiusOfCircumscribedCircle(center), getCenterOfCircumscribedCircle());
    }

    public Point getCenterOfCircumscribedCircle() {
        double[] bisector1 = CircleProcessor.perpendicularBisectorFromLine(vertexA, vertexB);
        double[] bisector2 = CircleProcessor.perpendicularBisectorFromLine(vertexB, vertexC);
        return CircleProcessor.lineIntersection(bisector1, bisector2);
    }

    double getRadiusOfCircumscribedCircle(Point center) {
        return CircleProcessor.distanceBetweenPoints(center, vertexA);
    }

    public String getTriangleAndInscribedCircleFormula() {
        double x1 = this.vertexA.getX(), x2 = this.vertexB.getX(), x3 = this.vertexC.getX();
        double y1 = this.vertexA.getY(), y2 = this.vertexB.getY(), y3 = this.vertexC.getY();

        String circleMathParFormula = getInscribedCircle().draw();

        return String.format(
                "\\showPlots(" +
                        "[\\tablePlot([[" + x1 + "," + y1 + "],[" + x2 + "," + y2 + "]])," +
                        "\\tablePlot([[" + x2 + "," + y2 + "],[" + x3 + "," + y3 + "]])," +
                        "\\tablePlot([[" + x3 + "," + y3 + "],[" + x1 + "," + y1 + "]])," +
                        "%s" +
                        "])", circleMathParFormula);
    }

    @Override
    public ArgType getType() {
        return ArgType.TRIANGLE;
    }
}
