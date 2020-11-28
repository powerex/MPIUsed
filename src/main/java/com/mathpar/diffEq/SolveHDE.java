package com.mathpar.diffEq;

import com.mathpar.func.CanonicForms;
import com.mathpar.func.F;
import com.mathpar.func.Fname;
import com.mathpar.number.Element;
import com.mathpar.number.Fraction;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
import com.mathpar.polynom.Polynom;
import com.mathpar.webWithout.Play_On_Ground;

/** Символьное решение однородных диф уравнений первого порядка.
 * Производит замену y(x) = y(x)*x, после чего получается уравнение с разд.
 * переменными, которое подается на вход функции решения уравнений такого типа.
 * Получаем решение типа VectorS.
 * @author Sergey Glazkov
 */
public class SolveHDE {
    Ring ring = new Ring("Q[x,y]");//Кольцо целых чисел
    Element rightpart = new Element(), leftpart = new Element();//Правая и левая части
    CanonicForms cf = new CanonicForms(ring, true);
    Polynom x = ring.varPolynom[0]; //
    Polynom y = ring.varPolynom[1]; //для удобства работы с переменными
    boolean logged = false;
    ProcedureSDE pr = new ProcedureSDE();
    
    /**
     * Делает замену y(x) на y(x)*x 
     * @param e входная функция
     * @param ring кольцо
     * @return измененная функция 
     */
    private Element replace1(Element e, Ring ring){
        if(e instanceof F){
            F f = (F)e;
            f = cf.ExpandForYourChoise(f, 1, 0, 0, 0, 0);
            if(f.name == F.d && pr.isDerivative(f)){
                F r = new F("\\d(y,x)x+y", ring);
                return r;
            }
            if(f.name == F.ID) return replace1(f.X[0], ring);
            if(f.name == F.INT) return e;
            for (int i = 0; i < f.X.length; i++)
                f.X[i] = replace1(f.X[i], ring);
            return f;
        }
        if(e instanceof Polynom){
            Element f = ring.numberZERO;
            Polynom[] monoms = ((Polynom)e).toMonomials(ring);
            for (Polynom p : monoms) {
                if(p.powers.length < 2) f = f.add(p, ring);
                else{
                    p.powers[0] += p.powers[1];
                    f = f.add(p, ring);
                }
            }
            return f;
        }
        if(e instanceof Fraction){
            Fraction f = (Fraction)e;
            f.num = replace1(f.num, ring);
            f.denom = replace1(f.denom, ring);
            return f;
        }
        if(e.isItNumber()) return e;
        return ring.numberZERO;
    }
    /**
     * Делает замену y(x) на y(x)/x
     * @param e входная функция
     * @param ring кольцо
     * @return измененная функция
     */
    private Element replace2(Element e, Ring ring){
        if(e instanceof F){
            F f = (F)e;
            f = cf.ExpandForYourChoise(f, 1, 0, 0, 0, 0);
            if(f.name == F.ID) return replace2(f.X[0], ring);
            if(f.name == F.INT) return new Fname(e + "\\ | y → y/x");
            for (int i = 0; i < f.X.length; i++){
                f.X[i] = replace2(f.X[i], ring);
            }
            return f;
        }
        if(e instanceof Polynom){
            Element f = ring.numberZERO;
            Polynom[] monoms = ((Polynom)e).toMonomials(ring);
            for (Polynom p : monoms) {
                if(p.powers.length < 2) f = f.add(p, ring);
                else{ 
                    Element div = x.pow(p.powers[1], ring);
                    f = f.add(p.divide(div, ring), ring);
                }
            }
            return f;
        }
        if(e instanceof Fname){
            return e;
        }
        if(e instanceof Fraction){
            Fraction f = (Fraction)e;
            f.num = replace2(f.num, ring);
            f.denom = replace2(f.denom, ring);
            return f;
        }
        if(e.isItNumber()) return e;
        return ring.numberZERO;
    } 
    public Element solve(Element f1, Element f2){
        //System.out.println(" >>> Вход: "+f1+" = "+f2);
        leftpart = replace1(f1, ring).simplify(ring);//
        rightpart = replace1(f2, ring).simplify(ring);//делаем замены y=yx
        //System.out.println("Замена и упрощение: "+leftpart+" = "+rightpart);//получили уравнение с разд.пер.
        
        //получаем решение ур-я с разд.переменными
        VectorS result = (VectorS)new SolveDESV().solve(leftpart, rightpart);
        if(result.V.length != 2) return result;//если размер решения != 2, вернем ошибку
        //System.out.println(" >>> Решение без замены: "+result);
        
        cf.cleanLists();
        leftpart = replace2(result.V[0], ring);//
        rightpart = replace2(result.V[1], ring);//делаем обратную замену
        
        result = new VectorS(new Element[]{leftpart, rightpart});
        //System.out.println(" >>> Решение после замены: "+result);
        
        return result;//возвращаем решение
    }
    
    public static void main(String[] args) throws Exception {
        String f1 = "SPACE=Q[x,y];\\solveDE(\\d(y,x) = y^2+x^2+y);";
        Play_On_Ground.startDebug(f1);
    }
}