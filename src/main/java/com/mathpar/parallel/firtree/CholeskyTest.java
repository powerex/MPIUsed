/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.parallel.firtree;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.Element;
import com.mathpar.number.Ring;
import java.util.Random;
import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author alla
 */
public class CholeskyTest {
    public static void main(String[]args) throws MPIException{
        
        
                MPI.Init(args);
                int rank = MPI.COMM_WORLD.Rank();
                
                Firtree f = new Firtree();
              
                Ring ring = new Ring("R64[]"); 
                MatrixS A = Tools.initForCholesky(4, ring);
                MatrixS [] init = new MatrixS[]{A};
                
                DispThread disp = new DispThread(5, f, 1000, args, init, ring);

                if (rank==0)
                    System.out.println("A= "+ A);
        
                 
        MPI.Finalize();
	}
    
}
