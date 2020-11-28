package com.mathpar.Graphic2D.paintElements.EngineImpl.component;

import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import lombok.Getter;
import lombok.Setter;

public class Line implements GeometryVar {
    public Point point1;
    public Point point2;

    @Setter
    @Getter
    private TypeOfLine typeOfLine = TypeOfLine.LINE;
    @Getter
    private double k;
    @Getter
    private double b;

    @Setter
    @Getter
    private boolean displayed;

    public Line(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
        calculateKAndB();
    }

    public Point getPoint1() {
        return point1;
    }

    public void setPoint1(Point point1) {
        this.point1 = point1;
        calculateKAndB();
    }

    public Point getPoint2() {
        return point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
        calculateKAndB();
    }

    public boolean isPointOnThisLine(Point p){
        double x = p.getX();
        double y = p.getY();

        return y == getK()*x+getB();
    }

    private void calculateKAndB(){
        if(point2.getX()==point1.getX()){
            this.k = (point2.getY()-point1.getY())/(point2.getX()-point1.getX());
            this.b = (point1.getY()*point2.getX() - point2.getY()*point1.getY())/(point2.getX()-point1.getX());
        }
        else{
            this.k = Double.POSITIVE_INFINITY;
            this.b = point1.getX();
        }
    }

    @Override
    public String toString() {
        return "Point{" +
                "point1=" + point1 +
                "point2=" + point2 +
                '}';
    }

    @Override
    public ArgType getType() {
        return ArgType.LINE;
    }

    @Override
    public String draw() {
        return String.format("\\tablePlot([[%s,%s],[%s,%s]])", this.point1.getX(), this.point2.getX(), this.point1.getY(), this.point2.getY());
    }

    enum TypeOfLine {
        LINE,
        INFINITE_LINE,
        RAY
    }
}
