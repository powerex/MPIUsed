package com.mathpar.Graphic2D.paintElements.EngineImpl.operator;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.InfiniteLine;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Ray;
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

public class RayByTwoPoints implements Operator {

    private static final List<ArgType> argTypes = Collections.unmodifiableList(Arrays.asList(
            ArgType.POINT, ArgType.POINT
    ));

    private static final String name = "Ray";


    @Override
    public List<ArgType> getArgTypes() {
        return argTypes;
    }

    @Override
    public String getFullName() {
        return name;
    }

    @Override
    public GeometryVar apply(List<GeometryVar> args, ScreenParams screen, Ring ring) throws OperatorException {
        ListIterator<GeometryVar> argsIt = args.listIterator();
        Point point1 = (Point) argsIt.next();
        Point point2 = (Point) argsIt.next();

        if(point1.equals(point2))
            throw new OperatorException("Points are equal");

        return new Ray(point1, point2, screen);
    }

}
