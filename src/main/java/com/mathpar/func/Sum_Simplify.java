/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

//import com.sun.org.apache.bcel.internal.generic.F2D;
//import com.sun.org.apache.xpath.internal.operations.Minus;
//import number.Element;
//import polynom.*;
import com.mathpar.number.*;

/**
 *
 * @author student Bobkov
 */
/**
Данный класс позволяет упрощать некоторые биномиальные формулы.
На входе имеем выражение, содержащее биномиальные коэффициенты;
 *
создаем функцию F, применяем к ней метод Transform и получаем результат.
Результат зависит от того, совпадает ли данное выражение на входе с одним из 6 методов:
если совпадает, то программа выводит результат - упрощение биномиального выражения;
если нет, то программа выводит выражение, поданное на входе.*/

/**
Для упрощения некоторых выражений по формулам:
 * \begin{equation}
 \label{1}
\sum_{k=0}^{r}\left(\begin{array}{c}
                r\\
                k+m\\
                        \end{array}\right)\left(\begin{array}{c}
                s\\
                n+k\\
                        \end{array}\right) = \left(\begin{array}{c}
                r+s\\
                r-m+n\\
                        \end{array}\right);
\end{equation}
\begin{equation}
\sum_{k=0}^{r}\left(\begin{array}{c}
                r\\
                k\\
                        \end{array}\right)\left(\begin{array}{c}
                s+k\\
                n\\
                        \end{array}\right)(-1)^{r-k} = \left(\begin{array}{c}
                s\\
                n-r\\
                        \end{array}\right);
\end{equation}
\begin{equation}
\sum_{k=0}^{r}\left(\begin{array}{c}
                r-k\\
                m\\
                        \end{array}\right)\left(\begin{array}{c}
                s\\
                k-t\\
                        \end{array}\right)(-1)^{k-t} = \left(\begin{array}{c}
                r-t-s\\
                r-t-m\\
                        \end{array}\right);
\end{equation}
\begin{equation}
\sum_{k=0}^{r}\left(\begin{array}{c}
                r-k\\
                m\\
                        \end{array}\right)\left(\begin{array}{c}
                s+k\\
                n\\
                        \end{array}\right) = \left(\begin{array}{c}
                r+s+1\\
                m+n+1\\
                        \end{array}\right);
\end{equation}
\begin{equation}
\sum_{k=0}^{r}\left(\begin{array}{c}
                r-t*k\\
                k\\
                        \end{array}\right)\left(\begin{array}{c}
                s-t*(n-k)\\
                n-k\\
                        \end{array}\right)\frac{r}{r-t*k} = \left(\begin{array}{c}
                r+s-t*n\\
                n\\
                        \end{array}\right);
\end{equation}
\begin{equation}
 \label{6}
\sum_{k=0}^{n}\left(\begin{array}{c}
                n\\
                k\\
                        \end{array}\right)a^{n-k}b^k = (a+b)^n.
\end{equation}
 * необходимо вызвать метод Transform,
 *
 *
        Ring ring = new Ring("R64[x,y]");
        Element one = ring.numberONE;
        //1 формула
        String qwerty = "\\sum_{k=0}^{r}(\\binomial(r,m+k)\\binomial(s,n+k))";
        F sss = new F(qwerty, ring);
        System.out.println("input1     " + sss);
        sss = Transform(sss, ring);
        System.out.println("res1     " + sss);

        //4 формула
        String qwertys = "\\sum_{k=0}^{r}(\\binomial(r-k,m)\\binomial(s+k,n))";
        F ss = new F(qwertys, ring);
        System.out.println("input4     " + ss);
        ss = Transform(ss, ring);
        System.out.println("res4     " + ss);


        //2 формула
        String qwertyq = "(\\sum_{k=0}^{r}(\\binomial(r,k)\\binomial(s+k,n)(-1)^(r-k))";
        F ssq = new F(qwertyq, ring);
        System.out.println("input2     " + ssq);
        CanonicForms qqq = new CanonicForms(ring);
        ssq.cleanOfRepeating(ring);
        qqq.InputForm(ssq, true, true, true, true);

        ssq = Transform(ssq, ring);
        System.out.println("res2     " + ssq);


        //3 формула
        String qwertyw = "\\sum_{k=0}^{r}(\\binomial(r-k,m)\\binomial(s,k-t)(-1)^(k-t))";
        F ssw = new F(qwertyw, ring);
        System.out.println("input3     " + ssw);
        ssw = Transform(ssw, ring);
        System.out.println("res3     " + ssw);

        //5 формула
        String qwertyt = "\\sum_{k=0}^{r}(\\binomial(r-t*k ,k)\\binomial(s-t*(n-k),n-k)(r/(r-t*k)))";
        F ssf = new F(qwertyt, ring);
        System.out.println("input5     " + ssf);
        ssf = Transform(ssf, ring);
        System.out.println("res5     " + ssf);

        //6 формула
        String qwertyy = "\\sum_{k=0}^{n}(\\binomial(n,k)a^(n-k)b^k)";
        F ssd = new F(qwertyy, ring);
        System.out.println("input6     " + ssd);
        ssd = Transform(ssd, ring);
        System.out.println("res6     " + ssd);
 *
 */

public class Sum_Simplify {

    public Sum_Simplify() {
    }


    /**
     * @param args the command line arguments
     */
    /**В функции h осуществляем поиск по дереву ф-ции с именем n, num-ым аргументом которой
    является element к. Выводит true если найдено и false в противном случае.
     * @param F h данная функция
     * @param Element k искомый элемент
     * @param int num номер аргумента
     * @param int n номер функции в h
     * @return true,если найдено, false в противном случае.
     */
    public  boolean FindTree(F h, Element k, int num, int n, Ring ring) {
        int l = h.X.length;
        boolean p = false;
        for (int i = 0; i < l; i++) {
            Element a = h.X[i];
            if (a instanceof F) {
                if ((((F) a).name) == n
                        && (!((((F) a).X[num]) instanceof F))
                        && ((((F) a).X[num]).equals(k, ring))) {
                    p = true;
                    break;
                }

            }
        }


        return p;
    }

    /**В функции h осуществляем поиск по дереву ф-ции с именем n, num-ым аргументом которой
    является ф-ция с именем к.Выводит true если найдено и false в противном случае.
     * @param F h данная функция
     * @param Element k искомый элемент
     * @param int num
     * @param int n номер функции в h
     * @return true,если найдено, false в противном случае.
     */
    public  boolean FindTree(F h, int k, int num, int n, Ring ring) {
        int l = h.X.length;
        boolean p = false;
        for (int i = 0; i < l; i++) {
            Element a = h.X[i];
            if (a instanceof F) {
                if ((((F) a).name) == n && ((F) a).X[num] instanceof F
                        && ((F) (((F) a).X[num])).name == k) {
                    p = true;
                    break;
                }
            }
        }
        return p;
    }

    /**
    Данный метод позволяет свободно перемещаться по всем элементам дерева,
    выбирая путь, в зависимости от разновидности поданного на вход выражения.
    Выводит упрощенный результат, если выражение подходит под один из шести методов,
    или начальное выражение. Параллельно с этим на экран выводится дерево функции
    и печается результат.
     * Формулы:
     * st1: \\sum_{k=0}^{r}(\\binomial(r,k+m)\\binomial(s,k+n)) = binomial(r+s, r-m+n);
     * st2: \\sum_{k=0}^{r}(\\binomial(r,k)\\binomial(s+k,n)(-1)^(r-k)) = binomial(s, n-r);
     * st3: \\sum_{k=0}^{r}(\\binomial(r-k,m)\\binomial(s,k-t)\\m_one^(k-t)) = binomial(r-t-s, r-t-m);
     * st4: \\sum_{k=0}^{r}(\\binomial(r-k,m)\\binomial(s+k,n)) = binomial(r+s+1, m+n+1);
     * st5: \\sum_{k=0}^{r}(\\binomial(r-t*k,k)\\binomial(s-t*(n-k),n-k)(r/(r-t*k))) = binomial(r+s-t*n, n);
     * st6: \\sum_{k=0}^{n}(\\binomial(n,k)\\a^(n-k)\\b^k) = (a+b)^n;
     * @param F h данная функция
     * @param Ring ring кольцо
     * @return F - преобразованная функция
     */
    public  F Transform(F f, Ring ring) {

        f.cleanOfRepeatingWithNewVectors(ring);
        /** выводит на один уровень однотипные арифметические операции*/
        CanonicForms cf =  ring.CForm;
        F fx = (F) f.X[0];
        Element ret = ((F) fx.X[0]).X[0];
        Fname r21 = (Fname) f.X[3];
        Fname k21 = (Fname) f.X[1];
        if (FindTree(fx, k21, 1, 51, ring)) {            //2,5,6
            if (FindTree(fx, ring.numberMINUS_ONE, 0, 53, ring)) {
                return ST2(f, ring);// st2
            } else {// st5 && st6
                if (ret instanceof Fname && FindTree(fx, ret, 0, 51, ring)) {
                    return ST6(f, ring);//st6
                } else {
                    if (ret instanceof F && (((F) ret).X[0]).equals(r21, ring)) {
                        return ST5(f, ring); ////// st 5
                    } else {
                        return f;
                    }
                }
            }
        } else {                       // st1 & st3 & st4

            if (FindTree(fx, ring.numberMINUS_ONE, 0, 53, ring)) {
                return ST3(f, ring);    // st3
            } else {
                if ((FindTree(fx, r21, 0, 51, ring))) {

                    return ST1(f, ring);          //st1
                } else {
                    if (FindTree(fx, 92, 0, 51, ring)) {

                        if (FindTree(((F) ((F) f.X[0]).X[0]), k21, 1, 92, ring)) {
                            if (FindTree(((F) ((F) f.X[0]).X[1]), k21, 1, 96, ring)) {
                                return ST4(f, ring);
                            }
                        }
                    }
                    return f;          ///st4
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {

      Sum_Simplify qwe=new Sum_Simplify();
        Ring ring = new Ring("R64[x,y]");

        Element one = ring.numberONE;
        /**1 формула*/
        String qwerty = "\\sum_{k=0}^{r}(\\binomial(r,m+k)\\binomial(s,n+k))";
        F sss = new F(qwerty, ring);
        System.out.println("input1     " + sss);
        sss = qwe.Transform(sss, ring);
        System.out.println("res1     " + sss);

        /**4 формула*/
        String qwertys = "\\sum_{k=0}^{r}(\\binomial(r-k,m)\\binomial(s+k,n))";
        F ss = new F(qwertys, ring);
        System.out.println("input4     " + ss);
        ss = qwe.Transform(ss, ring);
        System.out.println("res4     " + ss);


        /**2 формула*/
        String qwertyq = "(\\sum_{k=0}^{r}(\\binomial(r,k)\\binomial(s+k,n)(-1)^(r-k))";
        F ssq = new F(qwertyq, ring);
        System.out.println("input2     " + ssq);
      //  CanonicForms qqq = new CanonicForms(ring);
        ssq.cleanOfRepeatingWithNewVectors(ring); CanonicForms qqq=ring.CForm;;
       // qqq.InputForm(ssq, true, true, true, true);

        ssq = qwe.Transform(ssq, ring);
        System.out.println("res2     " + ssq);


        /**3 формула*/
        String qwertyw = "\\sum_{k=0}^{r}(\\binomial(r-k,m)\\binomial(s,k-t)(-1)^(k-t))";
        F ssw = new F(qwertyw, ring);
        System.out.println("input3     " + ssw);
        ssw = qwe.Transform(ssw, ring);
        System.out.println("res3     " + ssw);

        /**5 */
        String qwertyt = "\\sum_{k=0}^{r}(\\binomial(r-t*k ,k)\\binomial(s-t*(n-k),n-k)(r/(r-t*k)))";
        F ssf = new F(qwertyt, ring);
        System.out.println("input5     " + ssf);
        ssf = qwe.Transform(ssf, ring);
        System.out.println("res5     " + ssf);

        /**6 формула*/
        String qwertyy = "\\sum_{k=0}^{n}(\\binomial(n,k)a^(n-k)b^k)";
        F ssd = new F(qwertyy, ring);
        System.out.println("input6     " + ssd);
        ssd = qwe.Transform(ssd, ring);
        System.out.println("res6     " + ssd);



    }

    /**
    Описанные ниже методы позволяют упрощать некоторые выражения
    с биномиальными коэффициентами:
     * st1: \\sum_{k=0}^{r}(\\binomial(r,k+m)\\binomial(s,k+n)) = binomial(r+s, r-m+n);
     * st2: \\sum_{k=0}^{r}(\\binomial(r,k)\\binomial(s+k,n)(-1)^(r-k)) = binomial(s, n-r);
     * st3: \\sum_{k=0}^{r}(\\binomial(r-k,m)\\binomial(s,k-t)\\m_one^(k-t)) = binomial(r-t-s, r-t-m);
     * st4: \\sum_{k=0}^{r}(\\binomial(r-k,m)\\binomial(s+k,n)) = binomial(r+s+1, m+n+1);
     * st5: \\sum_{k=0}^{r}(\\binomial(r-t*k,k)\\binomial(s-t*(n-k),n-k)(r/(r-t*k))) = binomial(r+s-t*n, n);
     * st6: \\sum_{k=0}^{n}(\\binomial(n,k)\\a^(n-k)\\b^k) = (a+b)^n;
     * В каждом дереве находим нужные нам элементы,
       потом с их помощью выводим результат.Все 6 методов объединены в общий метод Transform.
     * @param F h данная функцияs
     * @param Ring ring кольцо
     * @param F f функция
     * @return F - преобразованная функция.*/

    /**st1: \\sum_{k=0}^{r}(\\binomial(r,k+m)\\binomial(s,k+n)) = binomial(r+s, r-m+n);*/

    public  F ST1(F f, Ring ring) {
        f.cleanOfRepeatingWithNewVectors(ring);
        F fx = (F) f.X[0];
        F result1 = f;
        Fname k = (Fname) f.X[1];
        Fname r1 = (Fname) f.X[3];
        Fname r = ((Fname) ((F) ((F) f.X[0]).X[0]).X[0]);
        Fname s = (Fname) ((Fname) ((F) ((F) f.X[0]).X[1]).X[0]);
        Fname m1;
        if (((F) ((F) f.X[0]).X[0]).X[1] instanceof F) {
            m1 = (Fname) ((Fname) ((F) ((F) ((F) f.X[0]).X[0]).X[1]).X[0]);
        } else {
            return f;
        }
        Fname n1 = (Fname) ((Fname) ((F) ((F) ((F) f.X[0]).X[1]).X[1]).X[0]);
        if (((F) ((F) fx.X[0]).X[1]).X[1].equals(k) && k.equals(((F) ((F) fx.X[1]).X[1]).X[1])) {
            F re1 = new F(F.ADD, new Element[]{r, s});
            F ree1 = new F(F.ADD, new Element[]{r, new F(F.SUBTRACT, new Element[]{n1, m1})});
            result1 = new F(F.BINOMIAL, new Element[]{re1, ree1});
        }
        return result1;
    }

    /** st4: \\sum_{k=0}^{r}(\\binomial(r-k,m)\\binomial(s+k,n)) = binomial(r+s+1, m+n+1);*/

    public  F ST4(F f, Ring ring) {
        f.cleanOfRepeatingWithNewVectors(ring);
        ring.setDefaultRing();
        F fx = (F) f.X[0];
        F result4 = f;
        Element one = ring.numberONE;
        f.cleanOfRepeatingWithNewVectors(ring);
        Fname r = (Fname) f.X[3];
        Fname s = (Fname) ((Fname) ((F) ((F) ((F) f.X[0]).X[1]).X[0]).X[0]);
        Fname m = (Fname) ((Fname) ((F) ((F) f.X[0]).X[0]).X[1]);
        Fname n = (Fname) ((Fname) ((F) ((F) f.X[0]).X[1]).X[1]);
        F re4 = new F(F.ADD, new Element[]{r, s, one});
        if (((F) ((F) fx.X[0]).X[0]).X[0].equals(r)) {
            F ree4 = new F(F.ADD, new Element[]{m, n, one});
            result4 = new F(F.BINOMIAL, new Element[]{re4, ree4});
        }
        return result4;
    }

    /**st2: \\sum_{k=0}^{r}(\\binomial(r,k)\\binomial(s+k,n)(-1)^(r-k)) = binomial(s, n-r);*/

    public  F ST2(F f, Ring ring) {
        F fx = (F) f.X[0];
        F result2 = f;
        Fname k = (Fname) f.X[1];
        Fname s = (Fname) ((Fname) ((F) ((F) ((F) f.X[0]).X[1]).X[0]).X[0]);
        Fname n = (Fname) ((Fname) ((F) ((F) f.X[0]).X[1]).X[1]);
        Fname r = (Fname) f.X[3];
        if (((F) fx.X[0]).X[0] == r && ((F) fx.X[0]).X[0] == ((F) ((F) fx.X[2]).X[1]).X[0]) {
            F re2 = new F(F.SUBTRACT, new Element[]{n, r});
            result2 = new F(F.BINOMIAL, new Element[]{s, re2});
        }
        return result2;
    }

    /**st3: \\sum_{k=0}^{r}(\\binomial(r-k,m)\\binomial(s,k-t)\\m_one^(k-t)) = binomial(r-t-s, r-t-m);*/

    public  F ST3(F f, Ring ring) {
        F fx = (F) f.X[0];
        fx.cleanOfRepeatingWithNewVectors(ring);
        F result3 = f;
        Fname m = ((Fname) ((F) ((F) f.X[0]).X[0]).X[1]);
        Fname r = (Fname) f.X[3];
        Fname k = (Fname) f.X[1];
        Fname s;
        if (((F) ((F) f.X[0]).X[1]).X[0] instanceof Fname) {
            s = ((Fname) ((F) ((F) f.X[0]).X[1]).X[0]);
        } else {
            return f;
        }
        Fname t = (Fname) ((Fname) ((F) ((F) ((F) f.X[0]).X[2]).X[1]).X[1]);
        F ret3 = new F(F.SUBTRACT, new Element[]{r, k});
        F qq1 = new F(F.SUBTRACT, new Element[]{(((F) fx.X[0]).X[0]), ret3});
        qq1.cleanOfRepeatingWithNewVectors(ring);
        if (qq1.X[0] == qq1.X[1]) {
            F ret32 = new F(F.SUBTRACT, new Element[]{k, t});
            if ((((F) ((F) fx.X[1]).X[1]).equals(ret32, ring)) && (((F) ((F) fx.X[2]).X[1]).equals(ret32, ring))) {
                F re3 = new F(F.SUBTRACT, new Element[]{r, t, s});
                F ree3 = new F(F.SUBTRACT, new Element[]{r, t, m});
                result3 = new F(F.BINOMIAL, new Element[]{re3, ree3});
            }
        }
        return result3;
    }

    /**st5: \\sum_{k=0}^{r}(\\binomial(r-t*k,k)\\binomial(s-t*(n-k),n-k)(r/(r-t*k))) = binomial(r+s-t*n, n);*/

    public  F ST5(F f, Ring ring) {
        f.cleanOfRepeatingWithNewVectors(ring);
        F fx = (F) f.X[0];
        F result5 = f;
        Fname k5 = (Fname) f.X[1];
        Fname k = ((Fname) ((F) ((F) f.X[0]).X[0]).X[1]);
        Fname r = (Fname) ((Fname) ((F) ((F) ((F) f.X[0]).X[0]).X[0]).X[0]);
        Fname t = (Fname) ((Fname) ((F) ((F) ((F) ((F) f.X[0]).X[1]).X[0]).X[1]).X[0]);
        Fname n = (Fname) ((Fname) ((F) ((F) ((F) ((F) f.X[0]).X[1]).X[0]).X[1]).X[1]);
        Fname s = (Fname) ((Fname) ((F) ((F) ((F) ((F) f.X[0]).X[1]).X[0]).X[0]).X[1]);
        if (((F) fx.X[0]).X[0].equals(((F) fx.X[2]).X[1]) && ((F) ((F) fx.X[1]).X[1]).X[0].equals((((F) ((F) ((F) fx.X[1]).X[0]).X[1]).X[1])) && ((F) ((F) fx.X[1]).X[1]).X[1].equals(((F) ((F) ((F) ((F) fx.X[1]).X[0]).X[0]).X[0]).X[1])) {
            F re5 = new F(F.MULTIPLY, new Element[]{t, n});
            F ree5 = new F(F.ADD, new Element[]{r, s});
            F reee5 = new F(F.SUBTRACT, new Element[]{ree5, re5});
            result5 = new F(F.BINOMIAL, new Element[]{reee5, n});
        }
        return result5;

    }

    /**st6: \\sum_{k=0}^{n}(\\binomial(n,k)\\a^(n-k)\\b^k) = (a+b)^n;*/

    public  F ST6(F f, Ring ring) {

        f.cleanOfRepeatingWithNewVectors(ring);
        F fx = (F) f.X[0];
        F result6 = f;
        Fname n = (Fname) f.X[3];
        Fname k = (Fname) f.X[1];
        Fname k6 = ((Fname) ((F) ((F) f.X[0]).X[0]).X[1]);
        Fname n6 = ((Fname) ((F) ((F) f.X[0]).X[0]).X[0]);
        Fname a = ((Fname) ((F) ((F) f.X[0]).X[1]).X[0]);
        Fname b = ((Fname) ((F) ((F) f.X[0]).X[2]).X[0]);
        if (((F) fx.X[0]).X[1].equals(k) && k.equals(((F) ((F) fx.X[1]).X[1]).X[1])
                && k.equals(((F) fx.X[2]).X[1]) && n.equals(((F) fx.X[0]).X[0])
                && n.equals(((F) ((F) fx.X[1]).X[1]).X[0])) {
            F re6 = new F(F.ADD, new Element[]{a, b});
            result6 = new F(F.intPOW, new Element[]{re6, n6});
        }
        return result6;

    }
}
