package com.mathpar.diffEq;

import com.mathpar.func.CanonicForms;
import com.mathpar.func.Integrate;
import com.mathpar.number.Element;
import com.mathpar.number.Fraction;
import com.mathpar.number.NumberZ;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
import com.mathpar.polynom.FactorPol;
import com.mathpar.polynom.Polynom;
import com.mathpar.webWithout.Play_On_Ground;

/**
 * @author Sergey Glazkov
 * июнь 2017
 * Класс решает урванения в полных дифференциалах классическим алгоритмом
 * Главный класс - solve
 */
public class SolveETD {
    Ring ring = new Ring("Q[x,y]");
    CanonicForms cf = new CanonicForms(ring, true);
    
    public VectorS solve(Element P, Element Q){
        Element IP = new Integrate().integrate(P, new Polynom("x", ring), ring).expand(ring).simplify(ring);//+ ф(y)
        if(IP instanceof FactorPol)
            IP = FPsimplify(IP).expand(ring).simplify(ring);
        System.out.println("A = "+IP);
        Element DP = IP.D(1, ring).expand(ring).simplify(ring);
        System.out.println("DP = "+DP);
        //DP + ф(y)'_y = Q, отсюда ф(y)'_y = Q-DP;
        Element fi = Q.subtract(DP, ring).expand(ring).simplify(ring);
        System.out.println("ф' = "+fi);
        if(fi.isZero(ring)) fi = new NumberZ(0);
        if(fi instanceof Fraction && ((Fraction)fi).denom.isOne(ring)) fi = ((Fraction)fi).num;
        fi = new Integrate().integrate(fi, ring.varPolynom[1], ring).expand(ring).simplify(ring);
        if(fi instanceof FactorPol)
            fi = FPsimplify(fi).expand(ring).simplify(ring);
        
        System.out.println("ф = "+fi);
        Element res;
        try{res = IP.add(fi, ring).EXPAND(ring).simplify(ring);}
        catch(Exception e){res = IP.add(fi, ring).expand(ring).simplify(ring);};
        System.out.println("RES = "+res);
        
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
        Play_On_Ground.startDebug(s1);
    }
}