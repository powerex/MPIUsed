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
 * Класс Interval являющийся наследником класса D2Element, содержит главный
 * конструктор Interval(Element x0, Element y0, Element x, Element y, String
 * name)-получение даннных от пользователя. Для построения требуются кординаты
 * двух точек (x0,y0,x,y)
 */
public class Interval extends D2Element {
    Element x0, x;
    Element y0, y;
    String name = "";
    String S, STR;

    Interval(Element x0, Element y0, Element x, Element y, String name) {
        this.x0 = x0;
        this.y0 = y0;
        this.x = x;
        this.y = y;
        this.name = name;
    }

    /**
     * Метод public String toString() создает строку с именем отрезка и
     * определяющими её параметрами
     *
     * @return
     */
    @Override
    public String toString() {
        S = name + " = [(" + x0 + "," + y0 + "),(" + x + "," + y + ")]";
        System.out.println("" + S);
        return S;
    }

    /**
     * Метод create для перенаправления полученных данных от пользователся в
     * класс DrawElement пакета Graphic2D запускает метод
     * Graphic2D.DrawElement.addInter()
     *
     * @param d
     */
    public void create(DrawElement d) {
        S = name;
        STR = S;
        d.Xvis = new Double(x0.doubleValue());//x0
        d.Yvis = new Double(y0.doubleValue());//y0
        d.Xvis1 = new Double(x.doubleValue());//x1
        d.Yvis1 = new Double(y.doubleValue());//y1
        d.x = DrawElement.Xvis * Axes.measure + Axes.l;
        d.y = -DrawElement.Yvis * Axes.measure + Axes.h;
        d.x1 = DrawElement.Xvis1 * Axes.measure + Axes.l;
        d.y1 = -DrawElement.Yvis1 * Axes.measure + Axes.h;
        d.addInter();
        ToolBar.setName = true;
        d.name = S;
        d.Xst = DrawElement.x + 5;
        d.Yst = DrawElement.y - 2;
        d.SetName();
        ToolBar.setName = true;
        d.name = S;
        d.Xst = DrawElement.x1 + 5;
        d.Yst = DrawElement.y1 - 2;
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
        Interval p = new Interval(new NumberR64(-5), new NumberR64(-5), new NumberR64(5), new NumberR64(5), "A B");
        p.print();
        p.toString();
    }
}
