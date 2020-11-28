package com.mathpar.Graphic2D.Element2D;

import com.mathpar.func.F;
import com.mathpar.func.Fname;
import com.mathpar.number.Element;
import com.mathpar.number.NumberR64;
import com.mathpar.polynom.Polynom;

import java.util.List;

public class PlanimetriaUtils {


    /**
     * реализация \showPlots()
     * Пример:
     * List<Element> list = new Arrays.toList(new Element[] {paramPlot, pointsPlot});
     * showPolots(list);
     *
     * @param plots принимает список графиков: tableplot, pointplot, paramplot
     * @return объект showPlots
     */
    public static F showPlots(List<Element> plots) {
        return new F(F.SHOWPLOTS, vector(plots.toArray(new Element[0])));
    }

    /**
     * реализация \showPlots()
     * Пример:
     * showPlots(paramPlot, pointsPlot);
     *
     * @param plots принимает список графиков: tableplot, pointplot, paramplot
     * @return объект showPlots
     */
    public static F showPlots(Element... plots) {
        return new F(F.SHOWPLOTS, plots);
    }

    /**
     * реализация \tablePlot([[x1, x2.. xn], [y1, y2.. yn]])
     * Пример:
     *
     * F vectorX = vector(1, 2, 3)
     * F vectorY = vector(4, 5, 6)
     *
     * tablePlot(vector(vectorX, vectorY));
     *
     * Будет построено 3 точки (1, 4), (2, 5), (3, 6), последовательно связанные линиями.
     *
     * @param nums вектор 2 векторов
     * @return объект tablePlot
     */
    public static F tablePlot(Element nums) {
        return new F(F.TABLEPLOT, nums);
    }

    /**
     * реализация \pointsPlot([[x1, x2.. xn], [y1, y2.. yn]], [a, b, c])
     * Пример:
     *
     * F vectorX = vector(1, 2, 3)
     * F vectorY = vector(4, 5, 6)
     *
     * tablePlot(vector(vectorX, vectorY), vector("A", "B", "C"));
     *
     * Будет построено 3 отдеьные точки (1, 4), (2, 5), (3, 6) с подписями A B и C соответвенно
     *
     * @param nums вектор 2 векторов
     * @param labels векткор подписей к точкам
     *
     * @return объект pointsPlot
     */
    public static F pointsPlot(Element nums, Element labels) {
        return new F(F.POINTSPLOT, new Element[]{nums, labels});
    }

    /**
     * реализация \paramPlot([[g, k], [a, b]])
     * Пример:
     *
     * F cosX = cosX()
     * F sinX = sinX()
     *
     * paramPlot((vector(cosX, sinX), vector(r64(0), pi()));
     *
     * Будет построено 3 отдеьные точки (1, 4), (2, 5), (3, 6).
     *
     * @param functions вектор 2 векторов
     * @param values вектор значений параметра
     * @return объект pointsPlot
     */
    public static F paramPlot(Element functions, Element values) {
        return new F(F.PARAMPLOT, new Element[]{functions, values});
    }


    /**
     * Вектор строк, например [a, b, c, ...]
     * @param args  елементы вектора
     * @return вектор
     */
    public static F vector(String... args) {
        return new F(F.VECTORS, elements(args));
    }

    /**
     * Вектор елементов, например [1, 2, 3, ...]
     *
     * @param list список елементов вектора
     * @return вектор
     */
    public static F vector(List<Element> list) {
        return vector(list.toArray(new Element[0]));
    }

    /**
     * Вектор елементов, например [1, 2, 3, ...]
     *
     * @param args елементы вектора
     * @return вектор
     */
    public static F vector(Element... args) {
        return new F(F.VECTORS, args);
    }

    /**
     * Вектор чисел, например [1, 2, 3, ...]
     *
     * @param nums елементы вектора
     * @return вектор
     */
    public static F vector(double... nums) {
        return new F(F.VECTORS, elements(nums));
    }

    /**
     * Вектор елементов, например [1, 2, 3, ...]
     *
     * @param nums елементы вектора
     * @return вектор
     */
    public static F vector(F... nums) {
        return new F(F.VECTORS, elements(nums));
    }

    /**
     * сумма 2 чисел
     *
     * @param nums елементы вектора
     * @return объект суммы
     */
    public static F add(Element... nums) {
        return new F(F.ADD, nums);
    }

    /**
     * разность 2 чисел
     *
     * @param nums елементы вектора
     * @return объект суммы
     */
    public static F subtract(Element... nums) {
        return new F(F.SUBTRACT, nums);
    }

    /**
     * произведение 2 чисел
     *
     * @param nums елементы вектора
     * @return объект суммы
     */
    public static F multiply(Element... nums) {
        return new F(F.MULTIPLY, nums);
    }

    /**
     * функция
     *
     * @param name название функции
     * @return объект функции
     */
    public static F func(String name) {
        return new F(F.ID, new Fname(name));
    }

    /**
     * функция
     *
     * @param name название функции
     * @return объект функции
     */
    public static F func(String name, F value) {
        return new F(F.ID, new Fname(name, value));
    }


    private static Element[] elements(F... nums) {
        return nums;
    }

    private static Element[] elements(String... args) {
        Element[] elements = new F[args.length];
        for (int i = 0; i < args.length; i++) {
            elements[i] = hbox(args[i]);
        }
        return elements;
    }

    private static Element[] elements(double... nums) {
        Element[] elements = new Element[nums.length];
        for (int i = 0; i < nums.length; i++) {
            elements[i] = r64(nums[i]);
        }
        return elements;
    }

    /**
     * Декйствительное число
     * @param num число
     * @return полином
     */
    public static Polynom r64(double num) {
        return new Polynom(new NumberR64(num));
    }

    /**
     * Обертка над строкой  hobx
     * @param str строка
     * @return hbox
     */
    public static F hbox(String str) {
        return new F(F.HBOX, new Element[]{new Fname(str)});
    }

    /**
     * синус
     * @return объект синуса
     */
    public static F sinX() {
        return new F(F.SIN, new Polynom(new int[]{1}, new Element[]{r64(1)}));
    }

    /**
     * косинус
     * @return объект косинуса
     */
    public static F cosX() {
        return new F(F.COS, new Polynom(new int[]{1}, new Element[]{r64(1)}));
    }

    public static F createIntervalF(double x1, double x2, double y1, double y2) {
        F fx1 = vector(x1, x2);
        F fy1 = vector(y1, y2);
        return vector(fx1, fy1);
    }

    public static F createPointF(double x, double y, String label) {
        F fx = vector(x);
        F fy = vector(y);
        F point = vector(fx, fy);

        return pointsPlot(point, vector(label));
    }
}
