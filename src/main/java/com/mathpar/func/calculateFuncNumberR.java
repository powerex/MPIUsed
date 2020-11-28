/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;
import com.mathpar.number.*;
import com.mathpar.number.Ring;
/**
 *
 * @author white_raven
 */
public class calculateFuncNumberR {
   requiredValueNumberR func; // наша функция в виде массива

   public Element calculate(F f,Element[] point, Ring r, int startAcuracy, int endAcuracy){
   func=new requiredValueNumberR(r);
   func.modifyTree(f); // та же функция но уже с другой структурой





   return NumberR.NAN;
   }




}
