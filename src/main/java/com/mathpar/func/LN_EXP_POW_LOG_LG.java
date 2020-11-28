/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import java.util.*;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

public class LN_EXP_POW_LOG_LG {
    public LN_EXP_POW_LOG_LG() {
    }

    /**
     * возвращает основание и аргумент логарифма
     *
     * @param f
     * @param ring
     *
     * @return
     */
    public static Element[] takeLogArgs(F f, Ring ring) {// выделяем фрагменты b и c  log{b}(c)
        if (f.name == F.LOG) {
            return new Element[] {f.X[0], f.X[1]};
        }
        if (f.name == F.LG) {
            return new Element[] {NumberZ.TEN, f.X[0]};
        }
        if (f.name == F.LN) {
            return new Element[] {new Fname("\\e"), f.X[0]};
        }
        return null;
    }

    /**
     * выделяем основание и степень
     *
     * @param f
     * @param ring
     *
     * @return а - основание, f - степень
     */
    public static Element[] takePowArgs(F f, Ring ring) {// выделяем фрагменты a,b и c  a^(log{b}(c))
        Element a = ring.numberZERO;
        if (f.name == F.POW || f.name == F.intPOW) {
            if (f.X[1] instanceof F) {
                a = (F) f.X[0];
                f = (F) f.X[1];
            } else {
                return null;
            }
        }
        if (f.name == F.EXP) {
            if (f.X[0] instanceof F) {
                a = new Fname("\\e");
                f = (F) f.X[0];
            } else {
                return null;
            }
        }
        return new Element[] {a, f};
    }

    /**
     * Основное логарифмическое тождество. Преобразовывает степень a^(log{a}(b))
     * -> b.
     *
     * @param f a^(log{a}(b))
     * @param ring
     *
     * @return b
     */
    public static Element mainLogIdentity(F f, Ring ring) {// выделяем фрагменты a,b и c  a^(log{b}(c))
        Element a, b, c;
        //   ----- part 1 ---------
        Element[] e = takePowArgs(f, ring);
        //   ----- part 2 ---------
        Element[] el = takeLogArgs((F) e[1], ring);
        a = e[0];
        b = el[0];
        c = el[1];
        //   ----- part 3 ---------
        if (a.equals(b, ring)) {
            return c;
        } else {
            return null;
        }
    }

    /**
     * процедура обхода дерева вглубь
     *
     * @param f - input function
     * @param ring - ring
     *
     * @return
     */
    public Element onTree(F f, Ring ring) {
        Element tempRes;
        if (f.X.length == 1) {
            tempRes=expandLog(f, ring);
            return tempRes==null ? f : tempRes;
        }
        Element[] arg = new Element[f.X.length];
        for (int i = 0; i < f.X.length; i++) {
            tempRes = (f.X[i] instanceof F) ? expandLog((F) f.X[i], ring) : f.X[i];
            arg[i]= (tempRes==null) ? f.X[i] : tempRes;
        }
        Element p = arg[0];
        for (int i = 1; i < arg.length; i++) {
            p = p.add(arg[i], ring);
        }
        return p;
    }
/**
 * найти степень в которую возводится osn для получения arg
 * @param osn NumberZ
 * @param arg NumberZ
 * @return 
 */
    private NumberZ pow(NumberZ osn, NumberZ arg) {
        int pow = 0; // частное от деления
        do {arg = arg.divide(osn); pow++;
        } while (arg.compareTo(NumberZ.ONE)>0);
        return new NumberZ(pow);
    }
    
     /**
     * Процедура на входе принимает аргумент логарифма, раскладывает аргумент
     * на множители и возвращает сумму логарифмов от найденных множителей.
     * 
     * В данной процедуре реализованы преобразования:
     * log(a*f(x)*g(x)) ---> log(a) + log(f(x)) + log(g(x))
     * log(g(x)^a)      ---> a*log(g(x))
     * log(f(x)/g(x))   ---> log(f(x)) - log(g(x)).
     * 
     * 
     * 
     * 
     * pol --- аргумент рассматриваемого логарифма, преобразованный
     * в полином (или рациональную дробь).
     * osn --- основание логарифма.
     * name --- значение F.name логарифма. То есть вид логарифма (F.LN или F.LG или F.LOG)
     * flagAbs --- должен быть true, если pol стоял в аргументе логарифма
     * под модулем (т. е. если рассматриваемый логарифм равен ln(abs(pol))   ).
     *             flagAbs должен быть false, если pol стоял в аргументе логарифма
     * без модуля (т. е. если рассматриваемый логарифм равен ln(pol)   ).
     * 
     * Процедура возвращает сумму логарифмов.
     * 
     * Например, пусть pol = 5x(x + 1)z, где z=sqrt[x/(x - 1)]
     * Пусть flagAbs = true,  osn = Element.CONSTANT_E,        name = F.LN.
     * Тогда процедура вернет ln(abs(5)) + ln(abs(x)) + ln(abs(x + 1)) +
     *                         + (1/2)*ln(abs(x)) - (1/2)*ln(abs(x - 1))
     * 
    */


    public Element toSumOfLogs(Element pol, Element osn, int name, boolean flagAbs, Ring ring) {
        if(pol instanceof Fraction) {
            Element el1 = toSumOfLogs(((Fraction)pol).num, osn, name, flagAbs, ring);
            Element el2 = toSumOfLogs(((Fraction)pol).denom, osn, name, flagAbs, ring);
            return new F(F.SUBTRACT, el1, el2);
        } else if(pol instanceof Polynom) {
            Element result = ring.numberZERO;
            // Если pol состоит из одного монома, то
            // каждую переменную этого полинома выносим в отдельный логарифм.
            // Коэффициент этого полинома тоже выносим в отдельный логарифм.
            if(((Polynom)pol).coeffs.length == 1) {
                Polynom p = (Polynom) pol;
                for(int i = 0; i < p.powers.length; i++) {
                    if(p.powers[i] == 0) {
                        continue;
                    }
                    Element func = (i < ring.varPolynom.length)? ring.varPolynom[i]: ring.CForm.ElementToF(ring.CForm.newRing.varPolynom[i]);
                    if( (func instanceof F) && 
                        ( ( ((F)func).name == F.POW) ||
                        ( ((F)func).name == F.intPOW) ||
                        ( ((F)func).name == F.SQRT) ||
                        ( ((F)func).name == F.CUBRT)  )  ) {
                            
                        Element coeff = null;
                        if( ((F)func).name == F.SQRT) {
                            coeff = ring.numberONE.divide(ring.posConst[2], ring);
                        } else if( ((F)func).name == F.CUBRT) {
                            coeff = ring.numberONE.divide(ring.posConst[3], ring);
                        } else {
                            coeff = ((F)func).X[1].multiply(ring.posConst[p.powers[i]], ring);
                        }
                        coeff = coeff.multiply(ring.posConst[p.powers[i]], ring);
                        
                        Element funcArg = ((F)func).X[0];
                        funcArg = ring.CForm.ElementConvertToPolynom(funcArg);
                        // Выносим в отдельный логарифм найденную функцию.
                        Element el = toSumOfLogs(funcArg, osn, name, flagAbs, ring);
                        el = el.multiply(coeff, ring);
                        result = result.add(el, ring);
                    } else {
                        if(flagAbs) {
                            func = new F(F.ABS, func);
                        }
                        Element el = (name == F.LOG)? new F(F.LOG, osn, func): new F(name, func);
                        el = el.multiply(ring.posConst[p.powers[i]], ring);
                        result = result.add(el, ring);
                    }
                }
                if(p.coeffs[0].isOne(ring) == false) {
                    Element number = p.coeffs[0];
                    if(flagAbs) {
                        number = new F(F.ABS, number);
                    }
                    Element el = (name == F.LOG)? new F(F.LOG, osn, number): new F(name, number);
                    result = result.add(el, ring);
                }
            } else {
                // Если pol содержит более одного монома, то
                // факторизуем его, и каждый найденный множитель
                // выносим в отдельный логарифм.
                Polynom p=(Polynom)pol;
                FactorPol fp=p.factorOfPol_inQ(false, ring.CForm.newRing);
                Element mm = null;
                for (int i = 0; i < fp.multin.length; i++) {
                    if(fp.multin[i].coeffs.length > 1) {
                        mm = ring.CForm.ElementToF(fp.multin[i]);
                        if(flagAbs) {
                            mm = new F(F.ABS, mm);
                        }
                        mm = (name == F.LOG)? new F(name, osn, mm): new F(name, mm);    
                        if(fp.powers[i] > 1) {
                            mm = mm.multiply(ring.posConst[fp.powers[i]], ring);
                        }
                        result = result.add(mm, ring);
                    } else {
                        result = result.add(toSumOfLogs(fp.multin[i], osn, name, flagAbs, ring), ring);
                    }
                }
            }
            return result;
        }
        Element func = ring.CForm.ElementToF(pol);
        if(flagAbs) {
            func = new F(F.ABS, func);
        }
        return (name == F.LOG)? new F(F.LOG, osn, func): new F(name, func);
    }

/** Recurcive exand of log for function (+Roma)
 * 
 * @param func
 * @param ring
 * @return 
 */
    public Element NewexpandLog(Element func, Ring ring){
        if (func instanceof F) {
            F f = (F) func;
         Element[] NEWARGS = new Element[f.X.length];
         for (int j = 0; j < NEWARGS.length; j++) NEWARGS[j] = NewexpandLog(f.X[j], ring);    
         Element[] argLog2=null; boolean notTaken=true; int name=0;
         switch (f.name) {
                case F.LOG: argLog2=f.X; notTaken=false; name=F.LOG; argLog2[1]=NEWARGS[1]; argLog2[0]=NEWARGS[0];
                case F.LG: if (notTaken){ argLog2=new Element[2]; 
                       argLog2[0]=NumberZ.TEN; argLog2[1]=NEWARGS[0]; notTaken=false; name = F.LG;}
                case F.LN: if (notTaken){ argLog2=new Element[2]; 
                      argLog2[0]=Element.CONSTANT_E; argLog2[1]=NEWARGS[0]; notTaken=false;name = F.LN;} 
                    Element osn = argLog2[0];//основание  
                    Element argument = argLog2[1];//аргумент;
                                    boolean flagAbs = false;
                if(argument instanceof F) {
                    argument = undressAbs((F) argument, ring);
                    if(argument == null) {
                        argument = argLog2[1];
                    } else {
                        flagAbs = true;
                    }
                }
                Element pol = ring.CForm.ElementConvertToPolynom(argument);
                return toSumOfLogs(pol, osn, name, flagAbs, ring);
//                    if(argument instanceof Polynom){
//                       Polynom p=(Polynom)argument; FactorPol fp=p.factorOfPol_inQ(false, ring);
//                       if(fp.multin.length>1){ Element[] mm= new Element[fp.multin.length];
//                          for (int i = 0; i < fp.multin.length; i++) 
//                              mm[i]=(name==F.LOG)?                                     
//                                new F(name, argLog2[0], fp.multin[i]): new F(name, fp.multin[i]) ;    
//                          for (int i = 0; i < fp.multin.length; i++) 
//                              if(fp.powers[i]>1)
//                                 mm[i]=new F(F.MULTIPLY, new NumberZ(fp.powers[i]),mm[i]);                                      
//                       return new F(F.ADD, mm);
//                      } else return (name==F.LOG)? new F(name, argLog2): new F(name, argLog2[1]);  
//                     }// end of polynom -----------------------------------------------------------    
//                    Element[] argN=null;
//                    if (name==F.LOG){
//                      if((NEWARGS[1] instanceof F)&& (((F)NEWARGS[1]).name==F.MULTIPLY) ) {F f3=(F)NEWARGS[1];
//                       argN=new Element[f3.X.length];   
//                      for (int i = 0; i < argN.length; i++) argN[i]= new F(name, NEWARGS[0], f3.X[i]);
//                      return new F(F.ADD, argN);}}
//                    else if ((NEWARGS[0] instanceof F)&& (((F)NEWARGS[0]).name==F.MULTIPLY) ) {F f3=(F)NEWARGS[0];
//                       argN=new Element[f3.X.length];   
//                      for (int i = 0; i < argN.length; i++) {
//                          if( (f3.X[i] instanceof F) && 
//                            ( (((F)f3.X[i]).name == F.POW) ||(((F)f3.X[i]).name == F.intPOW) ) ) {
//                              argN[i]= new F(F.MULTIPLY, ((F)f3.X[i]).X[1], new F(name, ((F)f3.X[i]).X[0]) );
//                          } else {
//                              argN[i]= new F(name, f3.X[i]);
//                          }
//                      }
//                      return new F(F.ADD, argN);
//                    } else return new F(name, NEWARGS);     
                case F.ID: return NEWARGS[0] ; 
                case F.ADD:
                  Element[] newArg= new Element[NEWARGS.length];
                  Element sum=ring.numberZERO; int j=0;
                    for (int i = 0; i < NEWARGS.length; i++) {       
                        if (NEWARGS[i].numbElementType() <= Ring.Polynom){sum = sum.add(NEWARGS[i], ring);
                        } else {newArg[j++] =  NEWARGS[i];}
                    }  
                  if(!sum.isZero(ring)) {newArg[j++]=sum;}
                  if ((j<NEWARGS.length)||(!sum.isZero(ring))) {if (j==NEWARGS.length) return new F(F.ADD, newArg);
                      Element[] newArg1= new Element[j];
                      System.arraycopy(newArg, 0, newArg1, 0, j); return (j>1)? new F(F.ADD, newArg1):newArg1[0];        
                   }else return new F(F.ADD, newArg);
                  case F.DIVIDE:
                     // Сокращаем дробь.
                     Element el = new F(F.DIVIDE, NEWARGS);
                     el = ring.CForm.ElementConvertToPolynom(el);
                     return ring.CForm.ElementToF(el);
                  case F.intPOW:
                  case F.POW:
                    if((NEWARGS[0] instanceof F)&&(((F)NEWARGS[0]).name==F.MULTIPLY)){
                      Element[] ee1=((F)NEWARGS[0]).X;
                      for (int i = 0; i < ee1.length; i++) {
                         if(ee1[i] instanceof F){
                             F sf=(F)ee1[i];
                             Element pow1= ((sf.name==F.intPOW)||(sf.name==F.POW))? NEWARGS[1].multiply(sf.X[1],ring):NEWARGS[1];
                             ee1[i]=((sf.name==F.intPOW)||(sf.name==F.POW))? new F(sf.name, sf.X[0], pow1):
                                 new F(f.name, sf, pow1);
                         }else ee1[i]=  new F(f.name, ee1[i], NEWARGS[1]);
                     }
                     return new F(F.MULTIPLY, ee1);
                    }else return new F(f.name, NEWARGS);
                     
                case F.ABS:  Element ARG=NEWARGS[0]; Element RES=null;       
                    if(ARG instanceof F){ F f1=(F)ARG;
                       if ((f1.name==F.POW)||(f1.name==F.intPOW))
                           return new F(f1.name, new F(F.ABS,f1.X[0] ),f1.X[1]);
                       if (f1.name==F.MULTIPLY){
                         Element[] nAr=new Element[f1.X.length];
                         for (int i = 0; i < nAr.length; i++) {
                            if ((f1.X[i] instanceof F)&& 
                                    ((((F)f1.X[i]).name==F.POW)||(((F)f1.X[i]).name==F.intPOW))){
                                F f2=(F)f1.X[i];
                                nAr[i]= new F(f2.name, new F(F.ABS,f2.X[0]),f2.X[1]);}
                            else nAr[i]= new F(F.ABS, f1.X[i]);  }
                         return new F(((F)ARG).name, nAr); 
                       }}else return new F(F.ABS, ARG);
               default:
                     return new F(f.name,NEWARGS);
            }
     } 
     if(func instanceof Fraction){
         if(func.isItNumber()) {
             return func;
         }
         Fraction frac = (Fraction)func;
         if( ( frac.num instanceof F) ||
             ( frac.denom instanceof F) ) {
             Element el = new F(F.DIVIDE, frac.num, frac.denom);
             return NewexpandLog(el, ring);
         }
         return frac.cancel(ring);
     }
     return func; // Fname, числа ,полиномы, матрицы и вектора  он оставляет без изменений
    }  
 
     
    public Element expandLog(Element el , Ring ring){
        Element res= NewexpandLog(el, ring); 
      return (res==null)? el: res;
 
    }
//    /**  old version - not recursive
//     * упрощает композицию логарифмических функций по формулам log(a^k*b^n*...)
//     * = k*log(a) + n*log(b) + ... log(a/b) = log(a) - log(b) слева направо
//     *
//     * @param el
//     * @param ring
//     *
//     * @return
//     */
//    public Element expandLog_(Element el, boolean flagAbs,  Ring ring) {
//        Element[] argLog2=null; boolean notTaken=true; int name=0;//LOG=3, LG,LN
//        if (el instanceof F) {
//            F f = (F) el;
//            switch (f.name) {
//                case F.LOG: argLog2=f.X; notTaken=false; name=F.LOG;
//                case F.LG: if (notTaken){ argLog2=new Element[2]; argLog2[0]=NumberZ.TEN; argLog2[1]=f.X[0]; notTaken=false; name = F.LG;}
//                case F.LN: if (notTaken){ argLog2=new Element[2]; argLog2[0]=Element.CONSTANT_E; argLog2[1]=f.X[0]; notTaken=false;name = F.LN;}
//                notTaken=true;    
//                    Element osn = argLog2[0];//основание  
//                    Element argument = argLog2[1];//аргумент;
//                    if(argument instanceof Polynom){
//                       Polynom p=(Polynom)argument; FactorPol fp=p.factorOfPol_inQ(false, ring);
//                       if(fp.multin.length>1){ Element[] mm= new Element[fp.multin.length];
//                          for (int i = 0; i < fp.multin.length; i++) 
//                              mm[i]=(name==F.LOG)? new F(name, argLog2): new F(name, argLog2[1]) ;    
//                          for (int i = 0; i < fp.multin.length; i++) 
//                              if(fp.powers[i]>1)
//                                 mm[i]=new F(F.MULTIPLY, new NumberZ(fp.powers[i]),mm[i]);                                      
//                       return new F(F.MULTIPLY, mm);
//                      } else return (name==F.LOG)? new F(name, argLog2): new F(name, argLog2[1]);  
//                     }// end of polynom -----------------------------------------------------------            
//                   if (osn.isItNumber()) {Element res= osnIsNumber(osn, argument, false, ring);
//                     if (res!=null)return res;}
//                     if (name!=F.LOG)
//                          return (argument.isItNumber())? argIsNumber(osn, argument, ring):
//                                  argIsElement(osn, argument, ring);
//                      Element V1=(argument.isItNumber(ring))?
//                              argIsNumber(Element.CONSTANT_E, (argument instanceof Polynom)? ((Polynom)argument).coeffs[0]:argument, ring):
//                              argIsElement(Element.CONSTANT_E, osn, ring);
//                      Element V2= (osn.isItNumber())? 
//                              argIsNumber(Element.CONSTANT_E, (osn instanceof Polynom)? ((Polynom)osn).coeffs[0]:osn, ring):
//                              argIsElement(Element.CONSTANT_E, osn, ring);
//                     return V1.divide(V2, ring);
//                case F.ADD:
//                    return onTree(f, ring);
//                case F.SUBTRACT:
//                    Element[] arg1 = new Element[f.X.length];
//                    for (int i = 0; i < f.X.length; i++) {
//                        if (f.X[i] instanceof F) {
//                            arg1[i] = expandLog((F) f.X[i], ring);
//                        }
//                    }
//                    Element pol = arg1[0].subtract(arg1[1], ring);
//                    return pol;
//                case F.MULTIPLY:
//                    for (int i = 0; i < f.X.length; i++) {
//                        if (f.X[i] instanceof F) {
//                            switch (((F) f.X[i]).name) {
//                                case F.LN:
//                                case F.LG:
//                                case F.LOG:
//                                    f.X[i] = expandLog((F) f.X[i], ring);
//                                    break;
//                                case F.POW:
//                                case F.intPOW:
//                                    ((F) f.X[i]).X[0] = expandLog((F) ((F) f.X[i]).X[0], ring);
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    }
//                    return(f == null) ? f : f.expand(ring);
//                case F.DIVIDE:
//                    if (f.X[0] instanceof F) {
//                        f.X[0] = expandLog((F) f.X[0], ring);
//                    }
//                    if (f.X[1] instanceof F) {
//                        f.X[1] = expandLog((F) f.X[1], ring);
//                    }
//                    return f;// тут был вызов экспанд еще раз на бесконечн цикл !!!!
//
//                case F.intPOW:
//                    if (f.X[0] instanceof F) {
//                        f.X[0] = expandLog(f.X[0], ring);
//                    }
//                    return f;
//
//                case F.POW:
//                    return expandExp(f, ring);
//                case F.EXP: F eexp=new F(F.POW, new Fname("\\e"),f.X[0]);
//                    return expandExp(eexp, ring);
//
//                default:
//                    return f;
//            }
//        }
//        return null;
//    }
    /** Упрощение функции ЛОГАРИФМ
     * когда точно знаем что АРГУМЕНТ - Number, а ОСНОВАНИЕ - любое число.
    * @param osn 
    * @param arg 
    * @param ring
    * @return 
    */
    private Element argIsNumber(Element osn, Element arg, Ring ring) {
        switch (osn.numbElementType()) {
         //   case Ring.Fname://??
            case Ring.Z:
            case Ring.Z64:
                switch (arg.numbElementType()) {
                    case Ring.Z:
                    case Ring.Z64:
                        NumberZ gcd = ((NumberZ) osn).GCD((NumberZ) arg);
                        if (gcd.equals(NumberZ.ONE))  return new F(F.LOG, osn, arg);
                        else  return logZ((NumberZ) arg, (NumberZ) osn, ring);
                    case Ring.R:
                    case Ring.R64:
                    case Ring.R128:
                        arg = arg.toNewRing(Ring.Q, ring);
                        if (!(arg instanceof Fraction)) {break;}
                        if (((Fraction) arg).num.isOne(ring)) {//если в числителе единица
                            NumberZ gcdd = ((NumberZ) ((Fraction) arg).denom).GCD((NumberZ) osn);
                            if (gcdd.equals(NumberZ.ONE)) {
                                return new F(F.LOG, osn, arg).multiply(ring.numberMINUS_ONE, ring);
                            } else {
                                return logZ((NumberZ) ((Fraction) arg).denom, (NumberZ) osn, ring).multiply(ring.numberMINUS_ONE, ring);
                            }
                        }
                        if (((Fraction) arg).denom.isOne(ring)) {
                            NumberZ gcdd = ((NumberZ) ((Fraction) arg).num).GCD((NumberZ) osn);
                            if (gcdd.equals(NumberZ.ONE)) {
                                return new F(F.LOG, osn, ((Fraction) arg).num);
                            } else {
                                return logZ((NumberZ) ((Fraction) arg).num, (NumberZ) osn, ring);
                            }
                        } else {
                            Element log1 = new F(F.LOG, osn, ((Fraction) arg).num);
                            Element log2 = new F(F.LOG, osn, ((Fraction) arg).denom);
                            return new F(F.SUBTRACT, log1, log2);
                        }
                    case Ring.Q:
                        if (((Fraction) arg).num.isOne(ring)) {//если в числителе единица
                             NumberZ gcdd = ((NumberZ) ((Fraction) arg).denom).GCD((NumberZ) osn);
                            if (gcdd.equals(NumberZ.ONE)) {
                                return new F(F.LOG, osn, ((Fraction) arg).denom).multiply(ring.numberMINUS_ONE, ring);
                            } else {
                              return logZ((NumberZ) ((Fraction) arg).denom, (NumberZ) osn, ring).multiply(ring.numberMINUS_ONE, ring);
                            }
                        }
                        if (((Fraction) arg).denom.isOne(ring)) {
                            NumberZ gcdd = ((NumberZ) ((Fraction) arg).num).GCD((NumberZ) osn);
                            if (gcdd.equals(NumberZ.ONE)) {return new F(F.LOG, osn, ((Fraction) arg).num);
                            } else {return logZ((NumberZ) ((Fraction) arg).num, (NumberZ) osn, ring);}
                        } else {
                            Element log1 = new F(F.LOG, osn, ((Fraction) arg).num);
                            Element log2 = new F(F.LOG, osn, ((Fraction) arg).denom);
                            return new F(F.SUBTRACT, log1, log2);
                        }
                }
            case Ring.R:
            case Ring.R64:
            case Ring.R128:
                switch (arg.numbElementType()) {
                    case Ring.Z:
                    case Ring.Z64:
                        osn = osn.toNewRing(Ring.Q, ring);
                        if (!(osn instanceof Fraction)) {break;}
                        if (((Fraction) osn).num.isOne(ring)) {//если в числителе единица
                            NumberZ gcdd = ((NumberZ) ((Fraction) osn).denom).GCD((NumberZ) arg);
                            if (gcdd.equals(NumberZ.ONE)) {
                                return new F(F.LOG, ((Fraction) osn).denom, arg).multiply(ring.numberMINUS_ONE, ring);
                            } else {
                                return logZ((NumberZ) arg, (NumberZ) ((Fraction) osn).denom, ring).multiply(ring.numberMINUS_ONE, ring);
                            }
                        }
                        if (((Fraction) osn).denom.isOne(ring)) {
                            NumberZ gcdd = ((NumberZ) ((Fraction) osn).num).GCD((NumberZ) arg);
                            if (gcdd.equals(NumberZ.ONE)) {
                                return new F(F.LOG, ((Fraction) osn).num, arg);
                            } else {return logZ((NumberZ) arg, (NumberZ) ((Fraction) osn).num, ring);}
                        } else {
                            Element log1 = new F(F.LOG, arg, ((Fraction) osn).num);
                            Element log2 = new F(F.LOG, arg, ((Fraction) osn).denom);
                            Element mult = new F(F.SUBTRACT, log1, log2);
                            return new F(F.DIVIDE, ring.numberONE, mult);
                        }
                    case Ring.R:
                    case Ring.R64:
                    case Ring.R128:
                        arg = arg.toNewRing(Ring.Q, ring);
                        osn = osn.toNewRing(Ring.Q, ring);
                        if (!(arg instanceof Fraction)) {break;}
                        if (((Fraction) arg).num.isOne(ring) & ((Fraction) osn).num.isOne(ring)) {
                            NumberZ gcdd = (NumberZ) ((Fraction) arg).denom.GCD(((Fraction) osn).denom, ring);
                            if (gcdd.equals(NumberZ.ONE)) {
                                return new F(F.LOG, ((Fraction) osn).denom, ((Fraction) arg).denom);
                            } else {
                                return logZ((NumberZ) ((Fraction) arg).denom, (NumberZ) ((Fraction) osn).denom, ring);
                            }
                        }
                        if (((Fraction) arg).denom.isOne(ring) & ((Fraction) osn).denom.isOne(ring)) {
                            NumberZ gcdd = (NumberZ) ((Fraction) arg).num.GCD(((Fraction) osn).num, ring);
                            if (gcdd.equals(NumberZ.ONE)) {
                                return new F(F.LOG, ((Fraction) osn).num, ((Fraction) arg).num);
                            } else {
                                return logZ((NumberZ) ((Fraction) arg).num, (NumberZ) ((Fraction) osn).num, ring);
                            }
                        }
                        //если в числителе аргумента или основания стоит единица
                        //и рассмотреть случаи, когда стоит единица в знаменателе основания или в знаменателе аргумента
                        if (((Fraction) arg).denom.isOne(ring)) {
                            osn = osn.toNewRing(Ring.Q, ring);
                            if (((Fraction) osn).denom.isOne(ring)) {
                                NumberZ gcdd = ((NumberZ) ((Fraction) osn).num).GCD((NumberZ) ((Fraction) arg).num);
                                if (gcdd.equals(NumberZ.ONE)) {
                                    return new F(F.LOG, ((Fraction) osn).num, ((Fraction) arg).num);
                                } else {return logZ((NumberZ) ((Fraction) arg).num, (NumberZ) ((Fraction) osn).num, ring);
                                }
                            } else {
                                //if(((Fraction)osn).num.isOne(ring))
                            }
                        }
                 default: break;
                }
          default: break; 
        }
       return new F(F.LOG, osn, arg);
    }
    
/**  "Это продолжение expand log
 *
 * @param osn
 * @param arg
 * @param ring
 * @return
 */
    private Element argIsElement(Element osn, Element arg, Ring ring) {
        if (arg instanceof F) {
            F func = (F) arg;
            if (func.name == F.ID) return argIsElement(osn, func.X[0], ring);
            if (func.name == F.MULTIPLY) {
                Element[] nX=new Element[func.X.length];   
                for (int i = 0; i < func.X.length; i++) {
                //    nX[i]= (flagAbs)?  expandLog(new F(F.LOG, osn, new F(F.ABS,func.X[i])),ring):
                  //   expandLog(new F(F.LOG, osn, func.X[i]),ring);
               nX[i]=  new F(F.LOG, osn, func.X[i]) ;     
                } return new F(F.ADD,nX); 
            }
            if (func.name == F.DIVIDE) {
                F num =  new F(F.LOG, osn, new F(F.ABS, func.X[0]));
                F denom =  new F(F.LOG, osn, new F(F.ABS, func.X[1]));
                F result = new F(F.SUBTRACT,  num,  denom);
                     //   new F(F.SUBTRACT, expandLog(num, ring), expandLog(denom, ring));
                return result;
            }
          //  if (func.name == F.ABS) return argIsElement(osn, func.X[0], true, ring);
        } 
        return null;
    }

    private Element osnIsNumber(Element osn, Element arg, boolean flagAbs, Ring ring) {
        if (arg.isItNumber()) {//если аргумент тоже число сначала преобразуем к одному виду
            if(flagAbs) arg=arg.abs(ring);
            switch (arg.numbElementType()) {
                case Ring.Z:
                case Ring.Z64:
                    arg = arg.toNumber(Ring.Z, ring);
                    NumberZ gcd = ((NumberZ) osn).GCD((NumberZ) arg);
                    if (gcd.equals(NumberZ.ONE)) {
                        return new F(F.LOG, osn, arg);
                    } else if (gcd.equals(osn)) {
                        return pow((NumberZ) osn, (NumberZ) arg);
                    } else {
                        return logZ((NumberZ) arg, (NumberZ) osn, ring);
                    }
                case Ring.R:
                case Ring.R64:
                case Ring.R128:
                    arg = arg.toNumber(Ring.R, ring);
                default:
                    break;
            }
        }
        if (arg instanceof F) {
            F func = (F) arg;
            if (func.name == F.MULTIPLY) {
                CanonicForms g = ring.CForm; //2015
                Element pol = g.ElementToPolynom(func, true);
                F t = log((Polynom) pol, osn, flagAbs, ring);
                F r = (F) new TrigonometricExpand().reConvert(t, g);
                return r;
            }
            if (func.name == F.DIVIDE) { Element NN=(flagAbs)? new F(F.ABS, func.X[0]): func.X[0];
                Element DD=(flagAbs)? new F(F.ABS, func.X[1]): func.X[1];
                F num = new F(F.LOG, osn, NN);
                F denom = new F(F.LOG, osn, DD);
                F result = new F(F.SUBTRACT, expandLog(num, ring), expandLog(denom, ring));
                return result;
            }
        } else if (arg instanceof Polynom) {
            return log((Polynom) arg, osn, flagAbs, ring);
        }
        return null;
    }

    private Element osnisElement(Element osn, Element arg, Ring ring) {
        return null;
    }
    public Element logZ(NumberZ arg, NumberZ osn, Ring ring) {
        FactorPol fZ = (FactorPol) arg.factor(ring);
        Element[] args = new Element[fZ.multin.length];
        for (int i = 0; i < fZ.multin.length; i++) {
            if (osn.equals(fZ.multin[i].coeffs[0])) {
                args[i] = new NumberZ(fZ.powers[i]);
            } else {
                F log = new F(F.LOG, osn, fZ.multin[i]);
                args[i] = log.multiply(new NumberZ(fZ.powers[i]), ring);
            }
        }
        return new F(F.ADD, args);
    }

    /**
     * Процедура логарифмирования одного монома полинома (pol) по основанию osn
     * Результатом будет сумма логарифмов
     *
     * @param p полином (состоящий из одного монома)
     * @param osn основание логарифмов
     * @param ring кольцо
     *
     * @return сумма логарифмов
     */
    static F log(Polynom p, Element osn, boolean flagAbs, Ring ring) {
        ArrayList<F> arr = new ArrayList();
        if (p.coeffs.length != 1) return null;
        if (!p.coeffs[0].isOne(ring)) {
            Element CF=(flagAbs)? p.coeffs[0].abs(ring):p.coeffs[0];
            if (osn.equals(NumberZ.TEN, ring))  
                arr.add(new F(F.LG, new Element[] {CF}));
             else if (osn.equals(new Fname("\\e"), ring))  
                arr.add(new F(F.LN, new Element[] {CF}));
              else   arr.add(new F(F.LOG, new Element[] {osn, CF}));
            
        }
        for (int i = 0; i < p.powers.length; i++) {
            if (p.powers[i] != 0) {
                int[] pow = new int[i + 1];
                pow[i] = 1;
                NumberZ nz = new NumberZ(p.powers[i]); 
                Polynom PP=new Polynom(pow, new Element[] {ring.numberONE});
                F f = null;
                if (osn.equals(NumberZ.TEN, ring)) {
                    f = (flagAbs)? new F(F.LG, new F(F.ABS, new Element[] {PP})): new F(F.LG, new Element[] {PP});
                } else if (osn.equals(new Fname("\\e"), ring)) {
                    f = (flagAbs)? new F(F.LN, new F(F.ABS, new Element[] {PP})): new F(F.LN, new Element[] {PP});
                } else {
                  f = (flagAbs)? new F(F.LOG, new F(F.ABS, new Element[] {osn, PP})): new F(F.LOG, new Element[] {osn, PP});
                }
                arr.add((F) nz.multiply(f, ring));
            }
        }
        Element[] RES=arr.toArray(new Element[arr.size()]);
//        if(flagAbs){for (int i = 0; i < arr.size(); i++) {
//                      F f=(F)RES[i]; RES[i]= new F(f.name, new F(F.ABS,f.X ));} }     
        return ( RES.length == 1 )? (F)RES[0] : new F(F.ADD, RES);
    }

    private int logNumbs(CanonicForms g) {
        int log_names = 0;
        for (int i = 0; i < g.List_of_Change.size(); i++) {
            if (g.List_of_Change.get(i) instanceof F) {
                if ((((F) g.List_of_Change.get(i)).name == F.LOG)
                        || (((F) g.List_of_Change.get(i)).name == F.LG)
                        || (((F) g.List_of_Change.get(i)).name == F.LN)) {
                    log_names += 1;
                }
            }
        }
        return log_names;
    }
    
    /**
     * Удаление взаимно обратных функций:
     * 
     * 
     * sin(arcsin(f(x))) ---> f(x)
     * cos(arcccos(f(x))) ---> f(x)
     * tg(arctg(f(x))) ---> f(x)
     * и так далее.
     * 
     * @param func
     * @param ring
     * @return 
     */
    public static Element deleteInverseFuncs(Element func, Ring ring) {
        if(func instanceof F) {
            F f = (F) func;
            int[] names1 = {F.SIN, F.COS, F.TG, F.CTG, F.SH, F.CH, F.TH, F.CTH, F.ARCSIN, F.ARCCOS, F.ARCTG, F.ARCCTG, F.ARCSH, F.ARCCH, F.ARCTGH, F.ARCCTGH};
            int[] names2 = {F.ARCSIN, F.ARCCOS, F.ARCTG, F.ARCCTG, F.ARCSH, F.ARCCH, F.ARCTGH, F.ARCCTGH, F.SIN, F.COS, F.TG, F.CTG, F.SH, F.CH, F.TH, F.CTH};
        
            for (int i = 0; i < names1.length; i++) {
                if( (f.name == names1[i]) && (f.X[0] instanceof F) && (((F)f.X[0]).name == names2[i]) ) {
                    F arg = (F) f.X[0];
                    return deleteInverseFuncs(arg.X[0], ring);
                }
            }
        
            Element[] ARGS = new Element[f.X.length];
            for (int i = 0; i < f.X.length; i++) {
                ARGS[i] = deleteInverseFuncs(f.X[i], ring);
            }
        
            return new F(f.name, ARGS);
        }
        return func;
    }

    /** The head of function factorLog (factorLogBody) is a special convertor. 
     *  Doing new CononicForm for the hole process of factorization.
     * @param func - clear initial function for Factorization of type Log-Exp
     * @param enteringFactorInLog = true, you want to do factorization in the argument of Log and Exp. 
     * @param ring Ring
     * @return result of factorization
     */
    public FactorPol factorLogExpHead(FactorPol factPl , int enteringFactorInLogExp, Ring ring){
                ArrayList<Integer> numbers = new ArrayList<Integer>();
        for (int i = 0; i < factPl.multin.length; i++) {
            if( (factPl.multin[i].isItNumber()) &&
                (factPl.multin[i].Re(ring).isZero(ring) == false) &&
                (factPl.multin[i].Im(ring).isZero(ring) == false) ) {
                numbers.add(i);
            }
        }
        
        
        for (int k = 0; k < factPl.multin.length; k++) {
           Polynom poll=factPl.multin[k]; if (poll.coeffs.length==0) return FactorPol.ZERO;
           int epVar=poll.powers.length / poll.coeffs.length;
           if (epVar<ring.varNames.length) continue;
           if(poll.isItNumber())continue;
           
           

//           if((ring.algebra[0]&(~Ring.Complex))==Ring.Q){
//               for (int i = 0; i < poll.coeffs.length; i++) {
//                   if(poll.coeffs[i] instanceof Fraction)
//               }
//           }
            boolean bool = false;
            Element poll2 = null;
           
            do {
               poll =factorL(poll, enteringFactorInLogExp, ring);         
          
 
               poll=  factorExpFunction(poll, enteringFactorInLogExp, ring);           
      
           
           
            if(poll2 != null) {
                if(poll.subtract(poll2.multiply(factPl.multin[numbers.get(0)], ring), ring).isZero(ring)) {
                    factPl.multin[k] = (Polynom) poll2;
                } else {
                    factPl.multin[k]=poll;
                    factPl.multin[numbers.get(0)] = new Polynom(new int[]{}, new Element[]{ring.numberONE});
                    numbers.remove(0);
                }
                bool = false;
            } else if( (numbers.size() > 0) &&
                       (poll.Im(ring).isZero(ring) == false) &&
                       (poll.Re(ring).isZero(ring) == false) ) {
                
                poll2 = poll;
                poll = poll.multiply(factPl.multin[numbers.get(0)], ring);
                bool = true;
            } else {
                factPl.multin[k]=poll;
                bool = false;
            }
            } while(bool);
            poll2 = null;
        }
        return factPl;  
    }   
    
     /**
     * Логарифмическое тождество a^log(a, b) = b.
     * @param f
     * @param ring
     * @return 
     */
    public static Element logIdentity(Element func, Ring ring) {
        if (func instanceof F) {
            F f = (F) func;

            if ((f.name == F.POW) || (f.name == F.intPOW) || (f.name == F.EXP)) {
                Element mnogitel = ring.numberONE;
                Element osn;
                Element pow;
                if ((f.name == F.POW) || (f.name == F.intPOW)) {
                    osn = (f.X[0] instanceof F) ? logIdentity((F) f.X[0], ring) : f.X[0];
                    pow = (f.X[1] instanceof F) ? logIdentity((F) f.X[1], ring) : f.X[1];
                } else {
                    osn = Element.CONSTANT_E;
                    pow = (f.X[0] instanceof F) ? logIdentity((F) f.X[0], ring) : f.X[0];
                }
                Element pol = ring.CForm.ElementConvertToPolynom(pow);
                Element num = null;
                Element denom = null;
                if (pol instanceof Fraction) {
                    num = (((Fraction) pol).num instanceof Polynom) ? ((Fraction) pol).num : new Polynom(new int[] {}, new Element[] {((Fraction) pol).num});
                    denom = ((Fraction) pol).denom;
                } else if (pol instanceof Polynom) {
                    num = pol;
                } else {
                    num = new Polynom(new int[] {}, new Element[] {pol});
                }
                int[] logPos = ring.CForm.getPosInNewRingTheseFuncs(new int[] {F.LOG, F.LN, F.LG});
                int[] mons = ((Polynom) num).monomsWithVars(logPos);
                if (mons.length == 0) {
                    if (f.name == F.EXP) {
                        return new F(f.name, new Element[] {pow});
                    } else {
                        return new F(f.name, new Element[] {osn, pow});
                    }
                } else {
                    // Если в показателе степени есть логарифмы, то делим
                    // показатель степени на отдельные слагаемые.
                    // Рассматриваем каждое слагаемое отдельно.
                    Polynom[] monoms = ring.CForm.dividePolynomToMonoms((Polynom) num);
                    for (int i = 0; i < mons.length; i++) {
                        for (int j = 0; j < (monoms[mons[i]].powers.length - ring.varPolynom.length); j++) {
                            // Ищем логарифм в рассматриваемом слагаемом.
                            if (monoms[mons[i]].powers[j + ring.varPolynom.length] > 0) {
                                Element ff = ring.CForm.List_of_Change.get(j);
                                if (ff instanceof F) {
                                    Element osnLog = null;
                                    Element argLog = null;
                                    if (((F) ff).name == F.LOG) {
                                        osnLog = ((F) ff).X[0];
                                        argLog = ((F) ff).X[1];
                                    } else if (((F) ff).name == F.LG) {
                                        osnLog = ring.posConst[10];
                                        argLog = ((F) ff).X[0];
                                    } else if (((F) ff).name == F.LN) {
                                        osnLog = Element.CONSTANT_E;
                                        argLog = ((F) ff).X[0];
                                    } else {
                                        continue;
                                    }
                                    Element sub = osnLog.subtract(osn, ring);
                                    if (sub instanceof F) {
                                        sub = sub.expand(ring);
                                    }
                                    if (sub.isZero(ring)) {
                                        // Основания степенной функции и логарифма совпали,
                                        // значит выражение можно упростить.
                                        // Пусть выражение имеет вид x^(2*log(x, y)*y + 5*y).
                                        // Это выражение можно упростить так:
                                        // x^(2*log(x, y)*y + 5*y) ---> x^(5*y) * y^(2*y)
                                        
                                        
                                        // Упрощение происходит следующим образом:
                                        // Вычитаем слагаемое 2*log(x, y)*y из
                                        // показателя степени 2*log(x, y)*y + 5*y.
                                        num = num.subtract(monoms[mons[i]], ring);
                                        // Делим слагаемое 2*log(x, y)*y на log(x, y)
                                        Element el = monoms[mons[i]].divide(ring.CForm.newRing.varPolynom[j + ring.varPolynom.length], ring);
                                        monoms[mons[i]] = (el instanceof Polynom) ? (Polynom) el : new Polynom(new int[] {}, new Element[] {el});
                                        // Возводим аргумент логарифма в степень,
                                        // равную слагаемому без логарифма:  y^(2*y)
                                        
                                        // Домножаем переменную mnogitel на выражение y^(2*y).
                                        mnogitel = (monoms[mons[i]].subtract(ring.numberONE, ring).isZero(ring))
                                                ? mnogitel.multiply(argLog, ring)
                                                : mnogitel.multiply(new F(F.POW, new Element[] {argLog, ring.CForm.ElementToF(monoms[mons[i]])}), ring);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (mnogitel.isOne(ring) == false) {
                    if (osn instanceof F) {
                        osn = osn.expand(ring);
                    }
                    Element res;
                    if (f.name == F.EXP) {
                        res = new F(F.MULTIPLY, new Element[] {new F(F.EXP, new Element[] {ring.CForm.ElementToF(num)}), mnogitel}).simplify(ring);
                    } else {
                        res = new F(F.MULTIPLY, new Element[] {new F(f.name, new Element[] {osn, ring.CForm.ElementToF(num)}), mnogitel}).simplify(ring);
                    }
                    if (denom != null) {
                        res = new F(F.POW, new Element[] {res, ring.numberONE.divide(ring.CForm.ElementToF(denom), ring)});
                    }
                    return (res instanceof F) ? logIdentity((F) res, ring) : res;
                }
            } else {
                Element[] ARGS = new Element[f.X.length];
                for (int i = 0; i < f.X.length; i++) {
                    ARGS[i] = (f.X[i] instanceof F) ? logIdentity((F) f.X[i], ring) : f.X[i];
                }
                return new F(f.name, ARGS);
            }
        }
        return func;
    }
    
    /**
     * Тождество log(a, a^b) = b.
     * @param f
     * @param ring
     * @return 
     */

    public static Element powIdentity(Element func, Ring ring) {
        if (func instanceof F) {
            F f = (F) func;
            if ((f.name == F.LOG) || (f.name == F.LG) || (f.name == F.LN)) {
                Element result = ring.numberZERO;
                Element logOsn;
                Element logArg;
                if (f.name == F.LOG) {
                    logOsn = (f.X[0] instanceof F) ? powIdentity((F) f.X[0], ring) : f.X[0];
                    logArg = (f.X[1] instanceof F) ? powIdentity((F) f.X[1], ring) : f.X[1];
                } else if (f.name == F.LN) {
                    logOsn = Element.CONSTANT_E;
                    logArg = (f.X[0] instanceof F) ? powIdentity((F) f.X[0], ring) : f.X[0];
                } else {
                    logOsn = ring.posConst[10];
                    logArg = (f.X[0] instanceof F) ? powIdentity((F) f.X[0], ring) : f.X[0];
                }
                boolean flagAbs = false;
                if (logArg instanceof F) {
                    Element el = undressAbs(logArg, ring);
                    if (el != null) {
                        flagAbs = true;
                        logArg = el;
                    }
                }
                Element pol = ring.CForm.ElementConvertToPolynom(logArg);
                Element num = null;
                Element denom = null;
                if (pol instanceof Fraction) {
                    num = (((Fraction) pol).num instanceof Polynom) ? ((Fraction) pol).num : new Polynom(new int[] {}, new Element[] {((Fraction) pol).num});
                    denom = ((Fraction) pol).denom;
                } else if (pol instanceof Polynom) {
                    num = pol;
                } else {
                    num = new Polynom(new int[] {}, new Element[] {pol});
                }
                int[] powPos = ring.CForm.getPosInNewRingTheseFuncs(new int[] {F.POW, F.intPOW, F.EXP});
                int[] mons = ((Polynom) num).monomsWithVars(powPos);
                // Если в аргументе логарифма стоит сумма или аргумент логарифма
                // не содержит показательных функций, то мы не можем упростить выражение.
                if( (mons.length != 1) || ( ((Polynom)num).coeffs.length != 1) ) {
                    if (flagAbs) {
                        logArg = new F(F.ABS, logArg);
                    }
                    if(f.name == F.LOG) {
                        return new F(f.name, new Element[]{logOsn, logArg});
                    } else {
                        return new F(f.name, new Element[]{logArg});
                    }
                }

                // Просматриваем все множители аргумента логарифма,
                // пока не найдем показательную функцию.
                for (int i = 0; i < (((Polynom) num).powers.length - ring.varPolynom.length); i++) {
                    if (((Polynom) num).powers[i + ring.varPolynom.length] != 0) {
                        Element ff = ring.CForm.List_of_Change.get(i);
                        if (ff instanceof F) {
                            Element powOsn = null;
                            Element powArg = null;
                            if ((((F) ff).name == F.POW) || (((F) ff).name == F.intPOW)) {
                                powOsn = ((F) ff).X[0];
                                powArg = ((F) ff).X[1];
                            } else if (((F) ff).name == F.EXP) {
                                powOsn = Element.CONSTANT_E;
                                powArg = ((F) ff).X[0];
                            } else {
                                continue;
                            }

                            Element sub = logOsn.subtract(powOsn, ring);
                            if (sub instanceof F) {
                                sub = sub.expand(ring);
                            }

                            if (sub.isZero(ring)) {
                               // Основания логарифма и степенной функции совпали,
                                // значит выражение можно упростить.
                                // Пусть выражение имеет вид log(x, z*x^(y)).
                                // Это выражение можно упростить так:
                                // log(x, z*x^(zy)) ---> log(x, z) + zy

                                // Упрощение происходит следующим образом:
                                int pow = ((Polynom) num).powers[i + ring.varPolynom.length];
                                // Добавляем к переменной result показатель степенной функции zy.
                                result = (pow == 1)? result.add(powArg, ring):
                                        result.add(powArg.pow(pow, ring), ring);
                                // Делим аргумент логарифма на показательную функцию.
                                Element el = num.divide(ring.CForm.newRing.varPolynom[i + ring.varPolynom.length].pow(ring.posConst[pow], ring), ring);
                                num = (el instanceof Polynom)? (Polynom) el: new Polynom(new int[]{}, new Element[]{el});
                            }
                        }
                    }
                }

                if (result.isZero(ring)) {
                    return func;
                }
                if (num.subtract(ring.numberONE, ring).isZero(ring) == false) {
                    Element ARG = null;
                    if (denom != null) {
                        ARG = ring.CForm.ElementToF(new Fraction(num, denom));
                    } else {
                        ARG = ring.CForm.ElementToF(num);
                    }
                    if (flagAbs) {
                        ARG = new F(F.ABS, ARG);
                    }

                    Element newLog = null;
                    if (f.name == F.LOG) {
                        newLog = new F(f.name, new Element[] {logOsn, ARG});
                    } else {
                        newLog = new F(f.name, new Element[] {ARG});
                    }
                    result = result.add(newLog, ring);
                }
                return (result instanceof F) ? powIdentity((F) result, ring) : result;
                
            } else {
                Element[] ARGS = new Element[f.X.length];
                for (int i = 0; i < f.X.length; i++) {
                    ARGS[i] = (f.X[i] instanceof F) ? powIdentity((F) f.X[i], ring) : f.X[i];
                }
                return new F(f.name, ARGS);
            }
        }
        return func;
    }
    
    /**
     * Entering Factor multiplier In Logarithm, as a power. ONLY FOR NUMBERS!
     * @param polynom - virtual polynomial-monomial witch has logarithm functions  
     * @param g CanonicForms
     * @return new virtual polynomial
     * Example:  -5\sin(x)\ln(a) --> -\sin(x)\ln(a^5)
     */
    public Polynom inputPow(Polynom polynom,   CanonicForms g) {F newF;
        if ( (polynom.coeffs[0].numbElementType() < Ring.Polynom) && (polynom.coeffs[0].Im(g.newRing).isZero(g.newRing)) ) {// т.е. у него числовой коэффициент
            for (int p = polynom.powers.length - 1; p > g.RING.varNames.length - 1; p--) {
                if (polynom.powers[p] != 0) {
                    if (polynom.powers[p] == 1) {Element simb=g.List_of_Change.get(p - g.RING.varNames.length);
                     if (simb instanceof F ) {
                        F func = (F) simb;
                        if (func.name == F.LN || func.name == F.LG) {
                            newF= new F(func.name, func.X[0].pow(polynom.coeffs[0].abs(g.newRing), g.newRing));
                        } else if (func.name == F.LOG) {
                            newF= new F(func.name, func.X[0], func.X[1].pow(polynom.coeffs[0].abs(g.newRing), g.newRing));
                      } else   continue;
                     Polynom newPol=(Polynom)g.addNewElement(newF, 1);
                     newPol.coeffs[0]= (polynom.coeffs[0].isNegative())? NumberZ.MINUS_ONE: NumberZ.ONE;
                     for (int i = 0; i < polynom.powers.length; i++)  newPol.powers[i]+=polynom.powers[i]; 
                     newPol.powers[p]=0;
                     return newPol;
                     }}                  
                }
            }
        }
        return polynom;
    }
/**Сумма логарифмов заменяется логарифмом от произведения аргументов при одинаковых
 * основаниях. Для этого перебираем все сочетания по 2. после создания 
    И еще логарифм плюс константа --->  суммв 2х логарифмов  --> логарифм от произведения
 * @param firstPol исходный виртуальный полином, в каждом мономе coeffs[logPos[i]]  есть логарифм
 * @param g - CanonicForms
 * @param t  - TrigonometricExpand
 * @return
 */
    public Polynom factorL(Polynom firstPol , int enteringFactorInLog, Ring ring ) {
        CanonicForms g= ring.CForm;
        boolean flag;
        int[] logPos= ring.CForm.getPosInNewRingTheseFuncs(new int[] {F.LOG, F.LN, F.LG});
        int[] mons=((Polynom)firstPol).monomsWithVars(logPos);
              //получили позиции в новом кольце для этих функций
        if(mons.length > 0) {
            Element func = ring.CForm.ElementToF(firstPol);
            func = factorArgsOfLogs(func, ring);
            firstPol = (Polynom) ring.CForm.ElementConvertToPolynom(func);
        }
        do {
            flag = false;
            Polynom add=Polynom.polynomZero;
            logPos= ring.CForm.getPosInNewRingTheseFuncs(new int[] {F.LOG, F.LN, F.LG});
            mons=((Polynom)firstPol).monomsWithVars(logPos);
            int lN=mons.length;
            if (lN<2){
                return firstPol;
            }
            ArrayList<Polynom> Add = new ArrayList<Polynom>();
            int kol = firstPol.powers.length / firstPol.coeffs.length;
            if(enteringFactorInLog==1)
              for (int i = 0; i < lN; i++){int nMon=mons[i];  int[] pow1 = new int[kol];
                  // вносим числовой множитель без знака  в каждый логарифм
                  System.arraycopy(firstPol.powers, nMon * kol, pow1, 0, kol);
                  Polynom first = new Polynom(pow1, new Element[] {firstPol.coeffs[nMon] }).normalNumbVar(ring);
                  add=add.add(first, ring); 
                  if (!((first.coeffs[0].isMinusOne(g.newRing))||(first.coeffs[0].isOne(g.newRing)) ) ) {
                      if( (first.Re(ring).isZero(ring) == false)&&
                          (first.Im(ring).isZero(ring) == false) ) {
                          Add.add(first.Re(ring));
                          Add.add((Polynom) first.Im(ring).multiply(ring.numberI, ring));
                      } else {
                          Add.add(inputPow(first, g));
                      }
                  } else {
                      Add.add(first);
                  }
              }
           loop:
            for (int i = 0; i < Add.size() - 1; i++) {
                if (Add.get(i) == null)continue; 
                for (int j = i + 1; j < Add.size(); j++) {
                    if (Add.get(j) == null)continue;
                    Polynom  sum = Add.get(i).add(Add.get(j), g.newRing);              
                    Polynom poly = sumOf2LogTo1Log(sum, g);
                    if (poly != null) {
                        Add.remove(j);
                        Add.remove(i);
                        Add.add(i, poly);
                        flag = true;
                        continue loop;
                    } 
                }
            }
            firstPol=firstPol.subtract(add, ring);
            for (int i = 0; i < Add.size(); i++){
                if (Add.get(i) != null) firstPol=firstPol.add(Add.get(i), ring);
            }
        } while (flag);
        return firstPol;
    }
    
    
         /**
     * Упрощение выражений вида ln(a + a*exp(f(x))) и ln(a - a*exp(f(x))).
     * 
     * 
     * Например, ln(1 + exp(2x)) = ln((exp(-x) + exp(x))*exp(x)) = ln(2ch(x)) + x
     * 
     * func --- произвольная функция, в которой будем искать
     * выражения вида ln(a + a*exp(f(x))) или ln(a - a*exp(f(x))).
     */


    Element factorArgsOfLogs(Element func, Ring ring) {
        if(func instanceof F) {
            F f = (F) func;
            if(f.name == F.LN) {
                Element arg = f.X[0];
                if( arg instanceof F ) {
                    boolean flagAbs = true;
                    arg = undressAbs(arg, ring);
                    if(arg == null) {
                        flagAbs = false;
                        arg = f.X[0];
                    }
                                    
                    Element argPol = ring.CForm.ElementConvertToPolynom(arg);
                    
                    if( (argPol instanceof Polynom) && (((Polynom)argPol).coeffs.length == 2) ) {
                        Polynom p = (Polynom) argPol;
                        // Ищем номера мономов, содержащих экспоненты.
                        int[] expPos= ring.CForm.getPosInNewRingTheseFuncs(new int[] {F.EXP});
                        int[] expMon=p.monomsWithVars(expPos);
                        if( expMon.length == 1 ) {
                            // Если есть ровно один моном содержащий экспоненту, то
                            // разбиваем полином на мономы, ищем экспоненту
                            // в списке List_of_Change.
                            Polynom[] p2 = ring.CForm.dividePolynomToMonoms(p);
                            for (int j = ring.varPolynom.length; j < p2[expMon[0]].powers.length; j++) {
                                if( p2[expMon[0]].powers[j] != 0 ) {
                                    Element exp = ring.CForm.List_of_Change.get(j - ring.varPolynom.length);
                                    if( (exp instanceof F) && (((F)exp).name == F.EXP) ) {
                                        Element coeffp2 =  p2[expMon[0]].coeffs[0];
                                        // Ищем моном, который является числом и проверяем, равен
                                        // ли он коэффициенту при мономе с экспонентой.
                                        // Т. е. проверяем, имеет ли аргумент логарифма вид:
                                        // a + a*exp(f(x)) или a - a*exp(f(x)).
                                        for (int k = 0; k < p2.length; k++) {
                                            if( coeffp2.Im(ring).isZero(ring) &&
                                                p2[k].isItNumber() &&
                                                p2[k].Im(ring).isZero(ring) &&
                                                p2[k].abs(ring).subtract(coeffp2.abs(ring), ring).isZero(ring) ) {
                                    
                                                // В переменную newExp записываем функцию exp(-f(x)/2)
                                                Element expArg = ((F)exp).X[0].multiply(ring.posConst[p2[expMon[0]].powers[j]], ring).divide(ring.posConst[2].negate(ring), ring);
                                                Element newExp = new F(F.EXP, expArg);
                                    
                                                // Аргумент a + a*exp(f(x)) (или a - a*exp(f(x)) ) рассматриваемого логарифма
                                                // домножаем на exp(-f(x)/2). В результате получим выражение:
                                                // [ a*exp(-f(x)/2) + a*exp(f(x)/2)) ]
                                                // Пытаемся свернуть полученное выражение
                                                // в тригонометрическую или гиперболическую функцию.
                                                Element newArg = ring.CForm.ElementToF(p).multiply(newExp, ring).simplify(ring).expand(ring);
                                                newArg = ring.CForm.ElementConvertToPolynom(newArg).factor(ring);
                                                Element te = new TrigonometricExpand().FactorAll((FactorPol)newArg, ring);
                                                if(te == null) {
                                                    return func;
                                                }
                                                newArg = ring.CForm.ElementToF(te);
                                                Element newLn = (flagAbs)? new F(F.LN, new F(F.ABS, newArg)):
                                                                     new F(F.LN, newArg);
                                                // Мы домножили аргумент логарифма на exp(-f(x)/2).
                                                // Чтобы функция не изменилась, нужно прибавить к логарифму f(x)/2
                                                // Т. е. ln(g(x)) = ln(g(x)*exp(-f(x)/2)) + f(x)/2
                                                return new F(F.ADD, newLn, expArg.multiply(ring.numberMINUS_ONE, ring));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Element[] ARGS = new Element[f.X.length];
                for (int i = 0; i < ARGS.length; i++) {
                    ARGS[i] = (f.X[i] instanceof F)? factorArgsOfLogs(  f.X[i], ring  ): f.X[i];
                }
                return new F(f.name, ARGS);
            }
        }
        return func;
    }

/** Сумма или разность логарифмов с одинаковым основанием заменяется
 * на логарифм произведения или частного.
 *          //  Ln(i-z)/(i+z)=2i arctg(z);  
 *            // Ln(a(bi-zb)) - Ln(bi+zb) = 2i*(arctg(z)+Ln(a));   
 *            // Ln(a(bi+zb)) - Ln(bi-zb) = -2i*(arctg(z)+Ln(a)); 
 *            // Ln(a(bi+zb)) - Ln(-bi+zb) = -2i*(arctg(z)+Ln(a)); 
 *            // a=Im(n)/Im(d) ; Re(n)/Re(d)+a==0; z=(Re(d)/Im(d)) --- arctg--  
 * 
 * @param sum полиномиальый образ, который может содержать сумму или разность логарифмов.
 * @param g CanonicForms (представитель)
 * @param t TrigonometricExpand (представитель)
 * @return  полиномиальый образ, который может содержать логарифм от произведения или частного
 */
    private Polynom sumOf2LogTo1Log(Polynom sum, CanonicForms g ) {
        if(sum.coeffs.length < 2) return null;
         Ring ring=g.RING; //newRing;
        boolean DIV=true, MUL=false, SIGN; // договорились обозначать !!
        if ((sum.coeffs[0].subtract(sum.coeffs[1], ring)).isZero(ring))SIGN=MUL;
        else if((sum.coeffs[0].add(sum.coeffs[1], ring)).isZero(ring)) SIGN=DIV;
             else return null;
        int var = sum.powers.length / sum.coeffs.length;
        FactorPol fpol = sum.factorizationOfTwoMonoms(g.newRing);
        Element osn1 = null;Element arg1 = null;Element osn2 = null;Element arg2 = null;
        Element new_f = null;
        int numRingVars=g.RING.varNames.length;
        int i = fpol.multin.length-1;
        int[] pows=fpol.multin[i].powers;//  Живой двучлен стоит последним
        int check = 0; // число логарифмов, приходящихся  на одно слагаемое. Требуется ровно 1.
        for (int j = 0; j < var; j++) check+=pows[j];
        if (check !=1) {return null;}
        for (int j = var; j < 2*var; j++) check+=pows[j];
        if (check !=2) {return null;}

        int size =g.List_of_Change.size();
        int minSizeVar=Math.min(size, var-numRingVars); Fname E=new Fname("\\e");
        b1:{for (int j = 0; j < minSizeVar; j++) {
                if (pows[j+numRingVars] == 1) {
                    Element Elog=g.List_of_Change.get(j );
                    if(Elog instanceof F){
                        F Log = (F)Elog ;
                        if (Log.name == F.LN) {osn1 = E;arg1 = Log.X[0];}
                        else if (Log.name == F.LG) {osn1 = NumberZ.TEN;arg1 = Log.X[0];}
                        else if (Log.name == F.LOG) {osn1 = Log.X[0];arg1 = Log.X[1];}}
                    break b1;
                }
          }return null;}
         b2:{ for (int j = 0; j < minSizeVar; j++) {
                if (pows[var+j+numRingVars] == 1) {
                    Element Elog=g.List_of_Change.get(j );
                     if(Elog instanceof F){
                        F Log = (F)Elog ;
                        if (Log.name == F.LN) {osn2 = E;arg2 = Log.X[0];}
                        else if (Log.name == F.LG) {osn2 = NumberZ.TEN;arg2 = Log.X[0];}
                        else {if (Log.name == F.LOG){osn2 = Log.X[0];arg2 = Log.X[1]; }}}
                     break b2;
                }
        }return null;  }
        if ((osn1!=osn2)||(osn1==null)) return null;       
        int fname=(NumberZ.TEN.equals(osn1, ring))? F.LG :
                            (E.equals(osn1, ring))? F.LN :F.LOG;           
        Element nArg=divMul(arg1,arg2, SIGN, ring); 
        // упростим Аргумент
        boolean flagABS=false;
        if ((nArg instanceof F)&&(((F)nArg).name==F.ABS)){ flagABS=true; nArg=((F)nArg).X[0];}
        if (nArg instanceof Fraction){
            Element denA=((Fraction) nArg).denom; Element numA=((Fraction) nArg).num; 
           denA=ring.CForm.simplify_init(denA); numA=ring.CForm.simplify_init(numA); 
          if (denA instanceof F) denA=((F)denA).Factor(false, ring);
          if (numA instanceof F) numA=((F)numA).Factor(false, ring);
          nArg= new Fraction(numA,denA);
        }else{ nArg=ring.CForm.simplify_init(nArg); 
              if (nArg instanceof F) nArg=((F)nArg).Factor(false, ring).expand(ring);}           
        if(flagABS)nArg=new F(F.ABS,nArg);  
        // упростили 
        Element newE= (fname==F.LOG)? new F(F.LOG, osn1, nArg): new F(fname,nArg);
        if (fname==F.LN){
           if (nArg instanceof F){ F fa=(F)nArg;  if(fa.name==F.ABS){nArg=fa.X[0];}}
             Fraction fr=Fraction.elementToFraction(nArg, ring);
             // Проверим на      ARCTG
             //  Ln(i+z)/(i-z)= - 2i arctg(z);  
             // Ln(a(bi+zb)/(bi-zb)) = 2i*arctg(z) + Ln(a);   
             // Ln(a(bi-zb)/(bi+zb)) = -2i*arctg(z) + Ln(a); 
             // a=Im(n)/Im(d) ; Re(n)/Re(d)+a==0; z=(Re(d)/Im(d)) --- arctg--    

             
             if(!fr.num.Re(ring).isZero(ring) && !fr.denom.Re(ring).isZero(ring) &&
                     !fr.num.Im(ring).isZero(ring) && !fr.denom.Im(ring).isZero(ring)){
                 // Сначала пытаемся привести аргумент логарифма к виду,
                 // похожему на формулы выше.
                 // Сначала конвертируем числитель и знаменатель в полином.
                 // Затем выделяем
                 
              // Дальнейшие действия будем рассматривать на примере.
              // Пусть в аргументе рассматриваемого нами логарифма находится выражение:
              // (1 - \\i*\\exp(x)) / (\\sin(x) + \\i*\\exp(x)*\\sin(x))
              
             // Разложим на множители числитель и знаменатель аргумента логарифма.
               FactorPol fpNum = (FactorPol) g.ElementConvertToPolynom(fr.num).factor(g.newRing);
               FactorPol fpDenom = (FactorPol) g.ElementConvertToPolynom(fr.denom).factor(g.newRing);
               
               fpNum.normalFormInField(ring);
               fpDenom.normalFormInField(ring);
               
               // Пусть процедура ElementConvertToPolynom обозначила функцию \\exp(x)
               // буквой y, а функцию \\sin(x) буквой z.
               // Тогда в нашем примере мы получаем:
               // fpNum = (-1)*(\\i*y - 1)
               // dpDenom = (z)*(\\i*y + 1)
               
               Element a = ring.numberONE;
               Polynom polNum = new Polynom(ring.numberONE);
               Polynom polDenom = new Polynom(ring.numberONE);
               
               
                 for (int j = 0; j < fpNum.multin.length; j++) {
                     if( !fpNum.multin[j].Re(ring).isZero(ring) &&
                         !fpNum.multin[j].Im(ring).isZero(ring) &&
                         !fpNum.multin[j].isItNumber() ) {
                         polNum = polNum.multiply(fpNum.multin[j], ring);
                     } else if(fpNum.multin[j].isItNumber()) {
                         polNum = polNum.multiply(fpNum.multin[j], ring);
                     }
                 }
                 
                   for (int j = 0; j < fpDenom.multin.length; j++) {
                     if( !fpDenom.multin[j].Re(ring).isZero(ring) &&
                         !fpDenom.multin[j].Im(ring).isZero(ring) &&
                         !fpDenom.multin[j].isItNumber()) {
                         polDenom = polDenom.multiply(fpDenom.multin[j], ring);
                     } else if(fpDenom.multin[j].isItNumber()) {
                         polDenom = polDenom.multiply(fpDenom.multin[j], ring);
                     }
                   }
                   
               // Если мнимые части числителя и знаменателя имеют разные знаки,
               // то умножаем и числитель и знаменатель на число \\i.
               // В этом случае мнимые части числителя и знаменателя будут
               // иметь одинаковые знаки.
               // Например, если бы аргумент нашего логарифма равнялся
               // ( \\ix + 1) / (-\\ix + 1)
               // то предыдущие шаги алгоритма эту дробь бы не изменили
               // и переменные polNum и polDenom равнялись бы:
               // polNum = \\ix + 1
               // polDenom = -\\ix + 1
               // Чтобы привести данную дробь к табличному виду, нужно умножить
               // числитель и знаменатель на число \\i.
                   if(polNum.Im(ring).add(polDenom.Im(ring), ring).isZero(ring)) {
                       polNum = (Polynom) polNum.multiply(ring.numberI, ring);
                       polDenom = (Polynom) polDenom.multiply(ring.numberI, ring);
                   }
                   
                
               
               // Множитель дроби записываем в переменную a
               // В нашем примере a = 1/\\sin(x)
               a = fpNum.toPolynomOrFraction(ring).divide(polNum, ring).divide(
                       fpDenom.toPolynomOrFraction(ring).divide(polDenom, ring), ring);
               a = ring.CForm.ElementToF(a);
               
              
           
           
               // В нашем примере мы умножаем и числитель и знаменатель на число \\i.
               // Теперь числитель и знаменатель имеют табличный вид:
               // polNum =   \\i + y
               // polDenom = \\i - y
               
               Element numRe = ring.CForm.ElementToF(polNum.Re(ring));
               Element numIm = ring.CForm.ElementToF(polNum.Im(ring));
               Element denRe = ring.CForm.ElementToF(polDenom.Re(ring));
               Element denIm = ring.CForm.ElementToF(polDenom.Im(ring));
               
               if(new F(F.SUBTRACT, numIm, denIm).simplify(ring).isZero(ring) &&
                  new F(F.ADD, numRe, denRe).simplify(ring).isZero(ring) ) {
                  Element argArT=new F(F.DIVIDE, numRe,numIm).expand(ring);
                  Element mult2i=new Complex(NumberZ.ZERO,ring.posConst[2].negate(ring));
                  
                  newE= (a.isOne(ring))?  new F(F.MULTIPLY, mult2i, new F(F.ARCTG, argArT)): 
                         new F(F.ADD, new F(F.MULTIPLY, mult2i, new F(F.ARCTG, argArT)), new F(F.LN, a.expand(ring)));
               }
             }
             
             fr=Fraction.elementToFraction(nArg, ring);
             // Если в числителе и знаменателе отсутствуют слагаемые с комплексными
             // коэффициентами, то проверяем, является ли наш логарифм функцией ARCTGH.
             // Ln( (f(x) + g(x))/(f(x) - g(x)) ) = 2*ARCTGH( g(x)/f(x) )
             //
             // Сначала находим f(x) и g(x) из нашей формулы:
             // (f(x) + g(x)) + (f(x) - g(x)) = 2*f(x)   =>  f(x) = ( num + denom )/2
             // (f(x) + g(x)) - (f(x) - g(x)) = 2*g(x)   =>  g(x) = ( num - denom )/2
             if(fr.num.Im(ring).isZero(ring) && fr.denom.Im(ring).isZero(ring)) {
                 Element Fx = fr.num.add(fr.denom, ring).divide(new NumberZ("2"), ring).expand(ring);
                 Element Gx = fr.num.subtract(fr.denom, ring).divide(new NumberZ("2"), ring).expand(ring);
                 if( !Fx.isZero(ring) && !Gx.isZero(ring) ) {
                 if( (Fx.add(Gx, ring).subtract(fr.num, ring).expand(ring).isZero(ring)) &&
                     (Fx.subtract(Gx, ring).subtract(fr.denom, ring).expand(ring).isZero(ring)) ) {
                     // Смотрим, аргумент какой функции (логарифм или ARCTGH) проще. Для
                     // этого преобразовываем в строку аргумент логарифма и полученный аргумент
                     // ARCTGH. Если строка для ARCTGH короче, значит выражение
                     // станет проще. Поэтому меняем логарифм на ARCTGH.
                     if(fr.toString().length() > Gx.divide(Fx, ring).expand(ring).toString().length()) {
                         newE = new F(F.MULTIPLY, new NumberZ("2"), new F(F.ARCTGH, Gx.divide(Fx, ring).expand(ring)));
                     }
                 }
                 }
             }
        }
        
        new_f = g.ElementToPolynom(newE, false);
        fpol.multin[i] = ((Polynom) new_f).multiplyByNumber(sum.coeffs[0], ring);
        if (new_f == null) {return null;}
        return (Polynom) (fpol.toPolynomOrFraction(ring));
    }


/** Возвращает частное или  произведение двух элеменов,
 * с учетом объединения степеней и взятия абсолютной величины:
 * (SQRT, CUBRT, POW, intPOW, ABS)
 * @param arg1 -first multiplier
 * @param arg2 -second multiplier
 * @param DIVIDE true=divide; false=multiply.
 * @param ring - Ring
 * @return
 */
public static Element divMul(Element arg1,
Element arg2, boolean DIVIDE, Ring ring){
      Element[] ND= new Element[2];
      Element nArg=null;
      boolean absFlag=false;
      int high=0;
      Element e1=arg1,e2=arg2;
      if ((arg1 instanceof F)&&(arg2 instanceof F)){
        F f1=(F)arg1; F f2=(F)arg2;  e1=f1; e2=f2;
        if (((f1.name==F.SQRT)&&(f2.name==F.SQRT))||
            ((f1.name==F.CUBRT)&&(f2.name==F.CUBRT)))
        {e1=f1.X[0]; e2=f2.X[0]; high=1;}
        else if (( ((f1.name==F.POW)&&(f2.name==F.POW))
                || ((f1.name==F.intPOW)&&(f2.name==F.intPOW))
              )&&(f1.X[0].equals(f2.X[0], ring)))
        {e1=f1.X[1];e2=f2.X[1];high=2;}
        if ( ((f1.name==F.POW)||(f1.name==F.POW))&&(f1.X[1].isMinusOne(ring))){ 
           if (DIVIDE){ e1=f1.X[1]; high=1; DIVIDE=false;} 
            else{ e2=f1.X[1]; e1=e2; DIVIDE=true;}}
        if ( ((f2.name==F.POW)||(f2.name==F.POW))&&(f2.X[1].isMinusOne(ring))){ 
           if (DIVIDE){ e2=f2.X[1]; DIVIDE=false;} 
            else{ e2=f2.X[1]; DIVIDE=true;}}
        else if ( ((f1.name==F.ROOTOF)&&(f2.name==F.ROOTOF))
                &&(f1.X[1].equals(f2.X[1], ring)))
        {e1=f1.X[0];e2=f2.X[0]; high=4;}
        if((e1 instanceof F)&&(e2 instanceof F)){
           F ef1=(F)e1;F ef2=(F)e2;
           if((ef1.name==F.ABS)&&(ef2.name==F.ABS)){ND[0]= ef1.X[0]; ND[1]= ef2.X[0]; absFlag=true;
             if(DIVIDE==true){  
                if((ef1.X[0] instanceof F)&&(((F) ef1.X[0]).name==F.EXP)) 
                  ef1.X[0]=new  F(F.intPOW, new F(F.EXP, (((F) ef1.X[0]).X[0]).negate(ring)), NumberZ.MINUS_ONE);
                else if((ef2.X[0] instanceof F)&&(((F) ef2.X[0]).name==F.EXP)) 
                  ef2.X[0]=new  F(F.intPOW, new F(F.EXP, (((F) ef2.X[0]).X[0]).negate(ring)), NumberZ.MINUS_ONE);}     
           }
           ND[0]=undressAbs(ef1, ring);ND[1]=undressAbs(ef2, ring);
           if((ND[0]!=null)&&((ND[1]!=null))) absFlag=true;        
           else {ND[0]=f1; ND[1]=f2;}
        }else   {ND[0]=e1; ND[1]=e2;}
      }else     {ND[0]=arg1; ND[1]=arg2;}
      nArg=(DIVIDE)? //((ND[0] instanceof Polynom)||(ND[0] instanceof NumberZ))
              ND[0]. divideToFraction(ND[1], ring).expand(ring)  //: ND[0].divide(ND[1], ring)
          :
          new F(F.MULTIPLY, ND[0],ND[1]);
        if (nArg instanceof Fraction) 
            nArg= new Fraction(((Fraction)nArg).num.Expand(ring),((Fraction)nArg).denom.Expand(ring));
        else  nArg= nArg.Expand(ring); 
      if(absFlag ) nArg= new F(F.ABS,nArg);
        switch (high) {
            case 1: nArg= new F(((F)arg1).name,nArg);                break;
            case 2: nArg= new F(((F)arg1).name,((F)arg1).X[0],nArg); break;
            case 4: nArg= new F(((F)arg1).name,nArg,((F)arg1).X[1]); break;
            default: break;
        }
  return nArg;
}
    /**
     *  Check is it Abs function and undress it
     * @param a -- the function for undressing
     * @param ring Ring
     * @return undressed function or null if it is not Abs or impossible to undress
     */
    public static Element undressAbsOld(F a, Ring ring){
        if (a.name==F.ABS){  
           if (a.X[0] instanceof F) {Element ee=undressAbs(((F)(a.X[0])), ring); if(ee!=null) return ee;}
           return a.X[0]; }                    
        Element[] nX=null;  boolean flagAbs=false;
        if (a.name==F.MULTIPLY) { nX=new Element[a.X.length];
            for (int i = 0; i < a.X.length; i++) {
                if ((a.X[i] instanceof F)&&(((F)a.X[i]).name==F.ABS)) {
                    Element ee=((F)a.X[i]).X[0];
                    flagAbs=true;
                    if (ee instanceof F){
                        ee = undressAbs(((F)ee), ring);
                        nX[i]= (ee==null)? ((F)a.X[i]).X[0]: ee;
                    } else {
                        nX[i] = ee;
                    }
                }
                else if ((a.X[i].isItNumber(ring))&&(!a.X[i].isNegative())) nX[i]=  a.X[i];
                else return null;
            } return (flagAbs)? new F (F.MULTIPLY, nX) : null;
        }
        if (a.name==F.DIVIDE) { nX=new Element[2];
            for (int i = 0; i < 2; i++) {
                if ((a.X[i] instanceof F)&&(((F)a.X[i]).name==F.ABS)){ Element ee=((F)a.X[i]).X[0]; flagAbs=true;
                    if ( (ee instanceof F)){ ee= undressAbs(((F)ee), ring); nX[i]= (ee==null)? ((F)a.X[i]).X[0]:ee;}}
                else if ((a.X[i].isItNumber(ring))&&(!a.X[i].isNegative())) nX[i]=  a.X[i];
                else return null;
            }
            return (flagAbs)? new F (F.DIVIDE, nX) : null;
        } return null;
    }
    
     /**
     *  Check is it Abs function and undress it
     * @param a -- the function for undressing
     * @param ring Ring
     * @return undressed function or null if it is not Abs or impossible to undress
     */
    public static Element undressAbs(Element a, Ring ring){
        if(a instanceof F) {
            if(((F)a).name == F.ABS) {
                return ((F)a).X[0];
            }
            if( (((F)a).name == F.MULTIPLY) || (((F)a).name == F.DIVIDE) ) {
                Element[] nX = new Element[((F)a).X.length];
                for(int i = 0; i < nX.length; i++) {
                    Element arg = ((F)a).X[i];
                    Element ee = null;
                    if(arg instanceof F) {
                        ee = undressAbs(arg, ring);
                    } else if((arg.isItNumber(ring))&&(arg.isNegative() == false)) {
                        ee = arg;
                    }
                    if(ee == null) {
                        return null;
                    }
                    nX[i] = ee;
                }
                return new F(((F)a).name, nX);
            }
            if(((F)a).name == F.intPOW) {
                Element arg = ((F)a).X[0];
                Element ee = null;
                if(arg instanceof F) {
                    ee = undressAbs(arg, ring);
                } else if((arg.isItNumber(ring))&&(arg.isNegative() == false)) {
                    ee = arg;
                }
                if(ee == null) {
                    return null;
                }
                return new F(F.intPOW, ee, ((F)a).X[1]);
            }
        }
        return null;
    }
    
    public static Element Simplify(Element E, Ring ring) {
        Element e=E.expand(ring);
        F fclone, eclone;
        if(!(e instanceof F)) return e; else eclone=(F)((F)e).clone(); 
        Element f = new LN_EXP_POW_LOG_LG().expandLog(e, ring);
        if(f instanceof F) fclone=(F)((F)f).clone(); else return f;
        Element gf=  new TrigonometricExpand().ExpandTr(f, ring);
        Element ge = new TrigonometricExpand().ExpandTr(e, ring);
        Object[] EE= new Element[]{E, eclone, fclone, gf, ge};
        int l_E=E.toString(ring).length(),l_e= eclone.toString(ring).length(), 
                l_f=fclone.toString(ring).length(), 
                l_gf=gf.toString(ring).length(), l_ge=ge.toString(ring).length();
       int[] LL={l_E, l_e, l_f,  l_gf, l_ge};
       int[] pos= Array.sortPosUp(LL); int best=LL[0];
        for (int i = 0; i < 5; i++) {
            if(EE[pos[i]]!=null)return (Element)EE[pos[i]];
        }return null; 
    }
    public static Element fullExpand(Element e, Ring ring) {
        e=e.expand(ring);
        if (e instanceof F) {
            Element f = new LN_EXP_POW_LOG_LG().expandLog(e, ring);
            Element fclone=f;
            if(f instanceof F)   fclone=(F)((F)f).clone(); 
            if (f == null) {return new TrigonometricExpand().ExpandTr(e, ring);}
            e = new TrigonometricExpand().ExpandTr(f, ring);
            if(e==null) return fclone;
        }
        return e;
    }
/** 
 * Full factor at one current level: log, exp, trigonom
 * @param e[0] - is a function, e[1] its virtual polynomial (obtained after factor)
 * @param enteringFactorInLog 
 * @param ring
 * @return 
 */
    public static Element fullFactor(Element[] e, int enteringFactorInLog,  Ring ring) {
        // 0
            FactorPol polORfrac = (FactorPol)e[1];//res[1] ;  // g.ElementToPolynom(e,false);
            FactorPol fLog= (polORfrac!=null)? new LN_EXP_POW_LOG_LG().factorLogExpHead(polORfrac, enteringFactorInLog, ring): null;          
            if (fLog == null) return new TrigonometricExpand().FactorAll(polORfrac, ring );
            Element te = new TrigonometricExpand().FactorAll(fLog, ring ); // pol OR Fract OR factPol
            return (te != null) ? te:   fLog  ; 
    }

    /**
     * метод упрощения композиции, использующий обе стратегии упрощения: и
     * факторизацию и разложение.
     *    ////  в процессе разработки  -- надо предложить в разработку 4 курсу
     * @param e
     * @param ring
     *
     * @return
     */
    public static Element simplify(Element e, Ring ring) {
        int enteringFactorInLog=0; // никогда не вводим множитель под логарифм
        if (!(e instanceof F)) {
            return null;
        }
        F f = (F) e;
        //найдём количество узлов дерева функции
        int[] leaves = f.leavesNodesDepth();
        int lnd = 0;
        for (int i = 0; i < leaves.length; i++) {
            lnd += leaves[i];
        }
        //нашли сумму узлов, листьев и глубины пришедшей функции
        //начинаем с FullExpand
        Element boll = fullExpand(f, ring); boll=(boll==null)?f:boll;
        if (boll.isItNumber()) {return boll;//проверяем на числа
        }
        if (boll instanceof F) {boll=((F)boll).Factor(true,ring);
            //boll = fullFactor(boll, enteringFactorInLog, ring); 
            boll=(boll==null)?f:boll;}
        if (boll.isItNumber()) { return boll;//проверяем на числа
        }        //считаем сумму для получившейся функции
        int[] leaves1 = ((F) boll).leavesNodesDepth();
        int lnd1 = 0;
        for (int i = 0; i < leaves1.length; i++) {
            lnd1 += leaves1[i];
        }
        //сравниваем получившиеся результаты
        Element result;
        if (lnd > lnd1) {
            result = simplify(boll, ring);
        } else if (lnd < lnd1) {
            result = e;
        } else {
            result = boll;
        }
        return result;
    }

    //-------------------показательная функция------------------------------
//    public Element expandExp(Element e, Ring ring) {
//        if (e instanceof F) {
//            F f = (F) e;
//            if (f.X[0].isItNumber()) {
//                if (!(f.X[1] instanceof Polynom)) {
//                    CanonicForms g = ring.CForm; //2015
//                    Element pol = g.ElementToPolynom((F) onTree((F) f.X[1], ring), true);
//                    F t = Step(f.X[0], (Polynom) pol, ring);
//                    F r = (F) new TrigonometricExpand().reConvert(t, g);
//                    return r;
//                }
//                return (f.X[1] instanceof Polynom) ? Step(f.X[0], (Polynom) f.X[1], ring) : f;
//            }
//            if (f.X[1] instanceof F) {
//                if (((F) f.X[1]).name == F.LOG || ((F) f.X[1]).name == F.LG || ((F) f.X[1]).name == F.LN) {
//                    return ((f.X[0]).compareTo(takeLogArgs((F) f.X[1], ring)[0]) == 0) ? takeLogArgs((F) f.X[1], ring)[1]
//                            : f;
//                }
//            }
//        }
//        return e;
//    }

    public F Step(Element osn, Polynom pol, Ring ring) {
        if (pol.coeffs.length == 1) {
            return new F(F.POW, osn, pol);
        }
        //разделяем полином на 2 полинома
//первый полином сосотоит из превого монома
        int kol = pol.powers.length / pol.coeffs.length;
        int[] pow = new int[kol];
        System.arraycopy(pol.powers, 0, pow, 0, pow.length);
        Polynom first = new Polynom(pow, new Element[] {pol.coeffs[0]});
//второй полином - все остальные мономы
        Element[] coeff = new Element[pol.coeffs.length - 1];
        System.arraycopy(pol.coeffs, 1, coeff, 0, pol.coeffs.length - 1);
        int[] power = new int[pol.powers.length - kol];
        System.arraycopy(pol.powers, kol, power, 0, pol.powers.length - kol);
        Polynom second = new Polynom(power, coeff);

        if (second.coeffs.length == 1) {
            F p1 = new F(F.POW, osn, first);
            F p2 = new F(F.POW, osn, second);
            F Result = new F(F.MULTIPLY, p1, p2);
            return Result;
        }

        F pos1 = new F(F.POW, osn, first);
        return new F(F.MULTIPLY, pos1, Step(osn, second, ring));
    }
 
/**
 *  упрощении вида a a^b a^c--> a^{b+c+1} или e^x e^g e^(x+g)
 * @param pol - виртуальный полином
 * @param g  CanonicForms
 * @return  полином с произведенными упрощениями
 *     при этом может измениться newRing в CanonicForms
 */
    // Polynom new_pol --> один моном после конца старых перем --.> int[fpow]
 
    public Element factorExponentialFunction_(Polynom pol, Ring ring ){
     Polynom[] ps= pol.toMonomials(ring);
     Element rezPol=Polynom.polynomZero;
     ArrayList<Element> res = new ArrayList<Element>();
        for (int i = 0; i < ps.length; i++) {
            Element oneADD = factorExponentialFunction1Monom_(ps[i], ring);
            if (oneADD instanceof F) {  res.add(oneADD);
            } else {  rezPol = rezPol.add(oneADD, ring);     }
        }
        if(!rezPol.isZero(ring)) res.add(rezPol);
        return res.isEmpty()? ring.numberZERO : res.size()==1 ? res.get(0) :
                new F(F.ADD, res.toArray(new Element[res.size()])) ;
    }

    public static Polynom factorExpFunction(Polynom pol, int recursiveFactoring, Ring ring ){
     Polynom[] ps= pol.toMonomials(ring);
     Polynom rezPol=Polynom.polynomZero;
        for (Polynom p : ps) {
            Polynom oneADD = (p.isItNumber(ring))? p: factorExpFunctMonom(p, recursiveFactoring, ring);
            rezPol = rezPol.add(oneADD, ring);     
        }
     return rezPol;
    }


 private static void deleteSQRT_and_CUBRT(Polynom inpMonom, Ring ring){CanonicForms g=ring.CForm;
 int  startPos= inpMonom.powers.length/inpMonom.coeffs.length- g.List_of_Change.size();// ring.varPolynom.length;
      startPos=Math.max(ring.varNames.length, startPos)  ;//  
 for(int i=startPos; i<inpMonom.powers.length;i++){
  if(inpMonom.powers[i]!=0){
  Element temp1=g.List_of_Change.get(i-startPos);
   if(temp1 instanceof F){
    if(((F)temp1).name==F.SQRT){
            Element powEl=new F(F.POW, new Element[]{ ((F)temp1).X[0],  new Fraction(ring.numberONE, ring.posConst[2]) });
            g.CleanElement(powEl);
            g.List_of_Change.set(i-startPos, powEl);
    }
    if(((F)temp1).name==F.CUBRT){
        Element powEl=new F(F.POW, new Element[]{ ((F)temp1).X[0],  new Fraction(ring.numberONE, ring.posConst[3]) });
        g.CleanElement(powEl);
        g.List_of_Change.set(i-startPos, powEl);
    }
        if(((F)temp1).name==F.ROOTOF){
        Element powEl=new F(F.POW, new Element[]{ ((F)temp1).X[0],  new Fraction(ring.numberONE, ((F)temp1).X[1]) });
        g.CleanElement(powEl);
        g.List_of_Change.set(i-startPos, powEl);
    }
   }
  }
 }
  }

/**
 * Factorization of Exponent Functions in one virtual Monomial
 * (a^b a^c--> a^{b+c})
 * @param inpMonom - virtual monomial
 * @param ring - Ring
 * @return new function obtained from this monomial
 * (not all functions de-converted)
 */
  public Element factorExponentialFunction1Monom_(Polynom inpMonom, Ring ring ) {
        Element[] res=factorExponentialFunction1Monom( inpMonom,  ring );
        return (res.length==0)?ring.numberONE:(res.length==1)? res[0]: new F(F.MULTIPLY,res);
  }
 /**
 * Factorization of Exponent Functions in one virtual Monomial
 * (a^b a^c--> a^{b+c})
 *  New functions saved in the List-of-Change.
 * General (((a^h)^g) a^b a^c--> a^{b+c+h*g})
 * @param inpMonom - virtual monomial
 * @param ring - Ring
 * @return Element[] with [0]= new virtual monomial with some deleted multiplies
 * other elements are the new functions-multiplies.
 */
 public static Element[] factorExponentialFunction1Monom(Polynom inpMonom, Ring ring ) {
 if(inpMonom.isItNumber(ring))return new Element[]{inpMonom};
 CanonicForms g=ring.CForm;
 deleteSQRT_and_CUBRT(inpMonom, ring);
 int startPos=Math.max(ring.varNames.length, inpMonom.powers.length-g.List_of_Change.size())  ;// ring.varPolynom.length;
 ArrayList<Element> res=new ArrayList<Element>();
 Element temp1; // для функции, которая соответствует виртуальной переменной
 Element new_pow1=ring.numberZERO;  // total degree
 Element[][] tempEl=new Element[inpMonom.powers.length][];
 for(int i=startPos; i<inpMonom.powers.length;i++){ 
     if(inpMonom.powers[i]!=0){    
      temp1=g.List_of_Change.get(i-startPos); 
      new_pow1=ring.numberONE.valOf(inpMonom.powers[i], ring);
      while((temp1 instanceof F)&&((((F)(temp1)).name==F.POW)||(((F)(temp1)).name==F.intPOW))){ 
        new_pow1=new_pow1.multiply(((F)temp1).X[1],ring);
        temp1=((F)temp1).X[0];
      }
      tempEl[i]=((temp1 instanceof F)&&(((F)(temp1)).name==F.EXP))?
          new Element[]{new Fname("\\e"), new_pow1.multiply(((F)(temp1)).X[0], ring)}:
          new Element[]{temp1,new_pow1};   
 }}
 for(int i=startPos; i<inpMonom.powers.length;i++){// главный цикл ------- for -------------
     if(tempEl[i]!=null){Element e_pow=tempEl[i][1];
       for(int j=i+1;j<inpMonom.powers.length;j++){
             if((tempEl[j]!=null)&&(tempEl[i][0].equals(tempEl[j][0], ring))){
                e_pow=tempEl[j][1].add(e_pow, ring);
                tempEl[j]=null;
                inpMonom.powers[j]=0;
             }
       }   
       if(!((e_pow instanceof Polynom)||(e_pow.isItNumber()))){
          e_pow=g.CleanElement(e_pow); 
          e_pow= g.SimplifyWithGether(e_pow, false);  }
        tempEl[i][1]=e_pow;
        Element ffi=g.powFSP(tempEl[i]);
        if ((tempEl[i][0] instanceof Fname)&&(((Fname)tempEl[i][0]).name.equals("\\e"))) 
             ffi= new F(F.EXP,tempEl[i][1] );
        res.add(ffi); inpMonom.powers[i]=0;
     }
 }      
  Polynom coef=inpMonom.truncate();
 if(res.size()==0)return new Element[]{coef};
 if(!coef.isOne(g.RING)) res.add(0, coef);
 return  res.toArray(new Element[res.size()]);
 }
 
/** 
 * Factorization of Exponent Functs in one virtual Monomial
 *  New functions saved in the List-of-Change.
 * @param inpMonom
 * @param ring
 * @param boolean deepFactoring (if needs recursive factoring)
 * @return one virtual monomial 
 */
    public static Polynom factorExpFunctMonom(Polynom inpMonom, int recursiveFactoring, Ring ring ) {
        if(inpMonom.isItNumber())return inpMonom;
        Element[] res=factorExponentialFunction1Monom( inpMonom,  ring );
        Polynom p= Polynom.polynomFromNot0Number(ring.numberONE);
        if (res.length==0) return p;
        int i=0; 
        if(res[0] instanceof Polynom) {p=(Polynom)res[0]; i++;}
        for (; i < res.length; i++) {
            Element ee=res[i];
            if((recursiveFactoring!=0)&&(ee  instanceof F)){ 
              F ff=(F)ee; 
              int Xl=ff.X.length;
              if(Xl>0) {Element[] XX= new Element[Xl]; 
                  for (int j = 0; j < Xl; j++) {
                   // XX[j]=(ff.X[j] instanceof F)? ((F)ff.X[j]).Factor(false,ring):ff.X[j];
                      XX[j]= ff.X[j];
                  } ee= new F(ff.name, XX);  
            }  }  //  ------------------------- Сделали дубль функции -------------------------------------
            Element el = p.multiply(ring.CForm.ElementToPolynom(ee, false), ring);
            p = (el instanceof Polynom)? (Polynom) el: new Polynom(new int[]{}, new Element[]{el});
        }
        return p;
  }

 

    public static void main(String[] args) {
        Ring ring = new Ring("Q[x,y,z]");  ring.page=new Page(ring, true);
        //1/\\log_{3}(2) + 2/\\log_{9}(4) - 3/\\log_{27}(8)
        F p = new F("\\sqrt(2)^{2x} \\sqrt(2)^{4y+1}+2+\\sin(x)\\e^{5} \\exp(3y)\\exp(2x)", ring); 
        Element pp= ring.CForm.ElementToPolynom(p,true);    
        System.out.println("result = " +   
                new LN_EXP_POW_LOG_LG().factorExponentialFunction_((Polynom)pp, ring));
    }
}

