/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform;

import com.mathpar.func.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.mathpar.number.*;
/**
 *
 * @author mixail
 */
public class TestSystemLDEAccuracy {

    public static void main(String[] args) {
        Ring ring = new Ring("R[t]");
        NumberR eps = new NumberR("1.E-8");
        ring.setMachineEpsilonR(eps);

        long l1 = System.currentTimeMillis();
////////////////////////////////////////////////////////////////////////////////
        F dif1 = new F("\\systLDE(3\\d(x,t)+2*x+\\d(y,t)=1,\\d(x,t)+4\\d(y,t)+3*y=0)", ring);
        F initC1 = new F("\\initCond(\\d(x,t,0,0)=a,\\d(x,t,0,1)=b,\\d(y,t,0,0)=c,\\d(y,t,0,1)=d)", ring);
        try {
               VectorS res1 = new SystemLDE().solveSystemLDE_accuracy(dif1, initC1, eps, ring);//для системы
        } catch (Exception ex) {
            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
        }
////////////////////////////////////////////////////////////////////////////////
//        F dif2 = new F("\\systLDE(\\d(x,t)-x-2*y=0,\\d(y,t)-2*x-y=1)", ring);
//        F initC2 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(y,t,0,0)=5)", ring);
//        try {
//               VectorS res2 = new SystemLDE().solveSystemLDE_accuracy(dif2, initC2, eps, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif3 = new F("\\systLDE(\\d(x,t,2)+\\d(y,t)+y=\\exp(t)-t,\\d(x,t)-x+2\\d(y,t,2)-y=-\\exp(-t))", ring);
//        F initC3 = new F("\\initCond(\\d(x,t,0,0)=1,\\d(x,t,0,1)=2,\\d(y,t,0,0)=0,\\d(y,t,0,1)=0)", ring);
//        try {
//               VectorS res3 = new SystemLDE().solveSystemLDE_accuracy(dif3, initC3, eps, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif4 = new F("\\systLDE(\\d(y,t)+y-3*x=0, -x-y+\\d(x,t)=\\exp(t))", ring);
//        F initC4 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(y,t,0,0)=0)", ring);
//        try {
//               VectorS res4 = new SystemLDE().solveSystemLDE_accuracy(dif4, initC4, eps, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif5 = new F("\\systLDE(\\d(y,t)+\\d(x,t)-x=\\exp(t), 2\\d(y,t)+\\d(x,t)+2*x=\\cos(t))", ring);
//        F initC5 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(y,t,0,0)=0)", ring);
//        try {
//               VectorS res5 = new SystemLDE().solveSystemLDE_accuracy(dif5, initC5, eps, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif6 = new F("\\systLDE(\\d(y,t)+y-x-z=0, \\d(x,t)-y+x-z=0, \\d(z,t)-y-x-z=0)", ring);
//        F initC6 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(y,t,0,0)=1,\\d(z,t,0,0)=0)", ring);
//        try {
//               VectorS res6 = new SystemLDE().solveSystemLDE_accuracy(dif6, initC6, eps, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif7 = new F("\\systLDE(\\d(y,t,2)+2*x=0, \\d(x,t,2)-2*y=0)", ring);
//        F initC7 = new F("\\initCond(\\d(x,t,0,0)=0,\\d(x,t,0,1)=0,\\d(y,t,0,0)=0,\\d(y,t,0,1)=1)", ring);
//        try {
//               VectorS res7 = new SystemLDE().solveSystemLDE_accuracy(dif7, initC7, eps, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif8 = new F("\\systLDE(\\d(x,t,2)+ \\d(y,t)+y=\\exp(t)-t,2*\\d(y,t,2)+\\d(x,t)-x-y=(-1)\\exp(-t))", ring);
//        F initC8 = new F("\\initCond(\\d(x,t,0,1)=2,\\d(x,t,0,0)=0,\\d(y,t,0,1)=0,\\d(y,t,0,0)=0)", ring);
//        try {
//               VectorS res8 = new SystemLDE().solveSystemLDE_accuracy(dif8, initC8, eps, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif9 = new F("\\systLDE(\\d(x,t)+x-2*y=0,\\d(y,t)+x+4*y=0)", ring);
//        F initC9 = new F("\\initCond(\\d(x,t,0,0)=1,\\d(y,t,0,0)=1)", ring);
//        try {
//               VectorS res9 = new SystemLDE().solveSystemLDE_accuracy(dif9, initC9, eps, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif10 = new F("\\systLDE(\\d(x,t)-2*y=0,\\d(y,t)-2*x=0)", ring);
//        F initC10 = new F("\\initCond(\\d(x,t,0,0)=2,\\d(y,t,0,0)=2)", ring);
//        try {
//               VectorS res10 = new SystemLDE().solveSystemLDE_accuracy(dif10, initC10, eps, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
//////////////////////////////////////////////////////////////////////////////////
//        F dif11 = new F("\\systLDE(\\d(x,t)+2*x+2*y=10\\exp(2t),\\d(y,t)-2*x+y=7\\exp(2t))", ring);
//        F initC11 = new F("\\initCond(\\d(x,t,0,0)=1,\\d(y,t,0,0)=3)", ring);
//        try {
//               VectorS res11 = new SystemLDE().solveSystemLDE_accuracy(dif11, initC11, eps, ring);//для системы
//        } catch (Exception ex) {
//            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
//        }
////////////////////////////////////////////////////////////////////////////////
         long l2 = System.currentTimeMillis();
         System.out.println("TOTAL TIME = " + (l2-l1));
 }
//////////////////////////////////////////////////////////////////////////////

}
