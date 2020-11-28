/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.matrix.file.mmio;

import com.mathpar.number.*;


/**
 * @author Yuri Valeev
 * @version 2.0
 */
public class CFWTextR64 extends CFWText{
    /**
     * NumberR64 --> "double"
     * @param el Element
     * @return String
     */
    protected String scalarToString(Element el){
        return ((NumberR64)el).toString();
    }
}
