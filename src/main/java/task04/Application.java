package task04;

/*
Напишіть програму для пересилання масиву чисел iз
процесора номер 2 іншим процесорам групи. Причому
процесор 0 повинен отримати одне число, процесор 1 – два
числа, процесор 2 – чотири числа i так далі. Протестуйте
програму на 4, 8, 12 процесорах
*/

import mpi.MPI;

import java.util.Arrays;

public class Application {

    public static void main(String[] args) {
        final int SOURCE = 2;

        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int np = MPI.COMM_WORLD.Size();
        int arraySize = (1 << np) - 1; // побітовий зсув вліво 00001 -> 00010

        // масив кількості елементів на передачу
        int[] sendcount = new int[np];
        for (int i = 0; i < np; i++) {
            sendcount[i] = 1 << i;
        }

        // масив зміщень номерів у масиві на відправку
        int[] displs = new int[np];
        for (int i = 0; i < np; i++) {
            displs[i] = (1 << i)  - 1;
        }

        int[] a = new int[arraySize]; // дані на відправку
        if (rank == SOURCE) {
            for (int i = 0; i < a.length; i++) a[i] = i+1;
            System.out.println("rank = " + rank + ": a = " + Arrays.toString(a));
        }
        int[] q = new int[1 << (rank)]; // масив для отримання даних
        MPI.COMM_WORLD.Barrier();
        MPI.COMM_WORLD.Scatterv(a, 0, sendcount, displs, MPI.INT,
                                q, 0, sendcount[rank], MPI.INT, SOURCE);
        System.out.println("rank = " + rank + ": q = " + Arrays.toString(q));
        MPI.Finalize();
    }
}
