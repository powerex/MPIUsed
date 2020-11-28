package com.mathpar.Graphic2D.tangent;

import com.mathpar.Graphic2D.tangent.lexerParser.ExpressionNode;
import com.mathpar.Graphic2D.tangent.lexerParser.Parser;
import com.mathpar.Graphic2D.tangent.lexerParser.SetVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TangentLine {


    private String function;
    //private String inverseTangentLineFunction;
    private String tangentLineFunction;
    private Boolean error;
    private String errorMessage;
    private ArrayList<Double> points;
    private Double m; //m-slope of a line
    private String toMathParString = "";
    private static Map<String , String > mapOfOperators = new HashMap<>();

///To use this class go to class Play_On_Groundin webWithout and create object of this class like this
    // TangentLine tangentLine = new TangentLine("\\set2D(-4,2,-4,4); tangent(sin(x^3),1);");
//        startDebug(tangentLine.getToMathParString());
    ///and in class Page package func change in line 2036  f.showGraphics(this, true, false, false,   second parametr
    // to "false", to save graphic to your home derictory

    static {
        mapOfOperators.put("sin","\\sin");
        mapOfOperators.put("cos","\\cos");
        mapOfOperators.put("arccos","\\arccos");
        mapOfOperators.put("arcsin","\\arcsin");
        mapOfOperators.put("sqrt","\\sqrt");
        mapOfOperators.put("exp","\\exp");
        mapOfOperators.put("ln","\\ln");
        mapOfOperators.put("lg","\\lg");
    }

    public TangentLine(String txt) {

        String set2d = txt.split(";")[0].trim();
        String tangent = txt.split(";")[1].trim();



        String[] split = tangent.substring(8, tangent.length() - 1).split(",");
        String tangentFunc = split[0];
        String tangentInDot = split[1];

        String tangentLineFunction = getTangentLine(tangentFunc, Double.valueOf(tangentInDot));
        System.out.println(tangentFunc);
        System.out.println(tangentLineFunction);

        String[] splitted = tangentFunc.split("\\(");
        String validTangentFunc = "";
        if (mapOfOperators.containsKey(splitted[0])){
            validTangentFunc = mapOfOperators.get(splitted[0]) + "("+splitted[1];
        }
        else {
            validTangentFunc = tangentFunc;
        }


        toMathParString = String.format("%s; f1 = \\plot(%s); f2 = \\plot(%s); \\showPlots([f1,f2]);",
                set2d,
                validTangentFunc,
                tangentLineFunction);
        System.out.println(toMathParString);



        this.error = false;

    }
        public String getToMathParString(){return toMathParString;}
    //The method returns a tangent line of a function through a given point in a String format
    public String getTangentLine(String function, Double x0) {
        this.function = function;

        // Function of a tangent line through a point (x0,y0) is y - y0 = m(x - x0) , => y = m*x-m*x0 + y0
        // when m - slope of the function that equals to a derivative of the function in that point
        // ; x0 - point through which tangent line of a function goes

        // getting a rounded to four decimal points function in a string for further calculations

        Double y0 = f(x0);
        this.m = round(d(x0), 4);
        Double calculation = round(this.m * x0 - y0, 4);
        return this.tangentLineFunction = this.m.toString() + "*x" + "-" + "(" + calculation.toString() + ")";
    }

    //Finds the 2 points forming the segment of a tangent line
    //Abscissa and ordinate values are added interchangeably
    public void findPoints(Double x, Double lineLength) {

        this.points = new ArrayList<>();
        LexerParserController c = new LexerParserController();
        error = false;// parser error

        //x coordinate of starting point of tangent line
        double xS = x - lineLength;
        //System.out.println("xS= " + xS);
        // if the result of function is NaN or Infinite, search for another point
        while (c.getFunctionValue(xS, this.tangentLineFunction).isInfinite() || c.getFunctionValue(xS, this.tangentLineFunction).isNaN()) {
            xS += 0.01d;
        }
        this.points.add(xS + 0.01d);
        this.points.add(round(c.getFunctionValue(xS, this.tangentLineFunction), 4));


        //check if there was an error whilst parsing
        if (c.getErrorFound() == true) {
            //   System.out.println(c.getErrorMessage());
            errorMessage = c.getErrorMessage();
            error = true;
        }

        // x coordinate of ending point of tangent line
        double xE = x + lineLength;
        //System.out.println("xE= " + xE);
        this.points.add(xE);
        this.points.add(round(c.getFunctionValue(xE, this.tangentLineFunction), 4));
        //System.out.println("x="+this.points.get(2)+"y="+this.points.get(3));


        //check if there was an error whilst parsing
        if (c.getErrorFound() == true) {
            //  System.out.println(c.getErrorMessage());
            errorMessage = c.getErrorMessage();
            error = true;
        }
    }


    //Method to provide the value of function f(x)
    public double f(double x) {
        Parser p = new Parser();
        double point;
        ExpressionNode exp = p.parse(this.function);
        exp.accept(new SetVariable("x", x));
        point = exp.getValue();
        return point;
    }

    //Method to provide the value of derivative f'(x)).
    public double d(double x) {
        double d = 0.000001;
        return (f(x + d) - f(x)) / d;
    }

    //round number to n places
    public static double round(double value, int places) {
        return Math.round(value * Math.pow(10d, places)) / Math.pow(10d, places);
    }

    public String getTangentLineFunction() {
        return tangentLineFunction;
    }

    public boolean isError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ArrayList<Double> getPoints() {
        return points;
    }

}




