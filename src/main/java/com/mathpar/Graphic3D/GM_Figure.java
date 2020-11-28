/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D;

import com.mathpar.func.F;
import com.mathpar.func.Page;
import java.util.Vector;
import com.mathpar.number.Element;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;

/**
 * Класс GM_Figure отвечает за рассчет точек объекта сцены,присвоение
 * необходимых атрибутов, а так же содержит методы, обеспечивающие
 * поворот,перемещение,масштабирование объектов и их проекцию на экран.
 *
 * @author JuliaGrishina
 */
public class GM_Figure {

    /*
     * Сцена,к которой относится объект
     */
    public GM_Figure Parent = null;
    /**
     * Прикрепленные за сценой Parent объекты
     */
    public Vector<GM_Figure> Children = new Vector<GM_Figure>();
    /**
     * Двумерный массив точек объекта
     */
    public GM_Vector Object[][] = null;
    public int u_count;
    public int v_count;
    /**
     * цвет объекта
     */
    public GM_Color color;
    /**
     * тип объекта
     */
    public static final byte graphEq = 0,
            light = 1,
            viewer = 2;
    // переменные,для хранения минимумов и максимумов параметров
    double mxX, mxY, mxZ, mnX, mnY, mnZ;
    boolean parcaPort;
    Page page;
    /**
     * матрица поворота,перемещения и масштабирования
     */
    public double m[][] = {
        {1, 0, 0, 0},
        {0, 1, 0, 0},
        {0, 0, 1, 0},
        {0, 0, 0, 1}};
    /**
     * обратная матрица к матрице
     * <code>m</code>
     */
    public double am[][] = {
        {1, 0, 0, 0},
        {0, 1, 0, 0},
        {0, 0, 1, 0},
        {0, 0, 0, 1}};
    private boolean changed = true; // true, если объект был перемещён,изменен в размерах или повёрнут

    protected GM_Figure() {
    }

    /**
     * Создание объекта,идентичного,уже построенному. Используется массив данных
     * существующего уже объекта для перезаписи в новый массив.
     *
     * @param Parent
     * @param color
     * @param obj
     */
    public GM_Figure(GM_Figure Parent, GM_Color color, GM_Vector[][] obj,
            Page page, boolean parcaPort) {
        this.page = page;
        this.parcaPort = parcaPort;
        if (Parent != null) {
            Parent.add(this);
        }

        this.color = color;
        u_count = obj.length - 1;
        v_count = obj[0].length - 1;
        Object = new GM_Vector[obj.length][obj[0].length];

        for (int i = 0; i <= u_count; i++) {
            for (int j = 0; j <= v_count; j++) {
                Object[i][j] = new GM_Vector(obj[i][j].x, obj[i][j].y, obj[i][j].z, obj[i][j].type);
            }
        }
    }

    /**
     * Создается точечный объект.
     *
     * @param Parent сцена к которой относится объект
     * @param x координата объекта по оси X
     * @param y координата объекта по оси Y
     * @param z координата объекта по оси z
     */
    public GM_Figure(Page page, boolean parcaPort, GM_Figure Parent, double x, double y, double z, int type) {
        this.page = page;
        this.parcaPort = parcaPort;
        if (Parent != null) {
            Parent.add(this);
        }
        u_count = 0;
        v_count = 0;
        Object = new GM_Vector[u_count + 1][v_count + 1];
        Object[0][0] = new GM_Vector(x, y, z, type);
    }

    /**
     * Создание объекта,заданного явным уравнением. ВЫчисление координат его
     * точек,нахождение экстремумов по координатам x,y и z
     *
     * @param renderW окно на котором строится изображение
     * @param Parent сцена,к которой прикреплен объект
     * @param color цвет объекта
     * @param S строка-уравнение,заданное явно
     * @param Xmin минимальное значение по x
     * @param Xmax максимальное значение по x
     * @param Ymin минимальное значение по y
     * @param Ymax максимальное значение по y
     * @param u_num количество точек,которое берется для построения по области
     * определения x
     * @param v_num количество точек,которое берется для построения по области
     * определения y
     */
    public GM_Figure(RenderWindow renderW, GM_Figure Parent, GM_Color color,
            String S, double Xmin, double Xmax, double Ymin, double Ymax,
            int u_num, int v_num) {     
        page = renderW.page;
        Ring ring = new Ring(page.ring, Ring.R64);
        page.ring=ring;
        ring.page=page;
        parcaPort = renderW.isForWeb;
        if (Parent != null) {
            Parent.add(this);
        }

        this.color = color;
        double u_size = Xmax - Xmin;
        double v_size = Ymax - Ymin;

        mxX = Xmax * renderW.kx;
        mxY = Ymax * renderW.ky;
        mnX = Xmin * renderW.kx;
        mnY = Ymin * renderW.ky;

        renderW.MaximumX.add(mxX);
        renderW.MaximumY.add(mxY);
        renderW.MinimumX.add(mnX);
        renderW.MinimumY.add(mnY);
        u_count = u_num;
        v_count = v_num;

        Object = new GM_Vector[u_count + 1][v_count + 1];
        F funcS= new F(S,ring);
        for (int i = 0; i <= u_count; i++) {
            for (int j = 0; j <= v_count; j++) {
                double x = (Xmin + i * u_size / u_count);
                double y = (Ymin + j * v_size / v_count);        
                double z = //FunctionZ(S, x, y, ring);
                  funcS.valueOf(new NumberR64[]{new NumberR64(x), new NumberR64(y)}, ring).doubleValue();
                Object[i][j] = new GM_Vector(x * renderW.kx, y * renderW.ky, z * renderW.kz, graphEq);

                if (i + j == 0 && !Double.isNaN(Object[i][j].z)) {
                    mxZ = Object[i][j].z;
                    mnZ = Object[i][j].z;
                }

                if ((Object[i][j].z) > mxZ && (!Double.isNaN(Object[i][j].z))) {
                    mxZ = Object[i][j].z;
                }
                if ((Object[i][j].z) < mnZ && !Double.isNaN(Object[i][j].z)) {
                    mnZ = Object[i][j].z;
                }
            }
        }
        renderW.MinimumZ.add(mnZ);
        renderW.MaximumZ.add(mxZ);

        renderW.MaxX.add(mxX / renderW.kx);
        renderW.MaxY.add(mxY / renderW.ky);
        renderW.MaxZ.add(mxZ / renderW.kz);
        renderW.MinX.add(mnX / renderW.kx);
        renderW.MinY.add(mnY / renderW.ky);
        renderW.MinZ.add(mnZ / renderW.kz);
    }

    /**
     * Создание объекта,заданного явным уравнением. ВЫчисление координат его
     * точек,нахождение экстремумов по координатам x,y и z
     *
     * @param renderW окно на котором строится изображение
     * @param Parent сцена,к которой прикреплен объект
     * @param color цвет объекта
     * @param Sx строка- параметрическое уравнение для координаты x
     * @param Sy строка- параметрическое уравнение для координаты y
     * @param Sz строка- параметрическое уравнение для координаты z
     * @param Xmin минимальное значение по параметру u
     * @param Xmax максимальное значение по параметру u
     * @param Ymin минимальное значение по параметру v
     * @param Ymax максимальное значение по параметру v
     * @param u_num количество точек,которое берется для построения по области
     * определения u
     * @param v_num количество точек,которое берется для построения по области
     * определения v
     */
    public GM_Figure(RenderWindow renderW, GM_Figure Parent, GM_Color color,
            String Sx, String Sy, String Sz, double u1, double u2, double v1,
            double v2, int u_num, int v_num) {
        this.page = renderW.page;
        this.parcaPort = renderW.isForWeb;
        if (Parent != null) {
            Parent.add(this);
        }

        this.color = color;
        double u_size = u2 - u1;
        double v_size = v2 - v1;

        u_count = u_num;
        v_count = v_num;

        Object = new GM_Vector[u_count + 1][v_count + 1];
        for (int i = 0; i <= u_count; i++) {
            for (int j = 0; j <= v_count; j++) {
                double x = FunctionParam(Sx, u1 + i * u_size / u_count, v1 + j * v_size / v_count);
                double y = FunctionParam(Sy, u1 + i * u_size / u_count, v1 + j * v_size / v_count);
                double z = FunctionParam(Sz, u1 + i * u_size / u_count, v1 + j * v_size / v_count);
                Object[i][j] = new GM_Vector(x * renderW.kx, y * renderW.ky, z * renderW.kz, graphEq);
                if (i + j == 0 && !Double.isNaN(Object[i][j].z) && !Double.isNaN(Object[i][j].y) && !Double.isNaN(Object[i][j].x)) {
                    mnX = Object[i][j].x;
                    mxX = Object[i][j].x;
                    mnY = Object[i][j].y;
                    mxY = Object[i][j].y;
                    mnZ = Object[i][j].z;
                    mxZ = Object[i][j].z;
                }
                if ((Object[i][j].x) < mnX && !Double.isNaN(Object[i][j].x)) {
                    mnX = Object[i][j].x;
                }
                if ((Object[i][j].y) < mnY && !Double.isNaN(Object[i][j].y)) {
                    mnY = Object[i][j].y;
                }
                if ((Object[i][j].z) < mnZ && !Double.isNaN(Object[i][j].z)) {
                    mnZ = Object[i][j].z;
                }
                if (Object[i][j].x > mxX && !Double.isNaN(Object[i][j].x)) {
                    mxX = Object[i][j].x;
                }
                if ((Object[i][j].y) > mxY && !Double.isNaN(Object[i][j].y)) {
                    mxY = Object[i][j].y;
                }
                if ((Object[i][j].z) > mxZ && !Double.isNaN(Object[i][j].z)) {
                    mxZ = Object[i][j].z;
                }
            }
        }
        renderW.MaximumX.add(mxX);
        renderW.MaximumY.add(mxY);
        renderW.MaximumZ.add(mxZ);
        renderW.MinimumX.add(mnX);
        renderW.MinimumY.add(mnY);
        renderW.MinimumZ.add(mnZ);
        renderW.MaxX.add(mxX / renderW.kx);
        renderW.MaxY.add(mxY / renderW.ky);
        renderW.MaxZ.add(mxZ / renderW.kz);
        renderW.MinX.add(mnX / renderW.kx);
        renderW.MinY.add(mnY / renderW.ky);
        renderW.MinZ.add(mnZ / renderW.kz);
    }

    /**
     * Вычисление координат по заданному параметрическому уравнению
     *
     * @param S строка с функцией вида F(u,v)
     * @param u значение параметра
     * @param v значение параметра
     * @return значение координаты
     */
    public final double FunctionParam(String S, double u, double v) {
        Ring ring = new Ring("R64[u,v]");//new Ring("R64[u,v]");
        ring.setDefaultRing();
        ring.createVarPolynoms();
        F f = new F(S, ring);
        Element g = f.valueOf(new NumberR64[]{new NumberR64(u), new NumberR64(v)}, ring);

        return g.doubleValue();
    }

    /**
     * Вычисление координаты z точки по явно заданной функции
     *
     * @param S строка с функцией вида F(x,y)
     * @param x значение координаты
     * @param y значение координаты
     * @return
     */
    public final double FunctionZ(F f, double x, double y, Ring ring) {

        Element g = f.valueOf(new NumberR64[]{new NumberR64(x), new NumberR64(y)}, ring);

        return g.doubleValue();
    }

    public void add_stereo(GM_Figure figure) {
        if (figure.Parent != null) {
            figure.Parent.Children.remove(figure);
        }
        figure.Parent = this;
        Children.add(figure);

        figure.m = m;
        figure.am = am;
    }

    /**
     * Прикрепление к сцене объекта
     *
     * @param figure
     */
    public void add(GM_Figure figure) {
        if (figure.Parent != null) {
            figure.Parent.Children.remove(figure);
        }
        figure.Parent = this;
        Children.add(figure);
        if (figure.parcaPort) {
            if (null != page.getImage()) {
                m = page.matrix3d();
            }
        }
        figure.m = m;
        figure.am = am;
    }

    /**
     * Метод вызывается для рассчета матрицы при повороте или
     * перемещение.Перенаправляет в метод
     * <code> rotateAndMove(double alpha, double betta, double gamma, double dx, double dy, double dz, double dk)
     * </code> со значение dk=0
     *
     * @param alpha угол поворота вокруг оси x
     * @param betta угол поворота вокруг оси y
     * @param gamma угол поворота вокруг оси z
     * @param dx перемещение вдоль оси x
     * @param dy перемещение вдоль оси y
     * @param dz перемещение вдоль оси z
     */
    public void rotateAndMove(double alpha, double betta, double gamma, double dx, double dy, double dz) {
        rotateAndMove(alpha, betta, gamma, dx, dy, dz, 0);
    }

    /**
     * Метод пересчета координат объектов сцены в случае необходимости сжатия по
     * одной из осей
     *
     * @param dx коэффициент сжатия по х
     * @param dy коэффициент сжатия по y
     * @param dz коэффициент сжатия по z
     */
    public void rotateAndMove(double dx, double dy, double dz) {
        for (GM_Figure cur : Children) {
            changed = true;
            for (int i = 0; i < cur.Object.length; i++) {
                for (int j = 0; j < cur.Object[0].length; j++) {
                    cur.Object[i][j] = new GM_Vector(cur.Object[i][j].x * dx, cur.Object[i][j].y * dy, cur.Object[i][j].z * dz, cur.Object[i][j].type);
                }
            }
        }
    }

    /**
     * Изменение матрицы поворота,перемещения и масштабирования
     * <code>m</code> в зависимости от совершаемого действия
     *
     * @param alpha угол поворота вокруг оси x
     * @param betta угол поворота вокруг оси y
     * @param gamma угол поворота вокруг оси z
     * @param dx перемещение вдоль оси x
     * @param dy перемещение вдоль оси y
     * @param dz перемещение вдоль оси z
     * @param dk коэффициент масштабирования
     */
    public void rotateAndMove(double alpha, double betta, double gamma, double dx, double dy, double dz, double dk) {
        for (GM_Figure cur : Children) {
            cur.rotateAndMove(alpha, betta, gamma, dx, dy, dz, dk);
        }
        changed = true;
        if (alpha != 0) {
            double cos = Math.cos(alpha);
            double sin = Math.sin(alpha);
            double[][] n = {
                {1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}};
            matrixMul(n);

        }
        if (betta != 0) {
            double cos = Math.cos(betta);
            double sin = Math.sin(betta);
            double[][] n = {
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}};
            matrixMul(n);

        }
        if (gamma != 0) {
            double cos = Math.cos(gamma);
            double sin = Math.sin(gamma);
            double[][] n = {
                {cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
            matrixMul(n);
        }
        if (alpha != 0) {
            double cos = Math.cos(alpha);
            double sin = -Math.sin(alpha);
            double[][] n = {
                {1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}};
            amatrixMul(n);
        }
        if (betta != 0) {
            double cos = Math.cos(betta);
            double sin = -Math.sin(betta);
            double[][] n = {
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}};
            amatrixMul(n);
        }
        if (gamma != 0) {
            double cos = Math.cos(gamma);
            double sin = -Math.sin(gamma);
            double[][] n = {
                {cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
            amatrixMul(n);
        }

        if (dx != 0 || dy != 0 || dz != 0) {
            double[][] n = {
                {1, 0, 0, dx},
                {0, 1, 0, dy},
                {0, 0, 1, dz},
                {0, 0, 0, 1}};
            matrixMul(n);
        }
        if (dx != 0 || dy != 0 || dz != 0) {
            double[][] n = {
                {1, 0, 0, -dx},
                {0, 1, 0, -dy},
                {0, 0, 1, -dz},
                {0, 0, 0, 1}};
            amatrixMul(n);
        }
        if (dk != 0) {
            double[][] n = {
                {1 + dk, 0, 0, 0},
                {0, 1 + dk, 0, 0},
                {0, 0, 1 + dk, 0},
                {0, 0, 0, 1}};
            matrixMul(n);
        }
    }

    /**
     * Метод производит умножение точек объектов на матрицу проекции,получая тем
     * самым экранные коориднаты точек
     *
     * @param render объект класса GM_RenderZBuffer
     * @param xSize2 определение координаты х центра сцены
     * @param ySize2 определение координаты у центра сцены
     * @param dist определение удаления центра сцены от наблюдателя(экрана)
     */
    public void applyTransformation(GM_RenderZBuffer render, double xSize2, double ySize2, double dist) {
        for (GM_Figure cur : Children) {
            cur.applyTransformation(render, xSize2, ySize2, dist);
        }

        double[][] old = m;

        // Матрица проекции
        double[][] proj = {
            {dist, 0, xSize2, xSize2 * dist},
            {0, dist, ySize2, ySize2 * dist},
            {0, 0, 1, 0},
            {0, 0, 1, dist}
        };

        matrixMul(proj);

        if (changed && Object != null) {
            for (int u = 0; u <= u_count; u++) {
                for (int v = 0; v <= v_count; v++) {
                    switch (Object[u][v].type) {
                        case graphEq: {
                            vectorProjection(Object[u][v]);
                            break;
                        }
                        case light: {
                            render.light = Object[u][v].multOnTransformMatrix(am);
                            break;
                        }
                        case viewer: {
                            render.viewer = Object[u][v].multOnTransformMatrix(am);
                            break;
                        }
                    }
                    for (int i = 0; i < render.extremPoints.length; i++) {
                        vectorProjection(render.extremPoints[i]);
                    }
                }
            }
        }

        changed = false;
        m = old;
    }

    /**
     * Умножение матрицы
     * <code>m</code> на матрицу n
     *
     * @param n матрица 4х4
     */
    private void matrixMul(double[][] n) {
        double[][] M = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    M[i][j] += m[k][j] * n[i][k];
                }
            }
        }
        m = M;
    }

    /**
     * Умножение обратной матрицы
     * <code>am</code> на матрицу n
     *
     * @param n матрица 4х4
     */
    private void amatrixMul(double[][] n) {
        double[][] M = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    M[i][j] += n[k][j] * am[i][k];
                }
            }
        }
        am = M;
    }

    /**
     * Умножение матрицы m на вектор для получения спроецированных координат
     * точки
     *
     * @param p радиус-вектор точки
     */
    public void vectorProjection(GM_Vector p) {

        double[] x = new double[4];
        for (int i = 0; i < 4; i++) {
            x[i] = m[i][0] * p.x
                    + m[i][1] * p.y
                    + m[i][2] * p.z
                    + m[i][3];
        }
        p.sx = x[0] / x[3];
        p.sy = (int) (x[1] / x[3]);
        p.sz = x[2];
    }
}
