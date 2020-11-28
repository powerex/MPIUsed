/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.showgraph;

import com.mathpar.func.F;
import com.mathpar.func.Fname;
import com.mathpar.func.Page;
import com.mathpar.matrix.MatrixD;
import com.mathpar.matrix.Table;
import com.mathpar.number.Element;
import com.mathpar.number.NumberR64;
import com.mathpar.number.Ring;

/**
 *
 * @author andy
 */
public class tests {
    public static void main(String[] args) throws Exception {
        /**
         * Путь к файлу с изображением
         */
        String path = System.getProperty("user.home") + "/image.png";

//        //параметры
//        //[0]: 1-все черное, 0-цвета генерятся автоматически
//        //[1]: толщина линий графика
//        //[2]: толщина осей
        NumberR64[] num=new NumberR64[5]; 
        num[0]=new NumberR64("1");
        num[1]=new NumberR64("1");
        num[2]=new NumberR64("12");
        num[3]=new NumberR64("3");
        num[4]=new NumberR64("3");
//        
//        
        Ring r=new Ring("R64[x]");
        Page p=new Page(r,true);
//        k(t) = ((-1.5)*3.0+1.5*\exp(2.0t)*3.0)
//h(t) = ((-0.75)+(-1.5t)+0.75*\exp(2t))
//        
//        //для явной функции
        F f=new F("\\unitBox(x,3)", r);
       // new Plots(p, f, -10, 10, 0, 2, false, "x", "y", "title", r,num, path);
//        //-----------------------------------------------------------------------------------------
//
//        //для построения графа
//        //массив таблиц с координатми начала и концов ребер
//        Element[] el=new Element[4];
//        el[0]=new Table(
//                new MatrixD(
//                     new Element[][]{{new NumberR64("1"),new NumberR64("1")},
//                                     {new NumberR64("1"),new NumberR64("4")}}));
//        el[1]=new Table(
//                new MatrixD(
//                     new Element[][]{{new NumberR64("1"),new NumberR64("4")},
//                                     {new NumberR64("4"),new NumberR64("4")}}));
//        el[2]=new Table(
//                new MatrixD(
//                     new Element[][]{{new NumberR64("4"),new NumberR64("4")},
//                                     {new NumberR64("4"),new NumberR64("1")}}));
//        el[3]=new Table(
//                new MatrixD(
//                     new Element[][]{{new NumberR64("4"),new NumberR64("1")},
//                                     {new NumberR64("1"),new NumberR64("1")}}));
//        new Plots(p, false, el, path);
//        //-----------------------------------------------------------------------------------------
//
//
//        //для табличных графиков
//        //построит ломанную по точкам ниже
//        Table t=new Table(
//                new MatrixD(
//                     new Element[][]{{new NumberR64("1"),new NumberR64("-4"),new NumberR64("3")},
//                                     {new NumberR64("1"),new NumberR64("4"),new NumberR64("7")}}));
//        
//        new Plots(p,false, t, new Fname(""), new Element[]{}, 0,path, true, num,true);
        
        //        //для табличных графиков
//        //построит ломанную по точкам ниже
//        Table t=new Table(
//                new MatrixD(
//                     new Element[][]{{new NumberR64("1"),new NumberR64("1"),new NumberR64("5"),new NumberR64("5")},
//                                     {new NumberR64("1"),new NumberR64("5"),new NumberR64("1"),new NumberR64("5")}}));
//        Element[]  text = new Element[]{new F("A",r),new F("B",r),new F("C",r),new F("D",r)};
//        Element[]  v = null;//new Element[]{new NumberR64("7"),new NumberR64("7"),new NumberR64("7")};
//        Element[]  g = null;//new Element[]{new NumberR64("0"),new NumberR64("0"),new NumberR64("5")};
//        new Plots(p,false, t, new Fname(""), new Element[]{}, 0,path, false, num, text, v, g);
//       
//        
//        
//        //для параметрических
//         Ring rt=new Ring("R64[t]");
//        Page pt=new Page(rt);
//        Element[] funcs=new F[]{new F("\\sin(t)",rt),new F("\\cos(t)",rt)};
//        new Plots(pt, funcs, -5, 5, -2, -2, -Math.PI,Math.PI, false, "x","y", "Title", pt.ring, num, path);
//        
        MatrixD I = new MatrixD(new Element[][] {
                    {new NumberR64(0), new NumberR64(1), new NumberR64(1), new NumberR64(0), new NumberR64(1), new NumberR64(0)},
                    {new NumberR64(1), new NumberR64(0), new NumberR64(0), new NumberR64(1), new NumberR64(1), new NumberR64(0)},
                    {new NumberR64(1), new NumberR64(0), new NumberR64(0), new NumberR64(0), new NumberR64(1), new NumberR64(1)},
                    {new NumberR64(0), new NumberR64(1), new NumberR64(0), new NumberR64(0), new NumberR64(0), new NumberR64(0)},
                    {new NumberR64(1), new NumberR64(1), new NumberR64(1), new NumberR64(0), new NumberR64(0), new NumberR64(1)},
                    {new NumberR64(0), new NumberR64(0), new NumberR64(1), new NumberR64(0), new NumberR64(1), new NumberR64(0)}
                });
        MatrixD M = new MatrixD(new Element[][] {
                    {new NumberR64(3), new NumberR64(2), new NumberR64(4), new NumberR64(1), new NumberR64(3), new NumberR64(5)},
                    {new NumberR64(3), new NumberR64(2), new NumberR64(2), new NumberR64(1), new NumberR64(1), new NumberR64(1)}});
       F.drawGraph(I, M, path);


    }
}
