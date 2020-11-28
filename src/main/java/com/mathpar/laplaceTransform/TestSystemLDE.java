/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform;

import com.mathpar.func.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mathpar.number.*;

/**
 * Тестовый класс для решения систем дифференциальных уравнений
 *
 * @author mixail
 * 2011-2012 years
 */
public class TestSystemLDE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Ring ring = new Ring("R64[t]");
        ring.page=new Page(ring,true);
        
//         NumberR64 zx = new NumberR64(2);
//         Complex cx = new Complex(0, 3.84);
//         System.out.println(zx.divide(cx, ring).toString(ring));



    //    long l1 = System.currentTimeMillis();
////////////////////////////////////////////////////////////////////////////////
      // F dif1 = new F("\\systLDE(\\d(x,t)-y+z=0, -x-y+\\d(y,t)=0, -x-z+\\d(z,t)=0)", ring);
       // F initC1 = new F("\\initCond(\\d(x,t,0,0)=1,\\d(y,t,0,0)=2,\\d(z,t,0,0)=3)", ring);
//        try {
//               VectorS res1 = new SystemLDE().solveSystemLDE(dif1, initC1, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
    //   F dif2 = new F("\\systLDE(\\d(x,t,2)+\\d(x,t)-\\d(y,t)=1,\\d(x,t)+x-\\d(y,t,2)=1+4\\exp(t))", ring);
  //     F initC2 = new F("\\initCond(\\d(x,t,0,0)=1,\\d(x,t,0,1)=2,\\d(y,t,0,0)=0,\\d(y,t,0,1)=1)", ring);
//        try {
//               VectorS res2 = new SystemLDE().solveSystemLDE(dif2, initC2, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
      //  F dif3 = new F("\\systLDE(3\\d(x,t)+2*x+\\d(y,t)=1,\\d(x,t)+4\\d(y,t)+3*y=0)", ring);
      //  F initC3 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(x,t,0,1)=0,\\d(y,t,0,0)=0,\\d(y,t,0,1)=0)", ring);
//        try {
//               VectorS res3 = new SystemLDE().solveSystemLDE(dif3, initC3, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
       // F dif4 = new F("\\systLDE(\\d(x,t)-y+z=0, -x-y+\\d(y,t)=0, -x-z+\\d(z,t)=0)", ring);
      //  F initC4 = new F("\\initCond(\\d(x,t,0,0)=a,\\d(y,t,0,0)=b,\\d(z,t,0,0)=c)", ring);
//        try {
//               VectorS res4 = new SystemLDE().solveSystemLDE(dif4, initC4, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
     //  F dif5 = new F("\\systLDE(\\d(x,t)+3*x-4*y=9\\exp(2t),2*x+\\d(y,t)-3*y=3\\exp(2t))", ring);
     //  F initC5 = new F("\\initCond(\\d(x,t,0,0)=2,\\d(y,t,0,0)=0)", ring);
//        try {
//               VectorS res5 = new SystemLDE().solveSystemLDE(dif5, initC5, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif6 = new F("\\systLDE(\\d(x,t)-x-2*y=0,\\d(y,t)-2*x-y=1)", ring);
//        F initC6 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(y,t,0,0)=5)", ring);
//        try {
//               VectorS res6 = new SystemLDE().solveSystemLDE(dif6, initC6, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif7 = new F("\\systLDE(\\d(x,t,2)+\\d(y,t)+y=\\exp(t)-t,\\d(x,t)-x+2\\d(y,t,2)-y=-\\exp(-t))", ring);
//        F initC7 = new F("\\initCond(\\d(x,t,0,0)=1,\\d(x,t,0,1)=2,\\d(y,t,0,0)=0,\\d(y,t,0,1)=0)", ring);
//        try {
//               VectorS res7 = new SystemLDE().solveSystemLDE(dif7, initC7, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
   //     F dif8 = new F("\\systLDE(\\d(x,t,2)+\\d(y,t)=\\sh(t)-\\sin(t)-t,\\d(y,t,2)+\\d(x,t)=\\ch(t)-\\cos(t))", ring);
   //    F initC8 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(x,t,0,1)=2,\\d(y,t,0,0)=1,\\d(y,t,0,1)=0)", ring);
//        try {
//               VectorS res8 = new SystemLDE().solveSystemLDE(dif8, initC8, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
      //  F dif9 = new F("\\systLDE(\\d(x,t,2)-\\d(x,t)+\\d(y,t)=\\exp(-t)+\\cos(t),\\d(x,t)-\\d(y,t,2)-\\d(y,t)=2*\\exp(t)+\\sin(t))", ring);
     //   F initC9 = new F("\\initCond(\\d(x,t,0,0)=2,\\d(x,t,0,1)=1,\\d(y,t,0,0)=0,\\d(y,t,0,1)=1)", ring);
//        try {
//               VectorS res9 = new SystemLDE().solveSystemLDE(dif9, initC9, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif10 = new F("\\systLDE(\\d(y,t)+y-3*x=0, -x-y+\\d(x,t)=\\exp(t))", ring);
//        F initC10 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(y,t,0,0)=0)", ring);
//        try {
//               VectorS res10 = new SystemLDE().solveSystemLDE(dif10, initC10, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
     //  F dif11 = new F("\\systLDE(\\d(y,t)+\\d(x,t)-x=\\exp(t), 2\\d(y,t)+\\d(x,t)+2*x=\\cos(t))", ring);
      //  F initC11 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(y,t,0,0)=0)", ring);
//        try {
//               VectorS res11 = new SystemLDE().solveSystemLDE(dif11, initC11, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif12 = new F("\\systLDE(\\d(y,t)-y+x=1.5*t^2, \\d(x,t)+2*x+4*y=4*t+1)", ring);
//        F initC12 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(y,t,0,0)=0)", ring);
//        try {
//               VectorS res12 = new SystemLDE().solveSystemLDE(dif12, initC12, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif13 = new F("\\systLDE(\\d(y,t)+y-x-z=0, \\d(x,t)-y+x-z=0, \\d(z,t)-y-x-z=0)", ring);
//        F initC13 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(y,t,0,0)=1,\\d(z,t,0,0)=0)", ring);
//        try {
//               VectorS res13 = new SystemLDE().solveSystemLDE(dif13, initC13, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
        F dif14 = new F("\\systLDE(\\d(y,t)-x+z=0, \\d(x,t)+2*y-x=0, \\d(z,t)-2*y+x=0)", ring);
        F initC14 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(y,t,0,0)=1,\\d(z,t,0,0)=0)", ring);
//        try {
//               VectorS res14 = new SystemLDE().solveSystemLDE(dif14, initC14, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif15 = new F("\\systLDE(\\d(y,t,2)+2*x=0, \\d(x,t,2)-2*y=0)", ring);
//        F initC15 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(x,t,0,1)=0,\\d(y,t,0,0)=0,\\d(y,t,0,1)=1)", ring);
//        try {
//               VectorS res15 = new SystemLDE().solveSystemLDE(dif15, initC15, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif16 = new F("\\systLDE(\\d(x,t)-8*y+x=0,\\d(y,t)-x-y=0)", ring);
//        F initC16 = new F("\\initCond(\\d(x,t,0,0)=a,\\d(y,t,0,0)=b)", ring);
//        try {
//               VectorS res16 = new SystemLDE().solveSystemLDE(dif16, initC16, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif17 = new F("\\systLDE(\\d(x,t)+3*x-4*y=9\\exp(t)^2,\\d(y,t)+2*x-3*y=3\\exp(t)^2)", ring);
//        F initC17 = new F("\\initCond(\\d(x,t,0,0)=2,\\d(y,t,0,0)=0)", ring);
//        try {
//               VectorS res17 = new SystemLDE().solveSystemLDE(dif17, initC17, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif18 = new F("\\systLDE(\\d(x,t,2)+ \\d(y,t)=\\sh(t)-\\sin(t)-t,\\d(y,t,2)-\\d(x,t)=\\ch(t)-\\cos(t))", ring);
//        F initC18 = new F("\\initCond(\\d(x,t,0,1)=2,\\d(x,t,0,0)=0,\\d(y,t,0,1)=0,\\d(y,t,0,0)=1)", ring);
//        try {
//               VectorS res18 = new SystemLDE().solveSystemLDE(dif18, initC18, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif19 = new F("\\systLDE(\\d(x,t,2)+ \\d(y,t)+y=\\exp(t)-t,2*\\d(y,t,2)+\\d(x,t)-x-y=(-1)\\exp(-t))", ring);
//        F initC19 = new F("\\initCond(\\d(x,t,0,1)=2,\\d(x,t,0,0)=0,\\d(y,t,0,1)=0,\\d(y,t,0,0)=0)", ring);
//        try {
//               VectorS res19 = new SystemLDE().solveSystemLDE(dif19, initC19, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
        F dif20 = new F("\\systLDE(\\d(x,t)+5*y-4*x=0,\\d(y,t)-x=0)", ring);
       F initC20 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(y,t,0,0)=1)", ring);
//        try {
//               VectorS res20 = new SystemLDE().solveSystemLDE(dif20, initC20, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif21 = new F("\\systLDE(\\d(x,t)+x-2*y=0,\\d(y,t)+x+4*y=0)", ring);
//        F initC21 = new F("\\initCond(\\d(x,t,0,0)=1,\\d(y,t,0,0)=1)", ring);
//        try {
//               VectorS res21 = new SystemLDE().solveSystemLDE(dif21, initC21, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif22 = new F("\\systLDE(\\d(x,t)-2*y=0,\\d(y,t)-2*x=0)", ring);
//        F initC22 = new F("\\initCond(\\d(x,t,0,0)=2,\\d(y,t,0,0)=2)", ring);
//        try {
//               VectorS res22 = new SystemLDE().solveSystemLDE(dif22, initC22, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif23 = new F("\\systLDE(\\d(x,t)-3*x-4*y=0,\\d(y,t)-4*x+3*y=0)", ring);
//        F initC23 = new F("\\initCond(\\d(x,t,0,0)=1,\\d(y,t,0,0)=1)", ring);
//        try {
//               VectorS res23 = new SystemLDE().solveSystemLDE(dif23, initC23, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif24 = new F("\\systLDE(\\d(x,t)+2*x+2*y=10\\exp(2t),\\d(y,t)-2*x+y=7\\exp(2t))", ring);
//        F initC24 = new F("\\initCond(\\d(x,t,0,0)=1,\\d(y,t,0,0)=3)", ring);
//        try {
//               VectorS res24 = new SystemLDE().solveSystemLDE(dif24, initC24, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif25 = new F("\\systLDE(\\d(x,t,2)+\\d(y,t)+y=\\exp(t)-t,2*\\d(y,t,2)+\\d(x,t)-x-y=-1*\\exp(-t))", ring);
//        F initC25 = new F("\\initCond(\\d(x,t,0,0)=1,\\d(x,t,0,1)=2,\\d(y,t,0,0)=0,\\d(y,t,0,1)=0)", ring);
//        try {
//               VectorS res25 = new SystemLDE().solveSystemLDE(dif25, initC25, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
////////////////////////////////////////////////////////////////////////////////
//        F dif25 = new F("\\systLDE(\\d(x,t)+3*x-4*y=9\\exp(t)^2,\\d(y,t)+2*x-3*y=3\\exp(t)^2)", ring);
//        F initC25 = new F("\\initCond(\\d(x,t,0,0)=2,\\d(y,t,0,0)=0)", ring);
//        try {
//               VectorS res25 = new SystemLDE().solveSystemLDE(dif25, initC25, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
 ////////////////////////////////////////////////////////////////////////////////
        //long l2 = System.currentTimeMillis();
       // System.out.println("Total time = " + (l2-l1));
        //Error
//        F dif25 = new F("\\systLDE(\\d(x,t)+\\d(y,t)-y=\\exp(t)*t^2 ,\\d(x,t)+\\d(y,t)+2*y=1+4\\cos(t))", ring);
//        F initC25 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(y,t,0,0)=0)", ring);
        ////////////////////////////////////////////////////////////////////////
        F dif26 = new F("\\systLDE(\\d(x,t,3)-\\d(x,t)-2*x-\\d(y,t,3)+y=t^2*\\exp(2t)*\\unitStep(t-1)-\\exp(t)*\\unitStep(t-1)+\\exp(t)*\\unitStep(t),3\\d(x,t,3)+\\d(x,t,2)-2\\d(x,t)+\\d(y,t,3)+y=\\exp(2t)*\\unitStep(t-1)-t*\\exp(t)*\\unitStep(t-1)+t\\exp(t)*\\unitStep(t))", ring);
        F initC26 = new F("\\initCond(\\d(x,t,0,0)=5,\\d(x,t,0,1)=10,\\d(x,t,0,2)=30,\\d(y,t,0,0)=4,\\d(y,t,0,1)=14,\\d(y,t,0,2)=20)", ring);




                try {
               Element res26 = new SystemLDE().solveSystemLDE(dif14, initC14, ring);//для системы
        } catch (Exception ex) {
            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
