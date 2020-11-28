/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.mathpar.number.math.MathContext;
import com.mathpar.number.math.RoundingMode;
import com.mathpar.number.*;

/**
 *
 * @author Максим Киселев
 */
/**Верификатор точности функций действительных переменных
 * Процедура для тестирования точности знаков после запятой у транцидентных функций
при различных действительных переменных(учитывая критические точки).
 *  */
public class TestFunc {

    private static class j<T> {

        public j() {
        }
    }
    MathContext mc;
    int accuracy;
    static Ring r;
    MathContext mc_old;

    public TestFunc() {
    }

    public TestFunc(Ring r, MathContext mc) {
        this.r = r;
        accuracy = r.getAccuracy();
        this.mc = mc;
    }

    public TestFunc(Ring r) {
        this.r = r;
        accuracy = r.getAccuracy();
        mc = new MathContext(accuracy, RoundingMode.HALF_EVEN);
    }

    /**
     * Add the number of exact decimal places
     * @param numbOfDecFig --extra number of exact decimal places
     */
    public void addAccuracy(int numbOfDecFigures) {
        accuracy += numbOfDecFigures;
        r.setAccuracy(accuracy);
        mc_old = mc;
        mc = new MathContext(accuracy, RoundingMode.HALF_EVEN);
    }

    public void subAccuracy(int numbOfDecFigures) {
        accuracy -= numbOfDecFigures;
         r.setAccuracy(accuracy);
        mc = mc_old;
    }

    /**
     * Set the number of exact decimal places in the number of NumberR type.
     *  Each operation with NumberR will be perform with this accurecy
     * @param numbOfDecFig -- number of exact decimal places
     */
    public void setAccuracy(int numbOfDecFigures) {
        accuracy = numbOfDecFigures;
         r.setAccuracy(accuracy);
        mc = new MathContext(numbOfDecFigures, RoundingMode.HALF_EVEN);
    }

    /**
     *Вычислим значения соответствующей функции d1 с заданной точностью и d2 с точностью увеличенной на 10.
    Затем находим разницу rez = |d1-d2|. Число rez сравниваем с каждым элементом массива array,
    как только rez станет больше какого-нибудь 10^{-n}, то мы выводим соответствующий номер элемента массива,
    который и будет равен количеству совпавших цифр, т. е. количество нулей.
     */
    public static int Test(int n, NumberR x, int acc) {

        r = new Ring("R[x]");
         r.setAccuracy(acc);
        System.out.println("acc=="+acc);
        FuncNumberR e = new FuncNumberR(r);
        NumberR k = (e.pi());
        NumberR rez = new NumberR(1);
        NumberR d = new NumberR();
        NumberR d1 = new NumberR();
     //   Element eps = (new NumberR(1)).divide(new NumberR(r.ACCURACY), r);
        Element array[] = new NumberR[100];
        for (int i = 1; i < 100; i += 1) {
            array[i - 1] = (new NumberR(0.1)).pow(i, r);

        }
        String name = "";
        switch (n) {
            case F.LN: {
                name = "Ln";
                d = (NumberR)e.ln(x);
                e.setAccuracy(r.getAccuracy()+10);
               // e.accuracy = r.ACCURACY + 10;
                d1 = (NumberR)e.ln(x);
                if (d == null) {
                    break;
                }
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.EXP: {
                name = "Exp";
                d = e.exp(x);
                e.setAccuracy(r.getAccuracy()+10);
                d1 = e.exp(x);

                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.SQRT: {
                name = "SQRT";
                d = (NumberR)e.sqrt(x);
            try {
                r.setAccuracy(r.getAccuracy()+10);
            } catch (Exception ex) {
                Logger.getLogger(TestFunc.class.getName()).log(Level.SEVERE, null, ex);
            }
            e = new FuncNumberR(r);
              //  e.accuracy = r.ACCURACY + 10;
                d1 = (NumberR)e.sqrt(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }

            case F.SIN: {
                name = "Sin";
                d = (NumberR)e.sin(x);
                e.setAccuracy(r.getAccuracy()+10);
                d1 = (NumberR)e.sin(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.COS: {
                name = "Cos";
                d = (NumberR)e.cos(x);
//                e.accuracy = r.ACCURACY + 10;
//                d1 = e.cos(x);
            try {
                r.setAccuracy(r.getAccuracy()+10);
            } catch (Exception ex) {
                Logger.getLogger(TestFunc.class.getName()).log(Level.SEVERE, null, ex);
            }

            e = new FuncNumberR(r);
                System.out.println("r ACC=="+r.getAccuracy());
            d1 = (NumberR)e.cos(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.TG: {
                name = "Tg";
                d = (NumberR)e.tan(x);
                e.setAccuracy(r.getAccuracy()+10);
                d1 = (NumberR)e.tan(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.CTG: {
                name = "Ctg";
                d = (NumberR)e.ctg(x);
                e.setAccuracy(r.getAccuracy()+10);
                d1 = (NumberR)e.ctg(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.ARCSIN: {
                name = "ArcSin";
                d = (NumberR)e.arcsn(x);
                if (d == null) {
                    System.out.println("arcsn(" + x + ") = " + d);

                    break;
                }

                e.setAccuracy(r.getAccuracy()+10);
                d1 = (NumberR)e.arcsn(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.ARCCOS: {
                name = "ArcCos";
                d = (NumberR)e.arccs(x);
                if (d == null) {
                    System.out.println("arccos(" + x + ") = " + d);

                    break;
                }
                e.setAccuracy(r.getAccuracy()+10);
                d1 = (NumberR)e.arccs(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.ARCTG: {
                name = "ArcTg";
                d = e.arctn(x);
                e.setAccuracy(r.getAccuracy()+10);
                d1 = e.arctn(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.ARCCTG: {
                name = "ArcCtg";
                d = e.arcctn(x);
                e.setAccuracy(r.getAccuracy()+10);
                d1 = e.arcctn(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.SH: {
                name = "Sh";
                d = e.sh(x);
                e.setAccuracy(r.getAccuracy()+10);
                d1 = e.sh(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.CH: {
                name = "Ch";
                d = e.ch(x);
                e.setAccuracy(r.getAccuracy()+10);
                d1 = e.ch(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.TH: {
                name = "Th";
                d = e.th(x);
                e.setAccuracy(r.getAccuracy()+10);
                d1 = e.th(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.CTH: {
                name = "Cth";
                d = (NumberR)e.cth(x);
                if (d == null) {
                    System.out.println("cth(" + x + ") = " + d);
                    break;
                }
                e.setAccuracy(r.getAccuracy()+10);
                d1 = (NumberR)e.cth(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.ARCSH: {
                name = "ArcSh";
                d = e.arsh(x);
                e.setAccuracy(r.getAccuracy()+10);
                d1 = e.arsh(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.ARCCH: {
                name = "ArcCh";
                d = (NumberR)e.arch(x);
                if (d == null) {
                    System.out.println("ArcCH(" + x + ") = " + d);
                    break;
                }
                e.setAccuracy(r.getAccuracy()+10);
                d1 = (NumberR)e.arch(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.ARCTGH: {
                name = "ArcTgh";
                d = (NumberR)e.arth(x);
                if (d == null) {
                    System.out.println("ARTGH(" + x + ") = " + d);
                    break;
                }
                e.setAccuracy(r.getAccuracy()+10);
                d1 = (NumberR)e.arth(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }
            case F.ARCCTGH: {
                name = "ArcCtgh";
                d = (NumberR)e.arcth(x);
                if (d == null) {
                    System.out.println("ARCCTGH(" + x + ") = " + d);
                    break;
                }
                e.setAccuracy(r.getAccuracy()+10);
                d1 = (NumberR)e.arcth(x);
                rez = (NumberR) d1.subtract(d, r).abs(r);

                break;
            }

        }


        int i = acc;
        for (i = acc; i > 0; i--) {
            if (rez.compareTo(array[i - 1], r) == -1) {
                break;
            }
        }
        System.out.println(name + "(" + x + ") = " + d);
        System.out.println("Точность " + i + " знаков после запятой");

        return i;
    }

    /**Метод определяющий одно значение функции и одну точность*/
    public static void vivod1(int n, NumberR x, int acc) {
        r = new Ring("R[x]");
        FuncNumberR e = new FuncNumberR(r);



        Test(n, x, acc);
    }

    /**Метод определяющий одно значение функции и несколько точностей*/
    public static void vivod2(int n, NumberR x, int[] acc) {
        r = new Ring("R[x]");
        FuncNumberR e = new FuncNumberR(r);

        for (int i = 0; i < acc.length; i++) {
            Test(n, x, acc[i]);
        }

    }

    /**Метод определяющий несколько значений функции и одну точность*/
    public static void vivod3(int n, NumberR[] x, int acc) {
        r = new Ring("R[x]");
        FuncNumberR e = new FuncNumberR(r);
        NumberR stolb[] = new NumberR[100];
        for (int i = 0; i < stolb.length; i++) {
            Test(n, x[i], acc);
        }
    }

    /**Метод определяющий несколько значений функции и несколько точностей*/
    public static void vivod4(int n, NumberR[] x, int[] acc) {
        r = new Ring("R[x]");
        FuncNumberR e = new FuncNumberR(r);
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < acc.length; j++) {
                Test(n, x[i], acc[j]);
            }
        }
    }

    public static void main(String[] args) {
     Ring   ring = new Ring("Q[x]"); 
        Fname h= (new Fname("f")); h.setFuncX(); Element g= h.D(ring);
        System.out.println(h+"  "+g); 
        Element e =  new Fname("\\pi").multiply(new Fraction(2, 8), ring);
        System.out.println(e); 
        Element ee=e.cos(ring);
        System.out.println(ee);
        System.out.println(ee.EXPAND(ring));
     //   FuncNumberR e = new FuncNumberR(r);
    //    NumberR k = (e.pi());
        // vivod1(F.COS, new NumberR(25), 15);
        // vivod2(F.COS, new NumberR(3), new int[]{5, 15, 30, 90});
        //vivod3(F.COS, new NumberR[]{k,new NumberR(1)},  20);
   //     vivod4(F.COS, new NumberR[]{k, new NumberR(2)}, new int[]{5, 15, 30, 90});
    }
}