/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.ZBuffer;

/**
 *
 * @author yuri
 */
public class GM_Triangle {

    GM_Vector vertex[];
    boolean visible;

    public GM_Triangle() {
        vertex = new GM_Vector[3];
        visible = true;
    }

    public GM_Triangle(GM_Vector v1, GM_Vector v2, GM_Vector v3) {
        vertex = new GM_Vector[3];
        visible = true;
        vertex[0] = v1;
        vertex[1] = v2;
        vertex[2] = v3;
    }
}
