/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic2D.DrawMatrix;

import com.mathpar.func.F;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JFrame;
import com.mathpar.matrix.MatrixD;
import com.mathpar.matrix.MatrixS;
import com.mathpar.number.Element;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;
import com.mathpar.polynom.Polynom;

/**
 *
 * @author heckfy
 */
public class DrawMatrix extends JComponent {
    public Element A;
    public int width = 800;
    public int height = 600;

    public DrawMatrix() {
    }

    public DrawMatrix(Element A, int width_in, int height_in) {
        this.A = A;
        this.height = height_in;
        this.width = width_in;
    }

    public void draw() {
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
        f.setTitle("DrawMatrix");
        f.setVisible(true);
        f.setBounds(30, 40, width, height);
        f.getContentPane().add(this);
        f.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                height = (int) f.getBounds().getHeight();
                width = (int) f.getBounds().getWidth();
                f.repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (A instanceof MatrixS) {
            Graphics2D g2d = (Graphics2D) g;
            Color c;
            g2d.setBackground(Color.WHITE);
            Ring r = new Ring("R64[x]");
            int wi = this.width / ((MatrixS) A).col.length;
            int hi = this.height / ((MatrixS) A).size;
            Element h = ((MatrixS) A).max(r);
            while (((MatrixS) A).colNumb > this.height || ((MatrixS) A).size > this.width) {
                A = shift(((MatrixS) A), r);
            }
            if (h != new NumberR64(0)) {
                for (int i = 0; i < ((MatrixS) A).M.length; i++) {
                    for (int j = 0; j < ((MatrixS) A).M[i].length; j++) {
                        //1-b[i][j]/h;
                        //Вычисляется цвет для соответствующего элемента.
                        NumberR64 a1 = (NumberR64) (((MatrixS) A).M[i][j].abs(r)).divide(h.abs(r), r);
                        NumberR64 s = (NumberR64) new NumberR64("1").subtract(a1.abs(r), r);
                        float t = s.floatValue();
                        if (((MatrixS) A).M[i][j].compareTo(new NumberR64(0), r) == 1) {
                            c = new Color((float) 1, t, (float) 1);
                            g2d.setColor(c);
                        } else {
                            c = new Color(t, (float) 1, (float) 1);
                            g2d.setColor(c);
                        }
                        g2d.fillRect(((MatrixS) A).col[i][j] * wi, i * hi, wi, hi);
                    }
                }
            } else {
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, this.width, this.height);

            }
        } else if (A instanceof MatrixD) {
            Graphics2D g2d = (Graphics2D) g;
            Color c;
            Ring r = new Ring("R64[x]");
            Element h = ((MatrixD) A).max(r);
            Element l = ((MatrixD) A).min(r);
            NumberR64 k = (NumberR64) h.subtract(l, r);
            if (((MatrixD) A).M.length > this.height || ((MatrixD) A).M[0].length > this.width) {
                A = shift(((MatrixD) A), r);
            }
            int wi = this.width / ((MatrixD) A).M.length;
            int hi = this.height / ((MatrixD) A).M[0].length;
            if (k != new NumberR64(0)) {
                for (int i = 0; i < ((MatrixD) A).M.length; i++) {
                    for (int j = 0; j < ((MatrixD) A).M[0].length; j++) {
                        //1 - (a[i][j]-l) / (h-l);
                        //Вычисляется цвет для соответствующего элемента.
                        NumberR64 a1 = (NumberR64) (((MatrixD) A).M[i][j].abs(r)).subtract(l.abs(r), r);
                        NumberR64 a2 = (NumberR64) (h.abs(r).subtract(l.abs(r), r));
                        NumberR64 a3 = (NumberR64) a1.divide(a2, r).abs(r);
                        NumberR64 s = (NumberR64) new NumberR64("1").subtract(a3.abs(r), r);
                        float t = s.floatValue();
                        if (((MatrixD) A).M[i][j].compareTo(new NumberR64(0), r) == 1) {
                            c = new Color((float) 1, t, (float) 1);
                            g2d.setColor(c);
                        } else {
                            c = new Color(t, (float) 1, (float) 1);
                            g2d.setColor(c);
                        }
                        g2d.fillRect(j * wi, i * hi, wi, hi);
                    }
                }
            } else {
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, this.width, this.height);
            }
        }
    }
    //Уменьшает размерность матрицы. Для MatrixS.

    public MatrixS shift(MatrixS B, Ring r) {
        int kx = (B.colNumb / this.width) << 1;
        int ky = (B.size / this.height) << 1;
        Element a[][] = new Element[B.colNumb][B.size];
        Element Bm[][] = new Element[B.colNumb / kx][B.size / ky];
        for (int i = 0; i < B.colNumb / kx; i++) {
            for (int j = 0; j < B.size / ky; j++) {
                Element p = r.numberZERO;
                for (int i1 = i * kx; i1 < (i + 1) * kx; i1++) {
                    for (int j2 = j * ky; j2 < (j + 1) * ky; j2++) {
                        a[i][j] = B.getElement(i, j, r);
                        p = p.add(a[i][j], r);
                    }
                }
                Bm[i][j] = p.divide(new NumberR64(kx * ky), r);
            }
        }
        MatrixS MB = new MatrixS(Bm, r);
        return MB;
    }
    //Уменьшает размерность матрицы. Для MatrixD.

    public MatrixD shift(MatrixD A, Ring r) {
        int kx = (A.M.length / this.width) << 1;
        int ky = (A.M[0].length / this.height) << 1;
        Element Am[][] = new Element[A.M.length / kx][A.M[0].length / ky];
        for (int i = 0; i < A.M.length / kx; i++) {
            for (int j = 0; j < A.M[0].length / ky; j++) {
                Element p = r.numberZERO;
                for (int i1 = i * kx; i1 < (i + 1) * kx; i1++) {
                    for (int j2 = j * ky; j2 < (j + 1) * ky; j2++) {
                        p = p.add(A.M[i][j], r);
                    }
                }
                Am[i][j] = p.divide(new NumberR64(kx * ky), r);
            }
        }
        MatrixD MA = new MatrixD(Am);
        return MA;
    }
////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////2014///////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

    /**
     * Вычисление нормы полинома Сумма всех коэффициентов полинома по abs
     *
     * @param el - элемент матрицы
     * @param ring
     *
     * @return - норма полинома
     */
    public Element valueNAddCoeffs(Element el, Ring ring) {
        Polynom p = (Polynom) el;
        Element res = ring.numberZERO;
        for (int i = 0; i < p.coeffs.length; i++) {
            NumberR64 e = new NumberR64(Math.abs(p.coeffs[i].value));
            res = res.add(e, ring);
        }
        return res;
    }

    /**
     * Вычисление нормы полинома Максимальный коэффициент полинома по abs
     *
     * @param el - элемент матрицы
     * @param ring
     *
     * @return - норма полинома
     */
    public Element valueNMaxCoeffs(Element el, Ring ring) {
        Polynom p = (Polynom) el;
        double max = Math.abs(p.coeffs[0].value);
        for (int i = 1; i < p.coeffs.length; i++) {
            if (Math.abs(p.coeffs[i].value) > max) {
                max = Math.abs(p.coeffs[i].value);
            }
        }
        return new NumberR64(max);
    }

    /**
     * Вычисление нормы полинома Максимальная степень полинома
     *
     * @param el - элемент матрицы
     * @param ring
     *
     * @return - норма полинома
     */
    public Element valueNMaxPowers(Element el, Ring ring) {
        return new NumberR64(((Polynom) el).powers[0]);
    }

    /**
     * Вычисление нормы функции Максимальное значение функции на отрезке [0, 1]
     *
     * @param el - элемент матрицы
     * @param ring
     *
     * @return - норма полинома
     */
    public Element valueNMaxValueFunc(Element el, Ring ring) {
        double a = 0;
        Element max = ((F) el).valueOf(new Element[] {new NumberR64(a)}, ring);
        double h = 0.1;
        while (h <= 1) {
            NumberR64 v = new NumberR64(h);
            //шаг
            h += 0.1;
            Element m = new NumberR64(0);
            if (((F) el).name != 0) {
                m = ((F) el).valueOf(new Element[] {v}, ring);
            }
            if (m.isNaN()) {
                //eсли значение NaN
                //m = new NumberR64(Double.MAX_VALUE);
                m = new NumberR64(1);
            }
            if (!max.equals(m, ring)) {
                max = m;
            }
        }
        return max.abs(ring);
    }

    /**
     * Вычисление нормы функции Значение интеграла функции на отрезке [0, 1]
     * Формула трапеции
     *
     * @param el - элемент матрицы
     * @param ring
     *
     * @return - норма полинома
     */
    public Element valueNIntValueFunc(Element el, Ring ring) {
        //массив значений
        double[] val = new double[10];
        val[0] = ((F) el).valueOf(new Element[] {new NumberR64(0)}, ring).value;
        val[9] = ((F) el).valueOf(new Element[] {new NumberR64(1)}, ring).value;
        //частичная сумма
        double s1 = (val[0] + val[9]) / 2;
        //шаг
        double h = 0.1;
        //частичная сумма
        double s2 = 0;
        for (int i = 1; i < val.length - 1; i++) {
            NumberR64 v = new NumberR64(h);
            val[i] = ((F) el).valueOf(new Element[] {v}, ring).value;
            h += 0.1;
        }
        return (new NumberR64(h * (s1 + s2)).isNaN())? new NumberR64(1) : new NumberR64(h * (s1 + s2)).abs(ring);
    }

    /**
     * Конвертация Random полиномиальной матрицы в числовую MatrixD
     *
     * @param m - полиномиальная матрица
     * @param n - тип нормы для полиномов
     * @param ring
     *
     * @return - числовая матрица
     */
    public MatrixD convertRandomPolynomToMatrixD(MatrixD m, int n, Ring ring) {
        Element[][] res = new Element[m.M.length][];
        for (int i = 0; i < m.M.length; i++) {
            res[i] = new Element[m.M[i].length];
            for (int j = 0; j < m.M[i].length; j++) {
                //устанавливаем необходимую норму
                switch (n) {
                    case 0: {
                        res[i][j] = valueNAddCoeffs(m.M[i][j], ring);
                        break;
                    }
                    case 1: {
                        res[i][j] = valueNMaxCoeffs(m.M[i][j], ring);
                        break;
                    }
                    case 2: {
                        res[i][j] = valueNMaxPowers(m.M[i][j], ring);
                        break;
                    }
                }
            }
        }
        return new MatrixD(res);
    }

    /**
     * Конвертация Random полиномиальной матрицы в числовую MatrixS
     *
     * @param m - полиномиальная матрица
     * @param n - тип нормы для полиномов
     * @param ring
     *
     * @return - числовая матрица
     */
    public MatrixS convertRandomPolynomToMatrixS(MatrixS m, int n, Ring ring) {
        Element[][] res = new Element[m.M.length][];
        for (int i = 0; i < m.M.length; i++) {
            res[i] = new Element[m.M[i].length];
            for (int j = 0; j < m.M[i].length; j++) {
                //устанавливаем необходимую норму
                switch (n) {
                    case 0: {
                        res[i][j] = valueNAddCoeffs(m.M[i][j], ring);
                        break;
                    }
                    case 1: {
                        res[i][j] = valueNMaxCoeffs(m.M[i][j], ring);
                        break;
                    }
                    case 2: {
                        res[i][j] = valueNMaxPowers(m.M[i][j], ring);
                        break;
                    }
                }
            }
        }
        return new MatrixS(res, ring);
    }

    /**
     * Конвертация Random функциональной матрицы в числовую MatrixD
     *
     * @param m - функциональная матрица
     * @param n - тип нормы для функции
     * @param ring
     *
     * @return - числовая матрица
     */
    public MatrixD convertRandomFuncsToMatrixD(MatrixD m, int n, Ring ring) {
        Element[][] res = new Element[m.M.length][];
        for (int i = 0; i < m.M.length; i++) {
            res[i] = new Element[m.M[i].length];
            for (int j = 0; j < m.M[i].length; j++) {
                //устанавливаем необходимую норму
                switch (n) {
                    case 0: {
                        res[i][j] = valueNMaxValueFunc(m.M[i][j], ring);
                        break;
                    }
                    case 1: {
                        res[i][j] = valueNIntValueFunc(m.M[i][j], ring);
                        break;
                    }
                }
            }
        }
        return new MatrixD(res);
    }

    /**
     * Конвертация Random функциональной матрицы в числовую MatrixS
     *
     * @param m - функциональная матрица
     * @param n - тип нормы для функции
     * @param ring
     *
     * @return - числовая матрица
     */
    public MatrixS convertRandomFuncsToMatrixS(MatrixS m, int n, Ring ring) {
        Element[][] res = new Element[m.M.length][];
        for (int i = 0; i < m.M.length; i++) {
            res[i] = new Element[m.M[i].length];
            for (int j = 0; j < m.M[i].length; j++) {
                //устанавливаем необходимую норму
                switch (n) {
                    case 0: {
                        res[i][j] = valueNMaxValueFunc(m.M[i][j], ring);
                        break;
                    }
                    case 1: {
                        res[i][j] = valueNIntValueFunc(m.M[i][j], ring);
                        break;
                    }
                }
            }
        }
        return new MatrixS(res, ring);
    }

    /**
     * Random конструктор функциональной матрицы
     *
     * @param n - число строк
     * @param m - число столбцов
     * @param ring
     *
     * @return - MatrixD
     */
    public MatrixD randomMatrixDFunc(int n, int m, Ring ring) {
        Element[][] res = new Element[n][m];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[i].length; j++) {
                Polynom arg = new Polynom(new int[] {1, 100, 100}, new Random(), ring);
                int name = new Random().nextInt(21);
                if (name >= 4 && name <= 21) {
                    res[i][j] = new F(name, new Element[] {arg});
                } else {
                    res[i][j] = new F(4, new Element[] {arg});
                }
            }
        }
        return new MatrixD(res);
    }

    /**
     * Random конструктор функциональной матрицы
     *
     * @param n - число строк
     * @param m - число столбцов
     * @param ring
     *
     * @return - MatrixS
     */
    public MatrixS randomMatrixSFunc(int n, int m, Ring ring) {
        Element[][] res = new Element[n][m];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[i].length; j++) {
                Polynom arg = new Polynom(new int[] {1, 100, 100}, new Random(), ring);
                int name = new Random().nextInt(21);
                if (name >= 4 && name <= 21) {
                    res[i][j] = new F(name, new Element[] {arg});
                } else {
                    if (name == 0) {
                        res[i][j] = new F(0, new Element[] {arg});
                    } else {
                        res[i][j] = new F(4, new Element[] {arg});
                    }
                }
            }
        }
        return new MatrixS(res, ring);
    }
}
