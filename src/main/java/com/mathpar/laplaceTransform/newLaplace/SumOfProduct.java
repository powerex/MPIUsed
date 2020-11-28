/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform.newLaplace;

import java.util.ArrayList;
import com.mathpar.number.*;
import com.mathpar.func.*;
import java.util.Arrays;
import com.mathpar.polynom.FactorPol;
import com.mathpar.polynom.Polynom;

/**
 * Класс для объектов: P1(t)/Q1(t)+P2(t)/Q2(t)+...+PN(t)/QN(t) в виде суммы
 * дробей, где каждая дробь произведение.
 *
 * @author Rybakov.M.A. & Smirnov R.
 * @years 2012
 */
public class SumOfProduct extends Element {
    public Product[] sum;
    public int col = 0;

    public SumOfProduct() {
    }

    public SumOfProduct(Product[] p) {
        sum = p;
        col = p.length;
    }

    /**
     * Специальный конструктор
     *
     * Метод предназначенный для решения задачи 1/det(A), где A - матрица
     * полученная после прямого преобразования Лапласа, левой части системы ЛДУ.
     * det(A) - детерминат матрицы
     *
     * @param p - det(A)
     * @param ring
     */
    public SumOfProduct create(Polynom p, Ring ring) {
        FactorPol zn = p.factorOfPol_inC(ring);
        zn = new NewSystemLDE().simplifyComplex(zn, ring);//zn.factor_notSOPR(ring);
        //if (zn.multin.length != 1) {
            zn.normalFormInField(ring);
            Element delet = new Complex(1.0, 0.0); //присваеваем на первом этапе 1 коэффициент
            if (zn.multin[0].isItNumber()) {
                //если первый полином в FactorPol - есть число ...
                delet = new Complex(zn.multin[0].coeffs[0].doubleValue(), 0.0); //запоминаем коэффициент
                int[] pow = new int[zn.powers.length - 1];
                Polynom[] mul = new Polynom[zn.multin.length - 1];
                for (int i = 1; i < zn.multin.length; i++) {
                    pow[i - 1] = zn.powers[i];
                    mul[i - 1] = zn.multin[i];
                }
                zn = new FactorPol(pow, mul);
            }
            System.out.println("Знаменатель дроби" + zn.toString(ring));
            Element[] solv = NewSystemLDE.primeFractionForSingle(new Polynom(Polynom.polynom_one(ring.numberONE).multiply(delet, ring)), zn, delet, ring);
            System.out.println("Числители для дробей" + Array.toString(solv));
            for (int i = 0; i < zn.multin.length; i++) {
                for (int j = 0; j < zn.multin[i].coeffs.length; j++) {
                    zn.multin[i].coeffs[j] = zn.multin[i].coeffs[j].toNumber(Ring.C64, new Ring("C64[p]"));
                }
            }
            zn.deletZeroCoeffsInMultins(ring);
            return new SumOfProduct(solv, zn, new Ring("C64[p]"));
       // } else {
       //     return null;
       // }
    }

    public SumOfProduct(Element[] a, FactorPol b, Ring ring) {
        ArrayList<Product> p = new ArrayList<Product>();
        int k = 0;
        int i = 0;
        int t = 0;
        while (k < a.length) {
            for (int j = 0; j < b.powers[i]; j++) {
                ArrayList<Element> m = new ArrayList<Element>();
                ArrayList<Element> n = new ArrayList<Element>();
                if (!a[k].isZero(ring)) {
                    m.add(a[k]);
                    n.add(ring.numberONE);
                    m.add(b.multin[i]);
                    n.add(ring.numberONE.valOf(-(j + 1), ring));//-b.powers[i]+j
                }
                k++;
                if ((m.size() != 0) && (n.size() != 0)) {
                    p.add(new Product(m, n, ring));
                    System.out.println("P[" + t + "]= " + new Product(m, n, ring));
                    t++;
                }
            }
            i++;
        }
        Product[] pp = new Product[t];
        p.toArray(pp);
        SumOfProduct s = new SumOfProduct(pp);
        s.sorting(ring);
        s.normalForm(ring);
        this.sum = s.sum;
        this.col = s.col;
        this.value = s.value;
    }

    private F workIM(Element num, Element denom, Ring r) {
        ArrayList<Element> newX = new ArrayList<Element>();
        if (num instanceof F) {
            if (((F) num).name == F.MULTIPLY) {
                newX.addAll(Arrays.asList(((F) num).X));
            } else {
                newX.add(num);
            }
        } else {
            newX.add(num);
        }
        if (denom instanceof F) {
            F den = r.CForm.multiplcative_Inversion(denom);
            if (den.name == F.MULTIPLY) {
                newX.addAll(Arrays.asList(den.X));
            } else {
                newX.add(den);
            }
        } else {
            newX.add(new F(F.intPOW, new Element[] {denom, r.numberONE.valOf(-1, r)}));
        }
        if (newX.get(0).isZero(r)) {
            return new F(r.numberZERO);
        } else {
            return new F(F.MULTIPLY, newX.toArray(new Element[newX.size()]));
        }
    }

    private F workToDivide(F f, Ring r) {
        if (f.X[0] instanceof F) {
            switch (((F) f.X[0]).name) {
                case F.ADD:
                    Element[] newArg = new Element[((F) f.X[0]).X.length];
                    for (int j = 0; j < newArg.length; j++) {
                        newArg[j] = workIM(((F) f.X[0]).X[j], f.X[1], r);
                    }
                    return new F(F.ADD, newArg);
                case F.SUBTRACT:
                    Element newtwo = ((F) f.X[0]).X[1].negate(r);
                    return new F(F.ADD, new Element[] {workIM(((F) f.X[0]).X[1], f.X[1], r), workIM(newtwo, f.X[1], r)});
//         case F.MULTIPLY:
//          return workIM(((F)f.X[0]), ((F)f.X[1]), r);
                default:
                    return workIM(((F) f.X[0]), ((F) f.X[1]), r);
            }
        } else {
            return workIM(f.X[0], f.X[1], r);
        }
    }

    private Element[] wmp(F f, Ring r) {
        ArrayList<Element> res = new ArrayList<Element>();
        for (int i = 0; i < f.X.length; i++) {
            if (f.X[i] instanceof F) {
                if (((F) f.X[i]).name == F.DIVIDE) {
                    F temp = workToDivide((F) f.X[i], r);//!!!
                    if (temp.name == F.MULTIPLY) {
                        res.addAll(Arrays.asList(temp.X));
                    } else {
                        res.add(temp);
                    }
                } else {
                    if (((F) f.X[i]).name == F.MULTIPLY) {
                        res.addAll(Arrays.asList(wmp((F) f.X[i], r)));

                    } else {
                        res.add(((F) f).X[i]);
                    }


                }
            } else {
                res.add(((F) f).X[i]);
            }
        }
        return res.toArray(new Element[res.size()]);
    }

    private Element[] createSOPArray(Element f, Ring r) {
        if (f instanceof F) {
            ArrayList<Element> res = new ArrayList<Element>();
            switch (((F) f).name) {
                case F.MULTIPLY:
                    return new Element[] {new F(F.MULTIPLY, wmp((F) f, r))};
                case F.intPOW:
                case F.POW:
                    return new Element[] {f};
                case F.DIVIDE:
                    F newD = workToDivide(((F) f), r);
                    if (newD.name == F.ADD) {
                        return newD.X;
                    } else {
                        return new Element[] {newD};
                    }
                case F.ADD:
                    for (int t = 0; t < ((F) f).X.length; t++) {
                        res.addAll(Arrays.asList(createSOPArray(((F) f).X[t], r)));
                    }
                    return res.toArray(new Element[res.size()]);
//         case F.SUBTRACT:
//          Element newTWo=expandF.X[1].negate(ring);
//          sum=new Product[]{new Product(expandF.X[0], ring), new Product(newTWo, ring)};
                default:
                    return new Element[] {f};
            }
        } else {
            return new Element[] {f};
        }
    }

    public void sorting(Ring ring) {
        int[] pos = Array.sortPosUp(sum, ring);
        Product[] p = new Product[sum.length];
        for (int i = 0; i < pos.length; i++) {
            p[i] = sum[pos[i]];
        }
        sum = p;
    }

    /**
     * Упрощения суммы произведений для обратного преобразования Лапласа
     *
     * @param ring
     */
    public void simplifyTheSolve(Ring ring) {
        Element[] el = Array.sortUp(sum, ring);
        ArrayList<Product> res = new ArrayList<Product>();
        boolean isZero = false;
        for(int i = 0; i < el.length; i++) {
            for(int j = 0; j < el.length; j++) {
                if ((i != j && el[i] != null && el[j] != null) && (el[i] instanceof Product && el[j] instanceof Product)) {
                    Product p1 = ((Product) el[i]);
                    Product p2 = ((Product) el[j]);
                    if (p1.multin.size() == 1 && p2.multin.size() == 1) {
                        if (p1.multin.get(0) instanceof FactorPol && p2.multin.get(0) instanceof FactorPol) {
                            FactorPol fp1 = ((FactorPol) p1.multin.get(0));
                            FactorPol fp2 = ((FactorPol) p2.multin.get(0));

                            Polynom polNum1 = Polynom.polynom_one(ring.numberONE());
                            Polynom polDenom1 = null;
                            int pow1 = 0;
                            for (int k1 = 0; k1 < fp1.multin.length; k1++) {
                                if (fp1.powers[k1] > 0) {
                                    polNum1 = polNum1.multiply(fp1.multin[k1], ring);
                                } else {
                                    polDenom1 = fp1.multin[k1];
                                    pow1 = fp1.powers[k1];
                                }
                            }

                            Polynom polNum2 = Polynom.polynom_one(ring.numberONE());
                            Polynom polDenom2 = null;
                            int pow2 = 0;
                            for (int k2 = 0; k2 < fp2.multin.length; k2++) {
                                if (fp2.powers[k2] > 0) {
                                    polNum2 = polNum2.multiply(fp2.multin[k2], ring);
                                } else {
                                    polDenom2 = fp2.multin[k2];
                                    pow2 = fp2.powers[k2];
                                }
                            }
                            if (polDenom1 != null && polDenom2 != null) {
                                if ((polDenom1.compareTo(polDenom2, ring) == 0) && (pow1 == pow2)) {
                                    Polynom ch = polNum1.add(polNum2, ring);
                                    Polynom[] pp = new Polynom[] {ch, polDenom1};
                                    int[] pw = new int[] {1, pow1};
                                    if (!ch.isZero(ring)) {
                                        isZero = false;
                                        el[i] = new Product(new FactorPol(pw, pp), ring);
                                        el[j] = null;
                                    } else {
                                        isZero = true;
                                        el[i] = new Product(new FactorPol(pw, pp), ring);
                                        el[j] = null;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!isZero && el[i] != null) {
                ((Product) el[i]).cancel(ring);
                res.add((Product) el[i]);
                el[i] = null;
            }
            isZero = false;
        }
        for (int h = 0; h < res.size(); h++) {
            for (int ii = 0; ii < res.get(h).multin.size(); ii++) {
                if (res.get(h).multin.get(ii) instanceof FactorPol) {
                    FactorPol fp = (FactorPol) res.get(h).multin.get(ii);
                    fp.normalFormInField(ring);
                    res.get(h).multin.set(ii, fp);
                }
            }
        }
        sum = res.toArray(new Product[res.size()]);
        simplifyNumber(ring);
    }

    public void simplifyNumber(Ring ring) {
        ArrayList<Product> res = new ArrayList<Product>();
        Element el = ring.numberZERO;
        for (int i = 0; i < sum.length; i++) {
            if (sum[i].multin.size() == 1) {
                if (sum[i].multin.get(0) instanceof FactorPol) {
                    FactorPol fp = (FactorPol) sum[i].multin.get(0);
                    if (fp.multin.length == 1) {
                        if (fp.powers[0] == 1) {
                            if (fp.multin[0].isItNumber()) {
                                if (!fp.multin[0].isZero(ring)) {
                                    el = el.add(fp.multin[0].coeffs[0], ring);
                                }
                            } else {
                                res.add(sum[i]);
                            }
                        } else {
                            res.add(sum[i]);
                        }
                    } else {
                        res.add(sum[i]);
                    }
                } else {
                    res.add(sum[i]);
                }
            } else {
                res.add(sum[i]);
            }
        }
        if (!el.isZero(ring)) {
            res.add(new Product(el, ring));
        }
        sum = res.toArray(new Product[res.size()]);
    }

    public void normalForm(Ring ring) {
        Element[] el = Array.sortUp(sum, ring);
        ArrayList<Product> res = new ArrayList<Product>();
        IntList count = new IntList();
        IntList countTransform = new IntList();
        int i = 0;
        int l = 0;
        int j = 1;
        boolean isZero = false;
        while (i < el.length) {
            int k = 1;
            int q = 1;
            while (j < el.length) {
                if (el[i] instanceof Product && el[j] instanceof Product) {
                    Product p1 = ((Product) el[i]);
                    Product p2 = ((Product) el[j]);
                    if (p1.multin.size() == 1 && p2.multin.size() == 1) {
                        if (p1.multin.get(0) instanceof FactorPol && p2.multin.get(0) instanceof FactorPol) {
                            FactorPol fp1 = ((FactorPol) p1.multin.get(0));
                            FactorPol fp2 = ((FactorPol) p2.multin.get(0));
                            if (fp1.multin.length == 2 && fp2.multin.length == 2) {
                                if (fp1.multin[0].isItNumber() && fp2.multin[0].isItNumber()) {
                                    if ((fp1.multin[1].compareTo(fp2.multin[1], ring) == 0) && (fp1.powers[1] == fp2.powers[1])) {
                                        Polynom ch = fp1.multin[0].add(fp2.multin[0], ring);
                                        Polynom[] pp = new Polynom[] {ch, fp1.multin[1]};
                                        int[] pw = new int[] {fp1.powers[0], fp1.powers[1]};
                                        if (!ch.isZero(ring)) {
                                            isZero = false;
                                            el[i] = new Product(new FactorPol(pw, pp), ring);
                                            k++;
                                        } else {
                                            isZero = true;
                                            el[i] = new Product(new FactorPol(pw, pp), ring);
                                            k++;
                                        }
                                        q = 0;
                                    }
                                }
                            }
                        }
                    }
                }
//                if ((i != j) && (el[i].compareTo(el[j], ring) == 0)) {
//                    k++;
//                }
                j++;
            }
            countTransform.arr[l] = q;
            count.arr[l] = k;
            if (!isZero) {
                res.add((Product) el[i]);
            }
            l++;
            q++;
            i = i + k;
            j = i + 1;
            isZero = false;
        }
        for (int h = 0; h < res.size(); h++) {
            for (int ii = 0; ii < res.get(h).multin.size(); ii++) {
                if (res.get(h).multin.get(ii) instanceof FactorPol) {
                    FactorPol fp = (FactorPol) res.get(h).multin.get(ii);
                    fp.normalFormInField(ring);
                    res.get(h).multin.set(ii, fp);
                }
            }
            if (count.arr[h] > 1 && countTransform.arr[h] >= 1) {
                res.set(h, res.get(h).multiplyFromNumber(ring.numberONE.valOf(count.arr[h], ring), ring));
            }
        }
        sum = res.toArray(new Product[res.size()]);
        simplifyNumber(ring);
    }

    public SumOfProduct(Element f, Ring ring) {
        if (f instanceof F) {
            Element el =ring.CForm.simplify_init(f);
            Element expandF = null;
            if (el instanceof F) {
                expandF = (F) el;
            } else {
                expandF = el;
            }
            //F expandF = (F) new CanonicForms(ring, true).InputForm((F) f, null, null);//(F) new CanonicForms(ring).SimplifyWithOutGether(f, false)
            Element[] newSum = createSOPArray(expandF, ring);
            sum = new Product[newSum.length];
            for (int i = 0; i < newSum.length; i++) {
                sum[i] = new Product(newSum[i], ring);
            }
            sorting(ring);
        } else {
            Product[] p = new Product[1];
            p[0] = new Product(f, ring);
            sum = p;
            sorting(ring);
        }
    }

    @Override
    public String toString(Ring ring) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < sum.length; i++) {
            if (i == this.sum.length - 1) {
                s = s.append(sum[i].toString(ring));
            } else {
                s = s.append(sum[i].toString(ring)).append("+");
            }
        }
        return s.toString();
    }

    @Override
    public String toString() {
        Ring ring = new Ring("R64[x]");
        return toString(ring);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Ring ring = new Ring("R64[x]");
//        ArrayList<Element> m = new ArrayList<Element>();
//        m.add(new Polynom("5", ring));
//        m.add(new Polynom("x-2", ring));
//        ArrayList<Element> p = new ArrayList<Element>();
//        p.add(new NumberR64("1"));
//        p.add(new NumberR64("-1"));
//        Product p1 = new Product(m, p, ring);
//        System.out.println("p1 = " + p1);
//
//        ArrayList<Element> m1 = new ArrayList<Element>();
//        m1.add(new Polynom("5", ring));
//        m1.add(new Polynom("x-1", ring));
//        ArrayList<Element> p11 = new ArrayList<Element>();
//        p11.add(new NumberR64("1"));
//        p11.add(new NumberR64("-1"));
//        Product p2 = new Product(m1, p11, ring);
//        System.out.println("p2 = " + p2);
//
//        System.out.println("p1*p2 = " + p1.multiply(p2, ring));
//
//        SumOfProduct s1 = new SumOfProduct(new Product[]{p1,p2});
//        System.out.println("s1 = " + s1);

        F ff1 = new F("\\sin(x)+\\cos(x)-\\exp(3x)\\tg(5x)^{4}/\\sin(x)+x/\\cos(x)", ring);
        SumOfProduct s1 = new SumOfProduct(ff1, ring);

        F ff2 = new F("\\sin(x)+\\cos(x)-\\exp(3x)\\tg(5x)^{4}/\\sin(x)+x/\\cos(x)", ring);
        SumOfProduct s2 = new SumOfProduct(ff2, ring);

        System.out.println("s1+s2 = " + s1.add(s2, ring).toString(ring));

        F ff3 = new F("\\sin(x)+\\cos(x)", ring);
        SumOfProduct s3 = new SumOfProduct(ff3, ring);

        F ff4 = new F("\\sin(x)+\\cos(x)", ring);
        SumOfProduct s4 = new SumOfProduct(ff4, ring);
        System.out.println("s3*s4 = " + s3.multiply(s4, ring).toString(ring));
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

    @Override
    public boolean equals(Element x, Ring r) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SumOfProduct add(Element x, Ring ring) {
        if (x instanceof SumOfProduct) {
            Product[] newSum = new Product[sum.length + ((SumOfProduct) x).sum.length];
            System.arraycopy(sum, 0, newSum, 0, sum.length);
            System.arraycopy(((SumOfProduct) x).sum, 0, newSum, sum.length, ((SumOfProduct) x).sum.length);
            Element[] el = Array.sortUp(newSum, ring);
            ArrayList<Product> res = new ArrayList<Product>();
            IntList count = new IntList();
            if (el.length > 32) {
                count = new IntList(el.length + 1);
            }
            int i = 0;
            int l = 0;
            int j = 1;
            while (i < el.length) {
                int k = 1;
                while (j < el.length) {
                    if (el[i].compareTo(el[j], ring) == 0) {
                        k++;
                    } else {
                        break;
                    }
                    j++;
                }
                count.arr[l] = k;
                if (!((Product) el[i]).multin.get(0).isZero(ring)) {
                    res.add((Product) el[i]);
                    //NEW!!!
                    l++;
                }

                i = i + k;
                j = i + 1;
            }
            for (int h = 0; h < res.size(); h++) {
                if (count.arr[h] > 1) {
                    res.set(h, res.get(h).multiplyFromNumber(ring.numberONE.valOf(count.arr[h], ring), ring));
                }
            }
            return new SumOfProduct(res.toArray(new Product[res.size()]));
        } else {
            if (!x.isZero(ring)) {
                SumOfProduct s = new SumOfProduct(x, ring);
                return add(s, ring);
            } else {
                return this;
            }

        }

    }

    public SumOfProduct multiply(SumOfProduct sp, Ring ring) {
        int l1 = sum.length;
        int l2 = sp.sum.length;
        Product[] res = new Product[l1 * l2];
        int k = 0;
        for (int i = 0; i < l1; i++) {
            for (int j = 0; j < l2; j++) {
                res[k] = sum[i].multiply(sp.sum[j], ring);
                k++;
            }
        }
        SumOfProduct s = new SumOfProduct(res);
        s.sorting(ring);
        s.normalForm(ring);
        return s;
    }

    @Override
    public SumOfProduct multiply(Element el, Ring ring) {
        if (el instanceof SumOfProduct) {
            return multiply(((SumOfProduct) el), ring);
        } else {
            int l1 = sum.length;
            Product[] res = new Product[l1];
            int k = 0;
            for (int i = 0; i < l1; i++) {
                res[k] = sum[i].multiply(el, ring);
                k++;
            }
            SumOfProduct s = new SumOfProduct(res);
            s.sorting(ring);
            s.normalForm(ring);
            return s;
        }
    }

    @Override
    public int numbElementType() {
        return Ring.SumOfProduct;
    }
}
