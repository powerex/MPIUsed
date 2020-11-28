/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic2D.DrawMatrix;

import java.io.IOException;

import com.mathpar.matrix.MatrixD;
import com.mathpar.matrix.MatrixS;
import com.mathpar.matrix.file.sparse.SFileMatrixS;
import com.mathpar.number.Ring;

public class Test {
    public static void main(String[] args) throws IOException {
//******************************2014********************************************
//  ============================MatrixS=========================================
//        //устанавливаем кольцо
//        Ring ring = new Ring("R64[x]");
//        //генерация полиномиальной матрицы
//        MatrixS m = new MatrixS(2, 2, 100, new int[] {5, 50, 100}, new Random(),ring.numberONE, ring);
//        DrawMatrix dm = new DrawMatrix();
//        //конвертация в MatrixD
//        MatrixS A = dm.convertRandomPolynomToMatrixS(m, 0, ring);
//        //вывод полученной матрицы
//        System.out.println("a1= " + m.toString(ring));
//        System.out.println("A = " + A.toString(ring));
//        DrawMatrix res = new DrawMatrix(A, 800, 600);
//        res.draw();

        //устанавливаем кольцо
        Ring ring = new Ring("R64[x]");
        DrawMatrix dm = new DrawMatrix();
        //генерация рандомной функциональной матрицы
        MatrixS m = dm.randomMatrixSFunc(2, 2, ring);
        //ковертация в MatrixD
        MatrixS A = dm.convertRandomFuncsToMatrixS(m, 1, ring);
        //вывод полученной матрицы
        System.out.println("A_1 = " + m.toString(ring));
        System.out.println("A = " + A.toString(ring));
        DrawMatrix res = new DrawMatrix(A, 800, 600);
        res.draw();
//  ============================MatrixD=========================================
//        //устанавливаем кольцо
//        Ring ring = new Ring("R64[x]");
//        //генерация полиномиальной матрицы
//        MatrixD m = new MatrixD(10, 10, 100, new int[] {5, 50, 100}, new Random(), ring);
//        DrawMatrix dm = new DrawMatrix();
//        //конвертация в MatrixD
//        MatrixD A = dm.convertRandomPolynomToMatrixD(m, 0, ring);
//        //вывод полученной матрицы
//        System.out.println("a1= " + m.toString(ring));
//        System.out.println("A = " + A.toString(ring));
//        DrawMatrix res = new DrawMatrix(A, 800, 600);
//        res.draw();

        //устанавливаем кольцо
//        Ring ring = new Ring("R64[x]");
//        DrawMatrix dm = new DrawMatrix();
//        //генерация рандомной функциональной матрицы
//        MatrixD m = dm.randomMatrixDFunc(5, 5, ring);
//        //ковертация в MatrixD
//        MatrixD A = dm.convertRandomFuncsToMatrixD(m, 1, ring);
//        //вывод полученной матрицы
//        System.out.println("a1= " + m.toString(ring));
//        System.out.println("A = " + A.toString(ring));
//        DrawMatrix res = new DrawMatrix(A, 800, 600);
//        res.draw();
//******************************2014********************************************

//  ============================MatrixS=========================================
//        MatrixS A = new MatrixS(1000, 1000, 10, new int[] {7}, rnd, r.numberONE, r);
//        DrawMatrix dm = new DrawMatrix(A, 800, 600);
//        dm.draw();


//  ============================SFileMatrixS====================================
//        SFileMatrixS A = new SFileMatrixS(1, new File("/home/heckfy/FileMatrix/"), 10, 10, 80, rnd, new int[] {7}, ring.numberONE, ring);
//        DrawFileMatrix dm = new DrawFileMatrix(A,800, 600,10,10,1);
//        dm.draw();
    }
}
