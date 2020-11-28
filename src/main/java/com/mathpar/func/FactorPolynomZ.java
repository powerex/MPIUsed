/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;
import com.mathpar.number.*;
import com.mathpar.number.Ring;
import com.mathpar.polynom.*;

public class FactorPolynomZ {

    public static FactorPol FactorPolynomZ(Polynom p, Ring ring) {
        int maxStepP = p.powers[0];
        Polynom monom[] = new Polynom[maxStepP];
        int StepMonom[] = new int[maxStepP];
        FactorPol pol = p.FactorPol_SquareFreeOneVar(ring);
        int over = pol.multin.length;
        int k = 0;
        for (int j = 0; j < over; j++) {
            if (pol.multin[j].isItNumber()) {
                monom[k] = pol.multin[j];
                StepMonom[k] = pol.powers[j];
                k++;
            }
        }

        int currentLen = k;
        for (int i = 0; i < over; i++) {
            if (pol.multin[i].isItNumber()) {
                continue;
            }
            try {
                Polynom factors[] = FactorizationPolynomialWithoutMultipleFactors.Factor(pol.multin[i], ring);
                int len = factors.length;
                System.arraycopy(factors, 0, monom, currentLen, len);
                int ii = pol.powers[i];
                for (int s = 0; s < len; s++) {
                    StepMonom[currentLen + s] = ii;
                }
                currentLen += len;
            } catch (ArrayIndexOutOfBoundsException e) {
                monom[currentLen] = pol.multin[i];
                StepMonom[currentLen] = pol.powers[i];
            }
        }
        int mas = 0;
        for (int i = 0; i < maxStepP; i++) {
            if (monom[i] == null) {
                mas = i;
                break;
            }
        }
        Polynom monom1[] = new Polynom[mas];
        int StepMonom1[] = new int[mas];
        System.arraycopy(monom, 0, monom1, 0, mas);
        System.arraycopy(StepMonom, 0, StepMonom1, 0, mas);
        return new FactorPol(StepMonom1, monom1);
    }
    
    public static void main(String[] args) {
        Ring ring = new Ring("C[x]"); 
        Polynom p = new Polynom("  x -\\i", ring);
       p.coeffs[1] =  new NumberR("1") ; // В качестве первого коэффициента задаю дробь 128/17.
      int[][] ppp =new int[1][];
        System.out.println("out:  "+p.factorOfPol_inQ(false, ring));
      //  System.out.println("out:  "+ppp[0][0]+ppp[0][1] +ppp[0][2] ) ;
    }
}
