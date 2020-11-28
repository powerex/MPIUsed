package com.mathpar.Graphic2D.paintElements.EngineImpl.operator;

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

public class NormalFromPointToLine implements Operator {

    private static final List<ArgType> argTypes = Collections.unmodifiableList(Arrays.asList(
            ArgType.POINT, ArgType.LINE
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
        if(line.isPointOnThisLine(point))
            throw new OperatorException("THIS POINT IS LOCATED ON THIS LINE");
        if(line.getPoint1().getY()==line.getPoint2().getY()){ //paralel to OX
            return new Point(point.getX(),line.getPoint1().getY());
        }
        double k = line.getK();
        if(k==Double.POSITIVE_INFINITY)
            return new Point(line.getPoint1().getX(),point.getY()); //paralel to OY
        double b = line.getB();
        double x = (point.getX()+ k*(point.getY()+b))/(k+1);
        double y = k * x + b;

        return new Point(x,y);
    }
}
