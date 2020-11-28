/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import java.util.ArrayList;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 * @author Yulya Matveeva forever!
 *
 */
public class Sequence extends F {

    /**
     * Метод, находящий максимальную степень в полиноме. На входе полином.
     * Возвращает старшую степень
     */
    public static int foundMaxPower(Polynom p) {
        int maxPower = 0;
        int lenPowers = p.powers.length;
        for (int i = 0; i < lenPowers; i++) {
            if (p.powers[i] > maxPower) {
                maxPower = p.powers[i];
            }
        }
        return maxPower;
    }

    /**
     * Метод вычисляет предел на бесконечности последовательности,
     * представляющей собой полином. На входе полином, возвращает объект типа
     * Element. Если полином представляет собой число, т.е. длина массива
     * коэффициентов равна 0, то возвращает само число. Если же длина массива
     * полиномов не 0, тогда возвращает POSITIVE_INFINITY, если старший
     * коэффициент положительный, и NEGATIVE_INFINITY, если старший коэффициент
     * отрицательный.
     *
     */
    public static Element limitOfPolynom(Polynom pol) {
        Element pCoeff0 = ((Polynom) pol).coeffs[0];
        if (pol.isItNumber()) {
            return pol;
        } else {
            if (pCoeff0.signum() == -1) {
                return NumberR.NEGATIVE_INFINITY;
            } else {
                return NumberR.POSITIVE_INFINITY;
            }
        }
    }

    /**
     * Метод вычисляет предел на бесконечности последовательности, предтавляющей
     * собой сумму радикалов разных знаков. На входе f типа F. Возвращает предел
     * последовательности типа Element
     */
    public static Element limit(F f, Ring ring) {

        Element[] x = f.X; // все радикалы
        int len = x.length; // число радикалов
        ArrayList<Element[]> ArMas = new ArrayList<Element[]>();// создаем ArrayList
        //Element[] temp = new Element[4];
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                if (((F) x[i]).X[0] instanceof Polynom) {
                    Element num = ((F) ((F) x[i]).X[1]).X[0];
                    Element denom = ((F) ((F) x[i]).X[1]).X[1];
                    Element[] temp = new Element[4];
                    temp[0] = ((Polynom) ((F) x[i]).X[0]); //содержит все полиномы
                    temp[1] = ((Polynom) temp[0]).coeffs[0]; //содержит все старшие коэффициенты
                    temp[2] = new Fraction(num, denom); //содержит дробные степени
                    temp[3] = NumberZ.ONE; //содержит знвки перед радикалами
                    ArMas.add(temp);
                } else {
                    int len2 = ((F) x[i]).X.length;
                    for (int j = 0; j < len2; j++) {
                        Element num = ((F) ((F) ((F) x[i]).X[j]).X[1]).X[0];
                        Element denom = ((F) ((F) ((F) x[i]).X[j]).X[1]).X[1];
                        Element[] temp = new Element[4];
                        temp[0] = ((Polynom) ((F) ((F) x[i]).X[j]).X[0]); //содержит все полиномы
                        temp[1] = ((Polynom) temp[0]).coeffs[0]; //содержит все старшие коэффициенты
                        temp[2] = new Fraction(num, denom); //содержит дробные степени
                        temp[3] = NumberZ.ONE; //содержит знвки перед радикалами
                        ArMas.add(temp);
                    }
                }
            } else { // если радикал умножается на число (на -1)
                if (((F) x[i]).X[0] instanceof Polynom) {
                    Element num = ((F) ((F) x[i]).X[1]).X[0];
                    Element denom = ((F) ((F) x[i]).X[1]).X[1];
                    Element[] temp = new Element[4];
                    temp[0] = ((Polynom) ((F) x[i]).X[0]); //содержит все полиномы
                    temp[1] = ((Polynom) temp[0]).coeffs[0]; //содержит все старшие коэффициенты
                    temp[2] = new Fraction(num, denom); //содержит дробные степени
                    temp[3] = NumberZ.MINUS_ONE; //содержит знвки перед радикалами
                    ArMas.add(temp);
                } else {
                    int len2 = ((F) x[i]).X.length;
                    for (int j = 0; j < len2; j++) {
                        Element num = ((F) ((F) ((F) x[i]).X[j]).X[1]).X[0];
                        Element denom = ((F) ((F) ((F) x[i]).X[j]).X[1]).X[1];
                        Element[] temp = new Element[4];
                        temp[0] = ((Polynom) ((F) ((F) ((F) x[i])).X[j]).X[0]); //содержит все полиномы
                        temp[1] = ((Polynom) temp[0]).coeffs[0]; //содержит все старшие коэффициенты
                        temp[2] = new Fraction(num, denom); //содержит дробные степени
                        temp[3] = NumberZ.MINUS_ONE;//все радикалы положиетльные, т.е. умножаются на 1
                        ArMas.add(temp);
                    }
                }
            }
            /* добавляем в ArrayList массив элементов, содержащий полиномы,
             страшие коэффициенты полиномов, дробные степени радикалов,
             знаки радикалов*/
        }
        Element[] temp = new Element[4];
        temp = ArMas.get(1);
        Element answer = NumberZ.ZERO;
        while (!(answer instanceof NumberR)) {
            if (ArMas.isEmpty()) {
                return answer;
            }
            answer = limitF(ArMas, ring);
        }
        return answer;
    }

    /**
     * Метод, вызываемый методом limitF0. На входе получает массив полиномов,
     * массив дробных степеней, массив старших коэффициентов полиномов, знаки
     * радикалов, количество радикалов и старшую степень среди всех радикалов и
     * полиномов, найденную в предыдущем методе
     */
    public static Element limitF(ArrayList<Element[]> ArMas, Ring ring) {
        // Element rrrr=ArMas.get(0)[2].multiply(new Fraction(new NumberZ(((Polynom)ArMas.get(0)[0]).powers[0]),Fraction.Z_ONE), ring);
        ArrayList<Element[]> newArMas = new ArrayList<Element[]>();
        // Arrays.sort(ArMas.get(0)[2].multiply(new Fraction(new NumberZ(((Polynom)ArMas.get(0)[0]).powers[0]),Fraction.Z_ONE), ring));
        boolean tt = true;
        while (tt) {
            tt = false;
            for (int i = 0; i < ArMas.size() - 1; i++) {
                //  Element t = ArMas.get(i)[2].multiply(new Fraction(new NumberZ(((Polynom) ArMas.get(i)[0]).powers[0]), Fraction.Z_ONE), ring);
                Element r1 = ArMas.get(i)[2].multiply(new Fraction(new NumberZ(((Polynom) ArMas.get(i)[0]).powers[0]), Fraction.Z_ONE), ring);
                Element r2 = ArMas.get(i + 1)[2].multiply(new Fraction(new NumberZ(((Polynom) ArMas.get(i + 1)[0]).powers[0]), Fraction.Z_ONE), ring);
                if (r1.compareTo(r2, ring) == -1) {
                    Element[] rr1 = ArMas.get(i);
                    Element[] rr2 = ArMas.get(i + 1);
                    ArMas.remove(i);
                    ArMas.remove(i);
                    ArMas.add(i, rr2);
                    ArMas.add(i + 1, rr1);
                    tt = true;
                }
            }
        }

        IntList indexHP = new IntList();
        NumberZ z2 = new NumberZ(2); // создаем 2 типа NumberZ
        Element one = ring.numberONE;
        Element zero = ring.numberZERO;
        boolean nan = false;
        Element hightPow = Fraction.Z_ZERO;
        int len = ArMas.size();
        Element[] temp = new Element[4];
        /* проверяем, не извлекается ли корень четной степени из -infinity
         при x->infinity под корнем */
        for (int i = 0; i < len; i++) {
            temp = ArMas.get(i);
            if ((temp[2] instanceof Fraction) || (temp[2] instanceof NumberZ)) {
                if ((temp[1].compareTo(zero, ring) == -1) && ((((Fraction) temp[2]).denom).mod(z2, ring)).isZero(ring)) {
                    nan = true;
                }
            }
            /* создаем общую старшую степень-произведение рациональной степени и
             степени полинома */
            //   Element hP = temp[2].multiply(new NumberZ(((Polynom) temp[0]).powers[0]),ring);
            Element hP = new Fraction(((Fraction) temp[2]).num.multiply(new NumberZ(((Polynom) temp[0]).powers[0]), ring), ((Fraction) temp[2]).denom);
            if (hP.compareTo(hightPow, ring) == 1) {
                hightPow = hP;
                indexHP = new IntList();
                indexHP.add(i); // запоминаем индексы при старшей степени и накапливаем
            } else if (hP.compareTo(hightPow, ring) == 0) {
                indexHP.add(i);
            }
        }

        if (nan) {
            return NumberR.NAN;
        } // возвращает NAN, если выполняется условие
        else {
            // если старшая степень hightPow отлична от 0
            if (!hightPow.isZero(ring)) {
                int[] indexAr = indexHP.toArray();
                int lenA = indexAr.length; // число полиномов в старшей степени
                Polynom[] pH = new Polynom[lenA]; //полиномы в max степени
                Element[] qH = new Element[lenA]; //старшие степени этих полиномов
                Element[] cH = new Element[lenA]; //старшие коэффициенты
                Element[] pSH = new Element[lenA];// знак радикала

                // заполняем массивы полиномов, старших коэффициентов,
                for (int i = 0; i < lenA; i++) {
                    pH[i] = (Polynom) ArMas.get(indexAr[i])[0];
                    cH[i] = ArMas.get(indexAr[i])[1];
                    qH[i] = ArMas.get(indexAr[i])[2];
                    pSH[i] = ArMas.get(indexAr[i])[3];
                }
                for (int i = lenA - 1; i >= 0; i--) {
                    ArMas.remove(i);
                }
                /* если старший коэффициент под корнем отрицательный, то
                 выносим минус из-под радикала, умножив при этом полином на -1*/
                for (int i = 0; i < lenA; i++) {
                    if (cH[i].compareTo(zero, ring) == -1) {
                        pH[i] = pH[i].multiplyByNumber(NumberZ.MINUS_ONE, ring);
                        pSH[i] = pSH[i].multiply(NumberZ.MINUS_ONE, ring);
                    }
                }
//        Element one = Ring.oneOfMainRing();
//        Element zero = one.myZero();
                Fraction qP; // переменная, в которой будет хранится степень радикала
                Element sumCoeffs1 = NumberR64.ZERO;
                Element sumCoeffs2 = sumCoeffs1;
                /* в sumCoeffs1 вычисляется сумма старших коэффициентов полиномов в
                 соответствующей им дробной степени "положительного" радикала
                 в sumCoeffs2 вычисляется сумма старших коэффициентов полиномов в
                 соответствующей им дробной степени "отрицательного" радикала*/
                for (int i = 0; i < lenA; i++) {
                    qP = new Fraction(((Fraction) hightPow).num.divide(new NumberZ(pH[i].powers[0]), ring), ((Fraction) hightPow).denom);
                    if (pSH[i] == NumberZ.ONE) {
                        sumCoeffs1 = sumCoeffs1.add(pH[i].coeffs[0].myPow(qP, ring), ring);
                    }
                    if (pSH[i] instanceof Polynom) {
                        if (pSH[i].equals(Polynom.polynomFromNumber(NumberZ.MINUS_ONE, ring))) {
                            sumCoeffs2 = sumCoeffs2.add(pH[i].coeffs[0].myPow(qP, ring), ring);
                        }
                    }
                    if ((pSH[i] == NumberZ.MINUS_ONE)) {
                        sumCoeffs2 = sumCoeffs2.add(pH[i].coeffs[0].myPow(qP, ring), ring);
                    }
                }
                // считаем разность sumCoeffs1 и sumCoeffs2
                sumCoeffs1 = sumCoeffs1.subtract(sumCoeffs2, ring);
                if (sumCoeffs1.compareTo(NumberR64.ZERO, ring) == 1) {
                    return NumberR.POSITIVE_INFINITY;
                }
                if (sumCoeffs1.compareTo(NumberR64.ZERO, ring) == -1) {
                    return NumberR.NEGATIVE_INFINITY;
                }
                if (sumCoeffs1.compareTo(NumberR64.ZERO, ring) == 0) {
                    Fraction hightPowQ = new Fraction(hightPow, ring);

                    // если старшая степень меньше единицы, но больше ноля
                    if ((hightPowQ.compareTo(Fraction.Z_ZERO, ring) == 1) && (hightPowQ.compareTo(Fraction.Z_ONE, ring) == -1)) {
                        return Fraction.Z_ZERO;
                    }
                    // если старшая степень единица
                    if (hightPowQ.compareTo(Fraction.Z_ONE, ring) == 0) {
                        Element sum1 = NumberR64.ZERO;
                        Element sum2 = sum1;
                        Element result = NumberR64.ZERO;
                        for (int i = 0; i < lenA; i++) {
                            qP = new Fraction(hightPow.divide(new NumberZ(pH[i].powers[0]), ring), ring);
                            Fraction degree = new Fraction(qP.cancel(ring), ring);
                            // для "положительных" радикалов
                            if (pSH[i] == NumberZ.ONE) {
                                Element a = degree.num;
                                Element b = degree.denom;
                                Element cd = degree.subtract(Fraction.Z_ONE, ring);
                                /* если полином не число и n-1 степень полинома строго
                                 меньше старшей n-ной степени полинома на 1*/
                                if ((pH[i].powers.length > 1) && (pH[i].powers[(pH[i].powers.length / pH[i].coeffs.length)] == pH[i].powers[0] - 1)) {
                                    sum1 = sum1.add(((cH[i].myPow(cd, ring)).multiply(pH[i].coeffs[1], ring)).multiply(a, ring).divide(b, ring), ring);
                                } else {
                                    sum1 = sum1.add(NumberR64.ZERO, ring);
                                }
                            }
                            if (pSH[i] instanceof Polynom) {
                                if (pSH[i].equals(Polynom.polynomFromNumber(NumberZ.MINUS_ONE, ring))) {
                                    Element a = degree.num;
                                    Element b = degree.denom;
                                    Element cd = degree.subtract(Fraction.Z_ONE, ring);
                                    if ((pH[i].powers.length > 1) && (pH[i].powers[(pH[i].powers.length / pH[i].coeffs.length)] == pH[i].powers[0] - 1)) {
                                        sum2 = sum2.add(((cH[i].myPow(cd, ring)).multiply(pH[i].coeffs[1], ring)).multiply(a, ring).divide(b, ring), ring);
                                    } else {
                                        sum2 = sum2.add(NumberR64.ZERO, ring);
                                    }
                                }
                            }
                            //для "отрицательных" радикалов
                            if ((pSH[i] == NumberZ.MINUS_ONE)) {
                                Element a = degree.num;
                                Element b = degree.denom;
                                Element cd = degree.subtract(Fraction.Z_ONE, ring);
                                if ((pH[i].powers.length > 1) && (pH[i].powers[(pH[i].powers.length / pH[i].coeffs.length)] == pH[i].powers[0] - 1)) {
                                    sum2 = sum2.add(((cH[i].myPow(cd, ring)).multiply(pH[i].coeffs[1], ring)).multiply(a, ring).divide(b, ring), ring);
                                } else {
                                    sum2 = sum2.add(NumberR64.ZERO, ring);
                                }
                            }
                        }
                        result = sum1.subtract(sum2, ring);
                        return result;
                    }

                    // если старшая степень больше единицы
                    if (hightPowQ.compareTo(Fraction.Z_ONE, ring) == 1) {
                        IntList indexHP2 = new IntList();
                        Polynom[] pH_After = new Polynom[lenA];
                        int hightPower2 = 0;
                        for (int i = 0; i < lenA; i++) {
                            /* отсекаем из каждого полинома одночлен в старшей степени
                             и создаем новые полиномы pH_After[i] */
                            pH_After[i] = pH[i].subtract((pH[i].getmonoms())[0], ring);
                            // находим старший полином в оставшихся частях полиномов
                            int k = Sequence.foundMaxPower(pH_After[i]);//
                            if (k > hightPower2) {
                                hightPower2 = k; // старшая степень нового полинома
                                indexHP2 = new IntList();
                                indexHP2.add(i);
                            } else if (k == hightPower2) {
                                indexHP2.add(i);
                            }
                        }
                        int[] indexAr2 = indexHP2.toArray();
                        int lenB = indexAr2.length;
                        Polynom[] pH2 = new Polynom[lenB];// "максимальные" полиномы
                        Element[] qH2 = new Element[lenB];// их коэффициенты
                        Element[] cH2 = new Element[lenB];
                        Polynom[] pH_After2 = new Polynom[lenB];
                        Element[] pSH2 = new Element[lenB];
                        for (int i = 0; i < lenB; i++) {
                            pH2[i] = pH[indexAr2[i]];
                            qH2[i] = qH[indexAr2[i]];
                            cH2[i] = pH2[i].coeffs[0];
                            pSH2[i] = pSH[indexAr2[i]];
                            pH_After2[i] = pH_After[i];
                        }
                        for (int i = 0; i < lenB; i++) {
                            if (cH2[i].compareTo(zero, ring) == -1) {
                                pH2[i] = pH2[i].multiplyByNumber(NumberZ.MINUS_ONE, ring);
                                pSH2[i] = pSH2[i].multiply(NumberZ.MINUS_ONE, ring);
                            }
                        }
                        Element sub = NumberR64.ZERO;
                        for (int i = 0; i < lenB; i++) {
                            /*находим разность между старшей степенью полинома и найденной
                             старшей степенью, после чего вычитаем из этого 1 - правило Лопиталя*/
                            Element powerSubtract = new Fraction(pH2[i].powers[0] - hightPower2 - 1);
                            // вычитаем из старшей степени единицу - правило Лопиталя
                            Element hightPowSub = hightPow.subtract(Fraction.Z_ONE, ring);
                            // вычитаем из  powerSubtract hightPowSub
                            sub = powerSubtract.subtract(hightPowSub, ring);
                        }
                        // если sub больше 0
                        if (sub.compareTo(Fraction.Z_ZERO, ring) == 1) {
                            return Fraction.Z_ZERO;

                        }
                        // если sub равен 0
                        if (sub.compareTo(Fraction.Z_ZERO, ring) == 0) {
                            Element sum1 = NumberR64.ZERO;
                            Element sum2 = sum1;
                            Element result = NumberR64.ZERO;
                            for (int i = 0; i < lenB; i++) {
                                qP = new Fraction(hightPow.divide(new NumberZ(pH[i].powers[0]), ring), ring);
                                Fraction degree = new Fraction(qP.cancel(ring), ring);
                                if (pSH2[i] == NumberZ.ONE) {
                                    Element a = degree.num;
                                    Element b = degree.denom;
                                    Element cd = degree.subtract(Fraction.Z_ONE, ring);
                                    if ((pH[i].powers.length > 1) && (pH[i].powers[(pH[i].powers.length / pH[i].coeffs.length)] == pH[i].powers[0] - 1)) {
                                        sum1 = sum1.add(((cH2[i].myPow(cd, ring)).multiply(pH2[i].coeffs[1], ring)).multiply(a, ring).divide(b, ring), ring);
                                    } else {
                                        sum1 = sum1.add(NumberR64.ZERO, ring);
                                    }
                                }
                                if (pSH2[i] == NumberZ.MINUS_ONE) {
                                    Element a = degree.num;
                                    Element b = degree.denom;
                                    Element cd = degree.subtract(Fraction.Z_ONE, ring);
                                    if ((pH[i].powers.length > 1) && (pH[i].powers[(pH[i].powers.length / pH[i].coeffs.length)] == pH[i].powers[0] - 1)) {
                                        sum2 = sum2.add(((cH2[i].myPow(cd, ring)).multiply(pH2[i].coeffs[1], ring)).multiply(a, ring).divide(b, ring), ring);
                                    } else {
                                        sum2 = sum2.add(NumberR64.ZERO, ring);
                                    }
                                }
                            }
                            result = sum1.subtract(sum2, ring);
                            return result;
                        }

                        // если sub меньше 0
                        if (sub.compareTo(Fraction.Z_ZERO, ring) == -1) {
                            Element sum1 = NumberR64.ZERO;
                            Element sum2 = sum1;
                            Element result;
                            for (int i = 0; i < lenB; i++) {
                                qP = new Fraction(hightPow.divide(new NumberZ(pH[i].powers[0]), ring), ring);
                                Fraction degree = new Fraction(qP.cancel(ring), ring);
                                if (pSH2[i] == NumberZ.ONE) {
                                    Element a = degree.num;
                                    Element b = degree.denom;
                                    Element cd = degree.subtract(Fraction.Z_ONE, ring);
                                    Element elem = new NumberZ(pH2[i].powers[0] - hightPower2);
                                    sum1 = sum1.add(((cH2[i].myPow(cd, ring)).multiply(pH2[i].coeffs[1], ring).
                                            multiply(elem, ring)).multiply(a, ring).
                                            divide(b, ring), ring);
                                }
                                if (pSH2[i] == NumberZ.MINUS_ONE) {
                                    Element a = degree.num;
                                    Element b = degree.denom;
                                    Element cd = degree.subtract(Fraction.Z_ONE, ring);
                                    Element elem = new NumberZ(pH2[i].powers[0] - hightPower2);
                                    sum2 = sum2.add(((cH2[i].myPow(cd, ring)).multiply(pH2[i].coeffs[1], ring).
                                            multiply(elem, ring)).multiply(a, ring).
                                            divide(b, ring), ring);
                                }
                            }
                            result = sum1.subtract(sum2, ring);
                            if (result.compareTo(NumberR64.ZERO, ring) == 1) {
                                return NumberR.POSITIVE_INFINITY;
                            }
                            if (result.compareTo(NumberR64.ZERO, ring) == -1) {
                                return NumberR.NEGATIVE_INFINITY;
                            }
                        }
                    }
                }
                return NumberR.ONE;
            }
        }
        return NumberR.ONE;
    }
}
