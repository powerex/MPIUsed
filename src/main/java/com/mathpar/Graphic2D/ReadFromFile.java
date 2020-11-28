/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic2D;

import java.util.ArrayList;
import java.awt.geom.*;

/**
 * *
 *
 * Данный метод реализует чтение информации об элементах методы: public
 * ReadFromFile()-создание списка строк файла public void Modify(int
 * j)-приведение строки в определенный единый для всех строк вид public void
 * ReadAndDel()-комбинация 3 действий при чтении слов public void Search()
 * -определение элемента,данные о котором считываются public void
 * ReadName()-чтение данных об именах точек public void ReadPoint()-чтение
 * данных о точке public void ReadInterval()-чтение данных об отрезке,и о точке
 * деления если она имеется public void ReadCorner()-чтение данных об угле
 * public void ReadCircle()- чтение данных об окружности,и о привязанных к ней
 * элементах public void ReadEllipse()- чтение данных об эллипсе,и о привязанных
 * к нему элементах public void ReadPolygon()- чтение данных о многоугольнике
 * public void ReadLine()- чтение данных о прямой
 */
public class ReadFromFile extends DrawElement {
    public static ArrayList<String> STR;
    public static boolean FromFile;
    public String tmpString;
    /**
     * Текстовая команда
     */
    public String nameС;
    /**
     * Первая часть текстовой команды - Имя объекта
     */
    public String nameElement;
    public StringBuffer S;

    /**
     *
     */
    public ReadFromFile() {
        FromFile = false;
        STR = new ArrayList<String>();
        STR = ToolBar.S;
        if (STR.isEmpty() != true) {
            for (int i = 0; i < STR.size(); i++) {
                tmpString = null;
                S = null;
                Modify(i);
                Search();
            }
        }
    }

    /**
     *
     * @param str
     */
    public ReadFromFile(String[] str) {
//        String S2 = "Окружность =( центр( 0 , 0 ); радиус (5) ;Сектор=(0, 30); Касательная=(20 , 10));";
//        String S3 = "Эллипс = центр( 0 , 5 ); полуосьХ  6, полуосьY 2 ; угол( 30 );Сектор=(0, 90); Касательная=(90 , 10);";
//        String S4 = "Угол =  значение 90, точки( (0, 0) , (5, 0) , (0,0) )";
//        String S5 = "Прямая = (10 , 2);";
//                String S55 = "Прямая = (2 , 1);";
//        String S6 = "Отрезок = (начало(0 , 0), конец (10, 10 ) ; деление( 2 : 1)) ";
//        String S7 = "Точка = (0,0)";
//        String S77 = "Точка = (1,1)";
//        String S8 = "Многоугольник = вершины (0,0),(5,0),(5,5) ... ";
//        String S9 = "Обозначения =( А(0 , 0), В(5, 0 ), C(5,5)) ";
//        String S10= "Масштаб = (уменьшить) ";
        FromFile = false;
        STR = new ArrayList<String>();
        for (int i = 0; i < str.length; i++) {
            str[i] = str[i].toUpperCase();
            STR.add(str[i]);
        }
        if (STR.isEmpty() != true) {
            for (int i = 0; i < STR.size(); i++) {
                tmpString = null;
                S = null;
                Modify(i);
                Search();
            }
        }
    }

    /**
     *
     * @return
     */
    public static double[] getPX() {
        return pX;
    }

    /**
     * Преобразет строку,осталвяя в ней только определяющие слова и данные.
     */
    public void Modify(int j) {
        S = new StringBuffer(STR.get(j));
        for (int i = 0; i < STR.get(j).length(); i++) {
            if (!(Character.isLetterOrDigit(STR.get(j).charAt(i)) || STR.get(j).charAt(i) == '-' || STR.get(j).charAt(i) == '.')) {
                S.setCharAt(i, ' ');
            }
        }
        if (!S.toString().equals("")) {
            while (S.charAt(0) == ' ') {
                S.deleteCharAt(0);
            }
            while (S.indexOf("  ") != -1) {
                S.delete(S.indexOf("  "), S.indexOf("  ") + 1);
            }
            if (S.charAt(S.length() - 1) != ' ') {
                S.append(' ');
            }
        }
    }

    /**
     * Считываение подстроки с начала строки до пробела, удаление этого набора
     * символов из строки,
     *
     */
    public void ReadAndDel() {
        tmpString = S.substring(0, S.indexOf(" ") + 1);
        S.delete(0, S.indexOf(" ") + 1);
    }

    /**
     * По первому слову определяется к какому элементу имеет отношение
     * информация
     */
    public void Search() {
        tmpString = S.substring(0, S.indexOf(" ") + 1);
        nameElement = tmpString;
        nameС = S.toString();
        if (tmpString.equals("ОБОЗНАЧЕНИЯ ")) {
            ReadName();
        }
        if (tmpString.equals("ТОЧКА ")) {
            ReadPoint();
        }
        if (tmpString.equals("ОТРЕЗОК ")) {
            ReadInterval();
        }
        if (tmpString.equals("ОКРУЖНОСТЬ ")) {
            ReadCircle();
        }
        if (tmpString.equals("ЭЛЛИПС ")) {
            ReadEllipse();
        }
        if (tmpString.equals("МНОГОУГОЛЬНИК ") || tmpString.equals("ТРЕУГОЛЬНИК ")) {
            ReadPolygon();
        }
        if (tmpString.equals("ПРЯМАЯ ")) {
            ReadLine();
        }
        if (tmpString.equalsIgnoreCase("УГОЛ ")) {
            ReadCorner();
        }
        if (tmpString.equals("МЕДИАНА ") || tmpString.equals("ВЫСОТА ") || tmpString.equals("БИССЕКТРИСА ")) {
            ReadTriangleEl();
        }
        FromFile = true;
        //ParamPanel.ClickImitation();
        FromFile = false;
    }

    /**
     * Считывание данных об именах точек Считывается имя и координата
     * расположения на плоскости
     */
    public void ReadName() {
        S.delete(0, S.indexOf(" ") + 1);
        while (S.indexOf(" ") != -1) {
            ReadAndDel();
            name = tmpString;
            ReadAndDel();
            double x = Double.parseDouble(tmpString);
            Xst = x * Axes.measure + Axes.l;
            ReadAndDel();
            double y = Double.parseDouble(tmpString);
            Yst = -y * Axes.measure + Axes.h;
            ToolBar.setName = true;
            SetName();
        }
    }

    /**
     * считывание данных о точке считываются 2 координаты точки
     */
    public void ReadPoint() {
        S.delete(0, S.indexOf(" ") + 1);
        ReadAndDel();
        Xvis = Double.parseDouble(tmpString);
        ReadAndDel();
        Yvis = Double.parseDouble(tmpString);

        x = Xvis * Axes.measure + Axes.l;
        y = -Yvis * Axes.measure + Axes.h;

        ToolBar.AddPoint = true;
        addPoint();
    }

    /**
     * считывание данных об отрезке передаем переменным координаты начала и
     * конца, а также если указано отношение, в котором задано деление отрезка
     */
    public void ReadInterval() {
        S.delete(0, S.indexOf(" ") + 1);
        while (S.indexOf(" ") != -1) {
            tmpString = S.substring(0, S.indexOf(" ") + 1);
            S.delete(0, S.indexOf(" ") + 1);
            if (!tmpString.equalsIgnoreCase("ДЕЛЕНИЕ ")) {
                if (tmpString.equalsIgnoreCase("НАЧАЛО ")) {
                    ReadAndDel();
                    Xvis = Double.parseDouble(tmpString);
                    ReadAndDel();
                    Yvis = Double.parseDouble(tmpString);

                    x = Xvis * Axes.measure + Axes.l;
                    y = -Yvis * Axes.measure + Axes.h;
                }

                if (tmpString.equalsIgnoreCase("КОНЕЦ ")) {
                    ReadAndDel();
                    Xvis1 = Double.parseDouble(tmpString);
                    ReadAndDel();
                    Yvis1 = Double.parseDouble(tmpString);

                    x1 = Xvis1 * Axes.measure + Axes.l;
                    y1 = -Yvis1 * Axes.measure + Axes.h;
                    ToolBar.AddInter = true;
                    addInter();
                }

            } else {
                ReadAndDel();
                FirstDiv = Double.parseDouble(tmpString);
                ReadAndDel();
                SecondDiv = Double.parseDouble(tmpString);

                ToolBar.Division = true;
                addDivision(null);
            }
        }


    }

    /**
     * считывание данных угла Получаем из строки координаты 3 базовых точек и
     * значения угла
     */
    public void ReadCorner() {
        S.delete(0, S.indexOf(" ") + 1);
        while (S.indexOf(" ") != -1) {
            tmpString = S.substring(0, S.indexOf(" ") + 1);
            S.delete(0, S.indexOf(" ") + 1);
            if (tmpString.equals("ЗНАЧЕНИЕ ")) {
                ReadAndDel();
                ALPHA = Double.parseDouble(tmpString);
                alpha = ALPHA * Math.PI / 180;
            }
            if (tmpString.equalsIgnoreCase("ТОЧКИ ")) {
                for (int i = 0; i < 3; i++) {
                    ReadAndDel();
                    Xvis = Double.parseDouble(tmpString);
                    ReadAndDel();
                    Yvis = Double.parseDouble(tmpString);
                    x = Xvis * Axes.measure + Axes.l;
                    y = -Yvis * Axes.measure + Axes.h;
                    TP.add(new Point2D.Double(x, y));
                }

            }
        }
        ToolBar.AddCorner = true;
        addCorner();
    }

    /**
     * считывание данных окружности Из строки берем координаты центра, значение
     * радиуса. Если есть дополнительные элементы,то у сектора узнаем полярную
     * координату начала сектора и величину разворота У касательной -полярную
     * координату точки касания и длину самой касательной
     */
    public void ReadCircle() {
        S.delete(0, S.indexOf(" ") + 1);
        while (S.indexOf(" ") != -1) {
            tmpString = S.substring(0, S.indexOf(" ") + 1);
            S.delete(0, S.indexOf(" ") + 1);

            if (tmpString.equals("ЦЕНТР ")) {
                ReadAndDel();
                Xvis = Double.parseDouble(tmpString);
                ReadAndDel();
                Yvis = Double.parseDouble(tmpString);
                ReadAndDel();
                Label = tmpString.trim();

                x = Xvis * Axes.measure + Axes.l;
                y = -Yvis * Axes.measure + Axes.h;
            }

            if (tmpString.equals("РАДИУС ")) {
                ReadAndDel();
                RadXvis = Double.parseDouble(tmpString);
                radiusX = RadXvis * Axes.measure;
                radiusY = radiusX;

            }

            if (tmpString.equals("СЕКТОР ")) {
                ReadAndDel();
                startArc = Double.parseDouble(tmpString);
                ReadAndDel();
                Arc = Double.parseDouble(tmpString);

                rArcX = 2 * radiusX;
                rArcY = rArcX;
                x1 = x - radiusX;
                y1 = y - radiusY;

                ToolBar.Sector = true;
            }

            if (tmpString.equals("КАСАТЕЛЬНАЯ ")) {
                ReadAndDel();
                Avis = Double.parseDouble(tmpString);
                ReadAndDel();
                Lvis = Double.parseDouble(tmpString);

                l = Lvis * Axes.measure;
                a = (double) (Avis * Math.PI / 180);

                ToolBar.Tangent = true;
            }

        }

        ToolBar.AddCirc = true;
        addEllipse();
    }

    /**
     * считывание данных эллипса Из строки берем координаты центра, значение
     * полуосей,значение наклона эллипса относительно ОХ Если есть
     * дополнительные элементы,то у сектора узнаем полярную координату начала
     * сектора и величину разворота У касательной -полярную координату точки
     * касания и длину самой касательной
     */
    public void ReadEllipse() {
        S.delete(0, S.indexOf(" ") + 1);
        while (S.indexOf(" ") != -1) {
            tmpString = S.substring(0, S.indexOf(" ") + 1);
            S.delete(0, S.indexOf(" ") + 1);

            if (tmpString.equals("ЦЕНТР ")) {
                ReadAndDel();
                Xvis = Double.parseDouble(tmpString);
                ReadAndDel();
                Yvis = Double.parseDouble(tmpString);
                ReadAndDel();
                Label = tmpString.trim();

                x = Xvis * Axes.measure + Axes.l;
                y = -Yvis * Axes.measure + Axes.h;
            }

            if (tmpString.equalsIgnoreCase("ПОЛУОСЬХ ") || tmpString.equalsIgnoreCase("ПОЛУОСЬX ")) {
                ReadAndDel();
                RadXvis = Double.parseDouble(tmpString);
                radiusX = RadXvis * Axes.measure;
            }

            if (tmpString.equalsIgnoreCase("ПОЛУОСЬY ") || tmpString.equalsIgnoreCase("ПОЛУОСЬУ ")) {
                ReadAndDel();
                RadYvis = Double.parseDouble(tmpString);
                radiusY = RadYvis * Axes.measure;
            }

            if (tmpString.equalsIgnoreCase("НАКЛОН ")) {
                ReadAndDel();
                RotationAngle = Double.parseDouble(tmpString);
            }

            if (tmpString.equals("СЕКТОР ")) {
                ReadAndDel();
                startArc = Double.parseDouble(tmpString);
                ReadAndDel();
                Arc = Double.parseDouble(tmpString);

                rArcX = 2 * radiusX;
                rArcY = 2 * radiusY;
                x1 = x - radiusX;
                y1 = y - radiusY;

                ToolBar.Sector = true;
                AddSector(null);
            }

            if (tmpString.equals("КАСАТЕЛЬНАЯ ")) {
                ReadAndDel();
                Avis = Double.parseDouble(tmpString);
                ReadAndDel();
                Lvis = Double.parseDouble(tmpString);

                l = Lvis * Axes.measure;
                a = (double) (Avis * Math.PI / 180);

                ToolBar.Tangent = true;
                addTangent(null);
            }


        }
        ToolBar.AddEll = true;
        addEllipse();
    }

    /**
     * считывание данных многоугольника Получаем из стрки координаты вершин
     */
    public void ReadPolygon() {
        S.delete(0, S.indexOf(" ") + 1);
        tmpString = S.substring(0, S.indexOf(" ") + 1);
        S.delete(0, S.indexOf(" ") + 1);

        if (tmpString.equals("ВЕРШИНЫ ")) {
            int k = 0;
            int num = 0;
            StringBuffer cS = new StringBuffer(S);
            while (cS.indexOf(" ") != -1) {
                cS.delete(0, cS.indexOf(" ") + 1);
                num++;
            }
            num = num / 2;
            NumOfPol = num;

            PolyX = new int[num];
            PolyY = new int[num];
            pX = new double[num];
            pY = new double[num];

            for (k = 0; k < num; k++) {
                ReadAndDel();
                pX[k] = Double.parseDouble(tmpString);
                PolyX[k] = (int) (pX[k] * Axes.measure + Axes.l);
                ReadAndDel();
                pY[k] = (Double.parseDouble(tmpString));
                PolyY[k] = (int) (-pY[k] * Axes.measure + Axes.h);
            }
        }

        ToolBar.numPolPoint = true;
        addPolygon();
    }

    /**
     * считывание данных прямой извлекаем значение коэффициентов уравнения
     * прямой y=kx+b
     */
    public void ReadLine() {
        S.delete(0, S.indexOf(" ") + 1);
        tmpString = S.substring(0, S.indexOf(" ") + 1);
        S.delete(0, S.indexOf(" ") + 1);
        coeffK = Double.parseDouble(tmpString);
        ReadAndDel();
        coeffB = Double.parseDouble(tmpString);

        ToolBar.AddLine = true;
        addLine();
    }

    /**
     * считывание данных для построения высоты, медианы и бисектрисы
     */
    public void ReadTriangleEl() {
        S.delete(0, S.indexOf(" ") + 1);
        S.delete(0, S.indexOf(" ") + 1);
        tmpString = S.substring(0, S.indexOf(" ") + 1);
        S.delete(0, S.indexOf(" ") + 1);

        if (tmpString.equals("ВЕРШИНЫ ")) {
            int k = 0;
            int num = 0;
            StringBuffer cS = new StringBuffer(S);
            while (cS.indexOf(" ") != -1) {
                cS.delete(0, cS.indexOf(" ") + 1);
                num++;
            }
            num = (num / 2) - 1;
            NumOfPol = num;

            PolyX = new int[num];
            PolyY = new int[num];
            pX = new double[num];
            pY = new double[num];

            for (k = 0; k < num; k++) {
                ReadAndDel();
                pX[k] = Double.parseDouble(tmpString);
                PolyX[k] = (int) (pX[k] * Axes.measure + Axes.l);
                ReadAndDel();
                pY[k] = (Double.parseDouble(tmpString));
                PolyY[k] = (int) (-pY[k] * Axes.measure + Axes.h);
            }

            ReadAndDel();
            pointFrom[0] = (Double.parseDouble(tmpString));
            ReadAndDel();
            pointFrom[1] = (Double.parseDouble(tmpString));
        }

        ToolBar.numPolPoint = true;
        addPolygon();
    }
}
