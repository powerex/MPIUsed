/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D;

import com.mathpar.func.Page;
import java.awt.Color;

/**
 * Класс предназначен для передачи необходимых данных объекту класса
 * RenderWindow для построения изображения
 *
 * @author JuliaGrishina
 */
public class Core implements Runnable {

    private static final int BUFFERS_NUMBER = 1;
    public final RenderWindow renderWindow;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
    boolean parcaPort;

    /**
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
     * @param isForWeb {@code true}, если вызываем из веб-интерфейса
     * @param page
     */
    public Core(boolean plot3dZ, boolean plot3dXYZ, String[] S,
            String[] Sx, String[] Sy, String[] Sz, double Umin, double Umax,
            double Vmin, double Vmax, int u_num, int v_num, Color color,
            boolean stereo, boolean mesh, boolean axes, Page page, boolean isForWeb) {
        renderWindow = new RenderWindow(WIDTH, HEIGHT, plot3dZ, plot3dXYZ, S, Sx, Sy, Sz, Umin, Umax,
                Vmin, Vmax, u_num, v_num, color, stereo, axes, mesh, page, isForWeb);
        run();
    }

    @Override
    public void run() {
        renderWindow.createBufferStrategy(BUFFERS_NUMBER);
        renderWindow.update(renderWindow.render, false, false);
    }
}
