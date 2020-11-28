/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform;

import com.mathpar.func.*;
import java.util.Vector;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 * Класс содержит методы для решения линейных дифференциальных уравнений с
 * постоянными коэффициентами
 *
 * @author Rybakov Michail
 * @years 2009 - 2013
 * @version 3.0
 */
public class LDE extends F {

    public LDE() {
    }

    public LDE(F f) {
        this.name = f.name;
        this.X = f.X;
    }

    /**
     * Метод преобразования по Лапласу входной системы дифференциальных
     * уравнений
     *
     * @param f - дифференциальное уравнение
     *
     * @return - возвращает матрицу преобразования
     */
    public void systDifEquationToMatrix(Element f, Vector Y) {
        Ring ring = new Ring("C64[t]");
        Polynom p = new Polynom("p", ring);
        for (int i = 0; i < ((F) f).X.length; i++) {
            if (((F) f).X[i] instanceof Fname) {
                Y.add(new Polynom("1", ring));
            }
           else if (((F) f).X[i] instanceof Polynom) {
                Y.add(((Polynom) ((F) f).X[i]).coeffs[0]);
            }
          else if (((F) f).X[i] instanceof F) {
                if (((F) ((F) f).X[i]).name == F.d) {
                    if (((F) ((F) f).X[i]).X.length == 3) {
                        Y.add(new Polynom(new int[] {((F) ((F) f).X[i]).X[2].intValue()}, new Element[] {NumberR64.ONE}));
                    } else {
                        Y.add(new Polynom(new int[] {1}, new Element[] {NumberR64.ONE}));
                    }
                } else {
                    if (((F) ((F) f).X[i]).name == F.MULTIPLY) {
                        if (((F) ((F) f).X[i]).X.length == 2) {
                            Element a = ((F) ((F) f).X[i]).X[0];
                            Element b = ((F) ((F) f).X[i]).X[1];
                            if (!(a instanceof F)) {//Number в произведении
                                if (!(a instanceof Polynom)) {
                                    if (b instanceof F) {
                                        if (((F) b).name == F.d) {
                                            if (((F) b).X.length == 3) {
                                                if (((F) b).X[2] instanceof Polynom) {
                                                    Y.add(new Polynom(new int[] {((Polynom) ((F) b).X[2]).coeffs[0].intValue()},
                                                            new Element[] {NumberR64.ONE}).multiply(a, ring));
                                                } else {
                                                    Y.add(new Polynom(new int[] {((F) b).X[2].intValue()},
                                                            new Element[] {NumberR64.ONE}).multiply(a, ring));
                                                }
                                            } else {
                                                Y.add(new Polynom(new int[] {1}, new Element[] {NumberR64.ONE}).multiply(a, ring));
                                            }
                                        }
                                    } else {
                                        if (b instanceof Fname) {
                                            Y.add(a);
                                        }
                                    }
                                }
                            }
                        } else if (((F) ((F) f).X[i]).X[1] instanceof Fname) {
                            if (!(((F) ((F) f).X[i]).X[0] instanceof F)) {
                                Y.add(((Polynom) ((F) ((F) f).X[i]).X[0]));
                            }
                        } else if (((F) ((F) ((F) f).X[i]).X[1]).name == F.ID) {
                            if (((F) ((F) ((F) f).X[i]).X[1]).X[0] instanceof Fname) {
                                Y.add(((Polynom) ((F) ((F) f).X[i]).X[0]));
                            } else {
                            }
                        } else if (((F) ((F) f).X[i]).X[0] instanceof F) {
                            if (((F) ((F) ((F) f).X[i]).X[0]).name != F.ID) {
                                if (((F) ((F) ((F) f).X[i]).X[0]).name == F.d) {
                                    if (((F) ((F) ((F) f).X[i]).X[0]).X.length == 3) {
                                        Y.add(new Polynom(new int[] {((Polynom) ((F) ((F) ((F) f).X[i]).X[0]).X[2]).coeffs[0].intValue()},
                                                new Element[] {NumberR64.ONE}).multiply(((Polynom) ((F) ((F) ((F) f).X[i]).X[1]).X[0]).coeffs[0], ring));
                                    } else {
                                        Y.add(new Polynom(new int[] {1}, new Element[] {NumberR64.ONE}).multiply(((Polynom) ((F) ((F) ((F) f).X[i]).X[1]).X[0]).coeffs[0], ring));
                                    }
                                }
                            }
                        } else {
                            if (((F) ((F) ((F) f).X[i]).X[1]).X.length == 3) {
                                Y.add(new Polynom(new int[] {((F) ((F) ((F) f).X[i]).X[1]).X[2].intValue()},
                                        new Element[] {NumberR64.ONE}).multiply(((Polynom) ((F) ((F) f).X[i]).X[0]).coeffs[0], ring));
                            } else {
                                Y.add(new Polynom(new int[] {1}, new Element[] {NumberR64.ONE}).multiply(((Polynom) ((F) ((F) f).X[i]).X[0]).coeffs[0], ring));
                            }
                        }
                    }
                    if (((F) ((F) f).X[i]).name == F.ADD || ((F) ((F) f).X[i]).name == F.SUBTRACT) {//обработка + \ -
                        systDifEquationToMatrix(((F) ((F) f).X[i]), Y);
                    }
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
    public Element DifEquation(F f, Ring ring) {
        Element dif = Polynom.polynom_zero(ring.numberONE);//0
        if (f instanceof F) {
            if (f.name == SYSTLDE) {
                Vector<Element> Y = new Vector<Element>();//по Y
                try {
                    F fff = ring.CForm.ExpandForYourChoise(((F) f.X[0]), 1, 1, 1, -1, 1);
                    systDifEquationToMatrix(fff, Y);
                } catch (Exception ex) {
                    ring.exception.append(
                            "Exeption in LDE in package LaplaceTransform: " + ex);
                }
                //суммирование элементов в векторах
                Element[] y = new Element[Y.size()];
                Y.copyInto(y);
                for (int l = 0; l < y.length; l++) {
                    dif = dif.add(y[l], ring);
                }
            }
        }
        return dif;
    }


    /**
     * разбор начальных условий по массивам их значений
     *
     * @param k - количество переменных по которым есть дифференциалы в исходной
     * системе уравнений
     * @param f - функция начальных условий
     *
     * @return
     */
    public Element[] arrayInitCond(F f, Ring ring) {
        Vector<Element> Y = new Vector<Element>();
        for (int i = 0; i < f.X.length; i++) {
            if (f.X[i] instanceof F) {
                if (((F) f.X[i]).name == F.d) {
                    if (f.X[i + 1] instanceof Polynom) {
                        if (((Polynom) f.X[i + 1]).isZero(ring)) {
                            Y.add(NumberR64.ZERO);
                        } else {
                            Y.add(((Polynom) f.X[i + 1]).coeffs[0]);
                        }
                    } else {
                        if (f.X[i + 1].isZero(ring)) {
                            Y.add(NumberR64.ZERO);
                        } else {
                            Y.add(f.X[i + 1]);
                        }
                    }
                }
            }
        }
        Element[] y = new Element[Y.size()];
        Y.copyInto(y);
        return y;
    }

    /**
     * Подстановка начальных условий в операторное уравнение
     *
     * @param pol - операторное уравнение
     * @param pols - массив значений начальных условий
     *
     * @return
     */
    public Element polDifEquationInitialCond(Element polOfEquation, Element[] pols, Ring ring) {
        Polynom pol = ((Polynom) polOfEquation);
        if (pol.powers.length == 0) {
            return Polynom.polynom_zero(NumberR64.ONE);
        }
        Polynom[] p = new Polynom[pols.length];
        Element result = Polynom.polynom_zero(NumberR64.ONE);
        Polynom zero = Polynom.polynom_zero(NumberR64.ONE);
        for (int i = 0; i < pols.length; i++) {
            int[] n;
            Element[] m;
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
            for (int j = 0; j < n.length; j++) {
                if (n[j] == -1) {
                    n[j] = 0;
                }
            }
            for (int j = 0; j < m.length; j++) {
                m[j] = pol.coeffs[j];
            }
            if (m.length == 0) {
                result = (Polynom) result.add(pol.coeffs[0].multiply(pols[i], ring), ring);
                break;
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
                result = result.add(p[i].multiply(pols[i], ring), ring);
            }
        }
        return (result instanceof Polynom)
                ? ((Polynom) result).ordering(0, 0, ((Polynom) result).coeffs.length, ring) : result;
    }

    /**
     * Преобразует по прямому преобразованию Лапласа правую часть входного
     * дифференциального уравнения
     *
     * @param f - входное диффернциальное уравнение
     *
     * @return - массив функций, каждая является результатом прямого
     * преобразования Лапласа
     */
    public Element vectorLaplaceTransform(F f, Ring ring) {
        return new LaplaceTransform().transform(f.X[1], ring);
    }

    /**
     * Построение списка функций для обратного преобразования Лапласа
     *
     * @param fp - FactorPol - определитель матрицы
     * @param el - массив неопределенных коэффициентов
     * @return - список функций
     */
    public Element[] solveFractions(FactorPol fp, Element[] el) {
        Element[] res = new Element[el.length];
        int i = 0;
        int n = 0;
        for (int j = 0; j < fp.multin.length; j++) {
            int k = 0;
            for (int h = 0; h < fp.powers[j]; h++) {
                if (!el[i].isZero(Ring.ringZxyz)) {//проверка нуля
                    res[i] = new F(F.DIVIDE, new Element[] {el[i], new F(F.intPOW, new Element[] {fp.multin[j], new NumberR64(k + 1)})});
                    n++;
                }
                k++;
                i++;
            }
        }
        Element[] result = new Element[n];
        int w = 0;
        for(int q = 0; q < res.length; q++){
            if(res[q] != null){
                result[w] = res[q];
                w++;
            }
        }
        return result;
    }

    /**
     * Преобразуем входной полином числителя в полином от одной переменной и с
     * целыми коэффициентами
     *
     * @param p
     *
     * @return
     */
    public Polynom cH(Polynom p, Ring ring) {
        Polynom polToFact = new Polynom();
        if ((p.coeffs.length != 0) && (p.powers.length != 0)) {
            int[] powers1 = new int[p.coeffs.length]; //новый массив для хранения степеней полиномов
            int k = 0;
            for (int a = 0; a < p.powers.length; a++) {
                if (p.powers[a] != 0) {
                    powers1[k] = p.powers[a];
                    k++;
                }
            }
            polToFact = new Polynom(powers1, p.coeffs); //формируем новый полином из массива новых степеней и массива коэффициентов старого полинома
            for (int fgh = 0; fgh < polToFact.coeffs.length; fgh++) {
                polToFact.coeffs[fgh] = polToFact.coeffs[fgh].toNumber(Ring.Z, ring);
            }
        } else {
            return p;
        }
        return polToFact;
    }

    /**
     * Создаем Fraction из функциональной дроби
     *
     * @param f
     * @param nb
     *
     * @return
     */
    public Fraction fr(F f) {
        Fraction result = new Fraction();
        result.num = (Polynom) f.X[0]; //new Polynom (ff.X[0].toString());
        result.denom = (Polynom) f.X[1]; //new Polynom ( ff.X[1].toString());
        return result;
    }

    /**
     * Возвращает simplify F
     *
     * @param a - объект типа F, полученный после подстановки начальных условий
     * в общем виде
     * @param ring
     *
     * @return
     */
    public Element f_toCH(Element a, Ring ring) {
        if (a instanceof F) {
            return  ring.CForm.Univers_Expand(((F) a), true);
        } else {
            return a;
        }
    }

    /**
     * Упрощение правой части диф-ого уравнения
     *
     * @param l
     *
     * @return
     */
    public Element rightPathSolve(Element l, Ring ring) throws Exception {
        if ((l instanceof F) && ((((F) l).name == F.ADD) || (((F) l).name == F.SUBTRACT))) {
            if (((F) l).X.length > 1) {
                Fraction result = new LDE().fr(((F) ((F) l).X[0]));
                for (int i = 1; i < ((F) l).X.length; i++) {
                    Fraction x = new LDE().fr(((F) ((F) l).X[i]));
                    Element Eh = result.add(x, ring);
                    if (Eh instanceof Fraction) {
                        result = (Fraction) Eh;
                    } else {
                        result = new Fraction(Eh, ring.numberONE);
                    }
                }
                return result.cancel(ring);
            }
        } else {
            return new LDE().fr(((F) l));
        }
        return null;
    }

    /**
     * Процедура решения линейного дифференциального уравнения с постоянными
     * коэффициентами
     *
     * @param dif - входное дифференциальное уравнение
     * @param inC - начальные условия для дифференциального уравнения
     * @param ring - кольцо от переменной t
     *
     * @return - Возвращает решение в виде объекта типа - F
     *
     * @throws Exception
     */
    public Element solveDifEquation(F dif, F inC, Ring ring) throws Exception {
        Ring rrr = new Ring("R64[p]",ring);  
        F difEquations = dif;//дифференциальное уравнений
        F initCondEquations = inC;//начальные  условия
        Element f = new LDE().DifEquation(difEquations, ring);
        //System.out.println("Операторное уравнение: " + f.toString(rrr));
        Element[] initCond = new LDE().arrayInitCond(initCondEquations, ring);//массив начальных значений
        //System.out.println("Начальные  значения: " + Array.toString(initCond, rrr));
        Element polInitCond = new LDE().polDifEquationInitialCond(f, initCond, ring);//полином начальных значений
        //System.out.println("Полином начальных условий: " + polInitCond.toString(ring));
        Element rightPat = new LDE().vectorLaplaceTransform(difEquations, ring);
        //System.out.println("Правая часть преобразованная по прямому преобразованию Лапласа: " + rightPat.toString(ring));
        Element resRightPath = new LDE().rightPathSolve(rightPat, ring);
        //System.out.println("Правая часть преобразованная по прямому преобразованию Лапласа после упрощения " + resRightPath.toString(ring));
        Fraction pol = new Fraction(polInitCond, ring);//полином нач условий
        //проверка на ноль
        Fraction simplPol = new Fraction();
        if (((Fraction) resRightPath).num.isZero(ring)) {
            simplPol = pol;
        } else {
            //   f1 = ffff.add(((Fraction) resRightPath), ring);
            Element Eh = pol.add(((Fraction) resRightPath), ring);
            if (Eh instanceof Fraction) {
                simplPol = (Fraction) Eh;
            } else {
                simplPol = new Fraction(Eh, ring.numberONE);
            }
        }
        Polynom p = new Polynom();
        if (simplPol.denom.ExpandFnameOrId() instanceof Polynom) {
            p = (Polynom) simplPol.denom.ExpandFnameOrId();
        } else {
            p = Polynom.polynomFromNumber(simplPol.denom.ExpandFnameOrId(), ring);
        }
        p = ((Polynom) f).multiply(p, ring);
        //работа с числителем
        Element num = null;//числитель будущей дроби
        if (((Fraction) simplPol).num instanceof Polynom) {
            num = new LDE().cH(((Polynom) ((Fraction) simplPol).num), ring);
        } else {
            num = new LDE().f_toCH(((Fraction) simplPol).num, ring);
        }
        //System.out.println("Числитель: " + num.toString(rrr));//полный характеристический полином
        int[] powers1 = new int[p.powers.length]; //новый массив для хранения степеней полиномов
        int k = 0;
        for (int i = 0; i < p.powers.length; i++) {
            if (p.powers[i] != 0) {
                powers1[k] = p.powers[i];
                k++;
            }
        }
        //формируем новый полином из массива новых степеней и массива коэффициентов старого полинома
        Polynom polToFact = new Polynom(powers1, p.coeffs);
        for (int q = 0; q < polToFact.coeffs.length; q++) {
            polToFact.coeffs[q] = polToFact.coeffs[q].toNumber(Ring.Z, ring);
        }
        FactorPol factor = polToFact.factorOfPol_inC(ring);
        // System.out.println("p после развала на простые сомножители... ->" +
        //                  factor.toString(nb.RING));
        //////////////////////////проверка что в знаменателе 1 полином 1 степени
        if ((factor.multin.length == 1) && (factor.powers.length == 1)) {
            if (factor.powers[0] == 1) {
                F f_L = new F(F.DIVIDE, new Element[] {num, factor.multin[0]});
          //      System.out.println("solve_difEquation = " + new InverseLaplaceTransform().inverseLaplaceTransform(f_L, ring));
                return new InverseLaplaceTransform().inverseLaplaceTransform(f_L, ring);
            }
        }
        Element[] resDet = new SystemLDE().primeFractionForSingle(num, factor, ring);//возвращает вектор решений
        //System.out.println("resDet[] = " + Array.toString(resDet));
        if (factor.multin[0].coeffs.length == 1 && factor.multin[0].powers.length == 0) {//если первый полином в FactorPol - есть число ...
            int[] pow = new int[factor.powers.length - 1];
            Polynom[] mul = new Polynom[factor.multin.length - 1];
            for (int j = 1; j < factor.multin.length; j++) {
                pow[j - 1] = factor.powers[j];
                mul[j - 1] = factor.multin[j];
            }
            factor = new FactorPol(pow, mul);
        }
        Element[] fDet = new LDE().solveFractions(factor, resDet);
//        System.out.println("  ----- разложенный det в виде FractionSum" +
        //                         Array.toString(fDet)); //вывод разложенного на суммы детерминанта
        F[] fArray = new F[fDet.length];
        for (int w = 0; w < fDet.length; w++) {
            try {
                    fArray[w] = new InverseLaplaceTransform().inverseLaplaceTransform(fDet[w], ring);
            } catch (Exception ex) {
                ring.exception.append(
                        "Exeption in LDE in package LaplaceTransform: " + ex);
            }
        }
        VectorS result = new VectorS(new Element[]{new F(F.ADD, fArray)});
        //System.out.println("solveLDE = " + new F(F.ADD, fArray).toString(ring));
        return result;//new F(F.ADD, fArray);
    }
}
