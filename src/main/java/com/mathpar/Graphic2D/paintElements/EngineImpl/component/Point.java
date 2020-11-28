package com.mathpar.Graphic2D.paintElements.EngineImpl.component;

import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import lombok.Getter;
import lombok.Setter;

public class Point implements GeometryVar {
    public double x;
    public double y;

    @Setter
    @Getter
    private boolean displayed;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getLengthToPoint(Point p){
        return Math.sqrt(Math.pow(x-p.x,2)+Math.pow(y-p.y,2));
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public String draw() {
        return String.format("\\pointsPlot([[%s],[%s]])", this.x, this.y);
    }

    public double[] getCoordinates(Point point) {
        double[] coords = new double[2];
        coords[0] = point.x;
        coords[1] = point.y;
        return coords;
    }

    @Override
    public ArgType getType() {
        return ArgType.POINT;
    }

}
