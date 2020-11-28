package example;

import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;

import java.io.*;

public class Transport {
    public static void bcastObjectArray(final Intracomm comm, final Object[] objects, final int root)
            throws IOException, MPIException,ClassNotFoundException
    {
        byte[] bytes = null;
        final int[] size = new int[1];

        final int rank = comm.Rank();

        if (rank == root) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            for (final Object object : objects)
                oos.writeObject(object);

            bytes = bos.toByteArray();

            size[0] = bytes.length;
        }

        comm.Bcast(size, 0,1, MPI.INT, root);

        if (rank != root)
            bytes = new byte[size[0]];

        comm.Bcast(bytes, 0, bytes.length, MPI.BYTE, root);

        if (rank != root) {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));

            for (int i = 0; i < objects.length; ++i)
                objects[i] = ois.readObject();
        }
    }
}

