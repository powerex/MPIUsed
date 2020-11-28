/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.parallel.webCluster.test;


import mpi.*;
import com.mathpar.number.Element;
import com.mathpar.parallel.webCluster.engine.QueryResult;
import com.mathpar.parallel.webCluster.engine.Tools;

/**
 *
 * @author r1d1
 */
public class Test {
    public static void main(String[] args)throws MPIException {
        MPI.Init(args);
        QueryResult queryRes=Tools.getDataFromClusterRootNode(args);
        int myRank=MPI.COMM_WORLD.Rank();
        if (myRank == 0) {
            Object []ar=queryRes.getData();
            System.out.println("test...");
            for (int i=0; i<ar.length; i++){
                System.out.println(((Element)ar[i]).intValue());
            }
        }
        Tools.sendFinishMessage(args);
        MPI.Finalize();
    }
}
