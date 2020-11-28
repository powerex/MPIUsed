/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.matrix;
import com.mathpar.number.Array;
import java.util.Random;
import com.mathpar.number.Element;
import com.mathpar.number.Fraction;
import com.mathpar.number.NumberZ;
import com.mathpar.number.Ring;
import com.mathpar.parallel.stat.MS.AELDU;
//import com.mathpar.students.llp2.student.Shcherbinin.Matrix_multiply;

import static com.mathpar.matrix.adjLDU.joinSortedArraysWithoutDoubling;

/**
 * Это случай, когда нет перестановок - просто проверена формула разложения без перестановок.
 * @author gennadi
 */
public class NewLDU {
        /**
     * Adjoint matrix
     */
    public MatrixS A;
    /**
     * Echelon form of the initial matrix. It head elements placed in the
     * positions {(I,J),(I+1,J+1),..,(I+rank-1, J+rank-1)}
     */
    public MatrixS S;
    /**
     * Determinant of the head minor of rank "rank" of initial matrix
     */
    public Element Det;
    /**
     * Rank of the initial matrix.
     */
    public int rank;
    /**
     * Номер первой нулевой строки у эшелонной матрицы и ее матрицы
     * перестановок
     */
    public int I;
    /**
     * Номер первого нулевой столбца у эшелонной матрицы и ее матрицы
     * перестановок
     */
    public int J;
    /**
     * Перестановка строк матрицы S. {i1,.... in,j1...jn} Отсортирована по
     * возрастанию первая половина {i1,.... in}, - это строки отправки.
     * Тождественная перестановка -- это пустой массив.
     */
    public int[] Er;
    /**
     * Перестановка столбцов матрицы S. {i1,.... in,j1...jn} Тождественная
     * перестановка -- это пустой массив.
     */
    public int[] Ec;
    /**
     * LLL matrix
     */
    public MatrixS L;
    /**
     * U matrix
     */
    public MatrixS U;
        /**
     * M matrix
     */
    public MatrixS M;
    /**
     * W matrix
     */
    public MatrixS W;
    /**
     * diagonal matrix
     */
    public Element[] D;
    private static final int[] IdPerm = new int[0];

    public NewLDU(MatrixS A, int[] Er, int[] Ec, MatrixS S, Element Det, int I, int J, int rank, MatrixS L, MatrixS U, MatrixS M, MatrixS W, Element[] D) {
        this.A = A;
        this.S = S;
        this.Er = Er;
        this.Ec = Ec;
        this.I = I;
        this.J = J;
        this.rank = rank;
        this.Det = Det;
        this.L = L;
        this.D = D;
        this.U = U;
        this.M = M;
        this.W = W;
    }
    
    //---------------------------------
// 
//      /** список строк матрицы Е полного ранга */
//   public int[] Eif;
//   /** список столбцов матрицы Е полного ранга */
//   public int[] Ejf;
    // ----------------------- new -----------------
    int n;
    MatrixS DD;  // лишнее ,было придумано для скорости тут
    Ring ring = Ring.ringZxyz;
    public NewLDU(MatrixS T){
        n = T.size;
        L = new MatrixS();
        D = new Element[T.size];
        U = new MatrixS();
        M = new MatrixS();
        W = new MatrixS();
    };
//    public void getLDU(MatrixS T,Element a ){
//        switch (n) {
//            case 1:
//                Element a_n = T.getElement(0, 0, ring);
//                L = new MatrixS(a_n);
//                DD = new MatrixS(a_n).inverse(ring);
//                U = new MatrixS(a_n);
//                M = new MatrixS(a);
//                W = new MatrixS(a);
//                D = new Element[]{a_n};
//                break;
//            case 2:
//                Element a_n_1 = T.getElement(0, 0, ring);
//                Element a_n_0 = T.det(ring).divide(a, ring);
//                Element gamma = T.getElement(1, 0, ring);
//                Element betta = T.getElement(0, 1, ring);
//                D = new Element[]{a_n_1,a_n_0};
//                Element[][] tmp0 = {{a_n_1,ring.numberZERO},
//                                    {gamma,a_n_0          }
//                };
//                L = new MatrixS(tmp0, ring);
//                DD = getD(D,a);
//                Element[][] tmp1 = {{a_n_1,             betta},
//                                    {ring.numberZERO,   a_n_0}
//                };
//                U = new MatrixS(tmp1, ring);
//                Element[][] tmp2 = {{a,                                        ring.numberZERO},
//                                    {gamma.multiply(ring.numberMINUS_ONE,ring),a_n_1          }
//                };
//                M = new MatrixS(tmp2, ring);
//                Element[][] tmp3 = {{a,betta.multiply(ring.numberMINUS_ONE, ring)},
//                                    {ring.numberZERO,                       a_n_1}
//                };
//                W = new MatrixS(tmp3, ring);
//                break;
//            default:
//
//                MatrixS[] A = T.split();
//                MatrixS Aks = A[0];
//                MatrixS Bs = A[1];
//                MatrixS Cs = A[2];
//                MatrixS Ds = A[3];
//
//
//
//
//                NewLDU first = new NewLDU(Aks);
//                first.getLDU(Aks, a );
//                MatrixS Ukssn = first.M.multiply(Bs, ring).divideByNumber(a, ring);
//                MatrixS Lkssn = Cs.multiply(first.W, ring).divideByNumber(a, ring);
//                Element as = first.D[first.D.length-1];
//                MatrixS Asn = Lkssn.multiply(first.DD, ring)
//                        .multiply(Ukssn, ring);
//                Asn = Ds.subtract(Asn, ring)
//                        .multiplyByNumber(as, ring)
//                        .divideByNumber(a, ring);
//
//
//                NewLDU second = new NewLDU(Asn);
//                second.getLDU(Asn, as );
//
//
//              D = new Element[first.D.length+second.D.length];
//                System.arraycopy(first.D, 0, D, 0, first.D.length);
//                System.arraycopy(second.D, 0, D, first.D.length, second.D.length);
//
//                MatrixS Mkssn = second.M.multiply(Lkssn, ring)
//                        .multiply(first.DD, ring)
//                        .multiply(first.M, ring)
//                      .divideByNumber(a , ring)
//                        .multiplyByNumber(ring.numberMINUS_ONE, ring);
//                MatrixS Wkssn = first.W.multiply(first.DD, ring)
//                        .multiply(Ukssn, ring)
//                        .multiply(second.W, ring)
//                         .divideByNumber(a , ring)
//                        .multiplyByNumber(ring.numberMINUS_ONE, ring);
//                DD = getD(D,a);
//                L = MatrixS.join(new MatrixS[]{first.L,MatrixS.zeroMatrix(first.L.size),Lkssn,second.L});
//                U = MatrixS.join(new MatrixS[]{first.U,Ukssn,MatrixS.zeroMatrix(first.U.size),second.U});
//                M = MatrixS.join(new MatrixS[]{first.M,MatrixS.zeroMatrix(first.M.size),Mkssn,second.M});
//                W = MatrixS.join(new MatrixS[]{first.W,Wkssn,MatrixS.zeroMatrix(first.W.size),second.W});       
//        }
//    };
//    public NewLDU getLDU(MatrixS A, MatrixS[] ms ){
//        NewLDU tmp = new NewLDU(A);
//        tmp.getLDU(A, Ring.ringZxyz.numberONE );
//        ms[0]= tmp.L;  ms[1]=  tmp.DD; ms[2]=  tmp.U; ms[3]=  tmp.M; ms[4]=  tmp.W;
//        return tmp;
//    }
//    public static MatrixS getD(Element[] d,Element ak){
//        Element[][] m = new Element[d.length][1];
//        int[][] c = new int[d.length][1];
//        m[0][0] = new Fraction(ak, ak.multiply(d[0],Ring.ringZxyz));//.multiply(Ring.ringZxyz.numberONE, Ring.ringZxyz);
//        for (int i = 1; i < c.length; i++) {
//            c[i][0]=i;
//            m[i][0] = new Fraction(ak, d[i].multiply(d[i-1], Ring.ringZxyz));
//        }
//        return new MatrixS(d.length, d.length, m, c);
//    };
//    
//public static MatrixS genM(int k,int eM){
//        Random rnd = new Random();
//        MatrixS res =new MatrixS(Ring.ringZxyz.numberZERO);
//        for (int i = 1; i < k+1; i++) {
//            int[][] tmp = new int[i][i];
//                for (int j = 0; j < i-1; j++) {
//                    for (int l = 0; l < res.M[j].length; l++) {
//                        tmp[j][res.col[j][l]] = Integer.parseInt(res.M[j][l].toString());
//                    }
//                }
//            while(true){
//                for (int j = 0; j < tmp.length-1; j++) {
//                    tmp[j][tmp.length-1] = rnd.nextInt(eM);
//                    tmp[tmp.length-1][j] = rnd.nextInt(eM);
//                }
//                tmp[tmp.length-1][tmp.length-1] = rnd.nextInt(eM);
//                res = new MatrixS(tmp, Ring.ringZxyz);
//                Element det = res.det(Ring.ringZxyz);
//                boolean flag = (det).equals(Ring.ringZxyz.numberZERO, Ring.ringZxyz);
//                if(!flag){
//                    System.out.println("d"+i+"="+det);
//                    break;
//                }
//            }
//        }
//        return res;
//    }
 
///**
// * Конструкторы полей для нулевой матрицы
// * @param m_size - размер
// * @param d0 - внешний угловой минор
// * @param ring
// */
//     private void ldu_for_ZERO(int m_size, Element d0, Ring  ring){
//     A= MatrixS.scalarMatrix(m_size,d0,ring);
//     L=  MatrixS.zeroMatrix(m_size); U= L; W= L; M= L; D= new Element[0];
//     Er=new int[0]; Ec= new int[0]; S= L; Det=d0; Eif=new int[m_size]; Ejf= new int[m_size];
//     for (int i=0; i<m_size; i++){Eif[i]=i; Ejf[i]=i;}
//   }



 /**
     * Main recursive constructor of AdjEchelon VERSION 26.11.2011
     *
     * @param m -- входная матрица
     * @param d0 -- determinant of the last upper block. For first step: d0=1. A
     * -- adjoin matrix for matrix m: Am=S -- echelon form of m. S -- echelon
     * form for matrix m Det -- determinant
     * @param I -- the number of the first rows in matrix m. For first step:
     * =0.
     * @param  J -- the number of the first columns in matrix m. For first step:
     * =0.
     * @param ring Ring
     */
    public NewLDU(MatrixS m, Element d0, int I, int  J, Ring ring) {
       this.I = I;
        this.J =  J;
        int N = m.size; //N is a number of rows in the matrix m
        if (m.isZero(ring)) {
            A = MatrixS.scalarMatrix(m.size, d0, ring);
            Er = IdPerm;
            Ec = IdPerm;
            S = MatrixS.zeroMatrix(N);
            Det = d0;
            rank = 0;
            L =  MatrixS.scalarMatrix(N, ring.numberZERO, ring);
            U = MatrixS.scalarMatrix(N, ring.numberZERO, ring);
            M = MatrixS.zeroMatrix(N);
            W = MatrixS.zeroMatrix(N);
            D = new Element[0];
        } else {
            if (N == 1) {
                A = new MatrixS(1, 1, new Element[][]{{d0}}, new int[][]{{0}});
                Er = IdPerm;
                Ec = IdPerm;
                S = m;
                Det = m.M[0][0];
                rank = 1;
                L = new MatrixS(Det);
                U = new MatrixS(Det);
                M = new MatrixS(ring.numberONE);
                W = new MatrixS(ring.numberONE);
                D = new Element[]{Det};
            } else {
                int N2 = N;
                N = N >>> 1;
                int IminN_or_0 = (I > N) ? (I - N) : 0;
                int JminN_or_0 = ( J > N) ? ( J - N) : 0;
                MatrixS[] M = m.split();
                NewLDU m11 = new NewLDU(M[0], d0, I,  J, ring); //--------- 1 STEP ------------------------//
    System.out.println("M[0]+m11.L+m11.M ==========="+M[0]+m11.L+m11.M);
                M[1] = M[1].permutationOfRows(m11.Er);
                M[2] = M[2].permutationOfColumns(m11.Ec);  //   меняем местами столбцы в соответствии с m11.Ec
                 Element d11 = m11.Det;
                Element d11_2 = d11.multiply(d11, ring);
                MatrixS y11 = m11.S.ES_min_dI(d11, m11, ring);
                MatrixS M12_1 = m11.A.multiplyDivRecursive(M[1], d0, ring);
                MatrixS M21_1 = M[2].multiplyDivRecursive(y11, d0.negate(ring), ring);      
                int l_11 = m11.rank;
                int I_plusl11 = I + l_11;
                int J_plusl11 =  J + l_11;
                NewLDU m21 = new NewLDU(M21_1, d11, IminN_or_0, J_plusl11, ring); //2 STEP  //
                NewLDU m12 = new NewLDU(M12_1.barImulA(m11), d11, I_plusl11, JminN_or_0, ring); //3 STEP  //
                                //--------- 2 and  3  STEPS -------------------------------------------------------------//
                System.out.println("M21_1+m21.L+m21.M ==========="+M21_1+m21.L+m21.M );
                System.out.println("M12_1.barImulA(m11)+m12.L+m12.M ==========="+M12_1.barImulA(m11)+m12.L+m12.M);
                                int l_12 = m12.rank;
                int l_21 = m21.rank;
                Element d12 = m12.Det;
                Element d21 = m21.Det;
                M[3] = M[3].permutationOfRows(m21.Er).permutationOfColumns(m12.Ec);
                M[2] = M[2].permutationOfRows(m21.Er);
 

                m11.A = m11.A.permutationOfRows(m12.Er).permutationOfColumns(m12.Er);
                m11.S = m11.S.permutationOfColumns(m21.Ec);
                m11.U = m11.U.permutationOfColumns(m21.Ec);
                M12_1 = M12_1.permutationOfRows(m12.Er).permutationOfColumns(m12.Ec);
                MatrixS M22_1 = ((M[3].multiplyByNumber(d11, ring)).subtract(M[2].multiplyRecursive(M12_1.ETmulA(m11), ring), ring)).divideByNumber(d0, ring);
                MatrixS y21 = m21.S.ES_min_dI(d21, m21, ring);
                MatrixS y12A = m12.S.ES_min_dI(d12, m12, ring);
                MatrixS A21M22 = m21.A.multiplyRecursive(M22_1, ring);
                MatrixS M22_low = A21M22.barImulA(m21);   // for L  43 63
                MatrixS M22_hight = A21M22.ImulA(m21);
                MatrixS M22_2_low = M22_low.multiplyDivRecursive(y12A, d11_2.negate(ring), ring);
                MatrixS M22yA = M22_hight.multiplyDivRecursive(y12A, d11_2.negate(ring), ring);
                Element ds = d12.multiply(d21, ring).divide(d11, ring);
                NewLDU m22 = new NewLDU(M22_2_low, ds, IminN_or_0 + m21.rank, JminN_or_0 + m12.rank, ring); //  4-STEP //
 System.out.println("M22_2_low+m22.L+m22.M ==========="+M22_2_low+m22.L+m22.M);
                int l_22 = m22.rank;
                rank = l_11 + l_21 + l_12 + l_22;
                Det = m22.Det;
                M22_1 = M22_1.permutationOfRows(m22.Er).permutationOfColumns(m22.Ec);
                M22yA = M22yA.permutationOfRows(m22.Er).permutationOfColumns(m22.Ec);
                M12_1 = M12_1.permutationOfColumns(m22.Ec);
                m12.S = m12.S.permutationOfColumns(m22.Ec);
                
                M[3] = M[3].permutationOfRows(m22.Er).permutationOfColumns(m22.Ec);
                M[1] = M[1].permutationOfColumns(m12.Ec).permutationOfColumns(m22.Ec);
                m21.A = m21.A.permutationOfRows(m22.Er).permutationOfColumns(m22.Er);
                MatrixS M11_2 = m11.S.multiplyDivRecursive(y21, d11.negate(ring), ring);
                MatrixS M12_1_I = M12_1.ImulA(m11);
                MatrixS y12B = m12.S.ES_min_dI(d12, m12, ring);
                MatrixS M12_2 = ((((m11.S.multiplyDivRecursive(m21.A.ETmulA(m21), d11, ring).multiplyRecursive(M22_1, ring)).subtract((M12_1_I.multiplyByNumber(d21, ring)), ring)).divideByNumber(d11, ring).multiplyRecursive(y12B, ring)).add((m12.S).multiplyByNumber(d21, ring), ring)).divideByNumber(d11, ring);
                MatrixS y22 = m22.S.ES_min_dI(Det, m22, ring);
                MatrixS M12_3 = M12_2.multiplyDivRecursive(y22, ds.negate(ring), ring);
                MatrixS M22_3 = (M22yA.multiplyDivRecursive(y22, ds.negate(ring), ring)).add(m22.S, ring);
                MatrixS A1 = m12.A.multiplyRecursive(m11.A, ring);
                MatrixS A2 = m22.A.multiplyRecursive(m21.A, ring);
                MatrixS A1_E12 = A1.ETmulA(m12);
                MatrixS P = (A1.subtract(M12_1_I.multiplyDivRecursive(A1_E12, d11, ring), ring)).divideMultiply(d11, Det, ring);
                MatrixS Q = (A2.subtract(M22yA.multiplyDivRecursive(A2.ETmulA(m22), ds, ring), ring)).divideByNumber(d21, ring);
                MatrixS F = (m11.S.multiplyDivMulRecursive(m21.A.ETmulA(m21) //  .multiplyLeftE(m21.Ec,m21.Er)
                        , d11, Det, ring).add(M12_2.multiplyDivRecursive(A2.ETmulA(m22), ds, ring), ring)).divideByNumber(d21.negate(ring), ring);
                MatrixS G = ((M[2].permutationOfRows(m22.Er)).multiplyDivMulRecursive(m11.A.ETmulA(m11), d0, d12, ring).add(M22_1.multiplyDivRecursive(A1_E12, d11, ring), ring)).divideByNumber(d11.negate(ring), ring);
                MatrixS[] AA = new MatrixS[4];
                AA[0] = (P.add(F.multiplyRecursive(G, ring), ring)).divideByNumber(d12, ring);
                AA[1] = F;
                AA[2] = Q.multiplyDivRecursive(G, d12, ring);
                AA[3] = Q;
                MatrixS M11_3 = M11_2.multiplyDivide(Det, d21, ring);
                MatrixS M21_3 = m21.S.multiplyDivide(Det, d21, ring);
                A = MatrixS.join(AA);
                S = MatrixS.join(new MatrixS[]{M11_3, M12_3, M21_3, M22_3});

                int from1 = N;
                int tothe1 = I_plusl11;
                // строим матрицу перестановок строк
                if (from1 <= I_plusl11) {
                    Er = new int[0];
                } else {
                    int from2 = I_plusl11;
                    int tothe2 = I_plusl11 + l_21;
                    int from3 = Math.max(N, I) + l_21;
                    int tothe3 = I_plusl11 + l_21 + l_12;
                    int from4 = I_plusl11 + l_12;
                    int tothe4 = tothe3 + l_22;
                    int l_back = (l_21 + l_22 == 0) ? 0 : N - I_plusl11 - l_12;
                    int Erlen = (I_plusl11 == N) ? l_back : l_21 + l_back;
                    if (l_21 != 0) {
                        Erlen += l_12;
                    }
                    if (from3 != tothe3) {
                        Erlen += l_22;
                    }
                    Er = new int[2 * Erlen];
                    int jj = Erlen;
                    if (Erlen != 0) {
                        int ii = 0;
                        for (int i = 0; i < l_21; i++) {
                            Er[ii++] = from1++;
                            Er[jj++] = tothe1++;
                        }
                        if (l_21 != 0) {
                            for (int i = 0; i < l_12; i++) {
                                Er[ii++] = from2++;
                                Er[jj++] = tothe2++;
                            }
                        }
                        if (from3 != tothe3) {
                            for (int i = 0; i < l_22; i++) {
                                Er[ii++] = from3++;
                                Er[jj++] = tothe3++;
                            }
                        }
                        if (l_21 + l_22 > 0) {
                            for (int i = 0; i < l_back; i++) {
                                Er[ii++] = from4++;
                                Er[jj++] = tothe4++;
                            }
                        }
                    }
                }
                // строим матрицу перестановок столбцов
                int from2 = l_21 + J_plusl11;
                int Ec_len = N - from2;
                int l12PLUSl22 = l_12 + l_22;
                if ((Ec_len <= 0) || (l12PLUSl22 == 0)) {Ec = new int[0];} 
                else {
                    Ec_len += l12PLUSl22;
                    Ec = new int[2 * Ec_len];
                    int jj = Ec_len;
                    from1 = N;
                    tothe1 = from2;
                    for (int i = 0; i < l12PLUSl22; i++) {
                        Ec[i] = from1++;
                        Ec[jj++] = tothe1++;
                    }
                    int tothe2 = from2 + l12PLUSl22;
                    for (int i = l12PLUSl22; i < Ec_len; i++) {
                        Ec[i] = from2++;
                        Ec[jj++] = tothe2++;
                    }
                }
                A = A.permutationOfRows(Er).permutationOfColumns(Er);
                 System.out.println("Ec= before  ="+Array.toString(Ec));
                S = S.permutationOfRows(Er).permutationOfColumns(Ec);
                Ec = MatrixS.permutationsOfOnestep(Ec, m11.Ec, m12.Ec, m21.Ec, m22.Ec, N);  
                System.out.println("Ec=  ="+Array.toString(Ec)+Array.toString(m11.Ec)+Array.toString(m12.Ec)+Array.toString(m21.Ec)+Array.toString(m22.Ec));
                Er = MatrixS.permutationsOfOnestep(Er, m11.Er, m21.Er, m12.Er, m22.Er, N);

                int l11PLUSl21 = l_11 + l_21;
                int PLUSl11_l21_l12 = l11PLUSl21 + l_12;
                int l11PLUSl12 = l_11 + l_12;
                int l21PLUSl22 = l_21 + l_22;
                int Nminl21l22 = N - l21PLUSl22;
                int Nminl11l12 = N - l11PLUSl12;
                int Nminl11l21 = N - l11PLUSl21;
                // ============================= строим матрицы U , L ==================================
                MatrixS LL21_41_61, u13_14_16, LL21A, A11M1 = null, u23_24_26 = null; // блоки будущих матриц

        Element[] DiagM11=adjLDU.GaussDiag(m11.D,    ring);    //  
        MatrixS Diag11 = new MatrixS(DiagM11.length,  DiagM11, ring);
        Element[] DiagM12=adjLDU.GaussDiag(m12.D,    ring);    //  
        MatrixS Diag12 = new MatrixS(DiagM12.length,  DiagM12, ring);
        MatrixS L21Block, L41Block, L42Block;
                if (l_11 == 0) {
                    u13_14_16 = MatrixS.zeroMatrix(N);
                    LL21A = m21.L;
                    LL21_41_61=MatrixS.zeroMatrix(N);
                    L21Block=MatrixS.zeroMatrix(l_21);
                    L41Block=MatrixS.zeroMatrix(l_22);
                    L42Block=MatrixS.zeroMatrix(l_22);
                } else {
                    MatrixS A11_i = m11.A.ImulA(m11).moveRows(I, 0, N - I).moveColumns(I, 0, N - I);
                    A11M1 = M12_1_I.moveRows(I, 0, l_11);
                    u13_14_16 = m11.U.multiplyDivRecursive(A11M1, m11.Det, ring);  // 13 14 16   U
                    MatrixS M2 = M[2].moveColumns( J, 0, l_11);
                    LL21_41_61 = M2.multiply(A11_i, ring).multiplyDivRecursive(m11.L, d0.multiply(m11.Det, ring), ring); //21   41   61    L
             System.out.println("1MM21_41_61=====+++++++++++++++++++++++++"+LL21_41_61);
             System.out.println("Diag11=====+++++++++++++++++++++++++"+Diag11);
                    L21Block= LL21_41_61.getRowBlock(0, l_21); // чистый блок L21
                    L41Block= LL21_41_61.getRowBlock( l_21, l_22); // чистый блок L41
                    L42Block= m21.L.getRowBlock( l_12, l_22); // чистый блок L42
                    LL21A =  LL21_41_61.add(m21.L.moveColumns(0, l_11, l_21), ring); //21 22, 41 42, 61 62   L                
                }
                LL21A = LL21A.permutationOfRows(m22.Er);
                LL21A = LL21A.moveRows(l_21 + IminN_or_0, l_21, l_22);
                if (l_21 == 0) {
                    u23_24_26 = MatrixS.zeroMatrix(N);
                } else {
                    if (l_11 == 0) {
                        u23_24_26 = m21.U.multiplyDivRecursive((m21.A.ImulA(m21).moveRows(m21.I, 0, l_21).multiply(M[3], ring)), m21.Det.multiply(d0, ring), ring);
                    } else {
                        u23_24_26 = m21.U.multiplyDivRecursive((m21.A.ImulA(m21).multiply(M22_1, ring)), m21.Det.multiply(d11, ring), ring); //23 24 26  U
                    }
                }
         
                MatrixS S0 = (l_11 == 0) ? m21.U : (l_21 == 0) ? m11.U : m21.U.moveColumns(0, l_11, l_21).insertRowsIn(m11.U.moveColumns(l_11 +  J, l_11, l_21), 0, l_11, l_21); // ready
                // сдиг block12  из-за шифта
                MatrixS u33_34_36 = m12.U.permutationOfColumns(m22.Ec).multiplyDivide(m21.Det, m11.Det, ring);
                u33_34_36 = u33_34_36.moveColumns(l_12 + JminN_or_0, l_12, l_22); // сдиг block34  из-за шифта
                MatrixS u33_34_36_44_46 = (l_12 == 0) ? m22.U : (l_22 == 0) ? u33_34_36
                        : m22.U.moveColumns(0, l_12, l_22).insertRowsIn(u33_34_36, 0, l_12, l_22);
                MatrixS u1_2_346 = (l_11 == 0) ? u23_24_26 : (l_21 == 0) ? u13_14_16 : u23_24_26.insertRowsIn(u13_14_16, 0, l_11, l_21);
                MatrixS S1_2 = u1_2_346.insertRowsIn(MatrixS.zeroMatrix(N + N), 0, 0, l11PLUSl21);
                U = S0.insertRowsIn(MatrixS.zeroMatrix(N + N), 0, 0, l11PLUSl21).moveColumns(l11PLUSl21, rank, N - l11PLUSl21);// move 5
                MatrixS S3_4 = u33_34_36_44_46.insertRowsIn(S1_2, 0, l11PLUSl21, l12PLUSl22);
                S3_4 = S3_4.moveColumns(l12PLUSl22, N + l12PLUSl22, N - l12PLUSl22).moveColumns(0, l11PLUSl21, l12PLUSl22);// move 6   then  33,44
                U = U.add(S3_4, ring);
                // ============================= строим матрицу L,M  ==================================

                MatrixS m11_L = m11.L.permutationOfRows(m12.Er); // не действует на ведущие строки 11 
        System.out.println(" m11_ L=="+ m11_L );
                MatrixS L11Block=m11_L.getRowBlock(0, l_11);
                MatrixS L31Block=m11_L.getRowBlock(I_plusl11, l_12);
  /* */    //          MatrixS M21Block=m12.M.multiply(L21Block, ring).multiply(L11Block, ring);
         System.out.println("---B-----__------m21.M -----------="+m21.M );
         System.out.println("---B-------------L31Block---------=" +L31Block );
         System.out.println("---B-------------L11Block---------="+ L11Block);
   //      System.out.println("---B------------M21Block------------="+ M21Block);
   
   
   Element[] DiagM_11=adjLDU.GaussDiag(m11.D,    ring);    
   System.out.println("Dddd-Proved-diag in LDU="+Array.toString(DiagM_11));
   MatrixS Diag_M11_M = new MatrixS(DiagM_11.length,  DiagM_11, ring).multiply(m11.M, ring); //.divideByNumber(d0, ring);
   
  /* */              MatrixS M21Block=m21.M.multiply(L21Block, ring).multiply(Diag_M11_M, ring).negate(ring);   
    /* */            MatrixS M31Block=m12.M.multiply(L31Block, ring).multiply(Diag_M11_M, ring).negate(ring);
                MatrixS   T1_2 = m11_L.insertRowsIn(MatrixS.zeroMatrix(Nminl11l21 + N), I_plusl11, 0, l_12);  // 31b
                
                T1_2 = m11_L.insertRowsIn(T1_2, l11PLUSl12, l12PLUSl22, Nminl11l12);  //51b
               
                MatrixS   T0 = LL21A.insertRowsIn(m11.L, 0, l_11, l_21); //b21 22
               
                T1_2 = LL21A.insertRowsIn(T1_2, l_21, l_12, l_22);  // b41 42
               
                T1_2 = LL21A.insertRowsIn(T1_2, l21PLUSl22, l_22 + N - l_11, Nminl21l22);  //61 62
                MatrixS m12A = m12.A.moveRows(I_plusl11, 0, N - I_plusl11).moveColumns(I + l_11, 0, N - I - l_11).EmulA(0, 0, l_12);
                MatrixS AL12 = m12A.multiply(m12.L, ring);
                M22_low = M22_low.moveColumns(m12.J, 0, l_12);  // 43 63;   редкий случай, когда у 12 есть сдвиг по столбцам...            
            
                MatrixS LL21_2moved = M22_low.multiplyDivRecursive(AL12, d12.multiply(d11, ring).multiply(d11, ring), ring);
                MatrixS L43Block = LL21_2moved.getRowBlock(0, l_22);
                LL21_2moved = LL21_2moved.permutationOfRows(m22.Er); // 43 63 
                 
                MatrixS m22Ln = m22.L.moveColumns(0, l_12, l_22).moveRows(0, l_21, l_22); // поставили на место 44 64
                MatrixS L43_44_63_64 = (l_12 == 0) ? m22Ln : LL21_2moved.add(m22Ln, ring);  // 43 44, 63 64
                MatrixS m12_L = m12.L.multiplyDivide(m21.Det, m11.Det, ring);
                                
             MatrixS   T3_4 = m12_L.insertRowsIn(MatrixS.zeroMatrix(N2), 0, l11PLUSl21, l_12);  // 33
                               System.out.println(" T3_4L=="+ T3_4 );
                T3_4 = m12_L.insertRowsIn(T3_4, l11PLUSl12, rank, Nminl11l12);  // 53
                        System.out.println(" T3_4L=="+ T3_4 );
                T3_4 = L43_44_63_64.insertRowsIn(T3_4, l_21, PLUSl11_l21_l12, l_22);  // 43 44
                       System.out.println(" T3_4L=="+ T3_4 );
                T3_4 = L43_44_63_64.insertRowsIn(T3_4, l21PLUSl22, N + l21PLUSl22, Nminl21l22);  // 63 64
                       System.out.println(" T3_4L=="+ T3_4 );
                L = T0.insertRowsIn(MatrixS.zeroMatrix(N2), 0, 0, l11PLUSl21);  // L11
                       System.out.println(" L=="+ L );
                L = T1_2.insertRowsIn(L, 0, l11PLUSl21, N + Nminl11l21);  // L21
                     System.out.println(" L=="+ L );
                L = L.add(T3_4.moveColumns(0, l11PLUSl21, N), ring);
                MatrixS L41424344=L.getRowBlock(PLUSl11_l21_l12, l_22);
        System.out.println("L=-----------------="+L);  
        System.out.println("L41424344=="+ L41424344+PLUSl11_l21_l12 + l_22);
   Element[] DiagM_21=adjLDU.GaussDiag(m21.D,    ring);    
   MatrixS Diag_M21_M = new MatrixS(DiagM_21.length,  DiagM_21, ring).multiply(m11.M, ring); //.divideByNumber(d0, ring);
   Element[] DiagM_12=adjLDU.GaussDiag(m12.D,    ring);    
   MatrixS Diag_M12_M = new MatrixS(DiagM_12.length,  DiagM_12, ring).multiply(m11.M, ring); //.divideByNumber(d0, ring);
   
        
        MatrixS bb2=M21Block.concatenationLR(m21.M, l_11);
        MatrixS bb3=M31Block.concatenationLR(m12.M, l11PLUSl21);
        MatrixS bb23=bb2.concatenationUD(bb3, l_21);
        MatrixS bb123=m11.M.concatenationUD(bb23, l_11);
// /* */     MatrixS D_112112_=MatrixS.makeBlockDiagonalMatrixS(new MatrixS[]{m11.M,m21.M,m12.M,MatrixS.zeroMatrix(l_22)}, new int[]{l_11,l_21,l_12,l_22});
// /* */          MatrixS M2131=  M21Block.insertRowsIn(MatrixS.zeroMatrix(rank), 0, l_11, l_21);
//        System.out.println("M21Block+M31Block+ M2131+ l11PLUSl21---= "+M21Block+M31Block+ M2131+ l11PLUSl21 );
// /* */               M2131=  M31Block.insertRowsIn(M2131, 0, l11PLUSl21, l_12);
// /* */               D_112112_=M2131.add(D_112112_, ring) ;
//        System.out.println("D_112122=--------------="+ D_112112_) ; 
//        System.out.println(" M31Block.insertRowsIn(M2131=="+ M2131);
//        
  /* */              MatrixS M414243= m22.M.multiply(L41424344, ring).multiply(bb123, ring).negate(ring);
        System.out.println(" M414243=="+ M414243);
  /* */              MatrixS M41424344= M414243.concatenationLR(m22.M, PLUSl11_l21_l12);
        System.out.println("M41424344=="+ M41424344);
 /* */               this.M = bb123.concatenationUD(M41424344, PLUSl11_l21_l12);
          System.out.println(" L=="+ L ); System.out.println(" M=================="+ this.M );
                // ============================= строим матрицу D  ==================================
                D = new Element[rank];
                System.arraycopy(m11.D, 0, D, 0, l_11);
                System.arraycopy(m21.D, 0, D, l_11, l_21);
                for (int i = 0; i < m12.D.length; i++) {
                    m12.D[i] = m12.D[i].multiply(m21.Det, ring).divide(m11.Det, ring);
                }
               System.arraycopy(m12.D, 0, D, l11PLUSl21, l_12);
               System.arraycopy(m22.D, 0, D, PLUSl11_l21_l12, l_22);
                // строим матрицу К ------------------------------------------
                
          System.out.println("--Ь=-------="+l_11+l_21+l_12+l_22 );        
                int[] blSizes=new int[]{l_11, l_21, l_12, l_22};
                MatrixS[] blMl=new MatrixS[]{MatrixS.zeroMatrix(l_11),m21.M,m12.M,m22.M};
                MatrixS[] blMr=new MatrixS[]{m11.M,m21.M,m12.M,MatrixS.zeroMatrix(m22.rank)};
               MatrixS lMul=MatrixS.makeBlockDiagonalMatrixS(blMl, blSizes);
           System.out.println("4 parts of  L L L L== "+m11.L+m21.L+ m12.L+m22.L );
           System.out.println("4 parts of M M M M=="+m11.M+m21.M+ m12.M+m22.M);
           System.out.println("lMul"+lMul);
               MatrixS rMul=MatrixS.makeBlockDiagonalMatrixS(blMr, blSizes);
           System.out.println("rMul"+rMul);
               this.M = L.deleteBlockDiagonalMatrixS(blSizes);
           System.out.println("K=L.deleteBlockDiagonalMatrixS(blSizes)=="+this.M);
               this.M = lMul.multiply(this.M, ring);
           System.out.println("lMul=="+lMul+"  K=lMul.multiply(K="+this.M);
               this.M = this.M.multiply(rMul, ring);
          System.out.println("K====="+this.M);
                
            }
        }
    }

    
    public static void main(String[] args) {
        int[][] al = {{2, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0},
                      {0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                      {0, 0, 2, 0, 1, 2, 0, 2, 0, 1, 0, 0, 1, 0, 1, 0},
                      {0, 0, 0, 2, 2, 2, 1, 2, 1, 2, 1, 0, 0, 1, 1, 0},
                      {0, 0, 0, 0, 2, 0, 2, 1, 1, 1, 0, 0, 0, 0, 2, 1},
                      {0, 0, 0, 0, 0, 2, 0, 1, 2, 2, 1, 0, 0, 0, 0, 2},
                      {0, 0, 0, 0, 0, 0, 2, 2, 1, 0, 1, 1, 1, 1, 2, 0},
                      {0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 1, 1, 0, 0, 1, 1},
                      {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 2, 0, 0, 1, 1},
                      {0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 2, 2, 0},
                      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 1, 1, 0},
                      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
                      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0},
                      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0},
                      {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0},
                      {7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}
        };
//                  al = new int[][]{{2,1 },
//                      {3, 7 },
//                    };
          al = new int[][]{{2,1, 4, 3 },
                      {2, 7, 6, 1 },
                      {1, 3, 0, 0 },
                      {1, 1, 0, 0}
    };
//        al = new int[][]{{2, 2, 0, 0, 0, 1, 1, 2},
//                      {1, 2, 0, 0, 1, 0, 2, 1},
//                      {1, 3, 0, 0, 1, 1, 0, 1},
//                      {1, 1, 0, 0, 2, 1, 0, 2},
//                      {1, 5, 2, 0, 1, 1, 2, 1},
//                      {1, 1, 1, 0, 1, 1, 0, 0},
//                      {0, 0, 2, 0, 2, 1, 2, 1},
//                      {2, 1, 1, 0, 1, 1, 2, 3}
//                     };
 //       int[] dets0 = {2,4,4,-8,16,-24,-56,76};
   //     int[] dets = {1,1,-4,-4,-22,-24,-16,144,1343,4277,4710,5060,2998,-11339,-37150,-10430};
//       al = new int[][]{{7, 2},{2,1}};
//       al = new int[][] {{0, 0, 6, 0}, {0, 0, 7, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
      Ring ring=new Ring("Z[]");
     MatrixS tmp = new MatrixS(al, ring);

      //  tmp = genM(8, 10);
        int sizeM=al.length;
     //   tmp=new MatrixS(sizeM, sizeM, 5, new int[]{3}, new Random(), ring.numberONE, ring);
        System.out.println("tmp="+tmp);
        new Ring("Q[]");
 //       MatrixS[] tmpLDU=new MatrixS[5];
//        NewLDU nldu = new NewLDU(tmp);
//          nldu = nldu.getLDU(tmp, tmpLDU);
        
 //       System.out.println();    
//        Element[] diagD =nldu.D;   
    //    System.out.println("length of diag ============"+diagD.length); 
//        for (int i = 0; i < tmpLDU.length; i++) 
//            System.out.print("   ldu("+i+")="+tmpLDU[i]); System.out.println();  
//     System.out.println("L="+tmpLDU[0]);
//        System.out.println("D="+tmpLDU[1]);
//        System.out.println("U="+tmpLDU[2]);
//        MatrixS res = tmpLDU[0].multiply(tmpLDU[1], ring).multiply(tmpLDU[2], ring);

//        System.out.println("res.subtract(tmp)"+res.subtract(tmp,ring));
//        System.out.println("M="+tmpLDU[3]);
//        System.out.println("W="+tmpLDU[4]);
//           Element det_ =diagD[diagD.length-1];
//            System.out.println("det="+det_+ "  "+diagD.length+"  "+diagD[1]);
//            MatrixS res1 = tmpLDU[4].multiply(tmpLDU[1], ring).multiply(tmpLDU[3], ring).multiplyByNumber(det_, ring);

//            System.out.println("res1="+res1);
              System.out.println("adj="+tmp.adjoint(ring));
//           System.out.println("res1.subtract(Adj_tmp)=============================="+
//                   res1.subtract(tmp.adjoint(ring),ring));
        //_________________________________________
   //     MatrixS A2=new MatrixS(m2,ring);
  //  MatrixS A1=new MatrixS (al,ring);// A=A.multiply(Matrix, ring)
  //           System.out.println( " A        ======  "+A1);
  //           MatrixS AF=tmp;
     NewLDU x = new NewLDU(tmp, ring.numberONE, 0, 0, ring);  // y as is
     System.out.println( "  Er="+Array.toString(x.Er)+"  Ec="+Array.toString(x.Ec)); 
 System.out.println("Eccccc =="+MatrixS.scalarMatrix(sizeM, sizeM, ring.numberONE, ring).permutationOfColumns(x.Ec).toString()); 
System.out.println("L =="+x.L);        // System.out.println("Lll="+tmpLDU[0]);
System.out.println("U =="+x.U); 
System.out.println("D =="+Array.toString(x.D));
Element[] DiagM=adjLDU.GaussDiag(x.D,    ring);    // ystem.out.println("Dddd-Proved-diag in LDU="+tmpLDU[1]);
MatrixS Diag = new MatrixS(DiagM.length,  DiagM, ring);
System.out.println("DiagM =="+Array.toString(DiagM));
  MatrixS UU=     x.L.multiply(Diag, ring).multiply(x.U, ring) ;
  System.out.println("UU =="+  UU.toString(ring));
MatrixS UU1 = UU.permutationOfRows(MatrixS.transposePermutation(x.Er));

System.out.println("UU1 1=="+  UU1.toString(ring));
int[] ttt= MatrixS.transposePermutation(x.Ec); 
System.out.println("ttt="+Array.toString(ttt)); 
  
  UU1 = UU1.permutationOfColumns(MatrixS.transposePermutation(x.Ec));

System.out.println("UU1 2=="+  UU1.toString(ring));

//System.out.println("U =="+x.U);      System.out.println("Uuu="+tmpLDU[2]);
//System.out.println("E =="+MatrixS.scalarMatrix(AF.size, NumberZ.ONE, ring).multiplyRightE(x.Er, x.Ec));

System.out.println("LDU =="+UU1+"  Er="+Array.toString(x.Er)+"  Ec="+Array.toString(x.Ec));   


MatrixS zero = UU1.subtract(tmp,ring);
System.out.println( zero.isZero(ring) ? "G-O-O-D____________!!!!!": "___B____A_____D______????????"   );    


//   MatrixS[] aa2=  new MatrixS[4]; int[] bb2= new int[]{4,4,4,4};
//        for (int i = 0; i < 4; i++)  aa2[i]=
//                new MatrixS(4, 4, 100, new int[]{3}, new Random(), ring.numberONE, ring);  
//        
//        MatrixS cc2=MatrixS.makeBlockDiagonalMatrixS(aa2,bb2);
//        System.out.println( cc2);
//    MatrixS  Y=  new MatrixS(16, 16, 100, new int[]{3}, new Random(), ring.numberONE, ring);
//    Element [][] MM=new Element[16][0]; int[][] CC=new int[16][0];  
//    for (int i = 0; i < 16; i++) {int nn=0;
//            for (int j = 0; j < Y.col[i].length; j++) {
//               if( Y.col[i][j]<i) nn++;
//            }  Element[] mm=new Element[Y.M[i].length-nn]; 
//               MM[i]=mm; 
//               int[] cc=new int[Y.col[i].length-nn]; int s=0; CC[i]=cc;
//               
//            for (int j = 0; j < Y.col[i].length; j++) {
//               if( Y.col[i][j]>=i) {cc[s]=Y.col[i][j]; mm[s++]=Y.M[i][j];}
//            }
//    }   Y.M=MM; Y.col=CC;
//    MatrixS cc4=Y.deleteBlockDiagonalMatrixS(bb2 ); System.out.println(cc4);
    
//    public static void getMinors(MatrixS A){
//        for (int i = 0; i < A.size; i++) {
//            MatrixS tmp = A.getSubMatrix(0, i, 0, i);
//            Element det = tmp.det(ring);
//            System.out.println("det"+(i+1)+"="+det);
         }
 
}
