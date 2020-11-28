/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform;

import com.mathpar.func.*;
import com.mathpar.number.Ring;

/**
 *
 * @author Ribakov/M
 */
public class TestInverseLaplaceTransform {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Ring ring = new Ring("R64[x]");

        F f = new F("25/(-4x-3)", ring);
        System.out.println("F_input =  " + f);
        F ff = new InverseLaplaceTransform(f).inverse_exp();
        System.out.println("F_output =  " + ff);

        F f1 = new F("5/(x^2+25)", ring);
        System.out.println("F_input =  " + f1);
        F ff1 = new InverseLaplaceTransform(f1).inverse_sin(ring);
        System.out.println("F_output =  " + ff1);

        F f2 = new F("5/(x^2+25)", ring);
        System.out.println("F_input =  " + f2);
        F ff2 = new InverseLaplaceTransform(f2).inverse_cos(ring);
        System.out.println("F_output =  " + ff2);

        F f3 = new F("(5x+7)/((x+3)^2+4)", ring);
        System.out.println("F_input =  " + f3);
        F ff3 = new InverseLaplaceTransform(f3).pol_div1();
        System.out.println("F_output =  " + ff3);

        F f4 = new F("(5x+7)/((x+3)^2-4)", ring);
        System.out.println("F_input =  " + f4);
        F ff4 = new InverseLaplaceTransform(f4).pol_div2();
        System.out.println("F_output =  " + ff4);

        F f5 = new F("(3x+4)/((x+5)^4)", ring);
        System.out.println("F_input =  " + f5);
        F ff5 = new InverseLaplaceTransform(f5).pol_div3();
        System.out.println("F_output =  " + ff5);

        F f6 = new F("3x^3/(x+5)^4", ring);
        System.out.println("F_input =  " + f6);
        F ff6 = new InverseLaplaceTransform(f6).pol_div4();
        System.out.println("F_output =  " + ff6);

        F f7 = new F("333/x^4", ring);
        System.out.println("F_input =  " + f7);
        F ff7 = new InverseLaplaceTransform(f7).pol_div5();
        System.out.println("F_output =  " + ff7);

        F f8 = new F("3/(x^2-4)", ring);
        System.out.println("F_input =  " + f8);
        F ff8 = new InverseLaplaceTransform(f8).inverse_sh();
        System.out.println("F_output =  " + ff8);

        F f9 = new F("3x/(x^2-4)", ring);
        System.out.println("F_input =  " + f9);
        F ff9 = new InverseLaplaceTransform(f9).inverse_ch();
        System.out.println("F_output =  " + ff9);



    }
}
