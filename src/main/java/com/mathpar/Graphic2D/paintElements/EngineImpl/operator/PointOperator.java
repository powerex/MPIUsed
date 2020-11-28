package com.mathpar.Graphic2D.paintElements.EngineImpl.operator;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.RealNum;
import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.Operator;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.model.ScreenParams;
import com.mathpar.number.Ring;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class PointOperator implements Operator {

    private static final List<ArgType> argTypes = Collections.unmodifiableList(Arrays.asList(
            ArgType.REAL_NUM, ArgType.REAL_NUM
    ));

    private static final String name = "Point";


    @Override
    public List<ArgType> getArgTypes() {
        return argTypes;
    }

    @Override
    public String getFullName() {
        return name;
    }

    @Override
    public GeometryVar apply(List<GeometryVar> args, ScreenParams screen, Ring ring) {
        ListIterator<GeometryVar> argsIt = args.listIterator();
        // args are already validated, so it's ok to keep using iterator to get all args
        RealNum x = (RealNum) argsIt.next();
        RealNum y = (RealNum) argsIt.next();

        return new Point(x.getValue(), y.getValue());
    }
}
