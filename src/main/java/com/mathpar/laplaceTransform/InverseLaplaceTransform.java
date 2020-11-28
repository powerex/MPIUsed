/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.laplaceTransform;

import com.mathpar.func.*;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 * Класс для обратного преобразования Лапласа
 *
 * @author Rybakov Michail
 */
public class InverseLaplaceTransform {

    /**
     * Функция для преобразования
     */
    private F inputF;

    public InverseLaplaceTransform() {
    }

    /**
     * Конструктор
     *
     * @param f - Функция для преобразования
     */
    public InverseLaplaceTransform(F f) {
        this.inputF = f;
    }

    /**
     * На входе функция вида: k/(ax+b)
     *
     * @param f
     * @return
     */
    public F inverse_exp() {
        Ring ring = new Ring("C64[t]");
        F res = new F();
        if (inputF.name == F.DIVIDE) {
            if (((inputF.X[0] instanceof Polynom) || (inputF.X[0].numbElementType() < Ring.NumberOfSimpleTypes)) && (inputF.X[1] instanceof Polynom)) {
                if (((Polynom) inputF.X[1]).coeffs.length == 2) {
                    F m1 = new F();
                    F m2 = new F();
                    boolean l = true;
                    Polynom t = new Polynom("t", ring);
                    Element tt = ring.numberONE();
                    Element ttt = ring.numberONE();
                    for (int i = 0; i < ((Polynom) inputF.X[1]).coeffs.length; i++) {
                        if (((Polynom) inputF.X[1]).powers[i] == 0) {
                            tt = ((Polynom) inputF.X[1]).coeffs[i];//коэффициент b
                        } else {
                            ttt = ((Polynom) inputF.X[1]).coeffs[i];//коэффициент a
                            m1 = new F(F.DIVIDE, new Element[]{(inputF.X[0].multiply(
                                        ((Polynom) inputF.X[1]).coeffs[i].minus_one(ring), ring)), ((Polynom) inputF.X[1]).coeffs[i].negate(ring)});
                            if (((Polynom) inputF.X[1]).coeffs[i].signum() == -1) {
                                l = true;
                            } else {
                                l = false;
                            }
                        }
                    }
                    if (l == false) {
                        tt = tt.negate(ring);
                    }
                    F tttt = new F(F.DIVIDE, new Element[]{tt, ttt.negate(ring)});
                    m2 = new F(F.EXP, new F(F.MULTIPLY, new Element[]{tttt, t}));
                    res = new F(F.MULTIPLY, new Element[]{m1, m2});
                }
            }
        }
        return res;
    }

    /**
     * На входе функция вида: a/(bx^2+c^2)
     *
     * @param f
     * @return - [a/sqrt[b]c]*Sin[ct/sqrt[b]]
     */
    public F inverse_sin(Ring ring) {
        F res = null;
        Polynom p = ((Polynom) ((F) inputF.X[1]).X[0]);
        if (p.coeffs[1].isOne(ring) && inputF.X[0].isOne(ring)) {
            Element a = inputF.X[0];
            Element c = null;
            Element a_div_c = null;
            Polynom t = new Polynom("t", new Ring("C64[t]"));
            if (p.coeffs[1].isOne(ring)) {
                c = p.coeffs[1];
                a_div_c = a;
                res = new F(F.SIN, new Element[]{t});
            } else {
                c = p.coeffs[1].powTheFirst(1, 2, ring);
                a_div_c = a.divide(c, ring);
                res = new F(F.SIN, new Element[]{new F(F.MULTIPLY, new Element[]{c, t})});
            }
            if (a_div_c.isOne(ring)) {
                return res;
            }
            return new F(F.MULTIPLY, new Element[]{a_div_c, res});
        } else {
            if (inputF.X[0].compareTo(p.coeffs[1].powTheFirst(1, 2, ring), ring) == 0) {
                Element a = inputF.X[0];
                Element c = p.coeffs[1].powTheFirst(1, 2, ring);
                Element a_div_c = a.divide(c, ring);
                Polynom t = new Polynom("t", new Ring("C64[t]"));
                res = new F(F.SIN, new Element[]{new F(F.MULTIPLY, new Element[]{c, t})});
                if (a_div_c.isOne(ring)) {
                    return res;
                }
                return new F(F.MULTIPLY, new Element[]{a_div_c, res});
            }
        }
        return res;
    }

    public F inverse_div(Ring ring) {
        F res = new F();
        if (inputF.X[0] instanceof Polynom) {
            Polynom p = (Polynom) inputF.X[0];
            Polynom q = (Polynom) ((F) inputF.X[1]).X[0];
            if (p.coeffs.length == 1) {
                if (p.powers.length == 1) {
                    if (p.powers[0] == 0) {
                        Element a = p.coeffs[0];
                        Element b = q.coeffs[0];
                        Element c = q.coeffs[1];
                        Element d = q.coeffs[2];
                        //c^2-4bd
                        Element dd = (c.multiply(c, ring)).subtract(b.multiply(d, ring).multiply(new NumberR64(4), ring), ring);
                        Element sqrtD = null;
                        if (dd.isOne(ring)) {
                            sqrtD = dd;
                        } else {
                            sqrtD = dd.sqrt(new Ring("C64[t]"));
                            if(sqrtD == null){
                                sqrtD = new F(F.intPOW, new Element[]{dd, new Polynom("0.5", ring)});
                            }
                        }
                        Element mult = null;
                        if (sqrtD.isOne(ring)) {
                            mult = a;
                        } else {
                            mult = a.divide(sqrtD, new Ring("R64[t]"));//ring
                        }
                        Element pow1 = null;
                        //-c/2b
                        Element pow11 = c.negate(ring).divide(new NumberR64(2).multiply(b, ring), ring);
                        //-sqrt(D)/2b
                        Element pow12 = sqrtD.negate(ring).divide(new NumberR64(2).multiply(b, ring), ring);
                        //t*(-sqrt(D)/2b)
                        Element pow13 = new Polynom("t", new Ring("C64[t]")).multiply(pow12, ring);
                        F exp1 = new F(F.EXP, new F(F.ADD, new Element[]{pow11, pow13}));
                        F f1 = new F(F.MULTIPLY, new Element[]{mult.negate(ring), exp1});
                        //sqrt(D)/b
                        Element pow14 = sqrtD.divide(b, ring);
                        //t*(sqrt(D)/b)
                        Element pow15 = new Polynom("t", new Ring("C64[t]")).multiply(pow14, ring);
                        F exp2 = new F(F.EXP, pow15);
                        F f2 = new F(F.MULTIPLY, new Element[]{mult, exp1, exp2});
                        return new F(F.ADD, new Element[]{f1, f2});
                    } else {
                        if (p.powers[0] == 1) {
                            Element a = p.coeffs[0];
                            Element b = q.coeffs[0];
                            Element c = q.coeffs[1];
                            Element d = q.coeffs[2];
                            //c^2-4bd
                            Element dd = (c.multiply(c, ring)).subtract(b.multiply(d, ring).multiply(new NumberR64(4), ring), ring);
                            Element sqrtD = null;
                            if (dd.isOne(ring)) {
                                sqrtD = dd;
                            } else {
                                sqrtD = dd.sqrt(new Ring("C64[t]"));
                                if(sqrtD == null){
                                    sqrtD = new F(F.intPOW, new Element[]{dd, new Polynom("0.5", ring)});
                                }
                            }
                            //b*sqrt(D)
                            Element el1 = b.multiply(sqrtD, ring);
                            F f1 = new F(F.DIVIDE, new Element[]{a, el1});
                            //-c/2*b
                            Element el2 = c.negate(ring).divide(new NumberR64(2).multiply(b, ring), ring);
                            Element el3 = new Polynom("t", new Ring("C64[t]")).multiply(el2, ring);
                            F f2 = new F(F.EXP, el3);
                            F f3 = new F(F.MULTIPLY, new Element[]{f1, f2});
                            //sqrt(D)/2*b
                            Element el4 = sqrtD.divide(new NumberR64(2).multiply(b, ring), ring);
                            Element el5 = new Polynom("t", new Ring("C64[t]")).multiply(el4, ring);
                            F f4 = new F(F.CH, el5);
                            F f5 = new F(F.SH, el5);
                            F f6 = new F(F.MULTIPLY, new Element[]{sqrtD, f4});
                            F f7 = new F(F.MULTIPLY, new Element[]{c, f5});
                            F f8 = new F(F.SUBTRACT, new Element[]{f6,f7});
                            return new F(F.MULTIPLY, new Element[]{f3, f8});
                        }
                    }
                } else {
                    Element a = p.coeffs[0];
                    Element b = q.coeffs[0];
                    Element c = q.coeffs[1];
                    Element d = q.coeffs[2];
                    //c^2-4bd
                    Element dd = (c.multiply(c, ring)).subtract(b.multiply(d, ring).multiply(new NumberR64(4), ring), ring);
                    Element sqrtD = null;
                    if (dd.isOne(ring)) {
                        sqrtD = dd;
                    } else {
                        sqrtD = dd.sqrt(new Ring("C64[t]"));
                        if(sqrtD == null){
                            sqrtD = new F(F.intPOW, new Element[]{dd, new Polynom("0.5", ring)});
                        }
                    }
                    Element mult = null;
                    if (sqrtD.isOne(ring)) {
                        mult = a;
                    } else {
                        mult = a.divide(sqrtD, ring);
                    }
                    Element pow1 = null;
                    //-c/2b
                    Element pow11 = c.negate(ring).divide(new NumberR64(2).multiply(b, ring), ring);
                    //-sqrt(D)/2b
                    Element pow12 = sqrtD.negate(ring).divide(new NumberR64(2).multiply(b, ring), ring);
                    //t*(-sqrt(D)/2b)
                    Element pow13 = new Polynom("t", new Ring("C64[t]")).multiply(pow12, ring);
                    F exp1 = new F(F.EXP, new F(F.ADD, new Element[]{pow11, pow13}));
                    F f1 = new F(F.MULTIPLY, new Element[]{mult.negate(ring), exp1});
                    //sqrt(D)/b
                    Element pow14 = sqrtD.divide(b, ring);
                    //t*(sqrt(D)/b)
                    Element pow15 = new Polynom("t", new Ring("C64[t]")).multiply(pow14, ring);
                    F exp2 = new F(F.EXP, pow15);
                    F f2 = new F(F.MULTIPLY, new Element[]{mult, exp1, exp2});
                    return new F(F.ADD, new Element[]{f1, f2});
                }
            } else if (p.coeffs.length == 2) {
                if (p.powers[0] == 1) {
                    Element a = p.coeffs[0];
                    Element b = p.coeffs[1];
                    Element c = q.coeffs[0];
                    Element d = q.coeffs[1];
                    Element e = q.coeffs[2];
                    //c^2-4bd
                    Element dd = (d.multiply(d, ring)).subtract(c.multiply(e, ring).multiply(new NumberR64(4), ring), ring);
                    Element sqrtD = null;
                    if (dd.isOne(ring)) {
                        sqrtD = dd;
                    } else {
                            sqrtD = dd.sqrt(new Ring("C64[t]"));
                        if(sqrtD == null){
                            sqrtD = new F(F.intPOW, new Element[]{dd, new Polynom("0.5", ring)});
                        }
                    }
                    //2*c*sqrt(D)
                    Element m1 = new Polynom("2", ring).multiply(c, ring).multiply(sqrtD, ring);
                    Element f1 = new F(F.DIVIDE, new Element[]{ring.numberONE, m1});
                    //-d/2c
                    Element pow11 = d.negate(ring).divide(new NumberR64(2).multiply(c, ring), ring);
                    //-sqrt(D)/2c
                    Element pow12 = sqrtD.negate(ring).divide(new NumberR64(2).multiply(c, ring), ring);
                    Element pow13 = new Polynom("t", new Ring("C64[t]")).multiply(pow12, ring);
                    F f2 = new F(F.EXP, new F(F.ADD, new Element[]{pow11, pow13}));
                    //*****
                    F f3 = new F(F.MULTIPLY, new Element[]{f1, f2});
                    //a*d
                    Element el1 = a.multiply(d, ring);
                    //a*sqrt(D)
                    Element el2 = a.multiply(sqrtD, ring);
                    //*****
                    F f4 = new F(F.ADD, new Element[]{el1, el2});
                    //sqrt(D)/c * t
                    Element pow14 = sqrtD.divide(c, ring).multiply(new Polynom("t", new Ring("C64[t]")), ring);
                    //-d*a
                    Element el3 = d.negate(ring).multiply(a, ring);
                    F f5 = new F(F.EXP, pow14);
                    //*****
                    F f6 = new F(F.MULTIPLY, new Element[]{el2, f5});
                    //*****
                    F f7 = new F(F.MULTIPLY, new Element[]{el3, f5});
                    //-2*b*c
                    Element el4 = new NumberR64(2).multiply(b.negate(ring).multiply(c, ring), ring);
                    //2*b*c
                    Element el5 = new NumberR64(2).multiply(b.multiply(c, ring), ring);
                    //*****
                    F f8 = new F(F.MULTIPLY, new Element[]{el4, f5});
                    F f9 = new F(F.ADD, new Element[]{f4, f6, f7, el4, f8});
                    return new F(F.MULTIPLY, new Element[]{f3, f9});
                }
            }
        }
        return res;
    }

    /**
     * На входе функция вида: ax/(bx^2+c^2)
     *
     * @param f
     * @return - [a/b]*Cos[ct/sqrt[b]]
     */
    public F inverse_cos(Ring ring) {
        F res = new F();
        if (inputF.X[0] instanceof Polynom) {
            Polynom p = null;
            if(inputF.X[1] instanceof F){
                p = ((Polynom)((F) inputF.X[1]).X[0]);
            }else{
                p = ((Polynom) inputF.X[1]);
            }
            if (((Polynom) inputF.X[0]).coeffs.length == 1) {
                if ((p.coeffs.length == 2)) {
                    if ((p.coeffs[0].signum() == 1) && (p.coeffs[1].signum() == 1)) {
                        if (p.powers[0] == 2) {
                            Element a = ((Polynom) inputF.X[0]).coeffs[0];
                            Element b_1 = p.coeffs[0];
                            Element c = null;
                            Element b_2 = null;
                            if (p.coeffs[1].isOne(ring)) {
                                c = p.coeffs[1];
                            } else {
                                c = p.coeffs[1].powTheFirst(1, 2, ring);
                            }
                            if (p.coeffs[0].isOne(ring)) {
                                b_2 = p.coeffs[0];
                            } else {
                                b_2 = p.coeffs[0].powTheFirst(1, 2, ring);
                            }
                            Element m1 = a.divide(b_1, ring);//a/b_1
                            Polynom t = new Polynom("t", new Ring("C64[t]"));
                            t.coeffs[0] = c.divide(b_2, ring);//ct/sqrt[b]
                            F m2 = new F(F.COS, t);
                            if (a.equals(b_1) || m1.isOne(ring)) {
                                return m2;
                            }
                            return new F(F.MULTIPLY, new Element[]{m1, m2});
                        }
                    }
                }
            } else {
                if (((Polynom) inputF.X[0]).coeffs.length == 2) {
                    Polynom q = ((Polynom) inputF.X[0]);//a*t+b
                    Element a = null;
                    Element b = null;
                    Element c = null;
                    Element s1 = null;
                    Element s2 = null;
                    if (p.coeffs[1].isOne(ring)) {
                        a = q.coeffs[0];
                        c = p.coeffs[0];
                        s1 = p.coeffs[1];
                    } else {
                        s1 = p.coeffs[1].pow(1, 2, ring)[0];//sqrt(d)
                        if(s1==null){
                            s1 = new F(F.intPOW, new Element[]{p.coeffs[1], new Polynom("0.5", ring)});
                        }
                        Element ell = p.coeffs[1].pow(1, 2, ring)[0];
                        //p.coeffs[1].powTheFirst(1, 2, ring)
                        //new F(F.intPOW, new Element[]{p.coeffs[1], new Polynom("0.5", ring)})
                        a = q.coeffs[0].multiply(ell, ring);//a*sqrt(d)
                        //p.coeffs[1].powTheFirst(1, 2, ring)
                        c = p.coeffs[0].multiply(ell, ring);//c*sqrt(d)
                    }
                    if (p.coeffs[0].isOne(ring)) {
                        b = q.coeffs[1];
                        s2 = p.coeffs[0];
                    } else {
                        s2 = p.coeffs[0].pow(1, 2, ring)[0];//sqrt(c)
                        if(s2==null){
                            s2 = new F(F.intPOW, new Element[]{p.coeffs[0], new Polynom("0.5", ring)});
                        }
                        b = q.coeffs[1].multiply(p.coeffs[0].powTheFirst(1, 2, ring), ring);//b*sqrt(c)
                    }
                    Element div = s1.divide(s2, ring);//sqrt(d)/sqrt(c)
                    Polynom t = new Polynom("t", new Ring("C64[t]"));
                    F fCos = new F(F.COS, new Element[]{div.multiply(t, ring)});//cos((sqrt(d)/sqrt(c))*t)
                    F fSin = new F(F.SIN, new Element[]{div.multiply(t, ring)});//cos((sqrt(d)/sqrt(c))*t)
                    Element el1 = a.divide(c, ring);
                    Element el2 = b.divide(c, ring);
                    F f1 = new F(F.MULTIPLY, new Element[]{el1, fCos});
                    F f2 = new F(F.MULTIPLY, new Element[]{el2, fSin});
                    return new F(F.ADD, new Element[]{f1, f2});
                }
            }
        } else {
            if (((Polynom) inputF.X[0]).coeffs.length == 2) {
            } else {
                if (((Polynom) inputF.X[1]).coeffs.length == 2) {
                    if ((((Polynom) inputF.X[1]).coeffs[0].signum() == 1) && (((Polynom) inputF.X[1]).coeffs[1].signum() == 1)) {
                        if (((Polynom) inputF.X[1]).powers[0] == 2) {
                            Element a = inputF.X[0];
                            Element b_1 = ((Polynom) inputF.X[1]).coeffs[0];
                            Element c = ((Polynom) inputF.X[1]).coeffs[1].powTheFirst(1, 2, ring);
                            Element b_2 = ((Polynom) inputF.X[1]).coeffs[0].powTheFirst(1, 2, ring);
                            F m1 = new F(F.DIVIDE, new Element[]{a, b_1});//[a/b]
                            Polynom t = new Polynom("t", new Ring("C64[t]"));
                            t.coeffs[0] = c.divide(b_2, ring);//ct/sqrt[b]
                            F m2 = new F(F.COS, t);
                            if (a.equals(b_1)) {
                                return m2;
                            }
                            return new F(F.MULTIPLY, new Element[]{m1, m2});
                        }
                    }
                }
            }
        }

        return res;
    }

    /**
     * На входе функция вида: a/(b^2 + (b-x)^2)
     *
     * @param f
     * @return - (a/b) Exp[c t] Sin[b t]
     */
    public F inverse_exp_sin() {
        Ring ring = new Ring("C64[t]");
        F res = new F();
        if (inputF.name == F.DIVIDE) {
            if ((inputF.X[0] instanceof Polynom) && (inputF.X[1] instanceof Polynom)) {
                if ((((Polynom) inputF.X[1]).coeffs.length == 2) && (((Polynom) inputF.X[0]).coeffs.length == 1)) {
                    if ((((Polynom) inputF.X[1]).coeffs[0].signum() == 1) && (((Polynom) inputF.X[1]).coeffs[1].signum() == 1)) {
                        if (((Polynom) inputF.X[1]).powers[0] == 2) {
                            Element a = ((Polynom) inputF.X[0]).coeffs[0];
                            Element b_1 = ((Polynom) inputF.X[1]).coeffs[0];
                            Element c = ((Polynom) inputF.X[1]).coeffs[1].powTheFirst(1, 2, ring);
                            Element b_2 = ((Polynom) inputF.X[1]).coeffs[0].powTheFirst(1, 2, ring);
                            F m1 = new F(F.DIVIDE, new Element[]{a, b_1});//[a/b]
                            Polynom t = new Polynom("t", ring);
                            t.coeffs[0] = c.divide(b_2, ring);//ct/sqrt[b]
                            F m2 = new F(F.COS, t);
                            if (a.equals(b_1)) {
                                return m2;
                            }
                            return new F(F.MULTIPLY, new Element[]{m1, m2});
                        }
                    }
                }
            }
        }
        return res;
    }

    /**
     * На входе функция вида: (ax+b)/((x+c)^2+d^2)
     *
     * @param f
     * @return
     */
    public F pol_div1() {
        Ring ring = new Ring("C64[t]");
        if (inputF.name == F.DIVIDE) {
            if ((((F) inputF.X[1]).name == F.ADD) && (((F) (((F) inputF.X[1]).X[1])).name == F.POW)) {
                if (((Polynom) ((F) (((F) inputF.X[1]).X[1])).X[1]).coeffs[0].equals(new NumberR64(2))) {
                    Element a = ((Polynom) inputF.X[0]).coeffs[0];
                    Element b = ((Polynom) inputF.X[0]).coeffs[1];
                    Element c = ((Polynom) ((F) ((F) inputF.X[1]).X[1]).X[0]).coeffs[1];
                    Element dk = ((Polynom) ((F) inputF.X[1]).X[0]).coeffs[0].powTheFirst(1, 2, ring);
                    Element el1 = b.subtract(a.multiply(c, ring), ring).divide(dk, ring);//(b-ac)/2
                    Polynom el2 = new Polynom(new int[]{1}, new Element[]{c.negate(ring)});//-ct
                    Polynom el3 = new Polynom(new int[]{1}, new Element[]{dk});//dt
                    F f1 = new F(F.MULTIPLY, new Element[]{a, new F(F.COS, el3)});//ad[cos(dt)]/d
                    F f2 = new F(F.MULTIPLY, new Element[]{el1, new F(F.SIN, el3)});//(b-ac)[sin(dt)]/d
                    F f3 = new F(F.ADD, new Element[]{f1, f2});//a[cos(dt)]+((b-ac)/d)[sin(dt)]
                    F f4 = new F(F.EXP, el2);//e^{-ct}
                    return new F(F.MULTIPLY, new Element[]{f4, f3});//e^{-ct}(a[cos(dt)]+((b-ac)/d)[sin(dt)])
                }
            }
        }
        return null;
    }

    /**
     * На входе функция вида: (ax+b)/((x+c)^2-d^2)
     *
     * @param f
     * @return
     */
    public F pol_div2() {
        Ring ring = new Ring("C64[t]");
        if (inputF.name == F.DIVIDE) {
            if ((((F) inputF.X[1]).name == F.SUBTRACT) && (((F) (((F) inputF.X[1]).X[0])).name == F.POW)) {
                if (((Polynom) ((F) (((F) inputF.X[1]).X[0])).X[1]).coeffs[0].equals(new NumberR64(2))) {
                    Element a = ((Polynom) inputF.X[0]).coeffs[0];
                    Element b = ((Polynom) inputF.X[0]).coeffs[1];
                    Element c = ((Polynom) ((F) ((F) inputF.X[1]).X[0]).X[0]).coeffs[1];
                    Element dk = ((Polynom) ((F) inputF.X[1]).X[1]).coeffs[0].powTheFirst(1, 2, ring);
                    Element el1 = b.subtract(a.multiply(c, ring), ring).divide(dk, ring);//(b-ac)/2
                    Polynom el2 = new Polynom(new int[]{1}, new Element[]{c.negate(ring)});//-ct
                    Polynom el3 = new Polynom(new int[]{1}, new Element[]{dk});//dt
                    F f1 = new F(F.MULTIPLY, new Element[]{a, new F(F.CH, el3)});//ad[cos(dt)]/d
                    F f2 = new F(F.MULTIPLY, new Element[]{el1, new F(F.SH, el3)});//(b-ac)[sin(dt)]/d
                    F f3 = new F(F.ADD, new Element[]{f1, f2});//a[cos(dt)]+((b-ac)/d)[sin(dt)]
                    F f4 = new F(F.EXP, el2);//e^{-ct}
                    return new F(F.MULTIPLY, new Element[]{f4, f3});//e^{-ct}(a[cos(dt)]+((b-ac)/d)[sin(dt)])
                }
            }
        }
        return null;
    }

    /**
     * На входе функция вида: (ax+b)/(x+c)^k
     *
     * @param f
     * @return
     */
    public F pol_div3() {
        Ring ring = new Ring("C64[t]");
        if ((inputF.name == F.DIVIDE) && (inputF.X.length == 2)) {
            if (((F) inputF.X[1]).name == F.POW) {
                Element a = ((Polynom) inputF.X[0]).coeffs[0];
                Element b = ((Polynom) inputF.X[0]).coeffs[1];
                Element c = ((Polynom) ((F) inputF.X[1]).X[0]).coeffs[1];
                Element k = ((Polynom) ((F) inputF.X[1]).X[1]).coeffs[0];
                Element el1 = new NumberR64(-2).add(k, ring);//-2+k
                Polynom el2 = new Polynom(new int[]{1}, new Element[]{c.negate(ring)});//-ct
                Polynom el3 = new Polynom(new int[]{1}, new Element[]{b});//bt
                Polynom el4 = new Polynom(new int[]{1, 0}, new Element[]{a.negate(ring).multiply(c, ring), a.negate(ring).add(a.multiply(k, ring), ring)});//(-a+k)-act
                Polynom el5 = new Polynom(new int[]{el1.intValue()}, new Element[]{new NumberR64(1)});//t^{-2+k}
                Polynom el6 = el3.add(el4, ring);//2bt+(-a+k)-act
                int kk = k.intValue() - 1;//k-1
                double fact = fact(kk);
                F f1 = new F(F.EXP, el2);//e^{-ct}
                F f2 = new F(F.MULTIPLY, new Element[]{f1, el5, el6});//(e^{-ct}*t^{-2+k}*[bt+(-a+k)-act]
                return new F(F.DIVIDE, new Element[]{f2, new NumberR64(fact)});//(e^{-ct}*t^{-2+k}*[bt+(-a+k)-act] / (k-1)!
            }
        }
        return null;
    }

    /**
     * Вычисляет биномиальный коэффициент
     *
     * @param r int
     * @param j int
     * @return int - возвращает значение коэффициента
     * @author Ribakov Mixail
     */
    int binomial(int r, int j) {
        int B;
        if (j != 0) {
            int r_fact;
            r_fact = 1;
            for (int i = 1; i <= r; i++) {
                r_fact = i * r_fact;
            }
            int j_fact;
            j_fact = 1;
            for (int i = 1; i <= j; i++) {
                j_fact = i * j_fact;
            }
            int r_j_fact;
            r_j_fact = 1;
            for (int i = 1; i <= r - j; i++) {
                r_j_fact = i * r_j_fact;
            }
            B = r_fact / (j_fact * r_j_fact);
            return B;
        } else {
            B = 1;
        }
        return B;
    }

    /**
     * Возвращает n!
     *
     * @param n int
     * @return double
     */
    double fact(int n) {
        double a = 1;
        for (int i = 1; i <= n; i++) {
            a = a * i;
        }
        return a;
    }

    /**
     * Возвращает n!
     *
     * @param n double
     * @return double
     */
    double fact(double n) {
        double a = 1;
        for (int i = 1; i <= n; i++) {
            a = a * i;
        }
        return a;
    }

    /**
     * Возвращает коэффициент по формуле Стирлинга
     *
     * @param x double
     * @return double
     */
    double gammastirf(double x) {
        double result = 0;
        double y = 0;
        double w = 0;
        double v = 0;
        double stir = 0;
        w = 1 / x;
        stir = 7.87311395793093628397E-4;
        stir = -2.29549961613378126380E-4 + w * stir;
        stir = -2.68132617805781232825E-3 + w * stir;
        stir = 3.47222221605458667310E-3 + w * stir;
        stir = 8.33333333333482257126E-2 + w * stir;
        w = 1 + w * stir;
        y = Math.exp(x);
        if (x > 143.01608) {
            v = Math.pow(x, 0.5 * x - 0.25);
            y = v * (v / y);
        } else {
            y = Math.pow(x, x - 0.5) / y;
        }
        result = 2.50662827463100050242 * y * w;
        return result;
    }

    /**
     * Возвращает значение Гаммы функции
     *
     * @param x double
     * @return double
     */
    double gamma(double x) {
        double result = 0;
        double p = 0;
        double pp = 0;
        double q = 0;
        double qq = 0;
        double z = 0;
        int i = 0;
        double sgngam = 0;
        sgngam = 1;
        q = Math.abs(x);
        if (q > 33.0) {
            if (x < 0.0) {
                p = (int) Math.floor(q);
                i = (int) Math.round(p);
                if (i % 2 == 0) {
                    sgngam = -1;
                }
                z = q - p;
                if (z > 0.5) {
                    p = p + 1;
                    z = q - p;
                }
                z = q * Math.sin(Math.PI * z);
                z = Math.abs(z);
                z = Math.PI / (z * gammastirf(q));
            } else {
                z = gammastirf(x);
            }
            result = sgngam * z;
            return result;
        }
        z = 1;
        while (x >= 3) {
            x = x - 1;
            z = z * x;
        }
        while (x < 0) {
            if (x > -0.000000001) {
                result = z / ((1 + 0.5772156649015329 * x) * x);
                return result;
            }
            z = z / x;
            x = x + 1;
        }
        while (x < 2) {
            if (x < 0.000000001) {
                result = z / ((1 + 0.5772156649015329 * x) * x);
                return result;
            }
            z = z / x;
            x = x + 1.0;
        }
        if (x == 2) {
            result = z;
            return result;
        }
        x = x - 2.0;
        pp = 1.60119522476751861407E-4;
        pp = 1.19135147006586384913E-3 + x * pp;
        pp = 1.04213797561761569935E-2 + x * pp;
        pp = 4.76367800457137231464E-2 + x * pp;
        pp = 2.07448227648435975150E-1 + x * pp;
        pp = 4.94214826801497100753E-1 + x * pp;
        pp = 9.99999999999999996796E-1 + x * pp;
        qq = -2.31581873324120129819E-5;
        qq = 5.39605580493303397842E-4 + x * qq;
        qq = -4.45641913851797240494E-3 + x * qq;
        qq = 1.18139785222060435552E-2 + x * qq;
        qq = 3.58236398605498653373E-2 + x * qq;
        qq = -2.34591795718243348568E-1 + x * qq;
        qq = 7.14304917030273074085E-2 + x * qq;
        qq = 1.00000000000000000320 + x * qq;
        result = z * pp / qq;
        return result;
    }

    /**
     * Вычисляет функцию ошибок разложеную в ряд Тейлора. По формуле
     * erf(x)=(2/sqrt(Pi))*Sum_{n=0}^{infiniti} ((-1)^{n}*x^{2n+1}/(n!*(2n+1)))
     * х- значение которое вводится
     *
     * @param x double
     * @return double
     */
    public double erfx(double x) { //вводится x
        double eps = 0.00001;
        double sum = 0; //сумма
        double zn = 1; //знак
        double mnx = x;
        double fakt = 1; //факториал
        double ch1 = 10;
        double chold = 0;
        int n = 0; //номер
        while (Math.abs(ch1 - chold) > eps) {
            if (n != 0) {
                fakt *= n; //вычисляем факториал
                mnx *= x * x; //вычилсяем множитель X
            }
            chold = ch1; //сохраняем старое значение члена
            ch1 = mnx / (fakt * (2 * n + 1)); //вычисляем новый член
            sum = sum + ch1 * zn; //прибавляем новый член к сумме
            zn = -zn; //меняем знак на противоположный
            n++; //увеличиваем количество членов ряда
        }
        sum = sum * 2 / Math.sqrt(Math.PI);
        return sum;

    }

    /**
     * Вычисляет функцию ошибок разложеную в ряд Тейлора. По формуле
     * erf(x)=(2/sqrt(Pi))*Sum_{n=0}^{infiniti} ((-1)^{n}*x^{2n+1}/(n!*(2n+1)))
     * х- значение которое вводится. eps- фиксированная точность.
     */
    public double erfx(double x, double eps) { //вводится x и точность eps
        double sum = 0; //сумма
        double zn = 1; //знак
        double mnx = x;
        double fakt = 1; //факториал
        double ch1 = 10;
        double chold = 0;
        int n = 0; //номер
        while (Math.abs(ch1 - chold) > eps) {
            if (n != 0) {
                fakt *= n; //вычисляем факториал
                mnx *= x * x; //вычилсяем множитель X
            }
            chold = ch1; //сохраняем старое значение члена
            ch1 = mnx / (fakt * (2 * n + 1)); //вычисляем новый член
            sum = sum + ch1 * zn; //прибавляем новый член к сумме
            zn = -zn; //меняем знак на противоположный
            n++; //увеличиваем количество членов ряда
        }
        sum = sum * 2 / Math.sqrt(Math.PI);
        return sum;
    }

    /**
     * Возвращает коэффициент равный - (-1)^{j} * w * c^{r-j} * Binomial[r,j] *
     * (k-1)! / (k-j-1)!
     *
     * @param j int - порядок
     * @param r_st int - степень полинома в числителе
     * @param w int - коэффициент числителя
     * @param c int - коэффициент знаменателя
     * @param k int - степень полинома в знаменателе
     * @return double
     * @author Ribakov Mixail
     */
    Element coeff5(int j, int r, Element w, Element c, int k, Ring ring) {
        Element one = w.myOne(ring);
        Element minus_one = w.minus_one(ring);
        Element coeff;
        //вычисляем (k-j-1)!
        Element fact_kj = one;
        int h = 1;
        for (h = 1; h <= k - j - 1; h++) {
            fact_kj = fact_kj.multiply(one.valOf(h, ring), ring);
        }
        //вычисляем (k-1)!
        Element fact_k = fact_kj;
        for (; h <= k - 1; h++) {
            fact_k = fact_k.multiply(one.valOf(h, ring), ring);
        }
        //вычисляем (-1)^j
        Element koef = ((j & 1) == 0) ? one : minus_one;
        //вычисляем c^{r-j)
        Element konst = c.powTheFirst(r - j, 1, ring);
        Element binomial = one.valOf(binomial(r, j), ring); //Биномиал (int) исправить!!!
        coeff = koef.multiply(w, ring).multiply(binomial, ring).multiply(fact_k, ring).
                multiply(konst, ring).divide(fact_kj, ring); //(-1)^j*w*binomial[r,j]*(k-1)!*c^{r-j) / (k-j-1)!
        return coeff;

    }

    /**
     * Возвращает полином вида p1*t^{s1} + p2*t^{s2} + ...
     *
     * @param r_st int - степень полинома в числителе
     * @param w double - коэффициент числителя
     * @param c double - коэффициент знаменателя
     * @param k int - степень полинома в знаменателе
     * @return PolynomR
     * @author Ribakov Mixail
     */
    public Polynom buildPolynom(int r, Element w, Element c, int k, Ring ring) {
        int n = r + 1;
        //int n1 = 2 * n;
        int st[] = new int[n]; //массив степеней полинома
        Element kf[] = new Element[n]; //массив коэффициентов полинома
        for (int i = 0; i < n; i++) { //Заполнение массивов
            //st[2 * r_st - 2 * i + 1] = i;
            st[i] = r - i;
            kf[i] = coeff5(i, r, w, c, k, ring);
        }
        Polynom p = new Polynom(st, kf);
        return p;
    }

    /**
     * На входе функция вида: ax^r/(x+b)^k
     *
     * @param f
     * @return
     */
    public F pol_div4() {
        Ring ring = new Ring("C64[t]");
        if ((inputF.name == F.DIVIDE) && (inputF.X.length == 2)) {
            if (((F) inputF.X[1]).name == F.POW) {
                Element a = ((Polynom) inputF.X[0]).coeffs[0];
                int s = ((Polynom) inputF.X[0]).powers[0];//степень полинома в числителе
                Element b = ((Polynom) ((F) inputF.X[1]).X[0]).coeffs[1];
                int k = ((Polynom) ((F) inputF.X[1]).X[1]).coeffs[0].intValue();//степень полинома в знаменателе
                if (s < k) {
                    F f1 = new F(F.EXP, new Polynom(new int[]{1}, new Element[]{b.negate(ring)}));//e^{-bt}
                    //вычисляем (-1)^r
                    Element koef = ((s & 1) == 0) ? NumberR64.ONE : NumberR64.MINUS_ONE;
                    Element fact = new NumberR64(fact(k - 1));//(k-1)!
                    Polynom p1 = new Polynom(new int[]{k - (s + 1)}, new Element[]{koef.divide(fact, ring)});//[(-1)^{r} / (k-1!)]t^{k-(r+1)}
                    Polynom p2 = buildPolynom(s, a, b, k, ring); //создаем полином вида p1*t^{s1} + p2*t^{s2} + ...
                    return new F(F.MULTIPLY, new Element[]{p1, f1, p2});//[(-1)^{r} / (k-1!)]t^{k-(r+1)} * e^{-bt} *p1*t^{s1} + p2*t^{s2} + ...
                }
            }
        }
        return null;
    }

    /**
     * На входе функция вида: a/x^{n}
     *
     * @param f
     * @return
     */
    public F pol_div5() {
        Ring ring = new Ring("C64[t]");
        if (inputF.name == F.DIVIDE) {
            if (inputF.X[1] instanceof Polynom) {
                if (((Polynom) inputF.X[1]).powers.length == 1 && (((Polynom) inputF.X[1]).powers[0] == 1)) {
                    return new F(inputF.X[0]);//a
                }
                if (((Polynom) inputF.X[1]).powers.length == 1 && (((Polynom) inputF.X[1]).powers[0] != 1)) {
                    int n = ((Polynom) inputF.X[1]).powers[0] - 1;
                    Element f1 = null;
                    if (inputF.X[0].isOne(ring)) {
                        f1 = new Polynom(new int[]{((Polynom) inputF.X[1]).powers[0] - 1}, new Element[]{new NumberR64(1)});
                    } else {
                        f1 = new F(F.MULTIPLY, new Element[]{inputF.X[0], new Polynom(new int[]{((Polynom) inputF.X[1]).powers[0] - 1}, new Element[]{new NumberR64(1)})});//a*t^{n-1}
                    }
                    if (new NumberR64(fact(n)).isOne(ring)) {
                        return new F(f1);
                    } else {
                        return new F(F.DIVIDE, new Element[]{f1, new NumberR64(fact(n))});//a*t^{n-1} / n!
                    }
                }
            } else {
                if (inputF.X[1] instanceof F) {
                    F ff = (F) inputF.X[1];
                    if (ff.name == F.intPOW) {
                        if (ff.X[0] instanceof Polynom) {
                            Polynom pp = (Polynom) ff.X[0];
                            if (pp.coeffs.length == 1) {
                                int n = 0;
                                Element f1 = null;
                                if (ff.X[1] instanceof Polynom) {
                                    Polynom pow = (Polynom) ff.X[1];
                                    n = pow.powers[0] - 1;
                                } else {
                                    n = ff.X[1].intValue() - 1;
                                }
                                if (inputF.X[0].isOne(ring)) {
                                    f1 = new Polynom(new int[]{n}, new Element[]{new NumberR64(1)});//a=0 t^{n-1}
                                } else {
                                    f1 = new Polynom(new int[]{n}, new Element[]{inputF.X[0]});//a=1 a*t^{n-1}
                                }
                                f1 = new Polynom(new int[]{n}, new Element[]{inputF.X[0].divide(new NumberR64(fact(n)), ring)});//a*t^{n-1} / n!
                                return new F(f1);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * На входе функция вида: a/(x+{-}n)^{k}
     *
     * @param f
     * @return - a*Exp[-{+}nt]*t^{-1+k}/ [Gamma[k]]
     */
    public F pol_div6() {
        Ring ring = new Ring("C64[t]");
        if (inputF.name == F.DIVIDE) {
            if (inputF.X[0].numbElementType() < Ring.NumberOfSimpleTypes) {
                if (inputF.X[1] instanceof Polynom) {
                    if (inputF.X[0].isOne(ring)) {
                        return new F(F.EXP, new Polynom(new int[]{1}, new Element[]{((Polynom) inputF.X[1]).coeffs[1].negate(ring)}));//a*Exp[-nt]
                    } else {
                        return new F(F.MULTIPLY, new Element[]{inputF.X[0], new F(F.EXP, new Polynom(new int[]{1}, new Element[]{((Polynom) inputF.X[1]).coeffs[1].negate(ring)}))});//a*Exp[-nt]
                    }
                }
            }
            if ((!(inputF.X[0] instanceof Polynom)) && (inputF.X[1] instanceof Polynom)) {
                if (inputF.X[0].isOne(ring)) {
                    return new F(F.EXP, new Polynom(new int[]{1}, new Element[]{((Polynom) inputF.X[1]).coeffs[1].negate(ring)}));//a*Exp[-nt]
                } else {
                    return new F(F.MULTIPLY, new Element[]{inputF.X[0], new F(F.EXP, new Polynom(new int[]{1}, new Element[]{((Polynom) inputF.X[1]).coeffs[1].negate(ring)}))});//a*Exp[-nt]
                }
            }
            if ((inputF.X[0] instanceof Polynom) && (inputF.X[1] instanceof Polynom)) {
                if (((Polynom) inputF.X[0]).coeffs[0].isOne(ring)) {
                    return new F(F.EXP, new Polynom(new int[]{1}, new Element[]{((Polynom) inputF.X[1]).coeffs[1].negate(ring)}));//a*Exp[-nt]
                } else {
                    return new F(F.MULTIPLY, new Element[]{inputF.X[0], new F(F.EXP, new Polynom(new int[]{1}, new Element[]{((Polynom) inputF.X[1]).coeffs[1].negate(ring)}))});//a*Exp[-nt]
                }
            }
            if (inputF.X[1] instanceof F) {
                if (((F) inputF.X[1]).name == F.intPOW) {
                    if (((F) inputF.X[1]).X[0] instanceof Polynom) {
                        if (((Polynom) ((F) inputF.X[1]).X[0]).coeffs.length == 2) {
                            if (!((Polynom) ((F) inputF.X[1]).X[0]).coeffs[1].isZero(ring)) {
                                F mult = null;
                                if (inputF.X[0].isOne(ring)) {
                                    mult = new F(F.EXP, new Polynom(new int[]{1}, new Element[]{((Polynom) ((F) inputF.X[1]).X[0]).coeffs[1].negate(ring)}));
                                } else {
                                    mult = new F(F.MULTIPLY, new Element[]{inputF.X[0], new F(F.EXP, new Polynom(new int[]{1}, new Element[]{((Polynom) ((F) inputF.X[1]).X[0]).coeffs[1].negate(ring)}))});//a*Exp[-nt]
                                }
                                Polynom p = new Polynom(new int[]{-1 + ((F) inputF.X[1]).X[1].intValue()}, new Element[]{new NumberR64(1)});
                                if ((p.powers[0] == 0) && (fact(((F) inputF.X[1]).X[1].intValue() - 1) == 1)) {
                                    return mult;//new F(DIVIDE, new Element[]{mult});
                                } else {
                                    if (fact(((F) inputF.X[1]).X[1].intValue() - 1) == 1) {
                                        return new F(F.MULTIPLY, new Element[]{mult, p});//new F(DIVIDE, new F(MULTIPLY, new Element[]{mult, p}));
                                    }
                                    return new F(F.DIVIDE, new Element[]{new F(F.MULTIPLY, new Element[]{mult, p}), new NumberR64(fact(((F) inputF.X[1]).X[1].intValue() - 1))});
                                }
                            } else {
                                Polynom p = new Polynom(new int[]{-1 + ((F) inputF.X[1]).X[1].intValue()}, new Element[]{new NumberR64(1)});
                                if ((p.powers[0] == 0) && (fact(((F) inputF.X[1]).X[1].intValue() - 1) == 1)) {
                                    return new F(inputF.X[0]);//new F(DIVIDE, new Element[]{f.X[0]});
                                } else {
                                    if (fact(((F) inputF.X[1]).X[1].intValue() - 1) == 1) {
                                        return new F(F.MULTIPLY, new Element[]{inputF.X[0], p});//new F(DIVIDE, new F(MULTIPLY, new Element[]{f.X[0], p}));
                                    }
                                    return new F(F.DIVIDE, new Element[]{new F(F.MULTIPLY, new Element[]{inputF.X[0], p}), new NumberR64(fact(((F) inputF.X[1]).X[1].intValue() - 1))});
                                }
                            }
                        } else {
                            if (((Polynom) ((F) inputF.X[1]).X[0]).coeffs.length == 1) {
                                if (((Polynom) ((F) inputF.X[1]).X[0]).coeffs[0].isOne(ring)) {
                                    return new F(inputF.X[0]);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * На входе функция вида: a/(x^{2}-b^2)
     *
     * @param f
     * @return - (a/b)Sh[bt]
     */
    public F inverse_sh() {
        Ring ring = new Ring("C64[t]");
        if (inputF.name == F.DIVIDE) {
            if (((Polynom) inputF.X[1]).powers.length == 2 && (((Polynom) inputF.X[1]).powers[0] == 2)) {
                Element a = null;
                if (inputF.X[0].numbElementType() < Ring.Polynom) {
                    a = inputF.X[0];
                } else {
                    a = ((Polynom) inputF.X[0]).coeffs[0];//a
                }
                Element b = null;
                if (((Polynom) inputF.X[1]).coeffs[1].negate(ring).isOne(ring)) {
                    b = ((Polynom) inputF.X[1]).coeffs[1].negate(ring);
                } else {
                    b = ((Polynom) inputF.X[1]).coeffs[1].negate(ring).powTheFirst(1, 2, ring);//b
                }
                F f1 = null;
                if (a.isOne(ring)) {
                    f1 = new F(F.SH, new Polynom(new int[]{1}, new Element[]{b}));//a*Sh[bt]
                } else {
                    f1 = new F(F.MULTIPLY, new Element[]{a, new F(F.SH, new Polynom(new int[]{1}, new Element[]{b}))});//a*Sh[bt]
                }
                if (b.isOne(ring)) {
                    return f1;
                } else {
                    return new F(F.DIVIDE, new Element[]{f1, b});//a*Sh[bt] / b
                }
            }
        }
        return null;
    }

    /**
     * На входе функция вида: ax/(x^{2}-b^2)
     *
     * @param f
     * @return - a*Ch[bt]
     */
    public F inverse_ch() {
        Ring ring = new Ring("C64[t]");
        if (inputF.name == F.DIVIDE) {
            if (((Polynom) inputF.X[0]).powers.length == 1 && (((Polynom) inputF.X[0]).powers[0] == 1)) {
                if (((Polynom) inputF.X[1]).powers.length == 2 && (((Polynom) inputF.X[1]).powers[0] == 2)) {
                    Element a = ((Polynom) inputF.X[0]).coeffs[0];//a
                    Element b = ((Polynom) inputF.X[1]).coeffs[1].negate(ring).powTheFirst(1, 2, ring);//b
                    return new F(F.MULTIPLY, new Element[]{a, new F(F.CH, new Polynom(new int[]{1}, new Element[]{b}))});//a*Ch[bt]
                }
            }
        }
        return null;
    }

    /**
     * На входе функция вида: exp[-as]/s
     *
     * @param f
     * @return - UnitStep(t-a)
     */
    public F inverse_unitstep(Ring ring) {
        if (inputF.X[1] instanceof Polynom) {
            if (((F) inputF.X[0]).X[0] instanceof Polynom) {
                if ((((Polynom) ((F) inputF.X[0]).X[0]).coeffs.length == 1) && (((Polynom) ((F) inputF.X[0]).X[0]).powers[0] == 1)) {
                    if (((Polynom) ((F) inputF.X[0]).X[0]).coeffs[0].isNegative()) {
                        return new F(F.UNITSTEP, new Polynom(new int[]{1, 0}, new Element[]{ring.numberONE, ((Polynom) ((F) inputF.X[0]).X[0]).coeffs[0]}));
                    }
                }
            }
        } else {
            //Exp[bp+c] / (p+d)^n
            if (inputF.X[1] instanceof F) {
                F ff = (F) inputF.X[1];
                if (ff.name == F.intPOW) {
                    Element el1 = ((F) inputF.X[0]).X[0];
                    Element el2 = ((F) inputF.X[1]).X[0];
                    Element b = ((Polynom) el1).coeffs[0];//b
                    Element c = ((Polynom) el1).coeffs[1];//c
                    Element d = ((Polynom) el2).coeffs[1];//d
                    int n = ((F) inputF.X[1]).X[1].intValue();
                    Element g = new NumberR64(gamma(n));//gamma(n)
                    Element el = ring.numberONE.divide(g, ring);//1/gamma(n)
                    Polynom p = new Polynom(new int[]{1, 0}, new Element[]{ring.numberONE, b});//b+t
                    Polynom p1 = (Polynom) d.multiply(p, ring);//d*(b+t)
                    Polynom p2 = (Polynom) c.subtract(p1, ring);//c-d*(b+t)
                    F exp = new F(F.EXP, p2);
                    F us = new F(F.UNITSTEP, p);
                    F pow = new F(F.intPOW, new Element[]{p, new NumberR64(-1 + n)});//(b+t)^{-1+n}
                    return new F(F.MULTIPLY, new Element[]{el, exp, pow, us});
                }
            }
        }
        return null;
    }

    /**
     *
     * @param f - a*Exp[bp+c] / (p+d)
     * @param ring
     * @return - a*Exp[-d*(t+b)+c]*unitStep(t+b)
     */
    public F inverse_coeffUnitStep(Ring ring) {
        Ring ring1 = new Ring("C64[t]");
        F f1 = (F) ((F) inputF.X[0]).X[0];//Exp[bp+c]
        Element a = ((F) inputF.X[0]).X[1];//a
        Element p = inputF.X[1];//(p+d)
        Element b = ((Polynom) f1.X[0]).coeffs[0];
        Element c = ((Polynom) f1.X[0]).coeffs[1];
        Element d = ((Polynom) p).coeffs[1];
        Polynom p1 = new Polynom(new int[]{1, 0}, new Element[]{ring1.numberONE, b});//t+b
        Polynom p2 = (Polynom) d.multiply(p1, ring).negate(ring);//-d*(t+b)
        Polynom p3 = (Polynom) p2.add(c, ring);//-d*(t+b)+c
        F exp = new F(F.EXP, p3);
        F us = new F(F.UNITSTEP, p1);
        return new F(F.MULTIPLY, new Element[]{a, exp, us});
    }

    /**
     * Замена узлов функции на вновь преобразованные по обратному преобразованию
     * Лапласа
     *
     * @param f - входная функция
     */
    public F inverseLaplaceTransform(Element f, Ring ring) {
        Element[] newX = null;
        if (!(f instanceof F)) {
            f = new F(f);
        }
        if (f instanceof F) {
            switch (((F) f).name) {
                case F.DIVIDE: {
                    if (((F) f).X.length == 2) {
                        if (((F) f).X[0] instanceof F) {
                            F func = ((F) ((F) f).X[0]);
                            switch (func.name) {
                                case F.EXP:
                                    return new InverseLaplaceTransform((F) f).inverse_unitstep(ring);
                                case F.MULTIPLY:
                                    return new InverseLaplaceTransform((F) f).inverse_coeffUnitStep(ring);
                            }
                        }
                        if (!(((F) f).X[0] instanceof Polynom)) {
                            if (((F) f).X[1] instanceof Polynom) {
                                if (((Polynom) ((F) f).X[1]).coeffs.length == 2 && ((Polynom) ((F) f).X[1]).powers[0] == 2) {
                                    if(((Polynom) ((F) f).X[1]).coeffs[1].signum() == 1){
                                    return new InverseLaplaceTransform((F) f).inverse_sin(ring);
                                }}
                            } else {
                                if (((F) f).X[1] instanceof F) {
                                    if (((F) ((F) f).X[1]).name == F.intPOW) {
                                        Polynom p = (Polynom) ((F) ((F) f).X[1]).X[0];
                                        if (((F) ((F) f).X[1]).X[1].isOne(ring)) {
                                            if (p.coeffs.length == 2 && p.powers[0] == 2) {
                                                return new InverseLaplaceTransform((F) f).inverse_sin(ring);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (((Polynom) ((F) f).X[0]).isOne(ring)) {
                                if (((F) f).X[1] instanceof Polynom) {
                                    if (((Polynom) ((F) f).X[1]).coeffs.length == 2 && ((Polynom) ((F) f).X[1]).powers[0] == 2) {
                                        if(((Polynom) ((F) f).X[1]).coeffs[1].signum() == 1){
                                        return new InverseLaplaceTransform((F) f).inverse_sin(ring);
                                    }}
                                } else {
                                    if (((F) f).X[1] instanceof F) {
                                        if (((F) ((F) f).X[1]).name == F.intPOW) {
                                            Polynom p = (Polynom) ((F) ((F) f).X[1]).X[0];
                                            if (((F) ((F) f).X[1]).X[1].isOne(ring)) {
                                                if (p.coeffs.length == 2 && p.powers[0] == 2) {
                                                    return new InverseLaplaceTransform((F) f).inverse_sin(ring);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (((F) f).X[1] instanceof Polynom) {
                                if (((Polynom) ((F) f).X[1]).coeffs.length == 2 && ((Polynom) ((F) f).X[1]).powers[0] == 2) {
                                    if(((Polynom) ((F) f).X[1]).coeffs[1].signum() == 1){
                                    return new InverseLaplaceTransform((F) f).inverse_cos(ring);
                                }}
                            }
                            if (((F) f).X[1] instanceof F) {
                                if (((F) ((F) f).X[1]).name == F.intPOW) {
                                    Polynom p = (Polynom) ((F) ((F) f).X[1]).X[0];
                                    if (((F) ((F) f).X[1]).X[1].isOne(ring)) {
                                        if (p.coeffs.length == 2 && p.powers[0] == 2) {
                                            return new InverseLaplaceTransform((F) f).inverse_cos(ring);
                                        } else {
                                            if (p.coeffs.length == 3) {
                                                if (p.powers[0] == 2) {
                                                    return new InverseLaplaceTransform((F) f).inverse_div(ring);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (((F) f).X[1] instanceof Polynom) {
                            int nn = ((Polynom) ((F) f).X[1]).powers.length / ((Polynom) ((F) f).X[1]).coeffs.length;
                            if ((((Polynom) ((F) f).X[1]).powers[nn - 1] > 1) && (((Polynom) ((F) f).X[1]).powers.length > 1)) {
                                return new InverseLaplaceTransform((F) f).inverse_sh();
                            } else {
                                if (((Polynom) ((F) f).X[1]).coeffs.length == 1) {
                                    return new InverseLaplaceTransform((F) f).pol_div5();
                                }
                            }
                        }
                        if (((F) f).X[1] instanceof F) {
                            if (((F) ((F) f).X[1]).X[0] instanceof Polynom) {
                                if (((Polynom) ((F) ((F) f).X[1]).X[0]).coeffs.length == 1) {
                                    return new InverseLaplaceTransform((F) f).pol_div5();
                                }
                            }
                        }
                        return new InverseLaplaceTransform((F) f).pol_div6();
                    }
                }
            }
        } else {
            if (((Polynom) (f)).powers.length == 0) {
                return new LaplaceTransform(new F(f)).transformNumber();
            } else {
                return new LaplaceTransform(new F(f)).transformPolynom();
            }
        }
        return new F(((F) f).name, newX);
    }
}
