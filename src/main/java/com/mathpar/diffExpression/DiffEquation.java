/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.diffExpression;

import com.mathpar.func.*;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 *
 * @author mixail
 */
public class DiffEquation {

    // массив выражений при d'
    public Element[] masPol;
    //массив для хранения d' в степенях
    public int[][] m;

    public DiffEquation() {
        this.masPol = new Element[]{};
        this.m = new int[][]{};
    }

    public DiffEquation(Element[] el, int[][] m) {
        this.m = m;
        this.masPol = el;
    }

    /**
     * Определяет максимальную производную во входном дифференциальном уравнении
     *
     * @param f - дифференциальное уравнение
     * @param ring
     * @return
     */
    public int maxDerivative(F f, Ring ring) {
        if (f.name == F.d) {
            if (f.X.length == 2) {
                return 1;
            } else {
                return f.X[2].intValue();
            }
        }
        int n = f.X.length;
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (f.X[i] instanceof F) {
                if (((F) f.X[i]).name == F.d) {
                    int q = 0;
                    if (((F) f.X[i]).X.length == 2) {
                        q = 1;
                    } else {
                        q = ((F) f.X[i]).X[2].intValue();
                    }
                    if (q > k) {
                        k = q;
                    }
                } else {
                    for (int j = 0; j < ((F) f.X[i]).X.length; j++) {
                        if (((F) f.X[i]).X[j] instanceof F) {
                            if (((F) ((F) f.X[i]).X[j]).name == F.intPOW) {
                                if (((F) ((F) ((F) f.X[i]).X[j]).X[0]).name == F.d) {
                                    int q = 0;
                                    if (((F) ((F) ((F) f.X[i]).X[j]).X[0]).X.length == 2) {
                                        q = 1;
                                    } else {
                                        q = ((F) ((F) ((F) f.X[i]).X[j]).X[0]).X[2].intValue();
                                    }
                                    if (q > k) {
                                        k = q;
                                    }
                                }
                            }
                            if (((F) ((F) f.X[i]).X[j]).name == F.d) {
                                int q = 0;
                                if (((F) ((F) f.X[i]).X[j]).X.length == 2) {
                                    q = 1;
                                } else {
                                    q = ((F) ((F) f.X[i]).X[j]).X[2].intValue();
                                }
                                if (q > k) {
                                    k = q;
                                }
                            }
                        }
                    }
                }
            } else {
            }
        }
        return k;
    }

    /**
     * Метод преобразования по Лапласу входного дифференциального уравнения
     *
     * @param f - дифференциальное уравнение
     * @return - возвращает матрицу преобразования
     */
    public void DifEquationToMatrix1(Element f, int[][] m, Element[] masPol, Ring ring) {
        Element right_f = f;
        int k = 0;//колличество слагаемых во входном выражении
        int k2 = 0;//колличество проходов в исходном дереве
        boolean fl = false;
        if (((F) right_f).name == F.d) {
            int maxD = maxDerivative(((F) right_f), ring);
            masPol = new Element[1];
            masPol[0] = ring.numberONE();
            m = new int[1][maxD];
            if (((F) right_f).X.length == 2) {
                m[0][0] = 1;
            } else {
                m[0][((F) right_f).X[2].intValue() - 1] = 1;
            }
            this.m = m;
            this.masPol = masPol;
            return;
        } else if (((F) right_f).name == F.intPOW) {
            fl = true;
            k = 1;
            k2 = 1;
        } else {
            if (((F) right_f).name == F.MULTIPLY) {
                fl = true;
                k = 1;
                k2 = ((F) right_f).X.length;
            } else {
                k = ((F) right_f).X.length;
                k2 = ((F) right_f).X.length;
            }
        }
        int maxD = maxDerivative(((F) right_f), ring);
        masPol = new Element[k];
        m = new int[k][maxD];
        int k1 = 0;
        for (int i = 0; i < k2; i++) {
            if (((F) right_f).X[i] instanceof F) {
                switch (((F) ((F) right_f).X[i]).name) {
                    case F.MULTIPLY: {
                        F ff = ((F) ((F) right_f).X[i]);
                        if (!findD(ff, ring, false)) {
                            masPol[k1] = ff;
                            k1++;
                            break;
                        } else {
                            int k3 = 0;
                            Element[] ell = new Element[]{};
                            boolean fl1 = false;
                            if (ff.X.length > 2) {
                                fl1 = true;
                                ell = new Element[ff.X.length - 1];
                            }
                            for (int j = 0; j < ff.X.length; j++) {
                                if (ff.X[j] instanceof F) {
                                    switch (((F) ff.X[j]).name) {
                                        case F.d: {
                                            if (((F) ff.X[j]).X.length == 2) {
                                                m[k1][0] = 1;
                                            } else {
                                                m[k1][((F) ff.X[j]).X[2].intValue() - 1] = 1;
                                            }
                                            break;
                                        }
                                        case F.intPOW: {
                                            if (((F) ((F) ff.X[j]).X[0]).X.length == 2) {
                                                m[k1][0] = ((F) ff.X[j]).X[1].intValue();
                                            } else {
                                                m[k1][((F) ((F) ff.X[j]).X[0]).X[2].intValue() - 1] = ((F) ff.X[j]).X[1].intValue();
                                            }
                                            break;
                                        }
                                        default: {
                                            if (fl1) {
                                                ell[k3] = ff.X[j];
                                                k3++;
                                            } else {
                                                masPol[k1] = ff.X[j];
                                            }
                                        }
                                    }
                                } else {
                                    if (ff.X[j] instanceof Polynom) {
                                        if (fl1) {
                                            ell[k3] = ff.X[j];
                                            k3++;
                                        } else {
                                            masPol[k1] = ff.X[j];
                                        }
                                    } else {
                                        if (ff.X[j] instanceof Fname) {
                                            if (fl1) {
                                                ell[k3] = ff.X[j];
                                                k3++;
                                            } else {
                                                masPol[k1] = ff.X[j];
                                            }
                                        } else {
                                            if (fl1) {
                                                ell[k3] = ff.X[j];
                                                k3++;
                                            } else {
                                                masPol[k1] = ff.X[j];
                                            }
                                        }
                                    }
                                }
                            }
                            if (fl1) {
                                masPol[k1] = new F(F.MULTIPLY, ell);
                            }
                        }
                        k1++;
                        break;
                    }
                    case F.d: {
                        masPol[k1] = ring.numberONE();
                        if (((F) ((F) right_f).X[i]).X.length == 2) {
                            if (fl) {
                                m[k1][0] = ((F) right_f).X[1].intValue();
                            } else {
                                m[k1][0] = 1;
                            }
                        } else {
                            if (fl) {
                                m[k1][((F) ((F) right_f).X[i]).X[2].intValue() - 1] = ((F) right_f).X[1].intValue();
                            } else {
                                m[k1][((F) ((F) right_f).X[i]).X[2].intValue() - 1] = 1;
                            }
                        }
                        k1++;
                        break;
                    }
                    case F.intPOW: {
                        if (((F) ((F) right_f).X[i]).X[0] instanceof F) {
                            masPol[k1] = ring.numberONE();
                            if (((F) ((F) ((F) right_f).X[i]).X[0]).X.length == 2) {
                                if (fl) {
                                    m[k1][0] = ((F) ((F) right_f).X[i]).X[1].intValue();
                                } else {
                                    m[k1][0] = 1;
                                }
                            } else {
                                if (fl) {
                                    m[k1][((F) ((F) ((F) right_f).X[i]).X[0]).X[2].intValue() - 1] = ((F) ((F) right_f).X[i]).X[1].intValue();
                                } else {
                                    m[k1][((F) ((F) ((F) right_f).X[i]).X[0]).X[2].intValue() - 1] = 1;
                                }
                            }
                        } else {
                            masPol[k1] = ring.numberONE();
                            //m[k1][0] = ((F) ((F) right_f).X[i]).X[1].intValue();
                            for (int h = 0; h < maxD; h++) {
                                m[k1][h] = 0;
                            }
                        }
                        if (!fl) {
                            k1++;
                            break;
                        } else {
                            break;
                        }
                    }
                    default: {
                        masPol[k1] = ring.numberONE();
                        // m[k1][0] = ((F) ((F) right_f).X[i]).X[1].intValue();
                        for (int h = 0; h < maxD; h++) {
                            m[k1][h] = 0;
                        }
                        k1++;
                    }
                }
            } else {
                if (((F) right_f).X[i] instanceof Polynom) {
                    masPol[k1] = ((F) right_f).X[i];
                    for (int h = 0; h < maxD; h++) {
                        m[k1][h] = 0;
                    }
                    k1++;
                } else {
                    if (((F) right_f).X[i] instanceof Fname) {
                        masPol[k1] = ring.numberONE();
                        //m[k1][0] = 1;
                        for (int h = 0; h < maxD; h++) {
                            m[k1][h] = 0;
                        }
                        k1++;
                    } else {
                        masPol[k1] = ((F) right_f).X[i];
                        for (int h = 0; h < maxD; h++) {
                            m[k1][h] = 0;
                        }
                        k1++;
                    }
                }
            }
        }
        this.m = m;
        this.masPol = masPol;
    }

    /**
     * поиск в произведении функций d'
     *
     * @param f
     * @param ring
     * @return
     */
    public boolean findD(F f, Ring ring, boolean fl) {
        for (int i = 0; i < f.X.length; i++) {
            if (f.X[i] instanceof F) {
                if (((F) f.X[i]).name == F.d) {
                    fl = true;
                } else {
                    if (((F) f.X[i]).name == F.intPOW) {
                        fl = findD(((F) f.X[i]), ring, fl);
                    }
                }
            }
        }
        return fl;
    }

    /**
     * Метод преобразования по Лапласу входного дифференциального уравнения
     *
     * @param f - дифференциальное уравнение
     * @return - возвращает матрицу преобразования
     */
    public void DifEquationToMatrix(F f, int[][] m, Element[] masPol, Ring ring) {
        int k = 0;//колличество слагаемых во входном выражении
        int k2 = 0;//колличество проходов в исходном дереве
        Element right_f = //new CanonicForms(
                ring.CForm.ExpandForYourChoise(((F) f.X[0]), 1, 1, 1, -1, 1);
        //случай когда -1(diffEquation)
        if (((F) right_f).name == F.MULTIPLY) {
            if (((F) right_f).X[0].numbElementType() < Ring.MASK_H) {
                Element[] el = new Element[((F) right_f).X.length - 1];
                System.arraycopy(((F) right_f).X, 1, el, 0, ((F) right_f).X.length - 1);
                if (el.length == 1) {
                    DifEquationToMatrix1(el[0], m, masPol, ring);
                } else {
                    DifEquationToMatrix1(new F(F.MULTIPLY, el), m, masPol, ring);
                }
                for (int i = 0; i < this.masPol.length; i++) {
                    this.masPol[i] = this.masPol[i].multiply(((F) right_f).X[0], ring);
                }
                return;
            }
        }
        boolean fl = false;
        if (((F) right_f).name == F.d) {
            int maxD = maxDerivative(((F) right_f), ring);
            masPol = new Element[1];
            masPol[0] = ring.numberONE();
            m = new int[1][maxD];
            if (((F) right_f).X.length == 2) {
                m[0][0] = 1;
            } else {
                m[0][((F) right_f).X[2].intValue() - 1] = 1;
            }
            this.m = m;
            this.masPol = masPol;
            return;
        } else if (((F) right_f).name == F.intPOW) {
            fl = true;
            k = 1;
            k2 = 1;
        } else {
            if (((F) right_f).name == F.MULTIPLY) {
                fl = true;
                k = 1;
                k2 = ((F) right_f).X.length;
            } else {
                k = ((F) right_f).X.length;
                k2 = ((F) right_f).X.length;
            }
        }
        int maxD = maxDerivative(((F) right_f), ring);
        masPol = new Element[k];
        m = new int[k][maxD];
        int k1 = 0;
        for (int i = 0; i < k2; i++) {
            if (((F) right_f).X[i] instanceof F) {
                switch (((F) ((F) right_f).X[i]).name) {
                    case F.MULTIPLY: {
                        F ff = ((F) ((F) right_f).X[i]);
                        if (!findD(ff, ring, false)) {
                            masPol[k1] = ff;
                            k1++;
                            break;
                        } else {
                            int k3 = 0;
                            Element[] ell = new Element[]{};
                            boolean fl1 = false;
                            if (ff.X.length > 2) {
                                fl1 = true;
                                ell = new Element[ff.X.length - 1];
                            }
                            for (int j = 0; j < ff.X.length; j++) {
                                if (ff.X[j] instanceof F) {
                                    switch (((F) ff.X[j]).name) {
                                        case F.d: {
                                            if (((F) ff.X[j]).X.length == 2) {
                                                m[k1][0] = 1;
                                            } else {
                                                m[k1][((F) ff.X[j]).X[2].intValue() - 1] = 1;
                                            }
                                            break;
                                        }
                                        case F.intPOW: {
                                            if (((F) ((F) ff.X[j]).X[0]).X.length == 2) {
                                                m[k1][0] = ((F) ff.X[j]).X[1].intValue();
                                            } else {
                                                m[k1][((F) ((F) ff.X[j]).X[0]).X[2].intValue() - 1] = ((F) ff.X[j]).X[1].intValue();
                                            }
                                            break;
                                        }
                                        default: {
                                            if (fl1) {
                                                ell[k3] = ff.X[j];
                                                k3++;
                                            } else {
                                                masPol[k1] = ff.X[j];
                                            }
                                        }

                                    }
                                } else {
                                    if (ff.X[j] instanceof Polynom) {
                                        if (fl1) {
                                            ell[k3] = ff.X[j];
                                            k3++;
                                        } else {
                                            masPol[k1] = ff.X[j];
                                        }
                                    } else {
                                        if (ff.X[j] instanceof Fname) {
                                            if (fl1) {
                                                ell[k3] = ff.X[j];
                                                k3++;
                                            } else {
                                                masPol[k1] = ff.X[j];
                                            }
                                        } else {
                                            if (fl1) {
                                                ell[k3] = ff.X[j];
                                                k3++;
                                            } else {
                                                masPol[k1] = ff.X[j];
                                            }
                                        }
                                    }
                                }
                            }
                            if (fl1) {
                                int l = 0;
                                for (int ii = 0; ii < ell.length; ii++) {
                                    if (ell[ii] != null) {
                                        l++;
                                    }
                                }
                                Element[] ell1 = new Element[l];
                                System.arraycopy(ell, 0, ell1, 0, l);
                                masPol[k1] = new F(F.MULTIPLY, ell1);
                            }
                        }
                        k1++;
                        break;
                    }
                    case F.d: {
                        masPol[k1] = ring.numberONE();
                        if (((F) ((F) right_f).X[i]).X.length == 2) {
                            if (fl) {
                                m[k1][0] = ((F) right_f).X[1].intValue();
                            } else {
                                m[k1][0] = 1;
                            }
                        } else {
                            if (fl) {
                                m[k1][((F) ((F) right_f).X[i]).X[2].intValue() - 1] = ((F) right_f).X[1].intValue();
                            } else {
                                m[k1][((F) ((F) right_f).X[i]).X[2].intValue() - 1] = 1;
                            }
                        }
                        k1++;
                        break;
                    }
                    case F.intPOW: {
                        if (((F) ((F) right_f).X[i]).X[0] instanceof F) {
                            masPol[k1] = ring.numberONE();
                            if (((F) ((F) ((F) right_f).X[i]).X[0]).X.length == 2) {
                                if (fl) {
                                    m[k1][0] = ((F) ((F) right_f).X[i]).X[1].intValue();
                                } else {
                                    m[k1][0] = 1;
                                }
                            } else {
                                if (fl) {
                                    m[k1][((F) ((F) ((F) right_f).X[i]).X[0]).X[2].intValue() - 1] = ((F) ((F) right_f).X[i]).X[1].intValue();
                                } else {
                                    m[k1][((F) ((F) ((F) right_f).X[i]).X[0]).X[2].intValue() - 1] = 1;
                                }
                            }
                        } else {
                            masPol[k1] = ring.numberONE();
                            //m[k1][0] = ((F) ((F) right_f).X[i]).X[1].intValue();
                            for (int h = 0; h < maxD; h++) {
                                m[k1][h] = 0;
                            }
                        }
                        if (!fl) {
                            k1++;
                            break;
                        } else {
                            break;
                        }
                    }
                    case F.DIVIDE: {
                        F ff = ((F) ((F) right_f).X[i]);// a/b
                        if (ff.X[0] instanceof F) {
                            if (((F) ff.X[0]).name == F.d) {
                                masPol[k1] = ring.numberONE();
                                if (((F) ((F) right_f).X[i]).X.length == 2) {
                                    if (fl) {
                                        m[k1][0] = ((F) right_f).X[1].intValue();
                                    } else {
                                        m[k1][0] = 1;
                                    }
                                } else {
                                    if (fl) {
                                        m[k1][((F) ((F) right_f).X[i]).X[2].intValue() - 1] = ((F) right_f).X[1].intValue();
                                    } else {
                                        m[k1][((F) ((F) right_f).X[i]).X[2].intValue() - 1] = 1;
                                    }
                                }
                                masPol[k1] = new F(F.DIVIDE, new Element[]{ring.numberONE(), ff.X[1]});
                                k1++;
                            } else {
                                masPol[k1] = ff;
                                for (int h = 0; h < maxD; h++) {
                                    m[k1][h] = 0;
                                }
                                k1++;
                            }
                        } else {
                            masPol[k1] = ff;
                            for (int h = 0; h < maxD; h++) {
                                m[k1][h] = 0;
                            }
                            k1++;
                        }
                        break;
                    }
                    default: {
                        masPol[k1] = ring.numberONE();
                        // m[k1][0] = ((F) ((F) right_f).X[i]).X[1].intValue();
                        for (int h = 0; h < maxD; h++) {
                            m[k1][h] = 0;
                        }
                        k1++;
                    }
                }
            } else {
                if (((F) right_f).X[i] instanceof Polynom) {
                    masPol[k1] = ((F) right_f).X[i];
                    for (int h = 0; h < maxD; h++) {
                        m[k1][h] = 0;
                    }
                    k1++;
                } else {
                    if (((F) right_f).X[i] instanceof Fname) {
                        masPol[k1] = ring.numberONE();
                        //m[k1][0] = 1;
                        for (int h = 0; h < maxD; h++) {
                            m[k1][h] = 0;
                        }
                        k1++;
                    } else {
                        masPol[k1] = ((F) right_f).X[i];
                        for (int h = 0; h < maxD; h++) {
                            m[k1][h] = 0;
                        }
                        k1++;
                    }
                }
            }
        }
        this.m = m;
        this.masPol = masPol;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Ring ring = new Ring("R64[x]");
//        ring.setDefaulRing();
//        DiffEquation df = new DiffEquation();
//           // F dif = new F("\\systLDE(2x^2 y\\d(y,t)+y^2-2=0)", ring);
//     // F dif = new F("\\systLDE(x\\d(y,t)+2 y\\d(y,t)-1=0)", ring);
//     // F dif = new F("\\systLDE(\\sin(x)y+\\cos(x)\\d(y,t)-2=0)", ring);
//     //F dif = new F("\\systLDE(x\\d(y,t)^2+\\d(y,t)+3=0)", ring);
//      F dif = new F("\\systLDE(x\\d(y,t,2)^4+1+y=0)", ring);
//      int[][] m = null;
//      Element[] el = null;
//      df.DifEquationToMatrix(dif, m, el, ring);

        //////////////////////////////////////////////////////

        Ring ring = new Ring("R64[x,y]");
        ring.setDefaultRing();
        DiffEquation df = new DiffEquation();
        //F dif = new F("\\systLDE(2x^2 y\\d(y,t)+y^2-2=0)", ring);
        //F dif = new F("\\systLDE(x\\d(y,t)+2 y\\d(y,t)-1=0)", ring);
        F dif = new F("\\systLDE(x/\\sin(y)\\d(y,t,1)+1/y+\\sin(y)/(x+y)\\d(y,t,1)+\\d(y,t,1)=0)", ring);
        //F dif = new F("\\systLDE(x\\d(y,t)^2+\\d(y,t)+3=0)", ring);
        //F dif = new F("\\systLDE(x\\d(y,t,2)^4+1+y=0)", ring);
        //  F dif = new F("\\systLDE(x\\d(y,t,3)+y\\d(y,t,5)+xy\\d(y,t,4)=0)", ring);
        int[][] m = null;
        Element[] el = null;
        df.DifEquationToMatrix(dif, m, el, ring);
    }
}
