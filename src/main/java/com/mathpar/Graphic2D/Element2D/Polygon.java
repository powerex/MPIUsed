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
 * Класс Polygon являющийся наследником класса D2Element, содержит главный
 * конструктор Polygon(int num, double[] pX, double[] pY, String name)-получение
 * даннных от пользователя. Для построения требуются кординаты точек
 * многоугольника (x0,x1,x2,...),(y0,y1,y2,...)
 */
public class Polygon extends D2Element {
    double[] pX, pY;
    int num;//-кол-во вершин многоугольника
    String name = "";
    String S;

    Polygon(int num, double[] pX, double[] pY, String name) {
        this.num = num;
        this.pX = pX;
        this.pY = pY;
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
        S = "= вершины ";
        for (int i = 0; i < num; i++) {
            S = "(" + pX[i] + ", " + pY[i] + ")";
        }
        System.out.println("" + S);
        return S;
    }

    /**
     * Метод create для перенаправления полученных данных от пользователся в
     * класс DrawElement пакета Graphic2D запускает метод
     * Graphic2D.DrawElement.addPolygon()
     *
     * @param d
     */
    public void create(DrawElement d) {
        S = name;
        ToolBar.numPolPoint = true;
        d.NumOfPol = num;
        d.pX = new double[num];
        d.PolyX = new int[num];
        d.PolyY = new int[num];
        d.pY = new double[num];
        for (int k = 0; k < num; k++) {
            DrawElement.pX[k] = pX[k];
            DrawElement.PolyX[k] = (int) (pX[k] * Axes.measure + Axes.l);
            DrawElement.pY[k] = pY[k];
            DrawElement.PolyY[k] = (int) (-DrawElement.pY[k] * Axes.measure + Axes.h);
        }
        ToolBar.numPolPoint = true;
        DrawElement.addPolygon();
        d.name = S;
        d.Xst = d.pX[1] + 5;
        d.Yst = d.pY[1] - 2;
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
        Polygon p = new Polygon(4, new double[] {1, 3, 10, 0}, new double[] {2, 4, 6, 9}, "A");
        p.print(); 
        p.toString();
    }
}
