package com.mathpar.Graphic2D.paintElements.EngineImpl.operator;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.RealNum;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.RegularPolygon;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Triangle;
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

public class EquilateralTriangleBySideAndCentre implements Operator {

    private static final List<ArgType> argTypes = Collections.unmodifiableList(Arrays.asList(
            ArgType.REAL_NUM, ArgType.POINT
    ));

    @Override
    public List<ArgType> getArgTypes() {
        return argTypes;
    }

    @Override
    public String getFullName() {
        return "EquilateralTriangle";
    }

    @Override
    public GeometryVar apply(List<GeometryVar> args, ScreenParams screen, Ring ring) throws OperatorException {
        double side;
        Point center;
        ListIterator<GeometryVar> iterator = args.listIterator();
        side = ((RealNum)iterator.next()).getValue();
        center = (Point) iterator.next();
        if(side<=0)
            throw new OperatorException("SIDE OF TRIANGLE CAN`T LOWER THEN 0");

        RegularPolygon rp = new RegularPolygon(3,side,center);
        Point[] vertexes = rp.getPoints();
        return new Triangle(vertexes[0],vertexes[1],vertexes[2]);
    }
}
