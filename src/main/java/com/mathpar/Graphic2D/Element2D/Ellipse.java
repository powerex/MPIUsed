/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic2D.Element2D;

import com.mathpar.Graphic2D.*;
import java.awt.EventQueue;
import javax.swing.JFrame;
import com.mathpar.number.*;

/**
 * Класс Ellipse являющийся наследником класса D2Element, содержит главный
 * конструктор Ellipse(Element x, Element y, Element rx, Element ry, Element
 * alfa, String name)-получение даннных от пользователя. Для построения
 * требуются кординаты центра (x0,y0), радиуса r и 2-х полуосей.
 */
public class Ellipse extends D2Element {
    Element x0;
    Element y0;
    Element radX;
    Element radY;
    Element corner;
    String name = "";
    String S = "";

    Ellipse(Element x, Element y, Element rx, Element ry, Element alfa, String name) {
        this.x0 = x;
        this.y0 = y;
        this.radX = rx;
        this.radY = ry;
        this.corner = alfa;
        this.name = name;
    }

    /**
     * Метод public String toString() создает строку с именем эллипса и
     * определяющими его параметрами
     *
     * @return
     */
    @Override
    public String toString() {
        S = name + "=" + "центр(" + x0 + "," + y0 + "),полусь х = " + radX + ",полуось у =" + radY;
        System.out.println("" + S);
        return S;
    }

    /**
     * Метод create для перенаправления полученных данных от пользователся в
     * класс DrawElement пакета Graphic2D запускает метод
     * Graphic2D.DrawElement.addEllipse()
     *
     * @param d
     */
    public void create(DrawElement d) {
        S = name;
        d.Xvis = new Double(x0.doubleValue());//x
        d.Yvis = new Double(y0.doubleValue());//y
        d.RadXvis = new Double(radX.doubleValue());//Полуось Х
        d.RadYvis = new Double(radY.doubleValue());//Полуось Y
        d.RotationAngle = new Double(corner.doubleValue());//Угол наклона
        d.radiusX = DrawElement.RadXvis * Axes.measure;
        d.radiusY = DrawElement.RadYvis * Axes.measure;
        d.x = DrawElement.Xvis * Axes.measure + Axes.l;
        d.y = -DrawElement.Yvis * Axes.measure + Axes.h;
        ToolBar.AddEll = true;
        d.addEllipse();
        d.name = S;
        d.Xst = DrawElement.x + 5;
        d.Yst = DrawElement.y - 2;
        d.SetName();
    }

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
        Ellipse p = new Ellipse(new NumberR64(0), new NumberR64(0), new NumberR64(5), new NumberR64(3), new NumberR64(0), "A");
        p.print();
        p.toString();
    }
}
