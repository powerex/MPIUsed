package com.mathpar.students.llp2.students;


import com.mathpar.number.*;
import com.mathpar.matrix.*;

public class Test__tugareva1 {

    public static void main(String[] args) throws Exception {
        System.out.println("This is a test for tugareva.java");
        Ring ring = new Ring("R[x]"); //над Z
        MatrixS Res = null;


//        double M[][] = {{8, 3}, //работает
//        {17.000, 6}};
//        MatrixD N = new MatrixD(M);
//        MatrixS NN=new MatrixS(N,ring);
//        System.out.println("A=" + NN);
//        Res=tugareva1.JordanForm(NN, ring);
//        System.out.println("fpol111=" + Res.toString(ring));

//        int M1[][] = {{1, 2}, {1, 2}}; //работает
//        MatrixS N1 = new MatrixS(M1, ring);
//        System.out.println("A=" + N1);
//        System.out.println("fpol111=" + tugareva1.JordanForm(N1, ring).toString(ring));


//          int M3[][] = {{2, 3},//работает
//          {1, 0}};
//          MatrixS N3 = new MatrixS(M3,ring);
//          System.out.println("A=" + N3);
//          System.out.println("fpol111=" +tugareva1.JordanForm(N3, ring).toString(ring));

//        int M4[][] = {{1, 1, 0},//работает
//        {0, 1, 1},
//        {1, 0, 1}};
//        MatrixS N4 = new MatrixS(M4, ring);
//        System.out.println("A=" + N4);
//        System.out.println("fpol111=" + tugareva1.JordanForm(N4, ring).toString(ring));

//        int M5[][] = {{0, 1, 0},//работает
//        {-4, 4, 0},
//        {-2, 1, 2}};
//        MatrixS N5 = new MatrixS(M5, ring);
//        System.out.println("A=" + N5);
//        System.out.println(tugareva1.JordanForm(N5, ring).toString(ring));


//        int M7[][] = {{4, -2, 2},//работает
//        {2, 0, 2},
//        {-1, 1, 1}};
//        MatrixS N7 = new MatrixS(M7, ring);
//        System.out.println("A=" + N7);
//        System.out.println(tugareva1.JordanForm(N7, ring).toString(ring));




//        int M8[][] = {{3, -5, 2}, //7,8,9 работает
//            {5, -8, 3},
//            {6, 17, 3}};
//        MatrixS N8 = new MatrixS(M8, ring);
//        System.out.println("A=" + N8);
//        Res = tugareva1.JordanForm(N8, ring);
//        System.out.println("fpol111=" + Res.toString(ring));


//          int M9[][] =
//          {{1,1,1,0},{-1,3,0,1},{-1,0,-1,1},{0,-1,-1,1}};//работает MatrixS N9
//          MatrixS N9= new MatrixS(M9, ring); System.out.println("A=" + N9);
//          System.out.println("fpol111=" + tugareva1.JordanForm(N9, ring).toString(ring));
//
//          int M10[][] =
//          {{0,1,0,0,0},{0,0,1,0,0},{0,0,0,1,0},{0,0,0,0,1},{1,-1,-2,2,1}}; //it's done
//          MatrixS N10 = new MatrixS(M10, ring); System.out.println("A=" + N10);
//          System.out.println("fpol111=" + tugareva1.JordanForm(N10, ring).toString(ring));


//          int M11[][] = {{0,1,0},{-4,4,0},{-2,1,2}};//работает
//          MatrixS N11=new MatrixS(M11, ring); System.out.println("A=" + N11);
//          System.out.println("fpol111=" + tugareva1.JordanForm(N11, ring).toString(ring));

//          int M12[][] = {{2,6,-15},{1,1,-5},{1,2,-6}};//работает
//          MatrixS N12 =new MatrixS(M12, ring); System.out.println("A=" + N12);
//          System.out.println("fpol111=" + tugareva1.JordanForm(N12,ring).toString(ring));


//          int M13[][] = {{9,-6,-2},{18,-12,-3},{18,-9,-6}};//работает
//          MatrixS N13 = new MatrixS(M13, ring); System.out.println("A=" + N13);
//          System.out.println("fpol111=" + tugareva1.JordanForm(N13,ring).toString(ring));
//

//          int M14[][] = {{4,6,-15},{1,3,-5},{1,2,-4}};//работает
//          MatrixS N14 = new MatrixS(M14, ring);
//          System.out.println("A=" + N14);
//          System.out.println("fpol111=" + tugareva1.JordanForm(N14,ring).toString(ring));


//          int M15[][] = {{0,-4,0},{1,-4,0},{1,-2,2}};//работает
//          MatrixS N15 = new MatrixS(M15, ring); System.out.println("A=" + N15);
//          System.out.println("fpol111=" + tugareva1.JordanForm(N15,ring).toString(ring));


//          int M16[][] = {{12,-6,-2},{18,-9,-3},{18,-9,-3}};//работает
//          MatrixS N16 = new MatrixS(M16, ring); System.out.println("A=" + N16);
//          System.out.println("fpol111=" + tugareva1.JordanForm(N16,ring).toString(ring));


//          int M17[][] = {{4,-5,2},{5,-7,3},{6,-9,4}};//работает
//          MatrixS N17 = new MatrixS(M17, ring); System.out.println("A=" + N17);
//          System.out.println("fpol111=" + tugareva1.JordanForm(N17,ring).toString(ring));

//        int M18[][] = {{5,-3,2},{6,-4,4},{4,-4,5}};//работает
//        MatrixS N18 = new MatrixS(M18, ring);
//        System.out.println("A=" + N18);
//        System.out.println("fpol111=" + tugareva1.JordanForm(N18, ring).toString(ring));


//          int M19[][] = {{1,-3,3},{-2,-6,13},{-1,-4,8}};//работает
//          MatrixS N19 = new MatrixS(M19, ring); System.out.println("A=" + N19);
//          System.out.println("fpol111=" + tugareva1.JordanForm(N19, ring).toString(ring));


//          int M20[][] = {{7,-12,6},{10,-19,10},{12,-24,13}};//работает
//          MatrixS N20 = new MatrixS(M20, ring); System.out.println("A=" + N20);
//          System.out.println("fpol111=" + tugareva1.JordanForm(N20,ring).toString(ring));

//        int M21[][] = {{1,-3,4},{4,-7,8},{6,-7,7}};//работает
//        MatrixS N21 = new MatrixS(M21, ring);
//        System.out.println("A=" + N21);
//        System.out.println("fpol111=" + tugareva1.JordanForm(N21, ring).toString(ring));

//        int M22[][] = {{3,-1,0},{6,-3,2},{8,-6,5}};//работает
//        MatrixS N22 = new MatrixS(M22, ring);
//        System.out.println("A=" + N22);
//        System.out.println("fpol111=" + tugareva1.JordanForm(N22, ring).toString(ring));


//        double[][] NewM = {{3, 0, 8}, {1, 7, 9}, {2, 5, 0}};//работает
//        MatrixD NewMD = new MatrixD(NewM);
//        MatrixS NewMS = new MatrixS(NewMD, ring);
//        System.out.println("A=" + NewMS);
//        Res = tugareva1.JordanForm(NewMS, ring);
//        System.out.println("JNF=" + Res.toString(ring));


        //работает
//            double[][] NewM = {{3, 0, 8, 7}, {1, 7, 9, 1}, {2, 5, 0, 1}, {0, 1, 2, 8}};
//            MatrixD NewMD = new MatrixD(NewM);
//            MatrixS NewMS = new MatrixS(NewMD, ring);
//            System.out.println("A=" + NewMS);
//            Res = tugareva1.JordanForm(NewMS, ring);
//            System.out.println("JNF=" + Res.toString(ring));


//        int[][] M={{1,2},{3,4}}; //работает
//        MatrixS MM=new MatrixS(M,ring);
//        int rank=MM.rank(ring);
//        Res=tugareva1.JordanForm(MM, ring);
//        System.out.println("Res="+Res);

//        int M6[][] = {{1, 0, 0},// работает
//        {0, 0, 0},
//        {0, 0, 0}};
//        MatrixS N6 = new MatrixS(M6, ring);
//        System.out.println("A=" + N6);
//        System.out.println("fpol=" + tugareva1.JordanForm(N6, ring).toString(ring));

        // работает
//        int[][] NewM = {{3, 0, 8, 7, 2}, {1, 7, 9, 1, 6}, {2, 5, 0, 1, 7}, {0, 1, 2, 8, 3}, {4, 9, 5, 12, 6}};
//        MatrixS NewMS = new MatrixS(NewM, ring);
//        System.out.println("A=" + NewMS);
//        Res = tugareva1.JordanForm(NewMS, ring);
//        System.out.println("JNF=" + Res.toString(ring));

//        int[][] NewM = {{3, 0, 8, 7, 11,6},
//            {1, 7, 9, 1, 2, 0},
//            {2, 5, 0, 1, 10, 3},
//            {0, 1, 2, 8, 2, 1},
//            {4, 9, 6, 12, 11, 7},
//            {13, 0, 1, 23, 17, 0}};
//            MatrixS NewMS = new MatrixS(NewM, ring);
//            System.out.println("A=" + NewMS);
//            Res = tugareva1.JordanForm(NewMS, ring);
//            System.out.println("JNF=" + Res.toString(ring));
//
//             int[][] NewM = {{3, 0, 8, 7, 11, 6, 1, 13},
//            {1, 7, 9, 1, 2, 0, 20, 5},
//            {2, 5, 0, 1, 10, 3, 9, 12},
//            {0, 1, 2, 8, 2, 1, 1, 0},
//            {4, 9, 6, 12, 11, 7, 5, 14},
//            {13, 0, 1, 23, 17, 0, 0, 4},
//            {1, 2, 3, 4, 5, 6, 7, 8},
//            {9, 8, 7, 6, 5, 4, 3, 2}};
//            MatrixS NewMS = new MatrixS(NewM, ring);
//            System.out.println("A=" + NewMS);
//            Res = tugareva1.JordanForm(NewMS, ring);
//            System.out.println("JNF=" + Res.toString(ring));
//
//        int[][] NewM = {{1,2,3,4,5,6,7,8,9,0},
//            {1,3,5,7,9,2,4,6,8,0},
//            {0,9,8,7,6,5,4,3,2,1},
//            {2,4,6,8,0,9,7,5,3,1},
//            {2,3,4,5,6,7,8,9,0,1},
//            {3,4,5,6,7,8,9,0,1,2},
//            {4,5,6,7,8,9,0,1,2,3},
//            {5,6,7,8,9,0,1,2,3,4},
//            {6,7,8,9,0,1,2,3,4,5},
//            {7,8,9,0,1,2,3,4,5,6}};
//            MatrixS NewMS = new MatrixS(NewM, ring);
//            System.out.println("A=" + NewMS);
//            Res = tugareva1.JordanForm(NewMS, ring);
//            System.out.println("JNF=" + Res.toString(ring));
//


//        int[][] NewM = {{65,0,0,0,0}, //yes
//            {0,70,0,0,0},
//            {0,0,61,0,8},
//            {0,0,0,0,0},
//            {0,93,0,9,0}};
//            MatrixS NewMS = new MatrixS(NewM, ring);
//            System.out.println("A=" + NewMS);
//            Res = tugareva1.JordanForm(NewMS, ring);
//            System.out.println("JNF=" + Res.toString(ring));


//         int[][] NewM = {{0,0,0,0,0}, //yes
//            {0,0,33,0,0},
//            {0,0,0,0,8},
//            {0,78,80,0,75},
//            {69,0,109,0,0}};
//
//            MatrixS NewMS = new MatrixS(NewM, ring);
//            System.out.println("A=" + NewMS);
//            Res = tugareva1.JordanForm(NewMS, ring);
//            System.out.println("JNF=" + Res.toString(ring));

//        int[][] NewM = {{0,0}, //?? что будет, если возвести нулевую матрицу в нулевую степень
//            {0,0}};
//            MatrixS NewMS = new MatrixS(NewM, ring);
//            System.out.println("A=" + NewMS);
//            Res = tugareva1.JordanForm(NewMS, ring);
//            System.out.println("JNF=" + Res.toString(ring));
//
        int[][] NewM = {{0, 2, 0, 34, 0, 0, 0, 0, 0, 0},//доделать
            {0, 0, 0, 0, 0, 0, 0, 10, 5, 0},
            {0, 0, 0, 0, 0, 51, 18, 0, 0, 47},
            {0, 0, 0, 0, 0, 0, 0, 0, 21, 0},
            {0, 0, 56, 0, 0, 0, 57, 0, 31, 0},
            {63, 17, 0, 0, 0, 31, 0, 7, 0, 0},
            {0, 0, 27, 0, 0, 0, 0, 0, 0, 26},
            {0, 0, 0, 49, 0, 0, 0, 0, 0, 0},
            {44, 0, 8, 19, 0, 0, 9, 51, 19, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

        MatrixS NewMS = new MatrixS(NewM, ring);
        System.out.println("A=" + NewMS);
        Res = tugareva1.JordanForm(NewMS, ring);
        System.out.println("JNF=" + Res.toString(ring));

//        double[][] NewM = {{0, 0, 22.00, 0, 44.00, 30.00, 16.00, 0, 0, 0}, //yes
//            {0, 0, 0, 30.00, 0, 0, 0, 63.00, 6.00, 0},
//            {0, 19.00, 0, 38.00, 0, 0, 0, 0, 19.00, 0},
//            {0, 0, 34.00, 30.00, 32.00, 0, 43.00, 0, 0, 0},
//            {47.00, 0, 55.00, 29.00, 0, 25.00, 0, 3.00, 35.00, 0},
//            {0, 0, 0, 8.00, 0, 15.00, 19.00, 0, 0, 0},
//            {0, 0, 0, 0, 19.00, 36.00, 0, 0, 0, 0},
//            {9.00, 0, 8.00, 4.00, 36.00, 0, 2.00, 0, 60.00, 52.00},
//            {0, 10.00, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 20.00, 30.00, 11.00, 0, 0, 0, 5.00, 0}};
//
//        MatrixD NewMD = new MatrixD(NewM);
//        MatrixS NewMS = new MatrixS(NewMD, ring);
//        System.out.println("A=" + NewMS);
//        Res = tugareva1.JordanForm(NewMS, ring);
//        System.out.println("JNF=" + Res.toString(ring));

//        double[][] NewM = {{0, 190, 713, 504, 0}, //yes +accuracy 125, на разности в 30 начинается exception
//            {578, 0, 0, 0, 0},
//            {0, 54, 0, 716, 0},
//            {0, 0, 0, 0, 0},
//            {0, 57, 0, 9, 0}};
//        MatrixD NewMD = new MatrixD(NewM);
//        MatrixS NewMS = new MatrixS(NewMD, ring);
//        System.out.println("A=" + NewMS);
//        Res = tugareva1.JordanForm(NewMS, ring);
//        System.out.println("JNF=" + Res.toString(ring));

//         double[][] NewM = {{0, 0, 0, 0, 122}, //yes
//            {0, 149, 0, 0, 0},
//            {0, 0, 0, 0, 585},
//            {0, 0, 82, 0, 0},
//            {808, 442, 0, 0, 0}};
//        MatrixD NewMD = new MatrixD(NewM);
//        MatrixS NewMS = new MatrixS(NewMD, ring);
//        System.out.println("A=" + NewMS);
//        Res = tugareva1.JordanForm(NewMS, ring);
//        System.out.println("JNF=" + Res.toString(ring));

//        double[][] NewM = {{0, 0, 829, 0, 370}, //yes
//            {0, 0, 0, 279, 0},
//            {0, 814, 320, 0, 861},
//            {0, 0, 391, 0, 0},
//            {808, 442, 0, 0, 0}};
//        MatrixD NewMD = new MatrixD(NewM);
//        MatrixS NewMS = new MatrixS(NewMD, ring);
//        System.out.println("A=" + NewMS);
//        Res = tugareva1.JordanForm(NewMS, ring);
//        System.out.println("JNF=" + Res.toString(ring));




//////   RANDOM!!!

//        Random rnd = new Random();
//        MatrixS New = new MatrixS();
//        int A[] = new int[]{5, 10, 10};
//        MatrixS Rand = new MatrixS(10, 10, 40, new int[]{5}, new Random(), ring.numberONE, ring);
//        System.out.println("Rand=" + Rand);
//        Res = tugareva1.JordanForm(Rand, ring);
//        System.out.println("JNF=" + Res.toString(ring));
   }
}
