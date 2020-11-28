/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import java.util.Vector;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 * решение алгебраических уравнений вида : ax=0; ax+b=0; ax^2+bx+c=0;
 * ax^3+bx^2+cx+d=0; ax^4+bx^3+cx^2+dx+f=0.
 *
 * @author Пашин Дмитрий 35гр( correct by Smirnov Roman)
 */
public class FQ extends F {

    FQ() {
    }

    ;
    public Factor FactorEl(Polynom f, int option, Ring ring) throws Exception {
        FactorPol ff = f.factorOfPol_inQ(false, ring); // факторизация
        return FactorEl(ff, option, ring);
    }

    public static Factor FactorEl(FactorPol f, int option, Ring ring) {
        Factor k = new Factor();
        Factor n = new Factor();
        Vector<Element> powk = new Vector<Element>();
        Vector<Element> resk = new Vector<Element>();
        Vector<Element> pown = new Vector<Element>();
        Vector<Element> resn = new Vector<Element>();
        for (int i = 0; i < f.multin.length; i++) {
            Polynom pol = f.multin[i];
            if (pol.powers.length == 0) {
                continue;
            }
            Factor res = solvAlgEq(pol, option, ring);
            if ((res == null) || (res.multin.isEmpty())) {
                pown.add(new NumberZ(f.powers[i]));
                resn.add(new F(f.multin[i]));
            } else {
                Vector<Element> p = res.powers;
                Vector<Element> g = new Vector<Element>();
                for (int j = 0; j < p.size(); j++) {
                    g.add(((NumberZ) p.get(j)).multiply(new NumberZ(f.powers[i])));
                }
                powk.addAll(g);
                resk.addAll(res.multin);
            }
        }
        k = new Factor(resk, powk);
        n = new Factor(resn, pown);
        k.multin.addAll(n.multin);
        k.powers.addAll(n.powers);
        return k;
    }

    public static void main(String[] args) {
        Ring r = new Ring("Z[x,y,z]");
        Polynom h = new Polynom("x^4z+x^2+7", r);
        System.out.println("   " + h.groupPolToString(h.groupPolynomialOfVar(0, r), 0, r));
    }

    private Factor ferrari(Element[] coeffs, Ring ring) {
        Vector<Element> res_multin = new Vector<Element>();
        Vector<Element> res_powers = new Vector<Element>();
        // - B/2A
        Element first = new F(DIVIDE, new Element[]{coeffs[1].negate(ring), coeffs[0].multiply(new NumberZ(4), ring)});


        return null;
    }

    private Polynom getCalculatePolynom(Polynom input, Ring ring) {
        FactorPol tempRes = input.groupPolynomialOfVar(0, ring);// пока что делаем по первой переменной в кольце
        int len_Eq = 0; // длинна уровнения (количество коэффициентов)
        for (int i = 0; i < tempRes.multin.length; i++) {
            if (!tempRes.multin[i].isZero(ring)) {
                len_Eq++;
            }
        }
        int[] new_pow = new int[len_Eq];
        Element[] new_coef = new Element[len_Eq];
        int index = 0;
        for (int i = tempRes.multin.length - 1; i >= 0; i--) {
            if (!tempRes.multin[i].isZero(ring)) {
                new_pow[index] = tempRes.powers[i];
                new_coef[index] = tempRes.multin[i];
                index++;
            }
        }
        return new Polynom(new_pow, new_coef);
    }

    /**
     * Определяет степень уравнения и предает его соответствующему методу Решает
     * только ур-ния до 4-ой степени
     *
     * @param (F) p -уравнение
     * @param (int)option 0 общий случай 1 действитеьные числа -1 комплексные
     * @return F[] массив корней уравнения. размер массива равен количеству
     * корней уравнения
     *
     */
    public Factor solvAlgEq(F p, int option, Ring ring) {
//        Polynom input=(Polynom)p.X[0];
//        FactorPol tempRes=input.groupPolynomialOfVar(0, ring);// пока что делаем по первой переменной в кольце
//
//        Factor res = null;
//        int p1,p2,p3,p4=0;
//
//        p1 = (tempRes.multin[0].isZero(ring)) ? 0 : 1;
//
//        if(tempRes.multin.length>2) p2 = 1;
//        if(tempRes.multin.length>3) p3=1;
//        if(tempRes.multin.length>4) p4=1;
//
//        int P = tempRes.multin.length-1;
//
//        int len_Eq=0; // длинна уровнения (количество коэффициентов)
//
//        for(int i=0; i < tempRes.multin.length; i++){
//            if(!tempRes.multin[i].isZero(ring)) len_Eq++;
//        }
//
//        int[] new_pow=new int[len_Eq];
//        Element[] new_coef=new Element[len_Eq];
//        int index=0;
//
//        for(int i=tempRes.multin.length-1; i >=0 ; i--){
//            if(!tempRes.multin[i].isZero(ring)) {
//            new_pow[index]=tempRes.powers[i];
//            new_coef[index]=tempRes.multin[i];
//            index++;
//            }
//        }
//
//        Polynom newPol=new Polynom(new_pow, new_coef);

        Polynom newPol = getCalculatePolynom((Polynom) p.X[0], ring);
        switch (newPol.coeffs.length - 1) {
            case (4): {
                return FQ.solvAlgEq4(newPol, 0, ring);
            }
            case (3): {
                return FQ.solvAlgEq3ord(newPol, 0, ring);
            }
            case (2): {
                return FQ.solvAlgEq2ord(newPol, 0, ring);
            }
            case (1): {
                return FQ.solvAlgEq2ord(newPol, 0, ring);
            }
            case (0): {
                return FQ.solvAlgEq2ord(newPol, 0, ring);
            }
            default:
                ring.exception.append(" Unknown equation !!! ");
                return null;
        }

//
//        for (int i = 0; i < p.X.length; i++) {
//            if (((p.X[i])instanceof F)&&(((F) (p.X[i])).name == MULTIPLY)) {
//                if (((F) (((F) (p.X[i])).X[1])).name == POW) {
//                  Element pow = ((F) (((F) (p.X[i])).X[1])).X[1];
//                  int powEq=pow.intValue();
//                  switch (powEq) {
//                    case (4): {p4++; if (P < 4) { P = 4; } continue; }
//                    case (3): {p3++; if (P < 3) { P = 3; } continue; }
//                    case (2): {p2++; if (P < 2) { P = 2; } continue; }
//                    case (1): {p1++; if (P < 1) { P = 1; } continue; }
//                  }
//                }
//            }
//        }
        // для функции найдена старшая степень и наличие остальных из четырех степеней
//        if ((p4<2)&&(p3<2)&&(p2<2)&&(p1<2)) {
//           switch (P) {
//             case (4): {
//               switch (len_Eq) {
//                  case (5): {
//                            F a = new F(tempRes.multin[0]);
//                            F b = new F(tempRes.multin[1]);
//                            F d = new F(tempRes.multin[2]);
//                            F e = new F(tempRes.multin[3]);
//                            if ((a.equals(e,ring)) && (b.equals(d, ring))) {
//                                res = FQ.solvAlgEq4Ret(p, option, ring);}// возвратное
//                            else {res = FQ.solvAlgEq4(p, option, ring);} // общий случай, Феррари
//                        }
//                  case (3): {if ((p4 == 1)&&(p3 == 0)&&(p2 == 1)&&(p1 == 0))
//                                       res = FQ.solvAlgEq4Bi(p, option, ring); } // биквадратное
//                }break;
//             }
//             case (3): {res = FQ.solvAlgEq3ord(p, option, ring);break;}// общий случай, квадратного
//             case (2): {res = FQ.solvAlgEq2ord(p, option, ring);break;}// общий случай, квадратного
//             case (1): {res = FQ.solvAlgEq2ord(p, option, ring);break;}// общий случай, квадратного
//             case (0): {res = FQ.solvAlgEq2ord(p, option, ring);break;}// общий случай, квадратного
//             default:  //System.out.println("Уравнения этой степени не поддерживаются");
//                    break;
//          }
//        }
//        return res;
    }

    /**
     * Определяет степень уравнения и предает его соответствующему методу Решает
     * только ур-ния до 4-ой степени
     *
     * @param (F) p -уравнение
     * @param (int)option 0 общий случай 1 действитеьные числа -1 комплексные
     * @return F[] массив корней уравнения. размер массива равен количеству
     * корней уравнения
     *
     */
    public static Factor solvAlgEq_(F p, int option, Ring ring) {
//        p=p.simplify(ring);
//        if(p.name==ID) {
//          int type= p.X[0].numbElementType();
//          if(type< Ring.Polynom) { return solvAlgEq(new Polynom(p.X[0]), option, ring);} else{
//          if(type==Ring.Polynom) {return solvAlgEq((Polynom)p.X[0], option, ring);}
//          }
//        }

        Factor res = null;
        int p1 = 0, p2 = 0, p3 = 0, p4 = 0;
        int P = 0;
        for (int i = 0; i < p.X.length; i++) {
            if (((p.X[i]) instanceof F) && (((F) (p.X[i])).name == MULTIPLY)) {
                if (((F) (((F) (p.X[i])).X[1])).name == POW) {
                    Element pow = ((F) (((F) (p.X[i])).X[1])).X[1];
                    int powEq = pow.intValue();
                    switch (powEq) {
                        case (4): {
                            p4++;
                            if (P < 4) {
                                P = 4;
                            }
                            continue;
                        }
                        case (3): {
                            p3++;
                            if (P < 3) {
                                P = 3;
                            }
                            continue;
                        }
                        case (2): {
                            p2++;
                            if (P < 2) {
                                P = 2;
                            }
                            continue;
                        }
                        case (1): {
                            p1++;
                            if (P < 1) {
                                P = 1;
                            }
                            continue;
                        }
                    }
                }
            }
        } // для функции найдена старшая степень и наличие остальных из четырех степеней
        if ((p4 < 2) && (p3 < 2) && (p2 < 2) && (p1 < 2)) {
            switch (P) {
                case (4): {
                    switch (p.X.length) {
                        case (5): {
                            F a = (F) (((F) (p.X[0])).X[0]);
                            F b = (F) (((F) (p.X[1])).X[0]);
                            F d = (F) (((F) (p.X[3])).X[0]);
                            F e = ((F) (p.X[4]));
                            if ((a.equals(e, ring)) && (b.equals(d, ring))) {
                                res = FQ.solvAlgEq4Ret(p, option, ring);
                            }// возвратное
                            else {
                                res = FQ.solvAlgEq4(p, option, ring);
                            } // общий случай, Феррари
                        }
                        case (3): {
                            if ((p4 == 1) && (p3 == 0) && (p2 == 1) && (p1 == 0)) {
                                res = FQ.solvAlgEq4Bi(p, option, ring);
                            }
                        } // биквадратное
                    }
                    break;
                }
                case (3): {
                    res = FQ.solvAlgEq3ord(p, option, ring);
                    break;
                }// общий случай, квадратного
                case (2): {
                    res = FQ.solvAlgEq2ord(p, option, ring);
                    break;
                }// общий случай, квадратного
                case (1): {
                    res = FQ.solvAlgEq2ord(p, option, ring);
                    break;
                }// общий случай, квадратного
                case (0): {
                    res = FQ.solvAlgEq2ord(p, option, ring);
                    break;
                }// общий случай, квадратного
                default:  //System.out.println("Уравнения этой степени не поддерживаются");
                    break;
            }
        }
        return res;
    }

    /**
     * Определяет степень уравнения и предает его соответствующему методу
     *
     * @param (F) p -уравнение
     * @param (int)option 0 общий случай 1 действитеьные числа -1 комплексные
     * @return F[] массив корней уравнения. размер массива равен количеству
     * корней уравнения
     *
     */
    public static Factor solvAlgEq(Polynom p, int option, Ring ring) {
        Factor res = null;
        int P = 0;
        for (int i = 0; i < p.powers.length; i++) {
            if (p.powers[i] > P) {
                P = p.powers[i];
            }
        }
        switch (P) {
            case (4): {
                return FQ.solvAlgEq4(p, option, ring);
            }
            case (3): {
                return FQ.solvAlgEq3ord(p, option, ring);
            }
            case (2): {
                return FQ.solvAlgEq2ord(p, option, ring);
            }
            case (1): {
                return FQ.solvAlgEq2ord(p, option, ring);
            }
            case (0): {
                return FQ.solvAlgEq2ord(p, option, ring);
            }
            default: //System.out.println("Уравнения этой степени не поддерживаются");
                break;
        }
        return res;
    }   // непонятно, где же разбор разных типов ур-ния 4-ой степени

    /**
     * решение биквадратного уравнения (F)
     *
     * @param (F) p -уравнение вида ax^4+bx^2+c
     * @param (int)option 0 общий случай 1 действитеьные числа -1 комплексные
     * @return F[] массив корней уравнения. размер массива равен количеству
     * корней уравнения
     *
     */
    public static Factor solvAlgEq4Bi(F p, int option, Ring ring) {
        Element[] res = null;
        Element[] k = new Element[4];

        F a = new F(ring.numberZERO);
        F b = new F(ring.numberZERO);
        F c = new F(ring.numberZERO);

        F x1 = null;
        F x2 = null;
        F x3 = null;
        F x4 = null;
        Factor Res = null;
        Vector<Element> K = new Vector<Element>();
        Vector<Element> X = new Vector<Element>();
        a = (F) ((F) (p.X[0])).X[0];
        b = (F) ((F) (p.X[1])).X[0];
        c = (F) (p.X[2]);
        Polynom y = new Polynom("y", ring);
        F yf = new F(y);
        F yp = new F(F.POW, new F[]{yf, new F(new NumberR64(2))});
        F ayf = new F(F.MULTIPLY, new F[]{a, yp});
        F byf = new F(F.MULTIPLY, new F[]{b, yf});
        F g = new F(F.ADD, new F[]{ayf, byf, c});

        switch (option) {
            case (1): {
                Factor FE = FQ.solvAlgEq2ord(g, option, ring);
                Element g1 = FE.multin.get(0);
                Element g2 = FE.multin.get(1);
                F y1 = new F(g1);
                F y2 = new F(g2);

                x1 = new F(F.SQRT, new F[]{y1});
                x2 = new F(F.MULTIPLY, new F[]{x1, new F(ring.numberMINUS_ONE)});
                x3 = new F(F.SQRT, new F[]{y2});
                x4 = new F(F.MULTIPLY, new F[]{x3, new F(ring.numberMINUS_ONE)});

                x1 = new F(x1.valueOf(ring));
                x2 = new F(x2.valueOf(ring));
                x3 = new F(x3.valueOf(ring));
                x4 = new F(x4.valueOf(ring));
                break;
            }
            case (0): {
                Factor FE = FQ.solvAlgEq2ord(g, option, ring);
                Element g1 = FE.multin.get(0);
                Element g2 = FE.multin.get(1);
                F y1 = new F(g1);
                F y2 = new F(g2);
                x1 = new F(F.SQRT, new F[]{y1});
                x2 = new F(F.MULTIPLY, new F[]{x1, new F(ring.numberMINUS_ONE)});
                x3 = new F(F.SQRT, new F[]{y2});
                x4 = new F(F.MULTIPLY, new F[]{x3, new F(ring.numberMINUS_ONE)});
                break;
            }
            case (-1): {
                Ring newRing = new Ring("C64[x,y,a,b,c,d,e]");
                Factor FE = FQ.solvAlgEq2ord(g, option, ring);
                Element g1 = FE.multin.get(0);
                Element g2 = FE.multin.get(1);
                F y1 = new F(g1);
                F y2 = new F(g2);
                Complex sq1 = new Complex(y1.valueOf(ring), ring);
                x1 = new F(sq1.sqrt(ring));
                x2 = (F) x1.negate(ring);
                Complex sq2 = new Complex((NumberR64) y2.valueOf(ring), ring);
                x3 = new F(sq2.sqrt(ring));
                x4 = (F) x3.negate(ring);
                break;
            }
        }
        X.add((new F(x1)).X[0]);
        X.add((new F(x2)).X[0]);
        X.add((new F(x3)).X[0]);
        X.add((new F(x4)).X[0]);
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        Res = new Factor(K, X);
        return Res;
    }

    /**
     * решение биквадратного уравнения (Polynom)
     *
     * @param (Polynom) p -уравнение вида ax^4+bx^2+c
     * @param (int)option 0 общий случай 1 действитеьные числа -1 комплексные
     * @return F[] массив корней уравнения. размер массива равен количеству
     * корней уравнения
     *
     */
    public static Factor solvAlgEq4Bi(Polynom p, int option, Ring ring) {
        Element ZERO = ring.numberZERO;

        Element a = new F(ZERO);
        Element b = new F(ZERO);
        Element c = new F(ZERO);

        Element x1 = null;
        Element x2 = null;
        Element x3 = null;
        Element x4 = null;

        Factor Res = null;
        Vector<Element> K = new Vector<Element>();
        Vector<Element> X = new Vector<Element>();


        for (int i = 0; i < p.coeffs.length; i++) {
            switch (p.powers[i]) {
                case (4):
                    a = p.coeffs[i];
                    break;
                case (3):
                    b = p.coeffs[i];
                    break;
                case (2):
                    c = p.coeffs[i];
                    break;
                default:
                    break;
            }
        }

        Polynom y = new Polynom("y", ring);
        F yf = new F(y);
        F yp = new F(F.POW, new Element[]{yf, new NumberR64(2)});
        F ayf = new F(F.MULTIPLY, new Element[]{a, yp});
        F byf = new F(F.MULTIPLY, new Element[]{b, yf});
        F g = new F(F.ADD, new Element[]{ayf, byf, c});



        switch (option) {
            case (1): {
                Factor FE = FQ.solvAlgEq2ord(g, option, ring);
                Element g1 = FE.multin.get(0);
                Element g2 = FE.multin.get(1);
                F y1 = new F(g1);
                F y2 = new F(g2);

                x1 = new F(F.SQRT, new Element[]{y1});
                x2 = new F(F.MULTIPLY, new Element[]{x1, ring.numberMINUS_ONE});
                x3 = new F(F.SQRT, new Element[]{y2});
                x4 = new F(F.MULTIPLY, new Element[]{x3, ring.numberMINUS_ONE});

                x1 = ((F) x1).valueOf(ring);
                x2 = ((F) x2).valueOf(ring);
                x3 = ((F) x3).valueOf(ring);
                x4 = ((F) x4).valueOf(ring);
                break;
            }
            case (0): {
                Factor FE = FQ.solvAlgEq2ord(g, option, ring);
                Element g1 = FE.multin.get(0);
                Element g2 = FE.multin.get(1);
                F y1 = new F(g1);
                F y2 = new F(g2);
                x1 = new F(F.SQRT, new F[]{y1});
                x2 = new F(F.MULTIPLY, new Element[]{x1, ring.numberMINUS_ONE});
                x3 = new F(F.SQRT, new F[]{y2});
                x4 = new F(F.MULTIPLY, new Element[]{x3, ring.numberMINUS_ONE});
                break;
            }
            case (-1): {
                ring = new Ring("C64[x,y,a,b,c,d,e]");
                Factor FE = FQ.solvAlgEq2ord(g, option, ring);
                Element g1 = FE.multin.get(0);
                Element g2 = FE.multin.get(1);
                F y1 = new F(g1);
                F y2 = new F(g2);
                Complex sq1 = new Complex((NumberR64) y1.valueOf(ring), ring);
                x1 = sq1.sqrt(ring);
                x2 = x1.negate(ring);
                Complex sq2 = new Complex((NumberR64) y2.valueOf(ring), ring);
                x3 = sq2.sqrt(ring);
                x4 = x3.negate(ring);
                break;
            }
        }
        X.add(x1);
        X.add(x2);
        X.add(x3);
        X.add(x4);
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        Res = new Factor(X, K); // комплектуем результат
        return Res;
    }

    /**
     * решение квадратного уравнения (F)
     *
     * @param (F) p -уравнение вида ax^2+bx+c
     * @param option 0 общий случай 1 действитеьные числа -1 комплексные
     * @return F[] массив корней уравнения. размер массива равен количеству
     * корней уравнения
     *
     */
    public static Factor solvAlgEq2ord(F p, int option, Ring ring) {
//        try {
//            Notebook.addRing("C64[x,y,z,a,b,c,d]");
//        } catch (PolynomException ex) {
//            Logger.getLogger(FQ.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ContextException ex) {
//            Logger.getLogger(FQ.class.getName()).log(Level.SEVERE, null, ex);
//        }
        Element two = new NumberR64(2);
        Element four = new NumberR64(4);
        Element zero = new NumberR64(0);

        Element a = zero;
        Element b = zero;
        Element c = zero;

        Element x1 = null;
        Element x2 = null;

        Element k1 = null;
        Element k2 = null;
        Factor Res = null;

        Vector<Element> K = new Vector<Element>();
        Vector<Element> X = new Vector<Element>();

        if ((p.X[0]) instanceof F) {
            a = ((F) (p.X[0])).X[0];
            b = ((F) (p.X[1])).X[0];
            c = (p.X[2]);
        } else {
            Polynom pp = (Polynom) p.X[0];
            Element zer = ring.numberZERO;
            a = (pp.powers[0] == 2) ? new F(pp.coeffs[0]) : new F(zer);
            b = (pp.powers[1] == 1) ? new F(pp.coeffs[1]) : new F(zer);
            a = (pp.powers[2] == 0) ? new F(pp.coeffs[2]) : new F(zer);
        }


        switch (option) {

//В действительных числах
            case (1): {
                if (a.equals(zero)) {
                    if (b.equals(zero)) {
                        x1 = NumberR.POSITIVE_INFINITY;
                        X.add(x1);
                        K.add(new NumberZ(1));
                        //         return new F[]{x1, x2};
                    } else {
                        if (c.equals(zero)) {
                            x1 = zero;
                            X.add(x1);
                            K.add(new NumberZ(1));
                            //             return new F[]{x1, x2};
                        } else {
                            x1 = new F(F.DIVIDE, new Element[]{c.negate(ring), b});
                            X.add(x1);
                            K.add(new NumberZ(1));
                            //             return new F[]{new F(x1.valueOf_ForR64(ring)), x2};
                        }
                    }
                }
                if (b.equals(zero)) {
                    if (c.equals(zero)) {
                        x1 = zero;
                        X.add(x1);
                        K.add(new NumberZ(2));
                        //      return new F[]{x1, x2};
                    } else {
                        F cDa = new F(F.DIVIDE, new Element[]{c.negate(ring), a});
                        x1 = new F(F.SQRT, new F[]{cDa});
                        x2 = x1.negate(ring);
                        X.add(x1);
                        X.add(x2);
                        K.add(new NumberZ(1));
                        K.add(new NumberZ(1));
                        //      return new F[]{new F(x1.valueOf_ForR64(ring)), new F(x1.valueOf_ForR64(ring))};
                    }
                }
                if (c.equals(zero)) {
                    x1 = new F(F.DIVIDE, new Element[]{b.negate(ring), a});
                    x2 = zero;
                    X.add(x1);
                    X.add(x2);
                    K.add(new NumberZ(1));
                    K.add(new NumberZ(1));

                } else {
                    Element bb = b.multiply(b, ring);//   new F(F.MULTIPLY, new Element[]{b, b});
                    F ac4 = new F(F.MULTIPLY, new Element[]{four, a, c});
                    F Discr = new F(F.SUBTRACT, new Element[]{bb, ac4});
                    Discr = new F(Discr.valueOf(ring));
                    switch (Discr.compareTo(zero, ring)) {
                        case (1): {
                            F sD = new F(F.SQRT, new Element[]{Discr});
                            F mb = new F(b.negate(ring));
                            F zn = new F(two.multiply(a, ring));
                            F ch = new F(F.SUBTRACT, new Element[]{mb, sD});
                            F ch2 = new F(F.ADD, new Element[]{mb, sD});
                            x1 = new F(F.DIVIDE, new Element[]{ch, zn});
                            x2 = new F(F.DIVIDE, new Element[]{ch2, zn});
                            x1 = new F(((F) x1).valueOf(ring));
                            x2 = new F(((F) x2).valueOf(ring));
                            X.add(x1);
                            X.add(x2);
                            K.add(new NumberZ(1));
                            K.add(new NumberZ(1));
                            break;
                        }
                        case (0): {
                            //!!!
                            x1 = (b.negate(ring)).
                                    divide(two.multiply(a, ring), ring);
                            X.add(x1);
                            K.add(new NumberZ(2));
                            break;
                        }
                        case (-1): {
                            x1 = NumberR.NAN;
                            X.add(x1);
                            K.add(new NumberZ(1));
                            break;
                        }
                    }
                    break;
                }
                break;
            }

//В общем виде

            case (0): {
                Element bb = b.multiply(b, ring);//   new F(F.MULTIPLY, new F[]{b, b});
                F ac4 = new F(F.MULTIPLY, new Element[]{four, a, c});
                F Discr = new F(F.SUBTRACT, new Element[]{bb, ac4});
                F sD = new F(F.SQRT, new Element[]{Discr});

                F mb = new F(b.negate(ring));
                F zn = new F(two.multiply(a, ring));
                F ch = new F(F.SUBTRACT, new Element[]{mb, sD});
                F ch2 = new F(F.ADD, new Element[]{mb, sD});
                x1 = new F(F.DIVIDE, new Element[]{ch, zn});
                x2 = new F(F.DIVIDE, new Element[]{ch2, zn});
                X.add(x1);
                X.add(x2);
                K.add(new NumberZ(1));
                K.add(new NumberZ(1));
                break;

            }

//В комплексных числах

            case (-1): {
                if (a.equals(zero)) {
                    if (b.equals(zero)) {
                        //!!!
                        x1 = NumberR.POSITIVE_INFINITY;
                        x2 = NumberR.POSITIVE_INFINITY;
                        X.add(x1);
                        X.add(x2);
                        K.add(new NumberZ(1));
                        K.add(new NumberZ(1));
                        //        return new F[]{x1, x2};
                    } else {
                        if (c.equals(zero)) {
                            x1 = zero;
                            X.add(x1);
                            K.add(new NumberZ(1));
                            //            return new F[]{x1, x2};
                        } else {
                            x1 = new F(F.DIVIDE, new Element[]{c.negate(ring), b});
                            X.add(x1);
                            K.add(new NumberZ(1));
                            //            return new F[]{new F(x1.valueOf_ForR64(ring)), x2};
                        }
                    }
                }
                if (b.equals(zero)) {
                    if (c.equals(zero)) {
                        x1 = zero;
                        X.add(x1);
                        K.add(new NumberZ(1));
                        //          return new F[]{x1, x2};
                    } else {
                        F cDa = new F(F.DIVIDE, new Element[]{c.negate(ring), a});
                        Complex Cd = new Complex((NumberR64) cDa.valueOf(ring), ring);
                        x1 = new F(Cd.sqrt(ring));
                        x2 = x1.negate(ring);
                        X.add(x1);
                        X.add(x2);
                        K.add(new NumberZ(1));
                        K.add(new NumberZ(1));
                        //        return new F[]{new F(x1.valueOf_ForR64(ring)), new F(x1.valueOf_ForR64(ring))};
                    }
                }
                if (c.equals(zero)) {
                    x1 = new F(F.DIVIDE, new Element[]{b.negate(ring), a});
                    x2 = zero;
                    X.add(x1);
                    X.add(x2);
                    K.add(new NumberZ(1));
                    K.add(new NumberZ(1));
                    //       return new F[]{new F(x1.valueOf_ForR64(ring)), x2};

                } else {
                    Element bb = b.multiply(b, ring);// new F(F.MULTIPLY, new F[]{b, b});
                    F ac4 = new F(F.MULTIPLY, new Element[]{four, a, c});
                    F Discr = new F(F.SUBTRACT, new Element[]{bb, ac4});
                    switch (Discr.valueOf(ring).compareTo(NumberR64.ZERO, ring)) {
                        case (1): {
                            F sD = new F(F.SQRT, new Element[]{Discr});
                            F mb = new F(b.negate(ring));
                            F zn = new F(two.multiply(a, ring));
                            F ch = new F(F.SUBTRACT, new Element[]{mb, sD});
                            F ch2 = new F(F.ADD, new Element[]{mb, sD});
                            x1 = new F(F.DIVIDE, new Element[]{ch, zn});
                            x2 = new F(F.DIVIDE, new Element[]{ch2, zn});
                            x1 = new F(((F) x1).valueOf(ring));
                            x2 = new F(((F) x2).valueOf(ring));
                            k1 = new NumberZ(1);
                            k2 = new NumberZ(1);
                            break;
                        }
                        case (0): {
                            x1 = (b.negate(ring)).divide(two.multiply(a, ring), ring);
                            X.add(x1);
                            K.add(new NumberZ(2));
                            break;
                        }
                        case (-1): {
                            Discr = new F(Discr.valueOf(ring));
                            Complex Cd = new Complex((NumberR64) Discr.X[0], ring);
                            Complex CsD = Cd.sqrt(ring);

                            F mb = new F(((F) b).valueOf(ring).negate(ring));
                            Complex Cmb = new Complex((NumberR64) mb.X[0], ring);

                            Complex Ctwo = new Complex((NumberR64) two, ring);
                            Complex Ca = new Complex((NumberR64) a, ring);
                            Element zn = Ctwo.multiply(Ca, ring);
                            Element ch = Cmb.subtract(CsD, ring);
                            Element ch2 = Cmb.add(CsD, ring);
                            Element Cx1 = ch.divide(zn, ring);
                            Element Cx2 = ch2.divide(zn, ring);
                            x1 = new F(Cx1);
                            x2 = new F(Cx2);
                            X.add((new F(x1)).X[0]);
                            X.add((new F(x2)).X[0]);
                            K.add(new NumberZ(1));
                            K.add(new NumberZ(1));
                            break;
                        }
                    }
                }
                break;
            }
        }

        Res = new Factor(K, X);
        return Res;
    }

    /**
     * решение квадратного уравнения (Polynom)
     *
     * @param (Polynom) p -уравнение вида ax^2+bx+c
     * @param (int)option 0 общий случай 1 действитеьные числа -1 комплексные
     * @return F[] массив корней уравнения. размер массива равен количеству
     * корней уравнения
     *
     */
    public static Factor solvAlgEq2ord(Polynom p, int option, Ring ring) {

        Element one = p.one(ring);
        Element zero = one.myZero(ring);
        Element m_one = one.myMinus_one(ring);
        Element two = one.add(one, ring);
        Element four = two.add(two, ring);

        Element a = zero;
        Element b = zero;
        Element c = zero;
        Element x1 = null;
        Element x2 = null;

        Factor Res = null;
        Vector<Element> K = new Vector<Element>();
        Vector<Element> X = new Vector<Element>();

        for (int i = 0; i < p.powers.length; i++) {
            switch (p.powers[i]) {
                case (2):
                    a = p.coeffs[i];
                    break;
                case (1):
                    b = p.coeffs[i];
                    break;
                case (0):
                    c = p.coeffs[i];
                    break;
            }
        }


        switch (option) {

//В действительных числах
            case (1): {
                if (a == zero) {
                    if (b == zero) {
                        x1 = new F(NumberR.POSITIVE_INFINITY);
                        X.add(x1);
                        K.add(one);
                        Res = new Factor(X, K);
                        return Res;
                    } else {
                        if (c == zero) {
                            x1 = zero;
                            X.add(x1);
                            K.add(one);
                            Res = new Factor(X, K);
                            return Res;
                        } else {
                            x1 = c.negate(ring).divide(b, ring);
                            X.add(x1);
                            K.add(one);
                            Res = new Factor(X, K);
                            return Res;
                        }
                    }
                }
                if (b == zero) {
                    if (c == zero) {
                        x1 = zero;
                        X.add(x1);
                        K.add(one);
                        Res = new Factor(X, K);
                        return Res;
                    } else {
                        Element cDa = c.negate(ring).divide(a, ring);
                        x1 = cDa.sqrt(ring);
                        x2 = x1.negate(ring);
                        X.add(x1);
                        X.add(x2);
                        K.add(one);
                        K.add(one);
                        Res = new Factor(X, K);
                        return Res;
                        //      return new F[]{new F(x1.valueOf_ForR64(ring)), new F(x1.valueOf_ForR64(ring))};
                    }
                }
                if (c == zero) {
                    x1 = b.negate(ring).divide(a, ring);
                    x2 = zero;
                    X.add(x1);
                    X.add(x2);
                    K.add(one);
                    K.add(one);
                    Res = new Factor(X, K);
                    return Res;

                } else {
                    Element bb = b.multiply(b, ring);
                    Element ac4 = four.multiply(a, ring).multiply(c, ring);
                    Element Discr = bb.subtract(ac4, ring);
                    switch (Discr.compareTo(zero, ring)) {
                        case (1): {
                            Element sD = Discr.sqrt(ring);
                            Element mb = b.negate(ring);
                            Element zn = two.multiply(a, ring);
                            Element ch = mb.subtract(sD, ring);
                            Element ch2 = mb.add(sD, ring);
                            x1 = ch.divide(zn, ring);
                            x2 = ch2.divide(zn, ring);
                            X.add(x1);
                            X.add(x2);
                            K.add(one);
                            K.add(one);
                            break;
                        }
                        case (0): {
                            //!!!
                            x1 = (b.negate(ring)).divide(two.multiply(a, ring), ring);
                            X.add(x1);
                            K.add(two);
                            break;
                        }
                        case (-1): {
                            x1 = new F(NumberR.NAN);
                            X.add(x1);
                            K.add(one);
                            break;
                        }
                    }
                }
                break;
            }



//В общем виде

            case (0): {
                Element bb = b.pow(2, ring);
                Element ac4 = four.multiply(a, ring).multiply(c, ring);
                Element Discr = bb.subtract(ac4, ring);
                Element sD = new F(SQRT, new Element[]{Discr});//    Discr.sqrt(ring);
                Element mb = b.negate(ring);
                Element zn = two.multiply(a, ring);
                Element ch = mb.subtract(sD, ring);
                Element ch2 = mb.add(sD, ring);
                x1 = ch.divide(zn, ring);
                x2 = ch2.divide(zn, ring);
                X.add(x1);
                X.add(x2);
                K.add(one);
                K.add(one);
                break;

            }

//В комплексных числах

            case (-1): {
                if (a.equals(zero)) {
                    if (b.equals(zero)) {
                        //!!!
                        x1 = new F(NumberR.POSITIVE_INFINITY);
                        x2 = new F(NumberR.POSITIVE_INFINITY);
                        X.add(x1);
                        X.add(x2);
                        K.add(one);
                        K.add(one);
                        Res = new Factor(X, K);
                        return Res;
                        //        return new F[]{x1, x2};
                    } else {
                        if (c.equals(zero)) {
                            x1 = zero;
                            X.add(x1);
                            K.add(one);
                            Res = new Factor(X, K);
                            return Res;
                            //            return new F[]{x1, x2};
                        } else {
                            x1 = c.negate(ring).divide(b, ring);
                            X.add(x1);
                            K.add(one);
                            Res = new Factor(X, K);
                            return Res;
                            //            return new F[]{new F(x1.valueOf_ForR64(ring)), x2};
                        }
                    }
                }
                if (b.equals(zero)) {
                    if (c.equals(zero)) {
                        x1 = zero;
                        X.add(x1);
                        K.add(one);
                        Res = new Factor(X, K);
                        return Res;
                        //          return new F[]{x1, x2};
                    } else {
                        Element cDa = c.negate(ring).divide(a, ring);
                        Complex Cd = new Complex((NumberR64) cDa, ring);
                        x1 = new F(Cd.sqrt(ring));
                        x2 = x1.negate(ring);
                        X.add(x1);
                        X.add(x2);
                        K.add(one);
                        K.add(one);
                        Res = new Factor(X, K);
                        return Res;
                        //        return new F[]{new F(x1.valueOf_ForR64(ring)), new F(x1.valueOf_ForR64(ring))};
                    }
                }
                if (c.equals(zero)) {
                    x1 = b.negate(ring).divide(a, ring);
                    x2 = zero;
                    X.add(x1);
                    X.add(x2);
                    K.add(one);
                    K.add(one);
                    Res = new Factor(X, K);
                    return Res;
                    //       return new F[]{new F(x1.valueOf_ForR64(ring)), x2};

                } else {
                    Element bb = b.pow(2, ring);
                    Element ac4 = four.multiply(a, ring).multiply(c, ring);
                    Element Discr = bb.subtract(ac4, ring);
                    switch (Discr.compareTo(one.myZero(ring), ring)) {
                        case (1): {
                            Element sD = Discr.powTheFirst(1, 2, ring);
                            Element mb = b.negate(ring);
                            Element zn = two.multiply(a, ring);
                            Element ch = mb.subtract(sD, ring);
                            Element ch2 = mb.add(sD, ring);
                            x1 = ch.divide(zn, ring);
                            x2 = ch2.divide(zn, ring);
                            X.add(x1);
                            X.add(x2);
                            K.add(one);
                            K.add(one);
                            break;
                        }
                        case (0): {
                            x1 = (b.negate(ring)).divide(two.multiply(a, ring), ring);
                            X.add(x1);
                            K.add(two);
                            break;
                        }
                        case (-1): {
                            Complex Cd = new Complex((NumberR64) Discr, ring);
                            Complex CsD = Cd.sqrt(ring);

                            Element mb = b.negate(ring);
                            Complex Cmb = new Complex((NumberR64) mb, ring);

                            Complex Ctwo = new Complex((NumberR64) two, ring);
                            Complex Ca = new Complex((NumberR64) a, ring);
                            Element zn = Ctwo.multiply(Ca, ring);
                            Element ch = Cmb.subtract(CsD, ring);
                            Element ch2 = Cmb.add(CsD, ring);
                            Element Cx1 = ch.divide(zn, ring);
                            Element Cx2 = ch2.divide(zn, ring);
                            x1 = new F(Cx1);
                            x2 = new F(Cx2);
                            X.add((new F(x1)).X[0]);
                            X.add((new F(x2)).X[0]);
                            K.add(new NumberZ(1));
                            K.add(new NumberZ(1));
                            break;
                        }
                    }
                }
                break;
            }
        }

        Res = new Factor(X, K);
        return Res;
    }

    /**
     * решение кубического уравнения (F)
     *
     * @param (F) p -уравнение вида ax^3+bx^2+cx+d
     * @param (int)option 0 общий случай 1 действитеьные числа -1 комплексные
     * @return F[] массив корней уравнения. размер массива равен количеству
     * корней уравнения
     *
     */
    public static Factor solvAlgEq3ord(F p, int option, Ring ring) {
        Element th = NumberR.valueOf(3);
        Element tw = NumberR.valueOf(2);
        F two = new F(tw);
        F three = new F(th);
        F ts = new F(new NumberR64(27));
        Element ZERO = ring.numberZERO;
        F a = new F(ZERO);
        F b = new F(ZERO);
        F c = new F(ZERO);
        F d = new F(ZERO);
        F x1 = null;
        F x2 = null;
        F x3 = null;

        Factor Res = null;
        Vector<Element> K = new Vector<Element>();
        Vector<Element> X = new Vector<Element>();



        a = (F) ((F) (p.X[0])).X[0];
        b = (F) ((F) (p.X[1])).X[0];
        c = (F) ((F) (p.X[2])).X[0];
        d = (F) (p.X[3]);

        /*
         Приведем уравнение к каноническому виду. Делаем замену переменных, от переменной x переходим к переменной y через равентсво:
         x=y-b/3*a
         Получим новое уравнение от переменной y:
         y^3+p*y+q=0
         где:
         **/
        F p1 = new F(F.MULTIPLY, new F[]{b, b});
        F p2 = new F(F.MULTIPLY, new F[]{a, a});
        F p6 = new F(F.MULTIPLY, new F[]{three, p2});
        F p3 = new F(F.DIVIDE, new F[]{p1, p6});
        F p4 = (F) p3.negate(ring);
        F p5 = new F(F.DIVIDE, new F[]{c, a});
        F P = new F(F.ADD, new F[]{p4, p5});

        F q1 = new F(F.MULTIPLY, new F[]{b, b, b});
        F q2 = new F(F.MULTIPLY, new F[]{two, q1});
        F q3 = new F(F.MULTIPLY, new F[]{a, a, a});
        F q4 = new F(F.MULTIPLY, new F[]{ts, q3});
        F q5 = new F(F.DIVIDE, new F[]{q2, q4});
        F q6 = new F(F.MULTIPLY, new F[]{b, c});
        F q7 = new F(F.MULTIPLY, new F[]{a, a});
        F q8 = new F(F.MULTIPLY, new F[]{three, q7});
        F q9 = new F(F.DIVIDE, new F[]{q6, q8});
        F q10 = new F(F.DIVIDE, new F[]{d, a});
        F q11 = new F(F.SUBTRACT, new F[]{q5, q9});
        F q = new F(F.ADD, new F[]{q11, q10});
        q = new F(q);

        if (option != 0) {
            q = new F(q.valueOf(ring));
            P = new F(P.valueOf(ring));
        }

        //Определим еще одну переменную Q:
        F Q1 = new F(F.DIVIDE, new F[]{P, three});
        F Q2 = new F(F.MULTIPLY, new F[]{Q1, Q1, Q1});
        F Q3 = new F(F.DIVIDE, new F[]{q, two});
        F Q4 = new F(F.MULTIPLY, new F[]{Q3, Q3});
        F Q = new F(F.ADD, new F[]{Q2, Q4});
        Q = new F(Q);


        if (option == 1 && ((Q.valueOf(ring)).compareTo(NumberR64.ZERO, ring) == -1)) {
            x1 = new F(NumberR.NAN);
            X.add((new F(x1)).X[0]);
            K.add(new NumberZ(1));
            Res = new Factor(K, X);
            return Res;
        }
        /*
         Число действительных корней кубического уравнения зависит от знака Q:
         Q > 0 - один действительный корень и два сопряженных комплексных корня.
         Q < 0 - три действительных корня.
         Q = 0 - один однократный действительный корень и два двукратных, или,
         если p = q = 0, то один трехкратный действительный корень.
         **/

        F hq = new F((q.divide(two, ring)).negate(ring));
        F sQ = new F(F.SQRT, new F[]{Q});

        if (option == -1 && ((Q.valueOf(ring)).compareTo(NumberR64.ZERO, ring) == -1)) {
            Complex cq = new Complex((NumberR64) Q.valueOf(ring), ring);
            sQ = new F(cq.sqrt(ring));
        }

        F sA = new F(F.ADD, new F[]{hq, sQ});
        F sB = new F(F.SUBTRACT, new F[]{hq, sQ});

        F A = new F(F.CUBRT, new F[]{sA});
        F B = new F(F.CUBRT, new F[]{sB});
        if (option != 0) {
            A = new F(F.CUBRT, new F[]{new F(sA.valueOf(ring))});
            B = new F(F.CUBRT, new F[]{new F(sB.valueOf(ring))});
        } else {
            A = new F(F.CUBRT, new F[]{sA});
            B = new F(F.CUBRT, new F[]{sB});
        }


        Complex i = new Complex(0, 1);
        F I = new F(i);
        F tH = new F(three);
        F sqTH = new F(F.SQRT, new F[]{tH});
        F aAb = new F(F.ADD, new F[]{A, B});
        F aSb = new F(F.SUBTRACT, new F[]{A, B});
        F Two = new F(two);
        F Y1 = new F(F.DIVIDE, new F[]{aAb, Two});
        F ny1 = (F) Y1.negate(ring);
        F sy = new F(F.DIVIDE, new F[]{aSb, Two});
        F isy = new F(F.MULTIPLY, new F[]{I, sy, sqTH});
        F Y2 = new F(F.ADD, new F[]{ny1, isy});
        F Y3 = new F(F.SUBTRACT, new F[]{ny1, isy});


        Element t = b.divide(three.multiply(a, ring), ring);
        F T = new F(t);
        if (option != 0) {
            T = new F(T.valueOf(ring));
            x1 = new F(F.SUBTRACT, new F[]{Y1, T});
            x2 = new F(F.SUBTRACT, new F[]{Y2, T});
            x3 = new F(F.SUBTRACT, new F[]{Y3, T});
        } else {
            x1 = new F(F.SUBTRACT, new F[]{Y1, T});
            x2 = new F(F.SUBTRACT, new F[]{Y2, T});
            x3 = new F(F.SUBTRACT, new F[]{Y3, T});
        }
        X.add((new F(x1)).X[0]);
        X.add((new F(x2)).X[0]);
        X.add((new F(x3)).X[0]);
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        Res = new Factor(K, X);
        return Res;
    }

    /**
     * решение кубического уравнения (Polynom)
     *
     * @param (Polynom) p -уравнение вида ax^3+bx^2+cx+d
     * @param (int)option 0 общий случай 1 действитеьные числа -1 комплексные
     * @return F[] массив корней уравнения. размер массива равен количеству
     * корней уравнения
     *
     */
    public static Factor solvAlgEq3ord(Polynom p, int option, Ring ring) {
        Element th = NumberR.valueOf(3);
        Element tw = NumberR.valueOf(2);
        Element two = tw;
        Element three = th;
        Element ts = new NumberR64(27);

        Element a = ring.numberZERO;
        Element b = ring.numberZERO;
        Element c = ring.numberZERO;
        Element d = ring.numberZERO;
        Element x1 = null;
        Element x2 = null;
        Element x3 = null;

        Factor Res = null;
        Vector<Element> K = new Vector<Element>();
        Vector<Element> X = new Vector<Element>();

        for (int i = 0; i < p.coeffs.length; i++) {
            switch (p.powers[i]) {
                case (3):
                    a = p.coeffs[i];
                    break;
                case (2):
                    b = p.coeffs[i];
                    break;
                case (1):
                    c = p.coeffs[i];
                    break;
                case (0):
                    d = p.coeffs[i];
                    break;
            }
        }

        /*
         Приведем уравнение к каноническому виду. Делаем замену переменных, от переменной x переходим к переменной y через равентсво:
         x=y-b/3*a
         Получим новое уравнение от переменной y:
         y^3+p*y+q=0
         где:
         **/
        F p1 = new F(F.MULTIPLY, new Element[]{b, b});
        F p2 = new F(F.MULTIPLY, new Element[]{a, a});
        F p6 = new F(F.MULTIPLY, new Element[]{three, p2});
        F p3 = new F(F.DIVIDE, new Element[]{p1, p6});
        F p4 = (F) p3.negate(ring);
        F p5 = new F(F.DIVIDE, new Element[]{c, a});
        Element P = new F(F.ADD, new Element[]{p4, p5});

        F q1 = new F(F.MULTIPLY, new Element[]{b, b, b});
        F q2 = new F(F.MULTIPLY, new Element[]{two, q1});
        F q3 = new F(F.MULTIPLY, new Element[]{a, a, a});
        F q4 = new F(F.MULTIPLY, new Element[]{ts, q3});
        F q5 = new F(F.DIVIDE, new Element[]{q2, q4});
        F q6 = new F(F.MULTIPLY, new Element[]{b, c});
        F q7 = new F(F.MULTIPLY, new Element[]{a, a});
        F q8 = new F(F.MULTIPLY, new Element[]{three, q7});
        F q9 = new F(F.DIVIDE, new Element[]{q6, q8});
        F q10 = new F(F.DIVIDE, new Element[]{d, a});
        F q11 = new F(F.SUBTRACT, new Element[]{q5, q9});
        Element q = new F(F.ADD, new Element[]{q11, q10});

        if (option != 0) {
            q = ((F) q).valueOf(ring);
            P = ((F) P).valueOf(ring);
        }

        //Определим еще одну переменную Q:
        F Q1 = new F(F.DIVIDE, new Element[]{P, three});
        F Q2 = new F(F.MULTIPLY, new Element[]{Q1, Q1, Q1});
        F Q3 = new F(F.DIVIDE, new Element[]{q, two});
        F Q4 = new F(F.MULTIPLY, new Element[]{Q3, Q3});
        F Q = new F(F.ADD, new Element[]{Q2, Q4});

        if (option == 1 & ((Q.valueOf(ring)).compareTo(NumberR64.ZERO, ring) == -1)) {
            x1 = new F(NumberR.NAN);
            X.add((new F(x1)).X[0]);
            K.add(new NumberZ(1));
            Res = new Factor(X, K);
            return Res;
        }

        /*
         Число действительных корней кубического уравнения зависит от знака Q:
         Q > 0 - один действительный корень и два сопряженных комплексных корня.
         Q < 0 - три действительных корня.
         Q = 0 - один однократный действительный корень и два двукратных, или,
         если p = q = 0, то один трехкратный действительный корень.
         **/

        F hq = new F((q.divide(two, ring)).negate(ring));
        F sQ = new F(F.SQRT, new Element[]{Q});

        if (option == -1 & ((Q.valueOf(ring)).compareTo(NumberR64.ZERO, ring) == -1)) {
            Complex cq = new Complex((NumberR64) Q.valueOf(ring), ring);
            sQ = new F(cq.sqrt(ring));
        }

        F sA = new F(F.ADD, new Element[]{hq, sQ});
        F sB = new F(F.SUBTRACT, new Element[]{hq, sQ});

        F A = new F(F.CUBRT, new Element[]{sA});
        F B = new F(F.CUBRT, new Element[]{sB});
        if (option != 0) {
            A = new F(F.CUBRT, new Element[]{sA.valueOf(ring)});
            B = new F(F.CUBRT, new Element[]{sB.valueOf(ring)});
        } else {
            A = new F(F.CUBRT, new Element[]{sA});
            B = new F(F.CUBRT, new Element[]{sB});
        }

        Complex i = new Complex(0, 1);
        F I = new F(i);
        F tH = new F(three);
        F sqTH = new F(F.SQRT, new Element[]{tH});
        F aAb = new F(F.ADD, new Element[]{A, B});
        F aSb = new F(F.SUBTRACT, new Element[]{A, B});
        F Two = new F(two);
        F Y1 = new F(F.DIVIDE, new Element[]{aAb, Two});
        F ny1 = (F) Y1.negate(ring);
        F sy = new F(F.DIVIDE, new Element[]{aSb, Two});
        F isy = new F(F.MULTIPLY, new Element[]{I, sy, sqTH});
        F Y2 = new F(F.ADD, new Element[]{ny1, isy});
        F Y3 = new F(F.SUBTRACT, new Element[]{ny1, isy});

        Element t = b.divide(three.multiply(a, ring), ring);
        F T = new F(t);
        if (option != 0) {
            T = new F(T.valueOf(ring));
            x1 = new F(F.SUBTRACT, new Element[]{Y1, T});
            x2 = new F(F.SUBTRACT, new Element[]{Y2, T});
            x3 = new F(F.SUBTRACT, new Element[]{Y3, T});
        } else {
            x1 = new F(F.SUBTRACT, new Element[]{Y1, T});
            x2 = new F(F.SUBTRACT, new Element[]{Y2, T});
            x3 = new F(F.SUBTRACT, new Element[]{Y3, T});
        }

        X.add((new F(x1)).X[0]);
        X.add((new F(x2)).X[0]);
        X.add((new F(x3)).X[0]);
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        Res = new Factor(X, K);
        return Res;
    }

    /**
     * решение возвратного уравнения (F)
     *
     * @param (F) p -уравнение вида ax^4+bx^3+cx^2+bx+a
     * @param (int)option 0 общий случай 1 действитеьные числа -1 комплексные
     * @return F[] массив корней уравнения. размер массива равен количеству
     * корней уравнения
     *
     */
    public static Factor solvAlgEq4Ret(F p, int option, Ring ring) {
        F one = new F(new NumberR64(1));
        F two = new F(new NumberR64(2));
        F a = new F(new NumberR64(0));
        F b = new F(new NumberR64(0));
        F c = new F(new NumberR64(0));
        F x1 = null;
        F x2 = null;
        F x3 = null;
        F x4 = null;

        Factor Res = null;
        Vector<Element> K = new Vector<Element>();
        Vector<Element> X = new Vector<Element>();

        a = (F) ((F) (p.X[0])).X[0];
        b = (F) ((F) (p.X[1])).X[0];
        c = (F) ((F) (p.X[2])).X[0];
        Polynom y = new Polynom("y", ring);
        F yf = new F(y);
        F yp = new F(F.POW, new F[]{yf, new F(new NumberR64(2))});
        F ayf = new F(F.MULTIPLY, new F[]{a, yp});
        F byf = new F(F.MULTIPLY, new F[]{b, yf});
        F ta = new F(F.MULTIPLY, new F[]{two, a});
        F cta = new F(F.SUBTRACT, new F[]{c, ta});
        if (option != 0) {
            cta = new F(cta.valueOf(ring));
        }
        F g = new F(F.ADD, new F[]{ayf, byf, cta});

        Factor FE = FQ.solvAlgEq2ord(g, option, ring);

        Element g1 = FE.multin.get(0);
        Element g2 = FE.multin.get(1);
        F y1 = new F(g1);
        F y2 = new F(g2);



        Polynom x = new Polynom("x", ring);
        F xx = new F(x);
        F xp = new F(F.POW, new F[]{xx, new F(new NumberR64(2))});
        F axf = new F(F.MULTIPLY, new F[]{one, xp});
        F bx1 = new F(F.MULTIPLY, new F[]{(F) y1.negate(ring), xx});
        F bx2 = new F(F.MULTIPLY, new F[]{(F) y2.negate(ring), xx});

        if (option != 0) {
            bx1 = new F(F.MULTIPLY, new F[]{new F(((F) y1.negate(ring)).valueOf(ring)), xx});
            bx2 = new F(F.MULTIPLY, new F[]{new F(((F) y2.negate(ring)).valueOf(ring)), xx});

        }
        F h1 = new F(F.ADD, new F[]{axf, bx1, one});
        F h2 = new F(F.ADD, new F[]{axf, bx2, one});

        Factor FE1 = FQ.solvAlgEq2ord(h1, option, ring);
        Factor FE2 = FQ.solvAlgEq2ord(h2, option, ring);

        Element u1 = FE1.multin.get(0);
        Element u2 = FE1.multin.get(1);
        Element u3 = FE2.multin.get(0);
        Element u4 = FE2.multin.get(1);
        x1 = new F(u1);
        x2 = new F(u2);
        x3 = new F(u3);
        x4 = new F(u4);

        X.add((new F(x1)).X[0]);
        X.add((new F(x2)).X[0]);
        X.add((new F(x3)).X[0]);
        X.add((new F(x4)).X[0]);
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        Res = new Factor(K, X);
        return Res;
    }

    /**
     * решение возвратного уравнения (Polynom)
     *
     * @param (Polynom) p -уравнение вида ax^4+bx^3+cx^2+bx+a
     * @param (int)option 0 общий случай 1 действитеьные числа -1 комплексные
     * @return F[] массив корней уравнения. размер массива равен количеству
     * корней уравнения
     *
     */
    public static Factor solvAlgEq4Ret(Polynom p, int option, Ring ring) {
        F one = new F(new NumberR64(1));
        F two = new F(new NumberR64(2));
        F a = new F(new NumberR64(0));
        F b = new F(new NumberR64(0));
        F c = new F(new NumberR64(0));
        F x1 = null;
        F x2 = null;
        F x3 = null;
        F x4 = null;
        Factor Res = null;
        Vector<Element> K = new Vector<Element>();
        Vector<Element> X = new Vector<Element>();

        for (int i = 0; i < p.coeffs.length; i++) {
            switch (p.powers[i]) {
                case (4):
                    a = new F(p.coeffs[i]);
                    break;
                case (3):
                    b = new F(p.coeffs[i]);
                    break;
                case (2):
                    c = new F(p.coeffs[i]);
                    break;
                default:
                    break;
            }
        }

        Polynom y = new Polynom("y", ring);
        F yf = new F(y);
        F yp = new F(F.POW, new F[]{yf, new F(new NumberR64(2))});
        F ayf = new F(F.MULTIPLY, new F[]{a, yp});
        F byf = new F(F.MULTIPLY, new F[]{b, yf});
        F ta = new F(F.MULTIPLY, new F[]{two, a});
        F cta = new F(F.SUBTRACT, new F[]{c, ta});
        if (option != 0) {
            cta = new F(cta.valueOf(ring));
        }
        F g = new F(F.ADD, new F[]{ayf, byf, cta});

        Factor FE = FQ.solvAlgEq2ord(g, option, ring);

        Element g1 = FE.multin.get(0);
        Element g2 = FE.multin.get(1);
        F y1 = new F(g1);
        F y2 = new F(g2);



        Polynom x = new Polynom("x", ring);
        F xx = new F(x);
        F xp = new F(F.POW, new F[]{xx, new F(new NumberR64(2))});
        F axf = new F(F.MULTIPLY, new F[]{one, xp});
        F bx1 = new F(F.MULTIPLY, new F[]{(F) y1.negate(ring), xx});
        F bx2 = new F(F.MULTIPLY, new F[]{(F) y2.negate(ring), xx});

        if (option != 0) {
            bx1 = new F(F.MULTIPLY, new F[]{new F(((F) y1.negate(ring)).valueOf(ring)), xx});
            bx2 = new F(F.MULTIPLY, new F[]{new F(((F) y2.negate(ring)).valueOf(ring)), xx});

        }
        F h1 = new F(F.ADD, new F[]{axf, bx1, one});
        F h2 = new F(F.ADD, new F[]{axf, bx2, one});

        Factor FE1 = FQ.solvAlgEq2ord(h1, option, ring);
        Factor FE2 = FQ.solvAlgEq2ord(h2, option, ring);

        Element u1 = FE1.multin.get(0);
        Element u2 = FE1.multin.get(1);
        Element u3 = FE2.multin.get(0);
        Element u4 = FE2.multin.get(1);
        x1 = new F(u1);
        x2 = new F(u2);
        x3 = new F(u3);
        x4 = new F(u4);

        X.add((new F(x1)).X[0]);
        X.add((new F(x2)).X[0]);
        X.add((new F(x3)).X[0]);
        X.add((new F(x4)).X[0]);
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        K.add(new NumberZ(1));
        Res = new Factor(X, K);
        return Res;
    }

    /**
     * решение уравнения 4й степени методом Феррари(F)
     *
     * @param (F) p -уравнение вида ax^4+bx^3+cx^2+dx+e
     * @param (int)option 0 общий случай 1 действитеьные числа -1 комплексные
     * @return F[] массив корней уравнения. размер массива равен количеству
     * корней уравнения
     *
     */
    public static Factor solvAlgEq4(F p, int option, Ring ring) {
        F two = new F(new NumberR64(2));
        F three = new F(new NumberR64(3));
        F four = new F(new NumberR64(4));
        F zero = new F(new NumberR64(0));
        F et = new F(new NumberR64(8));
        F a = zero;
        F b = zero;
        F c = zero;
        F d = zero;
        F e = zero;
        F x1 = null;
        F x2 = null;
        F x3 = null;
        F x4 = null;
        Factor Res = null;
        Vector<Element> Ko = new Vector<Element>();
        Vector<Element> Xo = new Vector<Element>();

        int p1 = 0, p2 = 0, p3 = 0, p4 = 0;

        a = (F) ((F) (p.X[0])).X[0];
        b = (F) ((F) (p.X[1])).X[0];
        c = (F) ((F) (p.X[2])).X[0];
        d = (F) ((F) (p.X[3])).X[0];
        e = (F) (p.X[4]);

        if ((b.equals(d)) && (a.equals(e))) {
            return FQ.solvAlgEq4Ret(p, option, ring);
        }
        if ((p4 == 1) && (p3 == 0) && (p2 == 1) && (p1 == 0)) {
            return FQ.solvAlgEq4Bi(p, option, ring);
        }

        F ap2 = new F(F.POW, new F[]{a, two});
        F ap3 = new F(F.POW, new F[]{a, three});
        F ap4 = new F(F.POW, new F[]{a, four});
        F bp2 = new F(F.POW, new F[]{b, two});
        F bp3 = new F(F.POW, new F[]{b, three});
        F bp4 = new F(F.POW, new F[]{b, four});

        F m1 = new F(F.MULTIPLY, new F[]{bp2, three});
        F m2 = new F(F.MULTIPLY, new F[]{ap2, et});
        F m3 = new F(F.MULTIPLY, new F[]{m1, m2});
        F mbda = (F) m3.negate(ring);
        F d1 = new F(F.DIVIDE, new F[]{c, a});

        F A = new F(F.ADD, new F[]{mbda, d1});
        if (option != 0) {
            A = new F(A.valueOf(ring));
        }

        F m4 = new F(F.MULTIPLY, new F[]{ap3, et});
        F d2 = new F(F.DIVIDE, new F[]{bp3, m4});
        F m5 = new F(F.MULTIPLY, new F[]{b, c});
        F m6 = new F(F.MULTIPLY, new F[]{ap2, two});
        F d3 = new F(F.DIVIDE, new F[]{m5, m6});
        F d4 = new F(F.DIVIDE, new F[]{d, a});

        F B = new F(F.ADD, new F[]{d2, (F) d3.negate(ring), d4});
        if (option != 0) {
            B = new F(A.valueOf(ring));
        }

        F m7 = new F(F.MULTIPLY, new Element[]{bp4, three.negate(ring)});
        F m8 = new F(F.MULTIPLY, new Element[]{ap4, new F(new NumberR64(256))});
        F d5 = new F(F.DIVIDE, new Element[]{m7, m8});
        F m9 = new F(F.MULTIPLY, new Element[]{c, bp2});
        F m10 = new F(F.MULTIPLY, new Element[]{ap3, new F(new NumberR64(16))});
        F d6 = new F(F.DIVIDE, new Element[]{m9, m10});
        F m11 = new F(F.MULTIPLY, new Element[]{b, d});
        F m12 = new F(F.MULTIPLY, new Element[]{ap2, new F(new NumberR64(4))});
        F d7 = new F(F.DIVIDE, new Element[]{m11, m12});
        F d8 = new F(F.DIVIDE, new Element[]{e, a});

        F G = new F(F.ADD, new Element[]{d5, d6, d7.negate(ring), d8});
        if (option != 0) {
            G = new F(A.valueOf(ring));
        }

        F Ap2 = new F(F.POW, new Element[]{A, two});
        F d9 = new F(F.DIVIDE, new Element[]{Ap2, new F(new NumberR64(12))});

        F P = new F(F.SUBTRACT, new Element[]{d9.negate(ring), G});
        if (option != 0) {
            P = new F(A.valueOf(ring));
        }

        F Ap3 = new F(F.POW, new Element[]{A, three});
        F d10 = new F(F.DIVIDE, new Element[]{Ap3, new F(new NumberR64(108))});
        F m13 = new F(F.MULTIPLY, new Element[]{A, G});
        F d11 = new F(F.DIVIDE, new Element[]{m13, three});
        F Bp2 = new F(F.POW, new Element[]{A, two});
        F d12 = new F(F.DIVIDE, new Element[]{Bp2, new F(new NumberR64(8))});

        F Q = new F(F.ADD, new Element[]{d10.negate(ring), d11, d12.negate(ring)});
        if (option != 0) {
            Q = new F(Q.valueOf(ring));
        }

        F d13 = new F(F.DIVIDE, new Element[]{Q, two});
        F Qp2 = new F(F.POW, new Element[]{Q, two});
        F d14 = new F(F.DIVIDE, new Element[]{Qp2, four});
        F Pp3 = new F(F.POW, new Element[]{P, three});
        F d15 = new F(F.DIVIDE, new Element[]{Pp3, new F(new NumberR64(27))});
        F a1 = new F(F.ADD, new Element[]{d14, d15});
        F sq1 = null;
        F R = null;

        switch (option) {
            case (1): {
                a1 = new F(a1.valueOf(ring));
                if (a1.compareTo(zero, ring) != -1) {
                    sq1 = new F(F.SQRT, new Element[]{a1});
                    R = new F(F.ADD, new Element[]{d13.negate(ring), sq1});
                    R = new F(R.valueOf(ring));
                } else {
                    R = null;
                }
                break;
            }
            case (0): {
                sq1 = new F(F.SQRT, new Element[]{a1});
                R = new F(F.ADD, new Element[]{d13.negate(ring), sq1});
                break;
            }
            case (-1): {
                a1 = new F(a1.valueOf(ring));
                if (a1.compareTo(zero, ring) != -1) {
                    sq1 = new F(F.SQRT, new Element[]{new F(a1)});
                    R = new F(F.ADD, new Element[]{d13.negate(ring), sq1});
                    R = new F(R.valueOf(ring));
                } else {
                    Complex aa = new Complex((NumberR64) a1.X[0], ring);
                    sq1 = new F(F.SQRT, new Element[]{new F(aa)});
                    R = new F(F.ADD, new Element[]{d13.negate(ring), sq1});
                    R = new F(R.valueOf(ring));
                }
                break;
            }
        }
        F U = new F(F.CUBRT, new F[]{R});
        if (option != 0) {
            if (R.equals(zero)) {
                U = new F(F.CUBRT, new F(Q));
                U = (F) U.negate(ring);
            } else {
                U = new F(F.CUBRT, new Element[]{R});
                F U3 = new F(F.MULTIPLY, new Element[]{three, U});
                U = new F(F.DIVIDE, new Element[]{P.negate(ring), U3});
            }

        } else {
            U = new F(F.CUBRT, new Element[]{R});
        }

        F d16 = new F(F.DIVIDE, new Element[]{new F(new NumberR64(5)), new F(new NumberR64(6))});
        F m14 = new F(F.MULTIPLY, new Element[]{d16.negate(ring), A});
        F m15 = new F(F.MULTIPLY, new Element[]{U, three});
        F d17 = new F(F.DIVIDE, new Element[]{P.negate(ring), m15});
        F y = null;
        if (option != 0) {
            y = new F(F.ADD, new Element[]{new F(m14.valueOf(ring)), U, d17});
        } else {
            y = new F(F.ADD, new Element[]{m14, U, d17});
        }

        F m16 = new F(F.MULTIPLY, new Element[]{y, two});
        F a2 = new F(F.ADD, new Element[]{A, m16});
        F W = new F(F.SQRT, new Element[]{a2});


        F m17 = new F(F.MULTIPLY, new Element[]{A, four});
        F K = new F(F.DIVIDE, new Element[]{B.negate(ring), m17});

        F m18 = new F(F.MULTIPLY, new Element[]{A, three});
        F m19 = new F(F.MULTIPLY, new Element[]{y, two});
        F m20 = new F(F.MULTIPLY, new Element[]{B, two});
        F d19 = new F(F.DIVIDE, new Element[]{m20, W});

        F sa1 = new F(F.ADD, new Element[]{m18, m19, d19});
        F SQRT1 = new F(F.ADD, new Element[]{sa1.negate(ring)});
        F sa2 = new F(F.ADD, new Element[]{m18, m19, d19.negate(ring)});
        F SQRT2 = new F(F.ADD, new Element[]{sa2.negate(ring)});

        F CH1 = new F(F.ADD, new Element[]{W, SQRT1});
        F CH2 = new F(F.ADD, new Element[]{W.negate(ring), SQRT2});
        F CH3 = new F(F.SUBTRACT, new Element[]{W, SQRT1});
        F CH4 = new F(F.SUBTRACT, new Element[]{W.negate(ring), SQRT2});

        F K1 = new F(F.DIVIDE, new Element[]{CH1, two});
        F K2 = new F(F.DIVIDE, new Element[]{CH2, two});
        F K3 = new F(F.DIVIDE, new Element[]{CH3, two});
        F K4 = new F(F.DIVIDE, new Element[]{CH4, two});


        switch (option) {
            case (1): {
                x1 = new F(F.ADD, new Element[]{new F(K.valueOf(ring)), K1});
                x2 = new F(F.ADD, new Element[]{new F(K.valueOf(ring)), K2});
                x3 = new F(F.ADD, new Element[]{new F(K.valueOf(ring)), K3});
                x4 = new F(F.ADD, new Element[]{new F(K.valueOf(ring)), K4});
                break;
            }
            case (0): {
                x1 = new F(F.ADD, new Element[]{K, K1});
                x2 = new F(F.ADD, new Element[]{K, K2});
                x3 = new F(F.ADD, new Element[]{K, K3});
                x4 = new F(F.ADD, new Element[]{K, K4});
                break;
            }
            case (-1): {
                x1 = new F(F.ADD, new Element[]{new F(K.valueOf(ring)), K1});
                x2 = new F(F.ADD, new Element[]{new F(K.valueOf(ring)), K2});
                x3 = new F(F.ADD, new Element[]{new F(K.valueOf(ring)), K3});
                x4 = new F(F.ADD, new Element[]{new F(K.valueOf(ring)), K4});
                break;
            }

        }
        Xo.add((new F(x1)).X[0]);
        Xo.add((new F(x2)).X[0]);
        Xo.add((new F(x3)).X[0]);
        Xo.add((new F(x4)).X[0]);
        Ko.add(new NumberZ(1));
        Ko.add(new NumberZ(1));
        Ko.add(new NumberZ(1));
        Ko.add(new NumberZ(1));
        Res = new Factor(Ko, Xo);
        return Res;
    }

    /**
     * решение уравнения 4й степени методом Феррари(Polynom)
     *
     * @param (Polynom) p -уравнение вида ax^4+bx^3+cx^2+dx+e
     * @param (int)option 0 общий случай 1 действитеьные числа -1 комплексные
     * @return F[] массив корней уравнения. размер массива равен количеству
     * корней уравнения
     *
     */
    public static Factor solvAlgEq4(Polynom p, int option, Ring ring) {
        F two = new F(new NumberR64(2));
        F three = new F(new NumberR64(3));
        F four = new F(new NumberR64(4));
        F zero = new F(new NumberR64(0));
        F et = new F(new NumberR64(8));
        //коэффициенты нашего уравнения при Х
        Element a = zero;
        Element b = zero;
        Element c = zero;
        Element d = zero;
        Element e = zero;
        // корни
        Element x1 = null;
        Element x2 = null;
        Element x3 = null;
        Element x4 = null;
        // результат
        Factor Res = null;
        Vector<Element> Ko = new Vector<Element>();
        Vector<Element> Xo = new Vector<Element>();

        int p1 = 0, p2 = 0, p3 = 0, p4 = 0;

        for (int i = 0; i < p.coeffs.length; i++) {
            switch (p.powers[i]) {
                case (4): {
                    p4 = 1;
                    a = p.coeffs[i];
                    break;
                }
                case (3): {
                    p3 = 1;
                    b = p.coeffs[i];
                    break;
                }
                case (2): {
                    p2 = 1;
                    c = p.coeffs[i];
                    break;
                }
                case (1): {
                    p1 = 1;
                    d = p.coeffs[i];
                    break;
                }
                case (0): {
                    e = p.coeffs[i];
                    break;
                }
                default:
                    System.err.println("ERROR");
            }
        }
        //Проверка типа уравнения. Если оно возвратное или биквадратное,
        // то используется соответствующий метод
        if ((b.equals(d)) && (a.equals(e))) {
            return FQ.solvAlgEq4Ret(p, option, ring);
        }
        if ((p4 == 1) && (p3 == 0) && (p2 == 1) && (p1 == 0)) {
            return FQ.solvAlgEq4Bi(p, option, ring);
        }

        F ap2 = new F(F.POW, new Element[]{a, two});
        F ap3 = new F(F.POW, new Element[]{a, three});
        F ap4 = new F(F.POW, new Element[]{a, four});
        F bp2 = new F(F.POW, new Element[]{b, two});
        F bp3 = new F(F.POW, new Element[]{b, three});
        F bp4 = new F(F.POW, new Element[]{b, four});

        F m1 = new F(F.MULTIPLY, new Element[]{bp2, three});
        F m2 = new F(F.MULTIPLY, new Element[]{ap2, et});
        F m3 = new F(F.MULTIPLY, new Element[]{m1, m2});
        F mbda = (F) m3.negate(ring);
        F d1 = new F(F.DIVIDE, new Element[]{c, a});

        F A = new F(F.ADD, new Element[]{mbda, d1});
        if (option != 0) {
            A = new F(A.valueOf(ring));
        }

        F m4 = new F(F.MULTIPLY, new Element[]{ap3, et});
        F d2 = new F(F.DIVIDE, new Element[]{bp3, m4});
        F m5 = new F(F.MULTIPLY, new Element[]{b, c});
        F m6 = new F(F.MULTIPLY, new Element[]{ap2, two});
        F d3 = new F(F.DIVIDE, new Element[]{m5, m6});
        F d4 = new F(F.DIVIDE, new Element[]{d, a});

        F B = new F(F.ADD, new Element[]{d2, d3.negate(ring), d4});
        if (option != 0) {
            B = new F(A.valueOf(ring));
        }

        F m7 = new F(F.MULTIPLY, new Element[]{bp4, (F) three.negate(ring)});
        F m8 = new F(F.MULTIPLY, new Element[]{ap4, new F(new NumberR64(256))});
        F d5 = new F(F.DIVIDE, new Element[]{m7, m8});
        F m9 = new F(F.MULTIPLY, new Element[]{c, bp2});
        F m10 = new F(F.MULTIPLY, new Element[]{ap3, new F(new NumberR64(16))});
        F d6 = new F(F.DIVIDE, new Element[]{m9, m10});
        F m11 = new F(F.MULTIPLY, new Element[]{b, d});
        F m12 = new F(F.MULTIPLY, new Element[]{ap2, new F(new NumberR64(4))});
        F d7 = new F(F.DIVIDE, new Element[]{m11, m12});
        F d8 = new F(F.DIVIDE, new Element[]{e, a});

        F G = new F(F.ADD, new Element[]{d5, d6, d7.negate(ring), d8});
        if (option != 0) {
            G = new F(A.valueOf(ring));
        }

        F Ap2 = new F(F.POW, new Element[]{A, two});
        F d9 = new F(F.DIVIDE, new Element[]{Ap2, new F(new NumberR64(12))});

        F P = new F(F.SUBTRACT, new Element[]{d9.negate(ring), G});
        if (option != 0) {
            P = new F(A.valueOf(ring));
        }

        F Ap3 = new F(F.POW, new Element[]{A, three});
        F d10 = new F(F.DIVIDE, new Element[]{Ap3, new F(new NumberR64(108))});
        F m13 = new F(F.MULTIPLY, new Element[]{A, G});
        F d11 = new F(F.DIVIDE, new Element[]{m13, three});
        F Bp2 = new F(F.POW, new Element[]{A, two});
        F d12 = new F(F.DIVIDE, new Element[]{Bp2, new F(new NumberR64(8))});

        F Q = new F(F.ADD, new Element[]{d10.negate(ring), d11, d12.negate(ring)});
        if (option != 0) {
            Q = new F(Q.valueOf(ring));
        }

        F d13 = new F(F.DIVIDE, new Element[]{Q, two});
        F Qp2 = new F(F.POW, new Element[]{Q, two});
        F d14 = new F(F.DIVIDE, new Element[]{Qp2, four});
        F Pp3 = new F(F.POW, new Element[]{P, three});
        F d15 = new F(F.DIVIDE, new Element[]{Pp3, new F(new NumberR64(27))});
        F a1 = new F(F.ADD, new Element[]{d14, d15});
        F sq1 = null;
        F R = null;

        switch (option) {
            case (1): {
                a1 = new F(a1.valueOf(ring));
                if (a1.compareTo(zero, ring) != -1) {
                    sq1 = new F(F.SQRT, new Element[]{new F(a1)});
                    R = new F(F.ADD, new Element[]{d13.negate(ring), sq1});
                    R = new F(R.valueOf(ring));
                } else {
                    R = null;
                }
                break;
            }
            case (0): {
                sq1 = new F(F.SQRT, new Element[]{a1});
                R = new F(F.ADD, new Element[]{d13.negate(ring), sq1});
                break;
            }
            case (-1): {
                a1 = new F(a1.valueOf(ring));
                if (a1.compareTo(zero, ring) != -1) {
                    sq1 = new F(F.SQRT, new Element[]{a1});
                    R = new F(F.ADD, new Element[]{d13.negate(ring), sq1});
                    R = new F(R.valueOf(ring));
                } else {
                    Complex aa = new Complex((NumberR64) a1.X[0], ring);
                    sq1 = new F(F.SQRT, new Element[]{aa});
                    R = new F(F.ADD, new Element[]{d13.negate(ring), sq1});
                    R = new F(R.valueOf(ring));
                }
                break;
            }
        }
        F U = new F(F.CUBRT, new Element[]{R});
        if (option != 0) {
            if (R.equals(zero)) {
                U = new F(F.CUBRT, new F(Q));
                U = (F) U.negate(ring);
            } else {
                U = new F(F.CUBRT, new Element[]{R});
                F U3 = new F(F.MULTIPLY, new Element[]{three, U});
                U = new F(F.DIVIDE, new Element[]{P.negate(ring), U3});
            }

        } else {
            U = new F(F.CUBRT, new Element[]{R});
        }

        F d16 = new F(F.DIVIDE, new Element[]{new NumberR64(5), new NumberR64(6)});
        F m14 = new F(F.MULTIPLY, new Element[]{d16.negate(ring), A});
        F m15 = new F(F.MULTIPLY, new Element[]{U, three});
        F d17 = new F(F.DIVIDE, new Element[]{P.negate(ring), m15});
        F y = null;
        if (option != 0) {
            y = new F(F.ADD, new Element[]{m14.valueOf(ring), U, d17});
        } else {
            y = new F(F.ADD, new Element[]{m14, U, d17});
        }

        F m16 = new F(F.MULTIPLY, new Element[]{y, two});
        F a2 = new F(F.ADD, new Element[]{A, m16});
        F W = new F(F.SQRT, new Element[]{a2});

        F m17 = new F(F.MULTIPLY, new Element[]{a, four});
        F K = new F(F.DIVIDE, new Element[]{b.negate(ring), m17});

        F m18 = new F(F.MULTIPLY, new Element[]{A, three});
        F m19 = new F(F.MULTIPLY, new Element[]{y, two});
        F m20 = new F(F.MULTIPLY, new Element[]{B, two});
        F d19 = new F(F.DIVIDE, new Element[]{m20, W});

        F sa1 = new F(F.ADD, new Element[]{m18, m19, d19});
        F SQRT1 = new F(F.ADD, new Element[]{sa1.negate(ring)});
        F sa2 = new F(F.ADD, new Element[]{m18, m19, d19.negate(ring)});
        F SQRT2 = new F(F.ADD, new Element[]{sa2.negate(ring)});

        F CH1 = new F(F.ADD, new Element[]{W, SQRT1});
        F CH2 = new F(F.ADD, new Element[]{W.negate(ring), SQRT2});
        F CH3 = new F(F.SUBTRACT, new Element[]{W, SQRT1});
        F CH4 = new F(F.SUBTRACT, new Element[]{W.negate(ring), SQRT2});

        F K1 = new F(F.DIVIDE, new Element[]{CH1, two});
        F K2 = new F(F.DIVIDE, new Element[]{CH2, two});
        F K3 = new F(F.DIVIDE, new Element[]{CH3, two});
        F K4 = new F(F.DIVIDE, new Element[]{CH4, two});


        switch (option) {
            case (1): {
                x1 = new F(F.ADD, new Element[]{K.valueOf(ring), K1});
                x2 = new F(F.ADD, new Element[]{K.valueOf(ring), K2});
                x3 = new F(F.ADD, new Element[]{K.valueOf(ring), K3});
                x4 = new F(F.ADD, new Element[]{K.valueOf(ring), K4});
                break;
            }
            case (0): {
                x1 = new F(F.ADD, new Element[]{K, K1});
                x2 = new F(F.ADD, new Element[]{K, K2});
                x3 = new F(F.ADD, new Element[]{K, K3});
                x4 = new F(F.ADD, new Element[]{K, K4});
                break;
            }
            case (-1): {
                x1 = new F(F.ADD, new Element[]{K.valueOf(ring), K1});
                x2 = new F(F.ADD, new Element[]{K.valueOf(ring), K2});
                x3 = new F(F.ADD, new Element[]{K.valueOf(ring), K3});
                x4 = new F(F.ADD, new Element[]{K.valueOf(ring), K4});
                break;
            }
        }
        Xo.add((new F(x1)).X[0]);
        Xo.add((new F(x2)).X[0]);
        Xo.add((new F(x3)).X[0]);
        Xo.add((new F(x4)).X[0]);
        Ko.add(new NumberZ(1));
        Ko.add(new NumberZ(1));
        Ko.add(new NumberZ(1));
        Ko.add(new NumberZ(1));
        Res = new Factor(Xo, Ko);
        return Res;
    }
}
