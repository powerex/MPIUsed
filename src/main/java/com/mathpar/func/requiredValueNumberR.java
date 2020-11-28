/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

/*
 * Класс , вычисляющий значения функции с гарантированной заданной точностью
 *
 */
package com.mathpar.func;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mathpar.number.*;
import com.mathpar.number.math.MathContext;
import com.mathpar.number.math.RoundingMode;
import com.mathpar.polynom.*;

/**
 *
 * @author Смирнов Роман
 */
public class requiredValueNumberR {

    MathContext mc;
    int accuracy;
    Ring r;// входное кольцо
    MathContext mc_old;
    ArrayList<Element> lists = new ArrayList<Element>(); // вектор листов функции
    int[] listsAccuracy; // необходимые точности для подсчета листов дерева которые нам понадобились
    NumberR[] calcLists; // подсчитанные значения листов
    ArrayList<int[]> func = new ArrayList<int[]>(); // преобразованная входная функция
    int[] funcAccuracy; // необходимые точности для узлов функции которые нам понадобились
    ArrayList<NumberR> calcFunc = new ArrayList<NumberR>(); // подсчитанные значения узлов дерева функции

    public requiredValueNumberR(Ring ring) {
        r = ring;
        accuracy = ring.getAccuracy();
        mc_old = ring.MC;
    }

    /**
     * Add the number of exact decimal places
     *
     * @param numbOfDecFig --extra number of exact decimal places
     */
    public void addAccuracy(int numbOfDecFigures) {
        accuracy += numbOfDecFigures;
        //r.MachineEpsilonR=NumberR.TEN.pow(-r.ACCURACY, r);
        r.setAccuracy(accuracy);
        r.MachineEpsilonR = NumberR.TEN.pow(-r.getAccuracy(), r);
        mc_old = mc;
        mc = new MathContext(accuracy, RoundingMode.HALF_EVEN);
    }

    /**
     * Substruct the number of exact decimal places
     *
     * @param numbOfDecFig --extra number of exact decimal places
     */
    public void subAccuracy(int numbOfDecFigures) {
        accuracy -= numbOfDecFigures;
        //r.MachineEpsilonR=NumberR.TEN.pow(-r.ACCURACY, r);
         r.setAccuracy(accuracy);
         r.MachineEpsilonR = NumberR.TEN.pow(-r.getAccuracy(), r);
         mc = mc_old;
    }

    /**
     * Set the number of exact decimal places in the number of NumberR type.
     * Each operation with NumberR will be perform with this accurecy
     *
     * @param numbOfDecFig -- number of exact decimal places
     */
    public void setAccuracy(int numbOfDecFigures) {
        accuracy = numbOfDecFigures;
         r.setAccuracy(accuracy);
        r.MachineEpsilonR = NumberR.TEN.pow(-r.getAccuracy(), r);

        mc = new MathContext(numbOfDecFigures, RoundingMode.HALF_EVEN);
    }

    /**
     * Главный метод трансформирующий дерево функции
     *
     * @param f
     */
    public void modifyTree(F f) {
        ArrayList<F>[] vectF = new ArrayList[F.FUNC_ARR_LEN];
        ArrayList<Integer>[] indexFunc = new ArrayList[F.FUNC_ARR_LEN];
        cleanOfRepeating(f, vectF, indexFunc, r);
        createTree(f, vectF, indexFunc);
        calcLists = new NumberR[lists.size()];
    }

    private int toIndexF(int name, ArrayList<Integer> vec) {
        return ((vec.size() - 1) < name) ? -1 : vec.get(name);
    }

    private void createTree(Element node,
            ArrayList<F>[] vectF, ArrayList<Integer>[] indexFunc) {
        if (node instanceof F) {
            int len = ((F) node).X.length;
            int repeatIndex = -1;
            switch (((F) node).name) {
                case F.SUBTRACT:
                case F.DIVIDE:
                case F.MULTIPLY:
                case F.POW:
                case F.LOG:
                case F.intPOW:
                case F.ADD:
                case F.ROOTOF:
                    repeatIndex = toIndexF(vectF[((F) node).name].indexOf(((F) node)), indexFunc[((F) node).name]);
                    if (repeatIndex != -1) {
                        func.add(new int[]{F.ID, repeatIndex});
                        return;
                    }
                    int[] add = new int[len + 1];
                    add[0] = ((F) node).name;
                    for (int i = 0; i < len; i++) {
                        createTree(((F) node).X[i], vectF, indexFunc);
                        if (func.get(func.size() - 1)[0] == F.ID) {
                            add[i + 1] = func.get(func.size() - 1)[1];
                            func.remove(func.size() - 1);
                        } else {
                            add[i + 1] = func.size() - 1;
                        }
                    }
                    func.add(add);
                    break;
                default: //все что с одним аргументом
                    repeatIndex = toIndexF(vectF[((F) node).name].indexOf(((F) node)), indexFunc[((F) node).name]);
                    if (repeatIndex != -1) {
                        func.add(new int[]{F.ID, repeatIndex});
                        return;
                    }
                    createTree(((F) node).X[0], vectF, indexFunc);
                    if (func.get(func.size() - 1)[0] == F.ID) {
                        indexFunc[((F) node).name].add(func.size() - 1);
                        int[] def = new int[]{((F) node).name, func.get(func.size() - 1)[1]};
                        func.remove(func.size() - 1);
                        func.add(def);
                    } else {
                        func.add(new int[]{((F) node).name, func.size() - 1});
                        indexFunc[((F) node).name].add(func.size() - 1);
                    }
            }
            return;
        }
        func.add(new int[]{F.ID, -lists.indexOf(node) - 1});
    }

    /**
     * Несколько модифицированный cleanOfRepeating из класса func.F
     *
     * @param node
     * @param vectF
     * @param indexF
     * @param ring
     * @return
     */
    private Element cleanOfRepeating(Element node,
            ArrayList<F>[] vectF, ArrayList<Integer>[] indexF, Ring ring) {
        if (node instanceof F) {
            F f_node = new F();
            f_node.name = ((F) node).name;
            f_node.X = ((F) node).X;
            Element[] X_node = f_node.X;
            int len = X_node.length;
            int f_name = f_node.name;
            for (int i = 0; i < len; i++) {
                if (X_node[i] != null) {
                    ((F) node).X[i] = cleanOfRepeating(X_node[i], vectF, indexF, ring);
                }
            }
            if (vectF[f_node.name] == null) {
                vectF[f_node.name] = new ArrayList<F>();
                indexF[f_node.name] = new ArrayList<Integer>();

            }
            for (F f : vectF[f_node.name]) {
                if (len == f.X.length) {
                    Element[] XX = f.X;
                    Boolean equalsF = true;
                    if ((f_name >= F.FIRST_INFIX_COMMUT_ARG) && (f_name <= F.LAST_INFIX_COMMUT_ARG)) {
                        int[] eq = new int[len];
                        int pos = len;
                        for (int j = 0; j < pos; j++) {
                            eq[j] = j;
                        }
                        search:
                        for (int j = 0; j < len; j++) {
                            for (int j1 = 0; j1 < pos; j1++) {
                                if ((X_node[j] == XX[eq[j1]])) {
                                    eq[j1] = eq[--pos];
                                    continue search;
                                }
                            }
                            equalsF = false;
                            break search;
                        }
                    } else {
                        for (int j = 0; j < len; j++) {
                            if (X_node[j] != XX[j]) {
                                equalsF = false;
                                break;
                            }
                        }
                    }
                    if (equalsF) {
                        return f;
                    }
                }
            }
            vectF[f_node.name].add(f_node);
            return f_node;
        }
        if (node == null) {
            return node;
        }
        for (Element el : lists) {
            if (node.compareTo(el, ring) == 0) {
                return el;
            }
        }
        lists.add(node);
        return node;
    }

    /**
     * Метод для распечатки функции данного типа
     *
     * @param i - номер строки в func
     * @return
     */
    private String elToString(int i) {
        int[] h = func.get(i);
        if (h.length == 2) {
            String f = (h[1] > -1) ? elToString(h[1]) : lists.get(Math.abs(h[1] + 1)).toString(r);
            return F.FUNC_NAMES[h[0]] + "(" + f + ")";
        }
        String res = "";
        switch (h[0]) {
            case F.ADD:
                for (int j = 1; j < h.length - 1; j++) {
                    res += (h[j] > -1) ? elToString(h[j]) : lists.get(Math.abs(h[j] + 1)).toString(r);
                    res += "+";
                }
                res += (h[h.length - 1] > -1) ? elToString(h[h.length - 1]) : lists.get(Math.abs(h[h.length - 1] + 1)).toString(r);
                return res;
            case F.MULTIPLY:
                for (int j = 1; j < h.length - 1; j++) {
                    res += "(";
                    res += (h[j] > -1) ? elToString(h[j]) : lists.get(Math.abs(h[j] + 1)).toString(r);
                    res += ")*";
                }
                res += "(";
                res += (h[h.length - 1] > -1) ? elToString(h[h.length - 1]) : lists.get(Math.abs(h[h.length - 1] + 1)).toString(r);
                return res + ")";
            case F.SUBTRACT:
                String res1 = (h[1] > -1) ? elToString(h[1]) : lists.get(Math.abs(h[1] + 1)).toString(r);
                String res2 = (h[2] > -1) ? elToString(h[2]) : lists.get(Math.abs(h[2] + 1)).toString(r);
                return res1 + "-" + res2;
            case F.DIVIDE:
                String res1d = (h[1] > -1) ? elToString(h[1]) : lists.get(Math.abs(h[1] + 1)).toString(r);
                String res2d = (h[2] > -1) ? elToString(h[2]) : lists.get(Math.abs(h[2] + 1)).toString(r);
                return "(" + res1d + ")/(" + res2d + ")";
            case F.POW:
            case F.intPOW:
                String res1p = (h[1] > -1) ? elToString(h[1]) : lists.get(Math.abs(h[1] + 1)).toString(r);
                String res2p = (h[2] > -1) ? elToString(h[2]) : lists.get(Math.abs(h[2] + 1)).toString(r);
                return res1p + "^{" + res2p + "}";
            case F.LOG:
                String res1l = (h[1] > -1) ? elToString(h[1]) : lists.get(Math.abs(h[1] + 1)).toString(r);
                String res2l = (h[2] > -1) ? elToString(h[2]) : lists.get(Math.abs(h[2] + 1)).toString(r);
                return F.FUNC_NAMES[F.LOG] + "_{" + res1l + "}(" + res2l + ")";
            case F.ROOTOF:
                String res1r = (h[1] > -1) ? elToString(h[1]) : lists.get(Math.abs(h[1] + 1)).toString(r);
                String res2r = (h[2] > -1) ? elToString(h[2]) : lists.get(Math.abs(h[2] + 1)).toString(r);
                return "\\sqrt" + "[" + res2r + "]{" + res1r + "}";
        }
        return " ";
    }

    @Override
    public String toString() {
        int i = func.size() - 1;
        return elToString(i);
    }

    public String toString(Ring r) {
        int i = func.size() - 1;
        return elToString(i);
    }

    String oneTableString(int i) {
        String res = F.FUNC_NAMES[func.get(i)[0]] + " | ";
        for (int j = 1; j < func.get(i).length - 1; j++) {
            res += func.get(i)[j] + ",";
        }
        res = (calcFunc.isEmpty()) ? (res + func.get(i)[func.get(i).length - 1]) : (res + func.get(i)[func.get(i).length - 1] + "      [  " + calcFunc.get(i) + "  ]");
        return res;
    }

    /**
     * Распечатываем функцию вида requiredValueNumberR
     */
    public void RVNRtoString() {
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        System.out.println("Ring :  " + r);
        System.out.println(" Element : " + Array.toString(lists.toArray(new Element[lists.size()]), r));
        if (calcLists != null) {
            System.out.println(" Calculate Elements : " + Arrays.toString(calcLists));
        }
        System.out.println(" Func :  ");
        for (int i = 0; i < func.size(); i++) {
            System.out.println(oneTableString(i));
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------");
    }
    //=========================================================================================================
    //====================  Подсчтет функции с заданной точностью ==========================================
    //=========================================================================================================
    int delta = 2;// по умолчанию шаг по интервалу accuracy
    Element[] Point;
    NumberR eps;

    /**
     * Метод вычисляющий значение функции f в точке point . Метод обычного
     * пересчета.
     *
     * @param f - функция
     * @param point - точка подсчета
     * @param startAccuracy - нижняя граница интервала точности
     * @param endAccuracy - верхняя граница
     * @return значение f в точке point с заданной пользователем точностью
     */
    public Element requiredValueOf_usualRepeat(Element[] point, int startAccuracy, int endAccuracy, int stepAccuracy) {
        if (stepAccuracy <= 0 | startAccuracy <= 0 | endAccuracy <= 0 | endAccuracy <= startAccuracy | (endAccuracy - startAccuracy) < delta) {
            try {
                throw new Exception(r.exception.append("Don't satisfy the accuracy interval !").toString());
            } catch (Exception ex) {
                Logger.getLogger(requiredValueNumberR.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int maxNumCalculate = Math.round((endAccuracy - startAccuracy) / delta); // максимальное колличество посчетов по интервалу accuracy
        setAccuracy(startAccuracy); // цепляем стартовую точность
        eps = NumberR.TEN.pow(-startAccuracy, r); // ноль соответствующий стартовой точности
        delta = stepAccuracy;
        int col = 0;
        while (col < maxNumCalculate) {
            addAccuracy(delta);
            Element func_delta = Usual_Value_Of(point);
            addAccuracy(delta);
            Element func_double_delta = Usual_Value_Of(point);
            NumberR res = (NumberR)((NumberR) func_delta).subtract((NumberR) func_double_delta, r.MC);
            if (!(res.compareTo(eps.abs(mc)) > 1)) {
                return func_double_delta;
            };
            col++;
        }
        return NumberR.NAN;
    }

    /**
     * Обычный посчет функции вида requiredValueNumberR в точке point. Значением
     * является последний элемент из calcFunc
     *
     * @param point
     * @return
     */
    public Element Usual_Value_Of(Element[] point) {
        RVNR_valueOf(point);
        int indexNaN = calcFunc.size() - func.size();
        return (indexNaN < 0) ? NumberR.NAN : calcFunc.get(calcFunc.size() - 1);
    }

    public Element debugValue(Element[] point, int startAccuracy, int endAccuracy, int stepAccuracy) {
        if (stepAccuracy <= 0 | startAccuracy <= 0 | endAccuracy <= 0 | endAccuracy <= startAccuracy | (endAccuracy - startAccuracy) < delta) {
            try {
                throw new Exception(r.exception.append("Don't satisfy the accuracy interval !").toString());
            } catch (Exception ex) {
                Logger.getLogger(requiredValueNumberR.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int maxNumCalculate = Math.round((endAccuracy - startAccuracy) / delta); // максимальное колличество посчетов по интервалу accuracy
        Point = point;
        setAccuracy(startAccuracy); // цепляем стартовую точность
        eps = NumberR.TEN.pow(-startAccuracy, r); // ноль соответствующий стартовой точности
        delta = stepAccuracy;
        Element res = Element.NAN;// будущий результат
        int i = 0;
        for (; i < func.size(); i++) {
            res = calculateFuncs(i, maxNumCalculate);
            if (res.isNaN()) {
                return res;
            }
        }
        return calcFunc.get(i - 1);
    }

    private Element calculateFuncs(int index, int stop) {
        int inputAccuracy = r.getAccuracy();
        int col = 0;
        while (col < stop) {
            addAccuracy(delta);
            Element func_delta = calc_tableFunc(index);
            addAccuracy(delta);
            Element func_double_delta = calc_tableFunc(index);
            NumberR res = (NumberR)((NumberR) func_delta).subtract((NumberR) func_double_delta, r.MC);
            if (!(res.compareTo(eps.abs(mc)) > 1)) {
                setAccuracy(inputAccuracy);
                calcFunc.add((NumberR) func_double_delta);
                return (NumberR) func_double_delta;
            }
        }
        setAccuracy(inputAccuracy);
        return Element.NAN;
    }

    private NumberR calculateLists(int index) {
        if (calcLists[index] == null) {
            Element Res = lists.get(index);
            Element Res_ = (Res.numbElementType() < Ring.Polynom) ? Res : ((Polynom) Res).value(Point, r);
            calcLists[index] = ((NumberR) Res_);
            return (NumberR) Res_;
        } else {
            return calcLists[index];
        }
    }

    /**
     * Достаем по индексу нужное посчитанное значение (-) - посчитанный листочек
     * (+) - узел функции
     *
     * @param index
     * @return
     */
    private NumberR getCalcArg(int index) {
        return (index < 0) ? calculateLists(-index - 1) : calcFunc.get(index);
    }

    private Element calc_tableFunc(int i) {
        int[] f = func.get(i);
        NumberR res = null;
        switch (f[0]) {
            case F.ID:
                return getCalcArg(f[1]);
            case F.ABS:
                return ((NumberR) getCalcArg(f[1])).abs(mc);
            case F.EXP:
                return (NumberR)(new FuncNumberR(r).exp(getCalcArg(f[1])));
            case F.SQRT:
                return (NumberR)(new FuncNumberR(r).sqrt(getCalcArg(f[1])));
            case F.COS:
                return (NumberR)(new FuncNumberR(r).cos(getCalcArg(f[1])));
            case F.SIN:
                return (NumberR)(new FuncNumberR(r).sin(getCalcArg(f[1])));
            case F.TG:
                return (NumberR)(new FuncNumberR(r).tan(getCalcArg(f[1])));
            case F.CTG:
                return (NumberR)(new FuncNumberR(r).ctg(getCalcArg(f[1])));
            case F.SH:
                return new FuncNumberR(r).sh(getCalcArg(f[1]));
            case F.CH:
                return new FuncNumberR(r).ch(getCalcArg(f[1]));
            case F.TH:
                return new FuncNumberR(r).th(getCalcArg(f[1]));
            case F.CTH:
                return (NumberR)(new FuncNumberR(r).cth(getCalcArg(f[1])));
            case F.LOG:
                return (NumberR)(((NumberR)(new FuncNumberR(r).ln(getCalcArg(f[1])))).divide((NumberR)(new FuncNumberR(r).ln(getCalcArg(f[2]))), mc));
            case F.LN:
                return (NumberR)(new FuncNumberR(r).ln(getCalcArg(f[1])));
            case F.LG:
                return (NumberR)(((NumberR)(new FuncNumberR(r).ln(NumberR.POSCONST[10]))).divide((NumberR)(new FuncNumberR(r).ln(getCalcArg(f[1]))), mc));
            case F.ARCSIN:
                return (NumberR)(new FuncNumberR(r).arcsn(getCalcArg(f[1])));
            case F.ARCCOS:
                return (NumberR)(new FuncNumberR(r).arccs(getCalcArg(f[1])));
            case F.ARCTG:
                return new FuncNumberR(r).arctn(getCalcArg(f[1]));
            case F.ARCCTG:
                return new FuncNumberR(r).arcctn(getCalcArg(f[1]));
            case F.ARCSH:
                return new FuncNumberR(r).arsh(getCalcArg(f[1]));
            case F.ARCCH:
                return (NumberR)(new FuncNumberR(r).arch(getCalcArg(f[1])));
            case F.ARCTGH:
                return (NumberR)(new FuncNumberR(r).arth(getCalcArg(f[1])));
            case F.intPOW:
                return getCalcArg(f[1]).pow(getCalcArg(f[2]).intValue(), r);
            case F.POW:
                NumberR w = (NumberR)(new FuncNumberR(r).ln(getCalcArg(f[1])));
                NumberR w_ = (NumberR)getCalcArg(f[2]).multiply(w, mc);
                return new FuncNumberR(r).exp(w_);
            case F.ARCCTGH:
                return (NumberR)(new FuncNumberR(r).arcth(getCalcArg(f[1])));
            case F.ADD:
                res = getCalcArg(f[1]);
                for (int in = 2; in < f.length; in++) {
                    res = (NumberR)res.add((NumberR) getCalcArg(f[in]), mc);
                }
                return (NumberR) res;
            case F.DIVIDE:
                return (NumberR)((NumberR) getCalcArg(f[1])).divide((NumberR) getCalcArg(f[2]), mc);
            case F.SUBTRACT:
                return (NumberR)((NumberR) getCalcArg(f[1])).subtract((NumberR) getCalcArg(f[2]), mc);
            case F.MULTIPLY:
                res = getCalcArg(f[1]);
                for (int in = 2; in < f.length; in++) {
                    res = (NumberR)res.multiply((NumberR) getCalcArg(f[in]), mc);
                }
                return (NumberR) res;
        }
        return Element.NAN;
    }

    public void RVNR_valueOf(Element[] point) {
        Element Res;
        for (int indexEl = 0; indexEl < lists.size(); indexEl++) {
            Res = lists.get(indexEl);
            Element Res_ = (Res.numbElementType() < Ring.Polynom) ? Res : ((Polynom) Res).value(point, r);
            calcLists[indexEl] = ((NumberR) Res_);
        }
        NumberR tempRes;
        for (int indexFunc = 0; indexFunc < func.size(); indexFunc++) {
            tempRes = (NumberR)calc_tableFunc(indexFunc);
            if (tempRes.isNaN()) {
                return;
            }
            calcFunc.add(tempRes);
        }
    }

    //=========================================================================================================
    public static void main(String[] args) {
//        String s="942809041582063365867792482806465385713114583584632048784453158660488318974738025900258356218427715156675897487274864683283224037233824808429414331399957220942148443951670395170533300334101854707047646739572386";
//        System.out.println(" FLOATPOS = " + s.length());
//






        Ring r = new Ring("R[x,y]");
        F h = new F(F.SQRT, new Element[]{new NumberR(2)});
        F l = new F(F.DIVIDE, new Element[]{new NumberR(1), new NumberR(3)});
        F h1 = new F(F.ADD, new Element[]{h, h});
        F res = new F(F.MULTIPLY, new Element[]{h1, l});
        r.setAccuracy(8);
        r.FLOATPOS = 9;
        F t = new F("\\sin(\\sin(x^2))+\\cos(\\sin(\\sin(x^2)))+x", r);


        Element point = new NumberR("5.08");

        long u = System.currentTimeMillis();
        System.out.println(" Обычный          " + new FvalOf(r).valOf(t, new Element[]{point}).toString(r) + "   Время " + (System.currentTimeMillis() - u));
        u = System.currentTimeMillis();
        requiredValueNumberR re = new requiredValueNumberR(r);
        re.modifyTree(t);
        NumberR d = ((NumberR) re.debugValue(new Element[]{point}, 100, 120, 5));

        System.out.println(" Модифицированный " + d.toString(r) + "   Время " + (System.currentTimeMillis() - u));

//        System.out.println(" usual  ----- >    "+t);
//        System.out.println("required ----->    "+re  );
        re.RVNRtoString();
    }
}
