package com.mathpar.Graphic2D.paintElements._interfaces_not_implemented.Dymchenko;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;

public class Main {

	public static void main(String[] args) {
		
		Point[] points = new Point[7];

		for(int i = 0; i < 7; ++i)
		{
			points[i] = new Point(i, i);
		}
		
		Polygon p = new Polygon(points);
		
		for(int i = 0; i < 7; ++i)
		{
			System.out.println(points[i]);
		}
		
		System.out.println(p.getMathparCode());
		
		
		Square s = new Square(10.0, new Point(2, 3));
		System.out.println(s.getMathparCode());
		
		Rectangle r = new Rectangle(10.0, 15.0, new Point(2, 3));
		System.out.println(r.getMathparCode());
	}  
}
