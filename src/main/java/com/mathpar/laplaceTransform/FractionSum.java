/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform;

import com.mathpar.number.Ring;
import com.mathpar.func.F;
import com.mathpar.polynom.*;
import com.mathpar.number.*;
import java.util.*;

/**
 * @author Rybakov Michail
 * @version 1.0
 */
public class FractionSum extends Element {
    Ring r = new Ring();
    /**
     * массив полиномов, хранящийся в отсортированном виде. Сортировка
     * производится по возрастанию старших степеней.
     *
     * Например полиномы (x^6-8), (x^2+x-9), (x^12-9x^5+4x^2-8) будут храниться
     * в следующем порядке (x^2+x-9), (x^6-8), (x^12-9x^5+4x^2-8)
     *
     * массив спепеней, хранящийся в отсортированном виде. Сортировка
     * производится в зависимости от старших степеней масива полиномов и не
     * зависит от самих степеней
     */
    public FactorPol arg;
    //массив коэффициентов
    //вектор решений
    public Element[] coeff;

    public FractionSum() {
    }

    /**
     * конструктор от FactorPol и Element[]. Полиномы, передаваемые в него
     * должны стать внутренними полиномами факторизованного (свернутого)
     * полинома
     *
     * @param arg FactorPol
     * @param coeff массив значений коэффициентов
     */
    public FractionSum(FactorPol arg, Element[] coeff, Ring ring) {
        int k = coeff.length;
        this.arg = arg;
        this.coeff = new Element[k];
        for (int i = 0; i < k; i++) {
            this.coeff[i] = coeff[i];
        }
    }

    /**
     * конструктор от FactorPol и int[]. Полиномы, передаваемые в него должны
     * стать внутренними полиномами факторизованного (свернутого) полинома
     *
     * @param arg FactorPol
     * @param coeff массив значений коэффициентов
     */
    public static FractionSum[] toFractionSum(Element[] vectorLP, Element[] polInitCond, Ring ring) {
        FractionSum[] result = new FractionSum[vectorLP.length];
        for (int i = 0; i < vectorLP.length; i++) {
            ArrayList<Element> znPol = new ArrayList<Element>();
            IntList pow = new IntList();
            ArrayList<Element> ch = new ArrayList<Element>();
            if (vectorLP[i] instanceof F) {
                F f1 = ((F) vectorLP[i]);
                if (f1.name == F.DIVIDE) {
                    Element el1 = f1.X[0];
                    Element el2 = f1.X[1];
                    if (el2 instanceof F) {
                        F f2 = ((F) el2);
                        if (f2.name == F.intPOW) {
                            Element el3 = f2.X[0];
                            Element el4 = f2.X[1];
                            //Определение степени
                            int k = el4.intValue();
                            for (int j = 1; j < k - 1; j++) {
                                ch.add(ring.numberZERO);
                                znPol.add(el3);
                                pow.add(j);
                            }
                            ch.add(el1);
                            znPol.add(el3);
                            pow.add(k);
                        } else {
                            if (!el1.isZero(ring)) {
                                ch.add(el1);
                                znPol.add(f2.X[0]);
                                pow.add(1);
                            }
                        }
                    } else {
                        if (!el1.isZero(ring)) {
                            ch.add(el1);
                            znPol.add(el2);
                            pow.add(1);
                        }
                    }
                }
                if (f1.name == F.ADD) {
                    for (int j = 0; j < f1.X.length; j++) {
                        if (vectorLP[i] instanceof F) {
                            F f11 = ((F) f1.X[i]);
                            if (f11.name == F.DIVIDE) {
                                Element el1 = f11.X[0];
                                Element el2 = f11.X[1];
                                if (el2 instanceof F) {
                                    F f2 = ((F) el2);
                                    if (f2.name == F.intPOW) {
                                        Element el3 = f2.X[0];
                                        Element el4 = f2.X[1];
                                        //Определение степени
                                        int k = el4.intValue();
                                        for (int h = 1; h < k - 1; h++) {
                                            ch.add(ring.numberZERO);
                                            znPol.add(el3);
                                            pow.add(h);
                                        }
                                        ch.add(el1);
                                        znPol.add(el3);
                                        pow.add(k);
                                    } else {
                                        if (!el1.isZero(ring)) {
                                            ch.add(el1);
                                            znPol.add(f2.X[0]);
                                            pow.add(1);
                                        }
                                    }
                                } else {
                                    if (!el1.isZero(ring)) {
                                        ch.add(el1);
                                        znPol.add(el2);
                                        pow.add(1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!polInitCond[i].isZero(ring)) {
                ch.add(polInitCond[i]);
                znPol.add(new Polynom(ring.numberONE));
                pow.add(1);
            }
            Polynom[] a = new Polynom[znPol.size()];
            znPol.toArray(a);
            int[] b = new int[pow.size];
            System.arraycopy(pow.arr, 0, b, 0, pow.size);
            Element[] c = new Element[ch.size()];
            ch.toArray(c);
            FactorPol arg = new FactorPol(new int[] {1}, new Polynom[] {new Polynom(ring.numberONE)});
            Element[] coeff = new Element[] {ring.numberZERO};
            if (c.length != 0) {
                arg = new FactorPol(b, a);
                coeff = c;
            }
            result[i] = new FractionSum(arg, coeff, ring);
        }
        return result;
    }

    /**
     * возвращает результат умножения - (1/x + 2/y)(x/y)
     *
     * @param s FractionSum
     * @param t Rational
     *
     * @return RationalFactorPol[]
     */
    public RationalFactorPol[] multToRational(Fraction t, Ring ring) {
        int k = 0;
        int l = 0;
        int n = coeff.length; //размер
        int m1 = 0;
        int m2 = 0;
        RationalFactorPol[] rFP = new RationalFactorPol[n];
        for (int i = 0; i < n; i++) {
            Polynom p = new Polynom(new int[0], new Element[] {coeff[i]}); //полином из коэффициента
            Polynom ch = (p).mulSS((Polynom) t.num, ring); //числитель
            Polynom pt = new Polynom(new int[] {0},
                    new Element[] {r.numberONE}); //1
            Polynom var = new Polynom(new int[] {1},
                    new Element[] {r.numberONE}); //x либо y..полином первой степени
            if (((Polynom) t.denom).equals(pt) != true) {
                int[] pow = new int[2];
                if (i < arg.powers[m1]) {
                    pow[0] = m1 + 1 + i;
                } else {
                    m1++;
                    pow[0] = arg.powers[m1];
                }
                pow[1] = 1;
                Polynom[] coef = new Polynom[2];
                if (i < arg.powers[m2]) {
                    coef[0] = arg.multin[m2];
                } else {
                    m2++;
                    coef[0] = arg.multin[m2];
                }
                coef[1] = (Polynom) t.denom;
                if (coef[1].equals(var) == true) {
                    coef[1].powers = new int[2];
                    coef[1].powers[0] = 1;
                    coef[1].powers[1] = 0;
                    coef[1].coeffs = new Element[2];
                    coef[1].coeffs[0] = r.numberONE;
                    coef[1].coeffs[1] = r.numberZERO;
                }
                FactorPol zn = new FactorPol(pow, coef);
                rFP[i] = new RationalFactorPol(ch, zn);
            } else {
                if (arg.multin.length == n) {//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    int[] pow = new int[1];
                    pow[0] = 1;
                    Polynom[] coef = new Polynom[1];
                    coef[0] = arg.multin[i];
                    FactorPol zn = new FactorPol(pow, coef);
                    rFP[i] = new RationalFactorPol(ch, zn);
                } else {
                    int[] pow = new int[1];
                    pow[0] = arg.powers[k] - l;
                    Polynom[] coef = new Polynom[1];
                    coef[0] = arg.multin[k];
                    FactorPol zn = new FactorPol(pow, coef);
                    rFP[i] = new RationalFactorPol(ch, zn);
                    if (l + 1 == arg.powers[k]) {
                        k++;
                        l = 0;
                    } else {
                        l++;//как только счетчик по степеням будет равняться степени полинома
                    }
                }
            }
        }
        return rFP;
    }

    /**
     * Умножение 2 объектов вида:1/() + ...+ 2/()
     *
     * @param s1 FractionSum
     * @param s2 FractionSum
     *
     * @return FractionSum
     */
    public FractionSum multiplay(FractionSum s, Ring ring) {
        int k = this.coeff.length * s.coeff.length;
        Element[] coeff = new Element[k];
        int l = 0;
        int maxD = 0;
        int minD = 0;
        if (this.coeff.length > s.coeff.length) {
            maxD = this.coeff.length;
            minD = s.coeff.length;
        } else {
            maxD = s.coeff.length;
            minD = this.coeff.length;
        }
        if (this.coeff.length < s.coeff.length) {
            for (int i = 0; i < minD; i++) {
                for (int j = 0; j < maxD; j++) {
                    coeff[l] = this.coeff[i].multiply(s.coeff[j], ring);
                    l++;
                }
            }
        } else {
            for (int i = 0; i < maxD; i++) {
                for (int j = 0; j < minD; j++) {
                    coeff[l] = this.coeff[i].multiply(s.coeff[j], ring);
                    l++;
                }
            }
        }
        Polynom[] polFact = new Polynom[k];
        int[] powFact = new int[k];
        Polynom[] p1 = new Polynom[this.coeff.length];
        Polynom[] p2 = new Polynom[s.coeff.length];
        int l1 = 0;
        int l2 = 0;
        for (int i = 0; i < this.arg.multin.length; i++) {
            Element one = this.arg.multin[0].one(ring);
            Polynom pr1 = new Polynom(new int[0], new Element[] {new Complex(1, 0)});//one
            for (int j = 0; j < this.arg.powers[i]; j++) {
                pr1 = pr1.mulSS(this.arg.multin[i], ring);
                p1[l1] = pr1;
                l1++;
            }
        }
        for (int i = 0; i < s.arg.multin.length; i++) {
            Element one = s.arg.multin[0].one(ring);
            Polynom pr2 = new Polynom(new int[0], new Element[] {new Complex(1, 0)});//one
            for (int j = 0; j < s.arg.powers[i]; j++) {
                pr2 = pr2.mulSS(s.arg.multin[i], ring);
                p2[l2] = pr2;
                l2++;
            }
        }
        int l3 = 0;
        for (int i = 0; i < p1.length; i++) {
            for (int j = 0; j < p2.length; j++) {
                polFact[l3] = p1[i].mulSS(p2[j], ring);
                powFact[l3] = 1;
                l3++;
            }
        }
        FactorPol fact = new FactorPol(powFact, polFact);
        FractionSum frSum = new FractionSum(fact, coeff, ring);
        return frSum;
    }

    /**
     * Произведение суммы дробей на элемент
     *
     * @param el - произвольный элемент
     * @param ring - кольцо
     *
     * @return - FractiomSum
     */
    public FractionSum multiply(Polynom el, Ring ring) {
        Element[] c = new Element[this.coeff.length];
        for (int i = 0; i < this.coeff.length; i++) {
            c[i] = this.coeff[i].multiply(el, ring);
        }
        return new FractionSum(this.arg, c, ring);
    }

    public RationalFactorPol[] multiplay(FractionSum s1, FractionSum s2, Ring ring) {
        int k = s1.coeff.length * s2.coeff.length;
        Element[] coeff = new Element[k];
        int l = 0;
        int maxD = 0; //максимальная длина массива коэффициентов из 2-ух входных FractionSum
        int minD = 0; //минимальная длина массива коэффициентов из 2-ух входных FractionSum
        if (s1.coeff.length > s2.coeff.length) { //определяем maxD и minD
            maxD = s1.coeff.length;
            minD = s2.coeff.length;
        } else {
            maxD = s2.coeff.length;
            minD = s1.coeff.length;
        }
        for (int i = 0; i < maxD; i++) {
            for (int j = 0; j < minD; j++) {
                coeff[l] = s1.coeff[i].multiply(s2.coeff[j], ring);
                l++;
            }
        }
        Polynom[] polCoeffs = new Polynom[k]; //создаем массив полиномов
        for (int i = 0; i < k; i++) {
            polCoeffs[i] = new Polynom(new int[0], new Element[] {coeff[i]}); //формируем массив полиномов
        }
        int kolvs1 = 0;
        for (int i = 0; i < s1.arg.powers.length; i++) {
            kolvs1 += s1.arg.powers[i];
        }
        int kolvs2 = 0;
        for (int i = 0; i < s2.arg.powers.length; i++) {
            kolvs2 += s2.arg.powers[i];
        }
        int l1 = 0;
        FactorPol[] p1 = new FactorPol[kolvs1]; //массив FactorPol
        for (int i = 0; i < s1.arg.multin.length; i++) {
            Element one = s1.arg.multin[0].one(ring);
            Polynom pr1 = new Polynom(new int[0], new Element[] {one}); //1
            for (int j = 0; j < s1.arg.powers[i]; j++) {
                p1[l1] = new FactorPol(new int[] {j + 1},
                        new Polynom[] {s1.arg.multin[i]});
                l1++;
            }
        }
        int l2 = 0;
        FactorPol[] p2 = new FactorPol[kolvs2]; //массив FactorPol
        for (int i = 0; i < s2.arg.multin.length; i++) {
            Element one = s2.arg.multin[0].one(ring);
            Polynom pr1 = new Polynom(new int[0], new Element[] {one}); //1
            for (int j = 0; j < s2.arg.powers[i]; j++) {
                p2[l2] = new FactorPol(new int[] {j + 1},
                        new Polynom[] {s2.arg.multin[i]});
                l2++;
            }
        }
        FactorPol[] p3 = new FactorPol[p1.length * p2.length]; //массив FactorPol из произведения p1 и p2
        int l3 = 0;
        for (int i = 0; i < p1.length; i++) {
            for (int j = 0; j < p2.length; j++) {
                int[] pow = new int[2];
                pow[0] = p1[i].powers[0];
                pow[1] = p2[j].powers[0];
                Polynom[] pols = new Polynom[2];
                pols[0] = p1[i].multin[0];
                pols[1] = p2[j].multin[0];
                p3[l3] = new FactorPol(pow, pols);
                l3++;
            }
        }
        RationalFactorPol[] frSum = new RationalFactorPol[p3.length];
        for (int i = 0; i < frSum.length; i++) {
            frSum[i] = new RationalFactorPol(polCoeffs[i], p3[i]);
        }
        return frSum;
    }

    /**
     * // * Сложение 2 объектов вида:1/() + ...+ 2/()
     * недописано!!!!!!!!!!!!!!!! //
     *
     * @param s1 FractionSum //
     * @param s2 FractionSum //
     *
     * @return FractionSum //
     */
    public FractionSum add(FractionSum s, Ring ring) {

        Element[] k1 = new Element[coeff.length];//коэффициенты this
        System.arraycopy(coeff, 0, k1, 0, coeff.length);
        Element[] k2 = new Element[s.coeff.length];//коэффициенты s
        System.arraycopy(s.coeff, 0, k2, 0, s.coeff.length);
        IntList pow1 = new IntList();//для степеней
        Vector<Polynom> pol1 = new Vector<Polynom>();
        int h = 0;
        //приводим к нормальному виду this
        for (int i = 0; i < arg.powers.length; i++) {
            if (arg.powers[i] == 1) {
                pow1.arr[h] = arg.powers[i];
                pol1.add(arg.multin[i]);
                h++;
            } else {
                if (arg.powers.length != k1.length) {
                    for (int j = 1; j <= arg.powers[i]; j++) {
                        pow1.arr[h] = j;
                        pol1.add(arg.multin[i]);
                        h++;
                    }
                } else {
                    pow1.arr[h] = arg.powers[i];
                    pol1.add(arg.multin[i]);
                    h++;
                }
            }
        }
        //сборка нормальных FractionSum
        int[] pw1 = new int[h];
        System.arraycopy(pow1.arr, 0, pw1, 0, h);
        Polynom[] p1 = new Polynom[pol1.size()];
        pol1.copyInto(p1);

        h = 0;
        IntList pow2 = new IntList();//для степеней
        Vector<Polynom> pol2 = new Vector<Polynom>();
        //приводим к нормальному виду s
        for (int i = 0; i < s.arg.powers.length; i++) {
            if (s.arg.powers[i] == 1) {
                pow2.arr[h] = s.arg.powers[i];
                pol2.add(s.arg.multin[i]);
                h++;
            } else {
                if (s.arg.powers.length != k2.length) {
                    for (int j = 1; j <= s.arg.powers[i]; j++) {
                        pow2.arr[h] = j;
                        pol2.add(s.arg.multin[i]);
                        h++;
                    }
                } else {
                    pow2.arr[h] = s.arg.powers[i];
                    pol2.add(s.arg.multin[i]);
                    h++;
                }
            }
        }
        //сборка нормальных FractionSum
        int[] pw2 = new int[h];
        System.arraycopy(pow2.arr, 0, pw2, 0, h);
        Polynom[] p2 = new Polynom[pol2.size()];
        pol2.copyInto(p2);

        //шаг сравнения №1 поиск совпадающих
        int l = 0;
        Element[] masK = new Element[k1.length + k2.length];
        int[] maspow = new int[pw1.length + pw2.length];
        Polynom[] maspol = new Polynom[p1.length + p2.length];
        for (int i = 0; i < p1.length; i++) {
            for (int j = 0; j < p2.length; j++) {
                if ((p1[i] != null) && (p2[j] != null)) {
                    if ((p1[i].equals(p2[j])) && (pw1[i] == pw2[j])) {
                        masK[l] = k1[i].add(k2[j], ring);
                        maspow[l] = pw1[i];
                        maspol[l] = p1[i];
                        k2[j] = null;
                        p2[j] = null;
                        pw2[j] = 0;
                        k1[i] = null;
                        p1[i] = null;
                        pw1[i] = 0;
                        l++;
                    }
                }
            }
        }

        //шаг сборки №2 //дозакидывание
        for (int i = 0; i < p1.length; i++) {
            if (p1[i] != null) {
                masK[l] = k1[i];
                maspow[l] = pw1[i];
                maspol[l] = p1[i];
                l++;
            }
        }
        for (int i = 0; i < p2.length; i++) {
            if (p2[i] != null) {
                masK[l] = k2[i];
                maspow[l] = pw2[i];
                maspol[l] = p2[i];
                l++;
            }
        }


        //шаг сборки №3 поиск совпадающих с разными степенями
        Element[] masK1 = new Element[k1.length + k2.length];
        int[] maspow1 = new int[pw1.length + pw2.length];
        Polynom[] maspol1 = new Polynom[p1.length + p2.length];
        int t = 0;
        int m = 0;
        for (int i = 0; i < maspol.length; i++) {
            for (int j = 0; j < maspol.length; j++) {
                if ((i != j) && (maspol[i] != null) && (maspol[j] != null)) {
                    if (maspol[i].equals(maspol[j])) {
                        if (maspow[i] != maspow[j]) {
                            if (maspow[i] > maspow[j]) {
                                maspol1[m] = maspol[i];
                                maspow1[m] = maspow[i];
                                masK1[t] = masK[j];
                                masK1[t + 1] = masK[i];
                                maspol[j] = null;
                                maspol[i] = null;
                                maspow[j] = 0;
                                maspow[i] = 0;
                                masK[i] = null;
                                masK[j] = null;
                                t += 2;
                                m++;
                            } else {
                                maspol1[m] = maspol[j];
                                maspow1[m] = maspow[j];
                                masK1[t] = masK[i];
                                masK1[t + 1] = masK[j];
                                maspol[i] = null;
                                maspol[j] = null;
                                maspow[i] = 0;
                                maspow[j] = 0;
                                masK[i] = null;
                                masK[j] = null;
                                t += 2;
                                m++;
                            }
                        }
                    }
                }
            }
        }


//дозакидываем коэффициенты
        int t1 = 0;
        for (int i = 0; i < masK.length; i++) {
            if ((masK[i] != null) && (!masK[i].isZero(ring))) {
                masK1[t1] = masK[i];
                t1++;
            }
        }
        int m1 = 0;
        for (int i = 0; i < maspow.length; i++) {
            if ((maspow[i] != 0) && (!masK[i].isZero(ring))) {
                maspow1[m1] = maspow[i];
                m1++;
            }
        }
        int m2 = 0;
        for (int i = 0; i < maspol.length; i++) {
            if ((maspol[i] != null) && (!masK[i].isZero(ring))) {
                maspol1[m2] = maspol[i];
                m2++;

            }
        }
        /////////////////////////////////////////////////////////////
        IntList v1 = new IntList();
        Vector<Polynom> v2 = new Vector<Polynom>();
        Vector<Element> v3 = new Vector<Element>();
        ////////////////////////////////////////////////////////////
        int k = 0;
        for (int i = 0; i < maspow1.length; i++) {
            if (maspow1[i] != 0) {
                v1.arr[k] = maspow1[i];
                k++;
            }
        }
        k = 0;
        for (int i = 0; i < maspol1.length; i++) {
            if (maspol1[i] != null) {
                v2.add(maspol1[i]);
                k++;
            }
        }
        k = 0;
        for (int i = 0; i < masK1.length; i++) {
            if (masK1[i] != null) {
                v3.add(masK1[i]);
                k++;
            }
        }
        int[] a1 = new int[v2.size()];
        System.arraycopy(v1.arr, 0, a1, 0, v2.size());
        Polynom[] a2 = new Polynom[v2.size()];
        v2.copyInto(a2);
        Element[] a3 = new Element[v3.size()];
        v3.copyInto(a3);
        return new FractionSum(new FactorPol(a1, a2), a3, ring);
    }

    /**
     * вывод FractionSum в разложенном виде
     *
     * @return полином в строковом виде
     */
    @Override
    public String toString(Ring ring) {
        StringBuilder res = new StringBuilder();
        Polynom[] masPol = new Polynom[coeff.length];
        int shift = 0;
        for (int i = 0; i < arg.multin.length; i++) {
            Polynom pr = Polynom.polynom_one(ring.numberONE);
            if (arg.powers[i] == 1) {
                masPol[shift] = arg.multin[i];
                shift++;
            } else {
                for (int j = 0; j < arg.powers[i]; j++) { //цикл по степеням полиномов в FactorPol
                    pr = pr.mulSS(arg.multin[i], ring);
                    masPol[shift] = pr;
                    shift++;
                }
            }
        }
        for (int h1 = 0; h1 < coeff.length; h1++) {
            if (h1 == coeff.length - 1) {
                // if(!coeff[h1].isZero(ring))
                res.append(coeff[h1].toString(ring) + " / " + "("
                        + masPol[h1].toString(ring) + ")");
            } else {
                //  if(!coeff[h1].isZero(ring))
                res.append(coeff[h1].toString(ring) + " / " + "("
                        + masPol[h1].toString(ring) + ") " + " + ");
            }
        }
        return res.toString();
    }

    /**
     * вывод FractionSum в разложенном виде
     *
     * @return полином в строковом виде
     */
    public String toStrings(Ring ring) {
        StringBuilder res = new StringBuilder();
        if (arg.multin.length != coeff.length) {
            int k = 0;
            for (int i = 0; i < arg.multin.length; i++) {
                int st = 0;
                for (int j = 0; j < arg.powers[i]; j++) {
                    st = 1 + j;
                    if (st == 1) {
                        if (k == coeff.length - 1) {
                            if (!coeff[k].isZero(ring)) {
                                res.append(coeff[k].toString(ring) + "/" + "("
                                        + arg.multin[i].toString(ring) + ")");
                            }
                        } else {
                            if (!coeff[k].isZero(ring)) {
                                res.append(coeff[k].toString(ring) + "/" + "("
                                        + arg.multin[i].toString(ring) + ")"
                                        + " + ");
                            }
                        }
                    } else {
                        if (k == coeff.length - 1) {
                            if (!coeff[k].isZero(ring)) {
                                res.append(coeff[k].toString(ring) + "/" + "("
                                        + arg.multin[i].toString(ring) + ")^"
                                        + st);
                            }
                        } else {
                            if (!coeff[k].isZero(ring)) {
                                res.append(coeff[k].toString(ring) + "/" + "("
                                        + arg.multin[i].toString(ring) + ")^"
                                        + st
                                        + " + ");
                            }
                        }
                    }
                    k++;
                }
            }
        }
        if (arg.multin.length == coeff.length) {
            for (int i = 0; i < arg.multin.length; i++) {
                if (i != arg.multin.length - 1) {
                    if (!coeff[i].isZero(ring)) {
                        res.append(coeff[i].toString(ring) + "/" + "("
                                + arg.multin[i].toString(ring) + ")^"
                                + arg.powers[i]
                                + " + ");
                    }
                } else {
                    if (!coeff[i].isZero(ring)) {
                        res.append(coeff[i].toString(ring) + "/" + "("
                                + arg.multin[i].toString(ring) + ")^"
                                + arg.powers[i]);
                    }

                }

            }
        }
        return res.toString();
    }

    //процедура пересборки FractionSum в случаях когда существуют нулевые числители
    public FractionSum deletZeroArg(Ring ring) {
        //поиск нулевых числителей
        int k = 0;
        for (int i = 0; i < this.coeff.length; i++) {
            if (this.coeff[i].isZero(ring)) {
                k++;
            }
        }
        if (k == 0) {
            return this; //если не один коэффициент в числителе не равен 0
        }
        Element[] s = new Element[this.coeff.length - k];
        Polynom[] pol = new Polynom[this.coeff.length - k];
        int[] pow = new int[this.coeff.length - k];
        int t = 0;
        int q = 0;
        for (int i = 0; i < this.arg.multin.length; i++) {
            for (int j = 0; j < this.arg.powers[i]; j++) {
                if (!this.coeff[t].isZero(ring)) {
                    s[q] = this.coeff[t];
                    pol[q] = this.arg.multin[i];
                    pow[q] = j + 1;
                    t++;
                    q++;
                } else {
                    t++;
                }
            }
        }
        FactorPol fr = new FactorPol(pow, pol);
        //проверка на совпадение полиномов в fr
        int h = 0;
        for (int i = 0; i < fr.multin.length - 1; i++) {
            if (fr.multin[i].equals(fr.multin[i + 1])) {
                fr.multin[i + 1] = null;
                if (fr.powers[i] < fr.powers[i + 1]) {
                    fr.powers[i] = fr.powers[i + 1];
                } else {
                    fr.powers[i] = fr.powers[i];
                }
                fr.powers[i + 1] = 0;
                h++;
            }
        }
        if (h != 0) {
            Polynom[] pm = new Polynom[fr.multin.length - h];
            int[] sm = new int[fr.powers.length - h];
            int l = 0;
            for (int i = 0; i < fr.multin.length - 1; i++) {
                if (fr.multin[i] != null) {
                    pm[l] = fr.multin[i];
                }
                if (fr.powers[i] != 0) {
                    sm[l] = fr.powers[i];
                }
                l++;
            }
            fr = new FactorPol(sm, pm);
        }
        return new FractionSum(fr, s, ring);
    }

//    public FractionSum jointSort(FractionSum fs, Ring ring){
//
//    }
    /**
     * процедура упращения FractionSum, в случае когда в аргумнетах стоят схожие
     * полиномы
     *
     * @return
     */
    public FractionSum cancel(FractionSum fs, Ring ring) {
        if (fs.arg.multin.length == 1) {
            if (fs.arg.multin[0].equals(fs.coeff[0], ring)) {
                if (fs.arg.powers[0] == 1) {
                    fs.arg.multin[0] = new Polynom(ring.numberONE);
                    fs.coeff[0] = ring.numberONE;
                } else {
                    fs.coeff[0] = ring.numberONE;
                    fs.arg.powers[0] = fs.arg.powers[0] - 1;
                }
            }
        } else {
            for (int i = 0; i < fs.arg.multin.length; i++) {
                for (int j = 0; j < fs.coeff.length; j++) {
                    if (fs.arg.multin[i] != null) {
                        if (fs.arg.multin[i].equals(fs.coeff[j], ring)) {
                            if (fs.arg.powers[i] == 1) {
                                fs.arg.multin[i] = null;
                            } else {
                                fs.coeff[j] = null;
                                fs.arg.powers[i] = fs.arg.powers[i] - 1;
                            }
                        }
                    }
                }
            }
        }
        Vector<Polynom> v = new Vector<Polynom>();
        for (int i = 0; i < fs.arg.multin.length; i++) {
            if (fs.arg.multin[i] != null) {
                v.add(fs.arg.multin[i]);
            }
        }
        Vector<Element> c = new Vector<Element>();
        for (int i = 0; i < fs.coeff.length; i++) {
            if (fs.coeff[i] != null) {
                c.add(fs.coeff[i]);
            }
        }
        Polynom[] pp = new Polynom[v.size()];
        v.copyInto(pp);
        FactorPol factP = new FactorPol(fs.arg.powers, pp);
        Element[] cc = new Element[c.size()];
        c.copyInto(cc);
        return new FractionSum(factP, cc, ring);
    }

    /**
     * процедура упращения FractionSum, в случае когда в аргумнетах стоят схожие
     * полиномы
     *
     * @return
     */
    public FractionSum simplify(FractionSum fs, Ring ring) {
        for (int i = 0; i < fs.arg.multin.length; i++) {
            for (int j = 0; j < fs.arg.multin.length; j++) {
                if ((i != j) && (fs.arg.multin[i] != null) && (fs.arg.multin[j] != null)) {
                    if (fs.arg.multin[i].equals(fs.arg.multin[j])) {
                        fs.arg.multin[j] = null;
                        fs.coeff[i] = fs.coeff[i].add(fs.coeff[j], ring);
                        fs.coeff[j] = null;
                    }
                }
            }
        }
        Vector<Polynom> v = new Vector<Polynom>();
        for (int i = 0; i < fs.arg.multin.length; i++) {
            if (fs.arg.multin[i] != null) {
                v.add(fs.arg.multin[i]);
            }
        }
        Vector<Element> c = new Vector<Element>();
        for (int i = 0; i < fs.coeff.length; i++) {
            if (fs.coeff[i] != null) {
                c.add(fs.coeff[i]);
            }
        }
        Polynom[] pp = new Polynom[v.size()];
        v.copyInto(pp);
        FactorPol factP = new FactorPol(fs.arg.powers, pp);
        Element[] cc = new Polynom[c.size()];
        c.copyInto(cc);
        return new FractionSum(factP, cc, ring);
    }

    @Override
    public boolean equals(Element x, Ring r) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public F toF() {
        return new F(F.ID, new Element[] {this});
    }

    public Element toNumber(Element oneOfNewType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int compareTo(Element o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(Element x, Ring ring) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean isZero(Ring ring) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean isOne(Ring ring) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
