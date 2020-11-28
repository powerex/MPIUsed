/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

//import com.sun.org.apache.bcel.internal.classfile.Code;
import com.mathpar.number.Ring;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 *
 * @author B02-R427-N006
 */
public class SimpleIdentity extends F {

 //   static private Ring ring = Ring.defaultxyzRing;

    public SimpleIdentity() {
    }

    public SimpleIdentity(F f, Ring ring) {
        this.name = f.name;
        this.X = f.X;
    }

    /**Процедура преобразования функции arccos (z)
     * в функцию i*arcch(z)
     * @param f - входная функция вида arccos (z)
     * @return возвращает функцию i*arcch(z)
     */
    public static F transformArccosForArcch(F f, Ring ring) {

        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);

        if (f.name != F.ARCCOS) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F A = new F(F.ARCCH, z);
            F mul = new F(F.MULTIPLY, new F[]{i, A});
            return mul;
        }
    }

    /**Процедура преобразования функции arcsin(z)
     * в функцию -i*arcsh(i*z)
     * @param f - входная функция вида arcsin(z)
     * @return возвращает функцию -i*arcsh(i*z)
     */
    public static F transformArcsinForArcsh(F f, Ring ring) {

        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.ARCSIN) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F _z = new F(F.MULTIPLY, new F[]{i, z});
            F A = new F(F.ARCSH, _z);
            F mul = new F(F.MULTIPLY, new F[]{mines_i, A});
            return mul;

        }
    }

    /**Процедура преобразования функции arctg(z)
     * в функцию -i*arcth(i*z)
     * @param f - входная функция вида arctg(z)
     * @return возвращает функцию -i*arcth(i*z)
     */
    public static F transformArctgForArcth(F f, Ring ring) {

        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.ARCTG) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F _z = new F(F.MULTIPLY, new F[]{i, z});
            F A = new F(F.ARCTGH, _z);
            F mul = new F(F.MULTIPLY, new F[]{mines_i, A});
            return mul;
        }
    }

    /**Процедура преобразования функции arcctg(z)
     * в функцию i*arccth(i*z)
     * @param f - входная функция вида arcctg(z)
     * @return возвращает функцию i*arccth(i*z)
     */
    public static F transformArcctgForArcth(F f, Ring ring) {

        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        if (f.name != F.ARCCTG) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F _z = new F(F.MULTIPLY, new F[]{i, z});
            F A = new F(F.ARCTGH, _z);
            F mul = new F(F.MULTIPLY, new F[]{i, A});
            return mul;
        }
    }

    /**Процедура преобразования функции arcch(z)
     * в функцию i*arccos(i*z)
     * @param f - входная функция arcch(z)
     * @return возвращает функцию i*arccos(i*z)
     */
    public static F transformArcchForArccos(F f, Ring ring) {

        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        if (f.name != F.ARCCH) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F A = new F(F.ARCCOS, z);
            F mul = new F(F.MULTIPLY, new F[]{i, A});
            return mul;
        }
    }

    /**Процедура преобразования функции arcsh(z)
     * в функцию -i*arcsin(i*z)
     * @param f - входная функция arcsh(z)
     * @return возвращает функцию -i*arcsin(i*z)
     */
    public static F transformArcshForArcsin(F f, Ring ring) {

        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.ARCSH) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F _z = new F(F.MULTIPLY, new F[]{i, z});
            F A = new F(F.ARCSIN, _z);
            F mul = new F(F.MULTIPLY, new F[]{mines_i, A});
            return mul;
        }
    }

    /**Процедура преобразования функции arcth(z)
     *в функцию -i*arctg(i*z)
     * @param f - входная функция arcth(z)
     * @return возвращает функцию -i*arctg(i*z)
     */
    public static F transformArcthForArctg(F f, Ring ring) {

        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.ARCTGH) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F _z = new F(F.MULTIPLY, new F[]{i, z});
            F A = new F(F.ARCTG, _z);
            F mul = new F(F.MULTIPLY, new F[]{mines_i, A});
            return mul;
        }
    }

    /**Процедура преобразования функции arccth(z)
     * в функцию i*arcctg(i*z)
     * @param f - входная функция arccth(z)
     * @return возвращает функцию i*arcctg(i*z)
     */
    public static F transformArccthForArcctg(F f, Ring ring) {

        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        if (f.name != F.ARCCTGH) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F _z = new F(F.MULTIPLY, new F[]{i, z});
            F A = new F(F.ARCCTG, _z);
            F mul = new F(F.MULTIPLY, new F[]{i, A});
            return mul;
        }
    }

    /**Процедура преобразования функции arccos(z)
     * в функцию -i*ln(z + i√(1-z^2))
     * @param f - входная функция arccos(z)
     * @return возвращает функцию -i*ln(z + i√(1-z^2))
     */
    public static F transformArccosForLn(F f, Ring ring) {
        NumberZ t = new NumberZ(2);
        NumberZ one = new NumberZ(1);
        //F two = new F("2");
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.ARCCOS) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F z2 = new F(F.POW, new Element[]{z, Polynom.polynomFromNot0Number(t)});
            F sub = new F(F.SUBTRACT, new Element[]{Polynom.polynomFromNot0Number(one), z2});
            F b = new F(F.DIVIDE, new Element[]{Polynom.polynomFromNot0Number(one),
                        Polynom.polynomFromNot0Number(t)
                    });
            F a = new F(F.POW, new Element[]{sub, b});
            F mul = new F(F.MULTIPLY, new F[]{i, a});
            F add = new F(F.ADD, new F[]{z, mul});
            F ln = new F(F.LN, new F[]{add});
            F _mul = new F(F.MULTIPLY, new F[]{mines_i, ln});
            return _mul;
        }
    }

    /**Процедура преобразования функции arcsin(z)
     * в функцию  -i*ln(i*z + √(1 – z^2))
     * @param f - входная функция arcsin(z)
     * @return возвращает функцию  -i*ln(i*z + √(1 – z^2))
     */
    public static F transformArcsinForLn(F f, Ring ring) {
        NumberZ one = new NumberZ(1);
        NumberZ t = new NumberZ(2);
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.ARCSIN) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F z2 = new F(F.POW, new Element[]{z, Polynom.polynomFromNot0Number(t)});
            F sub = new F(F.SUBTRACT, new Element[]{Polynom.polynomFromNot0Number(one), z2});
            F b = new F(F.DIVIDE, new Element[]{Polynom.polynomFromNot0Number(one),
                        Polynom.polynomFromNot0Number(t)
                    });
            F a = new F(F.POW, new Element[]{sub, b});
            F _z = new F(F.MULTIPLY, new F[]{i, z});
            F add = new F(F.ADD, new F[]{_z, a});
            F ln = new F(F.LN, new F[]{add});
            F _mul = new F(F.MULTIPLY, new F[]{mines_i, ln});
            return _mul;
        }
    }

    /**Процедура преобразования функции arctg(z)
     * в функцию -i/2*ln((1+i*z)/(1-i*z))
     * @param f - входная функция arctg(z)
     * @return возвращает функцию -i/2*ln((1+i*z)/(1-i*z))
     */
    public static F transformArctgForLn(F f, Ring ring) {
        NumberZ one = new NumberZ(1);
        NumberZ t = new NumberZ(2);
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.ARCTG) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F _z = new F(F.MULTIPLY, new F[]{i, z});
            F sub = new F(F.SUBTRACT, new Element[]{Polynom.polynomFromNot0Number(one), _z});
            F add = new F(F.ADD, new Element[]{Polynom.polynomFromNot0Number(one), _z});
            F div = new F(F.DIVIDE, new F[]{add, sub});
            F ln = new F(F.LN, new F[]{div});
            F a = new F(F.DIVIDE, new Element[]{mines_i, Polynom.polynomFromNot0Number(t)});
            F mul = new F(F.MULTIPLY, new F[]{a, ln});
            return mul;
        }
    }

    /**Процедура преобразования функции arcctg(z)
     *в функцию  -i/2*ln((i*z-1)/(i*z+1))
     * @param f - входная функция arcctg(z)
     * @return возвращает функцию -i/2*ln((i*z-1)/(i*z+1))
     */
    public static F transformArcctgForLn(F f, Ring ring) {
        NumberZ one = new NumberZ(1);
        NumberZ t = new NumberZ(2);
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.ARCCTG) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F _z = new F(F.MULTIPLY, new F[]{i, z});
            F sub = new F(F.SUBTRACT, new Element[]{_z, Polynom.polynomFromNot0Number(one)});
            F add = new F(F.ADD, new Element[]{_z, Polynom.polynomFromNot0Number(one)});
            F div = new F(F.DIVIDE, new F[]{sub, add});
            F ln = new F(F.LN, new F[]{div});
            F a = new F(F.DIVIDE, new Element[]{mines_i, Polynom.polynomFromNot0Number(t)});
            F mul = new F(F.MULTIPLY, new F[]{a, ln});
            return mul;
        }
    }

    /**Процедура преобразования функции arcch(z)
     * в функцию ln(z + √(z^2-1))
     * @param f - входная функция arcch(z)
     * @return возвращает функцию ln(z + √(z^2-1))
     */
    public static F transformArcchForLn(F f, Ring ring) {
        NumberZ one = new NumberZ(1);
        NumberZ t = new NumberZ(2);
        if (f.name != F.ARCCH) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F z2 = new F(F.POW, new Element[]{z, Polynom.polynomFromNot0Number(t)});
            F sub = new F(F.SUBTRACT, new Element[]{z2, Polynom.polynomFromNot0Number(one)});
            F b = new F(F.DIVIDE, new Element[]{Polynom.polynomFromNot0Number(one),
                        Polynom.polynomFromNot0Number(t)
                    });
            F a = new F(F.POW, new Element[]{sub, b});
            F add = new F(F.ADD, new F[]{z, a});
            F ln = new F(F.LN, new F[]{add});
            return ln;
        }
    }

    /**Процедура преобразования функции arcsh(z)
     * в функцию  ln(z + √(z^2+1))
     * @param f- входная функция arcsh(z)
     * @return возвращает функцию ln(z + √(z^2+1))
     */
    public static F transformArcshForLn(F f, Ring ring) {
        NumberZ one = new NumberZ(1);
        NumberZ t = new NumberZ(2);
        if (f.name != F.ARCSH) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F z2 = new F(F.POW, new Element[]{z, Polynom.polynomFromNot0Number(t)});
            F sub = new F(F.SUBTRACT, new Element[]{z2, Polynom.polynomFromNot0Number(one)});
            F b = new F(F.DIVIDE, new Element[]{Polynom.polynomFromNot0Number(one),
                        Polynom.polynomFromNot0Number(t)
                    });
            F a = new F(F.POW, new Element[]{sub, b});
            F add = new F(F.ADD, new F[]{z, a});
            F ln = new F(F.LN, new F[]{add});
            return ln;
        }
    }

    /**Процедура преобразования функции arcth(z)
     * в функцию  ½*ln((1+z)/(1-z))
     * @param f - входная функция arcth(z)
     * @return возвращает функцию  ½*ln((1+z)/(1-z))
     */
    public static F transformArcthForLn(F f, Ring ring) {
        NumberZ one = new NumberZ(1);
        NumberZ t = new NumberZ(2);
        if (f.name != F.ARCTGH) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F sub = new F(F.SUBTRACT, new Element[]{Polynom.polynomFromNot0Number(one), z});
            F add = new F(F.ADD, new Element[]{Polynom.polynomFromNot0Number(one), z});
            F div = new F(F.DIVIDE, new F[]{add, sub});
            F ln = new F(F.LN, new F[]{div});
            F a = new F(F.DIVIDE, new Element[]{Polynom.polynomFromNot0Number(one), Polynom.polynomFromNot0Number(t)});
            F mul = new F(F.MULTIPLY, new F[]{a, ln});
            return mul;
        }
    }

    /**Процедура преобразования функции arccth(z)
     * в функцию  ½*ln((z+1)/(z-1))
     * @param f - входная функция arccth(z)
     * @return возвращает функцию ½*ln((z+1)/(z-1))
     */
    public static F transformArccthForLn(F f, Ring ring) {
        NumberZ one = new NumberZ(1);
        NumberZ t = new NumberZ(2);
        if (f.name != F.ARCCTGH) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F sub = new F(F.SUBTRACT, new Element[]{z, Polynom.polynomFromNot0Number(one)});
            F add = new F(F.ADD, new Element[]{z, Polynom.polynomFromNot0Number(one)});
            F div = new F(F.DIVIDE, new F[]{add, sub});
            F ln = new F(F.LN, new F[]{div});
            F a = new F(F.DIVIDE, new Element[]{Polynom.polynomFromNot0Number(one), Polynom.polynomFromNot0Number(t)});
            F mul = new F(F.MULTIPLY, new F[]{a, ln});
            return mul;
        }
    }

    /**Процедура преобразования функции i*arch(z)
     * в функцию arccos (z)
     * @param f - на входе функция i*arch(z)
     * @return возвращает arccos(z)
     */
    public static F transformIArchForArccos(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        if (f.name != F.MULTIPLY) {
            return f;
        } else {

            if (((F) f.X[0]).compareTo(i, ring) != 0) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.ARCCH) {
                    return f;
                } else {

                    F z = new F(((F) f.X[1]).X[0]);
                    F a = new F(F.ARCCOS, new F[]{z});
                    return a;
                }
            }
        }
    }

    /**Процедура преобразования функции -i*arcsh (i*z)
     * в функцию arcsin(z)
     * @param f - на входе функция -i*arcsh (i*z)
     * @return возвращает arcsin(z)
     */
    public static F transform_IArshForArcsin(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(mines_i, ring) != 0) {

                return f;
            } else {
                if (((F) f.X[1]).name != F.ARCSH) {
                    return f;
                } else {
                    if ((((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])))).compareTo(mines_i, ring) != 0) {
                            return f;
                        } else {
                            F z = new F(((F) ((F) f.X[1]).X[0]).X[1]);
                            F a = new F(F.ARCSIN, new F[]{z});
                            return a;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции -i*arcth (i*z)
     * в функцию arctg (z)
     * @param f - на входе функция -i*arcth (i*z)
     * @return возвращает arctg (z)
     */
    public static F transform_IArthForArctg(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if ((((F) f.X[0]).compareTo(mines_i, ring) != 0)) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.ARCTGH) {
                    return f;
                } else {
                    if ((((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])))).compareTo(mines_i, ring) != 0) {
                            return f;
                        } else {
                            F z = new F(((F) ((F) f.X[1]).X[0]).X[1]);
                            F a = new F(F.ARCTG, new F[]{z});
                            return a;
                        }
                    }
                }
            }
        }
    }

    /**Процедура прелбразования функции  i*arccth (i*z)
     * в функцию arcctg (z)
     * @param f - на входе функция  i*arccth (i*z)
     * @return возвращает arcctg (z)
     */
    public static F transform_IArcthForArcctg(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if ((((F) f.X[0]).compareTo(i,ring) != 0)) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.ARCCTGH) {
                    return f;
                } else {
                    if ((((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])))).compareTo(i,ring) != 0) {
                            return f;
                        } else {
                            F z = new F(((F) ((F) f.X[1]).X[0]).X[1]);
                            F a = new F(F.ARCCTG, new F[]{z});
                            return a;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции i*arcos (z)
     * возвращает функцию arcch(z)
     * @param f - на входе функция i*arcos (z)
     * @return возвращает arcch(z)
     */
    public static F transform_IArccosForArcch(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if ((((F) f.X[0]).compareTo(i,ring) != 0)) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.ARCCOS) {
                    return f;
                } else {
                    F z = new F(((F) f.X[1]).X[0]);
                    F a = new F(F.ARCCH, new F[]{z});
                    return a;
                }
            }
        }
    }

    /**Процедура преобразования функции -i* arcsin (i*z)
     * в функцию arcsh (z)
     * @param f - на входе функция -i* arcsin (i*z)
     * @return возвращает arcsh (z)
     */
    public static F transform_IArcsinForArcsh(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if ((((F) f.X[0]).compareTo(mines_i, ring) != 0)) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.ARCSIN) {
                    return f;
                } else {
                    if ((((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])))).compareTo(mines_i, ring) != 0) {
                            return f;
                        } else {
                            F z = new F(((F) ((F) f.X[1]).X[0]).X[1]);
                            F a = new F(F.ARCSH, new F[]{z});
                            return a;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции -i*arctg (i*z)
     * в функцию arcth (z)
     * @param f - на входе функция -i*arctg (i*z)
     * @return возвращает arcth (z)
     */
    public static F transform_IArctgForArcth(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if ((((F) f.X[0]).compareTo(mines_i, ring) != 0)) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.ARCTG) {
                    return f;
                } else {
                    if ((((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])))).compareTo(mines_i, ring) != 0) {
                            return f;
                        } else {
                            F z = new F(((F) ((F) f.X[1]).X[0]).X[1]);
                            F a = new F(F.ARCTGH, new F[]{z});
                            return a;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции i*arcctg (i*z)
     * возвращает функцию arccth (z)
     * @param f - на входе функция i*arcctg (i*z)
     * @return возвращает arccth (z)
     */
    public static F transform_IArcctgForArccth(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if ((((F) f.X[0]).compareTo(i,ring) != 0)) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.ARCCTG) {
                    return f;
                } else {
                    if ((((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])))).compareTo(i,ring) != 0) {
                            return f;
                        } else {
                            F z = new F(((F) ((F) f.X[1]).X[0]).X[1]);
                            F a = new F(F.ARCCTGH, new F[]{z});
                            return a;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции -i*ln(i*z + √(1 – z^2))
     * возвращает функцию arcsin(z)
     * @param f - входная функция -i*ln(i*z + √(1 – z^2))
     * @return возвращает arcsin(z)
     */
    public static F transform_ILnForArcsin(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        NumberZ S = new NumberZ(1);
        F one = new F(S);
        NumberZ t = new NumberZ(2);
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if ((((F) f.X[0]).compareTo(mines_i, ring) != 0)) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.LN) {

                    return f;
                } else {
                    if ((((F) (((F) f.X[1]).X[0])).name != F.ADD)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).name != F.MULTIPLY) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).compareTo(i,ring) != 0)) {
                                return f;
                            } else {
                                if (((F) (((F) (((F) f.X[1]).X[0])).X[1])).name != F.SQRT) {
                                    return f;
                                } else {
                                    if (((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])))).name != F.SUBTRACT) {
                                        return f;
                                    } else {
                                        if ((((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).X[1]))).name != F.POW) {
                                            return f;
                                        } else {
                                            if (((((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).X[0])))).compareTo(one,ring) != 0) {
                                                return f;
                                            } else {
                                                if (((((F) ((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).X[1])).X[1]))).compareTo(two,ring) != 0) {
                                                    return f;
                                                } else {
                                                    if (((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[1])))).compareTo((((F) ((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).X[1])).X[0])),ring) != 0) {
                                                        return f;
                                                    } else {
                                                        F z = new F((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[1]))));
                                                        F r = new F(F.ARCSIN, new F[]{z});
                                                        return r;
                                                    }

                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции -i*ln(z + i√(1-z^2))
     * возвращает функцию arccos(z)
     * @param f - входная функция -i*ln(z + i√(1-z^2))
     * @return возвращает arccos(z)
     */
    public static F transform_ILnForArccos(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        NumberZ S = new NumberZ(1);
        F one = new F(S);
        NumberZ t = new NumberZ(2);
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if ((((F) f.X[0]).compareTo(mines_i, ring) != 0)) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.LN) {

                    return f;
                } else {
                    if ((((F) (((F) f.X[1]).X[0])).name != F.ADD)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[1]).X[0])).X[1])).name != F.MULTIPLY) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).compareTo(i,ring) != 0)) {
                                return f;
                            } else {
                                if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[1])).name != F.SQRT) {
                                    return f;
                                } else {
                                    if ((((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[1])).X[0])).name != F.SUBTRACT)) {
                                        return f;
                                    } else {
                                        if (((((F) ((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[1])).X[0])).X[1])).name != F.POW)) {
                                            return f;
                                        } else {
                                            if (((((F) ((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[1])).X[0])).X[0]))).compareTo(one,ring) != 0) {
                                                return f;
                                            } else {
                                                if (((((F) ((F) ((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[1])).X[0])).X[1])).X[1])).compareTo(two,ring) != 0) {
                                                    return f;
                                                } else {
                                                    if (((((F) (((F) (((F) f.X[1]).X[0])).X[0])))).compareTo(((F) ((F) (((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[1])).X[0])).X[1])).X[0]), ring) != 0) {
                                                        return f;
                                                    } else {
                                                        F z = new F((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0]))));
                                                        F r = new F(F.ARCCOS, new F[]{z});
                                                        return r;
                                                    }

                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
/**Процедура преобразования функции ½*ln((1+z)/(1-z))
 * возвращает функцию arcth(z)
 * @param f- входная функция ½*ln((1+z)/(1-z))
 * @return возвращает arcth(z)
 */
    public static F transform_LnForArcth(F f, Ring ring) {
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        NumberZ S = new NumberZ(1);
        F one = new F(S);
        F a = new F(F.DIVIDE, new F[]{one, two});
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.DIVIDE) {
                return f;
            } else {
                if (((F) (((F) f.X[0]).X[0])).compareTo(   one, ring) != 0) {
                    return f;
                } else {
                    if (((F) (((F) f.X[0]).X[1])).compareTo(two,ring) != 0) {
                        return f;
                    } else {
                        if (((F) f.X[1]).name != F.LN) {
                            return f;
                        } else {
                            if (((F) (((F) f.X[1]).X[0])).name != F.DIVIDE) {
                                return f;
                            } else {
                                if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).name != F.ADD) {
                                    return f;
                                } else {
                                    if (((F) (((F) (((F) f.X[1]).X[0])).X[1])).name !=
                                            F.SUBTRACT) {
                                        return f;
                                    } else {
                                        if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).compareTo(one,ring) != 0) {
                                            return f;
                                        } else {
                                            if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).compareTo((one),ring) != 0) {
                                                return f;
                                            } else {
                                                if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[1])).compareTo((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[1]),ring) != 0) {
                                                    return f;

                                                } else {
                                                    F z = new F((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[1]));
                                                    F r = new F(F.ARCTGH, new F[]{z});
                                                    return r;
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
/**Процедура преобразования функции ½*ln((z+1)/(z-1))
 * возвращает функцию arccth(z)
 * @param f- входная функция ½*ln((z+1)/(z-1))
 * @return возвращает arccth(z)
 */
    public static F transform_LnForArccth(F f, Ring ring) {
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        NumberZ S = new NumberZ(1);
        F one = new F(S);
        F a = new F(F.DIVIDE, new F[]{one, two});
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.DIVIDE) {
                return f;
            } else {
                if (((F) (((F) f.X[0]).X[0])).compareTo((F) (one),ring) != 0) {
                    return f;
                } else {
                    if (((F) (((F) f.X[0]).X[1])).compareTo(two,ring) != 0) {
                        return f;
                    } else {
                        if (((F) f.X[1]).name != F.LN) {
                            return f;
                        } else {
                            if (((F) (((F) f.X[1]).X[0])).name != F.DIVIDE) {
                                return f;
                            } else {
                                if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).name != F.ADD) {
                                    return f;
                                } else {
                                    if (((F) (((F) (((F) f.X[1]).X[0])).X[1])).name !=
                                            F.SUBTRACT) {
                                        return f;
                                    } else {
                                        if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[1])).compareTo(one,ring) != 0) {
                                            return f;
                                        } else {
                                            if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[1])).compareTo(one,ring) != 0) {
                                                return f;
                                            } else {
                                                if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).compareTo((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0]),ring) != 0) {
                                                    return f;

                                                } else {
                                                    F z = new F((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[1]));
                                                    F r = new F(F.ARCCTGH, new F[]{z});
                                                    return r;
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
/**Процедура преобразования функции -i/2*ln((1+i*z)/(1-i*z))
 * возвращает функцию arctg(z)
 * @param f- входная функция -i/2*ln((1+i*z)/(1-i*z))
 * @return возвращает arctg(z)
 */
    public static F transform_LnForArctg(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        NumberZ S = new NumberZ(1);
        F one = new F(S);

        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.DIVIDE) {
                return f;
            } else {
                if (((F) (((F) f.X[0]).X[0])).compareTo( (mines_i), ring) != 0) {
                    return f;
                } else {
                    if (((F) (((F) f.X[0]).X[1])).compareTo(two, ring) != 0) {
                        return f;
                    } else {
                        if (((F) f.X[1]).name != F.LN) {
                            return f;
                        } else {
                            if (((F) (((F) f.X[1]).X[0])).name != F.DIVIDE) {
                                return f;
                            } else {
                                if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).name != F.ADD) {
                                    return f;
                                } else {
                                    if (((F) (((F) (((F) f.X[1]).X[0])).X[1])).name !=
                                            F.SUBTRACT) {
                                        return f;
                                    } else {
                                        if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).compareTo(one,ring) != 0) {
                                            return f;
                                        } else {
                                            if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).compareTo(one,ring) != 0) {
                                                return f;
                                            } else {
                                                if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[1])).name != F.MULTIPLY) {
                                                    return f;
                                                } else {
                                                    if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[1])).name != F.MULTIPLY) {
                                                        return f;
                                                    } else {
                                                        if ((((F) ((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[1])).X[0])).compareTo(i,ring) != 0) {
                                                            return f;
                                                        } else {
                                                            if ((((F) ((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[1])).X[0])).compareTo(i,ring) != 0) {
                                                                return f;
                                                            } else {
                                                                if ((((F) ((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[1])).X[1])).compareTo((((F) ((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[1])).X[1])),ring) != 0) {
                                                                    return f;

                                                                } else {
                                                                    F z = new F(((F) ((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[1])).X[1]));
                                                                    F r = new F(F.ARCTG, new F[]{z});
                                                                    return r;
                                                                }
                                                            }
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
/**Процедура преобразования функции -i/2*ln((i*z-1)/(i*z+1))
 * возвращает функцию arcctg(z)
 * @param f- входная функция -i/2*ln((i*z-1)/(i*z+1))
 * @return возращает arcctg(z)
 */
    public static F transform_LnForArcctg(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i);
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        NumberZ S = new NumberZ(1);
        F one = new F(S);

        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.DIVIDE) {
                return f;
            } else {
                if (((F) (((F) f.X[0]).X[0])).compareTo(  (mines_i), ring) != 0) {
                    return f;
                } else {
                    if (((F) (((F) f.X[0]).X[1])).compareTo(two, ring) != 0) {
                        return f;
                    } else {
                        if (((F) f.X[1]).name != F.LN) {
                            return f;
                        } else {
                            if (((F) (((F) f.X[1]).X[0])).name != F.DIVIDE) {
                                return f;
                            } else {
                                if (((F) (((F) (((F) f.X[1]).X[0])).X[1])).name != F.ADD) {
                                    return f;
                                } else {
                                    if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).name !=
                                            F.SUBTRACT) {
                                        return f;
                                    } else {
                                        if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[1])).compareTo(one,ring) != 0) {
                                            return f;
                                        } else {
                                            if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[1])).compareTo(one,ring) != 0) {
                                                return f;
                                            } else {
                                                if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).name != F.MULTIPLY) {
                                                    return f;
                                                } else {
                                                    if (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).name != F.MULTIPLY) {
                                                        return f;
                                                    } else {
                                                        if ((((F) ((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[0])).compareTo(i,ring) != 0) {
                                                            return f;
                                                        } else {
                                                            if ((((F) ((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).X[0])).compareTo(i,ring) != 0) {
                                                                return f;
                                                            } else {
                                                                if ((((F) ((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[1])).compareTo((((F) ((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).X[1])),ring) != 0) {
                                                                    return f;

                                                                } else {
                                                                    F z = new F(((F) ((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[1]));
                                                                    F r = new F(F.ARCCTG, new F[]{z});
                                                                    return r;
                                                                }
                                                            }
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
/**Процедура преобразования функции ln(z + √(z^2-1))
 * возвращает функцию arcch(z)
 * @param f - входная функция ln(z + √(z^2-1))
 * @return возвращает arcch(z)
 */
    public static F transform_LnForArcch(F f, Ring ring) {
        NumberZ S = new NumberZ(1);
        F one = new F(S);

        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.LN) {
            return f;
        } else {

            if (((F) f.X[0]).name != F.ADD) {
                return f;
            } else {

                if ((((F) ((F) f.X[0]).X[1])).name != F.SQRT) {
                    return f;
                } else {

                    if ((((F) (((F) ((F) f.X[0]).X[1])).X[0])).name != F.SUBTRACT) {
                        return f;
                    } else {

                        if (((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[1])).compareTo(one,ring) != 0) {
                            return f;
                        } else {

                            if (((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).name != F.POW) {
                                return f;
                            } else {

                                if ((((F) ((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).X[1])).compareTo(two,ring) != 0) {
                                    return f;
                                } else {

                                    if (((((F) ((F) f.X[0]).X[0]))).compareTo(((F) ((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).X[0]), ring) != 0) {
                                        return f;
                                    } else {
                                        F z = new F(((F) ((F) f.X[0]).X[0]));
                                        F r = new F(F.ARCCH, new F[]{z});
                                        return r;

                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
/**Процедура преобразования функции ln(z + √(z^2+1))
 * возвращает функцию arcsh(z)
 * @param f-входная функция ln(z + √(z^2+1))
 * @return возвращает arcsh(z)
 */
    public static F transform_LnForArcsh(F f, Ring ring) {
        NumberZ S = new NumberZ(1);
        F one = new F(S);

        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.LN) {
            return f;
        } else {

            if (((F) f.X[0]).name != F.ADD) {
                return f;
            } else {

                if ((((F) ((F) f.X[0]).X[1])).name != F.SQRT) {
                    return f;
                } else {

                    if ((((F) (((F) ((F) f.X[0]).X[1])).X[0])).name != F.ADD) {
                        return f;
                    } else {

                        if (((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[1])).compareTo(one,ring) != 0) {
                            return f;
                        } else {

                            if (((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).name != F.POW) {
                                return f;
                            } else {

                                if ((((F) ((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).X[1])).compareTo(two,ring) != 0) {
                                    return f;
                                } else {

                                    if (((((F) ((F) f.X[0]).X[0]))).compareTo(((F) ((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).X[0]), ring) != 0) {
                                        return f;
                                    } else {
                                        F z = new F(((F) ((F) f.X[0]).X[0]));
                                        F r = new F(F.ARCSH, new F[]{z});
                                        return r;

                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    /** Вспомогательная процедура
     * @param f - входная функция функция
     * @param name имя функции
     * @param index индекс
     * @return возращает одну из функций  arccos z, arcsin z, arctg z, arcctg z, arcch z, arcsh z, arcth z, arcctg z
     */
    public static F transformArcForArh(F f, int name, int index, Ring ring) {
        F res = new F();
        F z = new F(Polynom.polynomFromNot0Number(f.X[0]));
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        switch (name) {
            case F.ARCCOS: {
                F A = new F(F.ARCCH, z);
                F mul = new F(F.MULTIPLY, new F[]{i, A});
                res = mul;
                break;
            }
            case F.ARCSIN: {
                F _z = new F(F.MULTIPLY, new F[]{i, z});
                F A = new F(F.ARCSH, _z);
                F mul = new F(F.MULTIPLY, new F[]{mines_i, A});
                res = mul;
                break;
            }
            case F.ARCTG: {
                F _z = new F(F.MULTIPLY, new F[]{i, z});
                F A = new F(F.ARCTGH, _z);
                F mul = new F(F.MULTIPLY, new F[]{mines_i, A});
                res = mul;
                break;
            }
            case F.ARCCTG: {
                F _z = new F(F.MULTIPLY, new F[]{i, z});
                F A = new F(F.ARCTGH, _z);
                F mul = new F(F.MULTIPLY, new F[]{i, A});
                res = mul;
                break;
            }
            case F.ARCCH: {
                F A = new F(F.ARCCOS, z);
                F mul = new F(F.MULTIPLY, new F[]{i, A});
                res = mul;
                break;
            }
            case F.ARCSH: {
                F _z = new F(F.MULTIPLY, new F[]{i, z});
                F A = new F(F.ARCSIN, _z);
                F mul = new F(F.MULTIPLY, new F[]{mines_i, A});
                res = mul;
                break;
            }
            case F.ARCTGH: {
                F _z = new F(F.MULTIPLY, new F[]{i, z});
                F A = new F(F.ARCTG, _z);
                F mul = new F(F.MULTIPLY, new F[]{mines_i, A});
                res = mul;
                break;
            }
            case F.ARCCTGH: {
                F _z = new F(F.MULTIPLY, new F[]{i, z});
                F A = new F(F.ARCCTG, _z);
                F mul = new F(F.MULTIPLY, new F[]{i, A});
                res = mul;
                break;
            }
            default: {
                return f;
            }
        }
        return res;
    }

    /** процедура замены функции,
     * содержащей в своих компонентах
     * arccos z, arcsin z, arctg z, arcctg z, arcch z, arcsh z, arcth z, arcctg z
     * эквивалентной ей функцией с заменой
     * @param f - входная функция,
     * содержащая компоненты,включающие  arccos z, arcsin z, arctg z, arcctg z, arcch z, arcsh z, arcth z, arcctg z
     * @return возвращает функцию,где вместо указанных
     * стоят их выражения через i и arccos z, arcsin z, arctg z, arcctg z, arcch z, arcsh z, arcth z, arcctg z
     *
     */
    public static F rec_transformArcForArh(F f, Ring ring) {
        if (f.X.length > 0) {
            for (int j = 0; j < f.X.length; j++) {
                if (f.X[j] instanceof Polynom) {
                    return SimpleIdentity.transformArcForArh(f, f.name, 0, ring);

                } else if (((F) f.X[j]).name < F.FIRST_INFIX_NAME && ((((F) f.X[j]).name == F.ARCCOS) || (((F) f.X[j]).name == F.ARCSIN) ||
                        (((F) f.X[j]).name == F.ARCTG) || (((F) f.X[j]).name == F.ARCCTG) || (((F) f.X[j]).name == F.ARCSH) ||
                        (((F) f.X[j]).name == F.ARCCH) || (((F) f.X[j]).name == F.ARCTGH) || (((F) f.X[j]).name == F.ARCCTGH))) {
                    f.X[j] = SimpleIdentity.transformArcForArh(((F) f.X[j]), ((F) f.X[j]).name, 0, ring);


                } else {
                    SimpleIdentity.rec_transformArcForArh((F) f.X[j], ring);
                }
            }
        }
        return f;
    }
           public static F rec(F f, Ring ring) {
        if (f.X.length > 0) {
            for (int j = 0; j < f.X.length; j++) {
                if (((F) f.X[j]).name < F.FIRST_INFIX_NAME && ((((F) f.X[j]).name == F.ARCCOS) || (((F) f.X[j]).name == F.ARCSIN) ||
                        (((F) f.X[j]).name == F.ARCTG) || (((F) f.X[j]).name == F.ARCCTG) || (((F) f.X[j]).name == F.ARCSH) ||
                        (((F) f.X[j]).name == F.ARCCH) || (((F) f.X[j]).name == F.ARCTGH) || (((F) f.X[j]).name == F.ARCCTGH))) {
                    f.X[j] = SimpleIdentity.transformArcForArh(((F) f.X[j]), ((F) f.X[j]).name, 0, ring);
                } else {
                    SimpleIdentity.rec_transformArcForArh((F) f.X[j], ring);
                }
            }
        }
        return f;
    }

    /** Вспомогательная процедура
     * @param f - входная функция функция
     * @param name имя функции
     * @param index индекс
     * @return возращает одну из функций  arccos z, arcsin z, arctg z, arcctg z, arcch z, arcsh z, arcth z, arcctg z
     */
    public static F transformArcForLn(F f, int name, int index, Ring ring) {
        F res = new F();
        F z = new F(Polynom.polynomFromNot0Number(f.X[0]));
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        NumberZ one = new NumberZ(1);
        NumberZ t = new NumberZ(2);
        switch (name) {
            case F.ARCCOS: {
                F z2 = new F(F.POW, new Element[]{z, Polynom.polynomFromNot0Number(t)});
                F sub = new F(F.SUBTRACT, new Element[]{Polynom.polynomFromNot0Number(one), z2});
                F b = new F(F.DIVIDE, new Element[]{Polynom.polynomFromNot0Number(one),
                            Polynom.polynomFromNot0Number(t)
                        });
                F a = new F(F.POW, new Element[]{sub, b});
                F mul = new F(F.MULTIPLY, new F[]{i, a});
                F add = new F(F.ADD, new F[]{z, mul});
                F ln = new F(F.LN, new F[]{add});
                F _mul = new F(F.MULTIPLY, new F[]{mines_i, ln});

                res = _mul;
                break;
            }
            case F.ARCSIN: {
                F z2 = new F(F.POW, new Element[]{z, Polynom.polynomFromNot0Number(t)});
                F sub = new F(F.SUBTRACT, new Element[]{Polynom.polynomFromNot0Number(one), z2});
                F b = new F(F.DIVIDE, new Element[]{Polynom.polynomFromNot0Number(one),
                            Polynom.polynomFromNot0Number(t)
                        });
                F a = new F(F.POW, new Element[]{sub, b});
                F _z = new F(F.MULTIPLY, new F[]{i, z});
                F add = new F(F.ADD, new F[]{_z, a});
                F ln = new F(F.LN, new F[]{add});
                F _mul = new F(F.MULTIPLY, new F[]{mines_i, ln});
                res = _mul;
                break;
            }
            case F.ARCTG: {
                F _z = new F(F.MULTIPLY, new F[]{i, z});
                F sub = new F(F.SUBTRACT, new Element[]{Polynom.polynomFromNot0Number(one), _z});
                F add = new F(F.ADD, new Element[]{Polynom.polynomFromNot0Number(one), _z});
                F div = new F(F.DIVIDE, new F[]{add, sub});
                F ln = new F(F.LN, new F[]{div});
                F a = new F(F.DIVIDE, new Element[]{mines_i, Polynom.polynomFromNot0Number(t)});
                F mul = new F(F.MULTIPLY, new F[]{a, ln});
                res = mul;
                break;
            }
            case F.ARCCTG: {
                F _z = new F(F.MULTIPLY, new F[]{i, z});
                F sub = new F(F.SUBTRACT, new Element[]{_z, Polynom.polynomFromNot0Number(one)});
                F add = new F(F.ADD, new Element[]{_z, Polynom.polynomFromNot0Number(one)});
                F div = new F(F.DIVIDE, new F[]{sub, add});
                F ln = new F(F.LN, new F[]{div});
                F a = new F(F.DIVIDE, new Element[]{mines_i, Polynom.polynomFromNot0Number(t)});
                F mul = new F(F.MULTIPLY, new F[]{a, ln});
                res = mul;
                break;
            }
            case F.ARCCH: {
                F z2 = new F(F.POW, new Element[]{z, Polynom.polynomFromNot0Number(t)});
                F sub = new F(F.SUBTRACT, new Element[]{z2, Polynom.polynomFromNot0Number(one)});
                F b = new F(F.DIVIDE, new Element[]{Polynom.polynomFromNot0Number(one),
                            Polynom.polynomFromNot0Number(t)
                        });
                F a = new F(F.POW, new Element[]{sub, b});
                F add = new F(F.ADD, new F[]{z, a});
                F ln = new F(F.LN, new F[]{add});
                res = ln;
                break;
            }
            case F.ARCSH: {
                F z2 = new F(F.POW, new Element[]{z, Polynom.polynomFromNot0Number(t)});
                F sub = new F(F.SUBTRACT, new Element[]{z2, Polynom.polynomFromNot0Number(one)});
                F b = new F(F.DIVIDE, new Element[]{Polynom.polynomFromNot0Number(one),
                            Polynom.polynomFromNot0Number(t)
                        });
                F a = new F(F.POW, new Element[]{sub, b});
                F add = new F(F.ADD, new F[]{z, a});
                F ln = new F(F.LN, new F[]{add});
                res = ln;
                break;
            }
            case F.ARCTGH: {
                F sub = new F(F.SUBTRACT, new Element[]{Polynom.polynomFromNot0Number(one), z});
                F add = new F(F.ADD, new Element[]{Polynom.polynomFromNot0Number(one), z});
                F div = new F(F.DIVIDE, new F[]{add, sub});
                F ln = new F(F.LN, new F[]{div});
                F a = new F(F.DIVIDE, new Element[]{Polynom.polynomFromNot0Number(one), Polynom.polynomFromNot0Number(t)});
                F mul = new F(F.MULTIPLY, new F[]{a, ln});
                res = mul;
                break;
            }
            case F.ARCCTGH: {
                F sub = new F(F.SUBTRACT, new Element[]{z, Polynom.polynomFromNot0Number(one)});
                F add = new F(F.ADD, new Element[]{z, Polynom.polynomFromNot0Number(one)});
                F div = new F(F.DIVIDE, new F[]{add, sub});
                F ln = new F(F.LN, new F[]{div});
                F a = new F(F.DIVIDE, new Element[]{Polynom.polynomFromNot0Number(one), Polynom.polynomFromNot0Number(t)});
                F mul = new F(F.MULTIPLY, new F[]{a, ln});
                res = mul;
                break;
            }
            default: {
                return f;
            }
        }
        return res;
    }

    /** процедура замены функции,
     * содержащей в своих компонентах
     * arccos z, arcsin z, arctg z, arcctg z, arcch z, arcsh z, arcth z, arcctg z
     * эквивалентной ей функцией с заменой
     * @param f - входная функция,
     * содержащая компоненты,включающие  arccos z, arcsin z, arctg z, arcctg z, arcch z, arcsh z, arcth z, arcctg z
     * @return возвращает функцию,где вместо указанных
     * стоят их выражения через i и ln
     *
     */

    public static F rec_transformArcForLn(F f, Ring ring) {
        if (f.X.length > 0) {
            for (int j = 0; j < f.X.length; j++) {
                if (f.X[j] instanceof Polynom) {
                    return SimpleIdentity.transformArcForLn(f, f.name, 0, ring);

                } else if (((F) f.X[j]).name < F.FIRST_INFIX_NAME && ((((F) f.X[j]).name == F.ARCCOS) || (((F) f.X[j]).name == F.ARCSIN) ||
                        (((F) f.X[j]).name == F.ARCTG) || (((F) f.X[j]).name == F.ARCCTG) || (((F) f.X[j]).name == F.ARCSH) ||
                        (((F) f.X[j]).name == F.ARCCH) || (((F) f.X[j]).name == F.ARCTGH) || (((F) f.X[j]).name == F.ARCCTGH))) {
                        f.X[j] = SimpleIdentity.transformArcForLn(((F) f.X[j]), ((F) f.X[j]).name, 0, ring);

               } else {
                    SimpleIdentity.rec_transformArcForLn((F) f.X[j], ring);
                }
            }
        }
        return f;
    }


        public static F rec1(F f, Ring ring) {
        if (f.X.length > 0) {
            for (int j = 0; j < f.X.length; j++) {
                if (((F) f.X[j]).name < F.FIRST_INFIX_NAME && ((((F) f.X[j]).name == F.ARCCOS) || (((F) f.X[j]).name == F.ARCSIN) ||
                        (((F) f.X[j]).name == F.ARCTG) || (((F) f.X[j]).name == F.ARCCTG) || (((F) f.X[j]).name == F.ARCSH) ||
                        (((F) f.X[j]).name == F.ARCCH) || (((F) f.X[j]).name == F.ARCTGH) || (((F) f.X[j]).name == F.ARCCTGH))) {
                    f.X[j] = SimpleIdentity.transformArcForLn(((F) f.X[j]), ((F) f.X[j]).name, 0, ring);
                } else {
                    SimpleIdentity.rec_transformArcForLn((F) f.X[j], ring);
                }
            }
        }
        return f;
    }

   /**Процедура преобразования функции вида exp(z)
     * в функцию с компонентами ch(z) и sh(z)
     * @param f - входная функция вида exp(z)
     * @return возвращает сумму ch(z) и sh(z), если z > 0
     * и разность ch(z) и sh(z), если z<0
     */
    public static F decompExp(F f, Ring ring)   {
//        Polynom M = new Polynom("-1");
        if (f.name != F.EXP) {
            return f;
        }
        if (f.X[0] instanceof Polynom) {
            if (((Polynom) f.X[0]).signum() == 1) {
                Element z = f.X[0];
                F A = new F(F.CH, z);
                F B = new F(F.SH, z);
                F add = new F(F.ADD, new F[]{A, B,});
                return add;
            } else {
                Element z = f.X[0];
                F A = new F(F.CH, z);
                F B = new F(F.SH, z);
                F sub = new F(F.SUBTRACT, new F[]{A, B,});
                return sub;
            }
        } else {
            if (((F) f.X[0]).name == F.MULTIPLY) {
                for (int i = 0; i <= f.X.length; i++) {
                    if (((F) f.X[0]).X[i].compareTo(NumberZ.MINUS_ONE, ring) == 0) {
                        Element z = f.X[0];
                        F A = new F(F.CH, z);
                        F B = new F(F.SH, z);
                        F sub = new F(F.SUBTRACT, new F[]{A, B,});
                        return sub;
                    }
                    return f;
                }
            }


            Element z = f.X[0];
            F A = new F(F.CH, z);
            F B = new F(F.SH, z);
            F add = new F(F.ADD, new F[]{A, B,});


            {
            }
            {
                return add;
            }
        }

    }
   /** Процедура преобразования функции вида exp(z)
     * в функцию с компонентами cos(z) и i*sin(z)
     * @param f - входная функция вида exp(i*z)]
     * @return возвращает сумму cos(z) и i*sin(z), если i*z > 0
     * и разность cos(z) и i*sin(z), если i*z<0
     */
public static F decompExpI (F f, Ring ring){
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
           Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.EXP) return f;
        if (f.X[0] instanceof Polynom) {
            if (((Polynom) f.X[0]).signum() == 1) {
                F z = new F(((F) f.X[0]).X[1]);
                F A = new F(F.COS, z);
                F B = new F(F.SIN, z);
                F C = new F(F.MULTIPLY, new F[]{i, B});
                F D = new F(F.ADD, new F[]{A, C});
                return D;
            } else {
                F z = new F(((F) f.X[0]).X[1]);
                F A = new F(F.COS, new F[]{z});
                F B = new F(F.SIN, new F[]{z});
                F C = new F(F.MULTIPLY, new F[]{i, B});
                F D = new F(F.SUBTRACT, new F[]{A, C});
                return D;
            }
        } else {
            if (((F) f.X[0]).name == F.MULTIPLY) {
                if (((F) f.X[0]).X[0].compareTo(mines_i, ring) == 0) {
                    F z = new F(((F) f.X[0]).X[1]);
                    F A = new F(F.COS, z);
                    F B = new F(F.SIN, z);
                    F C = new F(F.MULTIPLY, new F[]{i, B});
                    F D = new F(F.ADD, new F[]{A, C});
                    return D;
                } else {
                    if (((F) f.X[0]).X[0].compareTo(i,ring) == 0) {
                        F z = new F(((F) f.X[0]).X[1]);
                        F A = new F(F.COS, new F[]{z});
                        F B = new F(F.SIN, new F[]{z});
                        F C = new F(F.MULTIPLY, new F[]{i, B});
                        F D = new F(F.SUBTRACT, new F[]{A, C});
                        return D;

                    } else {
                        return f;
                    }


                }

            } else {
                return f;
            }
        }
    }

   /** Процедура преобразования функции вида cos(z)
     * в функцию ch (i*z)
     * @param f - входная функция вида cos(z)
     * @return возвращает ch (i*z)
     */
    public static F transformCosForCh(F f, Ring ring) {
        if (f.name != F.COS) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
            F i = new F(P_i);
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F ch = new F(F.CH, new F[]{mul});
            return ch;
        }
    }

   /** Процедура преобразования функции  вида sin(z)
     * в функцию  -i*sh (i*z)
     * @param f - входная функция вида sin(z)
     * @return возвращает -i*sh (i*z)
     */
    public static F transformSinForSh(F f, Ring ring) {
        if (f.name != F.SIN) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
            F i = new F(P_i); //i
            Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
            F mines_i = new F(P_mines_i); //-i
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F sh = new F(F.SH, new F[]{mul});
            F _sh = new F(F.MULTIPLY, new F[]{mines_i, sh});
            return _sh;
        }
    }

   /**Процедура преобразования функции  вида tg(z)
     * в функцию  -i*th (i*z)
     * @param f - входная функция вида tg(z)
     * @return возвращает -i*th (i*z)
     */
    public static F transformTgForTh(F f, Ring ring) {
        if (f.name != F.TG) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
            F i = new F(P_i); //i
            Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
            F mines_i = new F(P_mines_i); //-i
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F th = new F(F.TH, new F[]{mul});
            F _th = new F(F.MULTIPLY, new F[]{mines_i, th});
            return _th;
        }
    }

    /**Процедура преобразования функции вида сtg(z)
     * в функцию  i*сth (i*z)
     * @param f функция ctg(z)
     * @return возвращает i*cth (i*z)
     */
    public static F transformCtgForCth(F f, Ring ring) {
        if (f.name != F.CTG) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
            F i = new F(P_i); //i
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F cth = new F(F.CTH, new F[]{mul});
            F _cth = new F(F.MULTIPLY, new F[]{i, cth});
            return _cth;
        }
    }

    /**Процедура преобразования функции вида ch(z)
     * в функцию  cos (i*z)
     * @param f - входная функция вида ch(z)
     * @return возвращает cos (i*z)
     */
    public static F transformChForCos(F f, Ring ring) {
        if (f.name != F.CH) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
            F i = new F(P_i); //i
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F cos = new F(F.COS, new F[]{mul});
            return cos;
        }
    }

    /**Процедура преобразования функции вида sh(z)
     * в функцию  -i*sin (i*z)
     * @param f - входная функция вида sh(z)
     * @return возвращает -i*sin (i*z)
     */
    public static F transformShForSin(F f, Ring ring) {
        if (f.name != F.SH) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
            F i = new F(P_i); //i
            Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
            F mines_i = new F(P_mines_i); //-i
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F sin = new F(F.SIN, new F[]{mul});
            F _sin = new F(F.MULTIPLY, new F[]{mines_i, sin});
            return _sin;
        }
    }

    /**Процедура преобразования функции вида th(z)
     * в функцию  -i*tg (i*z)
     * @param f - входная функция вида th(z)
     * @return возвращает -i*tg (i*z)
     */
    public static F transformThForTg(F f, Ring ring) {
        if (f.name != F.TH) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
            F i = new F(P_i); //i
            Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
            F mines_i = new F(P_mines_i); //-i
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F tg = new F(F.TG, new F[]{mul});
            F _tg = new F(F.MULTIPLY, new F[]{mines_i, tg});
            return _tg;
        }
    }

    /**Процедура преобразования функции вида cth(z)
     * в функцию  i*ctg (i*z)
     * @param f - входная функция вида cth(z)
     * @return возвращает i*ctg (i*z)
     */
    public static F transformCthForCtg(F f, Ring ring) {
        if (f.name != F.CTH) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
            F i = new F(P_i); //i
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F ctg = new F(F.CTG, new F[]{mul});
            F _ctg = new F(F.MULTIPLY, new F[]{i, ctg});
            return _ctg;
        }
    }

    /**Процедура преобразования функции  вида cos(z)
     * в функцию с компонентами exp в степени i*z и exp в степени -i*z
     * @param f - входная функция вида cos(z)
     * @return возвращает сумму exp в степени i*z и exp в степени -i*z, деленную на 2
     */
    public static F transformCosForExp(F f, Ring ring) {
        NumberZ t = new NumberZ(2);
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.COS) {
            return f;
        } else {
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F _mul = new F(F.MULTIPLY, new F[]{mines_i, z});
            F E = new F(F.EXP, new F[]{mul});
            F _E = new F(F.EXP, new F[]{_mul});
            F sum = new F(F.ADD, new F[]{E, _E});
            F res = new F(F.DIVIDE, new Element[]{sum, Polynom.polynomFromNot0Number(t)});
            return res;
        }
    }

    /**Процедура преобразования функции  вида sin(z)
     * в функцию с компонентами exp в степени i*z и exp в степени -i*z
     * @param f - входная функция вида sin(z)
     * @return возвращает разность exp в степени i*z и exp в степени -i*z, деленную на 2*i
     */
    public static F transformSinForExp(F f, Ring ring) {
        NumberZ t = new NumberZ(2);
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        F two = new F(Polynom.polynomFromNot0Number(t));
        F A = new F(F.MULTIPLY, new F[]{two, i});
        if (f.name != F.SIN) {
            return f;
        } else {
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F _mul = new F(F.MULTIPLY, new F[]{mines_i, z});
            F E = new F(F.EXP, new F[]{mul});
            F _E = new F(F.EXP, new F[]{_mul});
            F sub = new F(F.SUBTRACT, new F[]{E, _E});
            F res = new F(F.DIVIDE, new F[]{sub, A});
            return res;
        }
    }

    /**Процедура преобразования функции  вида tg(z)
     * в функцию с компонентами exp в степени i*z и exp в степени -i*z
     * @param f - входная функция вида tg(z)
     * @return возвращает произведение i на частное разности exp в степени i*z и exp в степени -i*z
     * и суммы exp в степени i*z и exp в степени -i*z
     */
    public static F transformTgForExp(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.TG) {
            return f;
        } else {
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F _mul = new F(F.MULTIPLY, new F[]{mines_i, z});
            F E = new F(F.EXP, new F[]{mul});
            F _E = new F(F.EXP, new F[]{_mul});
            F sub = new F(F.SUBTRACT, new F[]{E, _E});
            F add = new F(F.ADD, new F[]{E, _E});
            F div = new F(F.DIVIDE, new F[]{sub, add});
            F res = new F(F.MULTIPLY, new F[]{i, div});
            return res;
        }
    }

    /**Процедура преобразования функции  вида сtg(z)
     * в функцию с компонентами exp в степени i*z и exp в степени -i*z
     * @param f - входная функция вида ctg(z)
     * @return возвращает произведение -i на частное суммы exp в степени i*z и exp в степени -i*z
     * и разности exp в степени i*z и exp в степени -i*z
     */
    public static F transformCtgForExp(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.CTG) {
            return f;
        } else {
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F _mul = new F(F.MULTIPLY, new F[]{mines_i, z});
            F E = new F(F.EXP, new F[]{mul});
            F _E = new F(F.EXP, new F[]{_mul});
            F sub = new F(F.SUBTRACT, new F[]{E, _E});
            F add = new F(F.ADD, new F[]{E, _E});
            F div = new F(F.DIVIDE, new F[]{add, sub});
            F res = new F(F.MULTIPLY, new F[]{mines_i, div});
            return res;
        }
    }

    /**Процедура преобразования функции  вида ch(z)
     * в функцию с компонентами exp в степени z и exp в степени -z
     * @param f - входная функция вида ch(z)
     * @return возвращает сумму exp в степени z и exp в степени -z, деленную на 2
     */
    public static F transformChForExp(F f, Ring ring) {
        NumberZ t = new NumberZ(2);
        if (f.name != F.CH) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F A = new F(F.MULTIPLY, new Element[]{NumberZ.MINUS_ONE, z});
            F B = new F(F.EXP, new F[]{A});
            F C = new F(F.EXP, new F[]{z});
            F D = new F(F.ADD, new F[]{C, B});
            F div = new F(F.DIVIDE, new Element[]{D, Polynom.polynomFromNot0Number(t)});
            return div;
        }
    }

    /**Процедура преобразования функции  вида sh(z)
     * в функцию с компонентами exp в степени z и exp в степени -z
     * @param f - входная функция вида sh(z)
     * @return возвращает разность exp в степени z и exp в степени -z, деленную на 2
     */
    public static F transformShForExp(F f, Ring ring) {
        NumberZ t = new NumberZ(2);
        if (f.name != F.SH) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F A = new F(F.MULTIPLY, new Element[]{NumberZ.MINUS_ONE, z});
            F B = new F(F.EXP, new F[]{A});
            F C = new F(F.EXP, new F[]{z});
            F D = new F(F.SUBTRACT, new F[]{C, B});
            F div = new F(F.DIVIDE, new Element[]{D, Polynom.polynomFromNot0Number(t)});
            return div;
        }
    }

    /**Процедура преобразования функции  вида th(z)
     * в функцию с компонентами exp в степени z и exp в степени -z
     * @param f - входная функция вида th(z)
     * @return возвращает частное от разности exp в степени z и exp в степени -z
     * на сумму e в степени z и e в степени -z
     */
    public static F transformThForExp(F f, Ring ring) {
        if (f.name != F.TH) {
            return f;
        } else {
            F z = new F((F) f.X[0]);
            F _z = new F(F.MULTIPLY, new Element[]{NumberZ.MINUS_ONE, z});
            F E = new F(F.EXP, new F[]{z});
            F _E = new F(F.EXP, new F[]{_z});
            F add = new F(F.ADD, new F[]{E, _E});
            F sub = new F(F.SUBTRACT, new F[]{E, _E});
            F res = new F(F.DIVIDE, new F[]{sub, add});
            return res;
        }
    }

    /**Процедура преобразования функции  вида cth(z)
     * в функцию с компонентами exp в степени z и exp в степени -z
     * @param f - входная функция вида cth(z)
     * @return возвращает частное от суммы exp в степени z и exp в степени -z
     * на разность exp в степени z и exp в степени -z
     */
    public static F transformCthForExp(F f, Ring ring) {
        if (f.name != F.CTH) {
            return f;
        } else {
            F z = new F((F) f.X[0]);
            F _z = new F(F.MULTIPLY, new Element[]{NumberZ.MINUS_ONE, z});
            F E = new F(F.EXP, new F[]{z});
            F _E = new F(F.EXP, new F[]{_z});
            F add = new F(F.ADD, new F[]{E, _E});
            F sub = new F(F.SUBTRACT, new F[]{E, _E});
            F res = new F(F.DIVIDE, new F[]{add, sub});
            return res;
        }
    }

    /**Процедура преобразования показательной функции  вида a^(i*x)
     * в функцию exp в степени i*x*ln(a)
     * @param f - входная функция вида a^(i*x)
     * @return возвращает exp в степени i*x*ln(a)
     */
    public static F transformPowForExp(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        if (f.name != F.POW) {
            return f;
        } else {
            if ((((F) f.X[1]).name != F.MULTIPLY) & (((F) f.X[0]).X[0].compareTo(i,ring) != 0)) {
                return f;
            } else {
                F z = new F(((F) f.X[1]).X[1]);
                F a = new F((F) f.X[0]);
                F ln = new F(F.LN, new F[]{a});
                F B = new F((F) f.X[1]);
                F mul = new F(F.MULTIPLY, new F[]{B, ln});
                F res = new F(F.EXP, new F[]{mul});
                return res;
            }
        }
    }

    /**Процедура преобразования показательной функции  вида a^(i*x)
     * в функцию с компонентами cos(x*ln a) и i*sin (x*ln a)
     * @param f - входная функция вида a^(i*x)
     * @return возвращает сумму cos(x*ln a) и i*sin (x*ln a)
     */
    public static F transformPowForCosAndSin(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        if (f.name != F.POW) {
            return f;
        } else {
            if ((((F) f.X[1]).name != F.MULTIPLY) & (((F) f.X[1]).X[0].compareTo(i,ring) != 0)) {
                return f;
            } else {
                F x = new F(((F) f.X[1]).X[1]);
                F a = new F((F) f.X[0]);
                F ln = new F(F.LN, new F[]{a});
                F B = new F(F.MULTIPLY, new F[]{x, ln});
                F cos = new F(F.COS, new F[]{B});
                F sin = new F(F.SIN, new F[]{B});
                F i_sin = new F(F.MULTIPLY, new F[]{i, sin});
                F res = new F(F.ADD, new F[]{cos, i_sin});
                return res;
            }
        }
    }

    /**Процедура преобразования степенной функции  вида x^i
     * в функцию exp в степени i*ln(x)
     * @param f - входная функция вида x^i
     * @return возаращает exp в степени i*ln(x)
     */
    public static F transformX_iForExp(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        if (f.name != F.POW) {
            return f;
        } else {
            if (((F) f.X[1]).compareTo(i,ring) != 0) {
                return f;
            } else {
                F x = new F((F) f.X[0]);
                F ln = new F(F.LN, new F[]{x});
                F mul = new F(F.MULTIPLY, new F[]{i, ln});
                F res = new F(F.EXP, new F[]{mul});
                return res;
            }
        }
    }

    /**Процедура преобразования степенной функции  вида x^i
     * в функцию с компонентами cos(ln x) и i*sin (ln x)
     * @param f - входная функция вида x^i
     * @return возвращает сумму cos(ln x) и i*sin (ln x)
     */
    public static F transformX_iForCosAndSin(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        if (f.name != F.POW) {
            return f;
        } else {
            if (((F) f.X[1]).compareTo(i,ring) != 0) {
                return f;
            } else {
                F x = new F((F) f.X[0]);
                F ln = new F(F.LN, new F[]{x});
                F cos = new F(F.COS, new F[]{ln});
                F sin = new F(F.SIN, new F[]{ln});
                F i_sin = new F(F.MULTIPLY, new F[]{i, sin});
                F res = new F(F.ADD, new F[]{cos, i_sin});
                return res;
            }
        }
    }

    /**Процедура преобразования показательной функции  вида i^x
     * в функцию exp в степени x*ln(i)
     * @param f - входная функция вида i^x
     * @return возаращает exp в степени x*ln(i)
     */
    public static F transformI_xForExp(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        if (f.name != F.POW) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(i,ring) != 0) {
                return f;
            } else {
                F x = new F((F) f.X[1]);
                F ln = new F(F.LN, new F[]{i});
                F mul = new F(F.MULTIPLY, new F[]{x, ln});
                F res = new F(F.EXP, new F[]{mul});
                return res;
            }
        }
    }

    /**Процедура преобразования показательной функции  вида i^x
     * в функцию с компонентами cos((PI/2)*x) и i*sin ((PI/2)*x)
     * @param f - входная функция вида i^x
     * @return возвращает сумму cos((PI/2)*x) и i*sin ((PI/2)*x)
     */
    public static F transformI_xForCosAndSin(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        F pi = new F(new Fname("\\pi"));
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.POW) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(i,ring) != 0) {
                return f;
            } else {
                F x = new F((F) f.X[1]);
                F div = new F(F.DIVIDE, new F[]{pi, two});
                F mul = new F(F.MULTIPLY, new F[]{div, x});
                F cos = new F(F.COS, new F[]{mul});
                F sin = new F(F.SIN, new F[]{mul});
                F i_sin = new F(F.MULTIPLY, new F[]{i, sin});
                F res = new F(F.ADD, new F[]{cos, i_sin});
                return res;
            }
        }
    }

    /**Процедура преобразования функции  вида exp в степени x*ln(i)
     * в функцию с компонентами cos((PI/2)*x) и i*sin ((PI/2)*x)
     * @param f - входная функция вида exp в степени x*ln(i)
     * @return возвращает сумму cos((PI/2)*x) и i*sin ((PI/2)*x)
     */
    public static F transformExpForCosAndSin(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        F pi = new F(new Fname("\\pi"));
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.EXP) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.MULTIPLY) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[1])).name != F.LN) &
                        (((F) (((F) (((F) f.X[0]).X[1])).X[0])).compareTo(i,ring) != 0)) {
                    return f;
                } else {
                    F x = new F(((F) f.X[0]).X[0]);
                    F arg = new F(F.DIVIDE, new F[]{pi, two});
                    F A = new F(F.MULTIPLY, new F[]{arg, x});
                    F cos = new F(F.COS, new F[]{A});
                    F sin = new F(F.SIN, new F[]{A});
                    F i_sin = new F(F.MULTIPLY, new F[]{i, sin});
                    F res = new F(F.ADD, new F[]{cos, i_sin});
                    return res;
                }
            }
        }
    }

    /**Процедура преобразования функции  вида exp в степени i*ln i
     * в  exp в степени -PI/2
     * @param f - входная функция вида exp в степени i*ln i
     * @return возвращает exp в степени -PI/2
     */
    public static F Exp_I_Ln_I_For_Exp_Pi(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        F pi = new F(new Fname("\\pi"));
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.EXP) {
            return f;
        } else {
            if ((((F) (((F) f.X[0]).X[0])).compareTo(i,ring) != 0) & (((F) (((F) f.X[0]).X[1])).name != F.LN)) {
                return f;
            } else {
                if (((F) (((F) (((F) f.X[0]).X[1])).X[0])).compareTo(i,ring) != 0) {
                    return f;
                } else {
                    F div = new F(F.DIVIDE, new F[]{pi, two});
                    F _div = new F(F.MULTIPLY, new Element[]{NumberZ.MINUS_ONE, div});
                    F res = new F(F.EXP, new F[]{_div});
                    return res;
                }
            }
        }
    }
    /**Процедура преобразования i^i
     * в  exp в степени -PI/2
     * @param f - входной параметр вида i^i
     * @return возвращает exp в степени -PI/2
     */
    public static F transformI_iFofExp_Pi(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        F pi = new F(new Fname("\\pi"));
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.POW) {
            return f;
        } else {
            if (((F) (f.X[0])).compareTo(i,ring) != 0) {
                return f;
            } else {
                F div = new F(F.DIVIDE, new F[]{pi, two});
                F _div = new F(F.MULTIPLY, new Element[]{NumberZ.MINUS_ONE, div});
                F res = new F(F.EXP, new F[]{_div});
                return res;
            }
        }
    }
    ////////////////////////////Обратные преобразования/////////////////////////////////
    /**Процедура преобразования функции  вида ch(z) +(-) sh(z)
     * в функцию exp в степени z(-z)
     * @param f - входная функция вида ch(z) +(-) sh(z)
     * @return возвращает exp в степени z(-z)
     */
    public static F transformChAddShForExp(F f, Ring ring) {
        if ((f.name != F.ADD) & (f.name != F.SUBTRACT)) {
            return f;
        } else {
            if ((f.name == F.ADD) & ((((F) f.X[0]).name == F.CH) || (((F) f.X[0]).name == F.SH)) &
                    ((((F) f.X[1]).name == F.SH) || (((F) f.X[1]).name == F.CH))) {
                if (((F) f.X[0]).X[0].compareTo(((F) f.X[1]).X[0], ring) == 0) {
                    F z = new F(((F) f.X[0]).X[0]);
                    F exp = new F(F.EXP, new F[]{z});
                    return exp;
                }
                return f;
            } else {
                if ((f.name == F.SUBTRACT) & (((F) f.X[0]).name == F.CH) &
                        (((F) f.X[1]).name == F.SH) &
                        (((F) f.X[0]).X[0].compareTo(((F) f.X[1]).X[0],ring) == 0)) {
                    F z = new F(((F) f.X[0]).X[0]);
                    F _z = new F(F.MULTIPLY,  new Element[]{NumberZ.MINUS_ONE, z});
                    F exp = new F(F.EXP, new F[]{_z});
                    return exp;
                }
            }
            return f;
        }
    }

    /**Процедура преобразования функции  вида (exp^z + exp^(-z))/2
     * в функцию ch(z)
     * @param f - входная функция вида (exp^z + exp^(-z))/2
     * @return возвращает ch(z)
     */
    public static F transformExpZAddExp_ZForCh(F f, Ring ring) {
        if (f.name != F.DIVIDE) {
            return f;
        } else {
            NumberZ t = new NumberZ(2);
            F two = new F(Polynom.polynomFromNot0Number(t));
            if ((((F) f.X[0]).name != F.ADD) & (((F) f.X[1]).compareTo(two,ring) != 0)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.EXP) &
                        (((F) (((F) f.X[0]).X[1])).name != F.EXP)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[1])).X[0])).name != F.MULTIPLY) &
                            !((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).isMinusOne(ring)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[1])),ring) != 0) {
                            return f;
                        } else {
                            F z = new F(((F) (((F) f.X[0]).X[0])).X[0]);
                            F ch = new F(F.CH, new F[]{z});
                            return ch;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида (exp^z - exp^(-z))/2
     * в функцию sh(z)
     * @param f - входная функция вида (exp^z - exp^(-z))/2
     * @return возвращает sh(z)
     */
    public static F transformExpZSubtractExp_ZForSh(F f, Ring ring) {
        if (f.name != F.DIVIDE) {
            return f;
        } else {
            NumberZ t = new NumberZ(2);
            F two = new F(Polynom.polynomFromNot0Number(t));
            if ((((F) f.X[0]).name != F.SUBTRACT) & (((F) f.X[1]).compareTo(two,ring) != 0)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.EXP) &
                        (((F) (((F) f.X[0]).X[1])).name != F.EXP)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[1])).X[0])).name != F.MULTIPLY) &
                            !((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).isMinusOne(ring)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[1])),ring) != 0) {
                            return f;
                        } else {
                            F z = new F(((F) (((F) f.X[0]).X[0])).X[0]);
                            F sh = new F(F.SH, new F[]{z});
                            return sh;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида (exp^z - exp^(-z))/(exp^z + exp^(-z))
     * в функцию th(z)
     * @param f - входная функция вида (exp^z - exp^(-z))/(exp^z + exp^(-z))
     * @return возвращает th(z)
     */
    public static F transformComposition_ExpForTh(F f, Ring ring) {
        if (f.name != F.DIVIDE) {
            return f;
        } else {
            if ((((F) f.X[0]).name != F.SUBTRACT) & (((F) f.X[1]).name != F.ADD)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.EXP) &
                        (((F) (((F) f.X[0]).X[1])).name != F.EXP) &
                        (((F) (((F) f.X[1]).X[0])).name != F.EXP) &
                        (((F) (((F) f.X[1]).X[1])).name != F.EXP)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[1])).X[0])).name != F.MULTIPLY) &
                            !((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).isMinusOne(ring)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[1])),ring) != 0) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.MULTIPLY) &
                                    !((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).isMinusOne(ring)) {
                                return f;
                            } else {
                                if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[1])),ring) != 0) {
                                    return f;
                                } else {
                                    if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) f.X[1]).X[0])).X[0])),ring) != 0) {
                                        return f;
                                    } else {
                                        F z = new F(((F) (((F) f.X[0]).X[0])).X[0]);
                                        F th = new F(F.TH, new F[]{z});
                                        return th;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида (exp^z + exp^(-z))/(exp^z - exp^(-z))
     * в функцию сth(z)
     * @param f - входная функция вида (exp^z + exp^(-z))/(exp^z - exp^(-z))
     * @return возвращает cth(z)
     */
    public static F transformComposition_ExpForCth(F f, Ring ring) {
        if (f.name != F.DIVIDE) {
            return f;
        } else {
            if ((((F) f.X[0]).name != F.ADD) & (((F) f.X[1]).name != F.SUBTRACT)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.EXP) &
                        (((F) (((F) f.X[0]).X[1])).name != F.EXP) &
                        (((F) (((F) f.X[1]).X[0])).name != F.EXP) &
                        (((F) (((F) f.X[1]).X[1])).name != F.EXP)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[1])).X[0])).name != F.MULTIPLY) &
                            !((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).isMinusOne(ring)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[1])),ring) != 0) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.MULTIPLY) &
                                    !((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).isMinusOne(ring)) {
                                return f;
                            } else {
                                if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[1])),ring) != 0) {
                                    return f;
                                } else {
                                    if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) f.X[1]).X[0])).X[0])),ring) != 0) {
                                        return f;
                                    } else {
                                        F z = new F(((F) (((F) f.X[0]).X[0])).X[0]);
                                        F cth = new F(F.CTH, new F[]{z});
                                        return cth;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида cos(z) +(-)i* sin(z)
     * в функцию exp в степени i*z(-i*z)
     * @param f - входная функция вида cos(z) +(-)i* sin(z)
     * @return возвращает exp в степени i*z(-i*z)
     */
    public static F transformCosAddSinForExp(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        Polynom P_minus_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F minus_i = new F(P_minus_i); //-i
        F i = new F(P_i); //i
        if ((f.name != F.ADD) & (f.name != F.SUBTRACT)) {
            return f;
        } else {
            if ((f.name == F.ADD) & (((F) f.X[0]).name == F.COS) &
                    (((F) (((F) f.X[1]).X[1])).name == F.SIN) &
                    (((F) f.X[1]).name == F.MULTIPLY) &
                    (((F) f.X[1]).X[0].compareTo(i,ring) == 0)) {
                if (((F) f.X[0]).X[0].compareTo(((F) (((F) f.X[1]).X[1])).X[0],ring) == 0) {
                    F z = new F(((F) f.X[0]).X[0]);
                    F iz = new F(F.MULTIPLY, new F[]{i, z});
                    F exp = new F(F.EXP, new F[]{iz});
                    return exp;
                }
                return f;
            } else {
                if ((f.name == F.SUBTRACT) & (((F) f.X[0]).name == F.COS) &
                        (((F) (((F) f.X[1]).X[1])).name == F.SIN) &
                        (((F) f.X[1]).name == F.MULTIPLY) &
                        (((F) f.X[1]).X[0].compareTo(i,ring) == 0) &
                        (((F) f.X[0]).X[0].compareTo(((F) (((F) f.X[1]).X[1])).X[0],ring) == 0)) {
                    F z = new F(((F) f.X[0]).X[0]);
                    F _iz = new F(F.MULTIPLY, new F[]{minus_i, z});
                    F exp = new F(F.EXP, new F[]{_iz});
                    return exp;
                }
            }
            return f;
        }
    }

    /**Процедура преобразования функции  вида (exp^(i*z) + exp^(-i*z))/2
     * в функцию cos(z)
     * @param f - входная функция вида (exp^(i*z) + exp^(-i*z))/2
     * @return возвращает cos(z)
     */
    public static F transformComposition_ExpForCos(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        Polynom P_minus_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F minus_i = new F(P_minus_i); //-i
        F i = new F(P_i); //i
        if (f.name != F.DIVIDE) {
            return f;
        } else {
            NumberZ t = new NumberZ(2);
            F two = new F(Polynom.polynomFromNot0Number(t));
            if ((((F) f.X[0]).name != F.ADD) & (((F) f.X[1]).compareTo(two,ring) != 0)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.EXP) &
                        (((F) (((F) f.X[0]).X[1])).name != F.EXP)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[0])).X[0])).name != F.MULTIPLY) &
                            (((F) (((F) (((F) f.X[0]).X[1])).X[0])).name != F.MULTIPLY) &
                            (((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).compareTo(minus_i,ring) != 0) &
                            (((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[0])).compareTo(i,ring) != 0)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[1])),ring) != 0) {
                            return f;
                        } else {
                            F z = new F((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[1]));
                            F cos = new F(F.COS, new F[]{z});
                            return cos;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида (exp^(i*z) - exp^(-i*z))/(2*i)
     * в функцию sin(z)
     * @param f - входная функция вида (exp^(i*z) - exp^(-i*z))/(2*i)
     * @return возвращает sin(z)
     */
    public static F transformComposition_ExpForSin(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        Polynom P_minus_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F minus_i = new F(P_minus_i); //-i
        F i = new F(P_i); //i
        if (f.name != F.DIVIDE) {
            return f;
        } else {
            NumberZ t = new NumberZ(2);
            F two = new F(Polynom.polynomFromNot0Number(t));
            if ((((F) f.X[0]).name != F.SUBTRACT) & (((F) f.X[1]).name != F.MULTIPLY)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.EXP) &
                        (((F) (((F) f.X[0]).X[1])).name != F.EXP) &
                        (((F) (((F) f.X[1]).X[0])).compareTo(two,ring) != 0) &
                        (((F) (((F) f.X[1]).X[1])).compareTo(i,ring) != 0)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[0])).X[0])).name != F.MULTIPLY) &
                            (((F) (((F) (((F) f.X[0]).X[1])).X[0])).name != F.MULTIPLY) &
                            !(((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).isMinusOne(ring)) &
                            (((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[0])).compareTo(i,ring) != 0)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[1])),ring) != 0) {
                            return f;
                        } else {
                            F z = new F((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[1]));
                            F sin = new F(F.SIN, new F[]{z});
                            return sin;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида
     * i*((exp^(i*z) - exp^(-i*z))/(exp^(i*z) + exp^(-i*z)))
     * в функцию tg z
     * @param f - входная функция вида i*((exp^(i*z) - exp^(-i*z))/(exp^(i*z) + exp^(-i*z)))
     * @return возвращает tg z
     */
    public static F transformComposition_ExpForTg(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        Polynom P_minus_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F minus_i = new F(P_minus_i); //-i
        F i = new F(P_i); //i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if ((((F) f.X[0]).compareTo(i,ring) != 0) &
                    (((F) f.X[1]).name != F.DIVIDE)) {
                return f;
            } else {
                if ((((F) (((F) f.X[1]).X[0])).name != F.SUBTRACT) &
                        (((F) (((F) f.X[1]).X[1])).name != F.ADD)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[1]).X[0])).X[0])).name != F.EXP) &
                            (((F) (((F) (((F) f.X[1]).X[0])).X[1])).name != F.EXP) &
                            (((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.EXP) &
                            (((F) (((F) (((F) f.X[1]).X[1])).X[1])).name != F.EXP)) {
                        return f;
                    } else {
                        if ((((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).name != F.MULTIPLY) &
                                (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).name != F.MULTIPLY) &
                                (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).name != F.MULTIPLY) &
                                (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[1])).X[0])).name != F.MULTIPLY)) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).X[1])),ring) != 0) &
                                    (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[1])).X[0])).X[1])),ring) != 0) &
                                    (((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[1])),ring) != 0)) {
                                return f;
                            } else {
                                if ((((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[0])).compareTo(i,ring) != 0) &
                                        (((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).X[0])).compareTo(minus_i,ring) != 0) &
                                        (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[0])).compareTo(i,ring) != 0) &
                                        (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[1])).X[0])).X[0])).compareTo(minus_i,ring) != 0)) {
                                    return f;
                                } else {
                                    F z = new F((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[1]));
                                    F tg = new F(F.TG, new F[]{z});
                                    return tg;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида
     * -i*((exp^(i*z) + exp^(-i*z))/(exp^(i*z) - exp^(-i*z)))
     * в функцию сtg z
     * @param f - входная функция вида -i*((exp^(i*z) + exp^(-i*z))/(exp^(i*z) - exp^(-i*z)))
     * @return возвращает ctg z
     */
    public static F transformComposition_ExpForCtg(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        Polynom P_minus_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F minus_i = new F(P_minus_i); //-i
        F i = new F(P_i); //i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if ((((F) f.X[0]).compareTo(minus_i,ring) != 0) &
                    (((F) f.X[1]).name != F.DIVIDE)) {
                return f;
            } else {
                if ((((F) (((F) f.X[1]).X[0])).name != F.ADD) &
                        (((F) (((F) f.X[1]).X[1])).name != F.SUBTRACT)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[1]).X[0])).X[0])).name != F.EXP) &
                            (((F) (((F) (((F) f.X[1]).X[0])).X[1])).name != F.EXP) &
                            (((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.EXP) &
                            (((F) (((F) (((F) f.X[1]).X[1])).X[1])).name != F.EXP)) {
                        return f;
                    } else {
                        if ((((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).name != F.MULTIPLY) &
                                (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).name != F.MULTIPLY) &
                                (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).name != F.MULTIPLY) &
                                (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[1])).X[0])).name != F.MULTIPLY)) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).X[1])),ring) != 0) &
                                    (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[1])).X[0])).X[1])),ring) != 0) &
                                    (((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[1])),ring) != 0)) {
                                return f;
                            } else {
                                if ((((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[0])).compareTo(i,ring) != 0) &
                                        (((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).X[0])).compareTo(minus_i,ring) != 0) &
                                        (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[0])).compareTo(i,ring) != 0) &
                                        (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[1])).X[0])).X[0])).compareTo(minus_i,ring) != 0)) {
                                    return f;
                                } else {
                                    F z = new F((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[1]));
                                    F ctg = new F(F.CTG, new F[]{z});
                                    return ctg;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида ch (i*z)
     * в функцию cos z
     * @param f - входная функция вида ch (i*z)
     * @return возвращает cos z
     */
    public static F transformChIZForCos(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        if (f.name != F.CH) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.MULTIPLY) {
                return f;
            } else {
                if (((F) (((F) f.X[0]).X[0])).compareTo(i,ring) != 0) {
                    return f;
                } else {
                    F z = new F((F) (((F) f.X[0]).X[1]));
                    F cos = new F(F.COS, new F[]{z});
                    return cos;
                }
            }
        }
    }

    /**Процедура преобразования функции  вида -i*sh (i*z)
     * в функцию sin z
     * @param f - входная функция вида -i*sh (i*z)
     * @return возвращает sin z
     */
    public static F transform_IShIZForSin(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        Polynom P_minus_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F minus_i = new F(P_minus_i); //-i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(minus_i,ring) != 0) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.SH) {
                    return f;
                } else {
                    if (((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(i,ring) != 0) {
                            return f;
                        } else {
                            F z = new F((F) (((F) (((F) f.X[1]).X[0])).X[1]));
                            F sin = new F(F.SIN, new F[]{z});
                            return sin;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида -i*th (i*z)
     * в функцию tg z
     * @param f - входная функция вида -i*th (i*z)
     * @return возвращает tg z
     */
    public static F transform_IThIZForTg(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        Polynom P_minus_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F minus_i = new F(P_minus_i); //-i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(minus_i,ring) != 0) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.TH) {
                    return f;
                } else {
                    if (((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(i,ring) != 0) {
                            return f;
                        } else {
                            F z = new F((F) (((F) (((F) f.X[1]).X[0])).X[1]));
                            F tg = new F(F.TG, new F[]{z});
                            return tg;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида i*cth (i*z)
     * в функцию сtg z
     * @param f - входная функция вида i*cth (i*z)
     * @return возвращает ctg z
     */
    public static F transformICthIZForCtg(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(i,ring) != 0) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.CTH) {
                    return f;
                } else {
                    if (((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(i,ring) != 0) {
                            return f;
                        } else {
                            F z = new F((F) (((F) (((F) f.X[1]).X[0])).X[1]));
                            F ctg = new F(F.CTG, new F[]{z});
                            return ctg;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида cos (i*z)
     * в функцию ch z
     * @param f - входная функция вида cos (i*z)
     * @return возвращает ch z
     */
    public static F transformCosIZForCh(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        if (f.name != F.COS) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.MULTIPLY) {
                return f;
            } else {
                if (((F) (((F) f.X[0]).X[0])).compareTo(i,ring) != 0) {
                    return f;
                } else {
                    F z = new F((F) (((F) f.X[0]).X[1]));
                    F ch = new F(F.CH, new F[]{z});
                    return ch;
                }
            }
        }
    }

    /**Процедура преобразования функции  вида -i*sin (i*z)
     * в функцию sh z
     * @param f - входная функция вида -i*sin (i*z)
     * @return возвращает sh z
     */
    public static F transform_ISinIZForSh(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        Polynom P_minus_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F minus_i = new F(P_minus_i); //-i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(minus_i,ring) != 0) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.SIN) {
                    return f;
                } else {
                    if (((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(i,ring) != 0) {
                            return f;
                        } else {
                            F z = new F((F) (((F) (((F) f.X[1]).X[0])).X[1]));
                            F sh = new F(F.SH, new F[]{z});
                            return sh;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида -i*tg (i*z)
     * в функцию th z
     * @param f - входная функция вида -i*tg (i*z)
     * @return возвращает th z
     */
    public static F transform_ITgIZForTh(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        Polynom P_minus_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F minus_i = new F(P_minus_i); //-i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(minus_i,ring) != 0) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.TG) {
                    return f;
                } else {
                    if (((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(i,ring) != 0) {
                            return f;
                        } else {
                            F z = new F((F) (((F) (((F) f.X[1]).X[0])).X[1]));
                            F th = new F(F.TH, new F[]{z});
                            return th;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида i*ctg(i*z)
     * в функцию cth z
     * @param f - входная функция вида i*ctg(i*z)
     * @return возвращает cth z
     */
    public static F transformICtgIZForCth(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(i,ring) != 0) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.CTG) {
                    return f;
                } else {
                    if (((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(i,ring) != 0) {
                            return f;
                        } else {
                            F z = new F((F) (((F) (((F) f.X[1]).X[0])).X[1]));
                            F cth = new F(F.CTH, new F[]{z});
                            return cth;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида cos(x*ln a)+i*sin(x*ln a)
     * в функцию  a^(i*x)
     * @param f - входная функция вида cos(x*ln a)+i*sin(x*ln a)
     * @return возвращает a^(i*x)
     */
    public static F transformCosAddSinForPow(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        if (f.name != F.ADD) {
            return f;
        } else {
            if ((((F) f.X[0]).name != F.COS) & (((F) f.X[1]).name != F.MULTIPLY)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.MULTIPLY) & (((F) (((F) f.X[1]).X[0])).compareTo(i,ring) != 0) & (((F) (((F) f.X[1]).X[1])).name != F.SIN)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[0])).X[1])).name != F.LN) & (((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.MULTIPLY)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[1])).name != F.LN) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])),ring) != 0) & (((F) (((F) (((F) (((F) f.X[0]).X[0])).X[1])).X[0])).compareTo(
                                    ((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[1])).X[0])),ring) != 0)) {
                                return f;
                            } else {
                                F x = new F((F) (((F) (((F) f.X[0]).X[0])).X[0]));
                                F a = new F((F) (((F) (((F) (((F) f.X[0]).X[0])).X[1])).X[0]));
                                F i_x = new F(F.MULTIPLY, new F[]{i, x});
                                F res = new F(F.POW, new F[]{a, i_x});
                                return res;
                            }
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида cos(ln x) + i*sin(ln x)
     * в функцию  x^i
     * @param f - входная функция вида cos(ln x) + i*sin(ln x)
     * @return возвращает x^i
     */
    public static F transformCosAddISinForX_I(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        if (f.name != F.ADD) {
            return f;
        } else {
            if ((((F) f.X[0]).name != F.COS) & (((F) f.X[1]).name != F.MULTIPLY)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.LN) & (((F) (((F) f.X[1]).X[0])).compareTo(i,ring) != 0) & (((F) (((F) f.X[1]).X[1])).name != F.SIN)) {
                    return f;
                } else {
                    if (((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.LN) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])),ring) != 0) {
                            return f;
                        } else {
                            F x = new F(((F) (((F) (((F) f.X[0]).X[0])).X[0])));
                            F res = new F(F.POW, new F[]{x, i});
                            return res;
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида cos((PI/2)*x) + i*sin((PI/2)*x)
     * в функцию  i^x
     * @param f - входная функция вида cos((PI/2)*x) + i*sin((PI/2)*x)
     * @return возвращает i^x
     */
    public static F transformCosAddSinForI_X(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        F pi = new F(new Fname("\\pi"));
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.ADD) {
            return f;
        } else {
            if ((((F) f.X[0]).name != F.COS) & (((F) f.X[1]).name != F.MULTIPLY)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.MULTIPLY) & (((F) (((F) f.X[1]).X[0])).compareTo(i,ring) != 0) & (((F) (((F) f.X[1]).X[1])).name != F.SIN)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[0])).X[0])).name != F.DIVIDE) & (((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.MULTIPLY)) {
                        return f;
                    } else {
                        if ((((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[0])).compareTo(pi,ring) != 0) & (((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[1])).compareTo(two,ring) != 0) & (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).name != F.DIVIDE)) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[0])).compareTo(pi,ring) != 0) &
                                    (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[1])).compareTo(two,ring) != 0)) {
                                return f;
                            } else {
                                if (((F) (((F) (((F) f.X[0]).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[1])),ring) != 0) {
                                    return f;
                                } else {
                                    F x = new F(((F) (((F) (((F) f.X[0]).X[0])).X[1])));
                                    F res = new F(F.POW, new F[]{i, x});
                                    return res;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования функции  вида cos((PI/2)*x) + i*sin((PI/2)*x)
     * в функцию  exp^(x*ln i)
     * @param f - входная функция вида cos((PI/2)*x) + i*sin((PI/2)*x)
     * @return возвращает exp^(x*ln i)
     */
    public static F Cos_Add_Sin_For_Exp_In_X_Mul_Ln_I(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        F pi = new F(new Fname("\\pi"));
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.ADD) {
            return f;
        } else {
            if ((((F) f.X[0]).name != F.COS) & (((F) f.X[1]).name != F.MULTIPLY)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.MULTIPLY) & (((F) (((F) f.X[1]).X[0])).compareTo(i,ring) != 0) & (((F) (((F) f.X[1]).X[1])).name != F.SIN)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[0])).X[0])).name != F.DIVIDE) & (((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.MULTIPLY)) {
                        return f;
                    } else {
                        if ((((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[0])).compareTo(pi,ring) != 0) & (((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[1])).compareTo(two,ring) != 0) & (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).name != F.DIVIDE)) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[0])).compareTo(pi,ring) != 0) &
                                    (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[1])).compareTo(two,ring) != 0)) {
                                return f;
                            } else {
                                if (((F) (((F) (((F) f.X[0]).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[1])),ring) != 0) {
                                    return f;
                                } else {
                                    F x = new F(((F) (((F) (((F) f.X[0]).X[0])).X[1])));
                                    F ln = new F(F.LN, new F[]{i});
                                    F mul = new F(F.MULTIPLY, new F[]{x, ln});
                                    F res = new F(F.EXP, new F[]{mul});
                                    return res;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**Процедура преобразования выражения вида exp^(-PI/2)
     * в i^i
     * @param f - входное выражение вида exp^(-PI/2)
     * @return возвращает i^i
     */
    public static F Exp_From_pi_For_I_In_I(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        F pi = new F(new Fname("\\pi"));
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.EXP) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.MULTIPLY) {
                return f;
            } else {
                if (!(((F) (((F) f.X[0]).X[0])).isMinusOne(ring)) & (((F) (((F) f.X[0]).X[1])).name != F.DIVIDE)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[1])).X[0])).compareTo(pi,ring) != 0) & (((F) (((F) (((F) f.X[0]).X[1])).X[1])).compareTo(two,ring) != 0)) {
                        return f;
                    } else {
                        F res = new F(F.POW, new F[]{i, i});
                        return res;
                    }
                }
            }
        }
    }

    /**Процедура преобразования выражения вида exp^(-PI/2)
     * в exp^(i*ln i)
     * @param f - входное выражение вида exp^(-PI/2)
     * @return возвращает exp^(i*ln i)
     */
    public static F Exp_From_pi_For_Exp_From_I_Ln_I(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        F pi = new F(new Fname("\\pi"));
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.EXP) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.MULTIPLY) {
                return f;
            } else {
                if (!(((F) (((F) f.X[0]).X[0])).isMinusOne(ring)) & (((F) (((F) f.X[0]).X[1])).name != F.DIVIDE)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[1])).X[0])).compareTo(pi,ring) != 0) & (((F) (((F) (((F) f.X[0]).X[1])).X[1])).compareTo(two,ring) != 0)) {
                        return f;
                    } else {
                        F ln = new F(F.LN, new F[]{i});
                        F mul = new F(F.MULTIPLY, new F[]{i, ln});
                        F res = new F(F.EXP, new F[]{mul});
                        return res;
                    }
                }
            }
        }
    }
    /**Процедура преобразования функции  вида exp в степени i*x* ln a
     * в функцию  a^(i*x)
     * @param f - входная функция вида exp в степени i*x* ln a
     * @return возвращает a^(i*x)
     */
    public static F transformExp_IX_LnForPow_IX(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        if (f.name != F.EXP) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.MULTIPLY) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.MULTIPLY) &&
                        (((F) (((F) f.X[0]).X[1])).name != F.LN)) {
                    return f;
                } else {
                    if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(i,ring) != 0) {
                        return f;
                    } else {
                        F z = new F((F) (((F) (((F) f.X[0]).X[0])).X[1]));
                        F a = new F((F) (((F) (((F) f.X[0]).X[1])).X[0]));
                        F iz = new F(F.MULTIPLY, new F[]{i, z});
                        F res = new F(F.POW, new F[]{a, iz});
                        return res;
                    }
                }
            }
        }
    }
    /**Процедура преобразования функции  вида exp в степени i* ln x
     * в функцию  x^i
     * @param f - входная функция вида exp в степени i* ln x
     * @return возвращает x^i
     */
    public static F transformExp_I_Ln_XForX_I(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        if (f.name != F.EXP) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.MULTIPLY) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).compareTo(i,ring) != 0) &&
                        (((F) (((F) f.X[0]).X[1])).name != F.LN)) {
                    return f;
                } else {
                    F z = new F((F) (((F) (((F) f.X[0]).X[1])).X[0]));
                    F res = new F(F.POW, new F[]{z, i});
                    return res;
                }
            }
        }
    }
    /**Процедура преобразования функции  вида exp в степени x* ln i
     * в функцию  i^x
     * @param f - входная функция вида exp в степени x* ln i
     * @return возвращает i^x
     */
    public static F transformExp_X_Ln_IForI_X(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        if (f.name != F.EXP) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.MULTIPLY) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[1])).name != F.LN) &&
                        (((F) (((F) (((F) f.X[0]).X[1])).X[0])).compareTo(i,ring) != 0)) {
                    return f;
                } else {
                    F z = new F((F) (((F) f.X[0]).X[0]));
                    F res = new F(F.POW, new F[]{i, z});
                    return res;
                }
            }
        }
    }
    ////////////////////////////////Преобразования//////////////////////////////////////////////
    /** Вспомогательная процедура
     * @param f - входная функция
     * @param name имя функции
     * @param index индекс
     * @return возращает одну из функций sin, cos, tg, ctg, sh, ch, th, cth
     */
    public static F transformTrigForExp(F f, int name, int index, Ring ring) {

        F res = new F();
        F z = new F(Polynom.polynomFromNot0Number(f.X[0]));
        NumberZ t = new NumberZ(2);
        NumberZ m_n = new NumberZ(-1);
        F two = new F(Polynom.polynomFromNot0Number(t));
        F min_n = new F(Polynom.polynomFromNot0Number(m_n));
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        switch (name) {
            case F.SIN: {
                F T1 = new F(F.EXP, new F[]{new F(F.MULTIPLY, new F[]{i, z})});
                F T2 = new F(F.EXP, new F[]{new F(F.MULTIPLY, new F[]{mines_i, z})});
                F T3 = new F(F.SUBTRACT, new F[]{T1, T2});
                F T4 = new F(F.DIVIDE, new F[]{T3, new F(F.MULTIPLY, new F[]{two, i})});
                res = T4;
                break;
            }
            case F.COS: {
                F T1 = new F(F.EXP, new F[]{new F(F.MULTIPLY, new F[]{i, z})});
                F T2 = new F(F.EXP, new F[]{new F(F.MULTIPLY, new F[]{mines_i, z})});
                F T3 = new F(F.ADD, new F[]{T1, T2});
                F T4 = new F(F.DIVIDE, new F[]{T3, two});
                res = T4;
                break;
            }
            case F.SH: {
                F T1 = new F(F.SUBTRACT, new F[]{new F(F.EXP, new F[]{z}),
                    new F(F.EXP, new F[]{new F(F.MULTIPLY, new F[]{min_n, z})})});
                F T2 = new F(F.DIVIDE, new F[]{T1, two});
                res = T2;
                break;
            }
            case F.CH: {
                F T1 = new F(F.ADD, new F[]{new F(F.EXP, new F[]{z}),
                    new F(F.EXP, new F[]{new F(F.MULTIPLY, new F[]{min_n, z})})});
                F T2 = new F(F.DIVIDE, new F[]{T1, two});
                res = T2;
                break;
            }
            case F.TG: {
                F T1 = new F(F.EXP, new F[]{new F(F.MULTIPLY, new F[]{i, z})});
                F T2 = new F(F.EXP, new F[]{new F(F.MULTIPLY, new F[]{mines_i, z})});
                F T3 = new F(F.SUBTRACT, new F[]{T1, T2});
                F T4 = new F(F.ADD, new F[]{T1, T2});
                F T5 = new F(F.DIVIDE, new F[]{T3, T4});
                F T6 = new F(F.MULTIPLY, new F[]{mines_i, T5});
                res = T6;
                break;
            }
            case F.CTG: {
                F T1 = new F(F.EXP, new F[]{new F(F.MULTIPLY, new F[]{i, z})});
                F T2 = new F(F.EXP, new F[]{new F(F.MULTIPLY, new F[]{mines_i, z})});
                F T3 = new F(F.SUBTRACT, new F[]{T1, T2});
                F T4 = new F(F.ADD, new F[]{T1, T2});
                F T5 = new F(F.DIVIDE, new F[]{T4, T3});
                F T6 = new F(F.MULTIPLY, new F[]{i, T5});
                res = T6;
                break;
            }
            case F.TH: {
                F T1 = new F(F.EXP, new F[]{new F(F.MULTIPLY, new F[]{i, z})});
                F T2 = new F(F.EXP, new F[]{new F(F.MULTIPLY, new F[]{min_n, z})});
                F T3 = new F(F.SUBTRACT, new F[]{T1, T2});
                F T4 = new F(F.ADD, new F[]{T1, T2});
                F T5 = new F(F.DIVIDE, new F[]{T3, T4});
                res = T5;
                break;
            }
            case F.CTH: {
                F T1 = new F(F.EXP, new F[]{new F(F.MULTIPLY, new F[]{i, z})});
                F T2 = new F(F.EXP, new F[]{new F(F.MULTIPLY, new F[]{min_n, z})});
                F T3 = new F(F.SUBTRACT, new F[]{T1, T2});
                F T4 = new F(F.ADD, new F[]{T1, T2});
                F T5 = new F(F.DIVIDE, new F[]{T4, T3});
                res = T5;
                break;
            }
            default: {
                return f;
            }
        }
        return res;
    }

    /**процедура замены функции,
     * содержащей в своих компонентах
     * тригонометрические и гиперболические функции sin, cos, tg, ctg, sh, ch, th, cth
     * эквивалентной ей функцией с заменой
     * тригонометрических и гиперболических функций на их представление через экспоненту
     * @param f входная функция,
     * содержащая компоненты, включающие sin, cos, tg, ctg, sh, ch, th, cth
     * @return возвращает функцию,где вместо указанных восьми функций
     * стоят их выражения через экспоненту
     */
    public static F rec_transformTrigForExp(F f, Ring ring) {

        if (f.X.length > 0) {
            for (int j = 0; j < f.X.length; j++) {
                if (f.X[j] instanceof Polynom) {
                    return SimpleIdentity.transformTrigForExp(f, f.name, 0, ring);
                } else if (((F) f.X[j]).name < F.FIRST_INFIX_NAME && ((((F) f.X[j]).name == F.SIN) || (((F) f.X[j]).name == F.COS)
                        || (((F) f.X[j]).name == F.TG) || (((F) f.X[j]).name == F.CTG) || (((F) f.X[j]).name == F.SH)
                        || (((F) f.X[j]).name == F.CH) || (((F) f.X[j]).name == F.TH) || (((F) f.X[j]).name == F.CTH))) {
                    if(((F) f.X[j]).X[0] instanceof Polynom)
                    f.X[j] = SimpleIdentity.transformTrigForExp(((F) f.X[j]), ((F) f.X[j]).name, 0, ring);
                    else SimpleIdentity.rec_transformTrigForExp((F) f.X[j], ring);
                } else {
                    SimpleIdentity.rec_transformTrigForExp((F) f.X[j], ring);
                }
            }
        }

        return f;
    }

    public static F rec_(F f, Ring ring) {
        if (f.X.length > 0) {
            for (int j = 0; j < f.X.length; j++) {
                   if (((F) f.X[j]).name < F.FIRST_INFIX_NAME && ((((F) f.X[j]).name == F.SIN) || (((F) f.X[j]).name == F.COS)
                        || (((F) f.X[j]).name == F.TG) || (((F) f.X[j]).name == F.CTG) || (((F) f.X[j]).name == F.SH)
                        || (((F) f.X[j]).name == F.CH) || (((F) f.X[j]).name == F.TH) || (((F) f.X[j]).name == F.CTH))) {
                    f.X[j] = SimpleIdentity.transformTrigForExp(((F) f.X[j]), ((F) f.X[j]).name, 0, ring);
                } else {
                    SimpleIdentity.rec_transformTrigForExp((F) f.X[j], ring);
                }
            }
        }
        return f;
    }
    /** Вспомогательная процедура
     * @param f- входная функция
     * @param name имя функции
     * @param index индекс
     * @return возращает одну из функций sin, cos, tg, ctg
     */
    public static F transformTrigForGip(F f, int name, int index, Ring ring) {

        F res = new F();
        F z = new F((F) f.X[0]);
        NumberZ t = new NumberZ(2);
        NumberZ m_n = new NumberZ(-1);
        F two = new F(Polynom.polynomFromNot0Number(t));
        F min_n = new F(Polynom.polynomFromNot0Number(m_n));
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        switch (name) {
            case F.SIN: {
                F mul = new F(F.MULTIPLY, new F[]{i, z});
                F sh = new F(F.SH, new F[]{mul});
                F _sh = new F(F.MULTIPLY, new F[]{mines_i, sh});
                res = _sh;
                break;
            }
            case F.COS: {
                F mul = new F(F.MULTIPLY, new F[]{i, z});
                F ch = new F(F.CH, new F[]{mul});
                res = ch;
                break;
            }
            case F.TG: {
                F mul = new F(F.MULTIPLY, new F[]{i, z});
                F th = new F(F.TH, new F[]{mul});
                F _th = new F(F.MULTIPLY, new F[]{mines_i, th});
                res = _th;
                break;
            }
            case F.CTG: {
                F mul = new F(F.MULTIPLY, new F[]{i, z});
                F cth = new F(F.CTH, new F[]{mul});
                F _cth = new F(F.MULTIPLY, new F[]{i, cth});
                res = _cth;
                break;
            }
            default: {
                return f;
            }
        }
        return res;
    }

    /**процедура замены функции,
     * содержащей в своих компонентах
     * тригонометрические функции sin, cos, tg, ctg
     * эквивалентной ей функцией с заменой тригонометрических функций на гиперболические
     * @param f - входная функция,
     * содержащая компоненты, включающие sin, cos, tg, ctg
     * @return возвращает функцию,где вместо указанных четырех функций
     * стоят их выражения через sh, ch, th, cth
     */
    public static F rec_transformTrigForGip(F f, Ring ring) {
        if (f.X.length > 0) {
            for (int j = 0; j < f.X.length; j++) {
                if (f.X[j] instanceof Polynom) {
                    return SimpleIdentity.transformTrigForExp(f, f.name, 0, ring);
                } else if (((F) f.X[j]).name < f.FIRST_INFIX_NAME && ((((F) f.X[j]).name == F.SIN) ||
                        (((F) f.X[j]).name == F.COS) || (((F) f.X[j]).name == F.TG) ||
                        (((F) f.X[j]).name == F.CTG))) {
                    f.X[j] = SimpleIdentity.transformTrigForGip(((F) f.X[j]), ((F) f.X[j]).name, 0, ring);
                } else {
                    SimpleIdentity.rec_transformTrigForGip((F) f.X[j], ring);
                }
            }
        }
        return f;

    }

    /** Вспомогательная процедура
     * @param f - входная функция функция
     * @param name имя функции
     * @param index индекс
     * @return возращает одну из функций  sh, ch, th, cth
     */
    public static F transformGipForTrig(F f, int name, int index, Ring ring) {

        F res = new F();
        F z = new F((F) f.X[0]);
        NumberZ t = new NumberZ(2);
        NumberZ m_n = new NumberZ(-1);
        F two = new F(Polynom.polynomFromNot0Number(t));
        F min_n = new F(Polynom.polynomFromNot0Number(m_n));
        Polynom P_i = Polynom.polynomFromNot0Number(ring.complexI());
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNot0Number(ring.complexMINUS_I());
        F mines_i = new F(P_mines_i); //-i
        switch (name) {
            case F.SH: {
                F mul = new F(F.MULTIPLY, new F[]{i, z});
                F sin = new F(F.SIN, new F[]{mul});
                F _sin = new F(F.MULTIPLY, new F[]{mines_i, sin});
                res = _sin;
                break;
            }
            case F.CH: {
                F mul = new F(F.MULTIPLY, new F[]{i, z});
                F cos = new F(F.COS, new F[]{mul});
                res = cos;
                break;
            }
            case F.TH: {
                F mul = new F(F.MULTIPLY, new F[]{i, z});
                F th = new F(F.TH, new F[]{mul});
                F _th = new F(F.MULTIPLY, new F[]{mines_i, th});
                res = _th;
                break;
            }
            case F.CTH: {
                F mul = new F(F.MULTIPLY, new F[]{i, z});
                F ctg = new F(F.CTG, new F[]{mul});
                F _ctg = new F(F.MULTIPLY, new F[]{i, ctg});
                res = _ctg;
                break;
            }
            default: {
                return f;
            }
        }
        return res;
    }

    /** процедура замены функции,
     * содержащей в своих компонентах
     * гиперболические функции sh, ch, th, cth
     * эквивалентной ей функцией с заменой гиперболических функций на тригонометрические
     * @param f - входная функция,
     * содержащая компоненты,включающие  sh, ch, th, cth
     * @return возвращает функцию,где вместо указанных четырех функций
     * стоят их выражения через sin, cos, tg, ctg
     *
     */
    public static F rec_transformGipForTrig(F f, Ring ring) {
        if (f.X.length > 0) {
            for (int j = 0; j < f.X.length; j++) {
                if (f.X[j] instanceof Polynom) {
                    return SimpleIdentity.transformTrigForExp(f, f.name, 0, ring);
                } else if (((F) f.X[j]).name < f.FIRST_INFIX_NAME && ((((F) f.X[j]).name == F.SH) ||
                        (((F) f.X[j]).name == F.CH) || (((F) f.X[j]).name == F.TH) ||
                        (((F) f.X[j]).name == F.CTH))) {
                    f.X[j] = SimpleIdentity.transformGipForTrig(((F) f.X[j]), ((F) f.X[j]).name, 0, ring);
                } else {
                    SimpleIdentity.rec_transformGipForTrig((F) f.X[j], ring);
                }
            }
        }
        return f;

    }



}
