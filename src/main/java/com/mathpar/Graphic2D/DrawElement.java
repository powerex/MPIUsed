/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic2D;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.awt.Paint;

import static com.mathpar.Graphic2D.FillElement.g2d;

/**
 * Данный класс реализовывает графическое построение планиметрических элементов
 * по данным,полученным от пользователя. Для хранения объектов используются
 * динамические списки,что позволяет легко получить доступ или найти необходимый
 * элемент. В этом же классе содержатся подклассы,отвечающий за различные
 * манипуляции мыши,такие как щелчок,нажатие,перемещение,отпускание кнопки мыши.
 * Методы, реализованные в классе class DrawElement extends JComponent: 1.
 * Методы поиска среди объектов списка по принадлежности точки указателя мыши
 * public Line2D findLine(Point2D p) public Ellipse2D findEll(Point2D p) public
 * Line2D findInter(Point2D p) public Polygon findPolygon(Point2D p) public
 * Ellipse2D findPoint(Point2D p) public Arc2D findArc(Point2D p) public
 * Ellipse2D findDivision(Point2D p) public Ellipse2D findFromAllPoints(Point2D
 * p) public static Line2D findTang(Point2D p) public static Rectangle2D
 * findSign(Point2D p) public static Line2D findCorner(Point2D p)
 *
 * 2.Методы добавления в списки новых объектов и сопутствующих им данных public
 * static void addSignature() public static void addDivision(Line2D l) public
 * static void addInter() public static void AddSector(Ellipse2D e) public
 * static void addTangent(Ellipse2D e) public static void addCorner() public
 * static void addEllipse() public static void addPoint() public static void
 * addPolygon() public static void SetName()
 *
 * 3. Методы удаления из списка определенного элемента public void
 * removeBound(Rectangle2D r) public void removeCorner(int k) public void
 * removeDivision(Ellipse2D e) public void removeInter(Line2D l) public void
 * removePolygon(Polygon s) public static void removeArcWithEll(Ellipse2D p)
 * public static void removeTanWithEll(Ellipse2D p) public void
 * removelEllipse(Ellipse2D s, Ellipse2D p) public void removeInter(Line2D p,
 * Ellipse2D t1, Ellipse2D t2) public void removeSector(Arc2D arc) public void
 * removeTangent(Line2D tan) public void removePoint(Ellipse2D e) public void
 * removeInter(Line2D l) public void removeAllObjects()
 *
 * 4. Метод для сортировки объектов в списке public static void change(ArrayList
 * ob, int i)
 *
 * 5. Метод вывода объектов на экран public void paintComponent(Graphics g)
 *
 * 6. Метод перерассчета координат объъектов при изменении масштаба public void
 * resize()
 *
 * 7.Метод выбора нужного обекта из всех,которым принадлежит координата
 * указателя мыши public void GetLessShape()
 *
 *
 * Методы реализованные в подклассе public class MouseHandler extends
 * MouseAdapter:
 *
 * 1. Стандартные методы класса обработки событий мыши public void
 * mousePressed(MouseEvent event) public void mouseClicked(MouseEvent event)
 * public void mouseReleased(MouseEvent event)
 *
 * 2. Методы добавления элементов с помощью мыши public void Corner_mouse()
 * public void Polygon_mouse() public void Interval_mouse() public void
 * Point_mouse() public void Line_mouse() public void Circle_mouse()
 *
 * 3. Метод изменения масштаба public void scale(double factor, double x, double
 * y)
 *
 * Методы реализованные в подклассе private class MouseMotionHandler implements
 * MouseMotionListener:
 *
 * 1. Методы обработки событий мыши public void mouseDragged(MouseEvent event)
 * public void mouseMoved(MouseEvent event)
 *
 * 2. Методы для перемещения элементов public static void movePointName(int k)
 * public static int find_name(Ellipse2D e) public void movePoint(MouseEvent e)
 * public void moveSign(MouseEvent e) public void moveInterval(MouseEvent e)
 * public void moveCorner(MouseEvent e) public void moveEllipse(MouseEvent e)
 * public void movePolygon(MouseEvent e)
 *
 *
 */
public class DrawElement extends JComponent{

    private static final long serialVersionUID = -288926549841664381L;
    public static int NumOfPol;
    public static double pointRad = 8;
    /**
     * ПЕременные,используемые для хранения данных об угле,его построения и
     * перемещения
     */
    public static double a, l, Lvis, Avis, alpha, betta, ALPHA;
    /**
     * список базовых отрезков угла
     */
    public static ArrayList<Line2D> based;
    /**
     * список велечин углов
     */
    public static ArrayList<Double> corners;
    /**
     * список второго образеющего отрезка
     */
    public static ArrayList<Line2D> cornLine;
    /**
     * список списков точек отрезков,образующих угол
     */
    public static ArrayList<ArrayList<Ellipse2D>> pCorner;
    /**
     * промежуточный список точек угла,который добавляется в основной pCorner
     */
    public static ArrayList<Ellipse2D> pC;
    /**
     * Переменные,используемые для хранения информации о точке деления отрезка в
     * заданном отношении и для его построения
     */
    /**
     * список отношения деления отрезка
     */
    public static ArrayList<Double> Division;
    public static double FirstDiv, SecondDiv;
    /**
     * список элементов-точек деления
     */
    public static ArrayList<Ellipse2D> pDivision;
    /**
     * текущая точка деления
     */
    public static Ellipse2D currentDiv;
    public static Line2D L;
    /**
     * список хэш-кодов отрезков для определения принадлежности точки
     */
    public static ArrayList<Integer> hashDiv;
    public static int hash;
    /**
     * ПЕременные для хранения и вывода на экран надписи.
     */
    /**
     * список строк-надписей
     */
    public static ArrayList<String> sign;
    /**
     * список прямоугольников-невидимых рамок надписей
     */
    public static ArrayList<Rectangle2D> coordSign;
    /**
     * текущая надпись
     */
    public static Rectangle2D currentBound;
    /**
     * Список,хранящий значения поворота эллипса
     */
    public static ArrayList<Double> Rotate;
    public static double RotationAngle;
    /**
     * Списки,хранящие параметры окружности(эллипса),к которому относится сектор
     */
    public static ArrayList<Point2D> cArc, rArc;
    /**
     * списки секторов
     */
    public static ArrayList<Arc2D> sectors;
    /**
     * текущий сектор
     */
    public static Arc2D currentArc;
    /**
     * списки касательных-отрезок
     */
    public static ArrayList<Line2D> tangents;
    /**
     * Списки,хранящие параметры окружности(эллипса),к которому относится
     * касательная
     */
    public static ArrayList<Point2D> cTan, rTan;
    /**
     * список окружностей,эллипсов
     */
    public static ArrayList<Ellipse2D> circles;
    /**
     * список центральных точек окружностей и эллипсов
     */
    public static ArrayList<Ellipse2D> pEll;
    public static double radiusX, radiusY;// значения полуосей(экранные)
    public static double RadXvis, RadYvis;// значения полуосей(пользовательские)
    public static Ellipse2D currentEll, TCircle, TCircle1;// текущий
    // эллипс,промежуточные
    // построения
    // эллипсов
    public static Ellipse2D currentCP;// текущая центральная точка
    /**
     * списки начала и конца отрезка
     */
    public static ArrayList<Ellipse2D> pInter1, pInter2;
    /**
     * список вершин для 1 многоугольника
     */
    public static ArrayList<Ellipse2D> pP;
    /**
     * список точек касания касательной к окружности или эллипсу
     */
    public static ArrayList<Ellipse2D> pTang;
    /**
     * список опорных точек на прямых
     */
    public static ArrayList<Ellipse2D> pLine;
    /**
     * список хранящий списки вершин многоугольников
     */
    public static ArrayList<ArrayList<Ellipse2D>> pPoly;// вершины
    // многоугольников
    public static int[] PolyX, PolyY;
    /**
     * массивы координат х и у вершин многоугольника
     */
    public static double[] pX, pY;

    /**
     * Точка, с которой будет опущена высота/медиана/бисектриса
    */
    public static double[] pointFrom = new double[2];
    
     public double[] pXX, pYY;
    /**
     * списки многоугольников
     */
    public static ArrayList<Polygon> polygon;// многоугольники
    public static Polygon currentPoly, TPolygon;// текущий многоугольник
    /**
     * списки точек
     */
    public static ArrayList<Ellipse2D> points;// самостоятельные точки
    public static Ellipse2D currentPoint, TPoint1, TPoint2, e;
    /**
     * список отрезков
     */
    public static ArrayList<Line2D> intervals;
    public static ArrayList<Line2D> TInterval;
    /**
     * список прямых
     */
    public static ArrayList<Line2D> lines;
    public static Line2D currentInter, inter, currentTang, currentLine,
            currentCorner;// текущий отрезок
    /**
     * Строки хранения данных об объектах
     */
    public static ArrayList<String> ParamEllipse, ParamPoly, ParamLine,
            ParamInter, ParamPoint, PointsNames, ParamCorner, ParamDiv,
            ParamSector, ParamTangent;
    /**
     * Переменные хранения и вывода имен точек
     */
    public static String name, STRnames;
    public static ArrayList<Point2D> nameXY;
    /**
     * переменные для определения очередности положения элементов на плоскости
     */
    boolean getEll, getRect, getInterval, getPoint, getArc, getTang, getLine,
            EndMouseMove = false, BeginMove;
    // вспомогательные переменные
    public static double x, y, x1, y1, x0, y0;
    public static double Xvis, Yvis, Xvis1, Yvis1, Xvis2, Yvis2;
    public static ArrayList<Point2D> TP;
    public static double factor; // переменная изменения масштаба
    public static double Xst, Yst, startArc, Arc, rArcX, rArcY;
    public static int dx, dy, k;
    public static Graphics2D g2;
    /**
     * коэффициенты прямой у=kx+b
     */
    public static double coeffB, coeffK;
    public static int[] nArc, nTan;
    public static int nc = 0;
    public static String S, Label;
    public static boolean StopResize;

    
    /**
     * Объявления всех динамических списков, Подключение слушателя событий мыши
     */
    public DrawElement() {
        nArc = new int[0];
        nTan = new int[0];
        nameXY = new ArrayList<Point2D>();
        PointsNames = new ArrayList<String>();
        pPoly = new ArrayList<ArrayList<Ellipse2D>>();
        Rotate = new ArrayList<Double>();
        pInter1 = new ArrayList<Ellipse2D>(); // точка радиуса на
        // окружности(можно ставить
        // точку мышью!!!!!)
        pInter2 = new ArrayList<Ellipse2D>();
        circles = new ArrayList<Ellipse2D>();
        pEll = new ArrayList<Ellipse2D>();
        intervals = new ArrayList<Line2D>();
        points = new ArrayList<Ellipse2D>();
        sectors = new ArrayList<Arc2D>();
        ParamSector = new ArrayList<String>();
        rArc = new ArrayList<Point2D>();
        cArc = new ArrayList<Point2D>();
        rTan = new ArrayList<Point2D>();
        cTan = new ArrayList<Point2D>();
        currentEll = null;
        currentCP = null;
        currentInter = null;
        ParamTangent = new ArrayList<String>();
        ParamEllipse = new ArrayList<String>();
        ParamPoly = new ArrayList<String>();
        ParamInter = new ArrayList<String>();
        ParamPoint = new ArrayList<String>();
        ParamLine = new ArrayList<String>();
        ParamDiv = new ArrayList<String>();
        ParamCorner = new ArrayList<String>();
        pLine = new ArrayList<Ellipse2D>();
        polygon = new ArrayList<Polygon>();
        currentPoly = null;
        sign = new ArrayList<String>();
        coordSign = new ArrayList<Rectangle2D>();
        tangents = new ArrayList<Line2D>();
        lines = new ArrayList<Line2D>();
        pTang = new ArrayList<Ellipse2D>();
        corners = new ArrayList<Double>();
        based = new ArrayList<Line2D>();
        cornLine = new ArrayList<Line2D>();
        pCorner = new ArrayList<ArrayList<Ellipse2D>>();
        Division = new ArrayList<Double>();
        pDivision = new ArrayList<Ellipse2D>();
        hashDiv = new ArrayList<Integer>();
        Axes.measure = 20;
        TCircle = new Ellipse2D.Double();
        inter = new Line2D.Double();
        TPoint1 = new Ellipse2D.Double();
        TPoint2 = new Ellipse2D.Double();
        TInterval = new ArrayList<Line2D>();
        TP = new ArrayList<Point2D>();
        x0 = 0;
        y0 = 0;

        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseMotionHandler());
    }
    
    public double[] getPx(){
        return pXX;
    }
    
    public double[] getPy(){
        return pYY;
    }

    /**
     * Поиск прямой,которая содержит точку координаты указателя мыши
     *
     * Пробегаются все объекты списка pLinе-(точка на прямой) Возвращается
     * объект списка lines
     */
    public Line2D findLine(Point2D p) {
        for (Ellipse2D l : pLine) {
            if (l.contains(p)) {
                return lines.get(pLine.indexOf(l));
            }
        }
        return null;
    }

    /**
     * Поиск отрезка по точке указателя мыши
     *
     * Поиск ведется по спискам точек(окружность) на концах отрезка pInter1
     * pInter2 Если объект списка содержит точку,то возвращается объект списка
     * intervals под индексом объекта из списков pInter1 или pInter2
     *
     */
    public Line2D findInter(Point2D p) {
        for (Ellipse2D l : pInter1) {
            if (l.contains(p)) {
                return intervals.get(pInter1.indexOf(l));
            }
        }
        for (Ellipse2D l : pInter2) {
            if (l.contains(p)) {
                return intervals.get(pInter2.indexOf(l));
            }
        }
        return null;
    }

    /**
     * Поиск точки по координатам указателя мыши
     *
     * Поиск по объектам списка points Если объект содержит точку с заданными
     * координатами,то и возвращается его
     */
    public Ellipse2D findPoint(Point2D p) {
        for (Ellipse2D c : points) {
            if (c.contains(p)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Поиск сектора,принадлежащего окружности
     *
     * Поиск проиходит по всем объектам списка sectors Если объект содержит
     * точку с заданными координатами,то и возвращается его
     */
    public Arc2D findArc(Point2D p) {
        for (Arc2D c : sectors) {
            if (c != null && c.contains(p)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Поиск точки деления отрезка в заданном соотношении
     *
     * Поиск идет по всем объектам списка pDivision Если объект содержит точку с
     * заданными координатами,то и возвращается его
     */
    public Ellipse2D findDivision(Point2D p) {
        for (Ellipse2D c : pDivision) {
            if (c.contains(p)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Поиск точки среди всех вершин и вспомогательных точек
     *
     * Этот метод используется для поиска точки,которой пользователь хочет дать
     * имя Списки ,содержащие объекты типа Ellipse2D: pEll-список центров
     * окружностей pPoly - список списков вершин многоугольников pInter1-список
     * начальных точек отрезков pInter2- список конечных точек отрезков
     * points-список точек,как самостоятельных объектов pTang- список точек
     * касания окружности касательных pLine - список базовых точек,определенных
     * на прямой pCorner - список списков базовых точек угла pDivision - список
     * точек деления отрезка в заданном соотношении
     */
    public Ellipse2D findFromAllPoints(Point2D p) {
        for (Ellipse2D a : pEll) {
            if (a.contains(p)) {
                return a;
            }
        }
        for (int i = 0; i < pPoly.size(); i++) {
            for (Ellipse2D b : pPoly.get(i)) {
                if (b.contains(p)) {
                    return b;
                }
            }
        }
        for (Ellipse2D c : pInter1) {
            if (c.contains(p)) {
                return c;
            }
        }
        for (Ellipse2D d : pInter2) {
            if (d.contains(p)) {
                return d;
            }
        }
        for (Ellipse2D e : points) {
            if (e.contains(p)) {
                return e;
            }
        }
        for (Ellipse2D f : pTang) {
            if (f.contains(p)) {
                return f;
            }
        }
        for (Ellipse2D g : pLine) {
            if (g.contains(p)) {
                return g;
            }
        }
        for (int i = 0; i < pCorner.size(); i++) {
            for (Ellipse2D h : pCorner.get(i)) {
                if (h.contains(p)) {
                    return h;
                }
            }
        }
        for (Ellipse2D k : pDivision) {
            if (k.contains(p)) {
                return k;
            }
        }

        return null;
    }

    /**
     * Поиск многоугольника
     *
     * Поиск проходит по объектам списка polygon,который содержит сами
     * многоугольники(объект типа Polygon), а так же по вершинам
     * многоугольника(список pPoly)
     */
    public Polygon findPolygon(Point2D p) {
        for (Polygon c : polygon) {
            if (c.contains(p)) {
                return c;
            }
            for (Ellipse2D e : pPoly.get(polygon.indexOf(c))) {
                if (e.contains(p)) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * Поиск окружностей/эллипсов по списку circles
     *
     * Если точка принадлежит некоторой области вокруг центра окружности /
     * эллипса, то возвращаем объект Ellipse2D саму окружность
     */
    public Ellipse2D findEll(Point2D p) {

        for (Ellipse2D c : circles) {

            Ellipse2D a = new Ellipse2D.Double(c.getCenterX() - 6,
                    c.getCenterY() - 6, 12, 12);
            if (a.contains(p)) {
                return c;
            }

        }
        return null;
    }

    /**
     * Поиск касательной по списку pTang,содержащему точки касания
     *
     * Если объект содержит точку p, то возвращается объект списка tangents типа
     * Line2D
     */
    public Line2D findTang(Point2D p) {
        for (Ellipse2D c : pTang) {
            if (c != null && c.contains(p)) {
                return tangents.get(pTang.indexOf(c));
            }
        }
        return null;
    }

    /**
     * Поиск угла по обной из базовых точек из списка pCorner
     *
     * Если точка принадлежит одному из объектов типа Ellipse2D,то возвращется
     * объект типа Line2D из списка based (базовый отрезок угла)
     */
    public Line2D findCorner(Point2D p) {
        for (Double c : corners) {
            for (Ellipse2D e : pCorner.get(corners.indexOf(c))) {
                if (e.contains(p)) {
                    return based.get(corners.indexOf(c));
                }
            }
        }
        return null;
    }

    /**
     * Поиск надписи на плоскости Поиск проходит по списку coordSign, который
     * содержит прямоугольник, охватывающий надпись
     */
    public Rectangle2D findSign(Point2D p) {
        for (Rectangle2D b : coordSign) {
            if (b.contains(p)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Перестановка
     *
     * Метод, который осуществляет перестановку двух объектов внутри списка На
     * место i-ого элемента списка ставится i-1,аналогично на место i-1 ставится
     * i-ый элемент
     */
    public static void change(ArrayList ob, int i) {
        Object c = ob.get(i);
        ob.remove(i);
        ob.add(i, ob.get(i - 1));
        ob.remove(i - 1);
        ob.add(i - 1, c);
    }

    /**
     * Добавление надписи
     *
     * В список sign добавляется строка типа String, в список coordSign
     * добавляется прямоугольник (Rectangle2D),охватывающий надпись
     */
    public static void addSignature() {
        sign.add(S);
        coordSign.add(new Rectangle2D.Double(x, y, S.length() * 20, 20));
        ToolBar.Signature = false;
    }

    /**
     * Добавление точки деления отрезка в заданном отношении
     *
     * Если входной параметр l не нулевой (в случае интерактивного
     * построения),то передаются некоторые параметры этого отрезка(координаты
     * начальной точкм,конченой точки и хэш код отрезка) Если же построение
     * ведется через открытие проекта(чтение текстового файла),то все переменные
     * уже имеют необходимое значение и нужно только добавить в список hashDiv
     * хэш код отрезка для прикрепеления за этой прямой точки деления.
     *
     * Далее в список Division добавляем коэффициент деления, вычисляем
     * координаты точки деления и добавляем в список pDivision объект типа
     * Ellipse2D на основе полученных данных
     *
     * Список ParamDiv добавляется объектом типа String,где хранится запись о
     * точке деления
     *
     */
    public static void addDivision(Line2D l) {
        if (l != null) {
            x = l.getX1();
            y = l.getY1();
            x1 = l.getX2();
            y1 = l.getY2();
            hashDiv.add(l.hashCode());
        } else {
            hashDiv.add(hash);
        }
        double div = (double) FirstDiv / (SecondDiv);
        Division.add(div);

        x0 = (double) (x + div * x1) / (1 + div);
        y0 = (double) (y + div * y1) / (1 + div);
        currentDiv = new Ellipse2D.Double(x0 - pointRad / 2, y0 - pointRad / 2,
                pointRad, pointRad);
        pDivision.add(currentDiv);

        S = "Деление (" + FirstDiv + " : " + SecondDiv + " ) ";
        ParamDiv.add(S);
        ToolBar.Division = false;
    }

    /**
     * Добавление отрезка
     *
     * По данным,полученным от пользователя(из формы или с самой плоскости),
     * получаем объекты,которые добавляем в соответствующие списки:
     *
     * pInter1-список объектов типа Ellipse2D,точки начала отрезка
     * pInter2-список объектов типа Ellipse2D,точки конца отрезка
     * intervals-список объектов типа Line2D,отрезок ParamIter- список
     * параметров отрезка для записи в файл
     */
    public static void addInter() {

        currentCP = new Ellipse2D.Double(x - pointRad / 2, y - pointRad / 2,
                pointRad, pointRad);
        pInter1.add(currentCP);
        currentCP = new Ellipse2D.Double(x1 - pointRad / 2, y1 - pointRad / 2,
                pointRad, pointRad);
        pInter2.add(currentCP);
        currentInter = new Line2D.Double(x, y, x1, y1);
        intervals.add(currentInter);
        hash = currentInter.hashCode();
        ParamInter.add("Отрезок= начало(" + Xvis + ", " + Yvis + "),конец ("
                + Xvis1 + ", " + Yvis1 + " ) ");

        ToolBar.AddInter = false;
    }

    /**
     * Добавление прямой
     *
     * Для построения используется уравенние прямой вида y=kx+b
     *
     * Прямая строится по двум точкам,которые выходят за видимые границы области
     * построения
     *
     * объект типа Line2D добавляется в список lines,т.е. добавляется сама
     * прямая объект типа Ellipse2D добавляется в список pLine,т.е. добаляется
     * точка на прямой,чтобы в дальнейшем было удобнее инициализировать эту
     * прямую для манипуляций. объект типа String,который содержит информацию о
     * прямой(коээффициенты k и b ),добавляется в список ParamLine
     */
    public static void addLine() {
        double x2 = 100 * Axes.measure + Axes.l;
        double x1 = -100 * Axes.measure + Axes.l;
        double y1 = -(coeffK * -100 + coeffB) * Axes.measure + Axes.h;
        double y2 = -(coeffK * 100 + coeffB) * Axes.measure + Axes.h;
        double xp = Axes.measure + Axes.l;
        double yp = -(coeffK + coeffB) * Axes.measure + Axes.h;
        currentLine = new Line2D.Double(x1, y1, x2, y2);
        lines.add(currentLine);
        currentCP = new Ellipse2D.Double(xp - pointRad / 2, yp - pointRad / 2,
                pointRad, pointRad);
        pLine.add(currentCP);
        ParamLine.add("Прямая= (" + coeffK + " ," + coeffB + " )");
        ToolBar.AddLine = false;
    }

    /**
     * Добавление сектора
     *
     * Этот элемент является зависимым,т.е. закреплен за другим
     * элементом,окружностью или эллипсом. Если построение ведется не из файла,а
     * непосредственно пользователем,то передаются некторые параметры
     * окружности/эллипса,такие как координаты верхнего левого угла
     * прямоугольника, описывающего эллипс/окружность. В список sectors
     * добавляется объект типа Arc2D(сектор)
     *
     * Списки cArc, rArc содержат данные об окружности,за которой закреплен
     * объект
     *
     * Для установления правила нахождения объекта проводим сортировку списка,на
     * тот случай,если 2 объекта наложены один на другой,или один содержит
     * другой,в этом случае берется тот,что меньше
     *
     *
     */
    public static void AddSector(Ellipse2D e) {
        if (e != null) {
            x1 = e.getMinX();
            y1 = e.getMinY();
            rArcX = e.getWidth();
            rArcY = e.getHeight();
        }
        currentArc = new Arc2D.Double(x1, y1, rArcX, rArcY, startArc, Arc,
                Arc2D.PIE);

        sectors.add(currentArc);

        cArc.add(new Point2D.Double(x1, y1));
        rArc.add(new Point2D.Double(rArcX, rArcY));
        S = "Сектор=( " + startArc + " , " + Arc + ")";
        ParamSector.add(S);

        for (int j = 1; j < sectors.size(); j++) {
            for (int i = 1; i < sectors.size(); i++) {
                boolean a = (sectors.get(i - 1).getAngleStart() <= sectors.get(
                        i).getAngleStart());
                boolean b = (sectors.get(i - 1).getAngleExtent() >= sectors
                        .get(i).getAngleExtent());
                if (a && b) {
                    change(sectors, i);
                    change(ParamSector, i);
                    change(rArc, i);
                    change(cArc, i);

                }

            }
        }
        ToolBar.Sector = false;
    }

    /**
     * Добавление касательной к окружности/эллипсу
     *
     * Добавление касательной проводится по тому же принципу что и сектора.
     *
     * В качестве параметров,необходимых для построения,берутся центр
     * эллипса,его полуоси. Точка касания задается ее полярной координатой на
     * окружности.
     */
    public static void addTangent(Ellipse2D e) {
        if (e != null) {
            x = e.getCenterX();
            y = e.getCenterY();
            radiusX = e.getWidth() * 0.5;
            radiusY = e.getHeight() * 0.5;

        }
        x0 = radiusX * Math.cos(a) + x;
        y0 = -radiusY * Math.sin(a) + y;
        double tg = -(double) ((x0 - x) * Math.pow(radiusY, 2) / ((y0 - y) * Math
                .pow(radiusX, 2)));

        double dx1 = Math.cos(Math.atan(tg)) * l / 2;
        double dy1 = Math.sin(Math.atan(tg)) * l / 2;

        currentTang = new Line2D.Double(x0 + dx1, y0 + dy1, x0 - dx1, y0 - dy1);
        tangents.add(currentTang);
        cTan.add(new Point2D.Double(x, y));
        rTan.add(new Point2D.Double(radiusX, radiusY));

        currentCP = new Ellipse2D.Double(x0 - pointRad / 2, y0 - pointRad / 2,
                pointRad, pointRad);
        pTang.add(currentCP);

        S = "Касательная (" + Avis + " , " + Lvis + " )";
        ParamTangent.add(S);
        ToolBar.Tangent = false;
    }

    /**
     * Добавление Угла
     *
     * Для построения угла необходимо иметь 2 отрезка,базовый (объект списка
     * based), от которого откладывается заданный угол,второй отрезок(объект
     * списка cornLine)заканчивает построение . Для хранения точек в список
     * pCorner добавляем список,содержащий объекты Ellipse2D (3 точки угла)
     */
    public static void addCorner() {
        corners.add(alpha);
        based.add(new Line2D.Double(TP.get(0), TP.get(1)));
        cornLine.add(new Line2D.Double(TP.get(1), TP.get(2)));
        Ellipse2D el;
        pC = new ArrayList<Ellipse2D>();
        for (int i = 0; i < 3; i++) {
            el = new Ellipse2D.Double(TP.get(i).getX() - pointRad / 2, TP
                    .get(i).getY() - pointRad / 2, pointRad, pointRad);
            pC.add(el);
        }
        pCorner.add(pC);
        Xvis = (double) (TP.get(0).getX() - Axes.l) / Axes.measure;
        Yvis = (double) (-TP.get(0).getY() + Axes.h) / Axes.measure;
        Xvis1 = (double) (TP.get(1).getX() - Axes.l) / Axes.measure;
        Yvis1 = (double) (-TP.get(1).getY() + Axes.h) / Axes.measure;
        Xvis2 = (double) (TP.get(2).getX() - Axes.l) / Axes.measure;
        Yvis2 = (double) (-TP.get(2).getY() + Axes.h) / Axes.measure;
        S = "Угол=  значение " + ALPHA + " точки( (" + Xvis + " , " + Yvis
                + ") , (" + Xvis1 + " , " + Yvis1 + ") , (" + Xvis2 + " , "
                + Yvis2 + ") )";
        ParamCorner.add(S);
        ToolBar.AddCorner = false;
        TP.removeAll(TP);

    }

    /**
     * Добавление эллипса/окружности
     *
     * Для добавления объекта Ellipse2D в список circles необходимо знать
     * координаты центра,а также радиус в случае окружности, или 2х полуосей в
     * случае эллипса
     *
     * Если идет построение эллипса,то так же включается еще один
     * параметр-поворот(список Rotate). Этот параметр отвечает за поворот
     * эллипса относительно оси ОХ против часовой стрелки
     *
     * Сортировка списка перемещает большую окружность в конец списка,чтобы при
     * поиске по координате точке указателя мыши, первой был найден меньший
     * объект
     */
    public static void addEllipse() {

        currentEll = new Ellipse2D.Double(x - radiusX, y - radiusY,
                radiusX * 2, radiusY * 2);
        circles.add(currentEll);

        currentCP = new Ellipse2D.Double(x - pointRad / 2, y - pointRad / 2,
                pointRad, pointRad);
        pEll.add(currentCP);

        if (ToolBar.AddCirc) {
            ParamEllipse.add("Окружность= центр( " + Xvis + ", " + Yvis
                    + " ); радиус ( " + RadXvis + " )");
            Rotate.add(0.0);
        }
        if (ToolBar.AddEll) {
            Rotate.add(RotationAngle);
            ParamEllipse.add("Эллипс= центр( " + Xvis + ", " + Yvis
                    + "); полуосьХ  " + RadXvis + ", полуосьY " + RadYvis
                    + " )" + "; наклон(" + RotationAngle + " )");
        }

        for (int j = 1; j < circles.size(); j++) {
            for (int i = 1; i < circles.size(); i++) {
                boolean a = circles.get(i - 1).getWidth() <= circles.get(i - 1)
                        .getHeight();
                boolean b = circles.get(i).getWidth() < circles.get(i - 1)
                        .getWidth();
                boolean m = circles.get(i).getHeight() < circles.get(i - 1)
                        .getHeight();
                boolean r = circles.get(i).getWidth() == circles.get(i)
                        .getHeight();

                if (((b || m) && !r) || (r && a && b) || (r && !a && m)) {
                    change(circles, i);
                    change(pEll, i);
                    change(ParamEllipse, i);
                    change(Rotate, i);
                }

            }
        }
        ToolBar.AddCirc = false;
        ToolBar.AddEll = false;
    }

    /**
     * Добавление точки
     *
     * Для построения требуются только кординаты. объект типа Ellipse2D
     * добавляется в список points
     */
    public static void addPoint() {
        currentCP = new Ellipse2D.Double(x - pointRad / 2, y - pointRad / 2,
                pointRad, pointRad);
        points.add(currentCP);
        ParamPoint.add("Точка=( " + Xvis + ", " + Yvis + " )");
        ToolBar.AddPoint = false;
    }

    /**
     * Добавление имени точки
     *
     * В список PointsNames добавляется само обозначение точки В список nameXY
     * добаляется объект типа Point2D,который привязывает имя за данной точкой
     *
     * В случае,если имя точки уже задействовано,то появляется окно содержащее
     * информацию об ошибке
     */
    public static void SetName() {
        if (!PointsNames.contains(name)) {
            PointsNames.add(name);
            nameXY.add(new Point2D.Double(Xst, Yst));
            ToolBar.setName = false;
        } else {
            PopUp.Error();
        }

    }

    /**
     * Добавление многоугольника
     *
     * Для построения pPoly списка вершин(Ellipse2D) многоугольников необходимо
     * составить списокg2.fill(t); для каждого из них. Список polygon добавляется объектом
     * Polygon,который создается на основе массива координат х и массива
     * координат у точек.
     *
     * Так же производится сортировка по размеру
     */
    public static void addPolygon() {

        pP = new ArrayList<Ellipse2D>();
        for (int i = 0; i < NumOfPol; i++) {
            Ellipse2D p = new Ellipse2D.Double(PolyX[i] - pointRad / 2,
                    PolyY[i] - pointRad / 2, pointRad, pointRad);
            pP.add(p);

        }
        pPoly.add(pP);
        currentPoly = new Polygon(PolyX, PolyY, NumOfPol);// Rectangle2D.Double(x,y,width,height);
        polygon.add(currentPoly);

        S = "Многоугольник= вершины ";
        for (int i = 0; i < NumOfPol; i++) {
            S = S + "(" + pX[i] + ", " + pY[i] + ")";
        }
        ParamPoly.add(S);

        for (int i = 1; i < polygon.size(); i++) {
            if ((polygon.get(i).getBounds().getWidth() < polygon.get(i - 1)
                    .getBounds().getWidth())
                    || (polygon.get(i).getBounds().getHeight() < polygon
                    .get(i - 1).getBounds().getHeight())) {
                change(polygon, i);
                change(ParamPoly, i);
                change(pPoly, i);

            }
        }

        ToolBar.numPolPoint = false;
        pP = null;
    }

    /**
     * Удаление надписи
     *
     * Удаляется объекты списков sign , coordSign
     */
    public void removeBound(Rectangle2D r) {
        sign.remove(coordSign.indexOf(r));
        coordSign.remove(r);
        repaint();
    }

    /**
     * Удаление угла
     *
     * Удаляются объекты списков,содержащих информацию об элементе,индекс
     * которого передается методу
     */
    public void removeCorner(int k) {
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < nameXY.size(); i++) {
                if (pCorner.get(k).get(j).contains(nameXY.get(i))) {
                    nameXY.remove(i);
                    PointsNames.remove(i);
                }
            }
        }
        corners.remove(k);
        pCorner.remove(k);
        based.remove(k);
        cornLine.remove(k);
        ParamCorner.remove(k);
        repaint();
    }

    /**
     * Удаление точки деления отрезка Удаляются объекты списков,содержищих
     * информацию об элементе
     */
    public void removeDivision(Ellipse2D e) {

        for (int i = 0; i < nameXY.size(); i++) {
            if (e.contains(nameXY.get(i))) {
                nameXY.remove(i);
                PointsNames.remove(i);
            }
        }

        ParamDiv.remove(pDivision.indexOf(e));
        Division.remove(pDivision.indexOf(e));
        hashDiv.remove(pDivision.indexOf(e));
        pDivision.remove(e);
        repaint();
    }

    /**
     * Удаление отрезка Удаляются объекты списков,содержищих информацию об
     * элементе,а так же о привязанных к нему дополнительных элементах Из-за
     * того что при удалении объекта оставшиеся перенумеровываются в списке,то
     * создаем вспомогательный список,куда записываем те объекты которые
     * принадлежат данному
     */
    public void removeInter(Line2D l) {
        ArrayList<Ellipse2D> a = new ArrayList<Ellipse2D>();
        ParamInter.remove(intervals.indexOf(l));

        for (int i = 0; i < nameXY.size(); i++) {
            if (pInter1.get(intervals.indexOf(l)).contains(nameXY.get(i))
                    || pInter2.get(intervals.indexOf(l))
                    .contains(nameXY.get(i))) {
                nameXY.remove(i);
                PointsNames.remove(i);
            }
        }
        pInter1.remove(pInter1.get(intervals.indexOf(l)));
        pInter2.remove(pInter2.get(intervals.indexOf(l)));
        for (int i = 0; i < hashDiv.size(); i++) {
            if (hashDiv.get(i) == l.hashCode()) {
                a.add(pDivision.get(i));
            }
        }
        intervals.remove(l);
        for (int i = 0; i < a.size(); i++) {
            removeDivision(a.get(i));
        }
        repaint();
    }

    /**
     * Удаление многоугольника
     *
     * Удаление из списков,содержащих информацию об элементе, соответствующих
     * объектов
     */
    public void removePolygon(Polygon s) {
        if (s == null) {
            return;
        }
        for (int j = 0; j < pPoly.get(polygon.indexOf(s)).size(); j++) {
            for (int i = 0; i < nameXY.size(); i++) {
                if (pPoly.get(polygon.indexOf(s)).get(j)
                        .contains(nameXY.get(i))) {
                    nameXY.remove(i);
                    PointsNames.remove(i);
                }
            }
        }
        pPoly.remove(polygon.indexOf(s));
        polygon.remove(s);
        repaint();

    }

    /**
     * Удаление сектора вместе с эллипсом/окружностью,которому он принадлежит
     *
     * Так как одному основному объекту может принадлежать несколько
     * дополнительных, то в вспомогательные списки добавляем те элементы
     * списка,которые нужно удалить. Определяется необходимый элемент по
     * параметрам,записанных в списках rArc и cArc.
     *
     * После из списка удаляется коллекция,которая содержится в вспомогательном
     * списке
     */
    public static void removeArcWithEll(Ellipse2D p) {
        int Size = sectors.size();
        ArrayList<Arc2D> a = new ArrayList<Arc2D>();
        ArrayList<String> b = new ArrayList<String>();
        ArrayList<Point2D> c = new ArrayList<Point2D>();
        ArrayList<Point2D> d = new ArrayList<Point2D>();
        for (int i = 0; i < Size; i++) {
            if (rArc.get(i).getX() == p.getWidth()
                    && rArc.get(i).getY() == p.getHeight()
                    && cArc.get(i).getX() == p.getMinX()
                    && cArc.get(i).getY() == p.getMinY()) {

                a.add(sectors.get(i));
                b.add(ParamSector.get(i));

                c.add(rArc.get(i));
                d.add(cArc.get(i));
            }

        }

        sectors.removeAll(a);
        ParamSector.removeAll(b);
        rArc.removeAll(c);
        cArc.removeAll(d);
        a.removeAll(a);
        b.removeAll(b);
        c.removeAll(c);
        d.removeAll(d);
    }

    /**
     * Удаление Касательных вместе с окружногстью/эллипсом
     *
     * Принцип аналогичен принципу удаления секторов,описан в removeArcWithEll
     */
    public static void removeTanWithEll(Ellipse2D p) {
        int Size = tangents.size();
        ArrayList<Line2D> t = new ArrayList<Line2D>();
        ArrayList<String> b = new ArrayList<String>();
        ArrayList<Point2D> c = new ArrayList<Point2D>();
        ArrayList<Point2D> d = new ArrayList<Point2D>();
        ArrayList<Ellipse2D> e = new ArrayList<Ellipse2D>();
        for (int i = 0; i < Size; i++) {
            if (cTan.get(i).getX() == p.getCenterX()
                    && cTan.get(i).getY() == p.getCenterY()
                    && rTan.get(i).getX() == p.getWidth() * 0.5
                    && rTan.get(i).getY() == p.getHeight() * 0.5) {
                t.add(tangents.get(i));
                b.add(ParamTangent.get(i));
                c.add(rTan.get(i));
                d.add(cTan.get(i));
                e.add(pTang.get(i));
                for (int j = 0; j < nameXY.size(); j++) {
                    if (pTang.get(i).contains(nameXY.get(j))) {
                        nameXY.remove(j);
                        PointsNames.remove(j);
                    }
                }
            }
        }

        tangents.removeAll(t);
        ParamTangent.removeAll(b);
        rTan.removeAll(c);
        cTan.removeAll(d);
        pTang.removeAll(e);
        t.removeAll(t);
        b.removeAll(b);
        c.removeAll(c);
        d.removeAll(d);
    }

    /**
     * Удаление окружности/эллипса
     *
     * Удаляется сама окружность,центральная точка, а так же все дополнительные
     * объекты, прикрепленные за ней,а так же имена точек.
     */
    public void removelEllipse(Ellipse2D s, Ellipse2D p) {
        if (s == null) {
            return;
        }

        for (int i = 0; i < nameXY.size(); i++) {
            if (s.contains(nameXY.get(i))) {
                nameXY.remove(i);
                PointsNames.remove(i);
            }
        }

        removeArcWithEll(p);
        removeTanWithEll(p);
        Rotate.remove(circles.indexOf(p));
        pEll.remove(s);
        circles.remove(p);
        repaint();
    }

    /**
     * Удаление сектора,как отдельного элемента
     */
    public void removeSector(Arc2D arc) {
        if (arc == null) {
            return;
        }
        ParamSector.remove(sectors.indexOf(arc));
        rArc.remove(sectors.indexOf(arc));
        cArc.remove(sectors.indexOf(arc));
        sectors.remove(arc);
        repaint();
    }

    /**
     * Удаление касательной как отдельного элемента
     */
    public void removeTangent(Line2D tan) {
        if (tan == null) {
            return;
        }
        for (int i = 0; i < nameXY.size(); i++) {
            if (pTang.get(tangents.indexOf(tan)).contains(nameXY.get(i))) {
                nameXY.remove(i);
                PointsNames.remove(i);
            }
        }
        ParamTangent.remove(tangents.indexOf(tan));
        pTang.remove(tangents.indexOf(tan));
        rTan.remove(tangents.indexOf(tan));
        cTan.remove(tangents.indexOf(tan));
        tangents.remove(tan);
        repaint();
    }

    /**
     * Удаление точки и ее имени,если оно ей присвоено
     */
    public void removePoint(Ellipse2D e) {
        if (e == null) {
            return;
        }

        for (int i = 0; i < nameXY.size(); i++) {
            if (e.contains(nameXY.get(i))) {
                nameXY.remove(i);
                PointsNames.remove(i);
            }
        }
        points.remove(e);
        repaint();
    }

    /**
     * Удаление прямой
     *
     * Удаляется сама прямая,принадлежащая ей точка(опорная),и имя точки,если
     * оно было присвоено
     */
    public void removeLine(Line2D l) {
        if (l == null) {
            return;
        }
        ParamLine.remove(lines.indexOf(l));
        for (int i = 0; i < nameXY.size(); i++) {
            if (pLine.get(lines.indexOf(l)).contains(nameXY.get(i))) {
                nameXY.remove(i);
                PointsNames.remove(i);
            }
        }
        pLine.remove(lines.indexOf(l));
        lines.remove(l);
        repaint();
    }

    /**
     * Удаление всех элементов на плоскости,очищение экрана Очищаются все
     * списки,содержащую информацию об элементах
     */
    public void removeAllObjects() {
        if (ToolBar.ClearAll) {
            // удаление надписей
            sign.removeAll(sign);
            coordSign.removeAll(coordSign);
            // удаление углов
            corners.removeAll(corners);
            pCorner.removeAll(pCorner);
            based.removeAll(based);
            cornLine.removeAll(cornLine);
            ParamCorner.removeAll(ParamCorner);
            // удаление точек
            points.removeAll(points);
            ParamPoint.removeAll(ParamPoint);
            // удаление многоугольников
            polygon.removeAll(polygon);
            pPoly.removeAll(pPoly);
            ParamPoly.removeAll(ParamPoly);
            // удаление окружностей,эллипсов
            circles.removeAll(circles);
            ParamEllipse.removeAll(ParamEllipse);
            pEll.removeAll(pEll);
            Rotate.removeAll(Rotate);
            // удаление секторов
            sectors.removeAll(sectors);
            ParamSector.removeAll(ParamSector);
            rArc.removeAll(rArc);
            cArc.removeAll(cArc);
            // удаление касательных
            tangents.removeAll(tangents);
            ParamTangent.removeAll(ParamTangent);
            rTan.removeAll(rTan);
            cTan.removeAll(cTan);
            pTang.removeAll(pTang);
            // удаление прямых
            lines.removeAll(lines);
            pLine.removeAll(pLine);
            ParamLine.removeAll(ParamLine);
            // удаление отрезков
            intervals.removeAll(intervals);
            ParamInter.removeAll(ParamInter);
            pInter1.removeAll(pInter1);
            pInter2.removeAll(pInter2);
            // удаление точек деления отрезков в заданном отношении
            pDivision.removeAll(pDivision);
            ParamDiv.removeAll(ParamDiv);
            hashDiv.removeAll(hashDiv);
            Division.removeAll(Division);
            // удаление имен точек
            PointsNames.removeAll(PointsNames);
            nameXY.removeAll(nameXY);
            repaint();
            ToolBar.ClearAll = false;
        }
    }
    int in = 0;

    /**
     * Определение элемента на плоскости,котрый будет захвачен
     *
     * Так как при захвате элемента координаты указателя мыши могут принадлежать
     * разным объектам, то необходимо взять тот,который имеет меньшие
     * размеры,т.е. лежит выше остальных
     */
    public void GetLessShape() {
        getEll = false;
        getRect = false;
        getInterval = false;
        getArc = false;
        getTang = false;
        getLine = false;
        getPoint = false;

        if (currentEll != null) {
            getEll = true;
            getLine = false;
        }
        if (currentPoly != null) {
            getRect = true;
            getEll = false;
            getLine = false;
        }
        if (currentArc != null && currentEll == null) {
            getArc = true;
            getRect = false;
            getEll = false;
            getLine = false;
        }
        if (currentEll != null && currentPoly != null) {
            for (int i = 0; i < currentPoly.npoints; i++) {
                if (currentEll.contains(new Point(currentPoly.xpoints[i],
                        currentPoly.ypoints[i]))) {
                    in++;
                }
            }

            if (in == currentPoly.npoints) {
                getArc = false;
                getRect = true;
                getEll = false;
                in = 0;
            }
            if (currentPoly.contains(currentEll.getFrame())) {
                getEll = true;
                getRect = false;
                getArc = false;
            }
        }

        if (currentEll != null && currentArc != null) {
            getEll = true;
            getArc = false;
            getRect = false;
            getLine = false;
        }

        if (currentPoly != null && currentArc != null) {
            if (currentPoly.contains(currentArc.getBounds2D())) {
                getRect = false;
                getArc = true;
                getEll = false;
            }

            for (int i = 0; i < currentPoly.npoints; i++) {
                if (currentArc.contains(new Point(currentPoly.xpoints[i],
                        currentPoly.ypoints[i]))) {
                    in++;
                }
            }

            if (in == currentPoly.npoints) {
                getArc = false;
                getRect = true;
                getEll = false;
                in = 0;
            }
        }

        if (currentLine != null) {
            getLine = true;
            getEll = false;
            getRect = false;
            getArc = false;
        }
        if (currentInter != null) {
            getInterval = true;
            getEll = false;
            getRect = false;
            getArc = false;
            getLine = false;
        }
        if (currentTang != null) {
            getTang = true;
            getArc = false;
            getRect = false;
            getEll = false;
            getInterval = false;
            getLine = false;
        }
        if (currentCP != null) {
            getPoint = true;
            getEll = false;
            getRect = false;
            getArc = false;
            getLine = false;
            getInterval = false;
            getTang = false;
        }

    }
    public static int oldL, oldH;

    /**
     * Метод вывода на экран графических элементов
     *
     * Этот метод вызывается автоматически при : -добавлении или удалении
     * элементов -изменении размеров окна -масштабировании -передвижении
     * объектов Каждый список,содержащий графические элементы просматривается
     * пообъектно и каждый объект списка строится на панели
     *
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g2 = (Graphics2D) g;

        oldL = Axes.l;
        oldH = Axes.h;
        ToolBar.DEFAULT_HEIGHT = getHeight();
        ToolBar.DEFAULT_WIDTH = getWidth();
        Axes.axes(g);

        BasicStroke pen1 = new BasicStroke(3);
        Font font = new Font("Arial Bold", Font.TYPE1_FONT, 30);
        g2.setStroke(pen1);
        g2.setFont(font);



        for (String s : sign) {
            g2.setColor(new Color(121, 121, 121));
            g2.drawString(s, (int) coordSign.get(sign.indexOf(s)).getMinX(),
                    (int) coordSign.get(sign.indexOf(s)).getMaxY());
            g2.setColor(Color.BLACK);
        }

        pen1 = new BasicStroke(3);
        font = new Font("Arial Bold", Font.TYPE1_FONT, 15);
        g2.setStroke(pen1);
        g2.setFont(font);

        if (oldL != Axes.l || oldH != Axes.h) {
            resize();
        }
        if ((Axes.measure <= 100 && ToolBar.Zoom)
                || (Axes.measure >= 5 && ToolBar.Reduce) && !StopResize) {
            resize();
        }

        for (Polygon t : polygon) {
           //Fill in polygon 
            g2.draw(t);
            g2.setPaint( new GradientPaint( 5, 30,         // x1, y1
                                        Color.blue,    // initial Color
                                        35, 100,       // x2, y2
                                        new Color(255,255,255,128),// end Color
            true)); 
            g2.fill(t);
     
            FillElement p = new FillElement();;
            //p.btnCreate(g2, t, Color.RED);
            p.btnCreate().addActionListener(new ActionListener(){
                @Override
                 public void actionPerformed(ActionEvent e) {
                    //System.out.print("Hello"+poly);
                     p.paint(g2,t, Color.blue); 
                     //g2.fill(t);
                }
            });
            
            ///getContentPane().add(panel);
            //setPreferredSize(new Dimension(320, 100));
            //p.paint(g2, t, Color.RED);
            p.pack();
            p.setLocationRelativeTo(null);
            p.setVisible(true);
             
             //FillElement.class.paint(t,g2,Color.GREEN);
           ////g2.fill(t);
           // g2.setColor(Color.RED);
            for (Ellipse2D s : pPoly.get(polygon.indexOf(t))) {
                //fill in ellypse start
                g2.fill(s);
            }
            g2.setColor(Color.BLUE);
        }

        for (Ellipse2D c : circles) {
            float[] dist = {0.0f, 0.5f, 1.0f };
            Point2D center = new Point2D.Float(0.5f, 0.5f);
            Color[] colors = { Color.RED, Color.GREEN, Color.BLUE };
            RadialGradientPaint p =
            new RadialGradientPaint(center, 0.5f, dist, colors);
            g2.setPaint(p);
            g2.fill(c);
            if (Rotate.get(circles.indexOf(c)) != 0.0) {
                g2.translate(c.getCenterX(), c.getCenterY());
                g2.rotate(Rotate.get(circles.indexOf(c)));
                g2.translate(-c.getCenterX(), -c.getCenterY());
                g2.draw(c);
                g2.fill(c);
                for (Arc2D a : sectors) {
                    if (rArc.get(sectors.indexOf(a)).getX() == c.getWidth()
                            && rArc.get(sectors.indexOf(a)).getY() == c
                            .getHeight()
                            && cArc.get(sectors.indexOf(a)).getX() == c
                            .getMinX()
                            && cArc.get(sectors.indexOf(a)).getY() == c
                            .getMinY()) {
                        Color color = new Color(250, 250, 140, 50);
                        g2.setColor(color);
                        g2.fill(a);
                        g2.setColor(Color.GREEN);
                        g2.draw(a);// }
                    }
                }
                for (Line2D l : tangents) {
                    if (cTan.get(tangents.indexOf(l)).getX() == c.getCenterX()
                            && cTan.get(tangents.indexOf(l)).getY() == c
                            .getCenterY()
                            && rTan.get(tangents.indexOf(l)).getX() == c
                            .getWidth() * 0.5
                            && rTan.get(tangents.indexOf(l)).getY() == c
                            .getHeight() * 0.5) {
                   
                        g2.draw(l);
                       // Rectangle rec = (Rectangle) l.getBounds();
                        //g2.fill(rec);
                        g2.setColor(Color.blue);
                        g2.fill(pTang.get(tangents.indexOf(l)));
                        g2.setColor(Color.red);
                    }
                }

            } else {

                g2.draw(c);
                for (Arc2D a : sectors) {
                    if (rArc.get(sectors.indexOf(a)).getX() == c.getWidth()
                            && rArc.get(sectors.indexOf(a)).getY() == c
                            .getHeight()
                            && cArc.get(sectors.indexOf(a)).getX() == c
                            .getMinX()
                            && cArc.get(sectors.indexOf(a)).getY() == c
                            .getMinY()) {
                        Color color = new Color(250, 250, 140, 50);
                        g2.setColor(color);
                        g2.fill(a);
                        g2.setColor(Color.BLACK);
                        g2.draw(a);
                    }
                }
                for (Line2D l : tangents) {
                    if (cTan.get(tangents.indexOf(l)).getX() == c.getCenterX()
                            && cTan.get(tangents.indexOf(l)).getY() == c
                            .getCenterY()
                            && rTan.get(tangents.indexOf(l)).getX() == c
                            .getWidth() * 0.5
                            && rTan.get(tangents.indexOf(l)).getY() == c
                            .getHeight() * 0.5) {

                        g2.draw(l);
                        g2.setColor(Color.blue);
                        g2.fill(pTang.get(tangents.indexOf(l)));
                        g2.setColor(Color.black);
                    }
                }

            }
            g2.translate(c.getCenterX(), c.getCenterY());
            g2.rotate(-Rotate.get(circles.indexOf(c)));
            g2.translate(-c.getCenterX(), -c.getCenterY());
        }

        for (Line2D l : intervals) {
            g2.draw(l);
            g2.setColor(Color.BLUE);
            g2.fill(pInter1.get(intervals.indexOf(l)));
            g2.fill(pInter2.get(intervals.indexOf(l)));
            g2.setColor(Color.BLACK);

        }
        for (Ellipse2D e : pDivision) {
            g2.setColor(Color.BLUE);
            g2.fill(e);
            g2.setColor(Color.BLACK);
        }

        if (ToolBar.IntervalMouseAdd) {
            g2.setColor(Color.GRAY);
            g2.draw(inter);
            g2.fill(TPoint1);
            g2.fill(TPoint2);
            g2.setColor(Color.black);
        }

        if (ToolBar.LineMouseAdd) {
            g2.setColor(Color.GRAY);
            g2.draw(inter);
            g2.fill(TPoint1);
            g2.setColor(Color.black);
        }

        if (ToolBar.polygonMouseAdd) {
            g2.setColor(Color.GRAY);
            g2.draw(inter);
            for (Line2D l : TInterval) {
                g2.draw(l);
            }
            g2.setColor(Color.black);
        }
        for (Line2D l : based) {
            g2.draw(l);
            for (Ellipse2D e : pCorner.get(based.indexOf(l))) {
                g2.setColor(Color.BLUE);
                g2.setColor(Color.BLACK);
            }

        }
        for (Line2D l : cornLine) {
            g2.draw(l);
        }

        if (ToolBar.CornerMouseAdd) {
            g2.setColor(Color.GRAY);
            g2.draw(inter);
            for (Line2D l : TInterval) {
                g2.draw(l);
            }

            g2.setColor(Color.black);
        }
        for (Ellipse2D e : points) {
            g2.fill(e);
        }

        for (Ellipse2D p : pEll) {
            g2.setColor(Color.BLUE);
            g2.fill(p);
            g2.setColor(Color.BLACK);
        }

        if (ToolBar.CircleMouseAdd || ToolBar.EllipseMouseAdd
                && TCircle1 != null && TCircle != null) {
            g2.setColor(Color.GRAY);
            g2.fill(TCircle1);
            g2.draw(TCircle);
            g2.setColor(Color.black);
        }

        for (Line2D l : lines) {
            g2.draw(l);
            g2.setColor(Color.BLUE);
            g2.fill(pLine.get(lines.indexOf(l)));
            g2.setColor(Color.BLACK);
        }
        for (int i = 0; i < nameXY.size(); i++) {

            g2.setColor(Color.red);
            g2.drawString(PointsNames.get(i), (int) nameXY.get(i).getX(),
                    (int) nameXY.get(i).getY());
            g2.setColor(Color.BLACK);
        }
    }

    /**
     * Изменение размеров и координат элементов
     *
     * Проводится перерасчет координат и длин для новой единицы измерения
     */
    public void resize() {
        // Масштабирование углов
        for (int i = 0; i < corners.size(); i++) {
            Line2D l = based.get(i);
            double x1 = (Axes.measure * (l.getX1() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double y1 = -(Axes.measure * (-l.getY1() + oldH) / (Axes.measure - factor))
                    + Axes.h;
            pCorner.get(i)
                    .get(0)
                    .setFrame(x1 - pointRad / 2, y1 - pointRad / 2, pointRad,
                    pointRad);
            double x2 = (Axes.measure * (l.getX2() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double y2 = -(Axes.measure * (-l.getY2() + oldH) / (Axes.measure - factor))
                    + Axes.h;
            pCorner.get(i)
                    .get(1)
                    .setFrame(x2 - pointRad / 2, y2 - pointRad / 2, pointRad,
                    pointRad);
            l = new Line2D.Double(x1, y1, x2, y2);
            based.set(i, l);
            l = cornLine.get(i);
            double z1 = (Axes.measure * (l.getX2() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double z2 = -(Axes.measure * (-l.getY2() + oldH) / (Axes.measure - factor))
                    + Axes.h;
            pCorner.get(i)
                    .get(2)
                    .setFrame(z1 - pointRad / 2, z2 - pointRad / 2, pointRad,
                    pointRad);
            l = new Line2D.Double(x2, y2, z1, z2);
            cornLine.set(i, l);

        }

        // МАсштабирование отрезков
        for (int i = 0; i < intervals.size(); i++) {
            Line2D l = intervals.get(i);
            double x1 = (Axes.measure * (l.getX1() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double y1 = -(Axes.measure * (-l.getY1() + oldH) / (Axes.measure - factor))
                    + Axes.h;
            double x2 = (Axes.measure * (l.getX2() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double y2 = -(Axes.measure * (-l.getY2() + oldH) / (Axes.measure - factor))
                    + Axes.h;

            l = new Line2D.Double(x1, y1, x2, y2);
            intervals.set(i, l);

            Ellipse2D p1 = pInter1.get(i);
            p1 = new Ellipse2D.Double(x1 - pointRad / 2, y1 - pointRad / 2,
                    pointRad, pointRad);
            pInter1.set(i, p1);

            Ellipse2D p2 = pInter2.get(i);
            p2 = new Ellipse2D.Double(x2 - pointRad / 2, y2 - pointRad / 2,
                    pointRad, pointRad);
            pInter2.set(i, p2);
        }
        // амсштабирование точек деления отрезков
        for (int i = 0; i < pDivision.size(); i++) {
            Ellipse2D c = pDivision.get(i);

            double xp = (double) (Axes.measure * (c.getMinX() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double yp = (double) -(Axes.measure * (-c.getMinY() + oldH) / (Axes.measure - factor))
                    + Axes.h;

            c = new Ellipse2D.Double(xp, yp, pointRad, pointRad);

            pDivision.set(i, c);
        }

        // МАсштабирование прямых
        for (int i = 0; i < lines.size(); i++) {
            double x2 = 100 * Axes.measure + Axes.l;
            double x1 = -100 * Axes.measure + Axes.l;
            double y1 = -(coeffK * -100 + coeffB) * Axes.measure + Axes.h;
            double y2 = -(coeffK * 100 + coeffB) * Axes.measure + Axes.h;
            double xp = Axes.measure + Axes.l;
            double yp = -(coeffK + coeffB) * Axes.measure + Axes.h;
            currentLine = new Line2D.Double(x1, y1, x2, y2);
            lines.set(i, currentLine);
            currentCP = new Ellipse2D.Double(xp - pointRad / 2, yp - pointRad
                    / 2, pointRad, pointRad);
            pLine.set(i, currentCP);
        }

        // МАсштабирование многоугольников
        for (int i = 0; i < polygon.size(); i++) {
            Polygon p = polygon.get(i);

            int[] x = new int[p.npoints];
            int[] y = new int[p.npoints];
            for (int j = 0; j < p.npoints; j++) {
                x[j] = (int) (Axes.measure * (p.xpoints[j] - oldL) / (Axes.measure - factor))
                        + Axes.l;
                y[j] = (int) -(Axes.measure * (-p.ypoints[j] + oldH) / (Axes.measure - factor))
                        + Axes.h;
                currentCP = pPoly.get(i).get(j);
                currentCP.setFrame(x[j] - pointRad / 2, y[j] - pointRad / 2,
                        pointRad, pointRad);
            }

            p = new Polygon(x, y, p.npoints);
            polygon.set(i, p);
        }

        // МАсштабирование окружностей,эллипсов
        for (int i = 0; i < circles.size(); i++) {
            Ellipse2D c = circles.get(i);

            double rx = c.getWidth() * Axes.measure / (Axes.measure - factor);
            double ry = c.getHeight() * Axes.measure / (Axes.measure - factor);
            double xp = (Axes.measure * (c.getMinX() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double yp = -(Axes.measure * (-c.getMinY() + oldH) / (Axes.measure - factor))
                    + Axes.h;

            c = new Ellipse2D.Double(xp, yp, rx, ry);

            circles.set(i, c);
        }
        // МАсштабирование центров окружностей,эллипсов
        for (int i = 0; i < pEll.size(); i++) {
            Ellipse2D c = pEll.get(i);

            double xp = (Axes.measure * (c.getMinX() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double yp = -(Axes.measure * (-c.getMinY() + oldH) / (Axes.measure - factor))
                    + Axes.h;

            c = new Ellipse2D.Double(xp, yp, pointRad, pointRad);

            pEll.set(i, c);
        }

        // МАсштабирование точек
        for (int i = 0; i < points.size(); i++) {
            Ellipse2D c = points.get(i);

            double xp = (Axes.measure * (c.getMinX() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double yp = -(Axes.measure * (-c.getMinY() + oldH) / (Axes.measure - factor))
                    + Axes.h;

            c = new Ellipse2D.Double(xp, yp, pointRad, pointRad);

            points.set(i, c);
        }

        // МАсштабирование имен точек
        for (int i = 0; i < nameXY.size(); i++) {
            Point2D p = nameXY.get(i);
            double xp = (Axes.measure * (p.getX() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double yp = -(Axes.measure * (-p.getY() + oldH) / (Axes.measure - factor))
                    + Axes.h;
            nameXY.set(i, new Point2D.Double(xp, yp));
        }

        // МАсштабирование секторов
        for (int i = 0; i < sectors.size(); i++) {
            Arc2D c = sectors.get(i);

            double x = (Axes.measure * (c.getMinX() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double y = -(Axes.measure * (-c.getMinY() + oldH) / (Axes.measure - factor))
                    + Axes.h;
            double rx = c.getWidth() * Axes.measure / (Axes.measure - factor);
            double ry = c.getHeight() * Axes.measure / (Axes.measure - factor);
            c = new Arc2D.Double(x, y, rx, ry, c.getAngleStart(),
                    c.getAngleExtent(), c.getArcType());

            sectors.set(i, c);

            Point2D p = cArc.get(i);
            Point2D r = rArc.get(i);
            double xp = (Axes.measure * (p.getX() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double yp = -(Axes.measure * (-p.getY() + oldH) / (Axes.measure - factor))
                    + Axes.h;
            double r1 = r.getX() * Axes.measure / (Axes.measure - factor);
            double r2 = r.getY() * Axes.measure / (Axes.measure - factor);
            cArc.set(i, new Point2D.Double(xp, yp));
            rArc.set(i, new Point2D.Double(r1, r2));
        }

        // МАсштабирование касательных
        for (int i = 0; i < tangents.size(); i++) {
            Line2D c = tangents.get(i);

            double x1 = (Axes.measure * (c.getX1() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double y1 = -(Axes.measure * (-c.getY1() + oldH) / (Axes.measure - factor))
                    + Axes.h;
            double x2 = (Axes.measure * (c.getX2() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double y2 = -(Axes.measure * (-c.getY2() + oldH) / (Axes.measure - factor))
                    + Axes.h;
            c = new Line2D.Double(x1, y1, x2, y2);
            tangents.set(i, c);
            Ellipse2D e = pTang.get(i);
            x1 = (Axes.measure * (e.getMinX() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            y1 = -(Axes.measure * (-e.getMinY() + oldH) / (Axes.measure - factor))
                    + Axes.h;
            e = new Ellipse2D.Double(x1, y1, pointRad, pointRad);
            pTang.set(i, e);

            Point2D p = cTan.get(i);
            Point2D r = rTan.get(i);
            double xp = (Axes.measure * (p.getX() - oldL) / (Axes.measure - factor))
                    + Axes.l;
            double yp = -(Axes.measure * (-p.getY() + oldH) / (Axes.measure - factor))
                    + Axes.h;
            double r1 = r.getX() * Axes.measure / (Axes.measure - factor);
            double r2 = r.getY() * Axes.measure / (Axes.measure - factor);
            cTan.set(i, new Point2D.Double(xp, yp));
            rTan.set(i, new Point2D.Double(r1, r2));
        }

        factor = 0;

        if (Axes.measure == 5 || Axes.measure == 100) {
            StopResize = true;
        } else {
            StopResize = false;
        }
        ToolBar.Reduce = false;
        ToolBar.Zoom = false;
        repaint();
    }
    int dxc, dyc;

    /**
     * Класс MouseHandler
     *
     * Содержит методы,обрабатывающие события мыши,такие как:
     *
     * -нажатие кнопки мыши public void mousePressed(MouseEvent event) -щелчок
     * кнопки мыши public void mouseClicked(MouseEvent event) -освобождение
     * кнопки мыши public void mouseReleased(MouseEvent event)
     *
     * Так же содержит методы, которые строят промежуточные элементы,которые
     * видит пользователем при построении различных фигур с помощью мыши: public
     * void Corner_mouse() public void Polygon_mouse() public void
     * Interval_mouse() public void Point_mouse() public void Line_mouse()
     * public void Circle_mouse()
     */
    public class MouseHandler extends MouseAdapter {

        /**
         * Событие мыши- нажата кнопка
         *
         * При этом событии находится элемент,который содержит точку,с
         * координатами указателя мыши, а так же обрабатывается искусственно
         * созданное нажатие мыши с помощью Robot. Если щелчок сгенерирован
         * машиной (ParamPanel.Click=true), то ведется построение
         * элемента,который необходимо добавить При нажатии кнопки на каком-либо
         * элементе,т.е. если он не пуст,считываются координаты указателя мыши и
         * записываются в переменные Xst Yst.
         */
        public void mousePressed(MouseEvent event) {

            if (ToolBar.ClearAll) {
                removeAllObjects();
            }
            currentPoint = findFromAllPoints(event.getPoint());
            if (currentPoint != null) {
                Xst = currentPoint.getCenterX();
                Yst = currentPoint.getCenterY();
            }
            currentArc = findArc(event.getPoint());
            currentTang = findTang(event.getPoint());

            currentLine = findLine(event.getPoint());
            currentCP = findPoint(event.getPoint());
            if (currentCP != null && ToolBar.moveObject) {
                Xst = event.getX();
                Yst = event.getY();
            }

            currentEll = findEll(event.getPoint());
            if (currentEll != null && (ToolBar.moveObject || ToolBar.Rotate)) {
                Xst = event.getX();
                Yst = event.getY();
            }

            currentCorner = findCorner(event.getPoint());
            if (currentCorner != null && ToolBar.moveObject) {
                Xst = event.getX();
                Yst = event.getY();
            }

            currentPoly = findPolygon(event.getPoint());
            if (currentPoly != null && (ToolBar.moveObject)) {
                Xst = event.getX();
                Yst = event.getY();
            }
            currentInter = findInter(event.getPoint());
            if (currentInter != null && ToolBar.moveObject) {
                Xst = event.getX();
                Yst = event.getY();
            }

            currentDiv = findDivision(event.getPoint());

            if (ToolBar.CircleMouseAdd || ToolBar.EllipseMouseAdd
                    || ToolBar.IntervalMouseAdd || ToolBar.polygonMouseAdd
                    || ToolBar.pointMouseAdd || ToolBar.LineMouseAdd
                    || ToolBar.CornerMouseAdd) {
                Xst = event.getX();
                Yst = event.getY();
            }

            currentBound = findSign(event.getPoint());
            if (currentBound != null && ToolBar.moveObject) {
                Xst = event.getX();
                Yst = event.getY();
            }

            if (ParamPanel.Click) {
                if ((ToolBar.AddCirc || ToolBar.AddEll)) {
                    addEllipse();
                }

                if ((ToolBar.numPolPoint)) {
                    addPolygon();
                }

                if (ToolBar.AddInter) {
                    addInter();
                }

                if (ToolBar.AddPoint) {
                    addPoint();
                }

                if (ToolBar.setName) {
                    SetName();
                }

                if (ToolBar.Sector) {
                    AddSector(e);
                }

                if (ToolBar.Division) {
                    addDivision(L);
                }

                if (ToolBar.Tangent) {
                    addTangent(e);
                }

                if (ToolBar.AddLine) {
                    addLine();
                }

                if (ToolBar.Signature) {
                    addSignature();
                }
                ParamPanel.Click = false;

            }
            GetLessShape();
            // добавление окружности с помощью мыши
            if (ToolBar.CircleMouseAdd || ToolBar.EllipseMouseAdd) {
                Circle_mouse();
            }
            // добавление отрезка с помощью мыши
            if (ToolBar.IntervalMouseAdd) {
                Interval_mouse();
            }
            // добавление многоугольника с помощью мыши
            if (ToolBar.polygonMouseAdd) {
                Polygon_mouse();
            }
            // добавление точки с помощью мыши
            if (ToolBar.pointMouseAdd) {
                Point_mouse();
            }
            // добавление прямой с помощью мыши
            if (ToolBar.LineMouseAdd) {
                Line_mouse();
            }
            // добавление угла с помощью мыши
            if (ToolBar.CornerMouseAdd) {
                Corner_mouse();
            }

        }
        double X, Y;
        public int n;

        /**
         * Событие мыши-щелчок
         *
         * При щелчке правой кнопкой мыши по элементу возникает всплывающее меню
         * PopUp, которое содержит возможные для данного элемента события.
         *
         * Так же обрабатывается искусственно сгенерированный щелчок при
         * масштабировании(увеличении уменьшении)
         *
         */
        public void mouseClicked(MouseEvent event) {
            n = 0;
            GetLessShape();
            // Увеличение
            if (ToolBar.Zoom) {
                factor = 5;
                scale(factor);
            }
            // Уменьшение
            if (ToolBar.Reduce) {
                factor = -5;
                scale(factor);
            }

            // Обработка события всплывающего меню удаление или назначение имени
            // точки

            if (currentPoint != null && event.getButton() == MouseEvent.BUTTON3
                    && event.getClickCount() == 1) {

                PopUp.popup.show(event.getComponent(), event.getX(),
                        event.getY());
                boolean DA = false;
                for (int j = 0; j < nameXY.size(); j++) {
                    if (currentPoint.contains(nameXY.get(j))) {

                        DA = true;
                        n = j;
                    }
                }

                if (DA) {
                    PopUp.menuItem2.setEnabled(true);
                } else {
                    PopUp.menuItem1.setEnabled(true);
                }

                if (nameXY.size() == 0) {
                    PopUp.menuItem1.setEnabled(true);
                }
                PopUp.menuItem2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        nameXY.remove(n);
                        PointsNames.remove(n);
                        repaint();
                    }
                });

                PopUp.menuItem1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ToolBar.setName = true;
                        ParamPanel p = new ParamPanel();
                        p.setVisible(true);

                    }
                });

            }

            // Обработка события всплывающего меню-удаление угла
            if ((event.getButton() == MouseEvent.BUTTON3)
                    && (event.getClickCount() == 1) && currentCorner != null) {
                PopUp.popup.show(event.getComponent(), event.getX(),
                        event.getY());
                PopUp.menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeCorner(based.indexOf(currentCorner));
                    }
                });

            }

            // Обработка события всплывающего меню-удаление надписи
            if ((event.getButton() == MouseEvent.BUTTON3)
                    && (event.getClickCount() == 1) && currentBound != null) {
                PopUp.popup.show(event.getComponent(), event.getX(),
                        event.getY());
                PopUp.menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeBound(currentBound);
                    }
                });

            }

            // Обработка события всплывающего меню-удаление точки
            if ((event.getButton() == MouseEvent.BUTTON3)
                    && (event.getClickCount() == 1) && getPoint) {
                PopUp.popup.show(event.getComponent(), event.getX(),
                        event.getY());
                PopUp.menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ParamPoint.remove(points.indexOf(currentCP));
                        removePoint(currentCP);
                    }
                });

            }

            // Обработка события дополнительного меню-добавление точки деления к
            // определенному отрезку
            if ((event.getButton() == MouseEvent.BUTTON1)
                    && (event.getClickCount() == 1) && currentInter != null
                    && (ToolBar.Division)) {
                L = currentInter;
                ParamPanel p = new ParamPanel();
                p.setVisible(true);
            }
            // Обработка события дополнительного меню - добавление касательной
            // или сектора к определенной окружности или эллипсу
            if ((event.getButton() == MouseEvent.BUTTON1)
                    && (event.getClickCount() == 1) && currentEll != null
                    && (ToolBar.Sector || ToolBar.Tangent)) {
                e = currentEll;
                ParamPanel p = new ParamPanel();
                p.setVisible(true);
            }
            // Обработка события всплывающего меню-удаление окружности,эллипса
            if ((event.getButton() == MouseEvent.BUTTON3)
                    && (event.getClickCount() == 1) && getEll) {
                PopUp.popup.show(event.getComponent(), event.getX(),
                        event.getY());
                PopUp.menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        ParamEllipse.remove(circles.indexOf(currentEll));
                        removelEllipse(pEll.get(circles.indexOf(currentEll)),
                                currentEll);
                    }
                });
            }
            // Обраюотка события всплывающего меню- удаление многоугольника

            if ((event.getButton() == MouseEvent.BUTTON3)
                    && event.getClickCount() == 1 && getRect) {
                PopUp.popup.show(event.getComponent(), event.getX(),
                        event.getY());
                PopUp.menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        ParamPoly.remove(polygon.indexOf(currentPoly));

                        removePolygon(currentPoly);
                    }
                });
            }

            // Обраюотка события всплывающего меню- удаление прямой
            if ((event.getButton() == MouseEvent.BUTTON3)
                    && event.getClickCount() == 1 && currentLine != null) {
                PopUp.popup.show(event.getComponent(), event.getX(),
                        event.getY());
                PopUp.menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeLine(currentLine);
                    }
                });
            }
            // Обраюотка события всплывающего меню- удаление сектора
            if ((event.getButton() == MouseEvent.BUTTON3)
                    && (event.getClickCount() == 1) && getArc) {
                PopUp.popup.show(event.getComponent(), event.getX(),
                        event.getY());
                PopUp.menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeSector(currentArc);

                    }
                });
            }

            // Обраюотка события всплывающего меню- удаление точки деления
            // отрезка
            if ((event.getButton() == MouseEvent.BUTTON3)
                    && (event.getClickCount() == 1) && currentDiv != null) {
                PopUp.popup.show(event.getComponent(), event.getX(),
                        event.getY());
                PopUp.menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeDivision(currentDiv);

                    }
                });
            }

            // Обраюотка события всплывающего меню- удаление касательной
            if ((event.getButton() == MouseEvent.BUTTON3)
                    && (event.getClickCount() == 1) && getTang) {
                PopUp.popup.show(event.getComponent(), event.getX(),
                        event.getY());
                PopUp.menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeTangent(currentTang);

                    }
                });
            }
            // Обработка события всплывающего меню- удаление отрезка
            if ((event.getButton() == MouseEvent.BUTTON3)
                    && event.getClickCount() == 1 && currentInter != null) {
                PopUp.popup.show(event.getComponent(), event.getX(),
                        event.getY());
                PopUp.menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeInter(currentInter);
                    }
                });
            }

            repaint();
        }

        /**
         * Событие мыши-освобождение курсора
         */
        public void mouseReleased(MouseEvent event) {
            GetFirstTime = 0;
        }

        /**
         * Изменение масштаба
         */
        public void scale(double factor) {

            if ((Axes.measure > 5 && ToolBar.Reduce)
                    || (Axes.measure < 100 && ToolBar.Zoom)) {
                Axes.measure += factor;
            }
        }

        /**
         * Добавление угла с помощью мыши
         *
         * Для такого построения необходимо 3 опорные точки,по которым уже
         * строются 2 отрезка под заданным углом Поэтому при каждом из 3 событий
         * выполняются свои операции. 1 шаг(nc=0)-запоминаются координаты точки
         * 2 шаг(nc=1)-запоминается вторая точка,строится первый
         * отрезок,вычисляется угол наклона отрезка относительно оси ОХ 3
         * шаг(EndMouseMove=true)-запоминается последняя точка,вызывается метод
         * построения угла addCorner().
         */
        public void Corner_mouse() {

            if (!EndMouseMove) {

                if (nc == 0) {

                    TP.add(new Point2D.Double(Xst, Yst));

                }
                if (nc == 1) {
                    TP.add(new Point2D.Double(Xst, Yst));
                    TInterval.add(inter);
                    betta = Math.PI
                            - Math.atan((double) (TP.get(1).getY() - TP.get(0)
                            .getY())
                            / (TP.get(1).getX() - TP.get(0).getX()));

                }

                nc++;

            } else {
                TP.add(inter.getP2());
                TInterval.add(inter);
                ToolBar.AddCorner = true;
                ToolBar.CornerMouseAdd = false;
                addCorner();
                nc = 0;
                EndMouseMove = false;
                TInterval.removeAll(TInterval);
                inter = new Line2D.Double();
            }
        }

        /**
         * Добавление многоугольника с помощью мыши При очередном щелчке мыши
         * запоминаются координаты точки,строится отрезок,соединяющий предыдущую
         * точку с текущей Если текущая точка попадает в начальную(плюс
         * небольшая область вокруг нее),то построение завершается и вызывается
         * метод добавления многоугольника в основной список.addPolygon().
         */
        public void Polygon_mouse() {

            if (!EndMouseMove) {

                BeginMove = true;
                TP.add(new Point2D.Double(Xst, Yst));
                TInterval.add(inter);
                k++;
                e = new Ellipse2D.Double(TP.get(0).getX() - 5,
                        TP.get(0).getY() - 5, 10, 10);

                if (e.contains(Xst, Yst) && k != 1) {
                    EndMouseMove = true;
                    BeginMove = false;
                    TP.remove(TP.size() - 1);

                    DrawElement.PolyX = new int[TP.size()];
                    DrawElement.PolyY = new int[TP.size()];
                    DrawElement.pX = new double[TP.size()];
                    DrawElement.pY = new double[TP.size()];
                    
                     pXX = new double[TP.size()];
                     pYY = new double[TP.size()];
                    for (int i = 0; i < TP.size(); i++) {
                        PolyX[i] = (int) TP.get(i).getX();
                        PolyY[i] = (int) TP.get(i).getY();
                        pX[i] = (double) (PolyX[i] - Axes.l) / Axes.measure;
                        pXX[i] = pX[i];
                        pYY[i] = pY[i];
                        pY[i] = (double) (-PolyY[i] + Axes.h) / Axes.measure;
                    }
                    NumOfPol = TP.size();
                    ToolBar.numPolPoint = true;
                    addPolygon();
                    BeginMove = false;
                    EndMouseMove = false;
                    ToolBar.polygonMouseAdd = false;
                    TP.removeAll(TP);
                    TInterval.removeAll(TInterval);
                    k = 0;
                    inter = new Line2D.Double();
                }
            }
        }

        /**
         * Добавление отрезка с помощью мыши Обрабатывается всего 2 щелчка мыши
         * первый- начальная точка второй-конечная точка При окончании
         * потсроения вызывается метод добавления отрезка в основной список
         * addInter()
         */
        public void Interval_mouse() {
            BeginMove = true;
            if (!EndMouseMove) {
                X = Xst;
                Y = Yst;
                TPoint1 = new Ellipse2D.Double(X - pointRad / 2, Y - pointRad
                        / 2, pointRad, pointRad);

            } else {
                x = X;
                y = Y;
                Xvis = (x - Axes.l) / (Axes.measure);
                Yvis = (-y + Axes.h) / Axes.measure;
                ToolBar.AddInter = true;
                addInter();
                ToolBar.IntervalMouseAdd = false;
                inter = new Line2D.Double();
                TPoint1 = new Ellipse2D.Double();
                TPoint2 = new Ellipse2D.Double();
                BeginMove = false;
                EndMouseMove = false;
            }
        }

        /**
         * Добавление точки с помощью мыши
         *
         * Обрабатывается только 1 щелчок мыши,запоминаются координаты указателя
         * и вызывается метод addPoint()
         */
        public void Point_mouse() {
            x = Xst;
            y = Yst;
            Xvis = (x - Axes.l) / (Axes.measure);
            Yvis = (-y + Axes.h) / Axes.measure;
            ToolBar.AddPoint = true;
            addPoint();
            ToolBar.pointMouseAdd = false;
        }

        /**
         * Добавление прямой с помощью мыши
         *
         * Обрабатывается 2 щелчка мыши Построение прямой ведется по 2
         * точкам,при движении мыши вычисляются коэффициенты (y=kx+b), данные о
         * которых передаются в addLine()
         */
        public void Line_mouse() {
            BeginMove = true;
            if (!EndMouseMove) {
                X = Xst;
                Y = Yst;

            } else {
                x = X;
                y = Y;

                ToolBar.AddLine = true;
                addLine();
                ToolBar.LineMouseAdd = false;
                inter = new Line2D.Double();

                BeginMove = false;
                EndMouseMove = false;
            }
        }

        /**
         * Добавление окружности,эллипса с помощью мыши
         *
         * Обрабатываются 2 события мыши 1-определяется центр 2-определяется
         * радиус(полуоси) Эта информация передается в метод addEllipse()
         */
        public void Circle_mouse() {
            BeginMove = true;
            if (!EndMouseMove) {
                X = Xst;
                Y = Yst;
                TCircle1 = new Ellipse2D.Double(X - pointRad / 2, Y - pointRad
                        / 2, pointRad, pointRad);
            } else {
                x = X;
                y = Y;
                Xvis = (x - Axes.l) / (Axes.measure);
                Yvis = (-y + Axes.h) / Axes.measure;
                RadXvis = (radiusX / Axes.measure);
                if (ToolBar.CircleMouseAdd) {
                    RadYvis = RadXvis;
                    ToolBar.AddCirc = true;
                }
                if (ToolBar.EllipseMouseAdd) {
                    RadYvis = (radiusY / Axes.measure);
                    ToolBar.AddEll = true;
                }

                addEllipse();
                ToolBar.CircleMouseAdd = false;
                ToolBar.EllipseMouseAdd = false;
                BeginMove = false;
                EndMouseMove = false;
                TCircle = new Ellipse2D.Double();
            }
        }
    }

    /**
     * Класс MouseMotionHandler. Обрабатывает такие события мыши как: -движение
     * мыши (без нажатых кнопок) public void mouseMoved(MouseEvent event)
     * -перемещение мыши при нажатой кнопке public void mouseDragged(MouseEvent
     * event)
     */
    private class MouseMotionHandler implements MouseMotionListener {

        double Vect1x, Vect1y, Vect2x, Vect2y;

        /**
         * Событие мыши- Движение без нажатой кнопки
         *
         * Этот метод используется при построении элементов с помощью мыши При
         * выполнении построения этот метод отправляет на экран промежуточное
         * визуальное представление того элемента,который строится
         */
        public void mouseMoved(MouseEvent event) {
            // Если строится окружность или эллипс,то при движении мыши на
            // экране отображается окружность(эллипс),
            // центр которой зафиксирован,а радиус зависит от модуля расстояния
            // между центром и указателем курсора мыши
            if (ToolBar.CircleMouseAdd || ToolBar.EllipseMouseAdd) {
                if (BeginMove) {
                    radiusX = Math.abs(Xst - event.getX());
                    if (ToolBar.CircleMouseAdd) {
                        radiusY = radiusX;
                    }
                    if (ToolBar.EllipseMouseAdd) {
                        radiusY = Math.abs(Yst - event.getY());
                    }
                    TCircle = new Ellipse2D.Double(Xst - radiusX,
                            Yst - radiusY, 2 * radiusX, 2 * radiusY);
                    repaint();
                    EndMouseMove = true;
                }
            }
            // Если строится отрезок,то при движении мыши на экран выводится
            // текущее представление этого отрезка,один конец которого уже
            // зафиксирован,а второй связан с указателем курсора мыши
            if (ToolBar.IntervalMouseAdd) {
                if (BeginMove) {

                    x1 = event.getX();
                    y1 = event.getY();
                    Xvis1 = (x1 - Axes.l) / Axes.measure;
                    Yvis1 = (-y1 + Axes.h) / Axes.measure;
                    inter = new Line2D.Double(Xst, Yst, x1, y1);
                    TPoint2 = new Ellipse2D.Double(x1 - pointRad / 2, y1
                            - pointRad / 2, pointRad, pointRad);
                    repaint();
                    EndMouseMove = true;
                }
            }
            // Если строится прямая,то на экран выводится отрезок,концы которого
            // за пределами видимой границы,однако угол наклона
            // определяется при движении мыши
            if (ToolBar.LineMouseAdd) {
                if (BeginMove) {

                    x1 = event.getX();
                    y1 = event.getY();

                    double Xst0 = (double) (Xst - Axes.l) / Axes.measure;
                    double Yst0 = (double) (-Yst + Axes.h) / Axes.measure;
                    double x10 = (double) (x1 - Axes.l) / Axes.measure;
                    double y10 = (double) (-y1 + Axes.h) / Axes.measure;
                    coeffK = (double) (Yst0 - y10) / (Xst0 - x10);
                    coeffB = Yst0 - coeffK * Xst0;
                    double x = 100 * Axes.measure + Axes.l;
                    double x0 = -100 * Axes.measure + Axes.l;
                    double y0 = -(coeffK * -100 + coeffB) * Axes.measure
                            + Axes.h;
                    double y = -(coeffK * 100 + coeffB) * Axes.measure + Axes.h;

                    inter = new Line2D.Double(x0, y0, x, y);

                    repaint();
                    EndMouseMove = true;
                }
            }
            // Если строится многоугольник,то на экарн выводится отрезок,который
            // соединяет
            // уже зафиксированную точку с указателем курсора мыши,которой
            // управляет пользователь
            if (ToolBar.polygonMouseAdd) {
                if (BeginMove) {
                    x1 = event.getX();
                    y1 = event.getY();
                    inter = new Line2D.Double(Xst, Yst, x1, y1);
                    repaint();

                }
            }
            // Если строится угол,то при каждом шаге(nc=1 и nc=2) на экран
            // выводятся отрезок,который меняет свой конец в зависимости от
            // движения мыши
            if (ToolBar.CornerMouseAdd) {

                if (nc == 1) {
                    x1 = event.getX();
                    y1 = event.getY();
                    inter = new Line2D.Double(Xst, Yst, x1, y1);
                    repaint();
                    Vect1x = x1 - Xst;
                    Vect1y = y1 - Yst;
                }
                if (nc == 2) {
                    x1 = event.getX();
                    y1 = event.getY();
                    ALPHA = ALPHA % 360;
                    boolean a = ALPHA >= 0 && ALPHA <= 90;
                    boolean c = ALPHA > 90 && ALPHA <= 180;
                    boolean d = ALPHA > 180 && ALPHA <= 270;
                    boolean f = ALPHA > 270 && ALPHA <= 360;

                    double k = Math.tan(alpha - betta);
                    double b = Yst - k * Xst;
                    Vect2x = x1 - Xst;
                    Vect2y = k * x1 + b - Yst;
                    if (((Vect1x * Vect2x + Vect1y * Vect2y) < 0 && a)
                            || ((Vect1x * Vect2x + Vect1y * Vect2y) > 0 && c)
                            || ((Vect1x * Vect2x + Vect1y * Vect2y) > 0 && d)
                            || ((Vect1x * Vect2x + Vect1y * Vect2y) < 0 && f)) {
                        inter = new Line2D.Double(Xst, Yst, x1, k * x1 + b);
                        repaint();
                        EndMouseMove = true;

                    }
                }
            }
        }

        /**
         * Событие мыши-перемещение при нажатой кнопке
         *
         * Осуществляется вызов методов,отвечающих за перемещение конкретного
         * типа элементов. А так же поворот эллипса на некоторый угол,величина
         * которого определяется движением мыши, после чего данные об элементе
         * перезаписываются
         */
        public void mouseDragged(MouseEvent event) {
            if (ToolBar.moveObject) {
                if (getEll) {
                    moveEllipse(event); // если захвачен эллипс,окружность
                }
                if (getRect) {
                    movePolygon(event); // если захвачен многоугольник
                }
                if (getInterval) {
                    moveInterval(event); // если захвачен отрезок
                }
                if (getPoint) {
                    movePoint(event); // если захвачена точка
                }
                if (currentCorner != null) { // если захвачен угол
                    moveCorner(event);
                }
                if (currentBound != null) {
                    moveSign(event); // если захвачена надпись
                }

            }
            if (ToolBar.Rotate) {
                if (getEll && currentEll.getHeight() != currentEll.getWidth()) {
                    double a = Math.atan2(-event.getX() + Xst - 100,
                            event.getY() - Yst);
                    Rotate.set(circles.indexOf(currentEll), a);
                    Xvis = (double) (currentEll.getCenterX() - Axes.l)
                            / Axes.measure;
                    Yvis = (double) (-currentEll.getCenterY() + Axes.h)
                            / Axes.measure;
                    RadXvis = (double) currentEll.getWidth() / 2 / Axes.measure;
                    RadYvis = (double) currentEll.getHeight() / 2
                            / Axes.measure;
                    ParamEllipse.set(
                            circles.indexOf(currentEll),
                            "Эллипс= центр( " + Xvis + ", " + Yvis
                            + "); полуосьХ  " + RadXvis + ", полуосьY "
                            + RadYvis + " )" + "; наклон("
                            + Rotate.get(circles.indexOf(currentEll))
                            + " )");
                }

            }
            repaint();
        }
    }
    static int n, m, f;
    static int[] r;
    static double xp, yp;
    static int GetFirstTime = 0;

    /**
     * Движение имени,закрепленного за некоторой точкой. Метод вызывается при
     * движении основных элементов
     */
    public static void movePointName(int k) {
        if (k >= 0) {
            xp = nameXY.get(k).getX();
            yp = nameXY.get(k).getY();
            nameXY.set(k, new Point2D.Double(xp + dx, yp + dy));

        }
    }

    /**
     * Поиск имени,закрепленного за некторой точкой Возвращает индекс
     * координат,определяющих положение имени, в списке nameXY
     */
    public static int find_name(Ellipse2D e) {
        if (nameXY.size() != 0) {
            for (int i = 0; i < nameXY.size(); i++) {
                if (e.contains(nameXY.get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Движение точки При первом перемещении находится имя точки,если оно за ней
     * закреплено Данные о точке перезаписываются
     */
    public void movePoint(MouseEvent e) {

        int x1 = e.getX();
        int y1 = e.getY();

        dx = (int) (x1 - Xst);
        dy = (int) (y1 - Yst);
        if (GetFirstTime == 0) {
            n = find_name(currentCP);
        }
        movePointName(n);
        currentCP.setFrame(currentCP.getMinX() + dx, currentCP.getMinY() + dy,
                pointRad, pointRad);

        Xst = x1;
        Yst = y1;

        Xvis = (double) (currentCP.getCenterX() - Axes.l) / Axes.measure;
        Yvis = (double) (-currentCP.getCenterY() + Axes.h) / Axes.measure;

        S = "Точка= ( " + Xvis + ", " + Yvis + " )";

        ParamPoint.set(points.indexOf(currentCP), S);
        GetFirstTime++;
    }

    /**
     * Движение надписи Прямоугольнику,охватывающему надпись,передаются новые
     * параметры,с новыми координатыми
     */
    public void moveSign(MouseEvent e) {
        int x1 = e.getX();
        int y1 = e.getY();

        dx = (int) (x1 - Xst);
        dy = (int) (y1 - Yst);

        currentBound.setFrame(currentBound.getMinX() + dx,
                currentBound.getMinY() + dy, currentBound.getWidth(),
                currentBound.getHeight());
        Xst = x1;
        Yst = y1;
    }

    /**
     * Движение отрезка Вместе с отрезком перемещаются и точки деления отрезка и
     * имена этих точек Данные об отрезке перезаписываются
     */
    public void moveInterval(MouseEvent e) {

        int x1 = e.getX();
        int y1 = e.getY();

        dx = (int) (x1 - Xst);
        dy = (int) (y1 - Yst);

        for (Ellipse2D p : pDivision) {
            if (hashDiv.get(pDivision.indexOf(p)) == currentInter.hashCode()) {
                if (GetFirstTime == 0) {
                    f = find_name(p);
                }
                movePointName(f);
                p.setFrame(p.getMinX() + dx, p.getMinY() + dy, pointRad,
                        pointRad);

            }
        }

        currentInter.setLine(currentInter.getX1() + dx, currentInter.getY1()
                + dy, currentInter.getX2() + dx, currentInter.getY2() + dy);

        currentCP = pInter1.get(intervals.indexOf(currentInter));
        if (GetFirstTime == 0) {
            n = find_name(currentCP);
        }
        movePointName(n);
        currentCP.setFrame(currentCP.getMinX() + dx, currentCP.getMinY() + dy,
                pointRad, pointRad);
        currentCP = pInter2.get(intervals.indexOf(currentInter));
        if (GetFirstTime == 0) {
            m = find_name(currentCP);
        }
        movePointName(m);
        currentCP.setFrame(currentCP.getMinX() + dx, currentCP.getMinY() + dy,
                pointRad, pointRad);

        Xst = x1;
        Yst = y1;

        Xvis = (double) (currentInter.getX1() - Axes.l) / Axes.measure;
        Yvis = (double) (-currentInter.getY1() + Axes.h) / Axes.measure;
        Xvis1 = (double) (currentInter.getX2() - Axes.l) / Axes.measure;
        Yvis1 = (double) (-currentInter.getY2() + Axes.h) / Axes.measure;

        S = "Отрезок= начало(" + Xvis + ", " + Yvis + "),конец (" + Xvis1
                + ", " + Yvis1 + " )";
        ParamInter.set(intervals.indexOf(currentInter), S);

        GetFirstTime++;
    }

    /**
     * Перемещение угла Отрезкам,составлющим угол, передаются новые координаты.
     * Новые координаты передаются так же "точкам" на концах отрезка, и именам
     * точек, если таковые им присвоены
     */
    public void moveCorner(MouseEvent e) {

        int x1 = e.getX();
        int y1 = e.getY();

        dx = (int) (x1 - Xst);
        dy = (int) (y1 - Yst);

        currentCorner.setLine(currentCorner.getX1() + dx, currentCorner.getY1()
                + dy, currentCorner.getX2() + dx, currentCorner.getY2() + dy);
        Line2D c = cornLine.get(based.indexOf(currentCorner));
        c.setLine(c.getX1() + dx, c.getY1() + dy, c.getX2() + dx, c.getY2()
                + dy);
        for (int i = 0; i < 3; i++) {

            currentCP = pCorner.get(cornLine.indexOf(c)).get(i);
            if (GetFirstTime == 0) {
                n = find_name(currentCP);
            }
            movePointName(n);
            currentCP.setFrame(currentCP.getMinX() + dx, currentCP.getMinY()
                    + dy, pointRad, pointRad);
        }

        Xst = x1;
        Yst = y1;
        GetFirstTime++;
    }
    int[] s;

    /**
     * Перемещение эллипса,окружности Вместе с основной фигурой перемещаются
     * сектор,касательная и имена точек Данные об основном элементе
     * перезаписываются
     */
    public void moveEllipse(MouseEvent e) {
        int x1 = e.getX();
        int y1 = e.getY();

        dx = (int) (x1 - Xst);
        dy = (int) (y1 - Yst);
        currentCP = pEll.get(circles.indexOf(currentEll));
        if (GetFirstTime == 0) {
            n = find_name(currentCP);
            nArc = new int[sectors.size()];
            nTan = new int[tangents.size()];
            s = new int[tangents.size()];
        }
        if (GetFirstTime == 0) {
            for (int i = 0; i < sectors.size(); i++) {
                if (rArc.get(i).getX() == currentEll.getWidth()
                        && rArc.get(i).getY() == currentEll.getHeight()
                        && cArc.get(i).getX() == currentEll.getMinX()
                        && cArc.get(i).getY() == currentEll.getMinY()) {
                    nArc[i] = i;
                } else {
                    nArc[i] = -1;
                }
            }

            for (int i = 0; i < tangents.size(); i++) {
                if (cTan.get(i).getX() == currentEll.getCenterX()
                        && cTan.get(i).getY() == currentEll.getCenterY()
                        && rTan.get(i).getX() == currentEll.getWidth() * 0.5
                        && rTan.get(i).getY() == currentEll.getHeight() * 0.5) {

                    nTan[i] = i;
                    s[i] = find_name(pTang.get(i));
                } else {
                    nTan[i] = -1;
                }
            }

        }

        currentEll.setFrame(currentEll.getMinX() + dx, currentEll.getMinY()
                + dy, currentEll.getWidth(), currentEll.getHeight());

        movePointName(n);
        currentCP.setFrame(currentCP.getMinX() + dx, currentCP.getMinY() + dy,
                pointRad, pointRad);

        for (int i = 0; i < tangents.size(); i++) {
            if (nTan[i] != -1) {
                Line2D t = tangents.get(nTan[i]);
                t.setLine(t.getX1() + dx, t.getY1() + dy, t.getX2() + dx,
                        t.getY2() + dy);
                Ellipse2D p = pTang.get(nTan[i]);
                p.setFrame(p.getMinX() + dx, p.getMinY() + dy, p.getWidth(),
                        p.getHeight());
                movePointName(s[i]);
                cTan.set(i, new Point2D.Double(currentEll.getCenterX(),
                        currentEll.getCenterY()));
            }
        }

        for (int i = 0; i < sectors.size(); i++) {
            if (nArc[i] != -1) {
                sectors.get(nArc[i]).setFrame(currentEll.getMinX(),
                        currentEll.getMinY(), currentEll.getWidth(),
                        currentEll.getHeight());
                cArc.set(
                        i,
                        new Point2D.Double(currentEll.getMinX(), currentEll
                        .getMinY()));
            }
        }

        Xst = x1;
        Yst = y1;

        Xvis = (double) (currentEll.getCenterX() - Axes.l) / Axes.measure;
        Yvis = (double) (-currentEll.getCenterY() + Axes.h) / Axes.measure;

        if (currentEll.getHeight() == currentEll.getWidth()) {
            RadXvis = (double) currentEll.getWidth() / 2 / Axes.measure;
            ParamEllipse.set(circles.indexOf(currentEll), "Окружность= центр("
                    + Xvis + ", " + Yvis + "), радиус  " + RadXvis);
        } else {
            RadXvis = (double) currentEll.getWidth() / 2 / Axes.measure;
            RadYvis = (double) currentEll.getHeight() / 2 / Axes.measure;
            ParamEllipse.set(circles.indexOf(currentEll),
                    "Эллипс= центр(" + Xvis + ", " + Yvis + "),полуосьХ  "
                    + RadXvis + ", полуосьY " + RadYvis + "; наклон("
                    + Rotate.get(circles.indexOf(currentEll)) + " )");
        }
        GetFirstTime++;
    }

    /**
     * Перемещение многоугольника С помощью функции translate производится
     * перемещение всего объекта на некоторую величину Эта величина определяется
     * разницей между предыдущим положением и новым Вместе с многоугольником
     * перемещаются и имена вершин,есмли они им присвоены
     */
    public void movePolygon(MouseEvent e) {
        int x1 = e.getX();
        int y1 = e.getY();

        dx = (int) (x1 - Xst);
        dy = (int) (y1 - Yst);
        if (GetFirstTime == 0) {
            r = new int[currentPoly.npoints];
        }

        currentPoly.translate(dx, dy);
        for (int i = 0; i < currentPoly.npoints; i++) {

            currentCP = pPoly.get(polygon.indexOf(currentPoly)).get(i);

            currentCP.setFrame(currentPoly.xpoints[i] - pointRad / 2,
                    currentPoly.ypoints[i] - pointRad / 2, pointRad, pointRad);
            if (GetFirstTime == 0) {
                r[i] = find_name(currentCP);
            }
            movePointName(r[i]);
        }
        Xst = x1;
        Yst = y1;
        S = "Многоугольник= вершины ";

        pX = new double[currentPoly.npoints];
        pY = new double[currentPoly.npoints];
        for (int i = 0; i < currentPoly.npoints; i++) {
            pX[i] = (double) (currentPoly.xpoints[i] - Axes.l) / Axes.measure;
            pY[i] = (double) (-currentPoly.ypoints[i] + Axes.h) / Axes.measure;
            S = S + "(" + pX[i] + ", " + pY[i] + ")";
        }
        ParamPoly.set(polygon.indexOf(currentPoly), S);

        GetFirstTime++;
    }
}
