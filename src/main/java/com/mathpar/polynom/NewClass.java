/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.polynom;

import com.mathpar.func.F;
import com.mathpar.func.Integrate;
import com.mathpar.func.Page;
import java.util.Arrays;
import com.mathpar.number.*;

/**
 *156051 4196
 * @author student
 */
public class NewClass {

    public static void main(String args[]) {
       // Ring r = new Ring("C64[x]");
    Ring ring = new Ring("Z[x,y]"); ring.page  =new Page(ring,true);
 // F f1= new F("1/3* \\ln(\\abs(1/2* \\cos(x^3+7)* \\exp(7\\i )))",ring);
//  Element ee= f1.ExpandLog(ring);
  //      Element resInt = new Integrate().integration(f, new Polynom("x", ring), ring);
   //     Element b = new F("((-3x) + (\\ln(\\abs(x+2) \\abs(x+1) \\abs(x+3)))+ x * \\ln(x^3+6x^2+11x+6))", ring);
    //   Element res = resInt.subtract(b, ring);
   //     System.out.println("RES=" +ee); // resInt);
    //    System.out.println("b="+  b );
    
Polynom f = new Polynom("yx +x+ 1",  ring);
Polynom p =  new Polynom("y^2x^2 +3x + 1",  ring);
Ring ring2=ring.CForm.newRing;
Element[] ww= p.prsExact(f, ring); 
System.out.println("ww="+ww[0]+ "   " +ww.length); // resInt);
    
    }
}
