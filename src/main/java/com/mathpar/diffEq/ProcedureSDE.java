/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.diffEq;

import com.mathpar.diffExpression.DiffEquation;
import com.mathpar.func.CanonicForms;
import com.mathpar.func.F;
import com.mathpar.number.Element;
import com.mathpar.number.Fraction;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
import com.mathpar.polynom.FactorPol;
import com.mathpar.polynom.Polynom;

/**
 *
 * @author sergey
 */
public class ProcedureSDE {
    Ring ring = new Ring("Q[x,y]");
    Element left, right;
    
    public void toNormalForm(){
        if(left instanceof Polynom){
            Polynom p = (Polynom)left;
            Polynom[] res = p.divAndRem(ring.varPolynom[1], ring);
            if(res[1].isItNumber() && !res[1].isZero(ring)){
                left = left.subtract(res[1], ring).simplify(ring);
                right = right.subtract(res[1], ring).simplify(ring);
                toNormalForm();
                return;
            }
            if(p.toMonomials(ring).length == 1){
                if(!p.coeffs[0].isOne(ring)){
                    left = left.divide(p.coeffs[0], ring).simplify(ring);
                    right = right.divide(p.coeffs[0], ring).simplify(ring);
                    toNormalForm();
                    return;
                }
                if(p.powers[0] == 0 && p.powers[1] != 1){
                    left = ring.varPolynom[1];
                    if(p.powers[1] == 2) right = new F(F.SQRT, right);
                    else right = right.pow(new Fraction(1, p.powers[1]), ring);
                    toNormalForm();
                    return;
                }
            }
            return;
        }
        if(left instanceof Fraction){
            Fraction f = (Fraction) left;
            if(varX(f.num)){
                left = f.denom;
                right = new F(F.DIVIDE, new Element[]{f.num, right});
                toNormalForm();
                return;
            }
            if(varX(f.denom)){
                left = f.num;
                right = right.multiply(f.denom, ring);
                toNormalForm();
                return;
            }
            return;
        }
        if(left instanceof F){
            F l = (F) left;
            switch(l.name){
                case F.LN:{
                    left = l.X[0];
                    right = new F(F.EXP, right).FACTOR(ring);
                    right = delABS(right);
                    toNormalForm();
                    return;
                }
                case F.ABS:{
                    if(right instanceof Fraction){
                        Fraction frac = (Fraction)right;
                        if(frac.num instanceof F && ((F)frac.num).name == F.ABS)
                            right = ((F)frac.num).X[0].divide(frac.denom, ring);
                        if(frac.denom instanceof F && ((F)frac.denom).name == F.ABS)
                            right = frac.num.divide(((F)frac.denom).X[0], ring);
                        left = l.X[0];
                        toNormalForm();
                    }
                    if(right instanceof F && ((F)right).name == F.EXP){
                        left = l.X[0];
                        toNormalForm();
                    }
                    if(right instanceof F && ((F)right).name == F.ABS){
                        left = l.X[0];
                        right = ((F)right).X[0];
                        toNormalForm();
                    }
                    if(right instanceof Polynom){
                        //Polynom r = (Polynom)right;
                        //if(r.coeffs.length == 1 && r.powers[0]%2 == 0)
                            left = l.X[0]; 
                        toNormalForm();
                    }
//                    if(l.X[0].subtract(ring.varPolynom[1], ring).isZero(ring))
                    if(l.X[0] instanceof Polynom){
                        left = l.X[0];
                        toNormalForm();
                    }
                    return;
                }
                case F.MULTIPLY:{
                    for (int i = 0; i < l.X.length; i++) {
                        if(l.X[i].isItNumber()){
                            right = right.divide(l.X[i], ring).simplify(ring);
                            left = left.divide(l.X[i], ring).simplify(ring);
                            toNormalForm();
                            return;
                        }
                    }
                }
                case F.DIVIDE:{
                    if(l.X[0].isItNumber()){
                        left = l.X[1];
                        right = l.X[0].divide(right, ring);
                        toNormalForm();
                        return;
                    }
                    if(l.X[1].isItNumber()){
                        left = l.X[0];
                        right = right.multiply(l.X[1], ring);
                        toNormalForm();
                        return;
                    }
                    return;
                }
                case F.EXP:{
                    left = l.X[0];
                    right = new F(F.LN, right);
                    toNormalForm();
                    return;
                }
                case F.ADD:{
                    for (int i = 0; i < l.X.length; i++) {
                        if(l.X[i].isItNumber(ring)){
                            left = left.subtract(l.X[i], ring).simplify(ring);
                            right = right.subtract(l.X[i], ring).simplify(ring);
                            toNormalForm();
                            return;
                        }
                    }
                    return;
                }
                case F.SQRT:{
                    left = l.X[0];
                    right = right.pow(2, ring);
                    toNormalForm();
                    return;
                }
                case F.POW:{
                    left = l.X[0];
                    right = right.pow(ring.numberONE.divide(l.X[1], ring), ring);
                    toNormalForm();
                    return;
                }
                case F.ARCTG:{
                    left = l.X[0];
                    right = new F(F.TG, right);
                    toNormalForm();
                    return;
                }
                case F.SIN:{
                    left = l.X[0];
                    right = new F(F.ARCSIN, right);
                    toNormalForm();
                    return;
                }
                case F.ARCSIN:{
                    left = l.X[0];
                    right = new F(F.SIN, right);
                    toNormalForm();
                    return;
                }
            }
            return;
        }
    }
    public Element delABS(Element e){
        if(e instanceof F){
            F f = (F)e;
            if(f.name == F.ABS) return f.X[0];
            for (int i = 0; i < f.X.length; i++)
                f.X[i] = delABS(f.X[i]);
            return f;
        }
        return e;
    }
    public boolean varX(Element e){
        if(e instanceof F){
            Element[] args = ((F)e).X;
            boolean sep = true;
            for (int i = 0; i < args.length; i++)
                sep &= varX(args[i]);
            return sep;
        }
        if(e instanceof Polynom){
            Polynom p = (Polynom)e;
            int count = p.powers.length/p.coeffs.length;
            if(count == 1) return true;
            boolean sep = true;
            for (int i = 0; i < count; i++)
                for (int j = 0; j < p.powers.length; j += count){
                    if(i == 0) continue;
                    if(i == 1 && p.powers[i+j] > 0) return false;
                }
            return sep;
        }
        if(e instanceof Fraction) return varX(((Fraction)e).num) && varX(((Fraction)e).denom);
        return e.isItNumber();
    }
    public boolean varY(Element e){
        if(e instanceof F){
            Element[] args = ((F)e).X;
            boolean sep = true;
            for (int i = 0; i < args.length; i++)
                sep &= varY(args[i]);
            return sep;
        }
        if(e instanceof Polynom){
            Polynom p = (Polynom)e;
            int count = p.powers.length/p.coeffs.length;
            boolean sep = true;
            if(p.powers.length >= 1 && p.powers[0] > 0) return false;
            for (int i = 0; i < count; i++)
                for (int j = 0; j < p.powers.length; j += count){
                    if(i == 0 && p.powers[i+j] > 0) return false;
                    if(i == 1) continue;
                }
            return sep;
        }
        if(e instanceof Fraction) return varY(((Fraction)e).num) && varY(((Fraction)e).denom);
        if(e instanceof FactorPol){
            FactorPol fp = (FactorPol)e;
            boolean sep = true;
            for (int i = 0; i < fp.multin.length; i++)
                sep &= varY(fp.multin[i]);
            return sep;
        }
        return e.isItNumber();
    }
    /**
     * Проверяет, является ли элемент производной первого порядка.
     * @param e проверяемый элемент
     * @return true, если является.
     */
    public boolean isDerivative(Element e){
        //return(e instanceof F && ((F)e).name == F.d && new DiffEquation().maxDerivative((F)e, ring) == 1);
        return(e instanceof F && ((F)e).name == F.d && ((F)e).X.length == 2);
    }
    
    /**
     * @param e входная функция
     * @param ring кольцо
     * @return измененная функция 
     */
    public Element toHDE(Element e, VectorS val){
        CanonicForms cf = new CanonicForms(ring, true);
        if(e instanceof F){
            F f = (F)e;
            f = cf.ExpandForYourChoise(f, 1, 0, 0, 0, 0);
            if(f.name == F.d && isDerivative(f)){
                return e;
            }
            if(f.name == F.ID) return toHDE(f.X[0], val);
            if(f.name == F.INT) return e;
            for (int i = 0; i < f.X.length; i++)
                f.X[i] = toHDE(f.X[i], val);
            return f;
        }
        if(e instanceof Polynom){
            Element f = ring.numberZERO;
            Polynom[] monoms = ((Polynom)e).toMonomials(ring);
            for (Polynom p : monoms) {
                Element el = p.coeffs[0], mult;
                for (int i = 0; i < p.powers.length; i++) {
                    mult = ring.varPolynom[i].add(val.V[i], ring).pow(p.powers[i], ring);//(x+a)^p (y+b)^q
                    el = el.multiply(mult, ring);
                }
                f = f.add(el, ring);
            }
            return f;
        }
        if(e instanceof Fraction){
            Fraction f = (Fraction)e;
            f.num = toHDE(f.num, val);
            f.denom = toHDE(f.denom, val);
            return f;
        }
        if(e.isItNumber()) return e;
        return ring.numberZERO;
    }
}
