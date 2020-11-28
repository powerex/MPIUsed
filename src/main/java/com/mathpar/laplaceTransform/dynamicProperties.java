/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform;

import com.mathpar.func.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mathpar.matrix.*;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 * @author Rybakov Michail 2011 solution for the dynamic characteristics of
 * control objects
 */
public class dynamicProperties {
    public void systDifEquationToMatrix_1(Element f, Vector Y, Ring ring) {
        Ring r = new Ring("R64[p]",ring);
        Polynom p = new Polynom("p", r);
        for (int i = 0; i < ((F) f).X.length; i++) {
            Element Xi = ((F) f).X[i];
            if (Xi instanceof Polynom) {
                Y.add(((Polynom) Xi));
            }
            if (Xi.isItNumber()) {
                Y.add(Xi);
            }
            if (Xi instanceof F) {
                if (((F) Xi).name == F.d) {
                    ((F) Xi).X[0] = ((F) Xi).X[0].ExpandFnameOrId();
                }
            }
            if (Xi instanceof Fname) {
                if (((Fname) Xi).name.equals("y")) {
                    Y.add(Polynom.polynomFromNumber(ring.numberONE, ring));
                }
            } else {
                if (Xi instanceof F) {
                    if (((F) Xi).name == F.d) {
                        if (((Fname) ((F) Xi).X[0]).name.equals("y")) {
                            if (((F) Xi).X.length > 2) {
                                Y.add(new Polynom(new int[] {((F) Xi).X[2].intValue()}, new Element[] {NumberR64.ONE}));
                            } else {
                                Y.add(p);
                            }
                        }
                    } else {
                        if (((F) Xi).name == F.MULTIPLY) {
                            if (((F) Xi).X[0] instanceof F) {//случай когда в произведении стоят оба объекта типа F - (-1)*x
                                if (((F) Xi).X[1] instanceof F) {
                                    if (((Fname) ((F) ((F) Xi).X[1]).X[0]).name.equals("y")) {
                                        if (((F) ((F) Xi).X[0]).X[0] instanceof Polynom) {
                                            Y.add(((F) ((F) Xi).X[0]).X[0]);
                                        } else {
                                            Y.add(Polynom.polynomFromNumber(((F) ((F) Xi).X[0]).X[0], ring));
                                        }
                                    }
                                }
                            }
                            if (((F) Xi).X[0] instanceof Polynom) {
                                if (!(((F) Xi).X[1] instanceof F)) {
                                    if (((Fname) ((F) Xi).X[1]).name.equals("y")) {
                                        if (((F) Xi).X[0] instanceof Polynom) {
                                            Y.add(((F) Xi).X[0]);
                                        } else {
                                            Y.add(Polynom.polynomFromNumber(((F) Xi).X[0], ring));
                                        }
                                    }
                                } else {
                                    if (((F) ((F) Xi).X[1]).name == F.d) {
                                        if (((Fname) ((F) ((F) Xi).X[1]).X[0]).name.equals("y")) {
                                            if (((F) ((F) Xi).X[1]).X.length > 2) {
                                                Y.add(new Polynom(new int[] {((F) ((F) Xi).X[1]).X[2].intValue()}, new Element[] {NumberR64.ONE}).multiply(((F) Xi).X[0], ring));
                                            } else {
                                                Y.add(p.multiply(((F) Xi).X[0], ring));
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (!(((F) Xi).X[1] instanceof F)) {
                                    if (((Fname) ((F) Xi).X[1]).name.equals("y")) {
                                        if (((F) Xi).X[0] instanceof Polynom) {
                                            Y.add(((F) Xi).X[0]);
                                        } else {
                                            Y.add(Polynom.polynomFromNumber(((F) Xi).X[0], ring));
                                        }
                                    }
                                } else {
                                    if (((F) ((F) Xi).X[1]).name == F.d) {
                                        Element el = ((F) ((F) Xi).X[1]).X[0];
                                        if (el instanceof Fname) {
                                            if (((F) ((F) Xi).X[1]).X.length > 2) {
                                                Y.add(new Polynom(new int[] {((F) ((F) Xi).X[1]).X[2].intValue()}, new Element[] {NumberR64.ONE}).multiply(((F) Xi).X[0], ring));
                                            } else {
                                                Y.add(p.multiply(((F) Xi).X[0], ring));
                                            }
                                        } else if (((Fname) ((F) ((F) ((F) Xi).X[1]).X[0]).X[0]).name.equals("y")) {
                                            if (((F) ((F) Xi).X[1]).X.length > 2) {
                                                Y.add(new Polynom(new int[] {((F) ((F) Xi).X[1]).X[2].intValue()}, new Element[] {NumberR64.ONE}).multiply(((F) Xi).X[0], ring));
                                            } else {
                                                Y.add(p.multiply(((F) Xi).X[0], ring));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (((F) Xi).name == F.ADD || ((F) Xi).name == F.SUBTRACT) {//обработка + \ -
                        systDifEquationToMatrix(((F) ((F) f).X[i]), Y, ring);
                    }
                }
            }
        }
    }

    /**
     * Метод преобразования по Лапласу входного дифференциального уравнения
     *
     * @param f - система дифференциальных уравнений
     *
     * @return - возвращает матрицу преобразования
     */
    public void systDifEquationToMatrix(Element f, Vector Y, Ring ring) {
        Ring r = new Ring("R64[p]",ring);
        Polynom p = new Polynom("p", r);
        if (f instanceof F) {
            if (((F) f).name == F.MULTIPLY) {
                if (((F) f).X.length == 2) {
                    if (((F) f).X[0] instanceof Fname) {
                        if (!(((F) f).X[1] instanceof Polynom)) {
                            Y.add(Polynom.polynomFromNumber(((F) f).X[1], ring));
                        }
                    } else {
                        if (((F) f).X[1] instanceof Fname) {
                            if (!(((F) f).X[0] instanceof Polynom)) {
                                Y.add(Polynom.polynomFromNumber(((F) f).X[0], ring));
                            }
                        }
                    }
                } else {
                    systDifEquationToMatrix_1(f, Y, ring);
                }
            } else {
                if (((F) f).name == F.d) {
                    if (((Fname) ((F) f).X[0]).name.equals("y")) {
                        if (((F) f).X.length > 2) {
                            Y.add(new Polynom(new int[] {((F) f).X[2].intValue()}, new Element[] {NumberR64.ONE}));
                        } else {
                            Y.add(p);
                        }
                    }
                } else {
                    systDifEquationToMatrix_1(f, Y, ring);
                }
            }
        }
    }

    /**
     * Метод преобразования по Лапласу входного дифференциального уравнения
     *
     * @param f - система дифференциальных уравнений
     *
     * @return - возвращает матрицу преобразования
     */
    public Element DifEquation(F f, Ring ring) {
        Element dif = Polynom.polynom_zero(NumberR64.ONE);//0
        if (f instanceof F) {
            Vector<Element> Y = new Vector<Element>();//по Y
            Element ff = ring.CForm.ExpandForYourChoise(f, 1, 1, 1, -1, 1);
            //f.expand(1, 1, 1, 1, -1, 1, new Notebook(ring))
            systDifEquationToMatrix(ff, Y, ring);
            //суммирование элементов в векторах
            Element[] y = new Element[Y.size()];
            Y.copyInto(y);
            for (int l = 0; l < y.length; l++) {
                dif = dif.add(y[l], ring);
            }
        }
        return dif;
    }

    /**
     * Передаточная функция
     *
     * @param y
     * @param x
     *
     * @return
     */
    public F pFunc(F y, F x, Ring ring) {
        Polynom f1 = (Polynom) new dynamicProperties().DifEquation(y, ring);
        Polynom f2 = (Polynom) new dynamicProperties().DifEquation(x, ring);
        return new F(F.DIVIDE, new Element[] {f2, f1});
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
     * возвращает вектор решений
     *
     * @param ch Polynom
     * @param zn FactorPol
     *
     * @return Element[]
     *
     * @author Ribakov Mixail
     */
    public Element[] primeFractionCoeffitions(Polynom ch, FactorPol zn, Ring ring) throws
            Exception {
        if ((zn.multin.length == 1) && (zn.powers.length == 1)) {
            if ((zn.multin[0].coeffs.length == 1) && (zn.multin[0].powers.length == 1)) {
                if (zn.multin[0].powers[0] == 1) {
                    return new Element[] {ch};
                }
            }
        }
        Element delet = new Complex(1.0, 0.0); //присваеваем на первом этапе 1 коэффициент
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
                del = del.divideExact(z1.multin[a], ring);
                polnew[m + ds1] = del;
                ds2 = ds2 + 1;
            }
            ds1 = ds2;
        }
        int L = 0;
        if (ch.powers.length != 0) {
            L = maxPowersCH(ch); //величина старшей степени полинома в числителе
        } else {
            L = 0;
        }
        int power_max = polnew[0].powers[0]; //старшая степень среди полиномов (добавок)
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
        double[] b = new double[b_length]; //массив свободных коэффициентов
        int n1 = (int) b_length / 2;
        if (ch.powers.length != 0) {
            for (int m = ch.powers.length - 1; m >= 0; m--) { //вычисляем столбец свободных членов
                b[(b_length / 2 - 1) - ch.powers[m]] = ch.coeffs[m].doubleValue(); //+varstep
            }
        } else {
            for (int m = 1; m < b_length; m++) { //вычисляем столбец свободных членов
                b[m] = 0; //NumberC64.ZERO;
            }
            b[b_length / 2 - 1] = ch.coeffs[0].doubleValue(); //ch.coeffs[0].doubleValue();//NumberC64.ONE.toNumber(ch.coeffs[0].doubleValue());
        }
        double[][] matrix = new double[b_length][2 * n + 1]; //создание матрицы
        for (int q = 0; q < b_length; q++) { //заполняем последний столбец матрицы свободными членами
            matrix[q][2 * n] = b[q];
        }
        for (int h = 0; h < n; h++) { // заполняем матрицу
            for (int d = 0; d < polnew[h].coeffs.length; d++) {
                Complex comp = (Complex) polnew[h].coeffs[d].toNumber(Ring.C64, ring);
                if (polnew[h].powers.length == 0) {
                    matrix[power_max - d][h] = comp.re.doubleValue(); //A
                    matrix[power_max - d][h + n] = -comp.im.doubleValue(); //-B
                    matrix[power_max - d + n][h] = comp.im.doubleValue(); //B
                    matrix[power_max - d + n][h + n] = comp.re.doubleValue(); //A
                } else if (power_max >= polnew[h].powers.length) {
                    matrix[power_max
                            - polnew[h].powers[d]][h] = comp.re.doubleValue(); //A
                    matrix[power_max - polnew[h].powers[d]][h
                            + n] = -comp.im.doubleValue(); //-B
                    matrix[power_max - polnew[h].powers[d]
                            + n][h] = comp.im.doubleValue(); //B
                    matrix[power_max - polnew[h].powers[d] + n][h
                            + n] = comp.re.doubleValue(); //A
                } else if (power_max < polnew[h].powers.length) {
                    matrix[d][h] = comp.re.doubleValue(); //A
                    matrix[d][h + n] = -comp.im.doubleValue(); //-B
                    matrix[d + n][h] = comp.im.doubleValue(); //B
                    matrix[d + n][h + n] = comp.re.doubleValue(); //A
                }
            }
        }
        int k1 = matrix.length; //размерность матрицы
        Element[][] sc = new Element[k1][matrix[0].length];
        for (int i = 0; i < k1; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) { //=null
                    if (i != 0) {
                        sc[i][j] = sc[i - 1][j].zero(ring);
                    } else {
                        sc[i][j] = sc[i][j - 1].zero(ring);
                    }
                } else {
                    sc[i][j] = new NumberR64(matrix[i][j]);
                }
            }
        }
        MatrixS M1 = new MatrixS(sc, ring); //преобразование в матрицу над полиномами
        MatrixS M2 = M1.toEchelonForm(ring); //преобразование матрицы к виду - на главной диагонале стоят определители а в последнем столбце свободные члены
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
                solve[i] = M2.M[i][M2.M[i].length - 1].divide(M2.M[0][0], ring);
            } else {
                solve[i] = (NumberR64.ZERO).divide(M2.M[0][0], ring);
            }
        }
        Element[] solveM = new Element[solve.length / 2];
        for (int i = 0; i < solveM.length; i++) {
            solveM[i] = (new Complex(solve[i].doubleValue(),
                    solve[i + k1 / 2].doubleValue())).divide(
                    delet, ring);
        }
        return solveM;
    }

    /**
     * Возвращает преобразованный FactorPol//в случае если встречается (x-0):
     * преобразует к виду -(x)
     *
     * @return FactorPol
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
                    if (pow.length != 0) {
                        pow[k] = p.multin[i].powers[j];
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
     * Поиск статического коэффициента усиления K(0)=b_m/a_n
     *
     * @param f1
     * @param f2
     * @param ring
     *
     * @return
     */
    public Element solveStaticGain(F f1, F f2, Ring ring) {
        Polynom y = (Polynom) new dynamicProperties().DifEquation(f1, ring);
        Polynom x = (Polynom) new dynamicProperties().DifEquation(f2, ring);
        Element bm = (y.powers[y.powers.length - 1] == 0) ? y.coeffs[y.coeffs.length - 1] : ring.numberZERO;
        Element am = (x.powers[x.powers.length - 1] == 0) ? x.coeffs[x.coeffs.length - 1] : ring.numberZERO;
        if (am.isZero(ring)) {
            return ring.numberZERO;
        }
        if (bm.isZero(ring)) {
            return ring.numberZERO;
        }
        return bm.divide(am, ring);
    }

    /**
     * Функция веса
     *
     * @param y
     * @param x
     *
     * @return
     */
    public F kFunc(F f1, F f2, Ring ring) {
        Polynom y = (Polynom) new dynamicProperties().DifEquation(f1, ring);
        Polynom x = (Polynom) new dynamicProperties().DifEquation(f2, ring);
        ring = new Ring("R64[p]",ring);
        Polynom p = (Polynom) y;
        int[] powers1 = new int[p.coeffs.length]; //новый массив для хранения степеней полиномов
        int numVar = p.powers.length / p.coeffs.length; //переменная отвечающая за количество переменных
        int shift = numVar - 1; //сдвиг
        for (int a = 0; a < p.coeffs.length; a++) {
            powers1[a] = p.powers[(shift)]; //заполнение нового массива степеней
            shift += numVar;
        }
        Polynom polToFact = new Polynom(powers1, p.coeffs); //формируем новый полином из массива новых степеней и массива коэффициентов старого полинома
        for (int fgh = 0; fgh < polToFact.coeffs.length; fgh++) {
            polToFact.coeffs[fgh] = polToFact.coeffs[fgh].toNumber(Ring.Z, ring);
            //из NumberR64 в NumderZ
        }
        FactorPol fact = polToFact.factorOfPol_inC(ring);
        FactorPol detFactMNK = fact.prMNK(ring);
        if ((detFactMNK.multin.length == 1) && (detFactMNK.powers.length == 1)) {
            detFactMNK = withZero(detFactMNK, ring);
        }
        Polynom ch = (Polynom) x;//new Polynom("3");//
        Element[] resDet = new Element[0];
        try {
            resDet = new dynamicProperties().primeFractionCoeffitions(ch, detFactMNK, ring); //возвращает вектор решений
        } catch (Exception ex) {
            Logger.getLogger(dynamicProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
        if ((detFactMNK.multin[0].coeffs.length == 1) && (detFactMNK.multin[0].powers.length == 0)) { //если первый полином в FactorPol - есть число ...
            int[] pow = new int[detFactMNK.powers.length - 1];
            Polynom[] mul = new Polynom[detFactMNK.multin.length - 1];
            for (int ij = 1; ij < detFactMNK.multin.length; ij++) {
                pow[ij - 1] = detFactMNK.powers[ij];
                mul[ij - 1] = detFactMNK.multin[ij];
            }
            detFactMNK = new FactorPol(pow, mul);
        }
        Element[] fDet = new LDE().solveFractions(detFactMNK, resDet); //создание объекта типа суммы pol1/pol2 + pol3/pol4...
        Element[] fArray = new Element[fDet.length];
        for (int i = 0; i < fDet.length; i++) {
            fArray[i] = new InverseLaplaceTransform().inverseLaplaceTransform(fDet[i], ring);
            //fArray[i] = fArray[i].multiply(x, ring);
        }
        return new F(F.ADD, fArray);
    }

    /**
     * Переходная функция
     *
     * @param y
     * @param x
     *
     * @return
     */
    public F hFunc(F f2, F f1, Ring ring) {
        Polynom y = (Polynom) new dynamicProperties().DifEquation(f2, ring);
        Polynom x = (Polynom) new dynamicProperties().DifEquation(f1, ring);
        ring = new Ring("R64[p]",ring);
        Polynom p = (Polynom) y.multiply(new Polynom("p", ring), ring);
        int[] powers1 = new int[p.coeffs.length]; //новый массив для хранения степеней полиномов
        int numVar = p.powers.length / p.coeffs.length; //переменная отвечающая за количество переменных
        int shift = numVar - 1; //сдвиг
        for (int a = 0; a < p.coeffs.length; a++) {
            powers1[a] = p.powers[(shift)]; //заполнение нового массива степеней
            shift += numVar;
        }
        Polynom polToFact = new Polynom(powers1, p.coeffs); //формируем новый полином из массива новых степеней и массива коэффициентов старого полинома
        for (int fgh = 0; fgh < polToFact.coeffs.length; fgh++) {
            polToFact.coeffs[fgh] = polToFact.coeffs[fgh].toNumber(Ring.Z, ring);
            //из NumberR64 в NumderZ
        }
        FactorPol fact = polToFact.factorOfPol_inC(ring);//new NumberR64( 0.00000000000000001),
        FactorPol detFactMNK = fact.prMNK(ring);
        Polynom ch = (Polynom) x;//new Polynom("3");
        Element[] resDet = new Element[0];
        try {
            resDet = new dynamicProperties().primeFractionCoeffitions(ch, detFactMNK, ring); //возвращает вектор решений
        } catch (Exception ex) {
            Logger.getLogger(dynamicProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
        detFactMNK = new SystemLDE().withZero(detFactMNK, ring);
        detFactMNK.normalForm(ring);
        if (detFactMNK.multin[0].powers.length == 0) { //если первый полином в FactorPol - есть число ...
            int[] pow = new int[detFactMNK.powers.length - 1];
            Polynom[] mul = new Polynom[detFactMNK.multin.length - 1];
            for (int ij = 1; ij < detFactMNK.multin.length; ij++) {
                pow[ij - 1] = detFactMNK.powers[ij];
                mul[ij - 1] = detFactMNK.multin[ij];
            }
            detFactMNK = new FactorPol(pow, mul);
        }
        Element[] fDet = new LDE().solveFractions(detFactMNK, resDet); //создание объекта типа суммы pol1/pol2 + pol3/pol4...
        F[] fArray = new F[fDet.length];
        for (int i = 0; i < fDet.length; i++) {
            fArray[i] = new InverseLaplaceTransform().inverseLaplaceTransform(fDet[i], ring);
        }
        return new F(F.ADD, fArray);
    }

    public F afchx(F f1, F f2, Ring ring) {
        Polynom y = (Polynom) new dynamicProperties().DifEquation(f2, ring);
        Polynom x = (Polynom) new dynamicProperties().DifEquation(f1, ring);
        //ring = new Ring("C64[p]");
        ring = new Ring("Z[j,w]");
        Polynom p1 = null;
        Polynom p2 = null;
        if (y.powers.length != 0) {
            p1 = (Polynom) y.value(new Element[] {new Polynom("jw", ring)}, ring);
        } else {
            p1 = y;
        }
        if (x.powers.length != 0) {
            p2 = (Polynom) x.value(new Element[] {new Polynom("jw", ring)}, ring);
        } else {
            p2 = x;
        }
        return new F(F.DIVIDE, new Element[] {p2, p1});
    }

    /**
     * амплитудно-частотная характеристика
     *
     * @param y - выход динамической системы
     * @param x - вход динамической системы
     *
     * @return - возвращает действительную и мнимые части
     *
     * @throws com.mathpar.polynom.PolynomException
     * @throws com.mathpar.func.ContextException
     */
    public static Polynom[] solveReAndImFrequenceResponse(Polynom input, Ring ring) {
        Polynom res = input;
        int n = 1;
        int m = 0;
        Vector<Element> v1 = new Vector<Element>();
        Vector<Element> v2 = new Vector<Element>();
        int i = 0;
        if (res.powers.length != 0) {
            while (i + n - 1 < res.powers.length) {
                if (res.powers[i + n - 1] == 2 || res.powers[i + n - 1] == 4) {//i^2
                    res.powers[i + n - 1] = 0;
                    i = i + ring.varNames.length;
                    res.coeffs[m] = res.coeffs[m].multiply(ring.numberONE.minus_one(ring), ring);
                    m++;
                } else if (res.powers[i + n - 1] == 3) {
                    res.powers[i + n - 1] = 1;
                    i = i + ring.varNames.length;
                    res.coeffs[m] = res.coeffs[m].multiply(ring.numberONE.minus_one(ring), ring);
                    m++;
                } else {
                    i = i + ring.varNames.length;
                    m++;
                }
            }
        } else {
        }
        i = 0;
        m = 0;
        if (res.powers.length != 0) {
            while (i + n - 1 < res.powers.length) {
                int up = i;
                int down = i + n - 1;
                if (res.powers[i + n - 1] == 1) {
                    res.powers[i + n - 1] = 0;
                    int[] arr = new int[ring.varNames.length];
                    System.arraycopy(res.powers, up, arr, 0, arr.length);
                    Polynom p = new Polynom(arr, new Element[] {res.coeffs[m]});
                    v1.add(p);
                    i = i + ring.varNames.length;
                    m++;
                } else {
                    int[] arr = new int[ring.varNames.length];
                    System.arraycopy(res.powers, up, arr, 0, arr.length);
                    Polynom p = new Polynom(arr, new Element[] {res.coeffs[m]});
                    v2.add(p);
                    i = i + ring.varNames.length;
                    m++;
                }
            }
        } else {
            v2.add(res);
            v1.add(Polynom.polynom_zero(ring.numberONE));
        }
        Polynom sum1 = Polynom.polynom_zero(ring.numberONE);
        for (int k = 0; k < v2.size(); k++) {
            sum1 = (Polynom) sum1.add(v2.get(k), ring);
        }
        Polynom sum2 = Polynom.polynom_zero(ring.numberONE);
        for (int k = 0; k < v1.size(); k++) {
            sum2 = (Polynom) sum2.add(v1.get(k), ring);
        }
        return new Polynom[] {sum1, sum2};
    }

    /**
     * Q(w) = sqrt[(ac+bd)/c^2+d^2] P(w) = sqrt[(bc-ad)/c^2+d^2] A(w) =
     * sqrt[P(w)^2+Q(w)^2]:sqrt[(a^2+b^2)/(c^2+d^2)]
     *
     * @param p1
     * @param p2
     *
     * @return
     */
    public static F achx(F f, Ring ring) {
        Polynom[] p2 = new Polynom[] {};
        if (!(f.X[1] instanceof Polynom)) {
            p2 = dynamicProperties.solveReAndImFrequenceResponse(Polynom.polynomFromNumber(f.X[1], ring), new Ring("R64[j,w]"));
        } else {
            p2 = dynamicProperties.solveReAndImFrequenceResponse((Polynom) f.X[1], new Ring("R64[j,w]"));
        }
        System.out.println("[c,d] = " + Array.toString(p2, new Ring("R64[j,w]")));
        Polynom[] p1 = new Polynom[] {};
        if (!(f.X[0] instanceof Polynom)) {
            p1 = dynamicProperties.solveReAndImFrequenceResponse(Polynom.polynomFromNumber(f.X[0], ring), new Ring("R64[j,w]"));
        } else {
            p1 = dynamicProperties.solveReAndImFrequenceResponse((Polynom) f.X[0], new Ring("R64[j,w]"));
        }
        System.out.println("[a,b] = " + Array.toString(p1, new Ring("R64[j,w]")));
        Polynom num = p1[0].multiply(p1[0], ring).add(p1[1].multiply(p1[1], ring), ring);
        Polynom denum = p2[0].multiply(p2[0], ring).add(p2[1].multiply(p2[1], ring), ring);
        return new F(F.SQRT, new F(F.DIVIDE, new Element[] {num, denum}));
    }

    /**
     * Q(w) = sqrt[(ac+bd)/c^2+d^2] P(w) = sqrt[(bc-ad)/c^2+d^2] f(w) =
     * arctg[Q(w)/P(w)] : arqtg[sqrt[(bc-ad)/(ac+bd)]]
     *
     * @param p1
     * @param p2
     *
     * @return
     */
    public static F fchx(F f, Ring ring) {
        Polynom[] p2 = new Polynom[] {};
        if (!(f.X[1] instanceof Polynom)) {
            p2 = dynamicProperties.solveReAndImFrequenceResponse(Polynom.polynomFromNumber(f.X[1], ring), new Ring("R64[j,w]"));
        } else {
            p2 = dynamicProperties.solveReAndImFrequenceResponse((Polynom) f.X[1], new Ring("R64[j,w]"));
        }
        System.out.println("[c,d] = " + Array.toString(p2, new Ring("R64[j,w]")));
        Polynom[] p1 = new Polynom[] {};
        if (!(f.X[0] instanceof Polynom)) {
            p1 = dynamicProperties.solveReAndImFrequenceResponse(Polynom.polynomFromNumber(f.X[0], ring), new Ring("R64[j,w]"));
        } else {
            p1 = dynamicProperties.solveReAndImFrequenceResponse((Polynom) f.X[0], new Ring("R64[j,w]"));
        }
        System.out.println("[a,b] = " + Array.toString(p1, new Ring("R64[j,w]")));
        Polynom num1 = p1[0].multiply(p2[0], ring).add(p1[1].multiply(p2[1], ring), ring);
        Polynom num2 = p1[1].multiply(p2[0], ring).subtract(p1[0].multiply(p2[1], ring), ring);
        for (int i = 0; i < num1.coeffs.length; i++) {
            num1.coeffs[i] = num1.coeffs[i].toNumber(Ring.Z, ring);
        }
        for (int i = 0; i < num2.coeffs.length; i++) {
            num2.coeffs[i] = num2.coeffs[i].toNumber(Ring.Z, ring);
        }
        Fraction rt = new Fraction(num2, num1);
        Fraction res = (Fraction) rt.cancel(ring);
        return new F(F.ARCTG, new F(F.SQRT, new F(F.DIVIDE, new Element[] {
                    res.num, res.denom
                })));
    }

    /**
     * Метод определяет ЛАЧХ
     *
     * @param ach - АЧХ
     * @param ring
     *
     * @return
     */
    public static F lach(F ach, Ring ring) {
        return new F(F.MULTIPLY, new Element[] {
                    Polynom.polynomFromNumber(new NumberR64(20), ring), new F(F.LG, ach)});
    }

    /**
     * Главный метод
     *
     * @param x
     * @param y
     *
     * @throws Exception
     */
    public static VectorS dinamik(F x, F y, Ring ring) {
        Element[] arr = new Element[7];
        arr[0] = new dynamicProperties().pFunc(y, x, ring);
        System.out.println("W(p) = " + arr[0].toString(new Ring("R64[p]",ring)));//Передаточная функция
        arr[1] = new dynamicProperties().kFunc(y, x, ring);
        System.out.println("k(t) = " + arr[1].toString(new Ring("R64[t]")));//Весовая функция
        arr[2] = new dynamicProperties().hFunc(y, x, ring);
        System.out.println("h(t) = " + arr[2].toString(new Ring("R64[t]")));//Переходная функция
        arr[3] = new dynamicProperties().afchx(x, y, ring);
        System.out.println("AFCHX = " + arr[3].toString(new Ring("R64[j,w]")));
        arr[4] = dynamicProperties.achx(new dynamicProperties().afchx(x, y, ring), ring);
        System.out.println("ACHX = " + arr[4].toString(new Ring("R64[j,w]")));
        arr[5] = dynamicProperties.fchx(new dynamicProperties().afchx(x, y, ring), ring);
        System.out.println("FCHX = " + arr[5].toString(new Ring("R64[j,w]")));
        arr[6] = dynamicProperties.lach((F) arr[4], ring);
        System.out.println("LACHX = " + arr[6].toString(new Ring("R64[j,w]")));
        return new VectorS(arr);
    }

    /**
     * Вычисление передаточной функции динамической системы
     *
     * @param x - вход
     * @param y - выход
     * @param ring
     *
     * @return
     */
    public static VectorS solveTransferFunction(F x, F y, Ring ring) {
        Element[] arr = new Element[1];
        arr[0] = new dynamicProperties().pFunc(y, x, ring);
        System.out.println("W(p) = " + arr[0].toString(new Ring("R64[p]",ring)));//Передаточная функция
        return new VectorS(arr);
    }

    /**
     * Вычисление временных характеристик динамической системы
     *
     * @param x - вход
     * @param y - выход
     * @param ring
     *
     * @return
     */
    public static VectorS solveTimeResponse(F x, F y, Ring ring) {
        Element[] arr = new Element[2];
        arr[0] = new dynamicProperties().kFunc(y, x, ring);
        System.out.println("k(t) = " + arr[0].toString(new Ring("R64[t]")));//Весовая функция
        arr[1] = new dynamicProperties().hFunc(y, x, ring);
        System.out.println("h(t) = " + arr[1].toString(new Ring("R64[t]")));//Переходная функция
        return new VectorS(arr);
    }

    /**
     * Вычисление частотных характеристик динамической системы
     *
     * @param x - вход
     * @param y - выход
     * @param ring
     *
     * @return
     */
    public static VectorS solveFrequenceResponse(F x, F y, Ring ring) {
        Element[] arr = new Element[4];
        arr[0] = new dynamicProperties().afchx(x, y, ring);
        System.out.println("AFCHX = " + arr[0].toString(new Ring("R64[j,w]")));
        arr[1] = dynamicProperties.achx(new dynamicProperties().afchx(x, y, ring), ring);
        System.out.println("ACHX = " + arr[1].toString(new Ring("R64[j,w]")));
        arr[2] = dynamicProperties.fchx(new dynamicProperties().afchx(x, y, ring), ring);
        System.out.println("FCHX = " + arr[2].toString(new Ring("R64[j,w]")));
        arr[3] = dynamicProperties.lach((F) arr[1], ring);
        System.out.println("LACHX = " + arr[3].toString(new Ring("R64[j,w]")));
        return new VectorS(arr);
    }
}
