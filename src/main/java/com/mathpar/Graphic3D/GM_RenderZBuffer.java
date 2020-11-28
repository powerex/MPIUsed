/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D;

import com.mathpar.func.Page;
import java.awt.*;
import java.awt.image.*;
import java.text.DecimalFormat;

/**
 * <p>
 * Класс GM_RenderZBuffer предназначен для преобразования массива треугольников
 * в двумерный массив (изображение) и вывода этого изображения на экран.</p>
 * <p>
 * Основной рабочей частью класса является алгоритм sz-буфера, модифицированный
 * для создания эффекта прозрачности.</p>
 * <p>
 * Это один из простейших алгоритмов удаления невидимых поверхностей. Впервые он
 * был предложен Кэтмулом [8]. Работает этот алгоритм в пространстве
 * изображения. Идея sz-буфера является простым обобщением идеи о буфере кадра.
 * Буфер кадра используется для запоминания атрибутов (интенсивности) каждого
 * пиксела в пространстве изображения, sz-буфер - это отдельный буфер глубины,
 * используемый для запоминания координаты sz или глубины каждого видимого
 * пиксела в пространстве изображения. В процессе работы глубина или значение sz
 * каждого нового пиксела, который нужно занести в буфер кадра, сравнивается с
 * глубиной того пиксела, который уже занесен в sz-буфер. Если это сравнение
 * показывает, что новый пиксел расположен впереди пиксела, находящегося в
 * буфере кадра, то новый пиксел заносится в этот буфер и, кроме того,
 * производится корректировка sz-буфера новым значением sz. Если же сравнение
 * дает противоположный результат, то никаких действий не производится. По сути,
 * алгоритм является поиском по х и у наибольшего значения функции sz (х,
 * у).</p>
 * <p>
 * Главное преимущество алгоритма - его простота. Кроме того, этот алгоритм
 * решает задачу об удалении невидимых поверхностей и делает тривиальной
 * визуализацию пересечений сложных поверхностей. Сцены могут быть любой
 * сложности. Поскольку габариты пространства изображения фиксированы, оценка
 * вычислительной трудоемкости алгоритма не более чем линейна. Поскольку
 * элементы сцены или картинки можно заносить в буфер кадра или в sz-буфер в
 * произвольном порядке, их не нужно предварительно сортировать по приоритету
 * глубины. Поэтому экономится вычислительное время, затрачиваемое на сортировку
 * по глубине.</p>
 * <p>
 * Другой недостаток алгоритма sz-буфера состоит в трудоемкости и высокой
 * стоимости устранения лестничного эффекта, а также реализации эффектов
 * прозрачности и просвечивания. Поскольку алгоритм заносит пикселы в буфер
 * кадра в произвольном порядке, то нелегко получить информацию, необходимую для
 * методов устранения лестничного эффекта, основывающихся на предварительной
 * фильтрации. При реализации эффектов прозрачности и просвечивания, пикселы
 * могут заноситься в буфер кадра в некорректном порядке, что ведет к локальным
 * ошибкам.</p>
 * <p>
 * Более формальное описание алгоритма sz-буфера таково:</p>
 * <p>
 * 1) Заполнить буфер кадра фоновым значением интенсивности или цвета.</p>
 * <p>
 * 2) Заполнить sz -буфер минимальным значением sz.</p>
 * <p>
 * 3) Преобразовать каждый многоугольник в растровую форму в произвольном
 * порядке.</p>
 * <p>
 * 4) Для каждого Пиксел(sx,sy) в многоугольнике вычислить его глубину
 * sz(sx,sy).</p>
 * <p>
 * 5) Сравнить глубину sz(х,у) со значением Zбуфер(х,у), хранящимся в sz-буфере
 * в этой же позиции.</p>
 * <p>
 * 6) Если sz(х, у) > Zбуфер (х,у), то записать атрибут этого многоугольника
 * (интенсивность, цвет и т. п.) в буфер кадра и заменить Zбуфер(х,у) на
 * sz(х,у). В противном случае никаких действий не производить.</p>
 *
 * @author JuliaGrishina
 */
public class GM_RenderZBuffer {

    /**
     * Буфер изображения хранит несколько слоёв изображения, то есть для каждой
     * точки изображения хранятся цвета соответствующих проецируемых в эту точку
     * поверхностей в порядке удалённости их от плоскости экрана.
     */
    public int[][] сolorBuffer;
    /**
     * Каждому элементу в буфере изображения соответствует элемент в буфере
     * глубины, в котором хранится соответствующее расстояние от плоскости
     * экрана до проецируемой поверхности.
     */
    public double[][] zBuffer;
    /**
     * Для подсчёта количества слоёв в каждой точке используется отдельный
     * массив.
     */
    public int[] zBufferC;
    /**
     * Переменная alphaBufDepth задаёт максимально возможное количество слоёв.
     */
    public int alphaBufDepth = 20;
    /**
     * В экранном буфере помещается готовое изображение, готовое для вывода на
     * экран.
     */
    public int[] screenBuffer, screen1, screen2;
    /**
     * Переменная GM_Scene s,s1,s2,Scn хранит объект сцены.
     */
    public GM_Scene s, s1, s2, Scn;
    int width;
    int height;
    /**
     * Цветовая модель для построения изображения
     */
    DirectColorModel dcm = new DirectColorModel(160, 0x00FF0000, 0x0000FF00, 0x000000FF);
    Component converter = new Canvas();
    public GM_Vector light = new GM_Vector(-200, -100, -300);
    public GM_Vector viewer = new GM_Vector(0, 0, -500);
    GM_Light J;
    /**
     * ПЕременная содержит изображение построенное по массиву точек
     */
    Image outputImage;
    RenderWindow renderW;
    boolean isPlotXyz = false, isPlotZ = false;
    public BufferedImage image;
    int look = 0;
    /**
     * Массив точек-экстремумов построения,необходимых для построения осей
     * координат
     */
    public GM_Vector[] extremPoints = {
        new GM_Vector(0, 0, 0),
        new GM_Vector(0, 0, 0),
        new GM_Vector(0, 0, 0),
        new GM_Vector(0, 0, 0),
        new GM_Vector(0, 0, 0),
        new GM_Vector(0, 0, 0),
        new GM_Vector(0, 0, 0),
        new GM_Vector(0, 0, 0)
    };
    double k_size = 1;

    public GM_RenderZBuffer() {
    }

    /**
     * @param renderWnd
     * @param width
     * @param height
     * @param plot3dZ {@code true}, если поверхность задана явно.
     * @param plot3dXYZ {@code true}, если поверхность задана параметрически.
     */
    public GM_RenderZBuffer(RenderWindow renderWnd, int width, int height, boolean plot3dZ, boolean plot3dXYZ) {
        this.renderW = renderWnd;
        this.isPlotZ = plot3dZ;
        this.isPlotXyz = plot3dXYZ;
        J = new GM_Light(light, viewer);
        Scn = new GM_Scene(renderWnd.page, width, height, 20);
        s = new GM_Scene(renderWnd.page, width, height, 20);

        s1 = new GM_Scene(renderWnd.page, width, height, 20);
        s2 = new GM_Scene(renderWnd.page, width, height, 20);

        this.width = width;
        this.height = height;
        сolorBuffer = new int[width * height][alphaBufDepth];
        zBuffer = new double[width * height][alphaBufDepth];
        zBufferC = new int[width * height];
        screenBuffer = new int[width * height];
        screen1 = new int[width * height];
        screen2 = new int[width * height];

        s1.rotateAndMove(2.15, 0.25, 0.2, 0, 0, 0);
        s2.rotateAndMove(2.15, 0.25, 0.2, 0, 0, 0);
        s.rotateAndMove(2.15, 0.25, 0.2, 0, 0, 0);
        Scn.rotateAndMove(2.15, 0.25, 0.2, 0, 0, 0);

        s2.rotateAndMove(0, 0.02, 0, 0, 0, 0);
        s1.rotateAndMove(0, -0.02, 0, 0, 0, 0);
    }

    /**
     * Коррекция размеров изображения под границы изображения. Слишком маленькое
     * изображение увеличивается,большое-уменьшается.
     *
     * @param isSizeChanged изменены ли размеры изображения до вызова этого
     * метода.
     * @param isScaleChanged изменен ли масштаб по одной из осей до вызова этого
     * метода.
     *
     * @return {@code true} если были изменены размеры изображение, иначе
     * {@code false}.
     */
    public boolean correctSize(boolean isSizeChanged, boolean isScaleChanged) {
        boolean isSizeChangedNow = false; // was size actually corrected?
        GM_Vector zero = new GM_Vector(extremPoints[7].sx, extremPoints[7].sy, extremPoints[7].sz);

        GM_Vector[] sExtr_scr = {
            new GM_Vector(extremPoints[0].sx, extremPoints[0].sy, extremPoints[0].sz),
            new GM_Vector(extremPoints[1].sx, extremPoints[1].sy, extremPoints[1].sz),
            new GM_Vector(extremPoints[2].sx, extremPoints[2].sy, extremPoints[2].sz),
            new GM_Vector(extremPoints[3].sx, extremPoints[3].sy, extremPoints[3].sz),
            new GM_Vector(extremPoints[4].sx, extremPoints[4].sy, extremPoints[4].sz),
            new GM_Vector(extremPoints[5].sx, extremPoints[5].sy, extremPoints[5].sz),
            new GM_Vector(extremPoints[6].sx, extremPoints[6].sy, extremPoints[6].sz),
            new GM_Vector(extremPoints[7].sx, extremPoints[7].sy, extremPoints[7].sz)
        };

        GM_Vector max_vect = new GM_Vector(extremPoints[7].sx, extremPoints[7].sy, extremPoints[7].sz);
        for (GM_Vector tmp : sExtr_scr) {
            if (tmp.getDistance(zero) > max_vect.getDistance(zero)) {
                max_vect = tmp;
            }
        }
        if (!isSizeChanged && max_vect.getDistance(zero) < 150) {
            k_size = 150 / max_vect.getDistance(zero);
            s.rotateAndMove(0, 0, 0, 0, 0, 0, k_size);
            s1.rotateAndMove(0, 0, 0, 0, 0, 0, k_size);
            s2.rotateAndMove(0, 0, 0, 0, 0, 0, k_size);
            Scn.rotateAndMove(0, 0, 0, 0, 0, 0, k_size);
            isSizeChangedNow = true;
            renderW.update(this, isSizeChangedNow, isScaleChanged);
        } else if (!isSizeChanged && max_vect.getDistance(zero) > 350) {
            isSizeChangedNow = true;
            k_size = - 300 / max_vect.getDistance(zero);
            if (Math.abs(k_size) < 0.1) {
                k_size = k_size * 10;
            }

            s.rotateAndMove(0, 0, 0, 0, 0, 0, k_size);
            s1.rotateAndMove(0, 0, 0, 0, 0, 0, k_size);
            s2.rotateAndMove(0, 0, 0, 0, 0, 0, k_size);
            Scn.rotateAndMove(0, 0, 0, 0, 0, 0, k_size);

            renderW.update(this, isSizeChangedNow, isScaleChanged);
        }
        return isSizeChangedNow;
    }

    /**
     * Коррекция масштаба по одной из осей, сжатие по оси происходит в
     * случае,если длина ее в 3 раза превышает длину наименьшей из осей. Все
     * точки объектов уже построенных, и тех,что буду строится умножаются на
     * коэффициент kx,ky или kz в зависимости от оси сжатия.
     *
     * @param isSizeChanged изменены ли размеры изображения до вызова этого
     * метода.
     * @param isScaleChanged изменен ли масштаб по одной из осей до вызова этого
     * метода.
     *
     * @return {@code true}, если был изменен масштаб по одной из осей, иначе
     * {@code false}.
     */
    public boolean correctScale(boolean isSizeChanged, boolean isScaleChanged) {
        boolean isScaleChangedNow = false;
        double min = 100000;
        double max = 0;
        double rX = renderW.maxX - renderW.minX;
        double rY = renderW.maxY - renderW.minY;
        double rZ = renderW.maxZ - renderW.minZ;

        if (rX > rY && rX > rZ) {
            max = rX;
        }
        if (rY > rX && rY > rZ) {
            max = rY;
        }
        if (rZ > rY && rZ > rX) {
            max = rZ;
        }
        if (rX <= rY && rX <= rZ) {
            min = rX;
        }
        if (rY < rX && rY <= rZ) {
            min = rY;
        }
        if (rZ < rY && rZ < rX) {
            min = rZ;
        }

        if ((max > 3 * min && min > 50)) {
            isScaleChangedNow = true;
            if (max == rX) {
                renderW.kx = (3 * min) / max;
                s.rotateAndMove(renderW.kx, 1, 1);
                s1.rotateAndMove(renderW.kx, 1, 1);
                s2.rotateAndMove(renderW.kx, 1, 1);
                for (int i = 0; i < renderW.MaximumX.size(); i++) {
                    renderW.MaximumX.set(i, renderW.MaximumX.get(i) * renderW.kx);
                }
                for (int i = 0; i < renderW.MinimumX.size(); i++) {
                    renderW.MinimumX.set(i, renderW.MinimumX.get(i) * renderW.kx);
                }
            }
            if (max == rY) {
                renderW.ky = (3 * min) / max;
                s.rotateAndMove(1, renderW.ky, 1);
                s1.rotateAndMove(1, renderW.ky, 1);
                s2.rotateAndMove(1, renderW.ky, 1);
                for (int i = 0; i < renderW.MaximumY.size(); i++) {
                    renderW.MaximumY.set(i, renderW.MaximumY.get(i) * renderW.ky);
                }
                for (int i = 0; i < renderW.MinimumY.size(); i++) {
                    renderW.MinimumY.set(i, renderW.MinimumY.get(i) * renderW.ky);
                }
            }
            if (max == rZ) {
                renderW.kz = (3 * min) / max;
                s.rotateAndMove(1, 1, renderW.kz);
                s1.rotateAndMove(1, 1, renderW.kz);
                s2.rotateAndMove(1, 1, renderW.kz);
                for (int i = 0; i < renderW.MaximumZ.size(); i++) {
                    renderW.MaximumZ.set(i, renderW.MaximumZ.get(i) * renderW.kz);
                }
                for (int i = 0; i < renderW.MinimumZ.size(); i++) {
                    renderW.MinimumZ.set(i, renderW.MinimumZ.get(i) * renderW.kz);
                }
            }
            renderW.update(this, isSizeChanged, isScaleChangedNow);
        }
        return isScaleChangedNow;
    }

    /**
     * Метод заполнения экранного буфера
     *
     * @param screenBuff
     */
    public void fillScreenBuff(int[] screenBuff) {
        int len = zBuffer.length;
        for (int i = 0; i < len; i++) {
            int count = zBufferC[i];
            double R = renderW.bgColor.getRed(),
                    G = renderW.bgColor.getGreen(),
                    B = renderW.bgColor.getBlue();
            if (count > 0) {
                while (count > 0) {
                    int C1 = сolorBuffer[i][--count];
                    double alpha = (double) ((C1 >> 24)) / RenderWindow.tspValue;
                    double alpha1 = 1 - alpha;
                    R = alpha * R + alpha1 * (C1 & 0xFF);
                    G = alpha * G + alpha1 * ((C1 >> 8) & 0xFF);
                    B = alpha * B + alpha1 * ((C1 >> 16) & 0xFF);
                }
                screenBuff[i] = ((int) R) | (((int) G) << 8) | (((int) B) << 16);
            } else {
                screenBuff[i] = ((int) R) | (((int) G) << 8) | (((int) B) << 16);
            }
        }

    }

    /**
     * Метод передачи изображения image типа BufferedImage объекту класса
     * UtilWeb после корректировок объектов методами correctSize() и
     * сorrectAxScale()
     *
     * @param g Graphics
     * @param isSizeChanged изменены ли размеры изображения до вызова этого
     * метода.
     * @param isScaleChanged изменен ли масштаб по одной из осей до вызова этого
     * метода.
     */
    public void draw(Graphics g, boolean isSizeChanged, boolean isScaleChanged) {
        if (isPlotXyz || isPlotZ) {
            look++;
            image = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_RGB);
            g = image.getGraphics();
        }

        extremPoints = new GM_Vector[] {
            new GM_Vector(renderW.maxX, renderW.maxY, renderW.minZ),
            new GM_Vector(renderW.minX, renderW.maxY, renderW.minZ),
            new GM_Vector(renderW.minX, renderW.minY, renderW.minZ),
            new GM_Vector(renderW.minX, renderW.minY, renderW.maxZ),
            new GM_Vector(renderW.minX, renderW.maxY, renderW.maxZ),
            new GM_Vector(renderW.maxX, renderW.maxY, renderW.maxZ),
            new GM_Vector(renderW.maxX, renderW.minY, renderW.minZ),
            new GM_Vector(0, 0, 0)
        };

        boolean isScaleChangedNow = correctScale(isSizeChanged, isScaleChanged);
        Scn.applyTransformation(this, width / 2, height / 2, 500);
        s1.applyTransformation(this, width / 2 + 7, height / 2, 500);
        s2.applyTransformation(this, width / 2 - 7, height / 2, 500);
        s.applyTransformation(this, width / 2, height / 2, 500);
        boolean isSizeChangedNow = correctSize(isSizeChanged, isScaleChangedNow);
        if (renderW.isForWeb) {
            Page page = renderW.page;
            page.matrix3d(s.m);
            double[][] xyzCube = page.xyzCube();
            for (int i = 0, l = extremPoints.length - 1; i < l; i++) {
                xyzCube[i][0] = extremPoints[i].x;
                xyzCube[i][1] = extremPoints[i].y;
                xyzCube[i][2] = extremPoints[i].z;
            }
        }
        int len = zBuffer.length;
        for (int i = 0; i < len; i++) {
            zBuffer[i][0] = 0x7FFFFFFF; //Буфер глубины
            zBufferC[i] = 0;  //Счётчик слоёв.
        }

        if (renderW.press) {
            AddAxes();
        }

        if (!isSizeChangedNow && !isScaleChangedNow) {
            if (!renderW.vision3D) {
                draw(s);
                fillScreenBuff(screenBuffer);
            } else {
                draw(s1);
                fillScreenBuff(screen1);
                //обнуляем массив глубины и слоев
                for (int i = 0; i < len; i++) {
                    zBuffer[i][0] = 0x7FFFFFFF; //Буфер глубины
                    zBufferC[i] = 0;  //Счётчик слоёв.
                }
                draw(s2);
                fillScreenBuff(screen2);
                //Массив по которому создается изображение заполняется объединением массивов изображений для построения анаглифа
                for (int i = 0; i < len; i++) {
                    screenBuffer[i] = screen2[i] | screen1[i];
                }
            }
        }

        MemoryImageSource sourceImage = new MemoryImageSource(width, height, dcm, screenBuffer, 0, width);
        outputImage = converter.createImage(sourceImage);

        repainter(g);

        if ((isPlotXyz || isPlotZ)) {
            if (!isSizeChangedNow && !isScaleChangedNow) {
                if (renderW.isForWeb) {
                    renderW.page.putImage(image);
                }
            }
        }
    }

    /**
     * <p>
     * Метод отрисовки на объекте g типа Graphics построенного изображения
     * outputImage, а так же вызов метода построения осей addText(),если нажата
     * соответствующая кнопка</p>
     *
     * @param g Graphics
     */
    public void repainter(Graphics g) {
        if (g == null) {
            return;
        }
        g.drawImage(outputImage, 0, 0, null);
        g.setColor(renderW.txtColor);
        if (renderW.press) {
            addText(g);
        }
    }

    /**
     * <p>
     * Метод заполнения точки с координатами (curX,curY,curZ) цветом color.</p>
     * <p>
     * xl-номер пиксела изображения в одномерном массиве.</p>
     * <p>
     * xl=curX + width * curY</p>
     * <p>
     * где width - ширина строимого изображения</p>
     *
     * @param xl
     * @param curZ
     * @param color
     */
    private void fillPoint(int xl, double curZ, int color) {
        int d = 0;
        int maxd = alphaBufDepth < zBufferC[xl] ? alphaBufDepth : zBufferC[xl];
        while (d < maxd && curZ > zBuffer[xl][d]) {
            d++;
        }
        if (d < alphaBufDepth) {
            if (maxd >= alphaBufDepth) {
                maxd = alphaBufDepth - 1;
            }
            zBufferC[xl] = maxd + 1;
            while (maxd > d) {

                zBuffer[xl][maxd] = zBuffer[xl][maxd - 1];
                сolorBuffer[xl][maxd] = сolorBuffer[xl][maxd - 1];
                maxd--;
            }
            zBuffer[xl][d] = curZ - 2;
            сolorBuffer[xl][d] = color;
        }
    }

    /**
     * <p>
     * Рассчет точек отрезка АВ для добавления в z-буфер их атрибутов.</p>
     * <p>
     * Используя экранные координаты, высчитываются коэффициенты k и b для
     * уравнения прямой на плоскости y=kx+b.</p>
     * <p>
     * Рассматривая разность х-координат и у-координат точек А и В , выбираем
     * большую,для более плотного изображения точек отрезка. </p>
     * <p>
     * Для определения величины sz (глубина ) используем каноническое задание
     * прямой в пространстве </p>
     * <p>
     * (x-x1)/(x2-x1)=(y-y1)/(y2-y1)=(z-z1)/(z2-z1)</p>
     * <p>
     * Переменная xl-рассчет номера элемента массива zBuffer для данной точки
     * </p>
     *
     * @param A радиус-вектор начала отрезка
     * @param B радиус-вектор конца отрезка
     * @param color цвет отрезка
     */
    public void drawL(GM_Vector A, GM_Vector B, int color) {
        double koeffZ, k, b;
        int count;

        int countX = (int) (B.sx - A.sx);
        int countY = (int) (B.sy - A.sy);
        k = (B.sy - A.sy) / (B.sx - A.sx);
        b = B.sy - k * B.sx;
        if (Math.abs(countX) > Math.abs(countY)) {
            count = countX;
            if (count < 0) {
                GM_Vector T = A;
                A = B;
                B = T;
            }

            koeffZ = (B.sz - A.sz) / (B.sx - A.sx);
            for (int curX = (int) A.sx; curX < (int) B.sx; curX++) {
                double curY = curX * k + b;
                int xl = (int) (curX + width * (int) (curY));
                double curZ = A.sz + (curX - A.sx) * koeffZ;
                if (0 < xl && xl < width * height && curX < 1000 && curX > 0 && curY > 0 && curY < 700) {
                    fillPoint(xl, curZ, color);
                }
            }
        } else {
            count = countY;
            if (count < 0) {
                GM_Vector T = A;
                A = B;
                B = T;
            }
            koeffZ = (B.sz - A.sz) / (B.sy - A.sy);
            for (int curY = (int) A.sy; curY < (int) B.sy; curY++) {
                double curX = (curY - b) / k;
                int xl = ((int) (curX) + width * (int) (curY));
                double curZ = A.sz + (curY - A.sy) * koeffZ;
                if (0 < xl && xl < width * height && curY < 700 && curY > 0 && curX > 0 && curX < 1000) {
                    fillPoint(xl, curZ, color);
                }
            }
        }
    }

    /**
     * Добавление осей координат Создаем двумерный массив,который хранит точки
     * эктремумов объектов сцены для построения осей. Все точки попарно в каждой
     * плоскости соединяются.
     */
    public void AddAxes() {
        GM_Vector[][] Am = {
            {extremPoints[1], extremPoints[0], extremPoints[5], extremPoints[4], extremPoints[1]},
            {extremPoints[1], extremPoints[4], extremPoints[3], extremPoints[2], extremPoints[1]},
            {extremPoints[1], extremPoints[0], extremPoints[6], extremPoints[2], extremPoints[1]}
        };
        int color;
        color = ((int) renderW.txtColor.getRed()) | (((int) renderW.txtColor.getGreen()) << 8) | (((int) renderW.txtColor.getBlue()) << 16);

        for (int i = 0; i < 3; i++) {
            for (int p = 0; p < 4; p++) {
                drawL(Am[i][p], Am[i][p + 1], color);
            }
        }
    }

    /**
     * Установка необходимых параметров текста. Добавление подписей осей
     * координат на объект g типа Graphics.
     *
     * @param g
     */
    public void addText(Graphics g) {
        DecimalFormat f = new DecimalFormat("#,##0.0");
        Font font = new Font("Arial Bold", Font.TYPE1_FONT, 9);
        g.setFont(font);
        g.setColor(Color.red);
        g.drawString("" + f.format(renderW.maxX_name), (int) extremPoints[6].sx - 10, (int) extremPoints[6].sy + 10);
        g.drawString("" + f.format(renderW.minX_name), (int) extremPoints[2].sx, (int) extremPoints[2].sy + 10);
        g.drawString("X", (int) (extremPoints[6].sx), (int) (extremPoints[6].sy) + 20);

        g.setColor(Color.green);
        g.drawString("" + f.format(renderW.maxY_name), (int) extremPoints[1].sx - 25, (int) extremPoints[1].sy);
        g.drawString("" + f.format(renderW.minY_name), (int) extremPoints[2].sx - 20, (int) extremPoints[2].sy - 5);
        g.drawString("Y", (int) (extremPoints[1].sx) - 13, (int) (extremPoints[1].sy) + 15);

        g.setColor(Color.blue);
        g.drawString("" + f.format(renderW.minZ_name), (int) extremPoints[2].sx - 25, (int) extremPoints[2].sy + 10);
        g.drawString("" + f.format(renderW.maxZ_name), (int) extremPoints[3].sx - 20, (int) extremPoints[3].sy - 5);

        g.drawString("Z", (int) (extremPoints[3].sx) - 10, (int) (extremPoints[3].sy) - 13);
    }

    /**
     * <p>
     * Метод заполнения треугольников,из которых состоит объект.Метод вызывается
     * для сцены,и далее выполняется для всех объектов,прикрепленных к ней. </p>
     *
     * @param plane объект типа GM_Figure
     */
    private void draw(GM_Figure figure) {
        J = new GM_Light(light, viewer);
        for (GM_Figure tmp : figure.Children) {
            draw(tmp);
        }
        if (figure.Object != null) {
            for (int i = 0; i < figure.Object.length - 1; i++) {
                for (int j = 0; j < figure.Object[0].length - 1; j++) {
                    GM_Vector p0 = figure.Object[i][j],
                            p1 = figure.Object[i + 1][j],
                            p2 = figure.Object[i][j + 1],
                            p3 = figure.Object[i + 1][j + 1];
                    if (renderW.meshObject) {
                        if (figure.Parent.equals(s2)) {
                            int color1 = compressColor(new Color(0, 78, 78));
                            if (p1.z <= renderW.maxZ && p2.z <= renderW.maxZ
                                    && p3.z <= renderW.maxZ && p0.z <= renderW.maxZ) {
                                drawL(p0, p1, color1);
                                drawL(p2, p0, color1);
                                drawL(p3, p1, color1);
                                drawL(p2, p3, color1);
                            }
                        }
                        if (figure.Parent.equals(s1)) {
                            int color1 = compressColor(new Color(80, 0, 0));
                            if (p1.z <= renderW.maxZ && p2.z <= renderW.maxZ
                                    && p3.z <= renderW.maxZ && p0.z <= renderW.maxZ) {
                                drawL(p0, p1, color1);
                                drawL(p2, p0, color1);
                                drawL(p3, p1, color1);
                                drawL(p2, p3, color1);
                            }
                        }
                        if (figure.Parent.equals(s)) {
                            if (p1.z <= renderW.maxZ && p2.z <= renderW.maxZ
                                    && p3.z <= renderW.maxZ && p0.z <= renderW.maxZ) {
                                drawL(p0, p1, 0);
                                drawL(p2, p0, 0);
                                drawL(p3, p1, 0);
                                drawL(p2, p3, 0);
                            }
                        }
                    }
                    filledTriangle(p1, p0, p2, figure.color.getColorI(p1, p0, p2, J));
                    filledTriangle(p2, p3, p1, figure.color.getColorI(p2, p3, p1, J));
                }
            }
        }
    }

    /**
     * Метод проверки на правильность подачи вершин треугольника, для заполнения
     * его цветом. экранные y-координаты вершин должны располагаться в порядке
     * возрастания p1.sy,p2.sy,p3.sy Вершины не должны превышать имеющиеся
     * ограничения изображения-иначе отрисовки этого треугольника не будет
     *
     * @param p1
     * @param p2
     * @param p3
     * @param color
     */
    void filledTriangle(GM_Vector p1, GM_Vector p2, GM_Vector p3, int color) {
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
        if (p1.sx > 0 && p1.sy > 0 && p1.sx < 1000 && p1.sy < 700
                && p2.sx > 0 && p2.sy > 0 && p2.sx < 1000 && p2.sy < 700
                && p3.sx > 0 && p3.sy > 0 && p3.sx < 1000 && p3.sy < 700) {
            drawHorizontalLines(p1, p2, p3, color);
            drawHorizontalLines(p3, p2, p1, color);
        }
    }

    /**
     * Метод непосредственного заполнения треугольника цветом. Треугольник
     * заполняется горизонтальными линиями.Значения цвета заносятся в массив
     * сolorBuffer
     *
     * @param pn
     * @param pk
     * @param pd
     * @param color
     */
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

    private static int compressColor(Color c) {
        return ((int) c.getRed() << 16) | (((int) c.getGreen()) << 8) | (((int) c.getBlue()));
    }
}
