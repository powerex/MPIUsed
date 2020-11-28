/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import com.mathpar.number.*;
import com.mathpar.number.math.MathContext;
import com.mathpar.number.math.RoundingMode;

class FuncNumberCQ {

    MathContext mc;
    int accuracy;
    Ring ring;
    MathContext mc_old;

//Notebook notebook;
    public FuncNumberCQ(Ring r, MathContext mc) {
        this.ring = r;
        accuracy = r.getAccuracy();
        this.mc = mc;
    }

    public FuncNumberCQ(Ring r) {
        this.ring = r;
        accuracy = r.getAccuracy();
        mc = new MathContext(accuracy, RoundingMode.HALF_EVEN);
    }

    /**
     * Add the number of exact decimal places
     *
     * @param numbOfDecFig --extra number of exact decimal places
     */
    public void addAccuracy(int numbOfDecFigures) {
        accuracy += numbOfDecFigures;
        ring.setAccuracy(accuracy);
        mc_old = mc;
        mc = new MathContext(accuracy, RoundingMode.HALF_EVEN);
    }

    public void subAccuracy(int numbOfDecFigures) {
        accuracy -= numbOfDecFigures;
         ring.setAccuracy(accuracy);
        mc = mc_old;
    }

    public Element trigFunc(int funcNumb, Element angle) {
        Element x = null;
        if (angle instanceof Fraction) {
            x = trigFunc_Rad(funcNumb, (Fraction) angle);
        }
        if (angle instanceof NumberZ) {
            x = trigFunc_Grad(funcNumb, (NumberZ) angle);
        }
        if (x == null) {
            return new F(funcNumb, angle);
        }
        return x;
    }

    /**
     * функция возвращает значение:
     *
     * @param k-множитель при \Pi
     * @return градусы
     */
    public Element trigFunc_Rad(int funcNumb, Fraction rad) {
        Fraction hh = (Fraction) rad.multiply(new NumberZ(180), ring);
        if (hh.denom.equals(NumberZ.ONE)) {
            NumberZ gg = new NumberZ(hh.num.intValue());
            return trigFunc_Grad(funcNumb, gg);
        }
        return null;


    }

    /**
     * функция возвращает значение: если его первая переменная синус то значение
     * синуса от градуса если его первая переменная косинус то значение косинуса
     * от градуса если его первая переменная тангенс то значение тангенса от
     * градуса если его первая переменная котангенс то значение котангенса син
     * от градуса
     *
     * @param фнкция и градусы
     * @return значение функции
     */
    public Element trigFunc_Grad(int funcNumb, NumberZ degreeZ) {
        Element sqrt_2_div_2 = new F(F.DIVIDE, new Element[]{new F(F.SQRT, new Element[]{new Fraction(2)}), new NumberZ(2)});
        Element sqrt_3_div_2 = new F(F.DIVIDE, new Element[]{new F(F.SQRT, new Element[]{new Fraction(3)}), new NumberZ(2)});
        Element neg_sqrt_2_div_2 = ((F) sqrt_2_div_2.negate(ring)).expand(ring);
        Element neg_sqrt_3_div_2 = ((F) sqrt_3_div_2.negate(ring)).expand(ring);
        Element sqrt_3_div_3 = new F(F.DIVIDE, new Element[]{new F(F.SQRT, new Element[]{new Fraction(3)}), new NumberZ(3)});
        Element sqrt_3 = new F(F.DIVIDE, new Element[]{new F(F.SQRT, new Element[]{new Fraction(3)}), new NumberZ(1)});
        Element neg_sqrt_3 = ((F) sqrt_3.negate(ring)).expand(ring);
        Element neg_sqrt_3_div_3 = ((F) sqrt_3_div_3.negate(ring)).expand(ring);
        int degree = degreeZ.remainder(new NumberZ(360)).intValue();
        if (degree < 0) {
            degree += 360;
        }
        switch (funcNumb) {
            case F.SIN:
                switch (degree) {
                    case 0:
                    case 180:
                        return new Fraction(NumberZ.ZERO, NumberZ.ONE);
                    case 30:
                    case 150:
                        return new Fraction(1, 2);
                    case 45:
                    case 135:
                        return sqrt_2_div_2;
                    case 60:
                    case 120:
                        return sqrt_3_div_2;
                    case 90:
                        return new Fraction(NumberZ.ONE, NumberZ.ONE);
                    case 210:
                    case 330:
                        return new Fraction(-1, 2);
                    case 225:
                    case 315:
                        return neg_sqrt_2_div_2;
                    case 240:
                    case 300:
                        return neg_sqrt_3_div_2;
                    case 270:
                        return new Fraction(NumberZ.MINUS_ONE, NumberZ.ONE);
                    default:
                        return null;
                }
            case F.COS:
                switch (degree) {
                    case 0:
                        return new Fraction(NumberZ.ONE, NumberZ.ONE);
                    case 180:
                        return new Fraction(NumberZ.MINUS_ONE, NumberZ.ONE);
                    case 30:
                    case 330:
                        return sqrt_3_div_2;
                    case 45:
                    case 315:
                        return sqrt_2_div_2;
                    case 60:
                    case 300:
                        return new Fraction(1, 2);
                    case 90:
                    case 270:
                        return new Fraction(NumberZ.ZERO, NumberZ.ONE);
                    case 120:
                    case 240:
                        return new Fraction(-1, 2);
                    case 135:
                    case 225:
                        return neg_sqrt_2_div_2;
                    case 150:
                    case 210:
                        return neg_sqrt_3_div_2;
                    default:
                        return null;
                }
            case F.TG:
                switch (degree) {
                    case 0:
                    case 180:
                        return new Fraction(NumberZ.ZERO, NumberZ.ONE);
                    case 30:
                    case 210:
                        return sqrt_3_div_3;
                    case 45:
                    case 225:
                        return new Fraction(NumberZ.ONE, NumberZ.ONE);
                    case 60:
                    case 240:
                        return sqrt_3;
                    case 90:
                        return NumberR.POSITIVE_INFINITY;
                    case 270:
                        return NumberR.NEGATIVE_INFINITY;
                    case 120:
                    case 300:
                        return neg_sqrt_3;
                    case 135:
                    case 315:
                        return new Fraction(NumberZ.MINUS_ONE, NumberZ.ONE);
                    case 150:
                    case 330:
                        return neg_sqrt_3_div_3;
                    default:
                        return null;
                }
            case F.CTG:
                switch (degree) {
                    case 0:
                        return NumberR.POSITIVE_INFINITY;
                    case 180:
                        return NumberR.NEGATIVE_INFINITY;
                    case 30:
                    case 210:
                        return sqrt_3;
                    case 45:
                    case 225:
                        return new Fraction(NumberZ.ONE, NumberZ.ONE);
                    case 60:
                    case 240:
                        return sqrt_3_div_3;
                    case 90:
                    case 270:
                        return new Fraction(NumberZ.ZERO, NumberZ.ONE);
                    case 120:
                    case 300:
                        return neg_sqrt_3_div_3;
                    case 135:
                    case 315:
                        return new Fraction(NumberZ.MINUS_ONE, NumberZ.ONE);
                    case 150:
                    case 330:
                        return neg_sqrt_3;
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    /**
     * Вычисление PI по формуле PI=16*arctg(1/5)-4*arctg(1/239)
     */
    public Fraction pi() {
        NumberR Pi = (new FuncNumberR(ring)).pi();
        Fraction PPi = new Fraction(Pi, NumberR.ONE);
        return PPi;
    }

//    public Fraction[] pi_() {
//        Fraction pi0 = PI();                                      //вычисляем PI
//        Fraction pi2 = pi0.multiply(new Fraction(2),ring);         //вычисляем 2*PI
//        Fraction pi_2 = //new NumberQ
//                (Fraction) (pi0.divide(new Fraction(2),ring));       // вычисляем PI/2
//        return new Fraction[]{pi0, pi2, pi_2};
//    }
    /**
     * вычисляем точность
     */
    public Fraction epsFromSCALE() {
        ring.setAccuracy(6);
        NumberZ eps1 = new NumberZ(10);
        for (int i = 0; i < ring.getAccuracy() - 1; i++) {
            eps1 = eps1.multiply(NumberZ.POSCONST[10]);
        }
        Fraction eps = new Fraction(NumberZ.ONE, eps1);
        return eps;
    }

    /* Вычисление тригонометрических функций sin(x), cos(x), tg(x), ctg(x)
     для  NumberCQ */
    /**
     * функция вычисления синуса на отрезке [0,2pi] с помощью разложения в ряд
     * Тейлора Sin(x) = x-x^3/3!+ x^5/5!- x^7/7!+…+(-1)^n+1*x^2n+1/(2n+1)!
     *
     * @param x NumberCQ аргумент
     * @param Notebook.SCALE Integer заданное количество цифр после запятой
     * @return Sin(x) NumberCQ результат вычисления
     */
    public Complex sin1(Complex x) {
        NumberZ n = new NumberZ(2);
        Fraction two = new Fraction(2);
        Complex y = new Complex(new Fraction(NumberZ.ONE, NumberZ.ONE), new Fraction(NumberZ.ZERO, NumberZ.ONE));                    //задаем первый член ряда
        Complex f = new Complex(new Fraction(NumberZ.ZERO, NumberZ.ONE), new Fraction(NumberZ.ZERO, NumberZ.ONE));                    //задаём начальное значение суммы ряда
        int sign = -1;  //знак
        Complex z = x;
        Complex step = (Complex)z.multiply(z, ring); //задаём начальное значение степени
        y = (Complex)y.multiply(z, ring);
        f = (Complex)f.add(y, ring);
        //формируем необходимую точность

        Fraction eps = epsFromSCALE();
        //разложение в ряд x-x^3/3!+x^5/5!-...
        while ((eps.compareTo(y.re, ring) == -1) || (eps.compareTo(y.im, ring) == -1)) //пока y>eps2
        {
            Fraction zn = new Fraction(n.multiply(n.add(NumberZ.ONE)), NumberZ.ONE);
            y = (Complex) y.multiply(step.divide(zn, ring), ring);                           //вычисляем один член ряда
            if (sign > 0) {                                      //проверка знака члена ряда
                f = (Complex)f.add(y, ring);
            } //накапливаем сумму (возможны два варианта в зависимости от знака)
            else {
                f = (Complex)f.subtract(y, ring);
            }
            //fak=fak.multiply(n.add(NumberR.ONE)).multiply(n.add(two));//формируем факториала
            sign = -sign;                                     //смена знака
            //step=step.multiply(z).multiply(z);          //формируем степень
            n = (NumberZ) n.add(two, ring);
        }
        return f;
    }

    /**
     * Функция вычисления косинуса на отрезке [0,2?] с помощью разложения в ряд
     * Тейлора Cos(x) = 1-x2/2!+ x4/4!- x6/6!+…+(-1)n*x2n/(2n)!
     *
     * @param x NumberC аргумент
     * @param Notebook.SCALE Integer заданное количество цифр после запятой
     * @return Cos(x) NumberC результат вычисления
     */
    public Complex cos1(Complex x) {
        NumberZ n = new NumberZ(1);
        Fraction two = new Fraction(2);
        Complex y = new Complex(new Fraction(NumberZ.ONE, NumberZ.ONE), new Fraction(NumberZ.ZERO, NumberZ.ONE));                                //задаём первый член ряда
        Complex f = new Complex(new Fraction(NumberZ.ZERO, NumberZ.ONE), new Fraction(NumberZ.ZERO, NumberZ.ONE));                                //задаём начальное значение суммы ряда
        int sign = -1;                                                     //знак
        //NumberR fak=NumberR.ONE;
        Complex zc = x;
        Complex step = (Complex)zc.multiply(zc, ring);                               //задаём начальное значение степени
        //формируем необходимую точность
        NumberZ eps1 = new NumberZ(10);
        for (int i = 0; i < ring.getAccuracy() - 1; i++) {
            eps1 = eps1.multiply(NumberZ.POSCONST[10]);
        }
        Fraction eps = new Fraction(NumberZ.ONE, eps1);
        //разложение в ряд 1-x^2/2!+x^4/4!-...
        while ((eps.compareTo(y.re, ring) == -1) || (eps.compareTo(y.im, ring) == -1)) //пока y>eps2
        {
            NumberZ zn = n.multiply(n.add(NumberZ.ONE));
            //fak=fak.multiply(n.subtract(NumberR.ONE),mc).multiply(n,mc);//задаём начальное значение факториала
            //NumberC fakt=new NumberC(fak,NumberR.ZERO);
            y = (Complex) y.multiply(step.divide(zn, ring), ring);                                        //вычисляем один член ряда
            if (sign > 0) {                                                   //проверка знака члена ряда
                f = (Complex)f.add(y, ring);
            } //накапливаем сумму (возможны два варианта в зависимости от знака
            else {
                f = (Complex)f.subtract(y, ring);
            }
            sign = -sign;                                                //смена знака
            //step=step.multiply(zc,mc).multiply(zc,mc);                     //формируем степень
            n = (NumberZ) n.add(two, ring);
        }
        return f;
    }

    /**
     * функция вычисления тангенса
     *
     * @param x NumberC аргумент
     * @param Notebook.SCALE Integer заданное количество цифр после запятой
     * @return tan(x) NumberC результат вычислений
     */
    public Complex tg(Complex x) {
        Complex t = (Complex)sin1(x).divide(cos1(x), ring);
        return t;
    }

    /**
     * функция вычисления котангенса
     *
     * @param x NumberC аргумент
     * @param Notebook.SCALE Integer заданное количество цифр после запятой
     * @return ctg(x) NumberC результат вычислений
     */
    public Complex ctg(Complex x) {
        Complex ct = (Complex)cos1(x).divide(sin1(x), ring);
        return ct;
    }

    /**
     * Вычисление обратных тригонометрических функций
     */
    /**
     * Вычисление арктангенса Метод вычисляет значение арктангенса с помощью
     * ряда Тейлора при -1<х<1 : x-x^3/3+x^5/5-x^7/7+.... при x>1 и x<-1 :
     * П/2-(1/x-1/3*x^3+1/5*x^5-1/7*x^7+.....)
     *

     *
     * @param Notebook.SCALE Integer заданное количество цифр после запятой
     * @param x NumberCQ аргумент
     * @return ArcTan(x) NumberCQ результат вычислений
     */
    /**
     * Метод вычисляет arctg(x), где 0<=x<=1
     */
    public Fraction atan1(Fraction x) {
        ring.setAccuracy(ring.getAccuracy()+5);
        Fraction y = new Fraction(1, 1);                //задаём первый член ряда
        Fraction f = new Fraction(0, 1);                //задаём начальное значение суммы ряда
        double znak = 1;                                   //знак
        Fraction zn = new Fraction(1);               //задаём начальное значение знаменатиля
        Fraction z = x;                                  //задаём начальное значение степени
        Fraction two = new Fraction(2);                //формируем необходимую точность
        Fraction eps = epsFromSCALE();           //разложение в ряд 1-x^3/3+x^5/5-...
        while (eps.compareTo(y.abs(ring), ring) == -1) //пока y>eps2
        {
            y = (Fraction) z.divide(zn, ring);                            //вычисляем один член ряда
            if (znak > 0) {                                  //проверка знака члена ряда
                f = (Fraction) f.add(y, ring);
            } //накапливаем сумму
            else {
                f = (Fraction) f.subtract(y, ring);
            }
            znak = znak * (-1);                                //смена знака
            z = (Fraction) z.multiply(x, ring).multiply(x, ring);             //формируем степень
            zn = (Fraction) zn.add(two, ring);
        }                            //формируем знаменатель
        return f;
    }
}
