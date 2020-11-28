/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.diffExpression;

import com.mathpar.func.*;
import com.mathpar.number.Array;
import com.mathpar.number.Ring;

/**
 *
 * @author VOVA
 */
public class Test {

    public static void main(String[] args) throws Exception {
        Ring ring = new Ring("R64[x,y]");
        CanonicForms cf=new CanonicForms(ring,true);
//        F f = new F("\\systLDE(\\d(y,x)+x=0)", ring);
//        F[] ff = Equals2.homogeneous(f, ring);
//        System.out.println(""+ff[0]);
//        F f = new F("y", ring);
//        F uu = Equals1.var_integrate("y", f, ring);
//        System.out.println(""+uu);
        //пример на понижение дифференцирования, случай когда (y^(n)^p=f(x))
        F dif1 = new F("\\systLDE(x\\d(y,t,5)-\\exp(x)=0)", ring);
        F[] res = Equals1.Solve(dif1, ring,cf);
        System.out.println("res"+Array.toString(res,ring));
    }
}
