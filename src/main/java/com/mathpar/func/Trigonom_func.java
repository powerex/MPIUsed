/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import com.mathpar.number.Ring;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 *
 * @author Чернышова Алёна
 */
public class Trigonom_func {
    Ring ring;
    Trigonom_func(Ring rng){ring = rng;}
    public static double xu[] = new double[10];
    public static Polynom x;
    public static int fx;
    public static NumberZ a, b, c, d, e;

    F Resh_Sin(double a) throws Exception {F x;
        if (a == 0) {
            x = new F("n *\\pi ",ring);
        } else if (a == 1) {
            x = (F)(new F("\\pi/2",ring)).add(new F("2 n \\pi",ring),ring);
        } else {
            //x = Math.pow(-1, n) * Math.asin(a) + Math.PI * n;
            x = (F)(new F("n \\pi",ring)).add(new Polynom((new NumberR64(Math.asin(a)))
                    .multiply(new F(F.POW,new Element[]{NumberZ.MINUS_ONE, (new F("n",ring))} ), ring)), ring);
        }
        return x;
    }

      F  Resh_Tng(double a) throws Exception {
           return (F)(new F("n *\\pi ",ring)).add( new NumberR64(Math.atan(a)), ring);
    }

     F Resh_Cos(double a) throws Exception {F x;
        if (a == 0) {
            x = (F)(new F("n \\pi",ring)).add(new F("2 \\pi",ring), ring);
        } else if (a == 1) {
            x = (new F("2 n \\pi",ring));
        } else if (a == -1) {
            x = (F)(new F(" \\pi ",ring)).add(new F("2 \\pi n",ring), ring);
        } else if (a > 0) {
            x = (F)(new F("2 n \\pi",ring)).add(new Polynom((new NumberR64(Math.acos(a)))), ring);
        } else {
            x = (F)(new F("2 n \\pi",ring)).add(new Polynom((new NumberR64(-Math.acos(a)))), ring);
        }
        return x;
    }

     F Resh_CoTng(double a) throws Exception {
        return new F(F.DIVIDE, new Element[]
        {NumberR64.ONE,
             ((new F("n \\pi",ring)).add(new Polynom((new NumberR64(Math.atan(a)))), ring))});
        }


    static void Step_1(int a, int b) {  xu[0] = (-(double)b) / a;}

    static void Step_2(int a, int b, int c) {
        double D = b * b - 4 * a * c;

        if (D == 0) {
            xu[0] = -b / (2 * a);
        } else if (D > 0) {
            xu[0] = (-b + Math.sqrt(D)) / (2 * a);
            xu[1] = (-b - Math.sqrt(D)) / (2 * a);
        } else {
        double Re = -b / 2 / a;
        double Im = Math.abs(Math.sqrt(-D) / (2 * a));
           Complex  pasha =new Complex(Re,Im);
            System.out.println(Re+"    pasha"+pasha);
        }
        return;

    }

    static void Step_3_Kardano(double a, double b, double c, double d) {
        double p = (3 * a * c - b * b) / (3 * a * a);
        double q = (2 * Math.pow(b, 3) - 9 * a * b * c + 27 * a * a * d) / (27 * Math.pow(a, 3));
        double S = (4 * (3 * a * c - b * b) * (3 * a * c - b * b) * (3 * a * c - b * b) + (2 * Math.pow(b, 3) - 9 * a * b * c + 27 * a * a * d) * (2 * Math.pow(b, 3) - 9 * a * b * c + 27 * a * a * d)) / (2916 * Math.pow(a, 6));
        double y1 = 0;
        double y2 = 0;
        double arg = 0;
        if (S < 0) {
            if (q < 0) {
                arg = Math.atan(-2 * Math.sqrt(-S) / q);
            }
            if (q > 0) {
                arg = Math.atan(-2 * Math.sqrt(-S) / q) + Math.PI;
            }
            if (q == 0) {
                arg = Math.PI / 2;
            }
            xu[0] = 2 * Math.sqrt(-p / 3) * Math.cos(arg / 3) - b / a / 3;
            xu[1] = 2 * Math.sqrt(-p / 3) * Math.cos((arg + 2 * Math.PI) / 3) - b / a / 3;
            if (q == 0) {
                xu[2] = -b / a / 3;
            } else {
                xu[2] = 2 * Math.sqrt(-p / 3) * Math.cos((arg + 4 * Math.PI) / 3) - b / a / 3;
            }
        }
        if (S > 0) {
            if (-q / 2 + Math.sqrt(S) > 0) {
                y1 = Math.exp(Math.log(Math.abs(-q / 2 + Math.sqrt(S))) / 3);
            }
            if (-q / 2 + Math.sqrt(S) < 0) {
                y1 = -Math.exp(Math.log(Math.abs(-q / 2 + Math.sqrt(S))) / 3);
            }
            if (-q / 2 + Math.sqrt(S) == 0) {
                y1 = 0;
            }
            if (-q / 2 - Math.sqrt(S) > 0) {
                y2 = Math.exp(Math.log(Math.abs(-q / 2 - Math.sqrt(S))) / 3);
            }
            if (-q / 2 - Math.sqrt(S) < 0) {
                y2 = -Math.exp(Math.log(Math.abs(-q / 2 - Math.sqrt(S))) / 3);
            }
            if (-q / 2 - Math.sqrt(S) == 0) {
                y2 = 0;
            }
            xu[0] = y1 + y2 - b / a / 3;
            /*double Re = -(y1 + y2) / 2 - b / a / 3;
            double Im = (y1 - y2) * Math.sqrt(3) / 2;*/
        }
        if (S == 0) {
            if (q < 0) {
                y1 = Math.exp(Math.log(Math.abs(-q / 2)) / 3);
            }
            if (q > 0) {
                y1 = -Math.exp(Math.log(Math.abs(-q / 2)) / 3);
            }
            if (q == 0) {
                y1 = 0;
            }
            xu[0] = 2 * y1 - b / a / 3;
            xu[1] = -y1 - b / a / 3;
            //xu[2] = -y1 - b / a / 3;
        }
        return;
    }

    static void Step_4(int a, int b, int c, int d, int e) {
        double p = (8 * a * c - 3 * b * b) / (8 * a * a);
        double q = (8 * a * a * d + Math.pow(b, 3) - 4 * a * b * c) / (8 * Math.pow(a, 3));
        double r = (16 * a * b * b * c - 64 * a * a * b * d - 3 * Math.pow(b, 4) + 256 * Math.pow(a, 3) * e) / (256 * Math.pow(a, 4));
        if (q != 0) {
            Step_3_Kardano(1, p, ((p * p - 4 * r) / 4), ((q * q) / 8));
            xu[0] = Math.pow(2 * xu[0], 1 / 2) + Math.pow(2 * xu[0] - 4 * (p / 2 + xu[0] + q / (2 * Math.pow(2 * xu[0], 1 / 2))), 1 / 2) / 2;
            xu[1] = Math.pow(2 * xu[0], 1 / 2) - Math.pow(2 * xu[0] - 4 * (p / 2 + xu[0] + q / (2 * Math.pow(2 * xu[0], 1 / 2))), 1 / 2) / 2;
            xu[2] = -Math.pow(2 * xu[0], 1 / 2) - Math.pow(2 * xu[0] - 4 * (p / 2 + xu[0] + q / (2 * Math.pow(2 * xu[0], 1 / 2))), 1 / 2) / 2;
            xu[3] = -Math.pow(2 * xu[0], 1 / 2) + Math.pow(2 * xu[0] - 4 * (p / 2 + xu[0] + q / (2 * Math.pow(2 * xu[0], 1 / 2))), 1 / 2) / 2;
        } else {
            xu[0] = Math.pow((-p - Math.pow(p * p - 4 * r, 1 / 2)) / 2, 1 / 2);
            xu[1] = -Math.pow((-p - Math.pow(p * p - 4 * r, 1 / 2)) / 2, 1 / 2);
            xu[2] = Math.pow((-p + Math.pow(p * p - 4 * r, 1 / 2)) / 2, 1 / 2);
            xu[3] = -Math.pow((-p + Math.pow(p * p - 4 * r, 1 / 2)) / 2, 1 / 2);
        }
        return;
    }

     Element[] Begin_prog(Element f) throws Exception {
          Element[] resh=null;
        int a1 = a.intValue();
        int b1 = b.intValue();
        int c1 = c.intValue();
        int d1 = d.intValue();
        int e1 = e.intValue();
        if ((a1 == 0) && (b1 == 0) && (c1 == 0)) {
            Step_1(d1, e1);
            if (fx == F.SIN) {
                for (int i = 0; i < 1; i++) {
                    F pp = Resh_Sin(xu[i]);
                    resh = new Element[]{pp};
                    //System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            } else if (fx == F.COS) {
                for (int i = 0; i < 1; i++) {
                    F pp = Resh_Cos(xu[i]);
                    System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            } else if (fx == F.TG) {
                for (int i = 0; i < 1; i++) {
                    F pp = Resh_Tng(xu[i]);
                    System.out.println("x[" + (i + 1) + "] = " + pp);


                }
            } else {
                for (int i = 0; i < 1; i++) {
                    F pp = Resh_CoTng(xu[i]);
                    System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            }
        } else if ((a1 == 0) && (b1 == 0)) {
            Step_2(c1, d1, e1);
            if (fx == F.SIN) {
                for (int i = 0; i < 2; i++) {
                    F pp = Resh_Sin(xu[i]);
                    System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            } else if (fx == F.COS) {
                for (int i = 0; i < 2; i++) {
                    F pp = Resh_Cos(xu[i]);
                    System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            } else if (fx == F.TG) {
                for (int i = 0; i < 2; i++) {
                    F pp = Resh_Tng(xu[i]);
                    System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    F pp = Resh_CoTng(xu[i]);
                    System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            }
        } else if (a1 == 0) {
            Step_3_Kardano(b1, c1, d1, e1);
            //Step_3_PReal(b1, c1, d1, e1);
            if (fx == F.SIN) {
                for (int i = 0; i < 3; i++) {
                    F pp = Resh_Sin(xu[i]);
                    System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            } else if (fx == F.COS) {
                for (int i = 0; i < 3; i++) {
                    F pp = Resh_Cos(xu[i]);
                    System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            } else if (fx == F.TG) {
                for (int i = 0; i < 3; i++) {
                    F pp = Resh_Tng(xu[i]);
                    System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    F pp = Resh_CoTng(xu[i]);
                    System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            }
        } else {
            Step_4(a1, b1, c1, d1, e1);
            if (fx == F.SIN) {
                for (int i = 0; i < 4; i++) {
                    F pp = Resh_Sin(xu[i]);
                    System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            } else if (fx == F.COS) {
                for (int i = 0; i < 4; i++) {
                    F pp = Resh_Cos(xu[i]);
                    System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            } else if (fx == F.TG) {
                for (int i = 0; i < 4; i++) {
                    F pp = Resh_Tng(xu[i]);
                    System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    F pp = Resh_CoTng(xu[i]);

                    System.out.println("x[" + (i + 1) + "] = " + pp);
                }
            }
        }
        return resh;
    }
}
