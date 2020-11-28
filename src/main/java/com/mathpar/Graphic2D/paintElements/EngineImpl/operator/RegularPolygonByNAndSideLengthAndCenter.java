package com.mathpar.Graphic2D.paintElements.EngineImpl.operator;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.IntNum;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.RealNum;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.RegularPolygon;
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

public class RegularPolygonByNAndSideLengthAndCenter implements Operator {

    private static final List<ArgType> argTypes = Collections.unmodifiableList(Arrays.asList(
            ArgType.INT, ArgType.REAL_NUM, ArgType.POINT
    ));

    private static final String name = "RegularPolygon";

    @Override
    public List<ArgType> getArgTypes() {
        return argTypes;
    }

    @Override
    public String getFullName() {
        return name;
    }

    @Override
    public GeometryVar apply(List<GeometryVar> args, ScreenParams screen, Ring ring) throws OperatorException{
        ListIterator<GeometryVar> argsIt = args.listIterator();
        int n = ((IntNum)argsIt.next()).getValue();
        if(n<3)
            throw new OperatorException("COUNT OF ANGLES MUST BE GREATER THEN 2");
        double len = ((RealNum)argsIt.next()).getValue();
        if(len<=0)
            throw new OperatorException("LENGTH OF SIDE MUST ME GREATER THEN 0");
        return new RegularPolygon(n,len, (Point)argsIt.next());
    }

}
