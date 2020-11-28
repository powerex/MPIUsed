/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

// © Разработчик Смирнов Роман Антонович (White_Raven), ТГУ'10.
package com.mathpar.func;

import java.util.Vector;
import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 * Класс вычисляющий значение предела функции заданной в символном виде типа F
 * @author Смирнов Роман (WhiteRaven)
 */

public class LimitOf {

    private int STOP = 0; // флаг отвечающий за остановку вычислений 0 и 2 - продолжаем, 1 - прекращаем

    private Element NAN = Element.NAN;

    Ring ring=Ring.ringR64xyzt; // get default Ring

    private int numVar=0;


    public LimitOf() {
    }

    /**
     * Конструктор от колечко
     * @param f
     */
    public LimitOf(Ring r) {
       ring=r;
    }

    public LimitOf(Ring r, int num) {
       ring=r;
       numVar=num;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////// /////////////////////
//////////////// вычисление пределов без стороны стремления /////////////////////////////////////
    /**
     * Главная процедура для вычисления предела
     * @param f - функция, предел которой необходимо посчитать
     * @param point - точка стремления
     * @return предел функции f
     */
    public Element Limit(Element f, Element[] point){
        // вызываем процедуру для вычисления предела
        return (f instanceof F) ? Limit_((F) f, point) : limitToPolynom(f, point,ring);//       Limit_(new F(ID,f),point);
    }

    /**
     * Главная процедура для вычисления предела
     * @param f - функция предел которой необходимо посчитать
     * @param point - точка стремления
     * @return предел функции f
     */
    public Element Limit(Element f, Element point) {
        // вызываем процедуру для вычисления предела
        return (f instanceof F) ? Limit_((F) f, new Element[]{point}) : Limit_(new F(F.ID, f), new Element[]{point});
    }

    /**
     * Процедура вычисляющая предел функции
     * @param f - функция
     * @param point - точка стремления
     * @return
     */
    public Element Limit_(F f, Element[] point){
        Element[] res = new Element[f.X.length];
        for (int i = 0; i < f.X.length; i++) {
            if (f.X[i] instanceof F) {
                res[i] = Limit((F) f.X[i], point);
            } else {
                res[i] = limitToPolynom(f.X[i], point, ring); // вычисляем значение полинома
            }
        }
        Element RES = res[0];
        if (STOP!=1) { // если один из результатов NAN, то дальше не вычисляем
            switch (f.name) {
                case F.ID:
                    return RES;
                // для переодических на бесконечности NAN
                case F.SIN:
                case F.COS:
                case F.ARCCOS:
                case F.ARCSIN:
                case F.ARCTG:
                case F.ARCCTG:
                    if (RES.isInfinite()) { STOP = 2;}
                    return new FvalOf(ring).valOf(new F(f.name,RES),new Element[0]);
                case F.TG:
                case F.CTG:
                     if (RES.isInfinite()) { STOP = 1;}
                     return new FvalOf(ring).valOf(new F(f.name,RES),new Element[0]);
 
                case F.ADD:
                    return ADDLimitProc(res, f.X, point);
                case F.MULTIPLY:
                    return MULTIPLYLimitProc(res, f.X, point);
                case F.SUBTRACT:
                    return SUBTRACTLimitProc(res, f.X, point);
                case F.DIVIDE:
                    return DIVIDELimitProc(res, f.X, point);
                case F.POW:
                case F.intPOW:
                    return POWLimitProc(res, f.X, point);
                case F.LOG:
                    return new FvalOf(ring).getFunctionValue(f.name, res[0], res[1]);
                default:
                    // для других считаем как обычное значение в точке
                    return new FvalOf(ring).valOf(new F(f.name,RES),new Element[0]);
            }
        } else {
            return NAN;
        }
    }

    /**
     * Посчет функции f в точке point, с учетом правил вычисления полиномов в точках \\infty & -\\infty
     * @param f - входная функция
     * @param point
     * @return значение f  в точке point.
     */
     private Element valOfLim(F f, Element[] point) {
        Element[] CalcArg = new Element[f.X.length];
        for (int i = 0; i < f.X.length; i++) {
            switch (f.X[i].numbElementType()) {
                case Ring.F:
                    CalcArg[i] = valOfLim((F) f.X[i], point);
                    break;
                case Ring.Polynom:
                    CalcArg[i] = limitToPolynom((Polynom)f.X[i], point, ring);
                    break;
                case Ring.Fname:
                    if (((Fname) f.X[i]).name.equals("\\e")) {
                        CalcArg[i] = ring.numberONE().e(ring);
                        break;
                    }
                    if (((Fname) f.X[i]).name.equals("\\pi")) {
                        CalcArg[i] = ring.numberONE().pi(ring);
                        break;
                    }
                    CalcArg[i] = f.X[i];
                    break;
                default:
                    CalcArg[i] = f.X[i];
            }
        }
        Element res = null;
        switch (f.name) {
           case F.ID:
                return (CalcArg[0] == null) ? new F(f.name, CalcArg) : CalcArg[0];
            case F.ABS:
                res = CalcArg[0].abs(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.EXP:
                res = CalcArg[0].exp(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.SQRT:
                res = CalcArg[0].sqrt(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.COS:
                res = CalcArg[0].cos(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.SIN:
                res = CalcArg[0].sin(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.TG:
                res = CalcArg[0].tan(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.CTG:
                res = CalcArg[0].ctg(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.SH:
                res = CalcArg[0].sh(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.CH:
                res = CalcArg[0].ch(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.TH:
                res = CalcArg[0].th(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.CTH:
                res = CalcArg[0].cth(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.LOG:
                res = CalcArg[1].log(CalcArg[0], ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.LN:
                res = CalcArg[0].ln(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.LG:
                res = CalcArg[0].lg(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.ARCSIN:
                res = CalcArg[0].arcsn(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.ARCCOS:
                res = CalcArg[0].arccs(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.ARCTG:
                res = CalcArg[0].arctn(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.ARCCTG:
                res = CalcArg[0].arcctn(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.ARCSH:
                res = CalcArg[0].arsh(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.ARCCH:
                res = CalcArg[0].arch(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.ARCTGH:
                res = CalcArg[0].arth(ring);
                return (res == null) ? new F(f.name, CalcArg) : res;
            case F.intPOW:
                return CalcArg[0].pow(CalcArg[1].intValue(), ring);
            case F.POW:
                return CalcArg[0].pow(CalcArg[1], ring);
            //------------------------------------------------------------------------------
            case F.ARCCTGH: // нет такой функции в элементе!!!!!!!!! ((((
                return new F(f.name, CalcArg);
            //------------------------------------------------------------------------------
            case F.ADD:
                res = CalcArg[0];
                for (int i = 1; i < CalcArg.length; i++) {
                    res = res.add(CalcArg[i], ring);
                }
                return res;
            case F.DIVIDE:
                return CalcArg[0].divide(CalcArg[1], ring);
            case F.SUBTRACT:
                return CalcArg[0].subtract(CalcArg[1], ring);
            case F.MULTIPLY:
                res = CalcArg[0];
                for (int i = 1; i < CalcArg.length; i++) {
                    res = res.multiply(CalcArg[i], ring);
                }
                return res;
        }
        return f;
    }



    /**
     * Процедура вычисляющая предел входного элемента el
     * @param el - входной элемент (Number.* или Polynom)
     * @param point - точка стремления
     * @return предел функции
     */
    private Element limitToPolynom(Element el, Element[] point, Ring ring) {
        if (el instanceof Polynom) {
            Polynom r = ((Polynom) el).normalNumbVar(ring);
            if ((r).isItNumber()) {return (r).isZero(ring) ? ring.numberZERO : ((Polynom) el).coeffs[0];}
            if (point[0].isInfinite()) { // значит на входе была бесконечность
                 if (((Polynom) el).powers[0] % 2 == 0) return (r.coeffs[0].isNegative()) ? Element.NEGATIVE_INFINITY: Element.POSITIVE_INFINITY;
                 return (r.coeffs[0].isNegative()) ? point[0].negate(ring) : point[0];    
            }else {return new FvalOf(ring).valOfToPolynom((Polynom) el,point);}
        } else return el ; 
    }

    /**
     * Процедура вычисляющая предел степени (0^{\\infty} , 1^{\\infty} , \\infty^{0})
     * @param Znach - значения аргументов степени
     * @param branch - аргументы степени
     * @param point - точка стремления
     * @return предел степени
     */
    private Element POWLimitProc(Element[] Znach, Element[] branch, Element[] point){
        Element RES = Znach[0].pow(Znach[1], ring);
        //Через Лапиталь Lim(y)=Lim(a^b)=e^Lim(b*Ln(a))
        if (RES.isNaN()) {
            Element num = new F(F.LN, branch[0]);
            Element denom = new F(F.DIVIDE, new Element[]{ring.numberONE, branch[1]});
            return new FvalOf(ring).valOf(new F(F.EXP,LopitalProc(num, denom, point)), new Element[0]);//  getFunctionValue(EXP, LopitalProc(num, denom, point,nb),nb);
        } else {
            return RES;
        }
    }

    /**
     * Процедура вычисляющая предел деления (\\infty/\\infty , 0/0)
     * @param Znach - значения аргументов деления
     * @param branch - аргументы деления
     * @param point - точка стремления
     * @return предел деления
     */
    private Element DIVIDELimitProc(Element[] Znach, Element[] branch, Element[] point){
        if((Znach[1].isInfinite())&&(!Znach[0].isInfinite()))return NumberZ.ZERO;
        // вычисляем значение деления
        Element RES = Znach[0].divide(Znach[1],ring);
        // Через Лапиталь --> Lim(a/b) = Lim(a'/b')
        return (RES.isNaN()) ? LopitalProc(branch[0], branch[1], point) : RES;
    }

    /**
     * Процедура вычисляющая предел разности (\\infty - \\infty)
     * @param Znach - значения аргументов разности
     * @param branch - аргументы разности
     * @param point - точка стремления
     * @return предел разности
     */
    private Element SUBTRACTLimitProc(Element[] Znach, Element[] branch, Element[] point)  {
        // вычисляем значение разности
        Element RES = Znach[0].subtract(Znach[1],ring);
        // через Лапиталь -- > Lim(a-b)= Lim(a(1-b/a)) если Lim b/a=1 , то ищем предел  (1-b/a)/(1/a)
         if(RES.isNaN()){
             F num = new F(F.DIVIDE, new Element[]{branch[1].multiply(ring.numberMINUS_ONE, ring), branch[0]}).expand(ring);
             F Denom = new F(F.DIVIDE, new Element[]{ring.numberONE, branch[0]}).expand(ring);
             return LopitalProc(num, Denom, point);
         }
        return RES;
    }

    /**
     * Метод сортирующий аргументы произведения F.e. Znach[]{0,4,infinity,8,-infinity}
     * на выходе [0,branch[0],infinity,branch[2],-infinity[4],32,NAN] ,
     * где последний элемент произведение всех , а предпоследний произведение всех не равных бесконечностям или нулю.
     * @param Znach - значения аргументов произведения
     * @param branch - аргументы прозведения
     * @return
     */
    Vector<Element> isInfinityOrZero(Element[] Znach, Element[] branch) {
        Vector<Element> Res = new Vector<Element>();
        Element fridom = ring.numberONE;
        Element MULTIPLE = Znach[0];
        if (Znach[0].isInfinite() | Znach[0].isZero(ring)) {
            Res.add(Znach[0]);
            Res.add(branch[0]);
        } else {
            fridom = Znach[0];
        }
        for (int i = 1; i < Znach.length; i++) {
            MULTIPLE = MULTIPLE.multiply(Znach[i],ring);
            if (Znach[i].isInfinite() | Znach[i].isZero(ring)) {
                Res.add(Znach[i]);
                Res.add(branch[i]);
            } else {
                fridom = fridom.multiply(Znach[i],ring);
            }
        }
        Res.add(fridom);
        Res.add(MULTIPLE);
        return Res;
    }

    /**
     * Процедура вычисляющая предел произведения( 0*\\infty)
     * @param Znach - значения аргументов произведения
     * @param branch - аргументы произведения
     * @param point - точка стремления
     * @return значение предела произведения
     */
    private Element MULTIPLYLimitProc(Element[] Znach, Element[] branch, Element[] point){
        Vector<Element> TempCalc = isInfinityOrZero(Znach, branch);
        if (!TempCalc.lastElement().isNaN()) {
            return TempCalc.lastElement();
        } // если не NAN  возвращаем результат умножения
        Element fridom = TempCalc.elementAt(TempCalc.size() - 2); // произведение свободных
        TempCalc.remove(TempCalc.lastElement());
        TempCalc.remove(TempCalc.lastElement());
        Element Calc = ring.numberONE;
        int indexPoz = -1;
        int indexNeg = -1;
        for (int i = 0; i < TempCalc.size(); i = +2) {
            Calc = Calc.multiply(TempCalc.elementAt(i),ring);
            if (TempCalc.elementAt(i).isZero(ring)) {
                indexNeg = i;
            } else {
                indexPoz = i;
            }
            if (indexPoz != -1 && indexNeg != -1) {
                // Через Лапиталь --> Lim(ab)=Lim(a/(1/b))
                Element Sub = DIVIDELimitProc(new Element[]{TempCalc.elementAt(indexPoz), TempCalc.elementAt(indexPoz)}, new Element[]{TempCalc.elementAt(indexPoz + 1),
                new F(F.DIVIDE, new Element[]{ring.numberONE, TempCalc.elementAt(indexNeg + 1)})}, point);
                TempCalc.remove(indexPoz);
                TempCalc.remove(indexPoz);
                if (!Sub.isInfinite()) {
                    fridom = fridom.multiply(Sub,ring);
                    TempCalc.remove(indexNeg - 2);
                    TempCalc.remove(indexNeg - 2);
                }
                i = 0;
                Calc = point[0].one(ring);
            }
        }
        return Calc.multiply(fridom,ring);
    }

    /**
     * Метод сортирующий аргументы суммы F.e. Znach[]{0,4,infinity,8,-infinity}
     * на выходе [infinity,branch[2],-infinity[4],12,NAN] ,
     * где последний элемент сумма всех , а предпоследний сумма всех не равных бесконечностям.
     * @param Znach - значения аргументов суммы
     * @param branch - аргументы суммы
     * @return
     */
    Vector<Element> isInfinity(Element[] Znach, Element[] branch, Ring ring) {
        Vector<Element> Res = new Vector<Element>();
        Element fridom = Znach[0].zero(ring);
        Element Add = Znach[0];
        if (Znach[0].isInfinite()) {
            Res.add(Znach[0]);
            Res.add(branch[0]);
        } else {
            fridom = Znach[0];
        }
        for (int i = 1; i < Znach.length; i++) {
            Add = Add.add(Znach[i],ring);
            if (Znach[i].isInfinite()) {
                Res.add(Znach[i]);
                Res.add(branch[i]);
            } else {
                fridom = fridom.add(Znach[i],ring);
            }
        }
        Res.add(fridom);
        Res.add(Add);
        return Res;
    }

    /**
     * Процедура вычисляющая предел суммы (\\infty+(-\\infty))
     * @param Znach - значения аргументов суммы
     * @param branch - аргументы суммы
     * @param point - точка стремления
     * @return предел суммы
     */
    private Element ADDLimitProc(Element[] Znach, Element[] branch, Element[] point) {
        Vector<Element> TempCalc = isInfinity(Znach, branch,ring); // "сортируем" аргументы суммы
        if (!TempCalc.lastElement().isNaN()) {
            return TempCalc.lastElement();
        } // если не NAN  возвращаем результат суммирования
        Element fridom = TempCalc.elementAt(TempCalc.size() - 2); // сумма свободных
        TempCalc.remove(TempCalc.lastElement());
        TempCalc.remove(TempCalc.lastElement());
        Element Calc = ring.numberZERO; // заводим переменную которая в которой будет сумма элементов из TempCalc
        int indexPoz = -1; // будущие индексы
        int indexNeg = -1;
        for (int i = 0; i < TempCalc.size(); i = +2) {
            Calc = Calc.add(TempCalc.elementAt(i),ring);
            // находим нужные нам элементы (-Infinity и Infinity)
            if (TempCalc.elementAt(i).isNegative()) {
                indexNeg = i;
            } else {
                indexPoz = i;
            }
            if (indexPoz != -1 && indexNeg != -1) {
                //Через Лапиталь аналогично разности
                Element Sub = SUBTRACTLimitProc(new Element[]{TempCalc.elementAt(indexPoz), TempCalc.elementAt(indexNeg)},
                        new Element[]{TempCalc.elementAt(indexPoz++), TempCalc.elementAt(indexNeg++)}, point);
                TempCalc.remove(indexPoz);
                TempCalc.remove(indexPoz);
                if (!Sub.isInfinite()) {
                    fridom = fridom.add(Sub,ring);
                    TempCalc.remove(indexNeg - 2);
                    TempCalc.remove(indexNeg - 2);
                }
                indexNeg = -1; //обнуляем все значения для следующего прохода
                indexPoz = -1;
                i = 0;
                Calc = ring.numberZERO;
            }
        }
        return Calc.add(fridom,ring);
    }

    /**
     * Метод вычисления предела функции y=f(x)/g(x) по формуле lim f(x)/g(x)=lim f'(x)/g'(x)
     * @param num - f(x)
     * @param denom - g(x)
     * @param point - точка в которой вычисляется предел
     * @return предел функции
     */
    Element LopitalProc(Element num, Element denom, Element[] point) {
        Element Dnum = num.D(numVar,ring); // находим производную числителя
        Element DDenom = denom.D(numVar,ring); //находим производную знаменателя
        F r=new F(F.DIVIDE, new Element[]{Dnum,DDenom});
        r=r.expand(ring);
        //считаем значения числителя и знаменателя в точке предела
        Element RES = valOfLim(r,point);
        // и если NAN вызываем себя еще раз ,иначе возвращаем результат
        return (RES.isNaN()) ? Limit(r, point) : RES;
}


    public static void main(String[] args) {
        Ring r=new Ring("R[x]");
        r.setAccuracy(50);
        F l=new F("(1/\\sin(x)-1/(\\sin(x))^3)",r);
        F k=new F("1/x-1/x^3",r);
        F f=new F("(1+1/x)^(x)",r);
        F a=new F("\\sin(x)/x",r);
        Element res = new LimitOf(r).Limit(l, NumberR.ZERO);
        System.out.println(l+" x----> 0 " + res.toString(r));
        Element res1 = new LimitOf(r).Limit(k, NumberR.ZERO);
        System.out.println(k+" x----> 0 " + res1.toString(r));
        Element res2 = new LimitOf(r).Limit(f, Element.POSITIVE_INFINITY);
        System.out.println(" 2 замечательный предел   " + f +"= "+res2.toString(r));
        Element res3 = new LimitOf(r).Limit(a, NumberR.ZERO);
        System.out.println(" 1 замечательный предел   " + a +"= "+res3.toString(r));
     }
}
