/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform;

import java.util.Vector;
//
import com.mathpar.number.*;
import com.mathpar.matrix.*;
import com.mathpar.polynom.*;
import com.mathpar.func.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Программный класс для решения систем ЛДУ. Неоднородная система линейных
 * дифференциальных уравнений с постоянными коэффициентами. Метод Лапласа.
 *
 *
 * @author Rybakov Michail
 * @version 3.0
 * @years 2010-2012
 */
public class SystemLDE extends F {
    public Ring newRing;
    public SystemLDE() {
    }

    public SystemLDE(F f) {
        this.name = f.name;
        this.X = f.X;
    }
 
    /**
     * Проверка разложения на простые дроби
     *
     * @param el - числитель дроби
     * @param coeff - массив коэффициентов
     * @param fr - разложенный числитель
     * @param ring - кольцо
     *
     * @return
     */
    public boolean equals_primeFractionCoeffitions(Element el, Element[] coeff, FactorPol fr, Ring ring) {
        int n = 0; // колличество дробей
        for (int i = 0; i < fr.powers.length; i++) {
            n = n + fr.powers[i];
        }
        Polynom polnew[] = new Polynom[n]; //создаём массив полиномов --- (добавки)
        Polynom pr = fr.multin[0].myOne(ring); //вспомогательный полином равный 1
        for (int j = 0; j < fr.powers.length; j++) { //вычисляем произведение полиномов (знаменатель)
            if (fr.powers[j] == 1) {
                pr = pr.multiply(fr.multin[j], ring);
            }
            if (fr.powers[j] != 1) {
                for (int i = 0; i < fr.powers[j]; i++) {
                    pr = pr.multiply(fr.multin[j], ring);
                }
            }
        }
        int ds1 = 0; //вспомогательная переменная
        int ds2 = 0; //вспомогательная переменная
        for (int a = 0; a < fr.powers.length; a++) { //вычисляем добавки к дробям
            Polynom del = pr;
            for (int m = 0; m < fr.powers[a]; m++) {
                del = del.divideExact(fr.multin[a], ring);
                polnew[m + ds1] = del;
                ds2 = ds2 + 1;
            }
            ds1 = ds2;
        }
        Element p = Polynom.polynomZero;
        for (int i = 0; i < n; i++) {
            p = p.add(coeff[i].multiply(polnew[i], ring), ring);
        }
        if (el.subtract(p, ring).isZero(ring)) {
            return true;
        }
        return false;
    }

    /**
     * Метод поиска производной в дереве функции(\\D)
     *
     * @param f - дерево функций
     *
     * @return - true - если найдена и false если не найдена
     */
    public boolean isD(Element f) {
        if (f instanceof F) {
            for (int i = 0; i < ((F) f).X.length; i++) {
                if (((F) f).X[i] instanceof F) {
                    if (((F) ((F) f).X[i]).name > 95 && ((F) ((F) f).X[i]).name < 100) {
                        return isD(((F) ((F) f).X[i]));
                    }
                } else {
                }
                if (((F) f).X[i] instanceof F) {
                    if (((F) ((F) f).X[i]).name == F.D) {
                        return true;
                    }
                } else {
                }
            }
        }
        return false;
    }

    /**
     * Метод поиска всех производных по переменным кольца в одном
     * дифференциальном уравнении из системы
     *
     * @param f - одно дифференциальное уравнение
     *
     * @return - true - если найдена и false если не найдена
     */
    public void isD_inSolv(Element f, Vector<Element> a) {
        if (f instanceof F) {
            for (int i = 0; i < ((F) f).X.length; i++) {
                if (((F) f).X[i] instanceof F) {
                    if (((F) ((F) f).X[i]).name > 95 && ((F) ((F) f).X[i]).name < 100) {
                        isD_inSolv(((F) ((F) f).X[i]), a);
                    }
                } else {
                }
                if (((F) f).X[i] instanceof F) {
                    if (((F) ((F) f).X[i]).name == F.D) {
                        a.add(((F) ((F) f).X[i]).X[0]);
                    }
                } else {
                }
            }
        }
    }

    public int numbD(Element f) {
        Vector<Element> a = new Vector<Element>();
        isD_inSolv(f, a);

        int k = 0;
        Element[] b = new Element[a.size()];
        a.copyInto(b);
        for (int i = 0; i < b.length - 1; i++) {
            for (int j = i + 1; j < b.length; j++) {
                if ((!b[i].equals(b[j])) && (i != j)) {
                    k++;
                }
            }
        }
        return k;
    }

    /**
     * Метод преобразования по Лапласу входной системы дифференциальных
     * уравнений
     *
     * @param f - система дифференциальных уравнений
     *
     * @return - возвращает матрицу преобразования
     */
    public void systDifEquationToMatrix(Element f, Vector[] var, Vector varFname, Ring ring) {
        Polynom p = new Polynom("p", new Ring("R64[p]",ring));
        p = p.toPolynom(new Ring("R64[p]",ring));
        for (int i = 0; i < ((F) f).X.length; i++) {
            Element Xi = ((F) f).X[i];
            if (Xi instanceof F) {
                if (((F) Xi).name == F.d) {
                    ((F) Xi).X[0] = ((F) Xi).X[0].ExpandFnameOrId();
                }
            }
            if (Xi instanceof Fname) {
                for (int j = 0; j < varFname.size(); j++) {
                    if (((Fname) Xi).name.equals(((Fname) varFname.get(j)).name)) {
                        var[j].add(Polynom.polynomFromNumber(ring.numberONE, ring));
                    }
                }
            } else {
                if (((F) Xi).name == F.d) {
                    for (int j = 0; j < varFname.size(); j++) {
                        if (((Fname) ((F) Xi).X[0]).name.equals(((Fname) varFname.get(j)).name)) {
                            if (((F) Xi).X.length > 2) {
                                var[j].add(new Polynom(new int[] {((F) Xi).X[2].intValue()}, new Element[] {NumberR64.ONE}));
                            } else {
                                var[j].add(p);
                            }
                        }
                    }
                } else {
                    if (((F) Xi).name == F.MULTIPLY) {
                        if (((F) Xi).X[0] instanceof F) {//случай когда в произведении стоят оба объекта типа F - (-1)*x
                            if (((F) Xi).X[1] instanceof F) {
                                for (int j = 0; j < varFname.size(); j++) {
                                    if (((Fname) ((F) ((F) Xi).X[1]).X[0]).name.equals(((Fname) varFname.get(j)).name)) {
                                        if (((F) ((F) Xi).X[0]).X[0] instanceof Polynom) {
                                            var[j].add(((F) ((F) Xi).X[0]).X[0]);
                                        } else {
                                            var[j].add(Polynom.polynomFromNumber(((F) ((F) Xi).X[0]).X[0], ring));
                                        }
                                    }
                                }
                            }
                        }
                        if (((F) Xi).X[0] instanceof Polynom) {
                            if (!(((F) Xi).X[1] instanceof F)) {
                                for (int j = 0; j < varFname.size(); j++) {
                                    if (((Fname) ((F) Xi).X[1]).name.equals(((Fname) varFname.get(j)).name)) {
                                        if (((F) Xi).X[0] instanceof Polynom) {
                                            var[j].add(((F) Xi).X[0]);
                                        } else {
                                            var[j].add(Polynom.polynomFromNumber(((F) Xi).X[0], ring));
                                        }
                                    }
                                }
                            } else {
                                if (((F) ((F) Xi).X[1]).name == F.d) {
                                    for (int j = 0; j < varFname.size(); j++) {
                                        if (((Fname) ((F) ((F) Xi).X[1]).X[0]).name.equals(((Fname) varFname.get(j)).name)) {
                                            if (((F) ((F) Xi).X[1]).X.length > 2) {
                                                var[j].add(new Polynom(new int[] {((F) ((F) Xi).X[1]).X[2].intValue()}, new Element[] {NumberR64.ONE}).multiply(((F) Xi).X[0], ring));
                                            } else {
                                                var[j].add(p.multiply(((F) Xi).X[0], ring));
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (!(((F) Xi).X[1] instanceof F)) {
                                for (int j = 0; j < varFname.size(); j++) {
                                    if (((Fname) ((F) Xi).X[1]).name.equals(((Fname) varFname.get(j)).name)) {
                                        if (((F) Xi).X[0] instanceof Polynom) {
                                            var[j].add(((F) Xi).X[0]);
                                        } else {
                                            var[j].add(Polynom.polynomFromNumber(((F) Xi).X[0], ring));
                                        }
                                    }
                                }
                            } else {
                                if (((F) ((F) Xi).X[1]).name == F.d) {
                                    for (int j = 0; j < varFname.size(); j++) {
                                        Element el = ((F) ((F) Xi).X[1]).X[0];
                                        if (el instanceof Fname) {
                                            if (((Fname) el).name.equals(((Fname) varFname.get(j)).name)) {
                                                if (((F) ((F) Xi).X[1]).X.length > 2) {
                                                    var[j].add(new Polynom(new int[] {((F) ((F) Xi).X[1]).X[2].intValue()}, new Element[] {NumberR64.ONE}).multiply(((F) Xi).X[0], ring));
                                                } else {
                                                    var[j].add(p.multiply(((F) Xi).X[0], ring));
                                                }
                                            }
                                        } else if (((Fname) ((F) ((F) ((F) Xi).X[1]).X[0]).X[0]).name.equals(((Fname) varFname.get(j)).name)) {
                                            if (((F) ((F) Xi).X[1]).X.length > 2) {
                                                var[j].add(new Polynom(new int[] {((F) ((F) Xi).X[1]).X[2].intValue()}, new Element[] {NumberR64.ONE}).multiply(((F) Xi).X[0], ring));
                                            } else {
                                                var[j].add(p.multiply(((F) Xi).X[0], ring));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (((F) Xi).name == F.ADD || ((F) Xi).name == F.SUBTRACT) {//обработка + \ -
                    systDifEquationToMatrix(((F) ((F) f).X[i]), var, varFname, ring);
                }
            }
        }
    }

    /**
     * Метод преобразования по Лапласу входной системы дифференциальных
     * уравнений
     *
     * @param f - система дифференциальных уравнений
     *
     * @return - возвращает матрицу преобразования
     */
    public Element[][] systDifEquationToMatrix(F f, int k, Vector varFname, Ring ring) {
        Element[][] masPol = new Element[f.X.length / 2][];
        //    if (f instanceof F) {
        int s = 0;
        for (int i = 0; i < f.X.length; i += 2) {
            Vector<Element> var[] = new Vector[k];
            try {
                Element right_f = ring.CForm.ExpandForYourChoise(((F) f.X[i]), 1, 1, 1, -1, 1);//.expand(1, 1, 1, 1, -1, 1, nb);
                for (int ii = 0; ii < var.length; ii++) {
                    var[ii] = new Vector();
                }
                systDifEquationToMatrix(right_f, var, varFname, ring);
            } catch (Exception ex) {
                ring.exception.append(
                        "Exeption in systDifEquationToMatrix in package LaplaceTransform: " + ex);
            }
            //суммирование элементов в векторах
            Element[] str = new Element[k];//от кольца
            for (int g = 0; g < k; g++) {
                Element[] x = new Element[var[g].size()];
                var[g].copyInto(x);
                str[g] = Polynom.polynom_zero(NumberR64.ONE);//0
                for (int l = 0; l < x.length; l++) {
                    str[g] = str[g].add(x[l], ring);
                }
            }
            masPol[s] = str;
            s++;
        }
        return masPol;
    }

    //обработка начальных условий
    /**
     * разбор начальных условий по массивам их значений
     *
     * @param k - количество переменных по которым есть дифференциалы в исходной
     * системе уравнений
     * @param f - функция начальных условий
     *
     * @return
     */
    public Element[][] arrayInitCond(int k, F f, Vector v, Ring ring) {
        Vector<Element> var[] = new Vector[k];
        for (int i = 0; i < k; i++) {
            var[i] = new Vector<Element>();
        }
        for (int i = 0; i < f.X.length; i++) {
            if (f.X[i] instanceof F) {
                if (((F) f.X[i]).name == F.d) {
                    for (int j = 0; j < v.size(); j++) {
                        String s1 = ((Fname) ((F) f.X[i]).X[0]).name;
                        String s2 = (((Fname) v.get(j)).name);
                        if (s1.equals(s2)) {
                            if (f.X[i + 1].isZero(ring)) {
                                var[j].add(NumberR64.ZERO);
                            } else {
                                var[j].add(f.X[i + 1]);//добавляем значения коэффициентов
                            }
                        }
                    }
                }
            }
        }
        Element[][] res = new Element[k][];
        for (int j = 0; j < k; j++) {
            Element[] x = new Element[var[j].size()];
            var[j].copyInto(x);
            res[j] = x;
        }
        return res;
    }

    /**
     * Подстановка начальных условий в полиномиальную матрицу
     *
     * @param pol - полином из матрицы полиномов исходной системы
     * дифференциальных уравнений
     * @param pols - массив значений начальных условий
     *
     * @return
     */
    public Element polOfSysDifEquationInitialCond(Polynom pol, Element[] pols, Ring ring) {
        if (pol.isZero(ring)) {
            return Polynom.polynom_zero(NumberR64.ONE);
        }
        if (pol.powers.length == 0) {
            return Polynom.polynom_zero(NumberR64.ONE);
        }

        Polynom[] p = new Polynom[pols.length];
        Element result = Polynom.polynom_zero(NumberR64.ONE);
        Polynom zero = Polynom.polynom_zero(NumberR64.ONE);
        for (int i = 0; i < pols.length; i++) {
            int[] n;
            Element[] m;
            //////////////////NEW!!!!!!!!!!!!!!!!!!!!!!!///
            if (pol.powers.length == 0) {
                result = result.add(Polynom.polynom_zero(NumberR64.ONE), ring);
            } else {
                if (pol.powers[pol.powers.length - 1] == 0) {
                    n = new int[pol.powers.length - 1];
                    m = new Element[pol.coeffs.length - 1];
                } else {
                    n = new int[pol.powers.length];
                    m = new Element[pol.coeffs.length];
                }
                for (int j = 0; j < n.length; j++) {
                    n[j] = pol.powers[j] - 1;
                }
                for (int j = 0; j < n.length; j++) {//работа с отриуательными степенями ...кольцо!!!
                    if (n[j] == -1) {
                        n[j] = 0;
                    }
                }
                for (int j = 0; j < m.length; j++) {
                    m[j] = pol.coeffs[j];
                }
                if (n.length == 0) {
                    p[i] = zero;
                    result = result.add(p[i], ring);
                    break;
                }
                if (n.length == 1 && (n[0] == 0)) {
                    n = new int[] {};
                }
                p[i] = new Polynom(n, m);
                pol = p[i];
                if (result.add(p[i].multiply(pols[i], ring), ring) instanceof Fname) {
                    result =
                            (Fname) result.add(p[i].multiply(pols[i], ring), ring);
                } else {
                    result = result.add(p[i].multiply(pols[i], ring), ring);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                }
            }
        }
        return (result instanceof Polynom)
                ? ((Polynom) result).ordering(0, 0, ((Polynom) result).coeffs.length, ring) : result;

    }

    /**
     * Вычисление полиномов начальных условий
     *
     * @param M Element[][] - матрица полиномов
     * @param coeff Element[][] - начальные значения
     *
     * @return Polynom[] - массив полиномов начальных условий для каждого
     * дифференциального уравнения системы LDE
     */
    public Element[] initCondLaplaceTransform(Element[][] M,
            Element[][] coeff, Ring ring) {
        int k = 0; //счетчик по элементам выходного массива
        Element[] pol = new Element[coeff.length]; //создаем массив полиномов начальных условий размера равного размеру входного массива значений начальных условий по каждой переменной
        for (int i = 0; i < M.length; i++) {
            Element p = Polynom.polynomZero; //создаем нулевой полином  {M[0][0].zero()}
            for (int j = 0; j < M[i].length; j++) {
                //  Element p1 = polOfSysDifEquationInitialCond((Polynom)M[i][j], //p+(полученный полином после применения начальных условий к каждому операторному уравнению)
                //         coeff[j], ring);
                p = p.add(polOfSysDifEquationInitialCond((Polynom) M[i][j], //p+(полученный полином после применения начальных условий к каждому операторному уравнению)
                        coeff[j], ring), ring);
            }
            pol[k] = p;
            k++;
        }
        return pol;
    }

    /**
     * Преобразует по прямому преобразованию Лапласа все правые части
     * дифференциальных уравнений входной системы LDEs
     *
     * @param f - входная система LDE
     *
     * @return - массив функций, каждая является результатом прямого
     * преобразования Лапласа
     */
    public Element[] vectorLaplaceTransform(F f, Ring ring) {
        F[] F_toLapalceTransform = new F[f.X.length / 2];//новый массив для хранения функций преобразованных по прямому преобразованию Лапласа
        int k = 0;
        for (int i = 1; i < f.X.length; i += 2) {
            F_toLapalceTransform[k] = new LaplaceTransform().transform(f.X[i], ring);
            k++;
        }
        return F_toLapalceTransform;
    }

    public Element[] vectorLPT1(F f, ArrayList<Element> a, Ring ring) {
        ArrayList<Element> b = new ArrayList<Element>();
        for (int i = 0; i < f.X.length; i++) {
            switch (((F) f.X[i]).name) {
                case F.SUBTRACT:
                case F.ADD: {
                    Element[] aa = vectorLPT1(((F) f.X[i]), b, ring);
                    for (int j = 0; j < aa.length; j++) {
                        b.add(aa[j]);
                    }
                    break;
                }
                default: {
                    b.add(f.X[i]);
                }
            }
        }
        Element[] list = new Element[b.size()];
        b.toArray(list);
        return list;
    }

    /**
     *
     * @param f
     * @param ring
     *
     * @return
     */
    public Element[][] vectorLPT(F f, Ring ring) {
        Element[][] F_toLapalceTransform = new Element[f.X.length / 2][];//новый массив для хранения функций преобразованных по прямому преобразованию Лапласа
        int k = 0;
        for (int i = 1; i < f.X.length; i += 2) {
            F ff = new LaplaceTransform().transform(f.X[i], ring);
            ArrayList<Element> a = new ArrayList<Element>();
            F_toLapalceTransform[k] = vectorLPT1(ff, a, ring);
            k++;
        }
        return F_toLapalceTransform;
    }

    /**
     * возвращает старшую степень полинома числителя, для задачи ch/zn
     *
     * @param ch Polynom
     *
     * @return int
     */
    public static int maxPowersCH(Polynom ch) {
        int max = 0; //значение максимальной степени
        for (int i = 0; i < ch.powers.length; i++) {
            for (int j = 0; j < ch.powers.length; j++) {
                if ((i != j) && (ch.powers[i] > ch.powers[j])) {
                    max = ch.powers[i];
                }
            }
        }
        return max;
    }

    /**
     * возвращает старшую степень для числителя, для задачи ch/zn, для случая
     * когда числитель представляет собой объект типа F
     *
     * @param ch - произвольный Element
     *
     * @return int
     */
    public static int maxPowersCH_F(Element ch) {
        int max = 0; //значение максимальной степени
        IntList aa = new IntList();
        int k = 0;
        if (ch instanceof F) {
            F f = ((F) ch);
            for (int i = 0; i < f.X.length; i++) {
                if (f.X[i] instanceof F) {
                    if (((F) f.X[i]).name == F.MULTIPLY) {
                        F f1 = ((F) f.X[i]);
                        for (int j = 0; j < f1.X.length; j++) {
                            if (f1.X[j] instanceof Polynom) {
                                Polynom pp = ((Polynom) f1.X[j]);
                                if (pp.powers.length == 1) {
                                    aa.arr[k] = pp.powers[0];
                                    k++;
                                } else {
                                    aa.arr[k] = 0;
                                    k++;
                                }
                            }
                        }
                    }
                } else {
                    if (f.X[i] instanceof Polynom) {
                        Polynom pp = ((Polynom) f.X[i]);
                        if (pp.powers.length == 1) {
                            aa.arr[k] = pp.powers[0];
                            k++;
                        } else {
                            aa.arr[k] = 0;
                            k++;
                        }
                    }
                }
            }
        }
        max = aa.arr[0];
        for (int z = 1; z < k; z++) {
            if (aa.arr[z] > max) {
                max = aa.arr[z];
            }
        }
        return max;
    }

    /**
     * Возвращает преобразованный FactorPol//в случае если встречается
     * (x+1)...(x+4)...(x-0)...преобразует к виду -(x+1)...(x+4)...(x)...
     *
     * @return FactorPol &&&&& ??????????
     */
    public FactorPol withZero(FactorPol p, Ring ring) {
        int[] power = new int[p.powers.length];
        for (int i = 0; i < p.powers.length; i++) {
            power[i] = p.powers[i];
        }
        Polynom zero = p.multin[0].myZero(ring);
        Polynom[] pol = new Polynom[p.multin.length];
        int[] shift = new int[p.multin.length];
        for (int i = 0; i < p.multin.length; i++) {
            for (int j = 0; j < p.multin[i].coeffs.length; j++) {
                if (p.multin[i].coeffs[j].isZero(ring) == true) {//equals(zero.coeffs[0])
                    shift[i] += 1;
                }
            }
        }
        for (int i = 0; i < p.multin.length; i++) {
            int k = 0;
            int[] pow = new int[p.multin[i].powers.length - shift[i]]; //степени полинома
            Element[] coeff = new Element[p.multin[i].coeffs.length - shift[i]]; //коэффициенты полинома
            for (int j = 0; j < p.multin[i].coeffs.length; j++) {
                if (p.multin[i].coeffs[j].isZero(ring) != true) {//equals(zero.coeffs[0])
                    coeff[k] = p.multin[i].coeffs[j];
                    if (p.multin[i].powers.length != 0)//случай когда полином числовой
                    {
                        pow[k] = p.multin[i].powers[j];
                    } else {
                        pow = p.multin[i].powers;
                    }
                    k++;
                }
            }
            pol[i] = new Polynom(pow, coeff);
        }

        FactorPol fct = new FactorPol(power, pol);
        return fct;
    }

    /**
     * проверка на решение дроби методом неопределенных коэффициентов
     *
     * @param ch
     * @param zn
     *
     * @return
     */
    public static boolean izprime(Polynom ch, FactorPol zn) {
        int maxpow = 0;
        for (int i = 0; i < zn.powers.length; i++) {
            if (zn.powers[i] > maxpow) {
                maxpow = zn.powers[i];
            }
        }
        if ((ch.powers.length == 0) && (maxpow > 1) && (zn.multin.length == 1)) {
            return false;
        }
        return true;
    }

    /**
     * Возвращает объект типа Factor, в котором будут хранятся коэффициенты типа
     * Func и степени при единственной переменной
     *
     * @param max - максимальная степень при единственной переменной
     * @param f
     *
     * @return
     */
    public static Factor F2FuncCoeffsPol(int max, Element f, Ring ring) {
        Factor res = new Factor();
        if (f instanceof F) {
            F w = ((F) f);
            for (int i = 0; i < w.X.length; i++) {
                if (w.X[i] instanceof Polynom) {
                    res.multin.add(((Polynom) w.X[i]).coeffs[0]);
                    // res.powers.add(new NumberZ(0));
                    res.powers.add(new NumberZ(((Polynom) w.X[i]).powers[0]));
                } else if (w.X[i] instanceof Fname) {
                    res.multin.add(w.X[i]);
                    res.powers.add(new NumberZ(0));
                } else if (w.X[i] instanceof F) {
                    F q = ((F) w.X[i]);
                    if ((q.X[0] instanceof Complex) || (q.X[0] instanceof NumberR64)) {
                        if (q.X[1] instanceof Fname) {
                            res.multin.add(q.X[0].multiply(q.X[1], ring));
                            res.powers.add(new NumberZ(0));
                        }
                    } else if (q.X[0] instanceof Polynom) {
                        Polynom pp = ((Polynom) q.X[0]);
                        if (pp.powers.length == 1) {
                            res.multin.add(q.X[1].multiply(pp.coeffs[0], ring));
                            res.powers.add(new NumberZ(pp.powers[0]));
                        } else {
                            if (pp.coeffs[0].signum() == -1) {
                                res.multin.add(q.X[1].multiply(pp.coeffs[0], ring));
                                res.powers.add(new NumberZ(0));
                            } else {
                                res.multin.add(q.X[1].multiply(pp.coeffs[0], ring));
                                res.powers.add(new NumberZ(0));
                            }
                        }
                    }
                    if (q.X[1] instanceof Polynom) {
                        Polynom pp = ((Polynom) q.X[1]);
                        if (pp.powers.length == 1) {
                            res.multin.add(q.X[0].multiply(pp.coeffs[0], ring));
                            res.powers.add(new NumberZ(pp.powers[0]));
                        } else {
                            if (pp.coeffs[0].signum() == -1) {
                                res.multin.add(q.X[0].negate(ring).multiply(pp.coeffs[0], ring));
                                res.powers.add(new NumberZ(0));
                            } else {
                                res.multin.add(q.X[0].multiply(pp.coeffs[0], ring));
                                res.powers.add(new NumberZ(0));
                            }
                        }

                    }
                } else {
                    res.multin.add(w.X[i]);
                    res.powers.add(new NumberZ(0));
                }
            }
        } else {
            if (f instanceof Fname) {
                res.multin.add(f);
                res.powers.add(new NumberZ(0));
            }
        }
        return res;
    }

    /**
     * Метод нормализации объекта типа FactorPol
     *
     * @param zn
     * @param ring
     *
     * @return - 0 - числовой коэффициент, 1 - объект типа FactorPol
     */
    public Element[] normCoeffFactor(FactorPol zn, Ring ring) {
        Element[] result = new Element[2];
        if (zn.multin.length != 1) {
            ArrayList<Integer> arr = new ArrayList<Integer>();
            zn.normalForm(ring);
            Element delet = new Complex(1.0, 0.0); //присваеваем на первом этапе 1 коэффициент
            int k = 0;//количество чисел при факторизации в полиноме
            for (int i = 0; i < zn.multin.length; i++) {
                if (zn.multin[i].isItNumber()) {
                    //если первый полином в FactorPol - есть число ...
                    Complex c = new Complex(zn.multin[i].coeffs[0].doubleValue(), 0.0);//запоминаем коэффициент
                    delet = delet.multiply(c, ring);
                    k++;
                    arr.add(i);
                }
            }
            Integer[] arrs = new Integer[arr.size()];
            arr.toArray(arrs);
            result[0] = delet;
            int[] pow = new int[zn.powers.length - k];
            Polynom[] mul = new Polynom[zn.multin.length - k];
            int t = 0;
            for (int i = 0; i < zn.multin.length; i++) {
                if (!isParam(arrs, i)) {
                    pow[t] = zn.powers[i];
                    mul[t] = zn.multin[i];
                    t++;
                }
            }
            zn = new FactorPol(pow, mul);
            result[1] = zn;
        }
        //NEW!!!!!!!!!!!!!!!!!!!!!!!!!2013.26.12
        if (zn.multin.length == 1) {
            result[0] = ring.numberONE();
            result[1] = zn;
        }
        return result;
    }

    public static boolean isParam(Integer[] n, int h) {
        boolean f = false;
        for (int i = 0; i < n.length; i++) {
            if (h == n[i]) {
                f = true;
                break;
            }
        }
        return f;
    }

    public Element[] testPrimeFractionForSingle(Element ch, FactorPol zn, Ring ring) {
        Element[] solv = new Element[] {};
        if (zn.multin.length != 1) {
            Element[] arr = normCoeffFactor(zn, ring);
            Element delet = arr[0];
            zn = (FactorPol) arr[1];
//            zn.normalForm(ring);
//            Element delet = new Complex(1.0, 0.0); //присваеваем на первом этапе 1 коэффициент
//            int k = 0;//количество чисел при факторизации в полиноме
//            for (int i = 0; i < zn.multin.length; i++) {
//                if (zn.multin[i].isItNumber()) {
//                    //если первый полином в FactorPol - есть число ...
//                    Complex c = new Complex(zn.multin[0].coeffs[0].doubleValue(), 0.0);//запоминаем коэффициент
//                    delet = delet.multiply(c, ring);
//                    k++;
//                }
//            }
//            int[] pow = new int[zn.powers.length - k];
//            Polynom[] mul = new Polynom[zn.multin.length - k];
//            for (int i = k; i < zn.multin.length; i++) {
//                pow[i - k] = zn.powers[i];
//                mul[i - k] = zn.multin[i];
//            }
//            zn = new FactorPol(pow, mul);
            System.out.println("Числитель дроби" + ch.toString(ring));
            System.out.println("Знаменатель дроби" + zn.toString(ring));
            if (delet.isMinusOne(ring)) {
                Element el = ((Polynom) ch).multiply(delet, ring);
                if (!(el instanceof Polynom)) {
                    solv = primeFractionForSingle(new Polynom(((Polynom) ch).multiply(delet, ring)), zn, delet.multiply(delet, ring), ring);//New!!!
                } else {
                    solv = primeFractionForSingle((Polynom) ((Polynom) ch).multiply(delet, ring), zn, delet.multiply(delet, ring), ring);//New!!!
                }
            } else {
                if (ch instanceof F) {
                    try {
                        solv = primeFractionForSingle(ch, zn, ring);//New!!!
                    } catch (Exception ex) {
                        Logger.getLogger(SystemLDE.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    solv = primeFractionForSingle((Polynom) ch, zn, delet, ring);//New!!!
                }
            }
            System.out.println("Числители для дробей" + Array.toString(solv));

        } else {
            solv = new Element[] {ch};
        }
        return solv;
    }

    /**
     *
     * @param ch - числитель дроби
     * @param zn - знаменатель дроби
     * @param delet - общий коэффициент для сокращения
     * @param ring
     *
     * @return
     */
    public static Element[] primeFractionForSingle(Polynom ch, FactorPol zn, Element delet, Ring ring) {
        //определяем количество столбцов в матрице А
        int k_col = 0;
        for (int j = 0; j < zn.multin.length; j++) {
            k_col = k_col + zn.multin[j].powers[0] * zn.powers[j];
        }
        //определяем количество строк в матрице А
        Polynom p = new Polynom("1", ring);
        for (int h = 0; h < zn.multin.length; h++) {
            for (int q = 0; q < zn.powers[h]; q++) {
                p = p.multiply(zn.multin[h], ring);
            }
        }
        int k_row = p.powers[0];
        Element[] b = null;
        Element[][] a = null;
        if (ch.powers.length != 0) {
            if (k_row > ch.powers[0]) {
                b = new Element[k_row + 1];
                a = new Element[k_row + 1][k_col + 1];
            } else {
                b = new Element[ch.powers[0] + 1];
                a = new Element[ch.powers[0] + 1][k_col + 1];
            }
        } else {
            b = new Element[k_row];//k_row + 1
            a = new Element[k_row][k_col + 1];//[k_row + 1][k_col + 1]
        }
        //заполняем столбец 0
        for (int w = 0; w < b.length; w++) {
            b[w] = NumberR64.ZERO;
        }
        //заполняем матрицу 0
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] = NumberR64.ZERO;
            }
        }
        //заполняем столбец b коэффициентами из числителя
        if (ch.powers.length != 0) {
            for (int l = 0; l < ch.powers.length; l++) {
                b[b.length - ch.powers[l] - 1] = ch.coeffs[l];
            }
        } else {
            b[b.length - 1] = ch.coeffs[0];
        }

        ArrayList<FactorPol> af = new ArrayList<FactorPol>();//все знаменатели
        IntList ap = new IntList();//все числители
        for (int i = 0; i < zn.multin.length; i++) {
            for (int j = 0; j < zn.powers[i]; j++) {
                af.add(new FactorPol(new int[] {j + 1}, new Polynom[] {zn.multin[i]}));
                ap.add(zn.multin[i].powers[0]);
            }
        }

        Polynom polnew[] = new Polynom[af.size()]; //создаём массив полиномов
        Polynom pr = zn.multin[0].myOne(ring); //вспомогательный полином равный 1
        for (int j = 0; j < zn.powers.length; j++) { //вычисляем произведение полиномов (знаменатель)
            if (zn.powers[j] == 1) {
                pr = pr.multiply(zn.multin[j], ring);
            }
            if (zn.powers[j] != 1) {
                for (int i = 0; i < zn.powers[j]; i++) {
                    pr = pr.multiply(zn.multin[j], ring);
                }
            }
        }
        int ds1 = 0; //вспомогательная переменная
        int ds2 = 0; //вспомогательная переменная
        for (int a1 = 0; a1 < zn.powers.length; a1++) { //вычисляем добавки к дробям
            Polynom del = pr;
            for (int m = 0; m < zn.powers[a1]; m++) {
                del = del.divideExact(zn.multin[a1], ring);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                polnew[m + ds1] = del;
                ds2 = ds2 + 1;
            }
            ds1 = ds2;
        }

        int m = 0;//счетчик по столбцам
        int g = 0;
        while (m < k_col) {
            for (int j = 0; j < ap.arr[g]; j++) {
                if (polnew[g].powers.length != 0) {
                    for (int i = 0; i < polnew[g].powers.length; i++) {
                        a[a.length - polnew[g].powers[i] - 1 - j][m] = polnew[g].coeffs[i];
                    }
                } else {
                    a[a.length - 1 - j][m] = polnew[g].coeffs[0];
                }
                m++;
            }
            g++;
        }
        for (int i = 0; i < a.length; i++) {
            a[i][k_col] = b[i];
        }
        System.out.println("************************");
        System.out.println(Array.toString(b));
        System.out.println("************************");
        System.out.println(Array.toString(a, ring));
        System.out.println("************************");
        //решение системы
        MatrixS M1 = new MatrixS(a, ring); //преобразование в матрицу над полиномами!!!
        System.out.println("====================MatrixS======================");
        System.out.println(M1.toString(ring));
        System.out.println("==================================================");
        System.out.println("=============MatrixS.toEchelonForm================");
        MatrixS M2 = null;
        CanonicForms cfs = null;
        M2 = M1.toEchelonForm(ring); //преобразование матрицы к виду - на главной диагонале стоят определители а в последнем столбце свободные члены
        System.out.println(M2.toString());
        System.out.println("==================================================");
        Element[] sysSolve = new Element[M2.colNumb - 1];
        if (M2.isSysSolvable()) {
            sysSolve = M2.oneSysSolv_and_Det(ring);
        } else {
            System.out.println("System error!!!");
            return sysSolve;
        }
        System.out.println("решение системы");
        System.out.println(Array.toString(sysSolve));

        int v1 = 0;
        int v2 = 0;
        Element det = sysSolve[sysSolve.length - 1];
        Element[] solve = new Element[af.size()];
        while (v1 < af.size()) {
            if (ap.arr[v1] == 2) {
                if (!(sysSolve[v2 + 1] instanceof Complex)) {//!!!!!!!!!!!!!!!!!!!!!!
                    sysSolve[v2 + 1] = new Complex(sysSolve[v2 + 1].doubleValue());
                }
                if (!(sysSolve[v2] instanceof Complex)) {////!!!!!!!!!!!!!!!!!!!!!!!!
                    sysSolve[v2] = new Complex(sysSolve[v2].doubleValue());
                }
                solve[v1] = new Polynom(new int[] {1, 0}, new Element[] {sysSolve[v2 + 1].divide(det, ring), sysSolve[v2].divide(det, ring)});
                v2 += ap.arr[v1];
            } else {
                if (!(sysSolve[v2] instanceof Complex)) {//!!!!!!!!!!!!!!!!!!!!!
                    sysSolve[v2] = new Complex(sysSolve[v2].doubleValue());
                }
                solve[v1] = new Polynom(new int[] {0}, new Element[] {sysSolve[v2].divide(det, ring)});
                v2 += ap.arr[v1];
            }
            v1++;
        }
        //убираем нулевые коэффициенты и делим на общий коэффициент числовой !!!
        for (int i = 0; i < solve.length; i++) {
            if (!delet.isOne(ring))//если общий коэффицент не равен 1 то сокращаем на него, полученные коэффициенты в числителях
            {
                solve[i] = ((Polynom) solve[i]).divide(delet, ring);//c[i] / delet
            }
            solve[i] = ((Polynom) solve[i]).deleteZeroCoeff(ring);
        }
        System.out.println("РЕШЕНИЕ!!!!!");
        System.out.println(Array.toString(solve));
        System.out.println("ЗНАМЕНАТЕЛИ");
        for (int i = 0; i < af.size(); i++) {
            System.out.println(i + " = " + af.get(i));
        }
        return solve;
    }

    /**
     * возвращает вектор решений
     *
     * @param ch Polynom
     * @param zn FactorPol
     *
     * @return Element[]
     *
     * @author Ribakov Mixail
     */
    public Element[] primeFractionForSingle(Element ch, FactorPol zn, Ring ring) throws
            Exception {
        Element delet = new Complex(1.0, 0.0); //присваеваем на первом этапе 1 коэффициент
        //&& (zn.multin[0].powers[0] == 0)
        if ((zn.multin[0].isOne(ring)) || (zn.multin[0].isMinusOne(ring)) || ((zn.multin[0].coeffs.length == 1) && (zn.multin[0].powers.length == 0))) {
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
        int n = 0; // колличество дробей
        for (int d = 0; d < zn.powers.length; d++) {
            n = n + zn.powers[d];
        }
        FactorPol z1 = new SystemLDE().withZero(zn, ring); //убираем нулевые коэффициенты в multin

        Polynom polnew[] = new Polynom[n]; //создаём массив полиномов --- (добавки)
        Polynom pr = z1.multin[0].myOne(ring); //вспомогательный полином равный 1
        for (int j = 0; j < z1.powers.length; j++) { //вычисляем произведение полиномов (знаменатель)
            if (z1.powers[j] == 1) {
                pr = pr.multiply(z1.multin[j], ring);
            }
            if (z1.powers[j] != 1) {
                for (int i = 0; i < z1.powers[j]; i++) {
                    pr = pr.multiply(z1.multin[j], ring);
                }
            }
        }
        int ds1 = 0; //вспомогательная переменная
        int ds2 = 0; //вспомогательная переменная
        for (int a = 0; a < z1.powers.length; a++) { //вычисляем добавки к дробям
            Polynom del = pr;
            for (int m = 0; m < z1.powers[a]; m++) {
                del = del.divideExact(z1.multin[a], ring);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                polnew[m + ds1] = del;
                ds2 = ds2 + 1;
            }
            ds1 = ds2;
        }
        boolean fl = true;//отвечает за числитель...
        int L = 0;
        if (ch instanceof Polynom) {
            if (((Polynom) ch).powers.length != 0) {
                // L = ch.powers[0]; //величина старшей степени полинома в числителе
                L = ((Polynom) ch).powers[0];//maxPowersCH(((Polynom) ch)); //величина старшей степени полинома в числителе
            } else {
                L = 0;
            }
        } else {
            if (!(ch instanceof F)) {
                L = 0;
            } else {
                L = maxPowersCH_F(ch);
            }
        }
        int power_max = (polnew[0].powers.length == 0) ? 0 : polnew[0].powers[0]; //старшая степень среди полиномов (добавок)
        for (int d1 = 1; d1 < polnew.length; d1++) {
            if (polnew[d1].powers.length != 0) {
                if (power_max < polnew[d1].powers[0]) {
                    power_max = polnew[d1].powers[0];
                }
            }
        }
        int b_length = 0;
        if (L >= power_max) {
            b_length = 2 * (L + 1); //массив свободных коэффициентов
        }
        if (L < power_max) {
            b_length = 2 * (power_max + 1); //массив свободных коэффициентов
        }
        //NumberC64[] b = new NumberC64[b_length]; //массив свободных коэффициентов
        Element[] b = new Element[b_length]; //массив свободных коэффициентов //double
        int n1 = (int) b_length / 2;
        // int varstep = 0;
        if (ch instanceof Polynom) {
            if (((Polynom) ch).powers.length != 0) {
                for (int m = ((Polynom) ch).powers.length - 1; m >= 0; m--) { //вычисляем столбец свободных членов
                    b[(b_length / 2 - 1) - ((Polynom) ch).powers[m]] = ((Polynom) ch).coeffs[m]; //doubleValue();
                    // varstep++;
                }
            } else {
                for (int m = 1; m < b_length; m++) { //вычисляем столбец свободных членов
                    b[m] = Complex.C64_ZERO; // 0
                }
                b[b_length / 2 - 1] = ((Polynom) ch).coeffs[0]; //doubleValue();
            }
        } else {//случай когда числитель не Polynom
            if (!(ch instanceof F) && !(ch instanceof Fname)) {///NEWWWWWWWWWW
                for (int m = 1; m < b_length; m++) { //вычисляем столбец свободных членов
                    b[m] = Complex.C64_ZERO; // 0
                }
                b[b_length / 2 - 1] = ch; //doubleValue();
            } else {
                fl = false;
                Factor fff = new Factor();
                fff = F2FuncCoeffsPol(L, ch, ring);
                Element[] arrayB = new Element[L + 1];
                for (int gg = 0; gg < L + 1; gg++) {
                    arrayB[gg] = Polynom.polynomZero;
                }
                int kk = 0;
                for (int hh = 0; hh < fff.powers.size(); hh++) {
                    arrayB[L - fff.powers.get(hh).intValue()] = arrayB[L - fff.powers.get(hh).intValue()].add(fff.multin.get(hh), ring);
                }
                int t = 0;
                for (int m = arrayB.length - 1; m >= 0; m--) { //вычисляем столбец свободных членов
                    b[(b_length / 2 - 1) - t] = arrayB[m]; //doubleValue();
                    t++;
                }
            }
        }
        Element[][] matrix = new Element[b_length][2 * n + 1]; //создание матрицы!!!!! //double
        for (int q = 0; q < b_length; q++) { //заполняем последний столбец матрицы свободными членами
            matrix[q][2 * n] = b[q];
        }
        ///////////////////////////M/////////////////////////////////////
        for (int h = 0; h < n; h++) { // заполняем матрицу
            for (int d = 0; d < polnew[h].coeffs.length; d++) {
                Complex comp = (Complex) polnew[h].coeffs[d].toNumber(Ring.C64, ring);
                //doubleValue();
                if (polnew[h].powers.length == 0) {////////новый блок обработки
                    matrix[power_max - d][h] = comp.re; //A
                    matrix[power_max - d][h + n] = comp.im.negate(ring); //-B
                    matrix[power_max - d + n][h] = comp.im; //B
                    matrix[power_max - d + n][h + n] = comp.re; //A
                } else if (power_max >= polnew[h].powers.length) {
                    matrix[power_max
                            - polnew[h].powers[d]][h] = comp.re; //A////////power_max
                    matrix[power_max - polnew[h].powers[d]][h
                            + n] = comp.im.negate(ring); //-B
                    matrix[power_max - polnew[h].powers[d]
                            + n][h] = comp.im; //B
                    matrix[power_max - polnew[h].powers[d] + n][h
                            + n] = comp.re; //A
                } else if (power_max < polnew[h].powers.length) {
                    matrix[d][h] = comp.re; //A
                    matrix[d][h + n] = comp.im.negate(ring); //-B
                    matrix[d + n][h] = comp.im; //B
                    matrix[d + n][h + n] = comp.re; //A
                }
            }
        }

        int k1 = matrix.length; //размерность матрицы
        Element[][] sc = new Element[k1][matrix[0].length];
        for (int i = 0; i < k1; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == null || matrix[i][j].isZero(ring)) { //== 0
                    if (i != 0) {
                        if (sc[i - 1][j] instanceof Fname) {
                            sc[i][j] = ring.numberZERO;
                        } else {
                            sc[i][j] = sc[i - 1][j].zero(ring);
                        }
                    } else {
                        sc[i][j] = sc[i][j - 1].zero(ring);
                    }
                } else {
                    sc[i][j] = matrix[i][j]; // new NumberR64(matrix[i][j]);
                }
            }
        }
//        System.out.println("=============Матрица преобразования==============");
//        //вывод полученной матрицы
//        for (int i = 0; i < sc.length; i++) {
//            for (int j = 0; j < sc[i].length; j++) {
//                System.out.print(sc[i][j].toString() + " | ");
//            }
//            System.out.println();
//        }
        MatrixS M1 = new MatrixS(sc, ring); //преобразование в матрицу над полиномами!!!
//        System.out.println("====================MatrixS======================");
//        System.out.println(M1.toString(ring));
//        System.out.println("==================================================");
//        System.out.println("=============MatrixS.toEchelonForm================");
        MatrixS M2 = null;
        CanonicForms cfs = null;
        if (fl == true) {
            M2 = M1.toEchelonForm(ring); //преобразование матрицы к виду - на главной диагонале стоят определители а в последнем столбце свободные члены
        } else {
            cfs = new CanonicForms(ring,true);
         //2015   cfs.makeNewVectFandVectEL();
            MatrixS el = (MatrixS) cfs.matrixF2P(M1);
            MatrixS eel = el.toEchelonForm(ring);
            M2 = (MatrixS) cfs.matrixP2F(eel);
        }
//        System.out.println(M2.toString());
//        System.out.println("==================================================");
        Element[] sysSolve = new Element[M2.colNumb - 1];
        if (M2.isSysSolvable()) {
            sysSolve = M2.oneSysSolv_and_Det(ring);
        } else {
            System.out.println("System error!!!");
            return sysSolve;
        }
//        if(fl == false){
//            for(int zz = 0; zz < sysSolve.length; zz++){
//                sysSolve[zz] = cfs.Univers_unconvert(sysSolve[zz]);
//            }
//        }
        int nn = 2 * n;// !!!!!!!!!1
        if (k1 != M2.M.length) {
            k1 = M2.M.length;
        } else {
            nn = 2 * n - 1;
        }
        Element[] solve = new Element[k1]; //создание массива решения
        for (int i = 0; i < M2.M.length; i++) {
            NumberR dlin = new NumberR(M2.M[i].length);
            if (!dlin.isOne(ring)) {
                if (M2.M[0][0].isOne(ring)) {
                    solve[i] = M2.M[i][M2.M[i].length - 1];
                } else {
                    solve[i] = M2.M[i][M2.M[i].length - 1].divide(M2.M[0][0], ring);
                }
            } else {
                if (M2.M[0][0].isOne(ring)) {
                    solve[i] = (NumberR.ZERO);
                } else {
                    solve[i] = (NumberR.ZERO);
                }
            }
        }
        Element[] solveM = new Element[solve.length / 2];
        for (int i = 0; i < solveM.length; i++) {
            if (solve[i] instanceof Fname) {
                if (delet.isOne(ring)) {
                    solveM[i] = new Complex(solve[i],
                            solve[i + k1 / 2]);
                } else {
                    solveM[i] = (new Complex(solve[i],
                            solve[i + k1 / 2])).divide(
                            delet, ring);
                }
            } else if (solve[i] instanceof F) {
                if (delet.isOne(ring)) {
                    solveM[i] = new Complex(solve[i],
                            solve[i + k1 / 2]);
                } else {
                    solveM[i] = (new Complex(solve[i],
                            solve[i + k1 / 2])).divide(
                            delet, ring);
                }
            } else {
                if (delet.isOne(ring)) {
                    solveM[i] = new Complex(solve[i],
                            solve[i + k1 / 2]);
                } else {
                    solveM[i] = (new Complex(solve[i],
                            solve[i + k1 / 2])).divide(
                            delet, ring);
                }
            }
        }
//        for (int i = 0; i < solveM.length; i++) {
//            System.out.println("solve[ " + i + " ]= " + solveM[i].toString(ring));
//        }
        return solveM;
    }

    /**
     * На входе некоторая сумма F 1) Преобразование каждого слагаемого в объект
     * типа Rational 2) Суммирование 3) Полученную одну дробь преобразуем в F
     *
     * @param f
     *
     * @return
     */
    public F addF_intoRational(F f, Ring ring) {
        return f.expand(ring);
    }

    /**
     * Суммирование преобразованных по Лапласу правых частей и полиномов
     * начальных условий
     *
     * @param m1-массив правых преобразованных частей по прямому преобразованию
     * Лапласа
     * @param m2-массив полиномов начальных условий
     *
     * @return - массив дробно-рациональных выражений
     */
    public Element[] rightPatSystLDELaplaceTransform(Element[] m1, Element[] m2, Ring ring) {
        F[] f = new F[m1.length];
        Element[] el = new Element[f.length];
        for (int i = 0; i < f.length; i++) {
            f[i] = new SystemLDE().addF_intoRational((F) m1[i], ring);//преобразование к одной дроби
            if (f[i].name == 0) {
                if (f[i].X.length == 1) {
                    Element div = f[i].X[0];
                    el[i] = new F(F.DIVIDE, new Element[] {div.add(m2[i], ring), ring.numberONE});
                }
            } else {
                el[i] = new F(F.DIVIDE, new Element[] {f[i].X[0].add(f[i].X[1].multiply(m2[i], ring), ring), f[i].X[1]});
            }
        }
        return el;
    }

    /*
     * Складывает дроби this и f, затем сокращает числитель и знаменатель
     * результата на их НОД и возвращает результат.
     * ///!!!!!!!!!! без сокращения
     * Параметры:
     * this, f   -  дроби типа Fraction
     */
    public Fraction addFR(Fraction f, Fraction f1, Ring ring) {
        if (f.num.signum() == 0 && f1.num.signum() == 0) {
            return f.myZero(ring);
        } else if (f.num.signum() == 0) {
            return f1;
        } else if (f1.num.signum() == 0) {
            return f;
        }
        Element resnum = f.num.multiply(f1.denom, ring).add(f1.num.multiply(f.denom, ring), ring);
        if (resnum.signum() == 0) {
            return f.myZero(ring);
        }
        Element resdenom = f.denom.multiply(f1.denom, ring);
        if (!(resnum instanceof F) && !(resdenom instanceof F)) {
            return new Fraction(resnum, resdenom);
        } else {
            return new Fraction(resnum, resdenom);
        }
    }

    /**
     * Возвращает трехмерный массив где в каждом элементе которого будет
     * записано произведение матрицы решений и матрицы с правыми частями
     *
     * @param mat Element[][] --- присоединенная матрица к полиномиальной
     * матрице по системе диф.ур.
     * @param pInput Polynom[][] --- матрица полиномов преобразованных по
     * прямому преобразованию Лапласа правых частей системы
     *
     * @return Element[][][]
     *
     * @throws PolynomException
     */
    public Element[] multiplyPolMatrix(Element[][] m, int[][] mm, Element[] b, Ring ring) {
        System.out.println("Столбец В: ");
        for (int i = 0; i < b.length; i++) {
            System.out.println(b[i].expand(ring).toString(ring));//simpl
        }
        System.out.println("Матрица М: ");
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j].toString(ring) + "  ");
            }
            System.out.println();
        }
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                for (int k = 0; k < ((Polynom) m[i][j]).coeffs.length; k++) {
                    ((Polynom) m[i][j]).coeffs[k] = ((Polynom) m[i][j]).coeffs[k].toNumber(Ring.C64, ring);
                }
            }
        }
        int n = b.length; //размер входной матрицы полиномов
        int N = m.length; //размер входной матрицы с промежуточными решениями
        Element[][] pOutput = new Element[N][];
        Element[] ppOut = null;
        Element el = null;
        for (int i = 0; i < N; i++) {
            ppOut = new Element[m[i].length];
            pOutput[i] = ppOut;
            int k = 0;
            for (int j = 0; j < m[i].length; j++) {

                //el = m[i][j].multiply(((F) b[k]).X[0], ring);
                el = m[i][mm[i][j]].multiply(((F) b[k]).X[0], ring);
                ppOut[j] = new F(F.DIVIDE, new Element[] {el, ((F) b[k]).X[1]});
                k++;
            }
        }
        System.out.println("Матрица М*B: ");
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < pOutput[i].length; j++) {
                System.out.print(pOutput[i][j].toString(ring) + "  ");
            }
            System.out.println();
        }
        //Переводим в дроби объекты типа F
        Fraction[][] rat = new Fraction[N][];
        for (int i = 0; i < N; i++) {
            Fraction[] rowFr = new Fraction[m[i].length];
            rat[i] = rowFr;
            for (int j = 0; j < m[i].length; j++) {
                rat[i][j] = new Fraction(((F) pOutput[i][j]).X[0], ((F) pOutput[i][j]).X[1]);
            }
        }
        Fraction[] res = new Fraction[n];
        Fraction h = null;
        for (int i = 0; i < N; i++) {
            h = rat[i][0];
            for (int j = 1; j < rat[i].length; j++) {

                Element Eh = h.add(rat[i][j], ring);//old
                if (Eh instanceof Fraction) {
                    h = (Fraction) Eh;
                } else {
                    h = new Fraction(Eh, ring.numberONE);
                }

                // h = addFR(h, rat[i][j], ring);
            }
            res[i] = h;
        }
        Element[] result = new Element[res.length];
        //result = res;
        //столбец после умножения
//        System.out.println("Матрица перед сокращением: ");
//        for (int i = 0; i < res.length; i++) {
//            System.out.print(res[i].toString(ring) + "  ");
//        }
        System.out.println("");
        Fraction[] res1 = res;/////!!!!!!!!!!!!!!!!
        result = cancelToMatrix(res, ring);
        for (int i = 0; i < result.length; i++) {
            if (((F) result[i]).X[0].isZero(ring) || ((F) result[i]).X[1].isZero(ring)) {
                Fraction h1 = null;

                h = rat[i][0];
                for (int l = 1; l < rat[i].length; l++) {

                    Element Eh = h.add(rat[i][l], ring);//old
                    if (Eh instanceof Fraction) {
                        h = (Fraction) Eh;
                    } else {
                        h = new Fraction(Eh, ring.numberONE);
                    }


                    // h = addFR(h, rat[i][j], ring);
                }
                res1[i] = h;
                result[i] = res1[i];


            }
        }
        System.out.println("***---матрица после умножения на столбец---***");
        System.out.println("******* Рациональные дроби: *******");
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i].toString(ring));
        }
        System.out.println("***********************************");
        return result;
    }

    /**
     *
     * @param w - матрица, все компоненты которой представляют сокращенные
     * дроби, знаменатели которых некотороые полиномы разложимые на простые
     * сомножители
     *
     * @return
     *
     * @throws java.lang.Exception
     */
    public Element[] solveMatrix(Element[] w, Ring ring) throws Exception {
        Element[] result = new Element[w.length];
        for (int i = 0; i < w.length; i++) {
            Polynom det = (Polynom) ((F) w[i]).X[1];//знаменатель
            int[] powers1 = new int[det.coeffs.length]; //новый массив для хранения степеней полиномов
            int numVar = det.powers.length / det.coeffs.length; //переменная отвечающая за количество переменных
            int shift = numVar - 1; //сдвиг
            for (int a = 0; a < det.coeffs.length; a++) {
                powers1[a] = det.powers[(shift)]; //заполнение нового массива степеней
                shift += numVar;
            }
            Polynom polToFact = new Polynom(powers1, det.coeffs); //формируем новый полином из массива новых степеней и массива коэффициентов старого полинома
            for (int fgh = 0; fgh < polToFact.coeffs.length; fgh++) {
                polToFact.coeffs[fgh] = polToFact.coeffs[fgh].toNumber(Ring.Z, ring);
                //из NumberR64 в NumderZ
            }
            System.out.println("Знаменатель " + polToFact);
////////////////////////////////////////////////////////////////////////////////
            FactorPol fact = polToFact.factorOfPol_inC(ring);
            System.out.println("det после развала на простые сомножители... ->"
                    + fact.toString());
            FactorPol detFactMNK = fact.prMNK(ring);
            System.out.println("det после развала на простые сомножители ->"
                    + detFactMNK.toString());
            ////////////////////////////////////////////////////////////////////////
            Polynom ch = ((Polynom) ((F) w[i]).X[0]);

            if (detFactMNK.powers.length != 1) {
                //работа с числителем
                int[] powe = new int[ch.coeffs.length]; //новый массив для хранения степеней полиномов
                int numV = ch.powers.length / ch.coeffs.length; //переменная отвечающая за количество переменных
                int shif = numV - 1; //сдвиг
                for (int a = 0; a < ch.coeffs.length; a++) {
                    powe[a] = ch.powers[(shif)]; //заполнение нового массива степеней
                    shif += numV;
                }
                Polynom polch = new Polynom(powe, ch.coeffs); //формируем новый полином из массива новых степеней и массива коэффициентов старого полинома
                //
                Element[] resDet = new SystemLDE().primeFractionForSingle(polch, detFactMNK, ring); //возвращает вектор решений
                //System.out.println("resDet[] = " + Array.toString(resDet));
                result[i] = new FractionSum(detFactMNK, resDet, ring); //создание объекта типа суммы pol1/pol2 + pol3/pol4...
                // System.out.println("  ----- разложенный det в виде FractionSum   : " +
                //                  ((FractionSum)result[i]).toStrings()); //вывод разложенного на суммы детерминанта
            } else {
                result[i] = new FractionSum(detFactMNK, new Element[] {ch.coeffs[0].toNumber(Ring.C64, ring)}, ring); //создание объекта типа суммы pol1/pol2 + pol3/pol4...
                //System.out.println("  ----- разложенный det в виде FractionSum   : " +
                //                   ((FractionSum)result[i])); //вывод разложенного на суммы детерминанта
            }
        }
        return result;
    }

    /**
     * Сокращение дробей В полученной матрице все объекты типа дробь
     *
     * @param a - Полиномиальная матрица полученная после умножения на столбец
     *
     * @return
     */
    public Element[] cancelToMatrix(Fraction[] a, Ring ring) {
        Element[] result = new Element[a.length];
        for (int k = 0; k < a.length; k++) {
            Element p1 = null;
            Element p2 = null;
            if (a[k].num instanceof Polynom) {
                if (a[k].num instanceof Polynom) {
                    p1 = (Polynom) a[k].num;//числитель
                    for (int i = 0; i < ((Polynom) p1).coeffs.length; i++) {
                        ((Polynom) p1).coeffs[i] = ((Polynom) p1).coeffs[i].toNumber(Ring.Z, ring);
                    }
                } else {
                    p1 = a[k].num;
                }
                if (a[k].denom instanceof Polynom) {
                    p2 = (Polynom) a[k].denom;//знаменатель
                    for (int i = 0; i < ((Polynom) p2).coeffs.length; i++) {
                        ((Polynom) p2).coeffs[i] = ((Polynom) p2).coeffs[i].toNumber(Ring.Z, ring);
                    }
                } else {
                    p2 = a[k].denom;
                }
                Fraction rat = new Fraction();
                if (a[k].denom.isZero(ring)) {
                    rat = a[k];
                } else {
                    Element Eh = a[k].cancel(ring);
                    if (Eh instanceof Fraction) {
                        rat = (Fraction) Eh;
                    } else {
                        rat = new Fraction(Eh, ring.numberONE);
                    }
                }
                result[k] = new F(F.DIVIDE, new Element[] {rat.num, rat.denom});

            } else {
                //случай когда в числителе стоит объект типа F
                CanonicForms def = new CanonicForms(ring,true); //2015
                if (a[k].num instanceof F && a[k].denom instanceof F) {//NEW BLOCK
                    a[k].num = ((F) a[k].num).expand(ring);
                    a[k].denom = ((F) a[k].denom).expand(ring);
                }
                Element el = def.Universal_Convert_With_Expand((F) a[k].num, true);
                Fraction rat = null;
                Polynom res = null;
                Element Erat;
                if (el instanceof Polynom) {
                    res = (Polynom) el;
                    Erat = new Fraction(res, a[k].denom).cancel(ring);
                } else {
                    Fraction re = (Fraction) el;
                    Erat = new Fraction(re.num, re.denom.multiply(a[k].denom, ring)).cancel(ring);
                }
                if (Erat instanceof Fraction) {
                    rat = (Fraction) Erat;
                } else {
                    rat = new Fraction(Erat, ring.numberONE);
                }
                Element ff = def.Univers_unconvert(rat.num);
                result[k] = new F(F.DIVIDE, new Element[] {ff, rat.denom});
            }
        }
        return result;
    }

    /**
     * преобразует полином от многих переменных в полином от одной переменной с
     * коэффициентами над NumberZ
     *
     * @param p
     */
    public static Polynom toPolynomOneVar(Element p, Ring ring) {
        if (p instanceof Polynom) {
            if (((Polynom) p).powers.length != 0) {
                int[] powers1 = new int[((Polynom) p).coeffs.length]; //новый массив для хранения степеней полиномов
                int numVar = ((Polynom) p).powers.length / ((Polynom) p).coeffs.length; //переменная отвечающая за количество переменных
                int shift = numVar - 1; //сдвиг
                for (int a = 0; a < ((Polynom) p).coeffs.length; a++) {
                    powers1[a] = ((Polynom) p).powers[(shift)]; //заполнение нового массива степеней
                    shift += numVar;
                }
                Polynom polToFact = new Polynom(powers1, ((Polynom) p).coeffs); //формируем новый полином из массива новых степеней и массива коэффициентов старого полинома
                for (int fgh = 0; fgh < polToFact.coeffs.length; fgh++) {
                    polToFact.coeffs[fgh] = polToFact.coeffs[fgh].toNumber(Ring.Z, ring);
                    //из NumberR64 в NumderZ
                }
                // System.out.println("Определитель полиномиальной матрицы: " + polToFact);
                return polToFact;
            } else {
                return (Polynom) p;
            }
        } else {
            return Polynom.polynomFromNumber(p, ring);
        }
    }

    public boolean findUnitStep(F f) {
        boolean chek = false;
        for (int i = 0; i < f.X.length; i++) {
            if (f.X[i] instanceof F) {
                F f1 = (F) f.X[i];
                switch (f1.name) {
                    case UNITSTEP: {
                        chek = true;
                        break;
                    }
                    case ADD:
                    case SUBTRACT:
                    case DIVIDE:
                    case MULTIPLY:
                        return chek = findUnitStep(f1);
                }

            }
        }

        return chek;
    }

    /**
     *
     * @return
     */
    public boolean findUnitStepSystemLDE(F syst_dif) {
        boolean chek = false;
        for (int i = 0; i < syst_dif.X.length; i++) {
            if (i % 2 != 0) {
                if (syst_dif.X[i] instanceof F) {
                    F f = ((F) syst_dif.X[i]);
                    chek = findUnitStep(f);
                    if (chek == true) {
                        break;
                    }
                }
            }
        }
        return chek;
    }

    /**
     * Разделяет на экспоненциальную и дробную части
     *
     * @param f
     *
     * @return
     */
    public Element[] expToSimplify(Element f, Ring ring) {

        Element[] result = new Element[2];
        if (f instanceof F) {
            if (((F) f).name == F.DIVIDE) {
                if (((F) f).X[0] instanceof F) {
                    F func = ((F) ((F) f).X[0]);
                    switch (func.name) {
                        case F.EXP: {
                            result[0] = func;
                            result[1] = new F(F.DIVIDE, new Element[] {ring.numberONE, ((F) f).X[1]});
                            break;
                        }
                        case F.MULTIPLY: {
                            result[0] = func.X[0];
                            result[1] = new F(F.DIVIDE, new Element[] {func.X[1], ((F) f).X[1]});
                            break;
                        }
                        default: {
                            result[0] = ring.numberONE;
                            result[1] = f;
                        }
                    }
                } else {
                    result[0] = ring.numberONE;
                    result[1] = f;
                }

            }
        } else {
            result[0] = ring.numberONE;;
            result[1] = new F(F.DIVIDE, new Element[] {f, ring.numberONE});
        }
        return result;
    }

    /**
     * Возвращает результат произведения матрицы на вектор-матрицу
     *
     * @param m - матрица
     * @param v - вектор
     * @param dataExp - данный по экспонентам
     * @param ring - входное полиномиальное кольцо
     *
     * @return
     */
    public Element[][] multiplyMToM(Element[][] m, Element[][] v, Element[][] dataExp, Ring ring) {
        Element[][] result = new Element[m.length][];//результат произведения
        for (int i = 0; i < m.length; i++) {
            result[i] = new Element[m[i].length * v[i].length];
            dataExp[i] = new Element[m[i].length * v[i].length];
            int n = 0;
            for (int j = 0; j < m[i].length; j++) {
                for (int k = 0; k < v[i].length; k++) {
                    if ((m[i][j] instanceof F) && ((v[i][k] instanceof F))) {
                        F f1 = ((F) m[i][j]);
                        F f22 = ((F) v[i][k]);
                        Element[] ell = expToSimplify(f22, ring);
                        dataExp[i][n] = ell[0];
                        F f2 = (F) ell[1];
                        Element el1 = f1.X[0];
                        Element el2 = f1.X[1];
                        Element el3 = f2.X[0];
                        Element el4 = f2.X[1];
                        if ((f1.name == F.DIVIDE) && (f2.name == F.DIVIDE)) {
                            if (f1.X[0] instanceof F) {
                                F f11 = ((F) f1.X[0]);
                                el1 = f1.X[0];
                                if (f11.name == F.intPOW) {
                                    Polynom p = (Polynom) f11.X[0].pow(f11.X[1].intValue(), ring);
                                    el1 = p;
                                }
                            }
                            if (f1.X[1] instanceof F) {
                                F f11 = ((F) f1.X[1]);
                                el2 = f1.X[1];
                                if (f11.name == F.intPOW) {
                                    Polynom p = (Polynom) f11.X[0].pow(f11.X[1].intValue(), ring);
                                    el2 = p;
                                }
                            }
                            if (f2.X[0] instanceof F) {
                                F f11 = ((F) f2.X[0]);
                                el3 = f2.X[0];
                                if (f11.name == F.intPOW) {
                                    Polynom p = (Polynom) f11.X[0].pow(f11.X[1].intValue(), ring);
                                    el3 = p;
                                }
                            }
                            if (f2.X[1] instanceof F) {
                                F f11 = ((F) f2.X[1]);
                                el4 = f2.X[1];
                                if (f11.name == F.intPOW) {
                                    Polynom p = (Polynom) f11.X[0].pow(f11.X[1].intValue(), ring);
                                    el4 = p;
                                }
                            }
                            Element ch = el1.multiply(el3, ring);
                            Element zn = el2.multiply(el4, ring);
                            result[i][n] = new F(F.DIVIDE, new Element[] {ch, zn});
                            n++;
                        }
                    } else {
                        F f1 = ((F) m[i][j]);
                        Element f22 = v[i][k];
                        Element[] ell = expToSimplify(f22, ring);
                        dataExp[i][n] = ell[0];
                        F f2 = (F) ell[1];
                        Element el1 = f1.X[0];
                        Element el2 = f1.X[1];
                        Element el3 = f2.X[0];
                        Element el4 = f2.X[1];
                        if ((f1.name == F.DIVIDE) && (f2.name == F.DIVIDE)) {
                            if (f1.X[0] instanceof F) {
                                F f11 = ((F) f1.X[0]);
                                el1 = f1.X[0];
                                if (f11.name == F.intPOW) {
                                    Polynom p = (Polynom) f11.X[0].pow(f11.X[1].intValue(), ring);
                                    el1 = p;
                                }
                            }
                            if (f1.X[1] instanceof F) {
                                F f11 = ((F) f1.X[1]);
                                el2 = f1.X[1];
                                if (f11.name == F.intPOW) {
                                    Polynom p = (Polynom) f11.X[0].pow(f11.X[1].intValue(), ring);
                                    el2 = p;
                                }
                            }
                            if (f2.X[0] instanceof F) {
                                F f11 = ((F) f2.X[0]);
                                el3 = f2.X[0];
                                if (f11.name == F.intPOW) {
                                    Polynom p = (Polynom) f11.X[0].pow(f11.X[1].intValue(), ring);
                                    el3 = p;
                                }
                            }
                            if (f2.X[1] instanceof F) {
                                F f11 = ((F) f2.X[1]);
                                el4 = f2.X[1];
                                if (f11.name == F.intPOW) {
                                    Polynom p = (Polynom) f11.X[0].pow(f11.X[1].intValue(), ring);
                                    el4 = p;
                                }
                            }
                            Element ch = el1.multiply(el3, ring);
                            Element zn = el2.multiply(el4, ring);
                            result[i][n] = new F(F.DIVIDE, new Element[] {ch, zn});
                            n++;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Возвращает произведение обратной полиномиальной матрицы на 1/det(AdjM)
     *
     * @param m - обратная полиномиальная матрица
     * @param det - определитель
     */
    public Element[][] multiplyDetMatrixToPolMatrix(Element[][] m, Element det) {
        Element[][] result = new Element[m.length][];
        for (int i = 0; i < m.length; i++) {
            result[i] = new Element[m[i].length];
            for (int j = 0; j < m[i].length; j++) {
                result[i][j] = new F(F.DIVIDE, new Element[] {m[i][j], det});
            }
        }
        return result;
    }

    /**
     * Возвращает сумму полиномов начальных условий и правых частей
     *
     * @param pol
     * @param arr
     *
     * @return
     */
    public Element[][] addInitPolAndArrayLP(Element[] pol, Element[][] arr) {
        Element[][] result = new Element[arr.length][];
        for (int i = 0; i < arr.length; i++) {
            result[i] = new Element[arr[i].length + 1];
            System.arraycopy(arr[i], 0, result[i], 0, arr[i].length);
            result[i][result[i].length - 1] = pol[i];
        }
        return result;
    }

    /**
     * Возвращает для обратного преобразования Лапласа дроби из числителей и
     * знаменателей
     *
     * @param zn
     * @param ch
     * @param ring
     *
     * @return
     */
    public Element[] vectorInverseLP(FactorPol zn, Element[] ch, Element el, Ring ring) {
        Vector<Element> res = new Vector<Element>();
        //если первый полином в FactorPol число
        if ((zn.multin[0].isOne(ring)) || (zn.multin[0].isMinusOne(ring)) || ((zn.multin[0].coeffs.length == 1) && (zn.multin[0].powers.length == 0))) {
            int[] pow = new int[zn.powers.length - 1];
            Polynom[] mul = new Polynom[zn.multin.length - 1];
            for (int i = 1; i < zn.multin.length; i++) {
                pow[i - 1] = zn.powers[i];
                mul[i - 1] = zn.multin[i];
            }
            zn = new FactorPol(pow, mul);
        }
        int k = 0;
        for (int i = 0; i < zn.multin.length; i++) {
            for (int j = 0; j < zn.powers[i]; j++) {
                if (!ch[k].isZero(ring)) {
                    F f = null;
                    if (el.isOne(ring)) {
                        if ((j + 1) == 1) {
                            f = new F(F.DIVIDE, new Element[] {ch[k], zn.multin[i]});
                        } else {
                            f = new F(F.DIVIDE, new Element[] {ch[k], new F(F.intPOW, new Element[] {zn.multin[i], new NumberR64(j + 1)})});
                        }
                        res.add(f);
                    } else {
                        if ((j + 1) == 1) {
                            F ff = new F(F.MULTIPLY, new Element[] {el, ch[k]});
                            f = new F(F.DIVIDE, new Element[] {ff, zn.multin[i]});
                        } else {
                            F ff = new F(F.MULTIPLY, new Element[] {el, ch[k]});
                            f = new F(F.DIVIDE, new Element[] {el, new F(F.intPOW, new Element[] {zn.multin[i], new NumberR64(j + 1)})});
                        }
                        res.add(f);
                    }
                }
            }
            k++;
        }
        Element[] result = new Element[res.size()];
        res.toArray(result);
        return result;
    }

    /**
     * Упрощает массивы дробей Складывает дроби с одинаковыми знаменателями
     *
     * @param a
     *
     * @return
     */
    public Element[][] simplifyVectorInverseLP(Element[][] a, Ring ring) {
        Element[][] result = new Element[a.length][];
        for (int i = 0; i < a.length; i++) {
            Vector<Element> res = new Vector<Element>();
            for (int j = 0; j < a[i].length; j++) {
                Element b1 = ((F) a[i][j]).X[1];
                Element a1 = ((F) a[i][j]).X[0];
                Element sum = a1;//0
                for (int k = 0; k < a[i].length; k++) {
                    if ((j != k) && (a[i][j] != null) && (a[i][k] != null)) {
                        Element b2 = ((F) a[i][k]).X[1];
                        if (b1.equals(b2, ring)) {
                            Element a2 = ((F) a[i][k]).X[0];
                            sum = sum.add(a2, ring).expand(ring);
                            a[i][k] = null;
                        }
                    }
                }
                F f = new F(F.DIVIDE, new Element[] {sum, b1});
                res.add(f);
                a[i][j] = null;
            }
            result[i] = res.toArray(new Element[res.size()]);
        }
        return result;
    }

    /**
     * Обратное преобразование Лапласа для случая функций - unitStep
     *
     * @param m
     *
     * @return
     */
    public Element[] vectorInverseLaplaceTransformUS(Element[][] m, Ring ring) {
        Element[] result = new Element[m.length];
        for (int i = 0; i < m.length; i++) {
            Vector<Element> res = new Vector<Element>();
            for (int j = 0; j < m[i].length; j++) {
                Element f = new InverseLaplaceTransform().inverseLaplaceTransform(m[i][j], ring);
                res.add(f);
            }
            Element[] arr = res.toArray(new Element[res.size()]);
            result[i] = new F(F.ADD, arr);
        }
        return result;
    }

    /**
     * Решение систем линейных дифференциальных уравнений с постоянными
     * коэффициентами В правой части функции unitStep
     *
     * @param syst_dif - система дифференциальных уравнений
     * @param syst_inC - система начальных условий
     * @param ring - входное кольцо
     *
     * @return - решение системы в виде вектора функций
     *
     * @throws Exception
     */
    public VectorS solveSystemLDEUnitStep(F syst_dif, F syst_inC, Ring ring) throws Exception {
        int numVars = new SystemLDE().numVarsOfSystLDE(syst_dif);//количество переменных
        //определяем переменные системы
        Vector v = vecFnameVar(syst_dif, ring);
        Element[][] polMatrix = new SystemLDE().systDifEquationToMatrix(syst_dif, numVars, v, ring);//полиномиальная матрица
        System.out.println("Полиномиальная матрица: " + Array.toString(polMatrix, new Ring("R64[p]",ring)));
        MatrixS polMatrixS = new MatrixS(polMatrix, ring);
        MatrixS adjointPolMatrixS = polMatrixS.adjoint(ring);//обратная матрица
        Element[][] arrAdjointPolMatrixS = adjointPolMatrixS.M;//обратная матрица в виде массива
        System.out.println("Обратная матрица: " + adjointPolMatrixS.toString(new Ring("R64[p]",ring)));
        Element[][] df = new SystemLDE().arrayInitCond(numVars, syst_inC, v, ring);//массив начальных значений
        System.out.println("Начальные  значения: " + Array.toString(df, new Ring("R64[p]",ring)));
        Element[] ppp = new SystemLDE().initCondLaplaceTransform(polMatrix, df, ring);//массив полиномов начальных значений
        System.out.println("Полиномы начальных условий: " + Array.toString(ppp, new Ring("R64[p]",ring)));
        Element[][] arrayLP = new SystemLDE().vectorLPT(syst_dif, ring);
        System.out.println("Правые части преобразованные по прямому преобразованию Лапласа: " + Array.toString(arrayLP, new Ring("R64[p]",ring)));
        Element[][] addICArrayLP = addInitPolAndArrayLP(ppp, arrayLP);
        System.out.println("Правые части преобразованные по прямому преобразованию Лапласа + полиномы начальных условий: " + Array.toString(addICArrayLP, new Ring("R64[p]",ring)));
        Polynom det = ((Polynom) polMatrixS.det(ring));
        System.out.println("Определитель полиномиальной матрицы: " + det.toString(new Ring("R64[p]",ring)));
/////////////Произведение AdjMatrix на det//////////////////////////////////////
        Element[][] mulDetToMatrix = multiplyDetMatrixToPolMatrix(arrAdjointPolMatrixS, det);
        System.out.println("Произведение AdjMatrix на det: " + Array.toString(mulDetToMatrix, new Ring("R64[p]",ring)));
///////////////////////////Произведение 2 объектов типа [][]////////////////////
        Element[][] dataExp = new Element[mulDetToMatrix.length][];
        Element[][] resultToMultiply = multiplyMToM(mulDetToMatrix, addICArrayLP, dataExp, ring);
        System.out.println("Произведение M_ON_M: " + Array.toString(resultToMultiply, new Ring("R64[p]",ring)));
        Element[][] solveINVLP = new Element[resultToMultiply.length][];
        for (int i = 0; i < resultToMultiply.length; i++) {
            Vector<Element> res = new Vector<Element>();
            for (int j = 0; j < resultToMultiply[i].length; j++) {
                F func = (F) resultToMultiply[i][j];
                Polynom p1 = (Polynom) func.X[0];
                Polynom p2 = (Polynom) func.X[1];
                FactorPol fact = SystemLDE.toPolynomOneVar(p2, ring).factorOfPol_inC(ring);
                fact.normalFormInField(ring);
                //  System.out.println(j + " = " + fact.toString(ring));
                Element[] coeffs = new SystemLDE().primeFractionForSingle(p1, fact, ring);
                //  System.out.println("coeffs = " + Array.toString(coeffs, ring));
                Element[] vec = vectorInverseLP(fact, coeffs, dataExp[i][j], ring);
                //  System.out.println("vecINVLP = " + Array.toString(vec, ring));
                for (int z = 0; z < vec.length; z++) {
                    res.add(vec[z]);
                }
            }
            solveINVLP[i] = res.toArray(new Element[res.size()]);
        }
        System.out.println("Дроби до преобразования Лапласа: " + Array.toString(solveINVLP, new Ring("R64[p]",ring)));
        //////////////////////////////Упрощение дробей//////////////////////////
        //Element[][] solveRes = simplifyVectorInverseLP(solveINVLP, ring);
        // System.out.println("Дроби после упрощения: " + Array.toString(solveRes, new Ring("R64[p]",ring)));
        ///////////////////////////Обратное преобразование Лапласа///////////////////////////////////
        Element[] solveSystLDE = vectorInverseLaplaceTransformUS(solveINVLP, ring);//решение системы дифференциальных уравнений
        System.out.println("После обратного преобразования Лапласа: " + Array.toString(solveSystLDE, new Ring("R64[p]",ring)));
        return new VectorS(solveSystLDE);
    }

    
    /**
     * Главный метод решения систем линейных дифференциальных уравнений с
     * постоянными коэффициентами С возможностью пошаговой трассировки
     * результатов вычисления
     *
     * @param syst_dif - система дифференциальных уравнений
     * @param syst_inC - система начальных условий
     * @param ring - входное кольцо
     *
     * @return - решение системы в виде вектора функций
     *
     * @throws Exception
     */
    public Element solveSystemLDE(F syst_dif, F syst_inC, Ring ring) throws Exception {
        VectorS solve = new VectorS();
        if (findUnitStepSystemLDE(syst_dif)) {
            solve = solveSystemLDEUnitStep(syst_dif, syst_inC, ring);
        } else {
            int numVars = new SystemLDE().numVarsOfSystLDE(syst_dif);//количество переменных
            //определяем переменные системы
            Vector v1 = vecFnameVar(syst_dif, ring);
            Element[] arrName = new Element[v1.size()];
            v1.toArray(arrName);
            Element[] sortName = Array.sortUp(arrName, ring);
            Vector v = new Vector();
            for (int s1 = 0; s1 < sortName.length; s1++) {
                v.add(sortName[s1]);
            }
            Element[][] fff = new SystemLDE().systDifEquationToMatrix(syst_dif, numVars, v, ring);//полиномиальная матрица
            System.out.println("Полиномиальная матрица: " + Array.toString(fff, new Ring("R64[p]",ring)));
            MatrixS ss = new MatrixS(fff, ring);
            if (ring.STEPBYSTEP == NumberZ64.ONE) {
                Fname step1 = new Fname("Шаг1.Полиномиальная$ $матрица$ $A: ");
                ring.addTraceStep(step1);
                ring.addTraceStep(ss);
            }

            MatrixS sss = ss.adjoint(ring);//обратная матрица
            if (ring.STEPBYSTEP == NumberZ64.ONE) {
                Fname step2 = new Fname("Шаг2.Присоединенная$ $матрица$ $A^{-1}: ");
                ring.addTraceStep(step2);
                ring.addTraceStep(sss);
            }
            System.out.println("Обратная матрица: " + sss.toString(new Ring("R64[p]",ring)));

            Element[][] df = new SystemLDE().arrayInitCond(numVars, syst_inC, v, ring);//массив начальных значений
            if (ring.STEPBYSTEP == NumberZ64.ONE) {
                Fname step3 = new Fname("Шаг3.Начальные$ $значения: ");
                ring.addTraceStep(step3);
                ring.addTraceStep(new MatrixS(df, ring));
            }
            System.out.println("Начальные  значения: " + Array.toString(df, new Ring("R64[p]",ring)));

            Element[] ppp = new SystemLDE().initCondLaplaceTransform(fff, df, ring);//массив полиномов начальных значений
            if (ring.STEPBYSTEP == NumberZ64.ONE) {
                Fname step4 = new Fname("Шаг4.Полиномы$ $начальных$ $условий$ $N: ");
                ring.addTraceStep(step4);
                ring.addTraceStep(new VectorS(ppp));
            }
            System.out.println("Полиномы начальных условий: " + Array.toString(ppp, new Ring("R64[p]",ring)));

            Element[] arrLP = new SystemLDE().vectorLaplaceTransform(syst_dif, ring);
            if (ring.STEPBYSTEP == NumberZ64.ONE) {
                Fname step5 = new Fname("Шаг5.Правые$ $части$ $преобразованные$ $по$ $прямому$ $преобразованию$ $Лапласа$ $B: ");
                ring.addTraceStep(step5);
                ring.addTraceStep(new VectorS(arrLP));
            }
            System.out.println("Правые части преобразованные по прямому преобразованию Лапласа: " + Array.toString(arrLP, new Ring("R64[p]",ring)));

            Element[] arrLP1 = new SystemLDE().addElementsToLaplace(arrLP, ring);
            System.out.println("----------------- " + Array.toString(arrLP1, new Ring("R64[p]",ring)));
            Element[] arrRightPath = new SystemLDE().rightPatSystLDELaplaceTransform(arrLP, ppp, ring);//arrLP1
            if (ring.STEPBYSTEP == NumberZ64.ONE) {
                Fname step6 = new Fname("Шаг6.Правые$ $части$ $+$ $полиномы$ $начальных$ $условий$ $H=B+N: ");
                ring.addTraceStep(step6);
                ring.addTraceStep(new VectorS(arrRightPath));
            }
            System.out.println("Правые части преобразованные по прямому преобразованию Лапласа + полиномы начальных условий " + Array.toString(arrRightPath, new Ring("R64[p]",ring)));

            Polynom det = ((Polynom) ss.det(ring));
            System.out.println("Определитель полиномиальной матрицы: " + det.toString(new Ring("R64[p]",ring)));
            if (ring.STEPBYSTEP == NumberZ64.ONE) {
                Fname step7 = new Fname("Шаг7.Определитель$ $матрицы$ $A$ $det: ");
                ring.addTraceStep(step7);
                ring.addTraceStep(det);
            }

////////////////////////////////////////////////////////////////////////////////
            FactorPol fact = SystemLDE.toPolynomOneVar(det, ring).factorOfPol_inC(ring);//0.00000000000000001  точность
            System.out.println("det после развала на простые сомножители... ->"
                    + fact.toString(new Ring("R64[p]",ring)));
            FactorPol detFactMNK = fact;
            detFactMNK = simplifyComplex(fact, ring);//NEW!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // FactorPol detFactMNK = fact.prMNK(ring);
            System.out.println("det после развала на простые сомножители ->"
                    + detFactMNK.toString(new Ring("R64[p]",ring)));
            if (ring.STEPBYSTEP == NumberZ64.ONE) {
                Fname step8 = new Fname("Шаг8.Определитель$ $det$ $после$ $факторизации: ");
                ring.addTraceStep(step8);
                ring.addTraceStep(detFactMNK);
            }
            ////////////////////////////////////////////////////////////////////////

            Polynom ch = new Polynom("1", ring);
            Element[] resDet = new SystemLDE().testPrimeFractionForSingle(ch, detFactMNK, ring); //primeFractionForSingle NEW!!!//возвращает вектор решений
            if (ring.STEPBYSTEP == NumberZ64.ONE) {
                Fname step9 = new Fname("Шаг9.Коэффициенты$ $дробей$ $1/det: ");
                ring.addTraceStep(step9);
                ring.addTraceStep(new VectorS(resDet));
            }
            System.out.println("resDet[] = " + Array.toString(resDet, new Ring("R64[p]",ring)));
            ////////////////////////////////////////////////////////////////////////
            //Нормализация FactorPol
            detFactMNK.normalFormInField(ring);
            if (detFactMNK.multin[0].isItNumber()) { //если первый полином в FactorPol - есть число ...detFactMNK.multin[0].coeffs.length == 1
                int[] pow = new int[detFactMNK.powers.length - 1];
                Polynom[] mul = new Polynom[detFactMNK.multin.length - 1];
                for (int ij = 1; ij < detFactMNK.multin.length; ij++) {
                    pow[ij - 1] = detFactMNK.powers[ij];
                    mul[ij - 1] = detFactMNK.multin[ij];
                }
                detFactMNK = new FactorPol(pow, mul);
            }
            ////////////////////////////////////////////////////////////////////////
            FractionSum fDet = new FractionSum(detFactMNK, resDet, ring); //создание объекта типа суммы pol1/pol2 + pol3/pol4...
//            System.out.println("  ----- разложенный det в виде FractionSum  : "
//                    + fDet.toStrings(new Ring("R64[p]",ring))); //вывод разложенного на суммы детерминанта
            if (ring.STEPBYSTEP == NumberZ64.ONE) {
                Fname step10 = new Fname("Шаг10.Полученная$ $сумма$ $дробей: ");
                ring.addTraceStep(step10);
                ring.addTraceStep(fDet);
            }
            ////////////////////////////////////////////////////////////////////////
            Element[] result = new SystemLDE().multiplyPolMatrix(sss.M, sss.col, arrRightPath, new Ring("R64[p]",ring)); //Возвращает произведение 2-ух матриц-решения и преобразованных по прямому преобразованию правых частей
            if (ring.STEPBYSTEP == NumberZ64.ONE) {
                Fname step11 = new Fname("Шаг11.V=A^{-1}*H: ");
                ring.addTraceStep(step11);
                ring.addTraceStep(new VectorS(result));
            }
            ////////////////////////////////////////////////////////////////////////
            Element[] solveFr = new SystemLDE().multiplyVector_and_Det(result, det, new Ring("R64[p]",ring));//Произведение 1/det * W
            if (ring.STEPBYSTEP == NumberZ64.ONE) {
                Fname step12 = new Fname("Шаг12.W=V*1/det: ");
                ring.addTraceStep(step12);
                ring.addTraceStep(new VectorS(solveFr));
            }
            //преобразование дробей по обратному преобразованию Лапласа
            Element[] solveSystLDE = new SystemLDE().vectorInverseLaplaceTransform((FractionSum[]) solveFr, v, ring);
            if (ring.STEPBYSTEP == NumberZ64.ONE) {
                Fname step13 = new Fname("Шаг13.Обратное$ $преобразование$ $Лапласа: ");
                ring.addTraceStep(step13);
                ring.addTraceStep(new VectorS(solveSystLDE));
            }
            solve = new VectorS(solveSystLDE);
        }
        return solve;
    }

    /**
     * Определение количества переменных в системе LDE
     *
     * @param syst_dif
     *
     * @return
     */
    public int numVarsOfSystLDE(F syst_dif) {
        int n = 0;//счетчик
        Element buf;
        for (int i = 0; i < syst_dif.X.length; i += 2) {
            n++;
        }
        return n;
    }

    /**
     * Проверка массива правых частей преобразованных по Лапласу Преобразование
     * композиций дробей
     *
     * @param a
     *
     * @return
     */
    public Element[] addElementsToLaplace(Element[] a, Ring ring) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] instanceof F) {
                if ((((F) a[i]).name == F.ADD) || (((F) a[i]).name == F.SUBTRACT)) {
                    a[i] = addF_intoRational(((F) a[i]), ring);
                }
            }
        }
        return a;
    }

    /**
     * метод для умножения SumOfProduct * Polynom
     *
     * @param a
     * @param b
     * @param ring
     *
     * @return
     *
     * @throws Exception
     */
    public Element[] multiplyVector_and_Det(Element[] a, Polynom b, Ring ring) throws Exception {
        Fraction[] c = new Fraction[a.length];
        Polynom[] pol = new Polynom[c.length];
        FractionSum[] frS = new FractionSum[pol.length];
        Element ch = null;
        FactorPol detFactMNK = null;
        for (int i = 0; i < a.length; i++) {
            if (a[i] instanceof F) {
                c[i] = new Fraction(((F) a[i]).X[0], ((F) a[i]).X[1]);
            }
            if (c[i].denom instanceof Polynom) {
                pol[i] = ((Polynom) c[i].denom).multiply(b, ring);//произведение det и знаменателя
            } else {
                pol[i] = (Polynom) (c[i].denom).multiply(b, ring);//произведение det и знаменателя
            }
            System.out.println("pol === " + pol[i].toString(ring));
            FactorPol fact = SystemLDE.toPolynomOneVar(pol[i], ring).factorOfPol_inC(ring);
            //  System.out.println("det после развала на простые сомножители... ->"
            //          + fact.toString(new Ring("R64[p]",ring)));
            FactorPol fppp = simplifyComplex(fact, ring);//NEW!!!!!!!!!!!!!!!!!!!!!!!!!!
            detFactMNK = fppp;//factfppp!!!!!!!!!!!!! NEW
            ch = SystemLDE.toPolynomOneVar(((F) a[i]).X[0], ring);//преобразование числителя в полином от одной переменной
            if (ch instanceof Polynom) {
                if (((Polynom) ch).powers.length == 0) {
                    if (((Polynom) ch).coeffs.length == 1) {
                        if (((Polynom) ch).coeffs[0] instanceof F) {
                            ch = ((Polynom) ch).coeffs[0];
                        }
                    }
                }
            }
            Element[] arr = normCoeffFactor(detFactMNK, ring);
            detFactMNK = (FactorPol) arr[1];

            Element[] resDet = testPrimeFractionForSingle(ch, detFactMNK, ring);//возвращает вектор решений
            //  System.out.println("resDet[] = " + Array.toString(resDet, new Ring("R64[p]",ring)));
            ////////////////////////////////////////////////////////////////////////
//            if (detFactMNK.multin[0].isItNumber()) { //если первый полином в FactorPol - есть число ...//detFactMNK.multin[0].coeffs.length == 1
//                int[] pow = new int[detFactMNK.powers.length - 1];
//                Polynom[] mul = new Polynom[detFactMNK.multin.length - 1];
//                for (int ij = 1; ij < detFactMNK.multin.length; ij++) {
//                    pow[ij - 1] = detFactMNK.powers[ij];
//                    mul[ij - 1] = detFactMNK.multin[ij];
//                }
//                detFactMNK = new FactorPol(pow, mul);
//            }
            ////////////////////////////////////////////////////////////////////////
            frS[i] = new FractionSum(detFactMNK, resDet, ring); //создание объекта типа суммы pol1/pol2 + pol3/pol4...
            //  System.out.println("FractionSum[" + i + "]  : "
            //         + frS[i].toStrings(ring)); //вывод разложенного на суммы детерминанта
        }
        return frS;
    }

    /**
     * Возвращает решение системы в виде вектора функций
     *
     * @param a - сумма дробей
     * @param v - вектор имен неизвестных функций
     * @param ring
     *
     * @return
     */
    public Element[] vectorInverseLaplaceTransform(FractionSum[] a, Vector v, Ring ring) {
        Element[] solveSyst = new Element[a.length];
        System.out.println("Решение системы:");
        for (int l = 0; l < a.length; l++) {
            ArrayList<Element> res = new ArrayList<Element>();
            int i = 0;
            for (int j = 0; j < a[l].arg.multin.length; j++) {
                int k = 0;
                for (int h = 0; h < a[l].arg.powers[j]; h++) {
                    if (!a[l].coeff[i].isZero(ring)) {
                        res.add(new F(F.DIVIDE, new Element[] {a[l].coeff[i], new F(F.intPOW, new Element[] {a[l].arg.multin[j], new NumberR64(k + 1)})}));
                    }
                    k++;
                    i++;
                }
            }
            Element[] res1 = new Element[res.size()];
            res.toArray(res1);
            F[] fArray = new F[res1.length];
            for (int k = 0; k < res1.length; k++) {
                try {
                    System.out.println("ILP Func[" + k + "]= " + res1[k].toString(ring));
                    fArray[k] = new InverseLaplaceTransform().inverseLaplaceTransform(res1[k], ring);
                } catch (Exception ex) {
                    ring.exception.append(
                            "Exeption in systDifEquationToMatrix in package LaplaceTransform: " + ex);
                }
            }
            System.out.println(((Fname) v.get(l)).name + "(t)  = " + new F(F.ADD, fArray).toString(ring));
            solveSyst[l] = new F(F.ADD, fArray);
        }
        return solveSyst;
    }

    /**
     * возвращает вектор входных переменных системы LDE
     *
     * @param right_f - левая часть системы дифференциальных уравнений
     * @param ring - входное кольцо
     *
     * @return
     */
    public Vector vecFnameVar(Element right_f, Ring ring) {
        Vector<F>[] vectF = new Vector[F.FUNC_ARR_LEN];
        Vector<Element>[] vectEl = new Vector[Ring.NumberOfElementTypes];
        for (int i1 = 0; i1 < Ring.NumberOfElementTypes; i1++) {
            vectEl[i1] = new Vector<Element>();
        }
        ring.CForm.vectEl=vectEl; ring.CForm.vectF=vectF;
        for (int s1 = 0; s1 < ((F) right_f).X.length; s1++) {
            ((F) right_f).X[s1] = F.cleanOfRepeating(((F) right_f).X[s1],  ring);
        }
        return vectEl[Ring.Fname];
    }

    /////////////////Методы для настройки точности вычислений///////////////////
    /**
     *
     * @param ch
     * @param zn
     * @param ring
     *
     * @return
     */
    public static Element[] primeFractionForSingleR(Polynom ch, FactorPol zn, Ring ring) {
        Element delet = new Complex(1.0, 0.0); //присваеваем на первом этапе 1 коэффициент
        //&& (zn.multin[0].powers[0] == 0)
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
        //определяем количество столбцов в матрице А
        int k_col = 0;
        for (int j = 0; j < zn.multin.length; j++) {
            k_col = k_col + zn.multin[j].powers[0] * zn.powers[j];
        }
        //определяем количество строк в матрице А
        Polynom p = new Polynom("1", ring);
        for (int h = 0; h < zn.multin.length; h++) {
            for (int q = 0; q < zn.powers[h]; q++) {
                p = p.multiply(zn.multin[h], ring);
            }
        }
        int k_row = p.powers[0];
        Element[] b = null;
        Element[][] a = null;
        if (ch.powers.length != 0) {
            if (k_row > ch.powers[0]) {
                b = new Element[k_row + 1];
                a = new Element[k_row + 1][k_col + 1];
            } else {
                b = new Element[ch.powers[0] + 1];
                a = new Element[ch.powers[0] + 1][k_col + 1];
            }
        } else {
            b = new Element[k_row];//k_row + 1
            a = new Element[k_row][k_col + 1];//[k_row + 1][k_col + 1]
        }
        //заполняем столбец 0
        for (int w = 0; w < b.length; w++) {
            b[w] = NumberR.ZERO;
        }
        //заполняем матрицу 0
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] = NumberR.ZERO;
            }
        }
        //заполняем столбец b коэффициентами из числителя
        if (ch.powers.length != 0) {
            for (int l = 0; l < ch.powers.length; l++) {
                b[b.length - ch.powers[l] - 1] = ch.coeffs[l];
            }
        } else {
            b[b.length - 1] = ch.coeffs[0];
        }

        ArrayList<FactorPol> af = new ArrayList<FactorPol>();//все знаменатели
        IntList ap = new IntList();//все числители
        for (int i = 0; i < zn.multin.length; i++) {
            for (int j = 0; j < zn.powers[i]; j++) {
                af.add(new FactorPol(new int[] {j + 1}, new Polynom[] {zn.multin[i]}));
                ap.add(zn.multin[i].powers[0]);
            }
        }

        Polynom polnew[] = new Polynom[af.size()]; //создаём массив полиномов
        Polynom pr = zn.multin[0].myOne(ring); //вспомогательный полином равный 1
        for (int j = 0; j < zn.powers.length; j++) { //вычисляем произведение полиномов (знаменатель)
            if (zn.powers[j] == 1) {
                pr = pr.multiply(zn.multin[j], ring);
            }
            if (zn.powers[j] != 1) {
                for (int i = 0; i < zn.powers[j]; i++) {
                    pr = pr.multiply(zn.multin[j], ring);
                }
            }
        }
        int ds1 = 0; //вспомогательная переменная
        int ds2 = 0; //вспомогательная переменная
        for (int a1 = 0; a1 < zn.powers.length; a1++) { //вычисляем добавки к дробям
            Polynom del = pr;
            for (int m = 0; m < zn.powers[a1]; m++) {
                del = del.divideExact(zn.multin[a1], ring);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                polnew[m + ds1] = del;
                ds2 = ds2 + 1;
            }
            ds1 = ds2;
        }

        int m = 0;//счетчик по столбцам
        int g = 0;
        while (m < k_col) {
            for (int j = 0; j < ap.arr[g]; j++) {
                if (polnew[g].powers.length != 0) {
                    for (int i = 0; i < polnew[g].powers.length; i++) {
                        a[a.length - polnew[g].powers[i] - 1 - j][m] = polnew[g].coeffs[i];
                    }
                } else {
                    a[a.length - 1 - j][m] = polnew[g].coeffs[0];
                }
                m++;
            }
            g++;
        }
        for (int i = 0; i < a.length; i++) {
            a[i][k_col] = b[i];
        }
        System.out.println("************************");
        System.out.println(Array.toString(b));
        System.out.println("************************");
        System.out.println(Array.toString(a, ring));
        System.out.println("************************");
        //решение системы
        MatrixS M1 = new MatrixS(a, ring); //преобразование в матрицу над полиномами!!!
        System.out.println("====================MatrixS======================");
        System.out.println(M1.toString(ring));
        System.out.println("==================================================");
        System.out.println("=============MatrixS.toEchelonForm================");
        MatrixS M2 = null;
        CanonicForms cfs = null;
        M2 = M1.toEchelonForm(ring); //преобразование матрицы к виду - на главной диагонале стоят определители а в последнем столбце свободные члены
        System.out.println(M2.toString());
        System.out.println("==================================================");
        Element[] sysSolve = new Element[M2.colNumb - 1];
        if (M2.isSysSolvable()) {
            sysSolve = M2.oneSysSolv_and_Det(ring);
        } else {
            System.out.println("System error!!!");
            return sysSolve;
        }
        System.out.println("решение системы");
        System.out.println(Array.toString(sysSolve));

        int v1 = 0;
        int v2 = 0;
        Element det = sysSolve[sysSolve.length - 1];
        Element[] solve = new Element[af.size()];
        while (v1 < af.size()) {
            if (ap.arr[v1] == 2) {
                if (!(sysSolve[v2 + 1] instanceof Complex)) {//!!!!!!!!!!!!!!!!!!!!!!
                    sysSolve[v2 + 1] = new Complex(sysSolve[v2 + 1].doubleValue());
                }
                if (!(sysSolve[v2] instanceof Complex)) {////!!!!!!!!!!!!!!!!!!!!!!!!
                    sysSolve[v2] = new Complex(sysSolve[v2].doubleValue());
                }
                solve[v1] = new Polynom(new int[] {1, 0}, new Element[] {sysSolve[v2 + 1].divide(det, ring), sysSolve[v2].divide(det, ring)});
                v2 += ap.arr[v1];
            } else {
                if (!(sysSolve[v2] instanceof Complex)) {//!!!!!!!!!!!!!!!!!!!!!
                    sysSolve[v2] = new Complex(sysSolve[v2].doubleValue());
                }
                solve[v1] = new Polynom(new int[] {0}, new Element[] {sysSolve[v2].divide(det, ring)});
                v2 += ap.arr[v1];
            }
            v1++;
        }
        //убираем нулевые коэффициенты
        for (int i = 0; i < solve.length; i++) {
            solve[i] = ((Polynom) solve[i].divide(delet, ring)).deleteZeroCoeff(ring);
        }
        System.out.println("РЕШЕНИЕ!!!!!");
        System.out.println(Array.toString(solve));
        System.out.println("ЗНАМЕНАТЕЛИ");
        for (int i = 0; i < af.size(); i++) {
            System.out.println(i + " = " + af.get(i));
        }
        return solve;
    }

    /**
     * Поиск максимума функции на отрезке от [0,T]
     *
     * @param a - отрезок поиска
     * @param f - функция
     * @param ring - входное кольцо
     *
     * @return
     */
    public Element maxFunc_InPoint(Element[] a, Element f, Ring ring) {
        Element buf_max = ring.numberONE;
        if (f instanceof F) {
            if (((F) f).name == F.ADD) {
                F f1 = ((F) f);
                for (int i = 0; i < f1.X.length; i++) {
                    if (f1.X[i] instanceof F) {
                        F f2 = (F) f1.X[i];
                        switch (f2.name) {
                            case F.EXP: {
                                if (f2.X[0] instanceof Polynom) {
                                    Element el = ((Polynom) f2.X[0]).coeffs[0];
                                    if (el.signum() == -1) {
                                        Element val = f2.valueOf(new Element[] {a[0]}, ring);
                                        buf_max = buf_max.multiply(val, ring);
                                    } else {
                                        Element val = f2.valueOf(new Element[] {a[1]}, ring);
                                        buf_max = buf_max.multiply(val, ring);
                                    }
                                }
                                break;
                            }
                            default: {
                                return maxFunc_InPoint(a, f2, ring);
                            }
                        }
                    } else {
                        Element el = maxPolynom(((Polynom) f1.X[i]), a[1], ring);
                        buf_max = buf_max.multiply(el, ring);
                    }
                }
            } else {
            }
        }
        return buf_max;
    }

    /**
     * Возвращает максимум полинома на комплексном круге радиуса r
     *
     * @param p
     * @param r
     * @param ring
     *
     * @return
     */
    public Element maxPolynom(Element p, Element r, Ring ring) {
        if (p instanceof Polynom) {
            if (((Polynom) p).isZero(ring)) {
                return ring.numberZERO;
            }
            if (((Polynom) p).isOne(ring) || ((Polynom) p).isMinusOne(ring)) {
                return ((Polynom) p).coeffs[0];
            }
            Element max = ((Polynom) p).coeffs[0].abs(ring);
            for (int i = 1; i < ((Polynom) p).coeffs.length; i++) {
                if (((Polynom) p).coeffs[i].abs(ring).compareTo(max, ring) == 1) {
                    max = ((Polynom) p).coeffs[i].abs(ring);
                }
            }
            Element el1 = r.pow(((Polynom) p).powers[0] + 1, ring);//r^{n+1}
            Element el2 = el1.subtract(ring.numberONE, ring).divide(r.subtract(ring.numberONE, ring), ring);//(r^{n+1}-1)/(r-1)
            return max.multiply(el2, ring);
        } else {
            return p;
        }
    }

    /**
     * 1/2MIN[(p_r-p_s)]
     *
     * @param fp1
     * @param fp2
     * @param ring
     *
     * @return
     */
    public Element delta_value(FactorPol fp1, FactorPol fp2, Ring ring) {
        Element el1 = fp1.multin[0];
        Element el2 = fp2.multin[0];
        if ((el1 instanceof Polynom) && (el2 instanceof Polynom)) {
            if (((Polynom) el1).powers.length == 1) {
                el1 = ((Polynom) el1).coeffs[0];
            }
            if (((Polynom) el2).powers.length == 1) {
                el2 = ((Polynom) el2).coeffs[0];
            }
        }
        Element el3 = el2.subtract(el1, ring).abs(ring);
        Element min = ring.numberZERO;
        if (fp1.multin.length == fp2.multin.length) {
            for (int i = 1; i < fp1.multin.length; i++) {
                el1 = fp1.multin[i];
                el2 = fp2.multin[i];
                if ((el1 instanceof Polynom) && (el2 instanceof Polynom)) {
                    if (((Polynom) el1).powers.length == 1) {
                        el1 = ((Polynom) el1).coeffs[0];
                    } else {
                        el1 = ((Polynom) el1).coeffs[1];
                    }
                    if (((Polynom) el2).powers.length == 1) {
                        el2 = ((Polynom) el2).coeffs[0];
                    } else {
                        el2 = ((Polynom) el2).coeffs[1];
                    }
                }
                el3 = el2.subtract(el1, ring).abs(ring);
                if (el3.compareTo(min, ring) == -1) {
                    min = el3;
                }
            }
        }
        return min.divide(ring.numberONE.valOf(2, ring), ring);
    }

    /**
     *
     * @param n - количество переменных(кол-во столбцов в матрице p)
     * @param p - матрица полиномов после преобразования системы диф.ур.
     * @param T - граница
     * @param ring
     *
     * @return
     */
    public Element M_value(int n, Element[][] p, Element T, Ring ring) {
        Element sum = ring.numberZERO;
        Element prod = ring.numberONE;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p.length; j++) {
                sum = sum.add(maxPolynom(((Polynom) p[j][i]), T, ring).pow(2, ring), ring);
            }
            prod = prod.multiply(sum, ring);
        }
        return prod.sqrt(ring);
    }

    /**
     *
     * @param syst_dif - входная система диф.ур.
     * @param syst_inC - начальные условия
     * @param E - погрешность user
     * @param k - коэфффициент настройки
     * @param ring - кольцо
     *
     * @return - точность корней в разложении det
     *
     * @throws Exception
     */
    public Element accuracy_solveLDE(F syst_dif, F syst_inC, Element E, Element k, Ring ring) throws Exception {
        int step = 1;
        ring.FLOATPOS = 20;
        ring.setMachineEpsilonR64(new NumberR64(0.000000001));
        Element[] T_point = new Element[] {NumberR.ZERO, NumberR.TEN};//[0...T]
        int n = new SystemLDE().numVarsOfSystLDE(syst_dif);//количество переменных
        int m = syst_dif.X.length / 2;//количество строк матрицы
        //определяем переменные системы
        Vector v = vecFnameVar(syst_dif, ring);
        Element[][] fff = new SystemLDE().systDifEquationToMatrix(syst_dif, n, v, ring);//полиномиальная матрица
        System.out.println("Полиномиальная матрица: " + Array.toString(fff, new Ring("R64[p]",ring)));
        Element sum_C = ring.numberZERO;
        Element C_mn = ring.numberZERO;
        for (int i = 0; i < fff.length; i++) {
            for (int j = 0; j < fff[i].length; j++) {
                if (maxPolynom(((Polynom) fff[i][j]), T_point[1], ring).compareTo(C_mn, ring) == 1) {
                    C_mn = maxPolynom(((Polynom) fff[i][j]), T_point[1], ring);
                }
                for (int z = 0; z < ((Polynom) fff[i][j]).coeffs.length; z++) {
                    sum_C = sum_C.add(((Polynom) fff[i][j]).coeffs[z].abs(ring), ring);
                }
            }
        }
        Element ro = sum_C.divide(C_mn, ring);
        MatrixS ss = new MatrixS(fff, ring);
        MatrixS sss = ss.adjoint(ring);//обратная матрица
        System.out.println("Обратная матрица: " + sss.toString(new Ring("R64[p]",ring)));
        Polynom det = ((Polynom) ss.det(ring));
        System.out.println("Определитель полиномиальной матрицы: " + det.toString(new Ring("R64[p]",ring)));
////////////////////////////////////////////////////////////////////////////////
        FactorPol fact = SystemLDE.toPolynomOneVar(det, ring).factorOfPol_inC(ring);//0.00000000000000001  точность
        System.out.println("det после развала на простые сомножители... ->"
                + fact.toString(new Ring("R64[p]",ring)));

        Element[][] df = new SystemLDE().arrayInitCond(n, syst_inC, v, ring);//массив начальных значений
        System.out.println("Начальные  значения: " + Array.toString(df, new Ring("R64[p]",ring)));

        Element[] ppp = new SystemLDE().initCondLaplaceTransform(fff, df, ring);//массив полиномов начальных значений
        System.out.println("Полиномы начальных условий: " + Array.toString(ppp, new Ring("R64[p]",ring)));

        Element[] arrLP = new SystemLDE().vectorLaplaceTransform(syst_dif, ring);
        System.out.println("Правые части преобразованные по прямому преобразованию Лапласа: " + Array.toString(arrLP, new Ring("R64[p]",ring)));
        Element[] arrLP1 = new SystemLDE().addElementsToLaplace(arrLP, ring);
        System.out.println("----------------- " + Array.toString(arrLP1, new Ring("R64[p]",ring)));

        Element[] arrRightPath = new SystemLDE().rightPatSystLDELaplaceTransform(arrLP, ppp, ring);
        System.out.println("Правые части преобразованные по прямому преобразованию Лапласа + полиномы начальных условий " + Array.toString(arrRightPath, new Ring("R64[p]",ring)));

        for (;;) {
            step = step * 100;
            System.out.println("setZERO_R64 1 step " + ring.MachineEpsilonR64.toString(ring));
            ring.setMachineEpsilonR64(new NumberR64(1 / Math.pow(10, step)));
            System.out.println("setZERO_R64 2 step " + ring.MachineEpsilonR64.toString(ring));
            Polynom det_step = ((Polynom) ss.det(ring));
            System.out.println("Определитель полиномиальной матрицы: " + det.toString(new Ring("R64[p]",ring)));
////////////////////////////////////////////////////////////////////////////////
            FactorPol fact_step = SystemLDE.toPolynomOneVar(det_step, ring).factorOfPol_inC(ring);//0.00000000000000001  точность
            System.out.println("det после развала на простые сомножители... ->"
                    + fact.toString(new Ring("R64[p]",ring)));
            Element L1 = null;
            Element L2 = null;
            Element a1 = ring.numberONE.valOf(m * n, ring).multiply(T_point[1], ring);//m*n*T
            Element a2 = delta_value(fact, fact_step, ring).pow(m * n, ring).multiply(C_mn, ring);//C_mn*delta^{m*n}
            Element a3 = a1.divide(a2, ring);//(m*n*T) / C_mn*delta^{m*n}
            Element a4 = ring.numberONE;
            Element a5 = ring.numberZERO;
            Element a6 = ring.numberONE;
            Element a7 = ring.numberZERO;
            int l = 1;
            for (int i = 0; i < m; i++) {
                Element val = new F("\\exp(t)", ring).valueOf(new Element[] {ro.multiply(T_point[1], ring)}, ring);
                a4 = a4.multiply(maxFunc_InPoint(T_point, syst_dif.X[l], ring), ring).multiply(M_value(n, fff, T_point[1], ring), ring).multiply(val, ring);//Mf_i * M_il * exp(ro * T)
                a5 = a5.add(a4, ring);
                a6 = a6.multiply(maxPolynom(ppp[i], T_point[1], ring), ring).multiply(M_value(n, fff, T_point[1], ring), ring).multiply(val, ring);//mxa |p| S_i(p) * M_il * exp(ro * T)
                a7 = a7.add(a6, ring);
                l += 2;
            }
            L1 = a3.multiply(a5, ring);//(m*n*T) / C_mn*delta^{m*n} * (SUM[Mf_i * M_il * exp(ro * T)]_{i...m})
            L2 = a3.multiply(a7, ring);//(m*n*T) / C_mn*delta^{m*n} * (SUM[mxa |p| S_i(p) * M_il * exp(ro * T)]_{i...m})
            F f_e = new F("\\ln(t)", ring);
            //ln(e*(L1+L2)^{-1}+1-k)
            Element val_E = f_e.valueOf(new Element[] {E.multiply(ring.numberONE.divide(L1.add(L2, ring), ring), ring).add(ring.numberONE, ring).subtract(k, ring)}, ring);
            Element delta_E = val_E.divide(T_point[1], ring);//точность корней полинома(!!!!!!!!!!!!!!!)
            if (delta_E.compareTo(delta_value(fact, fact_step, ring), -2, ring)) {
                return delta_E;
            }
            fact = fact_step;
        }
    }

    /**
     * Грубый механизм настройки точности вычисления решения системы LDE
     *
     * @param detp - детерминант полиномиальной матрицы (матрица системы)
     * @param eps - погрешность решения заданная пользователем
     *
     * @return - необходимая точность в разложении детерминанта на множители,
     * которая позволит получить решение с заданной погрешностью.
     */
    public int slde_accuracy(Polynom detp, Element eps, Ring ring) throws Exception {
        ring.setAccuracy(ring.MachineEpsilonR.scale() + 5);
        ring.setMachineEpsilonR(ring.MachineEpsilonR.scale() + 3);
        ring.setFLOATPOS(ring.MachineEpsilonR.scale() + 3);
        FactorPol x0 = detp.factorOfPol_inC(ring);
        x0.normalForm(ring);
        //  System.out.println("FP in step 0 = " + x0.toString(ring));
        int k = 2;
        for (;;) {
            Element e = ring.numberZERO();
            ring.setAccuracy(ring.MachineEpsilonR.scale() + 5 * k);
            ring.setMachineEpsilonR(ring.MachineEpsilonR.scale() + 3 * k);
            ring.setFLOATPOS(ring.MachineEpsilonR.scale() + 3 * k);
            FactorPol x = detp.factorOfPol_inC(ring);
            x.normalForm(ring);
            //   System.out.println("FP in step " + (k - 1) + " = " + x.toString(ring));
            Element max = ring.numberZERO();
            for (int i = 0; i < x.multin.length; i++) {
                if (!x.multin[i].isItNumber()) {
                    max = x.multin[i].coeffs[1].subtract(x0.multin[i].coeffs[1], ring).abs(ring);//|x_i - x_0|
                }
                if (max.compareTo(e, ring) == 1) {
                    e = max;
                }
            }
            x0 = x;
            //   System.out.println("step = " + (k - 1));
            k++;
            //    System.out.println("e = " + e.toString(ring));
            if (e.compareTo(eps, ring) == -1) {
                break;//условие выхода
            }
        }
        int accracy_SLDE = ring.getAccuracy();
        return accracy_SLDE;
    }

    /**
     * Грубый механизм настройки точности вычисления решения системы LDE
     *
     * @param detp - детерминант полиномиальной матрицы (матрица системы)
     * @param eps - погрешность решения заданная пользователем
     *
     * @return - необходимая точность в разложении детерминанта на множители,
     * которая позволит получить решение с заданной погрешностью.
     */
    public int slde_accuracy1(Polynom detp, Element eps, Ring ring) throws Exception {
        ring.setAccuracy(ring.MachineEpsilonR.scale() + 1);
        ring.setMachineEpsilonR(ring.MachineEpsilonR.scale());
        ring.setFLOATPOS(ring.MachineEpsilonR.scale());
        FactorPol x0 = detp.factorOfPol_inC(ring);
        x0.normalForm(ring);
        //  System.out.println("FP in step 0 = " + x0.toString(ring));
        int k = 1;
        for (;;) {
            Element e = ring.numberZERO();
            ring.setAccuracy(ring.MachineEpsilonR.scale() + (k + 1));
            ring.setMachineEpsilonR(ring.MachineEpsilonR.scale() + k);
            ring.setFLOATPOS(ring.MachineEpsilonR.scale() + k);
            FactorPol x = detp.factorOfPol_inC(ring);
            x.normalForm(ring);
            System.out.println("FP in step " + (k - 1) + " = " + x.toString(ring));
            Element max = ring.numberZERO();
            for (int i = 0; i < x.multin.length; i++) {
                if (!x.multin[i].isItNumber()) {
                    max = x.multin[i].coeffs[1].subtract(x0.multin[i].coeffs[1], ring).abs(ring);//|x_i - x_0|
                }
                if (max.compareTo(e, ring) == 1) {
                    e = max;
                }
            }
            x0 = x;
            //   System.out.println("step = " + (k - 1));
            k++;
            System.out.println("e = " + e.toString(ring));
            System.out.println("eps = " + eps.toString(ring));
            if (e.compareTo(eps, ring) <= 0) {
                System.out.println("yes");
                break;//условие выхода
            }
        }
        int accracy_SLDE = ring.getAccuracy();
        return accracy_SLDE;
    }

    /**
     * Решение систем дифференциальных уравнений
     *
     * @param syst_dif - система дифференциальных уравнений
     * @param syst_inC - система начальных условий
     * @param ring - входное кольцо
     *
     * @return - решение системы
     *
     * @throws Exception
     */
    public VectorS solveSystemLDE_accuracy(F syst_dif, F syst_inC, NumberR eps, Ring ring) throws Exception {
        newRing = ring;
        int numVars = new SystemLDE().numVarsOfSystLDE(syst_dif);//количество переменных
        //определяем переменные системы
        Vector v = vecFnameVar(syst_dif, ring);
        Element[][] fff = new SystemLDE().systDifEquationToMatrix(syst_dif, numVars, v, ring);//полиномиальная матрица
        //System.out.println("Полиномиальная матрица: " + Array.toString(polMatrix, new Ring("R64[p]",ring)));
        MatrixS ss = new MatrixS(fff, ring);
        MatrixS sss = ss.adjoint(ring);//обратная матрица
        // System.out.println("Обратная матрица: " + adjointPolMatrixS.toString(new Ring("R64[p]",ring)));
        Element[][] df = new SystemLDE().arrayInitCond(numVars, syst_inC, v, ring);//массив начальных значений
        // System.out.println("Начальные  значения: " + Array.toString(df, new Ring("R64[p]",ring)));
        Element[] ppp = new SystemLDE().initCondLaplaceTransform(fff, df, ring);//массив полиномов начальных значений
        // System.out.println("Полиномы начальных условий: " + Array.toString(ppp, new Ring("R64[p]",ring)));
        Element[] arrLP = new SystemLDE().vectorLaplaceTransform(syst_dif, ring);
        //   System.out.println("Правые части преобразованные по прямому преобразованию Лапласа: " + Array.toString(arrLP, new Ring("R64[p]",ring)));
        Element[] arrLP1 = new SystemLDE().addElementsToLaplace(arrLP, ring);
        //  System.out.println("----------------- " + Array.toString(arrLP1, new Ring("R64[p]",ring)));
        Element[] arrRightPath = new SystemLDE().rightPatSystLDELaplaceTransform(arrLP, ppp, ring);
        //  System.out.println("Правые части преобразованные по прямому преобразованию Лапласа + полиномы начальных условий " + Array.toString(arrRightPath, new Ring("R64[p]",ring)));
        Polynom det = ((Polynom) ss.det(ring));
        int eps_accuracy = slde_accuracy(det, eps, ring);//точность
        //   System.out.println("Определитель полиномиальной матрицы: " + det.toString(new Ring("R64[p]",ring)));
////////////////////////////////////////////////////////////////////////////////
        FactorPol fact = ((Polynom) SystemLDE.toPolynomOneVar(det, ring).toNewRing(ring.algebra[0], ring)).factorOfPol_inC(ring);//0.00000000000000001  точность
        //System.out.println("det после развала на простые сомножители... ->"
        //         + fact.toString(new Ring("R64[p]",ring)));
        FactorPol detFactMNK = fact;
        // FactorPol detFactMNK = fact.prMNK(ring);
        //  System.out.println("det после развала на простые сомножители ->"
        //          + detFactMNK.toString(new Ring("R64[p]",ring)));
        ////////////////////////////////////////////////////////////////////////
        Polynom ch = new Polynom("1", ring);//r
        Element[] resDet = new SystemLDE().primeFractionForSingle(ch, detFactMNK, ring); //возвращает вектор решений
        //   System.out.println("resDet[] = " + Array.toString(resDet, new Ring("R64[p]",ring)));
        ////////////////////////////////////////////////////////////////////////
        if (detFactMNK.multin[0].coeffs.length == 1) { //если первый полином в FactorPol - есть число ...
            int[] pow = new int[detFactMNK.powers.length - 1];
            Polynom[] mul = new Polynom[detFactMNK.multin.length - 1];
            for (int ij = 1; ij < detFactMNK.multin.length; ij++) {
                pow[ij - 1] = detFactMNK.powers[ij];
                mul[ij - 1] = detFactMNK.multin[ij];
            }
            detFactMNK = new FactorPol(pow, mul);
        }
        ////////////////////////////////////////////////////////////////////////
        FractionSum fDet = new FractionSum(detFactMNK, resDet, ring); //создание объекта типа суммы pol1/pol2 + pol3/pol4...
        // System.out.println("  ----- разложенный det в виде FractionSum  : "
        //          + fDet.toStrings(new Ring("R64[p]",ring))); //вывод разложенного на суммы детерминанта
        ////////////////////////////////////////////////////////////////////////
        Element[] result = new SystemLDE().multiplyPolMatrix(sss.M, sss.col, arrRightPath, new Ring("R64[p]",ring)); //Возвращает произведение 2-ух матриц-решения и преобразованных по прямому преобразованию правых частей
        ////////////////////////////////////////////////////////////////////////
        Element[] solveFr = new SystemLDE().multiplyVector_and_Det(result, det, new Ring("R64[p]",ring));//Произведение 1/det * W
        //преобразование дробей по обратному преобразованию Лапласа
        ring.setFLOATPOS(eps.scale());//*************
        Element[] solveSystLDE = new SystemLDE().vectorInverseLaplaceTransform((FractionSum[]) solveFr, v, ring);



        if (eps.doubleValue() == ring.numberONE.doubleValue()) {
            int valN = 0;
            String sName = "";
            for (int v1 = 0; v1 < df.length; v1++) {
                valN = valN + df[v1].length;
                for (int v2 = 0; v2 < df[v1].length; v2++) {
                    if(v1 == df.length - 1 && v2 == df[v1].length - 1)
                        sName += ((Fname) df[v1][v2]).name;
                    else
                        sName += ((Fname) df[v1][v2]).name+",";
                }
            }
            sName = ring.varNames[0] + "," + sName;
            newRing = new Ring("R["+sName+"]");
            newRing.setDefaultRing();
            for (int i = 0; i < solveSystLDE.length; i++) {
                solveSystLDE[i] = symbolToVariable(((F) solveSystLDE[i]), newRing);
            }
        }


        return new VectorS(solveSystLDE);
    }

    public static F symbolToVariable(F f, Ring r) {
        for (int i = 0; i < f.X.length; i++) {
            if (f.X[i] instanceof Complex) {
                if (((Complex) f.X[i]).re instanceof F) {
                    ((Complex) f.X[i]).re = symbolToVariable(((F) ((Complex) f.X[i]).re), r);
                }
            } else {
                if (f.X[i] instanceof F) {
                    f.X[i] = symbolToVariable(((F) f.X[i]), r);
                } else {
                    if (f.X[i] instanceof Fname) {
                        for (int j = 0; j < r.varPolynom.length; j++) {
                            if (((Fname) f.X[i]).name.equals(r.varNames[j])) {
                                f.X[i] = r.varPolynom[j];
                                break;
                            }
                        }
                    }
                }
            }
        }
        return f;
    }

    public static void main(String[] args) {
        Ring ring = new Ring("R64[p]");
        ring.page=new Page(ring, true);
//        ring.setDefaulRing();
//        F g=new F("\\systLDE(\\d(x,t)-y+z=0, -x-y+\\d(y,t)=0, -x-z+\\d(z,t)=0)", ring);
//        F f=new F("\\initCond(\\d(x,t,0,0)=1,\\d(y,t,0,0)=2,\\d(z,t,0,0)=3)", ring);
//        Element accuracy = new SystemLDE().accuracy_solveLDE(g, f, NumberR.value(0.00001), NumberR.value(2), ring);
//        System.out.println("Accuracy LDE = " + accuracy);
        ////////////////////////////////////////////////////////////////////////
        //  Polynom p = new Polynom("4p^6+p^5-3p^4-5p^3-p^2+p-2", ring);
        Polynom p = new Polynom("2p^4-2p^2+1", ring);
        NumberR64 eps = new NumberR64("1.E-11");
        ring.setMachineEpsilonR(eps.intValue());
        int res = 0;
        try {
            res = new SystemLDE().slde_accuracy1(p, eps, ring);
        } catch (Exception ex) {
            Logger.getLogger(SystemLDE.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("res = " + res);
    }

    /**
     * Метод для сокращения комплескно-сопряженных частей
     *
     * @param fp - Объект типа - FactorPol
     *
     * @return
     */
    public FactorPol simplifyComplex(FactorPol fp, Ring ring) {
        FactorPol res = new FactorPol();
        ArrayList<Polynom> coef = new ArrayList<Polynom>();
        IntList pow = new IntList();
        ArrayList<Polynom> coef1 = new ArrayList<Polynom>();
        IntList pow1 = new IntList();
        for (int i = 0; i < fp.multin.length; i++) {
            Polynom p = fp.multin[i];
            if (p.coeffs.length == 2) {
                if (p.coeffs[1] instanceof Complex) {
                    Complex c = (Complex) p.coeffs[1];
                    if (!c.im.isZero(ring)) {
                        coef1.add(p);
                        pow1.add(fp.powers[i]);
                    } else {
                        coef.add(p);
                        pow.add(fp.powers[i]);
                    }
                } else {
                    coef.add(p);
                    pow.add(fp.powers[i]);
                }
            } else {
                coef.add(p);
                pow.add(fp.powers[i]);
            }
        }
        FactorPol res1 = multiplySimplifyComplex(coef1, pow1, ring);
        Polynom[] coeffs = new Polynom[coef.size() + res1.multin.length];
        int[] powers = new int[pow.size + res1.powers.length];
        coef.toArray(coeffs);
        System.arraycopy(res1.multin, 0, coeffs, coef.size(), res1.multin.length);
        System.arraycopy(pow.arr, 0, powers, 0, powers.length);
        System.arraycopy(res1.powers, 0, powers, powers.length - res1.powers.length, res1.powers.length);
        res = new FactorPol(powers, coeffs);


        return res;
    }

    public FactorPol multiplySimplifyComplex(ArrayList<Polynom> coeffs, IntList powers, Ring ring) {
        FactorPol res = new FactorPol();
        ArrayList<Polynom> coef = new ArrayList<Polynom>();
        IntList pow = new IntList();
        for (int i = 0; i < coeffs.size(); i++) {
            Polynom p = coeffs.get(i);
            for (int j = 0; j < coeffs.size(); j++) {
                Polynom q = coeffs.get(j);
                if ((p != null && q != null) && (i != j)) {
                    Complex c1 = (Complex) p.coeffs[1];
                    Complex c2 = (Complex) q.coeffs[1];
                    if (c1.re.equals(c2.re, ring)) {
                        if (c1.im.add(c2.im, ring).isZero(ring)) {
                            coef.add(p.multiply(q, ring).deleteZeroCoeff(ring));
                            pow.add(1);
                            coeffs.set(i, null);
                            coeffs.set(j, null);
                        }
                    }
                }
            }
        }
        Polynom[] coeffs1 = new Polynom[coef.size()];
        int[] powers1 = new int[pow.size];
        coef.toArray(coeffs1);
        System.arraycopy(pow.arr, 0, powers1, 0, powers1.length);
        res = new FactorPol(powers1, coeffs1);
        return res;
    }
}
