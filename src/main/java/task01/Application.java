package task01;

/*
Напишіть програму для пересилання масиву чисел з процесора
номер 2 іншим процесорам групи. Протестуйте програму на 4,8,12 процесорах
 */

import mpi.MPI;

import java.util.Random;

public class Application {

    public static final int N = 5;

    public static void main(String[] args) {

        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int np = MPI.COMM_WORLD.Size();
        double[] a = new double[N];
        MPI.COMM_WORLD.Barrier();
        final int SOURCE = 1;
        if (rank == SOURCE) {
            System.out.println("Generated data");
            for (int i = 0; i < N; i++) {
                a[i] = (new Random()).nextDouble();
                System.out.printf("a[%d] = %.04f\t", i, a[i]);
            }
            System.out.println();

            for (int i = 0; i < np; i++) {
                if (i== SOURCE) continue;
                MPI.COMM_WORLD.Send(a, 0, N, MPI.DOUBLE, i, 3000);
            }
            System.out.println("Data from " + rank + " successfully sends" + "\n");
        } else {
            MPI.COMM_WORLD.Recv(a, 0, N, MPI.DOUBLE, SOURCE, 3000);
            for (int i = 0; i < N; i++) {
                System.out.printf("P{%d}\ta[%d] = %.04f\n", rank, i, a[i]);
            }
            System.out.println();
            System.out.println("Data received on " + rank);
        }

        MPI.Finalize();
    }

}
