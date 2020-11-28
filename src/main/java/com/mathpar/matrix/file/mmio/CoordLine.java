/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.matrix.file.mmio;

import com.mathpar.number.Element;


/**
 * @author not attributable
 * @version 1.0
 */
public class CoordLine {
    public int i;
    public int j;
    public Element el;

    public CoordLine(int i,int j,Element el){
        this.i=i;
        this.j=j;
        this.el=el;
    }
}
