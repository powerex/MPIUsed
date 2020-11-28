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
 * Класс Circle являющийся наследником класса D2Element, содержит главный
 * конструктор Circle(Element x, Element y,Element r, String name)-получение
 * даннных от пользователя. Для построения требуются кординаты центра окружности
 * двух точек (x0,y0) и радиуса r
 */
public class Circle extends D2Element {
    Element x0;
    Element y0;
    Element radius;
    String name = "";
    String S = "";

    Circle(Element x, Element y, Element r, String name) {
        this.x0 = x;
        this.y0 = y;
        this.radius = r;
        this.name = name;
    }

    /**
     * Метод public String toString() создает строку с именем окружности и
     * определяющими её параметрами
     *
     * @return
     */
    @Override
    public String toString() {
        S = name + "=" + "(" + x0 + "," + y0 + "), радиус = " + radius + ")";
        System.out.println("" + S);
        return S;
    }

    /**
     * Метод create для перенаправления полученных данных от пользователся в
     * класс DrawElement пакета Graphic2D запускает метод
     * Graphic2D.DrawElement.addCircle()
     *
     * @param d
     */
    public void create(DrawElement d) {
        S = name;
        d.Xvis = new Double(x0.doubleValue()); //x
        d.Yvis = new Double(y0.doubleValue());//y
        d.RadXvis = new Double(radius.doubleValue());//radius
        d.radiusX = DrawElement.RadXvis * Axes.measure;
        d.x = DrawElement.Xvis * Axes.measure + Axes.l;
        d.y = -DrawElement.Yvis * Axes.measure + Axes.h;
        d.radiusY = DrawElement.radiusX;
        ToolBar.AddCirc = true;
        d.addEllipse();
        d.name = S;
        d.Xst = d.radiusX + 5;
        d.Yst = d.y - 2;
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
        Circle p = new Circle(new NumberR64(1), new NumberR64(-5), new NumberR64(5), "A");
        p.print();
        p.toString();
    }
}
