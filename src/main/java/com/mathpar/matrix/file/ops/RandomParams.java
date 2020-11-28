/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.matrix.file.ops;

import java.util.*;

/**
 * @author Yuri Valeev
 * @version 0.5
 */
public class RandomParams {
    public int den;
    public double dden;
    public long mod;
    public int nbits;
    public int[] maxpowers;
    public Random rnd;
    public int[] randomType;


    public RandomParams(int den, double dden, long mod, int nbits, int[] maxpowers,
                        Random rnd, int[] randomType){
        this.den=den;
        this.dden=dden;
        this.mod=mod;
        this.nbits=nbits;
        this.maxpowers=maxpowers;
        this.rnd=rnd;
        this.randomType=randomType;
    }

}
