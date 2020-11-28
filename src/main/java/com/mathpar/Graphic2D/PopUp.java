/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic2D;

import javax.swing.*;

/***
 * Данный класс создает всплывающее меню для работы с элементами
 *
 */
public class PopUp extends JFrame {

    public static JPopupMenu popup;
    public static boolean param;
    public static JMenuItem menuItem, menuItem1, menuItem2;
    public static int DEFAULT_WIDTH = 350;
    public static int DEFAULT_HEIGHT = 100;
    public static String S;

    /**
     *Конструктор класса создает всплывающее меню
     */
    public PopUp() {

        popup = new JPopupMenu();
        menuItem = new JMenuItem("Удалить");
        menuItem1 = new JMenuItem("Назвать точку");
        menuItem2 = new JMenuItem("Удалить имя точки");
        popup.add(menuItem);
        popup.add(menuItem1);
        popup.add(menuItem2);
        menuItem1.setEnabled(false);
        menuItem2.setEnabled(false);
        add(popup);
        popup.setVisible(false);
    }

    /**
     * Создается новое окно для вывода сообщений об ошибке, в зависимости от ее типа
     */
    public static void Error() {
        if (ToolBar.setName) {
            S="Точка с таким именем уже имеется на плоскости";

        }
        if (param) {
            S="Недостаточно данных или данные некорректны";

        }
        JFrame f = new JFrame();
        f.setLocation(Axes.l, Axes.h);
        f.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        f.setTitle("ОШИБКА!");
        JLabel l = new JLabel(S);
        f.add(l);
        l.setBounds(10, 10, 300, 60);
        f.setVisible(true);
    }



}

