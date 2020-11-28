/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import java.util.ArrayList;
import java.util.Vector;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 *
 * @author kamp
 */
public class IntegrateFractions {

  public IntegrateFractions() {
  }
  /**
   *

   * на первом месте - a
   * на втором месте - b
   * на третем месте - b'
   */
/**
 * Тождество Безу, применяемое к знаменателю дробно-рац. функции zn и его
 * производной Dzn, в случак, когда zn не имеет кратных сомножителей,
 * Существуют a и b удовлетворяющие соотношению a*zn+b*Dzn = 1, где Dzn = w/v
 *
 * @param zn  полином
 * @param Dzn производная полинома zn
 * @param cf представитель класса CanonicForms
 * @return  [a,b,b'] коэффициенты в тождестве Безу и
 *                    производная от коэффициента при производной.
 * @throws Exception
 */
  public Element[] EqvalsBezu(Polynom zn, Element Dzn, int varNumb, Ring ring) throws Exception {
    Polynom w = null;
    Element v = null;
    if(Dzn instanceof F && ((F) Dzn).name == F.DIVIDE){
      Element ell = ring.CForm.ElementConvertToPolynom(((F) Dzn).X[0]);//ElementToPolynom(((F)Dzn).X[0], false);
      w = ell instanceof Polynom ? (Polynom) ell : new Polynom(ell);
      v = ((F) Dzn).X[1];
    }else{
      Element ell = ring.CForm.ElementConvertToPolynom(Dzn);
      w = ell instanceof Polynom ? (Polynom) ell : new Polynom(ell);
    }//ElementToPolynom(Dzn, false);

    Element[] bezu = new Element[3];
    if(w.powers.length == 0){
      bezu[0] = NumberZ.ZERO;
      if(v == null){
        if(w.isOne(ring.CForm.RING)){
          bezu[1] = NumberZ.ONE;
        }else{
          bezu[1] = new Fraction(NumberZ.ONE, w);
        }
        bezu[2] = NumberZ.ZERO;
      }else{
        bezu[1] = new F(F.DIVIDE, new Element[]{v, w}).expand(ring.CForm.RING);
        bezu[2] = new Integrate().DStructurTheorem(bezu[1], varNumb, ring.CForm.RING);
      }
    }else{
      VectorS gcd = zn.extendedGCD(w, ring.CForm.newRing);
      if(gcd.V.length > 5) {
        bezu[0] = new F(F.DIVIDE, new Element[]{gcd.V[1],
                gcd.V[5].multiply(gcd.V[3], ring.CForm.newRing)});
        bezu[1] = new F(F.DIVIDE, new Element[]{gcd.V[2],
                gcd.V[5].multiply(gcd.V[4], ring.CForm.newRing)});
      } else {
        bezu[0] = gcd.V[1];
        bezu[1] = gcd.V[2];
      }
      if(v == null){
        bezu[2] = bezu[1].D(ring.CForm.newRing);
      }else{
        bezu[1] = v.multiply(ring.CForm.ElementToF(bezu[1]), ring.CForm.RING).expand(
                ring.CForm.RING);
        bezu[2] = new Integrate().DStructurTheorem(bezu[1], varNumb, ring.CForm.RING);
      }
    }
    return bezu;
  }

  public Element[] extendedEvclid(Element q, Element r, Ring ring) throws Exception {
    Element[] Q = new Element[]{NumberZ.ZERO, NumberZ.ONE};
    Element[] R = new Element[]{NumberZ.ONE, NumberZ.ZERO};
    while(!r.isZero(ring)){
      Element div = q.divide(r, ring).expand(ring);
      Element t = NumberZ.ZERO;
      Element part = NumberZ.ONE;
      if(((F) div).name == F.DIVIDE){
        Element pol = ring.CForm.ElementConvertToPolynom(((F) div).X[0]);
        Polynom p1 = pol instanceof Polynom ? (Polynom) pol : new Polynom(pol);
        pol = ring.CForm.ElementConvertToPolynom(((F) div).X[1]);
        Polynom p2 = pol instanceof Polynom ? (Polynom) pol : new Polynom(pol);
        Element[] rat = p1.divAndRemToRational(p2, ring.CForm.newRing);
        part = ring.CForm.ElementToF(rat[0]);
        t = ring.CForm.ElementToF(rat[1]);
      }
      Element[] T = new Element[]{Q[0].subtract(part.multiply(R[0], ring), ring),
        Q[1].subtract(part.multiply(R[1], ring), ring)};
      q = r;
      r = t;
      Q = R;
      R = T;
    }
    return new Element[]{q, Q[0], Q[1]};
  }

  public Ring NormalVarRing(CanonicForms cf) {
    String sring = cf.RING.toString().replace("]", "," + cf.List_of_Change.
            toString().substring(1));
    return new Ring(sring);
  }
  
    
  /**
   * Разбиение дроби num/denom в сумму простых дробей.
   * 
   * Пусть denom = (x+a)^m*...*(x^2+bx+c)^k - разложение
   * denom на свободные от квадратов множители.
   * 
   * Представляем num/denom в виде:
   * 
   * num/denom = A[0]/(x+a) + A[1]/(x+a)^2 + ... + A[m]/(x+a)^{m} + ... +
   * + (A[n]+x*A[n+1] )/(x^2+bx+c) + (A[n+2] + x*A[n+3] )/(x^2+bx+c)^2 + ... +
   * + (A[n+2k-1]+x*A[n+2k])/(x^2+bx+c)^k,                                     (1)
   * где A[i] - константы.
   * 
   * Находим A[i] при помощи метода неопределенных коэффициентов.
   * Если привести правую часть к общему знаменателю (он будет равен denom),
   * то в числителе получится полином с неизвестными константами
   * A[i]. Обозначим этот полином pol_num.
   * Добавляем неизвестные константы в кольцо. Будем рассматривать 
   * полином pol_num как полином от переменных
   * x, A[0], ..., A[n+2k]. Вычтем из него num и передадим в метод
   * getValueOfVeriabl. getValueOfVeriabl вернет значения
   * переменных A[i], если такие существуют,
   * при которых выполняется pol_num - num = 0.
   * Если таких констант A[i],
   * что pol_num - num = 0, не существует, то возвращаеммассив [num, denom].
   * Если константы A[i] были найдены, то преобразуем num/denom к виду:
   * 
   * num/denom = (A[0]*(x+a)^(m-1)+A[1]*(x+a)^(m-2)+...+A[m])/(x+a)^m + ... +
   * + ((A[n]+x*A[n+1])*(x^2+bx+c)^(k-1)+(A[n-2]+x*A[n-3])*(x^2+bx+c)^(k-2) + ... +
   * + (A[n+2k-1]+x*A[n+2k]))/(x^2+bx+c)^k.
   * Возвращаем массив, состоящий из числителей полученного разложения,
   * в последнем элементе содержащий разложение denom на свободные
   * от квадратов множители:
   * [A[0]*(x+a)^(m-1)+A[1]*(x+a)^(m-2)+...+A[m], ...,
   * (A[n]+x*A[n+1])*(x^2+bx+c)^(k-1)+...+A[n+2k-1]+x*A[n+2k],
   * (x+a)^m*...*(x^2+bx+c)^k]
   * 
   * 
   * polZn - разбиение denom на свободные от квадратов множители.
   * n_var - номер старшей переменной.
   * var - имя старшей переменной.
   * 
   */
  public Object[] partialFractionOld(FactorPol polZn, Element num, Polynom denom, int n_var, Fname var, Ring ring) throws Exception {
    Polynom chr = num instanceof Polynom ? (Polynom) num : new Polynom(num);
    polZn.normalForm(ring);
    if(polZn.multin.length == 1){
      return new Object[]{new Element[]{ring.CForm.ElementToF(chr)}, polZn};
    }
    // lenCol - количество неизвестных констант A[i]
    // lenRow - количество мономов в полиноме pol_num
    // (обозначения см. в коментарии к этому методу.)
    int maxpow = 0, lenRow = 0, lenCol = 0, n_array = 0;
    for(int i = 0; i < polZn.powers.length; i++){
      maxpow = (polZn.multin[i]).degree(n_var);
      if(maxpow > 0){
        n_array += polZn.powers[i];
        lenCol += polZn.powers[i] * maxpow;
      }else{
        denom = denom.divideExact((Polynom) polZn.multin[i], ring);
      }
    }
    Polynom[] array_pol = new Polynom[n_array];
    n_array = 0;
    for(int i = 0; i < polZn.powers.length; i++){
      Polynom denum = denom;
      maxpow = (polZn.multin[i]).degree(n_var);
      if(maxpow > 0){
        for(int j = 0; j < polZn.powers[i]; j++){
          array_pol[n_array] = denum.divideExact(polZn.multin[i], ring);
          denum = array_pol[n_array];
          lenRow += array_pol[n_array].coeffs.length;
          if(maxpow == 2){
            lenRow += array_pol[n_array].coeffs.length;
          }
          n_array++;
        }
      }
    }
    int[][] intpow = new int[lenRow][lenCol + ring.CForm.newRing.varPolynom.length];
    Element[] newcoeff = new Element[lenRow];
    String sRing = "";
    int k = 0, row = 0;
    n_array = 0;
    int rjj = ring.CForm.newRing.varPolynom.length;
    Polynom zapom = Polynom.polynom_one(NumberZ.ONE);
    Vector<Integer> number = new Vector<Integer>();
    for(int i = 0; i < polZn.multin.length; i++){
      Polynom mul_i = (Polynom) polZn.multin[i];
      int deg = (mul_i).degree(n_var);
      if(deg <= 0){
        Polynom pol = mul_i;
        for(int j = 1; j < polZn.powers[i]; j++){
          pol = pol.multiply(mul_i, ring);
        }
        zapom = zapom.multiply(pol, ring);
        number.add(i);
      }else{
        for(int j = 0; j < polZn.powers[i]; j++){
          Polynom denum = array_pol[n_array];
          n_array++;
          int jj = denum.powers.length / denum.coeffs.length, kk = 0;
          sRing += "a" + k + ",";
          for(int ii = 0; ii < denum.coeffs.length; ii++){
            System.arraycopy(denum.powers, kk, intpow[row], 0, jj);
            intpow[row][k + rjj] = 1;
            kk += jj;
            newcoeff[row] = denum.coeffs[ii];
            row++;
          }
          k++;
          if((mul_i).degree(n_var) == 2){
            sRing += "a" + k + ",";
            kk = 0;
            for(int ii = 0; ii < denum.coeffs.length; ii++){
              System.arraycopy(denum.powers, kk, intpow[row], 0, jj);
              intpow[row][k + rjj] = 1;
              intpow[row][n_var] = intpow[row][n_var] + 1;
              newcoeff[row] = denum.coeffs[ii];
              kk += jj;
              lenRow++;
              row++;
            }
            k++;
          }
        }
      }
    }
    for(int i = 0; i < number.size(); i++){
      polZn = polZn.deliteMultin(number.get(i));
    }
    int[] newintpow = new int[intpow.length * intpow[0].length];
    k = 0;
    for(int i = 0; i < intpow.length; i++){
      System.arraycopy(intpow[i], 0, newintpow, k, intpow[0].length);
      k += intpow[0].length;
    }
    Ring r = new Ring(ring.toString().replace("]", "," + sRing.substring(0,
            sRing.length() - 1)) + "]");
    Polynom ppp = new Polynom(newintpow, newcoeff, r);
    Vector<Element> X = new Vector<Element>();
    ppp = ppp.subtract(chr, r);
    Element[] el_chr = new Element[polZn.multin.length];
    if(new StrucTheorem().getValueOfVeriabl(ppp, X, lenCol, n_var, ring)){
      int kk = 0;
      for(int i = 0; i < polZn.multin.length; i++){
        Element el = ring.CForm.ElementToF(polZn.multin[i]);
        maxpow = ((Polynom) polZn.multin[i]).degree(n_var);
        if(maxpow > 0){
          el_chr[i] = NumberZ.ZERO;
          if(maxpow == 2){
        	Element el2 = ring.numberONE;
            kk += polZn.powers[i] * 2 - 1;
            el_chr[i] = el_chr[i].add(X.get(kk).multiply(var.X[0], ring),
                    ring).add(X.get(kk - 1), ring);
            kk = kk - 2;
            for(int j = polZn.powers[i] - 2; j >= 0; j--){
              el2 = el2.multiply(el, ring);
              el_chr[i] = el_chr[i].add((X.get(kk).multiply(var.X[0], ring).
                      add(X.get(kk - 1), ring)).multiply(el2, ring),
                      ring);
              kk = kk - 2;
            }
            kk += polZn.powers[i] * 2;
          }
          if(maxpow == 1){
        	Element el2 = ring.numberONE;
            el_chr[i] = el_chr[i].add(X.get(kk + polZn.powers[i] - 1), ring);
            for(int j = polZn.powers[i] - 2; j >= 0; j--){
              el2 = el2.multiply(el, ring);
              el_chr[i] = el_chr[i].add(X.get(kk + j).multiply(el2, ring),
                      ring);
            }
            kk += polZn.powers[i];
          }
          if(!zapom.isOne(ring)){
            el_chr[i] = el_chr[i].divide(ring.CForm.ElementToF(zapom), ring);
          }
        }
      }
    }else{
      el_chr = new Element[]{
          ring.CForm.ElementToF(
                  (zapom.isOne(ring))? chr: chr.divide(zapom, ring)
          )
      };
      polZn = new FactorPol(denom, ring);
    }
    return new Object[]{el_chr, polZn};
  }
  
  
  /**
   * Разбиение дроби num/denom в сумму простых дробей.
   * 
   * 
   * Применяем формулу:
   * c/(pq) = cB/p + cA/q,
   * где A и B --- такие многочлены, что Ap + Bq = 1.
   * 
   * Проверяем, являются ли дроби cB/p и cA/q правильными.
   * Если какая-то из дробей не правильная, то выделяем целую часть.
   * 
   * Если в знаменателе больше двух сомножителей, например 4,
   * то применяем описанный выше алгоритм следующим образом:
   * 
   * с/(p1*p2*p3*p4) = a1/p1 + b2/(p2*p3*p4)
   * b2/(p2*p3*p4) = a2/p2 + b3/(p3*p4)
   * b3/(p3*p4) = a3/p3 + a4/p4
   * 
   * 
   * @param polZn
   * @param num
   * @param denom
   * @param n_var
   * @param ring
   * @return Object[]{числители,   знаменатели,   сумма всех целых частей от неправильных дробей}
   * @throws Exception 
   */
  public Object[] partialFraction(FactorPol polZn, Element num, Polynom denom, int n_var, Ring ring) throws Exception {
      Element el = Polynom.polynom_one(NumberZ.ONE);
      Vector<Integer> number = new Vector<Integer>();
      for(int i = 0; i < polZn.multin.length; i++) {
          if(polZn.multin[i].degree(n_var) <= 0) {
              el = el.multiply(polZn.multin[i].pow(polZn.powers[i], ring), ring);
              number.add(i);
          }
      }
      for(int i = 0; i < number.size(); i++){
          polZn = polZn.deliteMultin(number.get(i));
      }
      
      Element[] nums = new Element[polZn.multin.length];
      
      if(polZn.multin.length == 1) {
          nums[0] = (el.isOne(ring))? num: num.divide(el, ring);
          return new Object[]{nums, polZn, ring.numberZERO};
      }
      
      
      Element polPart = ring.numberZERO;
      Polynom newDenom = denom;
      Element newNum = num;
      for(int i = 0; i < polZn.multin.length - 1; i++) {
          Polynom mult = (Polynom) polZn.multin[i].pow(polZn.powers[i], ring);
          newDenom = (Polynom) newDenom.divide(mult, ring);
          Element[] bezu = new Element[2];
          VectorS gcd = mult.extendedGCD(newDenom, ring.CForm.newRing);
          if(gcd.V.length > 5) {
              bezu[0] = new F(F.DIVIDE, new Element[]{gcd.V[1],
                  gcd.V[5].multiply(gcd.V[3], ring.CForm.newRing)});
              bezu[1] = new F(F.DIVIDE, new Element[]{gcd.V[2],
                  gcd.V[5].multiply(gcd.V[4], ring.CForm.newRing)});
          } else {
              bezu[0] = gcd.V[1];
              bezu[1] = gcd.V[2];
          }
          nums[i] = bezu[1].multiply(newNum, ring);
          if( ( nums[i] instanceof Polynom ) &&
              ( ((Polynom)nums[i]).degree(n_var) >= mult.degree(n_var) ) ) {
              Element[] rat = ((Polynom)nums[i]).quotientAndRemainder(mult, n_var, ring.CForm.newRing);
              nums[i] = rat[1];
              polPart = polPart.add(rat[0], ring);
          }
          newNum = bezu[0].multiply(newNum, ring);
          if( ( newNum instanceof Polynom ) &&
              ( ((Polynom)newNum).degree(n_var) >= newDenom.degree(n_var) ) ) {
              Element[] rat = ((Polynom)newNum).quotientAndRemainder(newDenom, n_var, ring.CForm.newRing);
              newNum = rat[1];
              polPart = polPart.add(rat[0], ring);
          }
      }
      nums[nums.length - 1] = newNum;
      
      return new Object[]{nums, polZn, polPart};
  }

  
    public Element canonicForm(Element el, Ring ring) { // old variant
    if(el instanceof F){
      F f = (F) el;
      switch(f.name){
        case F.POW:
        case F.intPOW: {
          if(f.X[1].intValue() < 0){
            return new F(F.DIVIDE, new Element[]{NumberZ.ONE, new F(F.intPOW,
                      new Element[]{canonicForm(f.X[0], ring), f.X[1].abs(ring).
                        expand(ring)})});
          }
          if(f.X[1] instanceof Complex && ((Complex) f.X[1]).compareTo(
                  ring.numberMINUS_I, ring) <= 0){
            return new F(F.DIVIDE, new Element[]{NumberZ.ONE, new F(F.POW,
                      new Element[]{canonicForm(f.X[0], ring),
                        ((Complex) f.X[1]).conjugate(ring)})});//was POW
          }
          return new F(f.name, new Element[]{canonicForm(f.X[0], ring), f.X[1]}).
                  expand(ring);
        }
        case F.MULTIPLY:
        case F.ADD:
        case F.SUBTRACT: 
        case F.DIVIDE:  {
          Element[] els = new Element[f.X.length];
          for(int i = 0; i < f.X.length; i++){
            els[i] = canonicForm(f.X[i], ring);
          }
          return new F(f.name, els).expand(ring);
        }
      }
    }
    return el;
  }

  public Element canonicForm1111(Element el, Ring ring) {  // my - new - june - 2017
    if(el instanceof F){ 
      F f = (F) el; 
      switch(f.name){
        case F.POW:
        case F.intPOW: {
           if(f.X[0] instanceof F){F f0=(F)f.X[0]; int nam=f0.name;
               if(nam==F.EXP){Element ep=f0.X[0].multiply(f.X[1], ring).expand(ring);return new F(F.EXP, ep);}
               else if ((nam==F.POW)||(nam==F.intPOW)){
                   int newFn=((f.name==F.intPOW)||(nam==F.intPOW))? F.intPOW:F.POW;
                   Element ep=f0.X[1].multiply(f.X[1], ring).expand(ring);return new F(newFn, f0.X[0],ep);
               }}
           Element ee=canonicForm(f.X[0], ring);
          if(f.X[1].isNegative()) 
            return new F(F.DIVIDE, new Element[]{NumberZ.ONE,   
                (f.X[1].isMinusOne(ring))? ee: new F(f.name, new Element[]{ee, f.X[1].abs(ring).
                        expand(ring)})});
          return new F(f.name, new Element[]{ee, f.X[1]}).expand(ring);
        }
        case F.MULTIPLY:
        case F.ADD:
        case F.SUBTRACT: 
        case F.DIVIDE:  {
          Element[] els = new Element[f.X.length];
          for(int i = 0; i < f.X.length; i++){
            els[i] = canonicForm(f.X[i], ring);
          }
          return new F(f.name, els).expand(ring);
        }
      }
    }
    return el;
  }
    /**
     * Я думаю, что тут выделяется целая часть дроби и правильная дробь по переменной arg
     * у дроби frac
     * @param frac
     * @param simbolName
     * @param arg
     * @param cfFrac
     * @return [ Integer Part, Numerator of fraction part,
     *                  Denominator of fraction part, variable]
     * @throws Exception
     */
  public Element[] canonicForm(Element frac, ArrayList<Fname> simbolName, Element arg, CanonicForms cfFrac) throws Exception {
    Element polPart = null, num = null, denom = null;
    Ring ring = cfFrac.RING;
    if(frac instanceof F){num = ((F) frac).X[0]; denom = ((F) frac).X[1]; }
    if(frac instanceof Fraction){num=((Fraction) frac).num;denom=((Fraction) frac).denom;}
    Fname var = new IntPolynom().getLastRegularMonomial(denom, simbolName, arg, ring);
    Fname var_num = new IntPolynom().getLastRegularMonomial(num, simbolName,arg, ring);
    if(var_num.X[0] instanceof F && (((F) var_num.X[0]).name != F.SQRT && ((F) var_num.X[0]).name != F.CUBRT) && simbolName.
            indexOf(var) < simbolName.indexOf(var_num)){
      polPart = new IntPolynom().polPartInteg(frac, var_num, simbolName,arg, ring);
      return new Element[]{polPart, null, null, var_num};
    }
    if(var.X[0] instanceof F && ((F) var.X[0]).name == F.EXP){
      F eldiv = new F(F.DIVIDE, new Element[]{canonicForm(num, ring),
                canonicForm(denom, ring)}).expand(ring);
      num = eldiv.X[0]; denom = eldiv.X[1];
    }
    Element polNum = cfFrac.ElementConvertToPolynom(num);
    Element polDenom = cfFrac.ElementConvertToPolynom(denom);
    if(var_num.X[0] instanceof F && (((F) var_num.X[0]).name == F.SQRT || ((F) var_num.X[0]).name == F.CUBRT)){
      return new Element[]{null, num, denom, var_num};
    }
    if(!(polDenom instanceof Polynom)&&!(polDenom instanceof Fraction)){
      return new Element[]{new IntPolynom().polPartInteg(frac, new IntPolynom().getLastRegularMonomial(num, simbolName, arg, ring), simbolName,arg, ring )};
    }
    int n_var=0;
      for ( ; n_var < ring.varNames.length; n_var++)
           if(var.name.equals(ring.varNames[n_var]))  break;
    if(var.X[0] instanceof F){
      int ind = cfFrac.List_of_Change.indexOf(var);
      if(ind < 0){
        for(int i = 0; i < cfFrac.List_of_Change.size(); i++){
          Element elList = cfFrac.List_of_Change.get(i);
          if(elList instanceof F && ((F) elList).name == F.POW && ((F) elList).X[0].
                  compareTo(var, ring) == 0){
            n_var = i + ring.varPolynom.length;
            break;
          }
        }
      }else{
        n_var = ind + ring.varPolynom.length;
      }
    }

    if(polNum instanceof Polynom && polDenom instanceof Polynom && ((Polynom) polNum).degree(n_var) >= ((Polynom) polDenom).degree(n_var)){


        Element[] rat = ((Polynom) polNum).quotientAndRemainder((Polynom) polDenom, n_var,
              cfFrac.newRing);
      Fname name = new IntPolynom().getLastRegularMonomial(rat[0], simbolName, arg, cfFrac.newRing);
      Element convert = rat[0];
      if(var.X[0] instanceof F){
        convert = cfFrac.ElementToF(rat[0]);//Convert_Polynom_to_F(rat[0]);
      }
      polPart = new IntPolynom().polPartInteg(convert, name, simbolName, arg, ring);
      polNum = rat[1];
      if(rat[1] instanceof Fraction){
        polNum = ((Fraction) rat[1]).num;
        polDenom = polDenom.multiply(((Fraction) rat[1]).denom, ring);
      }
    }
    if(polDenom instanceof Fraction) {
      polNum = ((Fraction)polDenom).denom.multiply(polNum, ring);
      polDenom = ((Fraction)polDenom).num;
    }
    return new Element[]{polPart, polNum, polDenom, var};
  }
/**resultant of two polynomials for variable n_var
 * 
 * @param f one polynomial
 * @param g other polynomial
 * @param n_var - the main variable (position in the ring)
 * @param ring Ring
 * @return polynomial, which is the resultant (GCD) of f and g by variable n_var.
 */
  public Polynom resultant(Polynom f, Polynom g, int n_var, Ring ring) {
    int n = f.degree(n_var), m = g.degree(n_var); 
    if(n < 0)n = 0;
    if(m < 0)m = 0;
    if(n > m){// We want to start when deg g >= deg f.
       Polynom ress= resultant(g, f, n_var, ring);
       if((n*m%2)==1) ress=(Polynom)ress.negate(ring);
       return ress;
    }else{    // general case
      Polynom a_n = f.coefOfHightVar(n_var, ring); //lc(f)
      if(n == 0){return a_n.powNewS(m, ring);//a_n^m
      }else{ // остаток от деление g на f
        Element el = remToRational(g, f, n_var, ring);//g.remToRational(f, ring);
        if(el instanceof Fraction) el = ((Fraction) el).cancel(ring);
        if(el.isZero(ring))return new Polynom(NumberZ.ZERO);
        else{
          Polynom h = null;
            if(el instanceof Fraction){
              h = ((Fraction) el).num instanceof Polynom 
                    ? (Polynom) ((Fraction) el).num 
                    : new Polynom(((Fraction) el).num);
            }else h = el instanceof Polynom ? (Polynom) el : new Polynom(el);
          int p = h.degree(n_var);
          int pow = p < 0 ? m : m - p;
          return a_n.powNewS(pow, ring).multiply(resultant(f, h, n_var, ring),
                  ring);
        }
      }
    }
  }

  public Polynom polPowersInArray(Polynom pol, int n, Ring ring) {
    int var = ring.varPolynom.length, k = 0, varP = pol.powers.length / pol.coeffs.length;
    int[][] massPow = new int[pol.coeffs.length][var];
    for(int i = 0; i < pol.coeffs.length; i++){
      System.arraycopy(pol.powers, k, massPow[i], 0, varP);
      k += varP;
    }
    for(int i = 0; i < massPow.length; i++){
      int col = massPow[i][n];
      massPow[i][n] = massPow[i][massPow[i].length - 1];
      massPow[i][massPow[i].length - 1] = col;
    }
    //int[] newpow =
    k = 0;
    int[] poww = new int[var * massPow.length];
    for(int i = 0; i < massPow.length; i++){
      System.arraycopy(massPow[i], 0, poww, k, var);
      k += var;
    }
    return new Polynom(poww, pol.coeffs).ordering(ring);
  }

  /**
   * Процедура нахождения частного, остатка при делении двух полиномов this и
   * q
   *
   * @param q
   *            делитель
   * @return массив полиномов: на нулевом месте стоит частное деления this на
   *         q; на первом - остаток;
   */
  public Element remToRational(Polynom p, Polynom q, int var, Ring ring) {
    p = polPowersInArray(p, var, ring);
    q = polPowersInArray(q, var, ring);//changeOrderOfVars
    Polynom[] pp = p.divAndRem(q, ring);
    if(!pp[1].isZero(ring)){
      pp[1] = polPowersInArray(pp[1], var, ring);
    }
    if(!pp[2].isZero(ring)){
      pp[2] = polPowersInArray(pp[2], var, ring);
    }
    Element end = (new Fraction(pp[1].ordering(ring), pp[2].ordering(ring))).
            cancel(ring);
    return end;
  }

  public Element LogarithmicPartIntegralDivAndRem(Element a, Polynom b, Fname x, int n_var, CanonicForms cf) throws Exception {
//    System.out.println("b = "+b);
//    Polynom bb = b;
//    FactorPol polZn = bb.factorOfPol(cf.newRing);
//    System.out.println("b = "+b);
//    Object[] obj = partialFraction(polZn, a, b, n_var, x, cf);
    Element[] el_chr = new Element[]{a};//(Element[]) obj[0];
    FactorPol polZn_i = new FactorPol(b); //(FactorPol) obj[1];
    Element[] logarithmicPart = new Element[polZn_i.powers.length];
    for(int i = 0; i < polZn_i.powers.length; i++){
      Element zn_i = cf.ElementToF(polZn_i.multin[i]);//Convert_Polynom_to_F(polZn_i.multin[i]);
      Element Db = new Integrate().DStructurTheorem(zn_i, n_var, cf.RING).expand(
              cf.RING);

      F lnCh = new F(F.LN, cf.ElementToF(zn_i));//Convert_Polynom_to_F(zn_i));
      Element num = cf.ElementConvertToPolynom(el_chr[i]);//ElementToPolynom(el_chr[i], false);
      Element Ddenum = cf.ElementConvertToPolynom(Db);//ElementToPolynom(Db, false);
      Element eEEe = null;
      Polynom DdenumPol2 = null;

      if(x.X[0] instanceof F && ((F) x.X[0]).name == F.EXP){
        int ind = cf.List_of_Change.indexOf(x);
        Element arg = ((F) x.X[0]).X[0];
        Element Darg = new Integrate().DStructurTheorem(arg, n_var, cf.RING);
        Darg = cf.ElementConvertToPolynom(Darg);//ElementToPolynom(Darg, false);
        Polynom polDarg = Darg instanceof Polynom ? (Polynom) Darg : new Polynom(
                Darg);
        int N = polZn_i.multin[i].degree(n_var);
        Element pow = new NumberZ(N);
        if(ind < 0){
          for(int ii = 0; ii < cf.List_of_Change.size(); ii++){
            Element elList = cf.List_of_Change.get(ii);
            if(elList instanceof F && ((F) elList).name == F.POW && ((F) elList).X[0].
                    compareTo(x, cf.RING) == 0){
              pow = pow.multiply(((F) elList).X[1], cf.RING);
              break;
            }
          }
        }
        eEEe = pow.multiply(arg, cf.RING);
        pow = cf.ElementConvertToPolynom(pow);//ElementToPolynom(pow, false);
        Polynom polPow = pow instanceof Polynom ? (Polynom) pow : new Polynom(
                pow);
        polDarg = polDarg.multiply(polPow, cf.newRing);
        DdenumPol2 = polDarg.multiply(polZn_i.multin[i], cf.newRing);
      }
      if(num instanceof Fraction && Ddenum instanceof Fraction){
        Fraction fr1 = (Fraction) num;
        Fraction fr2 = (Fraction) Ddenum;
        num = fr1.num.multiply(fr2.denom, cf.newRing);
        Ddenum = fr2.num.multiply(fr1.denom, cf.newRing);
        if(DdenumPol2 != null){
          Polynom elel = fr1.denom instanceof Polynom ? (Polynom) fr1.denom : new Polynom(
                  fr1.denom);
          DdenumPol2 = elel.multiply(DdenumPol2, cf.newRing);
        }
      }
      if(num instanceof Fraction && !(Ddenum instanceof Fraction)){
        Fraction fr1 = (Fraction) num;
        Polynom fr2 = Ddenum instanceof Polynom ? (Polynom) Ddenum : new Polynom(
                Ddenum);
        num = fr1.num;
        Ddenum = fr2.multiply(fr1.denom, cf.newRing);
        if(DdenumPol2 != null){
          Polynom elel = fr1.denom instanceof Polynom ? (Polynom) fr1.denom : new Polynom(
                  fr1.denom);
          DdenumPol2 = elel.multiply(DdenumPol2, cf.newRing);
        }
      }
      if(!(num instanceof Fraction) && Ddenum instanceof Fraction){
        Polynom fr1 = num instanceof Polynom ? (Polynom) num : new Polynom(num);
        Fraction fr2 = (Fraction) Ddenum;
        num = fr1.multiply(fr2.denom, cf.newRing);
        Ddenum = fr2.num;
      }
      Element c = null;
      Polynom numPol = num instanceof Polynom ? (Polynom) num : new Polynom(num);
      Polynom DdenumPol = Ddenum instanceof Polynom ? (Polynom) Ddenum : new Polynom(
              Ddenum);
      if(DdenumPol2 != null){
        DdenumPol = DdenumPol.subtract(DdenumPol2, cf.newRing);
      }

      if(numPol.isItNumber() && DdenumPol.isItNumber()){
        c = new Fraction(numPol, DdenumPol);
      }else{
        Element[] rat = numPol.divAndRemToRational(DdenumPol, cf.newRing);
        if(rat[0] instanceof Fraction){
          if(((Polynom) ((Fraction) rat[0]).num).isItNumber() && ((Polynom) ((Fraction) rat[0]).denom).
                  isItNumber()){
            c = rat[0];
          }else{
            return null;
          }
        }else{
          if(rat[0].ExpandFnameOrId() instanceof Polynom){
            if(!(rat[0].isZero(cf.newRing)) && ((Polynom) rat[0]).isItNumber()){
              c = rat[0];
            }else{
              return null;
            }
          }
        }
      }
      if(x.X[0] instanceof F && (((F) x.X[0]).name == F.EXP || (((F) x.X[0]).name == F.POW && ((F) ((F) x.X[0]).X[0]).name == F.EXP))){
        lnCh = new F(F.SUBTRACT, new Element[]{lnCh, eEEe});
      }
      logarithmicPart[i] = new F(F.MULTIPLY, new Element[]{c, lnCh});
    }
    return new F(F.ADD, logarithmicPart);
  }

  public Element GCD(Polynom q, Polynom r, Fname alfa, int n_var, CanonicForms cf) {
    Ring ring = cf.newRing;
//    q = q.simplify(ring);
//    r = r.simplify(ring);
    int n = q.degree(n_var);
    int m = r.degree(n_var);
    if(n < 0){ n = 0;}
    if(m < 0){ m = 0;}
    if(n < m){
      Polynom t = q;
      q = r;
      r = t;
    }
    Ring rr =cf.newRing;
            //= new Ring("R64" + cf.newRing.toString().substring(cf.newRing.
           // toString().indexOf("[")));
    Element[] els = new Element[rr.varPolynom.length];
    System.arraycopy(rr.varPolynom, 0, els, 0, rr.varPolynom.length - 1);
    els[rr.varPolynom.length - 1] = alfa.X[0];
    if(r.degree(n_var) <= 0){
      Element root = r.toPolynom(rr).value(els, rr);
      if(root.isZero(ring)){
        return q;
      }
    }

    while(!r.isZero(ring)){
      Polynom t = null;
      Element el = remToRational(q.ordering(ring), r.ordering(ring), n_var, ring);
      if(el instanceof Fraction){
        t = ((Fraction) el).num instanceof Polynom ? (Polynom) ((Fraction) el).num : new Polynom(
                ((Fraction) el).num);
      }else{
        t = el instanceof Polynom ? (Polynom) el : new Polynom(el);
      }
      q = r;
      r = t;
      if(!r.isItNumber()){
        Element root = null;
        if(r.degree(n_var) <= 0){
          root = r.value(els, rr);
        }else{
          Polynom pol = r.coefOfHightVar(n_var, cf.newRing);
          root = pol.toPolynom(rr).value(els, rr);
//          root = new solveEq().solvePolynomEq(pol.toPolynom(r64NewR),cf.newRing.varPolynom.length-1,r64NewR);
        }
//        if(root instanceof Fraction) root = ((Fraction)root).cancel(r64NewR);
//        if(!(root instanceof VectorS) && (root.compareTo(alfa.X[0], 0, cf.RING) || root.equals(alfa.X[0], cf.RING)))
        if(root.isZero(rr)){
          r = new Polynom(NumberZ.ZERO);
        }
      }
    }
    return q;
  }

  public Element[] LogarithmicPartIntegralResultantRadical(Element num, Element denum, Element[] w, Fname var, int n_var, Ring ring) {
    //w - bases
   // CanonicForms cfr = new CanonicForms(ring, true);
    Element Ddenum = new Integrate().DStructurTheorem(denum, n_var, ring).expand(ring);
    if(num instanceof Fraction || (num instanceof F && ((F) num).name == F.DIVIDE)){
      if(num instanceof Fraction){
        Fraction fr = (Fraction) num;
        denum = fr.denom.multiply(denum, ring);
        num = fr.num;
      }else{
        F frac = (F) num;
        denum = frac.X[1].multiply(denum, ring);
        num = frac.X[0];
      }
    }
    Element el = ring.CForm.ElementConvertToPolynom(num);
    Polynom a = el instanceof Polynom ? (Polynom) el : new Polynom(el);

    el = ring.CForm.ElementConvertToPolynom(denum);
    Polynom b = el instanceof Polynom ? (Polynom) el : new Polynom(el);
    Element polPart = null;
    if(a.degree(n_var) >= b.degree(n_var)){
      Element[] rat = a.divAndRemToRational(b, ring.CForm.newRing);
      polPart = rat[0];
      if(var.X[0] instanceof F){
        polPart = ring.CForm.ElementToF(rat[0]);
      }
      if(rat[1] instanceof Fraction){
        Element p = ((Fraction) rat[1]).num;
        a = p instanceof Polynom ? (Polynom) p : new Polynom(p);
        p = ((Fraction) rat[1]).denom;
        b = p instanceof Polynom ? b.multiply((Polynom) p, ring.CForm.newRing) : b.
                multiply(new Polynom(p), ring.CForm.newRing);
      }else{
        a = rat[1] instanceof Polynom ? (Polynom) rat[1] : new Polynom(rat[1]);
      }
    }
    return null;
  }
  
    /**
     * Процедура вычисления логарифмической части интеграла от функции, содержащей радикал.
     * Алгоритм основан на использовании базисов Гребнера. Подробное описание алгоритма
     * приводится в статье:
     * Manuel Kauers. "Integration of Algebraic Functions: A Simple Heuristic for Finding the Logarithmic Part". 2005.
     * 
     * Общая схема алгоритма выглядит так:
     * 
     * Пусть f(x) --- алгебраическая функция, содержащяя радикал (функция f может
     * содержать несколько одинаковых радикалов). Заменим в этом выражении все вхождения радикала на
     * новую переменную y. Запишем f(x) в каноническом виде:
     * f(x) = u(x, y)/v(x), где u(x, y) --- полином от двух переменных x и y;
     * v(x) --- полином от переменной x.
     * 
     * Общая схема алгоритма интегрирования логарифмической части функции f(x) выглядит так.
     * 
     * Считаем базис Гребнера G от многочленов {m(x, y), v(x), u(x, y) - t*v'(x)}, где t --- новая переменная;
     * m(x, y) --- полином, задающий связь между переменными x и y (т. е. если в
     * этот полином вместо переменной y подставить радикал, то получим число 0);
     * v'(x) --- производная полинома v(x). Базис G считается с lex упорядочением с порядком переменных x > y > t.
     * В объекте типа Ring переменные упорядочиваются начиная с самой младшей и заканчивая самой старшей.
     * Новые переменные добавляются всегда в конец списка переменных кольца. Поэтому после добавления
     * переменных y и t, мы получаем кольцо вида: Q[x, ..., y, t], в котором t > y > ... > x.
     * Для того, чтобы задать нужный нам порядок x > y > t, приходится создавать новое кольцо
     * вида Q[t, y, ..., x]. В соответствии с новым кольцом приходится менять порядок переменных во всех полиномах.
     * 
     * 
     * Находим в полученном базисе Гребнера G такой многочлен q(t), который зависит только от переменной t.
     * 
     * Находим все разложимые множители q_i(t) полинома q(t).
     * 
     * Для каждого множителя q_i(t) выполняем следующие действия.
     * 
     * Считаем базис Гребнера A1 от многочленов {q_i(t), m(x, y), v(x), u(x, y) - t*v'(x)}.
     * 
     * Для каждого многочлена p(x, y, t) из базиса G вычисляем базис Гребнера A2 от многочленов {p(x, y, t), q_i(t), m(x, y)}.
     * 
     * Если A1 == A2, то находим все корни s_0, s_1, ..., s_w многочлена q_i(t). Прибавляем к ответу сумму:
     * 
     * \sum_{j=0}^w   s_j * ln(p(x, y, s_j))
     * 
     * 
     * 
     * @param num --- числитель подынтегральной функции
     * @param denom --- знаменатель подынтегральной функции
     * @param m --- полином, задающий связь между переменными x и y
     * @param nameVar --- радикал, содержащийся в подынтегральном выражении
     * @param Y_ind --- индекс переменной y в кольце ring
     * @param arg --- переменная интегрирования
     * @param ring --- кольцо
     * @return логарифмическая часть интеграла
     */
    public Element LogarithmicPartGroebner(Polynom num, Polynom denom, Polynom m, Fname nameVar, int Y_ind, Polynom arg, Ring ring) {
        Element integral = ring.numberZERO;
        // завели в кольце еще одну переменную "t"
        Polynom t = (Polynom) ring.CForm.ElementConvertToPolynom(new Fname("t"));
        String str = "Q[";
        for (int i = ring.CForm.newRing.varNames.length - 1; i >= 0; i--) {
            str = str + ring.CForm.newRing.varNames[i];
            if(i != 0) {
                str = str + ",";
            }
        }
        str = str + "]";
        Ring r = new Ring(str);
        
        int[] varsMap = new int[r.varPolynom.length];
        for (int i = 0; i < r.varPolynom.length; i++) {
            varsMap[i] = r.varPolynom.length - i - 1;
        }
        
        arg = arg.changeOrderOfVars(varsMap, r).normalNumbVar(r);
        num = num.changeOrderOfVars(varsMap, r).normalNumbVar(r);
        denom = denom.changeOrderOfVars(varsMap, r).normalNumbVar(r);
        t = t.changeOrderOfVars(varsMap, r).normalNumbVar(r);
        m = m.changeOrderOfVars(varsMap, r).normalNumbVar(r);
        
        
        
        int T_ind = t.powers.length - 1;
        Y_ind = varsMap[Y_ind];
        
        Polynom UsubTdV = num.subtract(denom.D(arg.powers.length - 1, r).multiply(t, ring), r);
        Element[] set = new Element[]{m, denom, UsubTdV};
        VectorS G = com.mathpar.polynom.gbasis.util.CommandsRunner.runGbasis(r, set);
        Polynom q = null;
        int indexOfq = 0;
        for(int i = 0; i < G.V.length; i++) {
            if( ((Polynom)G.V[i]).degree(arg.powers.length - 1) <= 0 &&
                ((Polynom)G.V[i]).degree(Y_ind) <= 0 &&
                ((Polynom)G.V[i]).degree(T_ind) > 0 ) {
                
                    q = (Polynom)G.V[i];
                    indexOfq = i;
            }
        }
        if(q == null) {
            return null;
        }
        FactorPol factor = (FactorPol)q.factor(ring.CForm.newRing);
        for (int i = 0; i < factor.multin.length; i++) {
            if(factor.multin[i].isItNumber()) {
                continue;
            }
            set = new Element[]{factor.multin[i], m,  denom, UsubTdV};
            VectorS A1 = com.mathpar.polynom.gbasis.util.CommandsRunner.runGbasis(ring.CForm.newRing, set);
            for (int j = 0; j < G.V.length; j++) {
                if(j == indexOfq) {
                    continue;
                }
                set = new Element[]{factor.multin[i], m, G.V[j]};
                VectorS A2 = com.mathpar.polynom.gbasis.util.CommandsRunner.runGbasis(ring.CForm.newRing, set);
                if(A1.V.length != A2.V.length) {
                    continue;
                }
                boolean A1equalsA2 = false;
                for (int k = 0; k < A1.V.length; k++) {
                    A1equalsA2 = false;
                    for (int s = 0; s < A2.V.length; s++) {
                        if(A1.V[k].subtract(A2.V[s], ring).isZero(ring)) {
                            A1equalsA2 = true;
                            break;
                        }
                    }
                    if(A1equalsA2 == false) {
                        break;
                    }
                }
                if(A1equalsA2 == false) {
                    continue;
                }
                
                // find all roots of q and add log to answer.
                Element solEE=new SolveEq(ring).solvePolynomEq(factor.multin[i].toPolynom(ring.CForm.newRing), T_ind, new Ring("C64[x, y, z]"));
                if(solEE == null)  return null;
                VectorS c = (solEE instanceof VectorS)? (VectorS)solEE: new VectorS(new Element[]{solEE});
                for (int k = 0; k < c.V.length; k++) {
                    c.V[k] = ring.CForm.ElementToF(c.V[k]);
                    c.V[k] = c.V[k].Expand(ring).Factor(false, ring);
                    
                    
                    
                    
                    Element argOfNewLN = null;
                            
                    Element[] els = new Element[ring.CForm.newRing.varPolynom.length];
                    System.arraycopy(ring.CForm.newRing.varPolynom, 0, els, 0, ring.CForm.newRing.varPolynom.length);
                    
                    argOfNewLN = ((Polynom)G.V[j]).changeOrderOfVars(varsMap, r).normalNumbVar(r);
                    els[varsMap[T_ind]] = c.V[k];
                    els[varsMap[Y_ind]] = nameVar.X[0];
                    argOfNewLN = ((Polynom)argOfNewLN).value(els, ring.CForm.newRing);
                    
                    Element NewLN = new F(F.LN, new F(F.ABS, argOfNewLN));
                    NewLN = NewLN.multiply(c.V[k], ring.CForm.newRing);             
                    integral = integral.add(NewLN , ring.CForm.newRing).expand(ring);
                }
            }
        }
        return integral;
    }
/**
 *
 * @param num   -- numerator - полином или число
 * @param denum -- denominator -полином со старшим коэффициентом 1
 * @param var   -- возможно указывает на полином - переменную в кольце
 *                 или на трансцендентную функцию, которую обозначили переменной
 * @param n_var  -- номер переменной var в кольце
 * @param ring Ring
 * @return
 */
  public Element[] LogarithmicPart(
          Element num, Element denum, Fname var, int n_var, Element NCPolDenom, Element argg, Ring ring ) {
 //     System.out.println("###="+num+denum+var+n_var+NCPolDenom+argg+ring +ring.CForm.index_in_Ring);
  //  CanonicForms cfr = new CanonicForms(ring, true);
          //    if(cf.List_of_Change.size() == 0) flagOfExp = true;
    if(num instanceof Fraction || (num instanceof F && ((F) num).name == F.DIVIDE)){
      if(num instanceof Fraction){
        Fraction fr = (Fraction) num;
        denum = new F(F.MULTIPLY, fr.denom, denum).expand(ring);
        num = fr.num;
      }else{
        F frac = (F) num;
        denum = new F(F.MULTIPLY, denum, frac.X[1]).expand(ring);
        num = frac.X[0];
      }
    }  // если числитель был дроью, то его знаменатель домножаем к denom
       // == а не позднова-то ли? может это делать чуть раньше?
   //  т.е. (p/q)/den---> p/(q*den)
   
   Element Ddenum = new Integrate().DStructurTheorem(denum, ((Polynom)argg).powers.length-1, ring).expand(ring);
          //  Ddenum -- это просто производная от знаменателя denom
          
          
    Element elNum = ring.CForm.ElementConvertToPolynom(num);//ElementToPolynom(num, false);
    Polynom a = elNum instanceof Polynom ? (Polynom) elNum : new Polynom(elNum);

    elNum = ring.CForm.ElementConvertToPolynom(denum);//ElementToPolynom(denum, false);
    Polynom b = elNum instanceof Polynom ? (Polynom) elNum : new Polynom(elNum);
 //    System.out.println("bbb="+b+Array.toString(b.powers));
    // получили отношение полиномов: a/b
    // Обозначили //////////////////////////////////////!!!!!!!!!!!!!!
     //
    Element polPart = null;
    if(a.degree(n_var) >= b.degree(n_var)){
      // если дробь неправильная, то выделим целую часть и правильную дробь
      Element[] rat = a.divAndRemToRational(b, ring.CForm.newRing);
      polPart = rat[0];  // целая часть
      if(var.X[0] instanceof F) polPart = ring.CForm.ElementToF(rat[0]);
      // целую часть вернули в исходный вид с  трансцендентной функией var
      if(rat[1] instanceof Fraction){
        Element p = ((Fraction) rat[1]).num;
        a = p instanceof Polynom ? (Polynom) p : new Polynom(p);
        p = ((Fraction) rat[1]).denom;
        b = p instanceof Polynom
                ? b.multiply((Polynom) p, ring.CForm.newRing)
                : b.multiply(new Polynom(p), ring.CForm.newRing);
      }else{
        a = rat[1] instanceof Polynom ? (Polynom) rat[1] : new Polynom(rat[1]);
      }
    }  // ну тут сделали новые a и b после выделения и изъятия целой части


    if((Ddenum.isItNumber())&&(a.degree(n_var)<1)){
        Element HC=b.highCoeff(n_var);
        Element v=ring.CForm.ElementToF(b).divide(HC, ring);
        Element e=a.divideToFraction(HC, ring);
        if(!NCPolDenom.isOne(ring)) e = e.divide(NCPolDenom, ring);
        return new Element[]{polPart, new F(F.LN, new F(F.ABS, v))
                .multiply(e, ring)};
  }
    Element arg = var.X[0], e = null;
    boolean flagOfExp = false;
    if(arg instanceof F){if(((F) arg).name == F.EXP){flagOfExp = true;}
      arg = ((F) arg).X[0];    }
    Polynom Db = null;
    elNum = ring.CForm.ElementConvertToPolynom(Ddenum);
     //ElementToPolynom(Ddenum, false);
     // производную знаменателя бросили в полином
    if(elNum instanceof Fraction){ // если она оказалась дробью,
        // то числитель дроби обозначаем Db,
        // а знаменатель домножаем в "a"
      Fraction fr = (Fraction) elNum;
      Db = fr.num instanceof Polynom ? (Polynom) fr.num : new Polynom(fr.num);
      a = fr.denom instanceof Polynom ?
              a.multiply((Polynom) fr.denom, ring.CForm.newRing) :
              a.multiply(new Polynom(fr.denom), ring.CForm.newRing);
      // тут перестарались со страховками.....,,
    }else{Db = elNum instanceof Polynom ? (Polynom) elNum : new Polynom(elNum);}


    Polynom z = (Polynom) ring.CForm.ElementConvertToPolynom(new Fname("zTemp"));
    int Z_ind=z.powers.length-1;
    // завели в кольце еще одну переменную "zTemp"
    Ring r = ring.CForm.newRing; r.page=ring.page;r.CForm=ring.CForm;
    Polynom pol;
    if(flagOfExp) {
        pol = (Polynom) ring.CForm.ElementConvertToPolynom(
                new Integrate().DStructurTheorem(arg, ((Polynom)argg).powers.length-1, ring).expand(ring).
                        multiply(new NumberZ(b.maxPowOfVars__(r)[n_var]), r).multiply(b, r)
        );
        pol = (Polynom) a.subtract(z.multiply(Db.subtract(pol, r), r), r);
    } else {
    	pol = a.subtract(z.multiply(Db, r), r);
    }
// System.out.println("n_var======1=a z Db========"+n_var+"  "+a+"   "+z+Array.toString(z.coeffs)+Array.toString(z.powers)+ "   "+Db);
//  System.out.println("pol AND b======1========="+pol+"  "+b);
    Polynom[] ress = pol.resultant2Pol( b, n_var, r);//R(z)<-pp_z(res_x(a-zb',b))???
    if(ress.length == 0) {ress = new Polynom[2];ress[0] = pol; 	ress[1] = b;  }
    //    res = polPowersInArray(res, n_var, r);
    Polynom res=ress[0]; // по этому результанту ищем корни
    Polynom res1=ress[1]; // и подставляем их сюда
//   System.out.println("res======1========="+res+"  "+res.powers.length);
 //    System.out.println("res1======1========="+res1); 
//    if(res.coeffs[0].numbElementType() == Ring.CZ && ((Complex) res.coeffs[0]).im.
//            compareTo(r.numberZERO, 0, r)){
//      res = res.toPolynom(Ring.Z, r);
//    }
    //если результант имел старшим коэффициентом комплексное число с нулевой мнимой
    // частью, то весь результант превратили в полином над Z
    res = res.divideExact(new Polynom(res.GCDNumPolCoeffs(r)), r);//cdPolCoefHVar(r), r);
//       System.out.println("resss======1111========="+res);
 //  res = polPowersInArray(res, n_var, r);
 //   to be or not to be   !!!!!!!!!
    // сократили весь результант на его GCDNumPolCoeffs
 //   res = res.ordering(r);
     // результант разложили на множители, хотя нужно просто найти корни...??
    FactorPol factorRes = (FactorPol)res.factor(r);//FactorPol_SquareFree(r); //{rx{z) rk{z)) <-factors(R(z))
 //      System.out.println("factorRes======1========="+factorRes.toString(r));
    factorRes.normalForm(r);
 //      System.out.println("factorRes======1========="+factorRes.toString(r));
    Element integral = r.numberZERO; //integral <— 0
 //   int n_z = ring.CForm.List_of_Change.indexOf(new Fname("zTemp")) + ring.varPolynom.length;
//    System.out.println("n_z======Z========="+n_z+ring.CForm.List_of_Change.indexOf(new Fname("zTemp"))+ring.varPolynom.length);
   r.algebra[0]|=Ring.MASK_C; //
 //   System.out.println("RRRRRr="+"С64" + r.toString() );
  //              System.out.println("=====-------3333---------"+Array.toString(r.varPolynom));
   // Перешли к комплексному кольцу !!!
    int ind = ring.CForm.List_of_Change.indexOf(var);
    // если var -- это экспонта, то поднимаем флаг flagOfExp
    // аргумент функции var в любом случае записываем в arg
     for(int i = 0; i < factorRes.powers.length; i++){ // бежим по сомножителям результанта
        if( (factorRes.multin[i].coeffs.length<2)||
            (factorRes.multin[i].degree(Z_ind) < 1) ){continue;}
        // пропускаем полиномы нулевой степени и содержащие 1 моном
        Polynom r_i = factorRes.multin[i];
        if(flagOfExp){
          Element pow = NumberZ.ONE;
          if(ind < 0){
            for(int ii = 0; ii < ring.CForm.List_of_Change.size(); ii++){
              Element elList = ring.CForm.List_of_Change.get(ii);
              if(elList instanceof F && ((F) elList).name == F.POW && ((F) elList).X[0].
                      compareTo(var, ring) == 0){
                pow = pow.multiply(((F) elList).X[1], ring);
                break;
              }
            }
          }
          e = pow.multiply(arg, ring);
   //                  System.out.println("arg==eeee==" +e+"   "+pow+"  "+arg+"  "+ring);
        }
        // если var не экспонента
        // решаем уравнение и все корни записываем в вектор с
        // преобразуем все к комплексному кольцу !!!!!
//    System.out.println("r_i======1========="+r_i);       
        Element solEE=new SolveEq(ring).solvePolynomEq(r_i.toPolynom(r), Z_ind, r);//  !!!! c <— solve(r_i(z)=0, z)
  //            System.out.println("=====-------2222---------"+Array.toString(r.varPolynom));
 //    System.out.println("r_i======1========="+r_i+" "+r_i.toPolynom(r)+"  "+n_z); 
 //        System.out.println("solEE======1111========="+solEE); 
        if((solEE==null)) //||(!(c.V[0].isItNumber())))
              return new Element[]{null, null};
         VectorS c = (solEE instanceof VectorS)? (VectorS)solEE: new VectorS(new Element[]{solEE}); 
          // перестановка переменных
        //   r = ring.CForm.newRing; //2015 говое кольцо
           for(int j = 0; j < c.V.length; j++){
               c.V[j] = ring.CForm.ElementToF(c.V[j]);
 //                System.out.println("=--------------------="+r.varNames.length+"   "+n_z);
               c.V[j]=c.V[j].Expand(ring).Factor(false, ring);
               //if(!c.V[j].isItNumber()) {
               //   return new Element[]{null, null};
               //}
               if(new Integrate().containsVar(c.V[j], (Polynom)argg)) {
                   return new Element[]{null, null};
               }
               if((c.V[j] instanceof FactorPol)&&(c.V[j].isItNumber())) {
            	   c.V[j] = (r.algebra[0] == Ring.CQ)? c.V[j].toNumber(Ring.CQ, r) : c.V[j].toNumber(Ring.C, r);
               }
               
           
 //           System.out.println("=--------------------="+r.varNames.length+"   "+n_z);
          
          int d = 0;
          Element v = null;
          for(int k=1; k<ress.length; k++) {
 
            // Цикл по всем корням, которые хранятся в векторе с ---------------         
            Element[] els = new Element[r.varPolynom.length];
            System.arraycopy(r.varPolynom, 0, els, 0, r.varPolynom.length);
                d=ress[k].degree(n_var);
                if (Z_ind>=ress[k].varNumb()) v=ress[k]; else {
                     els[Z_ind] = c.V[j]; v = ress[k].value(els, r); }
             //   els[r.varPolynom.length - 1] = c.V[j] ;//alfa.X[0];
           //     FactorPol LHP=RES1.coefOfHightVar(ring);               
  //           System.out.println("==----------------"+v+"  "  +ress[k]+"  "+Array.toString(els)+Z_ind+"  "+n_z);        
                if(!v.isItNumber()&& !(v instanceof F) ) {
                   if (v instanceof Fraction) v=((Fraction)v).num;
                   
                                  
               boolean bool = false;
               Element el = ring.numberZERO;
                    for (int l = 0; l < ((Polynom)v).coeffs.length; l++) {
                        if( ((Polynom)v).coeffs[l].isItNumber() == false ) {
                            bool = true;
                        }
                    }
                    
                    if( bool ) {
                        Polynom[] monoms = ring.CForm.dividePolynomToMonoms((Polynom)v);
                        for (int l = 0; l < monoms.length; l++) {
                            Element el2 = monoms[l].coeffs[0];
                            monoms[l].coeffs[0] = ring.numberONE;
                            el2 = new F(F.MULTIPLY, el2, ring.CForm.ElementToF(monoms[l]));
                            el = new F(F.ADD, el2, el);
                        }
                        v = el.expand(ring);
                    } else {
               Polynom cHV=((Polynom)v).coefOfHightVar(n_var,ring);
 //  System.out.println("vE======0========="+cHV);
               if(cHV.isItNumber()) {
                   v = v.divideToFraction(cHV.abs(ring), ring);
               } else {
                   v = v.divideToFraction(cHV, ring);
               }
 //  System.out.println("vE======1========="+v);
//   System.out.println("vE======2========="+v.toString(r));
                 v=v.expand(r);
                                  // Если старшая переменная - экспонента, полином v является полиномом
                 // первой степени, и при этом у полинома v коэффициенты имеют
                 // разные знаки, то умножаем этот полином на -1.
                 // Это помогает выдать более красивый ответ.
                 // Например, интеграл:
                 // \\int( \\exp(x)/(\\exp(2x) + 1) ) dx
                 // равен
                 // \\i/2 * (\\ln(\\i + \\exp(x)) - \\ln(\\i - \\exp(x))) = \\arctg(\\exp(x))
                 // но если не делать этого домножения полинома на -1, то
                 // ответ будет иметь вид:
                 // \\i/2 * (\\ln(\\i + \\exp(x)) - \\ln(\\exp(x) - \\i))
                 // и этот ответ равен \\arctg(\\exp(x)) - \\ln(-1).
                 // Этот ответ правильный, так как интеграл считается с точностью до
                 // аддитивной константы (т. е. \\int( \\exp(x)/(\\exp(2x) + 1) ) dx = \\arctg(\\exp(x)) + с
                 // и мы можем обозначить с = -\\ln(-1) )
                 // Но этот ответ не получится преобразовать к виду \\arctg(\\exp(x)).
                 if(  flagOfExp && (v instanceof Polynom) &&
                   ( ((Polynom)v).coeffs.length == 2 ) &&
                   ( ((Polynom)v).degree(n_var) == 1) &&
                   ( ((Polynom)v).coeffs[0].multiply(((Polynom)v).coeffs[1], ring).isNegative()) ) {
                     v = v.multiply(ring.numberMINUS_ONE, ring);
                 }
//   System.out.println("vE======3========="+v);
              // Fname c составным именем, который указывакт на число (корень)
              // и забрасываем его как переменную в кольцо xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
              if(!v.isItNumber())  v=v.factor(r);
              if(v instanceof FactorPol) {
            	  v = ( (FactorPol) v).toPolynomOrFraction(r);
              }
  //          System.out.println("vE======4========="+v);
       // эт	saprykintgu@mail.ruо и будет искомый знаменатель,
        // который пойдет под логарифм с числ коэфф перед лог. = cj
              v = ring.CForm.ElementToF(v).expand(ring);
                }
                }
        if (v instanceof F ) v=((F)v).expand(r);
        if(!v.isZero(ring)) break;
                
          }
          if(v.isZero(ring)) v = denum;
  //           System.out.println("vvv======5========="+v);
                      //cfr.ElementToF(cj);//# (where c[j] = RootOf(r_i(z)))
              // это всегда было число.... зачем его столько мучить 
              Element ccc=new F(F.LN, new F(F.ABS, v));
              if(flagOfExp){
                  ccc = ccc.subtract(e.multiply(new NumberZ(d), r), r).expand(ring);
              }
     //          System.out.println("ccc===========65A============="+c.V[j]+"   "+NCPolDenom+"   "+r+ring+j+c.V.length); 
              ccc = ccc.multiply(c.V[j].divide(NCPolDenom, r).expand(ring), r);             
              integral= //(cHV.isNegative())?integral.subtract(ccc , r):
                      integral.add(ccc , r).expand(ring);
                         //integral <- integral + c[j]*log(v[j])
      //        System.out.println("ccc===========6============="+ccc); 
            }// конец цикла по всем корням с
    }
    return new Element[]{polPart, integral};
  }

    private boolean containsVar(Element el, int numVar, Ring ring) {
      if(el.isItNumber()) return false;
      if(el instanceof Polynom) return (((Polynom)el).degree(numVar) == 0)? false: true;
      if(el instanceof F) {
          int[] array = ((F)el).indexsOfVars(ring);
          return ((array.length > numVar) && (array[numVar] == numVar))? true: false;
      }
      if(el instanceof Fraction) {
          return containsVar(((Fraction)el).num, numVar, ring) ||
          containsVar(((Fraction)el).denom, numVar, ring);
      }
      if(el instanceof FactorPol) {
          boolean result = false;
          for(int i=0; i<((FactorPol)el).multin.length; i++) {
              result = result || containsVar(((FactorPol)el).multin[i], numVar, ring);
          }
          return result;
      }
      return false;
  }
  
/**
 *  Is this Polynomial has one complex coefficient?
 * @param pol
 * @return true/false
 */
  public boolean isComplex(Polynom pol) {
    for(int i = 0; i < pol.coeffs.length; i++){
      if(pol.coeffs[i] instanceof Complex){
        return true;
      }
    }
    return false;
  }

  public Element[] HermiteRadical(Element num, Polynom denom, FactorPol denomSqFree, Fname var, Element argInt, Ring ring) throws Exception {
    Ring    newRing = ring.CForm.newRing;  
    int lenSqFree = denomSqFree.multin.length - 1, n = 3, m = denomSqFree.powers[lenSqFree];
    if(((F) var.X[0]).name == F.SQRT){
      n = 2;
    }
    Polynom V = denomSqFree.multin[0];//lenSqFree
    Polynom U = denom.divideExact(V.powNewS(m, newRing), newRing);

    Element zn = ring.CForm.ElementConvertToPolynom(num);
    Polynom pol_zn = zn instanceof Polynom ? (Polynom) zn : new Polynom(zn);

    CanonicForms newcf = ring.CForm;
    zn = (new Fname("$a1")).add((new Fname("$a2")).multiply(var, ring), ring);
    if(((F) var.X[0]).name == F.CUBRT){
      zn = zn.add((new Fname("$a3")).multiply(new F(F.intPOW, new Element[]{var,
                new NumberZ(2)}), ring), ring);
    }
    Polynom A = ((Polynom) newcf.ElementConvertToPolynom(zn)).subtract(pol_zn,
            newcf.newRing);
    Object[] newpol = new StrucTheorem().PolynomNormalVar(A, newcf);
    Vector<Element> X = new Vector<Element>();
    new StrucTheorem().getValueOfVeriabl((Polynom) newpol[0], X, n, ((Polynom)argInt).powers.length - 1, ring);//0,2x^8+1

    Element[] f = new Element[n];
    int varNumb= ((Polynom)var.X[0]).powers.length-1;
    Element H = new F(F.DIVIDE,
            new Element[]{new Integrate().DStructurTheorem(((F) var.X[0]).X[0],
             varNumb, ring).expand(ring), ((F) var.X[0]).X[0].multiply(new NumberZ(n),
              ring)}).expand(ring);
    Element elV = ring.CForm.ElementToF(V);
    H = H.multiply(elV, ring);
    Element mdV = (new Integrate().DStructurTheorem(elV, varNumb,  ring).expand(ring)).
            multiply(new NumberZ(m - 1), ring);
    for(int i = 0; i < n; i++){
      Element fdenom = ring.CForm.ElementToF(U).multiply(H.multiply(new NumberZ(
              i), ring).subtract(mdV, ring), ring);//4x^8, 4x^8
      f[i] = X.get(i).divide(fdenom, ring).expand(ring);//0,-(2x^8+1)/4x^8
    }
    Element Q = f[n - 1] instanceof F && ((F) f[n - 1]).name == F.DIVIDE ? ((F) f[n - 1]).X[1] : NumberZ.ONE;
    Element[] bezu = EqvalsBezu(V, Q, varNumb, ring);
    Element R = bezu[1];//-1
    bezu = EqvalsBezu(V, NumberZ.ONE, varNumb, ring);
    Element[] B = new Element[n];
    for(int i = 0; i < n; i++){
      B[i] = bezu[1].divide(R.multiply(Q.multiply(f[i], ring), ring), ring).
              expand(ring);
    }
    Element elB = B[0], y = NumberZ.ONE;
    for(int i = 1; i < n; i++){
      y = y.multiply(var, ring);
      elB = elB.add(B[0].multiply(y, ring), ring);
    }
   // int varName= ((Polynom)var.X[0]).powers.length-1;
    Element dBdivV = new Integrate().DStructurTheorem(elB.divide(elV,  ring),
           varNumb,  ring).expand(ring);
    Element h = NumberZ.ZERO;

    return null;
//    Element[] q = new Element[n];
//    q[n - 1] = el_chr.simplify(ring);//q_n
//    Element Dq = null;
//    for(int ii = n - 2; ii >= 0; ii--){
//      Dq = new Integrate().DStructurTheorem(q[ii + 1].simplify(ring),ring).simplify(ring);
//      F f1 = new F(F.MULTIPLY, new Element[]{bezu[0], new NumberZ(ii + 1)}).simplify(ring);
//      if(!bezu[2].isZero(ring)) f1 = new F(F.ADD, new Element[]{f1, bezu[2]}); //(a(n-1)+b')
//      F f2 = new F(F.MULTIPLY, new Element[]{q[ii + 1],f1}).simplify(ring);//q_n*(a(n-1)+b')
//      if(!bezu[1].isZero(ring)) f2 = new F(F.ADD, new Element[]{f2, Dq.multiply(bezu[1], ring).simplify(
//                ring)}).simplify(ring);//q_n*(a(n-1)+b')+q'_n*b
//      F f4 = new F(F.MULTIPLY, new Element[]{new Fraction(NumberZ.ONE, new NumberZ(ii + 1)),f2}); //1/(n-1)*f2
//      q[ii] = f4.simplify(ring);//q_(n-1)=1/(n-1)(q_n*(a(n-1)+b')+q'_n*b)
//
//      f1 = new F(F.POW, new Element[]{zn_i, new NumberZ(ii + 1)});//r^(n-1)
//      f2 = new F(F.MULTIPLY, new Element[]{new NumberZ(ii + 1), f1});//(n-1)r^(n-1)
//      F f3 = new F(F.DIVIDE, new Element[]{q[ii + 1], f2});//q_n/((n-1)r^(n-1))
//      integ = new F(F.ADD, new Element[]{integ,f3}).simplify(ring);//q_n/((n-1)r^(n-1))+...
//    }
//    if(!bezu[1].isZero(ring) && !integ.isZero(ring)){
//      integ = new F(F.MULTIPLY, new Element[]{NumberZ.MINUS_ONE, bezu[1],integ});
//    }
//    return new Element[]{integ, q[0]};

  }

  /*
   * Интегрирование дробно-рац. функции по методу Эрмита.
   * Из соотношения a*denom+b*Ddenom = 1 находим a и b при помощи тождества безу
   * \int( chr/denom^n )= \int( (chr*(a*denom+b*Ddenom))/denom^n ) =
   *                    = \int( (chr*a)/denom^{n-1})+\int( (chr*b*Ddenom)/denom^n)=
   *                    = |u = chr*b, v = -1/((n-1)*denom^{n-1})| =
   *                    = -(chr*b/(n-1))/(denom^{n-1})+\int((chr*a+(chr*b/(n-1))')/(denom^{n-1}))
   * нам удалось понизить показатель степени полиномо denom. Продолжаем подобным
   * образом до тех пор, пока показатель не станет равен 1, тогда оставшийся
   * интеграл равен сумме логарифмов:
   *      если старшая переменая - функция логарифма, то наша сумма будет иметь вид:
   *          \sum(ci*ln(vi)), где vi - разложение denom на множители, а ci=[числитель,Ddenom]=const
   *      если старшая переменая - функция экспоненты, то наша сумма будет иметь вид:
   *          \sum(ci*(ln(vi)-N*u'), где vi - разложение denom на множители, а
   *          ci=[числитель-N*u'*denom,Ddenom]=const, N - степень полинома denom,
   *          u - аргумент нашей старшей переменной.
   */
  public Element[] Hermite(Element el_chr, Polynom zn_pol, int n, Element arg, Ring ring) throws Exception {
     int varNunb=  ((Polynom)arg).powers.length-1;
    Element zn_i = ring.CForm.ElementToF(zn_pol);
    Element Dzn_i = new Integrate().DStructurTheorem(zn_i,varNunb, ring).expand(ring);
    Element integ = NumberZ.ZERO;
    Element[] bezu = EqvalsBezu(zn_pol, Dzn_i, varNunb, ring);
    for(int i=0; i<bezu.length; i++) {
        bezu[i] = bezu[i].expand(ring);
    }
    Element[] q = new Element[n];
    q[n - 1] = el_chr.expand(ring);//q_n
    Element Dq = null;
    for(int ii = n - 2; ii >= 0; ii--){
      Dq = new Integrate().DStructurTheorem(q[ii + 1].expand(ring), varNunb, ring).
              expand(ring);
      F f1 = new F(F.MULTIPLY, new Element[]{bezu[0], new NumberZ(ii + 1)}).
              expand(ring);
      if(!bezu[2].isZero(ring)){
        f1 = new F(F.ADD, new Element[]{f1, bezu[2]}); //(a(n-1)+b')
      }
      F f2 = new F(F.MULTIPLY, new Element[]{q[ii + 1], f1}).expand(ring);//q_n*(a(n-1)+b')
      if(!bezu[1].isZero(ring)){
        f2 = new F(F.ADD, new Element[]{f2, Dq.multiply(bezu[1], ring).expand(
                  ring)}).expand(ring);//q_n*(a(n-1)+b')+q'_n*b
      }
      F f4 = new F(F.MULTIPLY, new Element[]{new Fraction(NumberZ.ONE,
                new NumberZ(ii + 1)), f2}); //1/(n-1)*f2
      q[ii] = f4.expand(ring);//q_(n-1)=1/(n-1)(q_n*(a(n-1)+b')+q'_n*b)

      f1 = new F(F.POW, new Element[]{zn_i, new NumberZ(ii + 1)});//r^(n-1)
      f2 = new F(F.MULTIPLY, new Element[]{new NumberZ(ii + 1), f1});//(n-1)r^(n-1)
      F f3 = new F(F.DIVIDE, new Element[]{q[ii + 1], f2});//q_n/((n-1)r^(n-1))
      integ = new F(F.ADD, new Element[]{integ, f3}).expand(ring);//q_n/((n-1)r^(n-1))+...
    }
    if(!bezu[1].isZero(ring) && !integ.isZero(ring)){
      integ = new F(F.MULTIPLY, new Element[]{NumberZ.MINUS_ONE, bezu[1], integ});
    }
    return new Element[]{integ, q[0]};
  }
/**
 * Интегрирование чисто дробной части  интегрируемого выражения
 * @param frac  дробная часть подинтегральной функции
 * @param simbolName
 * @param ring    Ring
 * @return        результат интегрирования
 * @throws Exception
 */
  public Element integrateOld(Element frac, ArrayList<Fname> simbolName, Element arg, Ring ring ) throws Exception {
   // CanonicForms cf = new CanonicForms(ring, true);
    Element[] els = canonicForm(frac, simbolName, arg, ring.CForm);
    if(els.length == 1)return els[0];
    Element polPart = els[0];
    Element polNum = els[1];             // числитель дроби
    Polynom polDenom = (Polynom) els[2]; // знаменатель дроби
    if(polNum == null && polDenom == null) return polPart;
    Fname VARname = (Fname) els[3]; // имя перем интегр, за которым трансцендентная функция или
                                // или, как оно стоит в кольце

    Element NCPolDenom=polDenom.GCDNumPolCoeffs(ring);
    polDenom=polDenom.divideByNumber(NCPolDenom, ring);
    // выделили и сохранили числовой общий множитель в знаменателе
    int n_var =  ((Polynom)arg).powers.length-1; // номер переменной интегрирования в кольце
    if(VARname.X[0] instanceof F){ // если имя функции
      int ind = ring.CForm.List_of_Change.indexOf(VARname);
      if(ind < 0){ // не нашлось в заменах, значит стоит со степенью
        for(int i = 0; i < ring.CForm.List_of_Change.size(); i++){
          Element elList = ring.CForm.List_of_Change.get(i);
          if(elList instanceof F
             && ((F) elList).name == F.POW
             && ((F) elList).X[0].compareTo(VARname, ring) == 0){
                  n_var = i + ring.varPolynom.length; break; }
        }
      }else n_var = ind + ring.varPolynom.length;
    } // надо проверить как работает с POW ????? GI
    FactorPol mul_pow = polDenom.FactorPol_SquareFree(ring.CForm.newRing);
    if(VARname.X[0] instanceof F && (((F) VARname.X[0]).name == F.SQRT || ((F) VARname.X[0]).name == F.CUBRT)){
      Element[] elssss = HermiteRadical(els[1], polDenom, mul_pow, VARname, arg, ring);
      Element h = frac.subtract(elssss[1], ring);
      //LogarithmicPartIntegralResultantRadical
      return null;
    }
    Object[] obj = partialFractionOld(mul_pow, polNum, polDenom, n_var, VARname, ring);
    Element[] el_chr = (Element[]) obj[0];
    FactorPol polZn = (FactorPol) obj[1];
    Element[] integ = new Element[polZn.multin.length];
    // каждую дробь интегрируем отдельно и пишем в этот массив
    for(int i = 0; i < polZn.multin.length; i++){
      Element zn_i = ring.CForm.ElementToF(polZn.multin[i]);
      if(polZn.powers[i] == 1){
        Element[] lpir = LogarithmicPart(el_chr[i], zn_i, VARname,
                n_var, NCPolDenom, arg, ring);
        Element logPart = lpir[1];
        if(logPart == null)  return null;
        if(lpir[0] != null){
              Fname name = new IntPolynom().getLastRegularMonomial(lpir[0], simbolName, arg, ring);
              Element elt = new IntPolynom().polPartInteg(lpir[0], name, simbolName,arg, ring);
              logPart = logPart.add(elt, ring);}
        integ[i] = logPart;
      }else{
        Element[] hermiteReduce = Hermite(el_chr[i], polZn.multin[i], polZn.powers[i], arg, ring);
        integ[i] = hermiteReduce[0];
        Element q = hermiteReduce[1];
        if(!q.isZero(ring)){
          els = canonicForm(new F(F.DIVIDE, new Element[]{q, zn_i}), simbolName, arg, ring.CForm);
          Element[] lpir = LogarithmicPart
                            (els[1], els[2], VARname, n_var,NCPolDenom, arg, ring);
          Element logPart;
          if( (els[0] != null)&&(lpir[1] != null) ) {
              logPart = els[0].add(lpir[1], ring);
          } else if(els[0] != null) {
              logPart = els[0];
          } else {
              logPart = lpir[1];
          }
          if(logPart == null)return null;
          if(lpir[0] != null){
            Fname name = new IntPolynom().getLastRegularMonomial(lpir[0], simbolName, arg, ring);
            Element elt = new IntPolynom().polPartInteg(lpir[0], name, simbolName, arg, ring);
            logPart = logPart.add(elt, ring);
          }
          integ[i] = new F(F.ADD, new Element[]{integ[i], logPart});
        }
      }
    }
    if(polPart != null)
      return new F(F.ADD, new Element[]{polPart, new F(F.ADD, integ)});
    else return new F(F.ADD, integ);
  }
  
  
  /**
 * Интегрирование чисто дробной части  интегрируемого выражения.
 * В процессе работы этого алгоритма дробное выражение разбивается в сумму.
 * 
 * 
 * В процессе работы алгоритмов интегрирования дроби 
 * @param frac  дробная часть подинтегральной функции
 * @param simbolName
 * @param ring    Ring
 * @return  null, если не удалось посчитать хоть одну часть интеграла.
 *          Если дробная часть посчитана полностью, то возвращается
 *          массив из двух элементов {polPart, fracPart}.
 * 
 *          fracPart - уже посчитанный интеграл от дробной части.
 *          polPart - полиномиальное выражение, которое нужно будет проинтегрировать.
 *          Это полиномиальное выражение прибавляется к полиномиальной части интеграла,
 *          и вся сумма интегрируется алгоритмами для полиномиальной части.
 * @throws Exception
 */
  public Element[] fracPartInteg(Fraction frac, ArrayList<Fname> simbolName, Fname VARname, int n_var, Element arg, Ring ring ) throws Exception {
    // В переменную polPart записываем слагаемые, относящиеся к полиномиальной
    // части интеграла.
    Element polPart = null;
    // В переменную fracPart записываем посчитанный интеграл от дробной части.
    Element fracPart = null;
    
    
    Element polNum = frac.num;             // числитель дроби
    Polynom polDenom = (Polynom) frac.denom; // знаменатель дроби
    
    Element NCPolDenom=polDenom.GCDNumPolCoeffs(ring);
    polDenom=polDenom.divideByNumber(NCPolDenom, ring);
    // выделили и сохранили числовой общий множитель в знаменателе
    
    FactorPol mul_pow = null;
    boolean multExpInDenom = false;
    // Если последний регулярный моном --- экспонента, и знаменатель
    // подынтегрального выражения можно разделить без остатка на эту экспоненту, то
    // перестают работать алгоритмы Эрмита (процедура Hermite) и
    // Ротштейна-Трагера (процедура LogarithmicPart). Поэтому выносим
    // экспоненту в максимальной степени в отдельный множитель знаменателя.
    // Когда будем разбивать подынтегральную дробь в сумму дробей, в одном
    // из слагаемых суммы знаменатель будет экспонентой в некоторой степени.
    // Эта дробь отностися к полиномиальной части интеграла.
    // Знаменатели остальных слагаемых нельзя будет разделить без остатка
    // на экспоненту, поэтому их можно интегрировать процедурами Hermite и
    // LogarithmicPart.
    if(VARname.X[0] instanceof F && ((F) VARname.X[0]).name == F.EXP) {
        int minDegOfDenom = polDenom.powers[polDenom.powers.length - (polDenom.powers.length/polDenom.coeffs.length) + n_var];
        if(minDegOfDenom > 0) {
            Polynom exp = (Polynom) ring.CForm.ElementConvertToPolynom(VARname);
            Polynom p = (Polynom) polDenom.divide(exp, ring);
            for(int i = 0; i < minDegOfDenom - 1; i++) {
                p = (Polynom) p.divide(exp, ring);
            }
            mul_pow = p.FactorPol_SquareFree(ring.CForm.newRing);
            int[] newPow = new int[mul_pow.powers.length + 1];
            Polynom[] newMult = new Polynom[mul_pow.multin.length + 1];
            System.arraycopy(mul_pow.powers, 0, newPow, 0, mul_pow.powers.length);
            System.arraycopy(mul_pow.multin, 0, newMult, 0, mul_pow.multin.length);
            newPow[newPow.length - 1] = minDegOfDenom;
            newMult[newMult.length - 1] = exp;
            mul_pow = new FactorPol(newPow, newMult);
            multExpInDenom = true;
        }
    }
    
    if(mul_pow == null) {
        mul_pow = polDenom.FactorPol_SquareFree(ring.CForm.newRing);
    }
    
    
    
    if(VARname.X[0] instanceof F && (((F) VARname.X[0]).name == F.SQRT || ((F) VARname.X[0]).name == F.CUBRT)){
      Element[] elssss = HermiteRadical(frac.num, polDenom, mul_pow, VARname, arg, ring);
      Element h = frac.subtract(elssss[1], ring);
      //LogarithmicPartIntegralResultantRadical
      return null;
    }
    
    Object[] obj = partialFraction(mul_pow, polNum, polDenom, n_var, ring);
    Element[] el_chr = (Element[]) obj[0];
    FactorPol polZn = (FactorPol) obj[1];
    
    for(int i = 0; i < polZn.multin.length; i++){
      if( multExpInDenom  && (polZn.multin[i].coeffs.length == 1) ) {
          F f = new F(F.DIVIDE, ring.CForm.ElementToF(el_chr[i]),
                                ring.CForm.ElementToF(polZn.multin[i])
          );
          polPart = (polPart == null)? f: polPart.add(f, ring);
          continue;
      }
      Element ch_i = ring.CForm.ElementToF(el_chr[i]);
      Element zn_i = ring.CForm.ElementToF(polZn.multin[i]);
      Element[] lpir = null;
      if(polZn.powers[i] == 1) {
           lpir = LogarithmicPart(ch_i, zn_i, VARname,
                n_var, NCPolDenom, arg, ring);
           
      } else {
           Element[] hermiteReduce = Hermite(ch_i, polZn.multin[i], polZn.powers[i], arg, ring);
           Element el = hermiteReduce[0].divide(NCPolDenom, ring);
           fracPart = (fracPart == null)? el: fracPart.add(el, ring);
           
           if(hermiteReduce[1].isZero(ring)) {
               continue;
           }
           
           lpir = LogarithmicPart(hermiteReduce[1], zn_i, VARname,
                n_var, NCPolDenom, arg, ring);
      }
      
      if( (lpir == null) || (lpir[1] == null) ) {
          return null;
      }
      
      fracPart = (fracPart == null)? lpir[1]: fracPart.add(lpir[1], ring);
      if(lpir[0] != null){
            polPart = (polPart == null)? lpir[0]: polPart.add(lpir[0], ring);
      }
    }
    
    return new Element[]{polPart, fracPart};
  }
}