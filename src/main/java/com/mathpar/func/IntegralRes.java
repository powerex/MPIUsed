/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 *
 * @author IntegralRes Yulya
 */
/** Данный класс позволяет вычислять определенные интегралы для некоторого
 * класса функций с помощью вычетов. В частности вычисляются
 *
 * 1) определенные
 * интегралы по замкнутому контуру - окружности - для функций комплексного
 * переменного, представляющих собой:
 * а) отношение двух полиномов;
 * б) отношении объекта типа F (экспоненты, sin, cos) к полиному;
 *
 * 2) Имеется метод, позволяющий вычислять
 * несобственные интегралы с пределами интегрирования -\infinity и \infinity
 * для функции, представляющей собой отношение двух полиномов,  причем степень
 * знаменателя доджна быть больше степени числителя не менее, чем на 2.
 */
public class IntegralRes {

    /** Для вычисления вычетов функции f(x), представляющей собой отношение двух
     * полиномов p1 и p2, в комплексной точке a, являющейся полюсом функции f(x)
     * порядка n, в кольце "C64[x]" используется метод resForFrac(p1, p2, a, n, ring).
     *
     * f(x) = (x+1)/(x^2+1)
     * a = \i  (полюс)
     * n = 1  (порядок)
     * ring = C64[x]
     * r = Res_a f(x)  (искомый вычет)
     *
     * out:
     * r = -0.5\i+0.5  (вычет указанной функции в точке а)
     */
    public static Complex resForFrac(Polynom p1, Polynom p2, Complex a, int n, Ring ring) {
        Polynom p1Con = p1.conjugate(ring);
        Complex[] coeffs = {Complex.C64_ONE, a.negate(ring)};
        int[] powers = {1, 0};
        Polynom p = new Polynom(powers, coeffs, ring);
        Polynom pDeg = (Polynom) p.powBinS(n, ring); //возводит полином в степень типа int
        Polynom pDegCon = pDeg.conjugate(ring);
        Polynom pol = p1.multiply(p1Con, ring).multiply(pDeg, ring).multiply(pDegCon, ring);
        Fraction f = new Fraction(pol, p2);
        Element  ef = f.cancel(ring); 
        if(ef instanceof Fraction) f=(Fraction)ef; 
           else f=new Fraction(ef,ring.numberONE);
        Polynom p_num = (Polynom) f.num;
        Polynom p_den = (f.denom instanceof Polynom)?(Polynom) f.denom: new Polynom(f.denom);
        p_num = p_num.divideExact(p1Con, ring);
        Fraction r = new Fraction(p_num, p_den.multiply(pDegCon, ring));
        Element Dr = r;
        for (int i = 0; i < n - 1; i++) {
            Dr = Dr.D(ring);
        }
        Fraction DDr = ((Fraction) Dr);
        Dr = DDr.value(new Element[]{a}, ring);
        Element fact = new NumberZ(1);
        fact = fact.factorial(ring, n - 1);
        return (Complex) Dr.divide(fact, ring);
    }

    /** Для вычисления вычетов функции f(x), представляющей собой отношение
     * тригонометрической функции или экспоненты f к полиному p, в комплексной
     * точке a, являющейся полюсом функции f(x) порядка n, в кольце "C64[x]"
     * используется метод resForFunc(f, p, a, n, ring).
     *
     * f(x) = \exp(3x)/((x^2-1)(x^2-4))
     * a = 1
     * n = 1
     * ring = C64[x]
     * r = Res_a f(x)
     *
     * out:
     * r = -\exp(6)/6
     */

    public static Complex resForFunc(F f, Polynom p, Complex a, int n, Ring ring) {
        Complex[] coeffs = {Complex.C64_ONE, a.negate(ring)};
        int[] powers = {1, 0};
        Polynom pol = new Polynom(powers, coeffs, ring);
        Polynom pDeg = (Polynom) pol.powBinS(n, ring);
        Fraction frac = new Fraction(pDeg, p);
       // frac = frac.cancel(ring);
        Element  ef = frac.cancel(ring); if(ef instanceof Fraction) frac=(Fraction)ef; else frac=new Fraction(ef,ring.numberONE);


        F f_denom = new F(frac.denom);
        Fraction fract=new Fraction(f,f_denom);
        Element ff1 = fract.D(n-1, ring);
        Element denom =NumberZ.ONE; Element num=null;
        if(ff1 instanceof Fraction){ Fraction ff=(Fraction)ff1;
           num = ((F) ff.num).valueOf(new Element[]{a}, ring);
           denom = ((F) ff.denom).valueOf(new Element[]{a}, ring);}
        else num =  ff1.value(new Element[]{a}, ring);
        if (denom.isNegative()) {denom=denom.negate(ring); num=num.negate(ring);}
        Element fact = NumberZ.ONE;

        fact = fact.factorial(ring, n - 1);
        Complex den = (Complex) denom.multiply(fact, ring);
        return new Complex(num.divide(den, ring), ring);
    }

    /** Чтобы вычилить несобственный интеграл с пределами интегрирования -\infinity и
     * \infinity функции f(x), представляющей собой отношение
     * двух полиномов p1 и p2, в кольце "C64[x]" используется метод
     * IntegralResFrac(p1, p2, ring).
     *
     * f(x) = x^2/(x^4+6x^2+25)
     * ring = C64[x]
     * h = Integral_{-\infinity}^{\infinity}(x^2/(x^4+6x^2+25))
     *
     * out:
     * h = \pi*0.25
     */
    public static F IntegralResFrac(Polynom p1, Polynom p2, Ring ring) {
        Polynom pp = new Polynom("x", ring);
        pp = pp.deleteZeroCoeff(ring);
        FactorPol a = p2.factorOfPol_inC(ring);
        Polynom[] pols = new Polynom[a.multin.length];
        for (int i = 0; i < pols.length; i++) {
            pols[i] = a.multin[i];
        }
        Complex[] mas = new Complex[pols.length];
        Complex[] comp = new Complex[pols.length];
        int k = 0;
        Complex ress[] = new Complex[a.multin.length];
        for (int i = 0; i < a.multin.length; i++) {
            ress[i] = Complex.C64_ZERO;
        }
        for (int i = 0; i < a.multin.length; i++) {
            mas[i] = (Complex) (pols[i].subtract(pp, ring)).coeffs[0].negate(ring);
        }
        for (int i = 0; i < a.multin.length; i++) {
            if ((!(mas[i].im).isNegative()) && (!(mas[i].im).isZero(ring))) {
                k++;
                comp[i] = resForFrac(p1, p2, mas[i], a.powers[i], ring);
            } else {
                comp[i] = Complex.C64_ZERO;
            }
        }
        if (k == 0) {
            return new F(NumberR64.ZERO);
        }
        Complex sum = Complex.C64_ZERO;
        for (int i = 0; i < a.multin.length; i++) {
            sum = (Complex)sum.add(comp[i], ring);
        }
        sum = (Complex)sum.multiply(new Complex(0, 1), ring).multiply(new Complex(2, 0), ring);
        F f1 = new F("\\pi", ring);
        F result = (F) f1.multiply(sum, ring);
        return result;
    }

    /** Для вычисления определенного интеграла функции f(x), представляющей
     * собой дробь frac, по замкнутому контуру  - окружности с центром в комплексной
     * точке c и радиусом numb используется метод
     * IntegralResFrac2(frac, c, numb, ring), где ring - здесь кольцо C64[x].
     *
     * f(x) = (x+1)/(x^2+1)
     * L - замкнутый контур - окружность:
     * с = 0     (центр окружности на комплексной плоскости)
     * numb = 2  (радиус окружновти )
     * ring = C64[x]
     * h = Integral_L((x+1)/(x^2+1))
     *
     * out:
     * h = 2*\pi*\i
     */
    public static F IntegralResFrac2(Fraction frac, Complex c, NumberR64 numb, Ring ring) {
        F f = new F(frac.num);
        Polynom p = (Polynom) frac.denom;
        Polynom pp = new Polynom("x", ring);
        pp = pp.deleteZeroCoeff(ring);
        FactorPol a = p.factorOfPol_inC(ring);
        Polynom[] pols = new Polynom[a.multin.length];
        for (int i = 0; i < pols.length; i++) {
            pols[i] = a.multin[i].deleteZeroCoeff(ring);
        }
        Complex[] mas_res = new Complex[pols.length];
        Element[] mod = new Element[pols.length];
        Element[] tt = new Complex[pols.length];
        Complex[] comp = new Complex[pols.length];
        int k = 0;
        for (int i = 0; i < a.multin.length; i++) {
            mas_res[i] = (Complex) (pols[i].subtract(pp, ring)).coeffs[0].negate(ring);
            tt[i] =  mas_res[i].subtract(c, ring);
            mod[i] = ((Complex) tt[i]).abs(ring);
            if (mod[i].compareTo(numb, -2, ring)) {
                k++;
                if (frac.num instanceof Polynom) {
                    Polynom pol = (Polynom) frac.num;
                    comp[i] = resForFrac(pol, p, mas_res[i], a.powers[i], ring);
                } else {
                    comp[i] = resForFunc(f, p, mas_res[i], a.powers[i], ring);
                }
            } else {
                comp[i] = Complex.C64_ZERO;
            }
        }
        if (k == 0) {
            return new F(NumberR64.ZERO);
        }
        Element sum = Complex.C64_ZERO;
        for (int i = 0; i < a.multin.length; i++) {
            sum =  sum.add(comp[i], ring);
        }
        F f1 = new F("2\\pi\\i", ring);
        F result = (F) f1.multiply(sum, ring);
        return result;
    }

//    /**
//     * Для нахождения производной  n-1 порядка  для функции f(x), представляющей
//     * собой дробь, используется метод DfracF.
//     *
//     * f(x) = \exp(3x)/((x^2-1)(x^2-4))
//     * n = 1
//     * h = \D(f)
//     *
//     * out:
//     * h = \exp(3x)/((x^2-1)(x^2-4))
//     *
//     */
//    public static F DfracF1(F f1, F f2, int n, Ring ring) {
//        for (int i = 0; i < n - 1; i++) {
//            F first = (F) f1.D(ring);
//            F second = (F) f2.D(ring);
//            F d = (F) (first.multiply(f2, ring)).subtract(second.multiply(first, ring), ring);
//            F f3 = (F) second.multiply(second, ring);
//            f1 = d;
//            f2 = f3;
//        }
//        return new F(F.DIVIDE, new Element[]{f1, f2});
//    }

    public static void main(String args[]) {
//        Ring ring = new Ring("C64[x]");
//        ring.FLOATPOS = 2;
//        Polynom pol11 = new Polynom("1.0x+1.0", ring);
//        Polynom pol12 = new Polynom("1.0x^2+1.0", ring);
//        Complex centr = Complex.C64_ZERO;
//        Fraction fff = new Fraction(pol11, pol12);
// //       System.out.println("IntegralResFrac2(x+1, x^2+1, 0, new NumberR64(2.0))=  "
// //               + IntegralResFrac2(fff, centr, new NumberR64(2.0), ring));
//        Polynom pol1 = new Polynom("1.0x^2", ring);
//        Polynom pol2 = new Polynom("1.0x^4+6.0x^2+25.0", ring);
//        System.out.println("IntegralResFrac(x^2, x^4+6x^2+25, C64[x])=  "
//                + IntegralResFrac(pol1, pol2, ring));
//        F f = new F("\\exp(3x)", ring);
//        Polynom pp = new Polynom("(x^2-1)(x^2-4)", ring);
//        Complex c = new Complex(3, 0);
//        NumberR64 numb = new NumberR64(3.0);
//        Element el = (Element) f;
//        Fraction ff = new Fraction(el, pp);
        
        
   Ring ring = new Ring("Q[x,y,c$_1,c$_2,c$_3,c$_4,c$_5,c$_6,c$_7,c$_8,c$_9]");
        Polynom p = new Polynom("(-1/4)c$_3x+(((-1/2)\\i )/4)c$_3+(-1/4)c$_2x+(((1/2)\\i )/4)c$_2", ring);
        p.coeffs[0] = new Fraction(new NumberZ("-1"), new NumberZ("4"));
        p.coeffs[1] = new Fraction(new Complex(ring.numberZERO, new Fraction(new NumberZ("-1"), new NumberZ("2"))), new NumberZ("4"));
        p.coeffs[2] = new Fraction(new NumberZ("-1"), new NumberZ("4"));
        p.coeffs[3] = new Fraction(new Complex(ring.numberZERO, new Fraction(new NumberZ("1"), new NumberZ("2"))), new NumberZ("4"));
      Element fp=  p.factorOfPol_inQ(false, ring);     
        
        
        System.out.println("IntegralResFrac2(\\exp(3x),(x^2-1)(x^2-4), 3, 3.0)=  "+fp.toString(ring));
            //    + IntegralResFrac2(ff, c, numb, ring));
    }
}
