package com.mathpar.Graphic2D.paintElements.EngineImpl.operator;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Line;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.RealNum;
import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.error.OperatorException;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.Operator;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.model.ScreenParams;
import com.mathpar.number.Ring;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

//todo
public class NormalFromPointToLineByLength implements Operator {
    private static final List<ArgType> argTypes = Collections.unmodifiableList(Arrays.asList(
            ArgType.POINT, ArgType.LINE, ArgType.REAL_NUM
    ));

    @Override
    public List<ArgType> getArgTypes() {
        return argTypes;
    }

    @Override
    public String getFullName() {
        return "normal";
    }

    @Override
    public GeometryVar apply(List<GeometryVar> args, ScreenParams screen, Ring ring) throws OperatorException {
        ListIterator<GeometryVar> iterator = args.listIterator();

        Point point = (Point) iterator.next();
        Line line = (Line) iterator.next();
        double length = ((RealNum) iterator.next()).getValue();
        if (length == 0)
            throw new OperatorException("LENGTH CAN`T BO 0");
        if (line.getPoint1().getY() == line.getPoint2().getY()) { //paralel to OX
            return new Point(point.getX(), point.getY() - length);
        }
        double k = line.getK();
        if(k==Double.POSITIVE_INFINITY)
            return new Point(point.getX() + length, point.getY()); //paralel to OY
        double b = line.getB();
        double x;
        double y;
        if (line.isPointOnThisLine(point)) {
            try {
                double[] xs = quadraticEquation(1 + k * k, -2 * (point.getX() + k * point.getY() - 2 * k * b), Math.pow(point.getY() - b, 2) - length * length);
                x = Math.sqrt(Math.pow(xs[0]-point.getX(),2)+Math.pow(k * xs[0] + b - point.getY(),2))==length?xs[0]:xs[1];
            } catch (Exception e) {
                throw new OperatorException("COMPUTING ERROR");
            }
        } else {
            x = (point.getX() + k * (point.getY() + b)) / (k + 1);
        }
        y = k * x + b;
        return new Point(x, y);
    }

    private static double[] quadraticEquation(double a, double b, double c) throws ArithmeticException {
        double D = b * b - 4 * a * c;
        if (D < 0)
            throw new ArithmeticException();
        double d = Math.sqrt(D);
        return new double[]{(-b + d) / 2 * a, (-b - d) / 2 * a};
    }


    public static void main(String[] args) throws OperatorException {
        Point point = new Point(1, 2);
        Line line = new Line(new Point(0, 1), new Point(1, 1));

        NormalFromPointToLineByLength nc = new NormalFromPointToLineByLength();
        List<GeometryVar> arrgs = Collections.unmodifiableList(Arrays.asList(
                point, line, new RealNum(0.5)
        ));

        System.out.println(nc.apply(arrgs, new ScreenParams(), null).draw());
    }
}

