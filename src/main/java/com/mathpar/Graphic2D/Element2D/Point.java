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
 * Класс Point являющийся наследником класса D2Element, содержит главный
 * конструктор Point(Element x, Element y, String name) -получение даннных от
 * пользователя Для построения требуются только кординаты (x,y)
 */
public class Point extends D2Element {
    Element x;
    Element y;
    String name = "";
    String S = "";

    Point(Element x, Element y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    /**
     * Метод public String toString() создает строку с именем точки и
     * определяющими её параметрами
     *
     * @return
     */
    @Override
    public String toString() {
        S = name + "=(" + x + "," + y + ")";
        System.out.println("" + S);
        return S;
    }

    /**
     * Метод create для перенаправления полученных данных от пользователся в
     * класс DrawElement пакета Graphic2D, запускает метод
     * Graphic2D.DrawElement.addPoint()
     *
     * @param d
     */
    public void create(DrawElement d) {
        S = name;
        d.Xvis = new Double(x.doubleValue());//x
        d.Yvis = new Double(y.doubleValue());//y
        d.x = DrawElement.Xvis * Axes.measure + Axes.l;
        d.y = -DrawElement.Yvis * Axes.measure + Axes.h;
        d.addPoint();
        d.name = S;
        d.Xst = DrawElement.x + 5;
        d.Yst = DrawElement.y - 2;
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
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });

    }

    public static void main(String[] args) {
        Ring r = new Ring("R64[]");
        r.setDefaultRing();
        Point p1 = new Point(new NumberR64(0), new NumberR64(0), "А");
        p1.print();
        p1.toString();
    }
}
