/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

// © Разработчик Смирнов Роман Антонович (White_Raven), ТГУ'11
package com.mathpar.lcTransform;

import com.mathpar.func.*;
import com.mathpar.number.*;

/**
 * Класс решающий системы дифференциальных уравнений с частными производными
 * @author white_raven
 * @version 0.1
 */
public class solvePDE {

    Ring ring; // кольцо на входе
    Fname[] result; // хранится результат по каждой функции


    public solvePDE(Ring r) {
        ring = r;
    }

    public Element calculatePDE(F f) {
        // смотрим наличие начальных условий и если они отсутствуют - создаем
        boolean flagIC = true;
        if (f.X.length == 1) {
            makeIC ic=new makeIC(ring);
            F InitCond =ic.generateInitCond(((F) f.X[0]).X);
            f = new F(F.SOLVEPDE, new Element[]{f.X[0], InitCond});
            flagIC = false; // нет значений начальных условий
        }
        // вычисляем изображающее уравнение для каждой функции
        LCTransform LC = new LCTransform(ring, flagIC);
        LC.LC_transform(f);
        if(!flagIC){
        //если начальные условия не были заданы то решение выводим в общем виде
        inversLCTransform inLC=new inversLCTransform(LC.ic_index-LC.start_ic_index, LC.start_ic_index, flagIC,ring.varNames.length, LC.ring);
        Element h=inLC.inverse_Laplace_Carcon_transform_without_IC(LC.calculateFunctions.get(0).X[0]);
        }


        inversLCTransform iLC = new inversLCTransform(LC.ring);
        result = new Fname[LC.calculateFunctions.size()];

        for (int i = 0; i < LC.calculateFunctions.size(); i++) {
            // вот здесь и распараллелим для систем , так как они друг от друга уже не зависят
            Element iLcREs = iLC.workILCTransform(LC.calculateFunctions.get(i).X[0]);
            result[i] = new Fname(LC.calculateFunctions.get(i).name, iLcREs);
        }
        sout();
        return new F(F.VECTORS, result);
    }


    void sout(){
     System.out.println("Решение : ");
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i].name + " = " + result[i].X[0].toString(ring));
        }
       System.out.println("\n");
    }

    public static void main(String[] args) {
        Ring r = new Ring("R64[x,y]");
        solvePDE s = new solvePDE(r);

//
//                F sysw = new F("\\systLDE(\\d(f,x)-\\d(f,y)=xy)", r);
//                F test=new F(F.SOLVEPDE,sysw);
//
//                s.calculatePDE(test);
//




        F sys = new F("\\systLDE(\\d(f,x,2)-\\d(f,y)=xy)", r);
      //  F sysk=new F("\\systLDE(\\d(f,x)+\\d(g,y)=x,\\d(f,y)+\\d(g,x)=y)",r);
     //   s.make_Init_Cond(sys);



     //   System.out.println(sys.toString(r)); // b= -x^4/24 or b= -y^2/2;
        F initcond = new F("\\initCond(\\ic(f,x,0,0,y,,0)=1,\\ic(f,x,0,1,y,,0)=-y^2/2 , \\ic(f,x, ,0,y,0,0)=1)", r);

        F LC1 = new F(F.SOLVEPDE, new Element[]{sys, initcond});
       // F LC_withoutic = new F(F.SOLVEPDE, new Element[]{sys});

       //s.calculatePDE(LC1);
       // s.calculatePDE(LC1);
        s.calculatePDE(LC1);
        //s.calculatePDE(LC_withoutic);

//        solvePDE s1 = new solvePDE(r);
//        F sys1 = new F("\\systLDE(\\d(f,x,2)-\\d(f,y)=xy)", r);
//        System.out.println(sys1.toString(r)); // b= -x^4/24 or b= -y^2/2;
//        F initcond1 = new F("\\initCond(\\d(f,x,0,0)=1, \\d(f,x,0,y,1)=-x^4/24 , \\d(f,y,0,0)=1)", r);
//        F LC11 = new F(F.SOLVEPDE, new Element[]{sys1, initcond1});

               //s.calculatePDE(LC1);
       // s1.calculatePDE(LC11);





//        solvePDE s_ = new solvePDE(r);
//
//        F sys_ = new F("\\systLDE(\\d(f,x,2)-4f=4x)", r);
//        System.out.println(sys_.toString(r));
//         F initcond_ = new F("\\initCond(\\d(f,x,0,0)=1,\\d(f,x,0,1)=0)", r);
//         F LC1_ = new F(F.SOLVELDE, new Element[]{sys_, initcond_});
//        s_.calculatePDE(LC1_);

        //             System.out.println("  " + s.calculatePDE(LC1));
//               F d=new F("-y^2/2",r);
//               System.out.println("  " + LC.transform_Divide(d));


               //int y=LC.typeSystemLDE_and_makeRing(LC1);
             //  System.out.println(LC.calculateFunctions.get(0));

//               LCTransform LC_= new LCTransform(r);
//               F sys1=new F("\\systLDE(\\d(f,x)+\\d(g,y)=x,\\d(f,y)+\\d(g,x)=y)",r);
//               System.out.println("   " + sys1.toString(r));
//               F initcond1= new F("\\initCond(\\d(f,x,0,0)=5,\\d(g,x,0,0)=1,\\d(f,y,0,0)=3,\\d(g,y,0,0)=2)",r);
//               F LC1_= new F(F.SOLVELDE,new Element[]{sys1,initcond1});
//               LC_.LC_transform(LC1_);zzzzzzzz
    }

}
