package com.mathpar.Graphic2D.paintElements.EngineImpl.component;

import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import lombok.Getter;
import lombok.Setter;

public class Ellipse implements GeometryVar {
    @Setter
    @Getter
    private boolean displayed;

    private Point center;
    private double width;
    private double height;

    public Ellipse(double width, double height, Point center) {
        this.center = center;
        this.width = width;
        this.height = height;
    }

    public Ellipse(double width, double height) {
        this.center = new Point(0, 0);
        this.width = width;
        this.height = height;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Elipse{" +
                "center=" + center +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
    @Override
    public String draw(){
        return this.toUiFormula();
    }
    public String toUiFormula() {
        return "\\paramPlot([" +
                "((" + center.getX() + " + " + width + " * 1 * \\cos(x)))," +
                "((" + center.getY() + " + " + height + " * 1 * \\sin(x)))]," +
                "[0,2 * \\pi])";
    }

    public static String toUiFormula(Ellipse ellipse) {
        return ellipse.toUiFormula();
    }
    @Override
    public ArgType getType() {
        return ArgType.ELLIPSE;
    }
}
