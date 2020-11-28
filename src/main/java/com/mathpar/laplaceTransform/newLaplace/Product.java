/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform.newLaplace;

import com.mathpar.func.F;
import java.util.ArrayList;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 * Класс для хранения объектов: P(t)/Q(t) в виде произведения
 *
 * P(t),Q(t) - объекты типа Element. Каждый полином и объект типа Number в таком
 * объекте хранится в FactorPol Сортировка по элементам
 *
 *
 * @author Rybakov.M.A. & Smirnov R.
 * @years 2012
 */
public class Product extends Element {
    Ring ring = new Ring();
    //множители
    public ArrayList<Element> multin = new ArrayList<Element>();
    //степени
    public ArrayList<Element> powers = new ArrayList<Element>();

    public Product() {
    }

//    public Product(Element el, Ring r) {
//        if (el instanceof F) {
//            new Product((F) el, r);
//        } else {
//            multin.add(el);
//            powers.add(r.numberONE);
//        }
//    }
    /**
     * Сюда приходит только умножение или степень
     *
     * @param f
     * @param r
     */
//    public Product(F f, Ring r) {
//        ring = r;
//        if (f.name == F.intPOW | f.name == F.POW) {
//            multin.add(f.X[0]);
//            powers.add(f.X[1]);
//            return;
//        }
//        if (f.name == F.MULTIPLY) {
//            for (int i = 0; i < f.X.length; i++) {
//                if (f.X[i] instanceof F) {
//                    if (((F) f.X[i]).name == F.POW | ((F) f.X[i]).name == F.intPOW) {
//                        multin.add(((F) f.X[i]).X[0]);
//                        powers.add(((F) f.X[i]).X[1]);
//                    } else {
//                        multin.add(f.X[i]);
//                        powers.add(r.numberONE);
//                    }
//                } else {
//                    multin.add(f.X[i]);
//                    powers.add(r.numberONE);
//                }
//            }
//            sorting(ring);
//            return;
//        }
//        multin.add(f);
//        powers.add(r.numberONE);
//    }
    public Product(Element el, Ring r) {
        if (el instanceof F) {
            ring = r;
            Product temp = new Product((F) el, r);
            this.powers = temp.powers;
            this.multin = temp.multin;
        } else {
            if (el instanceof FactorPol) {
                multin.add(el);
                powers.add(r.numberONE);
            } else {
                multin.add(new FactorPol(el, ring));
                powers.add(r.numberONE);
            }
        }
    }

    /**
     * Сюда приходит только умножение или степень
     *
     * @param f
     * @param r
     */
    public Product(F f, Ring r) {
        ring = r;
        if (f.name == F.intPOW | f.name == F.POW) {
            this.multin.add(f.X[0]);
            this.powers.add(f.X[1]);
            return;
        }
        if (f.name == F.MULTIPLY) {
            for (int i = 0; i < f.X.length; i++) {
                if (f.X[i] instanceof F) {
                    if (((F) f.X[i]).name == F.POW | ((F) f.X[i]).name == F.intPOW) {
                        this.multin.add(((F) f.X[i]).X[0]);
                        this.powers.add(((F) f.X[i]).X[1]);
                    } else {
                        this.multin.add(f.X[i]);
                        this.powers.add(r.numberONE);
                    }
                } else {
//         if(!f.X[i].isZero(ring)){
                    this.multin.add(f.X[i]);
                    this.powers.add(r.numberONE);
//         }
                }
            }
            Product p = new Product(multin, powers, r);
            p.sorting(ring);
            multin = p.multin;
            powers = p.powers;
        } else {
            this.multin.add(f);
            this.powers.add(r.numberONE);
        }

    }

    public void fillVectors(Element el, ArrayList<Element> mult[], Element mnog, int i) {
        switch (el.numbElementType()) {
            case Ring.F: {
                Element new_p = ((F) el).X[0].multiply(powers.get(i), ring);
                F newExp = null;
                if (new_p instanceof Polynom) {
                    ((Polynom) new_p).coeffs[0] = ((Polynom) new_p).coeffs[0].abs(ring);
                    newExp = new F(F.EXP, new Element[] {new_p});
                } else {
                    newExp = new F(F.EXP, new Element[] {new_p.abs(ring)});
                }
                if (new_p.isNegative()) {
                    mult[1].add(newExp);
                    mult[5].add(ring.numberMINUS_ONE);
                } else {
                    mult[0].add(newExp);
                    mult[4].add(ring.numberONE);
                }
                break;
            }

            case Ring.Polynom: {
                if (((Polynom) el).isItNumber()) {
                    if (powers.get(i).isNegative()) {
                        mnog = mnog.divide(el.pow(powers.get(i).abs(ring).intValue(), ring), ring);
                    } else {
                        mnog = mnog.multiply(el.pow(powers.get(i).abs(ring).intValue(), ring), ring);
                    }
                } else {
                    if (powers.get(i).isNegative()) {
                        mult[3].add(el);
                        mult[7].add(powers.get(i));
                    } else {
                        mult[2].add(el);
                        mult[6].add(powers.get(i));
                    }
                }
                break;
            }
            default: {
                if (powers.get(i).isNegative()) {
                    mnog = mnog.divide(el.pow(powers.get(i).abs(ring).intValue(), ring), ring);
                } else {
                    mnog = mnog.multiply(el.pow(powers.get(i).abs(ring).intValue(), ring), ring);
                }
            }
        }

    }

//    /**
//     * Конструктор от функция типа F - divide
//     * @param f
//     * @param ring
//     */
//    public Product(F f, Ring ring) {
//        this.ring = ring;
//        if (f.name == F.DIVIDE) {
//            this.multin.add(f.X[0]);
//            this.powers.add(new NumberZ(1));
//            this.multin.add(f.X[1]);
//            this.powers.add(new NumberZ(1));
//        } else {
//            if (f.name == F.intPOW || f.name == F.POW) {
//                this.multin.add(f.X[0]);
//                this.powers.add(f.X[1]);
//            }
//        }
//    }
    /**
     * Конструктор от элемента типа Fraction
     *
     * @param fr
     * @param ring
     */
    public Product(Fraction fr, Ring ring) {
        this.ring = ring;
        this.multin.add(fr.num);
        this.powers.add(ring.numberONE);
        this.multin.add(fr.denom);
        this.powers.add(ring.numberMINUS_ONE);
    }

    /**
     *
     * @param m - объекты типа Element
     * @param p - степени
     * @param ring
     */
    public Product(ArrayList<Element> m, ArrayList<Element> p, Ring r) {
        ring = r;
        if (m.size() != p.size()) {
            System.out.println("Error !!!    IndexOutOfBoundsException");
        }
        ArrayList<Element> pol = new ArrayList<Element>();//временное хранилище для полиномов
        IntList pow = new IntList();//временное хранилище для степеней полиномов
        ArrayList<Element> pol_factor = new ArrayList<Element>();//временное хранилище для FactorPol
        IntList pow__factor = new IntList();//временное хранилище для степеней FactorPol
        for (int i = 0; i < m.size(); i++) {
            switch (m.get(i).numbElementType()) {
                case Ring.Polynom: {
                    if (!m.get(i).isZero(ring)) {
                        pol.add(m.get(i));
                        pow.add(p.get(i).intValue());
                    }
                    break;
                }
                case Ring.FactorPol: {
                    pol_factor.add(m.get(i));
                    pow__factor.add(p.get(i).intValue());
                    break;
                }
                case Ring.F: {
                    if (!m.get(i).isZero(ring)) {
                        multin.add(m.get(i));
                        powers.add(p.get(i));
                    }
                    break;
                }
                default: {//для Number
                    if (!m.get(i).isZero(ring)) {
                        pol.add(Polynom.polynomFromNumber(m.get(i), r));
                        pow.add(p.get(i).intValue());
                    }
                }
            }
        }
        if ((!pol.isEmpty()) && (pow.size != 0)) {
            Polynom[] pol1 = new Polynom[pol.size()];
            pol.toArray(pol1);
            int[] pow1 = new int[pow.size];
            System.arraycopy(pow.arr, 0, pow1, 0, pow.size);
            FactorPol fp = new FactorPol(pow1, pol1);
            fp.normalFormInField(r);
            //нормализация FactorPol
            for (int j = 0; j < pol_factor.size(); j++) {
                for (int k = 0; k < ((FactorPol) pol_factor.get(j)).multin.length; k++) {
                    ((FactorPol) pol_factor.get(j)).powers[k] = ((FactorPol) pol_factor.get(j)).powers[k] * pow__factor.arr[j];
                }
            }
            FactorPol mult = fp;
            if (pol_factor.size() == 1) {
                if (((FactorPol) pol_factor.get(0)).multin.length == 1) {
                    if (((FactorPol) pol_factor.get(0)).powers[0] == -1) {
                        Polynom[] ps = new Polynom[mult.multin.length + ((FactorPol) pol_factor.get(0)).multin.length];
                        int[] pw = new int[ps.length];
                        System.arraycopy(mult.multin, 0, ps, 0, mult.multin.length);
                        System.arraycopy(mult.powers, 0, pw, 0, mult.powers.length);
                        ps[ps.length - 1] = ((FactorPol) pol_factor.get(0)).multin[0];
                        pw[pw.length - 1] = ((FactorPol) pol_factor.get(0)).powers[0];
                        mult = new FactorPol(pw, ps);
                    } else {
                        for (int h = 0; h < pol_factor.size(); h++) {
                            mult = (FactorPol) (mult.multiply(pol_factor.get(h), r));
                        }
                    }
                } else {
                    for (int h = 0; h < pol_factor.size(); h++) {
                        mult = (FactorPol)mult.multiply(pol_factor.get(h), r);
                    }
                }
            } else {
                for (int h = 0; h < pol_factor.size(); h++) {
                    mult = (FactorPol)mult.multiply(pol_factor.get(h), r);
                }
            }
            mult.normalFormInField(r);
            multin.add(mult);
            powers.add(r.numberONE);
        }
        if (!pol_factor.isEmpty()) {
            FactorPol f_fp = (FactorPol) pol_factor.get(0);
            for (int i = 1; i < pol_factor.size(); i++) {
                f_fp = multiply(f_fp, (FactorPol) pol_factor.get(i), ring);
            }
            f_fp.normalFormInField(ring);
            if (pol_factor.size() > 1) {
                multin.add(f_fp);
                powers.add(r.numberONE);
            }
        }
        ring = r;
        sorting(ring);
    }

    /**
     * умножение на свернутый полином. Вызывается метод multyconvolute, в
     * который передаются полином и 1, означающая, что при идентичности степени
     * будут складываться
     *
     * @param P полином-множитель
     *
     * @return произведение полиномов
     */
    public FactorPol multiply(FactorPol a, FactorPol b, Ring ring) {
        int[][] pow = new int[1][0];
        Element[] res = com.mathpar.number.Array.jointUp(a.multin, b.multin, a.powers, b.powers, pow, ring);
        Polynom[] res1 = new Polynom[res.length];
        int[] pow1 = new int[res.length];
        for (int i = 0; i < res.length; i++) {
            res1[i] = (Polynom) res[i];
            pow1[i] = pow[0][i];
        }
        return new FactorPol(pow1, res1);
    }

    /**
     * Конструктор от массива элементов в некоторых степенях
     *
     * @param m
     * @param p
     * @param ring
     */
    public Product(Element[] m, Element[] p, Ring ring) {
        ArrayList<Element> pol = new ArrayList<Element>();//временное хранилище для полиномов
        IntList pow = new IntList();//временное хранилище для степеней полиномов
        for (int i = 0; i < m.length; i++) {
            if (m[i] instanceof Polynom) {
                pol.add(m[i]);
                pow.add(p[i].intValue());
            } else {
                this.multin.add(m[i]);
                this.powers.add(p[i]);
            }
        }
        Polynom[] pol1 = new Polynom[pol.size()];
        pol.toArray(pol1);
        int[] pow1 = new int[pow.size];
        System.arraycopy(pow.arr, 0, pow1, 0, pow.size);
        FactorPol fp = new FactorPol(pow1, pol1);
        if (fp.multin.length == 2) {
            if (fp.multin[0].powers.length == 0) {
                if (!(fp.multin[0].coeffs[0] instanceof Complex)) {
                    fp.normalFormInField(ring);
                }
            } else {
                fp.normalFormInField(ring);
            }
        } else {
            fp.normalFormInField(ring);
        }
        this.multin.add(fp);
        this.powers.add(ring.numberONE);
        sorting(ring);
    }

    /**
     * Перемножает два числа типа NumberR64 в степенях
     *
     * @param a
     * @param b
     * @param k
     * @param n
     *
     * @return
     */
    public Element multR64(Element a, Element b, int k, int n) {
        Element m = new NumberR64(1);
        for (int i = 0; i < k; i++) {
            m = m.multiply(a, ring);
        }
        for (int j = 0; j < n; j++) {
            m = m.multiply(b, ring);
        }
        return m;
    }

    public FactorPol normFactorPol(FactorPol fp, Ring ring) {
        ArrayList<Element> multin1 = new ArrayList<Element>();
        IntList powers1 = new IntList();
        Element el = ring.numberONE();
        int k = 0;
        int m = 1;
        for (int i = 0; i < fp.multin.length; i++) {
            if (fp.multin[i].numbElementType() < 60) {
                el = el.multiply(fp.multin[i], ring);
                m = m * fp.powers[i];
            } else {
                multin1.add(fp.multin[i]);
                powers1.arr[k] = fp.powers[i];
                k++;
            }
        }
        multin1.add(el);
        powers1.arr[k] = m;
        Polynom[] pol = new Polynom[multin1.size()];
        multin1.toArray(pol);
        int[] pow = new int[powers1.size];
        System.arraycopy(powers1.arr, 0, pow, 0, powers1.size);
        FactorPol f = new FactorPol(pow, pol);
        f.normalForm(ring);
        return f;
    }

    /**
     * Приведение объекта Product к нормальному виду: 1 - 5^{3}*6^{7}Числа типа
     * R,...,Z. 2 - e^{2}*e^{3} = e^{5} в одной степени 3 - полиномы в виде
     * FactorPol 4 - a^{b} * a^{c} = a^{b+c} показательные функции в одной
     * степени с одинаковыми основаниями 5 - Функции F.DIVIDE и объекты Fraction
     * в виде : 1) f[0]^{1}*f[1]^{-1} 2) num^{1}*denom^{-1} 6 -
     * 2.34^{3}*3,14^{2} = ... Числа типа NumberR64 перемножаются
     *
     * @param ring
     */
    public void normalForm(Ring ring) {
        ArrayList<Element> multin1 = new ArrayList<Element>();
        ArrayList<Element> powers1 = new ArrayList<Element>();
        int temp = 0;
        if (multin.get(0) instanceof FactorPol) {
            multin1.add(multin.get(0));//перекидываем FactorPol
            powers1.add(powers.get(0));
            temp = 1;
        } else {
            temp = 0;
        }
        for (int i = temp; i < multin.size(); i++) {
            for (int j = temp; j < multin.size(); j++) {
                if (i != j) {
                    if ((multin.get(i) != null) && (multin.get(j) != null)) {
                        if ((multin.get(i) instanceof F) && (multin.get(j) instanceof F)) {
                            if ((multin.get(i)).compareTo(multin.get(j), ring) == 0) {
                                switch (((F) multin.get(i)).name) {
                                    case F.EXP: {
                                        Element el = powers.get(i).add(powers.get(j), ring);
                                        multin.set(i, multin.get(i));
                                        powers.set(i, el);
                                        multin.set(j, null);
                                        break;
                                    }
                                    case F.intPOW: {
                                        Element el1 = ((F) multin.get(i)).X[1].multiply(powers.get(i), ring);
                                        Element el2 = ((F) multin.get(i)).X[1].multiply(powers.get(i), ring);
                                        Element el = el1.add(el2, ring);
                                        multin.set(i, new F(F.intPOW, new Element[] {((F) multin.get(i)).X[0], el}));
                                        powers.set(i, ring.numberONE);
                                        multin.set(j, null);
                                        break;
                                    }
                                    case F.POW: {
                                        Element el1 = ((F) multin.get(i)).X[1].multiply(powers.get(i), ring);
                                        Element el2 = ((F) multin.get(i)).X[1].multiply(powers.get(i), ring);
                                        Element el = el1.add(el2, ring);
                                        multin.set(i, new F(F.intPOW, new Element[] {((F) multin.get(i)).X[0], el}));
                                        powers.set(i, ring.numberONE);
                                        multin.set(j, null);
                                        break;
                                    }
                                    default: {
                                        multin1.add(multin.get(i));
                                        powers1.add(powers.get(i).add(powers.get(j), ring));
                                        multin.set(j, null);
                                        multin.set(i, null);
                                    }
                                }
                            }
                        } else {
                            if ((multin.get(i) instanceof FactorPol) && (multin.get(j) instanceof FactorPol)) {//случай когда у нас 2 FactorPol
                                if ((multin.get(i)).compareTo(multin.get(j), ring) == 0) {
                                    multin1.add(multin.get(i).multiply(multin.get(j), ring));
                                    powers1.add(ring.numberONE);
                                    multin.set(j, null);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = temp; i < multin.size(); i++) {
            if (multin.get(i) != null) {
                multin1.add(multin.get(i));
                powers1.add(powers.get(i));
            }
        }
        multin = multin1;
        powers = powers1;
    }

    public void sort(ArrayList<Element> arr, ArrayList<Element> pow, int i, int j) {
        Element t = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, t);
        Element p = pow.get(i);
        pow.set(i, pow.get(j));
        pow.set(j, p);
    }

    /**
     * Сортировка элементво объекта Product На 1 позиции всегда FactorPol потом
     * все объекты типа F, сортировка по именам функции.
     *
     * @param ring
     */
    public void sorting(Ring ring) {
        for (int i = multin.size() - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                if (multin.get(j).numbElementType() < multin.get(j + 1).numbElementType()) {
                    sort(multin, powers, j, j + 1);
                } else {
                    if ((multin.get(j) instanceof F) && (multin.get(j + 1) instanceof F)) {
                        if (((F) multin.get(j)).name < ((F) multin.get(j + 1)).name) {
                            sort(multin, powers, j, j + 1);
                        }
                    }
                }
            }
        }
    }

    @Override
    public Element one(Ring ring) {
        return null;
    }

    @Override
    public Element zero(Ring ring) {
        return null;
    }

    @Override
    public Element myOne(Ring ring) {
        return null;
    }

    @Override
    public Element myZero(Ring ring) {
        return null;
    }

    /**
     * Операция произведения P1(t)/Q1(t) * P2(t)/Q2(t)
     *
     * @param p1
     * @param ring
     *
     * @return
     */
    public Product multiply(Product p, Ring ring) {
        ArrayList<Element> m1 = new ArrayList<Element>();
        ArrayList<Element> p1 = new ArrayList<Element>();
        m1.addAll(multin);
        p1.addAll(powers);
        m1.addAll(p.multin);
        p1.addAll(p.powers);
        Product t = new Product(m1, p1, ring);
        t.sorting(ring);
        t.normalForm(ring);
        return t;
    }

    /**
     * Операция произведения P1(t)/Q1(t) * P2(t)/Q2(t)
     *
     * @param p1
     * @param ring
     *
     * @return
     */
    @Override
    public Product multiply(Element p, Ring ring) {
        if (p instanceof Product) {
            return multiply(((Product) p), ring);
        } else {
            if (multin.size() == 1) {
                if (multin.get(0).isZero(ring)) {
                    return this;
                }
            }
            ArrayList<Element> m1 = new ArrayList<Element>();
            ArrayList<Element> p1 = new ArrayList<Element>();
            m1.addAll(multin);
            p1.addAll(powers);
            m1.add(p);
            p1.add(ring.numberONE);
            Product t = new Product(m1, p1, ring);
            t.sorting(ring);
            t.normalForm(ring);
            return t;
        }
    }

    @Override
    public String toString(Ring ring) {
        StringBuffer s = new StringBuffer();
        if (this.powers.size() == this.multin.size()) {
            for (int i = 0; i < this.multin.size(); i++) {
                if (this.powers.get(i).compareTo(ring.numberONE, 0, ring)) {
                    s = s.append(this.multin.get(i).toString(ring));
                } else {
                    s = s.append("(").append(this.multin.get(i).toString(ring)).append(")").append("^{").append(this.powers.get(i).toString(ring)).append("}");
                }
            }
        }
        return s.toString();
    }

    @Override
    public String toString() {
        Ring r = new Ring("R64[x]");
        return toString(r);
    }

    @Override
    public Boolean isZero(Ring ring) {
        if ((this.multin.size() == 1) && (this.powers.size() == 1)) {
            if (this.multin.get(0).isZero(ring)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Boolean isOne(Ring ring) {
        if ((this.multin.size() == 1) && (this.powers.size() == 1)) {
            if (this.multin.get(0).isOne(ring)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Element x, Ring r) {
        if (compareTo(x, ring) == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Element o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(Element x, Ring ring) {
        if (x instanceof Product) {
            Product p = (Product) x;
            if (multin.size() > p.multin.size()) {
                return 1;
            }
            if (multin.size() < p.multin.size()) {
                return -1;
            }
            if ((multin.size() == p.multin.size()) && (powers.size() == p.powers.size())) {
                for (int i = multin.size() - 1; i >= 0; i--) {
                    if (multin.get(i).compareTo(p.multin.get(i), ring) == 0) {
                        return powers.get(i).compareTo(p.powers.get(i), ring);
                    }
                    return multin.get(i).compareTo(p.multin.get(i), ring);
                }
            }
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Умножение на число
     *
     * @param x
     *
     * @return
     */
    public Product multiplyFromNumber(Element x, Ring ring) {
        if (multin.get(0) instanceof FactorPol) {

            FactorPol fp = ((FactorPol) multin.get(0));
            if(fp.powers[0] >= 0){
                Element mult = fp.multin[0].multiply(x, ring);
                if (mult instanceof Polynom) {
                    fp.multin[0] = (Polynom) mult;
                } else {
                    fp.multin[0] = new Polynom(mult);
                }
            }else{
                fp = ((FactorPol) multin.get(0)).multiply(new FactorPol(x, ring), ring);
            }
            multin.remove(0);
            multin.add(0, fp);
            return this;
        } else {
            multin.add(0, new FactorPol(x, ring));
            powers.add(0, ring.numberONE);
            return this;
        }
    }

    /**
     * Раскладывает объект типа Product На выходе массив из 2 элементов на 0
     * позиции все элементы с +степенью на 1 позиции все элементы с -степенью
     *
     * @param ring
     *
     * @return
     */
    public Element[][] expandProduct(Ring ring) {
        ArrayList<Element> res_0 = new ArrayList<Element>();
        ArrayList<Element> res_1 = new ArrayList<Element>();
        if (multin.get(0) instanceof FactorPol) {
            FactorPol[] fp = ((FactorPol) multin.get(0)).toNumDenom(((FactorPol) multin.get(0)));
            res_0.add(fp[0]);
            res_1.add(fp[1]);
            for (int i = 1; i < multin.size(); i++) {
                if (powers.get(i).isNegative()) {
                    res_1.add(multin.get(i));
                } else {
                    res_0.add(multin.get(i));
                }
            }
        } else {
            for (int i = 0; i < multin.size(); i++) {
                if (powers.get(i).isNegative()) {
                    res_1.add(multin.get(i));
                } else {
                    res_0.add(multin.get(i));
                }
            }
        }
        Element[] el0 = new Element[res_0.size()];
        res_0.toArray(el0);
        Element[] el1 = new Element[res_1.size()];
        res_1.toArray(el1);
        return new Element[][] {el0, el1};
    }

    @Override
    public int numbElementType() {
        return Ring.Product;
    }

    /**
     * Преобразования произведения Elements
     * Когда можно сократить
     * @param ring
     */
    public void cancel(Ring ring){
        int n = multin.size();
        int m = powers.size();
        if(n == m){
            if(n == 1){
                Element el = multin.get(0);
                if(el instanceof FactorPol){
                    if(powers.get(0).isOne(ring)){
                        FactorPol fp = (FactorPol)el;
                        FactorPol[] arr = fp.toNumDenom(fp);
                        if(arr[0] != null && arr[1] != null){
                            Polynom num = (Polynom)arr[0].toPolynomOrFraction(ring);
                            for(int i = 0; i < arr[1].powers.length; i++){
                                arr[1].powers[i] = Math.abs(arr[1].powers[i]);
                            }
                            Polynom denom = (Polynom)arr[1].toPolynomOrFraction(ring);
                            if(num.coeffs.length !=0 && num.powers.length!=0&& denom.coeffs.length!=0 && denom.powers.length!=0){
                            if(num.coeffs.length >= 2 && denom.powers[0] >= 2){
                            Fraction d = new Fraction(num, denom);
                            d.cancel(ring);
                            Product p = new Product(new Element[]{d.num, d.denom}, new Element[]{ring.numberONE(), ring.numberMINUS_ONE()}, ring);
                            this.multin = p.multin;
                            this.powers = p.powers;
                            }
                            }     }
                    }
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Ring ring = new Ring("R64[x]");

//        ArrayList<Element> m = new ArrayList<Element>();
//        m.add(new NumberR64(5));
//        m.add(new NumberR64(2.2));
//        m.add(new F("\\exp(2x)", ring));
//        m.add(new F("\\sin(2x)", ring));
//        m.add(new F("\\cos(2x)", ring));
//        m.add(new F("\\exp(-6x)", ring));
//        m.add(new Polynom("x-2", ring));
//        m.add(new Polynom("x-3", ring));
//        m.add(new Polynom("x-4", ring));
//        m.add(new FactorPol(new int[]{1, 1}, new Polynom[]{new Polynom("x+2", ring), new Polynom("x", ring)}));
//
//        ArrayList<Element> p = new ArrayList<Element>();
//        p.add(new NumberR64("1"));
//        p.add(new NumberR64("1"));
//        p.add(new NumberR64("3"));
//        p.add(new NumberR64("1"));
//        p.add(new NumberR64("4"));
//        p.add(new NumberR64("-2"));
//        p.add(new NumberR64("1"));
//        p.add(new NumberR64("-1"));
//        p.add(new NumberR64("1"));
//        p.add(new NumberR64("1"));
//
//        Product p11 = new Product(m, p, ring);
//        System.out.println("p11 = " + p11.toString(ring));
//        p11.normalForm(ring);
//        System.out.println("normform  " + p11);
//////////////////////////////////////////000000000000
//        ArrayList<Element> m = new ArrayList<Element>();
//        m.add(new NumberR64(5));
//        m.add(new NumberR64(2.2));
//        m.add(new F("\\exp(2x)", ring));
//        m.add(new F("\\sin(2x)", ring));
//        m.add(new F("\\cos(2x)", ring));
//        m.add(new F("\\exp(2x)", ring));
//        m.add(new Polynom("x-2", ring));
//        m.add(new Polynom("x-3", ring));
//        m.add(new Polynom("x-4", ring));
//        m.add(new FactorPol(new int[]{1, 1}, new Polynom[]{new Polynom("x+2", ring), new Polynom("x", ring)}));
//
//        ArrayList<Element> p = new ArrayList<Element>();
//        p.add(new NumberR64("1"));
//        p.add(new NumberR64("1"));
//        p.add(new NumberR64("4"));
//        p.add(new NumberR64("1"));
//        p.add(new NumberR64("4"));
//        p.add(new NumberR64("-2"));
//        p.add(new NumberR64("1"));
//        p.add(new NumberR64("-1"));
//        p.add(new NumberR64("1"));
//        p.add(new NumberR64("1"));
//
//        Product p11 = new Product(m, p, ring);
//        System.out.println("p11 = " + p11.toString(ring));
//        p11.normalForm(ring);
//        System.out.println("normform  " + p11);

        ArrayList<Element> m1 = new ArrayList<Element>();
        m1.add(new F("\\exp(2x)", ring));
        m1.add(new Polynom("x-2", ring));
        m1.add(new Polynom("x-3", ring));

        ArrayList<Element> p1 = new ArrayList<Element>();
        p1.add(new NumberR64("1"));
        p1.add(new NumberR64("1"));
        p1.add(new NumberR64("1"));

        Product p111 = new Product(m1, p1, ring);
        System.out.println("p11 = " + p111.toString(ring));

        ArrayList<Element> m2 = new ArrayList<Element>();
        m2.add(new F("\\exp(2x)", ring));
        m2.add(new Polynom("x-2", ring));
        m2.add(new Polynom("x-3", ring));

        ArrayList<Element> p2 = new ArrayList<Element>();
        p2.add(new NumberR64("1"));
        p2.add(new NumberR64("1"));
        p2.add(new NumberR64("1"));

        Product p112 = new Product(m2, p2, ring);
        System.out.println("p11 = " + p112.toString(ring));

        Product p113 = p111.multiply(p112, ring);
        System.out.println("mult = " + p113);

        System.out.println("  " + p111.compareTo(p112, ring));

        ArrayList<Element> m3 = new ArrayList<Element>();
        m3.add(new F("\\cos(x)", ring));
        ArrayList<Element> p3 = new ArrayList<Element>();
        p3.add(new NumberR64("1"));

        ArrayList<Element> m4 = new ArrayList<Element>();
        m4.add(new F("\\cos(x)", ring));
        ArrayList<Element> p4 = new ArrayList<Element>();
        p4.add(new NumberR64("1"));

        Product pc1 = new Product(m3, p3, ring);
        Product pc2 = new Product(m4, p4, ring);
        System.out.println("compare == " + pc1.compareTo(pc2, ring));




    }
}
