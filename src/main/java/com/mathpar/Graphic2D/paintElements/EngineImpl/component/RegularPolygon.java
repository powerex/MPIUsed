package com.mathpar.Graphic2D.paintElements.EngineImpl.component;


import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import com.mathpar.Graphic2D.paintElements._interfaces_not_implemented.Dymchenko.Polygon;
import lombok.Getter;
import lombok.Setter;

public class RegularPolygon implements GeometryVar  {

    public RegularPolygon(int n, double sideLength){
        this(n, sideLength, new Point(0,0));
    }

    public RegularPolygon(int N, double sideLength, Point center){
        this.sideLength = sideLength;
        this.center = center;

        Point[] points = new Point[N];

        double R = sideLength/(2*Math.sin(Math.PI/N));
        double phi0 = 0;

        for(int i = 0; i < N; i++){
            points[i] = new Point(center.x+R*Math.sin(phi0+2*Math.PI*i/N),center.y+R*Math.cos(phi0+2*Math.PI*i/N));
        }

        polygon = new Polygon(points);

    }

    private Polygon polygon;
    private double sideLength;
    private Point center;

    @Setter
    @Getter
    private boolean displayed;

    public double getSideLength() {
        return sideLength;
    }

    public Point getCenter() {
        return center;
    }

    public Point[] getPoints(){ return polygon.getPoints(); }

    @Override
    public ArgType getType() {
        return ArgType.REGULAR_POLYGON;
    }
    @Override
    public String draw() {

        String str = "";
        Point[] points = polygon.getPoints();
        for(int i = 0; i < points.length+1; ++i)
        {
            str += points[i%points.length].x + (i == points.length  ? "],[" : ", ");
        }
        for(int i = 0; i < points.length+1; ++i)
        {
            str += points[i%points.length].y + (i == points.length  ? "" : ", ");
        }

        String code = "\\tablePlot([[" + str +  "]]);";


        return code;
    }

    public static void main(String[] args) {

        RegularPolygon rp = new RegularPolygon(3,3);
        System.out.println(rp.draw());
        rp = new RegularPolygon(8,2,new Point(1,1));
        System.out.println(rp.draw());


    }

}
