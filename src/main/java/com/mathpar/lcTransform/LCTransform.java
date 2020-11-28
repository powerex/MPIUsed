/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

// © Разработчик Смирнов Роман Антонович (White_Raven), ТГУ'10
package com.mathpar.lcTransform;

import com.mathpar.func.*;
import java.util.ArrayList;
import com.mathpar.matrix.MatrixS;
import com.mathpar.number.*;
import com.mathpar.polynom.Polynom;


/**
 * Класс осуществляющий прямое преобразование по Лапласу-Карсону
 * @author white_raven (Смирнов Роман)
 * @version 0.4
 */
public class LCTransform {

    Ring ring; //кольцо с добавленными переменными

    public LCTransform() {
    }

    public LCTransform(Ring r, boolean flag_Inint_Cond) {
        ring = r;
        inputRing = r;
        ic_flag = flag_Inint_Cond;
    }
    /**
     * Входное кольцо
     */
    Ring inputRing;
    /**
     * Флаг отвечающий за наличие начальных условий в уравнениях
     */
    boolean ic_flag;

    int ic_index;

    int start_ic_index;

    int func_index;
    /**
     * Список начальных условий для уравнения
     */
    ArrayList<initConditional> initCondditionals=new ArrayList<initConditional>();

    ArrayList<Element> calculateIC = new ArrayList<Element>();

    /**
     * Меняем кольцо на комплексное, и даписываем туда столько же новых полиномов сколько было переменных
     */
    private void changeRingAndMakeNewVariables(int numberFunc, int numberInitCond) {
        StringBuilder res = new StringBuilder(ring.toString());// берем за основу нового кольца - входное
        //res.replace(0, 1, "C");
        ic_index = ring.varNames.length; // устанавливаем место начала полиномов начальных условий в кольце
        start_ic_index = ic_index;
        func_index = ic_index + numberInitCond; //устанавливаем место начала полиномов-исходных функций
        // формируем новое кольцо с учетом numberFunc и numberInitCond
        String s = ",";
        for (int i = 0; i < numberInitCond; i++) {
            s += "_ic$" + i + "_,";
        }
        for (int i = 0; i < numberFunc - 1; i++) {
            s += "_func$" + i + "_,";
        }
        s += "_func$" + (numberFunc - 1) + "_";
        ring = new Ring(res.insert(res.length() - 1, s).toString());
    }

    /**
     * Возвращаем порядок \SysteLDE  и \initCond , и формируем ring
     * @param func
     * @return индекс по которому расположен \SysteLDE
     */
    private int typeSystemLDE_and_makeRing(F func) {
        if (((F) func.X[0]).name == F.SYSTLDE) {
            changeRingAndMakeNewVariables(((F) func.X[0]).X.length / 2, ((F) func.X[1]).X.length / 2);
            return 0;
        }
        changeRingAndMakeNewVariables(((F) func.X[1]).X.length / 2, ((F) func.X[0]).X.length / 2);
        return 1;
    }
//=====================================================================================================
    //=============================  LCTransform for Functions ========================================
//=====================================================================================================

    /**
     * Пока только для  p(x)/c , где p(x) - полином от любых переменных , c - число
     *
     * @param divf - входная функция представляющая собой объект типа F c именем DIVIDE
     * @return преобразованная при помощи LC функция divf
     */
    private Element transform_Divide(F divf) {
        // работаем с числителем
        Element num = transformRightPart(divf.X[0]);
        if (divf.X[1].numbElementType() < Ring.Polynom) {
            Element tempRes = num.divide(divf.X[1], ring);
            return (tempRes instanceof Fraction) ? ((Fraction) tempRes).cancel(ring) : (tempRes instanceof F) ? ((F) tempRes).expand(ring) : tempRes;
        }
        System.err.println(" Пока не реализовано !!!");
        return null;
    }

    /**
     * Метод вычесляющий прямое преобразование Лапласа-Карсона в правой части
     * уравнения вида e^{P(x)}*f(x)*H(Q(x)), где x={x,y,z,...} из R_+
     *
     * @param func - правая часть дифференциального уравнения
     * @return преобразованная по Лапдасу-Карсону правая часть
     */
    public Element transformRightPart(Element func) {
        switch (func.numbElementType()) {

            case Ring.F: // function of the form  ----->  e^{ax+by+c}*f(x,y)*H(f(x,y))
                switch (((F) func).name) {
                    case F.ID:
                        return transformRightPart(((F) func).X[0]);
                    case F.DIVIDE:
                        return transform_Divide((F) func);

                    default:
                        System.err.println(" Пока не реализовано !!!");
                        return null;
                }
            // System.err.println(" Пока не реализовано !!!");
            //  break;

            case Ring.Polynom:// function of the form ------> Sum_{a}(a*x^n*y^m)
                Polynom t = (Polynom) func;
                Element res = Fraction.Z_ZERO;
                int j = 0;
                int len = t.powers.length / t.coeffs.length;
                int coef_index = 0;
                while (j < t.powers.length) {
                    Fraction temp = (t.coeffs[coef_index].isOne(ring)) ? Fraction.Z_ONE : new Fraction(t.coeffs[coef_index], ring);
                    for (int i = j; i < (j + len); i++) {
                        if (t.powers[i] != 0) {
                          Element  tempE = temp.multiply(new Fraction(new NumberZ(factorial(t.powers[i])).toNumber(ring.algebra[0], ring), ring.varPolynom[i - j].pow(t.powers[i], ring)), ring);
                          if (tempE instanceof Fraction)  temp=(Fraction)tempE; else temp=new Fraction(tempE,ring.numberONE);
                        }
                    }
                    coef_index++;
                    j += len;
                    res = res.add(temp, ring);
                }
                return res;
            default:// if number return input element
                return func;
        }
    }

    /**
     * Процедура для вычиления факториала
     *
     * @param n аргумент факториала
     * @return n!
     */
    private int factorial(int n) {
        int res = n;
        while (n > 1) {
            n--;
            res *= n;
        }
        return res == 0 ? 1 : res;
    }

    /**
     * Метод преобразующий левую часть дифферециального уравнения по
     * Лапласу-Карсону
     *
     * @param func - левая часть дифференциального уравнения
     * @return изображающее уравнение
     */
    private Polynom transformLeftPart(Element func) {
        int nmb_type = func.numbElementType();
        if (nmb_type < Ring.Polynom) {
            return new Polynom(func);
        }
        switch (nmb_type) {
            case Ring.F: {
                switch (((F) func).name) {
                    case F.ADD:
                        Element res = transformLeftPart(((F) func).X[0]);
                        for (int i = 1; i < ((F) func).X.length; i++) {
                            res = res.add(transformLeftPart(((F) func).X[i]), ring);
                        }
                        return (Polynom) res;
                    case F.SUBTRACT:
                        return (Polynom) transformLeftPart(((F) func).X[0]).subtract(transformLeftPart(((F) func).X[1]), ring);
                    case F.MULTIPLY:
                        Element res_mul = transformLeftPart(((F) func).X[0]);
                        for (int i = 1; i < ((F) func).X.length; i++) {
                            res_mul = res_mul.multiply(transformLeftPart(((F) func).X[i]), ring);
                        }
                        return (Polynom) res_mul;
                    case F.d:
                        int name_func = searchNameFunc((Fname) ((F) func).X[0]);
                        if (((F) func).X.length == 2) {
                            if (name_func == -1) {
                                calculateFunctions.add(new Fname(((Fname) ((F) func).X[0]).name, new Element[0])); // если еще ее там нет то добавлем
                                return (Polynom) ((F) func).X[1].multiply(ring.varPolynom[func_index + calculateFunctions.size() - 1], ring);
                            } else {
                                return (Polynom) ((F) func).X[1].multiply(ring.varPolynom[func_index + name_func], ring);
                            }
                        } else {
                            if (name_func == -1) {
                                calculateFunctions.add(new Fname(((Fname) ((F) func).X[0]).name, new Element[0])); // если еще ее там нет то добавлем
                                return (Polynom) ((F) func).X[1].pow(((F) func).X[2].intValue(), ring).multiply(ring.varPolynom[func_index + calculateFunctions.size() - 1], ring);
                            } else {
                                return (Polynom) ((F) func).X[1].pow(((F) func).X[2].intValue(), ring).multiply(ring.varPolynom[func_index + name_func], ring);
                            }
                        }
                    default:
                        ring.exception.append(" Uncorrect function in left part ").append(func.toString(ring));
                        return null; // значит пришло что то то что не должно стоять в левой части
                }
            }
            case Ring.Fname:
                int name_func = searchNameFunc((Fname) func);
                return (name_func == -1) ? ring.varPolynom[func_index + calculateFunctions.size() - 1] : ring.varPolynom[func_index + name_func];
            default:
                ring.exception.append(" Uncorrect function in left part ").append(func.toString(ring));
                return null;
        }
    }

    private Polynom mixedReduce(Polynom p, int[] num) {
        Polynom res = reducePolynom(p, num[0]);
        for (int i = 1; i < num.length; i++) {
            res = reducePolynom(res, num[i]);
        }
        return res;
    }

    private Polynom colReduce(Polynom p, int col, int num) {
        Polynom res = reducePolynom(p, num);
        for (int i = 0; i < (col - 1); i++) {
            res = reducePolynom(res, num);
        }
        return res;
    }

    private Polynom reducePolynom(Polynom p, int num) {
        int[] newpow = p.powers.clone();
        Element[] newcoef = p.coeffs.clone();
        int col = p.powers.length / p.coeffs.length;
        int i = num;
        int coefindex = 0;
        while (i < p.powers.length) {
            if (p.powers[i] == 0) {
                newcoef[coefindex] = ring.numberZERO;
            } else {
                if (p.powers[i] != 0) {
                    newpow[i]--;
                }
            }
            coefindex++;
            i += col;
        }
        return new Polynom(newpow, newcoef).ordering(ring);
    }

    /**
     * Вернем номер ко
     * @param p
     * @return
     */
    private int returnNumPolInRing(Polynom p) {
        for (int i = 0; i < p.powers.length; i++) {
            if (p.powers[i] != 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Ищем имя функции в поле calculateFunctions , если нашли то вернем индекс, если нет -1.
     * @param f
     * @return
     */
    private int searchNameFunc(Fname f) {
        if (calculateFunctions.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < calculateFunctions.size(); i++) {
            if (f.compareTo(calculateFunctions.get(i), ring) == 0) {
                return i;
            }
        }
        return -1;
    }

    private Element valueOneIC(Polynom p, F IC, Element znachIC) {
        //Создаем начальное условие  и помещаем его в список начальных условий
        initConditional initCond= new initConditional();
        initCond.ic=IC;
        initCond.ring=ring;
        initCond.variable=ic_index;
        initCond.valueLC=(ic_flag)? transformRightPart(znachIC) : null;
        initCondditionals.add(initCond);
        //--------------------------------------------------------------------
        int i = 2;
        for (; i < IC.X.length; i += 3) {
            if (IC.X[i] != null) {
                break;
            }
        }
        int col_reduce = IC.X[i + 1].intValue();
        Polynom reducePolynom = (col_reduce == 0) ? p
                : colReduce(p, col_reduce, returnNumPolInRing((Polynom) IC.X[i - 1]));
        Element[] res = new Element[ring.varNames.length]; // массив для подстановки в редуцированный полином
        for (int j = 0; j < res.length; j++) {
            res[j] = ring.numberZERO;
        }
        res[func_index + searchNameFunc((Fname) IC.X[0])] = ring.varPolynom[ic_index];
        ic_index++;
        if (ic_flag) {
            calculateIC.add(transformRightPart(znachIC));
        }
        res[returnNumPolInRing((Polynom) IC.X[i - 1])] = IC.X[i - 1];

        return (new com.mathpar.func.FvalOf(ring)).valOfToPolynom(reducePolynom, res);
    }

    /**
     * Подстановка начальных условий в правую часть
     * @param p
     * @param initCond
     * @return
     */
    private Polynom valueInitCond(Polynom p, F initCond) {
        Element res = valueOneIC(p, (F) initCond.X[0], initCond.X[1]);
        for (int i = 2; i < initCond.X.length; i += 2) {
            res = res.add(valueOneIC(p, (F) initCond.X[i], initCond.X[i + 1]), ring);
        }
        return (Polynom) res;
    }


    /**
     * Здесь находяться искомые функции. В конце вычислений на X[0] каждого элемента будут подвешаны результаты.
     */
    ArrayList<Fname> calculateFunctions = new ArrayList<Fname>();

    private Polynom delFuncToResult(Polynom p) {
        int[] newpow = p.powers.clone();
        int num = ring.varNames.length - 1;
        for (int i = num; i < newpow.length; i += (num + 1)) {
            newpow[i] = 0;
        }
        return new Polynom(newpow, p.coeffs).normalNumbVar(ring);
    }

    /**
     * Создаем матрицу из коэффициентов искомых функций, при этом будут уничтожены сам функции по вполне понятным причинам
     * F.e. после преобразования имеем  :
     *  polMas[]=  { x*U , y*V , -z*W }  ,где U,V,W - функции значения которых надо найти.
     *  Результат в виде [] = { x ,  y  ,  -z }
     * @param polMas - массив прямых преобразований по Лапласу-Карсону левых частей диф.-уров
     * @return
     */
    private Element[] makeMatrixElements(Polynom[] polMas) {
        int index = 0;
        Element[] res = new Element[polMas.length];
        for (int i = (polMas.length - 1); i >= 0; i--) {
            int[] pow = polMas[i].powers.clone();
            pow[pow.length - 1] = 0;
            res[index] = (new Polynom(pow, polMas[i].coeffs)).normalNumbVar(ring);
            index++;
        }
        return res;
    }

    private MatrixS makeMatrixLeftParts(Polynom[] V) {
        Element[][] resM = new Element[V.length][];
        for (int i = 0; i < V.length; i++) {
            Polynom[] temp = ring.CForm.dividePolynomToMonoms(V[i]);
            resM[i] = makeMatrixElements(temp).clone();
        }
        return new MatrixS(resM, ring);
    }

    Element[] makeLastCalculatePoint() {
        Element[] res = new Element[func_index];
        int index = 0;
        int j = 0;
        for (int i = 0; i < inputRing.varNames.length; i++) {
            res[i] = ring.varPolynom[i];  // заполняем все p, q, etc...
            index++;
        }

        for (int i = index; i < func_index; i++) {
            res[i] = calculateIC.get(j);
            j++;
        }
        return res;
    }

    void workWith_LC_functions() {
        for (int i = 0; i < calculateFunctions.size(); i++) {
            Fname tf = calculateFunctions.get(i);
            Element res = tf.X[0];
            Element factorNum;
            Element factorDenom;
            if (res instanceof Fraction) {
                factorNum = new FvalOf(ring).valOf(((Fraction) res).num, makeLastCalculatePoint());
                factorDenom = new FvalOf(ring).valOf(((Fraction) res).denom, makeLastCalculatePoint());
                F TempRes = new F(F.DIVIDE, new Element[]{factorNum, factorDenom}).expand(ring);
                calculateFunctions.remove(i);
                calculateFunctions.add(i, new Fname(tf.name, TempRes));
            }
        }
    }

    /**
     * Прямое преобразование Лапласа-Карсона , все результаты в поле  calculateFunctions.
     * @param func - система диф. урав. с частными производными
     */
    public void LC_transform(F func) {
        int num = typeSystemLDE_and_makeRing(func); // узнаем на каком месте нач условия и формируем колечко
        int col_system = ring.varNames.length - func_index; // уравнение или система
        if (col_system == 1) { // значит уравнение
            Polynom LC_leftPart = transformLeftPart((F) ((F) func.X[num]).X[0]);
            Polynom valueInitCond = valueInitCond(LC_leftPart, (F) func.X[1 - num]);
            Element LC_rightPart = transformRightPart(((F) func.X[num]).X[1]).add(valueInitCond, ring); // готовая правая часть
            //      calculateFunctions.get(0).X[0] =
            //      new Fraction(LC_rightPart,delFuncToResult(LC_leftPart));
            //      LC_rightPart.divide();
            calculateFunctions.get(0).X[0] = LC_rightPart.divide(delFuncToResult(LC_leftPart), ring);
            if (ic_flag) {
                workWith_LC_functions();
            }
            sout();
            return;
        }
        // формируем будущую матрицу преобразований
        Polynom[] vecLeftParts = new Polynom[col_system];
        Element[] vecRightParts = new Element[col_system];
        int index = 0;
        for (int i = 0; i < ((F) func.X[num]).X.length; i += 2) {
            vecLeftParts[index] = transformLeftPart((F) ((F) func.X[num]).X[i]);
            Polynom valueInitCond = valueInitCond(vecLeftParts[index], (F) func.X[1 - num]);
            vecRightParts[index] = transformRightPart(((F) func.X[num]).X[i + 1]).add(valueInitCond, ring);
            ic_index = start_ic_index;
            index++;
        }
        //System.out.println(" -->  "+Array.toString(vecRightParts));
        //System.out.println("  <-- " +Array.toString(vecLeftParts));
        //Находим прямое отбражение каждой из функции в системе, в виде дроби
        MatrixS matrixLP = makeMatrixLeftParts(vecLeftParts);
        //     System.out.println("  " + matrixLP.toString(ring));
        //      System.out.println("   " + matrixLP.det(ring));
        matrixLP = matrixLP.inverseInFractions(ring);
        //      System.out.println("   " + matrixLP.toString(ring));
        VectorS pp = matrixLP.multiplyByColumn(new VectorS(vecRightParts), ring);
        //System.out.println("  " + pp.toString(ring)+"\n");
        // Заносим все результаты в вектор решений , на этом собственно все,
        //дальше разбиение на обычные дроби и обратное интегрирование
        for (int i = 0; i < pp.V.length; i++) {
            calculateFunctions.get(i).X[0] = ((Fraction) pp.V[i]).cancel(ring);
        }
        if (ic_flag) {
            workWith_LC_functions();
        }
        sout();
    }
/**
 * Вывод в строку
 */
    void sout() {
        if (ic_flag) {
            System.out.println("После прямого преобразования и подстановки начальных условий");
        } else {
            System.out.println("После прямого преобразования ");
        }
        for (int i = 0; i < calculateFunctions.size(); i++) {
            System.out.println(calculateFunctions.get(i).name + " = " + calculateFunctions.get(i).toString(ring));
        }
        System.out.println("\n");
    }

    public static void main(String[] args) {
        Ring r = new Ring("Z[x,y]");
        F sys = new F("\\systLDE(\\d(f,x,2)-\\d(f,y)=xy)", r);
      //  F sysk=new F("\\systLDE(\\d(f,x)+\\d(g,y)=x,\\d(f,y)+\\d(g,x)=y)",r);
     //   s.make_Init_Cond(sys);



     //   System.out.println(sys.toString(r)); // b= -x^4/24 or b= -y^2/2;
        F initcond = new F("\\initCond(\\ic(f,x,0,0,y,,0)=1,\\ic(f,x,0,1,y,,0)=-y^2/2 , \\ic(f,x, ,0,y,0,0)=1)", r);

        F LC1 = new F(F.SOLVEPDE, new Element[]{sys, initcond});





// ------- тест на прямое преобразование по Лапласу-Карсону уравнение
       LCTransform LC = new LCTransform(r, true);
//        System.out.println(" "+LC.factorial(15));
//
//        F sys = new F("\\systLDE(\\d(f,x,2)-\\d(f,y)=xy)", r);
//        System.out.println(sys.toString(r));
//        Polynom pol = new Polynom("-x^2y", r);
//        F pol2 = new F("-y^2/2", r);
//        Element a = new F("\\ic(f,x,0,0,y,,0)", r);
//        Element b = new F("\\ic(f,x,0,1,y,,0)", r);
//        Element c = new F("\\ic(f,x,,0,y,0,0)", r);
//        F initcond = new F(F.INITCOND, new Element[]{a, r.numberONE, b, pol2, c, r.numberONE});
//        System.out.println("" + initcond);
//        // F initcond= new F("\\initCond(\\ic(f,x,0,0,y,,0)=1, \\ic(f,x,0,1,y,,0)=-x^2y , \\ic(f,x,,0,y,0,0)=1)",r);
//        F LC1 = new F(F.SOLVELDE, new Element[]{sys, initcond});
        LC.LC_transform(LC1);

        ////                   ------- тест на прямое преобразование по Лапласу-Карсону система
//               LCTransform LC_= new LCTransform(r,true);
//               F sys1=new F("\\systLDE(\\d(f,x)+\\d(g,y)=x,\\d(f,y)+\\d(g,x)=y)",r);
//               System.out.println(sys1.toString(r));
//               F initcond1= new F("\\initCond(\\d(f,x,0,0)=0,\\d(g,x,0,0)=2y,\\d(f,y,0,0)=0,\\d(g,y,0,0)=2x)",r);
//               F LC1_= new F(F.SOLVELDE,new Element[]{sys1,initcond1});
//               LC_.LC_transform(LC1_);

//               LCTransform LC__= new LCTransform(r);
//               F sys_ = new F("\\systLDE(\\d(f,x,2)-4*f=4x)", r);
//               System.out.println(sys_.toString(r));
//               F initcond_ = new F("\\initCond(\\d(f,x,0,0)=1,\\d(f,x,0,1)=0)", r);
//               F Lcfunc = new F(F.SOLVELDE, new Element[]{sys_, initcond_});
//               LC__.LC_transform(Lcfunc);

//               LCTransform LC1= new LCTransform(r);
//               F sys1 = new F("\\systLDE(\\d(f,x,3)+3\\d(f,x,2)+3\\d(f,x)+f=1)", r);
//               System.out.println(sys1.toString(r));
//               F initcond1 = new F("\\initCond(\\d(f,x,0,0)=0,\\d(f,x,0,1)=0,\\d(f,x,0,2)=0)", r);
//               F Lcfun1 = new F(F.SOLVELDE, new Element[]{sys1, initcond1});
//               LC1.LC_transform(Lcfun1);

//               LCTransform LC2= new LCTransform(r);
//               F sys2 = new F("\\systLDE(\\d(f,x,2)+\\d(f,x)-12f=3)", r);
//               System.out.println(sys2.toString(r));
//               F initcond2 = new F("\\initCond(\\d(f,x,0,0)=1,\\d(f,x,0,1)=0)", r);
//               F Lcfun2 = new F(F.SOLVELDE, new Element[]{sys2, initcond2});
//               LC2.LC_transform(Lcfun2);
//
//               LCTransform LC3= new LCTransform(r);
//               F sys3 = new F("\\systLDE(\\d(f,x,2)-4\\d(f,x)+5f=0)", r);
//               System.out.println(sys3.toString(r));
//               F initcond3 = new F("\\initCond(\\d(f,x,0,0)=0,\\d(f,x,0,1)=1)", r);
//               F Lcfun3 = new F(F.SOLVELDE, new Element[]{sys3, initcond3});
//               LC3.LC_transform(Lcfun3);
//
//               LCTransform LC4= new LCTransform(r);
//               F sys4 = new F("\\systLDE(\\d(f,x)-2f=0)", r);
//               System.out.println(sys4.toString(r));
//               F initcond4 = new F("\\initCond(\\d(f,x,0,0)=1)", r);
//               F Lcfun4 = new F(F.SOLVELDE, new Element[]{sys4, initcond4});
//               LC4.LC_transform(Lcfun4);
//
//               LCTransform LC5= new LCTransform(r);
//               F sys5 = new F("\\systLDE(\\d(f,x,2)-9f=2-x)", r);
//               System.out.println(sys5.toString(r));
//               F initcond5 = new F("\\initCond(\\d(f,x,0,0)=0,\\d(f,x,0,1)=1)", r);
//               F Lcfun5 = new F(F.SOLVELDE, new Element[]{sys5, initcond5});
//               LC5.LC_transform(Lcfun5);




//               F d=new F("2y",r);
//               System.out.println(d+"  " + LC.transformRightPart(d));


        //int y=LC.typeSystemLDE_and_makeRing(LC1);
        //  System.out.println(LC.calculateFunctions.get(0));


    }
}
