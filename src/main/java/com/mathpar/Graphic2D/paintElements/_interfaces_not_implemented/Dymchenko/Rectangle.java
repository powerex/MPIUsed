package com.mathpar.Graphic2D.paintElements._interfaces_not_implemented.Dymchenko;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Rectangle {

	Point[] points = new Point[4];
	
	Rectangle(double width, double height)
	{
		Point bottomLeft = new Point(1,1);
		
		points[0] = new Point(bottomLeft.x, bottomLeft.y);
		
		points[1] = new Point(bottomLeft.x, bottomLeft.y + (double) height);
		
		points[2] = new Point(bottomLeft.x + (double) width, bottomLeft.y + (double) height);
		
		points[3] = new Point(bottomLeft.x + (double) width, bottomLeft.y);	
	}
	
	Rectangle(double width, double height, Point bottomLeft)
	{
		points[0] = new Point(bottomLeft.x, bottomLeft.y);
		
		points[1] = new Point(bottomLeft.x, bottomLeft.y + (double) height);
		
		points[2] = new Point(bottomLeft.x + (double) width, bottomLeft.y + (double) height);
		
		points[3] = new Point(bottomLeft.x + (double) width, bottomLeft.y);	
	}
	
	// methods used to set the size of graph
    private Point getMaxCoordinate() {
      	double maxX = 0.0;
    	double maxY = 0.0;
    	
        for(int i = 0; i < this.points.length; ++i)
        {
        	if (maxX < points[i].x)
        	{
        		maxX = points[i].x;
        	}
        	
        	if (maxY < points[i].y)
        	{
        		maxY = points[i].y;
        	}
        }
        
        return new Point(maxX, maxY);
    }

    private Point getMinCoordinate() {
    	double minX = 0.0;
    	double minY = 0.0;
    	
        for(int i = 0; i < this.points.length; ++i)
        {
        	if (minX > points[i].x)
        	{
        		minX = points[i].x;
        	}
        	
        	if (minY > points[i].y)
        	{
        		minY = points[i].y;
        	}
        }
        
        return new Point(minX, minY);
    }

    public String getMathparCode()
    {
		Point maxP = getMaxCoordinate();
		Point minP = getMinCoordinate();
		
        double xmin = minP.x - 2;
        double xmax = maxP.x + 2;
        double ymin = minP.y - 2;
        double ymax = maxP.y + 2;

        String str = "";
        
        for(int i = 0; i < this.points.length; ++i)
        {
        	str += points[i].x + (i == this.points.length - 1 ? "],[" : ", ");
        }
        
        for(int i = 0; i < this.points.length; ++i)
        {
        	str += points[i].y + (i == this.points.length - 1 ? "" : ", ");
        }
        
        String code = "\\set2D([" + xmin + "," + xmax + "," + ymin + "," + ymax + "], ['ES']); \\tablePlot([[" + str +  "]]);";

        codeToFile(code);

        return code;
    }
   
	private static void codeToFile(String str) {
        File myFile = new File("RectangleCreated");
        try {
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(myFile);
            myWriter.write(str);
            myWriter.write("\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
