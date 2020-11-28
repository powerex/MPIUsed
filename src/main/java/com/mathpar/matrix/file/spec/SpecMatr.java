/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.matrix.file.spec;


/**
 * @author Yuri Valeev
 * @version 2.0
 */
public interface SpecMatr {
    public int getRows();
    public int getCols();
    public boolean hasSubBlock(int nb);
    public SpecMatr getSubBlock(int nb);
    public boolean isNotZero();

    public boolean isEMatr();
    public EMArr getEMArr();

    public boolean isIMatr();
    public int[] getIMArr();
}
