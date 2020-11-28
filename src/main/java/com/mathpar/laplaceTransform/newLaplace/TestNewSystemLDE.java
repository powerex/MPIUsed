/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform.newLaplace;

import com.mathpar.func.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mathpar.laplaceTransform.TestSystemLDE;
import com.mathpar.number.*;

/**
 * Тестовый класс для отладки программы решения систем линейных дифференциальных
 * уравнений с постоянными коэффициентами
 *
 * 21.08.2013 - 70%
 * @author mixail
 */
public class TestNewSystemLDE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Ring ring = new Ring("R64[t]");
        ring.setDefaultRing();
        //WORK! Example №1
//        F dif = new F("\\systLDE(3\\d(x,t)+2*x+\\d(y,t)=1,\\d(x,t)+4\\d(y,t)+3*y=0)", ring);
//        F initC = new F("\\initCond(\\d(x,t,0,0)=0,\\d(x,t,0,1)=0,\\d(y,t,0,0)=0,\\d(y,t,0,1)=0)", ring);
        //Ответ : [(2.2*\exp(-0.55t)+(-2.2)*\exp(-t)), ((-5.5)+3.3*\exp(-0.55t)+2.2*\exp(-t))]
        //WORK! Example №2
//        F dif = new F("\\systLDE(\\d(x,t)-x-2*y=0,\\d(y,t)-2*x-y=1)", ring);
//        F initC = new F("\\initCond(\\d(x,t,0,0)=0,\\d(y,t,0,0)=5)", ring);
        //Ответ : [((-0.67)+(-2.0)*\exp(-t)+2.67*\exp(3.0t)), (0.33+2.0*\exp(-t)+2.67*\exp(3.0t))];
        //WORK! Example №3
//        F dif = new F("\\systLDE(\\d(x, t)-y+z=0, -x-y+\\d(y, t)=0, -x-z+\\d(z, t)=0)", ring);
//        F initC = new F("\\initCond(\\d(x, t, 0, 0)=1, \\d(y, t, 0, 0)=2, \\d(z, t, 0, 0)=3)", ring);
        //h = [((-2.0)+5.0*\exp(t)+(-1)*\exp(t)*t), (2.0+(-1)*\exp(t)), ((-2.0)+4.0*\exp(t)+(-1)*\exp(t)*t)];
        //WORK! Example №4
//        F dif = new F("\\systLDE(\\d(x, t)+3x-4y=9\\exp(2t), 2x+\\d(y, t)-3y=3\\exp(2t))", ring);
//        F initC = new F("\\initCond(\\d(x, t, 0, 0)=2, \\d(y, t, 0, 0)=0)", ring);
        //h = [(\exp(t)+\exp(2.0t)), (\exp(t)+(-1)*\exp(2.0t))]
        //WORK! Example №5
//        F dif = new F("\\systLDE(\\d(x, t, 2)+\\d(x, t)-\\d(y, t)=1, \\d(x, t)+x-\\d(y, t, 2)=1+4\\exp(t))", ring);
//        F initC = new F("\\initCond(\\d(x, t, 0, 0)=1, \\d(x, t, 0, 1)=2, \\d(y, t, 0, 0)=0, \\d(y, t, 0, 1)=1)", ring);
        //h = [((-1)+(-2.0)*\exp(t)+\exp(t)*t+2.0*\exp(-t)+\exp(-t)*t), (2.0+1*t+(-3.0)*\exp(t)+2.0*\exp(t)*t+\exp(-t))];
        //WORK! Example №6
//        F dif = new F("\\systLDE(\\d(x, t, 2)+\\d(y, t)+y=\\exp(t)-t, \\d(x, t)-x+2\\d(y, t, 2)-y=-\\exp(-t))", ring);
//        F initC = new F("\\initCond(\\d(x, t, 0, 0)=1, \\d(x, t, 0, 1)=2, \\d(y, t, 0, 0)=0, \\d(y, t, 0, 1)=0)", ring);
        //h = [((-2.0)+2.0t+2.0*\exp(-t)), ((-2.0t)+(-2.0)*\exp(t))];
        //WORK! Example №7
//        F dif = new F("\\systLDE(\\d(x, t, 2)+\\d(y, t)=\\sh(t)-\\sin(t)-t, \\d(y, t, 2)+\\d(x, t)=\\ch(t)-\\cos(t))", ring);
//        F initC = new F("\\initCond(\\d(x, t, 0, 0)=0, \\d(x, t, 0, 1)=2, \\d(y, t, 0, 0)=1, \\d(y, t, 0, 1)=0)", ring);
        //h = [(t+\sh(t)), ((-0.5t^2)+\cos(t))];
        //Example №8
        F dif = new F("\\systLDE(\\d(x, t, 2)-\\d(x, t)+\\d(y, t)=\\exp(-t)+\\cos(t), \\d(x, t)-\\d(y, t, 2)-\\d(y, t)=2\\exp(t)+\\sin(t))", ring);
        F initC = new F("\\initCond(\\d(x, t, 0, 0)=2, \\d(x, t, 0, 1)=1, \\d(y, t, 0, 0)=0, \\d(y, t, 0, 1)=1)", ring);
        //h = [(\cos(t)+(-1)*\exp(-t)), ((-1.0*\cos(t))+2.0*\exp(t))];


        try {
            VectorS res = new NewSystemLDE().solveSystemLDE(dif, initC, ring);
            System.out.println("res = " + res);
        } catch (Exception ex) {
            Logger.getLogger(TestSystemLDE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
