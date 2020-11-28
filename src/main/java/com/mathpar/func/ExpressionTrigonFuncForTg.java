/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import com.mathpar.number.Element;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;
import com.mathpar.polynom.*;

/**
 *
 * @author student
 */
public class ExpressionTrigonFuncForTg extends F {

    static private Polynom oneHalf = Polynom.polynomFromNumber(new NumberR64(0.5), new Ring("R64[]"));

    public ExpressionTrigonFuncForTg() {
    }

    /**
     * Конструктор, создающий функцию ExpressionTrigonFuncForTg от функции F
     *
     * @param a - входная функция
     */
    public ExpressionTrigonFuncForTg(F a) {
        this.name = a.name;
        this.X = a.X;
    }

    /**
     *
     * @param f
     * @return
     * @
     */

    /*public F rec_trigon_2a(F f)   {
     if (f.X.length > 0) {
     for (int i = 0; i < f.X.length; i++) {
     if (((F) f.X[i]).name < F.FIRST_INFIX_NAME) {
     f.X[i] = new Irina(((F) f.X[i])).trigon2();
     } else {
     rec_trigon_2a(((F) f.X[i]));
     }
     }
     }
     return f;
     }


     public F rec_trigon_tg_or_ctg(F f)   {
     if (f.X.length > 0) {
     for (int i = 0; i < f.X.length; i++) {
     if (((F) f.X[i]).name < F.FIRST_INFIX_NAME) {
     if(((F) f.X[i]).name == F.TG)
     f.X[i] = new Irina(((F) f.X[i])).decomp_Tg_OneSecondArgs();
     if(((F) f.X[i]).name == F.CTG)
     f.X[i] = new Irina(((F) f.X[i])).decomp_Ctg_OneSecondArgs();
     } else {
     rec_trigon_tg_or_ctg(((F) f.X[i]));
     }
     }
     }
     return f;
     }*/
//1
    /**
     * На входе функция - вида : Sin[a], где a - произвольный аргумент типа
     * Polynom
     *
     * @param f - входная функция
     * @return - возвращает выражение через универсальную подстановку - tg(a/2)
     */
    public F rec_trigon_sin(F f, Ring ring) {

        if (f.name == F.SIN) {
            if (f.X.length == 1) {
                if (f.X[0] instanceof Polynom) {
                    Polynom p = ((Polynom) f.X[0]);
                    Polynom[] p_arr_divandrem = p.divAndRem(oneHalf, ring);
                    Polynom arg = null;
                    if (p.coeffs[0].equals(new NumberR64(1))) {
                        arg = ((Polynom) f.X[0]).multiply(oneHalf, ring);//1/2*a
                    } else {
                        arg = ((Polynom) f.X[0]).divideExact(oneHalf, ring);//1/2*a
                    }

                    NumberR64 two = new NumberR64(2);
                    F t1 = new F(F.TG, arg);
                    NumberR64 one = new NumberR64(1);
                    F t2 = new F(F.POW, new Element[]{t1, two});
                    F t3 = new F(F.ADD, new Element[]{one, t2});
                    F t4 = new F(F.MULTIPLY, new Element[]{two, t1});
                    return new F(F.DIVIDE, new Element[]{t4, t3});
                }
            }
        }
        return f;
    }

//2
    /**
     * На входе функция - вида : Cos[a], где a - произвольный аргумент типа
     * Polynom
     *
     * @param f - входная функция
     * @return - возвращает выражение через универсальную подстановку - tg(a/2)
     */
    public F rec_trigon_cos(F f, Ring ring) {

        if (f.name == F.COS) {
            if (f.X.length == 1) {
                if (f.X[0] instanceof Polynom) {
                    Polynom p = ((Polynom) f.X[0]);
                    Polynom[] p_arr_divandrem = p.divAndRem(oneHalf, ring);
                    Polynom arg = null;
                    if (p.coeffs[0].equals(new NumberR64(1))) {
                        arg = ((Polynom) f.X[0]).multiply(oneHalf, ring);//1/2*a
                    } else {
                        arg = ((Polynom) f.X[0]).divideExact(oneHalf, ring);//1/2*a
                    }

                    NumberR64 two = new NumberR64(2);
                    F t1 = new F(F.TG, arg);
                    NumberR64 one = new NumberR64(1);
                    F t2 = new F(F.POW, new Element[]{t1, two});
                    F t3 = new F(F.ADD, new Element[]{one, t2});
                    F t4 = new F(F.SUBTRACT, new Element[]{one, t2});
                    return new F(F.DIVIDE, new Element[]{t4, t3});
                }
            }
        }
        return f;
    }

//3
    /**
     * На входе функция - вида : Ctg[a], где a - произвольный аргумент типа
     * Polynom
     *
     * @param f - входная функция
     * @return - возвращает выражение через универсальную подстановку - tg(a/2)
     */
    public F rec_trigon_ctg(F f, Ring ring) {

        if (f.name == F.CTG) {
            if (f.X.length == 1) {
                if (f.X[0] instanceof Polynom) {
                    Polynom p = ((Polynom) f.X[0]);
                    if (p.coeffs[0].equals(new NumberR64(0.5))) {
                        return new F(F.DIVIDE, new Element[]{new NumberR64(1), new F(F.TG, f.X[0])});
                    }
                    Polynom[] p_arr_divandrem = p.divAndRem(oneHalf, ring);
                    Polynom arg = null;
                    if (p.coeffs[0].equals(new NumberR64(1))) {
                        arg = ((Polynom) f.X[0]).multiply(oneHalf, ring);
                    } else {
                        arg = ((Polynom) f.X[0]).divideExact(oneHalf, ring);
                    }

                    NumberR64 two = new NumberR64(2);
                    F t1 = new F(F.TG, arg);
                    NumberR64 one = new NumberR64(1);
                    F t2 = new F(F.POW, new Element[]{t1, two});
                    F t3 = new F(F.SUBTRACT, new Element[]{one, t2});
                    F t4 = new F(F.MULTIPLY, new Element[]{two, t1});
                    return new F(F.DIVIDE, new Element[]{t3, t4});
                }
            }
        }
        return f;
    }

//4
    /**
     * На входе функция - вида : Tg[a], где a - произвольный аргумент типа
     * Polynom
     *
     * @param f - входная функция
     * @return - возвращает выражение через универсальную подстановку - tg(a/2)
     */
    public F rec_trigon_tg(F f, Ring ring) {

        if (f.name == F.TG) {
            if (f.X.length == 1) {
                if (f.X[0] instanceof Polynom) {
                    Polynom p = ((Polynom) f.X[0]);
                    if (p.coeffs[0].equals(new NumberR64(0.5))) {
                        return f;
                    }
                    Polynom[] p_arr_divandrem = p.divAndRem(oneHalf, ring);
                    Polynom arg = null;
                    if (p.coeffs[0].equals(new NumberR64(1))) {
                        arg = ((Polynom) f.X[0]).multiply(oneHalf, ring);//1/2*a
                    } else {
                        arg = ((Polynom) f.X[0]).divideExact(oneHalf, ring);//1/2*a
                    }

                    NumberR64 two = new NumberR64(2);//2
                    F t1 = new F(F.TG, arg);
                    NumberR64 one = new NumberR64(1);//1
                    F t2 = new F(F.POW, new Element[]{t1, two}); // tg^2(a/2)
                    F t3 = new F(F.SUBTRACT, new Element[]{one, t2}); // 1-tg^2(a/2)
                    F t4 = new F(F.MULTIPLY, new Element[]{two, t1}); //2tg(a/2)
                    return new F(F.DIVIDE, new Element[]{t4, t3});
                }
            }
        }
        return f;
    }

//5
    /**
     * На входе функция - вида : Cosec[a], где a - произвольный аргумент типа
     * Polynom
     *
     * @param f - входная функция
     * @return - возвращает выражение через универсальную подстановку - tg(a/2)
     */
    public F rec_trigon_cosec(F f, Ring ring) {

        if (f.name == F.CSC) {
            if (f.X.length == 1) {
                if (f.X[0] instanceof Polynom) {
                    Polynom p = ((Polynom) f.X[0]);
                    Polynom[] p_arr_divandrem = p.divAndRem(oneHalf, ring);
                    Polynom arg = null;
                    if (p.coeffs[0].equals(new NumberR64(1))) {
                        arg = ((Polynom) f.X[0]).multiply(oneHalf, ring);//1/2*a
                    } else {
                        arg = ((Polynom) f.X[0]).divideExact(oneHalf, ring);//1/2*a
                    }

                    NumberR64 two = new NumberR64(2);//2
                    F t1 = new F(F.TG, arg);
                    NumberR64 one = new NumberR64(1);//1
                    F t2 = new F(F.POW, new Element[]{t1, two}); //tg^2(a/2)
                    F t3 = new F(F.ADD, new Element[]{one, t2}); //1+tg^2(a/2)
                    F t4 = new F(F.SUBTRACT, new Element[]{one, t2}); // 1-tg^2(a/2)

                    return new F(F.DIVIDE, new Element[]{t3, t4});
                }
            }
        }
        return f;
    }

    //6
    /**
     * На входе функция - вида : Sec[a], где a - произвольный аргумент типа
     * Polynom
     *
     * @param f - входная функция
     * @return - возвращает выражение через универсальную подстановку - tg(a/2)
     */
    public F rec_trigon_sec(F f, Ring ring) {

        if (f.name == F.SC) {
            if (f.X.length == 1) {
                if (f.X[0] instanceof Polynom) {
                    Polynom p = ((Polynom) f.X[0]);
                    Polynom[] p_arr_divandrem = p.divAndRem(oneHalf, ring);
                    Polynom arg = null;
                    if (p.coeffs[0].equals(new NumberR64(1))) {
                        arg = ((Polynom) f.X[0]).multiply(oneHalf, ring);//1/2*a
                    } else {
                        arg = ((Polynom) f.X[0]).divideExact(oneHalf, ring);//1/2*a
                    }

                    NumberR64 two = new NumberR64(2);//2
                    F t1 = new F(F.TG, arg);
                    NumberR64 one = new NumberR64(1);//1
                    F t2 = new F(F.POW, new Element[]{t1, two});// tg^2(a/2)
                    F t3 = new F(F.ADD, new Element[]{one, t2}); //1+tg^2(a/2)
                    F t4 = new F(F.MULTIPLY, new Element[]{two, t1}); //2tg(a/2)
                    return new F(F.DIVIDE, new Element[]{t3, t4});
                }
            }
        }
        return f;
    }

    /**
     * метод, который анализирует функции и возвращает вызов соответствующего им
     * метода
     *
     * @param f - входная функция
     * @return вывод метода
     */
    public F trigTransform_tg_two_arg(F f, Ring ring) {
        if (f.name == F.SIN) {
            return rec_trigon_sin(f, ring);
        }
        if (f.name == F.COS) {
            return rec_trigon_cos(f, ring);
        }
        if (f.name == F.TG) {
            return rec_trigon_tg(f, ring);
        }
        if (f.name == F.CTG) {
            return rec_trigon_ctg(f, ring);
        }
        if (f.name == F.CSC) {
            return rec_trigon_cosec(f, ring);
        }
        if (f.name == F.SC) {
            return rec_trigon_sec(f, ring);
        }
        return f;
    }

//7
    /**
     * На входе функция - вида : Cosec[2a], где a - произвольный аргумент типа
     * Polynom
     *
     * @param f - входная функция
     * @return - возвращает выражение через универсальную подстановку - tg(a)
     */
    public F rec_trigon_cosec_2a(F f, Ring ring) {

        if (f.name == F.CSC) {
            if (f.X.length == 1) {
                if (f.X[0] instanceof Polynom) {
                    Polynom p = ((Polynom) f.X[0]);
                    Polynom arg = ((Polynom) f.X[0]).multiply(oneHalf, ring);

                    NumberR64 two = new NumberR64(2);//2
                    F t1 = new F(F.TG, arg);
                    NumberR64 one = new NumberR64(1);//1
                    F t2 = new F(F.POW, new Element[]{t1, two}); //tg^2(a/2)
                    F t3 = new F(F.ADD, new Element[]{one, t2}); //1+tg^2(a/2)
                    F t4 = new F(F.SUBTRACT, new Element[]{one, t2}); // 1-tg^2(a/2)

                    return new F(F.DIVIDE, new Element[]{t3, t4});
                }
            }
        }
        return f;
    }

//8
    /**
     * На входе функция - вида : Sec[2a], где a - произвольный аргумент типа
     * Polynom
     *
     * @param f - входная функция
     * @return - возвращает выражение через универсальную подстановку - tg(a)
     */
    public F rec_trigon_sec_2a(F f, Ring ring) {

        if (f.name == F.SC) {
            if (f.X.length == 1) {
                if (f.X[0] instanceof Polynom) {
                    Polynom p = ((Polynom) f.X[0]);
                    Polynom arg = ((Polynom) f.X[0]).multiply(oneHalf, ring);

                    NumberR64 two = new NumberR64(2);//2
                    F t1 = new F(F.TG, arg);
                    NumberR64 one = new NumberR64(1);//1
                    F t2 = new F(F.POW, new Element[]{t1, two});// tg^2(a/2)
                    F t3 = new F(F.ADD, new Element[]{one, t2}); //1+tg^2(a/2)
                    F t4 = new F(F.MULTIPLY, new Element[]{two, t1}); //2tg(a/2)
                    return new F(F.DIVIDE, new Element[]{t3, t4});
                }
            }
        }
        return f;
    }

//5
    /**
     * На входе функция - вида : Sin[2a], где a - произвольный аргумент типа
     * Polynom
     *
     * @param f - входная функция
     * @return - возвращает выражение через универсальную подстановку - tg(a)
     */
    public F rec_trigon_sin_2a(F f, Ring ring) {

        if (f.name == F.SIN) {
            if (f.X.length == 1) {
                if (f.X[0] instanceof Polynom) {
                    Polynom p = ((Polynom) f.X[0]);
                    Polynom[] p_arr_divandrem = p.divAndRem(oneHalf, ring);
                    Polynom arg = null;
                    if (p.coeffs[0].equals(new NumberR64(1))) {
                        arg = ((Polynom) f.X[0]).multiply(oneHalf, ring);
                    } else {
                        arg = ((Polynom) f.X[0]).divideExact(oneHalf, ring);
                    }

                    NumberR64 two = new NumberR64(2);
                    F t1 = new F(F.TG, arg);
                    NumberR64 one = new NumberR64(1);
                    F t2 = new F(F.POW, new Element[]{t1, two});
                    F t3 = new F(F.ADD, new Element[]{one, t2});
                    F t4 = new F(F.MULTIPLY, new Element[]{two, t1});
                    return new F(F.DIVIDE, new Element[]{t4, t3});
                }
            }
        }
        return f;
    }

//6
    /**
     * На входе функция - вида : Cos[2a], где a - произвольный аргумент типа
     * Polynom
     *
     * @param f - входная функция
     * @return - возвращает выражение через универсальную подстановку - tg(a)
     */
    public F rec_trigon_cos_2a(F f, Ring ring) {

        if (f.name == F.COS) {
            if (f.X.length == 1) {
                if (f.X[0] instanceof Polynom) {
                    Polynom p = ((Polynom) f.X[0]);
                    Polynom[] p_arr_divandrem = p.divAndRem(oneHalf, ring);
                    Polynom arg = null;
                    if (p.coeffs[0].equals(new NumberR64(1))) {
                        arg = ((Polynom) f.X[0]).multiply(oneHalf, ring);
                    } else {
                        arg = ((Polynom) f.X[0]).divideExact(oneHalf, ring);
                    }

                    NumberR64 two = new NumberR64(2);
                    F t1 = new F(F.TG, arg);
                    NumberR64 one = new NumberR64(1);
                    F t2 = new F(F.POW, new Element[]{t1, two});
                    F t3 = new F(F.ADD, new Element[]{one, t2});
                    F t4 = new F(F.SUBTRACT, new Element[]{one, t2});
                    return new F(F.DIVIDE, new Element[]{t4, t3});
                }
            }
        }
        return f;
    }

//7
    /**
     * На входе функция - вида : Ctg[2a], где a - произвольный аргумент типа
     * Polynom
     *
     * @param f - входная функция
     * @return - возвращает выражение через универсальную подстановку - tg(a)
     */
    public F rec_trigon_ctg_2a(F f, Ring ring) {

        if (f.name == F.CTG) {
            if (f.X.length == 1) {
                if (f.X[0] instanceof Polynom) {
                    Polynom p = ((Polynom) f.X[0]);
                    if (p.coeffs[0].equals(new NumberR64(0.5))) {
                        return new F(F.DIVIDE, new Element[]{new NumberR64(1), new F(F.TG, f.X[0])});
                    }
                    Polynom arg = ((Polynom) f.X[0]).multiply(oneHalf, ring);
                    NumberR64 two = new NumberR64(2);
                    F t1 = new F(F.TG, arg);
                    NumberR64 one = new NumberR64(1);
                    F t2 = new F(F.POW, new Element[]{t1, two});
                    F t3 = new F(F.SUBTRACT, new Element[]{one, t2});
                    F t4 = new F(F.MULTIPLY, new Element[]{two, t1});
                    return new F(F.DIVIDE, new Element[]{t3, t4});
                }
            }
        }
        return f;
    }

//8
    /**
     * На входе функция - вида : Tg[2a], где a - произвольный аргумент типа
     * Polynom
     *
     * @param f - входная функция
     * @return - возвращает выражение через универсальную подстановку - tg(a)
     */
    public F rec_trigon_tg_2a(F f, Ring ring) {

        if (f.name == F.TG) {
            if (f.X.length == 1) {
                if (f.X[0] instanceof Polynom) {
                    Polynom p = ((Polynom) f.X[0]);
                    if (p.coeffs[0].equals(new NumberR64(0.5))) {
                        return f;
                    }
                    Polynom[] p_arr_divandrem = p.divAndRem(oneHalf, ring);
                    Polynom arg = null;
                    if (p.coeffs[0].equals(new NumberR64(1))) {
                        arg = ((Polynom) f.X[0]).multiply(oneHalf, ring);
                    } else {
                        arg = ((Polynom) f.X[0]).divideExact(oneHalf, ring);
                    }

                    NumberR64 two = new NumberR64(2);//2
                    F t1 = new F(F.TG, arg);
                    NumberR64 one = new NumberR64(1);//1
                    F t2 = new F(F.POW, new Element[]{t1, two}); // tg^2(a/2)
                    F t3 = new F(F.SUBTRACT, new Element[]{one, t2}); // 1-tg^2(a/2)
                    F t4 = new F(F.MULTIPLY, new Element[]{two, t1}); //2tg(a/2)
                    return new F(F.DIVIDE, new Element[]{t4, t3});
                }
            }
        }
        return f;
    }

    /**
     * метод, который анализирует функции двойного аргумента и возвращает вызов
     * соответствующего им метода
     *
     * @param f - входная функция
     * @return вывод метода
     */
    public F trigTransform_tg_two_arg2(F f, Ring ring) {
        if (f.name == F.CSC) {
            return rec_trigon_cosec_2a(f, ring);
        }
        if (f.name == F.SC) {
            return rec_trigon_sec_2a(f, ring);
        }
        if (f.name == F.SIN) {
            return rec_trigon_sin_2a(f, ring);
        }
        if (f.name == F.COS) {
            return rec_trigon_cos_2a(f, ring);
        }
        if (f.name == F.TG) {
            return rec_trigon_tg_2a(f, ring);
        }
        if (f.name == F.CTG) {
            return rec_trigon_ctg_2a(f, ring);
        }
        return f;
    }

    /**
     * процедура замены функции, содержащей в своих компонентах
     * тригонометрические функции sin, cos, tg, ctg, sec, cosec эквивалентной ей
     * функцией с заменой на её представление через tg ;
     *
     * @param f - входная функция;
     * @return - возврвщает функциюб где вместо указанной функции стоит её
     * выражение через tg;
     */
    public Element transform_func(Element f, Ring ring) {
        if (f instanceof F) {
            for (int i = 0; i < ((F) f).X.length; i++) {
                if (((F) f).X[i] instanceof F) {
                    if (((F) ((F) f).X[i]).name < F.FIRST_INFIX_NAME) {
                        int n = ((F) ((F) f).X[i]).name;
                        if (n == F.SIN || n == F.COS || n == F.TG || n == F.CTG || n == F.CSC || n == F.SC) {
                            if (((F) ((F) ((F) f).X[i])).X.length == 1) {
                                if (((F) ((F) ((F) f).X[i])).X[0] instanceof Polynom) {
                                    Polynom res = ((Polynom) ((F) ((F) ((F) f).X[i])).X[0]);
                                    Element[] arr1 = res.coeffs[0].divideAndRemainder(new NumberR64(2), ring);
                                    Element[] arr2 = res.coeffs[0].divideAndRemainder(new NumberR64(0.5), ring);
                                    if (arr1[0].isZero(ring)) {
                                        ((F) f).X[i] = new ExpressionTrigonFuncForTg().trigTransform_tg_two_arg2(((F) ((F) f).X[i]), ring);
                                    }
                                    if (res.coeffs[0].isOne(ring)) {
                                        ((F) f).X[i] = new ExpressionTrigonFuncForTg().trigTransform_tg_two_arg(((F) ((F) f).X[i]), ring);
                                    }
                                    if (arr2[0].isZero(ring)) {
                                        if (((F) ((F) f).X[i]).name == TG) {
                                            ((F) f).X[i] = ((F) f).X[i];
                                        }
                                    }
                                    if (arr2[0].isZero(ring)) {
                                        if (((F) ((F) f).X[i]).name == CTG) {
                                            ((F) f).X[i] = new F(F.DIVIDE, new Element[]{new NumberR64(1), new F(F.TG, ((F) ((F) f).X[i]).X[0])});
                                        }
                                    }
                                }
                            }
                        } else {
                            transform_func(((F) ((F) f).X[i]), ring);
                        }
                    } else {
                        transform_func(((F) ((F) f).X[i]), ring);
                    }
                } else {
                    if (((F) f).X[i] instanceof Polynom) {
                        ((F) f).X[i] = ((F) f).X[i];
                    } else {
                        ((F) f).X[i] = ((F) f).X[i];
                    }
                }
            }
        }
        return f;
    }

    /**
     * На входе функция вида ALGEBRAIC_EQUATION[A,B] Возвращает результат
     * сравнения преобразованных A и B
     */
    @Override
    public boolean equals(F f, Ring ring) {
        F f1 = ((F) (f.X[0])).expand(ring );
        F f2 = ((F) (f.X[1])).expand(ring );
        return (f1.equals(f2)) ? true : false;
    }
}
