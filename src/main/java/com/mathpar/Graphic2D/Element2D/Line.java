/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic2D.Element2D;

import com.mathpar.Graphic2D.*;
import java.awt.EventQueue;
import javax.swing.JFrame;
import com.mathpar.number.*;

/**
 *
 * @author kiselew
 */
/**
 * Класс Line являющийся наследником класса D2Element, содержит главный
 * конструктор Line(Element k, Element b, String name) -получение даннных от
 * пользователя. Для построения требуются коеффиценты к,b уравенния прямой вида
 * y=kx+b (к,b)
 */
public class Line extends D2Element {
    Element coeffK;
    Element coeffB;
    String name = "";
    String S = "";

    Line(Element k, Element b, String name) {
        this.coeffK = k;
        this.coeffB = b;
        this.name = name;
    }

    /**
     * Метод public String toString() создает строку с именем прямой и
     * определяющими её параметрами
     *
     * @return
     */
    @Override
    public String toString() {
        S = name + "=" + "(" + coeffK + "x+" + coeffB + ")";
        System.out.println("" + S);
        return S;
    }

    /**
     * Метод create для перенаправления полученных данных от пользователся в
     * класс DrawElement пакета Graphic2D запускает метод
     * Graphic2D.DrawElement.addLine()
     *
     * @param d
     */
    public void create(DrawElement d) {
        S = name;
        d.coeffK = new Double(coeffK.doubleValue());//k
        d.coeffB = new Double(coeffB.doubleValue());//b
        d.addLine();
        d.name = name;
        d.Xst = d.coeffK + 5;
        d.Yst = d.coeffB - 2;
        d.SetName();
    }

    /**
     * Метод print отвечает за создание объекта класса Graphic2D.DrawElement,
     * передаваемого в метод public void create(DrawElement d)
     */
    @Override
    void print() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                DrawElement draw = new DrawElement();
                ToolBar frame = new ToolBar();
                create(draw);
                ParamPanel o = new ParamPanel();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        Ring r = new Ring("R64[]");
        r.setDefaultRing();
        Line p = new Line(new NumberR64(6), new NumberR64(2), "a");
        p.print();
        p.toString();
    }
}
