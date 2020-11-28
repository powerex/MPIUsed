package task05;

/*

норма матриці

1  -2   3   -4      SUM (1 + 2 + 3 + 4) = 10
3   4  -1   -2      SUM (3 + 4 + 1 + 2) = 10
3  -1   2    1      SUM (3 + 1 + 2 + 1) = 7

MAX (10, 10, 7) = 10


 */

import mpi.MPI;
import mpi.Status;

import java.util.Random;

public class Norm {

    public static final Random random = new Random();

    public static final int M = 7;
    public static final int N = 10;

    public static void main(String[] args) {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int np = MPI.COMM_WORLD.Size();

        if (rank == 0) {
            int[][] a = new int[N][M];
            // Array generation
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    a[i][j] = random.nextInt(21) - 10;
                    System.out.print(String.format("%4d", a[i][j]));
                }
                System.out.println();
            }

            //Rows sending
            for (int i = 0; i < N; i++) {
                //без врахування і 0
                row_send(i, i % (np - 1) + 1);

                MPI.COMM_WORLD.Send(
                        a[i].toString().getBytes(),
                        0,
                        a[i].toString().getBytes().length,
                        MPI.BYTE,
                        i % (np - 1) + 1,
                        3000);

            }

            // Gathering data


            //Find maximum

            //Print result

        } else {
            // Receiving data
            Status st = MPI.COMM_WORLD.Probe(0, 3000);
            int size = st.Get_count(MPI.BYTE);
            byte[] tmp = new byte[size];
            MPI.COMM_WORLD.Recv(tmp, 0, size, MPI.BYTE, 0, 3000);

            for (int i=0; i< tmp.length; i++) {
                System.out.print(tmp[i]);
            }
            System.out.println();
            //Find sum

            // Sending data
        }

        MPI.Finalize();
    }

    private static void row_send(int i, int i1) {

    }

}
