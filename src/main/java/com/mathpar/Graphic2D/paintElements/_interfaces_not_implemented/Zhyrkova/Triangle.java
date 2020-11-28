package com.mathpar.Graphic2D.paintElements._interfaces_not_implemented.Zhyrkova;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Triangle {
    Point p1;
    Point p2;
    Point p3;

    // Triangle constructor with points
    public Triangle(Point p1, Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Triangle() {
    }

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public Point getP3() {
        return p3;
    }

    public void setP3(Point p3) {
        this.p3 = p3;
    }

    // methods used to set the size of graph
    private double getMaxCoordinate(double x1, double x2, double x3) {
        return Math.max(x1, Math.max(x2, x3));
    }

    private double getMinCoordinate(double x1, double x2, double x3) {
        return Math.min(x1, Math.min(x2, x3));
    }

    // method that returns mathpar code written in file
    private void codeToFile(String str) {
        File myFile = new File("TriangleCreated.txt");
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

    // build Triangle using Points
    public String getMathparCode(Point p1, Point p2, Point p3) {
        double xmin = getMinCoordinate(p1.getX(), p2.getX(), p3.getX()) - 2;
        double xmax = getMaxCoordinate(p1.getX(), p2.getX(), p3.getX()) + 2;
        double ymin = getMinCoordinate(p1.getY(), p2.getY(), p3.getY()) - 2;
        double ymax = getMaxCoordinate(p1.getY(), p2.getY(), p3.getY()) + 2;

        String code = "\\set2D([" + xmin + "," + xmax + "," + ymin + "," + ymax + "], ['ES']); \\tablePlot([[" + p1.getX() + ", " + p2.getX() + ", " + p3.getX() + ", " + p1.getX() + "],[" + p1.getY() + ", " + p2.getY() + ", " + p3.getY() + ", " + p1.getY() + "]]);";

        codeToFile(code);

        return code;
    }

    // build Triangle using side lengths
    public String getMathparCode(double a, double b, double c) {
        double cosA = (Math.pow(b, 2) + Math.pow(c, 2) - Math.pow(a, 2)) / (2 * b * c);
        double cosB = (Math.pow(c, 2) + Math.pow(a, 2) - Math.pow(b, 2)) / (2 * c * a);
        double cosC = (Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2 * a * b);

        double b1 = 0;
        double b2 = 0;
        double c2 = 0;
        double c1 = c;
        double a1 = - (Math.pow(b, 2) - Math.pow(c, 2) - Math.pow(a, 2)) / (2 * c);
        double a2 = Math.sqrt(Math.pow(a, 2) - Math.pow(a1, 2));
        
        double xmin = getMinCoordinate(a1, b1, c1);
        double xmax = getMaxCoordinate(a1, b1, c1);
        double ymin = getMinCoordinate(a2, b2, c2);
        double ymax = getMaxCoordinate(a2, b2, c2);

        String code = "\\set2D([" + xmin + "," + xmax + "," + ymin + "," + ymax + "], ['ES']); \\tablePlot([[" + a1 + ", " + b1 + ", " + c1 + ", " + a1 + "],[" + a2 + ", " + b2 + ", " + c2 + ", " + a2 + "]]);";

        codeToFile(code);

        return  code;
    }

}
