/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;
//import java.util.Random;
import com.mathpar.func.F;
import com.mathpar.func.parser.Parser;
 import com.mathpar.number.*;
 import com.mathpar.polynom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

//import java.util.*;

//import java.util.ArrayList;

/**
 *
 * @author gennadi
 */
public class SolvDE_01 extends F {
public static   Polynom r = new Polynom();
            public static   Ring r2 = new Ring("ZMinMult[x]");
   /**
     * Обход дерева F с подсчетом количества листьев и узлов
     * @return int[]{ Число листьев, Число узлов}
     *  возвращает массив из трёх целых чисел:
     * первое - "Число листьев",
     * второе - "Число узлов",
     * третье - "максимальная Глубина".
     */
    public static void allPolynomials(F f, Vector<Element> polynomials) {
       // VectorS<Element> polynomials = new VectorS<Element>();
        for (int i = 0; i < (f.X).length; i++) {
            if (f.X[i] instanceof F) {
                 allPolynomials(((F) f.X[i]),polynomials);
            } else   polynomials.add(f.X[i]);
        }
    }


/**
 *
 * @param abc
 * @return
 * @
 * @throws ContextException
 */
    public boolean MulAndDiv(F abc)   {

        boolean f = false;
        //boolean flag;
        Vector<Element> polynomials = new Vector<Element>();
        int p[];
        int y[];
        if (abc.name == 97 || abc.name == 99) {
            f = true;
        } else {
            return false;
        }
        if (f) {
            allPolynomials(abc, polynomials);
            //  Element[] at=new Element[0];
            Polynom[] at = new Polynom[polynomials.size()];
            at = polynomials.toArray(at);


            y =  variables(at);
            if (y.length > 2) {
                return false;
            } else {
                for (int i = 0; i < at.length; i++) {
                    p = variables(at[i]);
                    if (p.length > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     *
     * @param run
     * @return
     * @
     */
    public static int[] DIFF(F[] run)   {
        Vector<Element> vary = new Vector<Element>();
        ArrayList<Integer> very = new ArrayList<Integer>();
        int[] u;
        int[] r = new int[1];
        r[0] = 255;
        int[] ght = new int[5];
        for (int i = 0; i < run.length; i++) {
            if (run[i].name == F.D) {
                allPolynomials(run[i], vary);
                Polynom[] dif = new Polynom[vary.size()];
                dif = vary.toArray(dif);
                for (int k = 0; k < dif.length; k++) {
          }
                if (dif.length > 2) return r;
                int s[];
                for (int j = 0; j < dif.length; j++) {
                    s = variables(dif[j]);
                    very.add(s[0]);}
                very.add(i);
            }
        }

        if (very.size() < 3) {
            return r;
        }
        Integer[] array = very.toArray(new Integer[very.size()]);
        u = new int[array.length];
        for (int y = 0; y < u.length; y++) {
            u[y] = array[y];
        }

        return u;
    }

public static F[] delvar (F[] mas, int per) {
    Vector<Element> polynomial = new Vector<Element>();
    ArrayList <F>vary= new ArrayList<F>();
    Polynom[]  gif;
    int [] var;
    F [] masvar;
    for (int i=0; i<mas.length;i++){
        gif=null;

       allPolynomials(mas[i],polynomial);

      gif=new Polynom[polynomial.size()];
       gif = polynomial.toArray(gif);
       for(int j=i; j<gif.length;j++){

       var=variables(gif[j]);

       if(var[0]==per){vary.add(mas[i]);}

       }
    }


     F[] array = vary.toArray(new F[vary.size()]);
       masvar = new F[array.length];
        for (int i = 0; i < masvar.length; i++) {
         masvar[i] = array[i];
            //System.out.println("masvar["+i+"]/////"+masvar[i]);
        }

    return masvar;
}
public static F funcvar (F[] first, F[] second , int y) {
    F [] left;
    F [] divleft;
    F lefty;
    left=delvar(first,y);
    divleft=delvar(second, y);
    Element [] y_1=new Element[left.length];
    for (int i=0; i<left.length; i++){
        y_1[i]=left[i];
    }
    Element [] y_2=new Element[divleft.length];
    for (int i=0; i<divleft.length; i++){
        y_2[i]=divleft[i];
    }

    F lefy= new F(F.MULTIPLY, y_1);
    F divy=new F(F.MULTIPLY, y_2);
    lefty=new F(F.DIVIDE,  new  Element []{lefy, divy});
    return lefty;
}

    public static F SDE_DP(F fun, Ring ring) {
        F anser=new F();
        if ((fun.name != ADD) && (fun.name != SUBTRACT)) {
            return fun;
        } else {
            Element [] parts = (fun.X);
            if (parts.length > 2) {
                return fun;
            }
            F[] part = new F[parts.length];
            for (int i = 0; i < parts.length; i++) {
                part[i] = ((F) parts[i]);
            }


            Element [] way = (part[0].X);
            F[] depart = new F[way.length];
            for (int i = 0; i < way.length; i++) {
                depart[i] = ((F) way[i]);
            }
            Element [] ways = (part[1].X);
            F[] section = new F[ways.length];
            for (int i = 0; i < ways.length; i++) {
                section[i] = ((F) ways[i]);
            }
            int flag;
           int [] dif=DIFF(depart);
            if(dif.length==1){
                dif=DIFF(section);
                flag=2;
            }else{flag=1;}


           int  one = dif[0];
           int two = dif[1];

            int ch = dif[2];
           F[] V;
           F[] U;
           F left;
           F right;
           F intl;
           F intr;

            if (flag == 1) {

                V = new F[depart.length - 1];
                for (int i = 0; i < ch; i++) {
                    V[i] = depart[i];
                }
                for (int i = ch; i < V.length; i++) {
                    V[i] = depart[i];
                }
                left = funcvar(V, section, one);
                right = funcvar(section, V, one);

            } else {

                U = new F[section.length - 1];
                for (int i = 0; i < ch; i++) {
                    U[i] = section[i];
                }
                for (int i = ch; i < U.length; i++) {
                    U[i] = section[i];
                }
                left = funcvar(U, depart, one);
                right = funcvar(depart, U, two);
            }
            intl = new F(F.INT, new Element[]{left, ring.varPolynom[one]});
            intr = new F(F.INT, new Element[]{right, ring.varPolynom[two]});
            anser = new F(F.SUBTRACT, new Element[]{intl, intr});
        }
        return anser;
    }
    
      public static int[] variables(Polynom[] Pols) {
        int f = 0; // суммарное количество переменных во всех полиномах
        int g = 0;
        for (int i = 0; i < Pols.length; i++) {
            int d = Pols[i].powers.length / Pols[i].coeffs.length;
            f += d;
        }
        int use[];
        List<Integer> rep = new ArrayList<Integer>();
        int vse[];
        vse = new int[f];
        for (int i = 0; i < Pols.length; i++) {
            int b = Pols[i].powers.length;
            int d = Pols[i].powers.length / Pols[i].coeffs.length;
            List<Integer> per = new ArrayList<Integer>();
            int u[];
            if (d != 0) {
                for (int m = 0; m < d; m++) {
                    for (int r = m; r < b; r += d) {
                        if (Pols[i].powers[r] > 0) {
                            per.add(m + 1);
                        }
                    }
                }
            }
            Integer[] array = per.toArray(new Integer[per.size()]);
            u = new int[array.length];
            System.arraycopy(array, 0, u, 0, u.length);
            for (int m = 0; m < u.length; m++) {
                boolean flag = false;
                for (int r = 0; r < f; r++) {
                    if (u[m] == vse[r]) {
                        flag = true;
                    }
                }
                if (flag == false) {
                    vse[g] = u[m];
                    g++;
                }
            }
        }
        for (int m = 0; m < f; m++) {
            if (vse[m] != 0) {
                rep.add(vse[m] - 1);
            }
        }
        Integer[] arr = rep.toArray(new Integer[rep.size()]);
        use = new int[arr.length];
        System.arraycopy(arr, 0, use, 0, use.length);
        return use;
    }

      
        public static int[] variables(Polynom p) {
        int b = p.powers.length;
        int d =  p.powers.length / p.coeffs.length;
        List<Integer> per = new ArrayList<Integer>();
        int u[];
        if (d != 0) {
            for (int i = 0; i < d; i++) {
                for (int r = i; r < b; r += d) {
                    if ( p.powers[r] > 0) {
                        per.add(i);
                    }
                }
            }
        }
        Integer[] array = per.toArray(new Integer[per.size()]);
        u = new int[array.length];
        System.arraycopy(array, 0, u, 0, u.length - 1);
        return u;
    }


            
        public static void main(String[] args) {Page p=new Page( ); Element a=new Element();
        Ring r = new Ring("ZMinMult[x,y]"); 
        F f = Parser.getF("3", r);
        System.out.println("WWWWWWWWWWWWWWWWW===="+f.toString(r));
    }
                
        
    }









