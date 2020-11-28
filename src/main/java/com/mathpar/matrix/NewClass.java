package com.mathpar.matrix;


import com.mathpar.matrix.LDU.ETD;
import com.mathpar.matrix.*;
import com.mathpar.number.*;
import java.util.Random;



public class NewClass { 
    public static void main(String[] args) throws Exception {
        Ring ring = new Ring("Z[x]");
        int n = 4;
        int[] rndType = new int[]{3};
        Random rnd = new Random();
        MatrixS A = null;
        do {
            A = new MatrixS(n, n, 100, rndType, rnd, ring.numberONE, ring);
        } while(A.det(ring).isZero(ring));
       
        long[] ll= {2,3,4,5}; long[] l1= {1,1,-1,-1};
        Element[] ee=(new VectorS(ll,ring)).V;

        A= MatrixS.diagonalMatrixSecond( ee, ring);
         System.out.println("inp-martix="+A);
        VectorS w =new VectorS(l1,ring);
        new VectorS(n, 100, rndType, rnd, ring);
        
        
        MatrixS[] array = ETD.ETDmodLDUWDK(A);
        if(array == null) {System.out.println("exit");       System.exit(0); }
        
        MatrixS L = array[0];
        MatrixS D = array[1];
        
        Element[] dets = new Element[n];
        for (int i = 0; i < n; i++) { System.out.println("i,j=="+ D.col[i] [0]+"  "+D.col[i][0]);
            dets[i] = L.M[D.col[i] [0]]  [L.M[D.col[i][0]].length - 1];
        }
        
        MatrixS W = array[3];
        MatrixS Da = (MatrixS) array[4].multiply(dets[n - 1], ring);
        MatrixS K = array[5];
     //   System.out.println("K = " + K);    System.out.println("W = " + W);   
            System.out.println("K = " + K);    System.out.println("W = " + W);   
         System.out.println("Da = " + Da); 
         MatrixS pp=W.multiply(Da, ring).multiply(K, ring).multiply(A, ring);
         System.out.println("pp = " + pp);
        MatrixS Rev=W.multiply(Da, ring).multiply(K, ring); System.out.println("Rev = " + Rev);
        MatrixS Rev1=Rev.multiply(A, ring) ; System.out.println("DET1 = " + Rev1);
        VectorS T = multiply1(K, w, ring);
        T = multiply2(Da, T, ring);
        T = multiply3(W, T, dets, ring);
        System.out.println("T = " + T);
        
        Element adj = A.adjoint(ring);
        adj = adj.multiply(w, ring);
        System.out.println("adj*w = " + adj);
        System.out.println("sub is zero " + adj.subtract(T, ring).isZero(ring));
    }

    static VectorS multiply1(MatrixS K, VectorS w, Ring ring) {
        int n = w.V.length;
        VectorS res = new VectorS(n, ring.numberZERO);
        for (int i = 0; i < K.M.length; i++) {
            for (int j = 0; j < K.M[i].length; j++) {
                res.V[i] = res.V[i].add(K.M[i][j].multiply(w.V[K.col[i][j]], ring), ring);
            }
        }
        return res;
    }
    
    static VectorS multiply2(MatrixS D, VectorS w, Ring ring) {
        int n = w.V.length;
        VectorS res = new VectorS(n, ring.numberZERO);
        for (int i = 0; i < n; i++) {
            res.V[i] = D.M[i][0].multiply(w.V[D.col[i][0]], ring);
        }
        return res;
    }
    
    static VectorS multiply3(MatrixS W, VectorS w, Element[] dets, Ring ring) {
        int n = w.V.length;
        VectorS res = new VectorS(n, ring.numberZERO);
        Element numb1 = dets[n - 1].multiply(dets[n - 2], ring);
        Element numb2 = numb1.multiply(w.V[n - 1], ring);
        Element numb3 = numb1.multiply(w.V[n - 2], ring);
        
        
        for (int i = 0; i < n; i++) {
            if(W.M[i].length == 1) {
                res.V[i] = w.V[W.col[i][0]].multiply(W.M[i][0], ring);
                continue;
            }
            Element mult = numb1;
            res.V[i] = numb1.multiply(w.V[W.col[i][W.col[i].length - 1]], ring).multiply(W.M[i][W.M[i].length - 1], ring);
            res.V[i] = res.V[i].add(numb1.multiply(w.V[W.col[i][W.col[i].length - 2]], ring).multiply(W.M[i][W.M[i].length - 2], ring), ring);
            
            if(W.M[i].length == 2) {
                res.V[i] = res.V[i].divide(mult, ring);
                continue;
            }
            
            for (int j = W.M[i].length - 3; j >= 0; j--) {
                res.V[i] = res.V[i].add(w.V[W.col[i][j]].multiply(mult.multiply(W.M[i][j], ring), ring), ring);
                if(j > 0) {
                    res.V[i] = res.V[i].multiply(dets[j], ring);
                }
                res.V[i] = res.V[i].divide(mult, ring);
                mult = dets[j];
            }
        }
        
        return res;
    }

}
