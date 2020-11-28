package com.mathpar.Graphic2D.paintElements.EngineImpl.operator;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Circle;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.RealNum;
import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.Operator;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.model.ScreenParams;
import com.mathpar.number.Ring;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class CircleByRadius implements Operator {

    private static final List<ArgType> argTypes = Collections.unmodifiableList(Collections.singletonList(
            ArgType.REAL_NUM
    ));

    private static final String name = "Circle";


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
        RealNum radius = (RealNum) argsIt.next();

        return new Circle(radius.getValue());
    }

}
