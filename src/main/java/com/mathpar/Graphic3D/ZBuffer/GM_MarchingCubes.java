/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.ZBuffer;

import com.mathpar.Graphic3D.dicom.DicomLayer;
import java.io.*;
import java.util.Vector;
import com.mathpar.splines.spline3D_LowMem;

/**
 *
 * @author yuri
 */
// 3D точка
class XYZ {

    double x, y, z;
}

// 3D треугольник
class TRIANGLE {

    XYZ[] p = new XYZ[3];
}

// 3D кубик (ячейка)
class GRIDCELL {

    XYZ[] p = new XYZ[8];      // координаты вершин кубика
    double[] val = new double[8]; // значения функции в вершинах
}

/**
 * В классе <b>GM_MarchingCubes</b> реализуется алгоритм "марширующих кубиков" —
 * алгоритм в компьютерной графике, впервые предложенный в 1987 году на
 * конференции SIGGRAPH Вильямом Лоренсеном и Харви Клайном для обработки
 * полигональной сетки изоповерхности трехмерного скалярного поля (чаще
 * называемой сеткой вокселей). Алгоритм пробегает скалярное поле, на каждой
 * итерации просматривает 8 соседних позиций (вершины куба, параллельного осям
 * координат) и определяет полигоны, необходимые для представления части
 * изоповерхности, проходящей через данный куб. Далее на экран выводятся
 * полигоны, образующие заданную изоповерхность. Так как алгоритм выбирает
 * полигоны, исходя только из положения вершин куба относительно изоповерхности,
 * всего получается 256 (2<sup>8</sup> = 256) возможных конфигураций полигонов,
 * которые можно вычислить заранее и сохранить в массиве. Поэтому каждый куб
 * можно представить восьмибитным числом, сопоставив каждой вершине 1, если
 * значение поля в точке больше, чем на изоповерхности, и 0 в противном случае.
 * Полученное число используется в качестве индекса элемента массива, хранящего
 * конфигурации полигонов. Наконец каждая вершина сгенерированного полигона
 * помещается в подходящую позицию на том ребре куба, на котором она лежала
 * изначально. Позиция вычисляется путем линейной интерполяции значений
 * скалярного поля в концах ребра.15 различных конфигураций куба Заранее
 * вычисленный массив из 256 конфигураций полигонов можно получить поворотами и
 * отражениями 15 различных конфигураций куба. Однако использование всего 15
 * базовых конфигураций не гарантирует получение замкнутой поверхности. Базовые
 * конфигурации, лишенные этого недостатка, можно найти в специальной
 * литературе. Градиент скалярного поля в каждой точке сетки также является
 * нормальным вектором предполагаемой изоповерхности, проходящей через эту
 * точку. Поэтому возможно интерполировать эти нормали вдоль ребер каждого куба,
 * чтобы найти нормали сгенерированных вершин, для корректного отображения
 * модели при использовании освещения.
 *
 * [1] William E. Lorensen, Harvey E. Cline: Marching Cubes: A high resolution
 * 3D surface construction algorithm. In: Computer Graphics, Vol. 21, Nr. 4,
 * July 1987
 *
 * @author yuri
 */
public class GM_MarchingCubes extends GM_Figure {

    /**
     * Трёхмерный массив для хранения скалярного поля
     */
    double array3D[][][];
    /**
     * Трёхмерный массив для хранения градиента скалярного поля
     */
    double array3DG[][][];
    int after_grad_d = 0;
    double K;
    double Kz;
    GM_Figure levels[];

    /**
     * Конструктор класса инициализирует трёхмерные массивы данными считанными
     * из файла.
     *
     * @param Parent
     * @param type
     */
    public GM_MarchingCubes(GM_Figure Parent, byte type) {
        super(Parent, type, null);
        levels = new GM_Figure[512];

        /*
         File file = new File("BostonTeapot.raw");
         byte[] data = null;
         try {
         data = getBytesFromFile(file);
         } catch (IOException ex) {
         System.out.println("file not found");
         return;
         }
         System.out.println("file found");

         if (data != null) {
         array3D = new double[256][256][178];
         int p = 0;
         for (int k = 0; k < array3D[0][0].length; k++) {
         for (int j = 0; j < array3D[0].length; j++) {
         for (int i = 0; i < array3D.length; i++) {
         array3D[i][j][k] = data[p++];
         }
         }
         }
         }
         div2();
         */

        /**
         * ********* Раскомментировать это, чтобы работал mcubes DicomLayer
         * dcmLayer = new DicomLayer(); array3D = dcmLayer.array3D;
         *
         * K = 1; Kz = K * 8; while (array3D.length > 150) { div2z(); } grad();
         *
         *
         * rotateAndMove(0, 0, 0, -array3D.length * K / 2, -array3D[0].length *
         * K / 2, -array3D[0][0].length * K / 2); //
         */
        //Здесь начинается использование 3д сплайна.
        DicomLayer dcmLayer = new DicomLayer();
        K = 1;
        Kz = K * 1;
        array3D = dcmLayer.array3D;
        while (array3D.length > 150) {
            div2z();
        }
        grad();

        spline3D_LowMem sp3d = new spline3D_LowMem(dcmLayer.array3D);
        System.gc();
        array3D = sp3d.resample(100, 100, 100);
        System.gc();
        //rotateAndMove(0, 0, 0, -array3D.length * K / 2, -array3D[0].length * K / 2, -array3D[0][0].length * K / 2);
    }

    /**
     * Метод устанавливает цвет изоповерхности для соответствующего уровня, а
     * если изоповерхность ещё не сгенерирована, то вызывается метод
     * <b>addFigureFromLevel</b>.
     *
     * @param level
     * @param color
     */
    public void setLayerLevel(int level, GM_Color color) {
        if (levels[level] == null) {
            levels[level] = addFigureFromLevel(level - (level > 255 ? 256 : 0), color, level > 255 ? array3DG : array3D);
            levels[level] = null;
        } else {
            levels[level].color = color;
            levels[level].visible = true;
        }
    }

    /**
     * Данный метод получает на вход уровень изоповерхности, цвет и трёхмерный
     * массив. Возвращает объект типа <b>GM_Figure</b>, который и является
     * искомой изоповерхностью.
     *
     * @param isolevel
     * @param currentColor
     * @param array3D
     * @return
     */
    private GM_Figure addFigureFromLevel(double isolevel, GM_Color currentColor, double array3D[][][]) {
        if (array3D != null) {
            for (int i = 0; i < array3D.length - 1; i++) {
                for (int j = 0; j < array3D[0].length - 1; j++) {
                    for (int k = 0; k < array3D[0][0].length - 1; k++) {
                        double step = 1, stepz = step,
                                x = i + after_grad_d,
                                y = j + after_grad_d,
                                z = k + after_grad_d;
                        GRIDCELL g = new GRIDCELL();
                        for (int ii = 0; ii < 8; ii++) {
                            g.p[ii] = new XYZ();
                        }
                        g.p[0].x = K * x;
                        g.p[0].y = K * y;
                        g.p[0].z = Kz * z;
                        g.val[0] = array3D[i][j][k];

                        g.p[1].x = K * (x + step);
                        g.p[1].y = K * y;
                        g.p[1].z = Kz * z;
                        g.val[1] = array3D[i + 1][j][k];

                        g.p[2].x = K * (x + step);
                        g.p[2].y = K * y;
                        g.p[2].z = Kz * (z + stepz);
                        g.val[2] = array3D[i + 1][j][k + 1];

                        g.p[3].x = K * x;
                        g.p[3].y = K * y;
                        g.p[3].z = Kz * (z + stepz);
                        g.val[3] = array3D[i][j][k + 1];

                        g.p[4].x = K * x;
                        g.p[4].y = K * (y + step);
                        g.p[4].z = Kz * z;
                        g.val[4] = array3D[i][j + 1][k];

                        g.p[5].x = K * (x + step);
                        g.p[5].y = K * (y + step);
                        g.p[5].z = Kz * z;
                        g.val[5] = array3D[i + 1][j + 1][k];

                        g.p[6].x = K * (x + step);
                        g.p[6].y = K * (y + step);
                        g.p[6].z = Kz * (z + step);
                        g.val[6] = array3D[i + 1][j + 1][k + 1];

                        g.p[7].x = K * x;
                        g.p[7].y = K * (y + step);
                        g.p[7].z = Kz * (z + step);
                        g.val[7] = array3D[i][j + 1][k + 1];

                        Polygonise(g, isolevel);
                    }
                }
            }
        }
        GM_Figure child = new GM_Figure(this, type, currentColor);
        int l = tmp_triangles.size();
        child.Triangles = new GM_Triangle[l];
        for (int i = 0; i < l; i++) {
            child.Triangles[i] = new GM_Triangle();
            for (int j = 0; j < 3; j++) {
                child.Triangles[i].vertex[j] = new GM_Vector(
                        tmp_triangles.elementAt(i).p[j].x,
                        tmp_triangles.elementAt(i).p[j].y,
                        tmp_triangles.elementAt(i).p[j].z);
            }
        }
        tmp_triangles.clear();
        return child;
    }
    /**
     * Таблица номеров пересекающихся с изоповерхностью ребер i-й бит в j-м
     * элементе соответствует i-му ребру для j-й конфигурации вершин (т.е. j-му
     * расположению вершин относительно изоповерхности)
     */
    private int[] edgeTable = new int[]{
        0x0, 0x109, 0x203, 0x30a, 0x406, 0x50f, 0x605, 0x70c,
        0x80c, 0x905, 0xa0f, 0xb06, 0xc0a, 0xd03, 0xe09, 0xf00,
        0x190, 0x99, 0x393, 0x29a, 0x596, 0x49f, 0x795, 0x69c,
        0x99c, 0x895, 0xb9f, 0xa96, 0xd9a, 0xc93, 0xf99, 0xe90,
        0x230, 0x339, 0x33, 0x13a, 0x636, 0x73f, 0x435, 0x53c,
        0xa3c, 0xb35, 0x83f, 0x936, 0xe3a, 0xf33, 0xc39, 0xd30,
        0x3a0, 0x2a9, 0x1a3, 0xaa, 0x7a6, 0x6af, 0x5a5, 0x4ac,
        0xbac, 0xaa5, 0x9af, 0x8a6, 0xfaa, 0xea3, 0xda9, 0xca0,
        0x460, 0x569, 0x663, 0x76a, 0x66, 0x16f, 0x265, 0x36c,
        0xc6c, 0xd65, 0xe6f, 0xf66, 0x86a, 0x963, 0xa69, 0xb60,
        0x5f0, 0x4f9, 0x7f3, 0x6fa, 0x1f6, 0xff, 0x3f5, 0x2fc,
        0xdfc, 0xcf5, 0xfff, 0xef6, 0x9fa, 0x8f3, 0xbf9, 0xaf0,
        0x650, 0x759, 0x453, 0x55a, 0x256, 0x35f, 0x55, 0x15c,
        0xe5c, 0xf55, 0xc5f, 0xd56, 0xa5a, 0xb53, 0x859, 0x950,
        0x7c0, 0x6c9, 0x5c3, 0x4ca, 0x3c6, 0x2cf, 0x1c5, 0xcc,
        0xfcc, 0xec5, 0xdcf, 0xcc6, 0xbca, 0xac3, 0x9c9, 0x8c0,
        0x8c0, 0x9c9, 0xac3, 0xbca, 0xcc6, 0xdcf, 0xec5, 0xfcc,
        0xcc, 0x1c5, 0x2cf, 0x3c6, 0x4ca, 0x5c3, 0x6c9, 0x7c0,
        0x950, 0x859, 0xb53, 0xa5a, 0xd56, 0xc5f, 0xf55, 0xe5c,
        0x15c, 0x55, 0x35f, 0x256, 0x55a, 0x453, 0x759, 0x650,
        0xaf0, 0xbf9, 0x8f3, 0x9fa, 0xef6, 0xfff, 0xcf5, 0xdfc,
        0x2fc, 0x3f5, 0xff, 0x1f6, 0x6fa, 0x7f3, 0x4f9, 0x5f0,
        0xb60, 0xa69, 0x963, 0x86a, 0xf66, 0xe6f, 0xd65, 0xc6c,
        0x36c, 0x265, 0x16f, 0x66, 0x76a, 0x663, 0x569, 0x460,
        0xca0, 0xda9, 0xea3, 0xfaa, 0x8a6, 0x9af, 0xaa5, 0xbac,
        0x4ac, 0x5a5, 0x6af, 0x7a6, 0xaa, 0x1a3, 0x2a9, 0x3a0,
        0xd30, 0xc39, 0xf33, 0xe3a, 0x936, 0x83f, 0xb35, 0xa3c,
        0x53c, 0x435, 0x73f, 0x636, 0x13a, 0x33, 0x339, 0x230,
        0xe90, 0xf99, 0xc93, 0xd9a, 0xa96, 0xb9f, 0x895, 0x99c,
        0x69c, 0x795, 0x49f, 0x596, 0x29a, 0x393, 0x99, 0x190,
        0xf00, 0xe09, 0xd03, 0xc0a, 0xb06, 0xa0f, 0x905, 0x80c,
        0x70c, 0x605, 0x50f, 0x406, 0x30a, 0x203, 0x109, 0x0
    };
    /**
     * Таблица номеров вершин генерируемых граней
     */
    int triTable[][] = new int[][]{
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {0, 8, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {0, 1, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {1, 8, 3, 9, 8, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {1, 2, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {0, 8, 3, 1, 2, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {9, 2, 10, 0, 2, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {2, 8, 3, 2, 10, 8, 10, 9, 8, -1, -1, -1, -1, -1, -1, -1},
        {3, 11, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {0, 11, 2, 8, 11, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {1, 9, 0, 2, 3, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {1, 11, 2, 1, 9, 11, 9, 8, 11, -1, -1, -1, -1, -1, -1, -1},
        {3, 10, 1, 11, 10, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {0, 10, 1, 0, 8, 10, 8, 11, 10, -1, -1, -1, -1, -1, -1, -1},
        {3, 9, 0, 3, 11, 9, 11, 10, 9, -1, -1, -1, -1, -1, -1, -1},
        {9, 8, 10, 10, 8, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {4, 7, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {4, 3, 0, 7, 3, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {0, 1, 9, 8, 4, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {4, 1, 9, 4, 7, 1, 7, 3, 1, -1, -1, -1, -1, -1, -1, -1},
        {1, 2, 10, 8, 4, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {3, 4, 7, 3, 0, 4, 1, 2, 10, -1, -1, -1, -1, -1, -1, -1},
        {9, 2, 10, 9, 0, 2, 8, 4, 7, -1, -1, -1, -1, -1, -1, -1},
        {2, 10, 9, 2, 9, 7, 2, 7, 3, 7, 9, 4, -1, -1, -1, -1},
        {8, 4, 7, 3, 11, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {11, 4, 7, 11, 2, 4, 2, 0, 4, -1, -1, -1, -1, -1, -1, -1},
        {9, 0, 1, 8, 4, 7, 2, 3, 11, -1, -1, -1, -1, -1, -1, -1},
        {4, 7, 11, 9, 4, 11, 9, 11, 2, 9, 2, 1, -1, -1, -1, -1},
        {3, 10, 1, 3, 11, 10, 7, 8, 4, -1, -1, -1, -1, -1, -1, -1},
        {1, 11, 10, 1, 4, 11, 1, 0, 4, 7, 11, 4, -1, -1, -1, -1},
        {4, 7, 8, 9, 0, 11, 9, 11, 10, 11, 0, 3, -1, -1, -1, -1},
        {4, 7, 11, 4, 11, 9, 9, 11, 10, -1, -1, -1, -1, -1, -1, -1},
        {9, 5, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {9, 5, 4, 0, 8, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {0, 5, 4, 1, 5, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {8, 5, 4, 8, 3, 5, 3, 1, 5, -1, -1, -1, -1, -1, -1, -1},
        {1, 2, 10, 9, 5, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {3, 0, 8, 1, 2, 10, 4, 9, 5, -1, -1, -1, -1, -1, -1, -1},
        {5, 2, 10, 5, 4, 2, 4, 0, 2, -1, -1, -1, -1, -1, -1, -1},
        {2, 10, 5, 3, 2, 5, 3, 5, 4, 3, 4, 8, -1, -1, -1, -1},
        {9, 5, 4, 2, 3, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {0, 11, 2, 0, 8, 11, 4, 9, 5, -1, -1, -1, -1, -1, -1, -1},
        {0, 5, 4, 0, 1, 5, 2, 3, 11, -1, -1, -1, -1, -1, -1, -1},
        {2, 1, 5, 2, 5, 8, 2, 8, 11, 4, 8, 5, -1, -1, -1, -1},
        {10, 3, 11, 10, 1, 3, 9, 5, 4, -1, -1, -1, -1, -1, -1, -1},
        {4, 9, 5, 0, 8, 1, 8, 10, 1, 8, 11, 10, -1, -1, -1, -1},
        {5, 4, 0, 5, 0, 11, 5, 11, 10, 11, 0, 3, -1, -1, -1, -1},
        {5, 4, 8, 5, 8, 10, 10, 8, 11, -1, -1, -1, -1, -1, -1, -1},
        {9, 7, 8, 5, 7, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {9, 3, 0, 9, 5, 3, 5, 7, 3, -1, -1, -1, -1, -1, -1, -1},
        {0, 7, 8, 0, 1, 7, 1, 5, 7, -1, -1, -1, -1, -1, -1, -1},
        {1, 5, 3, 3, 5, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {9, 7, 8, 9, 5, 7, 10, 1, 2, -1, -1, -1, -1, -1, -1, -1},
        {10, 1, 2, 9, 5, 0, 5, 3, 0, 5, 7, 3, -1, -1, -1, -1},
        {8, 0, 2, 8, 2, 5, 8, 5, 7, 10, 5, 2, -1, -1, -1, -1},
        {2, 10, 5, 2, 5, 3, 3, 5, 7, -1, -1, -1, -1, -1, -1, -1},
        {7, 9, 5, 7, 8, 9, 3, 11, 2, -1, -1, -1, -1, -1, -1, -1},
        {9, 5, 7, 9, 7, 2, 9, 2, 0, 2, 7, 11, -1, -1, -1, -1},
        {2, 3, 11, 0, 1, 8, 1, 7, 8, 1, 5, 7, -1, -1, -1, -1},
        {11, 2, 1, 11, 1, 7, 7, 1, 5, -1, -1, -1, -1, -1, -1, -1},
        {9, 5, 8, 8, 5, 7, 10, 1, 3, 10, 3, 11, -1, -1, -1, -1},
        {5, 7, 0, 5, 0, 9, 7, 11, 0, 1, 0, 10, 11, 10, 0, -1},
        {11, 10, 0, 11, 0, 3, 10, 5, 0, 8, 0, 7, 5, 7, 0, -1},
        {11, 10, 5, 7, 11, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {10, 6, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {0, 8, 3, 5, 10, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {9, 0, 1, 5, 10, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {1, 8, 3, 1, 9, 8, 5, 10, 6, -1, -1, -1, -1, -1, -1, -1},
        {1, 6, 5, 2, 6, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {1, 6, 5, 1, 2, 6, 3, 0, 8, -1, -1, -1, -1, -1, -1, -1},
        {9, 6, 5, 9, 0, 6, 0, 2, 6, -1, -1, -1, -1, -1, -1, -1},
        {5, 9, 8, 5, 8, 2, 5, 2, 6, 3, 2, 8, -1, -1, -1, -1},
        {2, 3, 11, 10, 6, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {11, 0, 8, 11, 2, 0, 10, 6, 5, -1, -1, -1, -1, -1, -1, -1},
        {0, 1, 9, 2, 3, 11, 5, 10, 6, -1, -1, -1, -1, -1, -1, -1},
        {5, 10, 6, 1, 9, 2, 9, 11, 2, 9, 8, 11, -1, -1, -1, -1},
        {6, 3, 11, 6, 5, 3, 5, 1, 3, -1, -1, -1, -1, -1, -1, -1},
        {0, 8, 11, 0, 11, 5, 0, 5, 1, 5, 11, 6, -1, -1, -1, -1},
        {3, 11, 6, 0, 3, 6, 0, 6, 5, 0, 5, 9, -1, -1, -1, -1},
        {6, 5, 9, 6, 9, 11, 11, 9, 8, -1, -1, -1, -1, -1, -1, -1},
        {5, 10, 6, 4, 7, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {4, 3, 0, 4, 7, 3, 6, 5, 10, -1, -1, -1, -1, -1, -1, -1},
        {1, 9, 0, 5, 10, 6, 8, 4, 7, -1, -1, -1, -1, -1, -1, -1},
        {10, 6, 5, 1, 9, 7, 1, 7, 3, 7, 9, 4, -1, -1, -1, -1},
        {6, 1, 2, 6, 5, 1, 4, 7, 8, -1, -1, -1, -1, -1, -1, -1},
        {1, 2, 5, 5, 2, 6, 3, 0, 4, 3, 4, 7, -1, -1, -1, -1},
        {8, 4, 7, 9, 0, 5, 0, 6, 5, 0, 2, 6, -1, -1, -1, -1},
        {7, 3, 9, 7, 9, 4, 3, 2, 9, 5, 9, 6, 2, 6, 9, -1},
        {3, 11, 2, 7, 8, 4, 10, 6, 5, -1, -1, -1, -1, -1, -1, -1},
        {5, 10, 6, 4, 7, 2, 4, 2, 0, 2, 7, 11, -1, -1, -1, -1},
        {0, 1, 9, 4, 7, 8, 2, 3, 11, 5, 10, 6, -1, -1, -1, -1},
        {9, 2, 1, 9, 11, 2, 9, 4, 11, 7, 11, 4, 5, 10, 6, -1},
        {8, 4, 7, 3, 11, 5, 3, 5, 1, 5, 11, 6, -1, -1, -1, -1},
        {5, 1, 11, 5, 11, 6, 1, 0, 11, 7, 11, 4, 0, 4, 11, -1},
        {0, 5, 9, 0, 6, 5, 0, 3, 6, 11, 6, 3, 8, 4, 7, -1},
        {6, 5, 9, 6, 9, 11, 4, 7, 9, 7, 11, 9, -1, -1, -1, -1},
        {10, 4, 9, 6, 4, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {4, 10, 6, 4, 9, 10, 0, 8, 3, -1, -1, -1, -1, -1, -1, -1},
        {10, 0, 1, 10, 6, 0, 6, 4, 0, -1, -1, -1, -1, -1, -1, -1},
        {8, 3, 1, 8, 1, 6, 8, 6, 4, 6, 1, 10, -1, -1, -1, -1},
        {1, 4, 9, 1, 2, 4, 2, 6, 4, -1, -1, -1, -1, -1, -1, -1},
        {3, 0, 8, 1, 2, 9, 2, 4, 9, 2, 6, 4, -1, -1, -1, -1},
        {0, 2, 4, 4, 2, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {8, 3, 2, 8, 2, 4, 4, 2, 6, -1, -1, -1, -1, -1, -1, -1},
        {10, 4, 9, 10, 6, 4, 11, 2, 3, -1, -1, -1, -1, -1, -1, -1},
        {0, 8, 2, 2, 8, 11, 4, 9, 10, 4, 10, 6, -1, -1, -1, -1},
        {3, 11, 2, 0, 1, 6, 0, 6, 4, 6, 1, 10, -1, -1, -1, -1},
        {6, 4, 1, 6, 1, 10, 4, 8, 1, 2, 1, 11, 8, 11, 1, -1},
        {9, 6, 4, 9, 3, 6, 9, 1, 3, 11, 6, 3, -1, -1, -1, -1},
        {8, 11, 1, 8, 1, 0, 11, 6, 1, 9, 1, 4, 6, 4, 1, -1},
        {3, 11, 6, 3, 6, 0, 0, 6, 4, -1, -1, -1, -1, -1, -1, -1},
        {6, 4, 8, 11, 6, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {7, 10, 6, 7, 8, 10, 8, 9, 10, -1, -1, -1, -1, -1, -1, -1},
        {0, 7, 3, 0, 10, 7, 0, 9, 10, 6, 7, 10, -1, -1, -1, -1},
        {10, 6, 7, 1, 10, 7, 1, 7, 8, 1, 8, 0, -1, -1, -1, -1},
        {10, 6, 7, 10, 7, 1, 1, 7, 3, -1, -1, -1, -1, -1, -1, -1},
        {1, 2, 6, 1, 6, 8, 1, 8, 9, 8, 6, 7, -1, -1, -1, -1},
        {2, 6, 9, 2, 9, 1, 6, 7, 9, 0, 9, 3, 7, 3, 9, -1},
        {7, 8, 0, 7, 0, 6, 6, 0, 2, -1, -1, -1, -1, -1, -1, -1},
        {7, 3, 2, 6, 7, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {2, 3, 11, 10, 6, 8, 10, 8, 9, 8, 6, 7, -1, -1, -1, -1},
        {2, 0, 7, 2, 7, 11, 0, 9, 7, 6, 7, 10, 9, 10, 7, -1},
        {1, 8, 0, 1, 7, 8, 1, 10, 7, 6, 7, 10, 2, 3, 11, -1},
        {11, 2, 1, 11, 1, 7, 10, 6, 1, 6, 7, 1, -1, -1, -1, -1},
        {8, 9, 6, 8, 6, 7, 9, 1, 6, 11, 6, 3, 1, 3, 6, -1},
        {0, 9, 1, 11, 6, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {7, 8, 0, 7, 0, 6, 3, 11, 0, 11, 6, 0, -1, -1, -1, -1},
        {7, 11, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {7, 6, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {3, 0, 8, 11, 7, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {0, 1, 9, 11, 7, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {8, 1, 9, 8, 3, 1, 11, 7, 6, -1, -1, -1, -1, -1, -1, -1},
        {10, 1, 2, 6, 11, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {1, 2, 10, 3, 0, 8, 6, 11, 7, -1, -1, -1, -1, -1, -1, -1},
        {2, 9, 0, 2, 10, 9, 6, 11, 7, -1, -1, -1, -1, -1, -1, -1},
        {6, 11, 7, 2, 10, 3, 10, 8, 3, 10, 9, 8, -1, -1, -1, -1},
        {7, 2, 3, 6, 2, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {7, 0, 8, 7, 6, 0, 6, 2, 0, -1, -1, -1, -1, -1, -1, -1},
        {2, 7, 6, 2, 3, 7, 0, 1, 9, -1, -1, -1, -1, -1, -1, -1},
        {1, 6, 2, 1, 8, 6, 1, 9, 8, 8, 7, 6, -1, -1, -1, -1},
        {10, 7, 6, 10, 1, 7, 1, 3, 7, -1, -1, -1, -1, -1, -1, -1},
        {10, 7, 6, 1, 7, 10, 1, 8, 7, 1, 0, 8, -1, -1, -1, -1},
        {0, 3, 7, 0, 7, 10, 0, 10, 9, 6, 10, 7, -1, -1, -1, -1},
        {7, 6, 10, 7, 10, 8, 8, 10, 9, -1, -1, -1, -1, -1, -1, -1},
        {6, 8, 4, 11, 8, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {3, 6, 11, 3, 0, 6, 0, 4, 6, -1, -1, -1, -1, -1, -1, -1},
        {8, 6, 11, 8, 4, 6, 9, 0, 1, -1, -1, -1, -1, -1, -1, -1},
        {9, 4, 6, 9, 6, 3, 9, 3, 1, 11, 3, 6, -1, -1, -1, -1},
        {6, 8, 4, 6, 11, 8, 2, 10, 1, -1, -1, -1, -1, -1, -1, -1},
        {1, 2, 10, 3, 0, 11, 0, 6, 11, 0, 4, 6, -1, -1, -1, -1},
        {4, 11, 8, 4, 6, 11, 0, 2, 9, 2, 10, 9, -1, -1, -1, -1},
        {10, 9, 3, 10, 3, 2, 9, 4, 3, 11, 3, 6, 4, 6, 3, -1},
        {8, 2, 3, 8, 4, 2, 4, 6, 2, -1, -1, -1, -1, -1, -1, -1},
        {0, 4, 2, 4, 6, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {1, 9, 0, 2, 3, 4, 2, 4, 6, 4, 3, 8, -1, -1, -1, -1},
        {1, 9, 4, 1, 4, 2, 2, 4, 6, -1, -1, -1, -1, -1, -1, -1},
        {8, 1, 3, 8, 6, 1, 8, 4, 6, 6, 10, 1, -1, -1, -1, -1},
        {10, 1, 0, 10, 0, 6, 6, 0, 4, -1, -1, -1, -1, -1, -1, -1},
        {4, 6, 3, 4, 3, 8, 6, 10, 3, 0, 3, 9, 10, 9, 3, -1},
        {10, 9, 4, 6, 10, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {4, 9, 5, 7, 6, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {0, 8, 3, 4, 9, 5, 11, 7, 6, -1, -1, -1, -1, -1, -1, -1},
        {5, 0, 1, 5, 4, 0, 7, 6, 11, -1, -1, -1, -1, -1, -1, -1},
        {11, 7, 6, 8, 3, 4, 3, 5, 4, 3, 1, 5, -1, -1, -1, -1},
        {9, 5, 4, 10, 1, 2, 7, 6, 11, -1, -1, -1, -1, -1, -1, -1},
        {6, 11, 7, 1, 2, 10, 0, 8, 3, 4, 9, 5, -1, -1, -1, -1},
        {7, 6, 11, 5, 4, 10, 4, 2, 10, 4, 0, 2, -1, -1, -1, -1},
        {3, 4, 8, 3, 5, 4, 3, 2, 5, 10, 5, 2, 11, 7, 6, -1},
        {7, 2, 3, 7, 6, 2, 5, 4, 9, -1, -1, -1, -1, -1, -1, -1},
        {9, 5, 4, 0, 8, 6, 0, 6, 2, 6, 8, 7, -1, -1, -1, -1},
        {3, 6, 2, 3, 7, 6, 1, 5, 0, 5, 4, 0, -1, -1, -1, -1},
        {6, 2, 8, 6, 8, 7, 2, 1, 8, 4, 8, 5, 1, 5, 8, -1},
        {9, 5, 4, 10, 1, 6, 1, 7, 6, 1, 3, 7, -1, -1, -1, -1},
        {1, 6, 10, 1, 7, 6, 1, 0, 7, 8, 7, 0, 9, 5, 4, -1},
        {4, 0, 10, 4, 10, 5, 0, 3, 10, 6, 10, 7, 3, 7, 10, -1},
        {7, 6, 10, 7, 10, 8, 5, 4, 10, 4, 8, 10, -1, -1, -1, -1},
        {6, 9, 5, 6, 11, 9, 11, 8, 9, -1, -1, -1, -1, -1, -1, -1},
        {3, 6, 11, 0, 6, 3, 0, 5, 6, 0, 9, 5, -1, -1, -1, -1},
        {0, 11, 8, 0, 5, 11, 0, 1, 5, 5, 6, 11, -1, -1, -1, -1},
        {6, 11, 3, 6, 3, 5, 5, 3, 1, -1, -1, -1, -1, -1, -1, -1},
        {1, 2, 10, 9, 5, 11, 9, 11, 8, 11, 5, 6, -1, -1, -1, -1},
        {0, 11, 3, 0, 6, 11, 0, 9, 6, 5, 6, 9, 1, 2, 10, -1},
        {11, 8, 5, 11, 5, 6, 8, 0, 5, 10, 5, 2, 0, 2, 5, -1},
        {6, 11, 3, 6, 3, 5, 2, 10, 3, 10, 5, 3, -1, -1, -1, -1},
        {5, 8, 9, 5, 2, 8, 5, 6, 2, 3, 8, 2, -1, -1, -1, -1},
        {9, 5, 6, 9, 6, 0, 0, 6, 2, -1, -1, -1, -1, -1, -1, -1},
        {1, 5, 8, 1, 8, 0, 5, 6, 8, 3, 8, 2, 6, 2, 8, -1},
        {1, 5, 6, 2, 1, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {1, 3, 6, 1, 6, 10, 3, 8, 6, 5, 6, 9, 8, 9, 6, -1},
        {10, 1, 0, 10, 0, 6, 9, 5, 0, 5, 6, 0, -1, -1, -1, -1},
        {0, 3, 8, 5, 6, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {10, 5, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {11, 5, 10, 7, 5, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {11, 5, 10, 11, 7, 5, 8, 3, 0, -1, -1, -1, -1, -1, -1, -1},
        {5, 11, 7, 5, 10, 11, 1, 9, 0, -1, -1, -1, -1, -1, -1, -1},
        {10, 7, 5, 10, 11, 7, 9, 8, 1, 8, 3, 1, -1, -1, -1, -1},
        {11, 1, 2, 11, 7, 1, 7, 5, 1, -1, -1, -1, -1, -1, -1, -1},
        {0, 8, 3, 1, 2, 7, 1, 7, 5, 7, 2, 11, -1, -1, -1, -1},
        {9, 7, 5, 9, 2, 7, 9, 0, 2, 2, 11, 7, -1, -1, -1, -1},
        {7, 5, 2, 7, 2, 11, 5, 9, 2, 3, 2, 8, 9, 8, 2, -1},
        {2, 5, 10, 2, 3, 5, 3, 7, 5, -1, -1, -1, -1, -1, -1, -1},
        {8, 2, 0, 8, 5, 2, 8, 7, 5, 10, 2, 5, -1, -1, -1, -1},
        {9, 0, 1, 5, 10, 3, 5, 3, 7, 3, 10, 2, -1, -1, -1, -1},
        {9, 8, 2, 9, 2, 1, 8, 7, 2, 10, 2, 5, 7, 5, 2, -1},
        {1, 3, 5, 3, 7, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {0, 8, 7, 0, 7, 1, 1, 7, 5, -1, -1, -1, -1, -1, -1, -1},
        {9, 0, 3, 9, 3, 5, 5, 3, 7, -1, -1, -1, -1, -1, -1, -1},
        {9, 8, 7, 5, 9, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {5, 8, 4, 5, 10, 8, 10, 11, 8, -1, -1, -1, -1, -1, -1, -1},
        {5, 0, 4, 5, 11, 0, 5, 10, 11, 11, 3, 0, -1, -1, -1, -1},
        {0, 1, 9, 8, 4, 10, 8, 10, 11, 10, 4, 5, -1, -1, -1, -1},
        {10, 11, 4, 10, 4, 5, 11, 3, 4, 9, 4, 1, 3, 1, 4, -1},
        {2, 5, 1, 2, 8, 5, 2, 11, 8, 4, 5, 8, -1, -1, -1, -1},
        {0, 4, 11, 0, 11, 3, 4, 5, 11, 2, 11, 1, 5, 1, 11, -1},
        {0, 2, 5, 0, 5, 9, 2, 11, 5, 4, 5, 8, 11, 8, 5, -1},
        {9, 4, 5, 2, 11, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {2, 5, 10, 3, 5, 2, 3, 4, 5, 3, 8, 4, -1, -1, -1, -1},
        {5, 10, 2, 5, 2, 4, 4, 2, 0, -1, -1, -1, -1, -1, -1, -1},
        {3, 10, 2, 3, 5, 10, 3, 8, 5, 4, 5, 8, 0, 1, 9, -1},
        {5, 10, 2, 5, 2, 4, 1, 9, 2, 9, 4, 2, -1, -1, -1, -1},
        {8, 4, 5, 8, 5, 3, 3, 5, 1, -1, -1, -1, -1, -1, -1, -1},
        {0, 4, 5, 1, 0, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {8, 4, 5, 8, 5, 3, 9, 0, 5, 0, 3, 5, -1, -1, -1, -1},
        {9, 4, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {4, 11, 7, 4, 9, 11, 9, 10, 11, -1, -1, -1, -1, -1, -1, -1},
        {0, 8, 3, 4, 9, 7, 9, 11, 7, 9, 10, 11, -1, -1, -1, -1},
        {1, 10, 11, 1, 11, 4, 1, 4, 0, 7, 4, 11, -1, -1, -1, -1},
        {3, 1, 4, 3, 4, 8, 1, 10, 4, 7, 4, 11, 10, 11, 4, -1},
        {4, 11, 7, 9, 11, 4, 9, 2, 11, 9, 1, 2, -1, -1, -1, -1},
        {9, 7, 4, 9, 11, 7, 9, 1, 11, 2, 11, 1, 0, 8, 3, -1},
        {11, 7, 4, 11, 4, 2, 2, 4, 0, -1, -1, -1, -1, -1, -1, -1},
        {11, 7, 4, 11, 4, 2, 8, 3, 4, 3, 2, 4, -1, -1, -1, -1},
        {2, 9, 10, 2, 7, 9, 2, 3, 7, 7, 4, 9, -1, -1, -1, -1},
        {9, 10, 7, 9, 7, 4, 10, 2, 7, 8, 7, 0, 2, 0, 7, -1},
        {3, 7, 10, 3, 10, 2, 7, 4, 10, 1, 10, 0, 4, 0, 10, -1},
        {1, 10, 2, 8, 7, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {4, 9, 1, 4, 1, 7, 7, 1, 3, -1, -1, -1, -1, -1, -1, -1},
        {4, 9, 1, 4, 1, 7, 0, 8, 1, 8, 7, 1, -1, -1, -1, -1},
        {4, 0, 3, 7, 4, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {4, 8, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {9, 10, 8, 10, 11, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {3, 0, 9, 3, 9, 11, 11, 9, 10, -1, -1, -1, -1, -1, -1, -1},
        {0, 1, 10, 0, 10, 8, 8, 10, 11, -1, -1, -1, -1, -1, -1, -1},
        {3, 1, 10, 11, 3, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {1, 2, 11, 1, 11, 9, 9, 11, 8, -1, -1, -1, -1, -1, -1, -1},
        {3, 0, 9, 3, 9, 11, 1, 2, 9, 2, 11, 9, -1, -1, -1, -1},
        {0, 2, 11, 8, 0, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {3, 2, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {2, 3, 8, 2, 8, 10, 10, 8, 9, -1, -1, -1, -1, -1, -1, -1},
        {9, 10, 2, 0, 9, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {2, 3, 8, 2, 8, 10, 0, 1, 8, 1, 10, 8, -1, -1, -1, -1},
        {1, 10, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {1, 3, 8, 9, 1, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {0, 9, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {0, 3, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}
    };
    Vector<TRIANGLE> tmp_triangles = new Vector<TRIANGLE>();

    /**
     * Метод принимает на вход уровень изоповерхности и объект типа
     * <b>GRIDCELL</b>, который является ячейкой скалярного поля и хранит
     * информацию о координатах своих вершин и значениях скалярной функции в
     * этих вершинах. На основе этих данные заполняется массив треугольников
     * tmp_triangles.
     *
     * @param grid
     * @param isolevel
     */
    private void Polygonise(GRIDCELL grid, double isolevel) {
        int cubeindex;
        XYZ[] vertlist = new XYZ[12];

        // определяем номер конфигурации вершин
        cubeindex = 0;
        if (grid.val[0] < isolevel) {
            cubeindex |= 1;
        }
        if (grid.val[1] < isolevel) {
            cubeindex |= 2;
        }
        if (grid.val[2] < isolevel) {
            cubeindex |= 4;
        }
        if (grid.val[3] < isolevel) {
            cubeindex |= 8;
        }
        if (grid.val[4] < isolevel) {
            cubeindex |= 16;
        }
        if (grid.val[5] < isolevel) {
            cubeindex |= 32;
        }
        if (grid.val[6] < isolevel) {
            cubeindex |= 64;
        }
        if (grid.val[7] < isolevel) {
            cubeindex |= 128;
        }

        // кубик находится целиком под или над изоповерхностью?
        if (edgeTable[cubeindex] == 0) {
            return;
        }

        // считаем точки пересечения изоповерхности и кубика
        if ((edgeTable[cubeindex] & 1) != 0) {
            vertlist[0] =
                    VertexInterpolate(isolevel, grid.p[0], grid.p[1], grid.val[0], grid.val[1]);
        }
        if ((edgeTable[cubeindex] & 2) != 0) {
            vertlist[1] =
                    VertexInterpolate(isolevel, grid.p[1], grid.p[2], grid.val[1], grid.val[2]);
        }
        if ((edgeTable[cubeindex] & 4) != 0) {
            vertlist[2] =
                    VertexInterpolate(isolevel, grid.p[2], grid.p[3], grid.val[2], grid.val[3]);
        }
        if ((edgeTable[cubeindex] & 8) != 0) {
            vertlist[3] =
                    VertexInterpolate(isolevel, grid.p[3], grid.p[0], grid.val[3], grid.val[0]);
        }
        if ((edgeTable[cubeindex] & 16) != 0) {
            vertlist[4] =
                    VertexInterpolate(isolevel, grid.p[4], grid.p[5], grid.val[4], grid.val[5]);
        }
        if ((edgeTable[cubeindex] & 32) != 0) {
            vertlist[5] =
                    VertexInterpolate(isolevel, grid.p[5], grid.p[6], grid.val[5], grid.val[6]);
        }
        if ((edgeTable[cubeindex] & 64) != 0) {
            vertlist[6] =
                    VertexInterpolate(isolevel, grid.p[6], grid.p[7], grid.val[6], grid.val[7]);
        }
        if ((edgeTable[cubeindex] & 128) != 0) {
            vertlist[7] =
                    VertexInterpolate(isolevel, grid.p[7], grid.p[4], grid.val[7], grid.val[4]);
        }
        if ((edgeTable[cubeindex] & 256) != 0) {
            vertlist[8] =
                    VertexInterpolate(isolevel, grid.p[0], grid.p[4], grid.val[0], grid.val[4]);
        }
        if ((edgeTable[cubeindex] & 512) != 0) {
            vertlist[9] =
                    VertexInterpolate(isolevel, grid.p[1], grid.p[5], grid.val[1], grid.val[5]);
        }
        if ((edgeTable[cubeindex] & 1024) != 0) {
            vertlist[10] =
                    VertexInterpolate(isolevel, grid.p[2], grid.p[6], grid.val[2], grid.val[6]);
        }
        if ((edgeTable[cubeindex] & 2048) != 0) {
            vertlist[11] =
                    VertexInterpolate(isolevel, grid.p[3], grid.p[7], grid.val[3], grid.val[7]);
        }

        // создаем грани
        for (int i = 0; triTable[cubeindex][i] != -1; i += 3) {
            TRIANGLE tmp = new TRIANGLE();
            tmp.p[0] = vertlist[triTable[cubeindex][i]];
            tmp.p[1] = vertlist[triTable[cubeindex][i + 1]];
            tmp.p[2] = vertlist[triTable[cubeindex][i + 2]];
            tmp_triangles.add(tmp);
        }

        return;
    }

    /**
     * Метод предназначен для вычисления точки пересечения ребра куба с
     * изоуровнем.
     *
     * @param isolevel - изоуровень.
     * @param p1 - координаты первой точки.
     * @param p2 - координаты второй точки.
     * @param valp1 - значения уровня в первой точке.
     * @param valp2 - значения уровня во второй точке.
     * @return - координаты точки пересечения.
     */
    private XYZ VertexInterpolate(double isolevel, XYZ p1, XYZ p2, double valp1, double valp2) {
        double mu;
        XYZ p = new XYZ();

        // точка пересечения совпадает с p1?
        if (Math.abs(isolevel - valp1) < 0.00001) {
            return (p1);
        }

        // точка пересечения совпадает с p2?
        if (Math.abs(isolevel - valp2) < 0.00001) {
            return (p2);
        }

        // значение функции не меняется на всем отрезке?
        if (Math.abs(valp1 - valp2) < 0.00001) {
            return (p1);
        }

        // собственно линейная интерполяция
        mu = (isolevel - valp1) / (valp2 - valp1);
        p.x = p1.x + mu * (p2.x - p1.x);
        p.y = p1.y + mu * (p2.y - p1.y);
        p.z = p1.z + mu * (p2.z - p1.z);

        return p;

    }

    /**
     * Метод вычисляет градиент скалярного поля и сохраняет в массив array3DG
     */
    private void grad() {
        if (array3D == null) {
            return;
        }
        array3DG = new double[array3D.length - 1][array3D[0].length - 1][array3D[0][0].length - 1];
        for (int i = 0; i < array3DG.length; i++) {
            for (int j = 0; j < array3DG[0].length; j++) {
                for (int k = 0; k < array3DG[0][0].length; k++) {
                    array3DG[i][j][k] = Math.sqrt(
                            (array3D[i + 1][j][k] - array3D[i][j][k]) * (array3D[i + 1][j][k] - array3D[i][j][k])
                            + (array3D[i][j + 1][k] - array3D[i][j][k]) * (array3D[i][j + 1][k] - array3D[i][j][k])
                            + (array3D[i][j][k + 1] - array3D[i][j][k]) * (array3D[i][j][k + 1] - array3D[i][j][k]));
                }
            }
        }
    }

    private void div2() {
        if (array3D == null) {
            return;
        }
        double newarray3D[][][] = new double[array3D.length / 2][array3D[0].length / 2][array3D[0][0].length / 2];
        for (int i = 0; i < newarray3D.length; i++) {
            for (int j = 0; j < newarray3D[0].length; j++) {
                for (int k = 0; k < newarray3D[0][0].length; k++) {
                    newarray3D[i][j][k] =
                            (array3D[2 * i][2 * j][2 * k] + array3D[2 * i + 1][2 * j][2 * k]
                            + array3D[2 * i][2 * j + 1][2 * k] + array3D[2 * i + 1][2 * j + 1][2 * k]
                            + array3D[2 * i][2 * j][2 * k + 1] + array3D[2 * i + 1][2 * j][2 * k + 1]
                            + array3D[2 * i][2 * j + 1][2 * k + 1] + array3D[2 * i + 1][2 * j + 1][2 * k + 1]) / 8;
                }
            }
        }
        array3D = newarray3D;
        K *= 2;
    }

    private void div2z() {
        if (array3D == null) {
            return;
        }
        double newarray3D[][][] = new double[array3D.length / 2][array3D[0].length / 2][array3D[0][0].length];
        for (int i = 0; i < newarray3D.length; i++) {
            for (int j = 0; j < newarray3D[0].length; j++) {
                for (int k = 0; k < newarray3D[0][0].length; k++) {
                    newarray3D[i][j][k] =
                            (array3D[2 * i][2 * j][k] + array3D[2 * i + 1][2 * j][k] + array3D[2 * i][2 * j + 1][k] + array3D[2 * i + 1][2 * j + 1][k]) / 4;
                }
            }
        }
        array3D = newarray3D;
        K *= 2;
    }

    /**
     * Метод считывает файл в одмерный байтовый массив.
     *
     * @param file - файл из которого будет производится считывание
     * @return - байтовый массив.
     * @throws java.io.IOException
     */
    private static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        // Get the size of the file
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
}
