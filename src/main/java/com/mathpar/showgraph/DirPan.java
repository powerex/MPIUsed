/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

// © Разработчик Репин Никита Сергеевич, ТГУ'09.
//Andrey Betin 10.12.2010 смена JFrame на CANVAS
package com.mathpar.showgraph;

import com.mathpar.func.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import com.mathpar.number.Element;
import com.mathpar.number.Fraction;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
import com.mathpar.polynom.*;

/**
 *
 * @author Repin Nikita Sergeevich TSU'09
 */
public class DirPan extends Canvas {

    static String HOME_DIR = "/tmp/";
    F f1 = new F();
    Element[] f2 = new Element[0];
    /**
     * Массив функций, заданных параметрически [0,0]=x0(t), [0,1]=y0(t),
     * [1,0]=x1(t), [1,1]=y1(t),....
     */
    String[][] MassParamFuncs;
    /**
     * левая граница интервала аргумента Х
     */
    double xMin;
    /**
     * правая граница интервала аргумента Х
     */
    double xMax;
    /**
     * Число дискретных значений аргумента
     */
    int NumbOfSteps = 32000;
    /**
     * Автоматический расчет интервала значений по оси ординат
     */
    boolean AvtoMatick = false;
    /**
     * нижняя граница интервала Y
     */
    double yMin;
    /**
     * верхняя граница интервала Y
     */
    double yMax;
    /**
     * Таблица значений Y
     */
    double yR[][] = new double[][]{};
    /**
     * Таблица значения X
     */
    double xR[][] = new double[][]{};
    /**
     * левая граница интервала аргумента Т
     */
    double tMin;
    /**
     * правая граница интервала аргумента Т
     */
    double tMax;
    /**
     * Коэффициэнт увеличения(уменьшения) по Х
     */
    double zoom1;
    /**
     * Коэффициэнт увеличения(уменьшения) по Y
     */
    double zoom2; //
    /**
     * Координаты центра начала координат
     */
    int xcor = 836;
    int ycor = 300;
    /**
     * Корректирующий коэффициент по Y
     */
    int yCorrect = 0;
    /**
     * Корректирующий коэффициент по Х
     */
    int xCorrect = 0;
    /**
     * Параметры автоматического построения графиков
     */
    double xStart = 0;
    double xEnd = 0;
    double yStart = 0;
    double yEnd = 0;
    /**
     * Переменная в которой храняться аналитические функции
     */
    Page page;
    /**
     * Массив значений аналитической функции, в столбцах номера функций, в
     * строках их значения
     */
    double[][] MassVich;
    /**
     * Переменная в которой храняться параметрические функции
     */
    Page pageParam;
    /**
     * Массив значений параметрической функции, в столбцах номера функций, в
     * строках их значения
     */
    double[][] MassParamVich;
    boolean parcaPort = false;
    String x_str;
    String y_str;
    String title_str;
    //--------------------------------------------------------------------------------------------------------------

    public DirPan() {
    }

    /**
     * Конструктор DirPan для явных функций
     *
     * @param pageObj
     * @param xMin
     * @param xMax
     * @param yMin
     * @param yMax
     * @throws Exception
     */
    public DirPan(Page pageObj, double xMin, double xMax, double yMin, double yMax,
            boolean parcaPort, String x_str, String y_str, String title_str) {
        this.page = pageObj;
        pageParam = new Page();
        this.parcaPort = parcaPort;
        this.x_str = x_str;
        this.y_str = y_str;
        this.title_str = title_str;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        MassVich = funcToMass();
        MassParamVich = funcParamToMass();
        if (parcaPort) {
            paint(super.getGraphics());
        } else {
            setSize(1366, 700);
            setVisible(true);
        }
    }

    /**
     * Конструктор DirPan для явных функций
     *
     * @param pageObj
     * @param xMin
     * @param xMax
     * @param yMin
     * @param yMax
     * @throws Exception
     */
    public DirPan(Page page, F func, double xMin, double xMax, double yMin, double yMax,
            boolean parcaPort, String x_str, String y_str, String title_str) throws Exception {
        this.page = page;
        Ring ring = new Ring("R64[x]");
        this.parcaPort = parcaPort;
        this.x_str = x_str;
        this.y_str = y_str;
        this.title_str = title_str;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;

        f1 = func;
        f2 = new F[0];
        double[][] massOfNumbers = new double[f1.X.length][NumbOfSteps];
        double xPer;

        for (int i = 0; i <= f1.X.length - 1; i++) {
            xPer = xMin;
            for (int j = 0; j <= NumbOfSteps - 1; j++) {
                if (((F) f1).X[i] instanceof F) {
                    massOfNumbers[i][j] = ((F) f1.X[i]).valueOf(new Element[]{new NumberR64(xPer)}, ring).doubleValue();
                }
                if (((F) f1).X[i] instanceof Polynom) {
                    massOfNumbers[i][j] = ((Polynom) f1.X[i]).value(new Element[]{new NumberR64(xPer)}, ring).doubleValue();
                }
                xPer = xPer + (xMax - xMin) / (NumbOfSteps);
            }
        }

        MassVich = massOfNumbers;
        if (parcaPort) {
            paint(super.getGraphics());
        } else {
            setSize(1366, 700);
            setVisible(true);
        }
    }

    /**
     * Конструктор DirPan для явных функций
     *
     * @param pageObj
     * @param xMin
     * @param xMax
     * @param yMin
     * @param yMax
     * @throws Exception
     */
    public DirPan(Page page, Element func, double xMin, double xMax, boolean parcaPort,
            String x_str, String y_str, String title_str) throws Exception {
        this.page = page;
        Ring ring = new Ring("R64[x]");
        double delta = (xMax - xMin) * 5;
        double a0 = -delta;
        double a1 = delta;
        boolean fl0 = false;
        boolean fl1 = false;
        this.parcaPort = parcaPort;
        this.x_str = x_str;
        this.y_str = y_str;
        this.title_str = title_str;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = 0;
        this.yMax = 0;
        double y0 = 0;
        double y1 = 0;
        if ((func instanceof F) && (((F) func).name != F.VECTORS)) {
            f1 = new F(F.VECTORS, func);
        } else if (func instanceof VectorS) {
            f1 = new F(F.VECTORS, ((VectorS) func).V);
        } else {
            f1 = (F) func;
        }

        f2 = new F[0];
        double[][] massOfNumbers = new double[f1.X.length][NumbOfSteps];
        double xPer;

        for (int i = 0; i <= f1.X.length - 1; i++) {
            xPer = xMin;
            for (int j = 0; j <= NumbOfSteps - 1; j++) {
                if (((F) f1).X[i] instanceof F) {
                    massOfNumbers[i][j] = ((F) f1.X[i]).valueOf(new Element[]{new NumberR64(xPer)}, ring).doubleValue();
                    if (massOfNumbers[i][j] > y1) {
                        y1 = massOfNumbers[i][j];
                    }
                    if (massOfNumbers[i][j] > a1) {
                        fl1 = true;
                        massOfNumbers[i][j] = a1 * 100;
                    }
                    if (massOfNumbers[i][j] < y0) {
                        y0 = massOfNumbers[i][j];
                    }
                    if (massOfNumbers[i][j] < a0) {
                        fl0 = true;
                        massOfNumbers[i][j] = a0 * 100;
                    }
                }
                if (((F) f1).X[i] instanceof Polynom) {
                    massOfNumbers[i][j] = ((Polynom) f1.X[i]).value(new Element[]{new NumberR64(xPer)}, ring).doubleValue();
                    if (massOfNumbers[i][j] > y1) {
                        y1 = massOfNumbers[i][j];
                    }
                    if (massOfNumbers[i][j] > a1) {
                        fl1 = true;
                        massOfNumbers[i][j] = a1 * 100;
                    }
                    if (massOfNumbers[i][j] < y0) {
                        y0 = massOfNumbers[i][j];
                    }
                    if (massOfNumbers[i][j] < a0) {
                        fl0 = true;
                        massOfNumbers[i][j] = a0 * 100;
                    }
                }
                xPer = xPer + (xMax - xMin) / (NumbOfSteps);
            }
        }

        this.yMin = (fl0) ? a0 : y0;
        this.yMax = (fl1) ? a1 : y1;

        MassVich = massOfNumbers;
        if (parcaPort) {
            paint(super.getGraphics());
        } else {
            setSize(1366, 700);
            setVisible(true);
        }
    }

    /**
     * конструктор DirPan для параметрических ф-ций
     *
     * @param tMin
     * @param tMax
     * @param yMin
     * @param yMax
     * @param pageObj
     * @throws Exception
     */
    public DirPan(Page page, Element[] func, double xMin, double xMax, double yMin, double yMax,
            double tMin, double tMax, boolean parcaPort,
            String x_str, String y_str, String title_str) throws Exception {
        Ring ring = new Ring("R64[x]");
        this.parcaPort = parcaPort;
        this.x_str = x_str;
        this.y_str = y_str;
        this.title_str = title_str;
        this.yMin = yMin;
        this.yMax = yMax;
        this.tMin = tMin;
        this.tMax = tMax;
        this.xMin = xMin;//t
        this.xMax = xMax;//t
        f1.name = F.ABS;
        f1.X = new Element[0];
        f2 = func;
        pageParam = new Page();
        this.page = page;
        double[][] massParamNumbers = new double[func.length][2 * NumbOfSteps];
        int xy;
        double tPer;
        for (int i = 0; i <= func.length / 2 - 1; i++) {
            tPer = tMin;
            xy = i * 2;
            for (int j = 0; j <= 2 * NumbOfSteps - 1; j++) {
                if (func[xy] instanceof Polynom) {
                    massParamNumbers[i][j] = ((Polynom) func[xy]).value(new Element[]{new NumberR64(tPer)}, ring).doubleValue();
                }
                if (func[xy] instanceof Fraction) {
                    massParamNumbers[i][j] = ((Fraction) func[xy]).value(new Element[]{new NumberR64(tPer)}, ring).doubleValue();
                }
                if (func[xy] instanceof F) {
                    if (((F) func[xy]).name == 0) {
                        if (((F) func[xy]).X[0] instanceof Fname) {
                            Element a = ((Fname) ((F) func[xy]).X[0]).X[0];
                            if (((F) a).name != 0) {
                                massParamNumbers[i][j] = ((F) a).valueOf(new Element[]{new NumberR64(tPer)}, ring).doubleValue();
                            } else {
                                massParamNumbers[i][j] = ((Polynom) ((F) a).X[0]).value(new Element[]{new NumberR64(tPer)}, ring).doubleValue();
                            }
                        }
                    } else {
                        massParamNumbers[i][j] = ((F) func[xy]).valueOf(new Element[]{new NumberR64(tPer)}, ring).doubleValue();
                    }
                }

                if (func[xy + 1] instanceof Polynom) {
                    massParamNumbers[xy + 1][j] = ((Polynom) func[xy + 1]).value(new Element[]{new NumberR64(tPer)}, ring).doubleValue();
                }
                if (func[xy + 1] instanceof Fraction) {
                    massParamNumbers[xy + 1][j] = ((Fraction) func[xy + 1]).value(new Element[]{new NumberR64(tPer)}, ring).doubleValue();
                }
                if (func[xy + 1] instanceof F) {
                    if (((F) func[xy + 1]).name == 0) {
                        if (((F) func[xy + 1]).X[0] instanceof Fname) {
                            Element a = ((Fname) ((F) func[xy + 1]).X[0]).X[0];
                            if (((F) a).name != 0) {
                                massParamNumbers[xy + 1][j] = ((F) a).valueOf(new Element[]{new NumberR64(tPer)}, ring).doubleValue();
                            } else {
                                massParamNumbers[xy + 1][j] = ((Polynom) ((F) a).X[0]).value(new Element[]{new NumberR64(tPer)}, ring).doubleValue();
                            }
                        }
                    } else {
                        massParamNumbers[xy + 1][j] = ((F) func[xy + 1]).valueOf(new Element[]{new NumberR64(tPer)}, ring).doubleValue();
                    }
                }
                tPer = tPer + (tMax - tMin) / (NumbOfSteps);
            }
        }
        MassParamVich = massParamNumbers;
        if (parcaPort) {
            paint(super.getGraphics());
        } else {
            setSize(1366, 700);
            setVisible(true);
        }
    }

    /**
     * конструктор DirPan для параметрических ф-ций
     *
     * @param tMin
     * @param tMax
     * @param yMin
     * @param yMax
     * @param pageObj
     * @throws Exception
     */
    public DirPan(Page page, double xMin, double xMax, double yMin, double yMax,
            double tMin, double tMax, boolean parcaPort,
            String x_str, String y_str, String title_str) {
        this.page = new Page();
        this.pageParam = page;
        this.parcaPort = parcaPort;
        this.x_str = x_str;
        this.y_str = y_str;
        this.title_str = title_str;
        this.yMin = yMin;
        this.yMax = yMax;
        this.tMin = tMin;
        this.tMax = tMax;
        this.xMin = xMin;
        this.xMax = xMax;
        MassParamVich = funcParamToMass();
        if (parcaPort) {
            paint(super.getGraphics());
        } else {
            setSize(1366, 700);
            setVisible(true);
        }
    }

    /**
     * Построение графика функции заданного таблицей
     *
     * @param xR double[][]-значения по оси X
     * @param yR double[][]-значения по оси Y
     * @param xMin double-min знач сетки по оси X
     * @param xMax double-max знач сетки по оси X
     * @param yMin double-min знач сетки по оси Y
     * @param yMax double-max знач сетки по оси Y
     * @throws Exception
     */
    public DirPan(Page page, double xR[][], double yR[][], double xMin, double xMax,
            double yMin, double yMax, boolean parcaPort,
            String x_str, String y_str, String title_str) {
        this.page = page;
        pageParam = new Page();
        this.parcaPort = parcaPort;
        this.x_str = x_str;
        this.y_str = y_str;
        this.title_str = title_str;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.xR = xR;
        this.yR = yR;
        f1.name = F.ABS;
        f1.X = new Element[0];
        f2 = new Element[0];
        if (parcaPort) {
            paint(super.getGraphics());
        } else {
            setSize(1366, 700);
            setVisible(true);
        }
    }

    /**
     * Конструктор для постороения всего подряд
     *
     * @param xMin
     * @param xMax
     * @param yMin
     * @param yMax
     * @param tMin
     * @param tMax
     * @param xR
     * @param yR
     * @throws Exception
     */
    public DirPan(Page page, F func, Element[] arr, double xMin, double xMax, double yMin,
            double yMax, double tMin, double tMax, double[][] xR, double[][] yR,
            boolean parcaPort, String x_str, String y_str, String title_str) {
        Ring ring = new Ring("R64[x]");
        this.page = page;
        this.parcaPort = parcaPort;
        this.x_str = x_str;
        this.y_str = y_str;
        this.title_str = title_str;
        this.xMin = xMin;
        this.xMax = xMax;
        if ((yMin == 0) & (yMax == 0)) {
            this.AvtoMatick = true;
        } else {
            this.AvtoMatick = false;
        }
        this.yMin = yMin;
        this.yMax = yMax;
        this.xR = xR;
        this.yR = yR;
        this.tMin = tMin;
        this.tMax = tMax;
        if (func == null) {
            f1.name = F.ABS;
            f1.X = new Element[0];
        } else {
            f1 = func;
        }
        if (arr == null) {
            f2 = new F[0];
        } else {
            f2 = arr;
        }
        double[][] massOfNumbers = new double[f1.X.length][NumbOfSteps];
        double xPer;
        if (f1.X.length == 1) {//случай когда явная функция одна
            xPer = xMin;
            for (int j = 0; j <= NumbOfSteps - 1; j++) {
                if (f1 instanceof F) {
                    massOfNumbers[0][j] = ((F) f1).valueOf(new Element[]{new NumberR64(xPer)}, ring).doubleValue();
                }
                xPer = xPer + (xMax - xMin) / (NumbOfSteps);
            }
        } else {//случай когда у нас вектор явных функций
            for (int i = 0; i <= f1.X.length - 1; i++) {
                xPer = xMin;
                for (int j = 0; j <= NumbOfSteps - 1; j++) {
                    if (f1.X[i] instanceof F) {
                        massOfNumbers[i][j] = ((F) f1.X[i]).valueOf(new Element[]{new NumberR64(xPer)}, ring).doubleValue();
                    }
                    if (f1.X[i] instanceof Polynom) {
                        massOfNumbers[i][j] = ((Polynom) f1.X[i]).value(new Element[]{new NumberR64(xPer)}, ring).doubleValue();
                    }
                    xPer = xPer + (xMax - xMin) / (NumbOfSteps);
                }
            }
        }
        MassVich = massOfNumbers;
        double[][] massParamNumbers = new double[arr.length][2 * NumbOfSteps];
        int xy;
        double tPer;
        for (int i = 0; i <= arr.length / 2 - 1; i++) {
            tPer = tMin;
            xy = i * 2;
            for (int j = 0; j <= 2 * NumbOfSteps - 1; j++) {
                if (arr[xy] instanceof Polynom) {
                    massParamNumbers[i * 2][j] =
                            ((Polynom) arr[xy]).value(new Element[]{new NumberR64(tPer)}, ring).doubleValue();
                }
                if (arr[xy] instanceof F) {
                    massParamNumbers[i * 2][j] =
                            ((F) arr[xy]).valueOf(new Element[]{new NumberR64(tPer)}, ring).doubleValue();
                }
                if (arr[xy + 1] instanceof Polynom) {
                    massParamNumbers[i * 2 + 1][j] =
                            ((Polynom) arr[xy + 1]).value(new Element[]{new NumberR64(tPer)}, ring).doubleValue();
                }
                if (arr[xy + 1] instanceof F) {
                    massParamNumbers[i * 2 + 1][j] =
                            ((F) arr[xy + 1]).valueOf(new Element[]{new NumberR64(tPer)}, ring).doubleValue();
                }
                tPer = tPer + (tMax - tMin) / (NumbOfSteps);
            }
        }
        MassParamVich = massParamNumbers;
        if (parcaPort) {
            paint(super.getGraphics());
        } else {
            setSize(1366, 700);
            setVisible(true);
        }
    }

    /**
     * Конструктор для постороения всего подряд
     *
     * @param pageObj
     * @param xMin
     * @param xMax
     * @param pageParamObj
     * @param NumbOfSteps
     * @param AvtoMatick
     * @param yMin
     * @param yMax
     * @param xR
     * @param yR
     * @param tMin
     * @param tMax
     * @throws Exception
     */
    public DirPan(Page pageObj, Page pageParamObj, double xMin, double xMax,
            double tMin, double tMax, double yMin, double yMax, double xR[][],
            double yR[][], boolean parcaPort,
            String x_str, String y_str, String title_str) {
        page = pageObj;
        pageParam = pageParamObj;
        this.parcaPort = parcaPort;
        this.x_str = x_str;
        this.y_str = y_str;
        this.title_str = title_str;
        this.xMin = xMin;
        this.xMax = xMax;
        if ((yMin == 0) & (yMax == 0)) {
            this.AvtoMatick = true;
        } else {
            this.AvtoMatick = false;
        }
        this.yMin = yMin;
        this.yMax = yMax;
        this.xR = xR;
        this.yR = yR;
        this.tMin = tMin;
        this.tMax = tMax;
        MassVich = funcToMass();
        MassParamVich = funcParamToMass();
        if (parcaPort) {
            paint(super.getGraphics());
        } else {
            setSize(1366, 700);
            setVisible(true);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------
    /**
     * Процедура вычисления значения функции, заданой аналитически
     *
     * @param x - значение x, для которого нам необходимо вычислить значение
     * F(x)
     * @param i - номер функции, для которой нам необходимо найти значение
     * @param page - список функций
     * @return y - Значение F(x)
     * @throws java.lang.Exception
     */
    public double VichFunc(double x, int i, Page page) {
        Ring ring = new Ring("R64[x]");
        Object obj = page.valueOf(page, i, new Element[]{new NumberR64(x)}, ring);
        if (obj instanceof F) {
            return ((F) page.valueOf(page, i, new Element[]{new NumberR64(x)}, ring)).doubleValue();//если результат F
        }
        return ((NumberR64) page.valueOf(page, i, new Element[]{new NumberR64(x)}, ring)).doubleValue();
    }

    /**
     * Процедура добавления в массив massOfNumbers значений функций
     *
     * @return massOfNumbers - массив значений функций
     * @throws java.lang.Exception
     */
    public final double[][] funcToMass() {
        double[][] massOfNumbers = new double[page.expr.size()][NumbOfSteps];
        double xPer;
        for (int i = 0; i <= page.expr.size() - 1; i++) {
            xPer = xMin;
            for (int j = 0; j <= NumbOfSteps - 1; j++) {
                massOfNumbers[i][j] = VichFunc(xPer, i, page);
                xPer = xPer + (xMax - xMin) / (NumbOfSteps);
            }
        }
        return massOfNumbers;
    }

    /**
     * Процедура вызова значений функций
     *
     * @param i - номер функции
     * @param j - значение в точке
     * @return MassVich - массив значений функций
     * @throws java.lang.Exception
     */
    public double funcToVich(int i, int j) throws Exception {
        return MassVich[i][j];
    }

    /**
     * Процедура вычисления значений параметрической функции
     *
     * @param t - значение t, для которого нам необходимо вычислить значение
     * X(t) или Y(t)
     * @param i - номер функции
     * @param pageParam - список функций
     * @return y - значение X(t) или Y(t)
     * @throws java.lang.Exception
     */
    public double VichFuncParam(double t, int i, Page pageParam) {
        Ring ring = new Ring("R64[x]");
        Object obj = pageParam.valueOf(pageParam, i,
                new Element[]{new NumberR64(t)}, ring);
        if (obj instanceof F) {
            return ((F) pageParam.valueOf(pageParam, i,
                    new Element[]{new NumberR64(t)}, ring)).doubleValue();
        }
        return ((NumberR64) (pageParam.valueOf(pageParam, i,
                new Element[]{new NumberR64(t)}, ring))).doubleValue();
    }

    /**
     * Процедура добавления в массив massParamNumbers значений функций
     *
     * @return massParamNumbers - массив значений параметрических функций вида
     * [i][j], где i - номер функции, j значение X(t), а j+1 значение Y(t)
     */
    public final double[][] funcParamToMass() {
        double[][] massParamNumbers = new double[this.pageParam.expr.size()][2 * NumbOfSteps];
        int xy;
        double tPer;
        for (int i = 0; i <= this.pageParam.expr.size() / 2 - 1; i++) {
            tPer = tMin;
            xy = i * 2;
            for (int j = 0; j <= 2 * NumbOfSteps - 1; j = j + 2) {
                massParamNumbers[i][j] = VichFuncParam(tPer, xy, pageParam);
                massParamNumbers[i][j + 1] = VichFuncParam(tPer, xy + 1, pageParam);
                tPer = tPer + (tMax - tMin) / (NumbOfSteps);
            }
        }

        return massParamNumbers;
    }

    /**
     * Процедура вызова значений функций
     *
     * @param i - номер функции
     * @param j - j значение X(t), а j+1 значение Y(t)
     * @return MassParamVich - массив значений функций
     * @throws java.lang.Exception
     */
    public double funcParamToVich(int i, int j) throws Exception {
        return MassParamVich[i][j];
    }

    /**
     * Процедура расчета коэффициэнта наклона прямой для графиков ф-ций заданных
     * таблицей значений
     *
     * @param i - номер строки таблицы
     * @param a1 - номер столбца таблицы, где храниться значение х1
     * @param a2 - номер столбца таблицы, где храниться значение х2
     * @return k1 - коэффициэнт наклона прямой
     */
    public double k(int i, int a1, int a2) {
        return (yR[i][a1] - yR[i][a2]) / (xR[i][a1] - xR[i][a2]);
    }

    /**
     * Процедура расчета сдвига прямой по оси ох для графиков ф-ций заданных
     * таблицей значений
     *
     * @param i - номер строки таблицы
     * @param a1 - номер столбца таблицы, где храниться значение х1
     * @param a2 - номер столбца таблицы, где храниться значение х2
     * @return B1 - сдвиг прямой по оси ох
     */
    public double L(int i, int a1, int a2) {
        return (xR[i][a1] * yR[i][a2] - yR[i][a1] * xR[i][a2])
                / (xR[i][a1] - xR[i][a2]);
    }

    /**
     * Основная процедура рисования
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        try {
            System.setProperty("java.awt.headless", "true");

            BufferedImage image = new BufferedImage(1366, 700, BufferedImage.TYPE_INT_RGB);
            if (parcaPort) {
                g = image.getGraphics();
            }
            //Ввод цветов для рисования графиков функций, если графиков
            //больше чем 6, то все остальные графики будут рисоваться темнокрасным цветом
            Color arr[] = new Color[101];
            arr[0] = new Color(255, 0, 0); //Красный
            arr[1] = new Color(255, 165, 2); //Оранжевый
            arr[2] = new Color(255, 50, 193); //Розовый
            arr[3] = new Color(0, 255, 0); //Зеленый
            arr[4] = new Color(135, 206, 250); //Голубой
            arr[5] = new Color(0, 0, 255); //Синий
            arr[6] = new Color(0, 0, 0); //Черный
            for (int i = 7; i <= 99; i++) {
                arr[i] = new Color(128, 0, 0); //Темнокрасный
            }
            arr[100] = new Color(255, 255, 255);
            g.setColor(arr[100]);
            g.fillRect(0, 0, 1366, 700);
            g.setColor(arr[6]);
            g.fillRect(370, 0, 5, 700);
            //Нахождение xStart,xEnd и yStart, yEnd, это параметры отвечающие
            //за автоматическое построение ф-ций
            xStart = xMin;
            xEnd = xMax;
            yStart = 0;
            yEnd = 0;
            double xMins = xMin;
            double xMaxs = xMax;

            //Нахождение максимального и минимального значения параметрических функций
            for (int i = 0; i <= f2.length - 1; i++) {
                for (int j = 0; j <= 2 * NumbOfSteps - 1; j = j + 2) {
                    double xST = funcParamToVich(i, j);
                    double yST = funcParamToVich(i, j + 1);
                    if (xST < xStart) {
                        xStart = xST;
                    }
                    if (xST > xEnd) {
                        xEnd = xST;
                    }
                    if (yST > yEnd) {
                        yEnd = yST;
                    }
                    if (yST < yStart) {
                        yStart = yST;
                    }
                }
            }

            //Нахождение максимального и минимального значения функций, заданных таблицей значений
            for (int i = 0; i <= xR.length - 1; i++) {
                for (int j = 0; j <= xR[i].length - 1; j++) {
                    double xST = xR[i][j];
                    double yST = yR[i][j];
                    if (xST < xStart) {
                        xStart = xST;
                    }
                    if (xST > xEnd) {
                        xEnd = xST;
                    }
                    if (yST > yEnd) {
                        yEnd = yST;
                    }
                    if (yST < yStart) {
                        yStart = yST;
                    }
                }
            }

            //Нахождение максимального и минимального значения функций заданых явно
            for (int i = 0; i <= f1.X.length - 1; i++) {
                for (int j = 0; j <= NumbOfSteps - 1; j++) {
                    double yST = funcToVich(i, j);
                    if (yST > yEnd) {
                        yEnd = yST;
                    }
                    if (yST < yStart) {
                        yStart = yST;
                    }
                }
            }

            //Если AvtoMatick = true, то вывод графиков идет в автоматическом режиме
            //При этом если значение по Y уходит на +? или на -?, то вывод графиков
            //мы ограничиваем промежутком от [-100,100].
            //Если AvtoMatick = false, то вывод графиков идет на промежутке,
            //по X [xMin,xMax], по Y [yMin,yMax], которые мы передаем в class DirPan.
            if (yStart < -100) {
                yStart = -100;
            }
            if (yEnd > 100) {
                yEnd = 100;
            }
            if (AvtoMatick) {
                yMin = yStart;
                yMax = yEnd;
                xMin = xStart;
                xMax = xEnd;
            }

            //Рисование системы координат
            if ((yMin > 0) && (yMax > 0)) {
                ycor = 500;
                zoom2 = Math.abs(400 / (yMax - yMin));
                yCorrect = (int) (yMin * zoom2);
            }
            if ((yMin <= 0) && (yMax >= 0)) {
                ycor = (int) (500 - 400 * Math.abs(yMin) / (yMax - yMin));
                zoom2 = Math.abs(400 / (yMax - yMin));
            }
            if ((yMin < 0) && (yMax < 0)) {
                ycor = 100;
                zoom2 = Math.abs(400 / (yMax - yMin));
                yCorrect = (int) (yMax * zoom2);
            }
            zoom1 = Math.abs(800 / (xMax - xMin));
            if ((xMin > 0) && (xMax > 0)) {
                xcor = 436;
                xCorrect = (int) (xMin * zoom1);
            }
            if (xMin == 0) {
                xcor = 436;
                g.drawString("0", xcor - 8, ycor + 15);
            }
            if (xMax == 0) {
                xcor = 1236;
                g.drawString("0", xcor - 8, ycor + 15);
            }
            if ((xMin < 0) && (xMax > 0)) {
                xcor = (int) (Math.abs(xMin) / (xMax - xMin) * 800 + 436);
                g.drawString("0", xcor - 8, ycor + 15);
            }
            if ((xMin < 0) && (xMax < 0)) {
                xcor = 1236;
                xCorrect = (int) (xMax * zoom1);
            }
            g.setColor(arr[6]);
            g.drawLine(xcor, 33, xcor, 540);
            g.drawLine(xcor, 30, xcor - 3, 45);
            g.drawLine(xcor, 30, xcor + 3, 45);
            g.drawLine(1336, ycor, 1321, ycor - 3);
            g.drawLine(1336, ycor, 1321, ycor + 3);
            g.drawLine(380, ycor, 1336, ycor);
            g.setColor(arr[1]);

            //Ввод интервала и других дополнительных переменных
            double a = xMins; //-10;
            double yk;
            double xk;
            double yp1;
            double yp2;
            double xp1;
            double xp2;
            double xppp;
            double yppp;
            double kk;
            double LL;
            double xgr = 436;
            int xviv = (int) xgr;
            double xkk;
            double xna;
            double yna;
            double yobr = yMax;
            //Вывод насечек на ось 0х
            for (int j = 0; j <= 20; j++) {
                xkk = xMin;
                g.setColor(arr[7]);
                g.drawLine(xviv, (ycor - 5), xviv, (ycor + 5));
                g.setColor(arr[5]);
                xkk = Math.round(1000 * (xkk + j * (xMax - xMin) / 20)) / 1000.0;
                if ((xkk) != 0) {
                    if (xkk == Math.round(xkk)) {
                        g.drawString(String.valueOf((int) xkk), (int) (xviv - 8), (ycor + 17));
                    } else {
                        g.drawString(String.valueOf(xkk), (int) (xviv - 8), (ycor + 17));
                    }
                }
                xviv += 40;
            }
            //Вывод насечек на ось 0у
            double yvv;
            for (int t = 0; t <= 20; t++) {
                g.setColor(arr[7]);
                g.drawLine((xcor - 5), (int) (t * 20 + 100), (xcor + 5), (int) (t * 20 + 100));
                g.setColor(arr[5]);
                if (yobr != 0) {
                    yvv = Math.round(1000 * (yobr)) / 1000.0;
                    if (yvv == Math.round(yvv)) {
                        g.drawString(String.valueOf((int) yvv), xcor - 30, (int) (t * 20 + 105));
                    } else {
                        g.drawString(String.valueOf(yvv), xcor - 30, (int) (t * 20 + 105));
                    }
                }
                yobr = yobr - (yMax - yMin) / 20;
            }
            ycor = ycor + yCorrect;
            xcor = xcor - xCorrect;
            //Вывод функций, заданной явно, на экран
            if (f1.X.length > 0) {
                for (int j = 0; j <= f1.X.length - 1; j++) {
                    xk = xMins;
                    for (int i = 0; i <= NumbOfSteps - 1; i++) {
                        yk = funcToVich(j, i);
                        xk = xk + (xMaxs - xMins) / NumbOfSteps;
                        g.setColor(arr[j]);
                        if ((yk <= yMax) && (yk >= yMin)) {
                            g.fillOval((int) (xk * zoom1 + xcor), (int) (-yk * zoom2 + ycor), 3, 3);
                        }
                    }
                    g.drawString("График ф-ции: " + this.y_str + " = " + f1.X[j].toString(), 20,
                            50 + (j + 1) * 20);
                }
            } else {
                if ((f1.name != F.ABS) && (f1.name != F.VECTORS)) {
                    xk = xMins;
                    for (int i = 0; i <= NumbOfSteps - 1; i++) {
                        yk = funcToVich(0, i);
                        xk = xk + (xMaxs - xMins) / NumbOfSteps;
                        g.setColor(arr[0]);
                        if ((yk <= yMax) && (yk >= yMin)) {
                            g.fillOval((int) (xk * zoom1 + xcor), (int) (-yk * zoom2 + ycor), 3, 3);
                        }
                    }
                    g.drawString("График ф-ции: " + this.y_str + " = " + f1.toString(), 20,
                            50 + (0 + 1) * 20);
                }
            }
            //Вывод функций, заданных параметрически, на экран
            int k = 0;
            for (int j = 0; j <= f2.length / 2 - 1; j++) {
                g.setColor(arr[j + f1.X.length]);
                g.drawString("График параметрической: " + this.x_str + " = "
                        + f2[k].toString()
                        + " " + this.y_str + " = " + f2[k + 1].toString(),
                        20, 50 + (f1.X.length + 1 + j) * 20);
                k = k + 2;
                for (int i = 0; i <= 2 * NumbOfSteps - 1; i++) {
                    xna = funcParamToVich(j * 2, i);
                    yna = funcParamToVich(j * 2 + 1, i);
                    if ((xna <= xMax) && (xna >= xMin)) {
                        if ((yna <= yMax) && (yna >= yMin)) {
                            g.fillOval((int) (xna * zoom1 + xcor), (int) (-yna * zoom2 + ycor), 3, 3);
                        }
                    }
                }
            }

            //Построение графиков функций заданных таблицей значений
            for (int j = 1; j <= xR.length; j++) {
                g.setColor(arr[j + f2.length + f1.X.length]);
                for (int i = 0; i <= xR[j - 1].length - 2; i++) {
                    xp1 = xR[j - 1][i];
                    xp2 = xR[j - 1][i + 1];
                    yp1 = yR[j - 1][i];
                    yp2 = yR[j - 1][i + 1];
                    xppp = Math.abs(xp2 - xp1);
                    yppp = Math.abs(yp2 - yp1);
                    kk = k(j - 1, i, i + 1);
                    LL = L(j - 1, i, i + 1);
                    for (int ii = 0; ii <= NumbOfSteps; ii++) {
                        if (xp2 == xp1) {
                            if (yp2 >= yp1) {
                                yp1 = yp1 + yppp / NumbOfSteps;
                            }
                            if (yp2 < yp1) {
                                yp1 = yp1 - yppp / NumbOfSteps;
                            }
                        }
                        if (xp2 != xp1) {
                            yp1 = kk * xp1 + LL;
                            if (xp2 > xp1) {
                                xp1 = xp1 + xppp / NumbOfSteps;
                            }
                            if (xp2 < xp1) {
                                xp1 = xp1 - xppp / NumbOfSteps;
                            }
                        }
                        if (xp1 <= xMax && xp1 >= xMin
                                && yp1 <= yMax && yp1 >= yMin) {
                            g.fillOval((int) (xp1 * zoom1 + xcor), (int) (-yp1 * zoom2 + ycor), 3, 3);
                        }
                    }
                }
            }
            //Вывод меток и подписей
            g.setColor(arr[5]);
            g.drawString(this.title_str, xcor + 10, 15);
            g.drawString(this.x_str, 1326, ycor + 15);
            g.drawString(this.y_str, xcor + 10, 45);
            if (xR.length > 0) {
                int yTl = 50 + (f1.X.length + f2.length) * 20;
                for (int countTableF = 1; countTableF <= xR.length; countTableF++) {
                    g.setColor(arr[f1.X.length + f2.length
                            + countTableF]);
                    g.drawString("Таблица: ", 20, yTl);
                    int xTl = 30;
                    for (int scb = 1; scb <= xR[countTableF - 1].length; scb++) {
                        if (xTl < 300) {
                            xTl = xTl + 60;
                        } else {
                            xTl = 90;
                            yTl = yTl + 20;
                        }
                        g.drawString("[" + (int) xR[countTableF - 1][scb - 1] + ","
                                + (int) yR[countTableF - 1][scb - 1] + "]", xTl, yTl);
                    }
                    yTl += 20;
                }
            }
            g.setColor(arr[5]);
            if (parcaPort) {
                page.putImage(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        ColorModel cm = image.getColorModel();
        WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
        boolean isRasterPremultiplied = cm.isAlphaPremultiplied();

        BufferedImage target = new BufferedImage(cm, raster, isRasterPremultiplied, null);
        Graphics2D g2 = target.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        double scalex = (double) target.getWidth() / image.getWidth();
        double scaley = (double) target.getHeight() / image.getHeight();

        AffineTransform xform = AffineTransform.getScaleInstance(scalex, scaley);
        g2.drawRenderedImage(image, xform);
        g2.dispose();
        return target;
    }
}
