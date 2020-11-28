package com.mathpar.Graphic2D.paintElements.EngineImpl.component;

import com.mathpar.Graphic2D.paintElements.GeometryEngine.model.ScreenParams;

public class Ray extends Line{
    public Ray(Point start, Point destination , ScreenParams sp) {
        super(start, destination);
        this.sp = sp;
        setTypeOfLine(TypeOfLine.RAY);
    }

    ScreenParams sp;


    @Override
    public String draw() {
        if(getPoint1().getX()<getPoint2().getX())
            return generateCodeForDraw(getPoint1().getX(),sp.getXMax(),getPoint1().y,getK()*sp.getXMax()+getB() );
        else if(getPoint1().getX()>getPoint2().getY())
            return generateCodeForDraw(sp.getXMin(), getPoint1().getX(),getK()*sp.getXMin(), getPoint1().getY());
        else
            if(getPoint1().getY()<getPoint2().getY())
                return  generateCodeForDraw(getPoint1().getX(),getPoint2().getX(),getPoint1().getY(),sp.getYMax());
            else
                return generateCodeForDraw(getPoint1().getX(),getPoint2().getX(),getPoint1().getY(),sp.getYMin());

    }

    private String generateCodeForDraw(double x1, double x2, double y1, double y2){
        return String.format("\\tablePlot([[%s,%s],[%s,%s]])", x1,  x2, y1, y2);
    }
}
