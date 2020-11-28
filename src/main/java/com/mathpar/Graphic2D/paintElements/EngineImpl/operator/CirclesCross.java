package com.mathpar.Graphic2D.paintElements.EngineImpl.operator;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Circle;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.InfiniteLine;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Line;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;
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

public class CirclesCross implements Operator {

    private static final List<ArgType> argTypes = Collections.unmodifiableList(Arrays.asList(
            ArgType.CIRCLE, ArgType.CIRCLE
    ));

    @Override
    public List<ArgType> getArgTypes() {
        return argTypes;
    }

    @Override
    public String getFullName() {
        return "circlesCross";
    }


    //TODO return smth
    @Override
    public GeometryVar apply(List<GeometryVar> args, ScreenParams screen, Ring ring) throws OperatorException {
        ListIterator<GeometryVar> argsIt = args.listIterator();
        Circle circle1 = (Circle) argsIt.next();
        Circle circle2 = (Circle) argsIt.next();
        double distanceBetweenCentres = circle1.getCenter().getLengthToPoint(circle2.getCenter());

        if(distanceBetweenCentres>circle1.getRadius()+circle2.getRadius())
            throw new OperatorException("Circles don`t cross");
        else if(Math.abs(circle1.getRadius()-circle2.getRadius())>distanceBetweenCentres)
            throw new OperatorException("Circle in circle");
        else if(distanceBetweenCentres == circle1.getRadius()+circle2.getRadius())
            return null;
        else{

            double delta_x = circle1.getCenter().getX();
            double delta_y = circle1.getCenter().getY();

            double a = circle2.getCenter().getX()-delta_x;
            double b = circle2.getCenter().getY()-delta_y;
            double z = (Math.pow(circle2.getRadius(),2) - Math.pow(circle1.getRadius(),2) - Math.pow(a,2) - Math.pow(b,2))/(2*b);

            circle1 = new Circle(circle1.getRadius());

            double[] xs = quadraticEquation((1+Math.pow(a/b,2)),a*z/b, Math.pow(z,2));

            Point[] res = new Point[]{new Point(xs[0]+delta_x,Math.sqrt(Math.pow(circle1.getRadius(),2)-Math.pow(xs[0],2))), new Point(xs[1]+delta_x,Math.sqrt(Math.pow(circle1.getRadius(),2)-Math.pow(xs[1],2)))};

            return null;
        }
    }

    private static double[] quadraticEquation(double a, double b, double c) throws ArithmeticException {
        double D = b * b - 4 * a * c;
        if (D < 0)
            throw new ArithmeticException();
        double d = Math.sqrt(D);
        return new double[]{(-b + d) / 2 * a, (-b - d) / 2 * a};
    }


    public static void main(String[] args){

    }
}
