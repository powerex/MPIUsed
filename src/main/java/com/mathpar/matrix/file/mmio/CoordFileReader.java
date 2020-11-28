/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.matrix.file.mmio;


/**
 * @author not attributable
 * @version 1.0
 */
public interface CoordFileReader {
    /**
     * Возвращает {m,n,nz}, m,n -- кол-во строк и столбцов,
     * nz -- кол-во ненулевых.
     * @return int[]
     * @throws MMIOException
     */
    public int[] readHeader() throws MMIOException;

    /**
     * Возвращает следующую строку.
     * нет строк -- null,
     * есть строка -- объект CoordLine.
     * @return CoordLine
     * @throws MMIOException
     */
    public CoordLine readLine() throws MMIOException;

    /**
     * Закрыть.
     * @throws MMIOException
     */
    public void close() throws MMIOException;
}
