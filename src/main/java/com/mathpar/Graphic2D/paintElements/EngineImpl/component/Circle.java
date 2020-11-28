package com.mathpar.Graphic2D.paintElements.EngineImpl.component;

import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import lombok.Getter;
import lombok.Setter;

public class Circle implements GeometryVar {

    private Point center;
    private double radius;

    @Setter
    @Getter
    private boolean displayed;

    public Circle(double radius, Point center) {
        this.center = center;
        this.radius = radius;
    }

    public Circle(double radius) {
        this.center = new Point(0, 0);
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "center=" + center +
                ", radius=" + radius +
                '}';
    }

    @Override
    public String draw(){
        return "\\paramPlot([" +
                "((" + center.getX() + " + " + radius + " * 1 * \\cos(x)))," +
                "((" + center.getY() + " + " + radius + " * 1 * \\sin(x)))]," +
                "[0,2 * \\pi])";
    }

    public static String draw(Circle circle){
        return circle.draw();
    }

    @Override
    public ArgType getType() {
        return ArgType.CIRCLE;
    }
}
