/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform;

import com.mathpar.func.*;
import com.mathpar.number.*;

/**
 *
 * @author Ribakov Mixail
 */
public class TestLaplaceTransform {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Ring ring = new Ring("R64[t]");
        ring.FLOATPOS = 2;

        F c5 = new F("\\exp(5t)", ring);
        System.out.println("исходный = " + c5);
        System.out.println("преобразованный = " + new LaplaceTransform(c5).transformExp());

//        F c9 = new F(F.UNITSTEP, new Polynom("-8t", ring));
//        System.out.println("исходный = " + c9);
//        System.out.println("преобразованный = " + new LaplaceTransform(c9).transformUnitStep());
//
//        F c99 = new F(F.UNITSTEP, new Polynom("8t", ring));
//        System.out.println("исходный = " + c99);
//        System.out.println("преобразованный = " + new LaplaceTransform(c99).transformUnitStep());
//
//        F c10 = new F(F.UNITSTEP, new Polynom("2t+5", ring));
//        System.out.println("исходный = " + c10);
//        System.out.println("преобразованный = " + new LaplaceTransform(c10).transformUnitStepShift());
//
//        F c11 = new F(F.EXP, new Polynom("5t", ring));
//        F c12 = new F(F.SIN, new Polynom("8t", ring));
//        F c13 = new F(F.MULTIPLY, new F[]{c11, c12});
//        System.out.println("исходный = " + c13);
//        System.out.println("преобразованный = " + new LaplaceTransform(c13).transformExpMultiplySin(ring));
//
//        F c14 = new F(F.EXP, new Polynom("6t", ring));
//        F c15 = new F(F.COS, new Polynom("9t", ring));
//        F c16 = new F(F.MULTIPLY, new F[]{c14, c15});
//        System.out.println("исходный = " + c16);
//        System.out.println("преобразованный = " + new LaplaceTransform(c16).transformExpMultiplyCos(ring));
//
//        F result = new F(new Polynom("3t^4", ring));
//        System.out.println("исходный = " + result);
//        F ff = new LaplaceTransform(result).transformPolynom();
//        System.out.println("преобразованный = " + new LaplaceTransform(result).transformPolynom());
//
//        F result1 = new F(new Polynom("3t+4t^2-6t^3", ring));
//        System.out.println("исходный = " + result1);
//        System.out.println("преобразованный = " + new LaplaceTransform(result1).transformPolynom());
//
//        F result3 = new F(F.EXP, new Polynom("6t", ring));
//        F result4 = new F(F.MULTIPLY, new Element[]{new Polynom("4t^2", ring), result3});
//        System.out.println("исходный = " + result4);
//        System.out.println("преобразованный = " + new LaplaceTransform(result4).transformPolMultiplyExp(ring));
//
//        F result5 = new F(new Polynom("4t^5", ring));
//        F result6 = new F(F.SIN, new Polynom("-3t", ring));
//        F result7 = new F(F.MULTIPLY, new F[]{result5, result6});
//        System.out.println("исходный = " + result7);
//        System.out.println("преобразованный = " + new LaplaceTransform(result7).transformPolMultiplySin());
//
//        F result8 = new F(new Polynom("2t^7", ring));
//        F result9 = new F(F.COS, new Polynom("-7t", ring));
//        F result10 = new F(F.MULTIPLY, new F[]{result8, result9});
//        System.out.println("исходный = " + result10);
//        System.out.println("преобразованный = " + new LaplaceTransform(result10).transformPolMultiplyCos());
//
//        F result11 = new F(new Polynom("2t^3", ring));
//        F result12 = new F(F.EXP, new Polynom("7t", ring));
//        F result13 = new F(F.SIN, new Polynom("5t", ring));
//        F result14 = new F(F.MULTIPLY, new F[]{result11, result12, result13});
//        System.out.println("исходный = " + result14);
//        System.out.println("преобразованный = " + new LaplaceTransform(result14).transformMultiplyPolExpSin());
//
//        F result15 = new F(new Polynom("3t^4", ring));
//        F result16 = new F(F.EXP, new Polynom("4t", ring));
//        F result17 = new F(F.SIN, new Polynom("6t", ring));
//        F result18 = new F(F.MULTIPLY, new F[]{result15, result16, result17});
//        System.out.println("исходный = " + result18);
//        System.out.println("преобразованный = " + new LaplaceTransform(result18).transformMultiplyPolExpCos());
//        System.out.println("!!!!!!!!!!!!! = " + new LaplaceTransform().newPolynomToF(4, new NumberR64(101), new NumberR64(117)));
//
//        F result19 = new F(new Polynom("t^5", ring));
//        F result20 = new F(F.UNITSTEP, new Polynom("3t-7", ring));
//        F result21 = new F(F.MULTIPLY, new F[]{result19, result20});
//        System.out.println("исходный = " + result21);
//        System.out.println("преобразованный = " + new LaplaceTransform(result21).transformPolMultiplyUnitStepShift());
//        System.out.println("Sum = " + new LaplaceTransform().newPolynomToElement(3, new NumberR64(3), new NumberR64(5), new NumberR64(7)));
//
//        F result22 = new F(new Polynom("t^2", ring));
//        F result23 = new F(F.UNITSTEP, new Polynom("5t-7", ring));
//        F result24 = new F(F.EXP, new Polynom("3t", ring));
//        F result25 = new F(F.MULTIPLY, new F[]{result22, result24, result23});
//        System.out.println("исходный = " + result25);
//        System.out.println("преобразованный = " + new LaplaceTransform(result25).transformMultiplyPolExpUnitStepShift());
//
//        F result26 = new F(F.UNITSTEP, new Polynom("3t-5", ring));
//        F result27 = new F(F.EXP, new Polynom("2t", ring));
//        F result28 = new F(F.MULTIPLY, new F[]{result27, result26});
//        System.out.println("исходный = " + result28);
//        System.out.println("преобразованный = " + new LaplaceTransform(result28).transformExpMultiplyUnitStepShift());
//
//        F result29 = new F(F.UNITSTEP, new Polynom("3t-5", ring));
//        F result30 = new F(F.SIN, new Polynom("2t", ring));
//        F result31 = new F(F.MULTIPLY, new F[]{result30, result29});
//        System.out.println("исходный = " + result31);
//        System.out.println("преобразованный = " + new LaplaceTransform(result31).transformSinMultiplyUnitStepShift());
//
//        F result32 = new F(F.UNITSTEP, new Polynom("3t-5", ring));
//        F result33 = new F(F.COS, new Polynom("2t", ring));
//        F result34 = new F(F.MULTIPLY, new F[]{result33, result32});
//        System.out.println("исходный = " + result34);
//        System.out.println("преобразованный = " + new LaplaceTransform(result34).transformCosMultiplyUnitStepShift());
//
//        F result35 = new F(F.UNITSTEP, new Polynom("7t-11", ring));
//        F result36 = new F(F.SIN, new Polynom("2t", ring));
//        F result37 = new F(F.EXP, new Polynom("3t", ring));
//        F result38 = new F(F.MULTIPLY, new F[]{result37, result36, result35});
//        System.out.println("исходный = " + result38);
//        System.out.println("преобразованный = " + new LaplaceTransform(result38).transformMultiplyExpSinUnitStepShift());
//
//
//        F result39 = new F(F.UNITSTEP, new Polynom("7t-11", ring));
//        F result40 = new F(F.COS, new Polynom("2t", ring));
//        F result41 = new F(F.EXP, new Polynom("3t", ring));
//        F result42 = new F(F.MULTIPLY, new F[]{result41, result40, result39});
//        System.out.println("исходный = " + result42);
//        System.out.println("преобразованный = " + new LaplaceTransform(result42).transformMultiplyExpCosUnitStepShift());
//
//        F result43 = new F(F.UNITSTEP, new Polynom("7t-11", ring));
//        F result44 = new F(new Polynom("t^3", ring));
//        F result45 = new F(F.MULTIPLY, new F[]{result44, result43});
//        System.out.println("исходный = " + result45);
//        System.out.println("преобразованный = " + new LaplaceTransform(result45).transformMultiplyPolUnitStepShift());
//
//        F result46 = new F(F.UNITSTEP, new Polynom("7t-11", ring));
//        F result47 = new F(new Polynom("t^3", ring));
//        F result48 = new F(F.EXP, new Polynom("5t", ring));
//        F result49 = new F(F.MULTIPLY, new F[]{result47, result48, result46});
//        System.out.println("исходный = " + result49);
//        System.out.println("преобразованный = " + new LaplaceTransform(result49).transformPolExpUnitStepShift());
//
//        F result50 = new F(F.UNITSTEP, new Polynom("7t-11", ring));
//        F result51 = new F(new Polynom("t^3", ring));
//        F result52 = new F(F.SIN, new Polynom("5t", ring));
//        F result53 = new F(F.MULTIPLY, new F[]{result51, result52, result50});
//        System.out.println("исходный = " + result53);
//        System.out.println("преобразованный = " + new LaplaceTransform(result53).transformPolSinUnitStepShift());
//
//        F result54 = new F(F.UNITSTEP, new Polynom("7t-11", ring));
//        F result55 = new F(new Polynom("t^3", ring));
//        F result56 = new F(F.COS, new Polynom("5t", ring));
//        F result57 = new F(F.MULTIPLY, new F[]{result55, result56, result54});
//        System.out.println("исходный = " + result57);
//        System.out.println("преобразованный = " + new LaplaceTransform(result57).transformPolCosUnitStepShift());
//
//        F result58 = new F(F.UNITSTEP, new Polynom("7t-11", ring));
//        F result59 = new F(new Polynom("t^2", ring));
//        F result60 = new F(F.EXP, new Polynom("3t", ring));
//        F result61 = new F(F.SIN, new Polynom("5t", ring));
//        F result62 = new F(F.MULTIPLY, new F[]{result59, result60, result61, result58});
//        System.out.println("исходный = " + result62);
//        System.out.println("преобразованный = " + new LaplaceTransform(result62).transformPolExpSinUnitStepShift());
//
//        F result63 = new F(F.UNITSTEP, new Polynom("7t-11", ring));
//        F result64 = new F(new Polynom("t^2", ring));
//        F result65 = new F(F.EXP, new Polynom("3t", ring));
//        F result66 = new F(F.COS, new Polynom("5t", ring));
//        F result67 = new F(F.MULTIPLY, new F[]{result64, result65, result66, result63});
//        System.out.println("исходный = " + result67);
//        System.out.println("преобразованный = " + new LaplaceTransform(result67).transformPolExpCosUnitStepShift());
//
//        F f1 = new F("1-\\unitStep(t-2)", ring);
//        F f2 = new LaplaceTransform(f1).transformUnitStepShift1(ring);
//        System.out.println("res1 = " + f2.toString(new Ring("C64[p]")));
//
//        F f3 = new F("1+\\unitStep(t-4)-2*\\unitStep(t-2)", ring);
//        F f4 = new LaplaceTransform(f3).transformUnitStepShift2(ring);
//        System.out.println("res2 = " + f4.toString(new Ring("C64[p]")));
//
//        F f5 = new F("\\unitStep(t-2)-\\unitStep(t-3)", ring);
//        F f6 = new LaplaceTransform(f5).transformUnitStepShift3(ring);
//        System.out.println("res3 = " + f6.toString(new Ring("C64[p]")));
//
//        F f7 = new F("\\unitStep(t-4)+\\unitStep(t-6)-2*\\unitStep(t-5)", ring);
//        F f8 = new LaplaceTransform(f7).transformUnitStepShift4(ring);
//        System.out.println("res4 = " + f8.toString(new Ring("C64[p]")));
//
//        F f9 = new F("t+(t-4)*\\unitStep(t-4)+2*(t-2)*\\unitStep(t-2)", ring);
//        F f10 = new LaplaceTransform(f9).transformUnitStepShift5(ring);
//        System.out.println("res5 = " + f10.toString(new Ring("C64[p]")));
//
//        F f11 = new F("(t-4)*\\unitStep(t-4)+(t-6)*\\unitStep(t-6)+2*(5-t)*\\unitStep(t-5)", ring);
//        F f12 = new LaplaceTransform(f11).transformUnitStepShift6(ring);
//        System.out.println("res6 = " + f12.toString(new Ring("C64[p]")));
//
//        F f13 = new F("1-\\exp(6-3t)*\\unitStep(t-2)", ring);
//        F f14 = new LaplaceTransform(f13).transformUnitStepShift7(ring);
//        System.out.println("res7 = " + f14.toString(new Ring("C64[p]")));
//
//        F f15 = new F("\\exp(6-3t)*\\unitStep(t-2)", ring);
//        F f16 = new LaplaceTransform(f15).transformUnitStepShift8(ring);
//        System.out.println("res8 = " + f16.toString(new Ring("C64[p]")));
//
//        F f17 = new F("(t-2)*\\unitStep(t-2)+(3-t)*\\unitStep(t-3)", ring);
//        F f18 = new LaplaceTransform(f17).transformUnitStepShift9(ring);
//        System.out.println("res9 = " + f18.toString(new Ring("C64[p]")));
//
//        F f19 = new F("t-(t-2)*\\unitStep(t-2)", ring);
//        F f20 = new LaplaceTransform(f19).transformUnitStepShift10(ring);
//        System.out.println("res10 = " + f20.toString(new Ring("C64[p]")));
    }
}
