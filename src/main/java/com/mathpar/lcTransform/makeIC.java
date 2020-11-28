/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

// © Разработчик Смирнов Роман Антонович (White_Raven), ТГУ'10
package com.mathpar.lcTransform;
import com.mathpar.number.*;
import com.mathpar.polynom.Polynom;
import com.mathpar.func.*;
import java.util.ArrayList;
/**
 * Класс создающий начальные условия для систем дифференциальных уравнений в частных производных.
 * @author Smirnov Roman.
 * @version 0.3
 */
public class makeIC {

    Ring ring=null;  // входное кольцо
    int col_num=0;

    public makeIC(Ring r) { // коструктор
        ring = r;
        col_num=ring.varPolynom.length;
    }

    /**
     * Главная процедура, генерируящая конструкции начальных условий.
     * @param systPDE - система диф.ур.
     * @return
     */
    public F generateInitCond(Element[] systPDE) {
        ArrayList<Fname> funcs = new ArrayList<Fname>();// функции из диф. ур.
        ArrayList<int[]> partial = new ArrayList<int[]>(); // соответствующие функциям частные производные
        for (int i = 0; i < systPDE.length; i += 2) { // пробегаем по левым частям
            fill_partialArrays((F) systPDE[i], funcs, partial);
        }
        //строим соответствующие начальные условия исходя из  funcs , partial
        return new F(F.INITCOND, createInitConditional(funcs, partial));
    }
  /** Метод заполняющий массивы funcs и partial в соответствии с левой частью уравнения из системы
     * диф. ур. ПРИМЕР:
     * \\d(f,x,2)-\\d(f,y) --> funcs = {f}, partial={2,1}
     * @param leftPart - левая часть диф. ур.
     * @param funcs - функции из диф. ур.
     * @param partial - соответствующие функциям частные производные.
     */
    private void fill_partialArrays(F leftPart, ArrayList<Fname> funcs, ArrayList<int[]> partial) {
        for (int i = 0; i < leftPart.X.length; i++) {
            if (leftPart.X[i] instanceof F) { // пробегаем по функции-дереву leftPart
                fill_partialArrays((F) leftPart.X[i], funcs, partial);
            }
        }
        if (leftPart.name == F.d) { // если нашли производную
            int indexF = funcs.indexOf(leftPart.X[0]);// ищем ее в funcs
            if (indexF == -1) { // если нет, то добавляем
                funcs.add((Fname) leftPart.X[0]);
                int[] dif = new int[col_num];// строим массив частных производных
                int variable = ((Polynom) leftPart.X[1]).powers.length - 1;
                if (leftPart.X.length == 2) {// случай \d(f,x)
                    dif[variable] = 1;
                } else { //случай \d(f,x,2)
                    dif[variable] = leftPart.X[2].intValue();
                }
                partial.add(dif);
            } else {
                int[] dif = partial.get(indexF);
                int variable = ((Polynom) leftPart.X[1]).powers.length - 1;
                if (leftPart.X.length == 2) {
                    if (dif[variable] < 1) {
                        dif[variable] = 1;
                    }
                } else {
                    int num_partial = leftPart.X[2].intValue();
                    if (dif[variable] < num_partial) {
                        dif[variable] = num_partial;
                    }
                }
                partial.set(indexF, dif);
            }
        }
    }
    /**
     * Метод строющий одно ачальное условие.
     * @param f - функция, начальное условие которой строится
     * @param num_partial - порядок производной
     * @param variable -  переменная по которой происходит дефферинциирование.
     * @return
     */
    private F makeOneIC(Fname f, int num_partial, int variable) {
        Element[] result = new Element[col_num * 3 + 1];
        result[0] = f;
        for (int i = 1; i < result.length; i += 3) {
            if ((i - 1) / 3 == variable) {
                result[i] = ring.varPolynom[variable];
                result[i + 1] = ring.numberZERO;
                result[i + 2] = ring.numberONE.valOf(num_partial, ring);
            } else {
                result[i] = ring.varPolynom[(i - 1) / 3];
                result[i + 1] = null;
                result[i + 2] = ring.numberZERO;
            }
        }
        return new F(F.IC, result);
    }

    /**
     * Метод создающий массив конструкций необходимых нам начальных условий.
     * @param funcs - функции из диф. ур.
     * @param partial - соответствующие функциям частные производные.
     * @return массив всех начальных условий
     */
    private Element[] createInitConditional(ArrayList<Fname> funcs, ArrayList<int[]> partial) {
        ArrayList<Element> result = new ArrayList<Element>();
        for (int j = 0; j < funcs.size(); j++) {
            int[] ic = partial.get(j);
            int flag = 0;
            for (int i = 0; i < ic.length; i++) {
                flag = ic[i] - 1;
                while (flag >= 0) {
                    result.add(makeOneIC(funcs.get(j), flag, i));
                    result.add(null);
                    flag--;
                }
            }
        }
        return result.toArray(new Element[result.size()]);
    }

    public static void main(String[] args) {
        Ring r=new Ring("Z[x,y]");
        F sys = new F("\\systLDE(\\d(f,x,2)-\\d(f,y)=xy)", r);
        makeIC ic=new makeIC(r);
        F q=ic.generateInitCond(sys.X);
        System.out.println(" "+q.toString(r));
    }


}
