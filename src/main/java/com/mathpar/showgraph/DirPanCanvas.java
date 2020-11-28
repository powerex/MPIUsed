/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.showgraph;

import com.mathpar.number.Ring;
import java.awt.*;
//import javax.swing.*;
import com.mathpar.func.*;
import com.mathpar.func.Page;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import com.mathpar.number.Element;
import com.mathpar.number.NumberR64;

/**
 *
 * @author andy
 */
public class DirPanCanvas  extends Canvas{


     static String HOME_DIR = System.getProperty("java.io.tmpdir") + File.separator;
    F f1 = new F();
    Element[] f2 = new Element[0];
    /** Массив функций, заданных параметрически [0,0]=x0(t), [0,1]=y0(t), [1,0]=x1(t), [1,1]=y1(t),....  */
    String[][] MassParamFuncs;
    /** левая граница интервала аргумента  Х */
    double xMin;
    /** правая граница интервала аргумента Х */
    double xMax;
    /** Число дискретных значений аргумента */
    int NumbOfSteps = 32000;
    /** Автоматический расчет интервала значений по оси ординат */
    boolean AvtoMatick = false;
    /** нижняя граница интервала Y */
    double yMin;
    /** верхняя граница интервала Y */
    double yMax;
    /** Таблица значений Y */
    double yR[][] = new double[][]{};
    /** Таблица значения X */
    double xR[][] = new double[][]{};
    /** левая граница интервала аргумента Т */
    double tMin;
    /** правая граница интервала аргумента Т */
    double tMax;
    /** Коэффициэнт увеличения(уменьшения) по Х */
    double zoom1;
    /**Коэффициэнт увеличения(уменьшения) по Y */
    double zoom2; //
    /** Координаты центра начала координат */
    int xcor = 836;
    int ycor = 300;
    /** Корректирующий коэффициент по Y */
    int yCorrect = 0;
    /** Корректирующий коэффициент по Х */
    int xCorrect = 0;
    /** Параметры автоматического построения графиков */
    double xStart = 0;
    double xEnd = 0;
    double yStart = 0;
    double yEnd = 0;
    /** Переменная в которой храняться аналитические функции */
    Page page;
    /** Массив значений аналитической функции, в столбцах номера функций, в строках их значения */
    double[][] MassVich;
    /** Переменная в которой храняться параметрические функции */
    Page pageParam;// = new Page();
    /** Массив значений параметрической функции, в столбцах номера функций, в строках их значения*/
    double[][] MassParamVich;
    boolean parcaPort=false;
    int sect;
    String id;
    String x_str;
    String y_str;
    String title_str;
    //--------------------------------------------------------------------------------------------------------------

    public DirPanCanvas() {
    }

     public DirPanCanvas(Page pageObj, double xMin, double xMax, double yMin, double yMax,boolean parcaPort, int sect, String id, String x_str, String y_str, String title_str) throws Exception {
        //super("2D Визуализатор графиков функций");
        this.parcaPort=parcaPort;
        this.sect=sect;
        this.id=id;
        this.x_str=x_str;
        this.y_str=y_str;
        this.title_str=title_str;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.page = pageObj;
        MassVich = funcToMass();
        pageParam = new Page();
        MassParamVich = funcParamToMass();
        Canvas cs=new Canvas(){

            @Override
            public void paint(Graphics g) {
                super.paint(g);
            }

        };
    }


      //-----------------------------------------------------------------------------------------------------------------
    /**
     * Процедура вычисления значения функции, заданой аналитически
     * @param x - значение x, для которого нам необходимо вычислить значение F(x)
     * @param i - номер функции, для которой нам необходимо найти значение
     * @param page - список функций
     * @return y - Значение F(x)
     * @throws java.lang.Exception
     */
    public double VichFunc(double x, int i, Page page) throws Exception {
         Ring ring=new Ring("R64[x]");
        Object obj = page.valueOf(page, i,
                new Element[]{new NumberR64(x)},ring);
        if (obj instanceof F) {
            return ((F) page.valueOf(page, i,
                    new Element[]{new NumberR64(x)},ring)  ).doubleValue();//если результат TF
        }
        return ((NumberR64) page.valueOf(page, i,
                new Element[]{new NumberR64(x)},ring)  ).doubleValue();

    }

    /**
     * Процедура добавления в массив massOfNumbers значений функций
     * @return massOfNumbers - массив значений функций
     * @throws java.lang.Exception
     */
    public double[][] funcToMass() throws Exception {
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
     * @param i - номер функции
     * @param j - значение в точке
     * @return MassVich - массив значений функций
     * @throws java.lang.Exception
     */
    public double funcToVich(int i, int j) throws Exception {
        return MassVich[i][j];
    }

    //-----------------------------------------------------------------------------------------------------------------
    /**
     * Процедура вычисления значений параметрической функции
     * @param t - значение t, для которого нам необходимо вычислить значение X(t) или Y(t)
     * @param i - номер функции
     * @param pageParam - список функций
     * @return y - значение X(t) или Y(t)
     * @throws java.lang.Exception
     */
    public double VichFuncParam(double t, int i, Page pageParam) throws
            Exception { Ring ring=new Ring("R64[x]");
        Object obj = pageParam.valueOf(pageParam, i,   new Element[]{new NumberR64(t)},ring);
        if (obj instanceof F) {
            return ((F) pageParam.valueOf(pageParam, i,
                    new Element[]{new NumberR64(t)},ring)  ).doubleValue();
        }
        return ((NumberR64) (pageParam.valueOf(pageParam, i,
                new Element[]{new NumberR64(t)},ring)  )).doubleValue();
    }

    /**
     * Процедура добавления в массив massParamNumbers значений функций
     * @return massParamNumbers - массив значений параметрических функций вида
     * [i][j], где i - номер функции, j значение X(t), а j+1 значение Y(t)
     * @throws java.lang.Exception
     */
    public double[][] funcParamToMass() throws Exception {
        double[][] massParamNumbers = new double[this.pageParam.expr.size()][2 * NumbOfSteps];
        int xy = 0;
        double tPer;
        for (int i = 0; i <= this.pageParam.expr.size() / 2 - 1; i++) {
            tPer = tMin;
            xy = i * 2;
            for (int j = 0; j <= 2 * NumbOfSteps - 1; j = j + 2) {
                massParamNumbers[i][j] = VichFuncParam(tPer, xy, pageParam);
                massParamNumbers[i][j + 1] = VichFuncParam(tPer, xy + 1, pageParam);
                tPer = tPer + (tMax - tMin) / (NumbOfSteps);
            }
        //xy = xy + 1;
        }

        return massParamNumbers;
    }

    /**
     * Процедура вызова значений функций
     * @param i - номер функции
     * @param j - j значение X(t), а j+1 значение Y(t)
     * @return MassParamVich - массив значений функций
     * @throws java.lang.Exception
     */
    public double funcParamToVich(int i, int j) throws Exception {
        return MassParamVich[i][j];
    }

    /**
     * Процедура расчета коэффициэнта наклона прямой для графиков ф-ций заданных таблицей значений
     * @param i - номер строки таблицы
     * @param a1 - номер столбца таблицы, где храниться значение х1
     * @param a2 - номер столбца таблицы, где храниться значение х2
     * @return k1 - коэффициэнт наклона прямой
     */
    public double k(int i, int a1, int a2) {
        double k1 = 0;
        k1 = (yR[i][a1] - yR[i][a2]) / (xR[i][a1] - xR[i][a2]);
        return k1;
    }

    /**
     * Процедура расчета сдвига прямой по оси ох для графиков ф-ций заданных таблицей значений
     * @param i - номер строки таблицы
     * @param a1 - номер столбца таблицы, где храниться значение х1
     * @param a2 - номер столбца таблицы, где храниться значение х2
     * @return B1 - сдвиг прямой по оси ох
     */
    public double L(int i, int a1, int a2) {
        double B1 = 0;
        B1 = (xR[i][a1] * yR[i][a2] - yR[i][a1] * xR[i][a2]) /
                (xR[i][a1] - xR[i][a2]);
        return B1;
    }



    @Override
    public void paint(Graphics g) {
        try {
            System.setProperty("java.awt.headless", "true");

            BufferedImage image=new BufferedImage(1366, 700, BufferedImage.TYPE_INT_RGB);
            if(parcaPort){
               g=image.getGraphics();
            }
            long t1 = System.currentTimeMillis();
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
            //System.out.println(str.length +" "+NumbGrA+" "+NumbOfSteps);

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
            System.out.println("Промежуток по "+this.x_str+" = [" + xStart + "," + xEnd +
                    "]");
            System.out.println("Промежуток по "+this.y_str+" = [" + yStart + "," + yEnd +
                    "]");

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
            double b = xMaxs; //10;
            double yk = 0;
            double xk = a;
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
            double xkk = xk;
            double xna = 0;
            double yna = 0;
            double tna = tMin;
            int AllNumbPlots = f1.X.length + f2.length +
                    xR.length;
            double yobr = yMax;

            //Вывод насечек на ось 0х
            for (int j = 0; j <= 20; j++) {
                xkk = xMin;
                g.setColor(arr[7]);
                g.drawLine(
                        xviv,
                        (ycor - 5),
                        xviv,
                        (ycor + 5));
                g.setColor(arr[5]);
                xkk = Math.round(1000 * (xkk + j * (xMax - xMin) / 20)) /
                        1000.0;
                //System.err.println(xkk);
                if ((xkk) != 0) {
                    if (xkk == Math.round(xkk)) {
                        g.drawString(new String("" + (int) xkk),
                                (int) (xviv - 8), (ycor + 17));
                    } else {
                        g.drawString(new String("" + xkk), (int) (xviv - 8),
                                (ycor + 17));
                    }
                }

                xviv = xviv + 40;
            }

            //Вывод насечек на ось 0у
            double yvv = 0;
            for (int t = 0; t <= 20; t++) {
                g.setColor(arr[7]);
                g.drawLine(
                        (xcor - 5),
                        (int) (t * 20 + 100),
                        (xcor + 5),
                        (int) (t * 20 + 100));
                g.setColor(arr[5]);
                if (yobr != 0) {
                    yvv = Math.round(1000 * (yobr)) / 1000.0;
                    if (yvv == Math.round(yvv)) {
                        g.drawString(new String("" + (int) (yvv)), xcor - 30,
                                (int) (t * 20 + 105));
                    } else {
                        g.drawString(new String("" + (yvv)), xcor - 30,
                                (int) (t * 20 + 105));
                    }
                }
                yobr = yobr - (yMax - yMin) / 20;
            //System.out.println(t*20+105);
            }
            ycor = ycor + yCorrect;
            xcor = xcor - xCorrect;

            //Вывод функций, заданной явно, на экран
            if (f1.X.length > 0) {
                for (int j = 0; j <= f1.X.length - 1; j++) {
                    xk = xMins;
                    for (int i = 0; i <= NumbOfSteps - 1; i++) {
                        //yk = func(xk, j);
                        yk = funcToVich(j, i);
                        xk = xk + (xMaxs - xMins) / NumbOfSteps;
                        //System.out.println(xgr + "  "+ yk);
                        g.setColor(arr[j]);
                        if ((yk <= yMax) && (yk >= yMin)) {
                            g.fillOval((int) (xk * zoom1 + xcor), (int) (-yk * zoom2 + ycor), 3, 3);
                        }
                    }
                    g.drawString("График ф-ции: "+this.y_str+" = " + f1.X[j].toString(), 20,
                            50 + (j + 1) * 20);
                }
            }else{
                if((f1.name!=F.ABS) && (f1.name != F.VECTORS)){
               xk = xMins;
                    for (int i = 0; i <= NumbOfSteps - 1; i++) {
                        //yk = func(xk, j);
                        yk = funcToVich(0, i);
                        xk = xk + (xMaxs - xMins) / NumbOfSteps;
                        //System.out.println(xgr + "  "+ yk);
                        g.setColor(arr[0]);
                        if ((yk <= yMax) && (yk >= yMin)) {
                            g.fillOval((int) (xk * zoom1 + xcor), (int) (-yk * zoom2 + ycor), 3, 3);
                        }
                    }
                    g.drawString("График ф-ции: "+this.y_str+" = " + f1.toString(), 20,
                            50 + (0 + 1) * 20);}
            }
            //System.out.println(j);
            //Вывод функций, заданных параметрически, на экран
            int k = 0;
            for (int j = 0; j <= f2.length / 2 - 1; j++) {
                g.setColor(arr[j + f1.X.length]);
                g.drawString("График параметрической: "+this.x_str+" = " +
                        f2[k].toString() +
                        " "+this.y_str+" = " + f2[k + 1].toString(),
                        20, 50 + (f1.X.length + 1 + j) * 20);
                k = k + 2;
                for (int i = 0; i <= 2 * NumbOfSteps - 1; i = i + 2) {
                    xna = funcParamToVich(j, i);
                    yna = funcParamToVich(j, i + 1);


                    if ((xna <= xMax) && (xna >= xMin)) {
                        if ((yna <= yMax) && (yna >= yMin)) {
                            g.fillOval((int) (xna * zoom1 + xcor), (int) (-yna * zoom2 + ycor), 3, 3);

                        }
                    }

                }
            }

            //Построение графиков функций заданных таблицей значений
            for (int j = 1; j <= xR.length; j++) {
                g.setColor(arr[j + f2.length +
                        f1.X.length]);
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
                        if ((xp1 <= xMax) && (xp1 >= xMin)) {
                            if ((yp1 <= yMax) && (yp1 >= yMin)) {
                                g.fillOval((int) (xp1 * zoom1 + xcor), (int) (-yp1 * zoom2 + ycor), 3, 3);

                            }
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
                    g.setColor(arr[f1.X.length + f2.length +
                            countTableF]);
                    g.drawString("Таблица: ", 20, yTl);
                    int xTl = 30;
                    for (int scb = 1; scb <= xR[countTableF - 1].length; scb++) {
                        if (xTl < 300) {
                            xTl = xTl + 60;
                        } else {
                            xTl = 90;
                            yTl = yTl + 20;

                        }
                        g.drawString(new String("[" + (int) xR[countTableF - 1][scb - 1] + "," +
                                (int) yR[countTableF - 1][scb - 1] + "]"), xTl,
                                yTl);
                    }
                    yTl = yTl + 20;
                }
            }
            g.setColor(arr[5]);
            long t2 = System.currentTimeMillis();
            System.err.println("=====" + (t2 - t1));
            if(parcaPort){
                //javax.imageio.ImageIO.write(resizeImage(image,683,350), "PNG", new File("/home/andy/"+id+sect+".png"));
                javax.imageio.ImageIO.write(image, "PNG", new File(HOME_DIR+""+id+sect+".png"));
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

        double scalex = (double) target.getWidth()/ image.getWidth();
        double scaley = (double) target.getHeight()/ image.getHeight();

        AffineTransform xform = AffineTransform.getScaleInstance(scalex, scaley);
        g2.drawRenderedImage(image, xform);
        g2.dispose();
        return target;
    }

}
