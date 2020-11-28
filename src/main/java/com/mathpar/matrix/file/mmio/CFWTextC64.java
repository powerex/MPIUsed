/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.matrix.file.mmio;

import com.mathpar.number.*;


/**
 * @author Yuri Valeev
 * @version 2.0
 */
public class CFWTextC64 extends CFWText{

    /**
     * NumberC64 --> "double" "double"
     * @param el Element
     * @return String
     */
    protected String scalarToString(Element el){
        Complex c=(Complex)el;
        return c.re+" "+c.im;
    }

}
