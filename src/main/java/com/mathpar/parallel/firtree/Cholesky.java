/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.parallel.firtree;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.Element;
import com.mathpar.number.Ring;
import com.mathpar.parallel.ddp.MD.examples.multiplyDoubleMatrix.DoubleMatrix;
import java.util.ArrayList;

/**
 *
 * @author alla
 */
public class Cholesky extends DropTask{
    
    public Cholesky(){
    inData = new Element[1];
    outData = new Element[2];
    numberOfMainComponents = 1;
    state = 0;
    arcs=new int [][]{{1,0,0, 2,1,1, 3,2,1}, {7,0,0, 2,1,0, 6,1,1},{3,0,0},
           {4,0,0, 7,1,1, 5,1,1},{7,0,2, 7,1,5, 5,1,0},{6,0,0},{7,0,4},{}};
    type=5;
    resultForOutFunctionLength = 6;
    inputDataLength = 1;
    }
    
    
    @Override
    public ArrayList<DropTask> doAmin() {
        amin = new ArrayList<DropTask>();
        amin.add(new Cholesky());
        amin.add(new Multiply());
        amin.add(new MultiplyExtended());
        amin.add(new Cholesky());
        amin.add(new MultiplyMinus());
        amin.add(new Multiply());
        
        return amin;    
    }

    @Override
    public void sequentialCalc(Ring ring) {
        MatrixS ms = (MatrixS)inData[0];
        
        if(ms.size < 2)
        {
            outData[0]=inData[0];
            outData[1]= new MatrixS(new Element(-ms.M[0][0].intValue()));
        }
        else
        {
            Element [][]m = ms.toScalarArray(ring);
            double a = m[0][0].doubleValue();
            double b = m[0][1].doubleValue();
            double c = m[1][0].doubleValue();
            double d = m[1][1].doubleValue();
            //System.out.println("a,b,c,d= "+a+" "+b+" "+c+" "+d);
            
            double ra = Math.sqrt(a);
            double rb = b/ra;
            double rd = (a*d - Math.pow(b, 2))/a;
            double rc = Math.sqrt(rd);
            
            //System.out.println("ra,rb,rd,rc= "+ra+" "+rb+" "+rd+" "+rc);
              
            Element [][]res1 = new Element[][]{{new Element(ra),new Element(0)},{new Element(rb),new Element(rc)}}; 
           
            double x = Math.pow(ra, -1);
            double y = Math.pow(rc, -1);
            double z = -y*rb*x;
            
            //System.out.println("x,y,z= "+x+" "+y+" "+z);
            
            Element [][]res2 = new Element[][]{{new Element(x),new Element(0)},{new Element(z),new Element(y)}}; 
            
            MatrixS m1 = new MatrixS(res1, ring);
            MatrixS m2 = new MatrixS(res2, ring);
            outData[0] = m1;
            outData[1] = m2; 
        }
        
    }

    @Override
    public MatrixS[] inputFunction(Element[] input) {
        MatrixS ms =  (MatrixS)input[0];
        return ms.split();   
    }

    @Override
    public Element[] outputFunction(Element[] input, Ring ring) {
        MatrixS[] resCh = new MatrixS[4];
        resCh[0] = (MatrixS)input[0];
        resCh[1] = Tools.doZeroMatrix(resCh[0].size);
        resCh[2] = (MatrixS)input[1];
        resCh[3] = (MatrixS)input[2];
        
        MatrixS[] resInv = new MatrixS[4];
        
        resInv[0] = (MatrixS)input[3];
        resInv[1] = Tools.doZeroMatrix(resInv[0].size);
        resInv[2] = (MatrixS)input[4];
        resInv[3] = (MatrixS)input[5];
        

        MatrixS[] res = new MatrixS[]{MatrixS.join(resCh), MatrixS.join(resInv)};  
        return res; 
        
    }

    @Override
    public boolean isItLeaf() {
        MatrixS ms = (MatrixS)inData[0];
        return (ms.size <= 2);
    }
    
}
