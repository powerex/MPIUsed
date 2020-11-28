package com.mathpar.students.llp2.students;

import com.mathpar.students.llp2.student.helloworldmpi.*;
import java.util.Random;
import com.mathpar.parallel.utils.parallel_debugger.ParallelDebug;
import com.mathpar.matrix.MatrixS;
import mpi.*;
import com.mathpar.number.*;
import com.mathpar.number.Ring;
import com.mathpar.polynom.Polynom;
//mpirun C java -cp /home/mixail/mathpar/target/classes -Djava.library.path=$LD_LIBRARY_PATH llp2.student.krivencev
//mpirun C java -cp /home/mixail/mathpar/build/web/WEB-INF/classes -Djava.library.path=$LD_LIBRARY_PATH llp2.student.krivencev

public class krivencev {
    static public void main(String[] args) throws MPIException {
        MPI.Init(new String[0]);
        int rank = MPI.COMM_WORLD.Rank();
        int p = MPI.COMM_WORLD.Size();
        Random ran = new Random();
        int tag = 0;
        Ring ring = new Ring("R[x]");
        Polynom one = new Polynom(NumberR.ONE);
        int[] randomType = new int[] {3, 0, 100, 3};
        ParallelDebug pred = new ParallelDebug(MPI.COMM_WORLD);
        MatrixS G = new MatrixS(4, 4, 100, randomType, ran, one, ring);
        MatrixS[] DD = new MatrixS[8];
        MatrixS[] FF = new MatrixS[8];
        MatrixS[] BB = new MatrixS[8];
        MatrixS CC = null;// co6iraet resultat } catch (Exception ex) {
        // pred.paddEvent("отсылка", "текст");
        if (rank == 0) {
            System.out.println("G" + G);
            MatrixS[] AA = G.split();
            DD[0] = AA[3].integrate(ring);
            for (int i = 1; i < p; i++) {
                /* Отсылка */
                Transport.sendObject(AA[i], i, tag);// отослали 1 процесору
                pred.paddEvent("отсылка", "" + AA[i]);
            }
            /* Прием */
            DD[1] = (MatrixS) Transport.recvObject(1, tag);
            DD[2] = (MatrixS) Transport.recvObject(2, tag);
            DD[3] = (MatrixS) Transport.recvObject(3, tag);
            // pred.paddEvent("Прием", "" + DD[1]);
            CC = MatrixS.join(DD);
            System.out.println("RES= " + CC.toString());
        } else {
            /* Прием */
            BB[0] = (MatrixS) Transport.recvObject(0, tag);
            pred.paddEvent("Принял", "" + BB[0]);
            FF[1] = BB[0].integrate(ring);
            pred.paddEvent("Проинтигрировали", "" + FF[1]);
            Transport.sendObject(FF[1], 0, tag);// отослали 0 процесору
        }
        pred.generateDebugLog();
        //!!!! MPI.Finalize();
    }
}
