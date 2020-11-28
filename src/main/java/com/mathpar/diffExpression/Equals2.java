/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.diffExpression;

/**
 *
 * @author pasha
 */
import com.mathpar.func.*;
import java.util.ArrayList;
import com.mathpar.number.*;

public class Equals2 {

    public static int[][] dif = null;
    public static F[] func = null;
    public static Ring r;
    public static Ring newRing;
    public static Element[] res;
    public static Boolean flag = false;

    public Equals2(){

    }
 public static Boolean proverka1() {
        /*
         * проверяем явл ли диф ур, ур-ем первого порядка.
         */
        if (dif[0].length > 1) {
            return false;
        }

        /*
         * ищем максимальную степень dy и провверям чтобы все степени были
         * одинаковыми если это не так, то это не уравнение с разделяющимися
         * переменными.
         */
        int p = -1;
        int k1 = 0;
        for (int i = 0; i < dif.length; i++) {
            if (dif[i][0] >= 1) {
                p = dif[i][0];
                k1 = i;
                break;
            }
        }
        int pp = p;
        for (int i = k1 + 1; i < dif.length; i++) {
            if (dif[i][0] >= 1) {
                pp = dif[i][0];

            }
            if (p != pp) {
                return false;

            }
        }


        int[] num = new int[dif.length];
        int k = 0;//кол-во производных

        //проверяем сколько всего и номера функций к которым  относятся производные
        for (int i = 0; i < dif.length; i++) {
            if (dif[i][0] != 0) {
                num[k] = i;
                k++;
            }
        }


        /*
         * если кол-во проивоздных больше 1 то решаем, если нет то это не диф
         * ур-е
         */


 return true;
 }

    public static Boolean proverka() {
        /*
         * проверяем явл ли диф ур, ур-ем первого порядка.
         */
        if (dif[0].length > 1) {
            return false;
        }

        /*
         * ищем максимальную степень dy и провверям чтобы все степени были
         * одинаковыми если это не так, то это не уравнение с разделяющимися
         * переменными.
         */
        int p = -1;
        int k1 = 0;
        for (int i = 0; i < dif.length; i++) {
            if (dif[i][0] >= 1) {
                p = dif[i][0];
                k1 = i;
                break;
            }
        }
        int pp = p;
        for (int i = k1 + 1; i < dif.length; i++) {
            if (dif[i][0] >= 1) {
                pp = dif[i][0];

            }
            if (p != pp) {
                return false;

            }
        }


        int[] num = new int[dif.length];
        int k = 0;//кол-во производных

        //проверяем сколько всего и номера функций к которым  относятся производные
        for (int i = 0; i < dif.length; i++) {
            if (dif[i][0] != 0) {
                num[k] = i;
                k++;
            }
        }


        /*
         * если кол-во проивоздных больше 1 то решаем, если нет то это не диф
         * ур-е
         */
        if (k >= 1) {

            int[][] dif_buf = new int[2][1];
            F[] func_buf = new F[2];

            for (int i = 0; i < k; i++) {
                if (func_buf[0] == null) {
                    func_buf[0] = func[num[i]];
                } else {
                    func_buf[0] = func_buf[0].add(func[num[i]], r);
                }
            }

            for (int i = 0; i < func.length; i++) {
                Boolean f = false;
                for (int j = 0; j < k; j++) {
                    f |= num[j] == i;
                }

                if (!f) {
                    if (func_buf[1] == null) {
                        func_buf[1] = func[i];
                    } else {
                        func_buf[1] = func_buf[1].add(func[i], r);
                    }
                }
            }

            dif_buf[0][0] = p;
            dif_buf[1][0] = 0;
            func_buf[0] = (F) (func_buf[0].expand(r));
            func_buf[1] = (F) (func_buf[1].expand(r));//упрощение


            ////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            if (p != 1) {
                func_buf[0] = new F("1", r);
                func_buf[1] = new F(F.ROOTOF, new Element[]{(((F) ((F) func[1].divide(func[0], r)).negate(r))).expand(r), new NumberR64(p)});
                func_buf[1] = ((F) func_buf[1].negate(r));
            } else {
                func_buf[0] = (F) (func_buf[0].expand(r));
                func_buf[1] = (F) (func_buf[1].expand(r));
            }
            //--------------------------------------------------------
            func = new F[2];
            func[0] = new F("1", r);
            func[1] = (F) ((func_buf[1].divide(func_buf[0], r)).expand(r)).negate(r);
            dif = dif_buf;

            Ring ring1 = Equals1.addVariables(1, r);
            F lx = (F) new F(ring1.varNames[0], ring1).multiply(new F(ring1.varNames[ring1.varNames.length - 1], ring1), ring1);
            F ly = (F) new F(ring1.varNames[1], ring1).multiply(new F(ring1.varNames[ring1.varNames.length - 1], ring1), ring1);
            F verh = null;
            F niz=null;
            F buf1 = (F) (func_buf[1].expand(r)).divide((F) (func_buf[0].expand(r)), ring1).expand(r);
            if (buf1.X[0] instanceof F) {
                verh = (F) (buf1.X[0]);
            } else {
                verh = new F(buf1.X[0]);
            }

            if (buf1.X[1] instanceof F) {
                niz = (F) (buf1.X[1]);
            } else {
                niz = new F(buf1.X[1]);
            }


            Element verh1 =  verh.valueOf(new Element[]{lx, ly}, ring1);
            if(verh1 instanceof F){
                verh=(F )verh1;
            }else {
                verh=new F(verh1);
            }
            F[] verhp = Equals1.factor(verh, ring1);
            int poz = -1;
            for (int i = 0; i < verhp.length; i++) {
                if (verhp[i].indexsOfVars(ring1)[2]>=1&verhp[i].indexsOfVars(ring1)[1]==0&verhp[i].indexsOfVars(ring1)[0]==0){
                    poz=i;
                }
            }
            if (poz == -1){
               return false;
            }
            for(int i=0;i<verhp.length;i++){
                if(i!=poz){
                    if(verhp[i].indexsOfVars(ring1)[2]!=0){
                        return false;
                    }
                }
            }



            Element niz1 =  niz.valueOf(new Element[]{lx, ly}, ring1);
            if(niz1 instanceof F){
                niz=(F )niz1;
            }else {
                niz=new F(niz1);
            }


            F[] nizp = Equals1.factor(niz, ring1);
            int poz1 = -1;
            for (int i=0;i<nizp.length;i++){
                if(nizp[i].indexsOfVars(ring1)[2]>=1&nizp[i].indexsOfVars(ring1)[1]==0&nizp[i].indexsOfVars(ring1)[0]==0){
                    poz1=i;
                }
            }
            if (poz1 == -1){
               return false;
            }
            for(int i=0;i<nizp.length;i++){
                if(i!=poz1){
                    if(nizp[i].indexsOfVars(ring1)[2]!=0){
                        return false;
                    }
                }
            }

            F buf = ((F) verhp[poz].divide(nizp[poz1], ring1)).expand(ring1);
            if (!buf.compareTo(new F("1",ring1), 0, ring1)){
                return false;
            }


        } else {
            return false;

        }






        return true;
    }

     /**
     * проверяем является ли диф. ур. 1 порядка. и разрешим относительно y`
     * приводим входное диф уравнение к виду F(x,y)y`+G(x,y)=0
     *
     * @param ring
     * @return
     */
    public static Boolean Prov_1(Ring ring) {
        Boolean flag = true;

        /*
         * ищем максимальную степень dy и провверям чтобы все степени были
         * одинаковыми если это не так, то оно не разрешимо относительно y'
         */
        int p = -1; //масимальная степень при y`
        for (int i = 0; i < dif.length; i++) {
            if (dif[i][0] >= 1) {
                p = dif[i][0];
                break;
            }
        }
        int pp = p;
        for (int i = 0; i < dif.length; i++) {
            if (dif[i][0] >= 1) {
                pp = dif[i][0];

            }
            if (p != pp) {
                flag = false;
                return flag;
            }
        }
        /*
         * проверяем явл ли диф ур, ур-ем первого порядка.
         */
        if (dif[0].length > 1) {
            flag = false;
            return flag;
        }

        int[] num = new int[dif.length];
        int k = 0;//кол-во производных

        //проверяем сколько всего и номера функций к которым они относятся
        for (int i = 0; i < dif.length; i++) {
            if (dif[i][0] != 0) {
                num[k] = i;
                k++;
            }
        }


        /*
         * если кол-во проивоздных больше 1 то решаем, если нет то это не диф
         * ур-е
         */
        if (k >= 1) {
            int[][] dif_buf = new int[2][1];
            F[] func_buf = new F[2];//в 0 хранится при y` в 1 всё остальное.
            for (int i = 0; i < k; i++) {
                if (func_buf[0] == null) func_buf[0] = func[num[i]]; else func_buf[0] = func_buf[0].add(func[num[i]], ring);
            }
            int y = 0;
            for (int i = 0; i < func.length; i++) {
                Boolean f = false;
                for (int j = 0; j < k; j++) {
                    f |= num[j] == i;
                }
                if (!f) {
                    if (func_buf[1] == null) func_buf[1] = func[i]; else  func_buf[1] = func_buf[1].add(func[i], ring);
                }
            }

            dif_buf[0][0] = 1;
            dif_buf[1][0] = 0;
            if (func_buf[1] == null) {
                func_buf[1] = new F("0", ring);
            }

            if (p != 1) {
                  func_buf[0] = new F("1", ring);
                  func_buf[1] = new F(F.ROOTOF, new Element[]{(((F) ((F) func[1].divide(func[0], ring)).negate(ring))).expand(ring),new NumberR64(p)   });
                  func_buf[1] = ((F) func_buf[1].negate(ring));
            } else {
                func_buf[0] = (F) (func_buf[0].expand(ring));
                func_buf[1] = (F) (func_buf[1].expand(ring));
            }
            dif = dif_buf;
            func = func_buf;
        } else {
            flag = false;
            return flag;
        }
        return flag;
    }
    /**
     * Решает уравнения в полных дифференциалах
     * @param f входное уравнение
     * @param x0 начальные условия
     * @param y0
     * @param ring
     * @return массив решений
     * @throws Exception
     */
    public static F[] full_dif (F f,NumberR64 x0, NumberR64 y0, Ring ring, CanonicForms cf) throws Exception{
          r = ring;
        DiffEquation p = new DiffEquation();
        int[][] m = null;
        Element[] el = null;
        p.DifEquationToMatrix(f, m, el, ring);//преобразуем входное уравнение в 2 массива : функций при производных и  степеней производных

        Element[] func1 = p.masPol;
        func = new F[func1.length];//функции при производных


        for (int i = 0; i < func1.length; i++) {
            if (!(func1[i] instanceof F)) {
                func[i] = new F(func1[i]);
            } else {

                func[i] = (F) func1[i];
            }
        }
        dif = p.m;//степени производных
        Boolean prov = Prov_1(ring);
        if (prov) {
            Element q_ = func[0].D(0, ring);
            Element p_ = func[1].D(1, ring);
            if (q_.compareTo(p_, 0, ring)) {
                F mm=null;
                Element rth=func[0].valueOf(new Element[]{new F(r.varNames[0],r),new F (y0)}, ring);
                if (rth instanceof F){
                  mm=(F)rth;
                }else {
                    mm =new F (rth);
                }
                ring.CForm=cf;
                Element pp = (new Integrate()).integrate(mm, ring.varPolynom[0], ring );
                Element qq = Equals1.var_integrate("y", func[1], ring,cf);
                F int_p=null;
                if (pp instanceof F){
                   int_p= (F)pp;

                }else {
                    int_p=new F(pp);
                }
                F int_q=null;
                if(qq instanceof F){
                    int_q=(F)qq;
                }else{
                    int_q=new F(qq);
                }
                Element podst11=int_p.valueOf(new Element []{new F(x0),new F(r.varNames[1],r)}, r);
                F podst=null;
                if(podst11 instanceof F){
                    podst=(F)podst11;

                }else{
                    podst=new F(podst11);
                }
                F res1=(F) int_p.subtract(podst, ring);
                Element podst1=int_q.valueOf(new Element[]{new F(r.varNames[1],r),new F (y0)}, r);
                F podst12=null;
                if(podst1 instanceof F){
                    podst12=(F)podst1;

                }else{
                    podst12=new F(podst1);
                }
                F res2=(F) int_q.subtract(podst12, r);
                F rres =res1.add(res2, r);


                if (rres instanceof F){
                    return new F[] {((F)rres)};
                }else {
                    return new F[]{(new F(rres))};
                }



            }
        }



        return null;
    }
    /**
     * решение однородных уравнений
     * @param f
     * @param ring
     * @return
     * @throws Exception
     */

    public static F[] homogeneous(F f, Ring ring, CanonicForms cf) throws Exception {
        r = ring;
        DiffEquation p = new DiffEquation();
        int[][] m = null;
        Element[] el = null;
        p.DifEquationToMatrix(f, m, el, ring);//преобразуем входное уравнение в 2 массива : функций при производных и  степеней производных

        Element[] func1 = p.masPol;
        func = new F[func1.length];//функции при производных


        for (int i = 0; i < func1.length; i++) {
            if (!(func1[i] instanceof F)) {
                func[i] = new F(func1[i]);
            } else {

                func[i] = (F) func1[i];
            }
        }
        dif = p.m;//степени производных
        flag = proverka();//проверяем на однородность и приводим к виду y`=f(x,y)
        if (flag) {
            F[] func_zam = new F[2];
            int[][] dif_zam = new int[2][1];
            dif_zam[0][0]=dif[0][0];
            func_zam[0] = new F(r.varNames[0], r);
            func_zam[1] = (F) ((F) func[1].subtract(new F(r.varNames[1], r), r)).negate(r);
            F[] out = Equals1.Separable_variables(dif_zam, func_zam, r,cf);
            for (int i = 0; i < out.length; i++) {
                F x = new F(Equals1.NewRing.varNames[0], Equals1.NewRing);
                F z=new F(Equals1.NewRing.varNames[2], Equals1.NewRing);
                F yx = (F) new F(Equals1.NewRing.varNames[1], Equals1.NewRing).divide(new F(Equals1.NewRing.varNames[0], Equals1.NewRing), Equals1.NewRing);

                Element niz1 = out[i].valueOf(new Element[]{x, yx,z}, Equals1.NewRing);

                if (niz1 instanceof F) {
                    out[i] = (F) niz1;
                } else {
                    out[i] = new F(niz1);
                }
            }

            return out;
        } else {
            return new F[]{};
        }
    }
    /**
     * Проверяет является ли уравнение, уравнением Клеро
     * @return
     */
    public static Boolean proverka_klero(){
        if (dif[0].length >1) return false;
       // Boolean flag=false;
        int k=0;//проверочная переменная
        int num =0;
        //проверяем есть ли xy' и сколько их
        for (int i=0;i<dif.length;i++){
            if (dif[i][0]==1){
                if(func[i].indexsOfVars(r)[0]>0 && func[i].indexsOfVars(r)[1]==0){//Если в func [i] есть х и нет у, то....
                    k++;
                    num=i;
                }
            }
        }
        if (k!=1){
            return false;
        }
        int k1=0;//Проверка наличия у'
       for (int i=0;i<func.length;i++){
           if(num!=i){
               if(func[i].indexsOfVars(r)[0]==0 && func[i].indexsOfVars(r)[1]==0){
                   if(dif [i][0]>0){
                   k1++;
               }
               }else {
                   return false;
               }
           }
       }
       if(k1==0) return false;



        return true;
    }
    /**
     * решает уравнение Клеро
     * @param f
     * @param ring
     * @return массив решений уравнения.
     */
    public static F[] Klero (F f,Ring ring){
         r = ring;
        DiffEquation p = new DiffEquation();
        int[][] m = null;
        Element[] el = null;
        p.DifEquationToMatrix(f, m, el, ring);//преобразуем входное уравнение в 2 массива : функций при производных и  степеней производных

        Element[] func1 = p.masPol;
        func = new F[func1.length];//функции при производных


        for (int i = 0; i < func1.length; i++) {
            if (!(func1[i] instanceof F)) {
                func[i] = new F(func1[i]);
            } else {

                func[i] = (F) func1[i];
            }
        }
        dif = p.m;//степени производных
      //  Boolean flag = proverka_klero();
        if( proverka_klero()){
            String z ="y^{1.00 }_t";
            newRing=Equals1.addVariables(1, ring);
            String var=newRing.varNames[newRing.varNames.length-1];

            String res1 = zam(f.toString(r),z,var);
            F rres=new F(res1,newRing);
            Element dres=rres.D(newRing.varNames.length-1, newRing);
            F Dres = null;
            if(dres instanceof F){
                Dres =(F) dres;
            }else{
                Dres = new F(dres);
            }
            System.out.println("Общее решение y="+rres);
            System.out.println("Особое решение, требуется выразить из системы ~C0~  y="+rres+"    0=" +Dres );
            return new F[]{(F)rres.subtract(new F(newRing.varNames[1],newRing), newRing),new F(F.SYSTLAE, new F[]{(F)rres.subtract(new F(newRing.varNames[1],newRing),newRing),Dres})};
        }


        return null;
    }

    public static String zam (String str, String z,String var){

        String res ="";
        Boolean flag = true;
        ArrayList<Integer> b = new ArrayList<Integer>();
        b.add(str.indexOf(z,10));

        while(flag){

            b.add(str.indexOf(z,b.get(b.size()-1)+1));
            if(b.get(b.size()-1)==-1){
                flag = false;
            }

        }
        int y=-1;
        for(int i=0; i<b.size();i++){
            if(b.get(i)!=-1){
                if(i!=0){
                    res+=str.substring(b.get(i-1)+z.length()+1, b.get(i)-1)+var;
                }else{
                    res+=str.substring(10, b.get(i)-1)+var;
                }
                y=b.get(i);
            }
        }
        res+=str.substring(y+z.length(), str.length()-9);
        System.out.println(""+res);

        return res;

    }
    public static void main(String[] args) throws Exception {
        Ring ring = new Ring("R64[x,y]");
        CanonicForms cf= new CanonicForms(ring,true);
       F g = new F("\\systLDE(x^2\\d(y,x,1)+y^2+xy+x^2=0)", ring);
        System.out.println(""+g);
       F[] ress = homogeneous(g, ring,cf);
       System.out.println("" + Array.toString(ress,ring));



    }
}
