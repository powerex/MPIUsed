/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform.newLaplace;

import com.mathpar.func.*;
import java.util.*;

import com.mathpar.laplaceTransform.InverseLaplaceTransform;
import com.mathpar.laplaceTransform.LaplaceTransform;
import com.mathpar.matrix.*;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 * Класс содержит методы для решения систем линейных дифференциальных уравнений
 * с постоянными коэффициентами с помощью преобразования Лапласа
 *
 * Под новую структуру данных
 *
 * @author Rybakov Michail
 */
public class NewSystemLDE {
    public NewSystemLDE() {
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
    public Element[] initCondSystLDE(Element[][] M,
            Element[][] coeff, Ring ring) {
        int k = 0; //счетчик по элементам выходного массива
        Element[] pol = new Element[coeff.length]; //создаем массив полиномов начальных условий размера равного размеру входного массива значений начальных условий по каждой переменной
        for (int i = 0; i < M.length; i++) {
            Element p = Polynom.polynomZero; //создаем нулевой полином  {M[0][0].zero()}
            for (int j = 0; j < M[i].length; j++) {
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
     * возвращает вектор решений
     *
     * @param ch Polynom
     * @param zn FactorPol
     *
     * @return Element[]
     *
     * @author Ribakov Mixail
     */
    public Element[] Univers_primeFractionCoeffitions(Element ch, FactorPol zn, Ring ring) throws
            Exception {
        //Этап №1. запоминаем числовой сомножитель полученный при факторизации
        Element delet = ring.numberONE; //присваеваем на первом этапе 1 коэффициент
        //&& (zn.multin[0].powers[0] == 0)
        if ((zn.multin[0].isOne(ring)) || (zn.multin[0].isMinusOne(ring)) || ((zn.multin[0].coeffs.length == 1) && (zn.multin[0].powers.length == 0))) {
            //если первый полином в FactorPol - есть число
            delet = zn.multin[0].coeffs[0]; //запоминаем коэффициент
            int[] pow = new int[zn.powers.length - 1];
            Polynom[] mul = new Polynom[zn.multin.length - 1];
            for (int i = 1; i < zn.multin.length; i++) {
                pow[i - 1] = zn.powers[i];
                mul[i - 1] = zn.multin[i];
            }
            zn = new FactorPol(pow, mul);
        }
        int n = 0; // колличество дробей
        for (int i = 0; i < zn.multin.length; i++) {
            if (zn.multin[i] instanceof Polynom) {
                if (zn.multin[i].powers[0] == 1) {
                    n = n + zn.powers[i];
                } else {
                    n = n + (zn.multin[i].powers[0] * zn.powers[i]);
                }
            }
        }
        //Этап №2. Создаем полиномы добавки к каждой дроби.
        Polynom polnew[] = new Polynom[n]; //создаём массив полиномов --- (добавки)
        int[] masFlag = new int[n];//массив флагов
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
        for (int a = 0; a < zn.powers.length; a++) { //вычисляем добавки к дробям
            Polynom del = pr;
            for (int m = 0; m < zn.powers[a]; m++) {
                if (zn.multin[a].powers[0] == 1) {
                    del = del.divideExact(zn.multin[a], ring);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    polnew[m + ds1] = del;
                    masFlag[m + ds1] = 0;
                    ds2 = ds2 + 1;
                } else {
                    for (int nm = 0; nm < zn.multin[a].powers[0]; nm++) {
                        del = pr;
                        del = del.divideExact(zn.multin[a], ring);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        for (int mn = 0; mn < del.powers.length; mn++) {
                            del.powers[mn] = del.powers[mn] + (zn.powers[a] * zn.multin[a].powers[0] - 1);
                        }
                        polnew[m + ds1] = del;
                        masFlag[m + ds1] = 1;
                        ds1++;
                        ds2 = ds2 + 1;
                    }
                }
            }
            ds1 = ds2;
        }
        //////////////////////////////////////////
//        System.out.println("POlYNOM D");
//        for (int kk = 0; kk < n; kk++) {
//            System.out.println("[" + kk + "] = " + polnew[kk]);
//        }
        /////////////////////////////////////////
        boolean fl = true;//отвечает за числитель...
        int L = 0;
        if (ch instanceof Polynom) {
            if (((Polynom) ch).powers.length != 0) {
                // L = ch.powers[0]; //величина старшей степени полинома в числителе
                L = maxPowersCH(((Polynom) ch)); //величина старшей степени полинома в числителе
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
            b_length = (L + 1); //массив свободных коэффициентов
        }
        if (L < power_max) {
            b_length = (power_max + 1); //массив свободных коэффициентов
        }
        //NumberC64[] b = new NumberC64[b_length]; //массив свободных коэффициентов
        Element[] b = new Element[b_length]; //массив свободных коэффициентов //double
        int n1 = (int) b_length / 2;
        // int varstep = 0;
        if (ch instanceof Polynom) {
            if (((Polynom) ch).powers.length != 0) {
                for (int m = 0; m < ((Polynom) ch).powers.length; m++) { //вычисляем столбец свободных членов
                    b[b_length - ((Polynom) ch).powers[m] - 1] = ((Polynom) ch).coeffs[m]; //doubleValue();
                    // varstep++;
                }
            } else {
                for (int m = 1; m < b_length; m++) { //вычисляем столбец свободных членов
                    b[m] = ring.numberZERO; // 0
                }
                b[b_length - 1] = ((Polynom) ch).coeffs[0]; //doubleValue();
            }
        } else {//случай когда числитель не Polynom
            if (!(ch instanceof F) && !(ch instanceof Fname)) {///NEWWWWWWWWWW
                for (int m = 1; m < b_length; m++) { //вычисляем столбец свободных членов
                    b[m] = ring.numberZERO; // 0
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
        Element[][] matrix = new Element[b_length][n + 1]; //создание матрицы!!!!! //double
        for (int q = 0; q < b_length; q++) { //заполняем последний столбец матрицы свободными членами
            matrix[q][n] = b[q];
        }
        ///////////////////////////M/////////////////////////////////////
        int dd = -1;
        for (int h = 0; h < n; h++) { // заполняем матрицу
            if (masFlag[h] == 1) {
                dd++;
                for (int hh = 0; hh < polnew[h].powers.length; hh++) {
                    matrix[b_length - polnew[h].powers[hh] - 1 + dd][h] = polnew[h].coeffs[hh];
                }
            } else {
                for (int hh = 0; hh < polnew[h].powers.length; hh++) {
                    matrix[b_length - polnew[h].powers[hh] - 1][h] = polnew[h].coeffs[hh];
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

        Element[] solve = new Element[k1]; //создание массива решения
        for (int i = 0; i < M2.M.length; i++) {
            NumberR64 dlin = new NumberR64(M2.M[i].length);
            if (dlin.equals(NumberR64.ONE) == false) {
                if (M2.M[0][0].isOne(ring)) {
                    solve[i] = M2.M[i][M2.M[i].length - 1];
                } else {
                    solve[i] = M2.M[i][M2.M[i].length - 1].divide(M2.M[0][0], ring);
                }
            } else {
                if (M2.M[0][0].isOne(ring)) {
                    solve[i] = (NumberR64.ZERO);
                } else {
                    solve[i] = (NumberR64.ZERO).divide(M2.M[0][0], ring);
                }
            }
        }
        Element[] solveM = new Element[solve.length];
        for (int i = 0; i < solveM.length; i++) {
            if (solve[i] instanceof Fname) {
                if (delet.isOne(ring)) {
                    solveM[i] = solve[i];
                } else {
                    solveM[i] = solve[i].divide(delet, ring);
                }
            } else if (solve[i] instanceof F) {
                if (delet.isOne(ring)) {
                    solveM[i] = solve[i];
                } else {
                    solveM[i] = solve[i].divide(delet, ring);
                }
            } else {
                if (delet.isOne(ring)) {
                    solveM[i] = solve[i];
                } else {
                    solveM[i] = solve[i].divide(delet, ring);
                }
            }
        }
//        for (int i = 0; i < solveM.length; i++) {
//            System.out.println("solve[ " + i + " ]= " + solveM[i].toString(ring));
//        }
        return solveM;
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
    public SumOfProduct[] rightPatSystLDELaplaceTransform(SumOfProduct[] m1, Element[] m2, Ring ring) {
        SumOfProduct[] f = new SumOfProduct[m1.length];
        for (int i = 0; i < f.length; i++) {
            f[i] = m1[i].add(m2[i], ring);
        }
        return f;
    }

//    /*
//     * Складывает дроби this и f, затем сокращает числитель и знаменатель
//     * результата на их НОД и возвращает результат.
//     * ///!!!!!!!!!! без сокращения
//     * Параметры:
//     * this, f   -  дроби типа Fraction
//     */
//    public Fraction addFR(Fraction f, Fraction f1, Ring ring) {
//        if (f.num.signum() == 0 && f1.num.signum() == 0) {
//            return f.myZero(ring);
//        } else if (f.num.signum() == 0) {
//            return f1;
//        } else if (f1.num.signum() == 0) {
//            return f;
//        }
//        Element resnum = f.num.multiply(f1.denom, ring).add(f1.num.multiply(f.denom, ring), ring);
//        if (resnum.signum() == 0) {
//            return f.myZero(ring);
//        }
//        Element resdenom = f.denom.multiply(f1.denom, ring);
//        if (!(resnum instanceof F) && !(resdenom instanceof F)) {
//            return new Fraction(resnum, resdenom);
//        } else {
//            return new Fraction(resnum, resdenom);
//        }
//    }
    /**
     * Возвращает объект типа SumOfProduct Произведение M*B
     *
     * @param m - матрица левой части СЛДУ
     * @param mm
     * @param b - столбец правых частей СЛДУ
     * @param ring
     *
     * @return
     */
    public SumOfProduct[] multiplyPolMatrix(Element[][] m, int[][] mm, SumOfProduct[] b, Ring ring) {
        SumOfProduct[] res = new SumOfProduct[b.length];
        System.out.println("Столбец В: ");
        for (int i = 0; i < b.length; i++) {
            System.out.println(b[i].toString(ring));
        }
        System.out.println("Матрица М: ");
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][mm[i][j]].toString(ring) + "  ");
            }
            System.out.println();
        }
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                for (int k = 0; k < ((Polynom) m[i][mm[i][j]]).coeffs.length; k++) {
                    ((Polynom) m[i][mm[i][j]]).coeffs[k] = ((Polynom) m[i][mm[i][j]]).coeffs[k].toNumber(Ring.C64, ring);
                }
            }
        }
        SumOfProduct[] ppOut = null;
        Element el = null;
        for (int i = 0; i < b.length; i++) {
            ppOut = new SumOfProduct[m[i].length];
            int k = 0;
            for (int j = 0; j < m[i].length; j++) {
                el = m[i][mm[i][j]].multiply(b[k], ring);
                ppOut[k] = (SumOfProduct) el;
                k++;
            }
            res[i] = ppOut[0];
            for (int z = 1; z < ppOut.length; z++) {
                res[i] = res[i].add(ppOut[z], ring);
            }
        }
        for (int i = 0; i < res.length; i++) {
            System.out.println("multiply Matrix and Vector [" + i + "]= " + res[i].toString(ring));
        }
        return res;

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

    /**
     * Решение систем обыкновенных линейных дифференциальных уравнений с
     * постоянными коэффициентами
     *
     * @param syst_dif - система дифференциальных уравнений
     * @param syst_inC - система начальных условий
     * @param ring - входное кольцо
     *
     * @return - решение системы дифференциальных уравнений
     *
     * @throws Exception
     */
    public VectorS solveSystemLDE(F syst_dif, F syst_inC, Ring ring) throws Exception {
        //Кольцо полиномов
        Ring r = new Ring("R64[p]",ring);
        //Определение количества переменных в системе
        int numVars = numVarsOfSystLDE(syst_dif);
        //Определение имен переменных в системе
        Vector nameVars = vecFnameVar(syst_dif, ring);
        //Получение полиномиальной матрицы
        Element[][] pMatrix = systDifEquationToMatrix(syst_dif, numVars, nameVars, ring);
        System.out.println("Полиномиальная матрица: " + Array.toString(pMatrix, r));
        //Преобразование матрицы полиномов в объект MatrixS
        MatrixS matrix = new MatrixS(pMatrix, ring);
        //Получение присоединенной матрицы
        MatrixS adjMatrix = matrix.adjoint(ring);
        System.out.println("Присоединенная матрица: " + adjMatrix.toString(r));
        //Получение значений начальных условий для системы
        Element[][] valueIC = arrayInitCond(numVars, syst_inC, nameVars, ring);
        System.out.println("Начальные  значения: " + Array.toString(valueIC, r));
        //Поулчение полиномов начальных условий
        Element[] pIC = initCondSystLDE(pMatrix, valueIC, ring);
        System.out.println("Полиномы начальных условий: " + Array.toString(pIC, r));
        //Вектор правых частей преобразованных по прямому преобразованию Лапласа
        Element[] vectorLP = vectorLaplaceTransform(syst_dif, ring);
        System.out.println("Правые части преобразованные по прямому преобразованию Лапласа: " + Array.toString(vectorLP, r));
        //Преобразование правых частей в объект SumOfProduct
        SumOfProduct[] vectorSP = convertSumOfProduct(vectorLP, ring);
        System.out.println("Правые части преобразованные в объекты SumOfProduct: " + Array.toString(vectorSP, r));
        //Преобразование суммы правых частей в объект SumOfProduct
        SumOfProduct[] rigthPath = rightPatSystLDELaplaceTransform(vectorSP, pIC, ring);
        System.out.println("Правые части преобразованные по прямому преобразованию Лапласа + полиномы начальных условий " + Array.toString(rigthPath, r));
        //Упрощение вектора правой части
        SumOfProduct[] B = koeff(rigthPath, r);
        System.out.println("before simplify rigthPath = " + Array.toString(B, r));
        clear(B, ring);
        System.out.println("after simplify rigthPath = " + Array.toString(B, r));
        //Получение определителя полиномиальной матрицы
        Polynom det = ((Polynom) matrix.det(ring));
        System.out.println("Определитель полиномиальной матрицы: " + det.toString(r));
        //Получение объекта SumOfProduct
        SumOfProduct scalar = new SumOfProduct().create(toPolynomOneVar(det, new Ring("Z[p]")), ring);
        System.out.println("1/det(A) = " + scalar.toString(ring));
        SumOfProduct[] multMV = multiplyPolMatrix(adjMatrix.M, adjMatrix.col, B, new Ring("C64[p]")); //Возвращает произведение 2-ух матриц-решения и преобразованных по прямому преобразованию правых частей
        SumOfProduct[] simplMultMV = new SumOfProduct[multMV.length];
        //Упрощение произведения матрицы на вектор.
        for (int i = 0; i < simplMultMV.length; i++) {
            simplMultMV[i] = expandFromSumOfProduct(multMV[i], ring);
        }
        SumOfProduct[] multVS = new SumOfProduct[simplMultMV.length];
        for (int j = 0; j < multVS.length; j++) {
            multVS[j] = simplMultMV[j].multiply(scalar, new Ring("C64[p]"));
            System.out.println("multiply Vector and Scalar [" + j + "]= " + multVS[j]);
        }
        //Упрощение произведения вектора на 1/det
        SumOfProduct[] W = new SumOfProduct[multVS.length];
        for (int z = 0; z < W.length; z++) {
            W[z] = expandFromSumOfProduct(multVS[z], new Ring("C64[p]"));
            System.out.println("multiply Vector and Scalar after simplify[" + z + "]= " + W[z]);
        }
        for (int k = 0; k < W.length; k++) {
            W[k].simplifyTheSolve(ring);
        }
        //преобразование дробей по обратному преобразованию Лапласа
        Element[] solveSystLDE = new NewSystemLDE().vectorInverseLaplaceTransform(W, nameVars, ring);
        return new VectorS(solveSystLDE);
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
        for (int i = 0; i < syst_dif.X.length; i += 2) {
            n++;
        }
        return n;
    }

    /**
     * Очищает вектор функций преобразованных с помощью обратного преобразования
     * Лапласа от null значений
     *
     * @param arr
     *
     * @return
     */
    public F[] clearNullSolveILP(F[] arr) {
        ArrayList<F> newArr = new ArrayList<F>();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                newArr.add(arr[i]);
            }
        }
        return newArr.toArray(new F[newArr.size()]);
    }

    /**
     * возвращает решение системы в виде массива Element[]
     *
     * @param a
     * @param ring
     *
     * @return
     */
    public Element[] vectorInverseLaplaceTransform(SumOfProduct[] a, Vector v, Ring ring) {
        Element[] solveSyst = new Element[a.length];
        System.out.println("Решение системы:");
        for (int l = 0; l < a.length; l++) {
            boolean fl = false;
            F[] fArray = new F[a[l].sum.length];
            for (int i = 0; i < a[l].sum.length; i++) {
                if (a[l].sum[i].multin.get(0) instanceof FactorPol) {
                    FactorPol fpz = (FactorPol) a[l].sum[i].multin.get(0);

                    for (int jj = 0; jj < fpz.powers.length; jj++) {
                        if (fpz.powers[jj] < 0) {
                            fl = true;
                        }
                    }
                }
                Element[][] arr = a[l].sum[i].expandProduct(ring);
                Element f1 = null;
                Element f2 = null;
                if ((arr[0].length != 1) && (arr[1].length != 1)) {
                    f1 = new F(F.MULTIPLY, arr[0]);
                    f2 = new F(F.MULTIPLY, arr[1]);
                } else {
                    f1 = arr[0][0];
                    f2 = arr[1][0];
                }
                Element res = null;
                if (f1 instanceof FactorPol && f2 instanceof FactorPol) {
                    Element el1 = f1;
                    if (((FactorPol) f1).multin.length == 0) {
                        el1 = Polynom.polynomFromNumber(ring.numberONE(), ring);
                    }
                    if (((FactorPol) f1).multin.length == 1) {
                        if (((FactorPol) f1).powers.length == 1) {
                            if (((FactorPol) f1).powers[0] == 1) {
                                el1 = ((FactorPol) f1).multin[0];
                            }
                        }
                    } else {//NEW!
                        el1 = Polynom.polynomFromNumber(ring.numberONE(), ring);
                        for (int q = 0; q < ((FactorPol) f1).multin.length; q++) {
                            for (int w = 0; w < ((FactorPol) f1).powers[q]; w++) {
                                el1 = el1.multiply(((FactorPol) f1).multin[q], ring);
                            }
                        }
                    }
                    Element el2 = f2;
                    if (((FactorPol) f2).multin.length == 1) {
                        if (((FactorPol) f2).powers.length == 1) {
                            if (((FactorPol) f2).powers[0] == -1) {
                                el2 = ((FactorPol) f2).multin[0];
                            } else {
                                el2 = new F(F.intPOW, new Element[] {((FactorPol) f2).multin[0], new NumberR64(((FactorPol) f2).powers[0]).abs(ring)});
                            }
                        }
                    }
                    res = new F(F.DIVIDE, new Element[] {el1, el2});
                } else {
                    res = new F(F.DIVIDE, new Element[] {f1, f2});
                }
                try {
                    if (fl) {
                        fArray[i] = new InverseLaplaceTransform().inverseLaplaceTransform(res, ring);
                    }
                } catch (Exception ex) {
                    ring.exception.append(
                            "Exeption in systDifEquationToMatrix in package LaplaceTransform: " + ex);
                }
            }
            fArray = clearNullSolveILP(fArray);
            System.out.println(((Fname) v.get(l)).name + "(t)  = " + new F(F.ADD, fArray).toString(ring));
            solveSyst[l] = new F(F.ADD, fArray);
        }
        return solveSyst;
    }

    /**
     * возвращает вектор входных переменных системы LDE
     *
     * @param right_f
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

    /**
     * Конвертирует массив функций преобразованных по Лалпаса в массив объектов
     * типа SumOfProduct
     *
     * @param el - массив F
     * @param ring
     *
     * @return
     */
    public SumOfProduct[] convertSumOfProduct(Element[] el, Ring ring) {
        int l = el.length;
        SumOfProduct[] res = new SumOfProduct[l];
        for (int i = 0; i < l; i++) {
            res[i] = new SumOfProduct(((F) el[i]), ring);
        }
        return res;


    }

    /**
     * Метод возвращает истину в случае, когда решение задачи нахождения корней,
     * на каждом шаге меньше чем искомая погрешность
     *
     * @param p
     * @param fp
     * @param eps
     * @param ring
     *
     * @return
     */
    public boolean epsValue(Polynom p, FactorPol fp, NumberR64 eps, Ring ring) {
        int i = 0;
        Element[] el_eps = primeFractionForSingle(p, fp, ring.numberONE(), ring);
        for (;;) {
            Element[] el = primeFractionForSingle(p, fp, ring.numberONE(), ring);
            Element[] sub = new Element[el.length];
            for (int j = 0; j < el.length; j++) {
                sub[j] = el[j].subtract(el_eps[j], ring).abs(ring);
            }
            Element max = sub[0];
            for (int k = 1; k < sub.length; k++) {
                if (sub[k].compareTo(max, ring) == 1) {
                    max = sub[k];
                }
            }
            if (max.compareTo(eps, ring) == -1) {
                return true;
            } else {
                el_eps = el;
            }
            ring.setAccuracy(ring.getAccuracy() + 1);
            ring.MachineEpsilonR = (NumberR) ring.MachineEpsilonR.add(NumberR.valueOf(1), ring);
        }
        //return false;
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
//        System.out.println("************************");
//        System.out.println(Array.toString(b));
//        System.out.println("************************");
//        System.out.println(Array.toString(a, ring));
//        System.out.println("************************");
        //решение системы
        MatrixS M1 = new MatrixS(a, ring); //преобразование в матрицу над полиномами!!!
//        System.out.println("====================MatrixS======================");
//        System.out.println(M1.toString(ring));
//        System.out.println("==================================================");
//        System.out.println("=============MatrixS.toEchelonForm================");
        MatrixS M2 = null;
        CanonicForms cfs = null;
        M2 = M1.toEchelonForm(ring); //преобразование матрицы к виду - на главной диагонале стоят определители а в последнем столбце свободные члены
//        System.out.println(M2.toString());
//        System.out.println("==================================================");
        Element[] sysSolve = new Element[M2.colNumb - 1];
        if (M2.isSysSolvable()) {
            sysSolve = M2.oneSysSolv_and_Det(ring);
        } else {
            System.out.println("System error!!!");
            return sysSolve;
        }
//        System.out.println("решение системы");
//        System.out.println(Array.toString(sysSolve));

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
     * На входе SumOfProduct[] после прямого преобразования лапласа правой части
     * ЛДУ и начальных условий Упрощение SumOfProduct[]
     *
     * @param s
     * @param ring
     *
     * @return
     */
    public SumOfProduct[] koeff(SumOfProduct[] s, Ring ring) {
        SumOfProduct[] res = new SumOfProduct[s.length];
        for (int j = 0; j < s.length; j++) {
            Product[] p = s[j].sum;
            ArrayList<SumOfProduct> arr = new ArrayList<SumOfProduct>();//временное хранилище
            for (int i = 0; i < p.length; i++) {
                if ((p[i].powers.size() == 1) && (p[i].multin.size() == 1)) {
                    if (p[i].powers.get(0).equals(Ring.oneOfType(p[i].powers.get(0).numbElementType()), ring)) {
                        if (p[i].multin.get(0) instanceof FactorPol) {
                            if (((FactorPol) p[i].multin.get(0)).multin.length != 1) {
                                FactorPol[] fp = new FactorPol[2];
                                fp = new FactorPol().toNumDenom(((FactorPol) p[i].multin.get(0)));
                                Polynom ch = (Polynom)fp[0].toPolynomOrFraction(ring);
                                for (int z = 0; z < fp[1].powers.length; z++) {
                                    fp[1].powers[z] = Math.abs(fp[1].powers[z]);//степени знаменателя FactorPol для полиномов делаем из -1 на +1
                                }
                                Polynom znn = (Polynom)fp[1].toPolynomOrFraction(ring);
                                znn = toPolynomOneVar(znn, ring);
                                if (ch.powers.length != 0) {
                                    if (ch.powers[0] < znn.powers[0]) {
                                        FactorPol zn = znn.factorOfPol_inC(ring);
                                        if (zn.multin.length != 1) {
                                            zn = simplifyComplex(zn, ring);//zn.factor_notSOPR(ring);
                                            System.out.println("zn _" + i + "  _" + zn.toString(ring));
                                            if (zn.multin.length != 1) {
                                                Element[] solv = primeFractionForSingle(ch, zn, ring.numberONE(), new Ring("R64[p]",ring));
                                                System.out.println("solv_" + i + " _" + Array.toString(solv));
                                                arr.add(new SumOfProduct(solv, zn, ring));
                                            } else {
                                                arr.add(new SumOfProduct(new Product[] {p[i]}));
                                            }
                                        } else {
                                            arr.add(new SumOfProduct(new Product[] {p[i]}));
                                        }
                                    } else {
                                        arr.add(new SumOfProduct(new Product[] {p[i]}));
                                    }
                                } else {
                                    FactorPol zn = znn.factorOfPol_inC(ring);
                                    if (zn.multin.length != 1) {
                                        zn = simplifyComplex(zn, ring);//zn.factor_notSOPR(ring);
                                        System.out.println("zn _" + i + "  _" + zn.toString(ring));
                                        if (zn.multin.length != 1) {
                                            Element[] solv = primeFractionForSingle(ch, zn, ring.numberONE(), new Ring("R64[p]",ring));
                                            System.out.println("solv_" + i + " _" + Array.toString(solv));
                                            arr.add(new SumOfProduct(solv, zn, ring));
                                        } else {
                                            arr.add(new SumOfProduct(new Product[] {p[i]}));
                                        }
                                    } else {
                                        arr.add(new SumOfProduct(new Product[] {p[i]}));
                                    }
                                }
                            } else {
                                arr.add(new SumOfProduct(new Product[] {p[i]}));
                            }
                        } else {
                            arr.add(new SumOfProduct(new Product[] {p[i]}));
                        }
                    }
                }
            }
            res[j] = arr.get(0);
            for (int q = 1; q < arr.size(); q++) {
                res[j] = res[j].add(arr.get(q), ring);
            }
        }
        return res;
    }

    /**
     * Возвращает сумму обхектов, после операции разложения по методу
     * неопределенных коэффициентов знаменателей
     *
     * @param s - сумма объектов, знаменатели у которых факторизованы
     * @param ring
     */
    public SumOfProduct expandFromSumOfProduct(SumOfProduct s, Ring ring) {
        ArrayList<Element> arr_SP = new ArrayList<Element>();//массив для хранения упрощенных объектов типа Product
        Product[] p = s.sum;
        for (int i = 0; i < p.length; i++) {
            if ((p[i].powers.size() == 1) && (p[i].multin.size() == 1)) {
                if (p[i].powers.get(0).equals(Ring.oneOfType(p[i].powers.get(0).numbElementType()), ring)) {
                    if (p[i].multin.get(0) instanceof FactorPol) {
                        FactorPol[] fp = new FactorPol[2];
                        fp = new FactorPol().toNumDenom(((FactorPol) p[i].multin.get(0)));
                        fp[0].normalFormInField(ring);
                        Polynom ch = (Polynom)fp[0].toPolynomOrFraction(ring);
                        for (int z = 0; z < fp[1].powers.length; z++) {
                            fp[1].powers[z] = Math.abs(fp[1].powers[z]);//степени знаменателя FactorPol для полиномов делаем из -1 на +1
                        }
                        Polynom znn = (Polynom)fp[1].toPolynomOrFraction(ring);
                        //удаление 0 коэффициентов
                        znn = znn.deleteZeroCoeff(ring);
                        znn = toPolynomOneVar(znn, ring);
                        if ((!ch.isItNumber()) && (!znn.isItNumber())) {//fp[1].powers[0] >= 2
                            if ((ch.powers[0] < znn.powers[0]) && (fp[1].multin.length != 1) && (znn.powers[0] >= 2)) {
                                FactorPol zn = fp[1];
                                zn = simplifyComplex(zn, ring);
                                System.out.println("zn _" + i + "  _" + zn.toString(ring));
                                Element[] solv = primeFractionForSingle(ch, zn, ring.numberONE(), ring);
                                System.out.println("solv_" + i + " _" + Array.toString(solv));
                                zn.deletZeroCoeffsInMultins(ring);
                                arr_SP.add(new SumOfProduct(solv, zn, ring));
                            } else {
                                if (((ch.powers[0] == 1) && (ch.coeffs.length >= 2)) && ((znn.powers[0] == 1) && (znn.coeffs.length >= 2))) {
                                    //Случай, когда дробь состоит из полиномиального числителя и знаменателя
                                    //Из R64 -> C64
                                    Polynom polR = (Polynom)fp[1].toPolynomOrFraction(ring);
                                    for (int kl = 0; kl < polR.coeffs.length; kl++) {
                                        polR.coeffs[kl] = polR.coeffs[kl].toNewRing(Ring.C64, ring);
                                    }
                                    Polynom[] arrDiv = ch.divAndRem(polR, ring);
                                    Product pr1 = new Product(arrDiv[0], ring);
                                    Product pr2 = null;
                                    //new!
                                    arrDiv[0] = arrDiv[0].deleteZeroCoeff(ring);
                                    arrDiv[1] = arrDiv[1].deleteZeroCoeff(ring);
                                    polR = polR.deleteZeroCoeff(ring);
                                    if (!arrDiv[1].isZero(ring)) {
                                        pr2 = new Product(new Element[] {arrDiv[1], polR}, new Element[] {ring.numberONE, ring.numberMINUS_ONE}, ring);
                                    }
                                    if (pr2 != null) {
                                        arr_SP.add(new SumOfProduct(new Product[] {pr1, pr2}));
                                    } else {
                                        arr_SP.add(new SumOfProduct(new Product[] {pr1}));
                                    }
                                } else {
                                    if ((ch.powers[0] >= 1) && (znn.powers[0] >= 1)) {
                                        //Случай, когда дробь состоит из полиномиального числителя и знаменателя
                                        //Из R64 -> C64
                                        Polynom polR = (Polynom)fp[1].toPolynomOrFraction(ring);
                                        for (int kl = 0; kl < polR.coeffs.length; kl++) {
                                            polR.coeffs[kl] = polR.coeffs[kl].toNewRing(Ring.C64, ring);
                                        }
                                        Polynom[] arrDiv = ch.divAndRem(polR, ring);
                                        Product pr1 = new Product(arrDiv[0], ring);
                                        Product pr2 = null;
                                        //New!
                                        arrDiv[0] = arrDiv[0].deleteZeroCoeff(ring);
                                        arrDiv[1] = arrDiv[1].deleteZeroCoeff(ring);
                                        polR = polR.deleteZeroCoeff(ring);
                                        if (!arrDiv[1].isZero(ring)) {
                                            if (fp[1].multin.length == 1) {
                                                if (fp[1].powers[0] >= 2) {
                                                    fp[1].multin[0].deleteZeroCoeff(ring);
                                                    pr2 = new Product(new Element[] {arrDiv[1], fp[1].multin[0]}, new Element[] {ring.numberONE, ring.numberMINUS_ONE.multiply(new Complex(fp[1].powers[0]), ring)}, ring);
                                                } else {
                                                    pr2 = new Product(new Element[] {arrDiv[1], polR}, new Element[] {ring.numberONE, ring.numberMINUS_ONE}, ring);
                                                }
                                            } else {
                                                pr2 = new Product(new Element[] {arrDiv[1], polR}, new Element[] {ring.numberONE, ring.numberMINUS_ONE}, ring);
                                            }
                                        }
                                        if (pr2 != null) {
                                            if (pr2.multin.size() == 1) {
                                                if (pr2.multin.get(0) instanceof FactorPol) {
                                                    FactorPol fpz = (FactorPol) pr2.multin.get(0);
                                                    if (fpz.multin[0] instanceof Polynom) {
                                                        if (fp[1].multin.length == 1) {
                                                            if (fp[1].powers[0] >= 2) {
                                                                fp[1] = simplifyComplex(fp[1], ring);
                                                                System.out.println("zn _" + i + "  _" + fp[1].toString(ring));
                                                                Element[] solv = primeFractionForSingle(arrDiv[1], fp[1], ring.numberONE(), ring);
                                                                System.out.println("solv_" + i + " _" + Array.toString(solv));
                                                                fp[1].deletZeroCoeffsInMultins(ring);
                                                                arr_SP.add(new SumOfProduct(solv, fp[1], ring));
                                                            } else {
                                                                arr_SP.add(new SumOfProduct(new Product[] {pr1, pr2}));
                                                            }
                                                        } else {
                                                            arr_SP.add(new SumOfProduct(new Product[] {pr1, pr2}));
                                                        }
                                                    } else {
                                                        arr_SP.add(new SumOfProduct(new Product[] {pr1, pr2}));
                                                    }
                                                } else {
                                                    arr_SP.add(new SumOfProduct(new Product[] {pr1, pr2}));
                                                }
                                            } else {
                                                arr_SP.add(new SumOfProduct(new Product[] {pr1, pr2}));

                                            }
                                        } else {
                                            arr_SP.add(new SumOfProduct(new Product[] {pr1}));
                                        }
                                    } else {
                                        arr_SP.add(p[i].multin.get(0));
                                    }

                                }
                            }
                        } else {
                            if ((ch.isItNumber()) && (!znn.isItNumber())) {
                                if (znn.powers[0] >= 2) {
                                    if (znn.coeffs.length == 1) {
                                        //fp[1].multin[0] -> znn
                                        Product prod = new Product(new Element[] {ch, znn}, new Element[] {ring.numberONE, ring.numberMINUS_ONE.multiply(new Complex(znn.powers[0]), ring)}, ring);
                                        arr_SP.add(new SumOfProduct(new Product[] {prod}));
                                    } else {
                                        FactorPol zn = fp[1];
                                        if (zn.multin.length != 1) {
                                            zn = simplifyComplex(zn, ring);
                                            for (int ii = 0; ii < zn.multin.length; ii++) {
                                                for (int jj = 0; jj < zn.multin[ii].coeffs.length; jj++) {
                                                    zn.multin[ii].coeffs[jj] = zn.multin[ii].coeffs[jj].toNumber(Ring.C64, new Ring("C64[p]"));
                                                }
                                            }
                                            System.out.println("zn _" + i + "  _" + zn.toString(ring));
                                            Element[] solv = primeFractionForSingle(ch, zn, ring.numberONE(), ring);
                                            System.out.println("solv_" + i + " _" + Array.toString(solv));
                                            zn.deletZeroCoeffsInMultins(ring);
                                            arr_SP.add(new SumOfProduct(solv, zn, ring));
                                        } else {
                                            arr_SP.add(p[i].multin.get(0));
                                        }
                                    }

                                } else {
                                    if (p[i].multin.get(0) instanceof FactorPol) {
                                        if (fp[0].multin.length == 1) {
                                            if (fp[1].multin.length == 1) {
                                                Polynom denom = fp[1].multin[0];
                                                for (int jj = 0; jj < denom.coeffs.length; jj++) {
                                                    denom.coeffs[jj] = denom.coeffs[jj].toNumber(Ring.C64, new Ring("C64[p]"));
                                                }
                                                FactorPol fpz = new FactorPol(new int[] {fp[0].powers[0], -1 * fp[1].powers[0]}, new Polynom[] {fp[0].multin[0], denom});
                                                if (!fpz.multin[0].isItNumber()) {

                                                    fpz.normalFormInField(ring);
                                                }

                                                arr_SP.add(fpz);
                                            } else {
                                                arr_SP.add(p[i].multin.get(0));
                                            }
                                        } else {
                                            arr_SP.add(p[i].multin.get(0));
                                        }
                                    } else {
                                        arr_SP.add(p[i].multin.get(0));
                                    }
                                }
                            } else {
                                arr_SP.add(p[i].multin.get(0));
                            }
                        }
                    } else {
                        arr_SP.add(p[i]);
                    }

                }
            }
        }
        SumOfProduct res = null;
        if (!(arr_SP.get(0) instanceof SumOfProduct)) {
            res = new SumOfProduct(arr_SP.get(0), ring);
        } else {
            res = (SumOfProduct) arr_SP.get(0);
        }
        for (int z = 1; z < arr_SP.size(); z++) {
            res = res.add(arr_SP.get(z), ring);
        }
        res.sorting(ring);
        res.normalForm(ring);
        return res;
    }

    public void clear(SumOfProduct[] s, Ring ring) {
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[i].sum.length; j++) {
                Product p = s[i].sum[j];
                if (p.multin.size() == 1) {
                    if (p.multin.get(0) instanceof FactorPol) {
                        FactorPol fp = (FactorPol) p.multin.get(0);
                        if (fp.multin.length != 1) {
                            FactorPol[] fp1 = fp.toNumDenom(fp);
                            Polynom pp = (Polynom)fp1[0].toPolynomOrFraction(ring);
                            Polynom[] ppp = new Polynom[fp1[1].multin.length + 1];
                            System.arraycopy(fp1[1].multin, 0, ppp, 0, fp1[1].multin.length);
                            ppp[ppp.length - 1] = pp;
                            int[] pow = new int[fp1[1].powers.length + 1];
                            System.arraycopy(fp1[1].powers, 0, pow, 0, fp1[1].powers.length);
                            pow[pow.length - 1] = 1;
                            FactorPol a = new FactorPol(pow, ppp);
                            a.normalForm(ring);
                            p.multin.set(0, a);
                        }
                    }
                }
            }
        }
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
            if (p.coeffs.length == 2 && p.powers[0] == 1) {
                if (p.coeffs[1] instanceof Complex) {
                    Complex c = (Complex) p.coeffs[1];
                    if (!c.im.isZero(ring)) {
                        coef1.add(p);
                        pow1.add(fp.powers[i]);
                    } else {
                        coef1.add(p);
                        pow1.add(fp.powers[i]);
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

    /**
     * Умножение комплексно-сопряженных полиномов
     *
     * @param coeffs - Массив полиномов
     * @param powers - Массив степеней полиномов
     * @param ring - кольцо
     *
     * @return
     */
    public FactorPol multiplySimplifyComplex(ArrayList<Polynom> coeffs, IntList powers, Ring ring) {
        ArrayList<Polynom> newCoeffs = new ArrayList<Polynom>();
        IntList newPowers = new IntList();
        if (coeffs.size() == 1) {
            newCoeffs.add(coeffs.get(0));
            newPowers.add(powers.arr[0]);
        } else {
            boolean conj = false;
            for (int i = 0; i < coeffs.size(); i++) {
                Polynom p = coeffs.get(i);
                for (int j = 0; j < coeffs.size(); j++) {
                    Polynom q = coeffs.get(j);
                    if ((p != null && q != null) && (i != j)) {
                        Complex c1 = (Complex) p.coeffs[1];
                        Complex c2 = (Complex) q.coeffs[1];
                        if (c1.im.abs(ring).isZero(ring) && c2.im.abs(ring).isZero(ring)) {
                            if (c1.re.abs(ring).equals(c2.re.abs(ring), ring)) {
                                if (c1.im.abs(ring).equals(c2.im.abs(ring), ring)) {
                                    if(powers.arr[i] == powers.arr[j]){
                                    conj = true;
                                    newCoeffs.add(p.multiply(q, ring).deleteZeroCoeff(ring));
                                    newPowers.add(1);
                                    coeffs.set(i, null);
                                    coeffs.set(j, null);
                                    }
                                }
                            }
                        } else {
                            if (c1.re.equals(c2.re, ring)) {
                                if (c1.im.abs(ring).equals(c2.im.abs(ring), ring)) {
                                    if(powers.arr[i] == powers.arr[j]){
                                    conj = true;
                                    newCoeffs.add(p.multiply(q, ring).deleteZeroCoeff(ring));
                                    newPowers.add(1);
                                    coeffs.set(i, null);
                                    coeffs.set(j, null);
                                    }
                                }
                            }
                        }
                    }
                }
                if (!conj) {
                    newCoeffs.add(p);
                    newPowers.add(powers.arr[i]);
                    coeffs.set(i, null);
                }
            }
        }
        Polynom[] coeffs1 = new Polynom[newCoeffs.size()];
        int[] powers1 = new int[newPowers.size];
        newCoeffs.toArray(coeffs1);
        System.arraycopy(newPowers.arr, 0, powers1, 0, powers1.length);
        return new FactorPol(powers1, coeffs1);
    }
}
