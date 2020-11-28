package com.mathpar.Graphic2D.paintElements.EngineImpl.component;

import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class Text implements GeometryVar {



    public Text(String text,Point start,int font_size){
        FontSize = font_size;
        StartPoint = start;
        TEXT = text;
    }

    private int FontSize;
    private Point StartPoint;
    @Getter
    private String TEXT;




    @Setter
    @Getter
    private boolean displayed;

    @Override
    public ArgType getType() {
        return ArgType.TEXT;
    }

    @Override
    public String draw() {
        return "\\textPlot(["+TEXT+", "+FontSize+", "+StartPoint.getX()+", "+StartPoint.getY() +"])";
    }

    public static void main(String[] args) {
        Text t = new Text("Proverka",new Point(1,1),15);
        System.out.println(t.draw());
    }
}
