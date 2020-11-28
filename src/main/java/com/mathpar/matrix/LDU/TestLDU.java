/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.matrix.LDU;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.Element;
import com.mathpar.number.Newton;
import com.mathpar.number.NumberR64;
import com.mathpar.number.NumberZ;
import com.mathpar.number.Ring;
import com.mathpar.students.llp2.student.lyanok.mpimyrpoject;
import com.mathpar.students.student.yakovleva.math;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpi.MPI;
import mpi.MPIException;

/**
 *
 * @author ridkeim
 */
public class TestLDU {
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, MPIException{
//        MPI.Init(args);
        test1(args);
//        MPI.Finalize();
    }
    public static void test1(String[] args) throws IOException {
//        Element r = new  NumberR64("32");
    }
    
    public static void test(String[] args) throws MPIException, IOException{
        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();
        int size1 = 100;
        if(args.length == 1){
                size1= Integer.valueOf(args[0]);
        }
        Random m = new Random();
        
        Integer[] data1 = new Integer[size1];
        
        int  count = size1;//m.nextInt(1000000);
        for (int j = 0; j< count; j++) {
            data1[j] = m.nextInt();
        }
        
//        MPI.COMM_WORLD.Barrier();
        long time =  System.currentTimeMillis();
//        List<Integer> rp = ETDmpiUtils.gatherv(data1, 0);
//        time = System.currentTimeMillis()-time;
//        System.out.println("normal on proc "+rank+"= "+time);
//        rp = null;
//        System.gc();
        MPI.COMM_WORLD.Barrier();
        time =  System.currentTimeMillis();
        List<Integer[]> gathervSeparated = ETDmpiUtils.gathervSeparated(data1, 0);
        time = System.currentTimeMillis()-time;
        System.out.println("separated on proc "+rank+"= "+time);
    }

    
}