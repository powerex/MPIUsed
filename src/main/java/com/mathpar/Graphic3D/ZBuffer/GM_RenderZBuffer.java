/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.ZBuffer;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

/**
 * <p>Класс GM_RenderZBuffer предназначен для преобразования массива треугольников в двумерных массив(изображение) и вывода этого изображения на экран.</p>
 * <p>Основной рабочей частью класса является алгоритм z-буфера, модифицированный для создания эффекта прозрачности.</p>
 * <p>Это один из простейших алгоритмов удаления невидимых поверхностей. Впервые он был предложен Кэтмулом [8]. Работает этот алгоритм в пространстве изображения. Идея z-буфера является простым обобщением идеи о буфере кадра. Буфер кадра используется для запоминания атрибутов (интенсивности) каждого пиксела в пространстве изображения, z-буфер - это отдельный буфер глубины, используемый для запоминания координаты z или глубины каждого видимого пиксела в пространстве изображения. В процессе работы глубина или значение z каждого нового пиксела, который нужно занести в буфер кадра, сравнивается с глубиной того пиксела, который уже занесен в z-буфер. Если это сравнение показывает, что новый пиксел расположен впереди пиксела, находящегося в буфере кадра, то новый пиксел заносится в этот буфер и, кроме того, производится корректировка z-буфера новым значением z. Если же сравнение дает противоположный результат, то никаких действий не производится. По сути, алгоритм является поиском по х и у наибольшего значения функции z (х, у).</p>
 * <p>Главное преимущество алгоритма - его простота. Кроме того, этот алгоритм решает задачу об удалении невидимых поверхностей и делает тривиальной визуализацию пересечений сложных поверхностей. Сцены могут быть любой сложности. Поскольку габариты пространства изображения фиксированы, оценка вычислительной трудоемкости алгоритма не более чем линейна. Поскольку элементы сцены или картинки можно заносить в буфер кадра или в z-буфер в произвольном порядке, их не нужно предварительно сортировать по приоритету глубины. Поэтому экономится вычислительное время, затрачиваемое на сортировку по глубине.</p>
 * <p>Другой недостаток алгоритма z-буфера состоит в трудоемкости и высокой стоимости устранения лестничного эффекта, а также реализации эффектов прозрачности и просвечивания. Поскольку алгоритм заносит пикселы в буфер кадра в произвольном порядке, то нелегко получить информацию, необходимую для методов устранения лестничного эффекта, основывающихся на предварительной фильтрации. При реализации эффектов прозрачности и просвечивания, пикселы могут заноситься в буфер кадра в некорректном порядке, что ведет к локальным ошибкам.</p>
 * <p>Более формальное описание алгоритма z-буфера таково:</p>
 * <p>1) Заполнить буфер кадра фоновым значением интенсивности или цвета.</p>
 * <p>2) Заполнить z -буфер минимальным значением z.</p>
 * <p>3) Преобразовать каждый многоугольник в растровую форму в произвольном порядке.</p>
 * <p>4) Для каждого Пиксел(x,y) в многоугольнике вычислить его глубину z(x,y).</p>
 * <p>5) Сравнить глубину z(х,у) со значением Zбуфер(х,у), хранящимся в z-буфере в этой же позиции.</p>
 * <p>6) Если z(х, у) > Zбуфер (х,у), то записать атрибут этого многоугольника (интенсивность, цвет и т. п.) в буфер кадра и заменить Zбуфер(х,у) на z(х,у). В противном случае никаких действий не производить.</p>
 *
 * @author yuri
 */
public class GM_RenderZBuffer {

    /**
     * Буфер изображения хранит несколько слоёв изображения, то есть для каждой точки изображения хранятся цвета соответствующих проецируемых в эту точку поверхностей
     *   в порядке удалённости их от плоскости экрана.
     */
    public int[][] сolorBuffer;
    /**
     * Каждому элементу в буфере изображения соответствует элемент в буфере глубины, в котором хранится соответствующее расстояние от плоскости экрана до проецируемой поверхности.
     */
    public double[][] zBuffer;
    /**
     * Для подсчёта количества слоёв в каждой точке используется отдельный массив.
     */
    public int[] zBufferC;
    /**
     * Переменная alphaBufDepth задаёт максимально возможное количество слоёв.
     */
    public int alphaBufDepth = 10;
    /**
     * В экранном буфера помещается готовое изображение, готовое для вывода на экран.
     */
    public int[] screenBuffer;
    /**
     * Переменная GM_Scene s хранит объект сцены.
     */
    public GM_Scene s;
    int width;
    int height;
    DirectColorModel dcm = new DirectColorModel(32, 0x00FF0000, 0x0000FF00, 0x000000FF);
    Component converter = new JPanel();
    int frame = 0;
    long t1 = 0;
    double fps = 0;

    /**
     * Конструктор класса принимает два параметра: ширину и высоту окна визуализации. Здесь происходит выделение памяти и инициализация основных
     * полей класса.
     */
    public GM_RenderZBuffer(int width, int height) {
        GM_Vector[] lights = {new GM_Vector(52, -50, 53)};
        double[] Jl = {10};
        GM_Light J = new GM_Light(lights, Jl, 10, 1, 0.5f, 0.35f, 0.15f);
        s = new GM_Scene(J, width, height, 20);
        this.width = width;
        this.height = height;
        сolorBuffer = new int[width * height][alphaBufDepth];
        zBuffer = new double[width * height][alphaBufDepth];
        zBufferC = new int[width * height];
        screenBuffer = new int[width * height];
    }

    public void draw(Graphics g) {
        s.applyTransformation(width / 2, height / 2, 500);

        int len = zBuffer.length;
        for (int i = 0; i < len; i++) {
            zBuffer[i][0] = 0x7FFFFFFF; //Буфер глубины
            zBufferC[i] = 0;  //Счётчик слоёв.
        }
        draw(s);

        for (int i = 0; i < len; i++) {
            int count = zBufferC[i];
            if (count > 0) {
                double R = 5, G = 5, B = 5;
                while (count > 0) {
                    int C1 = сolorBuffer[i][--count];
                    double alpha = (double) ((C1 >> 24) & 0xFF) / 100;
                    double alpha1 = 1 - alpha;
                    R = alpha * R + alpha1 * (C1 & 0xFF);
                    G = alpha * G + alpha1 * ((C1 >> 8) & 0xFF);
                    B = alpha * B + alpha1 * ((C1 >> 16) & 0xFF);
                }
                screenBuffer[i] = ((int) R) | (((int) G) << 8) | (((int) B) << 16);
            } else {
                screenBuffer[i] = ((int) 55) | (((int) 55) << 8) | (((int) 55) << 16);
            }
        }

        MemoryImageSource sourceImage = new MemoryImageSource(width, height, dcm, screenBuffer, 0, width);
        outputImage = converter.createImage(sourceImage);
        repainter(g);
    }
    Image outputImage;

    public void repainter(Graphics g) {
        if (g == null) {
            return;
        }
        g.drawImage(outputImage, 0, 0, null);

        // FPS
        if (frame == 0) {
            t1 = System.nanoTime();
        }
        frame++;
        if (frame % 1 == 0) {
            fps = (double) frame / (System.nanoTime() - t1) * 1e9;
        }
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 100, 10);
        g.setColor(Color.RED);
        g.drawString("fps: " + String.valueOf(fps), 0, 10);
    }

    private void draw(GM_Figure figure) {
        if (!figure.visible) {
            return;
        }
        for (GM_Figure tmp : figure.Children) {
            draw(tmp);
        }
        if (figure.Object != null) {
            switch (figure.type) {
                case GM_Figure.triangles:
                    for (int i = 0; i < figure.Object.length - 1; i++) {
                        for (int j = 0; j < figure.Object[0].length - 1; j++) {
                            GM_Vector p0 = figure.Object[i][j],
                                    p1 = figure.Object[i + 1][j],
                                    p2 = figure.Object[i][j + 1],
                                    p3 = figure.Object[i + 1][j + 1];
                            FilledTriangle(p1, p2, p0, figure.color.getColorI(p1, p2, p0, s.light, null));
                            FilledTriangle(p1, p2, p3, figure.color.getColorI(p1, p2, p3, s.light, null));
                        }
                    }
                    break;
            }
        }
        if (figure.Triangles != null) {
            for (int i = 0; i < figure.Triangles.length; i++) {
                if (figure.Triangles[i].visible) {
                    GM_Vector p1 = figure.Triangles[i].vertex[0],
                            p2 = figure.Triangles[i].vertex[1],
                            p3 = figure.Triangles[i].vertex[2];
                    FilledTriangle(p1, p2, p3, figure.color.getColorI(p1, p2, p3, s.light, null));
                }
            }
        }
    }

    void FilledTriangle(GM_Vector p1, GM_Vector p2, GM_Vector p3, int color) {
        GM_Vector t;
        if (p2.sy < p1.sy) {
            t = p1;
            p1 = p2;
            p2 = t;
        }
        if (p3.sy < p1.sy) {
            t = p1;
            p1 = p3;
            p3 = t;
        }
        if (p3.sy < p2.sy) {
            t = p2;
            p2 = p3;
            p3 = t;
        }

        if (p1.sx == p2.sx && p1.sy == p2.sy) {
            return;
        }
        if (p3.sx == p2.sx && p3.sy == p2.sy) {
            return;
        }

        drawHorizontalLines(p1, p2, p3, color);
        drawHorizontalLines(p3, p2, p1, color);
    }

    void drawHorizontalLines(GM_Vector pn, GM_Vector pk, GM_Vector pd, int color) {
        int n = (int) (pk.sy - pn.sy);
        if (n != 0) {
            double xl = pn.sx + width * (int) pn.sy,
                    xr = xl,
                    zl = pn.sz,
                    zr = zl,
                    dy = width,
                    dxl = (pk.sx - pn.sx) / (pk.sy - pn.sy) + dy,
                    dxr = (pd.sx - pn.sx) / (pd.sy - pn.sy) + dy,
                    dzl = (pk.sz - pn.sz) / (pk.sy - pn.sy),
                    dzr = (pd.sz - pn.sz) / (pd.sy - pn.sy);
            if (n < 0) {
                dxl = -dxl;
                dxr = -dxr;
                dzl = -dzl;
                dzr = -dzr;
            }
            if (dxr < dxl) {
                double t;
                t = dxl;
                dxl = dxr;
                dxr = t;
                t = dzl;
                dzl = dzr;
                dzr = t;
            }
            if (n < 0) {
                n = -n + 1;
            }
            for (int i = 0; i < n; i++) {
                for (int x = (int) (xl + 0.4999999999); x <= xr - 0.500000001; x++) {
                    if (x < 0 || x >= width * height) {
                        return;
                    }
                    double z = (x - xl) * (zr - zl) / (xr - xl);
                    double curZ = zl + z;
                    int d = 0;
                    int maxd = alphaBufDepth < zBufferC[x] ? alphaBufDepth : zBufferC[x];
                    while (d < maxd && curZ > zBuffer[x][d]) {
                        d++;
                    }
                    if (d < alphaBufDepth) {
                        if (maxd >= alphaBufDepth) {
                            maxd = alphaBufDepth - 1;
                        }
                        zBufferC[x] = maxd + 1;
                        while (maxd > d) {
                            zBuffer[x][maxd] = zBuffer[x][maxd - 1];
                            сolorBuffer[x][maxd] = сolorBuffer[x][maxd - 1];
                            maxd--;
                        }
                        zBuffer[x][d] = curZ;
                        сolorBuffer[x][d] = color;
                    }
                }
                xl += dxl;
                xr += dxr;
                zl += dzl;
                zr += dzr;
            }
        }
    }
}
