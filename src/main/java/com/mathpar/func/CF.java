/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import com.mathpar.number.*;
import com.mathpar.polynom.*;

public class CF extends Element {

   static Polynom two = new Polynom(new int[0], new Element[] {new NumberR64(2.0)});
    static Polynom four = new Polynom(new int[0], new Element[] {new NumberR64(4.0)});
   /**
     * поле имени функции (на данный момент построения класса всегда = 0)
     */
    public int name;
    /**
     * поле имен переменных
     */
    public Fname[] fname;
    /**
     * поле аргументов функции
     */
    public Element[] aX;

    public Element toNumber(Element oneOfNewType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public static Element ONE = NumberR64.ONE;
    public static Element MINUS_ONE = NumberR64.MINUS_ONE;
    public static Element ZERO = NumberR64.ZERO;

    public CF() {
    }

    /**
     * конструктор функции от массива аргументов
     * @param массив Element[], содержащий аргументы функции
     */
    public CF(Element[] x) {
        this.name = 0;
        this.fname = new Fname[]{};
        this.aX = x;
    }

    /**
     * конструктор функкции от массива аргументов и имени переменного
     * @param x - массив аргументов
     * @param s - строка с именем переменного
     */
    public CF(Element[] x, String s) {
        this.name = 0;
        this.fname = new Fname[]{new Fname(s)};
        this.aX = x;
    }

    /**
     * конструктор функции от одного аргумента
     * @param x - аргумент функции
     */
    public CF(Element x) {
        this.name = 0;
        this.fname = new Fname[]{};
        Element[] a = new Element[]{x};
        this.aX = a;
    }

    /**
     * конструктор функкции от аргумента и имени переменного
     * @param x - аргумент функции
     * @param n - строка с именем переменного
     */
    public CF(Element x, String n) {
        this.name = 0;
        this.fname = new Fname[]{new Fname(n)};
        Element[] a = new Element[]{x};
        this.aX = a;
    }

    /**
     * конструктор функции от массива функций и имени переменного
     * @param c - массив функций CF
     * @param s - строка с именем переменного
     */
    public CF(CF[] c, String s) {
        this.name = 0;
        Fname[] t = new Fname[]{new Fname(s)};
        this.fname = t;
        this.aX = new Element[c.length];
        for (int i = 0; i < c.length; i++) {
            this.aX[i] = c[i];
        }
    }

    /**
     * конструктор функции от элемента и 2 имен переменных
     * @param x - аргумент функции
     * @param b - строка с именем переменного
     * @param c - строка с именем переменного
     */
    public CF(Element x, String b, String c) {
        this.name = 0;
        Element[] a = new Element[]{x};
        this.aX = a;
        Fname[] d = new Fname[]{new Fname(b), new Fname(c)};
        this.fname = d;
    }

    /**
     * метод, реализующий построение фенкции CF
     * @return строковую запись функции
     */
    public String toString() {
        CF all = new CF(new Element[]{NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY});
        CF pust = new CF(new Element[]{});
        StringBuilder res = new StringBuilder();
        String temp = "";

        if (aX.length == 0) {
            res.append("emptySet");
            return res.toString();
        }//если массив пустой
        if (aX.length == 2) {//если 2 решения
            if ((aX[0] instanceof NumberR) && (aX[1] instanceof NumberR)) {
                if ((aX[0].equals(NumberR.NEGATIVE_INFINITY)) && (aX[1].equals(NumberR.POSITIVE_INFINITY))) {
                    res.append("allReals");
                    return res.toString();
                }//если массив из бесконечностей
            }
            if ((aX[0] instanceof NumberR) && (aX[0].equals(NumberR.NEGATIVE_INFINITY))) {
                if ((fname.length == 1) && (fname[0].equals(new Fname("int()")))) {
                    res.append("(" + aX[0].toString() + ", " + aX[1].toString() + ")");
                    return res.toString();
                }
                if ((fname.length == 1) && (fname[0].equals(new Fname("int(]")))) {
                    res.append("(" + aX[0].toString() + ", " + aX[1].toString() + "]");
                    return res.toString();
                }
            }
            if ((aX[1] instanceof NumberR) && (aX[1].equals(NumberR.POSITIVE_INFINITY))) {
                if ((fname.length == 1) && (fname[0].equals(new Fname("int()")))) {
                    res.append("(" + aX[0].toString() + ", " + aX[1].toString() + ")");
                    return res.toString();
                }
                if ((fname.length == 1) && (fname[0].equals(new Fname("int[)")))) {
                    res.append("[" + aX[0].toString() + ", " + aX[1].toString() + ")");
                    return res.toString();
                }
            }
            if ((fname.length == 1) && (fname[0].equals(new Fname("C")))) {//C буква
                res.append("(if " + fname[0] + "=0 : " + aX[0].toString() + ", if " + fname[0] + "!=0 : " + aX[1].toString() + ")");
                return res.toString();
            }
            if ((fname.length == 0) || ((fname.length == 1) && (fname[0].equals(new Fname(""))))) {
                res.append("(D=" + aX[0].toString() + "), " + aX[1].toString());
                return res.toString();
            }
            if ((fname.length == 1) && (fname[0].equals(new Fname("C>0")))) {
                res.append("(if C=0: " + aX[0].toString() + "), (if C>0: " + aX[1].toString() + "), (if C<0: " + aX[0].toString() + ")");
                return res.toString();
            }
            if ((fname.length == 1) && (fname[0].equals(new Fname("C<0")))) {
                res.append("(if C=0: " + aX[0].toString() + "), (if C>0: " + aX[0].toString() + "), (if C<0: " + aX[1].toString() + ")");
                return res.toString();
            }
            if ((fname.length == 1) && (fname[0].equals(new Fname("C>=0")))) {
                res.append("(if C=0: " + aX[1].toString() + "), (if C>0: " + aX[1].toString() + "), (if C<0: " + aX[0].toString() + ")");
                return res.toString();
            }
            if ((fname.length == 1) && (fname[0].equals(new Fname("C<=0")))) {
                res.append("(if C=0: " + aX[1].toString() + "), (if C>0: " + aX[0].toString() + "), (if C<0: " + aX[1].toString() + ")");
                return res.toString();
            }


        }
        if (aX.length == 1) {//если массив одного элемента
            if (fname.length == 0) {//нет букв
                res.append("(" + aX[0].toString() + ")");
                return res.toString();
            }
            if ((fname.length == 1) && (fname[0].equals(new Fname("B")))) {//B буква
                res.append("(if " + fname[0] + "=0 : " + pust + " if " + fname[0] + "!=0 : " + aX[0].toString() + ")");
                return res.toString();
            }
            if ((fname.length == 2) && (fname[0].equals(new Fname("B"))) && (fname[1].equals(new Fname("C")))) {
                res.append("(if " + fname[0] + "=0 : " + "(if " + fname[1] + "=0 : " + all + " , if " + fname[1] + "!=0 : " + pust + " ) " + "if " + fname[0] + "!=0 : " + aX[0].toString() + ")");
                return res.toString();
            }
        }
        if (aX.length == 3) {
            if ((aX[1] instanceof NumberR) && (aX[2] instanceof NumberR)) {
                if ((fname.length == 1) && (fname[0].equals(new Fname("B>0")))) {//B буква
                    res.append("(if (B=0): " + all + "), (if (B!=0): (" + aX[0].toString() + "," + aX[2].toString() + ") )");
                    return res.toString();
                }
                if ((fname.length == 1) && (fname[0].equals(new Fname("B<0")))) {//B буква
                    res.append("(if (B=0): " + pust + "), (if (B!=0): (" + aX[1].toString() + "," + aX[0].toString() + ") )");
                    return res.toString();
                }
                if ((fname.length == 1) && (fname[0].equals(new Fname("B>=0")))) {//B буква
                    res.append("(if (B=0): " + all + "), (if (B!=0): [" + aX[0].toString() + "," + aX[2].toString() + ") )");
                    return res.toString();
                }
                if ((fname.length == 1) && (fname[0].equals(new Fname("B<=0")))) {//B буква
                    res.append("(if (B=0): " + pust + "), (if (B!=0): (" + aX[1].toString() + "," + aX[0].toString() + ") )");
                    return res.toString();
                }

                if ((fname.length == 1) && (fname[0].equals(new Fname("B1>0")))) {//B буква
                    res.append("(if (B=0): " + pust + "), (if (B!=0): (" + aX[0].toString() + "," + aX[2].toString() + ") )");
                    return res.toString();
                }
                if ((fname.length == 1) && (fname[0].equals(new Fname("B1<0")))) {//B буква
                    res.append("(if (B=0): " + all + "), (if (B!=0): (" + aX[1].toString() + "," + aX[0].toString() + ") )");
                    return res.toString();
                }
                if ((fname.length == 1) && (fname[0].equals(new Fname("B1>=0")))) {//B буква
                    res.append("(if (B=0): " + pust + "), (if (B!=0): [" + aX[0].toString() + "," + aX[2].toString() + ") )");
                    return res.toString();
                }
                if ((fname.length == 1) && (fname[0].equals(new Fname("B1<=0")))) {//B буква
                    res.append("(if (B=0): " + all + "), (if (B!=0): (" + aX[1].toString() + "," + aX[0].toString() + ") )");
                    return res.toString();
                }

            }
            if (fname.length == 0) {
                res.append("( D=" + aX[0].toString() + "), " + aX[1].toString() + " , " + aX[2].toString());
                return res.toString();
            }
            if ((fname.length == 1) && (fname[0].equals(new Fname("int()")))) {
                res.append("( if (B<0): (" + aX[1].toString() + ", " + aX[0].toString() + ")" + " )," + "( if (B>0): (" + aX[0].toString() + ", " + aX[2].toString() + ")" + ")");
                return res.toString();
            }
            if ((fname.length == 1) && (fname[0].equals(new Fname("int1()")))) {
                res.append("( if (B<0): (" + aX[0].toString() + ", " + aX[2].toString() + ")" + " ), " + "( if (B>0): (" + aX[1].toString() + ", " + aX[0].toString() + ")" + ")");
                return res.toString();
            }
            if ((fname.length == 1) && (fname[0].equals(new Fname("int[)")))) {
                res.append("( if (B<0): (" + aX[1].toString() + ", " + aX[0].toString() + "]" + " ), " + "( if (B>0): [" + aX[0].toString() + ", " + aX[2].toString() + ")" + " )");
                return res.toString();
            }
            if ((fname.length == 1) && (fname[0].equals(new Fname("int1(]")))) {
                res.append("( if (B<0): [" + aX[0].toString() + ", " + aX[2].toString() + ") ), ( if (B>0): (" + aX[1].toString() + ", " + aX[0].toString() + "] )");
                return res.toString();
            }
            if ((fname.length == 1) && (fname[0].equals(new Fname("all")))) {
                res.append("( if (B=0):" + " (if (C=0): " + aX[0].toString() + ", if (C>0): " + aX[1].toString() + ", if (C<0): " + aX[0].toString() + ") , if (B!=0): " + aX[2].toString() + ") ");
                return res.toString();
            }
            if ((fname.length == 1) && (fname[0].equals(new Fname("all1")))) {
                res.append("( if (B=0):" + " (if (C=0): " + aX[1].toString() + ", if (C>0): " + aX[1].toString() + ", if (C<0): " + aX[0].toString() + ") , if (B!=0): " + aX[2].toString() + ") ");
                return res.toString();
            }


        }
        if (aX.length == 4) {
            if ((fname.length == 1) && (fname[0].equals(new Fname("D")))) {
                res.append("( D=" + aX[0].toString() + "), if ( " + fname[0] + ">0 : " + aX[1].toString() + "," + aX[2].toString() + ") , if " + fname[0] + "<0 :" + pust + "), if " + fname[0] + "=0 : " + aX[3].toString() + ")");
                return res.toString();
            }
        }



        return res.toString();
    }

    /**
     * метод решающий уравнение вида Bx+C=0
     * @param массив Element[] содержащий коэффициенты B, C
     * @return решение в виде числа, либо в общем виде
     */
    public CF Resh_lineal_CF(Element[] Coeffs) {//Bx+C=0
        if (Coeffs.length != 2) {
            System.out.println("неверное количество коэффициентов!для решения Bx+C=0 требуется 2 коэффициента");
            return new CF(new Element[]{});
        }

        CF emptySet = new CF(new Element[]{});
        CF allReals = new CF(new Element[]{NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY});

        Element B = Coeffs[0];
        Element C = Coeffs[1];

        if (((F) B).X[0] instanceof Polynom) {//если B число
            if (((Polynom) ((F) B).X[0]).powers.length == 0) {
                //если С число
                if (((F) C).X[0] instanceof Polynom) {
                    if (((Polynom) ((F) C).X[0]).powers.length == 0) {
                        if (B.isZero(Ring.ringZxyz)) {//B=0
                            if (C.isZero(Ring.ringZxyz)) {//C=0
                                return allReals;
                            } else {//C!=0
                                return emptySet;
                            }
                        } else {//B!=0
                            Polynom s = (Polynom) ((F) C).X[0];
                            Polynom s1 = (Polynom) ((F) B).X[0];
                            Element x = (s.divide(s1, Ring.ringZxyz)).multiply(MINUS_ONE, Ring.ringZxyz);
                            CF xx = new CF(x);
                            return xx;
                        }

                    }
                } //если С буква
                else {
                    if (B.isZero(Ring.ringZxyz)) {//B=0
                        CF[] r = new CF[]{allReals, emptySet};
                        CF rr = new CF(r, "C");
                        return rr;
                    } else {//B!=0
                        Polynom s1 = (Polynom) ((F) B).X[0];
                        Element x = (s1.multiply(MINUS_ONE, Ring.ringZxyz));
                        F x1 = new F(x);
                        Element x2 = C.divide(x1, Ring.ringZxyz);
                        CF xx = new CF(x2);
                        return xx;
                    }
                }
            }
        } else {//B буква
            //если С число
            if (((F) C).X[0] instanceof Polynom) {
                if (((Polynom) ((F) C).X[0]).powers.length == 0) {
                    if (C.isZero(Ring.ringZxyz)) {//C=0
                        CF xx = new CF(ZERO);
                        return xx;
                    } else {//C!=0
                        Polynom s1 = (Polynom) ((F) C).X[0];
                        Element x = (s1.multiply(MINUS_ONE, Ring.ringZxyz));
                        F x1 = new F(x);
                        Element x2 = x1.divide(B, Ring.ringZxyz);
                        CF xx = new CF(x2, "B");
                        return xx;
                    }
                }
            } //если C буква
            else {
                F o = new F(MINUS_ONE);
                Element x = (o.multiply(C, Ring.ringZxyz)).divide(B, Ring.ringZxyz);
                CF xx = new CF(x, "B", "C");
                return xx;
            }
        }

        return this;
    }

    /**
     * метод решающий уравнение вида Ax^2 +Bx+C=0
     * @param Element[] содержащий коэффициенты B, C
     * @return решение в виде числа, либо в общем виде
     */
    public CF Resh_kvadr_CF(Element[] Coeffs, Ring rng) {
        if (Coeffs.length != 3) {
            System.out.println("неверное количество коэффициентов!для решения Ax^2+Bx+C=0 требуется 3 коэффициента");
            return new CF(new Element[]{});
        }

        CF emptySet = new CF(new Element[]{});
        CF allReals = new CF(new Element[]{NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY});

        Element A = Coeffs[0];
        Element B = Coeffs[1];
        Element C = Coeffs[2];

        if (((F) A).X[0] instanceof Polynom) {//если A число
            if (((Polynom) ((F) A).X[0]).powers.length == 0) {
                if (A.isZero(Ring.ringZxyz)) {//a=0
                    CF s = new CF();
                    Element[] r = new Element[]{B, C};
                    return s.Resh_lineal_CF(r);
                } else {//a!=0
                    //если B число
                    if (((F) B).X[0] instanceof Polynom) {
                        if (((Polynom) ((F) B).X[0]).powers.length == 0) {
                            // C число
                            if (((F) C).X[0] instanceof Polynom) {
                                if (((Polynom) ((F) C).X[0]).powers.length == 0) {
                                    Polynom a1 = (Polynom) ((F) A).X[0];
                                    Polynom b1 = (Polynom) ((F) B).X[0];
                                    Polynom c1 = (Polynom) ((F) C).X[0];
                                    Polynom ch = four;
                                    Element D = b1.multiply(b1, Ring.ringZxyz).subtract(ch.multiply(a1, Ring.ringZxyz).multiply(c1, Ring.ringZxyz), Ring.ringZxyz);
                                    if (D.compareTo(rng.numberONE.myZero(Ring.ringZxyz), Ring.ringZxyz) == 1) {//D>0
                                        Element x1 = ((b1.multiply(MINUS_ONE, Ring.ringZxyz)).subtract(D.powTheFirst(1, 2, Ring.ringZxyz), Ring.ringZxyz)).divide((two).multiply(a1, Ring.ringZxyz), Ring.ringZxyz);
                                        Element x2 = ((b1.multiply(MINUS_ONE, Ring.ringZxyz)).add(D.powTheFirst(1, 2, Ring.ringZxyz), Ring.ringZxyz)).divide((two).multiply(a1, Ring.ringZxyz), Ring.ringZxyz);
                                        Element[] r = new Element[]{D, x1, x2};
                                        CF xx = new CF(r);
                                        return xx;
                                    }
                                    if (D.compareTo(rng.numberONE.myZero(Ring.ringZxyz), Ring.ringZxyz) == -1) {//D<0
                                        CF e = new CF(D);
                                        CF[] r = new CF[]{e, emptySet};
                                        CF xx = new CF(r, "");
                                        return xx;
                                    }
                                    if (D.isZero(Ring.ringZxyz)) {//D=0
                                        Element x = (b1.multiply(MINUS_ONE, Ring.ringZxyz)).divide((two).multiply(a1, Ring.ringZxyz), Ring.ringZxyz);
                                        Element[] r = new Element[]{D, x};
                                        CF xx = new CF(r);
                                        return xx;
                                    }

                                }
                            } //C-буква
                            else {
                                Polynom ch1 = four;
                                Polynom a1 = (Polynom) ((F) A).X[0];
                                Polynom b1 = (Polynom) ((F) B).X[0];
                                F c1 = new F(C);
                                Element b2 = b1.multiply(b1, Ring.ringZxyz);
                                Element ca = ch1.multiply(a1, Ring.ringZxyz);
                                F bb = new F(b2);
                                F cc = new F(ca);

                                Element mb = b1.multiply(MINUS_ONE, Ring.ringZxyz);
                                Element a2 = (two).multiply(a1, Ring.ringZxyz);
                                F mb1 = new F(mb);
                                F a22 = new F(a2);
                                Element D = bb.subtract(cc.multiply(c1, Ring.ringZxyz), Ring.ringZxyz);

                                F kor = new F(F.SQRT, new F[]{new F(D)});

                                Element x1 = ((mb1).subtract(kor, Ring.ringZxyz)).divide(a22, Ring.ringZxyz);
                                Element x2 = ((mb1).add(kor, Ring.ringZxyz)).divide(a22, Ring.ringZxyz);
                                Element x = (b1.multiply(MINUS_ONE, Ring.ringZxyz)).divide((two).multiply(a1, Ring.ringZxyz), Ring.ringZxyz);//число!!!!

                                Element[] p = new Element[]{D, x1, x2, x};
                                CF xx = new CF(p, "D");
                                return xx;
                            }
                        }
                    } //B буква
                    else {
                        // C число
                        if (((F) C).X[0] instanceof Polynom) {
                            if (((Polynom) ((F) C).X[0]).powers.length == 0) {
                                Polynom a1 = (Polynom) ((F) A).X[0];
                                Polynom c1 = (Polynom) ((F) C).X[0];
                                F a11 = new F(A);
                                F b11 = new F(B);
                                F c11 = new F(C);
                                Polynom ch = four;
                                Element s = ch.multiply(a1, Ring.ringZxyz).multiply(c1, Ring.ringZxyz);
                                F ss = new F(s);

                                Element D = (b11.multiply(b11, Ring.ringZxyz)).subtract(ss, Ring.ringZxyz);
                                Element a2 = (two).multiply(a1, Ring.ringZxyz);
                                F a22 = new F(a2);
                                F kor = new F(F.SQRT, new F[]{new F(D)});
                                Element x1 = ((b11.multiply(MINUS_ONE, Ring.ringZxyz)).subtract(kor, Ring.ringZxyz)).divide(a22, Ring.ringZxyz);
                                Element x2 = ((b11.multiply(MINUS_ONE, Ring.ringZxyz)).add(kor, Ring.ringZxyz)).divide(a22, Ring.ringZxyz);
                                Element x = (b11.multiply(MINUS_ONE, Ring.ringZxyz)).divide(a22, Ring.ringZxyz);

                                Element[] ccc = new Element[]{D, x1, x2, x};
                                CF xx = new CF(ccc, "D");
                                return xx;
                            }
                        } //C буква
                        else {
                            F a = new F(A);
                            F b = new F(B);
                            F c = new F(C);
                            Polynom ch1 = four;
                            F ch = new F(ch1);
                            Element D = b.multiply(b, Ring.ringZxyz).subtract(ch.multiply(a, Ring.ringZxyz).multiply(c, Ring.ringZxyz), Ring.ringZxyz);
                            F kor = new F(F.SQRT, new F[]{new F(D)});
                            Polynom tt = two;
                            F t = new F(tt);
                            Element x1 = ((b.multiply(MINUS_ONE, Ring.ringZxyz)).subtract(kor, Ring.ringZxyz)).divide(tt.multiply(a, Ring.ringZxyz), Ring.ringZxyz);
                            Element x2 = ((b.multiply(MINUS_ONE, Ring.ringZxyz)).add(kor, Ring.ringZxyz)).divide(tt.multiply(a, Ring.ringZxyz), Ring.ringZxyz);
                            Element x = (b.multiply(MINUS_ONE, Ring.ringZxyz)).divide(tt.multiply(a, Ring.ringZxyz), Ring.ringZxyz);
                            Element[] ccc = new Element[]{D, x1, x2, x};
                            CF xx = new CF(ccc, "D");
                            return xx;
                        }

                    }
                }


            }
        } //A буква
        else {
            //если B число
            if (((F) B).X[0] instanceof Polynom) {
                if (((Polynom) ((F) B).X[0]).powers.length == 0) {
                    // C число
                    if (((F) C).X[0] instanceof Polynom) {
                        if (((Polynom) ((F) C).X[0]).powers.length == 0) {
                            if ((B.isZero(Ring.ringZxyz)) && (C.isZero(Ring.ringZxyz))) {
                                CF t = new CF(ZERO);
                                return t;
                            }
                            Polynom b1 = (Polynom) ((F) B).X[0];
                            Polynom c1 = (Polynom) ((F) C).X[0];
                            Polynom ch = four;
                            Polynom tt = two;
                            F t = new F(tt);
                            Element b2 = b1.multiply(b1, Ring.ringZxyz);
                            Element c4 = ch.multiply(c1, Ring.ringZxyz);
                            F bb = new F(b2);
                            F cc = new F(c4);
                            F a = new F(A);
                            Element D = bb.subtract(cc.multiply(a, Ring.ringZxyz), Ring.ringZxyz);
                            F kor = new F(F.SQRT, new F[]{new F(D)});
                            F b = new F(B);
                            F c = new F(C);

                            Element x1 = ((b.multiply(MINUS_ONE, Ring.ringZxyz)).subtract(kor, Ring.ringZxyz)).divide(t.multiply(a, Ring.ringZxyz), Ring.ringZxyz);
                            Element x2 = ((b.multiply(MINUS_ONE, Ring.ringZxyz)).add(kor, Ring.ringZxyz)).divide(t.multiply(a, Ring.ringZxyz), Ring.ringZxyz);
                            Element x = (b.multiply(MINUS_ONE, Ring.ringZxyz)).divide(t.multiply(a, Ring.ringZxyz), Ring.ringZxyz);

                            Element[] ccc = new Element[]{D, x1, x2, x};
                            CF xx = new CF(ccc, "D");
                            return xx;

                        }
                    } //C-буква
                    else {
                        F a = new F(A);
                        F c = new F(C);
                        Polynom b1 = (Polynom) ((F) B).X[0];
                        Element b2 = b1.multiply(b1, Ring.ringZxyz);
                        F b = new F(b2);
                        Polynom ch1 = four;
                        F ch = new F(ch1);
                        Element D = b.subtract(ch.multiply(a, Ring.ringZxyz).multiply(c, Ring.ringZxyz), Ring.ringZxyz);
                        F kor = new F(F.SQRT, new F[]{new F(D)});

                        Polynom tt = two;
                        F t = new F(tt);
                        Element x1 = ((b.multiply(MINUS_ONE, Ring.ringZxyz)).subtract(kor, Ring.ringZxyz)).divide(tt.multiply(a, Ring.ringZxyz), Ring.ringZxyz);
                        Element x2 = ((b.multiply(MINUS_ONE, Ring.ringZxyz)).add(kor, Ring.ringZxyz)).divide(tt.multiply(a, Ring.ringZxyz), Ring.ringZxyz);
                        Element x = (b.multiply(MINUS_ONE, Ring.ringZxyz)).divide(tt.multiply(a, Ring.ringZxyz), Ring.ringZxyz);

                        Element[] p = new Element[]{D, x1, x2, x};
                        CF xx = new CF(p, "D");
                        return xx;
                    }
                }
            } //B буква
            else {
                // C число
                if (((F) C).X[0] instanceof Polynom) {
                    if (((Polynom) ((F) C).X[0]).powers.length == 0) {
                        F a = new F(A);
                        F b = new F(B);
                        Polynom c1 = (Polynom) ((F) C).X[0];
                        F c = new F(C);
                        Polynom ch = four;
                        Element c4 = ch.multiply(c1, Ring.ringZxyz);
                        F ss = new F(c4);

                        Element D = (b.multiply(b, Ring.ringZxyz)).subtract(ss.multiply(a, Ring.ringZxyz), Ring.ringZxyz);
                        Polynom t = two;
                        F tt = new F(t);
                        F kor = new F(F.SQRT, new F[]{new F(D)});



                        Element x1 = ((b.multiply(MINUS_ONE, Ring.ringZxyz)).subtract(kor, Ring.ringZxyz)).divide(tt.multiply(a, Ring.ringZxyz), Ring.ringZxyz);
                        Element x2 = ((b.multiply(MINUS_ONE, Ring.ringZxyz)).add(kor, Ring.ringZxyz)).divide(tt.multiply(a, Ring.ringZxyz), Ring.ringZxyz);
                        Element x = (b.multiply(MINUS_ONE, Ring.ringZxyz)).divide(tt.multiply(a, Ring.ringZxyz), Ring.ringZxyz);

                        Element[] ccc = new Element[]{D, x1, x2, x};
                        CF xx = new CF(ccc, "D");
                        return xx;
                    }
                } //C буква
                else {
                    F a = new F(A);
                    F b = new F(B);
                    F c = new F(C);
                    Polynom ch1 = four;
                    F ch = new F(ch1);
                    Element D = b.multiply(b, Ring.ringZxyz).subtract(ch.multiply(a, Ring.ringZxyz).multiply(c, Ring.ringZxyz), Ring.ringZxyz);
                    //F kor = new F(F.SQRT, new F[]{new F(D)});
                    F kor = new F(F.SQRT, D);
                    Polynom tt = two;
                    F t = new F(tt);
                    Element x1 = ((b.multiply(MINUS_ONE, Ring.ringZxyz)).subtract(kor, Ring.ringZxyz)).divide(tt.multiply(a, Ring.ringZxyz), Ring.ringZxyz);
                    Element x2 = ((b.multiply(MINUS_ONE, Ring.ringZxyz)).add(kor, Ring.ringZxyz)).divide(tt.multiply(a, Ring.ringZxyz), Ring.ringZxyz);
                    Element x = (b.multiply(MINUS_ONE, Ring.ringZxyz)).divide(tt.multiply(a, Ring.ringZxyz), Ring.ringZxyz);
                    Element[] ccc = new Element[]{D, x1, x2, x};
                    CF xx = new CF(ccc, "D");
                    return xx;
                }

            }
        }

        return this;
    }

    /**
     * метод, решающий линейные неравенства вида Bx+C>0 (<, <=, >=)
     * @param Coeffs - массив типа Element[], содержащий коэффициентс B и C
     * @param bm - знак неравенства, если bm=1 - больше, если bm=-1 - меньше 0
     * @param rnr - знак неравенства, если rnr=1 - рвно нулю rnr=-1 - не равно нулю
     * @return решение неравенства в виде интервала, либо в общем виде
     * @throws com.mathpar.polynom.PolynomException
     * @throws com.mathpar.func.ContextException
     */
     public CF Resh_lineal_Ner(Element[] Coeffs, int bm, int rnr, Ring rng) {

        boolean bol = (bm == 1) && (rnr == -1);
        boolean men = (bm == -1) && (rnr == -1);
        boolean br = (bm == 1) && (rnr == 1);
        boolean mr = (bm == -1) && (rnr == 1);

        CF emptySet = new CF(new Element[]{});
        CF allReals = new CF(new Element[]{NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY});

        Element B = Coeffs[0];
        Element C = Coeffs[1];

        if ((((F) B).X[0] instanceof Polynom) && (((Polynom) ((F) B).X[0]).powers.length == 0)) {//B число
            if ((((F) C).X[0] instanceof Polynom) && (((Polynom) ((F) C).X[0]).powers.length == 0)) {//С число
                if (B.isZero(Ring.ringZxyz)) {
                    if (C.isZero(Ring.ringZxyz)) {
                        if ((bol) || (men)) {
                            return emptySet;
                        }
                        if ((br) || (mr)) {
                            return allReals;
                        }
                    }
                    if ((C.compareTo(rng.numberONE.myZero(Ring.ringZxyz), Ring.ringZxyz) == 1) && (bol) && (br)) {
                        return allReals;
                    }//C>0
                    if ((C.compareTo(rng.numberONE.myZero(Ring.ringZxyz), Ring.ringZxyz) == -1) && (men) && (mr)) {
                        return allReals;
                    }//C<0
                    //в другом случае
                    return emptySet;
                } else {//B!=0
                    Polynom c = (Polynom) ((F) C).X[0];
                    Polynom b = (Polynom) ((F) B).X[0];
                    Element x = (c.divide(b, Ring.ringZxyz)).multiply(MINUS_ONE, Ring.ringZxyz);
                    if (bol) {
                        Element[] xx = new Element[]{x, NumberR.POSITIVE_INFINITY};
                        CF m = new CF(xx, "int()");
                        return m;
                    }
                    if (men) {
                        Element[] xx = new Element[]{NumberR.NEGATIVE_INFINITY, x};
                        CF m = new CF(xx, "int()");
                        return m;
                    }
                    if (br) {
                        Element[] xx = new Element[]{x, NumberR.POSITIVE_INFINITY};
                        CF m = new CF(xx, "int[)");
                        return m;
                    }
                    if (mr) {
                        Element[] xx = new Element[]{NumberR.NEGATIVE_INFINITY, x};
                        CF m = new CF(xx, "int(]");
                        return m;
                    }
                }
            } else {// C буква
                if (B.isZero(Ring.ringZxyz)) {
                    if (bol) {
                        CF[] t = new CF[]{emptySet, allReals};
                        CF y = new CF(t, "C>0");
                        return y;
                    }
                    if (men) {
                        CF[] t = new CF[]{emptySet, allReals};
                        CF y = new CF(t, "C<0");
                        return y;
                    }
                    if (br) {
                        CF[] t = new CF[]{emptySet, allReals};
                        CF y = new CF(t, "C>=0");
                        return y;
                    }
                    if (mr) {
                        CF[] t = new CF[]{emptySet, allReals};
                        CF y = new CF(t, "C<=0");
                        return y;
                    }
                } else {//B!=0
                    F b = new F(B);
                    F c = new F(C);
                    Element x = (c.divide(b, Ring.ringZxyz)).multiply(MINUS_ONE, Ring.ringZxyz);
                    if (bol) {
                        Element[] xx = new Element[]{x, NumberR.POSITIVE_INFINITY};
                        CF m = new CF(xx, "int()");
                        return m;
                    }
                    if (men) {
                        Element[] xx = new Element[]{NumberR.NEGATIVE_INFINITY, x};
                        CF m = new CF(xx, "int()");
                        return m;
                    }
                    if (br) {
                        Element[] xx = new Element[]{x, NumberR.POSITIVE_INFINITY};
                        CF m = new CF(xx, "int[)");
                        return m;
                    }
                    if (mr) {
                        Element[] xx = new Element[]{NumberR.NEGATIVE_INFINITY, x};
                        CF m = new CF(xx, "int(]");
                        return m;
                    }
                }
            }
        } // B буква
        else {
            //C число
            if ((((F) C).X[0] instanceof Polynom) && (((Polynom) ((F) C).X[0]).powers.length == 0)) {
                if (C.isZero(Ring.ringZxyz)) {
                    if (bol) {
                        Element[] t = new Element[]{NumberR.ZERO, NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY};
                        CF y = new CF(t, "int()");
                        return y;
                    }
                    if (men) {
                        Element[] t = new Element[]{NumberR.ZERO, NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY};
                        CF y = new CF(t, "int1()");
                        return y;
                    }
                    if (br) {
                        Element[] t = new Element[]{NumberR.ZERO, NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY};
                        CF y = new CF(t, "int[)");
                        return y;
                    }
                    if (mr) {
                        Element[] t = new Element[]{NumberR.ZERO, NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY};
                        CF y = new CF(t, "int(]");
                        return y;
                    }
                } else {
                    if ((C.compareTo(rng.numberONE.myZero(Ring.ringZxyz), Ring.ringZxyz) == 1)) {// C>0
                        F b = new F(B);
                        F c = new F(C);
                        Element x = (c.divide(b, Ring.ringZxyz)).multiply(MINUS_ONE, Ring.ringZxyz);
                        if (bol) {
                            Element[] t = new Element[]{x, NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY};
                            CF y = new CF(t, "B>0");
                            return y;
                        }
                        if (men) {
                            Element[] t = new Element[]{x, NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY};
                            CF y = new CF(t, "B<0");
                            return y;
                        }
                        if (br) {
                            Element[] t = new Element[]{x, NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY};
                            CF y = new CF(t, "B>=0");
                            return y;
                        }
                        if (mr) {
                            Element[] t = new Element[]{x, NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY};
                            CF y = new CF(t, "B>=0");
                            return y;
                        }
                    }
                    if ((C.compareTo(rng.numberONE.myZero(Ring.ringZxyz), Ring.ringZxyz) == -1)) {
                        F b = new F(B);
                        F c = new F(C);
                        Element x = (c.divide(b, Ring.ringZxyz)).multiply(MINUS_ONE, Ring.ringZxyz);
                        if (bol) {
                            Element[] t = new Element[]{x, NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY};
                            CF y = new CF(t, "B1>0");
                            return y;
                        }
                        if (men) {
                            Element[] t = new Element[]{x, NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY};
                            CF y = new CF(t, "B1<0");
                            return y;
                        }
                        if (br) {
                            Element[] t = new Element[]{x, NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY};
                            CF y = new CF(t, "B1>=0");
                            return y;
                        }
                        if (mr) {
                            Element[] t = new Element[]{x, NumberR.NEGATIVE_INFINITY, NumberR.POSITIVE_INFINITY};
                            CF y = new CF(t, "B1>=0");
                            return y;
                        }
                    }
                }
            } //C буква
            else {
                F b = new F(B);
                F c = new F(C);
                Element x = (c.divide(b, Ring.ringZxyz)).multiply(MINUS_ONE, Ring.ringZxyz);
                Element[] e1 = new Element[]{x, NumberR.POSITIVE_INFINITY};
                Element[] e2 = new Element[]{NumberR.NEGATIVE_INFINITY, x};
                CF a1 = new CF(e1, "int()");
                CF a2 = new CF(e1, "int[)");
                CF a3 = new CF(e2, "int()");
                CF a4 = new CF(e2, "int(]");

                if (bol) {
                    CF[] u = new CF[]{emptySet, allReals, a1};
                    CF t = new CF(u, "all");
                    return t;
                }
                if (br) {
                    CF[] u = new CF[]{emptySet, allReals, a2};
                    CF t = new CF(u, "all1");
                    return t;
                }
                if (men) {
                    CF[] u = new CF[]{allReals, emptySet, a2};
                    CF t = new CF(u, "all1");
                    return t;
                }
                if (mr) {
                    CF[] u = new CF[]{allReals, emptySet, a4};
                    CF t = new CF(u, "all");
                    return t;
                }
            }
        }
        return this;
    }


    public int compareTo(Element o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(Element x, Ring ring) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean isZero(Ring ring) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean equals(Element x, Ring r) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean isOne(Ring ring) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
