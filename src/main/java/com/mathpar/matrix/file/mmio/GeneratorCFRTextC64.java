/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.matrix.file.mmio;

import java.io.File;


/**
 * @author Yuri Valeev
 * @version 2.0
 */
public class GeneratorCFRTextC64 implements GeneratorCFR{
    public CoordFileReader getCFR(File in) throws MMIOException{
        return new CFRTextC64(in);
    }
}
