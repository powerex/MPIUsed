/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

// © Разработчик Смирнов Роман Антонович (White_Raven), ТГУ'11
package com.mathpar.lcTransform;
import com.mathpar.func.*;
import java.util.ArrayList;

import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 * Находим решение путем обратного преобразования Лапласа-Карсона
 * @author white_raven
 * @version 0.3
 */
public class inversLCTransform {

    Ring ring; // входное кольцо
    boolean flagIC=true;// флаг на наличие начальных условий

    int num_ic=0;// колличество начальных условий в уравнении
    int index_ic_inRing=0;// позиция начала начальных условий в кольце
    int num_parametrs=0;// колличество комплексных параметров в изображении
    Element[] valueVars;

    public inversLCTransform(Ring r) {
        ring = r;
    }

    public inversLCTransform(int n, int i, boolean flag, int p, Ring r) {
        num_ic = n;
        index_ic_inRing = i;
        flagIC = flag;
        ring = r;
        num_parametrs = p;
        valueVars = new Element[p];
    }

    /**
     * Процедура, проверяющая, является ли один из нулей знаменателя проблемным
     * f.e. : p-q, pq-1, p^2-q, p^2-q^2, p^2+q^2 etc.
     * @param p - ноль знаменателя изображающего уравнения
     * @return true | false
     */
    private boolean chekImageDenomRoot(Polynom p) {
        if (p.isItNumber()) {// связано с наличием констант при факторизации в FactorPol
            return false;
        }
        Complex res = (Complex) p.value(valueVars, ring); // если один из мономов действительной части меньше нуля, то возвращаем true
        Polynom Re = (Polynom) res.re;
        // проверка на случай (x-y)^2 и т.д., когда один из коэф-тов отрицателен но весь полином не может быть меньше нуля ????
        for (int i = 0; i < Re.coeffs.length; i++) {
            if (Re.coeffs[i].isNegative()) {
                return true;
            }
        }
        return false;
    }


 //===========================  Преобразования =====================================
    /**
     * Процедура вычисляющая обратное преобразование Лапласа-Карсона по изображению искомой функции el
     * @param el - изображающее уравнение искомой функции
     * @return значение функции в общем виде
     */
    public Element inverse_Laplace_Carcon_transform_without_IC(Element el) {
    //    Ring temp = new CanonicForms(new Ring(ring.toString().charAt(0)+"[_f$_]")).addNewVariable(num_parametrs * 2 + 1);
    Ring temp=new Ring(ring); temp.CForm.makeWorkRing(num_parametrs * 2 + 2);
    temp=temp.CForm.newRing;  temp.varNames[0]="_f$_";
        int index = 0;
        for (int i = 0; i < valueVars.length; i++) {
            valueVars[i] = new Complex(temp.varPolynom[index], temp.varPolynom[index + 1]);
            index += 2;
        }
        FactorPol denom = ( (Polynom) ( (Fraction) el ).denom ).factorOfPol_inQ(false, ring);
        ArrayList<Element> rootsDenom = new ArrayList<Element>();
        for (int i = 0; i < denom.multin.length; i++) {
            if (chekImageDenomRoot(denom.multin[i])) {
                rootsDenom.add(denom.multin[i]);
            }
        }
        if(rootsDenom.isEmpty()) return new F(F.INVERSELC,el); // если в знаменателе нет нулей



      return null;
    }











    public Element invers_Laplace_Carcon_transform(Element el){

    return null;
    }





    Element invers_work(Fraction el){
      if (el.isOne(ring)) return ring.numberONE;
      if(el.num.isOne(ring)){
       if(el.denom instanceof Polynom){
          return new LCTransform(ring,true).transformRightPart(el.denom).inverse(ring);
          }
      }
      if(el.num.isMinusOne(ring)){
       if(el.denom instanceof Polynom){
          return new LCTransform(ring,true).transformRightPart(el.denom).inverse(ring).negate(ring);
          }
      }

     return null;
     }



//==================================================================================
    Element work_with_Divide(Element num, Element denom) {

        // if(denom instanceof Polynom){
        //  denom=((Polynom)denom).factorOfPol_inQ(ring);
        // }
//        if(num instanceof Polynom & denom instanceof Polynom){
//        Element[]  resImage=((Polynom)num).divideExt((Polynom)denom, ring);
//         Element searchFunc=ring.numberONE;
////         for(int i=0;i<resImage.length;i++){
////           if(resImage[i].numbElementType()<Ring.Polynom) searchFunc=searchFunc.add(resImage[i], ring);else
////               searchFunc=searchFunc.add(invers_work((Fraction)resImage[i]), ring);
////         }
//        return searchFunc;
//        }



        Element[] Res = null;
        if (num instanceof Polynom) {
            if (((Polynom) num).coeffs.length > 1) {
                Polynom[] monoms =  ring.CForm.dividePolynomToMonoms(((Polynom) num));
                Res = new Element[monoms.length];
                for (int i = 0; i < monoms.length; i++) {
                    Fraction temp; // = new Fraction(monoms[i], denom).cancel(ring);

                    Element Eh= new Fraction(monoms[i], denom).cancel(ring);
                  if (Eh instanceof Fraction) temp=(Fraction)Eh; else temp=new Fraction(Eh, ring.numberONE);
                    Res[i] = invers_work(temp);
                }
            }
        }
        return new F(F.ADD, Res);
    }



    public Element workILCTransform(Element LCfunc) {
        if (LCfunc instanceof Fraction) {
        }
        if (LCfunc instanceof F) {
            switch (((F) LCfunc).name) {
                case F.DIVIDE:
                    return work_with_Divide(((F) LCfunc).X[0], ((F) LCfunc).X[1]);
            }
        }
        return null;
    }

}
