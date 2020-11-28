package com.mathpar.Graphic2D.paintElements.EngineImpl.component;

import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.model.ScreenParams;

public class InfiniteLine extends Line {

    private ScreenParams sp;


    public InfiniteLine(Point point1, Point point2, ScreenParams sp) {
        super(point1, point2);
        this.sp = sp;
        setTypeOfLine(TypeOfLine.INFINITE_LINE);
    }
    public InfiniteLine(double k, double b, ScreenParams sp){
        super(new Point(0,b), new Point(1,k+b));
        this.sp = sp;
        setTypeOfLine(TypeOfLine.INFINITE_LINE);
    }

    @Override
    public ArgType getType() {
        return ArgType.LINE;
    }

    @Override
    public String draw() {
        return String.format("\\tablePlot([[%s,%s],[%s,%s]])", sp.getXMin(), getK()*sp.getXMin()+getB(), sp.getXMax(), getK()*sp.getXMax()+getB());
    }
}
