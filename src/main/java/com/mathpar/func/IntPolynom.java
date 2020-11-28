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
public class IntPolynom {

  public IntPolynom() {
  }

//  /**
//   * Интегрирование полинома по одной переменной x.
//   * Входной полином должен быть от одной переменной  <br><br>
//   *
//   * Пример: <br>
//   *  1) IntPolynomX(x) = 1/2x^2 <br>
//   *  2) IntPolynomX(3x^2) = x^3 <br>
//   *
//   * @param p входной полином
//   * @return полином, проинтегрированный по x
//   */
//  public Polynom Q_PolynomX(Polynom p, Ring ring) {
//    if(p.isZero(ring))return Polynom.polynomZero;
//    if(p.powers.length == 0)
//      return new Polynom(new int[]{1}, p.coeffs);
//    int[] dpowers = new int[p.powers.length];
//    System.arraycopy(p.powers, 0, dpowers, 0, p.powers.length);
//    Element[] dcoeffs = new Element[p.coeffs.length];
//
//
//    int var = p.powers.length / p.coeffs.length, k = 0;
//    for(int i = 0; i < p.coeffs.length; i++){
//      dpowers[k] = p.powers[k] + 1;
//      k += var;
//      if(p.coeffs[i].numbElementType() != 2){
//        dcoeffs[i] = new Fraction(p.coeffs[i],ring.numberONE().valOf(Integer.toString(
//                p.powers[i] + 1), ring));//p.coeffs[i].divide(ring.numberONE().valOf(Integer.toString(
//                //p.powers[i] + 1), ring), ring);
//      }else if(p.coeffs[i].numbElementType() == 2){
//        Element number = new Fraction(p.coeffs[i], ring);
//        dcoeffs[i] = number.divide(ring.numberONE().valOf(Integer.toString(
//                p.powers[i] + 1), ring), ring);
//      }
//    }
//    return new Polynom(dpowers, dcoeffs, ring);
//  }

  /** Integration of polynomial p by variable arg.
   *
   * @param p - polynomial of many vars for polPartInteg
   * @param arg - variable (polynomial x or y or )
   * @param ring  Ring
   * @return    \int (p) d arg.
   */

  public Polynom integPol(Polynom p, Polynom arg, Ring ring) {
    if(p.isZero(ring))return Polynom.polynomZero;
    if(p.powers.length == 0) return new Polynom(arg.powers, p.coeffs);
    Polynom rezP;
    int[] dpowers;
    int var = p.powers.length / p.coeffs.length, k = arg.powers.length-1;
    int newV=arg.powers.length-var;
    if(newV>0){rezP=p.unTruncate(newV); dpowers=rezP.powers;}// вытягиваем масс powers
    else{dpowers = new int[p.powers.length];
          System.arraycopy(p.powers, 0, dpowers, 0, p.powers.length);
    }
    Element[] dcoeffs = new Element[p.coeffs.length];
    for(int i = 0; i < p.coeffs.length; i++){
      dpowers[k] =(newV>0)? 1 : p.powers[k] + 1; // степень переменной интегрирования увеличили на 1
      dcoeffs[i] = (dpowers[k]==1)? p.coeffs[i]:
         new Fraction(p.coeffs[i], p.coeffs[i].valOf(dpowers[k], ring)
            ).cancel(ring);
       k += var;
    }
    return new Polynom(dpowers, dcoeffs, ring);
  }



/**
 *  Найти во входной функции старшую переменную расширения поля.
 *
 * @param el - функция
 * @param simbName - список переменных расширения поля (последовательность регулярных мономов)
 * @param arg == new Fname("x", new Polynom("x", ring));
 *                       - переменная интегрирования
 * @param ring Ring
 * @return старшая переменная расширения поля, встречающаяся во входной функции.
 */

  public Fname getLastRegularMonomial(Element el, ArrayList<Fname> simbName,Element arg, Ring ring) {
  //  Fname name = new Fname("x", new Polynom("x", ring));
   //    Fname fnnnn= new Fname(ring.varNames[((Polynom)arg).powers.length-1], arg);
//    if(el instanceof F) el = el.ExpandFnameOrId();
    F elF = null;
    if(el instanceof F){
      elF = (F) el;
      switch(elF.name){
        case F.ID: {
          return getLastRegularMonomial(elF.X[0], simbName, arg, ring);
        }
        case F.DIVIDE:
        case F.MULTIPLY:
        case F.ADD:
        case F.SUBTRACT: {
          int ind = -1;
          Fname newEx = null;
          Fname name=null;
          for(int i = elF.X.length - 1; i >= 0; i--){
            name = getLastRegularMonomial(elF.X[i], simbName, arg, ring);
            if(simbName.indexOf(name) > ind){
              ind = simbName.indexOf(name);
              newEx = name;
            }
          }
          if(ind == -1) return (name==null)? new Fname(ring.varNames[((Polynom)arg).powers.length-1], arg): name;
          return newEx;
        }
//        case F.DIVIDE: {
//          for(int i = elF.X.length - 1; i >= 0; i--){
//            if(elF.X[i] instanceof Fname){
//              return (Fname) elF.X[i];
//            }else{
//              name = getLastRegularMonomial(elF.X[i], ring);
//              if(name != null){
//                return name;
//              }
//            }
//          }
//        }
//        case F.MULTIPLY: {
//          for(int i = elF.X.length - 1; i >= 0; i--){
//            if(elF.X[i] instanceof Fname){
//              return (Fname) elF.X[i];
//            }else{
//              if(elF.X[i] instanceof F && (((F) elF.X[i]).name == F.POW || ((F) elF.X[i]).name == F.intPOW)){
//                Element fnamePow = ((F) elF.X[i]).X[0];
//                if(fnamePow instanceof Fname){
//                  return (Fname) fnamePow;
//                }
//              }
//            }
//          }
//        }
        case F.POW:
        case F.intPOW: {
          return getLastRegularMonomial(elF.X[0], simbName, arg, ring);
        }
      }
    }
    if(el instanceof Fname)
      if(((Fname) el).X != null) return (Fname) el;
   //   return (Fname) el;
    return new Fname(ring.varNames[((Polynom)arg).powers.length-1], arg);
  }
  /*
   * Опред. коэф-ты при старшей переменной. В вектор coeff записываются коэффициенты,
   * а в вектор deg соответствующие степени старшей переменной.
   * Пример: Пусть дано выражение x*f2+f0*f2^2+f1*f2^2, тогда
   *         coeff[x, f0+f1] и deg[1,2]
 * @param el - выражение
 * @param var - имя старшей переменной
 * @param coeff - вектор коэфф. при старшей пременнной
 * @param deg   - вектор соот. степеней при старшей переменной
 * @param simbolName - видимо это замены функций на переменные
 * @param arg - видимо, это переменная интегрирования
 * @param ring - Ring
 */
  public void coefOfHightVar(Element el, Element var, Vector<Element> coeff, 
                             Vector<Element> deg, ArrayList<Fname> simbolName, Polynom arg, Ring ring) {
    el = el.expand(ring);
    if(el instanceof F){
      F elF = (F) el;
      switch(elF.name){
        case F.ID: {
          coefOfHightVar(elF.X[0], var, coeff, deg, simbolName, arg, ring);
          break;
        }
        case F.DIVIDE: {
          int len = coeff.size();
          Element div = elF.X[1];
          coefOfHightVar(elF.X[0], var, coeff, deg, simbolName, arg, ring);
          if(getLastRegularMonomial(elF.X[1], simbolName, arg, ring).equals(var)) {
        	  coefOfHightVar(elF.X[1], var, coeff, deg, simbolName, arg, ring);
        	  div = coeff.get(coeff.size()-1);
        	  coeff.remove(coeff.size()-1);;
        	  
        	  for(int i = len; i < coeff.size(); i++) {
        		Element d = deg.get(i).subtract(deg.get(deg.size()-1), ring); 
        		deg.remove(i);
        		deg.add(i, d);
        	  }
        	  deg.remove(deg.size()-1);
          }
          for(int i = len; i < coeff.size(); i++){
            Element c = coeff.get(i);
            coeff.remove(i);
            
            coeff.add(i, new F(F.DIVIDE, new Element[]{c, div}).expand(
                    ring));
          }
//          Vector<Element> coeff1 = new Vector<Element>();
//          Vector<Element> deg1 = new Vector<Element>();
//
//          coefOfHightVar(elF.X[0], var, coeff1, deg1, ring);
//          Vector<Element> coeff2 = new Vector<Element>();
//          Vector<Element> deg2 = new Vector<Element>();
//
//          coefOfHightVar(elF.X[1], var, coeff2, deg2, ring);
//          sortVector(coeff1, deg1,ring);
//          sumCoeffOfEqualsPows(coeff1, deg1,ring);
//          sortVector(coeff2, deg2,ring);
//          sumCoeffOfEqualsPows(coeff2, deg2,ring);
//          if(deg1.size() == 1 && deg1.elementAt(0) == NumberZ.ZERO && deg2.size() == 1 && deg2.elementAt(0) == NumberZ.ZERO){
//            coeff.add(elF);
//            deg.add(NumberZ.ONE);
//            break;
//          }
//          if(deg1.size() == 1 && !deg1.get(0).isZero(ring) && deg2.size() == 1 && deg2.get(0).isZero(ring)){
//            coeff.add(new F(F.DIVIDE, new Element[]{coeff1.get(0),elF.X[1]}));
//            deg.add(deg1.get(0));
//            break;
//          }
//          if(deg1.size() > 1 && deg2.size() == 1 && deg2.get(0).isZero(ring)){
//            for(int k = 0; k<deg1.size(); k++){
//              coeff.add(new F(F.DIVIDE, new Element[]{coeff1.get(k),elF.X[1]}));
//              deg.add(deg1.get(k));
//            }
//            break;
//          }
          break;
        }
        case F.ADD:
      for (Element X : elF.X) {
          coefOfHightVar(X, var, coeff, deg, simbolName, arg, ring);
      }
//          sumCoeffOfEqualsPows(coeff, deg,ring);
          break;
        case F.SUBTRACT: {
          coefOfHightVar(elF.X[0], var, coeff, deg, simbolName, arg, ring);
          for(int i = 1; i < elF.X.length; i++){
            coefOfHightVar(elF.X[i].multiply(NumberZ.MINUS_ONE, ring).expand(
                    ring), var, coeff, deg, simbolName, arg, ring);
          }
//          sumCoeffOfEqualsPows(coeff, deg,ring);
          break;
        }
        case F.MULTIPLY: {
          int len = coeff.size();
          coefOfHightVar(elF.X[0], var, coeff, deg, simbolName, arg, ring);
          int len0 = coeff.size();
          for(int i = 1; i < elF.X.length; i++){
            coefOfHightVar(elF.X[i], var, coeff, deg, simbolName, arg, ring);
            int len1 = coeff.size();
            for(int j = len; j < len0; j++){
              Element c = coeff.get(j);
              Element d = deg.get(j);
              for(int k = len0; k < len1; k++){
                coeff.add(new F(F.MULTIPLY, new Element[]{c, coeff.get(k)}).
                        expand(ring));
                deg.add(d.add(deg.get(k), ring));
              }
            }
            for(int j = len; j < len1; j++){
              coeff.remove(len);
              deg.remove(len);
            }
//            len = len;
            len0 = coeff.size();
          }
          break;
//          int size = coeff.size();
//          Element c  = coeff.get(len);
//          coeff.remove(len);
//          Element d  = deg.get(len);
//          deg.remove(len);
//          for(int i = len+1; i<size; i++){
//            c = c.multiply(coeff.get(len), ring).simplify(ring);
//            coeff.remove(len);
//            d = d.add(deg.get(len), ring).simplify(ring);
//            deg.remove(len);
//          }
//          coeff.add(c);
//          deg.add(d);
//

//          if(coeff.size()>len){
//            int size = coeff.size();
//            Element ell  = coeff.get(len);
//            coeff.remove(len);
//            Element ell2  = deg.get(len);
//            deg.remove(len);
//            for(int j = len+1; j<size; j++){
//              ell = ell.multiply(coeff.get(len), ring);
//              coeff.remove(len);
//              ell2 = ell2.add(deg.get(len), ring);
//              deg.remove(len);
//            }
//            coeff.add(ell.simplify(ring));
//            deg.add(ell2.simplify(ring));
//          }
//          if(coeff.size()==len){
//            coeff.add(elF);
//            deg.add(NumberZ.ZERO);
//          }

        }
        case F.POW:
        case F.intPOW: {
          if( (elF.X.length > 1)&&(elF.X[0] instanceof F)&&( (((F)elF.X[0]).name == F.ADD)||(((F)elF.X[0]).name == F.SUBTRACT)
              ||(((F)elF.X[0]).name == F.DIVIDE)||(((F)elF.X[0]).name == F.MULTIPLY) ) ) {
            F g = (F) elF.X[0];
            for(int i=1; i<elF.X[1].intValue(); i++) {
              g = (F) g.multiply((F) elF.X[0], ring);
            }
            coefOfHightVar(g.expand(ring), var, coeff, deg, simbolName, arg, ring);
            break;
          }
          int len = coeff.size();
          Element arg1 = elF.X[0];
          if(!(arg1 instanceof F) || (arg1 instanceof Fname)) {
              if(arg1.equals(var, ring)) {
                  deg.add(elF.X[1]);
                  coeff.add(NumberZ.ONE);

              } else {
              deg.add(NumberZ.ZERO);
                 coeff.add(elF);
              }
          } else {
          coefOfHightVar(arg1, var, coeff, deg, simbolName, arg, ring);
          if(coeff.size() == len + 1){
            Element d = deg.get(deg.size() - 1);
            deg.remove(deg.size() - 1);
            deg.add(((F) elF).X[1].multiply(d, ring).expand(ring));
            d = coeff.get(coeff.size() - 1);
            coeff.remove(coeff.size() - 1);
//            int n = F.POW;
//            if(((F) elF).X[1].numbElementType()<8) n =
            if(d.isOne(ring)){
              coeff.add(NumberZ.ONE);
            }else{
              coeff.add(new F(elF.name, new Element[]{d, ((F) elF).X[1]}).
                      expand(ring));
            }
          }
//          Element fnamePow = ((F) elF).X[0];
//          if(fnamePow instanceof Fname && ((Fname) fnamePow).compareTo(var, ring)==0){
//            coeff.add(NumberZ.ONE);
//            deg.add(((F) elF).X[1]);
//            break;
//          }
//          if(fnamePow instanceof F){
//            int len = coeff.size();
//            coefOfHightVar(fnamePow, var, coeff, deg, ring);
//            if(coeff.size()==len+1){
//              Element d = deg.get(deg.size()-1);
//              deg.remove(deg.size()-1);
//              deg.add(((F) elF).X[1].multiply(d, ring).simplify(ring));
//              d = coeff.get(coeff.size()-1);
//              coeff.remove(coeff.size()-1);
//              coeff.add(new F(F.POW, new Element[]{d,((F) elF).X[1]}).simplify(ring));
//            }
//            break;
//          }
//          coeff.add(elF);
//          deg.add(NumberZ.ZERO);
          
        }
          break;
        }
        default:  
          coeff.add(el);
          deg.add(NumberZ.ZERO);
      }
    }else{//el instanceof Fname or Polynom or Fraction or Number
      if(el instanceof Fname){
        if(((Fname) el).compareTo(var, ring) == 0){
          coeff.add(NumberZ.ONE);
          deg.add(NumberZ.ONE);
        }else{
          coeff.add(el);
          deg.add(NumberZ.ZERO);
        }
      }else{
        coeff.add(el);
        deg.add(NumberZ.ZERO);
      }
    }
  }

//  public int NExtantionInPage(ArrayList<Fname> listName, Element name) {
//    if(name instanceof Fname){
//      for(int i = listName.size() - 1; i >= 0; i--){
//        if(listName.get(i).equals(name)){
//          return i;
//        }
//      }
//    }
//    return -1;
//  }
  /*
   * Интегрирование полиномиальной части интеграла p. Если старшая переменная f
   * является функцией логарифма, то процесс интегрирования заключается в
   * нахождении коэффициентов Bi из равенства:
   * p = \sum{i=0}^{m}{Ai*f^{i}} = (\sum{i=0}^{n}{Bi*f^{i}})' =
   *   = \sum{i=0}^{n}{Bi'*f^{i}} + \sum{i=1}^{n}{i*Bi*f'*f^{i-1}}
   * Ai - коэф-ты при старшей переменной f, а Bi находим с помощью метода неопределенных коэфф.
   * Если старшая переменная - функция экспоненты, то используем процедуру integPolExp
   */
  public Element polPartInteg(Element simbolF, Fname nameVar, ArrayList<Fname> simbName, Element arg, Ring ring ) throws Exception {
    //определяем переменную по которой будет проводится интегрирование
//    int sizePage = simbolLnAndExp.size() - 1;
//    Element expression = null;
    if(!new Integrate().containsVar(simbolF, (Polynom)arg)) {
        return new F(F.MULTIPLY, simbolF, arg);
    }
    if(nameVar == null || nameVar.X[0] instanceof Polynom){//если подинтегральное выражение - число или полином от x
      simbolF = simbolF.ExpandFnameOrId();
      if(simbolF instanceof F && ((F) simbolF).name == F.POW){
        return new F(F.POW, new Element[]{((F) simbolF).X[0],
                  ((F) simbolF).X[1].add(NumberZ.ONE, ring)}).divide(((F) simbolF).X[1].
                add(NumberZ.ONE, ring), ring);
      }
      if(simbolF instanceof Fraction && (((Fraction) simbolF).denom.
              numbElementType() < Ring.Polynom || (((Fraction) simbolF).denom instanceof Polynom && ((Polynom) ((Fraction) simbolF).denom).powers.length == 0))){
        Polynom num = ((Fraction) simbolF).num instanceof Polynom ? (Polynom) ((Fraction) simbolF).num : new Polynom(
                ((Fraction) simbolF).num);
        return new Fraction(integPol(num, (Polynom)arg, ring), ((Fraction) simbolF).denom);
      }

      
      Polynom Pol = null;

      if(simbolF instanceof Polynom){
        Pol = (Polynom) simbolF;
      }else{
        Element el = ring.CForm.ElementConvertToPolynom(simbolF);
        Pol = el instanceof Polynom ? (Polynom) el : new Polynom(el);
      }
      return ring.CForm.ElementToF(integPol(Pol,(Polynom)arg, ring));
    }
    Element elIsPage = nameVar.X[0];
    //если посленее расширение - функция LN
    if(((F) elIsPage).name == F.LN){
      return integPolLN(simbolF, nameVar, simbName, arg, ring);
    }
    //***************************************************************************************
    //если переменная - EXP
    if(((F) elIsPage).name == F.EXP){
      return integPolExp(simbolF, nameVar, simbName, arg, ring);
    }
    return null;
  }
  
    Object[] containsNewLogs(ArrayList<Fname> simbName, Element func, Element arg, Fname nameVar, Ring ring) throws Exception {
    int n = simbName.lastIndexOf(nameVar);
    Vector<F> vLnAndExp = new Vector<F>();
    ArrayList<Fname> Names = new ArrayList<Fname>();
    Element el = new Integrate().makeListOfLNandExp(func, Names, vLnAndExp, (Polynom)arg, ring);
    Names.add(new Fname("end", el));
    StrucTheorem st = new StrucTheorem();
    st.makeRegularMonomialsSequence(Names, arg, ring);
    Names.remove(Names.size() - 1);
    
    Element LNinFUNC = null;
      for (int i = 0; i < Names.size(); i++) {
          boolean flag = false;
          boolean flagLN1 = false;
          boolean flagLN2 = false;
          Element el1 = Names.get(i).X[0];
          if(el1 instanceof F) {
              if(((F)el1).name == F.LN) {
                  flagLN1 = true;
              }
              
              el1 = ((F)el1).X[0];
              if( (el1 instanceof F)&&(((F)el1).name == F.ABS) ) {
                  el1 = ((F)el1).X[0];
              }
          }
          
          for (int j = 0; j <= n; j++) {
              Element el2 = simbName.get(j).X[0];
              if(el2 instanceof F) {
                  if(((F)el2).name == F.LN) {
                      flagLN2 = true;
                  }
                  
                  el2 = ((F)el2).X[0];
                  if( (el2 instanceof F)&&(((F)el2).name == F.ABS) ) {
                      el2 = ((F)el2).X[0];
                  }
              }
              
              if(flagLN1 && flagLN2) {
                  if(el1.subtract(el2, ring).Factor(true, ring).expand(ring).isZero(ring)) {
                      flag = true;
                      if(j == n) {
                          LNinFUNC = Names.get(i).X[0];
                      }
                  }
              }
          }
          
          if(flag == false) {
              return new Object[]{true, LNinFUNC};
          }
      }
    
    return new Object[]{false, LNinFUNC};
  }

  public Element parallelFirstPart(ArrayList<Fname> simbName, Element coeff_k, Element arg, Ring ring ) throws Exception{
      return new Integrate().mainProcOfInteg(
            (coeff_k instanceof F)? coeff_k: new F(F.ID, coeff_k), arg,  ring );
  }

  public Element parallelTwoPart(Element fun3, ArrayList<Fname> simbName, Element arg, Ring ring ) throws Exception{
      return new Integrate().mainProcOfInteg(
            (fun3 instanceof F)? fun3: new F(F.ID, fun3), arg, ring);
  }

  /* Интегрирование функции, старшей переменной которой является логарифм.
   *
   * Пусть p - полиномиальная часть подинтегрального выражения.
   * По теореме Лиувилля \int p = q + \sum \log(v_j), т.е.
   * p = q' + \sum v'_j/v_j
   * Пусть p=\sum_(i=0)^n p_i f^i, q=\sum_(i=0)^n B_i f^i. После раскрытия скобок
   * и приравнивания коэффициентов при одинаковых степенях неизвестной
   * получим систему уравнений вида:
   * B_n = \int p_n
   * B_{n-1} = \int (  p_(n-1) - n B_n f'  )
   *       ...........
   * B_k = \int (  p_k - (k+1) B_(k+1) f' )
   *       ...........
   * B_0 + \sum log(v_i) = \int (  p_0 - B_1 f' )
   *
   *
   * simbolF - подинтегральная функция
   * nameVar - последнее расширение дифференциального поля
   * simbName - список регулярных мономов
   * arg - переменная интегрирования
   */
  
  public F integPolLN(Element simbolF, Fname nameVar, ArrayList<Fname> simbName, Element arg, Ring ring ) throws Exception {
    //вычисляем производную от nameVar
    Element DLn = D(nameVar,((Polynom)arg).powers.length-1, ring).expand(ring);
    simbolF = simbolF.expand(ring);
    //вектор коэффициентов при данной функции
    Vector<Element> coeff = new Vector<Element>();
    //вектор стпеней при данной функции соответственно
    Vector<Element> deg = new Vector<Element>();
    coefOfHightVar(simbolF, nameVar, coeff, deg, simbName, (Polynom) arg, ring);
    sortVector(coeff, deg, ring);
    sumCoeffOfEqualsPows(coeff, deg, ring);
    int n = deg.get(0).intValue();//max deg var, по кот. производится интегрирование
    Element[] B = new Element[n + 2];//заводим массив неопределенных коэффициентов, кот. требуется найти
    B[n+1] = ring.numberZERO;
    Fname name1 = getLastRegularMonomial(coeff.elementAt(0), simbName, arg, ring);
    //находим В_n
    Element coeff_0 = coeff.get(0).ExpandFnameOrId();
    coeff_0 = (coeff_0 instanceof F)? coeff_0: new F(F.ID, coeff_0);
    B[n] = new Integrate().mainProcOfInteg(coeff_0, arg, ring);
    if( (B[n] instanceof F)&&(((F)B[n]).name == F.INT) ) {
        return null;
    }

    Object[] arrObj = containsNewLogs(simbName, B[n], arg, nameVar, ring);
    if((boolean) arrObj[0]) {
        return null;
    }
    Element[] arrEl = null;
    if(arrObj[1] != null) {
        arrEl = findConstant(B[n], (Element)arrObj[1], ring);
        if(arrEl != null) {
            B[n + 1] = (B[n + 1] != null)? 
            new F(F.ADD, new Element[]{ B[n + 1], arrEl[0].divide(ring.posConst[n + 1], ring)}).expand(ring):
                       arrEl[0].divide(ring.posConst[n + 1], ring);
            B[n] = new F(F.SUBTRACT, new Element[]{B[n], arrEl[1]}).expand(ring);
        }
    }
    int k = 1;
    for(int i = n - 1; i >= 0; i--){
      // A - первое слагаемое, A2 - второе слагаемое
      // правой части текущего уравнения системы.
      Element A = NumberZ.ZERO, A2=NumberZ.ZERO;
      if(k < deg.size() && i == deg.get(k).intValue()){
        Element coeff_k = coeff.get(k);
        coeff_k = coeff_k.ExpandFnameOrId();
        A = parallelFirstPart(simbName, coeff_k, arg,  ring);
        k++;
      }
      Element fun1 = new F(F.MULTIPLY, new Element[]{new NumberZ(i + 1),
                B[i + 1], ((F) DLn).X[0]});
      Element fun2 = ((F) DLn).X[1];
      //если производная от переменной-есть дробь, то пытаемся ее сократить
      Element eeee=simbolF;
      Element fun3 = new F(F.DIVIDE, new Element[]{fun1, fun2}).expand(ring);
      fun3 = fun3.ExpandFnameOrId();
      A2 = parallelTwoPart(fun3, simbName, arg,  ring);
      //СОБИРАЕМ ПЕРВУЮ И ВТОРУЮ ЧАСТИ - А И А2

       if(A == null || A2 == null) return null;
       if( ((A instanceof F)&&(((F)A).name == F.INT))||
          ((A2 instanceof F)&&(((F)A2).name == F.INT)) ){
              return null;
       }
       B[i] = new F(F.SUBTRACT, new Element[]{A, A2});
       if(i != 0) {
           arrObj = containsNewLogs(simbName, B[i], arg, nameVar, ring);
           if((boolean) arrObj[0]) {
               return null;
           }
           if(arrObj[1] != null) {
               arrEl = findConstant(B[i], (Element)arrObj[1], ring);
               if(arrEl != null) {
                    B[i + 1] = (B[i + 1] != null)? 
                   new F(F.ADD, new Element[]{ B[i + 1], arrEl[0].divide(ring.posConst[i + 1], ring)}).expand(ring):
                           arrEl[0].divide(ring.posConst[i + 1], ring);
                   B[i] = new F(F.SUBTRACT, new Element[]{B[i], arrEl[1]}).expand(ring);
               }
           }
       }

    } // end of i circle All B[i] are obtained
    
    
    
    Element[] fb = new Element[n + 2];
    fb[0] = B[0];
    if(n>0)  fb[1] = new F(F.MULTIPLY, new Element[]{nameVar.X[0] , B[1]});
    for(int i = 2; i < fb.length; i++){
      fb[i] = new F(F.MULTIPLY, new Element[]{new F(F.POW,
                new Element[]{nameVar.X[0], new NumberZ(i)}), B[i]});
    }
    F integ = new F(F.ADD, fb).expand(ring);
    return integ;
  }
  
    public Element[] findConstant(Element func, Element var, Ring ring) {
    Element fracOrPol = ring.CForm.ElementConvertToPolynom(func);
    int n_var = new Integrate().indexOfVar(var, ring);
    if(fracOrPol.isItNumber() == false) {
        Polynom pol = (fracOrPol instanceof Fraction)? (Polynom) ((Fraction)fracOrPol).num: (Polynom) fracOrPol;
        if(pol.degree(n_var) > 0) {
            Polynom[] mons = ring.CForm.dividePolynomToMonoms(pol);
            for (int i = 0; i < mons.length; i++) {
                if(mons[i].degree(n_var) > 0) {
                    boolean containsOtherVars = false;
                    for (int j = 0; j < mons[i].powers.length; j++) {
                        if(j == n_var) {
                            continue;
                        }
                        
                        if(mons[i].powers[j] > 0) {
                            containsOtherVars = true;
                            break;
                        }
                    }
                    
                    if(containsOtherVars == false) {
                        Element sub = mons[i];
                        if(fracOrPol instanceof Fraction) {
                            sub = sub.divide(((Fraction)fracOrPol).denom, ring);
                        }
                        sub = ring.CForm.ElementToF(sub);
                        return new Element[]{mons[i].coeffs[0], sub};
                    }
                }
            }
        }
    }
    
    return null;
  }

  public Element D(Element el, int varNumb, Ring ring) {
    return new Integrate().DStructurTheorem(el, varNumb, ring);
  }
  
   /*
   * Интегрирование полиномиальной части интеграла, старшей переменной которого
   * является функция экспоненты fm = exp(u).
   * По принципу Лиувилля:
   * p = \sum{j=-k}^{l}{pj*fm^{j}} = (\sum{j=-k}^{l}{qj*fm^{j}})'
   * pj = qj'+j*u'*qj при j!=0
   * p0 = q0
   * Снач. с помощью процедуры integPolExp определяется вид qj, где
   * в результате получается равенство:
   * pol = coeff_Da*a'+ coeff_a*a.  (1)
   * Тогда процесс интегрирования сводится к нахождении функции a
   * Пусть deg(a) = n, тогда deg(pol) = deg(ceff_a)+n.
   * Отсюда следует, что n = deg(pol) - deg(coeff_a)
   * если n<0, то данное подынтегральное выражение не интегрируется в элементарном виде
   * если n=0, то a=pol/coeff_a
   * если n>0, то a = \sum{i=0}^{n}{Bif_{m-1}^{i}} (2). Поставляем значение a из
   *    равенства (2) в равентво (1) и с помощью метода неопределенных коэффициентов
   *    находим Bi и их значения подстанавлием в равенство (2)
   *
   * Обозначим f = f_{m-1}
   * coeffs = Element[]{coeff_P, coeff_Da, coeff_a, r, denum}
   */

  public Element solveRischDiffEq(Element[] coeffs, ArrayList<Fname> simbolName, Element arg, Ring ring) throws Exception {
    Element Bi = null; int varNumb; varNumb=((Polynom)arg).powers.length-1;
  //  CanonicForms cf = new CanonicForms(ring, true);
    Object[] ob = hightDeg(new Element[]{coeffs[0], coeffs[1], coeffs[2]},
            simbolName, arg,  ring); //значение n и символьное имя функции последующего расширения
    if(ob[2] == null) return null;
    int n = Integer.parseInt((ob[0]).toString());
/*
Случай 1: последнее расширение кольца - моном.

В результате применения метода неопределенных коэффициентов получаем систему линейных уравнений.
Решив полученную систему линейных уравнений получаем коэффициенты искомого полинома а.
В этом случае производная полинома a 

*/
    if(n == 0 && ((Fname) ob[2]).X[0] instanceof Polynom){
// Если n=0, то a=coeff_p/coeff_a.
      Bi = (new F(F.DIVIDE, new Element[]{coeffs[0], coeffs[2]})).expand(ring);
      if(coeffs[3] != null){
        Bi = (new F(F.DIVIDE, new Element[]{Bi, coeffs[3]})).expand(ring);
      }
      return Bi;
    }
    Fname newExtand = null;
    if(n > 0 && ((Fname) ob[2]).X[0] instanceof Polynom){
      newExtand = (Fname) ob[2];
// В нулевой элемент массива factor записываем полином a.
// Так как коэффициенты этого полинома пока не известны, то
// в качестве этих коэффициентов используются объекты Fname, не указывающие на функции.
// В первый элемент массива factor записываем производную полинома a.
      Element[] factor = factorNPow(n, newExtand);
// Переносим все слагаемые исходного дифференциального уравнения в
// правую часть относительно знака =. Т. е. составляем выражение coeff_a*a + coeff_Da*Da - coeff_P
     F mul1 = new F(F.MULTIPLY, new Element[]{coeffs[2], factor[0]}).expand(
              ring);
      F mul2 = new F(F.MULTIPLY, new Element[]{NumberZ.MINUS_ONE, coeffs[0]}).
              expand(ring);
      Element mul3 = new F(F.MULTIPLY, new Element[]{factor[1], coeffs[1]}).
              expand(ring);
      Element ex = (new F(F.ADD, new Element[]{mul3, mul1, mul2})).expand(ring);
//      F infor = cf.InputForm((F) ex.ExpandFnameOrId(), true, true, true, true);


// Решаем систему линейных уравнений, получаемую при помощи метода
// неопределенных коэффициентов.
      Polynom exPol = (Polynom) ring.CForm.ElementConvertToPolynom(ex);
      Object[] newpol = StrucTheorem.PolynomNormalVar(exPol, ring.CForm);
      Vector<Element> X = new Vector<Element>();
      int kolYang = Integer.parseInt(newpol[2].toString());
      //cf.SimplifyWithGether(infor, false)

      if(new StrucTheorem().getValueOfVeriabl((Polynom) newpol[0], X, kolYang,
              ((Polynom)arg).powers.length - 1, ring)){
        Element[] el = new Element[ring.CForm.newRing.varPolynom.length];
        int len = ((Ring) newpol[1]).varPolynom.length - kolYang;
        System.arraycopy(((Ring) newpol[1]).varPolynom, 0, el, 0, len);
        for(int j = 0; j < X.size(); j++){
          el[len + j] = X.elementAt(j);
        }
        Element valEl = factor[0];
        for(int j = len; j < el.length; j++){
          valEl = new StrucTheorem().containsAndReplace(valEl, el[j],
                  new Fname(((Ring) newpol[1]).varNames[j]));
        }
        if(coeffs[0] instanceof F && ((F) coeffs[0]).name == F.DIVIDE){
          return new F(F.DIVIDE, new Element[]{valEl, coeffs[3]});
        }else{
          return valEl;
        }
      }else{
        return null;
      }
    }
// Если последнее расширение кольца - функция, то рассматриваем coeff_P, coeff_Da и coeff_a как
// полиномы от последнего расширения кольца. В результате применения метода неопределенных коэффициентов
// получаем систему дифференциальных уравнений, состоящую из дифференциальных уравнений такого же вида,
// как и исходное уравнение, но не зависящих от последнего расширения поля. Таким образом, решение исходного
// уравнения преобразовано к решению системы уравнений того же типа, но в кольце, имеющем на одно расширение меньше.
// Далее, рассматриваются различные случаи такого преобразования.

    if(((Fname) ob[2]).X[0] instanceof F){
      int min = 0;
      newExtand = (Fname) ob[2];
      F newF = (F) newExtand.X[0];

// Для coeff_P, coeff_Da и coeff_Da выделяем степени и коэффициенте при последнем расширении поля.
      Vector<Element> coeffP = new Vector<Element>();
      Vector<Element> degP = new Vector<Element>();
      coefOfHightVar(coeffs[0], newExtand, coeffP, degP, simbolName, (Polynom) arg, ring);
      sortVector(coeffP, degP, ring);
      sumCoeffOfEqualsPows(coeffP, degP, ring);
      fill(coeffP, degP);

      Vector<Element> coeff_Da = new Vector<Element>();
      Vector<Element> deg_Da = new Vector<Element>();
      coefOfHightVar(coeffs[1], (Fname) ob[2], coeff_Da, deg_Da, simbolName, (Polynom) arg, ring);
      sortVector(coeff_Da, deg_Da, ring);
      sumCoeffOfEqualsPows(coeff_Da, deg_Da, ring);
      fill(coeff_Da, deg_Da);

      Vector<Element> coeff_a = new Vector<Element>();
      Vector<Element> deg_a = new Vector<Element>();
      coefOfHightVar(coeffs[2], (Fname) ob[2], coeff_a, deg_a, simbolName, (Polynom) arg, ring);
      sortVector(coeff_a, deg_a, ring);
      sumCoeffOfEqualsPows(coeff_a, deg_a, ring);
      fill(coeff_a, deg_a);
      Fname name3 = new Fname("", ((Polynom)arg).clone());
      if(!degP.get(0).isZero(ring)){
        name3 = getLastRegularMonomial(coeffP.get(0), simbolName, arg, ring);
      }else{
        if(!deg_a.get(0).isZero(ring)){
          name3 = getLastRegularMonomial(coeff_a.get(0), simbolName,  arg, ring);
        }else{
          if(!deg_Da.get(0).isZero(ring)){
            name3 = getLastRegularMonomial(coeff_Da.get(0), simbolName, arg,  ring);
          }
        }
      }
      Element[] a = null;
// Случай 2: последнее расширение поля - логарифм.
// 
// Пусть coeff_P = p_0 f^4 + p_1 f^3 + p_2 f^2 + p_3 f + p_4
//      coeff_Da = d_0 f^2 + d_1 f + d_0
//       coeff_a = c_0 f^2 + c_1 f + c_0
//             a = a_2 f^2 + a_1 f + a_0
//            Da = a'_2 f^2 + 2*a_2 f f' + a'_1 f + a_1 f' + a'_0

      if(n >= 0 && newF.name == F.LN){
        Element Dob = D(newExtand,varNumb, ring);
        a = new Element[n + 1];
        Element P_i = coeffP.get(0);
        int pow_a = n;
// Если степени coeff_a и coeff_Da равны, то после раскрытия скобок и приравнивания
// коэффициентов при одинаковых степенях неизвестной, получаем следующие формулы:
// p_0 = d_0 a'_2 + c_0 a_2
// p_1 - 2 d_0 a_2 f' - d_1 a'_2 - c_1 a_2 = d_0 a'_1 + c_0 a_1
// p_2 - d_0 a_1 f' - 2 d_1 a_2 f' - d_1 a'_1 - d_2 a'_2 - c_1 a_1 - c_2 a_2 = d_0 a'_0 + c_0 a_0
// p_3 = d_1 a_1 f' + d_1 a'_0 + 2 d_2 a_2 f' + d_2 a'_1 + c_1 a_0 + c_2 a_1
// p_4 = d_2 a_1 f' + d_2 a'_0 + c_2 a_0
        if(((NumberZ) deg_Da.get(0)).compareTo((NumberZ) deg_a.get(0)) == 0){
          a[n] = solveRischDiffEq(new Element[]{P_i, coeff_Da.get(0),
                    coeff_a.get(0), null, null}, simbolName, arg,  ring);
          for(int i = 1; i <= n; i++){
            P_i = coeffP.get(i);
            for(int j = 0; j < i; j++){
              Element Da = D(a[pow_a + j],varNumb, ring);
              if(coeff_Da.size() > j) {
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                        new Element[]{new NumberZ(pow_a + j), coeff_Da.get(j),
                          a[pow_a + j], Dob})});
              }
              if(coeff_Da.size() > (j + 1)){
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                          new Element[]{coeff_Da.get(j + 1), Da})});
              }
              if(coeff_a.size() > (j + 1)) {
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(
                          F.MULTIPLY, new Element[]{coeff_a.get(j + 1),
                            a[pow_a + j]})});
              }
            }
            if(P_i.expand(ring).isZero(ring) && name3.X[0] instanceof Polynom){
              a[pow_a - 1] = NumberZ.ZERO;
            }else{
              Element[] els = notDenum(P_i, coeff_a.get(0), simbolName, arg,  ring);
              a[pow_a - 1] = solveRischDiffEq(new Element[]{els[0],
                        coeff_Da.get(0).multiply(els[1], ring), els[2], els[3],
                        els[4]}, simbolName, arg, ring);
            }
            pow_a--;
          }
          if(degP.get(0).intValue() > n){
            if(coeffP.size() > (n + 1)){
              P_i = coeffP.get(n + 1);
            }else{
              P_i = NumberZ.ZERO;
            }
            for(int j = 0; j < n + 1; j++){
              if(coeff_Da.size() > j) {
                P_i = new F(F.SUBTRACT, new Element[]{P_i,
                        new F(F.MULTIPLY, new Element[]{new NumberZ(j),
                          coeff_Da.get(j), a[j], Dob})});
              }
              if(coeff_Da.size() > (j + 1)){
                Element Da = D(a[j],varNumb, ring);
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                          new Element[]{coeff_Da.get(j + 1), Da})});
              }
              if(coeff_a.size() > (j + 1)) {
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                          new Element[]{coeff_a.get(j + 1), a[j]})});
              }
            }
            if(!P_i.expand(ring).isZero(ring)){
              return null;
            }
          }
        }
// Если степень coeff_a больше степени coeff_Da, то после раскрытия скобок и приравнивания
// коэффициентов при одинаковых степенях неизвестной, получаем следующие формулы:
// Пусть для определенности coeff_Da = d_0.
// p_0 = c_0 a_2
// p_1 - c_1 a_2 = c_0 a_1
// p_2 - d_0 a'_2 - c_1 a_1 - c_2 a_2 = c_0 a_0
// p_3 = 2 d_0 a_2 f' + d_0 a'_1 + c_1 a_0 + c_2 a_1
// p_4 = d_0 a_1 f' + d_0 a'_0 + c_2 a_0
        if(((NumberZ) deg_Da.get(0)).compareTo((NumberZ) deg_a.get(0)) < 0){
          a[n] = new F(F.DIVIDE, new Element[]{P_i, coeff_a.get(0)});
          int h = deg_a.get(0).subtract(deg_Da.get(0), ring).intValue();
          for(int i = 1; i <= n; i++){
            P_i = coeffP.get(i);
            int k = 0;
            for(int j = 0; j < i; j++){
              if(coeff_a.size() > (j + 1)) {
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                        new Element[]{coeff_a.get(j + 1), a[pow_a + j]})});
              }
              if(j >= h && k <= deg_Da.get(0).intValue()){
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                          new Element[]{new NumberZ(pow_a + j), coeff_Da.get(k),
                            a[pow_a + j], Dob})});
                k++;
              }
              if((j + 1) >= h && k <= deg_Da.get(0).intValue()){
                Element Da = D(a[pow_a + j], varNumb, ring);
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                          new Element[]{coeff_Da.get(k), Da})});
              }
            }
            if(P_i.expand(ring).isZero(ring) && name3.X[0] instanceof Polynom){
              a[pow_a - 1] = NumberZ.ZERO;
            }else{
              a[pow_a - 1] = new F(F.DIVIDE, new Element[]{P_i, coeff_a.get(0)});
            }
            pow_a--;
          }
          if(degP.get(0).intValue() > n){
            if(coeffP.size() > n + 1){
              P_i = coeffP.get(n + 1);
            }else{
              P_i = NumberZ.ZERO;
            }
            int k = 0;
            for(int j = 0; j < n + 1; j++){
              if(coeff_a.size() > (j + 1)){
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                          new Element[]{coeff_a.get(j + 1), a[j]})});
              }
              if( (j > (h - 1))&&(coeff_Da.size() > k) ){
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                          new Element[]{new NumberZ(j), coeff_Da.get(k), a[j],
                            Dob})});
                k++;
              }
              if( (j > (h - 2))&&(coeff_Da.size() > k) ){
                Element Da = D(a[j], varNumb, ring).expand(ring);
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                          new Element[]{coeff_Da.get(k), Da})});
              }
            }
            if(!P_i.expand(ring).isZero(ring)){
              return null;
            }
          }
        }
// Если степень coeff_a меньше степени coeff_Da, то после раскрытия скобок и приравнивания
// коэффициентов при одинаковых степенях неизвестной, получаем следующие формулы:
// Предположим для определенности, что coeff_a = c_0.
// p_0 = d_0 a'_2
// p_1 - 2 d_0 a_2 f' - d_1 a'_2 = d_0 a'_1
// p_2 - d_0 a_1 f' - 2 d_1 a_2 f' - d_1 a'_1 - d_2 a'_2 - c_0 a_2 = d_0 a'_0
// p_3 = d_1 a_1 f' + d_1 a'_0 + 2 d_2 a_2 f' + d_2 a'_1 + c_0 a_1
// p_4 = d_2 a_1 f' + d_2 a'_0 + c_0 a_0
        if(((NumberZ) deg_Da.get(0)).compareTo((NumberZ) deg_a.get(0)) > 0){
          int h = deg_Da.get(0).subtract(deg_a.get(0), ring).intValue();
          int step = deg_Da.get(0).intValue() + n;
          if(degP.get(0).intValue() != step) {
              P_i = NumberZ.ZERO;
          }
          
          a[n] =new Integrate().mainProcOfInteg(new F(F.DIVIDE,
                  new Element[]{P_i, coeff_Da.get(0)}), arg, ring);
          if( (a[n] instanceof F)&&(((F)a[n]).name == F.INT) ) {
              return null;
          }
          for(int i = 1; i <= n; i++){
            if(coeffP.size() > i) {
              P_i = (degP.get(i).intValue() == step)? coeffP.get(i) : NumberZ.ZERO;
            } else {
              P_i = NumberZ.ZERO;
            }
            for(int j = 0; j < i; j++){
              Element Da = D(a[pow_a + j], varNumb, ring);
              if(coeff_Da.size() > j) {
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                        new Element[]{new NumberZ(pow_a + j), coeff_Da.get(j),
                          a[pow_a + j], Dob})});
                if(coeff_Da.size() > (j + 1) ) {
                  P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                        new Element[]{coeff_Da.get(j + 1), Da})});
                }
              }
              if( (j >= (h - 1)) && ( (coeff_a.size() - 1) > (j - h)) ){
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                          new Element[]{coeff_a.get(j - h + 1), a[pow_a + j]})});
              }
            }
            if(P_i.expand(ring).isZero(ring) && name3.X[0] instanceof Polynom){
              a[pow_a - 1] = NumberZ.ZERO;
            }else{
                a[pow_a - 1] =new Integrate().mainProcOfInteg(new F(F.DIVIDE,
                      new Element[]{P_i, coeff_Da.get(0)}), arg, ring);
                if( (a[pow_a - 1] instanceof F)&&(((F)a[pow_a - 1]).name == F.INT) ) {
                    return null;
                }
            }
            pow_a--;
          }
          if(degP.get(0).intValue() > n){
            if(coeffP.size() > (n + 1)) {
              P_i = coeffP.get(n + 1);
            }else{
              P_i = NumberZ.ZERO;
            }
            for(int j = 0; j < n + 1; j++){
              Element Da = D(a[j], varNumb, ring);
              if( (coeff_Da.size() > j)&&(j != 0) ) {
                P_i = new F(F.SUBTRACT, new Element[]{P_i,
                        new F(F.MULTIPLY, new Element[]{new NumberZ(j),
                          coeff_Da.get(j), a[j], Dob})});
                if(coeff_Da.size() > (j + 1)) {
                  P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                          new Element[]{coeff_Da.get(j + 1), Da})});
                }
              }
              if( (j >= (h - 1)) && ((coeff_a.size() - 1) > (j - h)) ) {
                  P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                          new Element[]{coeff_a.get(j - h + 1), a[j]})});
              }
            }
            if(!P_i.expand(ring).isZero(ring)){
              return null;
            }
          }
        }
      }
      if(n < 0 && newF.name == F.LN){
        return null;
      }
// Случай 3: последнее расширение поля - экспонента.
// В этом случае полиномы могут содержать
// слагаемые, содержащие неизвестную в отрицательной степени. Пусть:
//        f = exp(u)
// coeff_Da = d_0 f + d_1 + d_2 f^-1
//  coeff_a = c_0 f + c_1 + c_2 f^-1
//        a = a_2 f + a_1 + a_0 f^-1
//       Da = a'_2 f + a_2 u' f + a'_1 + a'_0 f^-1 - a_0 u' f^-1
//  coeff_P = p_0 f^2 + p_1 f + p_2 + p_3 f^-1 + p_4 f^-2
      if(newF.name == F.EXP){
        int max = n;
        min = Integer.parseInt((ob[1]).toString());
        if(max < 0 && min >= 0){
          max = min;
          min = n;
        }
//        if() min = 0;
        Element Dob = D(newF.X[0], varNumb, ring);
        if(min > 0) {
            n = max - min;
        } else {
            n = max + Math.abs(min);
        }
        a = new Element[n + 1];
        Element P_i = coeffP.get(0);
        int p = 0;
        int step = max - 1;
//        if(degP.get(0).intValue()!=max){
//          P_i = NumberZ.ZERO; p=0;
//        }
        int pow_a = n;
        //=============================================================================
// Если степени coeff_a и coeff_Da равны, то после раскрытия скобок и приравнивания
// коэффициентов при одинаковых степенях неизвестной, получаем следующие формулы:
// p_0 = d_0 a'_2 + a_2(d_0 u' + c_0)    (коэффициенты при f^2)
// p_1 - d_1 a'_2 - d_1 a_2 u' - c_1 a_2 = d_0 a'_1 + a_1 c_0    (коэффициенты при f^1)
// p_2 - d_1 a'_1 - c_1 a_1 - d_2 a'_2 - d_2 a_2 u' - c_2 a_2 = d_0 a'_0 + a_0(c_0 - d_0 u')   (коэффициенты при f^0)
// p_3 = d_1 a'_0 - d_1 a_0 u' + d_2 a'_1 + c_1 a_0 + c_2 a_1     (коэффициенты при f^-1)
// p_4 = d_2 a'_0 - d_2 a_0 u' + c_2 a_0   (коэффициенты при f^-2)
//
// Если положительная степень coeff_Da больше степени coeff_a, то получаем следующие формулы:
// Пусть для определенности, coeff_a = c_0 + c_1 f^-1.
// p_0 = d_0 a'_2 + a_2 d_0 u'    (коэффициенты при f^2)
// p_1 - d_1 a'_2 - d_1 a_2 u' - c_0 a_2 = d_0 a'_1    (коэффициенты при f^1)
// p_2 - d_1 a'_1 - c_0 a_1 - d_2 a'_2 - d_2 a_2 u' - c_1 a_2 = d_0 a'_0 - a_0 d_0 u'   (коэффициенты при f^0)
// p_3 = d_1 a'_0 - d_1 a_0 u' + d_2 a'_1 + c_0 a_0 + c_1 a_1     (коэффициенты при f^-1)
// p_4 = d_2 a'_0 - d_2 a_0 u' + c_1 a_0   (коэффициенты при f^-2)
//
// Далее рассматриваются оба случая.
        if(((NumberZ) deg_Da.get(0)).compareTo((NumberZ) deg_a.get(0)) == 0 || ((NumberZ) deg_Da.
                get(0)).compareTo((NumberZ) deg_a.get(0)) > 0){
          int h = deg_Da.get(0).subtract(deg_a.get(0), ring).intValue();
          step = deg_Da.get(0).intValue() + max;
          if(degP.get(0).intValue() != step){
            P_i = NumberZ.ZERO;
          }else{
            p++;
          }
          step--;
          Element cai = coeff_a.get(0);
          if(h != 0){
            cai = NumberZ.ZERO;
          }
          Element[] els = notDenum(P_i, coeff_Da.get(0).multiply(new NumberZ(max),
                  ring).multiply(Dob, ring).add(cai, ring).expand(ring),
                  simbolName,  arg,  ring);
          if(els[0].isZero(ring)){
            a[pow_a] = NumberZ.ZERO;
          }else{
            a[pow_a] = solveRischDiffEq(new Element[]{els[0],
                      coeff_Da.get(0).multiply(els[1], ring), els[2], els[3],
                      els[4]}, simbolName, arg, ring);
          }
          for(int i = 1; i <= n; i++){
            if(coeffP.size() > p) {
              P_i = coeffP.get(p);
              if(degP.get(p).intValue() != step){
                P_i = NumberZ.ZERO;
              }else{
                p++;
              }
            } else {
              P_i = NumberZ.ZERO;
            }
            step--;
            for(int j = 0; j < i; j++){
              Element Da = D(a[pow_a + j], varNumb, ring);
              if( (coeff_Da.size() - 1) > j) {
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                        new Element[]{new NumberZ(min + pow_a + j),
                          coeff_Da.get(j + 1), a[pow_a + j], Dob}), new F(
                        F.MULTIPLY, new Element[]{coeff_Da.get(j + 1), Da})});
              }
              if( (j >= (h - 1))&&((coeff_a.size() - 1) > (j - h)) ){
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                          new Element[]{coeff_a.get(j - h + 1), a[pow_a + j]})});
              }
            }
            if(P_i.expand(ring).isZero(ring) && name3.X[0] instanceof Polynom){
              a[pow_a - 1] = NumberZ.ZERO;
            }else{
              els = notDenum(P_i, coeff_Da.get(0).multiply(
                      new NumberZ(min + pow_a - 1), ring).multiply(Dob, ring).add(cai
                              , ring).expand(ring), simbolName, arg,  ring);
              if(els[0].isZero(ring)){
                a[pow_a - 1] = NumberZ.ZERO;
              }else{
                a[pow_a - 1] = solveRischDiffEq(new Element[]{els[0],
                          coeff_Da.get(0).multiply(els[1], ring), els[2], els[3],
                          els[4]}, simbolName, arg,  ring);
              }
            }
            pow_a--;
          }
          //======test==============
          //=====end test===========
        }
        //=============================================================================
// Если степень coeff_a больше спепени coeff_Da, то после раскрытия скобок и приравнивания
// коэффициентов при одинаковых степенях неизвестной, получаем следующие формулы:
// Пусть для определенности, coeff_Da = d_0 + d_1 f^-1.
// p_0 = a_2 c_0    (коэффициенты при f^2)
// p_1 - d_0 a'_2 - d_0 a_2 u' - c_1 a_2 = a_1 c_0    (коэффициенты при f^1)
// p_2 - d_0 a'_1 - c_1 a_1 - d_1 a'_2 - d_1 a_2 u' - c_2 a_2 = a_0 c_0   (коэффициенты при f^0)
// p_3 = d_0 a'_0 - d_0 a_0 u' + d_1 a'_1 + c_1 a_0 + c_2 a_1     (коэффициенты при f^-1)
// p_4 = d_1 a'_0 - d_1 a_0 u' + c_2 a_0   (коэффициенты при f^-2)
        if(((NumberZ) deg_Da.get(0)).compareTo((NumberZ) deg_a.get(0)) < 0){
          step = deg_a.get(0).intValue() + max;
          if(degP.get(0).intValue() != step){
            P_i = NumberZ.ZERO;
          }else{
            p++;
          }
          step--;
          a[pow_a] = new F(F.DIVIDE, new Element[]{P_i, coeff_a.get(0)});
          int h = deg_a.get(0).subtract(deg_Da.get(0), ring).intValue();
          for(int i = 1; i <= n; i++){
            P_i = (degP.size() > p)? coeffP.get(p) : NumberZ.ZERO;
            if( (degP.size() > p)&&(degP.get(p).intValue() != step) ){
              P_i = NumberZ.ZERO;
            }else{
              p++;
            }
            step--;
            for(int j = 0; j < i; j++){
              if( (coeff_a.size() - 1) > j) {
                P_i = P_i.subtract(new F(F.MULTIPLY, new Element[]{coeff_a.get(
                        j + 1), a[pow_a + j]}), ring);
              }
              if( (j >= (h - 1))&&(coeff_Da.size() > j) ){
                Element Da = D(a[pow_a + j], varNumb, ring);
                P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                          new Element[]{new NumberZ(min + pow_a + j), coeff_Da.get(
                            j), a[pow_a + j], Dob}), new F(F.MULTIPLY,
                          new Element[]{coeff_Da.get(j), Da})});
              }
            }
            if(P_i.expand(ring).isZero(ring) && name3.X[0] instanceof Polynom){
              a[pow_a - 1] = NumberZ.ZERO;
            }else{
              a[pow_a - 1] = new F(F.DIVIDE, new Element[]{P_i, coeff_a.get(0)});
            }
            pow_a--;
          }
          //======test==============
          if(degP.size() > p && degP.get(p).intValue() == step){
            P_i = coeffP.get(p);
          }else{
            P_i = NumberZ.ZERO;
          }
          for(int j = 0; j < n; j++){
            Element Da = D(a[j], varNumb, ring);
            if((coeff_a.size() - 1) > j){
              P_i = P_i.subtract(new F(F.MULTIPLY, new Element[]{coeff_a.get(
                        j + 1), a[j]}), ring);
            }
            if(j >= (h - 1) && (coeff_Da.size() - 1) > (j - h) ){
              P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                        new Element[]{new NumberZ(min + j), coeff_Da.get(j - h + 1),
                            a[j], Dob})});
              P_i = new F(F.SUBTRACT, new Element[]{P_i, new F(F.MULTIPLY,
                        new Element[]{coeff_Da.get(j - h + 1), Da})});
            }
          }
          if(!P_i.expand(ring).isZero(ring)){
            return null;
          }
          //=====end test===========
        }
      }
// Используя найденные коэффициенты полинома a, собираем этот полином. (Умножаем
// каждый коэффициент полинома a на переменную в соответствующей степени, и складываем полученные выражения.)
      Bi = a[0];
      if(min != 0){
        Bi = new F(F.MULTIPLY, new Element[]{Bi, new F(F.POW, new Element[]{newF,
                    new NumberZ(min)})});
      }
      min++;
      for(int i = 1; i <= n; i++){
        Element el = a[i];
        if(min != 0){
          el = new F(F.MULTIPLY, new Element[]{el, new F(F.POW, new Element[]{
                      newF, new NumberZ(min)})});
        }
        min++;
        Bi = new F(F.ADD, new Element[]{Bi, el});
      }
      return Bi;
    }
    return Bi;
  } 
  
  /**
   * Заполнение векторов степеней и коэффициентов:
   *  Если все степени отрицательные, то заполняем векторы
   *  так, чтобы в векторе степеней содержались все степени
   *  от наименьшей степени до нулевой.
   * 
   *  Если все степени положительные, то заполняем векторы
   *  так, чтобы в векторе степеней содержались все степени
   *  от нулевой до наибольшей.
   * 
   *  Если есть и отрицательные и положительные степени,
   *  то заполняем векторы так, чтобы в векторе степеней
   *  содержались все степени от наименьшей отрицательной
   *  до наибольшей положительной.
   */
  public void fill(Vector<Element> coeff, Vector<Element> deg) {
      Element[] c = new Element[coeff.size()];
      Element[] d = new Element[deg.size()];
      for(int i=d.length - 1; i>=0; i--) {
          c[i] = coeff.get(i);
          d[i] = deg.get(i);
          coeff.remove(i);
          deg.remove(i);
      }
      int k = 0;
      int a = (d[0].intValue() > 0)? d[0].intValue(): 0;
      int b = (d[d.length - 1].intValue() < 0 )? d[d.length - 1].intValue(): 0;
      for(int i=a; i >= b; i--) {
          deg.add(new NumberZ(i));
          if( (d.length > k) && (i == d[k].intValue()) ) {
              coeff.add(c[k]);
              k++;
          } else {
              coeff.add(NumberZ.ZERO);
          }
      }
  }
  
  
  /* Избавляется от знаменателя в равенстве  pj = qj'+j*u'*qj при j!=0.
   * qj - рациональная функия. Пусть qj = a/b. Если у qj знаменатель не константа,
   * то степень знаменателя при дифференцировании qj возрастет и не может быть сокращен
   * с др. множителем.
   * Если pj - полином, то qj тоже будет полиномом:
   *    pj = a'+j*u'*a    (1)
   * Если pj - полином, а u' - дробно-рацю ф-ция (u'=v/w), то получаем следующее:
   *    pj = qj'+j*v/w*qj
   *    w*pj = w*qj'+j*v*qj
   *    отсюда видно, что qj является полиномом, т.к. левая часть равенства - полином
   *    Получаем: w*pj = w*a'+j*v*a      (2)
   * Если pj - дробно-рац. ф-ция, т.е. pj=r/g, то qj тоже будет дробно-рац. ф-ция (qj=a/b):
   *    pj = qj'+j*u'*qj
   *    r/g = (a'b-ab')/b^2+ju'a/b
   *    отсюда b=\sqrt(g), r = a'*b-a*b'+j*u'*a*b = b*a'+(-b'+j*u'*b)a    (3)
   * Если pj - дробно-рац. ф-ция, т.е. pj=r/g, и u' - дробно-рацю ф-ция (u'=v/w), то получаем следующее:
   *    r/g = qj'+j*v/w'*qj, следовательно qj тоже будет дробно-рац. ф-ция (qj=a/b)
   *    r/g = (a'b-ab')/b^2+j*v*a/(b*w)
   *    отсюда b=\sqrt(g/w), r = a'*b*w-a*b'*w+j*v*a*b = w*b*a'+(-b'*w+j*v*b)a     (4)
   * Обозначим в равенствах (1)(2)(3)(4) левую часть за coeff_P, коэфф-т при a' - coeff_Da,
   * а коэфф-т при a - coeff_a. В результате получается равенство:
   * coeff_P = coeff_Da*a'+ coeff_a*a    , где
   * a, coeff_P, coeff_Da, coeff_a - полиномы.
  */
  public Element[] notDenum(Element coeff_P, Element coeff_a, ArrayList<Fname> simbolName, Element arg, Ring ring) {
    Fname s1 = getLastRegularMonomial(coeff_P, simbolName, arg,  ring), s2 = getLastRegularMonomial(
            coeff_a, simbolName, arg,  ring);
    int varNumb=((Polynom)arg).powers.length-1;
    if(simbolName.indexOf(s1) > simbolName.indexOf(s2)){
      Element[] els = notDenum(coeff_P, NumberZ.ONE, s1,varNumb, simbolName, (Polynom) arg, ring);
      els[2] = els[2].multiply(coeff_a, ring);
      return els;
    }
    if(simbolName.indexOf(s1) < simbolName.indexOf(s2)){
      Element[] els = notDenum(NumberZ.ONE, coeff_a, s2,varNumb, simbolName, (Polynom) arg, ring);
      els[0] = els[0].multiply(coeff_P, ring);
      return els;
    }
    return notDenum(coeff_P.expand(ring), coeff_a, s1,varNumb, simbolName, (Polynom) arg, ring);
  }

  public Element[] notDenum(Element coeff_P, Element coeff_a, Fname name, int varNumb, ArrayList<Fname> simbName, Polynom arg, Ring ring) {
    Element r = null, denum = null, coeff_Da = NumberZ.ONE;
    
    
    int n_var = new Integrate().indexOfVar(name, ring);
    Element polOrFracP = ring.CForm.ElementConvertToPolynom(coeff_P);
    boolean PisFrac = new Integrate().isItFraction(polOrFracP, simbName, arg, n_var, ring);
    
    Element polOrFracA = ring.CForm.ElementConvertToPolynom(coeff_a);
    boolean AisFrac = new Integrate().isItFraction(polOrFracA, simbName, arg, n_var, ring);
    
    
    if(PisFrac && !AisFrac){
      Element numP = ring.CForm.ElementToF(((Fraction)polOrFracP).num);
      Element denomP = ring.CForm.ElementToF(((Fraction)polOrFracP).denom);
      
      r = new F(F.SQRT, denomP).expand(ring);
      if(((F) r).name == F.ABS){
        r = ((F) r).X[0];
      }
      Element Dr = D(r, varNumb, ring);
      Element el1 = new F(F.MULTIPLY, new Element[]{NumberZ.MINUS_ONE, Dr});
      Element el2 = new F(F.MULTIPLY, new Element[]{coeff_a, r});
      coeff_a = new F(F.ADD, new Element[]{el1, el2});
      coeff_P = numP;
      coeff_Da = r;
    } else if(!PisFrac && AisFrac){
      Element numA = ring.CForm.ElementToF(((Fraction)polOrFracA).num);
      Element denomA = ring.CForm.ElementToF(((Fraction)polOrFracA).denom);
      
      coeff_P = new F(F.MULTIPLY, new Element[]{coeff_P, denomA});
      coeff_Da = coeff_Da.multiply(denomA, ring);
      coeff_a = numA;
    } else if(PisFrac && AisFrac){
      Element numP = ring.CForm.ElementToF(((Fraction)polOrFracP).num);
      Element denomP = ring.CForm.ElementToF(((Fraction)polOrFracP).denom);
      Element numA = ring.CForm.ElementToF(((Fraction)polOrFracA).num);
      Element denomA = ring.CForm.ElementToF(((Fraction)polOrFracA).denom);
      
      r = (new F(F.DIVIDE, new Element[]{denomP, denomA})).expand(ring);
      r = new F(F.SQRT, r);
      Element Dr = D(r, varNumb, ring);
      Element el1 = new F(F.MULTIPLY, new Element[]{NumberZ.MINUS_ONE, Dr, denomA});
      Element el2 = new F(F.MULTIPLY, new Element[]{numA, r});
      coeff_Da = new F(F.MULTIPLY, new Element[]{r, denomA}).expand(ring);
      coeff_a = new F(F.ADD, new Element[]{el1, el2});
      coeff_P = numP;
    }
    
    return new Element[]{coeff_P, coeff_Da, coeff_a, r, denum};
  }
  /* 
   * Пусть p - полиномиальная часть подинтегрального выражения.
   * По теореме Лиувилля \int p = q + \sum \log(v_j), т.е.
   * p = q' + \sum v'_j/v_j
   * Пусть p=\sum_(i=-k)^l p_i f^i, q=\sum_(i=-k)^l q_i f^i. После раскрытия скобок
   * и приравнивания коэффициентов при одинаковых степенях неизвестной
   * получим систему уравнений вида:
   * p_i = q'_i + i u' q_i,      i!=0
   * p_i = q'_i + \sum v'_j/v_j, i==0
   * где u - аргумент функции f.
   * Решаем каждое уравнение системы в отдельности.
   * Если i!=0, то при помощи метода notDenum избавляемся от знаменателей,
   * а затем находим q_i из полученного уравнения при помощи метода 
   * solveRischDiffEq.
   *
   * Если i==0, то q_0 + \sum log(v_j) = \int p_0 (находим интеграл от p_0)
   *
   *
   * expression - подинтегральная функция
   * nameVar - последнее расширение дифференциального поля
   * simbName - список регулярных мономов
   * arg - переменная интегрирования
   */

  public F integPolExpOld(Element expression, Fname nameVar, ArrayList<Fname> simbName, Element arg, Ring ring ) throws Exception {
    //Darg - производная аргумента nameVar
    Element Darg = ((F) nameVar.X[0]).X[0];
   // Darg = Darg instanceof F ? cf.InputForm((F)Darg, new Page(ring), null) : Darg;
    Darg = D(Darg, ((Polynom)arg).powers.length-1, ring);
    Darg = Darg instanceof F ? ((F) Darg).expand(ring) : (new F(Darg)).
            expand(ring);//приводим производную к каноническому виду
    if(expression instanceof F){
      expression = ((F) expression).expand(ring);
    }
    //вектор коэффициентов при данной функции
    Vector<Element> coeff = new Vector<Element>();
    //вектор стпеней при данной функции соответственно
    Vector<Element> deg = new Vector<Element>();
    coefOfHightVar(expression, nameVar, coeff, deg, simbName, (Polynom) arg, ring);
    sumCoeffOfEqualsPows(coeff, deg, ring);
    Element[] B = new Element[deg.size()];
    Element dd = Darg;
    for(int i = 0; i < deg.size(); i++){
      Darg = (new F(F.MULTIPLY, new Element[]{dd, deg.elementAt(i)})).expand(
              ring);
      Element[] els = notDenum(coeff.elementAt(i), Darg, simbName,  arg,  ring);
      //интегрируем свободный член
      if((deg.elementAt(i)).isZero(ring)){
        Fname name = getLastRegularMonomial(els[0], simbName, arg,  ring);
        B[i] = polPartInteg(els[0], name, simbName, arg,  ring);
      }else{
        //находим B[i] из равенства: B'[i]+iB[i]Darg= coeff[i]
        B[i] = solveRischDiffEq(els, simbName, arg,  ring);
        if(B[i] == null){
          return null;//new F(F.INT, new Element[]{expression, new Polynom("x", ring)});
        }
      }
    }

    Element[] fb = new Element[deg.size()];
    Element funExp = nameVar.X[0];
    for(int i = 0; i < deg.size(); i++){
      if(deg.elementAt(i).isZero(ring)){
        fb[i] = B[i];
      }else{
        fb[i] = new F(F.MULTIPLY, new Element[]{B[i], new F(F.POW,
                  new Element[]{funExp, deg.elementAt(i)})});
      }
    }
    //page.replaceOfSymbols(new F(F.ADD, fb), page.expr);
    return (new F(F.ADD, fb));
  }
  
  
  
  
  
  public F integPolExp(Element expression, Fname nameVar, ArrayList<Fname> simbName, Element arg, Ring ring ) throws Exception {
    //Darg - производная аргумента nameVar
    Element Darg = ((F) nameVar.X[0]).X[0];
   // Darg = Darg instanceof F ? cf.InputForm((F)Darg, new Page(ring), null) : Darg;
    Darg = D(Darg, ((Polynom)arg).powers.length-1, ring);
    Darg = Darg instanceof F ? ((F) Darg).expand(ring) : (new F(Darg)).
            expand(ring);//приводим производную к каноническому виду
    if(expression instanceof F){
      expression = ((F) expression).expand(ring);
    }
    //вектор коэффициентов при данной функции
    Vector<Element> coeff = new Vector<Element>();
    //вектор стпеней при данной функции соответственно
    Vector<Element> deg = new Vector<Element>();
    coefOfHightVar(expression, nameVar, coeff, deg, simbName, (Polynom) arg, ring);
    sumCoeffOfEqualsPows(coeff, deg, ring);
    Element[] B = new Element[deg.size()];
    Element dd = Darg;
    for(int i = 0; i < deg.size(); i++){
      Darg = (new F(F.MULTIPLY, new Element[]{dd, deg.elementAt(i)})).expand(
              ring);
      Fname name1 = getLastRegularMonomial(coeff.elementAt(i), simbName, arg,  ring);
      Fname name2 = getLastRegularMonomial(Darg, simbName, arg,  ring);
      int varNumb=((Polynom)arg).powers.length-1;
      Fname name = name1;
    
      if(simbName.indexOf(name1) < simbName.indexOf(name2)){
          name = name2;
      }
      Element[] els = notDenom(coeff.elementAt(i), Darg, name,varNumb, simbName, (Polynom) arg, ring);
      //интегрируем свободный член
      if((deg.elementAt(i)).isZero(ring)){
        name = getLastRegularMonomial(els[2], simbName, arg,  ring);
        B[i] = polPartInteg(els[2], name, simbName, arg,  ring);
      }else{
        //находим B[i] из равенства: B'[i]+iB[i]Darg= coeff[i]
        B[i] = helpSolveDE(els, simbName, arg,  ring);
        if(B[i] == null){
          return null;//new F(F.INT, new Element[]{expression, new Polynom("x", ring)});
        } else if(els[3].isOne(ring) == false) {
            B[i] = new F(F.DIVIDE, new Element[]{B[i], els[3]});
        }
      }
    }

    Element[] fb = new Element[deg.size()];
    Element funExp = nameVar.X[0];
    for(int i = 0; i < deg.size(); i++){
      if(deg.elementAt(i).isZero(ring)){
        fb[i] = B[i];
      }else{
        fb[i] = new F(F.MULTIPLY, new Element[]{B[i], new F(F.POW,
                  new Element[]{funExp, deg.elementAt(i)})});
      }
    }
    //page.replaceOfSymbols(new F(F.ADD, fb), page.expr);
    return (new F(F.ADD, fb));
  }
  
  



    public Element[] notDenom(Element g, Element f, Fname name, int varNumb, ArrayList<Fname> simbName, Polynom arg, Ring ring) {
      Element[] coeffs = new Element[4];
      Element B = null;
      Element E = null;
      Fname varG = getLastRegularMonomial(g, simbName, arg, ring);
      boolean gIsPolynom = funcIsPolynom(g, varG, simbName, arg, ring);
      if ( (gIsPolynom == false) && (g instanceof F) && (((F)g).name == F.DIVIDE) ) {
          B = ((F)g).X[0];
          E = ((F)g).X[1];
      } else {
          B = g;
          E = ring.numberONE;
      }
      
      Element A = null;
      Element D = null;
      Fname varF = getLastRegularMonomial(f, simbName, arg, ring);
      boolean fIsPolynom = funcIsPolynom(f, varF, simbName, arg, ring);
      if ( (fIsPolynom == false) && (f instanceof F) && (((F)f).name == F.DIVIDE) ) {
          A = ((F)f).X[0];
          D = ((F)f).X[1];
      } else {
          A = f;
          D = ring.numberONE;
      }
      
      
      Element T = null;
      Element numT = null;
      Element denomT = null;
      Element Dvar = D(name, varNumb, ring).expand(ring);
      
      
      if ( gIsPolynom ) {
          numT = ring.numberONE;
      } else {
          Element DE = D(E, varNumb, ring).expand(ring);
          numT = new F(F.DIVIDE, new Element[]{DE, Dvar}).expand(ring).GCD(E, ring).expand(ring);
      }
      
      if ( fIsPolynom ) {
          denomT = ring.numberONE;
      } else {
          Element G = E.GCD(D, ring).expand(ring);
          Element DG = D(G, varNumb, ring).expand(ring);
          denomT = new F(F.DIVIDE, new Element[]{DG, Dvar}).expand(ring).GCD(G, ring).expand(ring);
      }
      
      T = new F(F.DIVIDE, numT, denomT).expand(ring);
      
      
      coeffs[0] = new F(F.MULTIPLY, new Element[]{T, D}).expand(ring);
      
      Element func1 = new F(F.MULTIPLY, new Element[]{A, T}).expand(ring);
      Element func2 = new F(F.MULTIPLY, new Element[]{D, D(T, varNumb, ring)}).expand(ring);
      coeffs[1] = new F(F.SUBTRACT, new Element[]{func1, func2}).expand(ring);
      
      coeffs[2] = new F(F.MULTIPLY, new Element[]{B, D, T, T}).expand(ring);
      coeffs[2] = new F(F.DIVIDE, new Element[]{coeffs[2], E}).expand(ring);
      
      coeffs[3] = T;
      return coeffs;
  }
    
    
    
  public Element helpSolveDE(Element[] els, ArrayList<Fname> simbName, Element arg, Ring ring) throws Exception {
      Fname[] names = new Fname[3];
      
      for (int j = 0; j < 3; j++) {
          names[j] = getLastRegularMonomial(els[j], simbName, arg,  ring);
      }
      
      Fname lastname = names[0];  
      for (int j = 0; j < 2; j++) {
          if (simbName.lastIndexOf(names[j]) < simbName.lastIndexOf(names[j + 1])) {
              lastname = names[j + 1];
          }
      }
      
      Element result = null;
      
      if (lastname.X[0] instanceof F) {
          if ( ((F)lastname.X[0]).name == F.LN) {
              result = lnDE(els[0], els[1], els[2], lastname, simbName, arg, ring);
          } else if (((F)lastname.X[0]).name == F.EXP) {
              result = expDE(els[0], els[1], els[2], lastname, simbName, arg, ring);
          }
      } else {
            result = polDE(els[0], els[1], els[2], lastname, simbName, arg, ring);
      }
      
      return result;
  }
  
  public Element polDE(Element A, Element B, Element C, Fname nameVar, ArrayList<Fname> simbName, Element arg, Ring ring) throws Exception {
      Element[] els = new Element[]{A, B, C};
      Polynom[] polABC = new Polynom[3];
      int[] degABC = new int[3];
      
      int n = 0;
      int intVar = ((Polynom)arg).powers.length - 1;
      
      for (int i = 0; i < 3; i++) {
          if (els[i] instanceof F) {
              els[i] = els[i].expand(ring).ExpandFnameOrId();
          }
          
          if (els[i] instanceof Polynom) {
              polABC[i] = (Polynom) els[i];
          } else {
              Element el = ring.CForm.ElementConvertToPolynom(els[i]);
              if (el.isItNumber()) {
                  el = new Polynom(new int[]{}, new Element[]{els[i]});
              }
              if (el instanceof Polynom == false) {
                  return null;
              }
              polABC[i] = (Polynom) el;
          }
          
          degABC[i] = polABC[i].degree(intVar);
          if (degABC[i] < 0) {
              degABC[i] = 0;
          }
      }
      
      int degA = degABC[0];
      int degB = degABC[1];
      int degC = degABC[2];
      
      Polynom polA = polABC[0];
      Polynom polB = polABC[1];
      Polynom polC = polABC[2];
      
      if(degA < degB + 1) {
          n = degC - degB;
      } else if(degA > degB + 1) {
          n = Math.max(0, degC - degA + 1);
      } else if(degA == degB + 1) {
          Element r = polB.coeffs[0].divide(polA.coeffs[0], ring).multiply(ring.numberMINUS_ONE, ring);
          
          if(new Integrate().isItFracNumber(r, ring) == false) {
              n = Math.max(r.intValue(), degC - degB);
          } else {
              n = degC - degB;
          }
      }
      
      return polSPDE(polA, polB, polC, n, nameVar, simbName, arg, ring);
  }
  
  public Element lnDE(Element A, Element B, Element C, Fname nameVar, ArrayList<Fname> simbName, Element arg, Ring ring) throws Exception {
      int n = 0;
      int b = 0;
      int intVar = ((Polynom)arg).powers.length;
      

      
      Vector<Element> coeffA = new Vector<Element>();
      Vector<Element> degA = new Vector<Element>();
      coefOfHightVar(A, nameVar, coeffA, degA, simbName, (Polynom) arg, ring);
      sumCoeffOfEqualsPows(coeffA, degA, ring);
      sortVector(coeffA, degA, ring);
      
      Vector<Element> coeffB = new Vector<Element>();
      Vector<Element> degB = new Vector<Element>();
      coefOfHightVar(B, nameVar, coeffB, degB, simbName, (Polynom) arg, ring);
      sumCoeffOfEqualsPows(coeffB, degB, ring);
      sortVector(coeffB, degB, ring);
      
      Vector<Element> coeffC = new Vector<Element>();
      Vector<Element> degC = new Vector<Element>();
      coefOfHightVar(C, nameVar, coeffC, degC, simbName, (Polynom) arg, ring);
      sumCoeffOfEqualsPows(coeffC, degC, ring);
      sortVector(coeffC, degC, ring);
      
      if (degA.get(0).intValue() == degB.get(0).intValue() && degA.get(0).isZero(ring)) {
          n = degC.get(0).subtract(degB.get(0), ring).intValue();
      } else if(degA.get(0).intValue() == degB.get(0).intValue() && (degA.get(0).isZero(ring) == false) ) {
          Element alpha = new F(F.DIVIDE, new Element[]{coeffB.get(0), coeffA.get(0)}).expand(ring);
          alpha = new Integrate().integrate(alpha, arg, ring);
          if(alpha instanceof F && ((F)alpha).name == F.INT) {
              return null;
          }
          alpha = new F(F.EXP, alpha.multiply(ring.numberMINUS_ONE, ring));
          alpha = isFuncBelongToField(alpha, nameVar, simbName, arg, ring);
          if(alpha != null) {
              Element H = null;
              
              Element newA = new F(F.MULTIPLY, new Element[]{A, alpha}).expand(ring);
              Element newB = new F(F.MULTIPLY, new Element[]{A, D(alpha, intVar, ring)});
              Element func1 = new F(F.MULTIPLY, new Element[]{B, alpha});
              newB = new F(F.ADD, new Element[]{newB, func1}).expand(ring);
              
              H = lnDE(newA, newB, C, nameVar, simbName, arg, ring);
              
              if (H == null) {
                  return null;
              } else {
                  return new F(F.MULTIPLY, new Element[]{H, alpha}).expand(ring);
              }
          }
          n = degC.get(0).subtract(degB.get(0), ring).intValue();
      } else if(degA.get(0).intValue() < degB.get(0).intValue()) {
          n = degC.get(0).subtract(degB.get(0), ring).intValue();
      } else if(degA.get(0).intValue() > degB.get(0).intValue() + 1) {
          n = Math.max(0, degC.get(0).subtract(degA.get(0), ring).intValue() + 1);
      } else if(degA.get(0).intValue() == degB.get(0).intValue() + 1) {
          Element I = new F(F.DIVIDE, new Element[]{coeffB.get(0), coeffA.get(0)});
          I = new F(F.MULTIPLY, new Element[]{I, ring.numberMINUS_ONE}).expand(ring);
          I = new Integrate().integrate(I, arg, ring);
          if(I instanceof F && ((F)I).name == F.INT) {
              return null;
          }
          I = isFuncBelongToField(I, nameVar, simbName, arg, ring);
          if(I != null  &&  funcIsPolynom(I, nameVar, simbName, arg, ring)) {
              Vector<Element> coeffI = new Vector<Element>();
              Vector<Element> degI = new Vector<Element>();
              coefOfHightVar(I, nameVar, coeffI, degI, simbName, (Polynom) arg, ring);
              sumCoeffOfEqualsPows(coeffI, degI, ring);
              sortVector(coeffI, degI, ring);
              
              // r = coefficient of nameVar in I.
              Element r = coeffI.get(0);
      
              if(r.isItNumber()  &&  new Integrate().isItFracNumber(r, ring) == false) {
                  n = Math.max(r.intValue(), degC.get(0).subtract(degB.get(0), ring).intValue());
              }
          } else {
              n = degC.get(0).subtract(degB.get(0), ring).intValue();
          }
      }
      return SPDE(A, B, C, n, nameVar, simbName, arg, ring);
  }
  
  public Element expDE(Element A, Element B, Element C, Fname nameVar, ArrayList<Fname> simbName, Element arg, Ring ring) throws Exception {
      int n = 0;
      int b = 0;
      int intVar = ((Polynom)arg).powers.length - 1;
      
      Vector<Element> coeffA = new Vector<Element>();
      Vector<Element> degA = new Vector<Element>();
      coefOfHightVar(A, nameVar, coeffA, degA, simbName, (Polynom) arg, ring);
      sumCoeffOfEqualsPows(coeffA, degA, ring);
      sortVector(coeffA, degA, ring);
      
      Vector<Element> coeffB = new Vector<Element>();
      Vector<Element> degB = new Vector<Element>();
      coefOfHightVar(B, nameVar, coeffB, degB, simbName, (Polynom) arg, ring);
      sumCoeffOfEqualsPows(coeffB, degB, ring);
      sortVector(coeffB, degB, ring);
      
      Vector<Element> coeffC = new Vector<Element>();
      Vector<Element> degC = new Vector<Element>();
      coefOfHightVar(C, nameVar, coeffC, degC, simbName, (Polynom) arg, ring);
      sumCoeffOfEqualsPows(coeffC, degC, ring);
      sortVector(coeffC, degC, ring);
      
      // Шаг 1. Нахождение b --- минимальной степени неизвестного многочлена.
      
      int nC = (degC.get(degC.size() - 1).isNegative())? degC.get(degC.size() - 1).intValue(): 0;
      int nB = (degB.get(degB.size() - 1).isNegative())? degB.get(degB.size() - 1).intValue(): 0;
      
      if(nB != 0) {
          b = Math.min(0, nC - Math.min(0, nB));
      } else if(nB == 0) {
          Element A0 = ring.numberZERO;
          for (int i = 0; i < degA.size(); i++) {
              if(degA.get(i).isZero(ring)) {
                  A0 = coeffA.get(i);
                  break;
              }
          }
          Element B0 = ring.numberZERO;
          for (int i = 0; i < degB.size(); i++) {
              if(degB.get(i).isZero(ring)) {
                  B0 = coeffB.get(i);
                  break;
              }
          }
          
          
          Element alpha = new Integrate().integrate(new F(F.DIVIDE, new Element[]{B0, A0}).expand(ring), arg, ring);
          if(alpha instanceof F  &&  ((F)alpha).name == F.INT) {
              return null;
          }
          
          alpha = new F(F.EXP, alpha.multiply(ring.numberMINUS_ONE, ring));
          alpha = isFuncBelongToField(alpha, nameVar, simbName, arg, ring);
          if (alpha != null  &&  funcIsPolynom(alpha, nameVar, simbName, arg, ring)) {
              Vector<Element> coeffAlpha = new Vector<Element>();
              Vector<Element> degAlpha = new Vector<Element>();
              coefOfHightVar(alpha, nameVar, coeffAlpha, degAlpha, simbName, (Polynom) arg, ring);
              sumCoeffOfEqualsPows(coeffAlpha, degAlpha, ring);
              sortVector(coeffAlpha, degAlpha, ring);
              b = Math.min(0, Math.min(degAlpha.get(0).intValue(), nC) );
          } else {
              b = Math.min(0, nC);
          }
          
      }
      
      // Шаг 2. Избавление от отрицательный степеней неизвестной в коэффициентах A, B и C.
      
      int m = Math.max(0, Math.max(-nB, b - nC));
      Element func1 = new F(F.MULTIPLY, new Element[]{ring.posConst[b], D(((F)nameVar.X[0]).X[0], intVar, ring), A}).expand(ring);
      Element func2 = new F(F.ADD, new Element[]{func1, B});
      Element func3 = new F(F.intPOW, new Element[]{nameVar, ring.posConst[m]});
      Element newB = new F(F.MULTIPLY, new Element[]{func2, func3}).expand(ring);
      
      Element newA = new F(F.MULTIPLY, new Element[]{A, func3}).expand(ring);
      
      Element newC = new F(F.intPOW, new Element[]{nameVar, ring.posConst[m - b]});
      newC = new F(F.MULTIPLY, new Element[]{C, newC}).expand(ring);
      
      
      coeffA = new Vector<Element>();
      degA = new Vector<Element>();
      coefOfHightVar(newA, nameVar, coeffA, degA, simbName, (Polynom) arg, ring);
      sumCoeffOfEqualsPows(coeffA, degA, ring);
      sortVector(coeffA, degA, ring);
      
      coeffB = new Vector<Element>();
      degB = new Vector<Element>();
      coefOfHightVar(newB, nameVar, coeffB, degB, simbName, (Polynom) arg, ring);
      sumCoeffOfEqualsPows(coeffB, degB, ring);
      sortVector(coeffB, degB, ring);
      
      coeffC = new Vector<Element>();
      degC = new Vector<Element>();
      coefOfHightVar(newC, nameVar, coeffC, degC, simbName, (Polynom) arg, ring);
      sumCoeffOfEqualsPows(coeffC, degC, ring);
      sortVector(coeffC, degC, ring);
      
      // Шаг 3. Нахождение верхней границы степени неизвестной и решение полиномиального уравнения.
      
      if(degA.get(0).intValue() < degB.get(0).intValue()) {
          m = degC.get(0).subtract(degB.get(0), ring).intValue();
      } else if(degA.get(0).intValue() > degB.get(0).intValue()) {
          m = Math.max(0, degC.get(0).subtract(degA.get(0), ring).intValue());
      } else if(degA.get(0).intValue() == degB.get(0).intValue()) {
          Element alpha = new F(F.DIVIDE, new Element[]{coeffB.get(0), coeffA.get(0)}).expand(ring);
          alpha = new Integrate().integrate(alpha, arg, ring);
          if(alpha instanceof F  &&  ((F)alpha).name == F.INT) {
              return null;
          }
              
          alpha = new F(F.EXP, alpha.multiply(ring.numberMINUS_ONE, ring));
          alpha = isFuncBelongToField(alpha, nameVar, simbName, arg, ring);
          if (alpha != null  &&  funcIsPolynom(alpha, nameVar, simbName, arg, ring)) {
              Vector<Element> coeffAlpha = new Vector<Element>();
              Vector<Element> degAlpha = new Vector<Element>();
              coefOfHightVar(alpha, nameVar, coeffAlpha, degAlpha, simbName, (Polynom) arg, ring);
              sumCoeffOfEqualsPows(coeffAlpha, degAlpha, ring);
              sortVector(coeffAlpha, degAlpha, ring);
              b = Math.min(0, Math.min(degAlpha.get(0).intValue(), nC) );
          } else {
              m = degC.get(0).subtract(degB.get(0), ring).intValue();
          }
      }
      
      Element H = SPDE(newA, newB, newC, m, nameVar, simbName, arg, ring);
      
      if (H == null) {
          return null;
      }
      
      Element result = new F(F.intPOW, new Element[]{nameVar, ring.posConst[b]});
      result = new F(F.MULTIPLY, new Element[]{H, result}).expand(ring);
      return result;
  }
  
  public Element SPDE(Element A, Element B, Element C, int n, Fname nameVar, ArrayList<Fname> simbName, Element arg, Ring ring) throws Exception {
      if (C.isZero(ring)) {
          return ring.numberZERO;
      }
      
      if (n < 0) {
          return null;
      }
      
      if (n == 0) {
          Element el = new F(F.DIVIDE, C, B).expand(ring);
          if (el.isItNumber(ring)) {
              return el;
          }
      }
      
      Element G = A.GCD(B, ring);
      
      if (G.isItNumber(ring) == false) {
          boolean bool = C.GCD(G, ring).isItNumber(ring)  &&  (C.isItNumber(ring) == false);
          bool = bool || ( C.isItNumber(ring) );
      
          if(bool) {
              return null;
          }
      
          A = new F(F.DIVIDE, new Element[]{A, G}).expand(ring);
          B = new F(F.DIVIDE, new Element[]{B, G}).expand(ring);
          C = new F(F.DIVIDE, new Element[]{C, G}).expand(ring);
      }
      
      if(B.isZero(ring)) {
          Element Q = new Integrate().mainProcOfInteg(C, arg, ring);
          Q = isFuncBelongToField(Q, nameVar, simbName, arg, ring); 
          if (Q != null  &&  funcIsPolynom(Q, nameVar, simbName, arg, ring)) {
              Vector<Element> coeffQ = new Vector<Element>();
              Vector<Element> degQ = new Vector<Element>();
              coefOfHightVar(Q, nameVar, coeffQ, degQ, simbName, (Polynom) arg, ring);
              sumCoeffOfEqualsPows(coeffQ, degQ, ring);
              sortVector(coeffQ, degQ, ring);
              if(degQ.get(0).intValue() <= n) {
                  return Q;
              }
          }
          return null;
      }
      
      Vector<Element> coeffA = new Vector<Element>();
      Vector<Element> degA = new Vector<Element>();
      coefOfHightVar(A, nameVar, coeffA, degA, simbName, (Polynom) arg, ring);
      sumCoeffOfEqualsPows(coeffA, degA, ring);
      sortVector(coeffA, degA, ring);
      
      
      Vector<Element> coeffB = new Vector<Element>();
      Vector<Element> degB = new Vector<Element>();
      coefOfHightVar(B, nameVar, coeffB, degB, simbName, (Polynom) arg, ring);
      sumCoeffOfEqualsPows(coeffB, degB, ring);
      sortVector(coeffB, degB, ring);
      
      int intVar = ((Polynom)arg).powers.length - 1;
      
      if(degA.get(0).intValue() > 0) {
          int n_var = new Integrate().indexOfVar(nameVar, ring);
          Polynom polA = (Polynom) ring.CForm.ElementConvertToPolynom(A);
          Polynom polB = (Polynom) ring.CForm.ElementConvertToPolynom(B);
          Polynom polC = (Polynom) ring.CForm.ElementConvertToPolynom(C);
          
          Element[] polAndFracParts = polC.quotientAndRemainder(
                        polA, n_var, ring.CForm.newRing);
          
          Element Z = polAndFracParts[0];
          Element R = null;
          
          if (polAndFracParts[1].isZero(ring)) {
              R = ring.numberZERO;
          } else {
              R = polAndFracParts[1].divide(B, ring);
              if ( (R instanceof Polynom == false) && (R.isItNumber(ring) == false) ) {
                  return null;
              }
          }
          
          Z = ring.CForm.ElementToF(Z);
          R = ring.CForm.ElementToF(R);
          
          if (funcIsPolynom(R, nameVar, simbName, arg, ring) == false ||
              funcIsPolynom(Z, nameVar, simbName, arg, ring) == false) {
              return null;
          }
          Vector<Element> coeffR = new Vector<Element>();
          Vector<Element> degR = new Vector<Element>();
          coefOfHightVar(R, nameVar, coeffR, degR, simbName, (Polynom) arg, ring);
          sumCoeffOfEqualsPows(coeffR, degR, ring);
          sortVector(coeffR, degR, ring);
          
          if (degR.get(0).intValue() > n) {
              return null;
          }
          
          Element func1 = new F(F.ADD, new Element[]{B, D(A, intVar, ring)}).expand(ring);
          Element func2 = new F(F.SUBTRACT, new Element[]{Z, D(R, intVar, ring)}).expand(ring);
          Element H = SPDE(A, func1, func2, n - degA.get(0).intValue(), nameVar, simbName, arg, ring);
          
          if(H == null) {
              return null;
          }
          
          Element result = new F(F.MULTIPLY, new Element[]{A, H});
          result = new F(F.ADD, new Element[]{result, R}).expand(ring);
          
          return result;
      } else if( (degA.get(0).intValue() == 0) && (degB.get(0).intValue() > 0) ) {
          
          Vector<Element> coeffC = new Vector<Element>();
          Vector<Element> degC = new Vector<Element>();
          coefOfHightVar(C, nameVar, coeffC, degC, simbName, (Polynom) arg, ring);
          sumCoeffOfEqualsPows(coeffC, degC, ring);
          sortVector(coeffC, degC, ring);
          
          int m = degC.get(0).intValue() - degB.get(0).intValue();
          if(m < 0  ||  m > n) {
              return null;
          }
          
          Element func = new F(F.intPOW, new Element[]{nameVar, ring.posConst[m]});
          func = new F(F.MULTIPLY, new Element[]{func, coeffC.get(0)});
          func = new F(F.DIVIDE, new Element[]{func, coeffB.get(0)}).expand(ring);
          
          Element func2 = new F(F.MULTIPLY, new Element[]{B, func});
          Element func3 = new F(F.MULTIPLY, new Element[]{A, D(func, intVar, ring)});
          
          Element func4 = new F(F.SUBTRACT, new Element[]{C, func2}).expand(ring);
          func4 = new F(F.SUBTRACT, new Element[]{func4, func3}).expand(ring);
          
          Element H = SPDE(A, B, func4, m - 1, nameVar, simbName, arg, ring);
          
          if(H == null) {
              return null;
          }
          
          Element result = new F(F.ADD, new Element[]{func, H}).expand(ring);
          return result;
          
      } else if( (degA.get(0).intValue() == 0) && (degB.get(0).intValue() == 0) ) {
          if (((F)nameVar.X[0]).name == F.LN) {
              return pde_k_ln(A, B, C, n, nameVar, simbName, arg, ring);
          } else if (((F)nameVar.X[0]).name == F.EXP) {
              return pde_k_exp(A, B, C, n, nameVar, simbName, arg, ring);
          }
      }
      
      return null;
  }
  
  public Element polSPDE(Polynom A, Polynom B, Polynom C, int n, Fname nameVar, ArrayList<Fname> simbName, Element arg, Ring ring) throws Exception {
      if (C.isZero(ring)) {
          return ring.numberZERO;
      }
      
      if (n < 0) {
          return null;
      }

      if (n == 0) {
          return ring.CForm.ElementToF(C.divide(B, ring));
      }
      
      Polynom G = A.gcd(B, ring);
      
      if (G.isItNumber(ring) == false) {
          if (G.gcd(C, ring).isItNumber(ring)) {
              return null;
          }
          
          A = (Polynom) A.divide(G, ring);
          B = (Polynom) B.divide(G, ring);
          C = (Polynom) C.divide(G, ring);
      }
      
      
      int intVar = ((Polynom)arg).powers.length - 1;
      int degA = A.degree(intVar);
      int degB = B.degree(intVar);
      int degC = C.degree(intVar);
      
      if (degA < 0) {
          degA = 0;
      }
      if (degB < 0) {
          degB = 0;
      }
      if (degC < 0) {
          degC = 0;
      }
      
      if (B.isZero(ring)) {
          Polynom Q = integPol(C,(Polynom)arg, ring);
          int degQ = Q.degree(((Polynom)arg).powers.length - 1);
          if ((degQ >= 0) && (degQ <= n)) {
              return ring.CForm.ElementToF(Q);
          } else {
              return null;
          }
      } else if(degA > 0) {
          Element[] polAndFracParts = C.quotientAndRemainder(
                        A, intVar, ring.CForm.newRing);
          
          Element Z = polAndFracParts[0];
          Element R = null;
          
          if (polAndFracParts[1].isZero(ring)) {
              R = ring.numberZERO;
          } else {
              R = polAndFracParts[1].divide(B, ring);
              if ( (R instanceof Polynom == false) && (R.isItNumber(ring) == false) ) {
                  return null;
              }
          }
          
          Polynom polR = null;
          Polynom polZ = null;
          
          if (R instanceof Polynom) {
              polR = (Polynom) R;
          } else if (R.isItNumber()) {
              polR = new Polynom(new int[]{}, new Element[]{R});
          } else {
              return null;
          }
          
          if (Z instanceof Polynom) {
              polZ = (Polynom) Z;
          } else if (Z.isItNumber()) {
              polZ = new Polynom(new int[]{}, new Element[]{Z});
          } else {
              return null;
          }
          
          if (polR.degree(intVar) > n) {
              return null;
          }
          
          Element H = polSPDE(A, B.add(A.D(intVar, ring), ring), polZ.subtract(polR.D(intVar, ring), ring), n - degA, nameVar, simbName, arg, ring);
          
          if (H == null) {
              return null;
          }
          Element res = A.multiply(H, ring).add(polR, ring);
          return ring.CForm.ElementToF(res);
      } else if (degA == 0  &&  degB >= 0) {
          int m = degC - degB;
          if (m < 0  ||  m > n) {
              return null;
          }
          
          Element func = arg.pow(m, ring).multiply(C.coeffs[0].divide(B.coeffs[0], ring), ring);
          Element newC = C.subtract(func.multiply(B, ring), ring);
          newC = newC.subtract(func.D(intVar, ring).multiply(A, ring), ring);
          if (newC.isItNumber()) {
              newC = new Polynom(new int[]{}, new Element[]{newC});
          }
          Element H = polSPDE(A, B, (Polynom) newC, m - 1, nameVar, simbName, arg, ring);
          
          if (H == null) {
              return null;
          }
          
          return ring.CForm.ElementToF(func.add(H, ring));
      }
      return null;
  }
  
  public Element pde_k_ln(Element a, Element b, Element C, int n, Fname nameVar, ArrayList<Fname> simbName, Element arg, Ring ring) throws Exception {
      if(C.isZero(ring)) {
          return ring.numberZERO;
      }
      
      if(n < 0) {
          return null;
      }
      
      Element alpha = new F(F.DIVIDE, new Element[]{b, a}).expand(ring);
      alpha = new Integrate().integrate(alpha, arg, ring);
      
      // if alpha in ring
      if( (alpha instanceof F)&&(((F)alpha).name == F.INT) ) {
          return null;
      }
      
      alpha = new F(F.EXP, alpha.multiply(ring.numberMINUS_ONE, ring));

      Integrate integ = new Integrate();
      alpha = isFuncBelongToField(alpha, nameVar, simbName, arg, ring);
      if (alpha != null) {
          Element func1 = new F(F.MULTIPLY, new Element[]{alpha, a});
          Element func2 = new F(F.DIVIDE, new Element[]{C, func1});
          Element Q = integ.integrate(func2.expand(ring), arg, ring);
          
          if( (Q instanceof F)&&(((F)Q).name == F.INT) ) {
              return null;
          }
          Q = new F(F.MULTIPLY, new Element[]{Q, alpha}).expand(ring);
          Q = isFuncBelongToField(Q, nameVar, simbName, arg, ring);
          if (Q != null  &&  funcIsPolynom(Q, nameVar, simbName, arg, ring)) {
              Vector<Element> coeffQ = new Vector<Element>();
              Vector<Element> degQ = new Vector<Element>();
              coefOfHightVar(Q, nameVar, coeffQ, degQ, simbName, (Polynom) arg, ring);
              sumCoeffOfEqualsPows(coeffQ, degQ, ring);
              sortVector(coeffQ, degQ, ring);
                          
              if (integ.isItFracNumber(degQ.get(0), ring) == false  &&  degQ.get(0).intValue() <= n) {
                  return Q;
              }
              return null;
          }
      }
      

      
      Vector<Element> coeffC = new Vector<Element>();
      Vector<Element> degC = new Vector<Element>();
      coefOfHightVar(C, nameVar, coeffC, degC, simbName, (Polynom) arg, ring);
      sumCoeffOfEqualsPows(coeffC, degC, ring);
      sortVector(coeffC, degC, ring);

      int m = degC.get(0).intValue();
      
      if(m > n) {
          return null;
      }
      
      // Solve equation   r' + (b/a)r = c/a
      Element func1 = new F(F.DIVIDE, new Element[]{coeffC.get(0), a}).expand(ring);
      Element func2 = new F(F.DIVIDE, new Element[]{b, a}).expand(ring);
      Fname name1 = getLastRegularMonomial(func1, simbName, arg,  ring);
      Fname name2 = getLastRegularMonomial(func2, simbName, arg,  ring);
      int varNumb=((Polynom)arg).powers.length-1;
      Fname name = name1;
    
      if(simbName.indexOf(name1) < simbName.indexOf(name2)){
          name = name2;
      }
      Element[] els = notDenom(func1, func2, name, varNumb, simbName, (Polynom)arg,  ring);
      Element r = helpSolveDE(els, simbName, arg,  ring).expand(ring);
      
      if(r == null){
          return null;
      } else if(els[3].isOne(ring) == false) {
          r = new F(F.DIVIDE, new Element[]{r, els[3]});
      }
      
      func1 = new F(F.intPOW, nameVar, ring.posConst[m]);
      func1 = new F(F.MULTIPLY, new Element[]{func1, r}).expand(ring);
      
      func2 = new F(F.MULTIPLY, new Element[]{func1, b});
      Element func3 = new F(F.MULTIPLY, new Element[]{D(func1, ((Polynom)arg).powers.length - 1, ring), a});
      Element func4 = new F(F.SUBTRACT, new Element[]{C, func2});
      func4 = new F(F.SUBTRACT, new Element[]{func4, func3}).expand(ring);
              
      Element H = pde_k_ln(a, b, func4, m - 1, nameVar, simbName, arg, ring);
      
      if (H == null) {
          return null;
      }
      
      Element result = new F(F.ADD, new Element[]{func1, H}).expand(ring);
      return result;
  }
  
  
  public Element pde_k_exp(Element a, Element b, Element C, int n, Fname nameVar, ArrayList<Fname> simbName, Element arg, Ring ring) throws Exception {
      if(C.isZero(ring)) {
          return ring.numberZERO;
      }
      
      if(n < 0) {
          return null;
      }
      
      Element alpha = new F(F.DIVIDE, new Element[]{b, a}).expand(ring);
      alpha = new Integrate().integrate(alpha, arg, ring);
      
      // if alpha in ring
      if( (alpha instanceof F)&&(((F)alpha).name == F.INT) ) {
          return null;
      }
      
      alpha = new F(F.EXP, alpha.multiply(ring.numberMINUS_ONE, ring));
      
      // if(alpha == beta theta^m)
      
      Integrate integ = new Integrate();
      alpha = isFuncBelongToField(alpha, nameVar, simbName, arg, ring);
      if (alpha != null  &&  funcIsPolynom(alpha, nameVar, simbName, arg, ring)) {
          Vector<Element> coeffAlpha = new Vector<Element>();
          Vector<Element> degAlpha = new Vector<Element>();
          coefOfHightVar(alpha, nameVar, coeffAlpha, degAlpha, simbName, (Polynom) arg, ring);
          sumCoeffOfEqualsPows(coeffAlpha, degAlpha, ring);
          sortVector(coeffAlpha, degAlpha, ring);
              
          if (integ.isItFracNumber(degAlpha.get(0), ring) == false  &&  degAlpha.get(0).intValue() >= 0) {
              Element func1 = new F(F.MULTIPLY, new Element[]{alpha, a});
              Element func2 = new F(F.DIVIDE, new Element[]{C, func1}).expand(ring);
              Element Q = integ.integrate(func2, arg, ring);
              if( (Q instanceof F)&&(((F)Q).name == F.INT) ) {
                  return null;
              }
              Q = Q.multiply(alpha, ring);
              Q = isFuncBelongToField(Q, nameVar, simbName, arg, ring);
              if (Q != null  &&  funcIsPolynom(Q, nameVar, simbName, arg, ring)) {
                  Vector<Element> coeffQ = new Vector<Element>();
                  Vector<Element> degQ = new Vector<Element>();
                  coefOfHightVar(Q, nameVar, coeffQ, degQ, simbName, (Polynom) arg, ring);
                  sumCoeffOfEqualsPows(coeffQ, degQ, ring);
                  sortVector(coeffQ, degQ, ring);
                          
                  if (integ.isItFracNumber(degQ.get(0), ring) == false  &&  degQ.get(0).intValue() <= n) {
                      return Q;
                  }
                  return null;
              }
          }
      }
      
      

      
      
      Vector<Element> coeffC = new Vector<Element>();
      Vector<Element> degC = new Vector<Element>();
      coefOfHightVar(C, nameVar, coeffC, degC, simbName, (Polynom) arg, ring);
      sumCoeffOfEqualsPows(coeffC, degC, ring);
      sortVector(coeffC, degC, ring);

      int m = degC.get(0).intValue();
      
      if(m > n) {
          return null;
      }
      
      // Solve equation   r' + (b/a + m*\nu')r = c/a
      Element func1 = new F(F.DIVIDE, new Element[]{coeffC.get(0), a}).expand(ring);
      Element func2 = new F(F.DIVIDE, new Element[]{b, a}).expand(ring);
      Element func3 = new F(F.MULTIPLY, new Element[]{((F)nameVar.X[0]).X[0], ring.posConst[m]});
      Element func4 = new F(F.ADD, new Element[]{func2, func3}).expand(ring);
      Fname name1 = getLastRegularMonomial(func1, simbName, arg,  ring);
      Fname name2 = getLastRegularMonomial(func4, simbName, arg,  ring);
      int varNumb=((Polynom)arg).powers.length-1;
      Fname name = name1;
    
      if(simbName.indexOf(name1) < simbName.indexOf(name2)){
          name = name2;
      }
      Element[] els = notDenom(func1, func4, name, varNumb, simbName, (Polynom) arg,  ring);
      Element r = helpSolveDE(els, simbName, arg,  ring);
      
      if(r == null){
          return null;
      } else if(els[3].isOne(ring) == false) {
          r = new F(F.DIVIDE, new Element[]{r, els[3]});
      }
      
      func1 = new F(F.intPOW, new Element[]{nameVar, ring.posConst[m - 1]});
      func1 = new F(F.MULTIPLY, new Element[]{C, func1});
      func1 = new F(F.MULTIPLY, new Element[]{func1, coeffC.get(0)}).expand(ring);
      Element H = pde_k_ln(a, b, func1, m - 1, nameVar, simbName, arg, ring);
      
      if (H == null) {
          return null;
      }
      
      Element result = new F(F.intPOW, new Element[]{nameVar, ring.posConst[m]});
      result = new F(F.MULTIPLY, new Element[]{result, r});
      result = new F(F.ADD, new Element[]{result, H}).expand(ring);
      return result;
  }
  
  public Element isFuncBelongToField(Element func, Fname nameVar, ArrayList<Fname> simbName, Element arg, Ring ring) throws Exception {
      if (func instanceof F) {
          Integrate integ = new Integrate();
          Vector<F> vLnAndExp = new Vector<F>();
          ArrayList<Fname> newList = (ArrayList<Fname>) simbName.clone();
          for (int i = 0; i < simbName.size() - 1; i++) {
              vLnAndExp.add((F)((Fname)simbName.get(i)).X[0]);
          }
          func = integ.makeListOfLNandExp(func, newList, vLnAndExp, (Polynom)arg, ring);
          newList.add(new Fname("end", func));
          StrucTheorem st = new StrucTheorem();
          st.makeRegularMonomialsSequence(newList, arg, ring);
          func = newList.get(newList.size() - 1).X[0];
          newList.remove(newList.size() - 1);
          
          if (newList.size() > simbName.size()) {
              return null;
          }
          
          Fname funcVar = getLastRegularMonomial(func, simbName, arg, ring);
          if (simbName.lastIndexOf(nameVar) < simbName.lastIndexOf(funcVar)) {
              return null;
          }
      }
      return func;
  }
  
  
  public boolean funcIsPolynom(Element func, Fname nameVar, ArrayList<Fname> simbName, Element arg, Ring ring) {
      if ( (func instanceof F) && (((F)func).name == F.DIVIDE) ) {
          Element denom = ((F)func).X[1];
          Fname funcVar = getLastRegularMonomial(denom, simbName, arg, ring);
          
          if (simbName.lastIndexOf(nameVar) < simbName.lastIndexOf(funcVar)) {
              return false;
          }
          Vector<Element> coeffs = new Vector<Element>();
          Vector<Element> degs = new Vector<Element>();
          coefOfHightVar(denom, funcVar, coeffs, degs, simbName, (Polynom) arg, ring);
          sumCoeffOfEqualsPows(coeffs, degs, ring);
          sortVector(coeffs, degs, ring);
          
          boolean bool1 = degs.size() == 1;
          boolean bool2 = degs.get(0).intValue() == 0;
          boolean bool3 = (degs.get(0).intValue() > 0)  && (((F)funcVar.X[0]).name == F.EXP);
          
          if ( bool1 && (bool2 || bool3) ) {
              return true;
          } else {
              return false;
          }
      }
      return true;
  }

  public Element[] factorNPow(int n, Element newExtand) {
    Element[] factor = new Element[n + 1];
    factor[0] = new Fname("$a0");
    Element[] Dfactor = new Element[n];
   if(n>0){
      Fname ai = new Fname("$a1");
      factor[1] = new F(F.MULTIPLY, new Element[]{ai, newExtand});
      Dfactor[0] = ai;  
    }  
    for(int i = 2; i <= n; i++){
      Fname ai = new Fname("$a" + i);
      Element pow = new F(F.intPOW, new Element[]{newExtand, new NumberZ(i)});
      factor[i] = new F(F.MULTIPLY, new Element[]{ai, pow});
      pow = new F(F.intPOW, new Element[]{newExtand, new NumberZ(i - 1)});
      Dfactor[i - 1] = new F(F.MULTIPLY, new Element[]{new NumberZ(i), ai, pow});
    }
    return new Element[]{new F(F.ADD, factor), new F(F.ADD, Dfactor)};
  }

  public int minDegreeForVar(Polynom pol, int k) {
    int min = pol.powers[k];
    int nV = pol.getVarsNum();
    for(int i = 1; i < pol.coeffs.length; i++){
      k += nV;
      if(pol.powers[k] < min){
        min = pol.powers[k];
      }
    }
    return min;
  }

  public Object[] hightDeg(Element[] el, ArrayList<Fname> listName, Element  arg,  Ring ring) {
    Fname[] name = new Fname[3];
    int[] hDeg = new int[3], sDeg = new int[3];
    for(int i = 0; i < 3; i++){
      Vector<Element> deg = new Vector<Element>();
//      el[i] = el[i].ExpandFnameOrId();
      el[i] = el[i].expand(ring);
      if(containsSQRT(el[i], ((Polynom)arg), ring) ) {
          return new Object[]{-1, 0, null};
      }
      if(el[i] instanceof F){
        if((((F) el[i]).name == F.DIVIDE && ((F) el[i]).X[1].numbElementType() < 8) || ((F) el[i]).name == F.ID){
          el[i] = ((F) el[i]).X[0];
        }
      }
      name[i] = getLastRegularMonomial(el[i], listName, arg,  ring);
      if(name[i].X[0] instanceof F){
        coefOfHightVar(el[i], name[i], new Vector<Element>(), deg, listName, (Polynom) arg, ring);
        sortVector(deg, ring);
        if(deg.get(0).intValue()*deg.get(deg.size() - 1).intValue() < 0) {
            hDeg[i] = deg.get(0).intValue();
            sDeg[i] = deg.get(deg.size() - 1).intValue();
        } else if(deg.get(0).intValue() >= 0) {
            hDeg[i] = deg.get(0).intValue();
            sDeg[i] = 0;
        } else if(deg.get(0).intValue() < 0) {
            hDeg[i] = 0;
            sDeg[i] = deg.get(deg.size() - 1).intValue();
        }
      }else{
        if(el[i] instanceof Polynom){
          Polynom polEl = (Polynom) el[i];
          hDeg[i] = polEl.degree(((Polynom)arg).powers.length-1);
          sDeg[i] = minDegreeForVar(polEl, ((Polynom)arg).powers.length-1);
        }else{
          hDeg[i] = 0;
          sDeg[i] = 0;
        }
      }
    }
    Element nNewEx = new Fname(ring.varNames[((Polynom)arg).powers.length-1], ((Polynom)arg).clone());
    int max = hDeg[0], min = sDeg[0], nmax = 0, nmin = 0;
    int i1 = listName.indexOf(name[1]), i2 = listName.indexOf(name[2]);
    if(i1 > i2){
      nmax = hDeg[1];
      nmin = sDeg[1];
      nNewEx = name[1];
    }
    if(i2 > i1){
      nmax = hDeg[2];
      nmin = sDeg[2];
      nNewEx = name[2];
    }
    if(i1 == i2){
      if(i1 >= 0){
        if(hDeg[1] >= hDeg[2]){
          nmax = hDeg[1];
        }else{
          nmax = hDeg[2];
        }
        if(sDeg[1] <= sDeg[2]){
          nmin = sDeg[1];
        }else{
          nmin = sDeg[2];
        }
      }else{
        if(hDeg[1] - 1 >= hDeg[2]){
          nmax = hDeg[1] - 1;
        }else{
          nmax = hDeg[2];
        }
      }
      nNewEx = name[1];
    }
    if(listName.indexOf(name[0]) > listName.indexOf(nNewEx)){
      return new Object[]{hDeg[0], sDeg[0], name[0]};
    }
    if(listName.indexOf(name[0]) < listName.indexOf(nNewEx)){
      return new Object[]{-nmax, -nmin, nNewEx};
    }
    if(listName.indexOf(name[0]) == listName.indexOf(nNewEx)){
      int n = hDeg[0] - nmax, m = sDeg[0] - nmin;
      return new Object[]{n, m, name[0]};
    }
    return new Object[]{0, 0, new Fname(ring.varNames[((Polynom)arg).powers.length-1], ((Polynom)arg).clone())};
  }

  public boolean containsSQRT(Element f, Polynom arg, Ring ring) {
      if( !(f instanceof F) ) return false;
      if( ((F)f).name == F.SQRT) {
          if( new Integrate().containsVar( ((F)f).X[0].expand(ring), arg))
              return true;
      } else {
          boolean res = false;
          for(int i=0; i<((F)f).X.length; i++) {
              res = res || containsSQRT( ((F)f).X[i], arg, ring);
          }
          return res;
      }
      return false;
  }
  
  public void sumCoeffOfEqualsPows(Vector<Element> coeff, Vector<Element> deg, Ring ring) {
    for(int i = 0; i < deg.size(); i++){
      Element deg1 = deg.elementAt(i).toNewRing(ring);
      for(int j = i + 1; j < deg.size(); j++){
        Element deg2 = deg.elementAt(j).toNewRing(ring);
        if(deg1.compareTo(deg2, ring) == 0){
          deg.remove(i);
          Element coeffElAt = new F(F.ADD, new Element[]{coeff.elementAt(i),
                    coeff.elementAt(j)}).expand(ring);
          coeff.remove(i);
          coeff.remove(i);
          coeff.insertElementAt(coeffElAt, i);
          j--;
        }else{
          break;
        }
      }
    }
  }

  public void sortVector(Vector<Element> deg, Ring ring) {
    for(int i = 0; i < deg.size(); i++){
      Element deg1 = deg.elementAt(i).toNewRing(ring);
      for(int j = i + 1; j < deg.size(); j++){
        Element deg2 = deg.elementAt(j).toNewRing(ring);
        if(deg1.compareTo(deg2, ring) == -1){
          deg.remove(i);
          deg.insertElementAt(deg2, i);
          deg.remove(j);
          deg.insertElementAt(deg1, j);
          i--;
          break;
        }
      }
    }
  }

  public void sortVector(Vector<Element> coeff, Vector<Element> deg, Ring ring) {
    for(int i = 0; i < deg.size(); i++){
      Element deg1 = deg.elementAt(i).toNewRing(ring);
      Element coeff1 = coeff.elementAt(i);
      for(int j = i + 1; j < deg.size(); j++){
        Element deg2 = deg.elementAt(j).toNewRing(ring);
        Element coeff2 = coeff.elementAt(j);
        if(deg1.compareTo(deg2, ring) == -1){
          deg.remove(i);
          deg.insertElementAt(deg2, i);
          deg.remove(j);
          deg.insertElementAt(deg1, j);
          coeff.remove(i);
          coeff.insertElementAt(coeff2, i);
          coeff.remove(j);
          coeff.insertElementAt(coeff1, j);
          i--;
          break;
        }
      }
    }
  }

  public void addZeroInNotDeg(Vector<Element> coeff, Vector<Element> deg, Ring ring) {
    sortVector(coeff, deg, ring);//{9,7,6,3,1,0}
    sumCoeffOfEqualsPows(coeff, deg, ring);
    int k = 0;
    for(int i = 0; i < deg.get(0).intValue(); i++){
      Element deg1 = deg.elementAt(i);
      Element deg2 = deg.elementAt(i + 1);
      if(deg1.compareTo(deg2.add(NumberZ.ONE, ring), ring) != 0){
        deg.insertElementAt(deg1.subtract(NumberZ.ONE, ring), i + 1);
        coeff.insertElementAt(NumberZ.ZERO, i + 1);
      }
    }
  }
}
