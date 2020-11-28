/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.RayTracing;

public class Core implements Runnable {

    private int width = 500; //Ширина окна просмотра
    private int height = 500; //Высота окна просмотра
    private RenderWindow renderWindow;
    private RayTracer rayTracer;

    public Core() {
        //Инициализация ядра
        init();
    }

    public final void init() {
        //Создаём 3D движок
        rayTracer = new RayTracer(width, height);
        //Создаём GUI
        renderWindow = new RenderWindow(rayTracer);
        // Запускаем приложение
        run();
    }

    public void run() {
        int i = 50000;
        while (i-- > 0) {
            renderWindow.update();
        }
    }
}
