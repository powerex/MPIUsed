package com.mathpar.Graphic2D.paintElements.EngineImpl.operator;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.RealNum;
import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.Operator;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.model.ScreenParams;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Ellipse;
import com.mathpar.number.Ring;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class EllipseByWidthAndHeight implements Operator {

    private static final List<ArgType> argTypes = Collections.unmodifiableList(Arrays.asList(
            ArgType.REAL_NUM, ArgType.REAL_NUM
    ));

    private static final String name = "Ellipse";


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
        RealNum width = (RealNum) argsIt.next();
        RealNum height = (RealNum) argsIt.next();

        return new Ellipse(width.getValue(), height.getValue());
    }
}
