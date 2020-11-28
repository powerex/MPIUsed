/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import com.mathpar.number.Ring;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 *
 * @author РљР»РѕС‡РЅРµРІР° РўР°С‚СЊСЏРЅР°(35 РіСЂСѓРїРїР°)
 */
public class simplifyTrig_andExp_andLog {

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё РІРёРґР° exp(z)
     * РІ С„СѓРЅРєС†РёСЋ СЃ РєРѕРјРїРѕРЅРµРЅС‚Р°РјРё ch(z) Рё sh(z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° exp(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ СЃСѓРјРјСѓ ch(z) Рё sh(z), РµСЃР»Рё z > 0
     * Рё СЂР°Р·РЅРѕСЃС‚СЊ ch(z) Рё sh(z), РµСЃР»Рё z<0
     */
    public static F decompExp(F f, Ring ring) {
        //       Polynom M = new Polynom("-1");
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
                    if (((F) f.X[0]).X[i].isMinusOne(ring)) {
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

    /** РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё РІРёРґР° exp(z)
     * РІ С„СѓРЅРєС†РёСЋ СЃ РєРѕРјРїРѕРЅРµРЅС‚Р°РјРё cos(z) Рё i*sin(z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° exp(i*z)]
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ СЃСѓРјРјСѓ cos(z) Рё i*sin(z), РµСЃР»Рё i*z > 0
     * Рё СЂР°Р·РЅРѕСЃС‚СЊ cos(z) Рё i*sin(z), РµСЃР»Рё i*z<0
     */
    public static F decompExpI(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.EXP) {
            return f;
        }
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
                    if (((F) f.X[0]).X[0].compareTo(i, ring) == 0) {
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

    /** РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё РІРёРґР° cos(z)
     * РІ С„СѓРЅРєС†РёСЋ ch (i*z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° cos(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ ch (i*z)
     */
    public static F transformCosForCh(F f, Ring ring) {
        if (f.name != F.COS) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
            F i = new F(P_i);
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F ch = new F(F.CH, new F[]{mul});
            return ch;
        }
    }

    /** РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° sin(z)
     * РІ С„СѓРЅРєС†РёСЋ  -i*sh (i*z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° sin(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ -i*sh (i*z)
     */
    public static F transformSinForSh(F f, Ring ring) {
        if (f.name != F.SIN) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
            F i = new F(P_i); //i
            Polynom P_mines_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
            F mines_i = new F(P_mines_i); //-i
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F sh = new F(F.SH, new F[]{mul});
            F _sh = new F(F.MULTIPLY, new F[]{mines_i, sh});
            return _sh;
        }
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° tg(z)
     * РІ С„СѓРЅРєС†РёСЋ  -i*th (i*z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° tg(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ -i*th (i*z)
     */
    public static F transformTgForTh(F f, Ring ring) {
        if (f.name != F.TG) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
            F i = new F(P_i); //i
            Polynom P_mines_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
            F mines_i = new F(P_mines_i); //-i
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F th = new F(F.TH, new F[]{mul});
            F _th = new F(F.MULTIPLY, new F[]{mines_i, th});
            return _th;
        }
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё РІРёРґР° СЃtg(z)
     * РІ С„СѓРЅРєС†РёСЋ  i*СЃth (i*z)
     * @param f С„СѓРЅРєС†РёСЏ ctg(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ i*cth (i*z)
     */
    public static F transformCtgForCth(F f, Ring ring) {
        if (f.name != F.CTG) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
            F i = new F(P_i); //i
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F cth = new F(F.CTH, new F[]{mul});
            F _cth = new F(F.MULTIPLY, new F[]{i, cth});
            return _cth;
        }
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё РІРёРґР° ch(z)
     * РІ С„СѓРЅРєС†РёСЋ  cos (i*z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° ch(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ cos (i*z)
     */
    public static F transformChForCos(F f, Ring ring) {
        if (f.name != F.CH) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
            F i = new F(P_i); //i
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F cos = new F(F.COS, new F[]{mul});
            return cos;
        }
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё РІРёРґР° sh(z)
     * РІ С„СѓРЅРєС†РёСЋ  -i*sin (i*z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° sh(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ -i*sin (i*z)
     */
    public static F transformShForSin(F f, Ring ring) {
        if (f.name != F.SH) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
            F i = new F(P_i); //i
            Polynom P_mines_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
            F mines_i = new F(P_mines_i); //-i
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F sin = new F(F.SIN, new F[]{mul});
            F _sin = new F(F.MULTIPLY, new F[]{mines_i, sin});
            return _sin;
        }
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё РІРёРґР° th(z)
     * РІ С„СѓРЅРєС†РёСЋ  -i*tg (i*z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° th(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ -i*tg (i*z)
     */
    public static F transformThForTg(F f, Ring ring) {
        if (f.name != F.TH) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
            F i = new F(P_i); //i
            Polynom P_mines_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
            F mines_i = new F(P_mines_i); //-i
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F tg = new F(F.TG, new F[]{mul});
            F _tg = new F(F.MULTIPLY, new F[]{mines_i, tg});
            return _tg;
        }
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё РІРёРґР° cth(z)
     * РІ С„СѓРЅРєС†РёСЋ  i*ctg (i*z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° cth(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ i*ctg (i*z)
     */
    public static F transformCthForCtg(F f, Ring ring) {
        if (f.name != F.CTH) {
            return f;
        } else {
            Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
            F i = new F(P_i); //i
            F z = new F((F) f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F ctg = new F(F.CTG, new F[]{mul});
            F _ctg = new F(F.MULTIPLY, new F[]{i, ctg});
            return _ctg;
        }
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° cos(z)
     * РІ С„СѓРЅРєС†РёСЋ СЃ РєРѕРјРїРѕРЅРµРЅС‚Р°РјРё exp РІ СЃС‚РµРїРµРЅРё i*z Рё exp РІ СЃС‚РµРїРµРЅРё -i*z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° cos(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ СЃСѓРјРјСѓ exp РІ СЃС‚РµРїРµРЅРё i*z Рё exp РІ СЃС‚РµРїРµРЅРё -i*z, РґРµР»РµРЅРЅСѓСЋ РЅР° 2
     */
    public static F transformCosForExp(F f, Ring ring) {
        NumberZ t = new NumberZ(2);
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
        F mines_i = new F(P_mines_i); //-i
        if (f.name != F.COS) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F mul = new F(F.MULTIPLY, new F[]{i, z});
            F _mul = new F(F.MULTIPLY, new F[]{mines_i, z});
            F E = new F(F.EXP, new F[]{mul});
            F _E = new F(F.EXP, new F[]{_mul});
            F sum = new F(F.ADD, new F[]{E, _E});
            F res = new F(F.DIVIDE, new Element[]{sum, Polynom.polynomFromNumber(t, ring)});
            return res;
        }
    }

/**Процедура преобразования функции cos(z)
* @param z - входная функция вида cos(z)
* @return преобразованную функцию вида (exp^{iz} + exp^{-iz})/(2)
*/
    public static F transformCos(Element z, Ring ring) {
        F E = new F(F.EXP, new Element[]{z.multiply(ring.numberI, ring)}); //exp i*z
        F _E = new F(F.EXP, new Element[]{z.multiply(ring.numberMINUS_I, ring)}); //exp -i*z
        F sub = (F)(new F(F.ADD, new F[]{ _E,E})).divide(NumberZ.TWO, ring);
        return sub; 

    }

    public static F transformSinForExp(F f, Ring ring) {
        NumberZ t = new NumberZ(2);
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
        F mines_i = new F(P_mines_i); //-i
        F two = new F(Polynom.polynomFromNumber(t, ring));
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

/**Процедура преобразования функции sin(z)
 * @param z - входная функция вида sin(z)
 * @return преобразованную функцию вида i(-exp^{iz} + exp^{-iz})/(2)
 */
    public static F transformSin(Element z, Ring ring) {
        F E = new F(F.EXP, new Element[]{z.multiply(ring.numberI, ring)}); //exp i*z
        F _E = new F(F.EXP, new Element[]{z.multiply(ring.numberMINUS_I, ring)}); //exp -i*z
        F sub = new F(F.SUBTRACT, new F[]{ _E,E});
        F res = new F(F.MULTIPLY, new Element[]{ring.numberI.divide(NumberZ.TWO, ring), sub});
        return res;
    }

    public static F transformTgForExp(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
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

/**Процедура преобразования функции tg(z)
* @param z - входная функция вида tg(z)
* @return преобразованную функцию вида i*(-exp^{iz} + exp^{-iz})/(exp^{iz} + exp^{-iz})
 */
    public static F transformTg(Element z, Ring ring) {
        F E = new F(F.EXP, new Element[]{z.multiply(ring.numberI, ring)}); //exp i*z
        F _E = new F(F.EXP, new Element[]{z.multiply(ring.numberMINUS_I, ring)}); //exp -i*z
        F sub = new F(F.SUBTRACT, new F[]{ _E,E});
        F res =new F(F.DIVIDE, new F(F.MULTIPLY, new Element[]{ring.numberI, sub}),
                               new F(F.ADD, new F[]{ _E,E}));
        return res;
    }

    public static F transformCtgForExp(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
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

/**Процедура преобразования функции ctg(z)
* @param z - входная функция вида ctg(z)
* @return преобразованную функцию вида -i*(exp^{iz} + exp^{-iz})/(exp^{iz} - exp^{-iz})
*/

    public static F transformCtg(Element z, Ring ring) {
        F E = new F(F.EXP, new Element[]{z.multiply(ring.numberI, ring)}); //exp i*z
        F _E = new F(F.EXP, new Element[]{z.multiply(ring.numberMINUS_I, ring)}); //exp -i*z
        F sub = new F(F.SUBTRACT, new F[]{E, _E});
        F add =new F(F.MULTIPLY, new F(F.ADD, new F[]{E, _E}), ring.numberI);
        F div = new F(F.DIVIDE, new F[]{add, sub});
        return div;
    }

/**Процедура преобразования функции arccos(z)
* @param z - входная функция вида arccos(z)
* @return преобразованную функцию вида -i*ln(z+sqrt(z^2 -1))
*/
    public static F transformArccos(Element z, Ring ring) {
        Element mI = ring.numberMINUS_I; //-i
        // F z = new F(f); //z
        F A = new F(F.POW, new Element[]{z, new NumberZ(2)}); //z^2
        F B = new F(F.SUBTRACT, new Element[]{A, NumberZ.ONE});//z^2-1
        F C = new F(F.SQRT, new F[]{B}); // sqrt z^2-1
        F D = new F(F.ADD, new Element[]{z, C});// z + sqrt z^2-1
        F E = new F(F.LN, new F[]{D});
        F res = new F(F.MULTIPLY, new Element[]{E, mI});
        return res;
    }

/**Процедура преобразования функции arcsin(z)
* @param z - входная функция вида arcsin(z)
* @return преобразованную функцию вида -i*ln(iz+sqrt(1-z^2))
*/
    public static F transformArcsin(Element f, Ring ring) {
        Element I = ring.numberI; //i
        Element mI = ring.numberMINUS_I; //-i
        F z = new F(f); //z
        F A = new F(F.POW, new Element[]{z, new NumberZ(2)}); //z^2
        F B = new F(F.SUBTRACT, new Element[]{NumberZ.ONE, A});//1-z^2
        F C = new F(F.SQRT, new F[]{B}); // sqrt 1-z^2
        F H = new F(F.MULTIPLY, new Element[]{I, z});
        F D = new F(F.ADD, new F[]{H, C});// z + sqrt 1-z^2
        F E = new F(F.LN, new F[]{D});
        F res = new F(F.MULTIPLY, new Element[]{E, mI});
        return res;
    }

/**Процедура преобразования функции arctg(z)
* @param z - входная функция вида arctg(z)
* @return преобразованную функцию вида (i/2)*(ln(1-iz)-ln(1+iz))
*/
    public static F transformArctg(Element z, Ring ring) {
        Element I = ring.numberI; //i
        Element mI = ring.numberMINUS_I; //-i
        Element mul = I.multiply(z, ring); // i*z
        Element _mul = mI.multiply(z, ring); //-i*z

        F A = new F(F.SUBTRACT, new Element[]{NumberZ.ONE, mul});//1-iz
        F B = new F(F.ADD, new Element[]{NumberZ.ONE, mul});//1+iz
        F C = new F(F.LN, new F[]{A}); // ln(1-iz)
        F K = new F(F.LN, new F[]{B}); // ln(1+iz)
        F H = new F(F.SUBTRACT, new F[]{C, K});// ln(1-iz)-ln(1+iz)
        F G = new F(F.DIVIDE, new Element[]{I, new NumberZ(2)}); //i/2
        F res = new F(F.MULTIPLY, new Element[]{G, H});
        return res;
    }

/**Процедура преобразования функции arcctg(z)
* @param z - входная функция вида arcctg(z)
* @return преобразованную функцию вида (i/2)*(ln(1-i/z)-ln(1+i/z))
*/
    public static F transformArcctg(Element z, Ring ring) {
        Element I = ring.numberI; //i
        F A = new F(F.DIVIDE, new Element[]{I, z}); //i/z
        F Aa = new F(F.SUBTRACT, new Element[]{NumberZ.ONE, A});//1-i/z
        F B = new F(F.ADD, new Element[]{NumberZ.ONE, A});//1+i/z
        F C = new F(F.LN, new F[]{Aa}); // ln(1-i/z)
        F K = new F(F.LN, new F[]{B}); // ln(1+i/z)
        F H = new F(F.SUBTRACT, new F[]{C, K});// ln(1-i/z)-ln(1+i/z)

        F G = new F(F.DIVIDE, new Element[]{I, new NumberZ(2)}); //i/2
        F res = new F(F.MULTIPLY, new F[]{G, H});
        return res;
    }

/**Процедура преобразования функции arccsc(z)
* @param z - входная функция вида arccsc(z)
* @return преобразованную функцию вида -i*ln(sqrt{1-1/z^2}+i/z)
*/
    public static F transformArccsc(Element z, Ring ring) {
        Element I = ring.numberI; //i
        Element mI = ring.numberMINUS_I; //-i
        F A = new F(F.POW, new Element[]{z, new NumberZ(2)}); //z^2
        F B = new F(F.DIVIDE, new Element[]{NumberZ.ONE, A}); //1/z^2
        F C = new F(F.SUBTRACT, new Element[]{NumberZ.ONE, B}); //1-1/z^2
        F D = new F(F.SQRT, new F[]{C}); //sqrt (1-1/z^2)
        F E = new F(F.DIVIDE, new Element[]{I, z}); // i/z
        F G = new F(F.ADD, new F[]{D, E}); //sqrt (1-1/z^2)+i/z
        F res = new F(F.MULTIPLY, new Element[]{mI, new F(F.LN, new F[]{G})});
        return res;
    }

/**Процедура преобразования функции arcsec(z)
* @param z - входная функция вида arcsec(z)
* @return преобразованную функцию вида -i*ln(sqrt{1/z^2-1}+1/z)
*/
    public static F transformArcsec(Element z, Ring ring) {
        Element I = ring.numberI; //i
        Element mI = ring.numberMINUS_I; //-i
        F A = new F(F.POW, new Element[]{z, new NumberZ(2)}); //z^2
        F B = new F(F.DIVIDE, new Element[]{NumberZ.ONE, A}); //1/z^2
        F C = new F(F.SUBTRACT, new Element[]{B, NumberZ.ONE}); //1/z^2 - 1
        F D = new F(F.SQRT, new F[]{C}); //sqrt (1/z^2 -1 )
        F E = new F(F.DIVIDE, new Element[]{NumberZ.ONE, z}); // 1/z
        F G = new F(F.ADD, new F[]{D, E}); //sqrt (1-1/z^2)+1/z
        F res = new F(F.MULTIPLY, new Element[]{mI, new F(F.LN, new F[]{G})});
        return res;
    }

    /*РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° ch(z)
     * РІ С„СѓРЅРєС†РёСЋ СЃ РєРѕРјРїРѕРЅРµРЅС‚Р°РјРё exp РІ СЃС‚РµРїРµРЅРё z Рё exp РІ СЃС‚РµРїРµРЅРё -z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° ch(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ СЃСѓРјРјСѓ exp РІ СЃС‚РµРїРµРЅРё z Рё exp РІ СЃС‚РµРїРµРЅРё -z, РґРµР»РµРЅРЅСѓСЋ РЅР° 2
     */
    public static F transformChForExp(F f) {
        NumberZ t = new NumberZ(2);
        if (f.name != F.CH) {
            return f;
        } else {
            F z = new F(f.X[0]);
            F A = new F(F.MULTIPLY, new Element[]{NumberZ.MINUS_ONE, z});
            F B = new F(F.EXP, new F[]{A});
            F C = new F(F.EXP, new F[]{z});
            F D = new F(F.ADD, new F[]{C, B});
            F div = new F(F.DIVIDE, new Element[]{D, new NumberZ(2)});
            return div;
        }
    }

//        public static F transformCH(  Element z, Ring ring) {
//           F A = new F(F.MULTIPLY, new Element[]{ NumberZ.MINUS_ONE, z});
//           F a= new F(z);
//           F s = new F(F.EXP, new F[]{a});
//           F v = new F(F.POW, new Element[]{s, z});
//           F _v = new F(F.POW, new Element[]{s, A});
//           F sum = new F(F.ADD, new F[]{v, _v});
//           F res =new F(F.DIVIDE, new Element[]{sum,new NumberZ(2)});
//           return res; }

/**Процедура преобразования функции ch(z)
* @param z - входная функция вида ch(z)
* @return преобразованную функцию вида (exp^{z} + exp^{-z})/(2)
*/
    public static F transformCH(Element z, Ring ring) {
        Element A = z.negate(ring);
        F E = new F(F.EXP, new Element[]{z}); //exp z
        F _E = new F(F.EXP, new Element[]{A}); //exp -z
        F sum = new F(F.ADD, new F[]{E, _E});
        F res = new F(F.DIVIDE, new Element[]{sum, new NumberZ(2)});
        return res;
    }

/**Процедура преобразования функции sh(z)
* @param z - входная функция вида sh(z)
* @return преобразованную функцию вида (exp^{z} - exp^{-z})/(2)
*/
    public static F transformSH(Element z, Ring ring) {
        Element A = z.negate(ring);
        F E = new F(F.EXP, new Element[]{z}); //exp z
        F _E = new F(F.EXP, new Element[]{A}); //exp -z
        F sub = new F(F.SUBTRACT, new F[]{E, _E});
        F res = new F(F.DIVIDE, new Element[]{sub, new NumberZ(2)});
        return res;
    }

/**Процедура преобразования функции th(z)
* @param z - входная функция вида th(z)
* @return преобразованную функцию вида (exp^{z} - exp^{-z})/(exp^{z} + exp^{-z})
*/
    public static F transformTH(Element z, Ring ring) {
        // F A = new F(F.MULTIPLY, new Element[]{ NumberZ.MINUS_ONE, z});
        Element A = z.negate(ring);
        F E = new F(F.EXP, new Element[]{z}); //exp z
        F _E = new F(F.EXP, new Element[]{A}); //exp -z
        F sub = new F(F.SUBTRACT, new F[]{E, _E});
        F sum = new F(F.ADD, new F[]{E, _E});
        F res = new F(F.DIVIDE, new Element[]{sub, sum});
        return res;
    }

    public static F transformCTH(Element z, Ring ring) {
        Element A = z.negate(ring);
        F E = new F(F.EXP, new Element[]{z}); //exp z
        F _E = new F(F.EXP, new Element[]{A}); //exp -z
        F sub = new F(F.SUBTRACT, new F[]{E, _E});
        F sum = new F(F.ADD, new F[]{E, _E});
        F res = new F(F.DIVIDE, new Element[]{sum, sub});
        return res;
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° sh(z)
     * РІ С„СѓРЅРєС†РёСЋ СЃ РєРѕРјРїРѕРЅРµРЅС‚Р°РјРё exp РІ СЃС‚РµРїРµРЅРё z Рё exp РІ СЃС‚РµРїРµРЅРё -z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° sh(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ СЂР°Р·РЅРѕСЃС‚СЊ exp РІ СЃС‚РµРїРµРЅРё z Рё exp РІ СЃС‚РµРїРµРЅРё -z, РґРµР»РµРЅРЅСѓСЋ РЅР° 2
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
            F div = new F(F.DIVIDE, new Element[]{D, Polynom.polynomFromNumber(t, ring)});
            return div;
        }
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° th(z)
     * РІ С„СѓРЅРєС†РёСЋ СЃ РєРѕРјРїРѕРЅРµРЅС‚Р°РјРё exp РІ СЃС‚РµРїРµРЅРё z Рё exp РІ СЃС‚РµРїРµРЅРё -z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° th(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ С‡Р°СЃС‚РЅРѕРµ РѕС‚ СЂР°Р·РЅРѕСЃС‚Рё exp РІ СЃС‚РµРїРµРЅРё z Рё exp РІ СЃС‚РµРїРµРЅРё -z
     * РЅР° СЃСѓРјРјСѓ e РІ СЃС‚РµРїРµРЅРё z Рё e РІ СЃС‚РµРїРµРЅРё -z
     */
    public static F transformThForExp(F f) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° cth(z)
     * РІ С„СѓРЅРєС†РёСЋ СЃ РєРѕРјРїРѕРЅРµРЅС‚Р°РјРё exp РІ СЃС‚РµРїРµРЅРё z Рё exp РІ СЃС‚РµРїРµРЅРё -z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° cth(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ С‡Р°СЃС‚РЅРѕРµ РѕС‚ СЃСѓРјРјС‹ exp РІ СЃС‚РµРїРµРЅРё z Рё exp РІ СЃС‚РµРїРµРЅРё -z
     * РЅР° СЂР°Р·РЅРѕСЃС‚СЊ exp РІ СЃС‚РµРїРµРЅРё z Рё exp РІ СЃС‚РµРїРµРЅРё -z
     */
    public static F transformCthForExp(F f) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ РїРѕРєР°Р·Р°С‚РµР»СЊРЅРѕР№ С„СѓРЅРєС†РёРё  РІРёРґР° a^(i*x)
     * РІ С„СѓРЅРєС†РёСЋ exp РІ СЃС‚РµРїРµРЅРё i*x*ln(a)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° a^(i*x)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ exp РІ СЃС‚РµРїРµРЅРё i*x*ln(a)
     */
    public static F transformPowForExp(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        if (f.name != F.POW) {
            return f;
        } else {
            if ((((F) f.X[1]).name != F.MULTIPLY) & (((F) f.X[0]).X[0].compareTo(i, ring) != 0)) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ РїРѕРєР°Р·Р°С‚РµР»СЊРЅРѕР№ С„СѓРЅРєС†РёРё  РІРёРґР° a^(i*x)
     * РІ С„СѓРЅРєС†РёСЋ СЃ РєРѕРјРїРѕРЅРµРЅС‚Р°РјРё cos(x*ln a) Рё i*sin (x*ln a)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° a^(i*x)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ СЃСѓРјРјСѓ cos(x*ln a) Рё i*sin (x*ln a)
     */
    public static F transformPowForCosAndSin(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        if (f.name != F.POW) {
            return f;
        } else {
            if ((((F) f.X[1]).name != F.MULTIPLY) & (((F) f.X[1]).X[0].compareTo(i, ring) != 0)) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ СЃС‚РµРїРµРЅРЅРѕР№ С„СѓРЅРєС†РёРё  РІРёРґР° x^i
     * РІ С„СѓРЅРєС†РёСЋ exp РІ СЃС‚РµРїРµРЅРё i*ln(x)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° x^i
     * @return РІРѕР·Р°СЂР°С‰Р°РµС‚ exp РІ СЃС‚РµРїРµРЅРё i*ln(x)
     */
    public static F transformX_iForExp(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        if (f.name != F.POW) {
            return f;
        } else {
            if (((F) f.X[1]).compareTo(i, ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ СЃС‚РµРїРµРЅРЅРѕР№ С„СѓРЅРєС†РёРё  РІРёРґР° x^i
     * РІ С„СѓРЅРєС†РёСЋ СЃ РєРѕРјРїРѕРЅРµРЅС‚Р°РјРё cos(ln x) Рё i*sin (ln x)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° x^i
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ СЃСѓРјРјСѓ cos(ln x) Рё i*sin (ln x)
     */
    public static F transformX_iForCosAndSin(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        if (f.name != F.POW) {
            return f;
        } else {
            if (((F) f.X[1]).compareTo(i, ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ РїРѕРєР°Р·Р°С‚РµР»СЊРЅРѕР№ С„СѓРЅРєС†РёРё  РІРёРґР° i^x
     * РІ С„СѓРЅРєС†РёСЋ exp РІ СЃС‚РµРїРµРЅРё x*ln(i)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° i^x
     * @return РІРѕР·Р°СЂР°С‰Р°РµС‚ exp РІ СЃС‚РµРїРµРЅРё x*ln(i)
     */
    public static F transformI_xForExp(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        if (f.name != F.POW) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(i, ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ РїРѕРєР°Р·Р°С‚РµР»СЊРЅРѕР№ С„СѓРЅРєС†РёРё  РІРёРґР° i^x
     * РІ С„СѓРЅРєС†РёСЋ СЃ РєРѕРјРїРѕРЅРµРЅС‚Р°РјРё cos((PI/2)*x) Рё i*sin ((PI/2)*x)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° i^x
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ СЃСѓРјРјСѓ cos((PI/2)*x) Рё i*sin ((PI/2)*x)
     */
    public static F transformI_xForCosAndSin(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        F pi = new F(new Fname("\\pi"));
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.POW) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(i, ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° exp РІ СЃС‚РµРїРµРЅРё x*ln(i)
     * РІ С„СѓРЅРєС†РёСЋ СЃ РєРѕРјРїРѕРЅРµРЅС‚Р°РјРё cos((PI/2)*x) Рё i*sin ((PI/2)*x)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° exp РІ СЃС‚РµРїРµРЅРё x*ln(i)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ СЃСѓРјРјСѓ cos((PI/2)*x) Рё i*sin ((PI/2)*x)
     */
    public static F transformExpForCosAndSin(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
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
                if ((((F) (((F) f.X[0]).X[1])).name != F.LN)
                        & (((F) (((F) (((F) f.X[0]).X[1])).X[0])).compareTo(i, ring) != 0)) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° exp РІ СЃС‚РµРїРµРЅРё i*ln i
     * РІ  exp РІ СЃС‚РµРїРµРЅРё -PI/2
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° exp РІ СЃС‚РµРїРµРЅРё i*ln i
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ exp РІ СЃС‚РµРїРµРЅРё -PI/2
     */
    public static F Exp_I_Ln_I_For_Exp_Pi(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        F pi = new F(new Fname("\\pi"));
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.EXP) {
            return f;
        } else {
            if ((((F) (((F) f.X[0]).X[0])).compareTo(i, ring) != 0) & (((F) (((F) f.X[0]).X[1])).name != F.LN)) {
                return f;
            } else {
                if (((F) (((F) (((F) f.X[0]).X[1])).X[0])).compareTo(i, ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ i^i
     * РІ  exp РІ СЃС‚РµРїРµРЅРё -PI/2
     * @param f - РІС…РѕРґРЅРѕР№ РїР°СЂР°РјРµС‚СЂ РІРёРґР° i^i
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ exp РІ СЃС‚РµРїРµРЅРё -PI/2
     */
    public static F transformI_iFofExp_Pi(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        F pi = new F(new Fname("\\pi"));
        NumberZ T = new NumberZ(2);
        F two = new F(T);
        if (f.name != F.POW) {
            return f;
        } else {
            if (((F) (f.X[0])).compareTo(i, ring) != 0) {
                return f;
            } else {
                F div = new F(F.DIVIDE, new F[]{pi, two});
                F _div = new F(F.MULTIPLY, new Element[]{NumberZ.MINUS_ONE, div});
                F res = new F(F.EXP, new F[]{_div});
                return res;
            }
        }
    }
    ////////////////////////////РћР±СЂР°С‚РЅС‹Рµ РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ/////////////////////////////////

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° ch(z) +(-) sh(z)
     * РІ С„СѓРЅРєС†РёСЋ exp РІ СЃС‚РµРїРµРЅРё z(-z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° ch(z) +(-) sh(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ exp РІ СЃС‚РµРїРµРЅРё z(-z)
     */
    public static F transformChAddShForExp(F f, Ring ring) {
        if ((f.name != F.ADD) & (f.name != F.SUBTRACT)) {
            return f;
        } else {
            if ((f.name == F.ADD) & ((((F) f.X[0]).name == F.CH) || (((F) f.X[0]).name == F.SH))
                    & ((((F) f.X[1]).name == F.SH) || (((F) f.X[1]).name == F.CH))) {
                if (((F) f.X[0]).X[0].compareTo(((F) f.X[1]).X[0], ring) == 0) {
                    F z = new F(((F) f.X[0]).X[0]);
                    F exp = new F(F.EXP, new F[]{z});
                    return exp;
                }
                return f;
            } else {
                if ((f.name == F.SUBTRACT) & (((F) f.X[0]).name == F.CH)
                        & (((F) f.X[1]).name == F.SH)
                        & (((F) f.X[0]).X[0].compareTo(((F) f.X[1]).X[0], ring) == 0)) {
                    F z = new F(((F) f.X[0]).X[0]);
                    F _z = new F(F.MULTIPLY, new Element[]{NumberZ.MINUS_ONE, z});
                    F exp = new F(F.EXP, new F[]{_z});
                    return exp;
                }
            }
            return f;
        }
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° (exp^z + exp^(-z))/2
     * РІ С„СѓРЅРєС†РёСЋ ch(z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° (exp^z + exp^(-z))/2
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ ch(z)
     */
    public static F transformExpZAddExp_ZForCh(F f, Ring ring) {
        if (f.name != F.DIVIDE) {
            return f;
        } else {
            NumberZ t = new NumberZ(2);
            F two = new F(Polynom.polynomFromNumber(t, ring));
            if ((((F) f.X[0]).name != F.ADD) & (((F) f.X[1]).compareTo(two, ring) != 0)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.EXP)
                        & (((F) (((F) f.X[0]).X[1])).name != F.EXP)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[1])).X[0])).name != F.MULTIPLY)
                            & !((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).isMinusOne(ring)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[1])), ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° (exp^z - exp^(-z))/2
     * РІ С„СѓРЅРєС†РёСЋ sh(z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° (exp^z - exp^(-z))/2
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ sh(z)
     */
    public static F transformExpZSubtractExp_ZForSh(F f, Ring ring) {
        if (f.name != F.DIVIDE) {
            return f;
        } else {
            NumberZ t = new NumberZ(2);
            F two = new F(Polynom.polynomFromNumber(t, ring));
            if ((((F) f.X[0]).name != F.SUBTRACT) & (((F) f.X[1]).compareTo(two, ring) != 0)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.EXP)
                        & (((F) (((F) f.X[0]).X[1])).name != F.EXP)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[1])).X[0])).name != F.MULTIPLY)
                            & !((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).isMinusOne(ring)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[1])), ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° (exp^z - exp^(-z))/(exp^z + exp^(-z))
     * РІ С„СѓРЅРєС†РёСЋ th(z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° (exp^z - exp^(-z))/(exp^z + exp^(-z))
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ th(z)
     */
    public static F transformComposition_ExpForTh(F f, Ring ring) {
        if (f.name != F.DIVIDE) {
            return f;
        } else {
            if ((((F) f.X[0]).name != F.SUBTRACT) & (((F) f.X[1]).name != F.ADD)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.EXP)
                        & (((F) (((F) f.X[0]).X[1])).name != F.EXP)
                        & (((F) (((F) f.X[1]).X[0])).name != F.EXP)
                        & (((F) (((F) f.X[1]).X[1])).name != F.EXP)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[1])).X[0])).name != F.MULTIPLY)
                            & !((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).isMinusOne(ring)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[1])), ring) != 0) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.MULTIPLY)
                                    & !((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).isMinusOne(ring)) {
                                return f;
                            } else {
                                if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[1])), ring) != 0) {
                                    return f;
                                } else {
                                    if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) f.X[1]).X[0])).X[0])), ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° (exp^z + exp^(-z))/(exp^z - exp^(-z))
     * РІ С„СѓРЅРєС†РёСЋ СЃth(z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° (exp^z + exp^(-z))/(exp^z - exp^(-z))
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ cth(z)
     */
    public static F transformComposition_ExpForCth(F f, Ring ring) {
        if (f.name != F.DIVIDE) {
            return f;
        } else {
            if ((((F) f.X[0]).name != F.ADD) & (((F) f.X[1]).name != F.SUBTRACT)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.EXP)
                        & (((F) (((F) f.X[0]).X[1])).name != F.EXP)
                        & (((F) (((F) f.X[1]).X[0])).name != F.EXP)
                        & (((F) (((F) f.X[1]).X[1])).name != F.EXP)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[1])).X[0])).name != F.MULTIPLY)
                            & !((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).isMinusOne(ring)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[1])), ring) != 0) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.MULTIPLY)
                                    & !((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).isMinusOne(ring)) {
                                return f;
                            } else {
                                if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[1])), ring) != 0) {
                                    return f;
                                } else {
                                    if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) f.X[1]).X[0])).X[0])), ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° cos(z) +(-)i* sin(z)
     * РІ С„СѓРЅРєС†РёСЋ exp РІ СЃС‚РµРїРµРЅРё i*z(-i*z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° cos(z) +(-)i* sin(z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ exp РІ СЃС‚РµРїРµРЅРё i*z(-i*z)
     */
    public static F transformCosAddSinForExp(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        Polynom P_minus_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
        F minus_i = new F(P_minus_i); //-i
        F i = new F(P_i); //i
        if ((f.name != F.ADD) & (f.name != F.SUBTRACT)) {
            return f;
        } else {
            if ((f.name == F.ADD) & (((F) f.X[0]).name == F.COS)
                    & (((F) (((F) f.X[1]).X[1])).name == F.SIN)
                    & (((F) f.X[1]).name == F.MULTIPLY)
                    & (((F) f.X[1]).X[0].compareTo(i, ring) == 0)) {
                if (((F) f.X[0]).X[0].compareTo(((F) (((F) f.X[1]).X[1])).X[0], ring) == 0) {
                    F z = new F(((F) f.X[0]).X[0]);
                    F iz = new F(F.MULTIPLY, new F[]{i, z});
                    F exp = new F(F.EXP, new F[]{iz});
                    return exp;
                }
                return f;
            } else {
                if ((f.name == F.SUBTRACT) & (((F) f.X[0]).name == F.COS)
                        & (((F) (((F) f.X[1]).X[1])).name == F.SIN)
                        & (((F) f.X[1]).name == F.MULTIPLY)
                        & (((F) f.X[1]).X[0].compareTo(i, ring) == 0)
                        & (((F) f.X[0]).X[0].compareTo(((F) (((F) f.X[1]).X[1])).X[0], ring) == 0)) {
                    F z = new F(((F) f.X[0]).X[0]);
                    F _iz = new F(F.MULTIPLY, new F[]{minus_i, z});
                    F exp = new F(F.EXP, new F[]{_iz});
                    return exp;
                }
            }
            return f;
        }
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° (exp^(i*z) + exp^(-i*z))/2
     * РІ С„СѓРЅРєС†РёСЋ cos(z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° (exp^(i*z) + exp^(-i*z))/2
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ cos(z)
     */
    public static F transformComposition_ExpForCos(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        Polynom P_minus_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
        F minus_i = new F(P_minus_i); //-i
        F i = new F(P_i); //i
        if (f.name != F.DIVIDE) {
            return f;
        } else {
            NumberZ t = new NumberZ(2);
            F two = new F(Polynom.polynomFromNumber(t, ring));
            if ((((F) f.X[0]).name != F.ADD) & (((F) f.X[1]).compareTo(two, ring) != 0)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.EXP)
                        & (((F) (((F) f.X[0]).X[1])).name != F.EXP)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[0])).X[0])).name != F.MULTIPLY)
                            & (((F) (((F) (((F) f.X[0]).X[1])).X[0])).name != F.MULTIPLY)
                            & !(((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).isMinusOne(ring))
                            & (((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[0])).compareTo(i, ring) != 0)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[1])), ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° (exp^(i*z) - exp^(-i*z))/(2*i)
     * РІ С„СѓРЅРєС†РёСЋ sin(z)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° (exp^(i*z) - exp^(-i*z))/(2*i)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ sin(z)
     */
    public static F transformComposition_ExpForSin(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        Polynom P_minus_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
        F minus_i = new F(P_minus_i); //-i
        F i = new F(P_i); //i
        if (f.name != F.DIVIDE) {
            return f;
        } else {
            NumberZ t = new NumberZ(2);
            F two = new F(Polynom.polynomFromNumber(t, ring));
            if ((((F) f.X[0]).name != F.SUBTRACT) & (((F) f.X[1]).name != F.MULTIPLY)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.EXP)
                        & (((F) (((F) f.X[0]).X[1])).name != F.EXP)
                        & (((F) (((F) f.X[1]).X[0])).compareTo(two, ring) != 0)
                        & (((F) (((F) f.X[1]).X[1])).compareTo(i, ring) != 0)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[0])).X[0])).name != F.MULTIPLY)
                            & (((F) (((F) (((F) f.X[0]).X[1])).X[0])).name != F.MULTIPLY)
                            & (((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[0])).compareTo(minus_i, ring) != 0)
                            & (((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[0])).compareTo(i, ring) != 0)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) f.X[0]).X[1])).X[0])).X[1])), ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР°
     * i*((exp^(i*z) - exp^(-i*z))/(exp^(i*z) + exp^(-i*z)))
     * РІ С„СѓРЅРєС†РёСЋ tg z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° i*((exp^(i*z) - exp^(-i*z))/(exp^(i*z) + exp^(-i*z)))
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ tg z
     */
    public static F transformComposition_ExpForTg(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        Polynom P_minus_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
        F minus_i = new F(P_minus_i); //-i
        F i = new F(P_i); //i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if ((((F) f.X[0]).compareTo(i, ring) != 0)
                    & (((F) f.X[1]).name != F.DIVIDE)) {
                return f;
            } else {
                if ((((F) (((F) f.X[1]).X[0])).name != F.SUBTRACT)
                        & (((F) (((F) f.X[1]).X[1])).name != F.ADD)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[1]).X[0])).X[0])).name != F.EXP)
                            & (((F) (((F) (((F) f.X[1]).X[0])).X[1])).name != F.EXP)
                            & (((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.EXP)
                            & (((F) (((F) (((F) f.X[1]).X[1])).X[1])).name != F.EXP)) {
                        return f;
                    } else {
                        if ((((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).name != F.MULTIPLY)
                                & (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).name != F.MULTIPLY)
                                & (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).name != F.MULTIPLY)
                                & (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[1])).X[0])).name != F.MULTIPLY)) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).X[1])), ring) != 0)
                                    & (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[1])).X[0])).X[1])), ring) != 0)
                                    & (((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[1])), ring) != 0)) {
                                return f;
                            } else {
                                if ((((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[0])).compareTo(i, ring) != 0)
                                        & (((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).X[0])).compareTo(minus_i, ring) != 0)
                                        & (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[0])).compareTo(i, ring) != 0)
                                        & (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[1])).X[0])).X[0])).compareTo(minus_i, ring) != 0)) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР°
     * -i*((exp^(i*z) + exp^(-i*z))/(exp^(i*z) - exp^(-i*z)))
     * РІ С„СѓРЅРєС†РёСЋ СЃtg z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° -i*((exp^(i*z) + exp^(-i*z))/(exp^(i*z) - exp^(-i*z)))
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ ctg z
     */
    public static F transformComposition_ExpForCtg(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        Polynom P_minus_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
        F minus_i = new F(P_minus_i); //-i
        F i = new F(P_i); //i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if ((((F) f.X[0]).compareTo(minus_i, ring) != 0)
                    & (((F) f.X[1]).name != F.DIVIDE)) {
                return f;
            } else {
                if ((((F) (((F) f.X[1]).X[0])).name != F.ADD)
                        & (((F) (((F) f.X[1]).X[1])).name != F.SUBTRACT)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[1]).X[0])).X[0])).name != F.EXP)
                            & (((F) (((F) (((F) f.X[1]).X[0])).X[1])).name != F.EXP)
                            & (((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.EXP)
                            & (((F) (((F) (((F) f.X[1]).X[1])).X[1])).name != F.EXP)) {
                        return f;
                    } else {
                        if ((((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).name != F.MULTIPLY)
                                & (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).name != F.MULTIPLY)
                                & (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).name != F.MULTIPLY)
                                & (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[1])).X[0])).name != F.MULTIPLY)) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).X[1])), ring) != 0)
                                    & (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[1])).X[0])).X[1])), ring) != 0)
                                    & (((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[1])), ring) != 0)) {
                                return f;
                            } else {
                                if ((((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[0])).X[0])).X[0])).compareTo(i, ring) != 0)
                                        & (((F) (((F) (((F) (((F) (((F) f.X[1]).X[0])).X[1])).X[0])).X[0])).compareTo(minus_i, ring) != 0)
                                        & (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[0])).compareTo(i, ring) != 0)
                                        & (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[1])).X[0])).X[0])).compareTo(minus_i, ring) != 0)) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° ch (i*z)
     * РІ С„СѓРЅРєС†РёСЋ cos z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° ch (i*z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ cos z
     */
    public static F transformChIZForCos(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        if (f.name != F.CH) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.MULTIPLY) {
                return f;
            } else {
                if (((F) (((F) f.X[0]).X[0])).compareTo(i, ring) != 0) {
                    return f;
                } else {
                    F z = new F((F) (((F) f.X[0]).X[1]));
                    F cos = new F(F.COS, new F[]{z});
                    return cos;
                }
            }
        }
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° -i*sh (i*z)
     * РІ С„СѓРЅРєС†РёСЋ sin z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° -i*sh (i*z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ sin z
     */
    public static F transform_IShIZForSin(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        Polynom P_minus_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
        F minus_i = new F(P_minus_i); //-i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(minus_i, ring) != 0) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.SH) {
                    return f;
                } else {
                    if (((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(i, ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° -i*th (i*z)
     * РІ С„СѓРЅРєС†РёСЋ tg z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° -i*th (i*z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ tg z
     */
    public static F transform_IThIZForTg(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        Polynom P_minus_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
        F minus_i = new F(P_minus_i); //-i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(minus_i, ring) != 0) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.TH) {
                    return f;
                } else {
                    if (((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(i, ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° i*cth (i*z)
     * РІ С„СѓРЅРєС†РёСЋ СЃtg z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° i*cth (i*z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ ctg z
     */
    public static F transformICthIZForCtg(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(i, ring) != 0) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.CTH) {
                    return f;
                } else {
                    if (((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(i, ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° cos (i*z)
     * РІ С„СѓРЅРєС†РёСЋ ch z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° cos (i*z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ ch z
     */
    public static F transformCosIZForCh(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        if (f.name != F.COS) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.MULTIPLY) {
                return f;
            } else {
                if (((F) (((F) f.X[0]).X[0])).compareTo(i, ring) != 0) {
                    return f;
                } else {
                    F z = new F((F) (((F) f.X[0]).X[1]));
                    F ch = new F(F.CH, new F[]{z});
                    return ch;
                }
            }
        }
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° -i*sin (i*z)
     * РІ С„СѓРЅРєС†РёСЋ sh z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° -i*sin (i*z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ sh z
     */
    public static F transform_ISinIZForSh(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        Polynom P_minus_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
        F minus_i = new F(P_minus_i); //-i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(minus_i, ring) != 0) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.SIN) {
                    return f;
                } else {
                    if (((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(i, ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° -i*tg (i*z)
     * РІ С„СѓРЅРєС†РёСЋ th z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° -i*tg (i*z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ th z
     */
    public static F transform_ITgIZForTh(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        Polynom P_minus_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
        F minus_i = new F(P_minus_i); //-i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(minus_i, ring) != 0) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.TG) {
                    return f;
                } else {
                    if (((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(i, ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° i*ctg(i*z)
     * РІ С„СѓРЅРєС†РёСЋ cth z
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° i*ctg(i*z)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ cth z
     */
    public static F transformICtgIZForCth(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        if (f.name != F.MULTIPLY) {
            return f;
        } else {
            if (((F) f.X[0]).compareTo(i, ring) != 0) {
                return f;
            } else {
                if (((F) f.X[1]).name != F.CTG) {
                    return f;
                } else {
                    if (((F) (((F) f.X[1]).X[0])).name != F.MULTIPLY) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[1]).X[0])).X[0])).compareTo(i, ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° cos(x*ln a)+i*sin(x*ln a)
     * РІ С„СѓРЅРєС†РёСЋ  a^(i*x)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° cos(x*ln a)+i*sin(x*ln a)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ a^(i*x)
     */
    public static F transformCosAddSinForPow(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        if (f.name != F.ADD) {
            return f;
        } else {
            if ((((F) f.X[0]).name != F.COS) & (((F) f.X[1]).name != F.MULTIPLY)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.MULTIPLY) & (((F) (((F) f.X[1]).X[0])).compareTo(i, ring) != 0) & (((F) (((F) f.X[1]).X[1])).name != F.SIN)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[0])).X[1])).name != F.LN) & (((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.MULTIPLY)) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[1])).name != F.LN) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])), ring) != 0) & (((F) (((F) (((F) (((F) f.X[0]).X[0])).X[1])).X[0])).compareTo(
                                    ((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[1])).X[0])), ring) != 0)) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° cos(ln x) + i*sin(ln x)
     * РІ С„СѓРЅРєС†РёСЋ  x^i
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° cos(ln x) + i*sin(ln x)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ x^i
     */
    public static F transformCosAddISinForX_I(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        if (f.name != F.ADD) {
            return f;
        } else {
            if ((((F) f.X[0]).name != F.COS) & (((F) f.X[1]).name != F.MULTIPLY)) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.LN) & (((F) (((F) f.X[1]).X[0])).compareTo(i, ring) != 0) & (((F) (((F) f.X[1]).X[1])).name != F.SIN)) {
                    return f;
                } else {
                    if (((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.LN) {
                        return f;
                    } else {
                        if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])), ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° cos((PI/2)*x) + i*sin((PI/2)*x)
     * РІ С„СѓРЅРєС†РёСЋ  i^x
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° cos((PI/2)*x) + i*sin((PI/2)*x)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ i^x
     */
    public static F transformCosAddSinForI_X(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
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
                if ((((F) (((F) f.X[0]).X[0])).name != F.MULTIPLY) & (((F) (((F) f.X[1]).X[0])).compareTo(i, ring) != 0) & (((F) (((F) f.X[1]).X[1])).name != F.SIN)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[0])).X[0])).name != F.DIVIDE) & (((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.MULTIPLY)) {
                        return f;
                    } else {
                        if ((((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[0])).compareTo(pi, ring) != 0) & (((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[1])).compareTo(two, ring) != 0)
                                & (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).name != F.DIVIDE)) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[0])).compareTo(pi, ring) != 0)
                                    & (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[1])).compareTo(two, ring) != 0)) {
                                return f;
                            } else {
                                if (((F) (((F) (((F) f.X[0]).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[1])), ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° cos((PI/2)*x) + i*sin((PI/2)*x)
     * РІ С„СѓРЅРєС†РёСЋ  exp^(x*ln i)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° cos((PI/2)*x) + i*sin((PI/2)*x)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ exp^(x*ln i)
     */
    public static F Cos_Add_Sin_For_Exp_In_X_Mul_Ln_I(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
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
                if ((((F) (((F) f.X[0]).X[0])).name != F.MULTIPLY) & (((F) (((F) f.X[1]).X[0])).compareTo(i, ring) != 0) & (((F) (((F) f.X[1]).X[1])).name != F.SIN)) {
                    return f;
                } else {
                    if ((((F) (((F) (((F) f.X[0]).X[0])).X[0])).name != F.DIVIDE) & (((F) (((F) (((F) f.X[1]).X[1])).X[0])).name != F.MULTIPLY)) {
                        return f;
                    } else {
                        if ((((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[0])).compareTo(pi, ring) != 0) & (((F) (((F) (((F) (((F) f.X[0]).X[0])).X[0])).X[1])).compareTo(two, ring) != 0) & (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).name != F.DIVIDE)) {
                            return f;
                        } else {
                            if ((((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[0])).compareTo(pi, ring) != 0)
                                    & (((F) (((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[0])).X[1])).compareTo(two, ring) != 0)) {
                                return f;
                            } else {
                                if (((F) (((F) (((F) f.X[0]).X[0])).X[1])).compareTo(((F) (((F) (((F) (((F) f.X[1]).X[1])).X[0])).X[1])), ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ РІС‹СЂР°Р¶РµРЅРёСЏ РІРёРґР° exp^(-PI/2)
     * РІ i^i
     * @param f - РІС…РѕРґРЅРѕРµ РІС‹СЂР°Р¶РµРЅРёРµ РІРёРґР° exp^(-PI/2)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ i^i
     */
    public static F Exp_From_pi_For_I_In_I(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
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
                    if ((((F) (((F) (((F) f.X[0]).X[1])).X[0])).compareTo(pi, ring) != 0) & (((F) (((F) (((F) f.X[0]).X[1])).X[1])).compareTo(two, ring) != 0)) {
                        return f;
                    } else {
                        F res = new F(F.POW, new F[]{i, i});
                        return res;
                    }
                }
            }
        }
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ РІС‹СЂР°Р¶РµРЅРёСЏ РІРёРґР° exp^(-PI/2)
     * РІ exp^(i*ln i)
     * @param f - РІС…РѕРґРЅРѕРµ РІС‹СЂР°Р¶РµРЅРёРµ РІРёРґР° exp^(-PI/2)
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ exp^(i*ln i)
     */
    public static F Exp_From_pi_For_Exp_From_I_Ln_I(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
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
                    if ((((F) (((F) (((F) f.X[0]).X[1])).X[0])).compareTo(pi, ring) != 0) & (((F) (((F) (((F) f.X[0]).X[1])).X[1])).compareTo(two, ring) != 0)) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° exp РІ СЃС‚РµРїРµРЅРё i*x* ln a
     * РІ С„СѓРЅРєС†РёСЋ  a^(i*x)
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° exp РІ СЃС‚РµРїРµРЅРё i*x* ln a
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ a^(i*x)
     */
    public static F transformExp_IX_LnForPow_IX(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        if (f.name != F.EXP) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.MULTIPLY) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).name != F.MULTIPLY)
                        && (((F) (((F) f.X[0]).X[1])).name != F.LN)) {
                    return f;
                } else {
                    if (((F) (((F) (((F) f.X[0]).X[0])).X[0])).compareTo(i, ring) != 0) {
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

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° exp РІ СЃС‚РµРїРµРЅРё i* ln x
     * РІ С„СѓРЅРєС†РёСЋ  x^i
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° exp РІ СЃС‚РµРїРµРЅРё i* ln x
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ x^i
     */
    public static F transformExp_I_Ln_XForX_I(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        if (f.name != F.EXP) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.MULTIPLY) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[0])).compareTo(i, ring) != 0)
                        && (((F) (((F) f.X[0]).X[1])).name != F.LN)) {
                    return f;
                } else {
                    F z = new F((F) (((F) (((F) f.X[0]).X[1])).X[0]));
                    F res = new F(F.POW, new F[]{z, i});
                    return res;
                }
            }
        }
    }

    /**РџСЂРѕС†РµРґСѓСЂР° РїСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ С„СѓРЅРєС†РёРё  РІРёРґР° exp РІ СЃС‚РµРїРµРЅРё x* ln i
     * РІ С„СѓРЅРєС†РёСЋ  i^x
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ РІРёРґР° exp РІ СЃС‚РµРїРµРЅРё x* ln i
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ i^x
     */
    public static F transformExp_X_Ln_IForI_X(F f, Ring ring) {
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        if (f.name != F.EXP) {
            return f;
        } else {
            if (((F) f.X[0]).name != F.MULTIPLY) {
                return f;
            } else {
                if ((((F) (((F) f.X[0]).X[1])).name != F.LN)
                        && (((F) (((F) (((F) f.X[0]).X[1])).X[0])).compareTo(i, ring) != 0)) {
                    return f;
                } else {
                    F z = new F((F) (((F) f.X[0]).X[0]));
                    F res = new F(F.POW, new F[]{i, z});
                    return res;
                }
            }
        }
    }
    ////////////////////////////////РџСЂРµРѕР±СЂР°Р·РѕРІР°РЅРёСЏ//////////////////////////////////////////////

    /** Р’СЃРїРѕРјРѕРіР°С‚РµР»СЊРЅР°СЏ РїСЂРѕС†РµРґСѓСЂР°
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ
     * @param name РёРјСЏ С„СѓРЅРєС†РёРё
     * @param index РёРЅРґРµРєСЃ
     * @return РІРѕР·СЂР°С‰Р°РµС‚ РѕРґРЅСѓ РёР· С„СѓРЅРєС†РёР№ sin, cos, tg, ctg, sh, ch, th, cth
     */
    public static F transformTrigForExp(F f, int name, int index, Ring ring) {

        F res = new F();
        F z = new F(Polynom.polynomFromNumber(f.X[0], ring));
        NumberZ t = new NumberZ(2);
        NumberZ m_n = new NumberZ(-1);
        F two = new F(Polynom.polynomFromNumber(t, ring));
        F min_n = new F(Polynom.polynomFromNumber(m_n, ring));
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
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

    /**РїСЂРѕС†РµРґСѓСЂР° Р·Р°РјРµРЅС‹ С„СѓРЅРєС†РёРё,
     * СЃРѕРґРµСЂР¶Р°С‰РµР№ РІ СЃРІРѕРёС… РєРѕРјРїРѕРЅРµРЅС‚Р°С…
     * С‚СЂРёРіРѕРЅРѕРјРµС‚СЂРёС‡РµСЃРєРёРµ Рё РіРёРїРµСЂР±РѕР»РёС‡РµСЃРєРёРµ С„СѓРЅРєС†РёРё sin, cos, tg, ctg, sh, ch, th, cth
     * СЌРєРІРёРІР°Р»РµРЅС‚РЅРѕР№ РµР№ С„СѓРЅРєС†РёРµР№ СЃ Р·Р°РјРµРЅРѕР№
     * С‚СЂРёРіРѕРЅРѕРјРµС‚СЂРёС‡РµСЃРєРёС… Рё РіРёРїРµСЂР±РѕР»РёС‡РµСЃРєРёС… С„СѓРЅРєС†РёР№ РЅР° РёС… РїСЂРµРґСЃС‚Р°РІР»РµРЅРёРµ С‡РµСЂРµР· СЌРєСЃРїРѕРЅРµРЅС‚Сѓ
     * @param f РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ,
     * СЃРѕРґРµСЂР¶Р°С‰Р°СЏ РєРѕРјРїРѕРЅРµРЅС‚С‹, РІРєР»СЋС‡Р°СЋС‰РёРµ sin, cos, tg, ctg, sh, ch, th, cth
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ С„СѓРЅРєС†РёСЋ,РіРґРµ РІРјРµСЃС‚Рѕ СѓРєР°Р·Р°РЅРЅС‹С… РІРѕСЃСЊРјРё С„СѓРЅРєС†РёР№
     * СЃС‚РѕСЏС‚ РёС… РІС‹СЂР°Р¶РµРЅРёСЏ С‡РµСЂРµР· СЌРєСЃРїРѕРЅРµРЅС‚Сѓ
     */
    public static F rec_transformTrigForExp(F f, Ring ring) {
        if (f.X.length > 0) {
            for (int j = 0; j < f.X.length; j++) {
                if (f.X[j] instanceof Polynom) {
                    return simplifyTrig_andExp_andLog.transformTrigForExp(f, f.name, 0, ring);
                } else if (((F) f.X[j]).name < f.FIRST_INFIX_NAME && ((((F) f.X[j]).name == F.SIN) || (((F) f.X[j]).name == F.COS)
                        || (((F) f.X[j]).name == F.TG) || (((F) f.X[j]).name == F.CTG) || (((F) f.X[j]).name == F.SH)
                        || (((F) f.X[j]).name == F.CH) || (((F) f.X[j]).name == F.TH) || (((F) f.X[j]).name == F.CTH))) {
                    f.X[j] = simplifyTrig_andExp_andLog.transformTrigForExp(((F) f.X[j]), ((F) f.X[j]).name, 0, ring);
                } else {
                    simplifyTrig_andExp_andLog.rec_transformTrigForExp((F) f.X[j], ring);
                }
            }
        }
        return f;

    }

    /** Р’СЃРїРѕРјРѕРіР°С‚РµР»СЊРЅР°СЏ РїСЂРѕС†РµРґСѓСЂР°
     * @param f- РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ
     * @param name РёРјСЏ С„СѓРЅРєС†РёРё
     * @param index РёРЅРґРµРєСЃ
     * @return РІРѕР·СЂР°С‰Р°РµС‚ РѕРґРЅСѓ РёР· С„СѓРЅРєС†РёР№ sin, cos, tg, ctg
     */
    public static F transformTrigForGip(F f, int name, int index, Ring ring) {

        F res = new F();
        F z = new F((F) f.X[0]);
        NumberZ t = new NumberZ(2);
        NumberZ m_n = new NumberZ(-1);
        F two = new F(Polynom.polynomFromNumber(t, ring));
        F min_n = new F(Polynom.polynomFromNumber(m_n, ring));
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
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

    /**РїСЂРѕС†РµРґСѓСЂР° Р·Р°РјРµРЅС‹ С„СѓРЅРєС†РёРё,
     * СЃРѕРґРµСЂР¶Р°С‰РµР№ РІ СЃРІРѕРёС… РєРѕРјРїРѕРЅРµРЅС‚Р°С…
     * С‚СЂРёРіРѕРЅРѕРјРµС‚СЂРёС‡РµСЃРєРёРµ С„СѓРЅРєС†РёРё sin, cos, tg, ctg
     * СЌРєРІРёРІР°Р»РµРЅС‚РЅРѕР№ РµР№ С„СѓРЅРєС†РёРµР№ СЃ Р·Р°РјРµРЅРѕР№ С‚СЂРёРіРѕРЅРѕРјРµС‚СЂРёС‡РµСЃРєРёС… С„СѓРЅРєС†РёР№ РЅР° РіРёРїРµСЂР±РѕР»РёС‡РµСЃРєРёРµ
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ,
     * СЃРѕРґРµСЂР¶Р°С‰Р°СЏ РєРѕРјРїРѕРЅРµРЅС‚С‹, РІРєР»СЋС‡Р°СЋС‰РёРµ sin, cos, tg, ctg
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ С„СѓРЅРєС†РёСЋ,РіРґРµ РІРјРµСЃС‚Рѕ СѓРєР°Р·Р°РЅРЅС‹С… С‡РµС‚С‹СЂРµС… С„СѓРЅРєС†РёР№
     * СЃС‚РѕСЏС‚ РёС… РІС‹СЂР°Р¶РµРЅРёСЏ С‡РµСЂРµР· sh, ch, th, cth
     */
    public static F rec_transformTrigForGip(F f, Ring ring) {
        if (f.X.length > 0) {
            for (int j = 0; j < f.X.length; j++) {
                if (f.X[j] instanceof Polynom) {
                    return simplifyTrig_andExp_andLog.transformTrigForExp(f, f.name, 0, ring);
                } else if (((F) f.X[j]).name < f.FIRST_INFIX_NAME && ((((F) f.X[j]).name == F.SIN)
                        || (((F) f.X[j]).name == F.COS) || (((F) f.X[j]).name == F.TG)
                        || (((F) f.X[j]).name == F.CTG))) {
                    f.X[j] = simplifyTrig_andExp_andLog.transformTrigForGip(((F) f.X[j]), ((F) f.X[j]).name, 0, ring);
                } else {
                    simplifyTrig_andExp_andLog.rec_transformTrigForGip((F) f.X[j], ring);
                }
            }
        }
        return f;

    }

    /** Р’СЃРїРѕРјРѕРіР°С‚РµР»СЊРЅР°СЏ РїСЂРѕС†РµРґСѓСЂР°
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ С„СѓРЅРєС†РёСЏ
     * @param name РёРјСЏ С„СѓРЅРєС†РёРё
     * @param index РёРЅРґРµРєСЃ
     * @return РІРѕР·СЂР°С‰Р°РµС‚ РѕРґРЅСѓ РёР· С„СѓРЅРєС†РёР№  sh, ch, th, cth
     */
    public static F transformGipForTrig(F f, int name, int index, Ring ring) {

        F res = new F();
        F z = new F((F) f.X[0]);
        NumberZ t = new NumberZ(2);
        NumberZ m_n = new NumberZ(-1);
        F two = new F(Polynom.polynomFromNumber(t, ring));
        F min_n = new F(Polynom.polynomFromNumber(m_n, ring));
        Polynom P_i = Polynom.polynomFromNumber(ring.complexI(), ring);
        F i = new F(P_i); //i
        Polynom P_mines_i = Polynom.polynomFromNumber(ring.complexMINUS_I(), ring);
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

    /** РїСЂРѕС†РµРґСѓСЂР° Р·Р°РјРµРЅС‹ С„СѓРЅРєС†РёРё,
     * СЃРѕРґРµСЂР¶Р°С‰РµР№ РІ СЃРІРѕРёС… РєРѕРјРїРѕРЅРµРЅС‚Р°С…
     * РіРёРїРµСЂР±РѕР»РёС‡РµСЃРєРёРµ С„СѓРЅРєС†РёРё sh, ch, th, cth
     * СЌРєРІРёРІР°Р»РµРЅС‚РЅРѕР№ РµР№ С„СѓРЅРєС†РёРµР№ СЃ Р·Р°РјРµРЅРѕР№ РіРёРїРµСЂР±РѕР»РёС‡РµСЃРєРёС… С„СѓРЅРєС†РёР№ РЅР° С‚СЂРёРіРѕРЅРѕРјРµС‚СЂРёС‡РµСЃРєРёРµ
     * @param f - РІС…РѕРґРЅР°СЏ С„СѓРЅРєС†РёСЏ,
     * СЃРѕРґРµСЂР¶Р°С‰Р°СЏ РєРѕРјРїРѕРЅРµРЅС‚С‹,РІРєР»СЋС‡Р°СЋС‰РёРµ  sh, ch, th, cth
     * @return РІРѕР·РІСЂР°С‰Р°РµС‚ С„СѓРЅРєС†РёСЋ,РіРґРµ РІРјРµСЃС‚Рѕ СѓРєР°Р·Р°РЅРЅС‹С… С‡РµС‚С‹СЂРµС… С„СѓРЅРєС†РёР№
     * СЃС‚РѕСЏС‚ РёС… РІС‹СЂР°Р¶РµРЅРёСЏ С‡РµСЂРµР· sin, cos, tg, ctg
     *
     */
    public static F rec_transformGipForTrig(F f, Ring ring) {
        if (f.X.length > 0) {
            for (int j = 0; j < f.X.length; j++) {
                if (f.X[j] instanceof Polynom) {
                    return simplifyTrig_andExp_andLog.transformTrigForExp(f, f.name, 0, ring);
                } else if (((F) f.X[j]).name < f.FIRST_INFIX_NAME && ((((F) f.X[j]).name == F.SH)
                        || (((F) f.X[j]).name == F.CH) || (((F) f.X[j]).name == F.TH)
                        || (((F) f.X[j]).name == F.CTH))) {
                    f.X[j] = simplifyTrig_andExp_andLog.transformGipForTrig(((F) f.X[j]), ((F) f.X[j]).name, 0, ring);
                } else {
                    simplifyTrig_andExp_andLog.rec_transformGipForTrig((F) f.X[j], ring);
                }
            }
        }
        return f;

    }
}
