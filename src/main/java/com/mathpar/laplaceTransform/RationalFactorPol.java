/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform;

import com.mathpar.polynom.*;
import com.mathpar.number.*;
//import factorpol.*;

/**
 * Класс отвечающий за дроби представленные в виде: числитель-некоторый Polynom,
 * знаменатель - некоторый FactorPol
 *
 * @author Rybakov Michail
 * @version 1.0
 */
public class RationalFactorPol {

    Ring r = new Ring();
    /**
     * Числитель дроби
     */
    public Polynom num;
    /**
     * Знаменатель дроби
     */
    public FactorPol denom;

    public RationalFactorPol() {
    }

    /**
     * создает из Polynom и FactorPol один объект - RationalFactorPol
     *
     * @param numerator Polynom
     * @param denumerator FactorPol
     */
    public RationalFactorPol(Polynom numerator, FactorPol denumerator) {
        this.num = numerator;
        this.denom = denumerator;
    }

    /**
     * Преобразовывает дробь в строку String
     */
    public String toString() {
        String s1 = num.toString();
        String s2 = denom.toString();
        String S = "[" + num.toString() + "]" + "/" + "[" + denom.toString()
                + "]";
        return S;
    }

//процедура поиска в знаменателе совпадающих полиномов, с пересчетом степеней...
    public RationalFactorPol[][] simplify(RationalFactorPol[][] Input) {
        //поиск и обнуление
        for (int i = 0; i < Input.length; i++) {
            for (int j = 0; j < Input[i].length; j++) {
                for (int k = 0; k < Input[i][j].denom.multin.length - 1; k++) {
                    if (Input[i][j].denom.multin[k] != null) {
                        if (Input[i][j].denom.multin[k].equals(Input[i][j].denom.multin[k + 1])) {
                            Input[i][j].denom.powers[k] = Input[i][j].denom.powers[k] + Input[i][j].denom.powers[k + 1];
                            Input[i][j].denom.multin[k + 1] = null;
                            Input[i][j].denom.powers[k + 1] = 0;
                        }
                    }
                }
            }
        }
        //создание после упрощения
        int[][] len = new int[Input.length][];
        int[][] powers = new int[Input.length][];
        for (int i = 0; i < Input.length; i++) {
            len[i] = new int[Input[i].length];
            powers[i] = new int[Input[i].length];
            for (int j = 0; j < Input[i].length; j++) {
                for (int k = 0; k < Input[i][j].denom.multin.length; k++) {
                    if (Input[i][j].denom.multin[k] != null) {
                        len[i][j]++;
                    }
                    if (Input[i][j].denom.powers[k] != 0) {
                        powers[i][j]++;
                    }
                }
            }
        }
        RationalFactorPol[][] Output = new RationalFactorPol[Input.length][];
        for (int i = 0; i < Input.length; i++) {
            Output[i] = new RationalFactorPol[Input[i].length];
            for (int j = 0; j < Input[i].length; j++) {
                Polynom[] pol = new Polynom[len[i][j]];
                int[] pow = new int[powers[i][j]];
                int t = 0; //счетчик
                for (int k = 0; k < Input[i][j].denom.multin.length; k++) {
                    if (Input[i][j].denom.multin[k] != null) {
                        pol[t] = Input[i][j].denom.multin[k];
                    }
                    if (Input[i][j].denom.powers[k] != 0) {
                        pow[t] = Input[i][j].denom.powers[k];
                    }
                    t++;
                }
                FactorPol fr = new FactorPol(pow, pol);
                Output[i][j] = new RationalFactorPol(Input[i][j].num, fr);
            }
        }
        for (int i = 0; i < Output.length; i++) {
            for (int j = 0; j < Output[i].length; j++) {
                if (j != Output[i].length - 1) {
                    System.out.print(Output[i][j].toString() + "+");
                } else {
                    System.out.print(Output[i][j].toString());
                }
            }
            System.out.println();
        }
        return Output;
    }

    //процедура умножения FractionSum на объект RationalFactorPol
    public RationalFactorPol[] multiplyToFractionSum(RationalFactorPol R,
            FractionSum F, Ring ring) {
        int length = F.coeff.length; //длина
        RationalFactorPol[] result = new RationalFactorPol[length];
        Polynom pol;
        int g;
        int d = 0;//счетчик по коэффициентам
        for (int i = 0; i < F.arg.multin.length; i++) {
            int t = 0;
            for (int j = 0; j < F.arg.powers[i]; j++) {
                FactorPol fr;
                Polynom p = R.num.multiplyByNumber(F.coeff[d], ring); //перемножаем числители
                for (int k = 0; k < R.denom.multin.length; k++) {
                    if (F.arg.multin[i].equals(R.denom.multin[k])) { //сравниваем
                        t++;
                    }
                }
                if (t == 0) {
                    pol = F.arg.multin[i];
                    g = F.arg.powers[i];
                    Polynom[] p1 = new Polynom[R.denom.multin.length + t];
                    int[] w1 = new int[R.denom.powers.length + t];
                    System.arraycopy(R.denom.multin, 0, p1, 0,
                            R.denom.multin.length);
                    System.arraycopy(R.denom.powers, 0, w1, 0,
                            R.denom.powers.length);
                    p1[p1.length - 1] = pol;
                    w1[w1.length - 1] = g;
                    fr = new FactorPol(w1, p1);
                } else {
                    Polynom[] p1 = new Polynom[R.denom.multin.length];
                    int[] w1 = new int[R.denom.powers.length];
                    System.arraycopy(R.denom.multin, 0, p1, 0,
                            R.denom.multin.length);
                    System.arraycopy(R.denom.powers, 0, w1, 0,
                            R.denom.powers.length);
                    for (int k = 0; k < R.denom.multin.length; k++) {
                        if (F.arg.multin[i].equals(R.denom.multin[k])) { //сравниваем
                            w1[k] = R.denom.powers[k] + j + 1;
                        }
                    }
                    fr = new FactorPol(w1, p1);
                }
                result[d] = new RationalFactorPol(p, fr);
                d++;
            }
        }
        return result;
    }
}
