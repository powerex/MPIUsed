/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import com.mathpar.number.Ring;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 Упрощение обратных тригонометрических функций и композиций
 * обратных и прямых тригонометрических функций.
 *
 * @author student
 */
public class SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc   {
  Ring ring;
  public SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc(Ring rng)
           {ring = rng;}



    /**
     * на входе функции:sin[arcsin[a]],cos[arccos[a]],tg[arctg[a]],ctg[arcctg[a]]
     * @return аргумент функции - a
     */
    public static F trigInversSubst(F f) {
        if (f.name == F.SIN && f.X.length == 1 && ((F) f.X[0]).name == F.ARCSIN) {
            if (((F) f.X[0]).X.length == 1) {
                return new F(((F) f.X[0]).X[0]);
            } else {
                return new F(F.ID, ((F) f.X[0]).X); //ловим аргумент}
            }
        }
        if (f.name == F.COS && f.X.length == 1 && ((F) f.X[0]).name == F.ARCCOS) {
            if (((F) f.X[0]).X.length == 1) {
                return new F(((F) f.X[0]).X[0]);
            } else {
                return new F(F.ID, ((F) f.X[0]).X); //ловим аргумент}
            }
        }
        if (f.name == F.TG && f.X.length == 1 && ((F) f.X[0]).name == F.ARCTG) {
            if (((F) f.X[0]).X.length == 1) {
                return new F(((F) f.X[0]).X[0]);
            } else {
                return new F(F.ID, ((F) f.X[0]).X); //ловим аргумент}
            }
        }
        if (f.name == F.CTG && f.X.length == 1 && ((F) f.X[0]).name == F.ARCCTG) {
            if (((F) f.X[0]).X.length == 1) {
                return new F(((F) f.X[0]).X[0]);
            } else {
                return new F(F.ID, ((F) f.X[0]).X); //ловим аргумент}
            }
        }
        return f;
    }

    /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу arcsin a +- arcsin b = arcsin( a*sqrt(1-b^2) +- b*sqrt(1-a^2)
     * @param (F) - функции вида arcsin a +- arcsin b
     * @return F - функции вида arcsin( a*sqrt(1-b^2) +- b*sqrt(1-a^2)
     */
    public   F trigInversadd_and_substract(F f) {Element one1= ring.numberONE();
        if (f.name == F.ADD && f.X.length == 2) {
            if (((F) f.X[0]).name == F.ARCSIN && ((F) f.X[1]).name == F.ARCSIN && ((F) f.X[0]).X.length == 1 && ((F) f.X[1]).X.length == 1) {
                F f1 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[1]).X[0], NumberZ.ONE.add(NumberZ.ONE)})}));//sqrt[1-b^2]
                F f2 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[0]).X[0], NumberZ.ONE.add(NumberZ.ONE),})}));//sqrt[1-a^2]

                F f3 = new F(F.MULTIPLY, new Element[]{((F) f.X[1]).X[0], f2});//b*sqrt[1-a^2]
                F f4 = new F(F.MULTIPLY, new Element[]{((F) f.X[0]).X[0], f1});//a*sqrt[1-b^2]
                return new F(F.ARCSIN, new F(F.ADD, new Element[]{f4, f3}));
            }
        } else {
            if (((F) f.X[0]).name == F.ARCSIN && ((F) f.X[1]).name == F.ARCSIN && ((F) f.X[0]).X.length == 1 && ((F) f.X[1]).X.length == 1) {
                F f1 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{ring.numberONE(),
                            new F(F.POW, new Element[]{((F) f.X[1]).X[0], NumberZ.ONE.add(NumberZ.ONE)})}));//sqrt[1-b^2]
                F f2 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[0]).X[0], NumberZ.ONE.add(NumberZ.ONE)})}));//sqrt[1-a^2]

                F f3 = new F(F.MULTIPLY, new Element[]{((F) f.X[1]).X[0], f2});//b*sqrt[1-a^2]
                F f4 = new F(F.MULTIPLY, new Element[]{((F) f.X[0]).X[0], f1});//a*sqrt[1-b^2]
                return new F(F.ARCSIN, new F(F.SUBTRACT, new Element[]{f4, f3}));

            }

        }
       return f;
    }
   /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу arcsin( a*sqrt(1-b^2) +- b*sqrt(1-a^2)=arccos(sqrt[1-a^2]*sqrt[1-b^2]-+ab
     * @param (F) - функции вида arcsin( a*sqrt(1-b^2) +- b*sqrt(1-a^2)
     * @return F - функции вида arccos(sqrt[1-a^2]*sqrt[1-b^2]-+ab
     */

    public F trigInversadd_(F f) { Element one1= ring.numberONE();
        if (f.name == F.ADD && f.X.length == 2) {
            if (((F) f.X[0]).name == F.ARCSIN && ((F) f.X[1]).name == F.ARCSIN && ((F) f.X[0]).X.length == 1 && ((F) f.X[1]).X.length == 1) {
                F f1 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[1]).X[0], NumberZ.ONE.add(NumberZ.ONE)})}));//sqrt[1-b^2]
                F f2 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[0]).X[0], NumberZ.ONE.add(NumberZ.ONE),})}));//sqrt[1-a^2]

                F f3 = new F(F.MULTIPLY, new Element[]{f2, f1});//A*B
                F f4 = new F(F.MULTIPLY, new Element[]{((F) f.X[0]).X[0], ((F) f.X[1]).X[0]});//sqrt[1-a^2]*sqrt[1-b^2]
                return new F(F.ARCCOS, new F(F.SUBTRACT, new Element[]{f3, f4}));
            }
        } else {
            if (((F) f.X[0]).name == F.ARCSIN && ((F) f.X[1]).name == F.ARCSIN && ((F) f.X[0]).X.length == 1 && ((F) f.X[1]).X.length == 1) {
                F f1 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[1]).X[0], NumberZ.TWO})}));//sqrt[1-b^2]
                F f2 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[0]).X[0], NumberZ.ONE.add(NumberZ.ONE)})}));//sqrt[1-a^2]

                F f3 = new F(F.MULTIPLY, new Element[]{f2, f1});
                F f4 = new F(F.MULTIPLY, new Element[]{((F) f.X[0]).X[0], ((F) f.X[1]).X[0]});
                return new F(F.ARCCOS, new F(F.ADD, new Element[]{f3, f4}));

            }

        }
      return f;
    }


    //Пример №2


 /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу arccos a +- arccos b = arccos( ab-+sqrt(1-a^2)*sqrt(1-a^2)
     * @param (F) - функции вида  arccos a +- arccos b
     * @return F - функция вида  arccos( ab-+sqrt(1-a^2)*sqrt(1-a^2)
     */
    public   F cos_and_substract(F f){Element one1=ring.numberONE();
        if(f.name==F.ADD&&f.X.length==2){
            if(((F)f.X[0]).name==F.ARCCOS&&((F)f.X[1]).name==F.ARCCOS&&((F) f.X[0]).X.length == 1 && ((F) f.X[1]).X.length == 1) {
        F f1 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[1]).X[0], NumberZ.ONE.add(NumberZ.ONE)})}));//sqrt[1-b^2]
                F f2 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[0]).X[0], NumberZ.ONE.add(NumberZ.ONE),})}));//sqrt[1-a^2]

                F f3 = new F(F.MULTIPLY, new Element[]{f2, f1});//sqrt[1-a^2]*sqrt[1-b^2]
                F f4 = new F(F.MULTIPLY, new Element[]{((F) f.X[0]).X[0], ((F) f.X[1]).X[0]});//a*b
                return new F(F.ARCCOS, new F(F.SUBTRACT, new Element[]{f4, f3}));
            }
        } else {
             if(f.name==F.SUBTRACT&&f.X.length==2)
            if (((F) f.X[0]).name == F.ARCCOS && ((F) f.X[1]).name == F.ARCCOS && ((F) f.X[0]).X.length == 1 && ((F) f.X[1]).X.length == 1) {
                F f1 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[1]).X[0], NumberZ.TWO})}));//sqrt[1-b^2]
                F f2 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[0]).X[0], NumberZ.TWO})}));//sqrt[1-a^2]

                F f3 = new F(F.MULTIPLY, new Element[]{f2, f1});
                F f4 = new F(F.MULTIPLY, new Element[]{((F) f.X[0]).X[0], ((F) f.X[1]).X[0]});
                return new F(F.ARCCOS, new F(F.ADD, new Element[]{f3, f4}));

            }

        }
     /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу arccos( ab-+sqrt(1-a^2)*sqrt(1-a^2)=arcsin(b*sqrt[1-a^2+-a*sqrt[1-b^2]
     * @param (F) - функции вида  arccos( ab-+sqrt(1-a^2)*sqrt(1-a^2)
     * @return F - функция вида  arccosarcsin(b*sqrt[1-a^2+-a*sqrt[1-b^2]
     */

      return f; }
      public F trigInversadd_and_substractARCSIN(F f) {Element one1=ring.numberONE();
        if (f.name == F.ADD && f.X.length == 2) {
            if (((F) f.X[0]).name == F.ARCCOS && ((F) f.X[1]).name == F.ARCCOS && ((F) f.X[0]).X.length == 1 && ((F) f.X[1]).X.length == 1) {
                F f1 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[1]).X[0], NumberZ.ONE.add(NumberZ.ONE)})}));//sqrt[1-b^2]
                F f2 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[0]).X[0], NumberZ.TWO,})}));//sqrt[1-a^2]

                F f3 = new F(F.MULTIPLY, new Element[]{((F) f.X[1]).X[0], f2});//b*sqrt[1-a^2]
                F f4 = new F(F.MULTIPLY, new Element[]{((F) f.X[0]).X[0], f1});//a*sqrt[1-b^2]
                return new F(F.ARCSIN, new F(F.ADD, new Element[]{f3, f4}));
            }
        } else {
            if (f.name == F.SUBTRACT && f.X.length == 2)
            if (((F) f.X[0]).name == F.ARCCOS && ((F) f.X[1]).name == F.ARCCOS && ((F) f.X[0]).X.length == 1 && ((F) f.X[1]).X.length == 1) {
                F f1 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[1]).X[0], NumberZ.TWO})}));//sqrt[1-b^2]
                F f2 = new F(F.SQRT,
                        new F(F.SUBTRACT, new Element[]{one1,
                            new F(F.POW, new Element[]{((F) f.X[0]).X[0], NumberZ.TWO})}));//sqrt[1-a^2]

                F f3 = new F(F.MULTIPLY, new Element[]{((F) f.X[1]).X[0], f2});//b*sqrt[1-a^2]
                F f4 = new F(F.MULTIPLY, new Element[]{((F) f.X[0]).X[0], f1});//a*sqrt[1-b^2]
                return new F(F.ARCSIN, new F(F.SUBTRACT, new Element[]{f3, f4}));

    }}return f;
      }
/**
     * алгоритмы преобразования обратной тригонометрической функции по правилу arctg a + arctg b = arctg (a+b)/(1-ab)
     * @param (F) - функции вида arctg a + arctg b
     * @return F - функции вида arctg (a+b)/(1-ab)
     */
    public static F tg_tg(F f){
        F res=f;
        if(f.name==F.ADD&&f.X.length==2){
            if(((F)f.X[0]).name==F.ARCTG&&((F)f.X[1]).name==F.ARCTG&&((F) f.X[0]).X.length == 1 && ((F) f.X[1]).X.length == 1) {
                F a=new F(((F)(f.X[0])).X[0]); //a
                F b=new F(((F)(f.X[1])).X[0]); //b
                F one = new F(NumberZ.ONE); // создаем 1
                F f1 = new F(F.ADD, new F[]{a,b});// a+b
                F f2 = new F(F.MULTIPLY, new F[]{a,b});//a*b
                F f3 = new F(F.SUBTRACT, new F[]{one,f2}); // 1-a*b
                F f4  = new F(F.DIVIDE, new F[]{f1,f3}); // (a+b)/(1-ab)
                res = new F(F.ARCTG, new F[]{f4}); //arctg (a+b)/(1-ab)
            }
        }
        return res;
    }
    /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу arctg a - arctg b = arctg (a-b)/(1+ab)
     * @param (F) - функции вида arctg a - arctg b
     * @return F - функции вида arctg (a-b)/(1+ab)
     */
    public  static F aatg_aatg (F f){
        F res = f;
        if(f.name==F.SUBTRACT&&f.X.length==2){
            if(((F)f.X[0]).name==F.ARCTG&&((F)f.X[1]).name==F.ARCTG&&((F) f.X[0]).X.length == 1 && ((F) f.X[1]).X.length == 1) {
               F a=new F(((F)(f.X[0])).X[0]); //a
               F b=new F(((F)(f.X[1])).X[0]); //b
               F one = new F(NumberZ.ONE); // создаем 1
               F f1 = new F(F.SUBTRACT, new F[]{a,b});// a-b
               F f2 = new F(F.MULTIPLY, new F[]{a,b});//a*b
               F f3 = new F(F.ADD, new F[]{one,f2}); // 1+a*b
               F f4  = new F(F.DIVIDE, new F[]{f1,f3}); // (a-b)/(1+ab)
               res = new F(F.ARCTG, new F[]{f4}); //arctg (a-b)/(1+ab)
            }
    }
        return res;}
    /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу arcsin a + arccos a = П/2
     * @param (F) - функции вида arcsin a + arccos a
     * @return F - функции вида П/2
     */
    public  static Element sin_cos (F f){
        F res = f;
        if(f.name==F.ADD&&f.X.length==2){
            if(((F)f.X[0]).name==F.ARCSIN&&((F)f.X[1]).name==F.ARCCOS&&((F) f.X[0]).X.length == 1 && ((F) f.X[1]).X.length == 1) {
        return new Polynom(new int[]{0,0,0,0,1}, new Element[]{new NumberR64("0.5")}); // Pi/2
            }
        }
    return res;
    }

     /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу arctg a + arcctg a = П/2
     * @param (F) - функции вида arctg a + arcctg a
     * @return F - функции вида П/2
     */
    public  static Element ctg_cctg (F f){
        F res = f;
        if(f.name==F.ADD&&f.X.length==2){
            if(((F)f.X[0]).name==F.ARCTG&&((F)f.X[1]).name==F.ARCCTG&&((F) f.X[0]).X.length == 1 && ((F) f.X[1]).X.length == 1) {
        return new Polynom(new int[]{0,0,0,0,1}, new Element[]{new NumberR64("0.5")}); // Pi/2
            }
        }
    return res;
    }
    /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу arcsec a + arccsc a = П/2
     * @param (F) - функции вида arcsec a + arccsc a
     * @return F - функции вида П/2
     */
    public  static Element sec_csc (F f){
        F res = f;
        if(f.name==F.ADD&&f.X.length==2){
            if(((F)f.X[0]).name==F.ARCSEC &&((F)f.X[1]).name==F.ARCCSC&&((F) f.X[0]).X.length == 1 && ((F) f.X[1]).X.length == 1) {
        return new Polynom(new int[]{0,0,0,0,1}, new Element[]{new NumberR64("0.5")}); // Pi/2
            }
        }
    return res;
    }
    /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу 1) arctg (tg a)= k * Pi + a; 2) arcctg (ctg a)= k * Pi + a
     * @param (F) - функции вида 1) arctg (tg a); 2) arctg (ctg a)
     * @return F - функции вида k * Pi + a
     */
    public   Element triginverse (F f){
        F res = f;
        if(f.name==F.ARCTG&&f.X.length==1){
        if (((F)f.X[0]).name==F.TG) ;//анализируем входную ф-ю
        Element a=((F)f.X[0]).X[0];// аргумент ф-ии
        return new F(F.ADD, new Element[] {new Polynom("k*\\pi",ring),a});}
        if(f.name==F.ARCCTG&&f.X.length==1){
        if (((F)f.X[0]).name==F.CTG) ;
        Element a=((F)f.X[0]).X[0];
        return new F(F.ADD, new Element[]{new Polynom("k*\\pi",ring),a});}
        return res;
    }
  /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  arcsin a = arcos sqrt(1-a^2) = arctg a/sqrt (1-a^2)
     * @param (F) - функции вида arcsin a
   *  name - cos:  arcos sqrt(1-a^2); tg:arctg a/sqrt (1-a^2);
     * @return F
     */

     public static Element inversetrig_sin (F f, int name){
         if(f.name==F.ARCSIN && f.X.length==1){
             if(name == F.COS){
                 F a1 = new F(F.intPOW, new Element[]{((F)f).X[0],new NumberR64(2)});// a^2
                 F a2 = new F(F.SUBTRACT, new Element[]{new NumberR64(1),a1}); // 1-a^2
                 F a3 = new F(F.SQRT, a2); // sqrt (1-a^2)
                 return new F(F.ARCCOS, a3);
             }
             if(name == F.TG){
                 F a1 = new F(F.intPOW, new Element[]{((F)f).X[0],new NumberR64(2)});// a^2
                 F a2 = new F(F.SUBTRACT, new Element[]{new NumberR64(1),a1});// 1-a^2
                 F a3 = new F(F.SQRT, a2); // sqrt (1-a^2)
                 F a4 = new F(F.DIVIDE, new Element[]{((F)f).X[0],a3}); // a/ sqrt(1-a^2)
                 return new F(F.ARCTG, a4);
             }
            }
         return f;

     }
     /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  arccos a = arsin sqrt(1-a^2) = arcctg a/sqrt (1-a^2)
     * @param (F) - функции вида arccos a
      * name - sin:  arsin sqrt(1-a^2); ctg:arcctg a/sqrt (1-a^2);
     * @return F
     */

     public static Element inversetrig_cos (F f, int name){
         if(f.name==F.ARCCOS && f.X.length==1){
             if(name == F.SIN){
                 F a1 = new F(F.intPOW, new Element[]{((F)f).X[0],new NumberR64(2)});// a^2
                 F a2 = new F(F.SUBTRACT, new Element[]{new NumberR64(1),a1}); // 1-a^2
                 F a3 = new F(F.SQRT, a2); // sqrt (1-a^2)
                 return new F(F.ARCSIN, a3);
             }
             if(name == F.CTG){
                 F a1 = new F(F.intPOW, new Element[]{((F)f).X[0],new NumberR64(2)});// a^2
                 F a2 = new F(F.SUBTRACT, new Element[]{new NumberR64(1),a1});// 1-a^2
                 F a3 = new F(F.SQRT, a2); // sqrt (1-a^2)
                 F a4 = new F(F.DIVIDE, new Element[]{((F)f).X[0],a3}); // a/ sqrt(1-a^2)
                 return new F(F.ARCCTG, a4);
             }
          }
         return f;

     }

      /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  arccos (cos a)=2k*Pi+a
     * @param (F) - функции вида arccos (cos a)
     * @return F - функции вида 2k*Pi+a
     */
    public  Element arccos_cosa (F f, Ring ring){
        F res = f;
        if(f.name==F.ARCCOS&&f.X.length==1){
        if (((F)f.X[0]).name==F.COS) ;//анализируем входную ф-ю
        Element a=((F)f.X[0]).X[0];// аргумент ф-ии
        NumberZ two = new NumberZ(2); // создаем 2
        Element k1 = new Polynom("k*\\pi", ring).multiply( NumberZ.TWO,ring);
        return new F(F.ADD, new Element[]{k1,a});
        }
        return res;
    }

     /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  arccos (cos a)=2k*Pi-a
     * @param (F) - функции вида arccos (cos a)
     * @return F - функции вида 2k*Pi-a
     */
    public Element arccos_ (F f){
        F res = f;
        if(f.name==F.ARCCOS&&f.X.length==1){
        if (((F)f.X[0]).name==F.COS) ;//анализируем входную ф-ю
        Element a=((F)f.X[0]).X[0];// аргумент ф-ии
        NumberZ two = new NumberZ(2); // создаем 2
        Element k1 = new Polynom("k*\\pi", ring).multiply( NumberZ.TWO,Ring.ringZxyz);
        return new F(F.SUBTRACT, new Element[]{k1,a});
        }
        return res;
    }
     /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  arcsin(sin a)= k*Pi + (-1)^k *a
     * @param (F) - функции вида arcsin(sin a)
     * @return F - функции вида k*Pi + (-1)^k *a
     */
    public  Element arcsin_ (F f){
       F res = f;
       F aa=new F(((F)(f.X[0])).X[0]);
       if(f.name==F.ARCSIN&&f.X.length==1){
       if (((F)f.X[0]).name==F.SIN) ;//анализируем входную ф-ю
       Element r1 = new Polynom("k*\\pi", ring);
       F j = new F(F.POW, new Element[]{ring.numberONE().negate(ring),
                                    new Polynom("k",ring)});// (-1) ^k
       F uu=new F(F.MULTIPLY, new F[]{j ,aa});// (-1)^k *a
       return new F(F.ADD,new Element[]{r1,uu});}//k*Pi + (-1)^k *a
       return res;
    }

     /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  cos( arcsin a )= sqrt (1-a^2)
     * @param (F) - функции вида cos( arcsin a )
     * @return F - функции вида  sqrt (1-a^2)
     */

   public static Element tg_ (F f){
       F res = f;
       if(f.name==F.COS&&f.X.length==1){
           if(((F)f.X[0]).name==F.ARCSIN);
           Element a=((F)f.X[0]).X[0];// аргумент ф-ии
            F a1 = new F(F.intPOW, new Element[]{a,new NumberR64(2)});// a^2
            F a2 = new F(F.SUBTRACT, new Element[]{new NumberR64(1),a1}); // 1-a^2
            return new F(F.SQRT, a2); // sqrt (1-a^2)
       }
       return res;
          }

    /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  tg( arcsin a )= a / sqrt (1-a^2)
     * @param (F) - функции вида tg( arcsin a )
     * @return F - функции вида  a / sqrt (1-a^2)
     */
       public static Element ctg_ (F f){
           F res = f;
       if(f.name==F.TG&&f.X.length==1){
           if(((F)f.X[0]).name==F.ARCSIN);
           Element a=((F)f.X[0]).X[0];// аргумент ф-ии
           F a1 = new F(F.intPOW, new Element[]{a,new NumberR64(2)});// a^2
                 F a2 = new F(F.SUBTRACT, new Element[]{new NumberR64(1),a1});// 1-a^2
                 F a3 = new F(F.SQRT, a2); // sqrt (1-a^2)
                 return new F(F.DIVIDE, new Element[]{a,a3}); // a/ sqrt(1-a^2)
       }
           return res;}
        /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  ctg( arcsin a )= sqrt (1-a^2) / a
     * @param (F) - функции вида ctg( arcsin a )
     * @return F - функции вида  sqrt (1-a^2) / a
     */

       public static Element ctg_sin (F f){
           F res = f;
       if(f.name==F.CTG&&f.X.length==1){
           if(((F)f.X[0]).name==F.ARCSIN);
           Element a=((F)f.X[0]).X[0];// аргумент ф-ии
           F a1 = new F(F.intPOW, new Element[]{a,new NumberR64(2)});// a^2
                 F a2 = new F(F.SUBTRACT, new Element[]{new NumberR64(1),a1});// 1-a^2
                 F a3 = new F(F.SQRT, a2); // sqrt (1-a^2)
                 return new F(F.DIVIDE, new Element[]{a3,a}); // a/ sqrt(1-a^2)
       }
           return res;}




       /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  sin( arccos a )= sqrt (1-a^2)
     * @param (F) - функции вида sin( arccos a )
     * @return F - функции вида  sqrt (1-a^2)
     */

   public static Element sin_sin (F f){
       F res = f;
       if(f.name==F.SIN&&f.X.length==1){
           if(((F)f.X[0]).name==F.ARCCOS);
           Element a=((F)f.X[0]).X[0];// аргумент ф-ии
            F a1 = new F(F.intPOW, new Element[]{a,new NumberR64(2)});// a^2
            F a2 = new F(F.SUBTRACT, new Element[]{new NumberR64(1),a1}); // 1-a^2
            return new F(F.SQRT, a2); // sqrt (1-a^2)
       }
       return res;
          }



        /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  tg( arccos a )= sqrt (1-a^2) / a
     * @param (F) - функции вида tg( arccos a )
     * @return F - функции вида  sqrt (1-a^2) / a
     */

       public static Element tg_cos (F f){
           F res = f;
       if(f.name==F.TG&&f.X.length==1){
           if(((F)f.X[0]).name==F.ARCCOS);
           Element a=((F)f.X[0]).X[0];// аргумент ф-ии
           F a1 = new F(F.intPOW, new Element[]{a,new NumberR64(2)});// a^2
                 F a2 = new F(F.SUBTRACT, new Element[]{new NumberR64(1),a1});// 1-a^2
                 F a3 = new F(F.SQRT, a2); // sqrt (1-a^2)
                 return new F(F.DIVIDE, new Element[]{a3,a}); // a/ sqrt(1-a^2)
       }
           return res;}

        /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  ctg( arccos a )= a / sqrt (1-a^2)
     * @param (F) - функции вида ctg( arccos a )
     * @return F - функции вида  a / sqrt (1-a^2)
     */
       public static Element ctg_cos (F f){
           F res = f;
       if(f.name==F.CTG&&f.X.length==1){
           if(((F)f.X[0]).name==F.ARCCOS);
           Element a=((F)f.X[0]).X[0];// аргумент ф-ии
           F a1 = new F(F.intPOW, new Element[]{a,new NumberR64(2)});// a^2
                 F a2 = new F(F.SUBTRACT, new Element[]{new NumberR64(1),a1});// 1-a^2
                 F a3 = new F(F.SQRT, a2); // sqrt (1-a^2)
                 return new F(F.DIVIDE, new Element[]{a,a3}); // a/ sqrt(1-a^2)
       }
           return res;}

         /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  sin( arctg a )= a / sqrt (1+a^2)
     * @param (F) - функции вида sin( arctg a )
     * @return F - функции вида  a / sqrt (1-a^2)
     */
       public static Element sin_ctg (F f){
           F res = f;
       if(f.name==F.SIN&&f.X.length==1){
           if(((F)f.X[0]).name==F.ARCTG);
           Element a=((F)f.X[0]).X[0];// аргумент ф-ии
           F a1 = new F(F.intPOW, new Element[]{a,new NumberR64(2)});// a^2
                 F a2 = new F(F.ADD, new Element[]{new NumberR64(1),a1});// 1+a^2
                 F a3 = new F(F.SQRT, a2); // sqrt (1+a^2)
                 return new F(F.DIVIDE, new Element[]{a,a3}); // a/ sqrt(1+a^2)
       }
           return res;}

           /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  cos( arctg a )= 1 / sqrt (1+a^2)
     * @param (F) - функции вида cos( arctg a )
     * @return F - функции вида  1 / sqrt (1-a^2)
     */
       public static Element cos_ctg (F f){
           F res = f;
       if(f.name==F.COS&&f.X.length==1){
           if(((F)f.X[0]).name==F.ARCTG);
           Element a=((F)f.X[0]).X[0];// аргумент ф-ии
           F a1 = new F(F.intPOW, new Element[]{a,new NumberR64(2)});// a^2
                 F a2 = new F(F.ADD, new Element[]{new NumberR64(1),a1});// 1+a^2
                 F a3 = new F(F.SQRT, a2); // sqrt (1+a^2)
                 F one = new F(NumberZ.ONE); // создаем 1
                return new F(F.DIVIDE, new Element[]{one,a3}); // 1/ sqrt(1+a^2)
        }
           return res;}




      /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  ctg( arctg a )= 1 / a
     * @param (F) - функции вида ctg( arctg a )
     * @return F - функции вида 1 / a
     */

      public static Element ctg_ctg (F f){
           F res = f;
       if(f.name==F.CTG&&f.X.length==1){
           if(((F)f.X[0]).name==F.ARCTG);
           Element a=((F)f.X[0]).X[0];// аргумент ф-ии
        F one = new F(NumberZ.ONE); // создаем 1
       return new F(F.DIVIDE, new Element[]{one,a}); // 1/ a
        }
           return res;}


              /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  sin( arcctg a )= 1 / sqrt (1+a^2)
     * @param (F) - функции вида sin( arcctg a )
     * @return F - функции вида  1 / sqrt (1-a^2)
     */
       public static Element sin_cctg (F f){
           F res = f;
       if(f.name==F.SIN&&f.X.length==1){
           if(((F)f.X[0]).name==F.ARCCTG);
           Element a=((F)f.X[0]).X[0];// аргумент ф-ии
           F a1 = new F(F.intPOW, new Element[]{a,new NumberR64(2)});// a^2
                 F a2 = new F(F.ADD, new Element[]{new NumberR64(1),a1});// 1+a^2
                 F a3 = new F(F.SQRT, a2); // sqrt (1+a^2)
                 F one = new F(NumberZ.ONE); // создаем 1
                return new F(F.DIVIDE, new Element[]{one,a3}); // 1/ sqrt(1+a^2)
        }
           return res;}


            /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  cos( arcctg a )= a / sqrt (1+a^2)
     * @param (F) - функции вида cos( arcctg a )
     * @return F - функции вида  a / sqrt (1-a^2)
     */
       public static Element cos_cctg (F f){
           F res = f;
       if(f.name==F.COS&&f.X.length==1){
           if(((F)f.X[0]).name==F.ARCCTG);
           Element a=((F)f.X[0]).X[0];// аргумент ф-ии
           F a1 = new F(F.intPOW, new Element[]{a,new NumberR64(2)});// a^2
                 F a2 = new F(F.ADD, new Element[]{new NumberR64(1),a1});// 1+a^2
                 F a3 = new F(F.SQRT, a2); // sqrt (1+a^2)
                 return new F(F.DIVIDE, new Element[]{a,a3}); // a/ sqrt(1+a^2)
       }
           return res;}


      /**
     * алгоритмы преобразования обратной тригонометрической функции по правилу  tg( arcctg a )= 1 / a
     * @param (F) - функции вида tg( arcctg a )
     * @return F - функции вида 1 / a
     */

      public static Element tg_cctg (F f){
           F res = f;
       if(f.name==F.TG&&f.X.length==1){
           if(((F)f.X[0]).name==F.ARCCTG);
           Element a=((F)f.X[0]).X[0];// аргумент ф-ии
        F one = new F(NumberZ.ONE); // создаем 1
       return new F(F.DIVIDE, new Element[]{one,a}); // 1/ a
        }
           return res;}

  /**     Возвращает композицию любой обратной тригонометрической функции
   *     с любой прямой тригонометрической функцией.
   */
   public static Element table_InverseTrig (F f,int name){
       if(name==F.SIN){
           if(((F)f.X[0]).name==F.ARCSIN){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.trigInversSubst(f);
           }
           if(((F)f.X[0]).name==F.ARCCOS){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.sin_sin(f);
           }
           if(((F)f.X[0]).name==F.ARCTG){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.sin_ctg(f);
           }
           if(((F)f.X[0]).name==F.ARCCTG){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.sin_cctg(f);
           }

       }
       if(name==F.COS){
           if(((F)f.X[0]).name==f.ARCSIN){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.tg_(f);
           }
           if(((F)f.X[0]).name==F.ARCCOS){
                return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.trigInversSubst(f);
           }
           if(((F)f.X[0]).name==F.ARCTG){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.cos_ctg(f);
           }
           if(((F)f.X[0]).name==F.ARCCTG){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.cos_cctg(f);
           }

       }if(name==F.TG){
           if(((F)f.X[0]).name==f.ARCSIN){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.ctg_(f);
           }
           if(((F)f.X[0]).name==F.ARCCOS){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.tg_cos(f);
           }
           if(((F)f.X[0]).name==F.ARCTG){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.trigInversSubst(f);
           }
           if(((F)f.X[0]).name==F.ARCCTG){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.tg_cctg(f);
           }

       }if(name==F.CTG){
           if(((F)f.X[0]).name==f.ARCSIN){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.ctg_sin(f);
           }
           if(((F)f.X[0]).name==F.ARCCOS){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.ctg_cos(f);
           }
           if(((F)f.X[0]).name==F.ARCTG){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.ctg_ctg(f);
           }
           if(((F)f.X[0]).name==F.ARCCTG){
               return SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.trigInversSubst(f);
           }

       }
       return f;
   }
   /**  Для заданной функции f , у которой на листьях располагаются
    *   композиции  обратных тригонометрических функции
    *    с прямыми тригонометрическими функциями на выходе будет построена
    * новая функции,
    *    в которой произведены соответствующие замены композиций.
    *
    * @param f заданная функция
    * @return новая функция
    */
    public Element rec_table(Element f) {
        if (f instanceof F) {
            if (((F) f).X.length > 0) {
                for (int i = 0; i < ((F) f).X.length; i++) { //цикл по всему элементу
                    if (((F) ((F) f).X[i]).name < F.FIRST_INFIX_NAME) { //проверка на пустое имя
                        ((F) f).X[i] = SimplifyInversTrigonFuncAndComposittionInversAndDirectTrigonFunc.table_InverseTrig(((F) ((F) f).X[i]), ((F) ((F) f).X[i]).name);//преобразование узла
                    } else {
                        rec_table(((F) f).X[i]);
                    }
                }
            }
        }

        return f;

    }

}
