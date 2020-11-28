/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D;

import com.mathpar.func.Page;
import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс предназначен для передачи сформированных данных объекту класса Core
 *
 * @author JuliaGrishina
 */
public class Plot3D {
    Core core;
    /**
     * Путь к файлу с изображением
     */
    public String path = System.getProperty("user.home") + "/image.png";

    Plot3D() {
    }

    /**
     * Конструктор передает необходимые данные для построения
     * поверхности,заданной параметрически объекту класса Core
     *
     * @param S массив строк явных уравнений для координаты x
     * @param Sx массив строк параметрических уравнений для координаты x
     * @param Sy массив строк параметрических уравнений для координаты y
     * @param Sz массив строк параметрических уравнений для координаты z
     * @param Umin минимальное значение параметра u
     * @param Umax максимальное значение параметра u
     * @param Vmin минимальное значение параметра v
     * @param Vmax максимальное значение параметра v
     * @param u_num количество шагов по u
     * @param v_num количество шагов по v
     * @param color цвет
     * @param mesh флаг показать/скрыть сетку
     * @param axes флаг показать/скрыть оси координат
     * @param parcaPort
     * @param sect
     * @param id
     */
    public Plot3D(String[] S, String[] Sx, String[] Sy, String[] Sz, double Umin,
            double Umax, double Vmin, double Vmax, int u_num, int v_num, Color color,
            boolean stereo, boolean mesh, Boolean axes, Page page, boolean parcaPort) {

        boolean plotZ = false;
        boolean plotXYZ = false;
        if (S != null) {
            plotZ = true;
        }
        if (Sx != null && Sy != null && Sz != null) {
            plotXYZ = true;
        }
        core = new Core(plotZ, plotXYZ, S, Sx, Sy, Sz, Umin, Umax,
                Vmin, Vmax, u_num, v_num, color, stereo, axes, mesh, page, parcaPort);
        if (!parcaPort) {
            System.out.println("IMAGE TO FILE");
            OutputStream os = null;
            try {
                try {
                    os = new BufferedOutputStream(new FileOutputStream(new File(path)));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GM_RenderZBuffer.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    javax.imageio.ImageIO.write(core.renderWindow.render.image, "PNG", os);
                } catch (IOException ex) {
                    Logger.getLogger(GM_RenderZBuffer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(GM_RenderZBuffer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
