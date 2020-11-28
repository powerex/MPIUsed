/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.showgraph;

import com.mathpar.func.*;

import java.awt.Canvas;

import com.mathpar.matrix.MatrixD;
import com.mathpar.number.Element;
import com.mathpar.number.Ring;
import com.mathpar.number.Array;

/**
 *
 * @author fake
 */
public final class NewClass extends Canvas {
    public double[][] strangeAlg1(MatrixD A, double k, double l, double cspr, double crep, double[][] coord, Ring ring){
        int numbVert=coord[0].length;
        double r,  pot,pspr, s1, s2, s3 = 0, s4 = 0,s5=0,s6=0,fotx,foty,fsprx,fspry, x[]=coord[0], y[]=coord[1],
                coordnew[][]= new double[2][coord[0].length];
        int i;
        int j;
                for (i=0; i<numbVert; i++){
            //System.out.println("i="+i);
            s1=coord[0][i];
         s2=coord[1][i];
          for (j=0; j<numbVert; j++){
              System.out.println("j="+j);
           r= Math.sqrt(Math.pow((x[i]-x[j]),2)+Math.pow((y[i]-y[j]),2));
              //System.out.println("r="+r);

              if (r>0/*&&r!=l*/){
     if( A.M[i][j].isZero(ring)){
    pot=crep/Math.pow(r,2);
                                        System.out.println("pot= "+pot);
                 fotx=k*pot*((x[j]-x[i])/r);
                   // System.out.println("fotx= "+fotx);
                    foty=k*pot*((y[j]-y[i])/r);
                 //System.out.println("foty= "+foty);
                    s3=s3+fotx;
                   // System.out.println("s3="+s3);
                    s4=s4+foty;
                 System.out.println("s4="+s4);

     }
                 if(!A.M[i][j].isZero(ring)) {
                     pspr= cspr*Math.log(r/l);
                    System.out.println("ppr= "+pspr);
                    fsprx=k*pspr*((x[i]-x[j])/r);
                  //  System.out.println("fsprx="+fsprx);
                    fspry=k*pspr*((y[i]-y[j])/r);
                  //  System.out.println("fspry="+fspry);
                    s5=s5+fsprx;
                    //System.out.println("s5="+s5);
                    s6=s6+fspry;

                    //System.out.println("s6="+s6);

                }

           }
          }
            coordnew[0][i]=s1+s3+s5;
            coordnew[1][i]=s2+s4+s6;

     }
        return coordnew;
    }
    public static void main(String[] args) throws Exception {
        String path0="/home/fake/prog12/image0.png";

                  String path="/home/fake/prog12/image", str;
       Ring r=new Ring("R64[x,y,t]");
       Page d=new Page(r,true);
        long[][]a=new long[7][7];
//a[0][0]=0;
a[0][1]=1;
a[0][2]=1;
a[0][3]=1;
//a[0][4]=0;
//a[0][5]=1;
//a[0][6]=1;
        a[1][0]=1;
         //a[1][1]=0;
         a[1][2]=0;
         a[1][3]=1;
         a[1][4]=1;
         //a[1][5]=0;
         a[1][6]=1;
         a[2][0]=1;
         //a[2][1]=0;
         //a[2][2]=0;
         a[2][3]=1;
        //a[2][4]=0;
         //a[2][5]=1;
         //a[2][6]=1;

       //a[3][0]=0;
         a[3][1]=1;
         a[3][2]=1;
         //a[3][3]=0;
         a[3][4]=1;
         //a[3][5]=0;
         //a[3][6]=1;
         //a[4][0]=0;
         a[4][1]=1;
         a[4][2]=1;
         a[4][3]=1;
        // a[4][4]=0;
         a[4][5]=1;
         a[4][6]=1;
         //a[5][0]=1;
         //a[5][1]=0;
         //a[5][2]=1;
         //a[5][3]=0;
         a[5][4]=1;
        // a[5][5]=0;
         //a[5][6]=0;
         //a[6][0]=1;
         a[6][1]=1;
         //a[6][2]=1;
         //a[6][3]=1;
        a[6][4]=1;
         //a[6][5]=0;
        // a[6][6]=0;


        MatrixD A= new MatrixD(a,r);
        MatrixD Coords = Element.circl(A.M.length);

        double coords[][]=new double[Coords.M.length][];//{{2, 3, 2, 5, 4, 1,6},{4, 2, 6, 5, 4, 1,3}};
        for(int i1 = 0; i1 < Coords.M.length; i1++){
            coords[i1] = new double[Coords.M[i1].length];
            for(int i2 = 0; i2 < Coords.M[i1].length; i2++){
                coords[i1][i2] = Coords.M[i1][i2].doubleValue();
            }
        }
        double k;
        double l=1;
        double crep=1;
        double cspr=2;
        NewClass p=new NewClass();
        // MatrixD Coords = new MatrixD(coords, r);

        F.drawGraph(A, Coords, path0);
        int i=0;
        System.out.println("begin:  "+Array.toString(coords));
        for(k=0.1; k>0;k-=0.001){
            coords=p.strangeAlg1(A,k,l,cspr,crep,coords,r);
            System.out.println("k= "+k+" "+Array.toString(coords));
            Coords = new MatrixD(coords, r);
            i++;
            str=path+i+".png";
            F.drawGraph(A, Coords, str);
        }

    }
}
