package com.mathpar.Graphic2D.paintElements.EngineImpl.operator;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.IntNum;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;
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

public class RegularPolygonByN implements Operator {

    private static final List<ArgType> argTypes = Collections.unmodifiableList(Arrays.asList(
            ArgType.INT
    ));

    @Override
    public List<ArgType> getArgTypes() {
        return argTypes;
    }

    @Override
    public String getFullName() {
        return "RegularPolygon";
    }

    @Override
    public GeometryVar apply(List<GeometryVar> args, ScreenParams screen, Ring ring) throws OperatorException {
        int n = ((IntNum)args.listIterator().next()).getValue();
        if(n<3)
            throw new OperatorException("COUNT OF ANGLES MUST BE GREATER THEN 2");
        return new RegularPolygon(n,2*((0.25)*Math.min(screen.getXMax()-screen.getXMin(),screen.getYMax()-screen.getYMin()))*Math.sin(2*Math.PI/n),new Point((screen.getXMax()+screen.getXMin())/2,(screen.getYMax()+ screen.getYMin())/2));
    }
}
