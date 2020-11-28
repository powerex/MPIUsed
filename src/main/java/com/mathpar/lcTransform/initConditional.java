/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.lcTransform;

import com.mathpar.func.F;
import java.util.ArrayList;
import com.mathpar.number.Element;
import com.mathpar.number.Ring;
import com.mathpar.polynom.Polynom;

/**
 * Вспомогательная структура, объект, представляющий собой начальное условие для дифференциального
 * уравнения с частными производными в случае, когда начальные условия не заданы.
 * @author white_raven (Смирнов Роман)
 * @version 0.2
 */
public class initConditional {

    /**
     * Кольцо в котором происходит нахождения решения
     */
    public Ring ring;
    /**
     * Индекс переменной в кольце ring, в котором данное начальное условие
     * находиться в символьном виде
     */
    public int variable = -1;
    /**
     * Конструкция начального условия в виде функции-дерева
     */
    public F ic;
    /**
     * Значение начального условия
     */
    public Element value = null;
    /**
     * Значение начального условия после прямого преобразования Лапласа-Карсона
    */
    public Element valueLC = null;

    public initConditional() {
    }

    /**
     * Метод возвращающий массив переменных в аргументе функции - начального
     * условия
     *
     */
    public Polynom[] getArgs() {
        ArrayList<Polynom> res = new ArrayList<Polynom>();
        for (int i = 2; i < ic.X.length; i += 3) {
            if (ic.X[i] == null) {
                res.add((Polynom) ic.X[i - 1]);
            }
        }
        return res.toArray(new Polynom[res.size()]);
    }


    /**
     * Вывод в строку
     *
     * @param r - кольцо
     * @return строковое представление this
     */
    public String toString(Ring r) {
        return ic.toString(r) + "=" + value;
    }
    @Override
    public String toString(){
      return ic.toString(Ring.ringR64xyzt) + "=" + value;
    }
}
