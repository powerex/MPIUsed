package com.mathpar.parallel.webCluster.algorithms.Adjoint;

import java.io.IOException;
import java.util.Random;
import com.mathpar.matrix.*;
import mpi.*;
import com.mathpar.number.*;
import com.mathpar.number.NFunctionZ32;
import com.mathpar.parallel.utils.MPITransport;
import com.mathpar.parallel.webCluster.engine.QueryCreator;
import com.mathpar.parallel.webCluster.engine.Tools;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

//import static com.mathpar.parallel.webCluster.algorithms.Adjoint.TestClass.numTaskForAll;

/**
 *
 * @author khvorov
 */
public class AdjointDetParallel {

    public static int NumbOfPrimes(MatrixS A, Ring ring) throws IOException, MPIException {
        int size = MPI.COMM_WORLD.Size();
        Element adamarN = A.transpose().adamarNumberNotZero(ring);
        NFunctionZ32.doStaticPrimesProdAndInd();// создаем список модулей
        int line = 1;
        int k = 0;
        for (int i = 0; line < adamarN.intValue(); i++) {
            line = line * NFunctionZ32.Primes[i];
            k++;
        }
        while (k < size || k % size != 0) {
            k++;
        }
        return k;
    }
    
    /**
     * 
     * @param taskLen
     * @param size
     * @param rank
     * @return 
     */
    public static int numTaskForAll(int taskLen, int size, int rank) {
        return (rank < (size - taskLen % size)) ? taskLen / size : (taskLen / size) + 1;
    }
    /**
     * 
     * @param A
     * @param maxNumberForSearch
     * @param firstNumber
     * @param lastNumber
     * @param ring
     * @return
     * @throws IOException
     * @throws MPIException
     * @throws Exception 
     */
    public static int[] primesForAdjointWithoutPrimesList(MatrixS A,int maxNumberForSearch, int firstNumber, int lastNumber, Ring ring) throws IOException, MPIException, Exception {
        NumberZ adamarN = (NumberZ) A.transpose().adamarNumberNotZero(ring);
        NumberZ line = NumberZ.ONE;
        PrimeGenerator primeGen = new PrimeGenerator(maxNumberForSearch); //2147483629
        int[] primesBlock = primeGen.generatePrimesIntVer(firstNumber, lastNumber); //2147418127, 2147483629
        int k = 0;
        int l = 0;
        for (int i = 0; adamarN.compareTo(line, 2, ring); i++) {
            line = line.multiply(new NumberZ(primesBlock[i]));
            l = i;
            k++;
        }
        while (k < MPI.COMM_WORLD.Size()) {
            k++;
        }
        int[] primes = new int[k];
        System.arraycopy(primesBlock, 0, primes, 0, k);
        return primes;
    }

    public static int[] primesForAdjoint(MatrixS A, int numBlock, Ring ring) throws IOException, MPIException {
        NumberZ adamarN = (NumberZ) A.transpose().adamarNumberNotZero(ring);
        NumberZ line = NumberZ.ONE;
        int k = 0;
        int l = 0;
        int[] block = NFunctionZ32.readBlockOfPrimesFromBack(numBlock, ring);
        for (int i = block.length - 1; adamarN.compareTo(line, 2, ring); i--) {
            line = line.multiply(new NumberZ(block[i]));
            l = i;
            k++;
        }
        int[] primes = new int[k];
        System.arraycopy(block, 0, primes, 0, k);
        return primes;
    }

    public static void gathervObjectArray(Object[] sendBuf, Object[] recvBuf, int root) throws MPIException, IOException, ClassNotFoundException {
        byte[] sendbuffer = null;
        int[] sendcount = new int[1];
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int[] recvcount = new int[size];
        int[] rdispls = new int[size];
        Object[][] recvTmp = new Object[size][sendBuf.length];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        for (int i = 0; i < sendBuf.length; i++) {
            oos.writeObject(sendBuf);
        }
        sendbuffer = bos.toByteArray();
        sendcount[0] = sendbuffer.length;
        MPI.COMM_WORLD.Allgather(sendcount, 0, 1, MPI.INT, recvcount, 0, 1, MPI.INT);
        int recvSize = 0;
        for (int i = 0; i < recvcount.length; i++) {
            rdispls[i] = recvSize;
            recvSize = recvSize + recvcount[i];
        }
        byte[] recvbuffer = new byte[recvSize];
        MPI.COMM_WORLD.gatherv(sendbuffer, sendbuffer.length, MPI.BYTE, recvbuffer, recvcount, rdispls, MPI.BYTE, root);
        if (rank == root) {
            for (int i = 0; i < recvcount.length; i++) {
                ByteArrayInputStream bis = new ByteArrayInputStream(recvbuffer, rdispls[i], recvcount[i]);
                ObjectInputStream ois = new ObjectInputStream(bis);
                Object rec = ois.readObject();
                recvTmp[i] = (Object[]) rec;
            }
        }
        if (rank == root) {
            for (int i = 0; i < recvTmp.length; i++) {
                int[] inter = getMyInterval(recvBuf.length, size, i);
                System.arraycopy(recvTmp[i], 0, recvBuf, inter[0], recvTmp[i].length);
            }
        }
        recvTmp = null;
    }

    public static int[] primesForMatrix(int numbOfPrimes, int posFromBigList) {
        int[] primes = new int[numbOfPrimes];
        System.arraycopy(NFunctionZ32.Primes, posFromBigList, primes, 0, numbOfPrimes);
        return primes;
    }

    /**
     *
     * @param n - количество задач
     * @param partNumb - количество частей, на которые нужно поделить задачи
     * @param myrank - номер текущего процессора
     *
     * @return интервал, с какой и по какую задачу следует брать текущему
     * процессору
     */
    public static int[] getMyInterval(int n, int partNumb, int myrank) {
        int[] interval = new int[2];
        int numb_proc = n / partNumb;
        int ost = n % partNumb;
        interval[0] = (myrank < (partNumb - ost)) ? (myrank * numb_proc) : myrank * numb_proc + (myrank - (partNumb - ost));
        interval[1] = (myrank < (partNumb - ost)) ? ((myrank * numb_proc) + numb_proc) : myrank * numb_proc + (myrank - (partNumb - ost)) + (numb_proc + 1);
        return interval;
    }

    /**
     * транспонирование целочисленной матрицы
     *
     * @param A - целочисленная матрица
     *
     * @return транспонированная матрица
     */
    public static int[][] intTransposition(int[][] A) {
        int[][] transp = new int[A[0].length][A.length];
        for (int i = 0; i < A[0].length; i++) {
            for (int j = 0; j < A.length; j++) {
                transp[i][j] = A[j][i];
            }
        }
        return transp;
    }

    public static NumberZ algorithmGarner(int[] mod, int[] rem, NumberZ[][] inverses) {
        int[] modMul = new int[mod.length];
        modMul[0] = mod[0];
        for (int i = 1; i < modMul.length; i++) {
            modMul[i] = modMul[i - 1] * mod[i];
        }
        int[] x = new int[mod.length];
        for (int i = 0; i < mod.length; i++) {
            x[i] = rem[i];
            for (int j = 0; j < i; j++) {
                x[i] = inverses[j][i].intValue() * (x[i] - x[j]);
                x[i] = x[i] % mod[i];
                if (x[i] < 0) {
                    x[i] += mod[i];
                }
            }
        }
        int X = x[0];
        for (int i = 1; i < x.length; i++) {
            X += x[i] * modMul[i - 1];
        }
        return new NumberZ(X);
    }

    public static NumberZ algorithmGarnerNumberZ(int[] mod, int[] rem, NumberZ[][] inverses) {
        int[] modMul = new int[mod.length];
        modMul[0] = mod[0];
        for (int i = 1; i < modMul.length; i++) {
            modMul[i] = modMul[i - 1] * mod[i];
        }
        NumberZ[] x = new NumberZ[mod.length];
        for (int i = 0; i < mod.length; i++) {
            x[i] = new NumberZ(rem[i]);
            for (int j = 0; j < i; j++) {
                x[i] = inverses[j][i].multiply(x[i].subtract(x[j]));
                x[i] = x[i].mod(new NumberZ(mod[i]));
                if (x[i].compareTo(NumberZ.ZERO, -2, Ring.ringZpX)) {
                    x[i] = x[i].add(new NumberZ(mod[i]));
                }
            }
        }
        NumberZ X = x[0];
        for (int i = 1; i < x.length; i++) {
            X = X.add(x[i].multiply(new NumberZ(modMul[i - 1])));
        }
        return X;
    }

    public static NumberZ[][] generateInverses(int[] mod) {
        NumberZ[][] inverses = new NumberZ[mod.length][mod.length];
        for (int i = 1; i < mod.length; i++) {
            long currentMod = mod[i];
            Ring ring = new Ring("Zp32");
            ring.MOD32 = currentMod;
            for (int j = 0; j < i; j++) {
                NumberZp32 temp = (new NumberZp32(mod[j])).inverse(ring);
                inverses[j][i] = (NumberZ) temp.toNumber(Ring.Z, new Ring("Z"));
            }
        }
        return inverses;
    }

    public static NumberZ[] generateZ(int[] mod) {
        NumberZ[] z = new NumberZ[mod.length - 1];
        for (int i = 0; i < z.length; i++) {
            z[i] = NumberZ.ONE;
        }
        for (int i = 1; i < mod.length; i++) {
            long currentMod = mod[i];
            Ring ring = new Ring("Zp32");
            ring.MOD32 = currentMod;
            for (int j = 0; j < i; j++) {
                NumberZp32 temp = (new NumberZp32(mod[j])).inverse(ring);
                z[i - 1] = z[i - 1].multiply((NumberZ) temp.toNumber(Ring.Z, new Ring("Z")));
            }
        }
        for (int i = 0; i < z.length; i++) {
            z[i] = z[i].mod(new NumberZ(mod[i + 1]));
        }
        return z;
    }
    
        public static MatrixS sendInitialMatrix(MatrixS A, Ring ring) throws MPIException, IOException, ClassNotFoundException {
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        if (rank == 0) {
            for (int i = 1; i < size; i++) {
                MPITransport.sendObject(A, i, 1);
            }
            return A;
        } else {
            MatrixS matr = (MatrixS) MPITransport.recvObject(0, 1);

            return matr;
        }
    }
    
      
/**
 * Вычисление присоединенных матриц и определителей в конечных полях на каждом процессоре
 * @param A - исходная матрица
 * @param adjZp - массив присоединенных матриц
 * @param detZp - массив определителей
 * @param rank - номер текущего процессора
 * @param size - общее количество процессоров
 * @param primes - массив простых модулей
 * @param ring - кольцо целых чисел
 */
    public static void adjointDetZp(MatrixS A, MatrixS[] adjZp, int[] detZp, int rank, int size, int[] primes, Ring ring) {
        Ring ringZp = new Ring("Zp32[x]");
        MatrixS Azp;
        int primesLen = primes.length;
        int[] interval = getMyInterval(primesLen, size, rank);//интервал из списка primes
        int k = 0;
        for (int i = interval[0]; i < interval[1]; i++) {
            ringZp.setMOD32(primes[i]);
            Azp = (MatrixS) A.toNewRing(Ring.Zp32, ringZp);//переводим матрицу А к кольцу по модулю primes[i] 
            Element[] c = new Element[] {ringZp.numberONE};
            System.out.println(" ONE=="+c[0]+"   RING========"+ringZp);
            adjZp[k] = Azp.adjointDet(c, ringZp);//записываем присоединенную матрицу по модулю primes[i]
        System.out.println(" DET=="+c[0] );
            detZp[k] = c[0].intValue();
            k++;
            
        }
    }
/**
     *
     * @param A - исходная матрица типа matrixS
     * @param rank - номер текущего процессора
     * @param size - количество процессоров
     * @param primes - список модулей
     * @param flag - если flag = true, то метод вычисляет присоединенные матрицы
     * по модулям из списка primes, если flag = false, то определители по
     * модулям.
     * @param ring - кольцо Z
     *
     * @return Массив присоединенных матриц и определителей, посчитанных на
     * одном процессоре в кольце Zp, где p - просто число из списка primes.
     * Каждой присоединенной матрицы соответствует свой простой модуль.
     *    ANTOSHA
     * @throws IOException
     */
    public static MatrixS[] adjointDetZp(MatrixS A, int rank, int size, int[] primes, boolean flag, Ring ring) throws IOException {
       // Done by Antosha for Antosha test = 
        Ring ringZp = new Ring("Zp32[x]");
        MatrixS Azp;
        int primesLen = primes.length;
        int[] interval = getMyInterval(primesLen, size, rank);//интервал из списка primes
        int numPrimesforProc = (rank < (size - primesLen % size)) ? primesLen / size : primesLen / size + 1;//число модулей на процессор
        MatrixS[] matrZp = new MatrixS[numPrimesforProc];//присоединенные матрицы по модулям
        if (flag == true) {
            int k = 0;
            for (int i = interval[0]; i < interval[1]; i++) {
                ringZp.setMOD32(primes[i]);
                Azp = (MatrixS) A.toNewRing(Ring.Zp32, ringZp);//переводим матрицу А к кольцу по модулю primes[i] 
                //System.out.println("Azp = " + Azp.toString(ring) + " rank= " + rank);
                Element[] c = new Element[] {ringZp.numberONE};
                matrZp[k] = Azp.adjointDet(c, ringZp);//записываем присоединенную матрицу по модулю primes[i] 
               // System.out.println("adjZp = " + matrZp[k] + " rank = " + rank+ " prime= "+primes[i]);
                k++;
                System.out.println("i rank = "+rank + " out adjointDetZp");
            }
        } else {
            int k = 0;
            for (int i = interval[0]; i < interval[1]; i++) {
                ringZp.setMOD32(primes[i]);
                Azp = (MatrixS) A.toNewRing(Ring.Zp32, ringZp);//переводим матрицу А к кольцу по модулю primes[i]
               // System.out.println("Azp = " + Azp.toString(ring) + " rank= " + rank + " num=" + k + " prime=" + primes[i] + " ");
                if (A.rank(ring) != Azp.rank(ringZp)) {//проверяем совпадают ли ранги исходной матрицы и новой
                    matrZp[k] = new MatrixS(ringZp.numberZERO);
                } else {
                    Element[] c = new Element[] {ringZp.numberONE};
                    MatrixS mtr = Azp.adjointDet(c, ringZp);
                    //System.out.println("Adjoint = "+mtr+" rank="+rank);
                    matrZp[k] = new MatrixS(c[0]);// записываем определитель по модулю primes[i]
                    
                }
                k++;
            }
        }
        return matrZp;
    }
    /**
     * Рассылка строк присоединенных матриц в кольце Zp между процессорами для
     * последующего их восстановления
     *
     * @param matrZp - массив присоединенных матриц
     * @param detZp - массив определителей
     * @param rank - номер текущего процессора
     * @param size - количество процессоров
     * @param primes - массив простых модулей
     *
     * @return массив строк
     *
     * @throws MPIException
     */
 
    public static IntBuffer sendRowBetweenProc(MatrixS[] matrZp, int[] detZp, int[] primes, int rank, int size) throws MPIException, IOException, ClassNotFoundException {
        int[] sdispls = new int[size];
        int[] sendcount = new int[size];
        int matr_len = matrZp[0].size;
        int det_len = detZp.length;
        MatrixS[] subM = new MatrixS[matrZp.length * size];
        int k = 0;
        for (int i = 0; i < size; i++) {
            int[] interval_i = getMyInterval(matr_len, size, i);
            for (int j = 0; j < matrZp.length; j++) {
                subM[k] = matrZp[j].getSubMatrix(interval_i[0], interval_i[1] - 1, 0, matrZp[j].colNumb);
                k++;
            }

        }
        int allElRow = 0;
        for (int i = 0; i < subM.length; i++) {
            allElRow = allElRow + subM[i].size * (subM[i].colNumb - 1);

        }
        int o = 0;
        int y = 0;
        for (int i = 0; i < subM.length && o < size; i++) {
            if(o == 0){
                sendcount[o] = sendcount[o] + subM[i].size * (subM[i].colNumb - 1);
            }else{
            sendcount[o] = sendcount[o] + subM[i].size * (subM[i].colNumb - 1);
            }
            y++;
            if (y == subM.length / size) {
                o++;
                y = 0;
            }

        }
        sendcount[0] = sendcount[0] + +det_len;
        for (int i = 1; i < size; i++) {
            sdispls[i] = sdispls[i - 1] + sendcount[i - 1];
        }
        int[] recvcount = new int[size];
        MPI.COMM_WORLD.allToAll(sendcount, 1, MPI.INT, recvcount, 1, MPI.INT);
        int[] rdispls = new int[size];
        for (int i = 1; i < size; i++) {
            rdispls[i] = rdispls[i - 1] + recvcount[i - 1];
        }
        IntBuffer sendRow = MPI.newIntBuffer(allElRow+det_len);
        for(int i = 0; i < det_len; i++){
            sendRow.put(detZp[i]);
        }
        int t = det_len;
        for (int i = 0; i < subM.length; i++) {
            for (int j = 0; j < subM[i].size; j++) {
                for (int l = 0; l < subM[i].colNumb - 1; l++) {
                    sendRow.put(t, (subM[i].getElement(j, l, Ring.ringZxyz).intValue()));
                    t++;
                }
            }
        }
        subM = null;
        int allRecv = 0;
        for (int i = 0; i < recvcount.length; i++) {
            allRecv += recvcount[i];
        }
        IntBuffer resRow = MPI.newIntBuffer(allRecv);
        //int[] resRow = new int[allRecv];
        MPI.COMM_WORLD.Barrier();
        try {
            MPI.COMM_WORLD.allToAllv(sendRow, sendcount, sdispls, MPI.INT, resRow, recvcount, rdispls, MPI.INT);
        } catch (Exception e) {
            System.out.println("Error sending rows between proc");
        }
        return resRow;
    }
    /**
     * Восстановление определителя исходной матрицы на нулевом процессоре
     * @param detParts - определители, полученные в конечных полях
     * @param primes - массив простых чисел
     * @param z - вспомогательный массив
     * @param ring - кольцо целых чисел
     * @return определитель
     * @throws MPIException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static Element detParallel(int[] detParts, int[] primes, NumberZ[] z, Ring ring) throws MPIException, IOException, ClassNotFoundException {
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int sendcount = detParts.length;
        int pr_len = primes.length;
        NumberZ determinant = new NumberZ();
        int[] recvcount = new int[size];
        int[] sendc = new int[1];
        sendc[0] = sendcount;
        long t = System.currentTimeMillis();
        MPI.COMM_WORLD.Barrier();
        MPI.COMM_WORLD.gather(sendc, 1, MPI.INT, recvcount, 1, MPI.INT, 0);
        int[] rdispls = new int[size];
        for (int i = 1; i < size; i++) {
            rdispls[i] = rdispls[i - 1] + recvcount[i - 1];
        }
        IntBuffer sendDet = MPI.newIntBuffer(sendcount);
        for (int i = 0; i < sendcount; i++) {
            sendDet.put(detParts[i]);
        }
        IntBuffer recvDet = MPI.newIntBuffer(pr_len);
        MPI.COMM_WORLD.gatherv(sendDet, sendcount, MPI.INT, recvDet, recvcount, rdispls, MPI.INT, 0);
        
        if (rank == 0) {

            int[] det = new int[pr_len];
            for (int i = 0; i < pr_len; i++) {
                det[i] = recvDet.get(i);
            }
            determinant = Newton.recoveryNewtonWithoutArr(primes, det, z);
      
        }
        return determinant;
    }

    public static void allToAllvObjectArray(Object[] sendBuf, Object[] recvBuf) throws MPIException, IOException, ClassNotFoundException {
        int taskLen = sendBuf.length;
        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();
        Object[][] recvTmp = new Object[size][taskLen];
        int[] sendcount = new int[size];
        int[] sdispls = new int[size];
        int[] recvcount = new int[size];
        int[] rdispls = new int[size];
        byte[][] sendbuffer = new byte[size][0];
        int pointer = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        int p = 0;
        for (int i = 0; i < size; i++) {
            int numTaskforProc = (rank < (size - taskLen % size)) ? taskLen / size : (taskLen / size) + 1;
            for (int j = 0; j < numTaskforProc; j++) {
                oos.writeObject(sendBuf[p]);
            }
            p++;
            sendbuffer[i] = bos.toByteArray();
            pointer = pointer + sendbuffer[i].length;
        }
        byte[] sendbuf = new byte[pointer];
        pointer = 0;
        for (int i = 0; i < sendbuffer.length; i++) {
            int temp = sendbuffer[i].length;
            sdispls[i] = pointer;
            sendcount[i] = temp;
            pointer = pointer + temp;
            System.arraycopy(sendbuffer[i], 0, sendbuf, sdispls[i], temp);
            sendbuffer[i] = null;
        }
        sendbuffer = null;
        MPI.COMM_WORLD.Barrier();
        MPI.COMM_WORLD.allToAll(sendcount, 1, MPI.INT, recvcount, 1, MPI.INT);
        int receivesize = 0;
        for (int i = 0; i < recvcount.length; i++) {
            rdispls[i] = receivesize;
            receivesize = receivesize + recvcount[i];
        }
        byte[] recvbuffer = new byte[receivesize];
        MPI.COMM_WORLD.Barrier();
        MPI.COMM_WORLD.allToAllv(sendbuf, sendcount, sdispls, MPI.BYTE, recvbuffer, recvcount, rdispls, MPI.BYTE);
        MPI.COMM_WORLD.Barrier();
        int k = 0;
        for (int i = 0; i < recvcount.length; i++) {
            if (recvcount[i] > 0) {
                try (ByteArrayInputStream bin = new ByteArrayInputStream(recvbuffer, rdispls[i], recvcount[i]);
                        ObjectInputStream oin = new ObjectInputStream(bin)) {
                    Object rec = oin.readObject();
                    System.out.println(rec + "rank = " + rank);
                    
                    recvBuf[i] = rec;
                } catch (IOException e) {
                    String msg = "Something bad while reading data on proc " + rank + " rd=" + rdispls[i] + " recvcount=" + recvcount[i];
                    Logger.getLogger(AdjointDetParallel.class.getName()).log(Level.SEVERE, msg, e);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(AdjointDetParallel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        recvTmp = null;
    }

        /**
     * Восстанавливает определитель
     *
     * @param B - квадратная матрица
     * @param ring - кольцо Z
     *
     * @return восстановленный определитель
     *
     * @throws MPIException
     */
    public static Element detParallel(MatrixS B, Ring ring) throws Exception {
        MatrixS A = sendInitialMatrix(B, ring);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        MatrixS tranA = A.transpose();
        int adamar = tranA.adamarBitCount(ring);
        //int[] primes = primesForMatrix(A, ring);
        int[] primes = Newton.primesForAdjoint(adamar, 1, ring);
        if(size > A.size || size > primes.length){
            System.out.println("Число процессоров превышает количество строк матрицы или число простых модулей. Уменьшите число процессоров!!!");
            return null;
        }else{
        MatrixS[] detParts = adjointDetZp(A, rank, size, primes, false, ring);
        // System.out.println("detParts = " + Array.toString(detParts) + " rank= " + rank);
        NumberZ determinant = new NumberZ();
        if (rank != 0) {
            MPITransport.sendObjectArray(detParts, 0, detParts.length, 0, 1);
        } else {
            int pr_len = primes.length;
            int[] inter_zero = getMyInterval(pr_len, size, rank);
            MatrixS[] temp = new MatrixS[pr_len];
            System.arraycopy(detParts, 0, temp, inter_zero[0], detParts.length);
            for (int i = 1; i < size; i++) {
                int n = (i < (size - pr_len % size)) ? pr_len / size : pr_len / size + 1;
                int[] interval = getMyInterval(pr_len, size, i);
                Object[] noname = new Object[n];
                MPITransport.recvObjectArray(noname, 0, noname.length, i, 1);
                MatrixS[] sborka = new MatrixS[noname.length];
                for (int p = 0; p < sborka.length; p++) {
                    sborka[p] = (MatrixS) noname[p];
                }
                System.arraycopy(sborka, 0, temp, interval[0], n);
            }
            int[] det = new int[pr_len];
            for (int i = 0; i < pr_len; i++) {
                det[i] = temp[i].getElement(0, 0, ring).intValue();
            }
            //System.out.println("det_i = " + Array.toString(det));
            determinant = Newton.recoveryNewton(primes, det);
        }
        return determinant;
        }
    }
    /**
     * Вычисляет параллельно присоединенную матрицу и определитель исходной матрицы.
     * Сборка присоединенной матрицы и определителя происходит на нулевом процессоре
     *
     * @param B - несколько строк присоединенных матриц по модулям из списка
     * primes
     * @param ring - кольцо Z
     *
     * @return присоединенная матрица в кольце Z
     *  ANTOSHA
     * @throws java.lang.Exception
     */
    public static MatrixS[] adjointDetParallel(MatrixS B, Ring ring) throws Exception {
        long t1 = System.currentTimeMillis();
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        MatrixS A = (MatrixS) MPITransport.bcastObject(B, 0);
        int matr_len = A.size;
        int[] primes = primesForAdjointWithoutPrimesList(A,2147483629, 2147418127, 2147483629, ring);
        int primesLen = primes.length;
      //  if(rank == 0)System.out.println("primes len = "+primesLen);
        NumberZ[] numbNewton = com.mathpar.number.Newton.arrayOfNumbersForNewton(primes);
        int numPrimesforProc;
      //  if (rank == 0) {
      //      System.out.println("Generate of matrix and primes =" + (System.currentTimeMillis() - t1));
      //  }
        if (size > A.size || size > primes.length) {
            System.out.println("Число процессоров превышает количество строк матрицы или число простых модулей. Уменьшите число процессоров!!!");
            return null;
        } else {
            NumberZ det = null;
            NumberZ[][] answer = null;
        //    long t2 = System.currentTimeMillis();
            numPrimesforProc = (rank < (size - primesLen % size)) ? primesLen / size : primesLen / size + 1;//число модулей на процессор
            MatrixS[] adjZp = new MatrixS[numPrimesforProc];
            int[] detZp = new int[numPrimesforProc];
            adjointDetZp(A, adjZp, detZp, rank, size, primes, ring);
             MPI.COMM_WORLD.Barrier();
         //   if (rank == 0) {
        //        System.out.println("Adjoint and Det on primes =" + (System.currentTimeMillis() - t2));
         //   }
         //   long t4 = System.currentTimeMillis();
            IntBuffer recSubMatr = sendRowBetweenProc(adjZp, detZp, primes, rank, size);
          //  if(rank ==0) System.out.println("Send rows between proc. =" + (System.currentTimeMillis() - t4));
          //  long t0 = System.currentTimeMillis();
            int[] detCount = new int[size];//количество определителей у каждого процессора
            MPI.COMM_WORLD.gather(new int[] {numPrimesforProc}, 1, MPI.INT, detCount, 1, MPI.INT, 0);
            int[] detParts = new int[primesLen];//определители для восстановления
            int countOfElRow = (rank==0) ? recSubMatr.capacity() - primesLen : recSubMatr.capacity();
            int[] recRows = new int[countOfElRow];
            int numRowForRank = numTaskForAll(adjZp[0].size, size, rank);
            if(rank !=0){
              for (int i = 0; i < recSubMatr.capacity(); i++) {
                recRows[i] = recSubMatr.get(i);
            }  
            }else{
            int k = 0;    
            int temp = 0;
            int dc = 0;//счетчик по определителям
            int rc = 0;//счетчик по строкам матрицы
            int left = 0;
            int right = detCount[k]; 
            for (int i = 0; i < recSubMatr.capacity(); i++) {
                if(i>=left && i<right && temp < detCount[k]){
                   
                  detParts[dc] = recSubMatr.get(i);
                  dc++;
                  temp++;
                  if(temp == detCount[k]&& k+1 < detCount.length){
                      k++;
                      left = right + matr_len*detCount[k-1]*numRowForRank;
                      right = left + detCount[k];
                      temp = 0;
                  }
                }else{
                recRows[rc] = recSubMatr.get(i);
                rc++;
                }
            }
            }
            if(rank == 0) det = Newton.recoveryNewtonWithoutArr(primes, detParts, numbNewton);
          //  if(rank == 0) System.out.println("Form and Recovery Det = "+ (System.currentTimeMillis() - t0));
            MPI.COMM_WORLD.Barrier();
         //   long t5 = System.currentTimeMillis();
            int[][][] revive = new int[numRowForRank][primesLen][matr_len];
            for (int i = 0; i < numRowForRank; i++) {
                int start = i * matr_len;
                for (int j = 0; j < primesLen; j++) {
                    System.arraycopy(recRows, start, revive[i][j], 0, matr_len);
                    start = start + numRowForRank * matr_len;
                }
            }
            for (int i = 0; i < revive.length; i++) {

                revive[i] = intTransposition(revive[i]);
            }
            int row = revive.length;
            int col = revive[0].length;
            answer = new NumberZ[row][col];
          //  if (rank == 0) {
          //      System.out.println("Prev recovery =" + (System.currentTimeMillis() - t5));
           // }
          //  long t7 = System.currentTimeMillis();
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {

                    answer[i][j] = Newton.recoveryNewtonWithoutArr(primes, revive[i][j], numbNewton);
                }
            }

          //  if (rank == 0) {
         //       System.out.println("Recovery adjoint =" + (System.currentTimeMillis() - t7));
        //    }
        //    long t6 = System.currentTimeMillis();
            if (rank != 0) {
                MPITransport.sendObjectArray(answer, 0, answer.length, 0, 2);
                return null;
            } else {
                Element[][] adj = new Element[matr_len][matr_len];
                int numbRowforProc = (rank < (size - matr_len % size)) ? matr_len / size : matr_len / size + 1;
                int[] inter_zero = getMyInterval(matr_len, size, rank);
                System.arraycopy(answer, 0, adj, inter_zero[0], numbRowforProc);
                for (int i = 1; i < size; i++) {
                    int numbRowforProcI = (i < (size - matr_len % size)) ? matr_len / size : matr_len / size + 1;
                    int[] interval = getMyInterval(matr_len, size, i);
                    Element[][] temp = new Element[numbRowforProcI][];
                    MPITransport.recvObjectArray(temp, 0, numbRowforProcI, i, 2);
                    System.arraycopy(temp, 0, adj, interval[0], numbRowforProcI);
                }
                MatrixS result = new MatrixS(adj, ring);
                //System.out.println("Final allGather =" + (System.currentTimeMillis() - t6));
               // System.out.println("Time for adjoint = " + (System.currentTimeMillis() - t1));
                return new MatrixS[] {result, new MatrixS(det)};
            }
        }

    }

    /**
     * Создает матрицу
     *
     * @param size - размер матрицы
     * @param numb - граница случайного целого числа
     * @param ring - кольцо
     *
     * @return матрица типа MatrixS
     */
    public static MatrixS randomMatrixS(int size, int numb, Ring ring) {
        Random rnd = new Random();
        NumberZ[][] a = new NumberZ[size][size];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] = new NumberZ((numb + rnd.nextInt(numb)));
            }
        }
        MatrixS A = new MatrixS(a, ring);
        return A;
    }
    
        /**
     * Восстанавливает строки присоединенных матриц по модулям
     *
     * @param A - несколько строк присоединенных матриц по модулям из списка
     * primes
     * @param ring - кольцо Z
     *
     * @return присоединенная матрица в кольце Z
     *
     * @throws java.lang.Exception
     */
    public static MatrixS adjointParallel(MatrixS B, Ring ring) throws Exception {
        // Done by Antosha
        MatrixS A = sendInitialMatrix(B, ring);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int matr_len = A.size;
        MatrixS tranA = A.transpose();
        int adamar = tranA.adamarBitCount(ring);
        //int[] primes = primesForMatrix(A, ring);
        int[] primes = Newton.primesForAdjoint(adamar, 1, ring);
         if (rank == 0) {
            System.out.println("Primes = " + Array.toString(primes));
        }
        if(size > A.size || size > primes.length){
            System.out.println("Число процессоров превышает количество строк матрицы или число простых модулей. Уменьшите число процессоров!!!");
            return null;
        }else{
        MatrixS[] adjZp = adjointDetZp(A, rank, size, primes, true, ring);
        MatrixS[] subMatr = sendRecvRowBetweenProc(adjZp, rank, size, primes);
        int[][][] revive = new int[subMatr[0].size][subMatr.length][matr_len];
        for (int i = 0; i < subMatr.length; i++) {
            for (int j = 0; j < subMatr[i].size; j++) {
                for (int k = 0; k < subMatr[i].colNumb - 1; k++) {
                    revive[j][i][k] = subMatr[i].getElement(j, k, ring).intValue();
                }
            }
        }
        for (int i = 0; i < revive.length; i++) {

            revive[i] = intTransposition(revive[i]);
        }
        Element[][] answer = new Element[revive.length][revive[0].length];
        for (int i = 0; i < revive.length; i++) {
            for (int j = 0; j < revive[i].length; j++) {
                answer[i][j] = com.mathpar.number.Newton.recoveryNewton(primes, revive[i][j]);
            }
        }
        if (rank != 0) {
            MPITransport.sendObjectArray(answer, 0, answer.length, 0, 2);
            return null;
        } else {
            Element[][] adj = new Element[matr_len][matr_len];
            int numbRowforProc = (rank < (size - matr_len % size)) ? matr_len / size : matr_len / size + 1;
            int[] inter_zero = getMyInterval(matr_len, size, rank);
            System.arraycopy(answer, 0, adj, inter_zero[0], numbRowforProc);
            for (int i = 1; i < size; i++) {
                int numbRowforProcI = (i < (size - matr_len % size)) ? matr_len / size : matr_len / size + 1;
                int[] interval = getMyInterval(matr_len, size, i);
                Element[][] temp = new Element[numbRowforProcI][];
                MPITransport.recvObjectArray(temp, 0, numbRowforProcI, i, 2);
                System.arraycopy(temp, 0, adj, interval[0], numbRowforProcI);
            }
            MatrixS result = new MatrixS(adj, ring);
           //  System.out.println("i rank = "+rank + " out adjointParallel");
            return result;
        }
        }
    }
    /**
     * Рассылка строк присоединенных матриц в кольце Zp между процессорами для
     * последующего их восстановления
     *
     * @param matrZp - список присоединенных матриц
     * @param rank - номер текущего процессора
     * @param size - количество процессоров
     * @param primes - массив простых модулей
     *
     * @return массив строк
     *     ANTOSHA
     * @throws MPIException
     */
    public static MatrixS[] sendRecvRowBetweenProc(MatrixS[] matrZp, int rank, int size, int[] primes) throws Exception {
       // adjointParallel  -- Antosha done
        int matr_len = matrZp[0].size;
        int primes_len = primes.length;
        int numbRowforProc = (rank < (size - primes_len % size)) ? primes_len / size : (primes_len / size) + 1;
        System.out.println("first i rank = "+rank);
        MatrixS[] recSubMatr = new MatrixS[primes_len];
        MatrixS[] subM = new MatrixS[matrZp.length];
        for (int i = 0; i < size; i++) {
            int[] interval_i = getMyInterval(matr_len, size, i);
            
            if (i != rank) {
                for (int j = 0; j < matrZp.length; j++) {
                    subM[j] = matrZp[j].getSubMatrix(interval_i[0], interval_i[1] - 1, 0, matrZp[j].colNumb);
                }
                MPITransport.sendObjectArray(subM, 0, subM.length, i, 1);

            } else {
                for (int j = 0; j < matrZp.length; j++) {
                    subM[j] = matrZp[j].getSubMatrix(interval_i[0], interval_i[1] - 1, 0, matrZp[j].colNumb);
                }

            }
            int[] interval = getMyInterval(primes_len, size, i);
            System.arraycopy(subM, 0, recSubMatr, interval[0], numbRowforProc);
        }
        System.out.println("send victory = "+ rank);
        for (int i = 0; i < size; i++) {
            int[] interval = getMyInterval(primes_len, size, i);
            if (i != rank) {
                int numbRowforProcI = (i < (size - primes_len % size)) ? primes_len / size : (primes_len / size) + 1;
                Object[] obj = new Object[numbRowforProcI];
                MPITransport.recvObjectArray(obj, 0, obj.length, i, 1);
                MatrixS[] temp = new MatrixS[obj.length];
                for (int j = 0; j < obj.length; j++) {
                    temp[j] = (MatrixS) obj[j];
                }
                System.out.println("second i rank = "+rank);
                System.arraycopy(temp, 0, recSubMatr, interval[0], numbRowforProcI);
                
            }
            System.out.println("third i rank = "+rank);
        }
       // System.out.println("i rank = "+rank + " out sendRecv");
        return recSubMatr;
    }

    /*
     SPACE = Z[x];
     A=[[0,1],[2,3]];
     TOTALNODES = 1;
     PROCPERNODE = 1;
     \adjointDetPar(A);
    
     */

// mpirun -np 2 java -cp /home/r1d1/NetBeansProjects/mathpar/target/classes com.mathpar.parallel.webCluster.algorithms.Adjoint.AdjointDetParallel
    public static void main(String[] args) throws Exception {
        MPI.Init(args);        
        long t1 = System.currentTimeMillis();
        Object []argsFromWeb=Tools.getDataFromClusterRootNode(args).getData();
        MatrixS rnd =new MatrixS();
        int rank = MPI.COMM_WORLD.Rank();
        if (rank==0){
            rnd=(MatrixS)argsFromWeb[0];
        }            
        Ring ring = (Ring)argsFromWeb[1];
        ring=(Ring)MPITransport.bcastObject(ring, 0);
        
        MatrixS[] adjDet= new MatrixS[2];
        if (rank == 0) {            
            adjDet = adjointDetParallel(rnd,  ring);
            System.out.println("Time = " + (System.currentTimeMillis() - t1));
        } else {            
            adjDet = adjointDetParallel(rnd, ring);
        }
        if (rank==0){            
            Object[] result = {adjDet[0], adjDet[1]};
            int userID= Integer.valueOf(args[0]);
            int taskID= Integer.valueOf(args[1]);
            QueryCreator qc= new QueryCreator(null,null);
            qc.saveCalcResultOnRootNode(userID, taskID, result);
        }
        MPI.Finalize();
    }
//    public static void main(String[] args) throws MPIException, IOException, Exception {
//        MPI.Init(args);
//        // QueryResult queryRes=Tools.getDataFromClusterRootNode(args); 
//        int rank = MPI.COMM_WORLD.Rank();
//        int size = MPI.COMM_WORLD.Size();
//        if (rank == 0) {
//        }
//        MPI.COMM_WORLD.Barrier();
//
//        long t1 = System.currentTimeMillis();
//        Ring ring = new Ring("Z[x]");
//
//        if (rank == 0) {
//            // int n = ((Element)ar[0]).intValue();
//            int n = Integer.parseInt(args[0]);
//            int den = Integer.parseInt(args[1]);
//            //MatrixS rnd = randomMatrixS(n, 2000000, ring);
//            Random ran = new Random();
//            MatrixS rnd = new MatrixS(n, n, den, new int[]{100,24}, ran, ring.numberONE, ring);
//            MatrixS[] adjoint = adjointDetParallel(rnd, ring);
//            //System.out.println("Adjoint = " + adjoint[0].toString(ring));
////            System.out.println("DET = " +adjoint[1].toString(ring));
//            Element[] c = new Element[] {ring.numberONE};
//            MatrixS mtr = rnd.adjointDet(c, ring);
//            if (adjoint[0].equals(mtr, ring)) {
//                System.out.println("Solved");
//            } else {
//                System.out.println("Not solved");
//            }
//            Element detRight = c[0];
////            //System.out.println("Правильный adjoint=" + mtr.toString(ring));
////           System.out.println("Правильный det=" + detRight);
//        } else {
//            MatrixS rnd = new MatrixS();
//            //MatrixS rnd = (MatrixS) MPITransport.recvObject(0, 1);
//            MatrixS[] adjoint = adjointDetParallel(rnd, ring);
//            //Element det = detParallel(rnd, rank, size, ring);
//        }
//        if (rank == 0) {
//            System.out.println("Общее время = " + (System.currentTimeMillis() - t1));
//        }
//        MPI.Finalize();
//    }
}