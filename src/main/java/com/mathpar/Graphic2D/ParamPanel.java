/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic2D;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Данный класс реализует получение данных от пользователя через панели для ввода
 * Для каждого элемента количество данных индивидуально,поэтому размеры окна варьируются в зависимости от
 * этого количества
 *
 * Методы,реализованные в классе:
 * конструктор  public ParamPanel()
 * метод public void InputElement(int n, String[] Name)
 * метод public static void ClickImitation()
 *
 */
public class ParamPanel extends JFrame {

    public static final int DEFAULT_WIDTH = 200;
    public static final int DEFAULT_HEIGHT = 300;
    public static JTextField[] textField;
    public static JLabel[] label;
    public static JButton OK;
    public static boolean ErrOfNum, Click = false;
    JTextField t1, t2;

    /**Конструктор класса
     * Здесь при значении определяющей переменной методу InputElement(int n, String[] Name)
     * передается количество необходимых данных,а также массив с подписями для
     * ячеек для ввода данных
     *
     */
    public ParamPanel() {
        setTitle("Параметры");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLayout(null);

        if (ToolBar.AddCirc) {
            String[] Name = {"X", "Y", "Радиус"};
            InputElement(3, Name);
        }
        if (ToolBar.AddEll) {
            String[] Name = {"X", "Y", "Полуось Х", "Полуось Y", "Угол наклона"};
            InputElement(5, Name);
        }

        if (ToolBar.AddPol) {
            String[] Name = {"Количество вершин многоугольника"};
            InputElement(1, Name);
        }

        if (ToolBar.AddInter) {
            String[] Name = {"Х1", "Y1", "X2", "Y2"};
            InputElement(4, Name);
        }

        if (ToolBar.AddPoint) {
            String[] Name = {"Х", "Y"};
            InputElement(2, Name);
        }
        if (ToolBar.setName) {
            String[] Name = {"Имя точки"};
            InputElement(1, Name);
        }

        if (ToolBar.Sector) {
            String[] Name = {"начальный угол", "угол сектора"};
            InputElement(2, Name);
        }

        if (ToolBar.AddLine) {
            String[] Name = {"k", "b"};
            InputElement(2, Name);
        }

        if (ToolBar.Tangent) {
            String[] Name = {"полярная координата точки касания", "длина касательной"};
            InputElement(2, Name);
        }
        if (ToolBar.CornerMouseAdd) {
            String[] Name = {"величина угла(градусы)"};
            InputElement(1, Name);
        }
        if (ToolBar.Division) {
            String[] Name = {"Деление отрезка в отношении"};
            InputElement(1, Name);
        }
        if (ToolBar.Signature) {
            String[] Name = {"Надпись", "X", "Y"};
            InputElement(3, Name);
        }
    }

    /** Размещение текстовых полей для ввода данных,а также подписей для них на панели
     * Обработка нажатия кнопки "ОК" при завершении ввода
     * Если ввод некорректен,то построение элемента не проводится и появляется сообщение об ошибке
     *
     * Полученные данные при необходимости  перерассчитываются в зависимости от масштаба
     */
    public void InputElement(int n, String[] Name) {

        textField = new JTextField[n];
        label = new JLabel[n];

        int x1 = 10;
        int x2 = 80;
        int y = 10;
        int dy = 0;
        int l1 = 60;
        int l2 = 200;
        int h = 20;
        if (!ToolBar.Division) {
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
        }

        if (ToolBar.AddLine) {
            JLabel L = new JLabel("Уравнение прямой : y=kx+b");
            L.setBounds(x1, y + dy, l2, h);
            add(L);
        }
        if (ToolBar.Division) {
            JLabel L = new JLabel("Деление отрезка в отношении ");
            L.setBounds(x1, y + dy, l2, h);
            add(L);
            JLabel l = new JLabel(" : ");
            l.setBounds(x1 + 65, y + 20, l2, h);
            add(l);
            t1 = new JTextField(" ");
            add(t1);
            t1.setBounds(x1, y + 20, l1, h);
            t2 = new JTextField(" ");
            add(t2);
            t2.setBounds(x1 + 80, y + 20, l1, h);
        }
        OK = new JButton("OK");
        add(OK);
        OK.setBounds(x1, 220, l1, h);
        OK.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    if (ToolBar.AddCirc) {
                        DrawElement.Xvis = Double.parseDouble(textField[0].getText().trim());
                        DrawElement.Yvis = Double.parseDouble(textField[1].getText().trim());
                        DrawElement.RadXvis = Double.parseDouble(textField[2].getText().trim());

                        DrawElement.radiusX = DrawElement.RadXvis * Axes.measure;
                        DrawElement.x = DrawElement.Xvis * Axes.measure + Axes.l;
                        DrawElement.y = -DrawElement.Yvis * Axes.measure + Axes.h;
                        DrawElement.radiusY = DrawElement.radiusX;
                    }
                    if (ToolBar.AddPoint) {
                        DrawElement.Xvis = Double.parseDouble(textField[0].getText().trim());
                        DrawElement.Yvis = Double.parseDouble(textField[1].getText().trim());

                        DrawElement.x = DrawElement.Xvis * Axes.measure + Axes.l;
                        DrawElement.y = -DrawElement.Yvis * Axes.measure + Axes.h;
                    }
                    if (ToolBar.AddEll) {
                        DrawElement.Xvis = Double.parseDouble(textField[0].getText().trim());
                        DrawElement.Yvis = Double.parseDouble(textField[1].getText().trim());
                        DrawElement.RadXvis = Double.parseDouble(textField[2].getText().trim());
                        DrawElement.RadYvis = Double.parseDouble(textField[3].getText().trim());
                        DrawElement.RotationAngle = Double.parseDouble(textField[4].getText().trim());

                        DrawElement.radiusX = DrawElement.RadXvis * Axes.measure;
                        DrawElement.radiusY = DrawElement.RadYvis * Axes.measure;
                        DrawElement.x = DrawElement.Xvis * Axes.measure + Axes.l;
                        DrawElement.y = -DrawElement.Yvis * Axes.measure + Axes.h;
                    }
                    if (ToolBar.AddPol) {
                        ErrOfNum = false;
                        PolyPanel.num = Integer.parseInt(textField[0].getText().trim());
                        if (PolyPanel.num < 3) {
                            PolyPanel.num = 0;
                        }
                        DrawElement.NumOfPol = PolyPanel.num;

                        PolyPanel p = new PolyPanel();
                        p.setVisible(true);
                        ToolBar.AddPol=false;
                    }
                    if (ToolBar.Sector) {
                        DrawElement.startArc = Double.parseDouble(textField[0].getText().trim());
                        DrawElement.Arc = Double.parseDouble(textField[1].getText().trim());
                    }

                    if (ToolBar.AddInter) {
                        DrawElement.Xvis = Double.parseDouble(textField[0].getText().trim());
                        DrawElement.Yvis = Double.parseDouble(textField[1].getText().trim());
                        DrawElement.Xvis1 = Double.parseDouble(textField[2].getText().trim());
                        DrawElement.Yvis1 = Double.parseDouble(textField[3].getText().trim());

                        DrawElement.x = DrawElement.Xvis * Axes.measure + Axes.l;
                        DrawElement.y = -DrawElement.Yvis * Axes.measure + Axes.h;
                        DrawElement.x1 = DrawElement.Xvis1 * Axes.measure + Axes.l;
                        DrawElement.y1 = -DrawElement.Yvis1 * Axes.measure + Axes.h;
                    }
                    if (ToolBar.AddLine) {
                        DrawElement.coeffK = Double.parseDouble(textField[0].getText().trim());
                        DrawElement.coeffB = Double.parseDouble(textField[1].getText().trim());
                    }
                    if (ToolBar.Tangent) {
                        DrawElement.Avis = Double.parseDouble(textField[0].getText().trim());
                        DrawElement.Lvis = Double.parseDouble(textField[1].getText().trim());
                        DrawElement.l = DrawElement.Lvis * Axes.measure;
                        DrawElement.a = (double) (DrawElement.Avis * Math.PI / 180);
                    }
                    if (ToolBar.setName) {
                        DrawElement.name = textField[0].getText();
                        if (DrawElement.name.equals(" ") || DrawElement.name.equals("")) {
                            ToolBar.setName = false;
                        }
                    }
                    if (ToolBar.CornerMouseAdd) {
                        DrawElement.ALPHA = Double.parseDouble(textField[0].getText().trim());
                        DrawElement.alpha = DrawElement.ALPHA * Math.PI / 180;
                    }
                    if (ToolBar.Division) {
                        DrawElement.FirstDiv = Double.parseDouble(t1.getText().trim());
                        DrawElement.SecondDiv = Double.parseDouble(t2.getText().trim());
                    }
                    if (ToolBar.Signature) {
                        DrawElement.S = textField[0].getText().trim();
                        DrawElement.Xvis = Double.parseDouble(textField[1].getText().trim());
                        DrawElement.Yvis = Double.parseDouble(textField[2].getText().trim());
                        DrawElement.x = DrawElement.Xvis * Axes.measure + Axes.l;
                        DrawElement.y = -DrawElement.Yvis * Axes.measure + Axes.h;
                    }

                } catch (NumberFormatException n) {
                    ToolBar.AddCirc = false;
                    ToolBar.AddEll = false;
                    ToolBar.AddInter = false;
                    ToolBar.AddPol = false;
                    ToolBar.Sector = false;
                    ToolBar.AddLine = false;
                    ToolBar.Tangent = false;
                    dispose();
                    PopUp.param = true;
                    PopUp.Error();
                }

                if (!ToolBar.AddPol && !ToolBar.CornerMouseAdd) {
                    ClickImitation();
                }

                dispose();
            }
        });
    }

    /**
     * Метод генерирования нажатия кнопки мыши для перерисовки
     */
    public static void ClickImitation() {
        try {
            Robot robot = new Robot();
            robot.mouseMove(Axes.l + (int) ToolBar.LEFT_ALIGNMENT, Axes.h + (int) ToolBar.TOP_ALIGNMENT);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            if (!ReadFromFile.FromFile) {
                Click = true;
            } else {
                Click = false;
            }
        } catch (Exception a) {
        }
    }
}
