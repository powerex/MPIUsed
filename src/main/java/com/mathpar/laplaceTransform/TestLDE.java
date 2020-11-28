/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform;

import com.mathpar.func.F;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mathpar.number.Element;
import com.mathpar.number.Ring;

/**
 *
 * @author Rybakov Michail
 * @years 2009 - 2013
 * @version 3.0
 */
public class TestLDE {
    public static void main(String[] args) {
        Ring ring = new Ring("R64[t]");
        F dif = new F("\\systLDE(\\d(y, t, 2)-4y=4t)", ring);
        F initC = new F("\\initCond(\\d(y, t, 0, 0)=a, \\d(y, t, 0, 1)=b)", ring);
        try {
            Element solveLDE = new LDE().solveDifEquation(dif, initC, ring);
        } catch (Exception ex) {
            Logger.getLogger(TestLDE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
