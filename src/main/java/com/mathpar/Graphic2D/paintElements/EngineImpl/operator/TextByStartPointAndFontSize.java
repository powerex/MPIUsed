package com.mathpar.Graphic2D.paintElements.EngineImpl.operator;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.IntNum;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.STRING;
import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Text;
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

public class TextByStartPointAndFontSize implements Operator {
    private static final List<ArgType> argTypes = Collections.unmodifiableList(Arrays.asList(
            ArgType.TEXT, ArgType.POINT, ArgType.INT
    ));

    @Override
    public List<ArgType> getArgTypes() {
        return argTypes;
    }

    @Override
    public String getFullName() {
        return "Text";
    }

    @Override
    public GeometryVar apply(List<GeometryVar> args, ScreenParams screen, Ring ring) throws OperatorException {
        ListIterator<GeometryVar> iterator = args.listIterator();

        String text = ((STRING)iterator.next()).getValue();
        Point s = (Point) iterator.next();
        int font_size = ((IntNum)iterator.next()).getValue();
        if(font_size<=0)
            throw new OperatorException("Font size must be greater then 0");
        return new Text(text,s,font_size);
    }
}
