/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.probability;

import com.mathpar.func.*;
import java.util.ArrayList;
import com.mathpar.matrix.*;
import com.mathpar.number.*;

/**
 *
 * @author kireev
 */
public class RandomQuantity {
    /**
     * -значения дискретной случайной величины
     */
    public Element X[]=null;

    /**
     * -вероятности, соответствующие значениям дискретной случайной величины
     */
    public Element P[]=null;

    /**
     * - левая граница
     */
    public Element a=null;

   /**
    * - правая граница
    */
    public Element b=null;

    /**
     * - плотность распределения вероятностей непрерывной случайной величины
     */
    public F f=null;

    public RandomQuantity() {
    }

    /**
     * Дискретная случайная величина
     * @param X1 значения дискретной случайной величины
     * @param P1 вероятности, соответствующие значениям дискретной случайной
     * величины
     */
    public RandomQuantity(Element X1[], Element P1[]) {
        X = X1;
        P = P1;
    }

    /**
     * Непрерывная случайная величина
     * @param a1 левая граница
     * @param b1 правая граница
     * @param f1 плотность распределения
     */
    public RandomQuantity(Element a1, Element b1, Element f1) {
        a = a1;
        b = b1;
        if(f1 instanceof F){
            f = (F) f1;
        }else{
            f = new F(F.ID, f1);
        }
    }

    @Override
    public String toString() {
        String s;
        if (X != null) {
            String s1 = Array.toString(X);
            String s2 = Array.toString(P);
            s = "x| " + s1 + "\n" + "p| " + s2;
            return s;
        } else {
            String s1 = f.toString();
            String s2 = ",  на ("+a+","+b+")";
            s = s1+s2;
            return s;
        }
    }

    /**
     * Функция для дискретных случайных величин.
     * Проверка попадания элементов массива P в диапазон от 0 до 1, и проверка
     * равенства массива X и P ring кольцо
     *
     * @param ring
     * @return true, если все элементы принадлежат [0,1],и массивы X и P равны,
     * false - в противном случае
     */
    public boolean verifyP(Ring ring) {
        if (X.length != P.length) {
            return false;
        }
        Element k = NumberR64.ZERO;
        for (int j = 0; j < P.length; j++) {
            k = P[j];
            if (k.compareTo(NumberR64.ZERO, ring) == -1) {
                break;
            }
        }
        Element s = NumberR64.ZERO;
        for (int i = 0; i < P.length; i++) {
            s = s.add(P[i], ring);
        }
        return ((s.compareTo(NumberR64.ONE, ring) == 0) && (k.compareTo(NumberR64.ZERO, 1, ring)));
    }

    /**
     * Вычисление математического ожидания случайной величины
     * (дискретной или непрерывной)
     * @param ring кольцо
     *
     * @return Значение математического ожидания случайной величины
     */
    public Element mathExpectation(Ring ring) {
        Element s = new NumberR64(0);
        if (X != null) {
            for (int i = 0; i < X.length; i++) {
                Element y = X[i].multiply(P[i], ring);
                s = s.add(y, ring);
            }
            return s;
        } else {
            F f1 = new F("x",ring);
            F f2 = (F) f1.multiply(f, ring);
            s = NInt.integrate(a, b, f2, ring);
            return s;
        }
    }

    /**
     * Вычисление дисперсии случайной величины (дискретной или непрерывной)
     *
     * @param ring кольцо
     *
     * @return Значение дисперсии случайной величины
     */
    public Element dispersion(Ring ring) {
        Element rez;
        RandomQuantity w = this;
        if (X != null) {
            Element x[] = new Element[w.X.length];
            Element m = w.mathExpectation(ring);
            for (int i = 0; i < w.X.length; i++) {
                x[i] = w.X[i].subtract(m, ring);
                x[i] = x[i].multiply(x[i], ring);
            }
            RandomQuantity y = new RandomQuantity(x, w.P);
            rez = y.mathExpectation(ring);
            return rez;
        } else {
            Element me = w.mathExpectation(ring);
            Element me2 = me.multiply(me, ring);
            F f1 = new F("x^2", ring);
            F f2 = (F) f1.multiply(f, ring);
            Element s = NInt.integrate(a, b, f2, ring);
            rez = s.subtract(me2, ring);
            return rez;
        }
    }

    /**
     * Вычисление среднего квадратичного отклонения случайной величины
     * (дискретной или непрерывной)
     * @param ring кольцо
     *
     * @return Значение среднего квадратичного отклонения случайной величины
     */
    public Element meanSquareDeviation(Ring ring) {
        RandomQuantity w = this;
        Element s = w.dispersion(ring);
        Element rez = s.sqrt(ring);
        return rez;
    }

    /**
     * Сортировка по возрастанию и объединение одинаковых значений дискретной случайной
     * величины w в один столбец, соответствующие одинаковым значениям,
     * вероятности складываются
     *
     * @param ring кольцо
     *
     * @return упрощенную случайную величину w
     */
    public RandomQuantity simplify(Ring ring) {
        Element y[] = Array.sortUp(X, ring);
        Element p[] = new Element[P.length];
        int[] pos = Array.sortPosUp(X, ring);
        int k = 0;
        for (int i = 0; i < P.length; i++) {
            p[i] = P[pos[k]];
            k++;
        }
        ArrayList<Element> x1 = new ArrayList<Element>();
        ArrayList<Element> p1 = new ArrayList<Element>();
        Element sum = NumberR.ZERO;
        int t = 0;
        while (t < y.length - 1) {
            sum = sum.add(p[t], ring);
            Element buf = y[t];
            while (y[t].compareTo(y[t + 1], 0, ring)) {
                sum = sum.add(p[t + 1], ring);
                t++;
            }
            p1.add(sum);
            x1.add(buf);
            t++;
            sum = NumberR.ZERO;
        }
        p1.add(p[p.length - 1]);
        x1.add(y[y.length - 1]);
        Element[] x2 = new Element[x1.size()];
        Element[] p2 = new Element[p1.size()];
        for (int q = 0; q < x2.length; q++) {
            x2[q] = x1.get(q);
            p2[q] = p1.get(q);
        }
        return new RandomQuantity(x2, p2);
    }

    /**
     * Сложение дискретных случайных величин v и w
     *
     * @param w дискретная случайная величина
     * @param ring кольцо
     *
     * @return v+w -случайная величина
     */
    public RandomQuantity add(RandomQuantity w, Ring ring) {
        RandomQuantity v = this;
        Element[] x = new Element[v.X.length * w.X.length];
        Element[] p = new Element[v.P.length * w.P.length];
        int k = 0;
        for (int i = 0; i < v.X.length; i++) {
            for (int j = 0; j < w.X.length; j++) {
                x[k] = v.X[i].add(w.X[j], ring);
                p[k] = v.P[i].multiply(w.P[j], ring);
                k++;
            }
        }
        RandomQuantity z = new RandomQuantity(x, p);
        RandomQuantity z1 = z.simplify(ring);
        return z1;
    }

    /**
     * Умножение дискретных случайных величин v на w
     *
     * @param w дискретная случайная величина
     * @param ring кольцо
     *
     * @return v*w -случайная величина
     */
    public RandomQuantity multiply(RandomQuantity w, Ring ring) {
        RandomQuantity v = this;
        Element[] x = new Element[v.X.length * w.X.length];
        Element[] p = new Element[v.P.length * w.P.length];
        int k = 0;
        for (int i = 0; i < v.X.length; i++) {
            for (int j = 0; j < w.X.length; j++) {
                x[k] = v.X[i].multiply(w.X[j], ring);
                p[k] = v.P[i].multiply(w.P[j], ring);
                k++;
            }
        }
        RandomQuantity z = new RandomQuantity(x, p);
        RandomQuantity z1 = z.simplify(ring);
        return z1;
    }

    /**
     * Вычисление коэффициента ковариации по формуле cov(v,w)=M((v-Mv)*(w-Mw))
     *
     * @param w дискретная случайная величина
     * @param ring кольцо
     *
     * @return значение коэффициента ковариации
     */
    public Element covariance(RandomQuantity w, Ring ring) {
        RandomQuantity v = this;
        Element v1 = v.mathExpectation(ring);
        Element w1 = w.mathExpectation(ring);
        Element[] x = new Element[v.X.length];
        Element[] y = new Element[w.X.length];
        for (int i = 0; i < v.X.length; i++) {
            x[i] = v.X[i].subtract(v1, ring);
        }
        for (int j = 0; j < w.X.length; j++) {
            y[j] = w.X[j].subtract(w1, ring);
        }
        if ((v.X == w.X) & (v.P == w.P)) {
            Element rez = v.dispersion(ring);
            return rez;
        } else {
            RandomQuantity x1 = new RandomQuantity(x, v.P);
            RandomQuantity x2 = new RandomQuantity(y, w.P);
            RandomQuantity z = x1.multiply(x2, ring);
            Element rez = z.mathExpectation(ring);
            return rez;
        }
    }

    /**
     * Вычисление коэффициента корреляции по формуле
     * cor(v,w)=cov(v,w)/(meanSquareDeviation(v)*meanSquareDeviation(w))
     *
     * @param w дискретная случайная величина
     * @param ring кольцо
     *
     * @return значение коэффициента корреляции
     */
    public Element correlation(RandomQuantity w, Ring ring) {
        RandomQuantity v = this;
        Element a1 = v.covariance(w, ring);
        Element b1 = v.meanSquareDeviation(ring);
        Element b2 = w.meanSquareDeviation(ring);
        Element rez = a1.divide(b1.multiply(b2, ring), ring);
        return rez;
    }

    /**
     * Вычисление выборочного среднего для выборки x
     *
     * @param x выборка
     * @param ring кольцо
     *
     * @return значение выборочного среднего выборки x
     */
    public static Element sampleMean(Element[] x, Ring ring) {
        Element s = new NumberR(0);
        for (int i = 0; i < x.length; i++) {
            s =  s.add(x[i], ring);
        }
        Element xl = new NumberR(x.length);
        Element rez = s.divide(xl, ring);
        return rez;
    }

    /**
     * Вычисление выборочной дисперсии для выборки x
     *
     * @param x выборка
     * @param ring кольцо
     *
     * @return значение выборочной дисперсии выборки x
     */
    public static Element sampleDispersion(Element x[], Ring ring) {
        Element m = sampleMean(x, ring);
        Element[] y = new Element[x.length];
        Element s = new NumberR(0);
        for (int i = 0; i < x.length; i++) {
            y[i] = x[i].subtract(m, ring);
            y[i] = y[i].multiply(y[i], ring);
            s = s.add(y[i], ring);
        }
        Element yl = new NumberR(y.length);
        Element rez = s.divide(yl, ring);
        return rez;
    }

    /**
     * Вычисление коэффициента ковариации для связанной выборки
     *
     * @param x значения 1-й выборки
     * @param y значения 2-й выборки
     * @param ring кольцо
     *
     * @return значение коэффициента ковариации
     */
    public static Element covarianceCoefficient(Element[] x, Element[] y, Ring ring) {
        Element m1 = sampleMean(x, ring);
        Element m2 = sampleMean(y, ring);
        Element[] x1 = new Element[x.length];
        Element[] y1 = new Element[y.length];
        Element[] z = new Element[x.length];
        for (int k = 0; k < x.length; k++) {
            x1[k] = x[k].subtract(m1, ring);
            y1[k] = y[k].subtract(m2, ring);
            z[k] = x1[k].multiply(y1[k], ring);
        }
        Element rez = sampleMean(z, ring);
        return rez;
    }

    /**
     * Вычисление коэффициента корреляции для связанной выборки
     *
     * @param x значения 1-й выборки
     * @param y значения 2-й выборки
     * @param ring кольцо
     *
     * @return значение коэффициента корреляции
     */
    public static Element correlationCoefficient(Element[] x, Element[] y, Ring ring) {
        Element rez1 = sampleDispersion(x, ring);
        Element rez2 = sampleDispersion(y, ring);
        Element c = covarianceCoefficient(x, y, ring);
        Element rez = c.divide((rez1.multiply(rez2, ring)).sqrt(ring), ring);
        return rez;
    }

    /**
     * Построение многоугольника распределения дискретной случайной величины
     * @return
     */
    public F plotPolygonDistribution() {
        RandomQuantity v = this.simplify(Ring.ringR64xyzt);
        NumberR64 xmin = ((NumberR64) v.X[0].toNumber(Ring.R64, Ring.ringR64xyzt)).subtract(new NumberR64(1));
        NumberR64 xmax = ((NumberR64) v.X[v.X.length - 1].toNumber(Ring.R64, Ring.ringR64xyzt)).add(new NumberR64(1));
        NumberR64 ymin = new NumberR64(-1);
        NumberR64 ymax = new NumberR64(2);
        Element[] a1 = new Element[X.length + 2];
        Element[] a2 = new Element[P.length + 2];
        a1[0] = v.X[0];
        a2[0] = new NumberR(0);
        a1[X.length + 1] = v.X[X.length - 1];
        a2[X.length + 1] = new NumberR(0);
        for (int i = 0; i < X.length; i++) {
            a1[i + 1] = v.X[i];
            a2[i + 1] = v.P[i];
        }
        F f1 = new F(F.TABLEPLOT, new Element[] {new F(F.VECTORS, new Element[] {new F(F.VECTORS, a1), new F(F.VECTORS, a2)}), new F(F.VECTORS, new Element[] {xmin, xmax, ymin, ymax})});
        return f1;
    }

    /**
     * Построение функции распределения дискретной или непрерывной случайной величины
     * @return
     */
    public F plotDistributionFunction() {
        if (X != null) {
            RandomQuantity w = this.simplify(Ring.ringR64xyzt);
            NumberR64 xmin = ((NumberR64) w.X[0].toNumber(Ring.R64, Ring.ringR64xyzt)).subtract(new NumberR64(5));
            NumberR64 xmax = ((NumberR64) w.X[w.X.length - 1].toNumber(Ring.R64, Ring.ringR64xyzt)).add(new NumberR64(5));
            NumberR64 ymin = new NumberR64(0);
            NumberR64 ymax = new NumberR64(1);
            Element x1[] = new Element[w.X.length + 2];
            Element p1[] = new Element[w.X.length + 2];
            Ring r = new Ring("R64[x,y,z]");
            x1[0] = xmin;
            x1[x1.length - 1] = xmax;
            System.arraycopy(w.X, 0, x1, 1, w.X.length);
            p1[0] = ymin;
            p1[p1.length - 1] = ymax;
            System.arraycopy(w.P, 0, p1, 1, w.P.length);
            Element[][] a1 = new Element[w.X.length + 1][];//x
            Element[][] a2 = new Element[w.P.length + 1][];//y
            F f1[] = new F[a1.length];
            Table[] tt = new Table[a1.length];
            int k1 = 0;
            int k2 = 0;
            Element res = new NumberR64(0);
            for (int i = 0; i < w.X.length + 1; i++) {
                a1[i] = new Element[2];
                a2[i] = new Element[2];
                res = res.add(p1[k2], r);
                for (int j = 0; j < 2; j++) {
                    a1[i][j] = x1[k1];
                    a2[i][j] = res;
                    k1++;
                }
                k1--;
                k2++;
                MatrixD mm = new MatrixD(new Element[2][a1[i].length]);
                mm.M[0] = a1[i];
                mm.M[1] = a2[i];
                tt[i] = new Table(mm);
                f1[i] = new F(F.TABLEPLOT, tt[i]);
            }
            F ff = new F(F.SHOWPLOTS, new F(F.VECTORS, f1));
            return ff;
        } else {
            Element xmin1 = this.a.subtract(new NumberR64(5), Ring.ringR64xyzt);
            Element xmax1 = this.b.add(new NumberR64(5), Ring.ringR64xyzt);
            F f2[] = new F[3];
            MatrixD m2 = new MatrixD(new Element[2][2]);
            m2.M[0][0]=xmin1;
            m2.M[0][1]=a;
            m2.M[1][0]=new NumberR64(0);
            m2.M[1][1]=new NumberR64(0);
            Table t2 = new Table(m2);
            f2[0] = new F(F.TABLEPLOT, t2);
            MatrixD m3 = new MatrixD(new Element[2][2]);
            m3.M[0][0]=b;
            m3.M[0][1]=xmax1;
            m3.M[1][0]=new NumberR64(1);
            m3.M[1][1]=new NumberR64(1);
            Table t3 = new Table(m3);
            f2[2] = new F(F.TABLEPLOT, t3);

//            if (f.X[0] instanceof Fname) {
//            ((Fname)f.X[0]).X=new Element[]{};
//        }

            Element[] qq = {f,new VectorS(new VectorS(new Element[]{a,b,new NumberR64(0),new NumberR64(1)}),0)};
            f2[1] = new F(F.PLOT, qq);
            F rez = new F(F.SHOWPLOTS, new F(F.VECTORS, f2));
            return rez;
        }
    }
}
