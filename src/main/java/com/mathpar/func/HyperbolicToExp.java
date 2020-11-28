/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import com.mathpar.number.*;

/**
 * @author Мокрецова Людмила 35гр
 */
public class HyperbolicToExp extends F {

    /**
     * процедура заменяющая вхождений гиперболических функций на эквивалентные
     *
     * @param F func любая функция
     * @return F -преобразованная функция
     *
     */

    public static F h_transform(F func, Ring ring)   {


        switch (func.X.length) {
            case (0): {
                return func;
            }
            case (2): {
                switch (func.name) {
                    case (MULTIPLY): {
                        if (((((F) (func.X[0])).name == SH || ((F) (func.X[0])).name == CH) &
                                (((F) (func.X[1])).name == SH || ((F) (func.X[1])).name == CH)) ||
                                (((F) (func.X[0])).name == TH & ((F) (func.X[1])).name == TH)) {
                            return func = HyperbolicToExp.Multiplys(func);
                        } else {
                            func.X[0] = HyperbolicToExp.h_transform((F) (func.X[0]),ring);
                            func.X[1] = HyperbolicToExp.h_transform((F) (func.X[1]),ring);
                        }
                        break;
                    }
                    case (ADD): {
                        if ((((F) (func.X[0])).name == SH & ((F) (func.X[1])).name == SH) ||
                                (((F) (func.X[0])).name == CH & ((F) (func.X[1])).name == CH) ||
                                (((F) (func.X[0])).name == TH & ((F) (func.X[1])).name == TH)) {
                            return func = HyperbolicToExp.Summs(func);
                        } else {
                            func.X[0] = HyperbolicToExp.h_transform((F) (func.X[0]),ring);
                            func.X[1] = HyperbolicToExp.h_transform((F) (func.X[1]),ring);
                        }
                        break;
                    }
                    case (SUBTRACT): {
                        if ((((F) (func.X[0])).name == SH || ((F) (func.X[1])).name == SH) &
                                (((F) (func.X[0])).name == CH || ((F) (func.X[1])).name == CH) &
                                (((F) (func.X[0])).name == TH & ((F) (func.X[1])).name == TH)) {
                            return func = HyperbolicToExp.Summs(func);
                        } else {
                            func.X[0] = HyperbolicToExp.h_transform((F) (func.X[0]),ring);
                            func.X[1] = HyperbolicToExp.h_transform((F) (func.X[1]),ring);
                        }
                        break;
                    }
                    default: {
                        func.X[0] = HyperbolicToExp.h_transform((F) (func.X[0]),ring);
                        func.X[1] = HyperbolicToExp.h_transform((F) (func.X[1]),ring);
                    }
                }
            }

            case (1): {
                switch (func.name) {
                    case (SH): {
                        if (((F) (func.X[0])).X.length > 2) {
                            for (int j = 0; j < ((F) (func.X[0])).X.length; j++) {
                                ((F) (func.X[0])).X[j] = HyperbolicToExp.h_transform(((F) ((F) (func.X[0])).X[j]),ring);
                            }
                        }
                        if (((F) (func.X[0])).X.length == 2) {
                            switch (((F) (func.X[0])).name) {
                                case (ADD): {
                                    return func = HyperbolicToExp.add(func);
                                }
                                case (SUBTRACT): {
                                    return func = HyperbolicToExp.add(func);
                                }
                                case (MULTIPLY): {
                                    func = HyperbolicToExp.X_angle(func, ring);
                                    return func = HyperbolicToExp.double_angle(func);
                                }
                            }
                        }
                        if (((F) (func.X[0])).X.length == 1) {
                            func = HyperbolicToExp.sh(func,ring);
                            return func = HyperbolicToExp.D(func);
                        }
                        break;
                    }

                    case (CH): {
                        if (((F) (func.X[0])).X.length > 2) {
                            for (int j = 0; j < ((F) (func.X[0])).X.length; j++) {
                                ((F) (func.X[0])).X[j] = HyperbolicToExp.h_transform(((F) ((F) (func.X[0])).X[j]),ring);
                            }
                        }
                        if (((F) (func.X[0])).X.length == 2) {
                            switch (((F) (func.X[0])).name) {
                                case (ADD): {
                                    return func = HyperbolicToExp.add(func);
                                }
                                case (SUBTRACT): {
                                    return func = HyperbolicToExp.add(func);
                                }
                                case (MULTIPLY): {
                                    func = HyperbolicToExp.X_angle(func,ring);
                                    return func = HyperbolicToExp.double_angle(func);
                                }
                            }
                        }
                        if (((F) (func.X[0])).X.length == 1) {
                            func = HyperbolicToExp.ch(func,ring);
                            return func = HyperbolicToExp.D(func);
                        }
                        break;
                    }
                    case (TH): {
                        if (((F) (func.X[0])).X.length > 2) {
                            for (int j = 0; j < ((F) (func.X[0])).X.length; j++) {
                                ((F) (func.X[0])).X[j] = HyperbolicToExp.h_transform(((F) ((F) (func.X[0])).X[j]),ring);
                            }
                        }
                        if (((F) (func.X[0])).X.length == 2) {
                            switch (((F) (func.X[0])).name) {
                                case (ADD): {
                                    return func = HyperbolicToExp.add(func);
                                }
                                case (SUBTRACT): {
                                    return func = HyperbolicToExp.add(func);
                                }
                                case (MULTIPLY): {
                                    func = HyperbolicToExp.X_angle(func,ring);
                                    return func = HyperbolicToExp.double_angle(func);
                                }
                            }
                        }
                        if (((F) (func.X[0])).X.length == 1) {
                            func = HyperbolicToExp.th(func);
                            return func = HyperbolicToExp.D(func);
                        }
                        break;
                    }
                    case (CTH): {
                        if (((F) (func.X[0])).X.length > 2) {
                            for (int j = 0; j < ((F) (func.X[0])).X.length; j++) {
                                ((F) (func.X[0])).X[j] = HyperbolicToExp.h_transform(((F) ((F) (func.X[0])).X[j]),ring);
                            }
                        }
                        if (((F) (func.X[0])).X.length == 2) {
                            switch (((F) (func.X[0])).name) {
                                case (ADD): {
                                    return func = HyperbolicToExp.add(func);
                                }
                                case (SUBTRACT): {
                                    return func = HyperbolicToExp.add(func);
                                }
                                case (MULTIPLY): {
                                    func = HyperbolicToExp.X_angle(func,ring);
                                    return func = HyperbolicToExp.double_angle(func);
                                }
                            }
                        }
                        if (((F) (func.X[0])).X.length == 1) {
                            func = HyperbolicToExp.cth(func);
                            return func = HyperbolicToExp.D(func);
                        }
                        break;
                    }
                }
                break;
            }

            default: {
                for (int i = 0; i < func.X.length; i++) {
                    if (((F) func.X[i]).X.length > 3) {
                        for (int j = 0; j < func.X.length; j++) {
                            ((F) func.X[i]).X[j] = HyperbolicToExp.h_transform((F) ((F) func.X[i]).X[j],ring);
                        }
                    }
                    else{
                        func.X[i]=HyperbolicToExp.h_transform((F)func.X[i],ring);
                    }
                }
            }

        }
        return func;
    }

    /**
     *  гиперболический синус
     *
     * @param F func = sh(x)
     * @return F -преобразованная функция
     *
     */
    public static F sh(F func, Ring ring) {
        NumberR t = new NumberR(2);
        if (func.name != SH) {
            return func;
        }
        F z = new F(func.X[0]);
        F A = new F(EXP, new F[]{z});
        F B = new F(EXP, new F[]{(F)z.negate(ring)});
        F C = new F(SUBTRACT, new F[]{A, B});
        F div = new F(DIVIDE, new F[]{C, new F(t)});
        return div;
    }

    /**
     *  гиперболический косинус
     *
     * @param F func = ch(x)
     * @return F -преобразованная функция
     *
     */
    public static F ch(F func,Ring ring) {
        NumberR t = new NumberR(2);
        if (func.name != CH) {
            return func;
        }
        F z = new F(func.X[0]);
        F A = new F(EXP, new F[]{z});
        F B = new F(EXP, new F[]{(F)z.negate(ring)});
        F C = new F(ADD, new F[]{A, B});
        F div = new F(DIVIDE, new F[]{C, new F(t)});
        return div;
    }

    /**
     *  гиперболический тангенс
     *
     * @param F func = th(x)
     * @return F -преобразованная функция
     *
     */
    public static F th(F func) {
        if (func.name != TH) {
            return func;
        }
        F z11 = new F(func.X[0]);
        F a11 = new F(SH, new F[]{z11});
        F b11 = new F(CH, new F[]{z11});
        F c11 = new F(DIVIDE, new F[]{a11, b11});
        return c11;
    }

    /**
     *  гиперболический котангенс
     *
     * @param F func = cth(x)
     * @return F
     *
     */
    public static F cth(F func) {
        if (func.name != TH) {
            return func;
        }
        F z11 = new F(func.X[0]);
        F a11 = new F(SH, new F[]{z11});
        F b11 = new F(CH, new F[]{z11});
        F c11 = new F(DIVIDE, new F[]{b11, a11});
        return c11;
    }

    /**
     *  Тождество ch^2x - sh^2x= 1
     *
     * @param F func = ch^2x - sh^2x
     * @return F -преобразованная функция
     *
     */
    public static F togd1(F func) {
        F res = null;
        if (func.name == SUBTRACT) {
            if (((F) func.X[0]).name == POW) {
                if (((F) ((F) func.X[0]).X[0]).name == CH) {
                    if (((F) func.X[1]).name == POW) {
                        if (((F) ((F) func.X[1]).X[0]).name == SH) {
                            res = new F(NumberR.ONE);
                        }
                    }
                }
            }
        }
        return res;
    }

    /**
     *  Четность
     *
     * @param F func = ch(x),sh(x),th(x)
     * @return F -преобразованная функция
     *
     */
    public static F chet(F func, Ring ring) {
        F x = new F(func.X[0]);
        F res = null;
        switch (func.name) {
            case (SH): {
                res = (F)(new F(F.SH, new F[]{(F)x.negate(ring)})).negate(ring);
                break;
            }
            case (CH): {
                res = (F)(new F(F.CH, new F[]{(F)x.negate(ring)}));
                break;
            }
            case (TH): {
                res = (F)(new F(F.TH, new F[]{(F)x.negate(ring)})).negate(ring);
                break;
            }
            default:
                res = func;
        }
        return res;
    }

    /**
     *  Формулы сложения:
     *
     * @param F func = ch(x+-y),sh(x+-y),th(x+-y)
     * @return F -преобразованная функция
     *
     */
    public static F add(F func) {
        if (func.name == SH) {
            if (((F) (func.X[0])).X.length == 2) {
                if (((F) ((F) (func.X[0]))).name == ADD) {
                    F X = new F(((F) (func.X[0])).X[0]);
                    F Y = new F(((F) (func.X[0])).X[1]);
                    F shX = new F(F.SH, new F[]{X});
                    F shY = new F(F.SH, new F[]{Y});
                    F chX = new F(F.CH, new F[]{X});
                    F chY = new F(F.CH, new F[]{Y});
                    F m1 = new F(F.MULTIPLY, new F[]{shX, chY});
                    F m2 = new F(F.MULTIPLY, new F[]{shY, chX});
                    return func = new F(F.ADD, new F[]{m1, m2});
                }
                if (((F) ((F) (func.X[0]))).name == SUBTRACT) {
                    F X = new F(((F) (func.X[0])).X[0]);
                    F Y = new F(((F) (func.X[0])).X[1]);
                    F shX = new F(F.SH, new F[]{X});
                    F shY = new F(F.SH, new F[]{Y});
                    F chX = new F(F.CH, new F[]{X});
                    F chY = new F(F.CH, new F[]{Y});
                    F m1 = new F(F.MULTIPLY, new F[]{shX, chY});
                    F m2 = new F(F.MULTIPLY, new F[]{shY, chX});
                    return func = new F(F.SUBTRACT, new F[]{m1, m2});
                }
            }
        }
        if (func.name == CH) {
            if (((F) (func.X[0])).X.length == 2) {
                if (((F) ((F) (func.X[0]))).name == ADD) {
                    F X = new F(((F) (func.X[0])).X[0]);
                    F Y = new F(((F) (func.X[0])).X[1]);
                    F shX = new F(F.SH, new F[]{X});
                    F shY = new F(F.SH, new F[]{Y});
                    F chX = new F(F.CH, new F[]{X});
                    F chY = new F(F.CH, new F[]{Y});
                    F m1 = new F(F.MULTIPLY, new F[]{shX, shY});
                    F m2 = new F(F.MULTIPLY, new F[]{chY, chX});
                    return func = new F(F.ADD, new F[]{m1, m2});
                }
                if (((F) ((F) (func.X[0]))).name == SUBTRACT) {
                    F X = new F(((F) (func.X[0])).X[0]);
                    F Y = new F(((F) (func.X[0])).X[1]);
                    F shX = new F(F.SH, new F[]{X});
                    F shY = new F(F.SH, new F[]{Y});
                    F chX = new F(F.CH, new F[]{X});
                    F chY = new F(F.CH, new F[]{Y});
                    F m1 = new F(F.MULTIPLY, new F[]{shX, shY});
                    F m2 = new F(F.MULTIPLY, new F[]{chY, chX});
                    return func = new F(F.SUBTRACT, new F[]{m2, m1});
                }
            }
        }
        if (func.name == TH) {
            if (((F) (func.X[0])).X.length == 2) {
                if (((F) ((F) (func.X[0]))).name == ADD) {
                    F X = new F(((F) (func.X[0])).X[0]);
                    F Y = new F(((F) (func.X[0])).X[1]);
                    F thX = new F(F.TH, new F[]{X});
                    F thY = new F(F.TH, new F[]{Y});
                    F a1 = new F(F.ADD, new F[]{thX, thY});
                    F m1 = new F(F.MULTIPLY, new F[]{thX, thY});
                    F a2 = new F(F.ADD, new F[]{new F(NumberR.ONE), m1});
                    return func = new F(F.DIVIDE, new F[]{a1, a2});
                }
                if (((F) ((F) (func.X[0]))).name == SUBTRACT) {
                    F X = new F(((F) (func.X[0])).X[0]);
                    F Y = new F(((F) (func.X[0])).X[1]);
                    F thX = new F(F.TH, new F[]{X});
                    F thY = new F(F.TH, new F[]{Y});
                    F s1 = new F(F.SUBTRACT, new F[]{thX, thY});
                    F m1 = new F(F.MULTIPLY, new F[]{thX, thY});
                    F s2 = new F(F.SUBTRACT, new F[]{new F(NumberR.ONE), m1});
                    return func = new F(F.DIVIDE, new F[]{s1, s2});
                }
            }
        }
        return func;
    }

    /**
     *  Формулы двойного угла
     *
     * @param F func = ch(2x),sh(2x),th(2x),cth(2x),th(x),ch(2x)+-sh(2x)
     * @return F -преобразованная функция
     *
     */
    public static F double_angle(F func)   {
        F two = new F(new NumberR(2));
        if (func.name == SH) {
            if (((F) ((F) (func.X[0]))).name == MULTIPLY) {
                if (((F) ((F) ((F) (func.X[0]))).X[1]).equals(two)) {
                    F X = new F(((F) ((F) (func.X[0]))).X[1]);
                    F shx = new F(F.SH, new F[]{X});
                    F chx = new F(F.CH, new F[]{X});
                    return func = new F(F.MULTIPLY, new F[]{two, shx, chx});
                }
            }
        }
        if (func.name == CH) {
            if (((F) (func.X[0])).X.length == 2) {
                if (((F) ((F) (func.X[0]))).name == MULTIPLY) {
                    if (((F) ((F) (func.X[0]))).X[1].equals(two)) {
                        F X = new F(((F) ((F) (func.X[0]))).X[1]);
                        F shx = new F(F.SH, new F[]{X});
                        F chx = new F(F.CH, new F[]{X});
                        F pchx = new F(F.POW, new F[]{chx, two});
                        F pshx = new F(F.POW, new F[]{shx, two});
                        return func = new F(F.ADD, new F[]{pshx, pchx});
                    }
                }
            }
        }
        if (func.name == TH) {
            if (((F) (func.X[0])).X.length == 2) {
                if (((F) ((F) (func.X[0]))).name == MULTIPLY) {
                    if (((F) ((F) (func.X[0]))).X[1].equals(two)) {
                        F X = new F(((F) ((F) (func.X[0]))).X[1]);
                        F sh2x = new F(F.SH, new F[]{X, two});
                        F ch2x = new F(F.CH, new F[]{X, two});
                        F m1 = new F(F.SUBTRACT, new F[]{ch2x, new F(NumberR.ONE)});
                        return func = new F(F.DIVIDE, new F[]{m1, sh2x});
                    }
                }
            }
        }
        if (func.name == CTH) {
            if (((F) (func.X[0])).X.length == 2) {
                if (((F) ((F) (func.X[0]))).name == MULTIPLY) {
                    if (((F) ((F) (func.X[0]))).X[1].equals(two)) {
                        F X = new F(((F) ((F) (func.X[0]))).X[1]);
                        F thx = new F(F.TH, new F[]{X});
                        F cthx = new F(F.CTH, new F[]{X});
                        F half = new F(new NumberR(1 / 2));
                        F a1 = new F(F.ADD, new F[]{thx, cthx});
                        return func = new F(F.MULTIPLY, new F[]{a1, half});
                    }
                }
            }
        }
        if (func.name == TH) {
            F x2 = new F(F.MULTIPLY, new F[]{new F(func.X[0]), two});
            F ch2x = new F(F.CH, new F[]{x2});
            F sh2x = new F(F.SH, new F[]{x2});
            F s1 = new F(F.SUBTRACT, new F[]{ch2x, new F(NumberR.ONE)});
            return func = new F(F.DIVIDE, new F[]{s1, sh2x});
        }
        if (func.name == ADD) {
            if (((F) (func.X[0])).name == CH) {
                if (((F) (func.X[1])).name == SH) {
                    if (((F) ((F) ((F) (func.X[0]))).X[0]).name == MULTIPLY) {
                        if (((F) ((F) ((F) (func.X[1]))).X[0]).name == MULTIPLY) {
                            if ((((F) ((F) ((F) (func.X[0]))).X[0]).X[1]).equals(two)) {
                                if ((((F) ((F) ((F) (func.X[1]))).X[0]).X[1]).equals(two)) {
                                    F x = new F((((F) ((F) ((F) (func.X[0]))).X[0]).X[0]));
                                    F chx = new F(F.CH, new F[]{x});
                                    F shx = new F(F.SH, new F[]{x});
                                    F a1 = new F(F.ADD, new F[]{shx, chx});
                                    return func = new F(F.POW, new F[]{a1, two});
                                }
                            }
                        }
                    }
                }
            }
        }
        if (func.name == SUBTRACT) {
            if (((F) (func.X[0])).name == CH) {
                if (((F) (func.X[1])).name == SH) {
                    if (((F) ((F) ((F) (func.X[0]))).X[0]).name == MULTIPLY) {
                        if (((F) ((F) ((F) (func.X[1]))).X[0]).name == MULTIPLY) {
                            if ((((F) ((F) ((F) (func.X[0]))).X[0]).X[1]).equals(two)) {
                                if ((((F) ((F) ((F) (func.X[1]))).X[0]).X[1]).equals(two)) {
                                    F x = new F((((F) ((F) ((F) (func.X[0]))).X[0]).X[0]));
                                    F chx = new F(F.CH, new F[]{x});
                                    F shx = new F(F.SH, new F[]{x});
                                    F s1 = new F(F.SUBTRACT, new F[]{shx, chx});
                                    return func = new F(F.POW, new F[]{s1, two});
                                }
                            }
                        }
                    }
                }
            }
        }
        return func;
    }

    /**
     *  Формулы кратных углов
     *
     * @param F func = ch(3x),sh(3x),th(3x),ch(5x),sh(5x),th(5x)
     * @return F -преобразованная функция
     *
     */
    public static F X_angle(F func, Ring ring) {
        F one = new F(new NumberR(1));
        F two = new F(new NumberR(2));
        F three = new F(new NumberR(3));
        F four = new F(new NumberR(4));
        F five = new F(new NumberR(5));
        switch (func.name) {
            case (SH): {
                if (((F) ((F) (func.X[0]))).name == MULTIPLY) {
                    if (((F) ((F) ((F) (func.X[0]))).X[1]).equals(three)) {
                        F X = new F(((F) ((F) (func.X[0]))).X[0]);
                        F shx = new F(F.SH, new F[]{X});
                        F pshx = new F(F.POW, new F[]{shx, three});
                        F m1 = new F(F.MULTIPLY, new F[]{four, pshx});
                        F m2 = new F(F.MULTIPLY, new F[]{three, shx});
                        return func = new F(F.ADD, new F[]{m1, m2});
                    }
                    if (((F) ((F) ((F) (func.X[0]))).X[1]).equals(five)) {
                        F X = new F(((F) ((F) (func.X[0]))).X[0]);
                        F shx = new F(F.SH, new F[]{X});
                        F p5sh = new F(F.POW, new F[]{shx, five});
                        F m1 = new F(F.MULTIPLY, new F[]{new F(new NumberR(16)), p5sh});
                        F p3sh = new F(F.POW, new F[]{shx, three});
                        F m2 = new F(F.MULTIPLY, new F[]{new F(new NumberR(20)), p3sh});
                        F m3 = new F(F.MULTIPLY, new F[]{new F(new NumberR(5)), shx});
                        return func = new F(F.ADD, new F[]{m1, m2, m3});
                    }
                }
                break;
            }
            case (CH): {
                if (((F) ((F) (func.X[0]))).name == MULTIPLY) {
                    if (((F) ((F) ((F) (func.X[0]))).X[1]).equals(three)) {
                        F X = new F(((F) ((F) (func.X[0]))).X[0]);
                        F chx = new F(F.CH, new F[]{X});
                        F pchx = new F(F.POW, new F[]{chx, three});
                        F m1 = new F(F.MULTIPLY, new F[]{four, pchx});
                        F m2 = new F(F.MULTIPLY, new F[]{three, chx});
                        return func = new F(F.SUBTRACT, new F[]{m1, m2});
                    }
                    if (((F) ((F) ((F) (func.X[0]))).X[1]).equals(five)) {
                        F X = new F(((F) ((F) (func.X[0]))).X[0]);
                        F chx = new F(F.CH, new F[]{X});
                        F p5ch = new F(F.POW, new F[]{chx, five});
                        F m1 = new F(F.MULTIPLY, new F[]{new F(new NumberR(16)), p5ch});
                        F p3ch = new F(F.POW, new F[]{chx, three});
                        F m2 = new F(F.MULTIPLY, new F[]{new F(new NumberR(20)), p3ch});
                        F m3 = new F(F.MULTIPLY, new F[]{new F(new NumberR(5)), chx});
                        return func = new F(F.ADD, new F[]{m1, (F)m2.negate(ring), m3});
                    }
                }
                break;
            }
            case (TH): {
                if (((F) ((F) (func.X[0]))).name == MULTIPLY) {
                    if (((F) ((F) ((F) (func.X[0]))).X[1]).equals(three)) {
                        F X = new F(((F) ((F) (func.X[0]))).X[0]);
                        F thx = new F(F.TH, new F[]{X});
                        F pthx = new F(F.POW, new F[]{thx, two});
                        F a1 = new F(F.ADD, new F[]{three, pthx});
                        F m1 = new F(F.MULTIPLY, new F[]{three, pthx});
                        F a2 = new F(F.ADD, new F[]{one, m1});
                        F d1 = new F(F.DIVIDE, new F[]{a1, a2});
                        return func = new F(F.MULTIPLY, new F[]{thx, d1});
                    }
                    if (((F) ((F) ((F) (func.X[0]))).X[1]).equals(five)) {
                        F X = new F(((F) ((F) (func.X[0]))).X[0]);
                        F thx = new F(F.TH, new F[]{X});
                        F p2th = new F(F.POW, new F[]{thx, two});
                        F p4th = new F(F.POW, new F[]{thx, four});
                        F m1 = new F(F.MULTIPLY, new F[]{new F(new NumberR(20)), p2th});
                        F a1 = new F(F.ADD, new F[]{p4th, m1, five});
                        F m2 = new F(F.MULTIPLY, new F[]{new F(new NumberR(5)), p4th});
                        F m3 = new F(F.MULTIPLY, new F[]{new F(new NumberR(10)), p2th});
                        F a2 = new F(F.ADD, new F[]{m2, m3, one});
                        F d1 = new F(F.DIVIDE, new F[]{a1, a2});
                        return func = new F(F.MULTIPLY, new F[]{thx, d1});
                    }
                    break;
                }
            }
        }
        return func;
    }

    /**
     *  Произведения
     *
     * @param F func = ch(x)*ch(x),ch(x)*sh(x),sh(x)*sh(x),th(x)*th(x)
     * @return F -преобразованная функция
     *
     */
    public static F Multiplys(F func) {
        F two = new F(new NumberR(2));
        if (func.name == MULTIPLY) {
            if ((((F) (func.X[0])).name == SH) && (((F) (func.X[1])).name == SH)) {
                F x = (F) (((F) (func.X[0])).X[0]);
                F y = (F) (((F) (func.X[1])).X[0]);
                F a1 = new F(F.ADD, new F[]{x, y});
                F s1 = new F(F.SUBTRACT, new F[]{x, y});
                F cha = new F(F.CH, new F[]{a1});
                F chs = new F(F.CH, new F[]{s1});
                F s2 = new F(F.SUBTRACT, new F[]{cha, chs});
                return func = new F(F.DIVIDE, new F[]{s2, two});
            }
            if ((((F) (func.X[0])).name == SH) && (((F) (func.X[1])).name == CH)) {
                F x = (F) (((F) (func.X[0])).X[0]);
                F y = (F) (((F) (func.X[1])).X[0]);
                F a1 = new F(F.ADD, new F[]{x, y});
                F s1 = new F(F.SUBTRACT, new F[]{x, y});
                F sha = new F(F.SH, new F[]{a1});
                F shs = new F(F.SH, new F[]{s1});
                F a2 = new F(F.ADD, new F[]{sha, shs});
                return func = new F(F.DIVIDE, new F[]{a2, two});
            }
            if ((((F) (func.X[0])).name == CH) && (((F) (func.X[1])).name == CH)) {
                F x = (F) (((F) (func.X[0])).X[0]);
                F y = (F) (((F) (func.X[1])).X[0]);
                F a1 = new F(F.ADD, new F[]{x, y});
                F s1 = new F(F.SUBTRACT, new F[]{x, y});
                F cha = new F(F.CH, new F[]{a1});
                F chs = new F(F.CH, new F[]{s1});
                F a2 = new F(F.ADD, new F[]{cha, chs});
                return func = new F(F.DIVIDE, new F[]{a2, two});
            }
            if ((((F) (func.X[0])).name == TH) && (((F) (func.X[1])).name == TH)) {
                F x = (F) (((F) (func.X[0])).X[0]);
                F y = (F) (((F) (func.X[1])).X[0]);
                F a1 = new F(F.ADD, new F[]{x, y});
                F s1 = new F(F.SUBTRACT, new F[]{x, y});
                F cha = new F(F.CH, new F[]{a1});
                F chs = new F(F.CH, new F[]{s1});
                F s2 = new F(F.SUBTRACT, new F[]{cha, chs});
                F a2 = new F(F.ADD, new F[]{cha, chs});
                return func = new F(F.DIVIDE, new F[]{s2, a2});
            }
        }
        return func;
    }

    /**
     *  Суммы
     *
     * @param F func = ch(x)+-ch(y),ch(x)+-sh(y),sh(x)+-sh(y),th(x)+-th(y)
     * @return F -преобразованная функция
     *
     */
    public static F Summs(F func) {
        F two = new F(new NumberR(2));

        if (func.name == ADD) {
            if ((((F) (func.X[0])).name == SH) && (((F) (func.X[1])).name == SH)) {
                F x = (F) (((F) (func.X[0])).X[0]);
                F y = (F) (((F) (func.X[1])).X[0]);
                F a1 = new F(F.ADD, new F[]{x, y});
                F s1 = new F(F.SUBTRACT, new F[]{x, y});
                F d1 = new F(F.DIVIDE, new F[]{a1, two});
                F d2 = new F(F.DIVIDE, new F[]{s1, two});
                F sh = new F(F.SH, new F[]{d1});
                F ch = new F(F.CH, new F[]{d2});
                return func = new F(F.MULTIPLY, new F[]{two, sh, ch});
            }
            if ((((F) (func.X[0])).name == CH) && (((F) (func.X[1])).name == CH)) {
                F x = (F) (((F) (func.X[0])).X[0]);
                F y = (F) (((F) (func.X[1])).X[0]);
                F a1 = new F(F.ADD, new F[]{x, y});
                F s1 = new F(F.SUBTRACT, new F[]{x, y});
                F d1 = new F(F.DIVIDE, new F[]{a1, two});
                F d2 = new F(F.DIVIDE, new F[]{s1, two});
                F ch1 = new F(F.CH, new F[]{d1});
                F ch2 = new F(F.CH, new F[]{d2});
                return func = new F(F.MULTIPLY, new F[]{two, ch1, ch2});
            }
            if ((((F) (func.X[0])).name == TH) && (((F) (func.X[1])).name == TH)) {
                F x = (F) (((F) (func.X[0])).X[0]);
                F y = (F) (((F) (func.X[1])).X[0]);
                F a1 = new F(F.ADD, new F[]{x, y});
                F sh = new F(F.SH, new F[]{a1});
                F chx = new F(F.SH, new F[]{x});
                F chy = new F(F.SH, new F[]{y});
                F m1 = new F(F.MULTIPLY, new F[]{chx, chy});
                return func = new F(F.DIVIDE, new F[]{sh, m1});
            }
        }
        if (func.name == SUBTRACT) {
            if ((((F) (func.X[0])).name == SH) && (((F) (func.X[1])).name == SH)) {
                F x = (F) (((F) (func.X[0])).X[0]);
                F y = (F) (((F) (func.X[1])).X[0]);
                F a1 = new F(F.ADD, new F[]{x, y});
                F s1 = new F(F.SUBTRACT, new F[]{x, y});
                F d1 = new F(F.DIVIDE, new F[]{a1, two});
                F d2 = new F(F.DIVIDE, new F[]{s1, two});
                F sh = new F(F.SH, new F[]{d2});
                F ch = new F(F.CH, new F[]{d1});
                return func = new F(F.MULTIPLY, new F[]{two, sh, ch});
            }
            if ((((F) (func.X[0])).name == CH) && (((F) (func.X[1])).name == CH)) {
                F x = (F) (((F) (func.X[0])).X[0]);
                F y = (F) (((F) (func.X[1])).X[0]);
                F a1 = new F(F.ADD, new F[]{x, y});
                F s1 = new F(F.SUBTRACT, new F[]{x, y});
                F d1 = new F(F.DIVIDE, new F[]{a1, two});
                F d2 = new F(F.DIVIDE, new F[]{s1, two});
                F sh1 = new F(F.SH, new F[]{d1});
                F sh2 = new F(F.SH, new F[]{d2});
                return func = new F(F.MULTIPLY, new F[]{two, sh1, sh2});
            }
            if ((((F) (func.X[0])).name == TH) && (((F) (func.X[1])).name == TH)) {
                F x = (F) (((F) (func.X[0])).X[0]);
                F y = (F) (((F) (func.X[1])).X[0]);
                F s1 = new F(F.SUBTRACT, new F[]{x, y});
                F sh = new F(F.SH, new F[]{s1});
                F chx = new F(F.SH, new F[]{x});
                F chy = new F(F.SH, new F[]{y});
                F m1 = new F(F.MULTIPLY, new F[]{chx, chy});
                return func = new F(F.DIVIDE, new F[]{sh, m1});
            }
        }
        return func;
    }

    /**
     *  Формулы понижения степени
     *
     * @param F func = (ch(x/2))^2, (sh(x/2))^2,(chx+1)/2,(shx-1)/2
     * @return F -преобразованная функция
     *
     */
    public static F low_pow(F func)   {
        F one = new F(new NumberR(1));
        F two = new F(new NumberR(2));
        F res = null;
        if (func.name == POW) {
            if ((((F) (func.X[0])).name == CH) && (((F) (func.X[1])).equals(new F(new NumberR(2)))) && (((F) (((F) (((F) (func.X[0])).X[0]))).X[1]).equals(new F(new NumberR(2))))) {
                F x = ((F) ((F) (((F) (func.X[0])).X[0])).X[0]);
                F ch = new F(F.CH, new F[]{x});
                F a1 = new F(F.ADD, new F[]{ch, one});
                res = new F(F.DIVIDE, new F[]{a1, two});
            }
            if ((((F) (func.X[0])).name == SH) && (((F) (func.X[1])).equals(new F(new NumberR(2)))) && (((F) (((F) (((F) (func.X[0])).X[0]))).X[1]).equals(new F(new NumberR(2))))) {
                F x = ((F) ((F) (((F) (func.X[0])).X[0])).X[0]);
                F ch = new F(F.CH, new F[]{x});
                F s1 = new F(F.SUBTRACT, new F[]{ch, one});
                res = new F(F.DIVIDE, new F[]{s1, two});
            }
        }
        if (func.name == DIVIDE) {
            if (((F) (func.X[0])).X.length == 2) {
                if (((F) (func.X[0])).name == ADD) {
                    if ((((F) ((F) (func.X[0])).X[0])).name == CH & (((F) (func.X[0])).X[1]).equals(new F(new NumberR(1)))) {
                        F arg = (F) (((F) ((F) (func.X[0])).X[0])).X[0];
                        F half_arg = new F(F.DIVIDE, new F[]{arg, new F(new NumberR(2))});
                        F ch2 = new F(F.CH, new F[]{half_arg});
                        return new F(F.POW, new F[]{ch2, new F(new NumberR(2))});
                    }
                }
                if (((F) (func.X[0])).name == SUBTRACT) {
                    if ((((F) ((F) (func.X[0])).X[0])).name == SH & (((F) (func.X[0])).X[1]).equals(new F(new NumberR(1)))) {
                        F arg = (F) (((F) ((F) (func.X[0])).X[0])).X[0];
                        F half_arg = new F(F.DIVIDE, new F[]{arg, new F(new NumberR(2))});
                        F sh2 = new F(F.SH, new F[]{half_arg});
                        return new F(F.POW, new F[]{sh2, new F(new NumberR(2))});
                    }

                }
            }
        }
        return res;
    }

    /**
     *  Производные
     *
     * @param F func = (ch(x))',(sh(x))',(th(x))',ch(x),sh(x),th(x)
     * @return F -преобразованная функция
     *
     */
    public static F D(F func) {
        F one = new F(new NumberR(1));
        F two = new F(new NumberR(2));
        F zero = new F(new NumberR(0));
        if (func.name == D) {
            F arg = (F) (((F) (func.X[0])).X[0]);
            switch (((F) (func.X[0])).name) {
                case (SH): {
                    return func = new F(F.CH, new F[]{arg});
                }
                case (CH): {
                    return func = new F(F.SH, new F[]{arg});
                }
                case (TH): {
                    F ch = new F(F.CH, new F[]{arg});
                    F pch = new F(F.POW, new F[]{ch, two});
                    return func = new F(F.DIVIDE, new F[]{one, pch});
                }
                default:
                    return func;
            }
        } else {
            F arg = (F) (func.X[0]);
            switch (func.name) {
                case (SH): {
                    F ch = new F(F.CH, new F[]{arg});
                    return func = new F(F.INT, new F[]{zero, arg, ch, arg});
                }
                case (CH): {
                    F sh = new F(F.SH, new F[]{arg});
                    F in = new F(F.INT, new F[]{zero, arg, sh, arg});
                    return func = new F(F.ADD, new F[]{one, in});
                }
                case (TH): {
                    F ch = new F(F.CH, new F[]{arg});
                    F pch = new F(F.POW, new F[]{ch, two});
                    F d = new F(F.DIVIDE, new F[]{one, pch});
                    return func = new F(F.INT, new F[]{zero, arg, d, arg});
                }
            }
        }
        return func;
    }
}





























































