/**
 * Copyright © 2011 Mathparca Ltd. All rights reserved.
 */

package com.mathpar.Graphic2D.Element2D;

import com.mathpar.func.Page;
import com.mathpar.number.Ring;

// \paintElement('Точка = (4,5)');
// \paintElement('Точка = (0,0)');
// \paintElement('Точка = (-1,5)');
// \paintElement('Точка = (-1,-2)');
// \paintElement('Точка = (7,7)');

// \paintElement('Отрезок = (начало(0, 0), конец (6, 6)');
// \paintElement('Отрезок = (начало(2, 2), конец (6, 6)');
// \paintElement('Отрезок = (начало(-1, -1), конец (6, 6)');
// \paintElement('Отрезок = (начало(3, 3), конец (6, 6)');
// \paintElement('Отрезок = (начало(-3, -3), конец (-1, -1)');

// \paintElement('Многоугольник=вершины(0,0),(0,6),(6,6),(6,0)');
// \paintElement('Многоугольник=вершины(1,1),(1,2),(2,2),(2,1)');
// \paintElement('Многоугольник=вершины(-1,-1),(-1,1),(1,1),(1,-1)');

/**
 *
 * @author unknown
 */
public class TestPlanimetria {
    public static void main(String[] args) {
        Ring r = new Ring("R64[x,y,t]");
        Page p = new Page(r);
        String d = "Окружность = (центр(0,0); радиус (5))";
        String h = "Многоугольник=вершины(0,0),(0,6),(6,6),(6,0)";
        Planimetria.builder(new String[]{h}).build();
        System.out.println("TEST");
    }
}
