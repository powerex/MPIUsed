/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform;

import com.mathpar.func.Fname;
import com.mathpar.func.Page;
import java.util.Random;
import com.mathpar.matrix.MatrixS;
import com.mathpar.number.Element;
import com.mathpar.number.Ring;

/**
 *
 * @author mixail
 */
public class TestRandomSystemLDE {

    /**
     * Генерация входных данных для задачи решения системы ЛДУ Генерируем 3
     * объекта: 1) Матрицу полиномов - матрица полученная после прямого
     * преобразования Лапласа 2) Вектор - столбец правых частей системы 3)
     * Начальные условия для входной системы ЛДУ
     *
     * @return
     */
    public static Object[] initDate(int n, int m) {
        Ring ring = new Ring("Z[p]");
        ring.setFLOATPOS(10);
        Random rnd = new Random();
        MatrixS A = new MatrixS(n, m, 100, new int[] {1, 100, 2}, rnd, ring.numberONE, ring);//матрица после прямого преобразования Лапласа левой части входной системы линейных дифференциальных уравнений
        Element[][] InitCond = new Element[n][];//двумерный массив начальных значений для входной системы линейных дифференциальных уравнений
        Element[] B = new Element[n];//вектор-столбец правой части входной системы линейных дифференциальных уравнений
        Element[] fname = new Element[n];//вектор имен неизвестных функций входной системы линейных дифференциальных уравнений
        //заполнение массивов B, InitCond, fname
        for (int i = 0; i < n; i++) {
            InitCond[i] = new Element[1];
            InitCond[i][0] = ring.numberZERO;//0
            if (i % 2 == 0) {
                B[i] = ring.numberZERO;//0
            } else {
                B[i] = ring.numberONE;//1
            }
            fname[i] = new Fname("x" + i);
        }
        return new Object[] {A, B, InitCond, fname};
    }

    public static void main(String[] args) {
        Ring ring = new Ring("R64[p]");//входное кольцо
        ring.page=new Page(ring,true);
        Object[] inDate = initDate(4, 4);
        MatrixS A = (MatrixS) inDate[0];
        Element[] B = (Element[]) inDate[1];
        Element[][] InitCond = (Element[][]) inDate[2];
        Element[] fname = (Element[]) inDate[3];
        //вычисление обратной матрица
        //******************************************************************
        //1 параллельный шаг
        MatrixS A_adj = ((MatrixS) A.toNewRing(Ring.Z, new Ring("Z[p]"))).adjoint(ring);
    }
}
