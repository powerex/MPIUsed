package task03;

/*
Напишiть програму для збору масиву чисел з усiх процесорiв
на процесорi номер 3. Причому процесор 0 пересилає п’ять
чисел, процесор 1 – десять чисел, процесор 2 – п’ятнадцять
чисел i так далi. Протестуйте програму на 4, 8, 12 процесорах.

0 -> 5  1     5  1
1 -> 10 2    15  3
2 -> 15 3    30  6
3 -> 20 4    50  10

*/

import mpi.MPI;

import java.util.Arrays;
import java.util.Random;

public class Application {
    public static final int N = 5;
    public static final Random random = new Random();

    public static void main(String[] args) {

        final int DESTINATION = 3;

        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int np = MPI.COMM_WORLD.Size();

        // масив кількості елементів на прийомі
        int[] recvcount = new int[np];
        for (int i = 0; i < np; i++) {
            recvcount[i] = N + N * i;
        }

        // масив зміщень номерів у результуючому масиві
        int[] displs = new int[np];
        displs[0] = 0;
        for (int i = 1; i < np; i++) {
            displs[i] = displs[i - 1] + i * N;
        }

        int arraySize = N + N * rank;
        int[] a = new int[arraySize];
        for (int i = 0; i < arraySize; i++)
            a[i] = random.nextInt(20);
        System.out.println("rank = " + rank + " a = " + Arrays.toString(a));
        int[] q = new int[sumToNumber(np) * N];
        MPI.COMM_WORLD.Gatherv(a, 0, arraySize, MPI.INT, q, 0, recvcount, displs, MPI.INT, DESTINATION);
        if (rank == DESTINATION) {
            System.out.println();
            for (int i=0; i<q.length; i++) {
                System.out.print(String.format("%4d", q[i]));
                if ((i+1) % 40 == 0)
                    System.out.println();
            }
        }
        MPI.Finalize();
    }

    public static int sumToNumber(int n) {
        int res = 0;
        for (int i = 1; i <= n; ++i)
            res += i;
        return res;
    }
}
