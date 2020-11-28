/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.diffEq;

import com.mathpar.func.CanonicForms;
import com.mathpar.func.Integrate;
import com.mathpar.number.Element;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
import com.mathpar.polynom.FactorPol;
import com.mathpar.polynom.Polynom;
import com.mathpar.webWithout.Play_On_Ground;

public class EqTotalDiff {
    Ring ring = new Ring("Q[x,y]");
    CanonicForms cf = new CanonicForms(ring, true);
    
    public VectorS solve(Element P, Element Q){
        Element IP = new Integrate().integrate(P, new Polynom("x", ring), ring).EXPAND(ring).simplify(ring);//+ ф(y)
        if(IP instanceof FactorPol)
            IP = FPsimplify(IP).EXPAND(ring).simplify(ring);
        Element DP = IP.D(1, ring).EXPAND(ring).simplify(ring);
        //DP + ф(y)'_y = Q, отсюда ф(y)'_y = Q-DP;
        Element fi = Q.subtract(DP, ring).EXPAND(ring).simplify(ring);
        
        fi = new Integrate().integrate(fi, new Polynom("y", ring), ring).EXPAND(ring).simplify(ring);
        if(fi instanceof FactorPol)
            fi = FPsimplify(fi).EXPAND(ring).simplify(ring);
        
        Element res = IP.add(fi, ring).EXPAND(ring).simplify(ring);
        
        return new VectorS(new Element[]{res, ring.numberZERO});
    }

    /**
     * Превращает FactorPol в сумму
     * @param el входной факторпол
     * @return результат
     */
    public Element FPsimplify(Element el){
        FactorPol fp = (FactorPol) el;
        Element mult = ring.numberONE;
        for (int i = 0; i < fp.multin.length; i++)
            mult = mult.multiply(fp.multin[i].pow(fp.powers[i], ring), ring);//.simplify(ring);
        return mult.simplify(ring);
    }
    
    public static void main(String[] args) throws Exception{
        String s1 = "SPACE=Q[x,y];f=\\solveDE((2x-y+1)\\d(x)+(2y-x-1)\\d(y) = 0);";
        String s2 = "SPACE=Q[x,y];f=\\solveDE((3x^2-3y^2+4x)\\d(x)-(6xy+4y)\\d(y) = 0);";
        String s3 = "SPACE=Q[x,y];f=\\solveDE((6y-3x^2+3y^2)\\d(x)+(6x+6xy)\\d(y) = 0);";
        String s4 = "SPACE=Q[x,y];f=\\solveDE((2x(1-\\exp(y))/(1+x^2)^2)\\d(x)+(\\exp(y)/(1+x^2))\\d(y) = 0);";
        String s5 = "SPACE=Q[x,y];f=\\solveDE((3x^2+6xy^2)\\d(x)+(6x^2y+4y^3)\\d(y) = 0);";
        String s6 = "SPACE=Q[x,y];f=\\solveDE(2xy\\d(x)+(x^2+3y^2)\\d(y) = 0);";
        String s7 = "SPACE=Q[x,y];f=\\solveDE((\\exp(x)y+\\cos(y)\\sin(x)-x^3y^6)\\d(x)+(\\exp(x)+\\sin(y)\\cos(x)-3/2*y^5x^4)\\d(y) = 0);";
        Play_On_Ground.startDebug(s7);
    }
}