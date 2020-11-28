/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.parallel.ddp.MD.examples.invMatrix;

import com.mathpar.parallel.ddp.MD.examples.multiplyDoubleMatrix.DoubleMatrix;
import com.mathpar.parallel.ddp.engine.DispThread;
import mpi.*;

/*
 mpirun C java -cp /home/r1d1/NetBeansProjects/DDPengine/build/classes  DDPMultonMatrix.Main 128
 mpirun C java -cp /home/yuri/r1d1/mpiJava/lib/classes:/home/yuri/NetBeansProjects/DDPengine/build/classes DDPMultonMatrix.Main
 mpirun C java -cp /home/yuri/NetBeansProjects/mathpar/target/classes DDPExamples.InvMatrix.Main 64
 */

public class Main {
    public static void main(String[] args) throws MPIException,InterruptedException{
        /*
         * args[0]- size of matrix
         */
        MPI.Init(args);
        Factory factory=new Factory();
        DispThread disp=new DispThread(1, factory,2, 5, args,null);
        int myRank=MPI.COMM_WORLD.Rank();
//!!!!        int myRank=MPI.COMM_WORLD.Rank();
        if (myRank==0){
            InvMatrixTask t=(InvMatrixTask)disp.GetStartTask();
            DoubleMatrix D=t.A.GetRev();
            System.out.println("DDP done at "+disp.GetExecuteTime());
            if (D.Compare(t.Result)){
                System.out.println("Result is correct");
            }
            else {
                System.out.println("Result is wrong");
            }
        }
        MPI.Finalize();
    }

}
