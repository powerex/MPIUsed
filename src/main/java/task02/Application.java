package task02;

/*
Напишіть програму для збору масиву чисел з усіх процесорів
на процесорі номер 1. Протестуйте програму на 4, 8, 12
процесорах.
 */

import mpi.MPI;
import java.util.Arrays;

public class Application {

    public static final int N = 5;

    public static void main(String[] args) {

        final int DESTINATION = 1;

        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int np = MPI.COMM_WORLD.Size();
        int[] a = new int[N];
        for (int i = 0; i < N; i++)
            a[i] = rank;
        System.out.println("rank = " + rank + " : a = " + Arrays.toString(a));
        int[] q = new int[N * np];
        MPI.COMM_WORLD.Gather(a, 0, N, MPI.INT, q, 0, N, MPI.INT, DESTINATION);
        if (rank == DESTINATION)
            System.out.println("rank = " + rank + " : q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}
