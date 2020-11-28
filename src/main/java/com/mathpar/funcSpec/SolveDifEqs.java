/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.funcSpec;

import com.mathpar.diffEq.SolveDESV;
import com.mathpar.func.CanonicForms;
import com.mathpar.func.F;
import com.mathpar.func.Fname;
import com.mathpar.number.Element;
import com.mathpar.number.NumberZ;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
import com.mathpar.polynom.Polynom;

/**
 *
 * @author student
 */
public class SolveDifEqs {

    
 /** This is root function to solve any type of differential equations
 * 
 * @param f - differential equation (left part minus right part)
 * @param r - Ring
 * @return  
 */    
public Element solve(Element f, Ring r) { Element Solution;
    //  1)  Try to solve as a diff. eq. with separable variables
    SolveDESV sdesv = new SolveDESV();
    Solution = sdesv.solve(f, NumberZ.ZERO);
    if (Solution instanceof VectorS){Element[]v=((VectorS)Solution).V; 
          Solution=new F(F.SYSTEM, new F(F.B_EQ, v[0], v[1].add(new Fname("C") ,r) ));}
    return Solution;
}
    
    
    /**
     *
     * @param f - правая часть дифференциального уравнения вида
     * F(x,y,y',y",...)= 0
     * @param r
     *
     * @return null - если на входе не диф.ур.
     *
     * NaN - диф.ур. которое не умеем решать
     *
     * VectorS - решение диф.ур.
     */
    public Element solve1(Element f, Ring r) {
        //старший порядок производной
        int k = 0;
        int l = 0;
        //массив коэффициентов при y'
        Element[] coef = new Element[0];
        if (f instanceof F) {
            coef = new Element[((F) f).X.length];
            for (Element X : ((F) f).X) {
                if (X instanceof F) {
                    if (((F) X).name == F.MULTIPLY) {
                        F m = (F) X;
                        for (Element X1 : m.X) {
                            if (X1 instanceof F) {
                                if (((F) X1).name == F.d) {
                                    if (((F) X1).X.length == 3) {
                                        int k1 = ((F) X1).X[2].intValue();
                                        if (k1 > k) {
                                            k = k1;
                                        }
                                    } else {
                                        int k1 = 1;
                                        if (k1 > k) {
                                            k = k1;
                                        }
                                    }
                                } else {
                                    coef[l] = X1;
                                    l++;
                                }
                            } else {
                                if (X1 instanceof Polynom) {
                                    coef[l] = X1;
                                    l++;
                                } else {
                                    coef[l] = X1;
                                    l++;
                                }
                            }
                        }
                    }
                    if (((F) X).name == F.d) {
                        if (((F) X).X.length == 3) {
                            int k1 = ((F) X).X[2].intValue();
                            if (k1 > k) {
                                k = k1;
                            }
                        } else {
                            int k1 = 1;
                            if (k1 > k) {
                                k = k1;
                            }
                        }
                        coef[l] = r.polynomONE;
                        l++;
                    }
                } else {
                    coef[l] = X;
                    l++;
                }
            }
        }
        if (k == 0) {
            return null;
        }
        return null;
    }

    public static void main(String[] args) {
        Ring r = new Ring("R64[x,y,t]");
        F f = new F("\\systLDE(\\sin(x)x^2\\d(y,t,2)+\\d(y,t)+5=0)", r);
        F ff = r.CForm.ExpandForYourChoise(((F) f.X[0]), 1, 1, 1, -1, 1);
        Element a = new SolveDifEqs().solve(ff, r);
    }

}