/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.matrix.file.mmio;

import java.io.*;


/**
 * @author Yuri Valeev
 * @version 2.0
 */
public interface GeneratorCFR {
    public CoordFileReader getCFR(File in)  throws MMIOException;
}
