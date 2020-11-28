/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D;

import com.mathpar.func.Page;
import java.awt.*;
import java.util.ArrayList;

/**
 * Класс отвечает за создание объетков,заданных пользователем,а так же содержит
 * методы для обновления изображения и нахождения экстремумов.
 *
 * @author JuliaGrishina
 */
public final class RenderWindow extends Canvas {

    /**
     * параметр прозрачности объектов
     */
    public static int tspValue = 100000000;
    public GM_RenderZBuffer render;
    /**
     * реальный экстремум системы объектов по соответсвующей координате.не
     * меняется при сжатии
     */
    double maxX_name, maxY_name, maxZ_name, minX_name, minY_name, minZ_name;
    /**
     * текущий экстремум системы объектов по соответсвующей координате. меняется
     * при сжатии
     */
    public double maxX, maxY, maxZ, minX, minY, minZ;
    /**
     * флаг действия axes показать/скрыть оси координат
     */
    public boolean press = false;
    /**
     * флаг действия mesh показать/скрыть сетку объекта
     */
    public boolean meshObject = false;
    /**
     * флаг действия pic3D смена режима просмотра изображения (3D\стд)
     */
    public boolean vision3D = false;
    public ArrayList<Double> MaximumX = new ArrayList<Double>();
    public ArrayList<Double> MaximumY = new ArrayList<Double>();
    public ArrayList<Double> MaximumZ = new ArrayList<Double>();
    public ArrayList<Double> MinimumX = new ArrayList<Double>();
    public ArrayList<Double> MinimumY = new ArrayList<Double>();
    public ArrayList<Double> MinimumZ = new ArrayList<Double>();
    public ArrayList<Double> MaxX = new ArrayList<Double>();
    public ArrayList<Double> MaxY = new ArrayList<Double>();
    public ArrayList<Double> MaxZ = new ArrayList<Double>();
    public ArrayList<Double> MinX = new ArrayList<Double>();
    public ArrayList<Double> MinY = new ArrayList<Double>();
    public ArrayList<Double> MinZ = new ArrayList<Double>();
    public Color bgColor = Color.white;
    public Color txtColor = Color.black;
    /**
     * коэффициент сжатия пространства вдоль \соответствующей оси координат
     */
    double kx = 1, ky = 1, kz = 1;
    static int width, height;
    boolean isForWeb;
    Page page;

    RenderWindow() {
    }

    /**
     * Создает объект класса GM_REnderZBuffer и создает объекты типа GM_Figure
     * заданные пользователем
     *
     * @param width ширина
     * @param height высота
     * @param plot3dZ флаг типа объекта true= поверхность задана явно
     * @param plot3dXYZ флаг типа объекта true= поверхность задана
     * параметрически
     * @param Sx массив строк уравнений для координаты x
     * @param Sy массив строк уравненийдля координаты y
     * @param Sz массив строк уравнений для координаты z
     * @param Umin минимальное значение параметра u
     * @param Umax максимальное значение параметра u
     * @param Vmin минимальное значение параметра v
     * @param Vmax максимальное значение параметра v
     * @param u_num количество шагов по u
     * @param v_num количество шагов по v
     * @param color цвет
     * @param mesh флаг показать/скрыть сетку
     * @param stereo {@code true}, если рисуем стереоизображение
     * @param axes флаг показать/скрыть оси координат
     * @param isForWeb
     * @param page
     */
    public RenderWindow(int width, int height, boolean plot3dZ, boolean plot3dXYZ,
            String[] S, String[] Sx, String[] Sy, String[] Sz, double Umin, double Umax,
            double Vmin, double Vmax, int u_num, int v_num, Color color,
            boolean stereo, boolean axes, boolean mesh, Page page, boolean isForWeb) {
        RenderWindow.width = width;
        RenderWindow.height = height;
        this.page = page;
        this.isForWeb = isForWeb;

        render = new GM_RenderZBuffer(this, width, height, plot3dZ, plot3dXYZ);

        if (plot3dXYZ) {
            for (int i = 0; i < Sz.length; i++) {//Sz.length - 1
                GM_Figure gM_Figure = new GM_Figure(this, render.s, new GM_Color(color),
                        Sx[i], Sy[i], Sz[i], Umin, Umax, Vmin, Vmax, u_num, v_num);
                if (stereo) {
                    GM_Figure gM_FigureL = new GM_Figure(render.s1,
                            new GM_Color(new Color(color.getRed(), 0, 0)),
                            gM_Figure.Object, page, isForWeb);
                    GM_Figure gM_FigureR = new GM_Figure(render.s2,
                            new GM_Color(new Color(0, color.getGreen(),
                                            color.getBlue())), gM_Figure.Object, page, isForWeb);
                }
            }
        }
        if (plot3dZ) {
            for (int i = 0; i < S.length; i++) {
                GM_Figure gM_Figure = new GM_Figure(this, render.s,
                        new GM_Color(color), S[i], Umin, Umax, Vmin, Vmax, u_num,
                        v_num);
                if (stereo) {
                    GM_Figure gM_FigureL = new GM_Figure(render.s1,
                            new GM_Color(new Color(color.getRed(), 0, 0)),
                            gM_Figure.Object, page, isForWeb);
                    GM_Figure gM_FigureR = new GM_Figure(render.s2,
                            new GM_Color(new Color(0, color.getGreen(), color.getBlue())),
                            gM_Figure.Object, page, isForWeb);
                }
            }
        }
        if (axes) {
            press = true;
        }
        if (mesh) {
            meshObject = true;
        }
        if (stereo) {
            vision3D = true;
            press = false;
            bgColor = Color.black;
        }
        GM_Figure light = new GM_Figure(page, isForWeb, render.Scn,
                render.light.x, render.light.y, render.light.z, GM_Figure.light);
        GM_Figure viewer = new GM_Figure(page, isForWeb, render.Scn,
                render.viewer.x, render.viewer.y, render.viewer.z, GM_Figure.viewer);
    }

    /**
     * Метод обновления содержимого окна
     *
     * @param render объект GM_RenderZBuffer
     * @param isSizeChanged изменены ли размеры изображения до вызова этого
     * метода.
     * @param isScaleChanged изменен ли масштаб по одной из осей до вызова этого
     * метода.
     */
    public void update(GM_RenderZBuffer render, boolean isSizeChanged, boolean isScaleChanged) {
        GetExtrem();
        Graphics g2d = this.getGraphics();
        render.draw(g2d, isSizeChanged, isScaleChanged);
    }

    /**
     * Выбор экстремумов всей системы построенных объектов из списка экстремумов
     * всех объектов. Списки экстремумов заполняются при построении объекта.
     */
    public void GetExtrem() {
        for (int i = 0; i < MaximumX.size(); i++) {
            if (i == 0) {
                maxX = MaximumX.get(i);
                maxX_name = MaxX.get(i);
            }
            if (MaximumX.get(i) > maxX) {
                maxX = MaximumX.get(i);

            }
            if (MaxX.get(i) > maxX_name) {
                maxX_name = MaxX.get(i);
            }
        }
        for (int i = 0; i < MaximumY.size(); i++) {
            if (i == 0) {
                maxY = MaximumY.get(i);
                maxY_name = MaxY.get(i);
            }
            if (MaximumY.get(i) > maxY) {
                maxY = MaximumY.get(i);

            }
            if (MaxY.get(i) > maxY_name) {
                maxY_name = MaxY.get(i);
            }
        }
        for (int i = 0; i < MinimumX.size(); i++) {
            if (i == 0) {
                minX = MinimumX.get(i);
                minX_name = MinX.get(i);
            }
            if (MinimumX.get(i) < minX) {
                minX = MinimumX.get(i);
            }
            if (MinX.get(i) < minX_name) {
                minX_name = MinX.get(i);
            }
        }
        for (int i = 0; i < MinimumY.size(); i++) {
            if (i == 0) {
                minY = MinimumY.get(i);
                minY_name = MinY.get(i);
            }
            if (MinimumY.get(i) < minY) {
                minY = MinimumY.get(i);
            }
            if (MinY.get(i) < minY) {
                minY_name = MinY.get(i);
            }
        }
        for (int i = 0; i < MaximumZ.size(); i++) {
            if (i == 0) {
                maxZ = MaximumZ.get(i);
                maxZ_name = MaxZ.get(i);
            }
            if (MaximumZ.get(i) > maxZ) {
                maxZ = MaximumZ.get(i);
            }
            if (MaxZ.get(i) > maxZ_name) {
                maxZ_name = MaxZ.get(i);
            }
        }
        for (int i = 0; i < MinimumZ.size(); i++) {
            if (i == 0) {
                minZ = MinimumZ.get(i);
                minZ_name = MinZ.get(i);
            }
            if (MinimumZ.get(i) < minZ) {
                minZ = MinimumZ.get(i);
            }
            if (MinZ.get(i) < minZ_name) {
                minZ_name = MinZ.get(i);
            }
        }
    }
}
