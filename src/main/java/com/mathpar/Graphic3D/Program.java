/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D;

import com.mathpar.func.Page;
import com.mathpar.number.Ring;
import java.awt.Color;

public class Program {
    public static void main(String[] args) {
        Page pa = new Page(Ring.ringR64xyzt,true);
        Plot3D p = new Plot3D(new String[]{"4"}, null, null, null,
                -20, 20, -20, 20, 30, 30, Color.yellow, false, true, true, pa, false);
    }
}
