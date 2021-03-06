/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.parallel.ddp.MD.examples.multiplyMatrix;
/**
 *
 mpirun C java -cp /home/r1d1/NetBeansProjects/mathpar/target/classes DDPExamples.MultiplyMatrix.Main 64

 */
import com.mathpar.parallel.ddp.engine.DispThread;



import com.mathpar.matrix.*;
import mpi.*;
import com.mathpar.number.*;

// mpirun -np 4 java -cp /home/r1d1/NetBeansProjects/mathpar/target/classes parallel.ddp.examples.multiplyMatrix.Main 64
public class Main {
    public static void main(String[] args) throws MPIException, InterruptedException {

        MPI.Init(args);

        FactoryMultiplyMatrix f = new FactoryMultiplyMatrix();
        DispThread disp = new DispThread(0, f, 2, 10, args,null);
        TaskMultiplyMatrix ab = (TaskMultiplyMatrix)disp.GetStartTask();
        int myrank=MPI.COMM_WORLD.Rank();
//!!!!        int myrank=MPI.COMM_WORLD.Rank();
        if (myrank==0){
//            System.out.println("ab.a="+ab.a);
//            System.out.println("ab.b="+ab.b);
//            System.out.println("ab.c="+ab.c);
              MatrixS res = ab.a.multiply(ab.b,Ring.ringZxyz);
//            System.out.println("res="+res);

            MatrixS sub = ab.c.subtract(res, Ring.ringZxyz);


            if (sub.isZero(Ring.ringZxyz)) {
               System.out.println("ok");
            } else{
                System.out.println("error");
           }
        }
        MPI.Finalize();
    }
}
