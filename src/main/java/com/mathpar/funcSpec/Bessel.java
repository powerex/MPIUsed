/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.funcSpec;

import com.mathpar.func.F;
import com.mathpar.number.Element;
import com.mathpar.number.NumberR;
import com.mathpar.number.NumberZ;
import com.mathpar.number.Ring;
import com.mathpar.polynom.Polynom;

public class Bessel {
    public static void main(String[] args) {
        Ring r = new Ring("R[x]");
        r.FLOATPOS = 10;
        r.setMachineEpsilonR(10);
        r.setMachineEpsilonR64(10);
        Element a = valueY(new NumberR("10"), new NumberR("0"), r);
        System.out.println("res = " + a.toString(r));
        //func(new Polynom("x^2", r), new Polynom("x", r), new Polynom("x^2", r), r);
    }

    public static Element valueJ(Element x, Element nu, Ring r) {
        F gamma = new F(F.GAMMA, new Element[] {new Polynom("x", r)});
        Element result = x.divide(new NumberZ("2"), r);
        result = result.pow(nu, r);
        result = result.divide(gamma.value(new Element[] {nu.add(NumberZ.ONE, r)}, r), r);
        Element temp = null;
        int k = 1;
        do {
            temp = gamma.value(new Element[] {NumberZ.ONE.add(new NumberZ(k))}, r);
            temp = temp.multiply(gamma.value(new Element[] {nu.add(NumberZ.ONE, r).add(new NumberZ(k), r)}, r), r);
            temp = temp.pow(NumberZ.MINUS_ONE, r).multiply(NumberZ.MINUS_ONE.pow(k), r);
            temp = temp.multiply(x.divide(new NumberZ("2"), r).pow(nu.add(new NumberZ(2 * k), r), r), r);
            result = result.add(temp, r);
            k++;
        } while (!temp.isZero(r));

        return result;
    }

    public static void func(Polynom a, Polynom b, Polynom c, Ring ring) {
        if ((a.coeffs.length != 1) || (a.powers[0] != 2)) {
            return;
        }
        if ((b.coeffs.length != 1) || (b.powers[0] != 1)) {
            return;
        }
        if (!((c.coeffs.length == 1) && (c.powers[0] == 2))) {
            if (!((c.coeffs.length == 2) && (c.powers[0] == 2) && (c.powers[1] == 0))) {
                return;
            }
        }
        Element nu;
        if (c.coeffs.length != 1) {
            nu = c.coeffs[1].negate(ring).sqrt(ring);
        } else {
            nu = NumberZ.ZERO;
        }

        if (nu.subtract(nu.round(ring), ring).isZero(ring)) {
            System.out.println("y = ...");
        }
    }

    public static Element valueY(Element x, Element nu, Ring ring) {
        Element result = ring.numberZERO;
        F log = new F("\\ln(x/2)", ring);
        Element C = new NumberR("0.57721566490");
        NumberZ two = new NumberZ(2);
        for (NumberZ i = NumberZ.ZERO;
                !nu.subtract(ring.numberONE, ring).subtract(i, ring).isNegative(); i = i.add(NumberZ.ONE)) {
            result = result.subtract(nu.subtract(i, ring).subtract(ring.numberONE, ring).factorial(ring)
                    .multiply(x.divide(two, ring).pow(i.multiply(two).subtract(nu, ring), ring), ring)
                    .divide(i.factorial(ring), ring), ring);
        }
        Element temp = ring.numberONE;
        for (int i = 0; !temp.isZero(ring); i++) {
            NumberZ k = new NumberZ(i);
            temp = log.value(new Element[] {x}, ring).add(C, ring);
            temp = temp.multiply(two, ring);
            for (int j = 1; j <= i; j++) {
                temp = temp.subtract(two.divide(new NumberZ(j), ring), ring);
            }
            for (NumberZ j = k.add(NumberZ.ONE); !nu.add(k, ring).subtract(j, ring).isNegative(); j = j.add(NumberZ.ONE)) {
                temp = temp.subtract(ring.numberONE.divide(j, ring), ring);
            }
            temp = temp.multiply(x.divide(two, ring).pow(nu.add(two.multiply(k, ring), ring), ring), ring);
            if (!k.divide(two, ring).subtract(k.divide(two, ring).round(ring), ring).isZero(ring)) {
                temp = temp.multiply(NumberZ.MINUS_ONE, ring);
            }
            temp = temp.divide(k.factorial(ring), ring);
            temp = temp.divide(k.add(nu, ring).factorial(ring), ring);
            result = result.add(temp, ring);
        }
        return result.divide(new NumberR().pi(ring), ring);
    }
}
