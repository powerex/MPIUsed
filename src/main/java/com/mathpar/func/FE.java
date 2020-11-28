/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import com.mathpar.number.*;
import com.mathpar.polynom.*;

/**
 *
 * @author Ribakov Mixail
 */
public class FE extends F {

    /**
     *
     */
    public FE() {
        super();
    }

    private FE(Element element) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     *
     * @param F
     * @return
     */
    public FE FE(F F) {
        FE tt = new FE();
        tt.name = F.name;
        tt.X = F.X;
        return tt;

    }

    /**
     *
     * @param f
     */
    public FE(F f) {
        this.name = f.name;
        this.X = f.X;
        int argNumb = f.X.length;
        for (int i = 0; i < argNumb; i++) {
            FE tfe = new FE(X[i]);
            X[i] = tfe;
        }
    }

    public FE(int name, Element Y) {
        this.name = name;
        this.X = new Element[]{Y};
    }

    public FE(int name, Element[] X) {
        this.name = name;
        this.X = X;
    }

    public FE(int name, F f) {
        this.name = name;
        this.X = new F[]{f};
    }

    public FE(Polynom pol) {
        this.name = ID;
        this.X = new Element[]{pol};
    }

    /**
     *
     * @param el
     * @return
     */
    public FE FfromNumber(Element el) {
        return new FE(0, new Polynom(new int[0], new Element[]{el}));
    }

    /**
     * Возвращает "имя" функции(по её номеру)
     * @param FunctionNumber int
     * @return String
     * @author
     */
    public static String GetTFName(int FunctionNumber) {
        String Result = null;
        switch (FunctionNumber) {
            case 1:
                Result = "abs";
                break;
            case 2:
                Result = "ln";
                break;
            case 3:
                Result = "lg";
                break;
            case 4:
                Result = "exp";
                break;
            case 5:
                Result = "sqrt";
                break;
            case 6:
                Result = "sin";
                break;
            case 7:
                Result = "cos";
                break;
            case 8:
                Result = "tg";
                break;
            case 9:
                Result = "ctg";
                break;
            case 10:
                Result = "arcsin";
                break;
            case 11:
                Result = "arccos";
                break;
            case 12:
                Result = "arctg";
                break;
            case 13:
                Result = "arcctg";
                break;
            case 14:
                Result = "sh";
                break;
            case 15:
                Result = "ch";
                break;
            case 16:
                Result = "tgh";
                break;
            case 17:
                Result = "ctgh";
                break;
            case 18:
                Result = "arcsh";
                break;
            case 19:
                Result = "arcch";
                break;
            case 20:
                Result = "arctgh";
                break;
            case 21:
                Result = "arcctgh";
                break;
            case 22:
                Result = "unitStep";
                break;
            case 23:
                Result = "fact";
                break;
            case 24:
                Result = "Gamma";
                break;
            case 27:
                Result = "sc";
                break;
            case 28:
                Result = "csc";
                break;
            case 50:
                Result = "log";
                break;
            case 51:
                Result = "pow";
                break;
        }
        return Result;
    }

    /**
     * Возвращает кол-во аргументов (строк) функции.
     * @param funcNum int номер функции
     * @return int
     */
    public static int getNumOfArgs(int funcNum) {
        if (funcNum < 50) {
            return 1;
        } else if (funcNum < 60) {
            return 2;
        } else {
            throw new RuntimeException("Error: Illegal function number: " +
                    funcNum);
        }
    }

    ///////////////////////////@author Пашин.Д 35 группа//////////////////////////
    /**
     * проверяет являеться ли функция простой
     * возвращает true если функция простая, false если сложная
     */
   /* public boolean isITSimple() {
        boolean S = true;
        int argNumb = X.length;
        for (int i = 0; i < argNumb; i++) {
            if ((X.length == 0) ||
                    (X.length == 1 &&
                    ((X[0]..length == 0) &&
                    ((((Polynom) X[0]).powers.length == 0) || (((Polynom) X[0].) == null))))) {
                S = true;
            } else {
                S = false;
                break;
            }
        }
        return S;
    }*/
}
