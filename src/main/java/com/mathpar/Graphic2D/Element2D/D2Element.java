/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic2D.Element2D;

import com.mathpar.Graphic2D.DrawElement;

/**
 * Абстрактный класс D2Element Конструкторы описанные в классе: void print() -
 * вывод на экран объектов void create(DrawElement d) -построение
 * планиметрических элементов по данным, полученным от пользователя. public
 * String toString()- просмотр информации об елементе
 *
 */
public abstract class D2Element {
    void print() {
    }

    void create(DrawElement d) {
    } 

    @Override
    public String toString() {
        return "";
    } 
}
