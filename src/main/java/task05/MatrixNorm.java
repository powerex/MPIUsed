package task05;

import com.mathpar.number.Array;
import com.mathpar.number.Ring;
import com.mathpar.students.llp2.student.helloworldmpi.Transport;
import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;


/*

mpirun --hostfile /home/akozachuk/hostfile -np 2 java -cp /home/akozachuk/mpi-dap-2/target/classes com/mathpar/NAUKMA/course4/Kozachuk/Lab4/NormMatrix 3 4

I'm processor 1
0.0 0.2 0.4 0.6
0.1 0.3 0.5 0.7
0.2 0.4 0.6 0.8
n =2
1.4

rank = 0 res = 1.4
rank = 1 row = [0.2, 0.4, 0.6, 0.8]
rank = 1 res = 1.2000000000000002
1.61245154965971


* */


public class MatrixNorm {
    public static void main(String[] args) throws MPIException, IOException, ClassNotFoundException {
        Ring ring = new Ring("Z[x]");
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        //розмір матриці
        int ord = Integer.parseInt(args[0]);
        //кількість рядків для процесорів з rank > 0
        int k = ord / size;
        //кількість рядків для 0 процесора
        int n = ord - k * (size - 1);
        Integer columnLen = Integer.valueOf(args[1]);
        Integer rowLen = Integer.valueOf(args[0]);
        Double procRes = 0.0;
        if (rank == 0) {
            Double[][] matrix = new Double[rowLen][columnLen];

            for (int i = 0; i < rowLen; i++) {
                for (int j = 0; j < columnLen; j++) {
                    matrix[i][j] = (i + j * 2) / 10.0;
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < columnLen; j++) {
                    Double el = matrix[i][j];
                    procRes += el * el;

                }
            }

            System.out.println("n =" + n);
            System.out.println(procRes);
            //розсилка рядків процесорам
            for (int j = 1; j < size; j++) {
                for (int z = 0; z < k; z++) {
                    System.out.println();
                    Transport.sendObject(matrix[n + (j - 1) * k
                            + z], j, 100 + j);
                }
            }


        } else {
            //програма виконується на процесорі з рангом  = rank
            System.out.println("I'm processor " + rank);
            //отримання рядків матриці від 0 процесора
            Double[][] recvMatrix = new Double[k][ord];
            for (int i = 0; i < k; i++) {
                recvMatrix[i] = (Double[])
                        Transport.recvObject(0, 100 + rank);
                System.out.println("rank = " + rank + " row = "
                        + Array.toString(recvMatrix[i]));
            }

            for (int i = 0; i < k; i++) {
                for (int j = 0; j < columnLen; j++) {
                    Double el = recvMatrix[i][j];
                    procRes += el * el;

                }
            }



        }
        double recvBuf[] = new double[1];
        System.out.println("rank = " + rank + " res = "
                + procRes);
        double[] arr = new double[1];
        arr[0] = procRes;
        MPI.COMM_WORLD.Reduce(arr,0, recvBuf, 0, 1, MPI.DOUBLE, MPI.SUM, 0);

        if (rank == 0) {
            System.out.println(Math.sqrt(recvBuf[0]));

        }

        MPI.Finalize();
    }


}