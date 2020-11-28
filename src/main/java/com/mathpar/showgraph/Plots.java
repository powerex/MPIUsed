/**
 * Copyright © 2011 Mathparca Ltd. All rights reserved.
 */
package com.mathpar.showgraph;

import com.mathpar.func.*;
import com.mathpar.matrix.MatrixD;
import com.mathpar.matrix.Table;
import com.mathpar.number.Array;
import com.mathpar.number.Element;
import com.mathpar.number.Fraction;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
import com.mathpar.polynom.Polynom;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Программный класс для отрисовки графиков. 1) Графики явных функций. 2)
 * Графики параметрических функций. 3) Графики заданные табличными значениями.
 *
 * @author mixail
 */
public final class Plots extends Canvas {
    //Текстовая информация
    Element[][] s;
    /**
     * Длина засечки на осях
     */
    int lenRisk = 2;
    /**
     * Набор стилей для табличных графиков в showPlots
     */
    String[] arrStyleTheTablePlot;
    /**
     * Набор стилей для графиков заданных в явном виде в showPlots
     */
    String[] arrStyleThePlot;
    /**
     * Набор стилей для параметрических графиков в showPlots
     */
    String[] arrStyleTheParamPlot;
    /**
     * пунктирные линии для tablePlot
     */
    boolean DASH = false;
    /**
     * пунктирные линии и стрелки
     */
    boolean DASHANDARROW = false;
    /**
     * отвечает за отрисовку координытных осей для showPlot
     */
    boolean NOAXES = false;
    /**
     * решетка
     */
    boolean LATTICE = false;
    /**
     * равномерные отрезки по осям
     */
    boolean EQUALSCALE = false;
    /**
     * отрезок стрелка
     */
    boolean ARROW;
    Element[] pointsTextShow;
    Element[] pointsK1Show;
    Element[] pointsK2Show;
    /**
     * Размер шрифта
     */
    private int TEXTSIZE;
    /**
     * points флаг
     */
    private boolean pointPlotting;
    /**
     * подписи к точкам
     */
    private String[] pointsText;
    private Element[] pointsK1;
    private Element[] pointsK2;
    /**
     * коэффициент сдвига по сторонам света
     */
    private int[][] k1;
    /**
     * коэффициент сдвига по горизонтали
     */
    private int[] k2;
    /**
     * Массив значений для PointsPlot и TablePlot
     */
    private boolean[] arrayLineTheTablePlot;
    /**
     * Отрисовка соединения точек линиями для графиков заданных таблицей
     */
    private boolean lineTheTablePlot;
    /**
     * Кольцо для вывода(печати) функций (текстовая легенда)
     */
    private Ring captionRing;
    /**
     * Для отрисовки графа
     */
    private boolean PLOTGRAPH = false;
    //Цвет графиков по умолчанию
    private boolean BLACK_COLOR = false;
    /**
     * Значения (после запятой) для вывода числовых надписей по осям
     */
    private int[] FloatPosH;
    private int FloatPosHx;
    private int FloatPosHy;
    /**
     * Значение шага для отрисовки рисок по оси Oy
     */
    private double[] hOy;
    /**
     * Значение шага для отрисовки рисок по оси Ox
     */
    private double[] hOx;
    /**
     * Имена функций для showPlots
     */
    private String[] names = new String[] {};
    private double yDelta = 10;
    /**
     * Таблица входных значений Y
     */
    private double yPointFuncParam[][] = new double[][] {};
    /**
     * Таблица входных значений Y
     */
    private double yPointFunc[][] = new double[][] {};
    /**
     * Таблица входных значения X
     */
    private double xPointFunc[] = new double[] {};
    /**
     * Таблица входных значения X
     */
    private double xPointFuncShowPlots[][] = new double[][] {};
    /**
     * Массив функций заданных таблицами
     */
    Element[] arrFuncTable = new Element[0];
    /**
     * Массив функций заданных таблицами по точкам
     */
    Element[] arrFuncPointsTable = new Element[0];
    /**
     * Объект для отрисовки табличных графиков
     */
    private Table table;
    /**
     * Флаг отрисовки вертикальных линий на графике
     */
    private boolean drawLinesFlag = false;
    /**
     * Номера опций для построения явных, параметрических, заданных таблицей
     * функций и построения всех типов графиков
     */
    private boolean[] PLOTSNUMBER = new boolean[] {false, false, false, false, false};
    /**
     * Число дискретных значений аргумента
     */
    private int NumbOfSteps = 1000;
    /**
     * Левая граница интервала аргумента Т
     */
    private double tMin;
    /**
     * Правая граница интервала аргумента Т
     */
    private double tMax;
    /**
     * Массив параметрических функций
     */
    private Element[] arrFuncParam = new Element[0];
    /**
     * Массив значений параметрической функции, в столбцах номера функций, в
     * строках их значения
     */
    private double[][] MassParamVich;
    /**
     * Функция заданная явно
     */
    private F func = new F();
    /**
     * Если true - то переданы координаты графического контекста, false -
     * координаты системы
     */
    private boolean PIX = false;
    /**
     * Если true - то каждая точка на графике имеет подпись, false - подписей у
     * точек нет
     */
    private boolean CAPTIONS = false;
    /**
     * Толщина линий осей
     */
    private int WIDTH_AXES = 3;
    /**
     * Толщина линий для графика
     */
    private int WIDTH_LINE = 3;
    /**
     * Интервал нанесения на ось Ox подписей для насечек
     */
    private int intervalCaptionOx = 5;
    /**
     * Интервал нанесения на ось Oy подписей для насечек
     */
    private int intervalCaptionOy = 5;
    /**
     * Указанные номера графиков для отображения
     */
    private int[] pointer = new int[] {};
    /**
     * Наборы номеров графиков из набора данных
     */
    private Color[][] colors = new Color[][] {};
    /**
     * Наборы номеров графиков для отображения во 2 и 3 режиме
     */
    private int[][] numbers = new int[][] {};
    /**
     * Все цвета для отрисовки в режиме изображения всех табличных данных
     */
    private ArrayList<Color> allColors = new ArrayList<Color>();
    /**
     * Координаты вертикальных линий
     */
    private double[] pointLine;
    /**
     * Массив цветов для отрисовки графиков
     */
    private Color colorArray[] = new Color[5];
    /**
     * Входные данные
     */
    private ArrayList<ArrayList<Element>> arrPoint;
    /**
     * Режим отрисовки графиков функций заданных табличными значениями 0 - 1
     * режим, 1 - 2 режим, 3 - 3 режим
     */
    private int options = 0;
    /**
     * Значения номеров в наборе входных данных для построения графиков по Y
     */
    private int[] numberFunc;
    /**
     * Левая граница интервала аргумента Х
     */
    private double[] xMin;
    /**
     * Правая граница интервала аргумента Х
     */
    private double[] xMax;
    /**
     * Автоматический расчет интервала значений по осям
     */
    private boolean avtoMatick = false;
    /**
     * Автоматический расчет интервала значений по оси Y
     */
    private boolean avtoMatickY = false;
    /**
     * Нижняя граница интервала Y
     */
    private double[] yMin;
    /**
     * Верхняя граница интервала Y
     */
    private double[] yMax;
    /**
     * Таблица входных значений Y
     */
    private double yPoint[][] = new double[][] {};
    /**
     * Таблица входных значения X
     */
    private double xPoint[] = new double[] {};
    /**
     * Коэффициэнт увеличения(уменьшения) по Х
     */
    private double[] zoom1;
    /**
     * Коэффициэнт увеличения(уменьшения) по Y
     */
    private double[] zoom2; //
    /**
     * Координаты центра начала координат по X
     */
    private int[] xcor = new int[] {836};
    /**
     * Координаты центра начала координат по Y
     */
    private int[] ycor = new int[] {300};
    /**
     * Корректирующий коэффициент по Y
     */
    private int[] yCorrect = new int[] {0};
    /**
     * Корректирующий коэффициент по Х
     */
    private int[] xCorrect = new int[] {0};
    /**
     * Параметры автоматического построения графика
     */
    private double xStart = 0;
    private double xEnd = 0;
    private double yStart = 0;
    private double yEnd = 0;
    /**
     *
     */
    Page page;
    /**
     *
     */
    boolean forWeb = false;
    private static String path = System.getProperty("user.home") + "/image.png";;
    /**
     * Подпись оси oX
     */
    private String[] x_str;
    /**
     * Подпись оси oY
     */
    private String[] y_str;
    /**
     * Заголовки графиков
     */
    private String[] title_str;

    public Plots() {
    }

    /**
     * Конструктор для plotGraph
     *
     * @throws Exception
     */
    public Plots(Page page, boolean forWeb, Element[] funcTable) {
        this(page, forWeb, funcTable, null);
    }

    public Plots(Page page, boolean forWeb, Element[] funcTable, String path_not_used) {
        //this.path = path;
        this.page = page;
        this.forWeb = forWeb;
        PLOTGRAPH = true;
        PLOTSNUMBER[3] = true;
        CAPTIONS = true;
        this.options = 0;
        this.x_str = new String[] {page.nameOX};
        this.y_str = new String[] {page.nameOY};
        this.title_str = new String[] {page.title};
        this.xMin = new double[] {0};
        this.xMax = new double[] {0};
        this.yMin = new double[] {0};
        this.yMax = new double[] {0};
        this.hOx = new double[1];
        this.hOy = new double[1];
        this.zoom1 = new double[1];
        this.zoom2 = new double[1];
        this.FloatPosH = new int[1];
        arrFuncTable = funcTable;
        setColorArray();
        paint(super.getGraphics());
    }

    /**
     * Установка опций для графика: черный цвет графика, равный масштаб, размер
     * шрифта, толщина осей, толщина линий графика
     */
    public void setOptions(Page p) {
        double[] a = p.optionsPlot;
        if (a != null) {
            BLACK_COLOR = (a[0] != 0);
            EQUALSCALE = (a[1] != 0);
            TEXTSIZE = (int) a[2];
            WIDTH_LINE = (int) a[3];
            WIDTH_AXES = (int) a[4];
        }
    }

    /**
     * Конструктор для showPlots
     *
     * @param xMin
     * @param xMax
     * @param yMin
     * @param yMax
     * @param tMin
     * @param tMax
     * @param xR
     * @param yR
     *
     * @throws Exception
     */
    public Plots(Page page, F func, Element[] funcParam, Element[] funcTable, double xMin, double xMax, double yMin,
            double yMax, double tMin, double tMax, boolean forWeb, Element[] arrParam, String[] names,
            boolean[] arrLineTheTablePlot, Element[] funcPointsTable, Element[] pointsText,
            Element[] pointsK1, Element[] pointsK2, String noAxes, String lattice,
            String[] arrStyleTheTablePlot, String[] arrStyleThePlot, String[] arrStyleTheParamPlot, Element[] tParamMin, Element[] tParamMax, Element[] plotParamX, Element[] plotParamY, Element[][] s) {
        this(page, func, funcParam, funcTable, xMin, xMax, yMin, yMax, tMin, tMax, forWeb, arrParam, names, null, arrLineTheTablePlot, funcPointsTable, pointsText,
                pointsK1, pointsK2, noAxes, lattice, arrStyleTheTablePlot, arrStyleThePlot, arrStyleTheParamPlot, tParamMin, tParamMax, plotParamX, plotParamY, s);
    }

    public Plots(Page page, F func, Element[] funcParam, Element[] funcTable, double xMin, double xMax, double yMin,
            double yMax, double tMin, double tMax, boolean forWeb, Element[] arrPointsParam, String[] names, String path_not_used,
            boolean[] arrLineTheTablePlot, Element[] funcPointsTable, Element[] text, Element[] k1,
            Element[] k2, String noAxess, String lattice, String[] arrStyleTheTablePlot, String[] arrStyleThePlot, String[] arrStyleTheParamPlot, Element[] tParamMin, Element[] tParamMax, Element[] plotParamX, Element[] plotParamY, Element[][] s) {
        Ring ring = new Ring("R64[x]");
        this.s = s;
        if (lattice.equals("")) {
            LATTICE = false;
        } else {
            LATTICE = true;
        }
        if (noAxess.equals("")) {
            NOAXES = false;
        } else {
            NOAXES = true;
        }
        if (funcPointsTable.length != 0) {
            pointPlotting = true;
        }
        pointsTextShow = text;
        pointsK1Show = k1;
        pointsK2Show = k2;
        this.arrayLineTheTablePlot = arrLineTheTablePlot;

        this.arrStyleTheTablePlot = arrStyleTheTablePlot;
        this.arrStyleTheParamPlot = arrStyleTheParamPlot;
        this.arrStyleThePlot = arrStyleThePlot;

        if (page.optionsPlot != null) {
            setOptions(page);
        } else {
            WIDTH_LINE = arrPointsParam[arrPointsParam.length - 2].intValue();
            WIDTH_AXES = arrPointsParam[arrPointsParam.length - 1].intValue();
            BLACK_COLOR = (arrPointsParam[arrPointsParam.length - 5].intValue() == 0) ? false : true;
            EQUALSCALE = (arrPointsParam[arrPointsParam.length - 4].intValue() == 0) ? false : true;
            TEXTSIZE = arrPointsParam[arrPointsParam.length - 3].intValue();
        }

        if (page.option.equals("ES")) {
            EQUALSCALE = true;
        }
        if (page.option.equals("BW")) {
            BLACK_COLOR = true;
        }
        if (page.option.equals("BWES") || page.option.equals("ESBW")) {
            EQUALSCALE = true;
            BLACK_COLOR = true;
        }
        //this.path = path;
        this.page = page;
        this.forWeb = forWeb;
        this.names = names;
        PLOTSNUMBER[3] = true;
        this.options = 0;
        this.x_str = new String[] {page.nameOX};
        this.y_str = new String[] {page.nameOY};
        this.title_str = new String[] {page.title};
        this.xMin = new double[] {xMin};
        this.xMax = new double[] {xMax};
        this.yMin = new double[] {yMin};
        this.yMax = new double[] {yMax};
        this.hOx = new double[1];
        this.hOy = new double[1];
        this.zoom1 = new double[1];
        this.zoom2 = new double[1];
        this.FloatPosH = new int[1];
        this.tMin = tMin;
        this.tMax = tMax;
        this.func = func;
        NumbOfSteps = 1000;
        yPointFunc = new double[this.func.X.length][NumbOfSteps];
        xPointFuncShowPlots = new double[this.func.X.length][NumbOfSteps];
        double xPer;
        if (this.func.X.length == 1) {//случай когда явная функция одна
            if (!arrStyleThePlot[0].equals("")) {
                if (arrStyleThePlot[0].equals("arrow")) {
                    this.ARROW = true;
                    NumbOfSteps = 1000;
                }
                if (arrStyleThePlot[0].equals("dash")) {
                    this.DASH = true;
                    NumbOfSteps = 1000;
                }
                if (arrStyleThePlot[0].equals("dashAndArrow")) {
                    this.DASHANDARROW = true;
                    NumbOfSteps = 1000;
                }
            }
            yPointFunc = new double[this.func.X.length][NumbOfSteps];
            xPointFuncShowPlots = new double[this.func.X.length][NumbOfSteps];
            xPer = xMin;
            if (plotParamX[0] != null) {
                xPer = plotParamX[0].value;
            }
            xPointFuncShowPlots[0][0] = xPer;
            if (arrPointsParam != null) {
                Element[] arr = new Element[arrPointsParam.length - 3 + 1];
                System.arraycopy(arrPointsParam, 0, arr, 1, (arrPointsParam.length - 3));
                arr[0] = new NumberR64(xPer);
                yPointFunc[0][0] = this.func.X[0].value(arr, page.ring).doubleValue();
            } else {
                yPointFunc[0][0] = this.func.X[0].value(new Element[] {new NumberR64(xPer)}, ring).doubleValue();
            }
            if (this.func.X[0].isItNumber()) {
                yPointFunc[0][0] = this.func.X[0].doubleValue();
            }
            for (int j = 1; j <= NumbOfSteps - 1; j++) {
                if (plotParamX[0] != null) {
                    xMin = plotParamX[0].value;
                    xMax = plotParamY[0].value;
                }
                xPer = xPer + (xMax - xMin) / (NumbOfSteps);
                xPointFuncShowPlots[0][j] = xPer;
                if (arrPointsParam != null) {
                    Element[] arr = new Element[arrPointsParam.length - 3 + 1];
                    System.arraycopy(arrPointsParam, 0, arr, 1, (arrPointsParam.length - 3));
                    arr[0] = new NumberR64(xPer);
                    yPointFunc[0][j] = ((F) this.func).X[0].value(arr, page.ring).doubleValue();
                } else {
                    yPointFunc[0][j] = ((F) this.func).X[0].value(new Element[] {new NumberR64(xPer)}, ring).doubleValue();
                }
                if (this.func.X[0].isItNumber()) {
                    yPointFunc[0][j] = this.func.X[0].doubleValue();
                }
            }
        } else {//случай когда у нас вектор явных функций
            for (int i = 0; i <= this.func.X.length - 1; i++) {
                xMin = this.xMin[0];
                xMax = this.xMax[0];
                if (!arrStyleThePlot[0].equals("")) {
                    if (arrStyleThePlot[i].equals("arrow")) {
                        this.ARROW = true;
                        NumbOfSteps = 1000;
                    }
                    if (arrStyleThePlot[i].equals("dash")) {
                        this.DASH = true;
                        NumbOfSteps = 1000;
                    }
                    if (arrStyleThePlot[i].equals("dashAndArrow")) {
                        this.DASHANDARROW = true;
                        NumbOfSteps = 1000;
                    }
                }
                yPointFunc[i] = new double[NumbOfSteps];
                xPointFuncShowPlots[i] = new double[NumbOfSteps];
                xPer = xMin;
                if (plotParamX.length != 1) {
                    if (plotParamX[i] != null) {
                        xPer = plotParamX[i].value;
                    }
                }
                xPointFuncShowPlots[i][0] = xPer;
                if (arrPointsParam != null) {
                    Element[] arr = new Element[arrPointsParam.length - 3 + 1];
                    System.arraycopy(arrPointsParam, 0, arr, 1, (arrPointsParam.length - 3));
                    arr[0] = new NumberR64(xPer);
                    yPointFunc[i][0] = this.func.X[i].value(arr, page.ring).doubleValue();
                } else {
                    yPointFunc[i][0] = this.func.X[i].value(new Element[] {new NumberR64(xPer)}, ring).doubleValue();
                }
                if (this.func.X[i].isItNumber()) {
                    yPointFunc[i][0] = this.func.X[i].doubleValue();
                }
                for (int j = 1; j <= NumbOfSteps - 1; j++) {
                    if (plotParamX.length != 1) {
                        if (plotParamX[i] != null) {
                            xMin = plotParamX[i].value;
                            xMax = plotParamY[i].value;
                        }
                    }
                    xPer = xPer + (xMax - xMin) / (NumbOfSteps);
                    xPointFuncShowPlots[i][j] = xPer;
                    if (arrPointsParam != null) {
                        Element[] arr = new Element[arrPointsParam.length - 3 + 1];
                        System.arraycopy(arrPointsParam, 0, arr, 1, (arrPointsParam.length - 3));
                        arr[0] = new NumberR64(xPer);
                        yPointFunc[i][j] = this.func.X[i].value(arr, page.ring).doubleValue();
                    } else {
                        yPointFunc[i][j] = this.func.X[i].value(new Element[] {new NumberR64(xPer)}, ring).doubleValue();
                    }
                    if (this.func.X[i].isItNumber()) {
                        yPointFunc[i][j] = this.func.X[i].doubleValue();
                    }
                }
            }
        }
        this.arrFuncParam = funcParam;
        NumbOfSteps = 1000;
        yPointFuncParam = new double[funcParam.length][2 * NumbOfSteps];
        int xy;
        double tPer;
        Element[] arr = new Element[page.ring.varNames.length];
        for (int num = 1; num < arr.length; num++) {
            arr[num] = new NumberR64(0);
        }
        for (int i = 0; i <= funcParam.length / 2 - 1; i++) {
            if (!arrStyleTheParamPlot[i].equals("")) {
                if (arrStyleTheParamPlot[i].equals("arrow")) {
                    this.ARROW = true;
                    NumbOfSteps = 1000;
                }
                if (arrStyleTheParamPlot[i].equals("dash")) {
                    this.DASH = true;
                    NumbOfSteps = 1000;
                }
                if (arrStyleTheParamPlot[i].equals("dashAndArrow")) {
                    this.DASHANDARROW = true;
                    NumbOfSteps = 1000;
                }
            }
            tPer = tParamMin[i].value;//tMin;
            xy = i * 2;
            for (int j = 0; j <= 2 * NumbOfSteps - 1; j++) {
                if (arrPointsParam != null) {
                    arr[0] = new NumberR64(tPer);
                    Element elF = funcParam[xy].value(arr, page.ring);
                    if (elF instanceof Fname) {
                        yPointFuncParam[i * 2][j] = ((Fname) elF).X[0].doubleValue();
                    } else {
                        yPointFuncParam[i * 2][j]
                                = elF.doubleValue();
                    }
                } else {
                    Element elF = funcParam[xy].value(new Element[] {new NumberR64(tPer)}, ring);
                    if (elF instanceof Fname) {
                        yPointFuncParam[i * 2][j] = ((Fname) elF).X[0].doubleValue();
                    } else {
                        yPointFuncParam[i * 2][j]
                                = elF.doubleValue();
                    }
                }
                if (arrPointsParam != null) {
                    arr[0] = new NumberR64(tPer);
                    Element elF = funcParam[xy + 1].value(arr, page.ring);
                    if (elF instanceof Fname) {
                        yPointFuncParam[i * 2 + 1][j] = ((Fname) elF).X[0].doubleValue();
                    } else {
                        yPointFuncParam[i * 2 + 1][j]
                                = elF.doubleValue();
                    }
                } else {
                    Element elF = funcParam[xy + 1].value(new Element[] {new NumberR64(tPer)}, ring);
                    if (elF instanceof Fname) {
                        yPointFuncParam[i * 2 + 1][j] = ((Fname) elF).X[0].doubleValue();
                    } else {
                        yPointFuncParam[i * 2 + 1][j]
                                = elF.doubleValue();
                    }
                }
                tPer = tPer + (tParamMax[i].value - tParamMin[i].value) / (2 * NumbOfSteps);//tPer + (tMax - tMin) / (2 * NumbOfSteps);
            }
        }
        arrFuncTable = funcTable;
        arrFuncPointsTable = funcPointsTable;
        setColorArray();
        paint(super.getGraphics());
    }

    /**
     * Конструктор для textPlot
     *
     * @param page
     * @param s
     * @param forWeb
     * @param ring1
     * @param arrPointsParam
     *
     * @throws Exception
     */
    public Plots(Page page, Element[][] s, boolean forWeb, Ring ring1, Element[] arrPointsParam) throws Exception {
        this(page, s, forWeb, ring1, arrPointsParam, null);
    }

    public Plots(Page page, Element[][] s, boolean forWeb, Ring ring1, Element[] arrPointsParam, String path_not_used) throws Exception {
        NumbOfSteps = 1000;
        this.s = s;
        //this.path = path;
        this.page = page;
        if (page.optionsPlot != null) {
            setOptions(page);
        } else {
            WIDTH_LINE = arrPointsParam[arrPointsParam.length - 2].intValue();
            WIDTH_AXES = arrPointsParam[arrPointsParam.length - 1].intValue();
            BLACK_COLOR = (arrPointsParam[arrPointsParam.length - 5].intValue() == 0) ? false : true;
            EQUALSCALE = (arrPointsParam[arrPointsParam.length - 4].intValue() == 0) ? false : true;
            TEXTSIZE = arrPointsParam[arrPointsParam.length - 3].intValue();
        }
        if (page.option.equals("ES")) {
            EQUALSCALE = true;
        }
        if (page.option.equals("BW")) {
            BLACK_COLOR = true;
        }
        if (page.option.equals("BWES") || page.option.equals("ESBW")) {
            EQUALSCALE = true;
            BLACK_COLOR = true;
        }
        PLOTSNUMBER[4] = true;
        this.forWeb = forWeb;
        this.x_str = new String[] {page.nameOX};
        this.y_str = new String[] {page.nameOY};
        this.title_str = new String[] {page.title};
        this.xMin = new double[] {page.xMin};
        this.xMax = new double[] {page.xMax};
        this.yMin = new double[] {page.yMin};
        this.yMax = new double[] {page.yMax};
        this.hOx = new double[1];
        this.hOy = new double[1];
        this.zoom1 = new double[1];
        this.zoom2 = new double[1];
        this.FloatPosH = new int[1];
        setColorArray();
        paint(super.getGraphics());
    }

    /**
     * Конструктор для явных функций
     *
     * @param pageObj
     * @param xMin
     * @param xMax
     * @param yMin
     * @param yMax
     *
     * @throws Exception
     */
    public Plots(Page page, F func, boolean forWeb, Ring ring1, Element[] arrPointsParam, String style) throws Exception {
        this(page, func, forWeb, ring1, arrPointsParam, null, style);
    }

    public Plots(Page page, F func, boolean forWeb, Ring ring1, Element[] arrPointsParam, String path_not_used, String style) throws Exception {
        NumbOfSteps = 1000;
        if (!style.equals("")) {
            if (style.equals("arrow")) {
                this.ARROW = true;
                NumbOfSteps = 400;
            }
            if (style.equals("dash")) {
                this.DASH = true;
                NumbOfSteps = 400;
            }
            if (style.equals("dashAndArrow")) {
                this.DASHANDARROW = true;
                NumbOfSteps = 400;
            }
        }
        //this.path = path;
        this.page = page;
        if (page.optionsPlot != null) {
            setOptions(page);
        } else {
            WIDTH_LINE = arrPointsParam[arrPointsParam.length - 2].intValue();
            WIDTH_AXES = arrPointsParam[arrPointsParam.length - 1].intValue();
            BLACK_COLOR = (arrPointsParam[arrPointsParam.length - 5].intValue() == 0) ? false : true;
            EQUALSCALE = (arrPointsParam[arrPointsParam.length - 4].intValue() == 0) ? false : true;
            TEXTSIZE = arrPointsParam[arrPointsParam.length - 3].intValue();
        }
        if (page.option.equals("ES")) {
            EQUALSCALE = true;
        }
        if (page.option.equals("BW")) {
            BLACK_COLOR = true;
        }
        if (page.option.equals("BWES") || page.option.equals("ESBW")) {
            EQUALSCALE = true;
            BLACK_COLOR = true;
        }
        PLOTSNUMBER[0] = true;

        Ring ring = new Ring("R64[x]");
        this.forWeb = forWeb;
        this.x_str = new String[] {page.nameOX};
        this.y_str = new String[] {page.nameOY};
        this.title_str = new String[] {page.title};
        this.xMin = new double[] {page.xMin};
        this.xMax = new double[] {page.xMax};
        this.yMin = new double[] {page.yMin};
        this.yMax = new double[] {page.yMax};
        this.hOx = new double[1];
        this.hOy = new double[1];
        this.zoom1 = new double[1];
        this.zoom2 = new double[1];
        this.FloatPosH = new int[1];
        this.func = func;
        yPoint = new double[this.func.X.length][NumbOfSteps];
        xPoint = new double[NumbOfSteps];
        double xPer;
        for (int i = 0; i <= this.func.X.length - 1; i++) {
            xPer = page.xMin;
            xPoint[0] = xPer;
            if (arrPointsParam != null) {
                Element[] arr = new Element[arrPointsParam.length - 3 + 1];
                System.arraycopy(arrPointsParam, 0, arr, 1, (arrPointsParam.length - 3));
                arr[0] = new NumberR64(xPer);
                yPoint[i][0] = this.func.X[i].value(arr, ring1).doubleValue();
            } else {
                yPoint[i][0] = this.func.X[i].value(new Element[] {new NumberR64(xPer)}, ring).doubleValue();
            }
            for (int j = 1; j <= NumbOfSteps - 1; j++) {
                xPer = xPer + (page.xMax - page.xMin) / (NumbOfSteps);
                xPoint[j] = xPer;
                if (arrPointsParam != null) {
                    Element[] arr = new Element[arrPointsParam.length - 3 + 1];
                    System.arraycopy(arrPointsParam, 0, arr, 1, (arrPointsParam.length - 3));
                    arr[0] = new NumberR64(xPer);
                    yPoint[i][j] = this.func.X[i].value(arr, ring1).doubleValue();
                } else {
                    yPoint[i][j] = this.func.X[i].value(new Element[] {new NumberR64(xPer)}, ring).doubleValue();
                }
            }
        }
        setColorArray();
        paint(super.getGraphics());
    }

    /**
     * Конструктор для явных функций
     *
     * @param pageObj
     * @param xMin
     * @param xMax
     * @param yMin
     * @param yMax
     *
     * @throws Exception
     */
    public Plots(Page page, Element func, double xMin, double xMax, boolean forWeb,
            Ring ring1, Element[] arrPointsParam, String style) throws Exception {
        this(page, func, xMin, xMax, forWeb, ring1, arrPointsParam, null, style);
    }

    public Plots(Page page, Element func, double xMin, double xMax, boolean forWeb,
            Ring ring1, Element[] arrPointsParam, String path_not_used, String style) throws Exception {
        NumbOfSteps = 1000;
        if (!style.equals("")) {
            if (style.equals("arrow")) {
                this.ARROW = true;
                NumbOfSteps = 400;
            }
            if (style.equals("dash")) {
                this.DASH = true;
                NumbOfSteps = 400;
            }
            if (style.equals("dashAndArrow")) {
                this.DASHANDARROW = true;
                NumbOfSteps = 400;
            }
        }
        this.page = page;
        //this.path = path;
        if (page.optionsPlot != null) {
            setOptions(page);
        } else {
            WIDTH_LINE = arrPointsParam[arrPointsParam.length - 2].intValue();
            WIDTH_AXES = arrPointsParam[arrPointsParam.length - 1].intValue();
            BLACK_COLOR = (arrPointsParam[arrPointsParam.length - 5].intValue() == 0) ? false : true;
            EQUALSCALE = (arrPointsParam[arrPointsParam.length - 4].intValue() == 0) ? false : true;
            TEXTSIZE = arrPointsParam[arrPointsParam.length - 3].intValue();
        }
        if (page.option.equals("ES")) {
            EQUALSCALE = true;
        }
        if (page.option.equals("BW")) {
            BLACK_COLOR = true;
        }
        if (page.option.equals("BWES") || page.option.equals("ESBW")) {
            EQUALSCALE = true;
            BLACK_COLOR = true;
        }
        PLOTSNUMBER[0] = true;

        xPoint = new double[NumbOfSteps];
        Ring ring = new Ring("R64[x]");
        double delta = (xMax - xMin) * 5;
        double a0 = -delta;
        double a1 = delta;
        boolean fl0 = false;
        boolean fl1 = false;
        this.forWeb = forWeb;
        this.x_str = new String[] {page.nameOX};
        this.y_str = new String[] {page.nameOY};
        this.title_str = new String[] {page.title};
        this.xMin = new double[] {xMin};
        this.xMax = new double[] {xMax};
        this.yMin = new double[] {0};
        this.yMax = new double[] {0};
        this.FloatPosH = new int[1];
        double y0 = 0;
        double y1 = 0;
        if ((func instanceof F) && (((F) func).name != F.VECTORS)) {
            this.func = new F(F.VECTORS, func);
        } else if (func instanceof VectorS) {
            this.func = new F(F.VECTORS, ((VectorS) func).V);
        } else {
            this.func = (F) func;
        }
        yPoint = new double[this.func.X.length][NumbOfSteps];
        double xPer;
        for (int i = 0; i <= this.func.X.length - 1; i++) {
            xPer = xMin;
            xPoint[0] = xPer;
            if (arrPointsParam != null) {
                Element[] arr = new Element[arrPointsParam.length - 3 + 1];
                System.arraycopy(arrPointsParam, 0, arr, 1, (arrPointsParam.length - 3));
                arr[0] = new NumberR64(xPer);
                yPoint[i][0] = this.func.X[i].value(arr, ring1).doubleValue();
            } else {
                yPoint[i][0] = this.func.X[i].value(new Element[] {new NumberR64(xPer)}, ring1).doubleValue();
            }
            if (yPoint[i][0] > y1) {
                y1 = yPoint[i][0];
            }
            if (yPoint[i][0] > a1) {
                fl1 = true;
                yPoint[i][0] = a1 * 100;
            }
            if (yPoint[i][0] < y0) {
                y0 = yPoint[i][0];
            }
            if (yPoint[i][0] < a0) {
                fl0 = true;
                yPoint[i][0] = a0 * 100;
            }
            for (int j = 1; j <= NumbOfSteps - 1; j++) {
                xPer = xPer + (xMax - xMin) / (NumbOfSteps);
                xPoint[j] = xPer;
                if (arrPointsParam != null) {
                    Element[] arr = new Element[arrPointsParam.length - 3 + 1];
                    System.arraycopy(arrPointsParam, 0, arr, 1, (arrPointsParam.length - 3));
                    arr[0] = new NumberR64(xPer);
                    yPoint[i][j] = this.func.X[i].value(arr, ring1).doubleValue();
                } else {
                    yPoint[i][j] = this.func.X[i].value(new Element[] {new NumberR64(xPer)}, ring1).doubleValue();
                }
                if (yPoint[i][j] > y1) {
                    y1 = yPoint[i][j];
                }
                if (yPoint[i][j] > a1) {
                    fl1 = true;
                    yPoint[i][j] = a1 * 100;
                }
                if (yPoint[i][j] < y0) {
                    y0 = yPoint[i][j];
                }
                if (yPoint[i][j] < a0) {
                    fl0 = true;
                    yPoint[i][j] = a0 * 100;
                }
            }
        }
        this.yMin[0] = (fl0) ? a0 : y0;
        this.yMax[0] = (fl1) ? a1 : y1;
        this.hOx = new double[1];
        this.hOy = new double[1];
        this.zoom1 = new double[1];
        this.zoom2 = new double[1];
        setColorArray();
        paint(super.getGraphics());
    }

    /**
     * конструктор для параметрических ф-ций
     *
     * @param tMin
     * @param tMax
     * @param yMin
     * @param yMax
     * @param pageObj
     *
     * @throws Exception
     */
    public Plots(Page page, Element[] arrFuncParam, double xMin, double xMax, double yMin, double yMax,
            double tMin, double tMax, boolean forWeb,
            Ring ring1, Element[] param, String style) throws Exception {
        this(page, arrFuncParam, xMin, xMax, yMin, yMax, tMin, tMax, forWeb, ring1, param, null, style);
    }

    public Plots(Page page, Element[] arrFuncParam, double xMin, double xMax, double yMin, double yMax,
            double tMin, double tMax, boolean forWeb,
            Ring ring1, Element[] arrPointsParam, String path_not_used, String style) throws Exception {
        NumbOfSteps = 1000;
        if (!style.equals("")) {
            if (style.equals("arrow")) {
                this.ARROW = true;
                NumbOfSteps = 1000;
            }
            if (style.equals("dash")) {
                this.DASH = true;
                NumbOfSteps = 1000;
            }
            if (style.equals("dashAndArrow")) {
                this.DASHANDARROW = true;
                NumbOfSteps = 1000;
            }
        }
        captionRing = ring1;
        Ring ring = new Ring("R64[x]");
        if (page.optionsPlot != null) {
            setOptions(page);
        } else {
            WIDTH_LINE = arrPointsParam[arrPointsParam.length - 2].intValue();
            WIDTH_AXES = arrPointsParam[arrPointsParam.length - 1].intValue();
            BLACK_COLOR = (arrPointsParam[arrPointsParam.length - 5].intValue() == 0) ? false : true;
            EQUALSCALE = (arrPointsParam[arrPointsParam.length - 4].intValue() == 0) ? false : true;
            TEXTSIZE = arrPointsParam[arrPointsParam.length - 3].intValue();
        }
        if (page.option.equals("ES")) {
            EQUALSCALE = true;
        }
        if (page.option.equals("BW")) {
            BLACK_COLOR = true;
        }
        if (page.option.equals("BWES") || page.option.equals("ESBW")) {
            EQUALSCALE = true;
            BLACK_COLOR = true;
        }
        PLOTSNUMBER[1] = true;
        avtoMatick = true;

        this.forWeb = forWeb;
        //this.path = path;
        this.x_str = new String[] {page.nameOX};
        this.y_str = new String[] {page.nameOY};
        this.title_str = new String[] {page.title};
        this.yMin = new double[] {yMin};
        this.yMax = new double[] {yMax};
        this.tMin = tMin;
        this.tMax = tMax;
        this.xMin = new double[] {xMin};
        this.xMax = new double[] {xMax};
        this.FloatPosH = new int[1];
        this.arrFuncParam = arrFuncParam;
        this.page = page;
        yPoint = new double[arrFuncParam.length][2 * NumbOfSteps];
        int xy;
        //счетчик
        double tPer;
        Element[] arr = new Element[page.ring.varNames.length];
        for (int num = 1; num < arr.length; num++) {
            arr[num] = new NumberR64(0);
        }
        ring = page.ring;
        for (int i = 0; i <= arrFuncParam.length / 2 - 1; i++) {
            tPer = tMin;
            xy = i * 2;
            for (int j = 0; j <= 2 * NumbOfSteps - 1; j++) {
                if (arrFuncParam[xy] instanceof Polynom) {
                    if (arrPointsParam != null) {
                        arr[0] = new NumberR64(tPer);
                        yPoint[i][j] = ((Polynom) arrFuncParam[xy]).value(arr, ring).doubleValue();
                    } else {
                        yPoint[i][j] = ((Polynom) arrFuncParam[xy]).value(new Element[] {new NumberR64(tPer)}, ring).doubleValue();
                    }
                } else {
                    if (arrFuncParam[xy] instanceof Fraction) {
                        if (arrPointsParam != null) {
                            arr[0] = new NumberR64(tPer);
                            yPoint[i][j] = ((Fraction) arrFuncParam[xy]).value(arr, ring).doubleValue();
                        } else {
                            yPoint[i][j] = ((Fraction) arrFuncParam[xy]).value(new Element[] {new NumberR64(tPer)}, ring).doubleValue();
                        }
                    } else {
                        if (arrFuncParam[xy] instanceof F) {
                            if (((F) arrFuncParam[xy]).name == 0) {
                                if (((F) arrFuncParam[xy]).X[0] instanceof Fname) {
                                    Element a = ((Fname) ((F) arrFuncParam[xy]).X[0]).X[0];
                                    if (((F) a).name != 0) {
                                        if (arrPointsParam != null) {
                                            arr[0] = new NumberR64(tPer);
                                            yPoint[i][j] = ((F) a).valueOf(arr, ring).doubleValue();
                                        } else {
                                            yPoint[i][j] = ((F) a).valueOf(new Element[] {new NumberR64(tPer)}, ring).doubleValue();

                                        }
                                    } else {
                                        if (arrPointsParam != null) {
                                            arr[0] = new NumberR64(tPer);
                                            yPoint[i][j] = ((Polynom) ((F) a).X[0]).value(arr, ring).doubleValue();
                                        } else {
                                            yPoint[i][j] = ((Polynom) ((F) a).X[0]).value(new Element[] {new NumberR64(tPer)}, ring).doubleValue();
                                        }
                                    }
                                }
                            } else {
                                if (arrPointsParam != null) {
                                    arr[0] = new NumberR64(tPer);
                                    yPoint[i][j] = ((F) arrFuncParam[xy]).valueOf(arr, ring).doubleValue();
                                } else {
                                    yPoint[i][j] = ((F) arrFuncParam[xy]).valueOf(new Element[] {new NumberR64(tPer)}, ring).doubleValue();
                                }
                            }
                        } else {
                            yPoint[i][j] = (arrFuncParam[xy]).doubleValue();
                        }
                    }
                }
                if (arrFuncParam[xy + 1] instanceof Polynom) {
                    if (arrPointsParam != null) {
                        arr[0] = new NumberR64(tPer);
                        yPoint[xy + 1][j] = ((Polynom) arrFuncParam[xy + 1]).value(arr, ring).doubleValue();
                    } else {
                        yPoint[xy + 1][j] = ((Polynom) arrFuncParam[xy + 1]).value(new Element[] {new NumberR64(tPer)}, ring).doubleValue();
                    }
                } else {
                    if (arrFuncParam[xy + 1] instanceof Fraction) {
                        if (arrPointsParam != null) {
                            arr[0] = new NumberR64(tPer);
                            yPoint[xy + 1][j] = ((Fraction) arrFuncParam[xy + 1]).value(arr, ring).doubleValue();
                        } else {
                            yPoint[xy + 1][j] = ((Fraction) arrFuncParam[xy + 1]).value(new Element[] {new NumberR64(tPer)}, ring).doubleValue();
                        }
                    } else {
                        if (arrFuncParam[xy + 1] instanceof F) {
                            if (((F) arrFuncParam[xy + 1]).name == 0) {
                                if (((F) arrFuncParam[xy + 1]).X[0] instanceof Fname) {
                                    Element a = ((Fname) ((F) arrFuncParam[xy + 1]).X[0]).X[0];
                                    if (((F) a).name != 0) {
                                        if (arrPointsParam != null) {
                                            arr[0] = new NumberR64(tPer);
                                            yPoint[xy + 1][j] = ((F) a).valueOf(arr, ring).doubleValue();
                                        } else {
                                            yPoint[xy + 1][j] = ((F) a).valueOf(new Element[] {new NumberR64(tPer)}, ring).doubleValue();

                                        }
                                    } else {
                                        if (arrPointsParam != null) {
                                            arr[0] = new NumberR64(tPer);
                                            yPoint[xy + 1][j] = ((Polynom) ((F) a).X[0]).value(arr, ring).doubleValue();
                                        } else {
                                            yPoint[xy + 1][j] = ((Polynom) ((F) a).X[0]).value(new Element[] {new NumberR64(tPer)}, ring).doubleValue();
                                        }
                                    }
                                }
                            } else {
                                if (arrPointsParam != null) {
                                    arr[0] = new NumberR64(tPer);
                                    yPoint[xy + 1][j] = ((F) arrFuncParam[xy + 1]).valueOf(arr, ring).doubleValue();
                                } else {
                                    yPoint[xy + 1][j] = ((F) arrFuncParam[xy + 1]).valueOf(new Element[] {new NumberR64(tPer)}, ring).doubleValue();
                                }
                            }
                        } else {
                            yPoint[xy + 1][j] = (arrFuncParam[xy + 1]).doubleValue();
                        }
                    }
                }
                tPer = tPer + (tMax - tMin) / (2 * NumbOfSteps);
            }
        }
        this.hOx = new double[1];
        this.hOy = new double[1];
        this.zoom1 = new double[1];
        this.zoom2 = new double[1];
        setColorArray();
        paint(super.getGraphics());
    }

    /**
     * Заполнение и установка векторов с подписями осей
     */
    public void setAxesSign() {
        String[] axesSign = table.axesSign;
        switch (options) {
            case 0: {
                if (axesSign != null) {
                    if (axesSign.length == 1) {
                        this.x_str = new String[] {table.axesSign[0]};
                        this.y_str = new String[] {page.nameOY};
                    } else {
                        this.x_str = new String[] {table.axesSign[0]};
                        this.y_str = new String[] {table.axesSign[1]};
                    }
                } else {
                    this.x_str = new String[] {page.nameOX};
                    this.y_str = new String[] {page.nameOY};
                }
                break;
            }
        }
    }

    /**
     * Определяет автоматический расчет границ построения графика
     *
     * @return - true - автомат, false - границы установленные \set2D()
     */
    public boolean isAutomatCalculation() {
        if (page.xMin == 0 && page.xMax == 0 && page.yMin == 0 && page.yMax == 0) {
            return true;
        }
        return false;
    }

    /**
     * Метод установки параметров границ
     */
    public void setMaxAndMinRegion() {
        NumberR64[] region = table.region;
        if (region != null) {
            if (region.length == 0) {
                this.avtoMatick = true;
                setOptions(0, 0, 0, 0);
            } else {
                if (region.length == 2) {
                    this.avtoMatick = false;
                    this.avtoMatickY = true;
                    setOptions(region[0].doubleValue(), region[1].doubleValue(), 0, 0);
                } else {
                    if (region.length == 4) {
                        this.avtoMatick = false;
                        setOptions(region[0].doubleValue(), region[1].doubleValue(), region[2].doubleValue(), region[3].doubleValue());
                    }
                }
            }
        } else {
            if (page.xMin == 0 && page.xMax == 0 && page.yMin == 0 && page.yMax == 0) {
                this.avtoMatick = true;
            } else {
                this.avtoMatick = false;//true
            }
            setOptions(page.xMin, page.xMax, page.yMin, page.yMax);
        }
    }

    /**
     * Конструктор класса для построения графиков функций заданных с помощью
     * объектов Table.
     *
     * @param page
     * @param tableEl - объект - Таблица данных для графика
     */
    public Plots(Page page, boolean forweb, Element tableEl, Element[] arrParamRegion, int options, boolean lineDrow, Element[] arrPointsParam, String style) {
        this(page, forweb, tableEl, arrParamRegion, options, null, lineDrow, arrPointsParam, style);
    }

    public Plots(Page page, boolean forweb, Element tableEl, Element[] arrParamRegion, int options, String path_not_used, boolean lineDrow, Element[] arrPointsParam, String style) {
        if (!style.equals("")) {
            if (style.equals("arrow")) {
                this.ARROW = true;
            }
            if (style.equals("dash")) {
                this.DASH = true;
            }
            if (style.equals("dashAndArrow")) {
                this.DASHANDARROW = true;
            }
        }

        this.lineTheTablePlot = lineDrow;
        if (page.optionsPlot != null) {
            setOptions(page);
        } else {
            WIDTH_LINE = arrPointsParam[arrPointsParam.length - 2].intValue();
            WIDTH_AXES = arrPointsParam[arrPointsParam.length - 1].intValue();
            BLACK_COLOR = (arrPointsParam[arrPointsParam.length - 5].intValue() == 0) ? false : true;
            EQUALSCALE = (arrPointsParam[arrPointsParam.length - 4].intValue() == 0) ? false : true;
            TEXTSIZE = arrPointsParam[arrPointsParam.length - 3].intValue();
        }
        if (page.option.equals("ES")) {
            EQUALSCALE = true;
        }
        if (page.option.equals("BW")) {
            BLACK_COLOR = true;
        }
        if (page.option.equals("BWES") || page.option.equals("ESBW")) {
            EQUALSCALE = true;
            BLACK_COLOR = true;
        }

        this.x_str = new String[] {page.nameOX};
        this.y_str = new String[] {page.nameOY};
        this.title_str = new String[] {page.title};
        this.xMin = new double[] {page.xMin};
        this.xMax = new double[] {page.xMax};
        this.yMin = new double[] {page.yMin};
        this.yMax = new double[] {page.yMax};

        //this.path = path;
        this.page = page;
        this.forWeb = forweb;
        this.options = options;
        PLOTSNUMBER[2] = true;
        this.table = (Table) tableEl;
        setAxesSign();
        this.pointer = new int[] {};
        this.arrPoint = setArraysPoints(table);
        if (table.P != null) {
            this.PIX = true;
            pointLine = new double[table.P.M[0].length];
            for (int i = 0; i < pointLine.length; i++) {
                if (table.P.M[0][i] != null) {
                    pointLine[i] = table.P.M[0][i].doubleValue();
                }
            }
            this.avtoMatickY = true;
            this.drawLinesFlag = true;
            setOptions(table.region[0].doubleValue(), table.region[1].doubleValue(), table.region[2].doubleValue(), table.region[3].doubleValue());
            //установка опций по умолчанию
            options = -1;
        } else {
            if (arrParamRegion.length != 0) {
                this.avtoMatickY = false;
                this.avtoMatick = false;
                setOptions(arrParamRegion[0].doubleValue(), arrParamRegion[1].doubleValue(), arrParamRegion[2].doubleValue(), arrParamRegion[3].doubleValue());
            } else {
                this.avtoMatickY = false;
                this.pointLine = new double[] {};
                setMaxAndMinRegion();
            }
        }
        setXPoint(this.arrPoint.get(0));
        setYPoint();
        setColorArray();
        paint(super.getGraphics());
    }

    /**
     * Конструктор класса для построения графиков по точкам
     *
     * @param page
     * @param tableEl - объект - Таблица данных для графика
     */
    public Plots(Page page, boolean forweb, Element tableEl, Element[] arrParamRegion, int options, boolean lineDrow, Element[] arrPointsParam, Element[] text, Element[] k1, Element[] k2) {
        this(page, forweb, tableEl, arrParamRegion, options, null, lineDrow, arrPointsParam, text, k1, k2);
    }

    public Plots(Page page, boolean forweb, Element tableEl, Element[] arrParamRegion, int options, String path_not_used, boolean lineDrow, Element[] arrPointsParam, Element[] text, Element[] k1, Element[] k2) {
        this.lineTheTablePlot = lineDrow;
        if (page.optionsPlot != null) {
            setOptions(page);
        } else {
            WIDTH_LINE = arrPointsParam[arrPointsParam.length - 2].intValue();
            WIDTH_AXES = arrPointsParam[arrPointsParam.length - 1].intValue();
            BLACK_COLOR = (arrPointsParam[arrPointsParam.length - 5].intValue() == 0) ? false : true;
            EQUALSCALE = (arrPointsParam[arrPointsParam.length - 4].intValue() == 0) ? false : true;
            TEXTSIZE = arrPointsParam[arrPointsParam.length - 3].intValue();
        }
        if (page.option.equals("ES")) {
            EQUALSCALE = true;
        }
        if (page.option.equals("BW")) {
            BLACK_COLOR = true;
        }
        if (page.option.equals("BWES") || page.option.equals("ESBW")) {
            EQUALSCALE = true;
            BLACK_COLOR = true;
        }
        this.pointPlotting = true;
        if (text != null) {
            pointsText = new String[text.length];
            for (int i = 0; i < text.length; i++) {
                if (text[i] instanceof F) {
                    if (((F) text[i]).X[0] instanceof Fname) {
                        pointsText[i] = ((Fname) ((F) text[i]).X[0]).name;
                    } else {
                        if (((F) text[i]).X[0] instanceof F) {
                            if (((F) ((F) text[i]).X[0]).X[0] instanceof Fname) {
                                pointsText[i] = ((Fname) ((F) ((F) text[i]).X[0]).X[0]).name;
                            }
                        } else {
                            pointsText[i] = ((Fname) ((F) text[i]).X[0]).name;
                        }
                    }
                } else {
                    pointsText[i] = ((Fname) ((F) text[i]).X[0]).name;
                }
            }
        }
        this.pointsK1 = k1;
        this.pointsK2 = k2;
        if (this.pointsK1 == null) {
            this.k1 = new int[2][];
            this.k1[0] = new int[((Table) tableEl).M.M[0].length];
            this.k1[1] = new int[((Table) tableEl).M.M[0].length];
        } else {
            this.k1 = new int[2][];
            this.k1[0] = new int[pointsK1.length];
            this.k1[1] = new int[pointsK1.length];
        }
        if (this.pointsK2 == null) {
            this.k2 = new int[((Table) tableEl).M.M[0].length];
        } else {
            this.k2 = new int[pointsK2.length];
        }

        this.x_str = new String[] {page.nameOX};
        this.y_str = new String[] {page.nameOY};
        this.title_str = new String[] {page.title};
        this.xMin = new double[] {page.xMin};
        this.xMax = new double[] {page.xMax};
        this.yMin = new double[] {page.yMin};
        this.yMax = new double[] {page.yMax};

        CAPTIONS = true;
      //  this.path = path;
        this.page = page;
        this.forWeb = forweb;
        this.options = options;
        PLOTSNUMBER[2] = true;
        this.table = (Table) tableEl;
        setAxesSign();
        this.pointer = new int[] {};
        this.arrPoint = setArraysPoints(table);
        if (table.P != null) {
            this.PIX = true;
            pointLine = new double[table.P.M[0].length];
            for (int i = 0; i < pointLine.length; i++) {
                if (table.P.M[0][i] != null) {
                    pointLine[i] = table.P.M[0][i].doubleValue();
                }
            }
            this.avtoMatickY = true;
            this.drawLinesFlag = true;
            setOptions(table.region[0].doubleValue(), table.region[1].doubleValue(), table.region[2].doubleValue(), table.region[3].doubleValue());
            //установка опций по умолчанию
            options = -1;
        } else {
            if (arrParamRegion.length != 0) {
                this.avtoMatickY = false;
                this.avtoMatick = false;
                setOptions(arrParamRegion[0].doubleValue(), arrParamRegion[1].doubleValue(), arrParamRegion[2].doubleValue(), arrParamRegion[3].doubleValue());
            } else {
                this.avtoMatickY = false;
                this.pointLine = new double[] {};
                setMaxAndMinRegion();
            }
        }
        setXPoint(this.arrPoint.get(0));
        setYPoint();
        setColorArray();
        paint(super.getGraphics());
    }

    /**
     * Создание наборов значений
     *
     * @param t - объект "Таблица"
     */
    public void setXYPoints(Table t) {
        ArrayList<ArrayList<Element>> result = new ArrayList<ArrayList<Element>>();
        ArrayList<Element> arg = new ArrayList<Element>();
        MatrixD tArg = t.M;
        for (int i = 0; i < tArg.M[0].length; i++) {
            arg.add(tArg.M[0][i]);
        }
        setXPoint(arg);
        result.add(arg);
        for (int j = 1; j < tArg.M.length; j++) {
            ArrayList<Element> f = new ArrayList<Element>();
            for (int z = 0; z < tArg.M[j].length; z++) {
                f.add(tArg.M[j][z]);
            }
            result.add(f);
        }
        int k = 0;
        this.yPoint = new double[result.size() - 1][];
        for (int i = 1; i < result.size(); i++) {
            yPoint[k] = new double[result.get(i).size()];
            for (int j = 0; j < result.get(i).size(); j++) {
                this.yPoint[k][j] = result.get(i).get(j).doubleValue();
            }
            k++;
        }
    }

    /**
     * Создание наборов значений
     *
     * @param t - объект "Таблица"
     */
    public ArrayList<ArrayList<Element>> setArraysPoints(Table t) {
        ArrayList<ArrayList<Element>> result = new ArrayList<ArrayList<Element>>();
        ArrayList<Element> arg = new ArrayList<Element>();
        MatrixD tArg = t.M;
        for (int i = 0; i < tArg.M[0].length; i++) {
            arg.add(tArg.M[0][i]);
        }
        result.add(arg);
        for (int j = 1; j < tArg.M.length; j++) {
            ArrayList<Element> f = new ArrayList<Element>();
            for (int z = 0; z < tArg.M[j].length; z++) {
                f.add(tArg.M[j][z]);
            }
            result.add(f);
        }
        return result;
    }

    /**
     * Метод установки значений интервалов нанесения на ось Ox и Oy подписей для
     * насечек
     */
    public void setIntervalCaptionOxOy(int x, int y) {
        this.intervalCaptionOx = x;
        this.intervalCaptionOy = y;
    }

    /**
     * Установка опций для 1 режима работы. Когда происходит отрисовка всего
     * набора Y
     *
     * @param xMin - Min знач сетки по оси X
     * @param xMax - Max знач сетки по оси X
     * @param yMin - Min знач сетки по оси Y
     * @param yMax - Max знач сетки по оси Y
     */
    public void setAllOptionsStepOne(double xMin, double xMax,
            double yMin, double yMax) {
        this.options = -1;
        this.xMin = new double[] {xMin};
        this.xMax = new double[] {xMax};
        this.yMin = new double[] {yMin};
        this.yMax = new double[] {yMax};
        this.hOx = new double[1];
        this.hOy = new double[1];
        this.zoom1 = new double[1];
        this.zoom2 = new double[1];
        this.FloatPosH = new int[1];
        setNumberFunc();
    }

    /**
     * Установка опций отрисовки
     *
     * @param xMin - Min знач сетки по оси X
     * @param xMax - Max знач сетки по оси X
     * @param yMin - Min знач сетки по оси Y
     * @param yMax - Max знач сетки по оси Y
     */
    public void setOptions(double xMin, double xMax,
            double yMin, double yMax) {
        switch (options) {
            case 0: {
                setAllOptionsStepOne(xMin, xMax, yMin, yMax);
                break;
            }
        }
    }

    /**
     * Метод установки номеров тех наборов по Y которые будут изображены
     */
    public void setNumberFunc() {
        this.numberFunc = new int[arrPoint.size() - 1];
        for (int i = 0; i < arrPoint.size() - 1; i++) {
            this.numberFunc[i] = i;
        }
    }

    /**
     * Метод задания массива цветов
     */
    public void setColorArray() {
        colorArray[0] = new Color(255, 0, 0); //Красный
        colorArray[1] = new Color(0, 255, 0); //Зеленый
        colorArray[2] = new Color(0, 0, 255); //Синий
        colorArray[3] = new Color(0, 0, 0); //Черный
        colorArray[4] = new Color(255, 255, 255); //Белый
    }

    /**
     * Метод задания координат построения X
     *
     * @param x - Входные данные
     */
    public void setXPoint(ArrayList<Element> x) {
        this.xPoint = new double[x.size()];
        for (int i = 0; i < xPoint.length; i++) {
            this.xPoint[i] = x.get(i).doubleValue();
        }
    }

    /**
     * Метод задания координат построения Y
     */
    public void setYPoint() {
        this.yPoint = new double[arrPoint.size() - 1][];
        for (int i = 0; i < arrPoint.size() - 1; i++) {
            yPoint[i] = new double[arrPoint.get(i + 1).size()];
            for (int j = 0; j < arrPoint.get(i + 1).size(); j++) {
                this.yPoint[i][j] = arrPoint.get(i + 1).get(j).doubleValue();
            }
        }
    }

    /**
     * Метод отрисовки системы координат для 1 режима
     *
     * @param g - Графический контекст
     */
    public void drawSystemCor(Graphics g) {
        for (int i = 0; i <= options; i++) {
            //Рисование системы координат
            if ((yMin[i] > 0) && (yMax[i] > 0)) {
                ycor[i] = 500;
                zoom2[i] = Math.abs(400 / (yMax[i] - yMin[i]));
                yCorrect[i] = (int) (yMin[i] * zoom2[i]);
            }
            if ((yMin[i] <= 0) && (yMax[i] >= 0)) {
                ycor[i] = (int) (500 - 400 * Math.abs(yMin[i]) / (yMax[i] - yMin[i]));
                zoom2[i] = Math.abs(400 / (yMax[i] - yMin[i]));
            }
            if ((yMin[i] < 0) && (yMax[i] < 0)) {
                ycor[i] = 100;
                zoom2[i] = Math.abs(400 / (yMax[i] - yMin[i]));
                yCorrect[i] = (int) (yMax[i] * zoom2[i]);
            }
            zoom1[i] = Math.abs(800 / (xMax[i] - xMin[i]));
            if ((xMin[i] > 0) && (xMax[i] > 0)) {
                xcor[i] = 436;
                xCorrect[i] = (int) (xMin[i] * zoom1[i]);
            }
            if (xMin[i] == 0) {
                xcor[i] = 436;
                if (!PLOTGRAPH) {
                    if (NOAXES == false) {
                        g.drawString("0", xcor[i] - 8, ycor[i] + 15);
                    }
                }
            }
            if (xMax[i] == 0) {
                xcor[i] = 1236;
                if (!PLOTGRAPH) {
                    if (NOAXES == false) {
                        g.drawString("0", xcor[i] - 8, ycor[i] + 15);
                    }
                }
            }
            if ((xMin[i] < 0) && (xMax[i] > 0)) {
                xcor[i] = (int) (Math.abs(xMin[i]) / (xMax[i] - xMin[i]) * 800 + 336);//436
                if (!PLOTGRAPH) {
                    if (NOAXES == false) {
                        g.drawString("0", xcor[i] - 8, ycor[i] + 15);
                    }
                }
            }
            if ((xMin[i] < 0) && (xMax[i] < 0)) {
                xcor[i] = 1236;
                xCorrect[i] = (int) (xMax[i] * zoom1[i]);
            }
            g.setColor(colorArray[3]);
            if (!PLOTGRAPH) {
                if (NOAXES == false) {
                    g.drawLine(xcor[i], 33, xcor[i], 540);
                    g.drawLine(xcor[i], 30, xcor[i] - 3, 45);
                    g.drawLine(xcor[i], 30, xcor[i] + 3, 45);
                    g.drawLine(1336, ycor[i], 1321, ycor[i] - 3);
                    g.drawLine(1336, ycor[i], 1321, ycor[i] + 3);
                    g.drawLine(280, ycor[i], 1336, ycor[i]);
                }
            }
            g.setColor(colorArray[0]);
        }
    }

    /**
     * Пересчет max и min границ по оси Ox Исходя из новой концепции отрисовки
     * "рисок"
     *
     */
    public void initIntervalOx(Graphics g) {
        for (int i = 0; i <= options; i++) {
            double xStart = 0;
            double xEnd = 0;
            double x;
            //Шаг по оси Ox
            if (xMax[i] - xMin[i] != 0) {
                hOx[i] = roundScale((xMax[i] - xMin[i]) / 20)[0];
                FloatPosH[i] = (int) roundScale((xMax[i] - xMin[i]) / 20)[1];
                FloatPosHx = (int) roundScale((xMax[i] - xMin[i]) / 20)[1];
            } else {
                hOx[i] = roundScale(xMax[i] / 20)[0];
                FloatPosH[i] = (int) roundScale(xMax[i] / 20)[1];
                FloatPosHx = (int) roundScale(xMax[i] / 20)[1];
            }
//            if(yMax[i] - yMin[i] != 0){
//                FloatPosH[i] = (int) roundScale((yMax[i] - yMin[i]) / 20)[1];
//            }else{
//               FloatPosH[i] = (int) roundScale(yMax[i] / 20)[1];
//            }
            //Случай когда весь график от -Ox до +Ox
            if (xMax[i] > 0 && xMin[i] < 0) {
                double x1 = 0;
                int j = 0;
                while (x1 > xMin[i]) {
                    x = 0;
                    if (j != 0) {
                        x1 = x - j * hOx[i];
                    }
                    j++;
                }
                //Новый xMin
                xStart = x1;
                double x2 = 0;
                int w = 0;
                while (x2 < xMax[i]) {
                    x = 0;
                    if (w != 0) {
                        x2 = x + w * hOx[i];
                    }
                    w++;
                }
                //Новый xMax
                xEnd = x2;
            }
            //Случай когда весь график в +Ox
            if (xMax[i] > 0 && xMin[i] >= 0) {
                double x1 = 0;
                int j = 0;
                boolean f = true;
                while (x1 <= xMax[i]) {//<
                    x = 0;
                    if (j != 0) {
                        x1 = x + j * hOx[i];
                        if (x1 >= xMin[i]) {//>
                            if (f) {
                                //Новый xMin
                                xStart = x1 - hOx[i];
                            }
                            f = false;
                        }
                    }
                    j++;
                }
                //Новый xMax
                xEnd = x1;
            }
            //Случай когда весь график в -Ox
            if (xMax[i] <= 0 && xMin[i] < 0) {
                double x1 = 0;
                int j = 0;
                boolean f = true;
                while (x1 > xMin[i]) {
                    x = 0;
                    if (j != 0) {
                        x1 = x - j * hOx[i];
                        if (x1 < xMax[i]) {
                            if (f) {
                                xEnd = x1 + hOx[i];
                            }
                            f = false;
                        }
                    }
                    j++;
                }
                xStart = x1;
            }
            xMax[i] = xEnd;
            xMin[i] = xStart;
        }
    }

    /**
     * Пересчет max и min границ по оси Oy Исходя из новой концепции отрисовки
     * "рисок"
     *
     */
    public void initIntervalOy(Graphics g) {
        for (int i = 0; i <= options; i++) {
            double yStart = 0;
            double yEnd = 0;
            double y;
            //Шаг по оси Oy
            if (yMax[i] - yMin[i] != 0) {
                hOy[i] = roundScale((yMax[i] - yMin[i]) / 20)[0];
                FloatPosH[i] = (int) roundScale((yMax[i] - yMin[i]) / 20)[1];
                FloatPosHy = (int) roundScale((yMax[i] - yMin[i]) / 20)[1];
            } else {
                hOy[i] = roundScale(yMax[i] / 20)[0];
                FloatPosH[i] = (int) roundScale(yMax[i] / 20)[1];
                FloatPosHy = (int) roundScale(yMax[i] / 20)[1];
            }
            //Вывод насечек на ось 0х
            if (yMax[i] > 0 && yMin[i] < 0) {
                double y1 = 0;
                int j = 0;
                while (y1 > yMin[i]) {
                    y = 0;
                    if (j != 0) {
                        y1 = y - j * hOy[i];
                    }
                    j++;
                }
                yStart = y1;
                double y2 = 0;
                int w = 0;
                while (y2 < yMax[i]) {
                    y = 0;
                    if (w != 0) {
                        y2 = y + w * hOy[i];
                    }
                    w++;
                }
                yEnd = y2;
            }
            if (yMax[i] > 0 && yMin[i] >= 0) {
                double y1 = 0;
                int j = 0;
                boolean f = true;
                while (y1 <= yMax[i]) {//<
                    y = 0;
                    if (j != 0) {
                        y1 = y + j * hOy[i];
                        if (y1 >= yMin[i]) {//>
                            if (f) {
                                yStart = y1 - hOy[i];
                            }
                            f = false;
                        }
                    }
                    j++;
                }
                yEnd = y1;
            }
            if (yMax[i] <= 0 && yMin[i] < 0) {
                double y1 = 0;
                int j = 0;
                boolean f = true;
                while (y1 > yMin[i]) {
                    y = 0;
                    if (j != 0) {
                        y1 = y - j * hOy[i];
                        if (y1 < yMax[i]) {
                            if (f) {
                                yEnd = y1 + hOy[i];
                            }
                            f = false;
                        }
                    }
                    j++;
                }
                yStart = y1;
            }
            yMax[i] = yEnd;
            yMin[i] = yStart;
        }
    }

    /**
     * Вывод насечек по оси Ox
     *
     * @param g - графический контекст
     */
    public void drawOx(Graphics g) {
        int oldFloatpos = page.ring.FLOATPOS;
        for (int i = 0; i <= options; i++) {
            //page.ring.FLOATPOS = FloatPosH[i];
            page.ring.FLOATPOS = FloatPosHx;
            int valX;
            double x;
            //Âûâîä íàñå÷åê íà îñü 0õ
            if (xMax[i] > 0 && xMin[i] < 0) {
                double x1 = 0;
                int j = 0;
                while (x1 > xMin[i]) {
                    x = 0;
                    g.setColor(colorArray[3]);
                    if (j != 0) {
                        x1 = x - j * hOx[i];
                        valX = (int) (x1 * zoom1[i] + xcor[i]);
                        if (!PLOTGRAPH) {
                            if (NOAXES == false) {
                                if (LATTICE == true) {
                                    ((Graphics2D) g).setStroke(new BasicStroke(1));
                                    int lenRiskUp = (int) (yMax[0] * zoom2[0] - yCorrect[0]);
                                    int lenRiskDown = (int) (yMin[0] * zoom2[0] - yCorrect[0]);
                                    g.drawLine(valX, (ycor[i] - lenRiskUp), valX, (ycor[i] - lenRiskDown));
                                } else {
                                    g.drawLine(valX, (ycor[i] - lenRisk), valX, (ycor[i] + lenRisk));
                                }
                            }
                        }
                        if (BLACK_COLOR) {
                            g.setColor(colorArray[3]);
                        } else {
                            g.setColor(colorArray[2]);
                        }
                        if (j % intervalCaptionOx == 0) {
                            if (!PLOTGRAPH) {
                                if (NOAXES == false) {
                                    g.drawString(new NumberR64(x1).toString(page.ring), (int) (valX - 8), (ycor[i] + 17));
                                }
                            }
                        }
                    }
                    j++;
                }
                double x2 = 0;
                int w = 0;
                while (x2 < xMax[i]) {
                    x = 0;
                    g.setColor(colorArray[3]);
                    if (w != 0) {
                        x2 = x + w * hOx[i];
                        valX = (int) (x2 * zoom1[i] + xcor[i]);
                        if (!PLOTGRAPH) {
                            if (NOAXES == false) {
                                if (LATTICE == true) {
                                    ((Graphics2D) g).setStroke(new BasicStroke(1));
                                    int lenRiskUp = (int) (yMax[0] * zoom2[0] - yCorrect[0]);
                                    int lenRiskDown = (int) (yMin[0] * zoom2[0] - yCorrect[0]);
                                    g.drawLine(valX, (ycor[i] - lenRiskUp), valX, (ycor[i] - lenRiskDown));
                                } else {
                                    g.drawLine(valX, (ycor[i] - lenRisk), valX, (ycor[i] + lenRisk));
                                }
                            }
                        }
                        if (BLACK_COLOR) {
                            g.setColor(colorArray[3]);
                        } else {
                            g.setColor(colorArray[2]);
                        }
                        if (w % intervalCaptionOx == 0) {
                            if (!PLOTGRAPH) {
                                if (NOAXES == false) {
                                    g.drawString(new NumberR64(x2).toString(page.ring), (int) (valX - 8), (ycor[i] + 17));
                                }
                            }
                        }
                    }
                    w++;
                }
            }
            //Случай когда весь график в +Ox
            if (xMax[i] > 0 && xMin[i] >= 0) {
                double x1 = 0;
                int j = 0;
                double step = xcor[i];
                if (xMin[i] != 0) {
                    step = xcor[i] - xCorrect[i];
                }
                while (x1 < xMax[i]) {
                    x = xMin[i];
                    g.setColor(colorArray[3]);
                    x1 = x + j * hOx[i];
                    valX = (int) (x1 * zoom1[i] + step);
                    if (!PLOTGRAPH) {
                        if (NOAXES == false) {
                            if (LATTICE == true) {
                                ((Graphics2D) g).setStroke(new BasicStroke(1));
                                int lenRiskUp = (int) (yMax[0] * zoom2[0] - yCorrect[0]);
                                int lenRiskDown = (int) (yMin[0] * zoom2[0] - yCorrect[0]);
                                g.drawLine(valX, (ycor[i] - lenRiskUp), valX, (ycor[i] - lenRiskDown));
                            } else {
                                g.drawLine(valX, (ycor[i] - lenRisk), valX, (ycor[i] + lenRisk));
                            }
                        }
                    }
                    if (BLACK_COLOR) {
                        g.setColor(colorArray[3]);
                    } else {
                        g.setColor(colorArray[2]);
                    }
                    if (j % intervalCaptionOx == 0) {
                        if (!PLOTGRAPH) {
                            if (NOAXES == false) {
                                g.drawString(new NumberR64(x1).toString(page.ring), (int) (valX - 8), (ycor[i] + 17));
                            }
                        }
                    }
                    j++;
                }
            }
            if (xMax[i] <= 0 && xMin[i] < 0) {
                double x1 = 0;
                int j = 0;
                double step = xcor[i];
                if (xMax[i] != 0) {
                    step = xcor[i] - xCorrect[i];
                }
                while (x1 > xMin[i]) {
                    x = xMax[i];
                    g.setColor(colorArray[3]);
                    x1 = x - j * hOx[i];
                    valX = (int) (x1 * zoom1[i] + step);
                    if (!PLOTGRAPH) {
                        if (NOAXES == false) {
                            if (LATTICE == true) {
                                ((Graphics2D) g).setStroke(new BasicStroke(1));
                                int lenRiskUp = (int) (yMax[0] * zoom2[0] - yCorrect[0]);
                                int lenRiskDown = (int) (yMin[0] * zoom2[0] - yCorrect[0]);
                                g.drawLine(valX, (ycor[i] - lenRiskUp), valX, (ycor[i] - lenRiskDown));
                            } else {
                                g.drawLine(valX, (ycor[i] - lenRisk), valX, (ycor[i] + lenRisk));
                            }
                        }
                    }
                    if (BLACK_COLOR) {
                        g.setColor(colorArray[3]);
                    } else {
                        g.setColor(colorArray[2]);
                    }
                    if (j % intervalCaptionOx == 0) {
                        if (!PLOTGRAPH) {
                            if (NOAXES == false) {
                                g.drawString(new NumberR64(x1).toString(page.ring), (int) (valX - 8), (ycor[i] + 17));
                            }
                        }
                    }
                    j++;
                }
            }
        }
        page.ring.FLOATPOS = oldFloatpos;
        //   ((Graphics2D) g).setStroke(new BasicStroke(WIDTH_LINE));
    }

    /**
     * Вывод насечек по оси Ox
     *
     */
    public void drawOy(Graphics g) {
        int oldFloatpos = page.ring.FLOATPOS;
        for (int i = 0; i <= options; i++) {
            //page.ring.FLOATPOS = FloatPosH[i];
            page.ring.FLOATPOS = FloatPosHy;
            int valY;
            double y;
            //Âûâîä íàñå÷åê íà îñü 0õ
            if (yMax[i] > 0 && yMin[i] < 0) {
                double y1 = 0;
                int j = 0;
                while (y1 > yMin[i]) {
                    y = 0;
                    g.setColor(colorArray[3]);
                    if (j != 0) {
                        y1 = y - j * hOy[i];
                        valY = (int) (-y1 * zoom2[i] + ycor[i]);
                        if (!PLOTGRAPH) {
                            if (NOAXES == false) {
                                if (LATTICE == true) {
                                    ((Graphics2D) g).setStroke(new BasicStroke(1));
                                    int lenRiskUp = (int) (xMax[0] * zoom1[0] - xCorrect[0]);
                                    int lenRiskDown = (int) (xMin[0] * zoom1[0] - xCorrect[0]);
                                    g.drawLine(xcor[i] + lenRiskDown, valY, xcor[i] + lenRiskUp, valY);
                                } else {
                                    g.drawLine(xcor[i] - lenRisk, valY, xcor[i] + lenRisk, valY);
                                }
                            }
                        }
                        if (BLACK_COLOR) {
                            g.setColor(colorArray[3]);
                        } else {
                            g.setColor(colorArray[2]);
                        }
                        if (j % intervalCaptionOx == 0) {
                            if (!PLOTGRAPH) {
                                if (NOAXES == false) {
                                    g.drawString(new NumberR64(y1).toString(page.ring), (xcor[i] - 35), (int) (valY + 5));
                                }
                            }
                        }
                    }
                    j++;
                }
                double y2 = 0;
                int w = 0;
                while (y2 < yMax[i]) {
                    y = 0;
                    g.setColor(colorArray[3]);
                    if (w != 0) {
                        y2 = y + w * hOy[i];
                        valY = (int) (-y2 * zoom2[i] + ycor[i]);
                        if (!PLOTGRAPH) {
                            if (NOAXES == false) {
                                if (LATTICE == true) {
                                    ((Graphics2D) g).setStroke(new BasicStroke(1));
                                    int lenRiskUp = (int) (xMax[0] * zoom1[0] - xCorrect[0]);
                                    int lenRiskDown = (int) (xMin[0] * zoom1[0] - xCorrect[0]);
                                    g.drawLine(xcor[i] + lenRiskDown, valY, xcor[i] + lenRiskUp, valY);
                                } else {
                                    g.drawLine(xcor[i] - lenRisk, valY, xcor[i] + lenRisk, valY);
                                }
                            }
                        }
                        if (BLACK_COLOR) {
                            g.setColor(colorArray[3]);
                        } else {
                            g.setColor(colorArray[2]);
                        }
                        if (w % intervalCaptionOx == 0) {
                            if (!PLOTGRAPH) {
                                if (NOAXES == false) {
                                    g.drawString(new NumberR64(y2).toString(page.ring), (xcor[i] - 35), (int) (valY + 5));
                                }
                            }
                        }
                    }
                    w++;
                }
            }
            if (yMax[i] > 0 && yMin[i] >= 0) {
                double y1 = 0;
                int j = 0;
                double step = ycor[i];
                if (yMin[i] != 0) {
                    step = ycor[i] + yCorrect[i];
                }
                while (y1 < yMax[i]) {
                    y = 0;
                    g.setColor(colorArray[3]);
                    if (j != 0) {
                        y1 = y + j * hOy[i];
                        if (y1 > yMin[i]) {
                            valY = (int) (-y1 * zoom2[i] + step);
                            if (!PLOTGRAPH) {
                                if (NOAXES == false) {
                                    if (LATTICE == true) {
                                        ((Graphics2D) g).setStroke(new BasicStroke(1));
                                        int lenRiskUp = (int) (xMax[0] * zoom1[0] - xCorrect[0]);
                                        int lenRiskDown = (int) (xMin[0] * zoom1[0] - xCorrect[0]);
                                        g.drawLine(xcor[i] + lenRiskDown, valY, xcor[i] + lenRiskUp, valY);
                                    } else {
                                        g.drawLine(xcor[i] - lenRisk, valY, xcor[i] + lenRisk, valY);
                                    }
                                }
                            }
                            if (BLACK_COLOR) {
                                g.setColor(colorArray[3]);
                            } else {
                                g.setColor(colorArray[2]);
                            }
                            if (j % intervalCaptionOx == 0) {
                                if (!PLOTGRAPH) {
                                    if (NOAXES == false) {
                                        g.drawString(new NumberR64(y1).toString(page.ring), xcor[i] - 35, (int) (valY + 5));
                                    }
                                }
                            }
                        }
                    }
                    j++;
                }
            }
            if (yMax[i] <= 0 && yMin[i] < 0) {
                double y1 = 0;
                int j = 0;
                double step = ycor[i];
                if (yMax[i] != 0) {
                    step = ycor[i] + yCorrect[i];
                }
                while (y1 > yMin[i]) {
                    y = 0;
                    g.setColor(colorArray[3]);
                    if (j != 0) {
                        y1 = y - j * hOy[i];
                        if (y1 < yMax[i]) {
                            valY = (int) (-y1 * zoom2[i] + step);
                            if (!PLOTGRAPH) {
                                if (NOAXES == false) {
                                    if (LATTICE == true) {
                                        ((Graphics2D) g).setStroke(new BasicStroke(1));
                                        int lenRiskUp = (int) (xMax[0] * zoom1[0] - xCorrect[0]);
                                        int lenRiskDown = (int) (xMin[0] * zoom1[0] - xCorrect[0]);
                                        g.drawLine(xcor[i] + lenRiskDown, valY, xcor[i] + lenRiskUp, valY);
                                    } else {
                                        g.drawLine(xcor[i] - lenRisk, valY, xcor[i] + lenRisk, valY);
                                    }
                                }
                            }
                            if (BLACK_COLOR) {
                                g.setColor(colorArray[3]);
                            } else {
                                g.setColor(colorArray[2]);
                            }
                            if (j % intervalCaptionOx == 0) {
                                if (!PLOTGRAPH) {
                                    if (NOAXES == false) {
                                        g.drawString(new NumberR64(y1).toString(page.ring), xcor[i] - 35, (int) (valY + 5));
                                    }
                                }
                            }
                        }
                    }
                    j++;
                }
            }
        }
        page.ring.FLOATPOS = oldFloatpos;
        //      ((Graphics2D) g).setStroke(new BasicStroke(WIDTH_LINE));
    }

    /**
     * Метод отрисовки насечек на ось Ox и Oy
     *
     * @param g - Графический контекст
     */
    public void drawOxOy1(Graphics g) {
        //Вывод насечек по оси Ox
        drawOx(g);
        drawOy(g);
        for (int i = 0; i <= options; i++) {
            ycor[i] = ycor[i] + yCorrect[i];
            xcor[i] = xcor[i] - xCorrect[i];
        }
    }

    /**
     * Метод отрисовки информации по параметрическим функциям. Отрисовка
     * подписей осей и загаловков графиков. Для режима отображения всего набора
     * данных
     *
     * @param g - Графический контекст
     */
    public void drawAllCaptionForParamPlot(Graphics g) {
        int yTl = 50;
        int xTl = 30;
        g.setColor(colorArray[3]);
        //Заголовок
        g.drawString(this.title_str[0], xcor[0] + 10, 15);
        //Подпись оси oX
        g.drawString(this.x_str[0], 1326, ycor[0] - yCorrect[0] + 15);
        //Подпись оси oY
        g.drawString(this.y_str[0], xcor[0] + xCorrect[0] + 10, 45);
        yTl = yTl + 20;
        //       g.drawString("Список графиков: ", 20, yTl);
        int k = 0;
        for (int j = 0; j < allColors.size(); j++) {
            yTl = yTl + 30;
            g.setColor(colorArray[3]);
            g.drawString("x = " + arrFuncParam[k].toString(captionRing) + " \n y = " + arrFuncParam[k + 1].toString(captionRing), 30, yTl + 10);
            g.setColor(allColors.get(j));
            g.fillRect(xTl, yTl - 3, 100, 3);
            yTl += 5;
            k = k + 2;
        }

    }

    /**
     * Метод отрисовки информации по явным функциям. Отрисовка подписей осей и
     * загаловков графиков. Для режима отображения всего набора данных
     *
     * @param g - Графический контекст
     */
    public void drawAllCaptionForPlot(Graphics g) {
        int yTl = 50;
        int xTl = 30;
        g.setColor(colorArray[3]);
        //Заголовок
        g.drawString(this.title_str[0], xcor[0] + 10, 15);
        //Подпись оси oX
        g.drawString(this.x_str[0], 1326, ycor[0] - yCorrect[0] + 15);
        //Подпись оси oY
        g.drawString(this.y_str[0], xcor[0] + xCorrect[0] + 10, 45);
        yTl = yTl + 20;
        for (int j = 0; j < allColors.size(); j++) {
            yTl = yTl + 30;
            g.setColor(colorArray[3]);
            if (allColors.size() == 1) {
                g.drawString(this.func.toString(), 80, yTl);
            } else {
                g.drawString(this.func.X[j].toString(page.ring), 80, yTl);
            }
            g.setColor(allColors.get(j));
            g.fillRect(xTl, yTl - 3, 50, 3);
            yTl += 5;
        }
    }

    /**
     * Метод отрисовки информации по таблице данных. Для функции showPlots.
     *
     * @param g - Графический контекст
     */
    public void drawAllCaptionForShowPlots(Graphics g) {
        int yTl = 50;
        int xTl = 30;
        for (int i = 0; i <= options; i++) {
            g.setColor(colorArray[3]);
            //Заголовок
            if (NOAXES == false) {
                g.drawString(this.title_str[i], xcor[i] + xCorrect[i] + 30, 15);
            }
            //Подпись оси oX
            if (NOAXES == false) {
                g.drawString(this.x_str[i], 1326, ycor[i] - yCorrect[i] + 15);
            }
            //Подпись оси oY
            if (NOAXES == false) {
                g.drawString(this.y_str[i], xcor[i] + xCorrect[i] + 10, 45);
            }
            yTl = yTl + 20;
            //         g.drawString("Список графиков: ", 20, yTl);
            for (int j = 0; j < allColors.size(); j++) {
                yTl = yTl + 30;
                g.setColor(colorArray[3]);
                g.drawString(names[j] + ":", xTl, yTl);
              //  g.drawString(j + ":", xTl, yTl);
                g.setColor(allColors.get(j));
                g.fillRect(xTl + 15, yTl + 5, 85, 3);
                yTl += 5;
            }
        }
    }

    /**
     * Метод отрисовки информации по таблице данных. Для функции showPlots.
     *
     * @param g - Графический контекст
     */
    public void drawAllCaptionForPlotGraph(Graphics g) {
        int yTl = 50;
        int xTl = 30;
        for (int i = 0; i <= options; i++) {
            g.setColor(colorArray[3]);
            //Заголовок
            g.drawString(this.title_str[i], xcor[i] + xCorrect[i] + 30, 15);
            //Подпись оси oX
            if (!PLOTGRAPH) {
                g.drawString(this.x_str[i], 1326, ycor[i] - yCorrect[i] + 15);
            }
            //Подпись оси oY
            if (!PLOTGRAPH) {
                g.drawString(this.y_str[i], xcor[i] + xCorrect[i] + 10, 45);
            }
            yTl = yTl + 20;
            g.drawString("Список ребер: ", 20, yTl);
            for (int j = 0; j < allColors.size(); j++) {
                yTl = yTl + 30;
                g.setColor(colorArray[3]);
                g.drawString((j + 1) + ":", xTl, yTl);
                g.setColor(allColors.get(j));
                g.fillRect(xTl + 20, yTl - 3, 85, 3);
                yTl += 5;
            }
        }
    }

    /**
     * Метод отрисовки информации по таблице данных. Отрисовка подписей осей и
     * загаловков графиков. Для режима отображения всего набора данных
     *
     * @param g - Графический контекст
     */
    public void drawAllCaptionForTablePlot(Graphics g) {
        int yTl = 50;
        int xTl = 30;
        for (int i = 0; i <= options; i++) {
            g.setColor(colorArray[3]);
            //Заголовок
            g.drawString(this.title_str[i], xcor[i] + xCorrect[i] + 30, 15);
            //Подпись оси oX
            g.drawString(this.x_str[i], 1326, ycor[i] - yCorrect[i] + 15);
            //Подпись оси oY
            g.drawString(this.y_str[i], xcor[i] + xCorrect[i] + 10, 45);
            yTl = yTl + 20;
            //         g.drawString("Список графиков: ", 20, yTl);
            int kk = 1;
            if (xPoint.length > 0) {
                for (int j = 0; j < allColors.size(); j++) {
                    yTl = yTl + 30;
                    g.setColor(colorArray[3]);
                    if ((table.axesSign != null) && (table.axesSign[kk] != null)) {
                        g.drawString(table.axesSign[kk] + ":", xTl, yTl);
                        g.setColor(allColors.get(j));
                        g.fillRect(xTl + 45, yTl - 3, 105, 3);
                        kk++;
                    } else {
                        g.drawString(pointer[j] + ":", xTl, yTl);
                        g.setColor(allColors.get(j));
                        g.fillRect(xTl + 15, yTl - 3, 85, 3);
                    }
                    //g.drawString(pointer[j] + ":", xTl, yTl);
                    //g.setColor(allColors.get(j));
                    //g.fillRect(xTl + 15, yTl - 3, 85, 3);
                    yTl += 5;
                }
            }
        }
    }

    /**
     * Метод отрисовки точек для построения графиков парметрических функций
     *
     * @param g - Графический контекст
     */
    public void drawPointForParamPlot(Graphics g) {
        if (arrFuncParam.length > 0) {
            NumberR64 xm1 = new NumberR64(xMax[0]);
            NumberR64 xm2 = new NumberR64(xMin[0]);
            NumberR64 ym1 = new NumberR64(yMax[0]);
            NumberR64 ym2 = new NumberR64(yMin[0]);
            for (int j = 0; j <= arrFuncParam.length / 2 - 1; j++) {
                Color c = null;
                if (BLACK_COLOR) {
                    c = colorArray[3];
                } else {
                    c = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
                }
                g.setColor(c);
                //счетчик для наполнения набора цветов
                int m = 0;
                int kFrame = 0;
                for (int i = 0; i <= 2 * NumbOfSteps - 2; i++) {
                    int x1 = (int) (yPoint[j * 2][i] * zoom1[0] + xcor[0]);
                    int x2 = (int) (yPoint[j * 2][i + 1] * zoom1[0] + xcor[0]);
                    int y1 = (int) (-yPoint[j * 2 + 1][i] * zoom2[0] + ycor[0]);
                    int y2 = (int) (-yPoint[j * 2 + 1][i + 1] * zoom2[0] + ycor[0]);
                    Ring ring = new Ring("R64[x]");
                    NumberR64 a1 = new NumberR64(yPoint[j * 2][i]);
                    NumberR64 a2 = new NumberR64(yPoint[j * 2][i + 1]);
                    NumberR64 b1 = new NumberR64(yPoint[j * 2 + 1][i]);
                    NumberR64 b2 = new NumberR64(yPoint[j * 2 + 1][i + 1]);
                    ring.MachineEpsilonR64 = new NumberR64(0.1);
                    if ((a1.compareTo(xm1, ring) < 1) && (a1.compareTo(xm2, ring) >= 0)
                            && (b1.compareTo(ym1, ring) < 1) && (b1.compareTo(ym2, ring) >= 0)
                            && (a2.compareTo(xm1, ring) < 1) && (a2.compareTo(xm2, ring) >= 0)
                            && (b2.compareTo(ym1, ring) < 1) && (b2.compareTo(ym2, ring) >= 0)) {
                        //kFrame++;
                        kFrame = i;
                        dashAndArrow(g, new int[] {x1, y1, x2, y2}, i, false);
                        m++;
                    }
                }
                if ((ARROW == true) || (DASHANDARROW == true)) {
//                    if (kFrame == 2 * NumbOfSteps - 1) {
//                        kFrame = kFrame - 1;
//                    }
                    int x1 = (int) (yPoint[j * 2][kFrame] * zoom1[0] + xcor[0]);
                    int x2 = (int) (yPoint[j * 2][kFrame + 1] * zoom1[0] + xcor[0]);
                    int y1 = (int) (-yPoint[j * 2 + 1][kFrame] * zoom2[0] + ycor[0]);
                    int y2 = (int) (-yPoint[j * 2 + 1][kFrame + 1] * zoom2[0] + ycor[0]);
                    drawArrow(g, x1, y1, x2, y2);
                }
                if (m > 1) {
                    //Добавляем новый цвет в набор
                    allColors.add(c);
                }
            }
        }
    }

    /**
     * Метод отрисовки точек для построения графиков явных функций
     *
     * @param g - Графический контекст
     */
    public void drawPointForPlot(Graphics g) {
        //Вывод функций, заданной явно, на экран
        if (this.func.X.length > 0) {
            for (int j = 0; j <= this.func.X.length - 1; j++) {
                Color c = null;
                if (BLACK_COLOR) {
                    c = colorArray[3];
                } else {
                    c = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
                }
                g.setColor(c);
                //счетчик
                int k = 0;
                int kFrame = 0;
                //счетчик для наполнения набора цветов
                int m = 0;
                for (int i = 0; i < xPoint.length - 1; i++) {
                    int x1 = (int) (xPoint[k] * zoom1[0] + xcor[0]);
                    int x2 = (int) (xPoint[k + 1] * zoom1[0] + xcor[0]);
                    int y1 = (int) (-yPoint[j][k] * zoom2[0] + ycor[0]);
                    int y2 = (int) (-yPoint[j][k + 1] * zoom2[0] + ycor[0]);
                    if (xPoint[k] <= xMax[0] && xPoint[k] >= xMin[0]
                            && yPoint[j][k] <= yMax[0] && yPoint[j][k] >= yMin[0] && xPoint[k + 1] <= xMax[0] && xPoint[k + 1] >= xMin[0]
                            && yPoint[j][k + 1] <= yMax[0] && yPoint[j][k + 1] >= yMin[0]) {
                        //kFrame++;
                        kFrame = k;
                        dashAndArrow(g, new int[] {x1, y1, x2, y2}, i, false);
                        m++;
                    }
                    k++;
                }
                if ((ARROW == true) || (DASHANDARROW == true)) {
                    if (kFrame == xPoint.length - 1) {
                        kFrame = kFrame - 1;
                    }
                    int x1 = (int) (xPoint[kFrame - 1] * zoom1[0] + xcor[0]);
                    int x2 = (int) (xPoint[kFrame] * zoom1[0] + xcor[0]);
                    int y1 = (int) (-yPoint[j][kFrame - 1] * zoom2[0] + ycor[0]);
                    int y2 = (int) (-yPoint[j][kFrame] * zoom2[0] + ycor[0]);
                    drawArrow(g, x1, y1, x2, y2);
                }
                if (m > 1) {
                    //Добавляем новый цвет в набор
                    allColors.add(c);
                }
            }
        } else {
            if ((this.func.name != F.ABS) && (this.func.name != F.VECTORS)) {
                Color c = null;
                if (BLACK_COLOR) {
                    c = colorArray[3];
                } else {
                    c = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
                }
                g.setColor(c);
                //счетчик
                int k = 0;
                int kFrame = 0;
                //счетчик для наполнения набора цветов
                int m = 0;
                for (int i = 0; i < xPoint.length - 1; i++) {
                    int x1 = (int) (xPoint[k] * zoom1[0] + xcor[0]);
                    int x2 = (int) (xPoint[k + 1] * zoom1[0] + xcor[0]);
                    int y1 = (int) (-yPoint[0][k] * zoom2[0] + ycor[0]);
                    int y2 = (int) (-yPoint[0][k + 1] * zoom2[0] + ycor[0]);
                    if (xPoint[k] <= xMax[0] && xPoint[k] >= xMin[0]
                            && yPoint[0][k] <= yMax[0] && yPoint[0][k] >= yMin[0] && xPoint[k + 1] <= xMax[0] && xPoint[k + 1] >= xMin[0]
                            && yPoint[0][k + 1] <= yMax[0] && yPoint[0][k + 1] >= yMin[0]) {
                        //отрисовка точки по входным координатам
                        //g.fillOval((int) (xPoint[k] * zoom1[0] + xcor[0] - 3), (int) (-yPoint[0][k] * zoom2[0] + ycor[0] - 3), 3, 3);
                        //отрисовка точки по входным координатам
                        //  g.fillOval((int) (xPoint[k + 1] * zoom1[0] + xcor[0] - 3), (int) (-yPoint[0][k + 1] * zoom2[0] + ycor[0] - 3), 3, 3);
                        // kFrame++;
                        kFrame = k;
                        dashAndArrow(g, new int[] {x1, y1, x2, y2}, i, false);
                        m++;
                    }
                    k++;
                }
                if ((ARROW == true) || (DASHANDARROW == true)) {
                    if (kFrame == xPoint.length - 1) {
                        kFrame = kFrame - 1;
                    }
                    int x1 = (int) (xPoint[kFrame - 1] * zoom1[0] + xcor[0]);
                    int x2 = (int) (xPoint[kFrame] * zoom1[0] + xcor[0]);
                    int y1 = (int) (-yPoint[0][kFrame - 1] * zoom2[0] + ycor[0]);
                    int y2 = (int) (-yPoint[0][kFrame] * zoom2[0] + ycor[0]);
                    drawArrow(g, x1, y1, x2, y2);
                }
                if (m > 1) {
                    //Добавляем новый цвет в набор
                    allColors.add(c);
                }
            }
        }
    }

    /**
     * Метод отрисовки точек для построения графиков textPlot
     *
     * @param g - Графический контекст
     */
    public void drawPointForTextPlot(Graphics g) {
        g.setColor(colorArray[3]);
        if (s != null) //  added GIM 8 june 2016
        {
            for (Element[] arg : s) {
                if ((arg.length > 3)
                        && ((arg[2].doubleValue() <= xMax[0]
                        && arg[2].doubleValue() >= xMin[0]
                        && arg[3].doubleValue() <= yMax[0]
                        && arg[3].doubleValue() >= yMin[0]))) {
                    int size = arg[1].intValue();
                    Font f = new Font(Font.DIALOG, Font.PLAIN, size);
                    String str = ((Fname) arg[0]).name;
                    //сдвиг от центра текста
                    //double k = ((0.2 * str.length()) * (size / 16)) / 2;
                    //попорот текста относительно центра текста
                    double alpha = 0;
                    if (arg.length == 5) {
                        alpha = (arg[4].doubleValue() * Math.PI) / 180;
                    }
                    //AffineTransform.getRotateInstance(alpha,arg[2].doubleValue(),-arg[3].doubleValue())
                    AffineTransform transform = new AffineTransform(
                            Math.cos(alpha), -Math.sin(alpha),
                            Math.sin(alpha), Math.cos(alpha), 0, 0);
                    f = f.deriveFont(transform);
                    g.setFont(f);
                    int x1 = (int) (arg[2].doubleValue() * zoom1[0] + xcor[0]);
                    int y1 = (int) (-arg[3].doubleValue() * zoom2[0] + ycor[0]);
                    g.drawString(str, x1, y1);
                }
            }
        }
        g.setFont(new Font(Font.DIALOG, Font.PLAIN, TEXTSIZE));
    }

    /**
     * Установка номера вершины для plotGraph
     *
     * @param x
     * @param y
     * @param xArr
     * @param yArr
     *
     * @return
     */
    private boolean findPointsCaption(double x, Double[] xArr) {
        boolean f1 = true;
        for (int i = 0; i < xArr.length; i++) {
            if (x == xArr[i]) {
                f1 = false;
                break;
            }
        }
        return f1;
    }

    /**
     * Метод отрисовки точек для построения графиков. Для режима отображения
     * всего набора данных.
     *
     * @param g - Графический контекст
     */
    public void drawAllPoinFortPlotGraph(Graphics g) {
        ArrayList<Double> xArr = new ArrayList<Double>();
        for (int j = 0; j < arrFuncTable.length; j++) {
            Table t = ((Table) arrFuncTable[j]);
            setXYPoints(t);
            for (int z = 0; z < yPoint.length; z++) {
                Color c = null;
                if (BLACK_COLOR) {
                    c = colorArray[3];
                } else {
                    c = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
                }
                g.setColor(c);
                //счетчик
                int k = 0;
                //счетчик для наполнения набора цветов
                int m = 0;
                for (int i = 0; i < xPoint.length - 2; i++) {
                    int x1 = (int) (xPoint[k] * zoom1[0] + xcor[0]);
                    int x2 = (int) (xPoint[k + 1] * zoom1[0] + xcor[0]);
                    int y1 = (int) (-yPoint[z][k] * zoom2[0] + ycor[0]);
                    int y2 = (int) (-yPoint[z][k + 1] * zoom2[0] + ycor[0]);
                    if (xPoint[k] <= xMax[0]
                            && xPoint[k] >= xMin[0]
                            && yPoint[z][k] <= yMax[0]
                            && yPoint[z][k] >= yMin[0]
                            && xPoint[k + 1] <= xMax[0]
                            && xPoint[k + 1] >= xMin[0]
                            && yPoint[z][k + 1] <= yMax[0]
                            && yPoint[z][k + 1] >= yMin[0]) {
                        //отрисовка точки по входным координатам
                        g.fillOval((int) (xPoint[k] * zoom1[0] + xcor[0] - 3), (int) (-yPoint[z][k] * zoom2[0] + ycor[0] - 3), 6, 6);
                        //отрисовка точки по входным координатам
                        g.fillOval((int) (xPoint[k + 1] * zoom1[0] + xcor[0] - 3), (int) (-yPoint[z][k + 1] * zoom2[0] + ycor[0] - 3), 6, 6);
                        if (CAPTIONS) {
                            if (j == 0) {
                                xArr.add(xPoint[2]);
                                xArr.add(yPoint[z][2]);
                            }
                            boolean fl1 = true;
                            boolean fl2 = true;
                            if (j != 0) {
                                Double[] xM = new Double[xArr.size()];
                                xArr.toArray(xM);
                                fl1 = findPointsCaption(xPoint[2], xM);
                                if (fl1) {
                                    xArr.add(xPoint[2]);
                                }
                            }
                            if (fl1) {
                                //вывод печати координат точки
                                g.setColor(colorArray[3]);
                                g.drawString("(" + Integer.toString((int) xPoint[2]) + ")", (int) (xPoint[k] * zoom1[0] + xcor[0] - 6), (int) (-yPoint[z][k] * zoom2[0] + ycor[0] - 6));
                                g.setColor(c);
                            }
                            if (j != 0) {
                                Double[] xM = new Double[xArr.size()];
                                xArr.toArray(xM);
                                fl2 = findPointsCaption(yPoint[z][2], xM);
                                if (fl2) {
                                    xArr.add(yPoint[z][2]);
                                }
                            }
                            if (fl2) {
                                g.setColor(colorArray[3]);
                                g.drawString("(" + Integer.toString((int) yPoint[z][2]) + ")", (int) (xPoint[k + 1] * zoom1[0] + xcor[0] - 6), (int) (-yPoint[z][k + 1] * zoom2[0] + ycor[0] - 6));
                                g.setColor(c);
                            }
                        }
                        g.drawLine(x1, y1, x2, y2);
                        m++;
                    }
                    k++;
                }
                if (m >= 1) {
                    //Добавляем новый цвет в набор
                    allColors.add(c);
                }
            }
        }
    }

    /**
     * Метод отрисовки точек для построения графиков. Для режима отображения
     * всего набора данных.
     *
     * @param g - Графический контекст
     */
    public void drawAllPointForShowPlots(Graphics g) {
        //Вывод функций, заданной явно, на экран
        if (this.func.X.length > 0) {
            for (int j = 0; j <= this.func.X.length - 1; j++) {
                int kFrame = 0;
                Color c = null;
                if (BLACK_COLOR) {
                    c = colorArray[3];
                } else {
                    c = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
                }
                g.setColor(c);
                //счетчик
                int k = 0;
                //счетчик для наполнения набора цветов
                int m = 0;
                for (int i = 0; i < xPointFuncShowPlots[j].length - 1; i++) {
                    int x1 = (int) (xPointFuncShowPlots[j][k] * zoom1[0] + xcor[0]);
                    int x2 = (int) (xPointFuncShowPlots[j][k + 1] * zoom1[0] + xcor[0]);
                    int y1 = (int) (-yPointFunc[j][k] * zoom2[0] + ycor[0]);
                    int y2 = (int) (-yPointFunc[j][k + 1] * zoom2[0] + ycor[0]);
                    if (xPointFuncShowPlots[j][k] <= xMax[0] && xPointFuncShowPlots[j][k] >= xMin[0]
                            && yPointFunc[j][k] <= yMax[0] && yPointFunc[j][k] >= yMin[0] && xPointFuncShowPlots[j][k + 1] <= xMax[0] && xPointFuncShowPlots[j][k + 1] >= xMin[0]
                            && yPointFunc[j][k + 1] <= yMax[0] && yPointFunc[j][k + 1] >= yMin[0]) {
                        dashAndArrowForShowPlots(g, arrStyleThePlot[j], new int[] {x1, y1, x2, y2}, i, false);
                        m++;
                    }
                    kFrame = i;
                    k++;
                }
                if (arrStyleThePlot[0].equals("arrow") || arrStyleThePlot[0].equals("dashAndArrow")) {
                    int x1 = (int) (xPointFuncShowPlots[j][kFrame] * zoom1[0] + xcor[0]);
                    int x2 = (int) (xPointFuncShowPlots[j][kFrame + 1] * zoom1[0] + xcor[0]);
                    int y1 = (int) (-yPointFunc[j][kFrame] * zoom2[0] + ycor[0]);
                    int y2 = (int) (-yPointFunc[j][kFrame + 1] * zoom2[0] + ycor[0]);
                    drawArrow(g, x1, y1, x2, y2);
                }
                if (m > 1) {
                    //Добавляем новый цвет в набор
                    allColors.add(c);
                }
            }
        } else {
            if ((this.func.name != F.ABS) && (this.func.name != F.VECTORS)) {
                Color c = null;
                if (BLACK_COLOR) {
                    c = colorArray[3];
                } else {
                    c = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
                }
                g.setColor(c);
                //счетчик
                int kFrame = 0;
                int k = 0;
                //счетчик для наполнения набора цветов
                int m = 0;
                for (int i = 0; i < xPointFunc.length - 1; i++) {
                    int x1 = (int) (xPointFunc[k] * zoom1[0] + xcor[0]);
                    int x2 = (int) (xPointFunc[k + 1] * zoom1[0] + xcor[0]);
                    int y1 = (int) (-yPointFunc[0][k] * zoom2[0] + ycor[0]);
                    int y2 = (int) (-yPointFunc[0][k + 1] * zoom2[0] + ycor[0]);
                    if (xPointFunc[k] <= xMax[0] && xPointFunc[k] >= xMin[0]
                            && yPointFunc[0][k] <= yMax[0] && yPointFunc[0][k] >= yMin[0] && xPointFunc[k + 1] <= xMax[0] && xPointFunc[k + 1] >= xMin[0]
                            && yPointFunc[0][k + 1] <= yMax[0] && yPointFunc[0][k + 1] >= yMin[0]) {
                        //отрисовка точки по входным координатам
                        g.fillOval((int) (xPointFunc[k] * zoom1[0] + xcor[0] - 3), (int) (-yPointFunc[0][k] * zoom2[0] + ycor[0] - 3), 3, 3);
                        //отрисовка точки по входным координатам
                        g.fillOval((int) (xPointFunc[k + 1] * zoom1[0] + xcor[0] - 3), (int) (-yPointFunc[0][k + 1] * zoom2[0] + ycor[0] - 3), 3, 3);
                        dashAndArrowForShowPlots(g, arrStyleThePlot[0], new int[] {x1, y1, x2, y2}, i, false);
                        m++;
                    }
                    kFrame = i;
                    k++;
                }
                if (arrStyleThePlot[0].equals("arrow") || arrStyleThePlot[0].equals("dashAndArrow")) {
                    int x1 = (int) (xPointFunc[kFrame] * zoom1[0] + xcor[0]);
                    int x2 = (int) (xPointFunc[kFrame + 1] * zoom1[0] + xcor[0]);
                    int y1 = (int) (-yPointFunc[0][kFrame] * zoom2[0] + ycor[0]);
                    int y2 = (int) (-yPointFunc[0][kFrame + 1] * zoom2[0] + ycor[0]);
                    drawArrow(g, x1, y1, x2, y2);
                }
                if (m > 1) {
                    //Добавляем новый цвет в набор
                    allColors.add(c);
                }
            }
        }
        //Вывод функций, заданной явно, на экран
        if (arrFuncParam.length > 0) {
            for (int j = 0; j <= arrFuncParam.length / 2 - 1; j++) {
                int kFrame = 0;
                Color c = null;
                if (BLACK_COLOR) {
                    c = colorArray[3];
                } else {
                    c = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
                }
                g.setColor(c);
                //счетчик для наполнения набора цветов
                int m = 0;
                for (int i = 0; i <= 2 * NumbOfSteps - 2; i++) {
                    int x1 = (int) (yPointFuncParam[j * 2][i] * zoom1[0] + xcor[0]);
                    int x2 = (int) (yPointFuncParam[j * 2][i + 1] * zoom1[0] + xcor[0]);
                    int y1 = (int) (-yPointFuncParam[j * 2 + 1][i] * zoom2[0] + ycor[0]);
                    int y2 = (int) (-yPointFuncParam[j * 2 + 1][i + 1] * zoom2[0] + ycor[0]);
                    if ((yPointFuncParam[j * 2][i] <= xMax[0]) && (yPointFuncParam[j * 2][i] >= xMin[0])
                            && (yPointFuncParam[j * 2 + 1][i] <= yMax[0]) && (yPointFuncParam[j * 2 + 1][i] >= yMin[0])
                            && (yPointFuncParam[j * 2][i + 1] <= xMax[0]) && (yPointFuncParam[j * 2][i + 1] >= xMin[0])
                            && (yPointFuncParam[j * 2 + 1][i + 1] <= yMax[0]) && (yPointFuncParam[j * 2 + 1][i + 1] >= yMin[0])) {
                        kFrame = i;
                        dashAndArrowForShowPlots(g, arrStyleTheParamPlot[j], new int[] {x1, y1, x2, y2}, i, false);
                        m++;
                    }
                }
                if (arrStyleTheParamPlot[j].equals("arrow") || arrStyleTheParamPlot[j].equals("dashAndArrow")) {
                    int x1 = (int) (yPointFuncParam[j * 2][kFrame] * zoom1[0] + xcor[0]);
                    int x2 = (int) (yPointFuncParam[j * 2][kFrame + 1] * zoom1[0] + xcor[0]);
                    int y1 = (int) (-yPointFuncParam[j * 2 + 1][kFrame] * zoom2[0] + ycor[0]);
                    int y2 = (int) (-yPointFuncParam[j * 2 + 1][kFrame + 1] * zoom2[0] + ycor[0]);
                    drawArrow(g, x1, y1, x2, y2);
                }
                if (m > 1) {
                    //Добавляем новый цвет в набор
                    allColors.add(c);
                }
            }
        }
        for (int j = 0; j < arrFuncTable.length; j++) {
            Table t = ((Table) arrFuncTable[j]);
            setXYPoints(t);
            for (int z = 0; z < yPoint.length; z++) {
                Color c = null;
                if (BLACK_COLOR) {
                    c = colorArray[3];
                } else {
                    c = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
                }
                g.setColor(c);
                //счетчик
                int k = 0;
                //счетчик для наполнения набора цветов
                int m = 0;
                for (int i = 0; i < xPoint.length - 1; i++) {
                    int x1 = (int) (xPoint[k] * zoom1[0] + xcor[0]);
                    int x2 = (int) (xPoint[k + 1] * zoom1[0] + xcor[0]);
                    int y1 = (int) (-yPoint[z][k] * zoom2[0] + ycor[0]);
                    int y2 = (int) (-yPoint[z][k + 1] * zoom2[0] + ycor[0]);
                    if (xPoint[k] <= xMax[0] && xPoint[k] >= xMin[0]
                            && yPoint[z][k] <= yMax[0] && yPoint[z][k] >= yMin[0] && xPoint[k + 1] <= xMax[0] && xPoint[k + 1] >= xMin[0]
                            && yPoint[z][k + 1] <= yMax[0] && yPoint[z][k + 1] >= yMin[0]) {
                        //отрисовка точки по входным координатам
                        g.fillOval((int) (xPoint[k] * zoom1[0] + xcor[0] - 3), (int) (-yPoint[z][k] * zoom2[0] + ycor[0] - 3), 6, 6);
                        //отрисовка точки по входным координатам
                        g.fillOval((int) (xPoint[k + 1] * zoom1[0] + xcor[0] - 3), (int) (-yPoint[z][k + 1] * zoom2[0] + ycor[0] - 3), 6, 6);
                        if (CAPTIONS) {
                            //вывод печати координат точки
                            g.drawString("[" + xPoint[k] + "," + yPoint[z][k] + "]", (int) (xPoint[k] * zoom1[0] + xcor[0] - 6), (int) (-yPoint[z][k] * zoom2[0] + ycor[0] - 6));
                            g.drawString("[" + xPoint[k + 1] + "," + yPoint[z][k + 1] + "]", (int) (xPoint[k + 1] * zoom1[0] + xcor[0] - 6), (int) (-yPoint[z][k + 1] * zoom2[0] + ycor[0] - 6));
                        }
                        dashAndArrowForShowPlots(g, arrStyleTheTablePlot[j], new int[] {x1, y1, x2, y2}, i, true);
                        m++;
                    }
                    k++;
                }
                if (m >= 1) {
                    //Добавляем новый цвет в набор
                    allColors.add(c);
                }
            }
        }
        //для pointsPlot
        for (int j = 0; j < 0;){//arrFuncPointsTable.length; j++) {

            pointsText = new String[((VectorS) pointsTextShow[j]).V.length];
            for (int i = 0; i < pointsText.length; i++) {
                pointsText[i] = ((Fname) ((F) ((VectorS) pointsTextShow[j]).V[i]).X[0]).name;
            }

            if (((VectorS) pointsK2Show[j]).V.length == 0) {
                k2 = new int[pointsText.length];
                for (int i = 0; i < k2.length; i++) {
                    k2[i] = 0;
                }
            } else {
                k2 = new int[pointsText.length];
                for (int i = 0; i < ((VectorS) pointsK2Show[j]).V.length; i++) {
                    k2[i] = -(int) ((VectorS) pointsK2Show[j]).V[i].value * TEXTSIZE;
                }
            }
            if (((VectorS) pointsK1Show[j]).V.length == 0) {
                k1 = new int[2][];
                k1[0] = new int[pointsText.length];
                k1[1] = new int[pointsText.length];
                for (int i = 0; i < k1[0].length; i++) {
                    k1[0][i] = 0;
                    k1[1][i] = -6;
                }
            } else {
                k1 = new int[2][];
                k1[0] = new int[pointsText.length];
                k1[1] = new int[pointsText.length];
                for (int i = 0; i < ((VectorS) pointsK1Show[j]).V.length; i++) {
                    int k = (int) ((VectorS) pointsK1Show[j]).V[i].value;
                    switch (k) {
                        case 0:
                        case 8: {
                            k1[0][i] = 0;
                            k1[1][i] = -TEXTSIZE;
                            break;
                        }
                        case 1: {
                            k1[0][i] = TEXTSIZE / 2;
                            k1[1][i] = -TEXTSIZE / 2;
                            break;
                        }
                        case 2: {
                            k1[0][i] = TEXTSIZE;
                            k1[1][i] = 3;
                            break;
                        }
                        case 3: {
                            k1[0][i] = TEXTSIZE / 2 + 3;
                            k1[1][i] = TEXTSIZE / 2 + 3;
                            break;
                        }
                        case 4: {
                            k1[0][i] = 0;
                            k1[1][i] = TEXTSIZE + 3;
                            break;
                        }
                        case 5: {
                            k1[0][i] = -TEXTSIZE / 2 - 3;
                            k1[1][i] = TEXTSIZE / 2 + 3;
                            break;
                        }
                        case 6: {
                            k1[0][i] = -TEXTSIZE;
                            k1[1][i] = 3;
                            break;
                        }
                        case 7: {
                            k1[0][i] = -TEXTSIZE / 2;
                            k1[1][i] = -TEXTSIZE / 2;
                            break;
                        }
                    }
                }
            }
            Table t = ((Table) arrFuncPointsTable[j]);
            setXYPoints(t);
            for (int z = 0; z < yPoint.length; z++) {
                Color c = null;
                if (BLACK_COLOR) {
                    c = colorArray[3];
                } else {
                    c = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
                }
                g.setColor(c);
                //счетчик
                int k = 0;
                //счетчик для наполнения набора цветов
                int m = 0;
                if (xPoint.length == 1) {
                    if (xPoint[k] <= xMax[0] && xPoint[k] >= xMin[0]
                            && yPoint[z][k] <= yMax[0] && yPoint[z][k] >= yMin[0]) {
                        //отрисовка точки по входным координатам
                        g.fillOval((int) (xPoint[k] * zoom1[0] + xcor[0] - 3), (int) (-yPoint[z][k] * zoom2[0] + ycor[0] - 3), 6, 6);
                        g.drawString(pointsText[k], (int) (xPoint[k] * zoom1[0] + xcor[0] - 3 - k2[k] + k1[0][k]), (int) (-yPoint[z][k] * zoom2[0] + ycor[0] + k1[1][k]));
                        m++;
                    }
                } else {
                    for (int i = 0; i < xPoint.length - 1; i++) {
                        if (xPoint[k] <= xMax[0] && xPoint[k] >= xMin[0]
                                && yPoint[z][k] <= yMax[0] && yPoint[z][k] >= yMin[0] && xPoint[k + 1] <= xMax[0] && xPoint[k + 1] >= xMin[0]
                                && yPoint[z][k + 1] <= yMax[0] && yPoint[z][k + 1] >= yMin[0]) {
                            //отрисовка точки по входным координатам
                            g.fillOval((int) (xPoint[k] * zoom1[0] + xcor[0] - 3), (int) (-yPoint[z][k] * zoom2[0] + ycor[0] - 3), 6, 6);
                            //отрисовка точки по входным координатам
                            g.fillOval((int) (xPoint[k + 1] * zoom1[0] + xcor[0] - 3), (int) (-yPoint[z][k + 1] * zoom2[0] + ycor[0] - 3), 6, 6);
                            g.drawString(pointsText[k], (int) (xPoint[k] * zoom1[0] + xcor[0] - 3 - k2[k] + k1[0][k]), (int) (-yPoint[z][k] * zoom2[0] + ycor[0] + k1[1][k]));
                            g.drawString(pointsText[k + 1], (int) (xPoint[k + 1] * zoom1[0] + xcor[0] - 3 - k2[k + 1] + k1[0][k + 1]), (int) (-yPoint[z][k + 1] * zoom2[0] + ycor[0] + k1[1][k + 1]));
                            m++;
                        }
                        k++;
                    }
                }
                if (m >= 1) {
                    //Добавляем новый цвет в набор
                    allColors.add(c);
                }
            }
        }
    }

    /**
     * Метод для отрисовки стрелки
     *
     * @param g
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        int x = x2 - x1;
        int y = y2 - y1;
        double beta = Math.atan2(y, x);
        double alfa = Math.PI / 12;
        int r1 = 20;
        double rho = beta + alfa;
        int x21 = (int) Math.round(x2 - r1 * Math.cos(rho));
        int y21 = (int) Math.round(y2 - r1 * Math.sin(rho));
        g.drawLine(x2, y2, x21, y21);
        rho = beta - alfa;
        int x22 = (int) Math.round(x2 - r1 * Math.cos(rho));
        int y22 = (int) Math.round(y2 - r1 * Math.sin(rho));
        g.drawLine(x2, y2, x22, y22);
    }

    /**
     * Метод отрисовки точек для построения табличных графиков.
     *
     * @param g - Графический контекст
     */
    public void drawAllPointForTablePlot(Graphics g) {
        //Номера тех графиков которые будут отображены на графике
        ArrayList<Integer> numbersPointer = new ArrayList<Integer>();
        for (int j = 0; j < numberFunc.length; j++) {
            Color c = null;
            if (BLACK_COLOR) {
                c = colorArray[3];
            } else {
                c = new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
            }
            g.setColor(c);
            //счетчик
            int k = 0;
            //счетчик для наполнения набора цветов
            int m = 0;
            //случай когда 1 точка (x,y) в таблице
            if (xPoint.length == 1) {
                if (xPoint[k] <= xMax[0] && xPoint[k] >= xMin[0]
                        && yPoint[numberFunc[j]][k] <= yMax[0] && yPoint[numberFunc[j]][k] >= yMin[0]) {
                    //отрисовка точки по входным координатам
                    g.fillOval((int) (xPoint[k] * zoom1[0] + xcor[0] - 3), (int) (-yPoint[numberFunc[j]][k] * zoom2[0] + ycor[0] - 3), 6, 6);
                    if (CAPTIONS) {
                        if (pointPlotting) {
                            g.drawString( // it was cut to the 3 rows -- to see what has happens
                                    pointsText[k],
                                    (int) (xPoint[k] * zoom1[0] + xcor[0] - 3 - k2[k] + k1[0][k]),
                                    (int) (-yPoint[numberFunc[j]][k] * zoom2[0] + ycor[0] + k1[1][k]));
                        } else {
                            //вывод печати координат точки
                            g.drawString("[" + xPoint[k] + "," + yPoint[numberFunc[j]][k] + "]", (int) (xPoint[k] * zoom1[0] + xcor[0] - 6), (int) (-yPoint[numberFunc[j]][k] * zoom2[0] + ycor[0] - 6));
                        }
                    }
                }
                m++;
            }
            for (int i = 0; i < xPoint.length - 1; i++) {
                int x1 = (int) (xPoint[k] * zoom1[0] + xcor[0]);
                int x2 = (int) (xPoint[k + 1] * zoom1[0] + xcor[0]);
                int y1 = (int) (-yPoint[numberFunc[j]][k] * zoom2[0] + ycor[0]);
                int y2 = (int) (-yPoint[numberFunc[j]][k + 1] * zoom2[0] + ycor[0]);
                if (xPoint[k] <= xMax[0] && xPoint[k] >= xMin[0]
                        && yPoint[numberFunc[j]][k] <= yMax[0] && yPoint[numberFunc[j]][k] >= yMin[0] && xPoint[k + 1] <= xMax[0] && xPoint[k + 1] >= xMin[0]
                        && yPoint[numberFunc[j]][k + 1] <= yMax[0] && yPoint[numberFunc[j]][k + 1] >= yMin[0]) {
                    //отрисовка точки по входным координатам
                    g.fillOval((int) (xPoint[k] * zoom1[0] + xcor[0] - 3), (int) (-yPoint[numberFunc[j]][k] * zoom2[0] + ycor[0] - 3), 6, 6);
                    //отрисовка точки по входным координатам
                    g.fillOval((int) (xPoint[k + 1] * zoom1[0] + xcor[0] - 3), (int) (-yPoint[numberFunc[j]][k + 1] * zoom2[0] + ycor[0] - 3), 6, 6);
                    if (CAPTIONS) {
                        if (pointPlotting) {
                            if (pointsText != null) {
                                g.drawString(pointsText[k], (int) (xPoint[k] * zoom1[0] + xcor[0] - 3 - k2[k] + k1[0][k]), (int) (-yPoint[numberFunc[j]][k] * zoom2[0] + ycor[0] + k1[1][k]));
                                g.drawString(pointsText[k + 1], (int) (xPoint[k + 1] * zoom1[0] + xcor[0] - 3 - k2[k + 1] + k1[0][k + 1]), (int) (-yPoint[numberFunc[j]][k + 1] * zoom2[0] + ycor[0] + k1[1][k + 1]));
                            } else {
                                g.drawString("", (int) (xPoint[k] * zoom1[0] + xcor[0] - 3 - k2[k] + k1[0][k]), (int) (-yPoint[numberFunc[j]][k] * zoom2[0] + ycor[0] + k1[1][k]));
                                g.drawString("", (int) (xPoint[k + 1] * zoom1[0] + xcor[0] - 3 - k2[k + 1] + k1[0][k + 1]), (int) (-yPoint[numberFunc[j]][k + 1] * zoom2[0] + ycor[0] + k1[1][k + 1]));
                            }
                        } else {
                            //вывод печати координат точки
                            g.drawString("[" + xPoint[k] + "," + yPoint[numberFunc[j]][k] + "]", (int) (xPoint[k] * zoom1[0] + xcor[0] - 6), (int) (-yPoint[numberFunc[j]][k] * zoom2[0] + ycor[0] - 6));
                            g.drawString("[" + xPoint[k + 1] + "," + yPoint[numberFunc[j]][k + 1] + "]", (int) (xPoint[k + 1] * zoom1[0] + xcor[0] - 6), (int) (-yPoint[numberFunc[j]][k + 1] * zoom2[0] + ycor[0] - 6));
                        }
                    }
                    if (lineTheTablePlot == true) {
                        dashAndArrow(g, new int[] {x1, y1, x2, y2}, i, true);
                    }
                    m++;
                }
                k++;
            }
            if (m >= 1) {
                //Добавляем новый цвет в набор
                allColors.add(c);
                numbersPointer.add(j);
            }
        }
        pointer = new int[numbersPointer.size()];
        for (int i = 0; i < pointer.length; i++) {
            pointer[i] = numbersPointer.get(i);
        }
    }

    /**
     * Метод вычисления параметров-границ отрисовки графиков. В случае когда не
     * задан ни один из режимов
     *
     * @param g - - Графический контекст
     */
    public void drawAllParamStepOne(Graphics g) {
        if (avtoMatick == false) {
            xStart = xMin[0];
            xEnd = xMax[0];
            yStart = yMin[0];
            yEnd = yMax[0];

        } else {
            xStart = xMin[0];
            xEnd = xMax[0];
            yStart = yMin[0];
            yEnd = yMax[0];
            if (avtoMatick) {
                if (xPoint.length != 0) {
                    xStart = xPoint[0];
                    xEnd = xPoint[0];
                }
                if (yPoint.length != 0) {
                    yStart = yPoint[numberFunc[0]][0];
                    yEnd = yPoint[numberFunc[0]][0];
                }
            }
            for (int j = 0; j < numberFunc.length; j++) {
                for (int i = 1; i < xPoint.length; i++) {
                    double xST = xPoint[i];
                    double yST = yPoint[numberFunc[j]][i];
                    if (xST < xStart) {
                        xStart = xST;
                    } else {
                        if (xST > xEnd) {
                            xEnd = xST;
                        }
                    }
                    if (yST < yStart) {
                        yStart = yST;
                    } else {
                        if (yST > yEnd) {
                            yEnd = yST;
                        }
                    }
                }
                if (avtoMatick) {
                    yMin[0] = yStart;
                    yMax[0] = yEnd;
                    xMin[0] = xStart;
                    xMax[0] = xEnd;
                    //когда xStart = xEnd
                    if (xStart == xEnd) {
                        xMin[0] = xStart - 1;
                        xMax[0] = xStart + 1;
                    }
                    //когда yStart = yEnd
                    if (yStart == yEnd) {
                        yMin[0] = yStart - 1;
                        yMax[0] = yStart + 1;
                    }
                }
                if (avtoMatickY) {
                    yMin[0] = yStart;
                    yMax[0] = yEnd;
                }
            }
        }
    }

    /**
     * Метод вычисления параметров-границ отрисовки графиков. Для отрисовки
     * параметрических функций.
     *
     * @param g
     */
    public void drawAllParamForParamPlot(Graphics g) {
        if (isAutomatCalculation() == false) {
            yMin[0] = page.yMin;
            yMax[0] = page.yMax;
            xMin[0] = page.xMin;
            xMax[0] = page.xMax;
        } else {
            //Нахождение максимального и минимального значения параметрических функций
            for (int i = 0; i <= arrFuncParam.length - 1; i++) {
                for (int j = 0; j <= 2 * NumbOfSteps - 1; j = j + 2) {
                    double xST = yPoint[i][j];
                    double yST = yPoint[i][j + 1];
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
            if (avtoMatick) {
                yMin[0] = yStart;
                yMax[0] = yEnd;
                xMin[0] = xStart;
                xMax[0] = xEnd;
            }
        }
    }

    /**
     * Метод вычисления параметров-границ отрисовки графиков. Для отрисовки
     * явных функций.
     *
     * @param g - - Графический контекст
     */
    public void drawAllParamForPlot(Graphics g) {
        //Нахождение максимального и минимального значения функций заданых явно
        xStart = xMin[0];
        xEnd = xMax[0];
        yStart = yMin[0];
        yEnd = yMax[0];
        //Нахождение максимального и минимального значения функций, заданных таблицей значений
        xStart = xPoint[0];
        for (int i = 0; i <= this.func.X.length - 1; i++) {
            yStart = yPoint[i][0];
            for (int j = 0; j <= NumbOfSteps - 1; j++) {
                double yST = yPoint[i][j];
                if (yST < yStart) {
                    yStart = yST;
                } else {
                    if (yST > yEnd) {
                        yEnd = yST;
                    }
                }
            }
        }
        if (yStart < -100) {
            yStart = -100;
        }
        if (yEnd > 100) {
            yEnd = 100;
        }
        if (avtoMatick) {
            yMin[0] = yStart;
            yMax[0] = yEnd;
            xMin[0] = xStart;
            xMax[0] = xEnd;
        }
    }

    /**
     * Метод вычисления параметров-границ отрисовки графиков
     *
     * @param g - - Графический контекст
     */
    public void drawParam(Graphics g) {
        for (int j = 0; j <= options; j++) {
            //Нахождение xStart,xEnd и yStart, yEnd, это параметры отвечающие
            //за автоматическое построение ф-ций
            xStart = xMin[j];
            xEnd = xMax[j];
            yStart = 0;
            yEnd = 0;
            //Нахождение максимального и минимального значения функций, заданных таблицей значений
            for (int i = 0; i < xPoint.length; i++) {
                double xST = xPoint[i];
                double yST = yPoint[numberFunc[j]][i];//currentIndex
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
            if (avtoMatick) {
                yMin[j] = yStart;
                yMax[j] = yEnd;
                xMin[j] = xStart;
                xMax[j] = xEnd;
            }
        }
    }

    /**
     * Метод вычисления параметров-границ отрисовки графиков. Для режима
     * отображения всего набора данных
     *
     * @param g - - Графический контекст
     */
    public void drawAllParam(Graphics g) {
        for (int j = 0; j <= options; j++) {
            //Нахождение xStart,xEnd и yStart, yEnd, это параметры отвечающие
            //за автоматическое построение ф-ций
            xStart = xMin[j];
            xEnd = xMax[j];
            yStart = yMin[j];
            yEnd = yMax[j];
            if (avtoMatick) {
                if (xPoint.length != 0) {
                    xStart = xPoint[0];
                    xEnd = xPoint[0];
                }
                if (yPoint.length != 0) {
                    yStart = yPoint[numbers[j][0]][0];
                    yEnd = yPoint[numbers[j][0]][0];
                }
            }
            for (int w = 0; w < numbers[j].length; w++) {
                //Нахождение максимального и минимального значения функций, заданных таблицей значений
                for (int i = 0; i < xPoint.length; i++) {
                    double xST = xPoint[i];
                    double yST = yPoint[numbers[j][w]][i];
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
                if (avtoMatick) {
                    yMin[j] = yStart;
                    yMax[j] = yEnd;
                    xMin[j] = xStart;
                    xMax[j] = xEnd;
                }
            }
        }
    }

    /**
     * Метод отрисовки линий, заданных координатами. Работает в 0 - режиме
     *
     * @param g - Графический контекст
     */
    public void addDrawLines(Graphics g) {
        int oldFloatPos = page.ring.FLOATPOS;
        //Массив для координат пересечения линий по X
        Element[] xCoorLine = new Element[pointLine.length];
        //Массив для координат пересечения линий по Y
        Element[] yCoorLine = new Element[pointLine.length];
        g.setColor(colorArray[2]);
        BasicStroke stroke = new BasicStroke(1);
        ((Graphics2D) g).setStroke(stroke);
        if (pointLine.length != 0) {
            int k = 0;
            while (k < pointLine.length) {
                if (PIX) {
                    //Координата x в контексте построения
                    int xPix = (int) pointLine[k];
                    //Реальная координата
                    double x = ((xPix - xcor[0]) / zoom1[0]);
                    xCoorLine[k] = new NumberR64(x);
                    if ((x > xPoint[0]) && (x < xPoint[xPoint.length - 1])) {
                        int[] number = findIntervalPoint(x);
                        double y = (((x - xPoint[number[0]]) / (xPoint[number[0]] - xPoint[number[1]])) * (yPoint[0][number[0]] - yPoint[0][number[1]]) + yPoint[0][number[0]]);
                        yCoorLine[k] = new NumberR64(y);
                        int yPix = (int) (-y * zoom2[0] + ycor[0]);
                        g.drawLine(xPix, 33, xPix, 540);
                        g.fillOval((int) (xPix - 3), (int) (yPix - 3), 6, 6);
                        page.ring.FLOATPOS = 3;
                        g.drawString("[" + new NumberR64(x).toString(page.ring) + " , " + new NumberR64(y).toString(page.ring) + "]", xPix - 3, yPix - 3);
                    } else {
                        if (x > xMin[0] && x < xMax[0]) {
                            g.drawLine(xPix, 33, xPix, 540);
                        }
                    }
                } else {
                    double x = pointLine[k];
                    int xPix = (int) (x * zoom1[0] + xcor[0]);
                    if ((x > xPoint[0]) && (x < xPoint[xPoint.length - 1])) {
                        int[] number = findIntervalPoint(x);
                        double y = (((x - xPoint[number[0]]) / (xPoint[number[0]] - xPoint[number[1]])) * (yPoint[0][number[0]] - yPoint[0][number[1]]) + yPoint[0][number[0]]);
                        int yPix = (int) (-y * zoom2[0] + ycor[0]);
                        g.drawLine(xPix, 33, xPix, ycor[0]);
                        g.fillOval((int) (xPix - 3), (int) (yPix - 3), 6, 6);
                        g.drawString("[" + x + " , " + y + "]", xPix - 3, yPix - 3);
                    } else {
                        if (x > xMin[0] && x < xMax[0]) {
                            g.drawLine(xPix, 33, xPix, ycor[0]);
                        }
                    }
                }
                k++;
            }
        }
        Element[][] xyCoorLine = new Element[2][];
        xyCoorLine[0] = xCoorLine;
        xyCoorLine[1] = yCoorLine;
        //Создание матрицы с точками пересечения вертикальных прямых и графика
        table.P = new MatrixD(xyCoorLine);
        stroke = new BasicStroke(3);
        ((Graphics2D) g).setStroke(stroke);
        page.ring.FLOATPOS = oldFloatPos;
    }

    /**
     * Определение номеров значений x1 и x2 между которыми находится x0
     *
     * @param x0 - точка через которую проводят вертикальную прямую
     *
     * @return - значения порядковых номеров точек
     */
    public int[] findIntervalPoint(double x0) {
        int x2Number = 0;
        for (int i = 0; i < xPoint.length; i++) {
            if (x0 < xPoint[i]) {
                x2Number = i;
                break;
            }
        }
        return new int[] {x2Number - 1, x2Number};
    }

    /**
     * Метод вычисления параметров-границ отрисовки графиков. Для функции
     * plotGraph.
     *
     * @param g - - Графический контекст
     */
    public void drawAllParamForPlotGraph(Graphics g) {
        //Нахождение максимального и минимального значения функций заданых явно
        xStart = xMin[0];
        xEnd = xMax[0];
        yStart = yMin[0];
        yEnd = yMax[0];
        for (int j = 0; j < arrFuncTable.length; j++) {
            Table t = ((Table) arrFuncTable[j]);
            for (int i = 0; i < t.M.M[0].length - 1; i++) {
                double xST = t.M.M[0][i].doubleValue();
                if (xST < xStart) {
                    xStart = xST;
                } else {
                    if (xST > xEnd) {
                        xEnd = xST;
                    }
                }
            }
            for (int i = 1; i <= t.M.M.length - 1; i++) {
                for (int z = 0; z < t.M.M[i].length - 1; z++) {
                    double yST = t.M.M[i][z].doubleValue();
                    if (yST < yStart) {
                        yStart = yST;
                    } else {
                        if (yST > yEnd) {
                            yEnd = yST;
                        }
                    }
                }
            }
        }
        yMin[0] = yStart;
        yMax[0] = yEnd;
        xMin[0] = xStart;
        xMax[0] = xEnd;
    }

    /**
     * Метод вычисления параметров-границ отрисовки графиков. Для функции
     * showPlots.
     *
     * @param g - - Графический контекст
     */
    public void drawAllParamForShowPlots(Graphics g) {
        if (isAutomatCalculation() == false) {
            yMin[0] = page.yMin;
            yMax[0] = page.yMax;
            xMin[0] = page.xMin;
            xMax[0] = page.xMax;
        } else {
            xStart = xMin[0];
            xEnd = xMax[0];
            yStart = yMin[0];
            yEnd = yMax[0];
            for (int i = 0; i <= this.func.X.length - 1; i++) {
                yStart = yPointFunc[i][0];
                xStart = xPointFuncShowPlots[i][0];
                for (int j = 0; j <= 499; j++) {
                    double yST = yPointFunc[i][j];
                    double xST = xPointFuncShowPlots[i][j];
                    if (yST < yStart) {
                        yStart = yST;
                    } else {
                        if (yST > yEnd) {
                            yEnd = yST;
                        }
                    }
                    if (xST < xStart) {
                        xStart = xST;
                    } else {
                        if (xST > xEnd) {
                            xEnd = xST;
                        }
                    }
                }
            }
            for (int i = 0; i <= arrFuncParam.length - 1; i++) {
                for (int j = 0; j <= 2 * NumbOfSteps - 1; j = j + 2) {
                    double xST = yPointFuncParam[i][j];
                    double yST = yPointFuncParam[i][j + 1];
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
            for (int j = 0; j < arrFuncTable.length; j++) {
                Table t = ((Table) arrFuncTable[j]);
                for (int i = 0; i < t.M.M[0].length; i++) {
                    double xST = t.M.M[0][i].doubleValue();
                    if (xST < xStart) {
                        xStart = xST;
                    } else {
                        if (xST > xEnd) {
                            xEnd = xST;
                        }
                    }
                }
                for (int i = 1; i <= t.M.M.length - 1; i++) {
                    for (int z = 0; z < t.M.M[i].length; z++) {
                        double yST = t.M.M[i][z].doubleValue();
                        if (yST < yStart) {
                            yStart = yST;
                        } else {
                            if (yST > yEnd) {
                                yEnd = yST;
                            }
                        }
                    }
                }
            }
            //
            for (int j = 0; j < arrFuncPointsTable.length; j++) {
                Table t = ((Table) arrFuncPointsTable[j]);
                for (int i = 0; i < t.M.M[0].length; i++) {
                    double xST = t.M.M[0][i].doubleValue();
                    if (xST < xStart) {
                        xStart = xST;
                    } else {
                        if (xST > xEnd) {
                            xEnd = xST;
                        }
                    }
                }
                for (int i = 1; i <= t.M.M.length - 1; i++) {
                    for (int z = 0; z < t.M.M[i].length; z++) {
                        double yST = t.M.M[i][z].doubleValue();
                        if (yST < yStart) {
                            yStart = yST;
                        } else {
                            if (yST > yEnd) {
                                yEnd = yST;
                            }
                        }
                    }
                }
            }
            if (yStart < -100) {
                yStart = -Math.abs(xEnd - xStart) / 2;
            }
            if (yEnd > 100) {
                yEnd = Math.abs(xEnd - xStart) / 2;
            }
            yMin[0] = yStart;
            yMax[0] = yEnd;
            xMin[0] = xStart;
            xMax[0] = xEnd;
        }
    }

    /**
     * Округление значений числовых подписей для осевых рисок.
     *
     * @param x - числовое значение подписи
     *
     * @return - округленное значение
     */
    private double[] roundScale(double x) {
        double t = 1;
        int k = 0;
        if ((x > 0) && (x < 1)) {
            while (x < 1) {
                x = x * 10;
                t = t / 10;
                k++;
            }
        } else {
            if (x > 10) {
                while (x > 10) {
                    x = x / 10;
                    t = t * 10;
                    k--;
                }
            }
        }
        x = Math.round(x);
        x = x * t;
        return new double[] {x, (k > 0) ? k : 0};
    }

    /**
     * Вычисление сдвигов по горизонтали
     */
    public void coeffG() {
        if (pointsK2 == null) {
            for (int i = 0; i < k2.length; i++) {
                k2[i] = 0;
            }
        } else {
            for (int i = 0; i < pointsK2.length; i++) {
                k2[i] = -(int) pointsK2[i].value * TEXTSIZE;
            }
        }
    }

    /**
     * Вычисление сдвигов по вертикали
     */
    public void coeffV() {
        if (pointsK1 == null) {
            for (int i = 0; i < k1[0].length; i++) {
                k1[0][i] = 0;
                k1[1][i] = -6;
            }
        } else {
            for (int i = 0; i < pointsK1.length; i++) {
                int k = (int) pointsK1[i].value;
                switch (k) {
                    case 0:
                    case 8: {
                        k1[0][i] = 0;
                        k1[1][i] = -TEXTSIZE;
                        break;
                    }
                    case 1: {
                        k1[0][i] = TEXTSIZE / 2;
                        k1[1][i] = -TEXTSIZE / 2;
                        break;
                    }
                    case 2: {
                        k1[0][i] = TEXTSIZE;
                        k1[1][i] = 3;
                        break;
                    }
                    case 3: {
                        k1[0][i] = TEXTSIZE / 2 + 3;
                        k1[1][i] = TEXTSIZE / 2 + 3;
                        break;
                    }
                    case 4: {
                        k1[0][i] = 0;
                        k1[1][i] = TEXTSIZE + 3;
                        break;
                    }
                    case 5: {
                        k1[0][i] = -TEXTSIZE / 2 - 3;
                        k1[1][i] = TEXTSIZE / 2 + 3;
                        break;
                    }
                    case 6: {
                        k1[0][i] = -TEXTSIZE;
                        k1[1][i] = 3;
                        break;
                    }
                    case 7: {
                        k1[0][i] = -TEXTSIZE / 2;
                        k1[1][i] = -TEXTSIZE / 2;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Метод отрисовки режимов dash, arrow, dashAndArrow в Plot, ParamPlot,
     * TablePlot
     *
     * @param g - графический контекст 
     * @param coord - [x1,y1,x2,y2] - координаты двух точек для отрисовки линии
     * @param i - идентификатор порядкового номера
     * @param isTablePlot - boolean флаг для TablePlot
     */
    public void dashAndArrow(Graphics g, int[] coord, int i, boolean isTablePlot) {
        if (isTablePlot == true) {
            //случай табличных графиков
            if (DASH == true) {
                float[] dashPattern = {3f, 30f};
                Graphics2D g2 = ((Graphics2D) g);
                BasicStroke bss = (BasicStroke) g2.getStroke();
                BasicStroke bs = new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10f, dashPattern, 0);
                g2.setStroke(bs);
                g = g2;
                g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                g2.setStroke(bss);
                g = g2;
            } else {
                if (DASHANDARROW == true) {
                    float[] dashPattern = {3f, 30f};
                    Graphics2D g2 = ((Graphics2D) g);
                    BasicStroke bss = (BasicStroke) g2.getStroke();
                    BasicStroke bs = new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10f, dashPattern, 0);
                    g2.setStroke(bs);
                    g = g2;
                    g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                    g2.setStroke(bss);
                    g = g2;
                    drawArrow(g, coord[0], coord[1], coord[2], coord[3]);
                } else {
                    if (ARROW == true) {
                        g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                        drawArrow(g, coord[0], coord[1], coord[2], coord[3]);
                    } else {
                        g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                    }
                }
            }
        } else {
            if (DASH == true) {
                if (i % 2 == 0) {
                    g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                }
            } else {
                if (DASHANDARROW == true) {
                    if (i % 2 == 0) {
                        g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                    }
                } else {
                    if (ARROW == true) {
                        g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                    } else {
                        g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                    }
                }
            }
        }
    }

    /**
     * Метод отрисовки режимов dash, arrow, dashAndArrow в showPlots
     *
     * @param g
     * @param style - режим (dash, arrow, dashAndArrow)
     * @param coord - [x1,y1,x2,y2]
     * @param i
     * @param isTablePlot
     */
    public void dashAndArrowForShowPlots(Graphics g, String style, int[] coord, int i, boolean isTablePlot) {
        if (isTablePlot == true) {
            //случай табличных графиков
            if (style.equals("dash")) {
                float[] dashPattern = {3f, 30f};
                Graphics2D g2 = ((Graphics2D) g);
                BasicStroke bss = (BasicStroke) g2.getStroke();
                BasicStroke bs = new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10f, dashPattern, 0);
                g2.setStroke(bs);
                g = g2;
                g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                g2.setStroke(bss);
                g = g2;
            } else {
                if (style.equals("dashAndArrow")) {
                    float[] dashPattern = {3f, 30f};
                    Graphics2D g2 = ((Graphics2D) g);
                    BasicStroke bss = (BasicStroke) g2.getStroke();
                    BasicStroke bs = new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10f, dashPattern, 0);
                    g2.setStroke(bs);
                    g = g2;
                    g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                    g2.setStroke(bss);
                    g = g2;
                    drawArrow(g, coord[0], coord[1], coord[2], coord[3]);
                } else {
                    if (style.equals("arrow")) {
                        g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                        drawArrow(g, coord[0], coord[1], coord[2], coord[3]);
                    } else {
                        g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                    }
                }
            }
        } else {
            if (style.equals("dash")) {
                if (i % 55 == 0) {
                    g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                }
            } else {
                if (style.equals("dashAndArrow")) {
                    if (i % 55 == 0) {
                        g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                    }
                    //  drawArrow(g, coord[0], coord[1], coord[2], coord[3]);
                } else {
                    if (style.equals("arrow")) {
                        g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                        //   drawArrow(g, coord[0], coord[1], coord[2], coord[3]);
                    } else {
                        g.drawLine(coord[0], coord[1], coord[2], coord[3]);
                    }
                }
            }
        }
    }

    /**
     * Устанавливает настройки для режима - "равный масштаб"
     */
    public void isEqualsScale() {
        if (EQUALSCALE == true) {
            for (int i = 0; i < zoom2.length; i++) {
                zoom1[i] = zoom2[i];
                hOx[i] = hOy[i];
            }
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

    /**
     * Метод вычисляет новые координаты графа с использованием силовых
     * алгоритмов
     *
     * @param A - матрица смежности
     * @param ring - кольцо
     * @param k - коэффициент
     * @param coord - массив координат
     *
     * @author repin
     */
    public void shiftOfCoordinats(MatrixD A, double k, double[][] coord, Ring ring) {
        int numbVert = coord[0].length;
        double r, p, s, s1, s2, f0 = 0, f1 = 0, x[] = coord[0], y[] = coord[1];
        int i;
        int j;
        for (i = 0; i < numbVert; i++) {
            s1 = coord[0][i];
            s2 = coord[1][i];
            for (j = 0; j < numbVert; j++) {
                r = Math.sqrt(Math.pow((x[i] - x[j]), 2) + Math.pow((y[i] - y[j]), 2));
                if (r > 0 && r != 1) {
                    if ((r < 1) && A.M[i][j].isZero(ring)) {
                        p = Math.pow((1 - r), 2);
                        f0 = k * p * (x[i] - x[j]) / Math.pow(r, 2);
                        f1 = k * p * (y[i] - y[j]) / Math.pow(r, 2);
                        s1 = s1 + f0;
                        s2 = s2 + f1;

                    } else if (!A.M[i][j].isZero(ring)) {
                        p = Math.pow((1 - r), 5);
                        f0 = k * p * (x[i] - x[j]) / Math.pow(r, 5);
                        f1 = k * p * (y[i] - y[j]) / Math.pow(r, 5);
                        s1 = s1 + f0;
                        s2 = s2 + f1;

                    }
                }
            }
            coord[0][i] = s1;
            coord[1][i] = s2;
        }
    }

    /**
     * Главный метод отрисовки всего набора входных данных
     *
     * @param g - Графический контекст
     */
    public void drawAllGraphics(Graphics g) {
        g.drawLine(200, 0, 200, 700);
        if (PLOTSNUMBER[0]) {
            drawAllParamForPlot(g);
        }
        if (PLOTSNUMBER[1]) {
            drawAllParamForParamPlot(g);
        }
        if (PLOTSNUMBER[2]) {
            drawAllParamStepOne(g);
        }
        if (PLOTSNUMBER[3]) {
            if (PLOTGRAPH) {
                drawAllParamForPlotGraph(g);
            } else {
                drawAllParamForShowPlots(g);
            }
        }
        initIntervalOx(g);
        initIntervalOy(g);
        drawSystemCor(g);
        if (PLOTSNUMBER[3]) {
            if (PLOTGRAPH) {
                if (zoom1[0] > zoom2[0]) {
                    zoom1[0] = zoom2[0];
                } else {
                    zoom2[0] = zoom1[0];
                }
            }
        }
        isEqualsScale();
        drawOxOy1(g);
        ((Graphics2D) g).setStroke(new BasicStroke(WIDTH_LINE));
        if (PLOTSNUMBER[0]) {
            drawPointForPlot(g);
            drawAllCaptionForPlot(g);
        }
        if (PLOTSNUMBER[1]) {
            drawPointForParamPlot(g);
            drawAllCaptionForParamPlot(g);
        }
        if (PLOTSNUMBER[2]) {
            drawAllPointForTablePlot(g);
            drawAllCaptionForTablePlot(g);
            if (drawLinesFlag) {
                addDrawLines(g);
            }
        }
        if (PLOTSNUMBER[3]) {
            if (PLOTGRAPH) {
                drawAllPoinFortPlotGraph(g);
                drawAllCaptionForPlotGraph(g);
            } else {
                drawAllPointForShowPlots(g);
                drawAllCaptionForShowPlots(g);
                if (s != null) {
                    drawPointForTextPlot(g);
                }
            }
        }
        if (PLOTSNUMBER[4]) {
            drawPointForTextPlot(g);
        }
    }

    /**
     * Основная процедура отрисовки
     *
     * @param g - Графический контекст
     */
    @Override
    public void paint(Graphics g) {
        try {
            System.setProperty("java.awt.headless", "true");
            BufferedImage image = new BufferedImage(1366, 700, BufferedImage.TYPE_INT_RGB);//1366 700
            g = image.getGraphics();
            BasicStroke stroke = new BasicStroke(WIDTH_AXES);
            ((Graphics2D) g).setStroke(stroke);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            RescaleOp rop = new RescaleOp(1.1f, 20.0f, null);
            ((Graphics2D) g).drawImage(image, rop, 0, 0);
            g.setColor(colorArray[4]);
            g.fillRect(0, 0, 1366, 700);
            g.setColor(colorArray[3]);
            g.setFont(new Font(Font.DIALOG, Font.PLAIN, TEXTSIZE));
            //явные, параметрические, текст, showPlots, графы
            if (PLOTSNUMBER[0] || PLOTSNUMBER[1] || PLOTSNUMBER[3] || PLOTSNUMBER[4]) {
                drawAllGraphics(g);
            }
            //Табличные графики
            if (PLOTSNUMBER[2]) {
                if (pointPlotting) {
                    coeffG();
                    coeffV();
                }
                switch (options) {
                    case 0: {
                        //drawAllGraphicsForTablePlot(g);
                        drawAllGraphics(g);
                        break;
                    }
                    case -1: {
                        options = 0;
                        // drawAllGraphicsForTablePlot(g);
                        drawAllGraphics(g);
                        break;
                    }
                }
            }
            g.setColor(colorArray[3]);
            if (forWeb) {
                page.putImage(image);
            } else {
                System.out.println("IMAGE TO FILE");
                OutputStream os = new BufferedOutputStream(new FileOutputStream(new File(path)));
                try {
                    javax.imageio.ImageIO.write(image, "PNG", os);
                } finally {
                    os.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String path1 = System.getProperty("user.home") + "/image1.png";
        String path2 = System.getProperty("user.home") + "/image2.png";
        String path3 = System.getProperty("user.home") + "/image3.png";
        Ring r = new Ring("R64[x,y,t]");
        long[][] a = new long[][] {
            {0, 1, 0, 0, 1, 1, 0},
            {1, 0, 0, 1, 0, 1, 0},
            {0, 0, 0, 0, 0, 1, 1},
            {0, 1, 0, 0, 1, 1, 0},
            {1, 0, 0, 1, 0, 0, 1},
            {1, 1, 1, 1, 0, 0, 1},
            {0, 0, 1, 0, 1, 1, 0}};
        MatrixD A = new MatrixD(a, r);
        double coords[][] = new double[][] {{5, 2, 7, 2, 6, 9, 4}, {1, 4, 2, 3, 8, 3, 2}};
        double k = 1;
        Plots p = new Plots();
        p.shiftOfCoordinats(A, k, coords, r);
        System.out.println("1" + Array.toString(coords));
        MatrixD Coords = new MatrixD(coords, r);
        F.drawGraph(A, Coords, path1);
        System.out.println("A=" + A);
        p.shiftOfCoordinats(A, k, coords, r);
        System.out.println("2" + Array.toString(coords));
        MatrixD Coords1 = new MatrixD(coords, r);
        F.drawGraph(A, Coords1, path2);
        p.shiftOfCoordinats(A, k, coords, r);
        System.out.println("3" + Array.toString(coords));
        MatrixD Coords2 = new MatrixD(coords, r);
        F.drawGraph(A, Coords2, path3);
    }
}
