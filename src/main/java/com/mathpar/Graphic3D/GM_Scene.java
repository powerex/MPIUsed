/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D;

import com.mathpar.func.Page;

/**
 * Класс является наследником класса <code>GM_Figure</code> и представляет собой объект-сцену.
 * @author JuliaGrishina
 */
public class GM_Scene extends GM_Figure {
    /**
     * ширина сцены
     */
    public int width;
    /**
     * высота сцены
     */
    public int height;

    public GM_Scene(Page page, int width, int height, int scale) {
        this.page = page;
        this.width = width;
        this.height = height;
    }
}
