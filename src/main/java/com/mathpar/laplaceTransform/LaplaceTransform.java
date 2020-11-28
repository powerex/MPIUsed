/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform;

import com.mathpar.func.F;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 * Класс для прямого преобразования Лапласа
 *
 * @author Рыбаков Михаил Анатольевич, email: mixail08101987@mail.ru
 * @version 1.0
 * @year 2012
 */
public class LaplaceTransform {

    /**
     * Функция для преобразования
     */
    private F inputF;

    public LaplaceTransform() {
    }

    /**
     * Конструктор
     *
     * @param f - Функция для преобразования
     */
    public LaplaceTransform(F f) {
        this.inputF = f;
    }

    /**
     * Метод преобразования полинома в степени n по формуле:
     * a0+a1*t+a2*t^2+a3*t^3+......an*t^n a*t^[n] => a * n! * s^[-n-1]
     *
     * @return - Выражение вида: a * n! * s^[-n-1]
     */
    public F transformPolynom() {
        Ring ring = new Ring("R64[p]");
        Polynom arg1 = ((Polynom) inputF.X[0]);
        F result = null;
        if (arg1.coeffs.length == 1) {
            int h = 0;
            for (int i = 0; i < arg1.powers.length; i++) {
                if (arg1.powers[i] != 0) {
                    h = arg1.powers[i];
                }
            }
            double n = fact(arg1.powers[0]); //n!
            NumberR64 k = (NumberR64) (new NumberR64(n)).multiply(arg1.coeffs[0], ring); //a*n!
            return result = new F(F.DIVIDE, new Element[]{Polynom.polynomFromNumber(k, ring), new Polynom(new int[]{h + 1}, new Element[]{Complex.C64_ONE})}); //a*n!*s^[-n-1]
        }
        if (arg1.powers.length == 1) { //один моном
            Polynom arg2 = new Polynom("p", ring);
            double n = fact(arg1.powers[0]); //n!
            NumberR64 k = ((NumberR64) arg1.coeffs[0]).multiply(new NumberR64(n));
            result = new F(F.MULTIPLY, new F[]{new F().FfromNumber(k),
                        new F(F.POW, new F[]{new F(arg2),
                            new F().FfromNumber(new NumberR64(-arg1.powers[0]
                            - 1))})}); //a*n!*s^[-n-1]
        } else { //не один моном
            Polynom arg3 = new Polynom("p", ring); //s
            F[] f = new F[arg1.powers.length];
            for (int i = 0; i < arg1.powers.length; i++) {
                double n = fact(arg1.powers[i]);
                NumberR64 k = (NumberR64) (new NumberR64(n)).multiply(arg1.coeffs[i], ring);//коэф домножения
                NumberR64 factor = new NumberR64(fact(arg1.powers[i]));
                Element el1 = new Polynom(new int[0], new Element[]{k.multiply(factor)});//k*(n!)
                Element el2 = new Polynom(new int[]{arg1.powers[i] + 1}, new Element[]{ring.numberONE});//p^[n+1]
                f[i] = new F(F.DIVIDE, new Element[]{el1, el2});
                // f[i] = new F(F.MULTIPLY, new F[]{FfromNumber(k),
                //           new F(F.POW, new F[]{new F(arg3),
                //             FfromNumber(new NumberR64(-arg1.powers[i]
                //           - 1))})}); //1/s^[n+1]
            }
            result = new F(F.ADD, f);
        }
        return result;
    }

    /**
     * Метод преобразования любого коэффициента
     *
     * @return
     */
    public F transformNumber() {
        Ring ring = new Ring("C64[p]");
        F result = null;
        if (inputF.X[0] instanceof Polynom) {
            Polynom arg1 = ((Polynom) inputF.X[0]);
            if (arg1.powers.length == 1) { //один моном
                Polynom arg2 = new Polynom("p", ring);
                double n = fact(arg1.powers[0]); //n!
                //NumberR64 k = ((NumberR64) arg1.coeffs[0]).multiply(new NumberR64(n));
                Element k = arg1.coeffs[0].multiply(new NumberR64(n), ring);
                result = new F(F.DIVIDE, new Element[]{k, new Polynom(new int[]{arg1.powers[0] + 1}, new Element[]{new Complex(1)})});
                //result = new F(F.MULTIPLY, new Element[]{k,
                //          new F(intF.POW, new Element[]{arg2, new NumberR64(-arg1.powers[0] - 1)})}); //a*n!*s^[-n-1]
            } else { //коэффициент//Ring.oneOfMainRing()
                Polynom pp = new Polynom("p", ring);
                //Polynom.polynom_one(ring.numberONE)
                result = new F(F.DIVIDE, new Element[]{arg1, pp});//coeff/s
            }
        } else {
            Polynom pp = new Polynom("p", ring);
            //Polynom.polynom_one(ring.numberONE)
            result = new F(F.DIVIDE, new Element[]{Polynom.polynomFromNumber(inputF.X[0], ring), pp});//coeff/s
        }
        return result;
    }

    /**
     * Метод преобразование n-го порядка с частотным сдвигом по формуле:
     * b*t^[n]exp[at] => b*n!*(-a+s)^[-1-n]
     *
     * @return Выражение вида: b*n!*(-a+s)^[-1-n];
     */
    public F transformPolMultiplyExp(Ring ring) {
        Polynom arg1 = ((Polynom) inputF.X[0]); //степень
        F arg_exp = ((F) inputF.X[1]); //аргумент exp
        Polynom arg2 = ((Polynom) arg_exp.X[0]);
        NumberR64 k = ((NumberR64) arg2.coeffs[0]); //a
        double n = fact(arg1.powers[0]); //n!
        NumberR64 n_k = new NumberR64(n); //n!
        Polynom p1 = new Polynom(new int[]{0}, new Element[]{new Complex(n_k.multiply(((NumberR64) arg1.coeffs[0])).doubleValue())});//a*n!
        Polynom p2 = new Polynom(new int[]{1, 0}, new Element[]{new Complex(1), new Complex(k.doubleValue()).negate(ring)});
        Element p3 = new Complex(arg1.powers[0] + 1);
        F pow = new F(F.intPOW, new Element[]{p2, p3});
        return new F(F.DIVIDE, new Element[]{p1, pow}); //a*n!*(s+a)^[-n-1]
    }

    /**
     * Метод преобразования "экспоненциальное затухание" по формуле: a*exp[bt]
     * => a/(+-b+s)
     *
     * @return - Выражение вида: a/(+-b+s)
     */
    public F transformExp() {
        Ring ring = new Ring("C64[p]");//
        if (inputF.name == F.EXP) {
            Polynom arg1 = ((Polynom) inputF.X[0]); //аргумент
            Element a = arg1.coeffs[0];
            Polynom a_p = new Polynom(new int[]{0},
                    new Element[]{new Complex(a.negate(ring).
                        doubleValue())}); //-a
            Polynom arg2 = new Polynom("p", ring);
            Polynom add = a_p.add(arg2, ring);
            Polynom one = Polynom.polynom_one(ring.numberONE);//1
            F result = new F(F.DIVIDE, new Element[]{one, add}); //1/(-a+s)
            return result;
        } else {
            Element a = null;
            Element b = null;
            if (inputF.name == F.MULTIPLY) { //случай произведения коэффициента на функцию
                if (inputF.X.length == 2) {
                    for (int i = 0; i < inputF.X.length; i++) {
                        if (inputF.X[i] instanceof F) {
                            if (((F) inputF.X[i]).name == F.EXP) {
                                b = ((Polynom) ((F) inputF.X[i]).X[0]).coeffs[0];
                            }
                        } else {
                            if (!(inputF.X[i] instanceof Polynom)) {
                                a = inputF.X[i];
                            } else {
                                a = ((Polynom) inputF.X[i]).coeffs[0];
                            }
                        }
                    }
                }
            }
            Polynom a_p = new Polynom(new int[]{0},
                    new Element[]{new Complex(b.negate(ring).
                        doubleValue())}); //-a
            Polynom arg2 = new Polynom(new int[]{1}, new Element[]{ring.numberONE});
            Polynom add = a_p.add(arg2, ring);
            Polynom one = Polynom.polynom_one(ring.numberONE);//1
            F result = new F(F.DIVIDE, new Element[]{one.multiply(a, ring), add}); //1/(-a+s)
            return result;
        }
    }

    /**
     * Метод преобразования "экспоненциальное затухание" по формуле: k*exp[at]
     * => k/(-a+s)
     *
     * @return - Выражение вида: k/(-a+s)
     */
    public F transformNumberMultiplyExp() {
        Ring ring = new Ring("C64[p]");
        F result = null;
        if (inputF.name == F.MULTIPLY) {
            if (inputF.X[0] instanceof Polynom) {
                Polynom arg1 = ((Polynom) ((F) inputF.X[1]).X[0]); //аргумент
                Element a = arg1.coeffs[0];
                Polynom a_p = new Polynom(new int[]{},
                        new Element[]{new Complex(a.negate(ring).doubleValue(), 0.0)}); //-a
                Polynom arg2 = new Polynom("p", ring);
                Polynom add = a_p.add(arg2, ring);
                Polynom one = ((Polynom) inputF.X[0]);// Polynom.polynom_one(NumberC64.ONE);//1
                for (int i = 0; i < one.coeffs.length; i++) {
                    one.coeffs[i] = one.coeffs[i].toNumber(Ring.C64, ring);
                }
                result = new F(F.DIVIDE, new Element[]{one, add}); //1/(-a+s)
            } else {
                Polynom arg1 = null;
                if (!(inputF.X[0] instanceof F)) {
                    arg1 = new Polynom(new int[]{}, new Element[]{inputF.X[0]});
                } else {
                    arg1 = ((Polynom) ((F) inputF.X[0]).X[0]);
                } //аргумент
                Element k = arg1.coeffs[0];
                Element a = ((Polynom) ((F) inputF.X[1]).X[0]).coeffs[0];
                Polynom a_p = new Polynom(new int[]{},
                        new Element[]{new Complex(a.negate(ring).
                            doubleValue())}); //-a
                Polynom arg2 = new Polynom("p", ring);
                Polynom add = a_p.add(arg2, ring);
//                Polynom one =  Polynom.polynom_one(new Complex(NumberR64.ONE,ring));
//                        //((Polynom) this.X[1]);// Polynom.polynom_one(NumberC64.ONE);//
//                for (int i = 0; i < one.coeffs.length; i++) {
//                    one.coeffs[i] = one.coeffs[i].toNumber(Ring.C64,ring);
//                }
                result = new F(F.DIVIDE, new Element[]{k, add}); //k/(-a+s)
            }
        }
        return result;
    }

    /**
     * Метод преобразования "экспоненциальное приближение" по формуле:
     * 1-exp[-at] => a/s*(s+a)
     *
     * @return - Выражение вида: a/s*(s+a)
     */
    public F transformExp_(Ring ring) {
        Polynom arg1 = ((Polynom) inputF.X[1]); //аргумент
        Polynom arg2 = new Polynom("p", ring);
        Polynom add = arg2.add(arg1, ring);
        return new F(F.DIVIDE, new F[]{new F(arg1),
                    new F(F.MULTIPLY, new F[]{new F(arg2),
                        new F(add)})}); //a/s(s+a)
    }

    /**
     * Метод преобразования по формуле: sin[wt]^n => w^n*n!/()
     *
     * @return - Выражение вида: w^n*n!/()
     */
    public Element transformSinPowN(Ring ring) {
        if (inputF.name == F.intPOW) {
            int n = 0;
            int pos = 0;
            for (int i = 0; i < 2; i++) {
                if (!(inputF.X[i] instanceof F) && !(inputF.X[i] instanceof Polynom)) {
                    n = inputF.X[i].intValue();
                } else {
                    pos = i;
                }
            }
            Element a = ((Polynom) ((F) inputF.X[0]).X[0]).coeffs[0];//w
            Element nn = new Complex(fact(n));//n!
            Element aa = a.pow(n, ring);//a^n
            Polynom s = new Polynom(new int[]{0, 0, 1}, new Element[]{ring.numberONE});
            if (n % 2 == 0) {
                for (int i = 1; i <= n; i++) {
                    if (i % 2 == 0) {
                        s = s.multiply(new Polynom(new int[]{0, 0, 2, 0, 0, 0},
                                new Element[]{ring.numberONE,
                                    new Complex(i * i).multiply(new Complex(a.multiply(a, ring), ring), ring)}), ring);
                    }
                }
                return new Fraction(nn.multiply(new Complex(aa, ring), ring), s);
            }
            Polynom ppp = Polynom.polynom_one(ring.numberONE);
            if (n % 2 != 0) {
                for (int i = 1; i <= n; i++) {
                    if (i % 2 != 0) {
                        ppp = ppp.multiply(new Polynom(new int[]{0, 0, 2, 0, 0, 0},
                                new Element[]{ring.numberONE,
                                    new Complex(i * i).multiply(new Complex(a.multiply(a, ring), ring), ring)}), ring);
                    }
                }
                return new Fraction(nn.multiply(new Complex(aa, ring), ring), ppp);
            }
        }
        return null;
    }

    /**
     * Метод преобразования по формуле: a*sin[wt] => (w*a)/(s^2+w^2);
     *
     * @return - Выражение вида: (w*a)/(s^2+w^2)
     */
    public F transformSin(Ring ring) {
        if (inputF.name == F.SIN) {
            Polynom arg1 = ((Polynom) inputF.X[0]); //аргумент
            Polynom add = new Polynom(new int[]{2, 0}, new Element[]{new Complex(1.0), new Complex(arg1.coeffs[0].pow(2, ring).doubleValue())}); //s^2+w^2
            return new F(F.DIVIDE, new Element[]{new Polynom(new int[]{0}, new Element[]{new Complex(arg1.coeffs[0].doubleValue())}), add}); //w/(s^[2]+w^[2])
        } else {
            Element a = null;
            Polynom w = new Polynom();
            if (inputF.name == F.MULTIPLY) { //случай произведения коэффициента на функцию
                if (inputF.X.length == 2) {
                    for (int i = 0; i < inputF.X.length; i++) {
                        if (inputF.X[i] instanceof F) {
                            if (((F) inputF.X[i]).name == F.SIN) {
                                w = ((Polynom) ((F) ((F) inputF.X[i]).X[0]).X[0]);
                            }
                        } else {
                            a = ((Polynom) inputF.X[i]).coeffs[0];
                        }
                    }
                }
            }
            Polynom add = new Polynom(new int[]{2, 0}, new Element[]{new Complex(1.0), new Complex(w.coeffs[0].pow(2, ring).doubleValue())}); //s^2+w^2
            return new F(F.DIVIDE, new Element[]{new Polynom(new int[]{0}, new Element[]{new Complex(a.multiply(w.coeffs[0], ring).doubleValue())}), add}); //a*w/(s^[2]+w^[2])
        }
    }

    /**
     * Метод преобразования по формуле: cos[wt] => s/(s^2+w^2)
     *
     * @return - Выражение вида: s/(s^2+w^2)
     */
    public F transformCos(Ring ring) {
        Polynom arg1 = ((Polynom) inputF.X[0]); //аргумент
        Polynom add = new Polynom(new int[]{2, 0}, new Element[]{new Complex(1.0), new Complex(arg1.coeffs[0].pow(2, ring).doubleValue())}); //s^2+w^2
        Polynom p = new Polynom(new int[]{1}, new Element[]{new Complex(1.0)});//s
        return new F(F.DIVIDE, new Element[]{p, add}); //s/(s^[2]+w^[2])
    }

    /**
     * Метод преобразования по формуле: sh[wt] => w/(s^[2]-w^[2])
     *
     * @return - Выражение вида: w/(s^[2]-w^[2])
     */
    public F transformSh(Ring ring) {
        Polynom arg1 = ((Polynom) inputF.X[0]); //аргумент
        Polynom p1 = new Polynom(new int[]{2, 0}, new Element[]{new Complex(1.0), new Complex(arg1.coeffs[0].pow(2, ring).negate(ring).doubleValue())}); //s^2-w^2
        return new F(F.DIVIDE, new Element[]{new Polynom(new int[]{0}, new Element[]{new Complex(arg1.coeffs[0].doubleValue())}), p1}); //w/(s^[2]-w^[2])
    }

    /**
     * Метод преобразования по формуле: ch[wt] => s/(s^[2]-w^[2])
     *
     * @return - Выражение вида: s/(s^[2]-w^[2])
     */
    public F transformCh(Ring ring) {
        Polynom arg1 = ((Polynom) inputF.X[0]); //аргумент
        Polynom p2 = new Polynom(new int[]{2, 0}, new Element[]{new Complex(1.0), new Complex(arg1.coeffs[0].pow(2, ring).negate(ring).doubleValue())}); //s^2-w^2
        Polynom p1 = new Polynom(new int[]{1}, new Element[]{new Complex(1.0)});//s
        return new F(F.DIVIDE, new Element[]{p1, p2}); //s/(s^[2]-w^[2])
    }

    /**
     * Метод преобразования по формуле: exp[at]sin[wt] => w/(s+a)^2+w^2
     *
     * @return - Выражение вида: w/(s+a)^2+w^2
     */
    public F transformExpMultiplySin(Ring ring) {
        F f1 = ((F) inputF.X[0]);//exp[at]
        F f2 = ((F) inputF.X[1]);//sin[wt]
        Polynom arg1 = ((Polynom) f1.X[0]); //аргумент a
        Polynom arg2 = ((Polynom) f2.X[0]); //аргумент w
        Polynom p1 = new Polynom(new int[]{1, 0}, new Element[]{new Complex(1.0), new Complex(arg1.coeffs[0].negate(ring).doubleValue())});//s+a
        F pow = new F(F.POW, new Element[]{p1, new F().FfromNumber(ring.numberONE().add(ring.numberONE(), ring))}); //(s+a)^2
        Polynom p2 = new Polynom(new int[]{0}, new Element[]{new Complex(arg2.coeffs[0].doubleValue() * arg2.coeffs[0].doubleValue())}); //w^2
        F zn = new F(F.ADD, new F[]{pow, new F(p2)}); //(s+a)^2+w^2
        return new F(F.DIVIDE, new F[]{new F(new Polynom(new int[]{0}, new Element[]{new Complex(arg2.coeffs[0].doubleValue())})), zn}); //w/(s+a)^2+w^2
    }

    /**
     * Метод преобразования по формуле: exp[at]cos[wt] => (s+a)/(s+a)^2+w^2
     *
     * @return - Выражение вида: (s+a)/(s+a)^2+w^2
     */
    public F transformExpMultiplyCos(Ring ring) {
        F f1 = ((F) inputF.X[0]);//exp[at]
        F f2 = ((F) inputF.X[1]);//cos[wt]
        Polynom arg1 = ((Polynom) f1.X[0]); //аргумент a
        Polynom arg2 = ((Polynom) f2.X[0]); //аргумент w
        Polynom p1 = new Polynom(new int[]{1, 0}, new Element[]{new Complex(1.0), new Complex(arg1.coeffs[0].negate(ring).doubleValue())}); //s-a
        F pow = new F(F.POW, new Element[]{p1,
                    new F().FfromNumber(ring.numberONE().add(ring.numberONE(), ring))}); //(s-a)^2
        Polynom p2 = new Polynom(new int[]{0}, new Element[]{new Complex(arg2.coeffs[0].doubleValue() * arg2.coeffs[0].doubleValue())}); //w^2
        F zn = new F(F.ADD, new F[]{pow, new F(p2)}); //(s-a)^2+w^2
        return new F(F.DIVIDE, new F[]{new F(p1), zn}); //(s-a)/(s-a)^2+w^2
    }

    /**
     * Метод преобразования по формуле: at^[n]sin[bt] =>
     * (a*n!*(1/2)*[(-is+b)*((s+ib)^n)+(is+b)*((s-ib)^n)]) / ((s^2+b^2)^[n+1])
     *
     * @return - Выражение вида:
     * (a*n!*(1/2)*[(-is+b)*((s+ib)^n)+(is+b)*((s-ib)^n)]) / ((s^2+b^2)^[n+1])
     */
    public F transformPolMultiplySin() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[0]);
        F f2 = ((F) inputF.X[1]);
        Polynom arg1 = ((Polynom) f1.X[0]); //аргумент при t
        double n = fact(arg1.powers[0]); //n!
        Complex n_k = new Complex(-arg1.powers[0]);
        Complex k1 = new Complex(n);
        Complex k2 = new Complex(-0.5);
        Complex k3 = (Complex)(k1.multiply(k2, ring)).multiply(new Complex(arg1.coeffs[0].doubleValue()), ring);
        F mn = new F().FfromNumber(k3); //множитель
        Polynom arg2 = ((Polynom) f2.X[0]); //аргумент при sin
        Element b = arg2.coeffs[0];//b
        Polynom p1 = new Polynom(new int[]{1, 0}, new Element[]{new Complex(0.0, -1.0), new Complex(b.doubleValue())});//-is+b
        Polynom p2 = (Polynom) new Polynom(new int[]{1, 0}, new Element[]{new Complex(1.0), new Complex(0.0, b.doubleValue())}).pow(arg1.powers[0], ring);//(s+ib)^n
        Polynom p3 = new Polynom(new int[]{1, 0}, new Element[]{new Complex(0.0, 1.0), new Complex(b.doubleValue())});//is+b
        Polynom p4 = (Polynom) new Polynom(new int[]{1, 0}, new Element[]{new Complex(1.0), new Complex(0.0, b.doubleValue()).negate(ring)}).pow(arg1.powers[0], ring);//(s-ib)^n
        Polynom muls1 = p1.multiply(p2, ring);//(-is+b)*((s+ib)^n)
        Polynom muls2 = p3.multiply(p4, ring);//(is+b)*((s-ib)^n)
        Polynom add = muls1.add(muls2, ring);//(-is+b)*((s+ib)^n)+(is+b)*((s-ib)^n)
        Polynom p5 = new Polynom(new int[]{2, 0}, new Element[]{new Complex(1.0), new Complex(b.doubleValue() * b.doubleValue())});//s^2+b^2
        F zn = new F(F.POW, new Element[]{p5, new F().FfromNumber(new Complex(arg1.powers[0] + 1))});//(s^2+b^2)^[n+1]
        F ch = new F(F.MULTIPLY, new Element[]{mn, add}); //a*n!*(1/2)*[(-is+b)*((s+ib)^n)+(is+b)*((s-ib)^n)]
        return new F(F.DIVIDE, new F[]{ch, zn});//(a*n!*(1/2)*[(-is+b)*((s+ib)^n)+(is+b)*((s-ib)^n)]) / ((s^2+b^2)^[n+1])
    }

    /**
     * Метод преобразования по формуле: at^[n]cos[bt] =
     * a*n!*(1/2)*[(s+ib)^[n+1]+(s-ib)^[n+1]] / [(s^2+b^2)^[n+1]]
     *
     * @return - Выражение вида: a*n!*(1/2)*[(s+ib)^[n+1]+(s-ib)^[n+1]] /
     * [(s^2+b^2)^[n+1]]
     */
    public F transformPolMultiplyCos() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[0]);
        F f2 = ((F) inputF.X[1]);
        Polynom arg1 = ((Polynom) f1.X[0]); //аргумент при t
        double n = fact(arg1.powers[0]); //n!
        Complex n_k = new Complex(-arg1.powers[0] - 1);
        Complex k1 = new Complex(n);
        Complex k2 = new Complex(0.5);
        Complex k3 = (Complex)(k1.multiply(k2, ring)).multiply(new Complex(arg1.coeffs[0].doubleValue()), ring);
        F mn = new F().FfromNumber(k3); //множитель
        Polynom arg2 = ((Polynom) f2.X[0]); //аргумент при sin
        Element b = arg2.coeffs[0];//b
        Polynom p1 = (Polynom) new Polynom(new int[]{1, 0}, new Element[]{new Complex(1.0), new Complex(0.0, b.doubleValue())}).pow(arg1.powers[0] + 1, ring);//(s+ib)^[n+1]
        Polynom p2 = (Polynom) new Polynom(new int[]{1, 0}, new Element[]{new Complex(1.0), new Complex(0.0, b.doubleValue()).negate(ring)}).pow(arg1.powers[0] + 1, ring);//(s-ib)^[n+1]
        Polynom add = p1.add(p2, ring);//(s+ib)^[n+1]+(s-ib)^[n+1]
        Polynom p3 = new Polynom(new int[]{2, 0}, new Element[]{new Complex(1.0), new Complex(b.doubleValue() * b.doubleValue())});//s^2+b^2
        F zn = new F(F.POW, new Element[]{p3, new F().FfromNumber(new Complex(arg1.powers[0] + 1))});//(s^2+b^2)^[n+1]
        F ch = new F(F.MULTIPLY, new Element[]{mn, add}); //a*n!*(1/2)*[(s+ib)^[n+1]+(s-ib)^[n+1]]
        return new F(F.DIVIDE, new F[]{ch, zn});//a*n!*(1/2)*[(s+ib)^[n+1]+(s-ib)^[n+1]] / [(s^2+b^2)^[n+1]]
    }

    /**
     * Метод преобразования по формуле: at^[n]exp[bt]sin[ct] =>
     * -1/2i*a*n!((-b-ic+s)^[-n-1] - (-b+ic+s)^[-n-1])
     *
     * @return - Выражение вида: -1/2i*a*n!((-b-ic+s)^[-n-1] - (-b+ic+s)^[-n-1])
     */
    public F transformMultiplyPolExpSin() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[0]);
        F f2 = ((F) inputF.X[1]);
        F f3 = ((F) inputF.X[2]);
        Polynom arg1 = ((Polynom) f1.X[0]); //аргумент при t
        double n = fact(arg1.powers[0]);
        Complex k1 = new Complex(n / 2); //n!/2
        Complex k2 = (Complex)k1.multiply(new Complex(arg1.coeffs[0].doubleValue()), ring); //a*[n!/2]
        F mn = new F().FfromNumber(k2); //множитель
        Polynom arg2 = ((Polynom) f2.X[0]); //аргумент при exp
        Polynom arg3 = ((Polynom) f3.X[0]); //аргумент при sin
        Element b = arg2.coeffs[0];//b
        Element c = arg3.coeffs[0];//c
        Polynom p1 = (Polynom) new Polynom(new int[]{1, 0}, new Element[]{new Complex(1.0), new Complex(b.negate(ring).doubleValue(), c.negate(ring).doubleValue())}).pow(arg1.powers[0] + 1, ring);//(s-b-ic)^[n+1]
        Polynom p2 = (Polynom) new Polynom(new int[]{1, 0}, new Element[]{new Complex(1.0), new Complex(b.negate(ring).doubleValue(), c.doubleValue())}).pow(arg1.powers[0] + 1, ring);//(s-b+ic)^[n+1]
        Polynom sub = (Polynom) (p2.subtract(p1, ring)).multiply(new Complex(0.0, -1.0), ring);//[(s-b+ic)^[n+1]-(s-b-ic)^[n+1]]*[-i]
        Polynom p3 = new Polynom(new int[]{2, 1, 0}, new Element[]{new Complex(1), new Complex(b.negate(ring).doubleValue() * 2), new Complex(b.doubleValue() * b.doubleValue() + c.doubleValue() * c.doubleValue())});//s^2-2bs+b^2+c^2
        F zn = new F(F.POW, new Element[]{p3, new F().FfromNumber(new Complex(arg1.powers[0] + 1))});//(s^2-2bs+b^2+c^2)^[n+1]
        F ch = new F(F.MULTIPLY, new Element[]{mn, sub}); //a*[n!/2]*[[(s-b+ic)^[n+1]-(s-b-ic)^[n+1]]*[-i]]
        return new F(F.DIVIDE, new F[]{ch, zn});
    }

    /**
     * Метод преобразования по формуле: at^[n]exp[bt]cos[ct] =>
     * 1/2*a*n!((-b-ic+s)^[-n-1] + (-b+ic+s)^[-n-1])
     *
     * @return - Выражение вида: 1/2*a*n!((-b-ic+s)^[-n-1] + (-b+ic+s)^[-n-1])
     */
    public F transformMultiplyPolExpCos() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[0]);
        F f2 = ((F) inputF.X[1]);
        F f3 = ((F) inputF.X[2]);
        Polynom arg1 = ((Polynom) f1.X[0]); //аргумент при t
        double n = fact(arg1.powers[0]);
        Complex n_k = new Complex(-arg1.powers[0] - 1); //-n-1
        Complex k1 = new Complex(n / 2); //n!/2
        Complex k2 = (Complex)(k1).multiply(new Complex(arg1.coeffs[0].doubleValue()), ring); //a*n!/2
        F mn = new F().FfromNumber(k2); //множитель
        Polynom arg2 = ((Polynom) f2.X[0]); //аргумент при exp
        Polynom arg3 = ((Polynom) f3.X[0]); //аргумент при cos
        Element b = arg2.coeffs[0];//b
        Element c = arg3.coeffs[0];//c
        Polynom p1 = (Polynom) new Polynom(new int[]{1, 0}, new Element[]{new Complex(1.0), new Complex(b.negate(ring).doubleValue(), c.negate(ring).doubleValue())}).pow(arg1.powers[0] + 1, ring);//(s-b-ic)^[n+1]
        Polynom p2 = (Polynom) new Polynom(new int[]{1, 0}, new Element[]{new Complex(1.0), new Complex(b.negate(ring).doubleValue(), c.doubleValue())}).pow(arg1.powers[0] + 1, ring);//(s-b+ic)^[n+1]
        Polynom add = (p2.add(p1, ring));//[(s-b+ic)^[n+1]-(s-b-ic)^[n+1]]
        Polynom p3 = new Polynom(new int[]{2, 1, 0}, new Element[]{new Complex(1.0), new Complex(b.negate(ring).doubleValue() * 2), new Complex(b.doubleValue() * b.doubleValue() + c.doubleValue() * c.doubleValue())});//s^2-2bs+b^2+c^2
        F zn = new F(F.POW, new Element[]{p3, new F().FfromNumber(new Complex(arg1.powers[0] + 1))});//(s^2-2bs+b^2+c^2)^[n+1]
        F ch = new F(F.MULTIPLY, new Element[]{mn, add}); //a*[n!/2]*[[(s-b+ic)^[n+1]-(s-b-ic)^[n+1]]]
        return new F(F.DIVIDE, new F[]{ch, zn});//a*[n!/2]*[[(s-b+ic)^[n+1]-(s-b-ic)^[n+1]]] / [s^2-2bs+b^2+c^2]
    }

    /**
     * Метод преобразования выражения вида: t^[n]*UnitStep[a*t], если a > 0 то
     * преобрзование t^[n], иначе 0
     *
     * @return - Выражение вида: LPT[t^[n]] || 0
     */
    public F transformPolMultiplyUnitStep() {
        Polynom arg = ((Polynom) inputF.X[1]);
        if (arg.coeffs[0].signum() == 1) {
            return this.transformPolynom();
        }
        return null;
    }

    /**
     * Метод преобразования выражения вида: exp[a*t]*UnitStep[b*t], если b>0 то
     * преобразование exp[a*t], иначе 0
     *
     * @return - Выражение вида: LPT[exp[a*t]] || 0
     */
    public F transformExpMultiplyUnitStep() {
        F f1 = ((F) inputF.X[1]);
        Polynom arg = ((Polynom) f1.X[0]);
        if (arg.coeffs[0].signum() == 1) {
            return new LaplaceTransform(((F) inputF.X[0])).transformExp();
        }
        return null;
    }

    /**
     * Метод преобразования выражения вида: sin[a*t]*UnitStep[b*t], если b>0 то
     * преобразование sin[a*t], иначе 0
     *
     * @return - Выражение вида: LPT[sin[a*t]] || 0
     */
    public F transformSinMultiplyUnitStep() {
        Ring ring = new Ring("C64[p]");
        Polynom arg = ((Polynom) inputF.X[1]);
        if (arg.coeffs[0].signum() == 1) {
            return this.transformSin(ring);
        }
        return null;
    }

    /**
     * Метод преобразования выражения вида: cos[a*t]*UnitStep[b*t], если b>0 то
     * преобразование cos[a*t], иначе 0
     *
     * @return - Выражение вида: LPT[cos[a*t]] || 0
     */
    public F transformCosMultiplyUnitStep() {
        Ring ring = new Ring("C64[p]");
        Polynom arg = ((Polynom) inputF.X[1]);
        if (arg.coeffs[0].signum() == 1) {
            return this.transformCos(ring);
        }
        return null;
    }

    /**
     * Метод преобразования выражения вида: t^[n]*exp[a*t]*UnitStep[b*t], если
     * b>0 то преобразование t^[n]*exp[a*t], иначе 0
     *
     * @return - Выражение вида: LPT[t^[n]*exp[a*t]] || 0
     */
    public F transformMultiplyPolExpUnitStep() {
        Ring ring = new Ring("C64[p]");
        Polynom arg = ((Polynom) inputF.X[2]);
        if (arg.coeffs[0].signum() == 1) {
            return this.transformPolMultiplyExp(ring);
        }
        return null;
    }

    /**
     * Метод преобразования выражения вида: t^[n]*sin[a*t]*UnitStep[b*t], если
     * b>0 то преобразование t^[n]*sin[a*t], иначе 0
     *
     * @return - Выражение вида: LPT[t^[n]*sin[a*t]] || 0
     */
    public F transformMultiplyPolSinUnitStep() {
        Polynom arg = ((Polynom) inputF.X[2]);
        if (arg.coeffs[0].signum() == 1) {
            return this.transformPolMultiplySin();
        }
        return null;
    }

    /**
     * Метод преобразования выражения вида: t^[n]*cos[a*t]*UnitStep[b*t], если
     * b>0 то преобразование t^[n]*cos[a*t], иначе 0
     *
     * @return - Выражение вида: LPT[t^[n]*cos[a*t]] || 0
     */
    public F transformMultiplyPolCosUnitStep() {
        Polynom arg = ((Polynom) inputF.X[2]);
        if (arg.coeffs[0].signum() == 1) {
            return this.transformPolMultiplyCos();
        }
        return null;
    }

    /**
     * Метод преобразования выражения вида: exp[a*t]*sin[b*t]*UnitStep[c*t],
     * если с > 0 то преобразование exp[a*t]*sin[b*t], иначе 0
     *
     * @return - Выражение вида: LPT[exp[a*t]*sin[b*t]] || 0
     */
    public F transformMultiplyExpSinUnitStep() {
        Ring ring = new Ring("C64[p]");
        Polynom arg = ((Polynom) inputF.X[2]);
        if (arg.coeffs[0].signum() == 1) {
            return this.transformExpMultiplySin(ring);
        }
        return null;
    }

    /**
     * Метод преобразования выражения вида: exp[a*t]*cos[b*t]*UnitStep[c*t],
     * если с > 0 то преобразование exp[a*t]*cos[b*t], иначе 0
     *
     * @return - Выражение вида: LPT[exp[a*t]*cos[b*t]] || 0
     */
    public F transformMultiplyExpCosUnitStep() {
        Ring ring = new Ring("C64[p]");
        Polynom arg = ((Polynom) inputF.X[2]);
        if (arg.coeffs[0].signum() == 1) {
            return this.transformExpMultiplyCos(ring);
        }
        return null;
    }

    /**
     * Метод преобразования выражения вида:
     * t^[n]*exp[a*t]*sin[b*t]*UnitStep[c*t], если с > 0 то преобразование
     * t^[n]*exp[a*t]*sin[b*t], иначе 0
     *
     * @return - Выражение вида: LPT[t^[n]*exp[a*t]*sin[b*t]] || 0
     */
    public F transformMultiplyPolExpSinUnitStep() {
        Polynom arg = ((Polynom) inputF.X[3]);
        if (arg.coeffs[0].signum() == 1) {
            return this.transformMultiplyPolExpSin();
        }
        return null;
    }

    /**
     * Метод преобразования выражения вида:
     * t^[n]*exp[a*t]*cos[b*t]*UnitStep[c*t], если с > 0 то преобразование
     * t^[n]*exp[a*t]*cos[b*t], иначе 0
     *
     * @return - Выражение вида: LPT[t^[n]*exp[a*t]*cos[b*t]] || 0
     */
    public F transformMultiplyPolExpCosUnitStep() {
        Polynom arg = ((Polynom) inputF.X[3]);
        if (arg.coeffs[0].signum() == 1) {
            return this.transformMultiplyPolExpCos();
        }
        return null;
    }

    /**
     * Метод преобразования выражения вида: t^[n]*UnitStep[at-b], если b > 0 то
     * преобразование t^[n], иначе 0
     *
     * @return - Выражение вида: LPT[t^[n]] || 0
     */
    public F transformPolMultiplyUnitStepShift() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[1]);
        F f2 = ((F) inputF.X[0]);
        Polynom arg = ((Polynom) f1.X[0]); //для a и b
        Polynom n = ((Polynom) f2.X[0]); //для степени - n
        if (arg.coeffs[1].signum() == 1) {
            return this.transformPolynom();
        } else {
            Polynom spol = new Polynom("p", ring);
            F s = new F(spol); //s
            Element a = arg.coeffs[0];
            Element b = arg.coeffs[1];
            F power = new F(F.DIVIDE,
                    new F[]{new F(F.MULTIPLY,
                        new F[]{new F().FfromNumber(b), s}),
                        new F().FfromNumber(a)}); //-bs/a
            Polynom s_n = new Polynom(new int[]{n.powers[0] + 1},
                    new Element[]{new Complex(a.pow(n.powers[0], ring).doubleValue())}); //a^[n]*s^[n+1]
            F pols = newPolynomToF(n.powers[0], a, b.negate(ring));
            F mul = new F(F.MULTIPLY, new F[]{new F(F.EXP, power), pols}); //exp[-bs/a]*polynom
            return new F(F.DIVIDE, new Element[]{mul, s_n});
        }
    }

    /**
     * Метод вычисления n!
     *
     * @param n - Входное значение
     * @return - Результат
     */
    public double fact(int n) {
        double a = 1;
        for (int i = 1; i <= n; i++) {
            a = a * i;
        }
        return a;
    }

    /**
     * Метод вычисления n!
     *
     * @param n - Входное значение
     * @return - Результат
     */
    public double fact(double n) {
        double a = 1;
        for (int i = 1; i <= n; i++) {
            a = a * i;
        }
        return a;
    }

    /**
     * Метод вычисления характеристического полинома n-ой степени по формуле:
     * x^n*a^[0]*b^[n]+n*x^[n-1]*a^[1]*b^[n-1]+n*[n-1]*x^[n-2]*a^[2]*b^[n-2]+n*[n-1]*[n-2]*x^[n-3]*a^[3]*b^[n-3]+...+n!*a^[n]*b^[0]
     *
     * @param n - Старшая степень полинома
     * @param a - 1-ый коэффициент
     * @param b - 2-ый коэффициент
     * @return - Характеристический полином
     */
    public F newPolynomToF(int n, Element a, Element b) {
        Ring ring = new Ring("C64[p]");
        double k = fact(n); //n!
        Polynom[] p = new Polynom[n - 1];
        Polynom p0 = new Polynom(new int[0],
                new Element[]{((new Complex(a.doubleValue()).pow(n, ring)).multiply((new Complex(b.doubleValue()).pow(0, ring)), ring)).multiply(new Complex(k), ring)}); //0 моном
        Polynom pn = new Polynom(new int[]{n},
                new Element[]{(new Complex(a.doubleValue()).pow(0, ring)).multiply((new Complex(b.doubleValue()).pow(n, ring)), ring)}); //последний момном, n+1
        int h = 1;
        double g = n;
        int i = n - 1;
        for (int s = 0; s < p.length; s++) {
            p[s] = new Polynom(new int[]{i},
                    new Element[]{((new Complex(a.doubleValue()).pow(h, ring)).multiply((new Complex(
                        b.doubleValue()).pow(i, ring)), ring)).multiply(new Complex(g), ring)});
            g = g * i;
            i--;
            h++;
        }
        Polynom sum = new Polynom(new int[]{}, new Element[]{ring.numberZERO}); //0
        for (int j = 0; j < n - 1; j++) {
            sum = sum.add(p[j], ring);
        }
        sum = (sum.add(p0, ring)).add(pn, ring);
        return new F(sum);
    }

    /**
     * Метод преобразования выражения вида: t^[n]*exp[a*t]*UnitStep[b*t-c], если
     * b>0 то преобразование t^[n]*exp[a*t], иначе 0
     *
     * (e^[(-c/b)*(-a+s)]*[(n!/k!)*b^[n-k]*c^[k]*(-a+s)^k],k=0..n)/(b^[n]*(-a+s)^[1+n])
     *
     * @return - Выражение вида: LPT[t^[n]*exp[a*t]] || 0
     */
    public F transformMultiplyPolExpUnitStepShift() {
        Ring ring = new Ring("C64[p]");
        Polynom f1 = (Polynom) inputF.X[0];
        F f2, f3 = null;
        if (((F) inputF.X[1]).name == F.EXP) {
            f2 = ((F) inputF.X[1]);//exp
        } else {
            f2 = ((F) inputF.X[2]);//exp
        }
        if (((F) inputF.X[2]).name == F.UNITSTEP) {
            f3 = ((F) inputF.X[2]);//unitStep
        } else {
            f3 = ((F) inputF.X[1]);//unitStep
        }
        Polynom arg = ((Polynom) f3.X[0]); //для b и c
        Polynom a_pol = ((Polynom) f2.X[0]); //для a
        int n = f1.powers[0]; //для степени n
        if (arg.coeffs.length == 1) {
            F fthis = new F(inputF.name, new Element[]{inputF.X[0], inputF.X[1]});
            return new LaplaceTransform(fthis).transformPolMultiplyExp(ring);
        } else {
            Polynom s_pol = new Polynom(new int[]{1, 0},
                    new Element[]{new Complex(1.0, 0.0),
                        new Complex((a_pol.coeffs[0].negate(ring)).doubleValue())}); //(-a+s)
            F s = new F(s_pol); //-a+s
            Element a = a_pol.coeffs[0];
            Element b = arg.coeffs[0];
            Element c = arg.coeffs[1].negate(ring);
            Element pols = newPolynomToElement(n, a, b, c); //полином в числителе
            F b_pow = new F().FfromNumber(b.pow(n, ring)); //b^[n]
            F power = new F(F.intPOW, new Element[]{s_pol,
                        new Complex(n + 1)}); //(-a+s)^[n+1]
            Polynom st = new Polynom(new int[]{1, 0},
                    new Element[]{((c).divide(b, ring)).negate(ring),
                        ((c).multiply(a, ring)).divide(b, ring)}); //ca/b-c/b*s
            F e = new F(F.EXP, st); //e^[ca/b-c/b*s]
            F mul = new F(F.MULTIPLY, new Element[]{e, pols}); //exp[ca/b-c/b*s]*polynom
            F div = null;
            if (b_pow.isOne(ring)) {
                div = power;
            } else {
                div = new F(F.MULTIPLY, new Element[]{b_pow, power}); //b^[n]*(-a+s)^[n+1]
            }
            return new F(F.DIVIDE, new Element[]{mul, div}); //[exp[ca/b-c/b*s]*polynom] / [b^[n]*(-a+s)^[n+1]]
        }
    }

    /**
     * Метод вычисления характеристического полинома n-ой степени по формуле:
     * a[0] = n!, (a[k-1]/k)*b^[n-k]*c^[k]*[-a+s]^k and k:1...n
     *
     * @param n - Старшая степень полинома
     * @param a - 1-ый коэффициент
     * @param b - 2-ый коэффициент
     * @param c - 3-ый коэффициент
     * @return - Характеристический полином
     */
    public Element newPolynomToElement(int n, Element a, Element b, Element c) {
        Ring ring = new Ring("C64[p]");
        Polynom[] p = new Polynom[n + 1];
        a = a.negate(ring);
        Polynom s_pol = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(1.0, 0.0),
                    new Complex(a.doubleValue())}); //(-a+s)
        for (int i = 0; i <= n; i++) {
            p[i] = new Polynom(new int[0],
                    new Element[]{((new Complex(c.doubleValue()).pow(i, ring)).multiply((new Complex(
                        b.doubleValue()).pow(n - i, ring)), ring)).multiply(new Complex(fact(
                        n) / fact(i)), ring)});
            Polynom ppow = (Polynom) s_pol.pow(i, ring);
            p[i] = p[i].multiply(ppow, ring);
        }
        Polynom sum = p[0];
        for (int j = 1; j < n + 1; j++) {
            sum = sum.add(p[j], ring);
        }
        return sum;
    }

    /**
     * Метод преобразования выражения вида: exp[a*t]*UnitStep[b*t-c], если b > 0
     * то преобразование exp[a*t], иначе 0
     *
     * @return - Выражение вида: LPT[exp[a*t]] || 0
     */
    public F transformExpMultiplyUnitStepShift() {
        Ring ring = new Ring("C64[p]");
        F f1, f2 = null;
        if (((F) inputF.X[0]).name == F.EXP) {
            f1 = ((F) inputF.X[0]);//exp
        } else {
            f1 = ((F) inputF.X[1]);//exp
        }
        if (((F) inputF.X[1]).name == F.UNITSTEP) {
            f2 = ((F) inputF.X[1]);//unitStep
        } else {
            f2 = ((F) inputF.X[0]);//unitStep
        }
        Polynom arg = ((Polynom) f2.X[0]); //для b и c
        Polynom a_pol = ((Polynom) f1.X[0]); //для a
        if (arg.coeffs.length == 1) {
            return new LaplaceTransform(f1).transformExp();
        } else {
            Element a = a_pol.coeffs[0];
            Element b = arg.coeffs[0];
            Element c = arg.coeffs[1];
            Polynom s_pol = new Polynom(new int[]{1, 0},
                    new Element[]{new Complex((c.doubleValue()) / (b.doubleValue()), 0),
                        new Complex((a.doubleValue()) * c.negate(ring).doubleValue() / b.doubleValue())}); //(ac/b-sc/b)
            Polynom st = new Polynom(new int[]{1, 0},
                    new Element[]{new Complex(1.0, 0.0), new Complex(a.negate(ring).doubleValue())}); //-a+s
            Element s = st;//-a+s
            F e = new F(F.EXP, s_pol); //exp^[(ac/b-sc/b)]
            return new F(F.DIVIDE, new Element[]{e, s}); //exp^[(ac/b-sc/b)] / [-a+s]
        }
    }

    /**
     * Метод преобразования выражения вида: sin[a*t]*UnitStep[b*t-c], если b > 0
     * то преобразование sin[a*t], иначе 0
     *
     * @return - Выражение вида: LPT[sin[a*t]] || 0
     */
    public F transformSinMultiplyUnitStepShift() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[0]);
        F f2 = ((F) inputF.X[1]);
        Polynom arg = ((Polynom) f2.X[0]); //для b и c
        Polynom a_pol = ((Polynom) f1.X[0]); //для a
        if (arg.coeffs[1].signum() == 1) {
            return this.transformSin(ring);
        } else {
            Polynom s_pol = new Polynom(new int[]{1}, new Element[]{new Complex(1.0, 0.0)}); //(s)
            Polynom min_s_pol = new Polynom(new int[]{1}, new Element[]{new Complex(1.0, 0.0).negate(ring)}); //(-s)
            F s = new F(s_pol); //s
            Element a = a_pol.coeffs[0];
            Element b = arg.coeffs[0];
            Element c = arg.coeffs[1];
            Polynom acb = new Polynom(new int[]{0}, new Element[]{((c.negate(ring)).multiply(a, ring)).divide(b, ring)}); //ac/b
            Polynom cb = new Polynom(new int[]{0}, new Element[]{((c.negate(ring)).divide(b, ring))}); //c/b
            Polynom ap = new Polynom(new int[]{0}, new Element[]{a}); //a
            F e = new F(F.EXP, new F(F.MULTIPLY, new Element[]{min_s_pol, cb})); //exp^[(-s) * c/b]
            F m1 = new F(F.MULTIPLY, new F[]{new F(ap), new F(F.COS, acb)}); //a*Cos[ac/b]
            F m2 = new F(F.MULTIPLY, new F[]{s, new F(F.SIN, acb)}); //s*Sin[ac/b]
            F add = new F(F.ADD, new F[]{m1, m2}); //a*Cos[ac/b]+s*Sin[ac/b]
            Polynom as = new Polynom(new int[]{2, 0}, new Element[]{new Complex(1.0, 0.0), new Complex(a.doubleValue()).pow(2, ring)}); //a^2+s^2
            F m3 = new F(F.MULTIPLY, new F[]{e, add}); //exp^[(-s) * c/b] * [a*Cos[ac/b]+s*Sin[ac/b]]
            return new F(F.DIVIDE, new F[]{m3, new F(as)}); //[exp^[(-s) * c/b] * [a*Cos[ac/b]+s*Sin[ac/b]]] / [a^2+s^2]
        }
    }

    /**
     * Метод преобразования выражения вида: cos[a*t]*UnitStep[b*t-c], если b > 0
     * то преобразование cos[a*t], иначе 0
     *
     * @return - Выражение вида: LPT[cos[a*t]] || 0
     */
    public F transformCosMultiplyUnitStepShift() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[0]);
        F f2 = ((F) inputF.X[1]);
        Polynom arg = ((Polynom) f2.X[0]); //для b и c
        Polynom a_pol = ((Polynom) f1.X[0]); //для a
        if (arg.coeffs[1].signum() == 1) {
            return this.transformSin(ring);
        } else {
            Polynom s_pol = new Polynom(new int[]{1}, new Element[]{new Complex(1.0, 0.0)}); //(s)
            Polynom min_s_pol = new Polynom(new int[]{1}, new Element[]{new Complex(1.0, 0.0).negate(ring)}); //(-s)
            F s = new F(s_pol); //s
            Element a = a_pol.coeffs[0];
            Element b = arg.coeffs[0];
            Element c = arg.coeffs[1];
            Polynom acb = new Polynom(new int[]{0}, new Element[]{((c.negate(ring)).multiply(a, ring)).divide(b, ring)}); //ac/b
            Polynom cb = new Polynom(new int[]{0}, new Element[]{((c.negate(ring)).divide(b, ring))}); //c/b
            Polynom ap = new Polynom(new int[]{0}, new Element[]{a}); //a
            F e = new F(F.EXP, new F(F.MULTIPLY, new Element[]{min_s_pol, cb})); //exp^[(-s) * c/b]
            F m1 = new F(F.MULTIPLY, new F[]{s, new F(F.COS, new F(acb))}); //s*Cos[ac/b]
            F m2 = new F(F.MULTIPLY, new F[]{new F(ap), new F(F.SIN, new F(acb))}); //a*Sin[ac/b]
            F add = new F(F.SUBTRACT, new F[]{m1, m2}); //s*Cos[ac/b]-s*Sin[ac/b]
            Polynom as = new Polynom(new int[]{2, 0}, new Element[]{new Complex(1.0, 0.0), new Complex(a.doubleValue()).pow(2, ring)}); //a^2+s^2
            F m3 = new F(F.MULTIPLY, new F[]{e, add}); //exp^[(-s) * c/b] * [s*Cos[ac/b]-a*Sin[ac/b]]
            return new F(F.DIVIDE, new F[]{m3, new F(as)}); //[exp^[(-s) * c/b] * [s*Cos[ac/b]-a*Sin[ac/b]]] / [a^2+s^2]
        }
    }

    /**
     * Метод преобразования выражения вида: exp[a*t]sin[b*t]*UnitStep[c*t-d],
     * если c > 0 то преобразование exp[a*t]sin[b*t], иначе 0
     *
     * @return - Выражение вида: LPT[exp[a*t]sin[b*t]] || 0
     */
    public F transformMultiplyExpSinUnitStepShift() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[0]);
        F f2 = ((F) inputF.X[1]);
        F f3 = ((F) inputF.X[2]);
        Polynom arg = ((Polynom) f3.X[0]); //для c и d
        Polynom arg1 = ((Polynom) f2.X[0]); //для b
        Polynom a_pol = ((Polynom) f1.X[0]); //для a
        if (arg.coeffs[1].signum() == 1) {
            return this.transformSin(ring);
        } else {
            Element a = a_pol.coeffs[0];
            Element b = arg1.coeffs[0];
            Element c = arg.coeffs[0];
            Element dk = arg.coeffs[1];
            Polynom s_pol = new Polynom(new int[]{1, 0},
                    new Element[]{new Complex(1.0, 0.0).negate(ring), new Complex(a.doubleValue())}); //(a-s)
            Polynom min_s_pol = new Polynom(new int[]{1, 0},
                    new Element[]{new Complex(1.0, 0.0),
                        new Complex(a.doubleValue()).negate(ring)}); //(-a+s)
            F s = new F(s_pol); //a-s
            F s_min = new F(min_s_pol); //-a+s
            Polynom bdc = new Polynom(new int[]{0},
                    new Element[]{((dk.negate(ring)).multiply(b, ring)).divide(c, ring)}); //bd/c
            Polynom dc = new Polynom(new int[]{0},
                    new Element[]{((dk.negate(ring)).divide(c, ring))}); //d/c
            Polynom bp = new Polynom(new int[]{0}, new Element[]{b}); //b
            F e = new F(F.EXP, new F(F.MULTIPLY, new F[]{s, new F(dc)})); //exp^[(a-s) * d/c]
            F m1 = new F(F.MULTIPLY, new F[]{new F(bp),
                        new F(F.COS, new F(bdc))}); //b*Cos[bd/c]
            F m2 = new F(F.MULTIPLY, new F[]{s_min,
                        new F(F.SIN, new F(bdc))}); //(-a+s)*Sin[bd/c]
            F add = new F(F.ADD, new F[]{m1, m2}); //b*Cos[bd/c]+(-a+s)*Sin[bd/c]
            F stp = new F(F.POW, new F[]{s, new F().FfromNumber(ring.numberONE().add(ring.numberONE(), ring))}); //(a-s)^2
            F m3 = new F(F.ADD, new F[]{new F().FfromNumber(b.pow(2, ring)), stp}); //b^2+(a-s)^2
            F m4 = new F(F.MULTIPLY, new F[]{e, add}); //exp^[(a-s) * d/c]*[b*Cos[bd/c]+(-a+s)*Sin[bd/c]]
            return new F(F.DIVIDE, new F[]{m4, m3}); //exp^[(a-s) * d/c]*[b*Cos[bd/c]+(-a+s)*Sin[bd/c]] / [b^2+(a-s)^2]
        }
    }

    /**
     * Метод преобразования выражения вида: exp[a*t]cos[b*t]*UnitStep[c*t-d],
     * если c > 0 то преобразование exp[a*t]cos[b*t], иначе 0
     *
     * @return - Выражение вида: LPT[exp[a*t]cos[b*t]] || 0
     */
    public F transformMultiplyExpCosUnitStepShift() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[0]);
        F f2 = ((F) inputF.X[1]);
        F f3 = ((F) inputF.X[2]);
        Polynom arg = ((Polynom) f3.X[0]); //для c и d
        Polynom arg1 = ((Polynom) f2.X[0]); //для b
        Polynom a_pol = ((Polynom) f1.X[0]); //для a
        if (arg.coeffs[1].signum() == 1) {
            return this.transformSin(ring);
        } else {
            Element a = a_pol.coeffs[0];
            Element b = arg1.coeffs[0];
            Element c = arg.coeffs[0];
            Element dk = arg.coeffs[1];
            Polynom s_pol = new Polynom(new int[]{1, 0},
                    new Element[]{new Complex(1.0, 0.0).negate(ring), new Complex(a.doubleValue())}); //(a-s)
            F s = new F(s_pol); //a-s
            Polynom bdc = new Polynom(new int[]{0},
                    new Element[]{((dk.negate(ring)).multiply(b, ring)).divide(c, ring)}); //bd/c
            Polynom dc = new Polynom(new int[]{0},
                    new Element[]{((dk.negate(ring)).divide(c, ring))}); //d/c
            Polynom bp = new Polynom(new int[]{0}, new Element[]{b}); //b
            F e = new F(F.EXP, new F(F.MULTIPLY, new F[]{s, new F(dc)})); //exp^[(a-s) * d/c]
            F m1 = new F(F.MULTIPLY, new F[]{s, new F(F.COS, new F(bdc))}); //(a-s)*Cos[bd/c]
            F m2 = new F(F.MULTIPLY, new F[]{new F(bp),
                        new F(F.SIN, new F(bdc))}); //b*Sin[bd/c]
            F add = new F(F.ADD, new F[]{m1, m2}); //(a-s)*Cos[bd/c]+b*Sin[bd/c]
            F stp = new F(F.POW, new F[]{s, new F().FfromNumber(new Complex(2.0))}); //(a-s)^2
            F m3 = new F(F.ADD, new F[]{new F().FfromNumber(b.pow(2, ring)), stp}); //b^2+(a-s)^2
            F m4 = new F(F.MULTIPLY, new F[]{e, add}); //exp^[(a-s) * d/c]*[(a-s)*Cos[bd/c]+b*Sin[bd/c]]
            return new F(F.DIVIDE, new F[]{m4, m3}); //exp^[(a-s) * d/c]*[(a-s)*Cos[bd/c]+b*Sin[bd/c]] / [b^2+(a-s)^2]
        }
    }

    /**
     * Метод преобразования выражения вида: t^n * UnitStep[a*t-b], если a > 0 то
     * преобразование t^n, иначе 0
     *
     * @return - Выражение вида: LPT[t^n] || 0
     */
    public F transformMultiplyPolUnitStepShift() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[0]);
        F f2 = ((F) inputF.X[1]);
        Polynom arg1 = ((Polynom) f1.X[0]); //t^n
        Polynom arg2 = ((Polynom) f2.X[0]); //at-b
        int n = arg1.powers[0]; //n
        Element a = arg2.coeffs[0]; //a
        Element b = arg2.coeffs[1].negate(ring); //-b//b
        Polynom s1 = new Polynom(new int[]{1 + n},
                new Element[]{new Complex(1.0, 0.0)}); //s^[1+n]
        Polynom s2 = new Polynom(new int[]{1},
                new Element[]{new Complex(b.doubleValue()
                    / a.doubleValue())}); //bs/a
        return new F(F.DIVIDE, new F[]{new F(F.GAMMA2, new Element[]{new F().FfromNumber(new Complex(1 + n)), s2}), new F(s1)}); //Gamma[1+n,bs/a] / [s^[1+n]]
    }

    /**
     * Метод преобразования выражения вида: t^n * exp[a*t] * UnitStep[b*t-c],
     * если b > 0 то преобразование t^n * exp[a*t], иначе 0
     *
     * @return - Выражение вида: LPT[t^n * exp[a*t]] || 0
     */
    public F transformPolExpUnitStepShift() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[0]);
        F f2 = ((F) inputF.X[1]);
        F f3 = ((F) inputF.X[2]);
        Polynom arg1 = ((Polynom) f1.X[0]); //t^n
        Polynom arg2 = ((Polynom) f2.X[0]); //at
        Polynom arg3 = ((Polynom) f3.X[0]); //bt-c
        int n = arg1.powers[0]; //n
        Element a = arg2.coeffs[0]; //a
        Element b = arg3.coeffs[0]; //b
        Element c = arg3.coeffs[1].negate(ring); //-c//c
        Polynom s1 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(1.0, 0.0),
                    new Complex(a.doubleValue()).negate(ring)}); //-a+s
        Polynom s2 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(c.doubleValue()
                    / b.doubleValue()),
                    (new Complex(a.doubleValue()).negate(ring).
                    multiply(new Complex(c.doubleValue()), ring)).divide(new Complex(b.doubleValue()), ring)}); //cs/b - ac/b
        F d1 = new F(F.POW, new Element[]{s1, new F().FfromNumber(new Complex(1 + n))}); //(-a+s)^[1+n]
        F d2 = new F(F.GAMMA2, new Element[]{new F().FfromNumber(new Complex(1 + n)), s2}); //Gamma[1+n,(-a+s)c/b]
        return new F(F.DIVIDE, new F[]{d2, d1}); //Gamma[1+n,cs/b-ac/b] / [(-a+s)^[1+n]]
    }

    /**
     * Метод преобразования выражения вида: t^n * sin[a*t] * UnitStep[b*t-c],
     * если b > 0 то преобразование t^n * sin[a*t], иначе 0
     *
     * @return - Выражение вида: LPT[t^n * sin[a*t]] || 0
     */
    public F transformPolSinUnitStepShift() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[0]);
        F f2 = ((F) inputF.X[1]);
        F f3 = ((F) inputF.X[2]);
        Polynom arg1 = ((Polynom) f1.X[0]); //t^n
        Polynom arg2 = ((Polynom) f2.X[0]); //at
        Polynom arg3 = ((Polynom) f3.X[0]); //bt-c
        int n = arg1.powers[0]; //n
        Element a = arg2.coeffs[0]; //a
        Element b = arg3.coeffs[0]; //b
        Element c = arg3.coeffs[1].negate(ring); //-c//c
        Polynom s1 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(1.0, 0.0),
                    new Complex(0.0, a.doubleValue()).negate(ring)}); //-ai+s
        Polynom s2 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(c.doubleValue()
                    / b.doubleValue()),
                    (new Complex(0.0, a.doubleValue()).negate(ring).
                    multiply(new Complex(c.doubleValue()), ring)).divide(new Complex(b.doubleValue()), ring)}); //cs/b - aic/b
        Polynom s3 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(1.0, 0.0),
                    new Complex(0.0, a.doubleValue())}); //ai+s
        Polynom s4 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(c.doubleValue()
                    / b.doubleValue()),
                    (new Complex(0.0,
                    a.doubleValue()).multiply(new Complex(c.doubleValue()), ring)).divide(new Complex(b.doubleValue()), ring)}); //cs/b + aic/b
        Polynom s5 = new Polynom(new int[]{0},
                new Element[]{new Complex(0.0, 1.0).negate(ring).
                    divide(new Complex(2.0), ring)}); //-i/2
        F d1 = new F(F.POW, new Element[]{s1, new F().FfromNumber(new Complex(1 + n))}); //(-ai+s)^[1+n]
        F d2 = new F(F.GAMMA2, new Element[]{new F().FfromNumber(new Complex(1 + n)), s2}); //Gamma[1+n,(-ai+s)c/b]
        F d3 = new F(F.POW, new Element[]{s3, new F().FfromNumber(new Complex(1 + n))}); //(ai+s)^[1+n]
        F d4 = new F(F.GAMMA2, new Element[]{new F().FfromNumber(new Complex(1 + n)), s4}); //Gamma[1+n,(ai+s)c/b]
        F r1 = new F(F.DIVIDE, new F[]{d2, d1}); //Gamma[1+n,cs/b-aic/b] / [(-ai+s)^[1+n]]
        F r2 = new F(F.DIVIDE, new F[]{d4, d3}); //Gamma[1+n,cs/b+aic/b] / [(ai+s)^[1+n]]
        F r3 = new F(F.SUBTRACT, new F[]{r1, r2}); //Gamma[1+n,cs/b-aic/b] / [(-ai+s)^[1+n]] - Gamma[1+n,cs/b+aic/b] / [(ai+s)^[1+n]]
        return new F(F.MULTIPLY, new F[]{new F(s5), r3}); //[-i/2]*[Gamma[1+n,cs/b-aic/b] / [(-ai+s)^[1+n]] - Gamma[1+n,cs/b+aic/b] / [(ai+s)^[1+n]]]
    }

    /**
     * Метод преобразования выражения вида: t^n * cos[a*t] * UnitStep[b*t-c],
     * если b > 0 то преобразование t^n * cos[a*t], иначе 0
     *
     * @return - Выражение вида: LPT[t^n * cos[a*t]] || 0
     */
    public F transformPolCosUnitStepShift() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[0]);
        F f2 = ((F) inputF.X[1]);
        F f3 = ((F) inputF.X[2]);
        Polynom arg1 = ((Polynom) f1.X[0]); //t^n
        Polynom arg2 = ((Polynom) f2.X[0]); //at
        Polynom arg3 = ((Polynom) f3.X[0]); //bt-c
        int n = arg1.powers[0]; //n
        Element a = arg2.coeffs[0]; //a
        Element b = arg3.coeffs[0]; //b
        Element c = arg3.coeffs[1].negate(ring); //-c//c
        Polynom s1 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(1.0, 0.0),
                    new Complex(0.0, a.doubleValue()).negate(ring)}); //-ai+s
        Polynom s2 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(c.doubleValue()
                    / b.doubleValue()),
                    (new Complex(0.0, a.doubleValue()).negate(ring).
                    multiply(new Complex(c.doubleValue()), ring)).divide(new Complex(b.doubleValue()), ring)}); //cs/b - aic/b
        Polynom s3 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(1.0, 0.0),
                    new Complex(0.0, a.doubleValue())}); //ai+s
        Polynom s4 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(c.doubleValue()
                    / b.doubleValue()),
                    (new Complex(0.0,
                    a.doubleValue()).multiply(new Complex(c.doubleValue()), ring)).divide(new Complex(b.doubleValue()), ring)}); //cs/b + aic/b
        Polynom s5 = new Polynom(new int[]{0},
                new Element[]{new Complex(1.0,
                    0.0).divide(new Complex(2.0), ring)}); //1/2
        F d1 = new F(F.POW, new Element[]{s1, new F().FfromNumber(new Complex(1 + n))}); //(-ai+s)^[1+n]
        F d2 = new F(F.GAMMA2, new Element[]{new F().FfromNumber(new Complex(1 + n)), s2}); //Gamma[1+n,(-ai+s)c/b]
        F d3 = new F(F.POW, new Element[]{s3, new F().FfromNumber(new Complex(1 + n))}); //(ai+s)^[1+n]
        F d4 = new F(F.GAMMA2, new Element[]{new F().FfromNumber(new Complex(1 + n)), s4}); //Gamma[1+n,(ai+s)c/b]
        F r1 = new F(F.DIVIDE, new F[]{d2, d1}); //Gamma[1+n,cs/b-aic/b] / [(-ai+s)^[1+n]]
        F r2 = new F(F.DIVIDE, new F[]{d4, d3}); //Gamma[1+n,cs/b+aic/b] / [(ai+s)^[1+n]]
        F r3 = new F(F.ADD, new F[]{r1, r2}); //Gamma[1+n,cs/b-aic/b] / [(-ai+s)^[1+n]] + Gamma[1+n,cs/b+aic/b] / [(ai+s)^[1+n]]
        return new F(F.MULTIPLY, new F[]{new F(s5), r3}); //[1/2]*[Gamma[1+n,cs/b-aic/b] / [(-ai+s)^[1+n]] + Gamma[1+n,cs/b+aic/b] / [(ai+s)^[1+n]]]
    }

    /**
     * Метод преобразования выражения вида: t^n * exp[a*t] * sin[b*t] *
     * UnitStep[c*t-d], если c > 0 то преобразование t^n * exp[a*t] * sin[b*t],
     * иначе 0
     *
     * @return - Выражение вида: LPT[t^n * exp[a*t] * sin[b*t]] || 0
     */
    public F transformPolExpSinUnitStepShift() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[0]);
        F f2 = ((F) inputF.X[1]);
        F f3 = ((F) inputF.X[2]);
        F f4 = ((F) inputF.X[3]);
        Polynom arg1 = ((Polynom) f1.X[0]); //t^n
        Polynom arg2 = ((Polynom) f2.X[0]); //at
        Polynom arg3 = ((Polynom) f3.X[0]); //bt
        Polynom arg4 = ((Polynom) f4.X[0]); //ct-d
        int n = arg1.powers[0]; //n
        Element a = arg2.coeffs[0]; //a
        Element b = arg3.coeffs[0]; //b
        Element c = arg4.coeffs[0]; //c
        Element dk = arg4.coeffs[1].negate(ring); //-d//d
        Polynom s1 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(1.0, 0.0),
                    new Complex(a.doubleValue(), 0.0).negate(ring).
                    subtract(new Complex(0.0, b.doubleValue()), ring)}); //-a-ib+s
        Polynom s2 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(dk.doubleValue()
                    / c.doubleValue()),
                    (new Complex(a.negate(ring).doubleValue() * dk.doubleValue()
                    / c.doubleValue(),
                    b.negate(ring).doubleValue()
                    * dk.doubleValue() / c.doubleValue()))}); //ds/c - ad/c-ibd/c
        Polynom s3 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(1.0, 0.0),
                    new Complex(a.doubleValue(),
                    0.0).negate(ring).add(new Complex(0, b.doubleValue()), ring)}); //-a+ib+s
        Polynom s4 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(dk.doubleValue()
                    / c.doubleValue()),
                    (new Complex(a.negate(ring).doubleValue() * dk.doubleValue()
                    / c.doubleValue(),
                    b.doubleValue() * dk.doubleValue()
                    / c.doubleValue()))}); //ds/c - ad/c+ibd/c
        Polynom s5 = new Polynom(new int[]{0},
                new Element[]{new Complex(0.0, 1.0).negate(ring).
                    divide(new Complex(2.0), ring)}); //-i/2
        F d1 = new F(F.POW, new Element[]{s1, new F().FfromNumber(new Complex(1 + n))}); //(-a-ib+s)^[1+n]
        F d2 = new F(F.GAMMA2, new Element[]{new F().FfromNumber(new Complex(1 + n)), s2}); //Gamma[1+n,(-a-ib+s)d/c]
        F d3 = new F(F.POW, new Element[]{s3, new F().FfromNumber(new Complex(1 + n))}); //(-a+ib+s)^[1+n]
        F d4 = new F(F.GAMMA2, new Element[]{new F().FfromNumber(new Complex(1 + n)), s4}); //Gamma[1+n,(-a+ib+s)d/c]
        F r1 = new F(F.DIVIDE, new F[]{d2, d1}); //Gamma[1+n,ds/c-ad/c-ibd/c] / [(-a-ib+s)^[1+n]]
        F r2 = new F(F.DIVIDE, new F[]{d4, d3}); //Gamma[1+n,ds/c-ad/c+ibd/c] / [(-a+ib+s)^[1+n]]
        F r3 = new F(F.SUBTRACT, new F[]{r1, r2}); //Gamma[1+n,ds/c-ad/c-ibd/c] / [(-a-ib+s)^[1+n]] - Gamma[1+n,ds/c-ad/c+ibd/c] / [(-a+ib+s)^[1+n]]
        return new F(F.MULTIPLY, new F[]{new F(s5), r3}); //[-i/2]*[Gamma[1+n,ds/c-ad/c-ibd/c] / [(-a-ib+s)^[1+n]] - Gamma[1+n,ds/c-ad/c+ibd/c] / [(-a+ib+s)^[1+n]]]
    }

    /**
     * Метод преобразования выражения вида: t^n * exp[a*t] * cos[b*t] *
     * UnitStep[c*t-d], если c > 0 то преобразование t^n * exp[a*t] * cos[b*t],
     * иначе 0
     *
     * @return - Выражение вида: LPT[t^n * exp[a*t] * cos[b*t]] || 0
     */
    public F transformPolExpCosUnitStepShift() {
        Ring ring = new Ring("C64[p]");
        F f1 = ((F) inputF.X[0]);
        F f2 = ((F) inputF.X[1]);
        F f3 = ((F) inputF.X[2]);
        F f4 = ((F) inputF.X[3]);
        Polynom arg1 = ((Polynom) f1.X[0]); //t^n
        Polynom arg2 = ((Polynom) f2.X[0]); //at
        Polynom arg3 = ((Polynom) f3.X[0]); //bt
        Polynom arg4 = ((Polynom) f4.X[0]); //ct-d
        int n = arg1.powers[0]; //n
        Element a = arg2.coeffs[0]; //a
        Element b = arg3.coeffs[0]; //b
        Element c = arg4.coeffs[0]; //c
        Element dk = arg4.coeffs[1].negate(ring); //-d//d
        Polynom s1 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(1.0, 0.0),
                    new Complex(a.doubleValue(), 0.0).negate(ring).
                    subtract(new Complex(0.0, b.doubleValue()), ring)}); //-a-ib+s
        Polynom s2 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(dk.doubleValue()
                    / c.doubleValue()),
                    (new Complex(a.negate(ring).doubleValue() * dk.doubleValue()
                    / c.doubleValue(),
                    b.negate(ring).doubleValue()
                    * dk.doubleValue() / c.doubleValue()))}); //ds/c - ad/c-ibd/c
        Polynom s3 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(1.0, 0.0),
                    new Complex(a.doubleValue(),
                    0.0).negate(ring).add(new Complex(0.0, b.doubleValue()), ring)}); //-a+ib+s
        Polynom s4 = new Polynom(new int[]{1, 0},
                new Element[]{new Complex(dk.doubleValue()
                    / c.doubleValue()),
                    (new Complex(a.negate(ring).doubleValue() * dk.doubleValue()
                    / c.doubleValue(),
                    b.doubleValue() * dk.doubleValue()
                    / c.doubleValue()))}); //ds/c - ad/c+ibd/c
        Polynom s5 = new Polynom(new int[]{0},
                new Element[]{new Complex(1.0,
                    0.0).divide(new Complex(2.0), ring)}); //1/2
        F d1 = new F(F.POW, new Element[]{s1, new F().FfromNumber(new Complex(1 + n))}); //(-a-ib+s)^[1+n]
        F d2 = new F(F.GAMMA2, new Element[]{new F().FfromNumber(new Complex(1 + n)), s2}); //Gamma[1+n,(-a-ib+s)d/c]
        F d3 = new F(F.POW, new Element[]{s3, new F().FfromNumber(new Complex(1 + n))}); //(-a+ib+s)^[1+n]
        F d4 = new F(F.GAMMA2, new Element[]{new F().FfromNumber(new Complex(1 + n)), s4}); //Gamma[1+n,(-a+ib+s)d/c]
        F r1 = new F(F.DIVIDE, new F[]{d2, d1}); //Gamma[1+n,ds/c-ad/c-ibd/c] / [(-a-ib+s)^[1+n]]
        F r2 = new F(F.DIVIDE, new F[]{d4, d3}); //Gamma[1+n,ds/c-ad/c+ibd/c] / [(-a+ib+s)^[1+n]]
        F r3 = new F(F.ADD, new F[]{r1, r2}); //Gamma[1+n,ds/c-ad/c-ibd/c] / [(-a-ib+s)^[1+n]] + Gamma[1+n,ds/c-ad/c+ibd/c] / [(-a+ib+s)^[1+n]]
        return new F(F.MULTIPLY, new F[]{new F(s5), r3}); //[-i/2]*[Gamma[1+n,ds/c-ad/c-ibd/c] / [(-a-ib+s)^[1+n]] + Gamma[1+n,ds/c-ad/c+ibd/c] / [(-a+ib+s)^[1+n]]]
    }

    /**
     * Метод замены узлов входной функции в произведении на преобразованные по
     * прямому преобразованию Лапласа
     *
     * @param f - Входная функция
     */
    public F transformMultiplyElement(Element f, Ring ring) {
        F res = null;
        if (f instanceof F) {
            int argLengtn = ((F) f).X.length;
            if (argLengtn == 2) {
                if ((((F) f).X[0] instanceof F) && (((F) f).X[1] instanceof F)) {
                    F f1 = ((F) ((F) f).X[0]);
                    F f2 = ((F) ((F) f).X[1]);
                    if ((f1.name == F.EXP) && (f2.name == F.UNITSTEP)) {
                        res = new LaplaceTransform(((F) f)).transformExpMultiplyUnitStepShift();
                    }
                    if ((f2.name == F.EXP) && (f1.name == F.UNITSTEP)) {
                        res = new LaplaceTransform(((F) f)).transformExpMultiplyUnitStepShift();
                    }
                }
            }
            if (argLengtn == 3) {
                if ((((F) f).X[0] instanceof Polynom) && (((F) f).X[1] instanceof F) && (((F) f).X[2] instanceof F)) {
                    F f1 = ((F) ((F) f).X[1]);
                    F f2 = ((F) ((F) f).X[2]);
                    if ((f1.name == F.EXP) && (f2.name == F.UNITSTEP)) {
                        res = new LaplaceTransform(((F) f)).transformMultiplyPolExpUnitStepShift();
                    }
                    if ((f2.name == F.EXP) && (f1.name == F.UNITSTEP)) {
                        res = new LaplaceTransform(((F) f)).transformMultiplyPolExpUnitStepShift();
                    }
                }
            }
        }
        return new LaplaceTransform((F) f).transformNumberMultiplyExp();
    }

    /**
     * Метод замены узлов функции на преобразованные по прямому преобразованию
     * Лапласа
     *
     * @param f - Входная функция
     */
    public F transform(Element f, Ring ring) {
        Element[] newX = null;
        if (f instanceof F) {
            newX = new Element[((F) f).X.length];
            if (((F) f).name == F.intPOW) {
                if (((F) f).X.length == 2) {
                    for (int i = 0; i < ((F) f).X.length; i++) {
                        if (((F) f).X[i] instanceof F) {
                            if (((F) ((F) f).X[i]).name == F.SIN) {
                                return (F) new LaplaceTransform(((F) f)).transformSinPowN(ring);
                            }
                        }
                    }
                }
            }
            if (((F) f).name == F.MULTIPLY) { //случай произведения коэффициента на функцию
                if (((F) f).X.length == 2) {
                    if ((((F) f).X[0] instanceof F) && (((F) f).X[1] instanceof F)) {
                        if (((F) ((F) f).X[0]).name == F.EXP) {
                            F f1 = new LaplaceTransform().transform(((F) f).X[1], ring);
                        } else {
                            F f1 = new LaplaceTransform().transform(((F) f).X[0], ring);
                        }
                    } else {
                        for (int i = 0; i < ((F) f).X.length; i++) {
                            if (((F) f).X[i] instanceof F) {
                                if (((F) ((F) f).X[i]).name == F.SIN) {
                                    return new LaplaceTransform(((F) f)).transformSin(ring);
                                }
                                if (((F) ((F) f).X[i]).name == F.EXP) {
                                    return new LaplaceTransform(((F) f)).transformExp();
                                }
                            }
                        }
                    }
                }
            }
            if (((F) f).X.length == 1) {
                LaplaceTransform Lpt = new LaplaceTransform((((F) f)));
                switch (((F) f).name) {
                    case F.ID: {
                        if (((Polynom) ((F) ((F) f).X[0]).X[0]).powers.length == 0) {
                            newX[0] = ((Element) Lpt.transformNumber());
                        } else {
                            newX[0] = ((Element) Lpt.transformPolynom());
                        }
                    }
                    case F.SIN:
                        return Lpt.transformSin(ring);
                    case F.COS:
                        return Lpt.transformCos(ring);
                    case F.EXP:
                        return Lpt.transformExp();
                    case F.MULTIPLY:
                        return Lpt.transformNumberMultiplyExp();
                }
            }
            for (int i = 0; i < ((F) f).X.length; i++) {
                LaplaceTransform Lpt = null;
                if(((F) f).X[i] instanceof F){
                 Lpt = new LaplaceTransform(((F) ((F) f).X[i]));
                }else Lpt = new LaplaceTransform(new F(((F) f).X[i]));
                if (((F) f).X[i] instanceof F) {
                    switch (((F) ((F) f).X[i]).name) {
                        case F.ID: {
                            if (((Polynom) ((F) ((F) f).X[i]).X[0]).powers.length == 0) {
                                newX[i] = ((Element) Lpt.transformNumber());
                            } else {
                                newX[i] = ((Element) Lpt.transformPolynom());
                            }
                        }
                        case F.CH:
                            newX[i] = ((Element) Lpt.transformCh(ring));
                            break;
                        case F.SH:
                            newX[i] = ((Element) Lpt.transformSh(ring));
                            break;
                        case F.COS:
                            newX[i] = ((Element) Lpt.transformCos(ring));
                            break;
                        case F.SIN:
                            newX[i] = ((Element) Lpt.transformSin(ring));
                            break;
                        case F.EXP:
                            newX[i] = ((Element) Lpt.transformExp());
                            break;
                        case F.MULTIPLY:
                            newX[i] = transformMultiplyElement(((F) ((F) f).X[i]), ring);
                            //было        newX[i] = ((Element) new LaplaceTransform(((F) ((F) f).X[i])).transformNumberMultiplyExp());
                            break;
                        default:
                            newX[i] = ((Element) Lpt.transform(((F) ((F) f).X[i]), ring));
                    }
                } else {
                    newX[i] = Lpt.transformNumber();
                }
            }
            F ff = new F(((F) f).name, newX);
        } else {
            if (f instanceof Polynom) {
                return new LaplaceTransform(new F(f)).transformPolynom();
            } else {
                return new LaplaceTransform(new F(f)).transformNumber();
            }
        }
        return new F(((F) f).name, newX);
    }

    /**
     * Метод преобразования по формуле: UnitStep[a*t] => 1/p
     *
     * @return - Выражение вида: 1/p
     */
    public F transformUnitStep() {
        Ring ring = new Ring("C64[p]");
        if ((((Polynom) inputF.X[0]).coeffs[0]).signum() == 1) { //a>0
            return new F(F.DIVIDE, new F[]{new F().FfromNumber(ring.numberONE()),
                        new F(new Polynom("p", ring))}); //1/s
        }
        return null;
    }

    /**
     * Метод преобразования по формуле: UnitStep[a*t-b] => exp[-b*p/a]/p
     *
     * @return - Выражение вида: exp[-b*p/a]/p
     */
    public F transformUnitStepShift() {
        Ring ring = new Ring("C64[p]");
        if ((((Polynom) inputF.X[0]).coeffs[0]).equals(NumberR64.ONE)) {
            if ((((Polynom) inputF.X[0]).coeffs[1]).signum() == -1) {
                F arg1 = new F(new Polynom("p", ring));
                Polynom arg = ((Polynom) inputF.X[0]);
                F mult = new F(F.MULTIPLY,
                        new F[]{new F().FfromNumber(new Complex(arg.coeffs[1].doubleValue())), arg1}); //-b*s
                return new F(F.DIVIDE, new F[]{new F(F.EXP, mult),
                            new F(new Polynom("p", ring))}); //exp[-b*s]/s
            } else {
                return this.transformUnitStep();
            }
        }
        if (!(((Polynom) inputF.X[0]).coeffs[0]).equals(NumberR64.ONE)) {
            if ((((Polynom) inputF.X[0]).coeffs[1]).signum() == -1) {
                F arg1 = new F(new Polynom("p", ring));
                Polynom arg = ((Polynom) inputF.X[0]);
                F mult = new F(F.MULTIPLY,
                        new F[]{new F().FfromNumber(new Complex(arg.coeffs[1].doubleValue()
                            / arg.coeffs[0].doubleValue())), arg1}); //-[b/a]*s
                return new F(F.DIVIDE, new F[]{new F(F.EXP, mult),
                            new F(new Polynom("p", ring))}); //exp[-[b/a]*s]/s
            } else {
                return this.transformUnitStep();
            }
        }
        return null;
    }

    /**
     * Метод преобразования по формуле: 1-UnitStep[a*t-b] => (1-exp[-b*p/a])/p
     *
     * @return - Выражение вида: (1-exp[-b*p/a])/p
     */
    public F transformUnitStepShift1(Ring r) {
        Ring ring = new Ring("C64[p]");
        if (inputF.name == F.SUBTRACT) {
            if (inputF.X[0].isOne(r)) {
                Polynom p = new Polynom("p", ring);
                F dif = new F(F.DIVIDE, new Element[]{ring.numberONE, p});
                F f = new LaplaceTransform((F) inputF.X[1]).transformUnitStepShift();
                return new F(F.SUBTRACT, new Element[]{dif, f});
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    /**
     * Метод преобразования по формуле: 1+UnitStep[t-2*а]-2*UnitStep[t-а]=
     * (1-exp[-а*p])^2/p
     *
     * @param r - Кольцо
     * @return - Выражение вида: (1-exp[-а*p])^2/p
     */
    public F transformUnitStepShift2(Ring r) {
        Ring ring = new Ring("C64[p]");
        if (inputF.name == F.SUBTRACT) {
            if (((F) (inputF.X[1])).name == F.MULTIPLY) {
                F ff = ((F) (inputF.X[1]));
                if (ff.X[0].equals(r.numberONE.valOf(2, r), r)) {
                    if (ff.X[1] instanceof F) {
                        if (((F) ff.X[1]).name == F.UNITSTEP) {
                            if (((F) inputF.X[0]).name == F.ADD) {
                                F f = ((F) inputF.X[0]);
                                if (f.X[0].isOne(r)) {
                                    if (f.X[1] instanceof F) {
                                        if (((F) f.X[1]).name == F.UNITSTEP) {
                                            Element a = ((Polynom) ((F) f.X[1]).X[0]).coeffs[1].divide(r.numberONE.valOf(2, r), ring);
                                            Polynom p = new Polynom("p", ring);
                                            F exp = new F(F.EXP, a.multiply(p, ring));
                                            F f2 = new F(F.SUBTRACT, new Element[]{ring.numberONE, exp});
                                            F f3 = new F(F.intPOW, new Element[]{f2, NumberZ.TWO});
                                            return new F(F.DIVIDE, new Element[]{f3, p});
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Метод преобразования по формуле: UnitStep[t-а]-UnitStep[t-b] =>
     * exp[-a*p]-exp[-b*p]/p
     *
     * @param r - Кольцо
     * @return - Выражение вида: exp[-a*p]-exp[-b*p]/p
     */
    public F transformUnitStepShift3(Ring r) {
        Ring ring = new Ring("C64[p]");
        if (inputF.name == F.SUBTRACT) {
            F ff1 = ((F) (inputF.X[0]));
            F ff2 = ((F) (inputF.X[1]));
            if (ff1.name == F.UNITSTEP && ff2.name == F.UNITSTEP) {
                if ((ff1.X[0] instanceof Polynom) && (ff2.X[0] instanceof Polynom)) {
                    Polynom p1 = ((Polynom) ff1.X[0]);
                    Polynom p2 = ((Polynom) ff2.X[0]);
                    if ((p1.coeffs.length == 2) && (p2.coeffs.length == 2)) {
                        Element a = p1.coeffs[1];
                        Element b = p2.coeffs[1];
                        Polynom p = new Polynom("p", ring);
                        F exp1 = new F(F.EXP, a.multiply(p, ring));
                        F exp2 = new F(F.EXP, b.multiply(p, ring));
                        F f2 = new F(F.SUBTRACT, new Element[]{exp1, exp2});
                        return new F(F.DIVIDE, new Element[]{f2, p});
                    }
                }
            }
        }
        return null;
    }

    /**
     * Метод преобразования по формуле:
     * UnitStep[t-2*а]+UnitStep[t-2*b]-2*UnitStep[t-а-b] =>
     * (exp[-a*p]-exp[-b*p])^2/p
     *
     * @param r - Кольцо
     * @return - Выражение вида: (exp[-a*p]-exp[-b*p])^2/p
     */
    public F transformUnitStepShift4(Ring r) {
        Ring ring = new Ring("C64[p]");
        Element a = null;
        Element b = null;
        if (inputF.name == F.SUBTRACT) {
            if (((F) (inputF.X[0])).name == F.ADD) {
                F f = ((F) (inputF.X[0]));
                if (((F) f.X[0]).name == F.UNITSTEP && ((F) f.X[1]).name == F.UNITSTEP) {
                    Polynom p1 = (Polynom) (((F) f.X[0]).X[0]);
                    Polynom p2 = (Polynom) (((F) f.X[1]).X[0]);
                    if ((p1.coeffs.length == 2) && (p2.coeffs.length == 2)) {
                        a = p1.coeffs[1].divide(r.numberONE.valOf(2, r), r);
                        b = p2.coeffs[1].divide(r.numberONE.valOf(2, r), r);
                        if (((F) inputF.X[1]).name == F.MULTIPLY) {
                            F ff = ((F) (inputF.X[1]));
                            if (((((F) ff.X[1]).name == F.UNITSTEP) && (!(ff.X[0] instanceof F)))) {
                                if (ff.X[0].equals(r.numberONE.valOf(2, r), r)) {
                                    if (a.add(b, r).equals(((Polynom) ((F) ff.X[1]).X[0]).coeffs[1], r)) {
                                        Polynom p = new Polynom("p", ring);
                                        F exp1 = new F(F.EXP, a.multiply(p, ring));
                                        F exp2 = new F(F.EXP, b.multiply(p, ring));
                                        F exp12 = new F(F.SUBTRACT, new Element[]{exp1, exp2});
                                        F f3 = new F(F.intPOW, new Element[]{exp12, NumberZ.TWO});
                                        return new F(F.DIVIDE, new Element[]{f3, p});
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Метод преобразования по формуле:
     * t+[t-2*a]*UnitStep[t-2*а]+2*[t-a]*UnitStep[t-a] => (1-exp[-a*p])^2/p^2
     *
     * @param r - Кольцо
     * @return - Выражение вида: (1-exp[-a*p])^2/p^2
     */
    public F transformUnitStepShift5(Ring r) {
        Ring ring = new Ring("C64[p]");
        int k = 0;
        if (inputF.X.length == 3) {
            Element a1 = null;
            Element a2 = null;
            for (int i = 0; i < inputF.X.length; i++) {
                if (inputF.X[i] instanceof Polynom) {
                    if ((((Polynom) inputF.X[i]).coeffs.length == 1) && (((Polynom) inputF.X[i]).powers[0] == 1)) {
                        k++;
                    }
                }
                if (inputF.X[i] instanceof F) {
                    if (((F) inputF.X[i]).name == F.MULTIPLY) {
                        if (((F) inputF.X[i]).X.length == 2) {
                            Polynom p1 = null;
                            Polynom p2 = null;
                            for (int j = 0; j < ((F) inputF.X[i]).X.length; j++) {
                                if (((F) inputF.X[i]).X[j] instanceof Polynom) {
                                    p1 = (Polynom) ((F) inputF.X[i]).X[j];
                                }
                                if (((F) inputF.X[i]).X[j] instanceof F) {
                                    if (((F) ((F) inputF.X[i]).X[j]).name == F.UNITSTEP) {
                                        p2 = (Polynom) ((F) ((F) inputF.X[i]).X[j]).X[0];
                                    }
                                }
                            }
                            a1 = p1.coeffs[1].divide(r.numberONE.valOf(2, r), r);//2a/2 = a
                            if (p1.equals(p2, r)) {
                                k++;
                            }
                        } else {
                            Polynom p1 = null;
                            Polynom p2 = null;
                            boolean fl = false;
                            for (int j = 0; j < ((F) inputF.X[i]).X.length; j++) {
                                if (((F) inputF.X[i]).X[j] instanceof Polynom) {
                                    p1 = (Polynom) ((F) inputF.X[i]).X[j];
                                } else if (((F) inputF.X[i]).X[j] instanceof F) {
                                    if (((F) ((F) inputF.X[i]).X[j]).name == F.UNITSTEP) {
                                        p2 = (Polynom) ((F) ((F) inputF.X[i]).X[j]).X[0];
                                    }
                                } else {
                                    Element b = ((F) inputF.X[i]).X[j];
                                    if (b.equals(r.numberONE.valOf(2, r), r)) {
                                        fl = true;
                                    }
                                }
                            }
                            a2 = p1.coeffs[1];//a
                            if (p1.equals(p2, r) && fl) {
                                k++;
                            }
                        }
                    }
                }
            }
            if (a1.equals(a2, r) && (k == 3)) {
                Polynom p = new Polynom("p", ring);
                Element c = r.numberONE;
                F exp = new F(F.EXP, a1.multiply(p, ring));
                F exp1 = new F(F.SUBTRACT, new Element[]{c, exp});
                F f3 = new F(F.intPOW, new Element[]{exp1, NumberZ.TWO});
                Polynom p2 = new Polynom("p^2", ring);
                return new F(F.DIVIDE, new Element[]{f3, p2});
            }
        }
        return null;
    }

    /**
     * Метод преобразования по формуле:
     * [t-2*a]*UnitStep[t-2*а]+[t-2*b]*UnitStep[t-2*b]+2*[a+b-t]*UnitStep[t-a-b]
     * => (exp[-a*p]-exp[-b*p])^2/p^2
     *
     * @param r - Кольцо
     * @return - Выражение вида: (exp[-a*p]-exp[-b*p])^2/p^2
     */
    public F transformUnitStepShift6(Ring r) {
        Ring ring = new Ring("C64[p]");
        int k = 0;
        if (inputF.X.length == 2) {
            Element a = null;
            Element b = null;
            Element c = null;
            for (int i = 0; i < inputF.X.length; i++) {
                if (inputF.X[i] instanceof F) {
                    if (((F) inputF.X[i]).name == F.MULTIPLY) {
                        Polynom p1 = null;
                        Polynom p2 = null;
                        boolean fl = false;
                        for (int j = 0; j < ((F) inputF.X[i]).X.length; j++) {
                            if (((F) inputF.X[i]).X[j] instanceof Polynom) {
                                p1 = (Polynom) ((F) inputF.X[i]).X[j];
                            } else if (((F) inputF.X[i]).X[j] instanceof F) {
                                if (((F) ((F) inputF.X[i]).X[j]).name == F.UNITSTEP) {
                                    p2 = (Polynom) ((F) ((F) inputF.X[i]).X[j]).X[0];
                                }
                            } else {
                                c = ((F) inputF.X[i]).X[j];
                                if (c.equals(r.numberONE.valOf(2, r), r)) {
                                    fl = true;
                                }
                            }
                        }
                        if (p1.coeffs[1].equals(p2.coeffs[1], r) && fl) {
                            k++;
                        }
                    }
                    if (((F) inputF.X[i]).name == F.ADD) {
                        if (((F) inputF.X[i]).X.length == 2) {
                            F f1 = (F) ((F) inputF.X[i]).X[0];
                            F f2 = (F) ((F) inputF.X[i]).X[1];
                            Polynom p11 = (Polynom) ((F) (F) ((F) inputF.X[i]).X[0]).X[0];
                            Polynom p12 = (Polynom) ((F) ((F) (F) ((F) inputF.X[i]).X[0]).X[1]).X[0];
                            a = p11.coeffs[1].divide(r.numberONE.valOf(2, r), r);
                            if (p11.coeffs[1].equals(p12.coeffs[1], r)) {
                                k++;
                            }
                            Polynom p13 = (Polynom) ((F) (F) ((F) inputF.X[i]).X[1]).X[0];
                            Polynom p14 = (Polynom) ((F) ((F) (F) ((F) inputF.X[i]).X[1]).X[1]).X[0];
                            b = p13.coeffs[1].divide(r.numberONE.valOf(2, r), r);
                            if (p13.coeffs[1].equals(p14.coeffs[1], r)) {
                                k++;
                            }
                        }

                    }
                }
            }
            if (k == 3) {
                Polynom p = new Polynom("p", ring);
                F exp1 = new F(F.EXP, a.multiply(p, ring));
                F exp2 = new F(F.EXP, b.multiply(p, ring));
                F s = new F(F.SUBTRACT, new Element[]{exp1, exp2});
                F f3 = new F(F.intPOW, new Element[]{s, NumberZ.TWO});
                Polynom p2 = new Polynom("p^2", ring);
                return new F(F.DIVIDE, new Element[]{f3, p2});
            }
        }
        return null;
    }

    /**
     * Метод преобразования по формуле: [1-exp[a*b-b*t]*UnitStep[t-a]] =>
     * [b*exp[-a*p]/[p^2+b*p]]
     *
     * @param r - Кольцо
     * @return - Выражение вида: [b*exp[-a*p]/[p^2+b*p]]
     */
    public F transformUnitStepShift7(Ring r) {
        Ring ring = new Ring("C64[p]");
        Element a1 = null;
        Element a2 = null;
        Element b = null;
        int k = 0;
        if (inputF.name == F.SUBTRACT) {
            Element ff1 = inputF.X[0];
            F ff2 = ((F) (inputF.X[1]));
            if (ff1.isOne(r)) {
                k++;
            }
            if (ff2.name == F.MULTIPLY) {
                F ff3 = ((F) (ff2.X[0]));
                F ff4 = ((F) (ff2.X[1]));
                Polynom p2 = null;
                Polynom p3 = null;

                if (ff3.name == F.EXP) {
                    p2 = (Polynom) ff3.X[0];
                    b = p2.coeffs[0].negate(r);
                    a1 = p2.coeffs[1].divide(p2.coeffs[0], r);
                }
                if (ff4.name == F.UNITSTEP) {
                    p3 = (Polynom) ff4.X[0];
                    a2 = p3.coeffs[1];
                }
                if (p2.coeffs[1].divide(p2.coeffs[0], r).equals(a2, r)) {
                    k++;
                }
            }
        }
        if ((a1.equals(a2, r)) && (k == 2)) {
            Polynom p = new Polynom("p", ring);
            F exp1 = new F(F.EXP, a1.multiply(p, ring));
            F bexp = new F(F.MULTIPLY, new Element[]{b, exp1});
            Polynom p2 = new Polynom(new int[]{2, 1}, new Element[]{ring.numberONE, b});
            return new F(F.DIVIDE, new Element[]{bexp, p2});
        }
        return null;
    }

    /**
     * Метод преобразования по формуле: exp[a*b-b*t]*UnitStep[t-a] =>
     * exp[-a*p]/[p+b]
     *
     * @param r - Кольцо
     * @return - Выражение вида: exp[-a*p]/[p+b]
     */
    public F transformUnitStepShift8(Ring r) {
        Ring ring = new Ring("C64[p]");
        Element a1 = null;
        Element a2 = null;
        Element b = null;
        if (inputF.name == F.MULTIPLY) {
            F ff1 = ((F) (inputF.X[0]));
            F ff2 = ((F) (inputF.X[1]));
            if (ff1.name == F.EXP) {
                Polynom p1 = ((Polynom) ff1.X[0]);
                p1 = (Polynom) ff1.X[0];
                b = p1.coeffs[0];
                a1 = p1.coeffs[1].divide(b, r);
            }
            if (ff2.name == F.UNITSTEP) {
                Polynom p2 = (Polynom) ff2.X[0];
                a2 = p2.coeffs[1];
            }
            if (a1.equals(a2, r)) {
                Polynom p = new Polynom("p", ring);
                F exp1 = new F(F.EXP, a1.multiply(p, ring));
                Polynom p3 = new Polynom(new int[]{1, 0}, new Element[]{ring.numberONE, b});
                return new F(F.DIVIDE, new Element[]{exp1, p3});
            }
        }
        return null;
    }

    /**
     * Метод преобразования по формуле:
     * [t-a]*UnitStep[t-a]+[b-t]*UnitStep[t-b]=[exp[-a*p]-exp[-b*p]]/p^2
     *
     * @param r - Кольцо
     * @return - Выражение вида: [exp[-a*p]-exp[-b*p]]/p^2
     */
    public F transformUnitStepShift9(Ring r) {
        Ring ring = new Ring("C64[p]");
        Element a1 = null;
        Element a2 = null;
        Element b1 = null;
        Element b2 = null;
        int k = 0;
        if (inputF.name == F.SUBTRACT) {
            F ff1 = ((F) (inputF.X[0]));
            F ff2 = ((F) (inputF.X[1]));
            if (ff1.name == F.MULTIPLY) {
                Polynom p1 = (Polynom) ff1.X[0];
                a1 = p1.coeffs[1];
                if (((F) ff1.X[1]).name == F.UNITSTEP) {
                    Polynom p2 = (Polynom) ((F) ff1.X[1]).X[0];
                    a2 = p2.coeffs[1];
                    if (a1.equals(a2, r)) {
                        k++;
                    }
                }
            }
            if (ff2.name == F.MULTIPLY) {
                Polynom p3 = (Polynom) ff2.X[0];
                if (((F) ff2.X[1]).name == F.UNITSTEP) {
                    Polynom p2 = (Polynom) ((F) ff2.X[1]).X[0];
                    b2 = p3.coeffs[1];//neg
                    b1 = p2.coeffs[1];
                    if (b1.equals(b2, r)) {
                        k++;
                    }
                }
            }
            if (k == 2) {
                Polynom p = new Polynom("p", ring);
                F exp1 = new F(F.EXP, a2.multiply(p, ring));
                F exp2 = new F(F.EXP, b2.multiply(p, ring));
                F exp3 = new F(F.SUBTRACT, new Element[]{exp1, exp2});
                Polynom p2 = new Polynom("p^2", ring);
                return new F(F.DIVIDE, new Element[]{exp3, p2});
            }
        }
        return null;
    }

    /**
     * Метод преобразования по формуле: t-[t-a]*UnitStep[t-а]= [1-exp[-a*p]]/p^2
     *
     * @param r - Кольцо
     * @return - Выражение вида: [1-exp[-a*p]]/p^2
     */
    public F transformUnitStepShift10(Ring r) {
        Ring ring = new Ring("C64[p]");
        Element a1 = null;
        Element a2 = null;
        int k = 0;
        if (inputF.name == F.SUBTRACT) {
            Element ff1 = inputF.X[0];
            Element ff2 = inputF.X[1];
            if (ff1 instanceof Polynom) {
                if ((((Polynom) ff1).coeffs.length == 1) && (((Polynom) ff1).powers[0] == 1)) {
                    k++;
                }
            }
            if (ff2 instanceof F) {
                if (((F) ff2).name == F.MULTIPLY) {
                    Element ff3 = ((F) ff2).X[0];
                    Element ff4 = ((F) ff2).X[1];
                    if (ff3 instanceof Polynom) {
                        a1 = ((Polynom) ff3).coeffs[1];
                    }
                    if (ff4 instanceof F) {
                        if (((F) ff4).name == F.UNITSTEP) {
                            a2 = ((Polynom) ((F) ff4).X[0]).coeffs[1];
                        }
                    }
                }
            }
            if (a1.equals(a2, r) && k == 1) {
                Polynom p = new Polynom("p", ring);
                Element c = r.numberONE;
                F exp = new F(F.EXP, a1.multiply(p, ring));
                F exp1 = new F(F.SUBTRACT, new Element[]{c, exp});
                Polynom p2 = new Polynom("p^2", ring);
                return new F(F.DIVIDE, new Element[]{exp1, p2});
            }
        }
        return null;
    }
}
