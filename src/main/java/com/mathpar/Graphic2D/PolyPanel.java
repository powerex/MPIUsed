/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic2D;

import java.awt.event.*;
import javax.swing.*;

/**
 *    Данный класс реализует считывание данных для многоугольника
 * Количество текстовых полей для ввода варьируется в зависимости от количества вершин
 * Метод InputXY(int n) обрабатывает нажатие кнопки "ОК" и при некорректности данных
 * выдает сообщение об ошибке
 */
public class PolyPanel extends JFrame {

    public static JTextField[] textField;
    public static JLabel[] label;
    public static int DEFAULT_WIDTH = 150;
    public static int DEFAULT_HEIGHT;
    public static int num;
    public static String[] Name;

    public PolyPanel() {
        ToolBar.numPolPoint = true;
        setTitle("Координаты вершин");

        DEFAULT_HEIGHT = 30 * (2 * num + 4);
        if (num == 0) {
            DEFAULT_WIDTH = 300;
        }
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLayout(null);
        if (ToolBar.numPolPoint) {
            Name = new String[2 * num];
            int k = 0;
            for (int i = 0; i < 2 * num - 1; i++) {
                if (i % 2 == 0) {
                    k++;
                    Name[i] = "X" + k;
                    Name[i + 1] = "Y" + k;
                    i++;
                }
            }

            InputXY(num * 2);

        }

    }
/**метод строит окно для ввода данных,
 * обрабатывает событие кнопки ОК по завершению ввода данных,
 * считывает введенные значения и присваивает их соответствующим переменным с учетом масштабирования
 */
    public void InputXY(int n) {
        textField = new JTextField[n];
        if (num == 0) {
            label = new JLabel[1];
            label[0] = new JLabel("Ошибка! Попробуйте повторить ввод данных");
            add(label[0]);
            label[0].setBounds(10, 10, 300, 15);
        }
        label = new JLabel[n];
        int x1 = 10;
        int x2 = 80;
        int y = 10;
        int dy = 0;
        int l1 = 60;
        int l2 = 100;
        int h = 20;
        for (int i = 0; i < n; i++) {
            textField[i] = new JTextField("");
            add(textField[i]);
            textField[i].setBounds(x1, y + dy, l1, h);
            label[i] = new JLabel(Name[i]);
            add(label[i]);
            label[i].setBounds(x2, y + dy, l2, h);
            y = y + dy;
            dy = 30;
        }
        JButton OK = new JButton("OK");
        add(OK);
        OK.setBounds(x1, y + 40, l1, h);
        OK.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                actionPoly_Performed();
                ParamPanel.ClickImitation();
                dispose();
            }
        });
    }
/**Метод обработки информации,полученной через диалоговое окно.
 * Записывается массив координат х и у вершин многоугольника,вычисленные с учетом масштаба
 */
    public void actionPoly_Performed() {
        try {
            if (ToolBar.numPolPoint) {
                int k = 0;
                int p = 0;
                DrawElement.PolyX = new int[num];
                DrawElement.PolyY = new int[num];
                DrawElement.pX = new double[num];
                DrawElement.pY = new double[num];
                for (int i = 0; i < 2 * num; i++) {
                    if (textField[i].getText().length()!=0) {
                        if (i % 2 == 0) {
                            DrawElement.pX[k] = (Double.parseDouble(textField[i].getText().trim()));
                            DrawElement.PolyX[k] = (int) (DrawElement.pX[k] * Axes.measure + Axes.l);
                            k++;
                        } else {
                            DrawElement.pY[p] = (Double.parseDouble(textField[i].getText().trim()));
                            DrawElement.PolyY[p] = (int) (-DrawElement.pY[p] * Axes.measure + Axes.h);
                            p++;
                        }
                    } else {
                        if (i % 2 == 0) {
                            DrawElement.PolyX[k] = Axes.l;
                            k++;
                        } else {
                            DrawElement.PolyY[p] = Axes.h;
                            p++;
                        }
                    }
                }
            }
        } catch (NumberFormatException n) {
            ToolBar.numPolPoint = false;
            dispose();
        }
    }
}

