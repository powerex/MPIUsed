/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.students.llp2.student.Shcherbinin;

/**
 *
 * @author ridkeim
 */
import java.util.Arrays;
import mpi.*;
public class All {
    public static void main(String[] Args) throws MPIException{
        MPI.Init(Args);
        int myrank = MPI.COMM_WORLD.Rank();
        int np =  MPI.COMM_WORLD.Size();
        int[] n = new int[4];
        int[] k = new int[4];
        for (int i = 0; i < n.length; i++) {
            n[i]=myrank*np+i;
        }
        //!!!! MPI.COMM_WORLD.allToAll(n,1,//!!!! MPI.INT,k,1,//!!!! MPI.INT);
//!!!!        //!!!! MPI.COMM_WORLD.Alltoall(n, 0, 1, //!!!! MPI.INT, k, 0, 1, //!!!! MPI.INT);
        //!!!! MPI.COMM_WORLD.Barrier();
        System.out.println("n "+myrank+" == "+Arrays.toString(n));
        System.out.println("k "+myrank+" == "+Arrays.toString(k));
        //!!!! MPI.Finalize();

    }
}
