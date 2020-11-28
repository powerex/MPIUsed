/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import java.util.*;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 * Класс TrigonometricExpand предназначен для упрощения компрозиций, содержащих
 * тригонометрические функции, а также логарифмические и экспонентциальные
 * функции, аргументом которых является комплексное число, используя следующие
 * тождества: 1 = Sin(a+b) = Sin(a)Cos(b) + Cos(a)Sin(b) Cos(a+b) = Cos(a)Cos(b)
 * - Sin(a)Sin(b) Sin(a-b) = Sin(a)Cos(b) - Cos(a)Sin(b) Cos(a-b) = Cos(a)Cos(b)
 * + Sin(a)Sin(b) tg и Ctg мы рассматриваем как: tg = Sin/Cos Ctg = Cos/Sin И
 * применяем формулы для Sin и Cos, описанные выше e^iz - e^-iz = 2iSin(z) e^iz
 * + e^-iz = 2Cos(z) Ln(1+iz) - Ln(1-iz) = 2i*arctg(z); Ln(1-iz) - Ln(1+iz) =
 * 2i*arcctg(z);
 *
 * @author dmitry
 */
public class TrigonometricExpand {

    public TrigonometricExpand() {
    }

//---------------------Trigonometric Functions----------------------------------
//-------------------------D E C O M P O S I T I O N----------------------------
    /**
     * Данный блок рассматривает раскладывание функций Sin и Cos по формулам:
     * Sin(a+b) = Sin(a)Cos(b) + Cos(a)Sin(b) Cos(a+b) = Cos(a)Cos(b) -
     * Sin(a)Sin(b) Sin(a-b) = Sin(a)Cos(b) - Cos(a)Sin(b) Cos(a-b) =
     * Cos(a)Cos(b) + Sin(a)Sin(b) tg и Ctg мы рассматриваем как: tg = Sin/Cos
     * Ctg = Cos/Sin И применяем формулы для Sin и Cos, описанные выше.
     */
    /**
     * Процедура обхода дерева ??? По-моему, это Вычисление ExpandTr для суммы
     * тригонометрических функций. Входной элемент = не функция, то возвращается
     * в исходном виде, Входной элемент = функция, тогда каждый аргумент
     * отправляется на ExpandTr и полученные результаты суммируются
     *
     * @param е - input Element
     * @param ring - ring
     *
     * @return
     */
    public Element onTree(Element e, Ring ring) {
        F f;
        //  if(e instanceof Fname) return e;
        if (e instanceof F)  f = (F) e;  else   return e;
        //  F f = (F)e;
        //if (f.X.length == 1) {return ExpandTr(f.X[0], ring);}
        Element[] arg = new Element[f.X.length];
        for (int i = 0; i < f.X.length; i++) {
            arg[i] = (f.X[i] instanceof F) ? ExpandTr((F) f.X[i], ring) : f.X[i];
        }
        return new F(((F)e).name, arg);
    }

    /**
     * процедура ввода обратной замены
     *
     * @param func - input function
     * @param cf - параметр, в котором хранится вся информация о функции после
     * перехода от древовидного представления к полиномиальному виду
     *
     * @return
     */
    public Element reConvert(F func, CanonicForms cf) {
        Element[] arg = new Element[func.X.length];
        for (int i = 0; i < func.X.length; i++) {
            arg[i] = (func.X[i] instanceof F) ? reConvert((F) func.X[i], cf) : cf.Unconvert_Polynom_to_Element(func.X[i]);
        }
        return new F(func.name, arg);
    }

    /**
     * метод, раскладывающий Sin, Cos, tg, Ctg по основным тождествам
     *
     * @param el
     * @param ring
     *
     * @return
     */
    public F ExpandTr(Element el, Ring ring) {CanonicForms g=ring.CForm;
        if (el instanceof F) {
            F f = (F) el;
            switch (f.name) {

                case F.SIN:
                    if (!(f.X[0] instanceof Polynom)) {
                      //  CanonicForms g = new CanonicForms(ring);
                        Element onTr = onTree(f.X[0], ring);
                        if (onTr.isItNumber()) {
                            return new F(F.SIN, onTr);
                        }
                        if ((onTr instanceof Fname) && (onTr.equals(new Fname("\\pi")))) {
                            return new F(NumberZ.ZERO);
                        }
                        Element pol = g.ElementToPolynom(onTr, true);
                        if (pol instanceof Fraction) {
                            F ff = new F(F.DIVIDE, ((Fraction) pol).num, ((Fraction) pol).denom);
                            F t = new F(F.SIN, ff);
                            F r = (F) reConvert(t, g);
                            return r;
                        }
                        F t = Sin((Polynom) pol, ring);
                        F r = (F) reConvert(t, g);
                        return r;
                    }
                    return (f.X[0] instanceof Polynom) ? Sin((Polynom) f.X[0], ring) : f;

                case F.COS:
                    if (!(f.X[0] instanceof Polynom)) {
                      //  CanonicForms g = new CanonicForms(ring);  //2015
                        Element onTr = onTree(f.X[0], ring);
                        if (onTr.isItNumber()) {
                            return new F(F.COS, onTr);
                        }
                        if ((onTr instanceof Fname) && (onTr.equals(new Fname("\\pi")))) {
                            return new F(NumberZ.MINUS_ONE);
                        }
                        Element pol = g.ElementToPolynom(onTr, true);
                        if (pol instanceof Fraction) {
                            F ff = new F(F.DIVIDE, ((Fraction) pol).num, ((Fraction) pol).denom);
                            F t = new F(F.COS, ff);
                            F r = (F) reConvert(t, g);
                            return r;
                        }
                        F t = Cos(pol, ring);
                        F r = (F) reConvert(t, g);

                        return r;
                    }
                    return (f.X[0] instanceof Polynom) ? Cos((Polynom) f.X[0], ring) : f;

                case F.TG:
                    if (!(f.X[0] instanceof Polynom)) {
                    //    CanonicForms g = new CanonicForms(ring);  //2015
                        Element onTr = onTree(f.X[0], ring);
                        if ((onTr instanceof Fname) && (onTr.equals(new Fname("\\pi")))) {
                            return new F(NumberZ.ZERO);
                        }
                        Element pol = g.ElementToPolynom(onTr, true);
                        F t = Tg((Polynom) pol, ring);
                        F r = (F) reConvert(t, g);
                        return r;
                    }
                    return (f.X[0] instanceof Polynom) ? Tg((Polynom) f.X[0], ring) : f;

                case F.CTG:
                    if (!(f.X[0] instanceof Polynom)) {
                      //  CanonicForms g = new CanonicForms(ring);  //2015
                        Element onTr = onTree(f.X[0], ring);
                        if ((onTr instanceof Fname) && (onTr.equals(new Fname("\\pi")))) {
                            return new F(Element.NAN);
                        }
                        Element pol = g.ElementToPolynom(onTr, true);
                        F t = Ctg((Polynom) pol, ring);
                        F r = (F) reConvert(t, g);
                        return r;
                    }
                    return (f.X[0] instanceof Polynom) ? Ctg((Polynom) f.X[0], ring) : f;

                case F.ADD:
                case F.SUBTRACT:
                    Element result = (F) onTree(f, ring);
                    result =g.simplify_init(result);
                    return (result instanceof F)? (F) result: new F(result);


                case F.MULTIPLY:
                    for (int i = 0; i < f.X.length; i++) {
                        if (f.X[i] instanceof F) {
                            switch (((F) f.X[i]).name) {
                                case F.LOG:
                                    if (f.X[0] instanceof F) {
                                        f.X[0] = (F) ExpandTr((F) f.X[0], ring);
                                    }
                                    if (f.X[1] instanceof F) {
                                        f.X[1] = (F) ExpandTr((F) f.X[1], ring);
                                    }
                                    break;

                                case F.LN:
                                case F.LG:
                                case F.COS:
                                case F.SIN:
                                case F.TG:
                                case F.CTG:
                                    f.X[i] = ExpandTr((F) f.X[i], ring);
                                    break;

                                case F.intPOW:
                                    if (((F) f.X[i]).X[0] instanceof F) {
                                        ((F) f.X[i]).X[0] = ExpandTr((F) ((F) f.X[i]).X[0], ring);
                                    }
                                    break;
                                default:
                                    continue;
                            }
                        } else {
                            continue;
                        }
                    }
                    return f;
                case F.DIVIDE:
                    if (f.X[0] instanceof F) {
                        f.X[0] = (F) ExpandTr((F) f.X[0], ring);
                    }
                    if (f.X[1] instanceof F) {
                        f.X[1] = (F) ExpandTr((F) f.X[1], ring);
                    }
                    return f;
                case F.intPOW:
                    if (f.X[0] instanceof F) {
                        f.X[0] = (F) ExpandTr((F) f.X[0], ring);
                    }
                    return f;

                default:
                    return f;
            }
        }
        return null;
    }

    /**
     * метод раскладывает sin по формуле sin(a+b)=sin(a)cos(b)+cos(a)sin(b)
     *
     * @param pol
     *
     * @return
     */
    public F Sin(Polynom pol, Ring ring) {
        if (pol.coeffs.length == 1) {
            for (int i = 2; i < ring.posConst.length - 6; i++) {
                if (pol.coeffs[0].equals(ring.posConst[i], ring)) {
                    int[] pow = new int[pol.powers.length];
                    System.arraycopy(pol.powers, 0, pow, 0, pol.powers.length);
                    int c = ring.posConst[i].divide(new NumberZ(2), ring).intValue();
                    Polynom first = new Polynom(pow, new Element[] {new NumberZ(c)});
                    Polynom second = pol.subtract(first, ring);
                    return Sin(first, second, ring);
                }
            }
            return new F(F.SIN, pol);
        }
//разделяем полином на 2 полинома
//первый полином сосотоит из превого монома
        int kol = pol.powers.length / pol.coeffs.length;
        int[] pow = new int[kol];
        System.arraycopy(pol.powers, 0, pow, 0, pow.length);
        Polynom first = new Polynom(pow, new Element[] {pol.coeffs[0]});
//второй полином - все остальные мономы
        Element[] coeff = new Element[pol.coeffs.length - 1];
        System.arraycopy(pol.coeffs, 1, coeff, 0, pol.coeffs.length - 1);
        int[] power = new int[pol.powers.length - kol];
        System.arraycopy(pol.powers, kol, power, 0, pol.powers.length - kol);
        Polynom second = new Polynom(power, coeff);

        return Sin(first, second, ring);
    }

    //раскладываем синус имея в руках сумму/разность 2-х мономов
    public F Sin(Polynom first, Polynom second, Ring ring) {
        Element cos = new F(F.COS, second);
        Element sin = new F(F.SIN, second);
        Element cos1 = new F(F.COS, second.negate(ring));
        Element sin1 = new F(F.SIN, second.negate(ring));
 //     if((onTr instanceof Fname)&&(onTr.equals(new Fname("\\pi")))) return new F(NumberZ.MINUS_ONE);

        if (second.coeffs.length == 1) {
            Element p1, p2;
            if (first.coeffs[0].isNegative() & second.coeffs[0].isNegative()) {
                p1 = new F(F.MULTIPLY, new Element[] {ring.numberMINUS_ONE, new F(F.SIN, first.negate(ring)), cos1});
                p2 = new F(F.MULTIPLY, new Element[] {ring.numberMINUS_ONE, new F(F.COS, first.negate(ring)), sin1});
            } else if (first.coeffs[0].isNegative()) {
                p1 = new F(F.MULTIPLY, new Element[] {ring.numberMINUS_ONE, new F(F.SIN, first.negate(ring)), cos});
                p2 = new F(F.MULTIPLY, new F(F.COS, first.negate(ring)), sin);
            } else if (second.coeffs[0].isNegative()) {
                p1 = new F(F.MULTIPLY, new F(F.SIN, first), cos1);
                p2 = new F(F.MULTIPLY, new Element[] {ring.numberMINUS_ONE, new F(F.COS, first), sin1});
            } else {
                p1 = new F(F.MULTIPLY, new F(F.SIN, first), cos);
                p2 = new F(F.MULTIPLY, new F(F.COS, first), sin);
            }
            F Result = new F(F.ADD, p1, p2);
            return Result;
        }
        if (first.coeffs[0].isNegative()) {
            F pos1 = new F(F.MULTIPLY, new Element[] {ring.numberMINUS_ONE, new F(F.SIN, first.negate(ring)), Cos(second, ring)});
            F pos2 = new F(F.MULTIPLY, new F(F.COS, first.negate(Ring.ringR64xyzt)), Sin(second, ring));
            return new F(F.ADD, pos1, pos2);
        }
        F pos1 = new F(F.MULTIPLY, new F(F.SIN, first), Cos(second, ring));
        F pos2 = new F(F.MULTIPLY, new F(F.COS, first), Sin(second, ring));
        return new F(F.ADD, pos1, pos2);
    }

    /**
     * метод раскладывает cos по формуле cos(a+b)=cos(a)cos(b)+sin(a)sin(b)
     *
     * @param pol
     *
     * @return
     */
    public F Cos(Element pol1, Ring ring) {
        Polynom pol = null;
        if (pol1 instanceof Polynom) {
            pol = (Polynom) pol1;
        } else {
            return new F(pol1);
        }
        if (pol.coeffs.length == 1) {
            for (int i = 2; i < ring.posConst.length - 6; i++) {
                if (pol.coeffs[0].equals(ring.posConst[i], ring)) {
                    int[] pow = new int[pol.powers.length];
                    System.arraycopy(pol.powers, 0, pow, 0, pol.powers.length);
                    Element c = ring.posConst[i].divide(new NumberZ(2), ring).toNewRing(Ring.Z, ring);
                    Polynom first = new Polynom(pow, new Element[] {c});
                    Polynom second = pol.subtract(first, ring);
                    return Cos(first, second, ring);
                }
            }
            return new F(F.COS, pol);
        }
//разделяем полином на 2 полинома
//первй полином сосотоит из превого монома
        int kol = pol.powers.length / pol.coeffs.length;// число переменных
        int[] pow = new int[kol];
        System.arraycopy(pol.powers, 0, pow, 0, kol);
        Polynom first = new Polynom(pow, new Element[] {pol.coeffs[0]});
//второй полином - все остальные мономы
        Element[] coeff = new Element[pol.coeffs.length - 1];
        System.arraycopy(pol.coeffs, 1, coeff, 0, pol.coeffs.length - 1);
        int[] power = new int[pol.powers.length - kol];
        System.arraycopy(pol.powers, kol, power, 0, pol.powers.length - kol);
        Polynom second = (new Polynom(power, coeff)).truncate();

        return Cos(first, second, ring);
    }

    public F Cos(Polynom first, Polynom second, Ring ring) {
        F cos = new F(F.COS, second);
        F sin = new F(F.SIN, second);
        F cos1 = new F(F.COS, second.negate(ring));
        F sin1 = new F(F.SIN, second.negate(ring));

        if (second.coeffs.length == 1) {
            Element p1, p2;
            if (first.coeffs[0].isNegative() & second.coeffs[0].isNegative()) {
                p1 = new F(F.MULTIPLY, new F(F.COS, first.negate(ring)), cos1);
                p2 = new F(F.MULTIPLY, new F(F.SIN, first.negate(ring)), sin1);
            } else if (first.coeffs[0].isNegative()) {
                p1 = new F(F.MULTIPLY, new F(F.COS, first.negate(ring)), cos);
                p2 = new F(F.MULTIPLY, new Element[] {ring.numberMINUS_ONE, new F(F.SIN, first.negate(ring)), sin});
            } else if (second.coeffs[0].isNegative()) {
                p1 = new F(F.MULTIPLY, new F(F.COS, first), cos1);
                p2 = new F(F.MULTIPLY, new Element[] {ring.numberMINUS_ONE, new F(F.SIN, first), sin1});
            } else {
                p1 = new F(F.MULTIPLY, new F(F.COS, first), cos);
                p2 = new F(F.MULTIPLY, new F(F.SIN, first), sin);
            }
            F Result = new F(F.SUBTRACT, p1, p2);
            return Result;
        }
        if (first.coeffs[0].isNegative()) {
            F pos1 = new F(F.MULTIPLY, new F(F.COS, first.negate(ring)), Cos(second, ring));
            F pos2 = new F(F.MULTIPLY, new Element[] {ring.numberMINUS_ONE, new F(F.SIN, first.negate(ring)), Sin(second, ring)});
            return new F(F.SUBTRACT, pos1, pos2);
        }
        F pos1 = new F(F.MULTIPLY, new F(F.COS, first), Cos(second, ring));
        F pos2 = new F(F.MULTIPLY, new F(F.SIN, first), Sin(second, ring));
        return new F(F.SUBTRACT, pos1, pos2);
    }

    /**
     * представляем тангенс как Sin/Cos и раскладываем Sin и Cos по фомулам выше
     *
     * @param pol
     *
     * @return
     */
    public F Tg(Polynom pol, Ring ring) {
        F r = new F(F.DIVIDE, new Element[] {Sin(pol, ring), Cos(pol, ring)});
        return r;
    }

    /**
     * представляем катангенс как Cos/Sin и раскладываем Sin и Cos по фомулам
     * выше
     *
     * @param pol
     *
     * @return
     */
    public F Ctg(Polynom pol, Ring ring) {
        return new F(F.DIVIDE, new Element[] {Cos(pol, ring), Sin(pol, ring)});
    }

    //----------------------F A C T O R I Z A T I O N---------------------------
    /**
     * Данный блок упрощает композиции функций по следующим формулам:
     * sin(x)cos(y) + cos(x)sin(y) = sin(x+y)   //1
     * sin(x)cos(y) - cos(x)sin(y) = sin(x-y)   //2
     * cos(x)cos(y) + sin(x)sin(y) = cos(x-y)   //3
     * cos(x)cos(y) - sin(x)sin(y) = cos(x+y)   //4
     * (sin(x))^2 + (cos(x))^2 = 1              //5
     * (sin(x))^2 - (cos(x))^2 = cos(2x)        //6
     * 2sin(x)cos(x) = sin(2x)                  //7
     * e^iz + e^-iz = 2Cos(z)                   //8
     * e^iz - e^-iz = 2iSin(z).                   //9
     * Ln(b+izb) - Ln(b-izb) = 2i*arctg(z);           //10
     * Ln(b-izb) - Ln(b+izb) = 2i*arcctg(z);          //11
     * Re(f1)=Re(f2);Im(f1)=-Im(f2);z=\abs(Im(f1)/Re(f)) --- arctg--
     * e^iz + e^-iz = 2Cos(z);                      //12
     * e^iz - e^-iz = 2iSin(z).                     //13
     * tg(x)=sin(x)/cos(x)
     * ctg(x)=cos(x)/sin(x)
     * Такие фрагменты выбираются на верхнем уровне и
     * заменяются.
     * @param f - input function
     * @param ring - ring
     * @return  возвращается упрощенная функция в виде: // pol OR Fractin-of-pol OR FactorPol
     *           или "null", если не удалось упростить,
     */
    public Element FactorAll(FactorPol factPl,  Ring ring) {
        CanonicForms g=ring.CForm;
        // Посчитаем, сколько множителей, содержащих экспоненту,
        // отностися к знаменателю, а сколько - к числителю.
        int numberOfMultinsInNum = 0;
        int numberOfMultinsInDenom = 0;
        int indexOfNum = -1;
        int indexOfDenom = -1;
        for (int i = 0; i < factPl.multin.length; i++) {
            if(factPl.multin[i].isItNumber()) {
                continue;
            }
            
            int[] pos= g.getPosInNewRingTheseFuncs(new int[] {F.EXP});
            int[] mons=factPl.multin[i].monomsWithVars(pos);
            if(mons.length > 0) {
                if(factPl.powers[i] < 0) {
                    numberOfMultinsInDenom ++;
                    indexOfDenom = i;
                }
                if(factPl.powers[i] >= 0) {
                    numberOfMultinsInNum ++;
                    indexOfNum = i;
                }
            }
        }
        
        Element full = null;
        
        if( (numberOfMultinsInDenom > 0) && (numberOfMultinsInNum > 0) ) {
            
            
            if ( (numberOfMultinsInDenom == 1) && (factPl.multin[indexOfDenom].coeffs.length == 1) ) {
                // Рассматриваемая функция является дробью, знаменатель которой
                // состоит ровно из одного монома, содержащего экспоненту.
                // Нпример, если рассматриваемая функция имеет вид:
                // A(x)*(1 + exp(2ig(x)))/(2x^2exp(ig(x)))
                // то чтобы ее свернуть нужно числитель и знаменатель
                // домножить на exp(-ig(x)). В этом случае функция будет иметь вид:
                // A(x)*(exp(-ig(x)) + exp(ig(x)))/(2x^2)
                // Полученное выражение легко сворачивается к виду:
                // A(x)*cos(g(x))/x^2
                
                
                // Пытаемся свернуть числитель.
                Polynom ep = factorSumExpFuncs(factPl.multin[indexOfNum], ring);
                
                if (ep.powers.length != factPl.multin[indexOfNum].powers.length) {
                    factPl.multin[indexOfNum] = ep;
                } else {
                    // Если числитель свернуть не удалось, домножаем числитель
                    // и знаменатель на экспоненту с аргументом противоположного знака.
                    // После чего попытаемся свернуть новый числитель.
                    Element num = ring.CForm.ElementToF(factPl.multin[indexOfNum]);
                    Element polDenom = ring.CForm.ElementToF(factPl.multin[indexOfDenom]);
                    
                    
                    if (polDenom instanceof F) {
                        F denom = (F) polDenom;
                        if ( denom.name == F.EXP ) {
                            Element el = new F(F.EXP, new Element[]{denom.X[0].multiply(ring.numberMINUS_ONE, ring)});
                            num = new F(F.MULTIPLY, new Element[]{num, el}).simplify(ring).expand(ring);
                            denom = new F(F.ID, ring.numberONE);
                        } else if (denom.name == F.MULTIPLY) {
                            Element el = null;
                
                            for (int i = 0; i < denom.X.length; i++) {
                                if (denom.X[i] instanceof F && ((F)denom.X[i]).name == F.EXP) {
                                    el = new F(F.EXP, new Element[]{((F)denom.X[i]).X[0].multiply(ring.numberMINUS_ONE, ring)});
                                    denom.X[i] = ring.numberONE;
                                    num = new F(F.MULTIPLY, new Element[]{num, el}).simplify(ring).expand(ring);
                                }
                            }
                        }
                        
                        Element polNum = ring.CForm.ElementConvertToPolynom(num);
                        if (polNum instanceof Polynom) {
                            factPl.multin[indexOfNum] = factorSumExpFuncs((Polynom) polNum, ring);
                        } else if (polNum.numbElementType() < Ring.Polynom) {
                            factPl.multin[indexOfNum] = new Polynom(new int[]{}, new Element[]{polNum});
                        }
                        
                        polDenom = ring.CForm.ElementConvertToPolynom(denom);
                        if (polDenom instanceof Polynom) {
                            factPl.multin[indexOfDenom] = factorSumExpFuncs((Polynom) polDenom, ring);
                        } else if (polDenom.numbElementType() < Ring.Polynom) {
                            factPl.multin[indexOfDenom] = new Polynom(new int[]{}, new Element[]{polDenom});
                        }
                        
                        full= factPl.toPolynomOrFraction(ring);
                    }
                }
            } else {
                // В переменной oldDenom будем сохранять знаменатель в том виде, 
                // в котором он был перед сворачиванием.
                Polynom oldDenom = factPl.multin[indexOfDenom];
                // Попытка свернуть знаменатель
                Polynom ep = factorSumExpFuncs(factPl.multin[indexOfDenom], ring);
                // если знаменатель не свернулся, то возможно, что он имеет вид 
                // a*exp(A) + a
                // где a - константа, A - функция.
                // Поэтому проверяем, содержит ли знаменатель
                // сумму или разность экспоненты и числа
                if(ep.powers.length == factPl.multin[indexOfDenom].powers.length) {
                    int[] pos= g.getPosInNewRingTheseFuncs(new int[] {F.EXP});
                    int[] mons=ep.monomsWithVars(pos);
                    if(mons.length == 1) {
                        Polynom[] pols = ring.CForm.dividePolynomToMonoms(ep);
                        for(int i=0; i<pols.length; i++) {
                            if(pols[i].isItNumber() &&
                              (pols[i].coeffs[0].abs(ring).subtract(pols[mons[0]].coeffs[0].abs(ring), ring).isZero(ring)) ) {
                                Element el = ring.CForm.ElementToF(pols[mons[0]]);
                                if( (el instanceof F) && (((F)el).name == F.EXP) ) {
                                    // Проверка показала, что знаменатель имеет вид:
                                    // a*exp(A) + a
                                    // умножаем на exp(-A/2) и числитель и знаменатель.
                                    // в знаменателе должно получиться выражение вида:
                                    // a*exp(A/2) + a*exp(-A/2)
                                    // После умножения снова пытаемся свернуть знаменатель.
                                    el = new F(F.EXP, ((F)el).X[0].divide(new NumberZ("-2"), ring));
                                    Element p = ring.CForm.ElementToF(ep).multiply(el, ring).expand(ring).simplify(ring);
                                            
                                    oldDenom = (Polynom)ring.CForm.ElementConvertToPolynom(p);
                                    factPl.multin[indexOfDenom] = factorSumExpFuncs(oldDenom, ring);
                                    
                                    p = ring.CForm.ElementToF(factPl.multin[indexOfNum]).multiply(el, ring).expand(ring).simplify(ring);
                                    factPl.multin[indexOfNum] = (Polynom)ring.CForm.ElementConvertToPolynom(p);
                                    break;
                                }
                            }
                        }
                    }
                }
                // Пытаемся свернуть числитель.
                ep = factorSumExpFuncs(factPl.multin[indexOfNum], ring);
                // Если числитель не свернулся, то пытаемся найти моном,
                // который содержится и в числителе, и в знаменателе
                // (причем знаменатель берем до сворачивания).
                int[] pos = g.getPosInNewRingTheseFuncs(new int[] {F.EXP});
                int[] monsNum = ep.monomsWithVars(pos);
                if(monsNum.length > 0) {
                    Polynom denom = (Polynom)oldDenom.pow(new NumberZ(factPl.powers[indexOfDenom]).negate(ring), ring);
                    int[] monsDenom = denom.monomsWithVars(pos);
                    
                    Polynom[] polsNum = ring.CForm.dividePolynomToMonoms(ep);
                    Polynom[] polsDenom = ring.CForm.dividePolynomToMonoms(denom);
                    Element coeff = ring.numberZERO;
                    for (int i = 0; i < polsNum.length; i++) {
                        for (int j = 0; j < polsDenom.length; j++) {
                            if( !polsNum[i].isItNumber()&& !polsDenom[j].isItNumber()&&
                                    Arrays.equals(polsNum[i].powers, polsDenom[j].powers)) {
                                Element a = polsNum[i].coeffs[0];
                                Element b = polsDenom[j].coeffs[0];
                                polsNum[i].coeffs[0] = ring.numberONE;
                                polsDenom[j].coeffs[0] = ring.numberONE;
                                Element elNum = ring.CForm.ElementToF(polsNum[i]);
                                Element elDenom = ring.CForm.ElementToF(polsDenom[j]);
                                if( (elNum instanceof F)&&(elDenom instanceof F)&&
                                  (((F)elNum).name == F.EXP)&&(((F)elDenom).name == F.EXP) ) {
                                    // Нашли экспоненту, которая встречается и в числителе,
                                    // и в знаменателе. Обозначим ее exp(A/2)
                                    // Прибавляем к нашей дроби константу:
                                    // a*exp(A/2)/( b*exp(A/2) + b*exp(-A/2) ) + c =
                                    // ( (a + cb)*exp(A/2) + cb*exp(-A/2) )/( b*exp(A/2) + b*exp(-A/2) )
                                    // Константу c мы подбираем так, чтобы abs(a + cb) = abs(cb)
                                    
                                    Element sign = (a.divide(b, ring).isNegative())?
                                        ring.numberONE : ring.numberMINUS_ONE;
                                    coeff = a.divide(
                                            b.multiply(new NumberZ("2"), ring),
                                            ring).multiply(sign, ring);
                                    ep = (Polynom) ep.add(denom.multiply(coeff, ring), ring);
                                    break;
                                }
                            }
                        }
                    }
                    // Пробуем свернуть преобразованный числитель.
                    factPl.multin[indexOfNum] = factorSumExpFuncs(ep, ring);
                    
                    factPl = (FactorPol) toTg(factPl, ring);
                    
                    
                    // Так как мы прибавляли константу к нашей дроби,
                    // чтобы функция не изменилась, мы должны ее вычесть.
                    // Но в этот раз мы будем вычитать ее из всего выражения,
                    // поэтому нужно умножить ее на все числовые множители,
                    // вынесенные за скобки (т. е. числа, на которые умножается вся наша дробь).
                    for (int i = 0; i < factPl.multin.length; i++) {
                        if(factPl.multin[i].isItNumber()) {
                            coeff = coeff.multiply(factPl.multin[i]
                                    .pow(factPl.powers[i], ring), ring);
                        }
                    }
                    full = factPl.toPolynomOrFraction(ring);
                    full = full.subtract(coeff, ring);
                    
                } else {
                    factPl.multin[indexOfNum] = ep;
                    full = factPl.toPolynomOrFraction(ring);
                }
            }
        } else {
            for (int i = 0; i < factPl.multin.length; i++) {
                Polynom ep=factPl.multin[i];
                if(ep.coeffs.length==0)return ring.numberZERO;
                int epVar=ep.powers.length / ep.coeffs.length;
                if (epVar<g.RING.varNames.length) continue;
                factPl.multin[i] = factorSumExpFuncs(ep, ring);
            }
            factPl = (FactorPol) toTg(factPl, ring);
            full= factPl.toPolynomOrFraction(ring);
        }
        
          //упрощение тригонометрии в произведении всех сомножителей
          // В переменной full содержится произведение всех сомножителей.
          Polynom num,denom; 
          if (full instanceof Fraction) {
              if( ((Fraction) full).num instanceof Polynom) {
                  num = (Polynom) ((Fraction) full).num;
                  Element el = factorTrig( num, g);
                  if( (el != null) && (el.subtract(num, ring).isZero(ring) == false) ) {
                         ((Fraction) full).num = el;
                  }
              }
              if( ((Fraction)full).denom instanceof Polynom) {
                  denom = (Polynom) ((Fraction) full).denom;
                  Element el = factorTrig(denom, g);
                  if( (el != null) && (el.subtract(denom, ring).isZero(ring) == false) ) {
                         ((Fraction) full).num = el;
                  }
              }
          } else if(full instanceof Polynom) {
              Element el = factorTrig((Polynom) full, g);
              if(el != null) {
                  full = el;
              }
          }
          
          return full;
    }    
    
        public Polynom factorSumExpFuncs(Polynom ep, Ring ring) {
       
        // если сумма экспонент не преобразуется ни к функции sin,
            // ни к функции cos, то умножение на (1+i) позволяет преобразовать
            // такое выражение к сумме sin и cos. Например:
            // (1+i)*(e^iz + ie^-iz) = e^iz + ie^-iz + i*e^iz - e^-iz = 
            // = 2*i*sin(x) + 2*i*cos(x)
            Complex c = new Complex(ring.numberONE, ring.numberONE);
            ep = (Polynom)ep.multiply(c, ring);
            Polynom[] RI=new Polynom[]{ep.Re(ring),ep.Im(ring)};
            for (int j = 0; j < RI.length; j++) {ep=RI[j];
 
            
            Polynom exp_simple = (ep.isItNumber())? ep: factorExp(ep,ring.CForm); //упрощение экспнент
            if(exp_simple==null) exp_simple=ep;
            if(exp_simple.isItNumber()) RI[j]=exp_simple; 
            else{
                Polynom trig_simple = factorTrig((Polynom) exp_simple, ring.CForm);//упрощение тригонометрии
                if(trig_simple==null)trig_simple=exp_simple ;
               // factPl.multin[i]=trig_simple;
               RI[j]=trig_simple;}
        }  
            return (Polynom) (RI[1].multiply(ring.numberI, ring)).add(RI[0], ring).divide(c, ring);
    }
        
        
        // Процедура, выполняющая преобразования:
        //
        // sin(x)/cos(x)  --->   tg(x)
        // cos(x)/sin(x)  --->   ctg(x)
        // 1/tg(x)   --->  ctg(x)
        // 1/ctg(x)  --->  tg(x)
        public Element toTg(Element expression, Ring ring) {
            if(expression instanceof FactorPol) {
                for (int i = 0; i < ((FactorPol)expression).multin.length; i++) {
                    // Ищем множители, относящиеся к знаменателю и состоящие из одного монома.
                    if( ( ((FactorPol)expression).powers[i] < 0) &&
                        ( ((FactorPol)expression).multin[i].coeffs.length == 1) ) {
                        Polynom p = ((FactorPol)expression).multin[i];
                        // Проверяем, содержится ли в этом множителе
                        // хотя-бы одна из функций: sin, cos, tg, ctg.
                        for (int j = 0; j < ring.CForm.List_of_Polynom.size(); j++) {
                            if( (p.powers.length > j + ring.varPolynom.length) && (p.powers[j + ring.varPolynom.length] != 0) && (ring.CForm.List_of_Change.get(j) instanceof F) ) {
                                F func = (F) ring.CForm.List_of_Change.get(j);
                                if( (func.name == F.SIN) || (func.name == F.COS) ) {
                                    // Если в нашем множителе содержится либо sin,
                                    // либо cos, то пытаемся найти множитель, относящийся к числителю
                                    // и состоящий из одного монома.
                                    for (int k = 0; k < ((FactorPol)expression).multin.length; k++) {
                                        if( ( ((FactorPol)expression).powers[k] > 0) &&
                                            ( ((FactorPol)expression).multin[k].coeffs.length == 1) ) {
                                            Polynom p2 = ((FactorPol)expression).multin[k];
                                            // Если нашли такой моном, то смотрим, содержится ли
                                            // в нем sin, или cos.
                                            for (int l = 0; l < ring.CForm.List_of_Polynom.size(); l++) {
                                                if( (p2.powers.length > l + ring.varPolynom.length) && (p2.powers[l + ring.varPolynom.length] != 0) && (ring.CForm.List_of_Change.get(l) instanceof F) ) {
                                                    F func2 = (F) ring.CForm.List_of_Change.get(l);
                                                    if( ( (func2.name == F.SIN) || (func2.name == F.COS) ) && func.X[0].subtract(func2.X[0], ring).isZero(ring) ) {
                                                        if(func.name == func2.name) {
                                                            continue;
                                                        }
                                                        // Делим числитель на наш найденный sin или cos.
                                                        Polynom p3 = (Polynom) ring.CForm.List_of_Polynom.get(j);
                                                        ((FactorPol)expression).multin[i] = (Polynom) ((FactorPol)expression).multin[i].divide(p3, ring);
                                                        
                                                        // Делим знаменатель на наш найденный sin или cos.
                                                        p3 = (Polynom) ring.CForm.List_of_Polynom.get(l);
                                                        ((FactorPol)expression).multin[k] = (Polynom) ((FactorPol)expression).multin[k].divide(p3, ring);
                                                        
                                                        
                                                        // Если мы находили в знаменателе cos, а в числителе sin,
                                                        // то домножаем числитель на tg. Если все с точностью наоборот,
                                                        // то домножаем числитель на ctg.
                                                        Element func3 = (func.name == F.COS)? new F(F.TG, func.X[0]) : new F(F.CTG, func.X[0]);
                                                        p3 = (Polynom) ring.CForm.addNewElement(func3, 1);
                                                        ((FactorPol)expression).multin[k] = ((FactorPol)expression).multin[k].multiply(p3, ring);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else if( (func.name == F.TG) || (func.name == F.CTG) ) {
                                    // Если в нашем множителе содержится tg, то делим этот множитель
                                    // на tg, и домножаем все выражение expression на ctg.
                                    // Если в нашем множителе содержится ctg, то действуем аналогично.
                                    Polynom p2 = (Polynom) ring.CForm.List_of_Polynom.get(j);
                                    ((FactorPol)expression).multin[i] = (Polynom) ((FactorPol)expression).multin[i].divide(p2, ring);
                                    Element func2 = (func.name == F.TG)? new F(F.CTG, func.X[0]) : new F(F.TG, func.X[0]);
                                    p2 = (Polynom) ring.CForm.addNewElement(func2, 1);
                                    FactorPol newMultin = new FactorPol(p2, ring);
                                    expression = ((FactorPol)expression).multiply(newMultin, ring);
                                }
                            }
                        }
                    }
                }
            }

            
            return expression;
        }
 
    
     /**
     * процедура упрощения полинома. Находит элементы в многочлене к которым
     * применимы тождества: e^iz + e^-iz = 2Cos(z), e^iz - e^-iz = 2iSin(z) и
     * сворачивает их по соответствующей формуле Если в результате получится
     * новая функция(которой нет в уже существующем списке замен), то она
     * добавляется в список, ей присваивается новая переменная и добавляется в
     * кольцо.
     *
     * @param first_pol - polinom
     * @param g - CanonicForms
     *
     * @return 
     */
    public Polynom factorExp(Polynom firstPol,   CanonicForms g ) { //SINCOS
    boolean flag; Polynom addP= Polynom.polynomZero;
    do {
     int[] logPos= g.getPosInNewRingTheseFuncs(new int[] {F.EXP});
     int[] mons=firstPol.monomsWithVars(logPos);
     int lN=mons.length;
     if (lN<2)return firstPol;    
        int kol = firstPol.powers.length / firstPol.coeffs.length;
        int[] pow1 = new int[kol]; // for one monomial
        int[] pow2 = new int[kol];// for other monomial
        flag = false;
        for (int i = 0; i < lN-1; i++) { int nMon=mons[i]; if(nMon==-1)continue;          
                   System.arraycopy(firstPol.powers, nMon * kol, pow1, 0, kol); 
        //берем первый моном нашего полинома
                Polynom first = new Polynom(pow1, new Element[] {firstPol.coeffs[nMon]});
                Polynom firstI=null;
                if (firstPol.coeffs[nMon] instanceof Complex) {Complex Fcoeff=(Complex)firstPol.coeffs[nMon];
                    firstI= new Polynom(pow1, new Element[] {Fcoeff.im});
                    first = (Fcoeff.re.isZero(g.RING))? Polynom.polynomZero: new Polynom(pow1, new Element[] {Fcoeff.re});
                }      
                for (int j = i + 1; j < lN; j++) { int nMon2=mons[j]; if(nMon2==-1)continue;
                    System.arraycopy(firstPol.powers, nMon2 * kol, pow2, 0, kol);
                    //Берем следующий моном, чтобы сравнить с первым
                    Polynom second = new Polynom(pow2, new Element[] {firstPol.coeffs[nMon2]});
                    Polynom secondI=null;
                    if (firstPol.coeffs[nMon2] instanceof Complex) {Complex Scoeff=(Complex)firstPol.coeffs[nMon2];
                        secondI= new Polynom(pow2, new Element[] {Scoeff.im});
                        second =(Scoeff.re.isZero(g.RING))? Polynom.polynomZero: new Polynom(pow2, new Element[] {Scoeff.re});
                    }          
                    Polynom s = first.add(second, g.newRing); // сохраним нетронутый
                    Polynom  sum = first.add( second, g.newRing); // тоже, но с внесенными степенями
                    Polynom poly1 = expToSCShCh(sum , g, g.newRing, g.RING);
                    if (!((poly1 == null)|| (poly1.equals(firstPol, g.newRing)))) 
                       // { firstPol = (firstPol.subtract(s, g.newRing)).add(sum, g.newRing);}
                     {addP = (addP.subtract(s, g.newRing)). add(poly1, g.newRing);
                      mons[j]=-1;   
                    }   
                    if (!((secondI==null)|| (firstI==null))){
                      s = firstI.add(secondI, g.newRing); // сохраним нетронутый
                      sum = firstI.add( secondI, g.newRing); // тоже, но с внесенными степенями
                      poly1 = expToSCShCh(sum , g, g.newRing, g.RING);
                      if (!((poly1 == null)|| (poly1.equals(firstPol, g.newRing)))) // УДАЛИТЬ ВТОРУЮ ЧАСТЬ
                        // { firstPol = (firstPol.subtract(s, g.newRing)).add(sum, g.newRing);}
                        {addP = addP.subtract(s.subtract(poly1, g.newRing).multiplyByNumber(g.newRing.numberI, g.newRing), g.newRing);
                      mons[j]=-1;}   
                    }  
                    if (mons[j]==-1){flag = true;   break;}
                }
            }
        firstPol=firstPol.add(addP, g.newRing);
        addP = Polynom.polynomZero;
        } while (flag);
        return firstPol;
    }
    
    
    /**
     * проверяем, стоит ли рассматривать данные 2 монома на применение к ним
     * формул (правда ли что они кодируют одну и ту же функцию?)
     *
     * @param first - первый моном
     * @param second - второй моном
     * @param g - CanonicForms
     *
     * @return
     */
    private boolean first_Check(Polynom first, Polynom second, CanonicForms g, int func) {
        int kol = first.powers.length / first.coeffs.length;
        int[] c = new int[kol];
        int num = 0;
        for (int i = 0; i < kol; i++) {
            c[i] = first.powers[i] - second.powers[i];
            if (c[i] != 0) {
                num++;
                int pp=i - g.RING.varNames.length;
                if (pp<0)return false; //  есть полиномиальный мноитель у одной и нет его у другой
                if (((F) g.List_of_Change.get(pp)).name == func) {
                    continue;
                } else {return false;}
            }
        }
        if (num == 2) {return true; }
        return false;
    }
    /**
     * Применяем к полиному, состоящему из 2-х мономов формулы:
     * e^iz + e^-iz = 2Cos(z);
     * e^iz - e^-iz = 2iSin(z)
     * e^ z + e^- z = 2CH(z);
     * e^ z - e^- z = 2SH(z)
     * @param sum - polynomial for changes
     * @param g - canonicForms
     *
     * @return polynomial with changes or null in another case
     */
    private Polynom expToSCShCh(Polynom sum, CanonicForms g, Ring ring, Ring gring) { 
      if(sum.coeffs.length<2) return null;
        int var = sum.powers.length / sum.coeffs.length; 
        if (!sum.isHomogeneous() ) return null;
        // Теперь sum - это однородный полином
        Polynom third = null;
        Element Abs0Sub1=sum.coeffs[0].abs(ring).subtract(sum.coeffs[1].abs(ring), ring);
        int GorL= Abs0Sub1.signum();     
        int[] pow = new int[var];
        if (GorL > 0) {System.arraycopy(sum.powers, 0, pow, 0, var);
            Element newVal = (sum.coeffs[0].isNegative()) ? sum.coeffs[0].add(Abs0Sub1 , ring) : sum.coeffs[0].subtract(Abs0Sub1, ring);
            third =  new Polynom(pow, new Element[] {sum.coeffs[0].subtract(newVal, ring) });
            sum.coeffs[0] = newVal;
        }
        else if (GorL< 0) { System.arraycopy(sum.powers, var, pow, 0, var);
            Element newVal = sum.coeffs[1].isNegative() ? sum.coeffs[1].subtract(Abs0Sub1 , ring) : sum.coeffs[1].add(Abs0Sub1, ring);
            third =  new Polynom(pow, new Element[]{sum.coeffs[1].subtract(newVal, ring) });
            sum.coeffs[1]= newVal;
        }
        FactorPol fpol = sum.factorizationOfTwoMonoms(ring);
        Element Pow1 = null; //показатель степени при 0
        Element Pow2 = null;  //показатель степени при 1
        Polynom new_f = null;
        int ringBaseN=g.RING.varNames.length;
        boolean from1subtr2=true;
        for (int i = 0; i < fpol.multin.length; i++) {
            if (fpol.multin[i].coeffs.length == 1) {continue;}
            if (!fpol.multin[i].isHomogeneous() )  {continue;}
            for (int j = ringBaseN; j < var; j++) {
                if (fpol.multin[i].powers[j] != 0) {
                    F Exp1 = (F) g.List_of_Change.get(j - ringBaseN);
                    Pow1 = Exp1.X[0];
                     if((Pow1 instanceof F)&& (((F)Pow1).name==F.EXP))
                       if(Exp1.name==F.MULTIPLY){ 
                        Pow1=((F)Pow1).X[0];
                        for (int k = 1; k < Exp1.X.length; k++) {
                           if ((Exp1.X[k] instanceof F)&& (((F)(Exp1.X[k])).name==F.EXP))Pow1=Pow1.add(((F)(Exp1.X[k])).X[0],ring);
                           else return null;                        
                        }
                      }else return null;                                
                    break;}
            }
            for (int j = var+ringBaseN; j < 2 * var; j++) {
                if (fpol.multin[i].powers[j] != 0) {
                    F Exp2 = (F) g.List_of_Change.get(j - ringBaseN - var);
                    Pow2 = Exp2.X[0];
                    if((Pow2 instanceof F)&& (((F)Pow2).name==F.EXP))
                      if(Exp2.name==F.MULTIPLY){ 
                        Pow2=((F)Pow2).X[0];
                        for (int k = 1; k < Exp2.X.length; k++) {
                           if ((Exp2.X[k] instanceof F)&& (((F)(Exp2.X[k])).name==F.EXP))Pow2=Pow2.add(((F)(Exp2.X[k])).X[0],ring);
                           else return null;                        
                        }
                      }else return null;  
                    break;}
            }
            Element arg =  (Pow1.isNegative()) ? Pow2 : Pow1; 
                Element Pow1Re= Pow1.Re(gring);
                Element Pow1Im= Pow1.Im(gring);
                Element Pow2Re= Pow2.Re(gring);
                Element Pow2Im= Pow2.Im(gring); 
                if (Pow1Im.isZero(ring) && Pow2Im.isZero(ring)) {  
                    if (Pow1Re.add(Pow2Re, ring).isZero(ring)) {
                        if (fpol.multin[i].coeffs[0].compareTo(fpol.multin[i].coeffs[1], ring) == 0) {
                            new_f = (Polynom) g.addNewElement(new F(F.CH, arg), 1);
                        } else {new_f = (Polynom) g.addNewElement(new F(F.SH, arg), 1);}
                        fpol.multin[i] = new_f.multiplyByNumber( (Pow1.isNegative())?
                            sum.coeffs[1].add(sum.coeffs[1], ring): 
                            sum.coeffs[0].add(sum.coeffs[0], ring)  , ring);  
                    }
                } else{ Element Re1Sub2=Pow1Re.subtract(Pow2Re, ring); 
                if (Re1Sub2.simplify(gring).isZero(gring)) {
                    //если действительная часть обоих = 0 ---------------------------------------------
                if (Pow1Im.add(Pow2Im, ring).expand(gring).isItNumber(gring)) { 
                  Element newArg=Pow1Im.subtract(Pow2Im, ring).divide(NumberZ.TWO, ring);  
                  if(newArg.isNegative()){
                     from1subtr2=false; newArg=newArg.negate(ring);}
                  Element dopM=newArg.subtract((from1subtr2)? Pow1Im: Pow2Im, ring).simplify(gring);  
                  Element c0=fpol.multin[i].coeffs[0].toNewRing(ring);
                  Element c1=fpol.multin[i].coeffs[1].toNewRing(ring);
                  if (c0.abs(ring).compareTo(c1.abs(ring), ring) == 0){
                      boolean isItCOS = c0.compareTo(c1, ring) == 0;
                      if (isItCOS) { new_f = (Polynom) g.addNewElement(new F(F.COS, newArg), 1);
                        } else  new_f = (Polynom) g.addNewElement(new F(F.SIN, newArg), 1);
                       fpol.multin[i] = new_f.multiplyByNumber( (from1subtr2)?
                            sum.coeffs[0].add(sum.coeffs[0], ring): 
                            sum.coeffs[1].add(sum.coeffs[1], ring), ring);
                      if( ! isItCOS) {
                          fpol.multin[i] = fpol.multin[i].multiplyByNumber(ring.numberI, ring); 
                      }
                       if(!dopM.isZero(ring)){Polynom dop= (Polynom) g.addNewElement(new F(F.EXP,dopM.multiply(ring.numberI, ring)),1);
                            fpol.multin[i]=fpol.multin[i].multiply(dop, ring);      }
                       Re1Sub2=(from1subtr2)? Pow1Re : Pow2Re;
                       if (! Re1Sub2.isZero(ring)){ Polynom dop= (Polynom) g.addNewElement(new F(F.EXP,Re1Sub2),1); 
                             fpol.multin[i]=fpol.multin[i].multiply(dop, ring);  }
                    }// SIN  COS
                 } // im1+im2=const
                }} // re1==0  & re2==0
        } // for-for
        if (new_f == null)   return null;
        Polynom result=((Polynom)(fpol.toPolynomOrFraction(ring)));  
        return (third == null)? result : result.add(third, ring);
    }

 
    /**
     * возвращает количество экспонент в списке замен(List_of_Chenge)
     *
     * @param g - параметр, в котором хранится вся информация о функции после
     * перехода от древовидного представления к полиномиальному виду
     *
     * @return
     */
    private int ExpNumbs(CanonicForms g) {
        int exp_names = 0;
        for (int i = 0; i < g.List_of_Change.size(); i++) {
            if (!(g.List_of_Change.get(i) instanceof F)) {
                continue;
            }
            if (((F) g.List_of_Change.get(i)).name == F.EXP) {
                exp_names += 1;
            }
        }
        return exp_names;
    }

    /**
     * процедура упрощения полинома. Находит элементы в многочлене к которым
     * применимы тригонометрические тождества и сворачивает их по
     * соответствующей формуле. если в результате получится новая
     * функция(которой нет в уже существующем списке замен), то она добавляется
     * в список, ей присваивается новая переменная и добавляется в кольцо.
     *
     * sin(x)cos(y) + cos(x)sin(y) = sin(x+y)   //1
     * sin(x)cos(y) - cos(x)sin(y) = sin(x-y)   //2
     * cos(x)cos(y) + sin(x)sin(y) = cos(x-y)   //3
     * cos(x)cos(y) - sin(x)sin(y) = cos(x+y)   //4
     * 2sin(x)cos(x) = sin(2x)                  //7
     * @param first_pol - polinom
     * @param g -- параметр, в котором хранится вся информация о функции после
     * перехода от древовидного представления к полиномиальному виду
     *
     * @return virtual polynomial with factoring parts
     */
   public Polynom factorTrig(Polynom firstPol,   CanonicForms g ) {
     boolean flag;
     do{
     int[] logPos= g.getPosInNewRingTheseFuncs(new int[] {F.SIN,F.COS});
     int[] mons=firstPol.monomsWithVars(logPos);
     int lN=mons.length;
     if (lN<2)break;    
        int kol = firstPol.powers.length / firstPol.coeffs.length;
        int[] pow1 = new int[kol];  int[] pow2 = new int[kol];
            flag = false; Polynom newPart=Polynom.polynomZero;
            for (int i = 0; i < lN-1; i++) {
                int nMon=mons[i];
                System.arraycopy(firstPol.powers, nMon * kol, pow1, 0, kol);
                Polynom first = new Polynom(pow1, new Element[] {firstPol.coeffs[nMon]});
                for (int j = i + 1; j < lN; j++) { int nMon2=mons[j];
                    System.arraycopy(firstPol.powers, nMon2 * kol, pow2, 0, kol);
                    Polynom second = new Polynom(pow2, new Element[] {firstPol.coeffs[nMon2]});
                    Polynom s = first.add(second, g.newRing); // сохраним нетронутый
                    Polynom  sum = first.add( second, g.newRing); // тоже, но с внесенными степенями
                     Polynom poly1  = toSinCosOfSum(sum, g);
                    if (!((poly1 == null)|| (poly1.equals(sum, g.newRing)))){
                       newPart=newPart.add(poly1, g.newRing).subtract(s, g.newRing);
                       flag = true;  break; }                
                }
            } if (flag) firstPol=firstPol.add( newPart, g.newRing);
        } while (flag);
        if (firstPol.isItNumber()) {return firstPol;}
        int[] logPos= g.getPosInNewRingTheseFuncs(new int[] {F.SIN,F.COS});    
        int[] mons=firstPol.monomsWithVars(logPos);
        Polynom newfirstPol = toSin2Arg(firstPol,mons, g);
        if (newfirstPol != null) {firstPol = factorTrig(newfirstPol, g);}
        return firstPol;
    }   
    
    
    /**
     * применяем к упрощенному полиному формулу
     * 2sin(x)cos(x) = sin(2x)
     *
     * @param new_pol - polinom
     * @param g - CanonicForms
     * @return
     */
    private Polynom toSin2Arg(Polynom new_pol, int[] logPos, CanonicForms g) {
        int test = 0;
        int powSin = 0;
        int powCos = 0;
        int pwr;
        boolean flag;
        if (new_pol.powers.length / new_pol.coeffs.length != g.newRing.varNames.length) {
            new_pol = new_pol.unTruncate(g.newRing.varNames.length - new_pol.powers.length / new_pol.coeffs.length);
        }
        do {
            test++;
            flag = false;
            for(int nP=0;nP<logPos.length;nP++){int coef=logPos[nP]+1; 
          //  for (int coef = 1; coef < new_pol.coeffs.length + 1; coef++) {
                for (int i = (coef - 1) * g.newRing.varNames.length + g.RING.varNames.length; i < coef * g.newRing.varNames.length; i++) {
                    if (new_pol.powers[i] == 0) {
                        continue;
                    }
                    int step = (coef - 1) * g.newRing.varNames.length + g.RING.varNames.length;//step - шаг между коэффициентами
                    if (i == coef * g.newRing.varNames.length - 1) {
                        continue;
                    }
                    //дописать для логарифмов ln(a/b)=2iarctan()
                    Element argSin = null;
                    Element argCos = null;
                    F new_func = null;
                    if (g.List_of_Change.get(i - step) instanceof Fname) {
                        continue;
                    }
                    if ((((F) g.List_of_Change.get(i - step)).name == F.SIN) | (((F) g.List_of_Change.get(i - step)).name == F.COS)) {
                        if (((F) g.List_of_Change.get(i - step)).name == F.SIN) {
                            Element arg = ((F) g.List_of_Change.get(i - step)).X[0];
                            argSin = (arg instanceof Polynom) ? ((Polynom) arg).truncate() : arg;
                            powSin = new_pol.powers[i];
                        } else {
                            Element arg = ((F) g.List_of_Change.get(i - step)).X[0];
                            argCos = (arg instanceof Polynom) ? ((Polynom) arg).truncate() : arg;
                            powCos = new_pol.powers[i];
                        }
                        for (int j = i + 1; j < coef * g.newRing.varNames.length; j++) {
                            if (new_pol.powers[j] == 0) {
                                continue;
                            }
                            if (argSin == null) {
                                if (((F) g.List_of_Change.get(j - step)).name == F.SIN) {
                                    Element arg = (((F) g.List_of_Change.get(j - step)).X[0] instanceof Polynom)
                                            ? ((Polynom) ((F) g.List_of_Change.get(j - step)).X[0]).truncate()
                                            : ((F) g.List_of_Change.get(j - step)).X[0];
                                    if (arg.equals(argCos, g.newRing)) {
                                        new_func = new F(F.SIN, argCos.add(argCos, g.newRing).expand(g.newRing));
                                        powSin = new_pol.powers[j];
                                        pwr = (powCos > powSin) ? powSin : powCos;
                                        int old_index = g.index_in_Ring;
                                        Polynom pol = (Polynom) g.addNewElement(new_func, 1);
                                        if (old_index == g.index_in_Ring) {
                                            for (int k = 0; k < g.List_of_Polynom.size(); k++) {
                                                if (g.List_of_Polynom.get(k).subtract(pol, g.newRing).isZero(g.newRing)) {
                                                    new_pol.powers[k + step] = new_pol.powers[k + step] + pwr;
                                                    new_pol.powers[i] = new_pol.powers[i] - pwr;
                                                    new_pol.powers[j] = new_pol.powers[j] - pwr;
                                                }
                                            }
                                        } else {
                                            new_pol = new_pol.unTruncate(1);
                                            new_pol.powers[coef * g.newRing.varNames.length - 1] = pwr;
                                            new_pol.powers[i + coef - 1] = new_pol.powers[i + coef - 1] - pwr;
                                            new_pol.powers[j + coef - 1] = new_pol.powers[j + coef - 1] - pwr;
                                        }
                                        new_pol.coeffs[coef - 1] = new_pol.coeffs[coef - 1].divide(new NumberZ(2).pow(pwr, g.newRing), g.newRing);//?
                                        flag = true;
                                        break;
                                    }
                                }
                            }
                            if (argCos == null) {
                                if (((F) g.List_of_Change.get(j - step)).name == F.COS) {
                                    Element arg = (((F) g.List_of_Change.get(j - step)).X[0] instanceof Polynom)
                                            ? ((Polynom) ((F) g.List_of_Change.get(j - step)).X[0]).truncate()
                                            : ((F) g.List_of_Change.get(j - step)).X[0];
                                    if (arg.equals(argSin, g.newRing)) {
                                        new_func = new F(F.SIN, argSin.add(argSin, g.newRing).expand(g.newRing));
                                        powCos = new_pol.powers[j];
                                        pwr = (powCos > powSin) ? powSin : powCos;
                                        int old_index = g.index_in_Ring;
                                        Polynom pol = (Polynom) g.addNewElement(new_func, 1);
                                        if (old_index == g.index_in_Ring) {
                                            for (int k = 0; k < g.List_of_Polynom.size(); k++) {
                                                if (g.List_of_Polynom.get(k).subtract(pol, g.newRing).isZero(g.newRing)) {
                                                    new_pol.powers[k + step] = new_pol.powers[k + step] + pwr;
                                                    new_pol.powers[i] = new_pol.powers[i] - pwr;
                                                    new_pol.powers[j] = new_pol.powers[j] - pwr;
                                                }
                                            }
                                        } else {
                                            new_pol = new_pol.unTruncate(1);
                                            new_pol.powers[coef * g.newRing.varNames.length - 1] = pwr;
                                            new_pol.powers[i + coef - 1] = new_pol.powers[i + coef - 1] - pwr;
                                            new_pol.powers[j + coef - 1] = new_pol.powers[j + coef - 1] - pwr;
                                        }
                                        new_pol.coeffs[coef - 1] = new_pol.coeffs[coef - 1].divide(new NumberZ(2).pow(pwr, g.newRing), g.newRing);
                                        flag = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } while (flag);
        if (test == 1) {
            return null;
        }
        return new_pol;
    }

    /**
     * разбитие полинома на 2 части: первая часть содержит функции, которые
     * требуется упростить, вторая - все оставшиеся мономы.
     *
     * @param poly - полином, который требуется разбить
     * @param border - граница разбиения
     *
     * @return
     */
    public Polynom[] highAndLowPartsOfPolynom(Polynom poly, int border) {
       if(poly.coeffs.length==0) return new Polynom[]{new Polynom(new int[0],new Element[0]),new Polynom(new int[0],new Element[0])};
        int kol_vo = poly.powers.length / poly.coeffs.length;
        int[] power_first = new int[border * kol_vo];
        System.arraycopy(poly.powers, 0, power_first, 0, border * kol_vo);
        Element[] coeffs_first = new Element[border];
        System.arraycopy(poly.coeffs, 0, coeffs_first, 0, border);
        int[] power_second = new int[poly.powers.length - border * kol_vo];
        System.arraycopy(poly.powers, border * kol_vo, power_second, 0, poly.powers.length - border * kol_vo);
        Element[] coeffs_second = new Element[poly.coeffs.length - border];
        System.arraycopy(poly.coeffs, border, coeffs_second, 0, poly.coeffs.length - border);
        Polynom first_pol = new Polynom(power_first, coeffs_first);//с экспонентами
        Polynom second_pol = (new Polynom(power_second, coeffs_second)).truncate();//все остальное
        return new Polynom[] {first_pol, second_pol};
    } // поменять местами переменные в кольце (на будущее)

    /**
     * Выполняет преобразование к синусу или косинусу от суммы или
     * разности аргументов. На входе и на выходе полиномиальное
     * представление верхнего уровня композиции функций.
     *
     *
     * @param sum - polynomial of 2 monomials,  
     * @param g - CanonicForms
     *
     * @return polynomial, which is the map of first level
     *                      of compositions of functions, with simplifications
     */
    private Polynom toSinCosOfSum(Polynom sum, CanonicForms g) {
        Polynom third = null;

        if ((sum.coeffs[0].abs(g.newRing)).compareTo(sum.coeffs[1].abs(g.newRing), g.newRing) > 0) {
            Element subtract = (sum.coeffs[0].abs(g.newRing)).subtract(sum.coeffs[1].abs(g.newRing), g.newRing);
            int[] pow = new int[sum.powers.length / sum.coeffs.length];
            System.arraycopy(sum.powers, 0, pow, 0, sum.powers.length / sum.coeffs.length);
            sum.coeffs[0] = (sum.coeffs[0].isNegative()) ? sum.coeffs[0].add(subtract, g.newRing) : sum.coeffs[0].subtract(subtract, g.newRing);
            third = (sum.coeffs[0].isNegative()) ? new Polynom(pow, new Element[] {subtract.negate(g.newRing)})
                    : new Polynom(pow, new Element[] {subtract});
        }
        if ((sum.coeffs[0].abs(g.newRing)).compareTo(sum.coeffs[1].abs(g.newRing), g.newRing) < 0) {
            Element subtract = (sum.coeffs[1].abs(g.newRing)).subtract(sum.coeffs[0].abs(g.newRing), g.newRing);
            int[] pow = new int[sum.powers.length / sum.coeffs.length];
            System.arraycopy(sum.powers, sum.powers.length / sum.coeffs.length, pow, 0, sum.powers.length / sum.coeffs.length);
            sum.coeffs[1] = (sum.coeffs[1].isNegative()) ? sum.coeffs[1].add(subtract, g.newRing) : sum.coeffs[1].subtract(subtract, g.newRing);
            third = (sum.coeffs[1].isNegative()) ? new Polynom(pow, new Element[] {subtract.negate(g.newRing)})
                    : new Polynom(pow, new Element[] {subtract});
        }
        Element gcdCoeffs = sum.coeffs[0].abs(g.newRing);
        sum.coeffs[0] = sum.coeffs[0].divide(gcdCoeffs, g.newRing);
        sum.coeffs[1] = sum.coeffs[1].divide(gcdCoeffs, g.newRing);
        FactorPol fpol = sum.factorizationOfTwoMonoms(g.newRing);

        Element new_trig_f = null;
        m1:
        for (int i = 0; i < fpol.multin.length; i++) {
            if (fpol.multin[i].coeffs.length == 2) {
                int fpol_coeff = fpol.multin[i].powers.length / fpol.multin[i].coeffs.length;
                int step = 0;
                while (step < fpol.multin[i].powers.length) {
                    for (int k = step; k < fpol_coeff + step; k++) {
                        if (fpol.multin[i].powers[k] != 0) {
                            if( (k - step) < g.RING.varNames.length ) {
                                continue m1;
                            }
                            
                            Element el = g.List_of_Change.get(k - step - g.RING.varNames.length );
                            if(! (el instanceof F) && ( (((F)el).name == F.SIN) || (((F)el).name == F.COS) ) ) {
                                continue m1;
                            }
                        }
                    }
                    step += fpol_coeff;
                }

                int l = 0;

                Element one = null;
                Element two = null;
                Element three = null;
                Element four = null;
                int p_1 = 0;
                int p_2 = 0;
                int p_3 = 0;
                int p_4 = 0;

                for (int k = l; k < fpol.multin[i].powers.length; k++) {
                    if (fpol.multin[i].powers[k] != 0) {
                        p_1 = fpol.multin[i].powers[k];
                        one = g.List_of_Change.get(k - g.RING.varNames.length);
                        l = k;
                        break;
                    }
                }

                for (int k = l + 1; k < fpol.multin[i].powers.length; k++) {
                    if (fpol.multin[i].powers[k] != 0) {
                        p_2 = fpol.multin[i].powers[k];
                        two = (k > fpol_coeff) ? g.List_of_Change.get(k - g.RING.varNames.length - fpol_coeff)
                                : g.List_of_Change.get(k - g.RING.varNames.length);
                        l = k;
                        break;
                    }
                }

                for (int k = l + 1; k < fpol.multin[i].powers.length; k++) {
                    if (fpol.multin[i].powers[k] != 0) {
                        p_3 = fpol.multin[i].powers[k];
                        three = (k > fpol_coeff) ? g.List_of_Change.get(k - g.RING.varNames.length - fpol_coeff)
                                : null;
                        if (three == null) {
                            return null;
                        }
                        l = k;
                        break;
                    }
                }

                for (int k = l + 1; k < fpol.multin[i].powers.length; k++) {
                    if (fpol.multin[i].powers[k] != 0) {
                        p_4 = fpol.multin[i].powers[k];
                        four = g.List_of_Change.get(k - g.RING.varNames.length - fpol_coeff);
                        l = k;
                        break;
                    }
                }

                if (p_3 == 0 && p_4 == 0) {
                    if (p_1 == p_2) {
                        if (p_1 == 1) {
                            continue m1;
                        }
                        if (p_1 == 2) {
                            if (((F) one).X[0].equals(((F) two).X[0], g.newRing)) {
                                if (sum.coeffs[0].equals(sum.coeffs[1], g.newRing)) {
                                    new_trig_f = (sum.coeffs[0].isNegative()) ? Polynom.polynomFromNumber(g.newRing.numberMINUS_ONE, g.newRing)
                                            : Polynom.polynomFromNumber(g.newRing.numberONE, g.newRing);
                                } else {
                                    if (((F) one).name == F.COS) {
                                        new_trig_f = (sum.coeffs[1].isNegative()) ? new F(F.COS, ((F) one).X[0].add(((F) two).X[0], g.newRing))
                                                : new F(F.COS, ((F) one).X[0].add(((F) two).X[0], g.newRing)).negate(g.newRing); //cos A+B или -cos A+B
                                    }
                                    if (((F) one).name == F.SIN) {
                                        new_trig_f = (sum.coeffs[1].isNegative()) ? new F(F.COS, ((F) one).X[0].add(((F) two).X[0], g.newRing)).negate(g.newRing)
                                                : new F(F.COS, ((F) one).X[0].add(((F) two).X[0], g.newRing));//cos A+B или -cos A+B
                                    }
                                }
                            }
                        } else {
                            new_trig_f = SimplifyPowers(sum, g);
                        }
                    } else {
                        continue;
                    }
                }
                if (p_3 != 0 & p_4 != 0) {
                    if ((p_1 == p_2) & (p_2 == p_3) & (p_3 == p_4) & (p_1 == 1)) {
                        if ((((F) one).X[0].equals(((F) three).X[0]) & ((F) two).X[0].equals(((F) four).X[0]))
                                || (((F) one).X[0].equals(((F) four).X[0]) & ((F) two).X[0].equals(((F) three).X[0]))) {
                            if (((F) one).name == F.COS) {
                                if (((F) one).name == ((F) two).name) {
                                    new_trig_f = (sum.coeffs[1].isNegative()) ? new F(F.COS, ((F) one).X[0].add(((F) two).X[0], g.newRing))
                                            : new F(F.COS, ((F) one).X[0].subtract(((F) two).X[0], g.newRing)); //cos A+B или cos A-B
                                } else {
                                    new_trig_f = (sum.coeffs[1].isNegative()) ? new F(F.SIN, ((F) two).X[0].subtract(((F) one).X[0], g.newRing))
                                            : new F(F.SIN, ((F) two).X[0].add(((F) one).X[0], g.newRing));// sin A+B или sin A-B
                                }
                            }
                            if (((F) one).name == F.SIN) {
                                if (((F) one).name == ((F) two).name) {
                                    new_trig_f = (sum.coeffs[1].isNegative()) ? new F(F.COS, ((F) one).X[0].add(((F) two).X[0], g.newRing)).negate(g.newRing)
                                            : new F(F.COS, ((F) one).X[0].subtract(((F) two).X[0], g.newRing)); // cos A+B или cos A-B
                                } else {
                                    new_trig_f = (sum.coeffs[1].isNegative()) ? new F(F.SIN, ((F) one).X[0].subtract(((F) two).X[0], g.newRing))
                                            : new F(F.SIN, ((F) one).X[0].add(((F) two).X[0], g.newRing)); // sin A+B или sin A-B
                                }
                            }
                        }
                    }
                }
                if (new_trig_f == null) {return null;}
                if (new_trig_f instanceof F) {
                    fpol.multin[i] = (Polynom) g.addNewElement(new_trig_f, 1);
                } else {
                    fpol.multin[i] = (Polynom) new_trig_f;
                }
            }
        }
        if (new_trig_f == null) { return null;}
        if (third == null) {
            return (Polynom) (fpol.toPolynomOrFraction(g.newRing)).multiply(Polynom.polynomFromNumber(gcdCoeffs, g.newRing), g.newRing);
        } else {
            return (Polynom) (fpol.toPolynomOrFraction(g.newRing)).multiply(Polynom.polynomFromNumber(gcdCoeffs, g.newRing), g.newRing).add(third, g.newRing);
        }
    }

    /**
     * возвращает количество синусов или косинусов в списке
     * замен(List_of_Chenge) и плюс количество умножений чисел на любую функцию....
     *
     * @param g - CanonicForms
     * @return
     */
    private int TrigNumbs(CanonicForms g) {
        int trig_names = 0;
        for (int i = 0; i < g.List_of_Change.size(); i++) {
            if (!(g.List_of_Change.get(i) instanceof F))  continue;
            switch (((F) g.List_of_Change.get(i)).name) {
                case F.SIN:
                case F.COS:
                    trig_names++; break;
                case F.MULTIPLY:
                    F f = ((F) g.List_of_Change.get(i));
                    if (f.X[0].isItNumber() && f.X[1] instanceof F) {
                        trig_names++;  //  ?????????????? !!!!!!!!! зачем?
                    }
                    break;
            }
        }
        return trig_names;
    }

    /**
     * Определяем границу раздела. под границей будем понимать количество
     * мономов, в которых присутствует хотя бы одна из старших переменных
     *
     * @param poly - polynom
     * @param g - параметр, в котором хранится вся информация о функции после
     * перехода от древовидного представления к полиномиальному виду
     * @param numbs -- количество старших переменных, которые нужно отделить
     *
     * @return -- число старших мономов, которые нужно отделить при откалывании
     * от полинома части содержащей мономы со старшими переменными.
     */
    public int Border(Polynom poly, CanonicForms g, int numbs) {
        if(poly.coeffs.length==0)return 0;
        int kol_vo = poly.powers.length / poly.coeffs.length;
        int i = 0;
        int j = g.newRing.varNames.length - numbs;
        m1:
        while (i < poly.coeffs.length) {
            for (int c = j; c < kol_vo; c++) {
                if (poly.powers[c] != 0) {
                    i++;
                    j += poly.powers.length / poly.coeffs.length;
                    kol_vo += poly.powers.length / poly.coeffs.length;
                    continue m1;
                }
            }
            break;
        }
        return i;
    }


    /**
     * метод применяет разность квадратов к (x^2n - x^2n) пока не останется
     * (x^2-y^2)(x^2+y^2)...-- для x,y=sin,cos и подставляет тригонометрические формулы
     *
     * @param p -- polynomial of 2 monomials of sin and cos
     *
     * @return 
     */
    private Polynom SimplifyPowers(Polynom p, CanonicForms g) {
        int vars = p.powers.length / p.coeffs.length;
        int p1 = 0;
        int p2 = 0;
        int i1 = 0;
        int i2 = 0;
        for (int i = 0; i < p.powers.length; i++) {
            if (p.powers[i] != 0) {
                p1 = p.powers[i];
                i1 = i;
                break;
            }
        }
        for (int i = i1 + 1; i < p.powers.length; i++) {
            if (p.powers[i] != 0) {
                p2 = p.powers[i];
                i2 = i;
                break;
            }
        }
        if (p.coeffs[0].equals(p.coeffs[1], g.newRing)) {
            if (p1 % 2 == 0) {
                return null;
            }
            int[] pow1 = new int[2 * vars];
            System.arraycopy(p.powers, 0, pow1, 0, 2 * vars);
            pow1[i1] = 1;
            pow1[i2] = 1;
            Polynom pol1 = new Polynom(pow1, p.coeffs);
            int[] pow2 = new int[p1 * vars];
            Element[] coeff = new Element[p1];
            for (int i = 0; i < p1; i++) {
                coeff[i] = (i % 2 == 0) ? g.newRing.numberONE : g.newRing.numberMINUS_ONE;
            }
            int c = 1;
            i2 -= vars;
            do {
                pow2[vars * (c - 1) + i1] = p1 - c;
                pow2[vars * (c - 1) + i2] = c - 1;
                c++;
            } while (c < p1 + 1);
            Polynom pol2 = new Polynom(pow2, coeff);
            pol2 = factorTrig(pol2, g);
            return (p.coeffs[0].isNegative()) ? pol1.multiply(pol2, g.newRing).negateThis(g.newRing)
                    : pol1.multiply(pol2, g.newRing);
        }
        if (p1 % 4 == 0) {
            int[] pow1 = new int[p.powers.length];
            System.arraycopy(p.powers, 0, pow1, 0, 2 * vars);
            pow1[i1] = 2;
            pow1[i2] = 2;
            Polynom pol1 = new Polynom(pow1, p.coeffs);
            Polynom pol2 = new Polynom(pow1, new Element[] {new NumberZ(1), new NumberZ(1)});
            pol1 = factorTrig(pol1, g);
            pol2 = factorTrig(pol2, g);
            if (p1 == 4) {
                return pol1.multiply(pol2, g.newRing);
            }
            int[] pow3 = new int[vars * p1 / 4];
            Element[] coeff = new Element[p1 / 4];
            for (int i = 0; i < coeff.length; i++) {
                coeff[i] = g.newRing.numberONE;
            }
            int c = 1;
            i2 -= vars;
            do {
                pow3[vars * (c - 1) + i1] = p1 - 4 * c;
                pow3[vars * (c - 1) + i2] = 4 * (c - 1);
                c++;
            } while (c < p1 / 4 + 1);
            Polynom pol3 = new Polynom(pow3, coeff);
            pol3 = factorTrig(pol3, g);
            return pol1.multiply(pol2, g.newRing).multiply(pol3, g.newRing);
        }
        if (p1 % 2 == 0) {
            int[] pow1 = new int[p.powers.length];
            System.arraycopy(p.powers, 0, pow1, 0, 2 * vars);
            pow1[i1] = 2;
            pow1[i2] = 2;
            Polynom pol1 = new Polynom(pow1, p.coeffs);
            pol1 = factorTrig(pol1, g);
            int[] pow2 = new int[vars * p1 / 2];
            Element[] coeff = new Element[p1 / 2];
            for (int i = 0; i < coeff.length; i++) {
                coeff[i] = g.newRing.numberONE;
            }
            int c = 1;
            i2 -= vars;
            do {
                pow2[vars * (c - 1) + i1] = p1 - 2 * c;
                pow2[vars * (c - 1) + i2] = 2 * (c - 1);
                c++;
            } while (c < p1 / 2 + 1);
            Polynom pol2 = new Polynom(pow2, coeff);
            pol2 = factorTrig(pol2, g);
            return pol1.multiply(pol2, g.newRing);
        }
        int[] pow1 = new int[2 * vars];
        System.arraycopy(p.powers, 0, pow1, 0, 2 * vars);
        pow1[i1] = 1;
        pow1[i2] = 1;
        Polynom pol1 = new Polynom(pow1, p.coeffs);
        int[] pow2 = new int[p1 * vars];
        Element[] coeff = new Element[p1];
        for (int i = 0; i < p1; i++) {
            coeff[i] = g.newRing.numberONE;
        }
        int c = 1;
        i2 -= vars;
        do {
            pow2[vars * (c - 1) + i1] = p1 - c;
            pow2[vars * (c - 1) + i2] = c - 1;
            c++;
        } while (c < p1 + 1);
        Polynom pol2 = new Polynom(pow2, coeff);
        pol2 = factorTrig(pol2, g);
        return pol1.multiply(pol2, g.newRing);
    }

    /**
     * проверям стоит ли применять к данным 2-м мономам формулы:
     * Если имеется от 2х до 4х переменных с разными значениями
     * степени переменных, то результат true, иначе false.
     *
     * @param first - polynomial with 1 monomial
     * @param second - polynomial with 1 monomial
     *
     * @return false, if these monomials cannot be transformed to any formula
     */
    public boolean first_Check(Polynom first, Polynom second) {
        int kol = first.powers.length / first.coeffs.length;
        int[] c = new int[kol];
        int num = 0;
        for (int i = 0; i < kol; i++) {
            c[i] = first.powers[i] - second.powers[i];
            if (c[i] != 0) { num++; }
        }
        if ((num > 1) && (num < 5)) { return true;}
        return false;
    }

//    /**
//     * упрощаем полином по формулам:
//     * e^iz + e^-iz = 2Cos(z)
//     * e^iz - e^-iz = 2iSin(z)
//     * и тут же гиперболические синус и косинус
//     * @param p - polynom
//     * @param g - параметр, в котором хранится вся информация о функции после
//     * перехода от древовидного представления к полиномиальному виду
//     *
//     * @return
//     */
//    private Polynom expToSinCos(Polynom p, CanonicForms g) {
//        Polynom poly = getPosInListOfChTheseFuncs(p, g, new int[] {F.EXP });//отсортировали
//        int border = Border(poly, g, ExpNumbs(g));//разграничили
//        Polynom[] polys = highAndLowPartsOfPolynom(poly, border); //получили 2 полинома
//        if (polys[0].isZero(g.newRing)) return p;
//        polys[0] = FactorExponent(polys[0], g);
//        polys[1].truncate();
//        polys[0].ordering(g.newRing);
//        return polys[0].add(polys[1], g.newRing);
//    }
//
//    private Polynom SimplifyTrigonometric(Polynom p, CanonicForms g) {
//        
//        Polynom poly =  null; int[] posListCh = g.getPosInListOfChTheseFuncs(p, new int[] {F.SIN, F.COS});//отсортировали
//        int border = Border(poly, g, TrigNumbs(g));//разграничили
//        Polynom[] polys = highAndLowPartsOfPolynom(poly, border); //получили 2 полинома
//        polys[0] = FactorTrig(polys[0], g);
//        polys[1].truncate();
//        polys[0].ordering(g.newRing);
//        return polys[0].add(polys[1], g.newRing);
//    }

//---------------------Inverse Trigonometric Functions--------------------------
//public F FactorInverseTr(F f, Ring ring){
//   CanonicForms g = new CanonicForms(ring);
//   Element e = g.ElementToPolynom(f,true);
//   if (e instanceof Fraction) return WorkWithFraction(e, g, ring);
//   Polynom p = (Polynom)g.ElementToPolynom(f,true);
//   Polynom poly = getPosInListOfChTheseFuncs(p, g, new int[]{F.LN}); //отсортировали
//   int border = Border(poly, g, LnNumbs(g));//нашли границу
//   Polynom[] polys = highAndLowPartsOfPolynom(poly, border); //получили 2 полинома
//   polys[0] = MakeArctgAndArcctg(polys[0], g);//упрощение полинома по логарифмам
//   polys[0].ordering(g.newRing);
//   polys[1].truncate();
//   Element result = g.Convert_Polynom_to_F(polys[0].add(polys[1], g.newRing));
//   if(result instanceof NumberZ) {return new F(result);}
//     return (F)result;
//}
    /**
     * возвращает количество экспонент в списке замен(List_of_Chenge)
     *
     * @param g
     *
     * @return
     */
//    private int LnNumbs(CanonicForms g){
//        int exp_names = 0;
//         for(int i = 0; i < g.List_of_Change.size(); i++){
//       if((((F)g.List_of_Change.get(i)).name == F.LN))
//       {exp_names+=1;}
//         }return exp_names;
//            }
//
    /**
     * поиск 2-х мономов, к которым применима одна из формул: Ln(1+iz) -
     * Ln(1-iz) = 2i*arctg(z); Ln(1-iz) - Ln(1+iz) = 2i*arcctg(z);
     * i *Ln(1+iz)/(1-iz)=-2 arctg(z); i* Ln(1-iz)/(1+iz)= -2 *arcctg(z);
     * @param p - polynom
     * @param g - параметр, в котором хранится вся информация о функции после
     * перехода от древовидного представления к полиномиальному виду
     *
     * @return
     */
//    private Polynom MakeArctgAndArcctg1(Polynom  p , Ring ring) {CanonicForms g=ring.CForm;
//      Polynom poly=null;  int[] posListCh = g.getPosInListOfChTheseFuncs(p, new int[] {F.LN });//отсортировали
//        int nPos=  posListCh.length;
//        if (nPos<2) return p;
//        Polynom new_pol=null;
//        Polynom first_pol = null; // тут все переменные - являются функциями - а во второй части обычный полином
//        boolean flag;
//        do {
//            flag = false;
//            new_pol = first_pol;
//            m2:
//            for (int i = 0; i < first_pol.coeffs.length; i++) {
//                //если то последний моном
//                if (i == first_pol.coeffs.length - 1) {
//                    return p;
//                }
//                int vars = first_pol.powers.length / first_pol.coeffs.length;
//                int[] pow1 = new int[vars];
//                System.arraycopy(first_pol.powers, i * vars, pow1, 0, vars);
//                //берем первый моном нашего полинома
//                Polynom first = new Polynom(pow1, new Element[] {first_pol.coeffs[i]});
//                for (int j = i + 1; j < first_pol.coeffs.length; j++) {
//                    int[] pow2 = new int[vars];
//                    System.arraycopy(first_pol.powers, j * vars, pow2, 0, vars);
//                    //берем следующий моном, чтобы сранить с первым
//                    Polynom second = new Polynom(pow2, new Element[] {first_pol.coeffs[j]});
////отсееваем ненужное и смотрим на наличие формулы
//                    if (!first_Check(first, second, g, F.LN)) {
//                        continue;
//                    }
//                    Polynom sum = first.add(second, g.newRing);
//                    Polynom poly1 = CheckLn(sum, g);
//                    if (poly1 != null) {
//                        first_pol = (first_pol.subtract(first.add(second, g.newRing), g.newRing)).add(poly1, g.newRing);
//                        flag = true;
//                        break m2;
//                    }
//                }
//            }
//        } while (flag);
//   //    polys[1].truncate();
//        if (!new_pol.isItNumber()) {
//          new_pol = lnArgIsDivide(first_pol, g);
//          new_pol.ordering(g.newRing);}
//        return null; //new_pol.add(polys[1], g.newRing);
//    }
//
    
     /**
     * поиск 2-х мономов, к которым применима одна из формул: Ln(1+iz) -
     * Ln(1-iz) = 2i*arctg(z); Ln(1-iz) - Ln(1+iz) = 2i*arcctg(z); или одного
     * монома
     *  Ln(1+iz)/(1-iz)=-2i arctg(z);  Ln(1-iz)/(1+iz)= -2i *arcctg(z);
     * @param p - polynom
     * @param g - параметр, в котором хранится вся информация о функции после
     * перехода от древовидного представления к полиномиальному виду
     *  a+aif /a +  b-bif/b==2 &  - === своб член=0, Re(rasnosti)=0
     * @return
     */
   public Polynom MakeArctgAndArcctg(Polynom firstPol,   CanonicForms g ) {
     boolean flag;
     do{
     int[] logPos= g.getPosInNewRingTheseFuncs(new int[] {F.SIN,F.COS});
     int[] mons=firstPol.monomsWithVars(logPos);
     int lN=mons.length;  
     if (lN<2)return firstPol;    
        int kol = firstPol.powers.length / firstPol.coeffs.length;
        int[] pow1 = new int[kol];  int[] pow2 = new int[kol];
        flag = false; Polynom newPart=Polynom.polynomZero;
        for (int i = 0; i < lN-1; i++) {
                int nMon=logPos[i];
                System.arraycopy(firstPol.powers, nMon * kol, pow1, 0, kol);
                Polynom first = new Polynom(pow1, new Element[] {firstPol.coeffs[nMon]});
                for (int j = i + 1; j < lN; j++) { int nMon2=logPos[j];
                    System.arraycopy(firstPol.powers, nMon2 * kol, pow2, 0, kol);
                    Polynom second = new Polynom(pow2, new Element[] {firstPol.coeffs[nMon2]});
                    if (!first_Check(first, second, g, F.LN)) {continue;}
                    Polynom s = first.add(second, g.newRing);  
                    Polynom poly1 = CheckLn(s, g); 
                    if (!((poly1 == null)|| (poly1.equals(s, g.newRing)))){
                       newPart=newPart.add(poly1, g.newRing).subtract(s, g.newRing);
                       flag = true;  break; }                
                }
            } if (flag) firstPol=firstPol.add( newPart, g.newRing);
        } while (flag);
        if (firstPol.isItNumber()) {return firstPol;}
        int[] logPos= g.getPosInNewRingTheseFuncs(new int[] {F.SIN,F.COS});
        int[] mons=firstPol.monomsWithVars(logPos);
        Polynom newfirstPol = toSin2Arg(firstPol,mons, g);
        if (newfirstPol != null) {firstPol = factorTrig(newfirstPol, g);}
        return firstPol;
    }   
    
    private Polynom lnArgIsDivide(Polynom new_pol, CanonicForms g) {
        int pwr;
        boolean flag;
        if (new_pol.powers.length / new_pol.coeffs.length != g.newRing.varNames.length) {
            new_pol = new_pol.unTruncate(g.newRing.varNames.length - new_pol.powers.length / new_pol.coeffs.length);
        }
        do {
            flag = false;
            for (int coef = 1; coef < new_pol.coeffs.length + 1; coef++) {
                for (int i = (coef - 1) * g.newRing.varNames.length + g.RING.varNames.length; i < coef * g.newRing.varNames.length; i++) {
                    if (new_pol.powers[i] == 0) continue;

                    //if(i == coef * g.newRing.varNames.length - 1) {continue;}
                    int step = (coef - 1) * g.newRing.varNames.length + g.RING.varNames.length;//step - шаг между коэффициентами
                    Element new_func = null;
                    if (((F) g.List_of_Change.get(i - step)).name == F.LN) {
                        if (((F) g.List_of_Change.get(i - step)).X[0] instanceof Fraction) {
                            pwr = new_pol.powers[i];
                            Fraction arg = (Fraction) ((F) g.List_of_Change.get(i - step)).X[0];
                            Element num = arg.num;
                            Element denom = arg.denom;
                            num = num.subtract(g.RING.numberONE(), g.newRing);
                            denom = denom.subtract(g.RING.numberONE(), g.newRing);
                            Element argum = (num.isNegative()) ? denom : num;
                            if (num.compareTo(denom.negate(g.newRing), g.newRing) == 0) {
                                if (num.isNegative()) {
                                    new_func = (Polynom) g.addNewElement(new F(F.ARCCTG, argum.multiply(g.RING.complexMINUS_I(), g.newRing)), 1);
                                    new_pol = new_pol.unTruncate(1);
                                    new_pol.coeffs[coef - 1] = new_pol.coeffs[coef - 1].multiply(Polynom.polynomFromNumber(new NumberZ(2), g.newRing), g.newRing).multiply(g.newRing.numberMINUS_I, g.newRing);
                                    new_pol.powers[coef * g.newRing.varNames.length - 1] = pwr;
                                    new_pol.powers[i + coef - 1] = new_pol.powers[i + coef - 1] - pwr;
                                    flag = true;
                                    break;
                                } else {
                                    new_func = (Polynom) g.addNewElement(new F(F.ARCTG, arg.multiply(g.RING.complexMINUS_I(), g.newRing)), 1);
                                    new_pol = new_pol.unTruncate(1);
                                    new_pol.coeffs[coef - 1] = new_pol.coeffs[coef - 1].multiply(Polynom.polynomFromNumber(new NumberZ(2), g.newRing), g.newRing).multiply(g.newRing.numberMINUS_I, g.newRing);
                                    new_pol.powers[coef * g.newRing.varNames.length - 1] = 1;
                                    new_pol.powers[i + coef - 1] = new_pol.powers[i + coef - 1] - 1;
                                    flag = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } while (flag);

        return new_pol;
    }

    /**
     * примнеяем к полиному формулы: Ln(1+iz) - Ln(1-iz) = 2i*arctg(z); Ln(1-iz)
     * - Ln(1+iz) = 2i*arcctg(z);
     *
     * @param sum - polynom
     * @param g - CanonicForms
     *
     * @return
     */
    private Polynom CheckLn(Polynom sum, CanonicForms g) {
        int var = sum.powers.length / sum.coeffs.length;
        Polynom third = null;

        if ((sum.coeffs[0].abs(g.newRing)).compareTo(sum.coeffs[1].abs(g.newRing), g.newRing) > 0) {
            Element subtract = (sum.coeffs[0].abs(g.newRing)).subtract(sum.coeffs[1].abs(g.newRing), g.newRing);
            int[] pow = new int[sum.powers.length / sum.coeffs.length];
            System.arraycopy(sum.powers, 0, pow, 0, sum.powers.length / sum.coeffs.length);
            sum.coeffs[0] = (sum.coeffs[0].isNegative()) ? sum.coeffs[0].add(subtract, g.newRing) : sum.coeffs[0].subtract(subtract, g.newRing);
            third = (sum.coeffs[0].isNegative()) ? new Polynom(pow, new Element[] {subtract.negate(g.newRing)})
                    : new Polynom(pow, new Element[] {subtract});
        }
        if ((sum.coeffs[0].abs(g.newRing)).compareTo(sum.coeffs[1].abs(g.newRing), g.newRing) < 0) {
            Element subtract = (sum.coeffs[1].abs(g.newRing)).subtract(sum.coeffs[0].abs(g.newRing), g.newRing);
            int[] pow = new int[sum.powers.length / sum.coeffs.length];
            System.arraycopy(sum.powers, sum.powers.length / sum.coeffs.length, pow, 0, sum.powers.length / sum.coeffs.length);
            sum.coeffs[1] = (sum.coeffs[1].isNegative()) ? sum.coeffs[1].add(subtract, g.newRing) : sum.coeffs[1].subtract(subtract, g.newRing);
            third = (sum.coeffs[1].isNegative()) ? new Polynom(pow, new Element[] {subtract.negate(g.newRing)})
                    : new Polynom(pow, new Element[] {subtract});
        }
        Element gcdCoeffs = sum.coeffs[0];

        FactorPol fpol = sum.factorizationOfTwoMonoms(g.newRing);
        Element Pow1 = null;
        Element Pow2 = null;
        Element new_f = null;
        for (int i = 0; i < fpol.multin.length; i++) {
            if (fpol.multin[i].coeffs.length == 1) {
                continue;
            }
            for (int j = 0; j < var; j++) {
                if (fpol.multin[i].powers[j] != 0) {
                    F Ln1 = (F) g.List_of_Change.get(j - g.RING.varNames.length);
                    Pow1 = Ln1.X[0];
                    break;
                }
            }
            for (int j = var; j < 2 * var; j++) {
                if (fpol.multin[i].powers[j] != 0) {
                    F Ln2 = (F) g.List_of_Change.get(j - g.RING.varNames.length - var);
                    Pow2 = Ln2.X[0];
                    break;
                }
            }

            Pow1 = Pow1.subtract(g.RING.numberONE(), g.newRing);
            Pow2 = Pow2.subtract(g.RING.numberONE(), g.newRing);
            Element arg = (Pow1.isNegative()) ? Pow2 : Pow1;
            if (Pow1.compareTo(Pow2.negate(g.newRing), g.newRing) == 0) {
                if (fpol.multin[i].coeffs[1].isNegative()) {
                    if (Pow1.isNegative()) {
                        new_f = (Polynom) g.addNewElement(new F(F.ARCCTG, arg.multiply(g.RING.complexMINUS_I(), g.newRing)), 1);
                        fpol.multin[i] = (Polynom) new_f.multiply(Polynom.polynomFromNumber(new NumberZ(2), g.newRing), g.newRing).multiply(new Complex(0, 1), g.newRing);
                    } else {
                        new_f = (Polynom) g.addNewElement(new F(F.ARCTG, arg.multiply(g.RING.complexMINUS_I(), g.newRing)), 1);
                        fpol.multin[i] = (Polynom) new_f.multiply(Polynom.polynomFromNumber(new NumberZ(2), g.newRing), g.newRing).multiply(new Complex(0, 1), g.newRing);
                    }
                } else {
                    return null;
                }
            }

        }
        if (new_f == null) {
            return null;
        }
        if (third == null) {
            return (Polynom) (fpol.toPolynomOrFraction(g.newRing)).multiply(Polynom.polynomFromNumber(gcdCoeffs, g.newRing), g.newRing);
        } else {
            return (Polynom) (fpol.toPolynomOrFraction(g.newRing)).multiply(Polynom.polynomFromNumber(gcdCoeffs, g.newRing), g.newRing).add(third, g.newRing);
        }

    }

    /**
     * упрощаем полином p по формуле: Ln(iz+sqrt(1-z^2)) = i*arcsin(z);
     *                 или Ln() = 2iArctan(z)////////////..............
     * @param p - polynom
     * @param g - параметр, в котором хранится вся информация о функции после
     * перехода от древовидного представления к полиномиальному виду
     *
     * @return
     */
    private Polynom MakeArcsin(Polynom new_pol, CanonicForms g) {
        boolean flag;
        Element arg_f = null;
        Element el = null;
        int var = new_pol.powers.length / new_pol.coeffs.length;
        do {
            flag = false;
            for (int coef = 1; coef < new_pol.coeffs.length + 1; coef++) {
                for (int i = (coef - 1) * g.newRing.varNames.length + g.RING.varNames.length;
                        i < coef * g.newRing.varNames.length; i++) {
                    if (new_pol.powers[i] == 0)     continue;
                    int step = (coef - 1) * g.newRing.varNames.length + g.RING.varNames.length;  //step - шаг между коэффициентами
                    if (i == coef * g.newRing.varNames.length - 1) continue;
                    F new_func = null;
                    Element z = null;
                    if ((((F) g.List_of_Change.get(i - step)).name == F.LN)) {
                        arg_f = ((F) g.List_of_Change.get(i - step)).X[0];
                    }
                    if (arg_f instanceof F) {
                        F arg = (F) arg_f;
                        if (arg.name == F.ADD) {
                            if (arg.X.length == 2) {
                                if (((F) arg.X[0]).name == F.MULTIPLY & ((F) arg.X[1]).name == F.SQRT) {
                                    if (((F) arg.X[0]).X.length == 2) {
                                        if (((F) arg.X[0]).X[0].isI(g.newRing)) {
                                            z = ((F) arg.X[0]).X[1];
                                        } else if (((F) arg.X[0]).X[1].isI(g.newRing)) {
                                            z = ((F) arg.X[0]).X[0];
                                        }
                                    }
                                    if (((F) ((F) arg.X[1]).X[0]).name == F.SUBTRACT) {
                                        if (((F) ((F) arg.X[1]).X[0]).X[0].isOne(g.newRing)
                                                & ((F) ((F) arg.X[1]).X[0]).X[1].compareTo(z.pow(2, g.newRing)) == 0) {
                                            //new_f = (Polynom)g.addNewElement(new F(F.ARCCTG,
                                        }
                                    }
                                } else if (((F) arg.X[1]).name == F.MULTIPLY & ((F) arg.X[0]).name == F.SQRT) {
                                    if (((F) arg.X[1]).X.length == 2) {
                                    }
                                }
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }

        } while (flag);
        return new_pol;
    }

    /**
     * работа с дробями
     *
     * @param frac - дробь
     * @param ring
     *
     * @return
     */
    public F WorkWithFraction(Element frac, Ring ring) {CanonicForms g = ring.CForm;
        Element num = frac.num(ring);
        Element denom = frac.denom(ring);
        Element deNum = denom.multiply(ring.numberI.multiply(new NumberZ(2), ring), ring);
        if (num.subtract(deNum.subtract(new NumberZ(2), ring), ring).compareTo(Polynom.polynomFromNumber(new Complex(0, 0), ring), ring) == 0) {
            if (g.List_of_Change.size() == 1 && ((F) g.List_of_Change.get(0)).name == F.EXP) {
                Element pow = ((F) g.List_of_Change.get(0)).X[0];
                Element p = new Polynom(new int[] {1}, new Element[] {ring.numberI.multiply(new NumberZ(2), ring)});
                if (pow.compareTo(p, ring) == 0) {
                    return new F(F.CTG, pow.divide(ring.numberI.multiply(new NumberZ(2), ring), ring));
                }
            }
        }
        if (num.add(deNum.subtract(new NumberZ(2), ring), ring).compareTo(Polynom.polynomFromNumber(new Complex(0, 0), ring), ring) == 0) {
            if (g.List_of_Change.size() == 1 && ((F) g.List_of_Change.get(0)).name == F.EXP) {
                Element pow = ((F) g.List_of_Change.get(0)).X[0];
                Element p = new Polynom(new int[] {1}, new Element[] {ring.numberI.multiply(new NumberZ(2), ring)});
                if (pow.compareTo(p, ring) == 0) {
                    return (F) new F(F.CTG, pow.divide(ring.numberI.multiply(new NumberZ(2), ring), ring)).negate(ring);
                }
            }
        }
        if (num.add(deNum.add(new NumberZ(2), ring), ring).compareTo(Polynom.polynomFromNumber(new Complex(0, 0), ring), ring) == 0) {
            if (g.List_of_Change.size() == 1 && ((F) g.List_of_Change.get(0)).name == F.EXP) {
                Element pow = ((F) g.List_of_Change.get(0)).X[0];
                Element p = new Polynom(new int[] {1}, new Element[] {ring.numberI.multiply(new NumberZ(2), ring)});
                if (pow.compareTo(p, ring) == 0) {
                    return new F(F.TG, pow.divide(ring.numberI.multiply(new NumberZ(2), ring), ring));
                }
            }
        }
        if (num.subtract(deNum.add(new NumberZ(2), ring), ring).compareTo(Polynom.polynomFromNumber(new Complex(0, 0), ring), ring) == 0) {
            if (g.List_of_Change.size() == 1 && ((F) g.List_of_Change.get(0)).name == F.EXP) {
                Element pow = ((F) g.List_of_Change.get(0)).X[0];
                Element p = new Polynom(new int[] {1}, new Element[] {ring.numberI.multiply(new NumberZ(2), ring)});
                if (pow.compareTo(p, ring) == 0) {
                    return (F) new F(F.TG, pow.divide(ring.numberI.multiply(new NumberZ(2), ring), ring)).negate(ring);
                }
            }
        }
        if (num instanceof Polynom) {
            num = NumAndDenom((Polynom) num, g);
        }
        if (denom instanceof Polynom) {
            denom = NumAndDenom((Polynom) denom, g);
        }
        return new F(F.DIVIDE, num, denom);
    }

    /**
     * упрощение числителя и знаменателя
     *
     * @param p - polynom
     * @param g - параметр, в котором хранится вся информация о функции после
     * перехода от древовидного представления к полиномиальному виду
     *
     * @return
     */
    private Element NumAndDenom(Polynom p, CanonicForms g) {
        Polynom exp_simple = factorExp((Polynom) p, g);//упрощение экспнент
        Polynom trig_simple = factorTrig((Polynom) exp_simple, g);//упрощение тригонометрии
//      Polynom poly = getPosInListOfChTheseFuncs((Polynom) p, g, new int[]{F.EXP, F.LN}); //отсортировали
//      int border = Border(poly, g, ExpNumbs(g));//нашли границу
//      Polynom[] polys = highAndLowPartsOfPolynom(poly, border); //получили 2 полинома
//      polys[0] = FactorExponent(polys[0], g);//упрощение полинома по экспонентам
//      polys[1].truncate();
//      polys[0].ordering(g.newRing);//поставили все мономы на место в соответствии с кольцом g.newRING
//      p = polys[0].add(polys[1], g.newRing);
        return g.Unconvert_Polynom_to_Element(trig_simple);
    }

    public static void main(String[] args) {
        Ring ring = new Ring("CQ[x]");  
        Element sin = new F("-3 \\sin(x)", ring);
        Element cos = new F("-7 \\cos(x)", ring);
        Element p1 = ring.CForm.ElementConvertToPolynom(sin);
        Element p2 = ring.CForm.ElementConvertToPolynom(cos);
       System.out.println("hh=    "+ p1+"    "+p2 );      
        FactorPol fp1 = (FactorPol) p1.factor(ring);
        FactorPol fp2 = (FactorPol) p2.factor(ring);
        fp2.powers[1] = -1;  System.out.println("fp2222==== " + fp2);
        Element ee1=fp1.multiply(fp2, ring);
                System.out.println("fp==== " + ee1);
        Element ee = ring.CForm.UnconvertToElement(ee1);
        System.out.println("multiply = " + ee);
        // result is 
//        Ring ring = new Ring("Q[x,y,z]"); Page page=new Page(ring, true); 
// 
//
//        F p = new F("\\sin(x^2+4x+2)", ring);
// 
//        Element ffff = new TrigonometricExpand().ExpandTr(p, ring);
//        Element kkkk = new TrigonometricExpand().ExpandTr(ffff, ring);
//        System.out.println("RES="+ kkkk);
 ;
    }
}
