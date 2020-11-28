/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.diffExpression;

/**
 *
 * @author VOVA (недоделка.........)
 */
import com.mathpar.func.*;
import com.mathpar.func.CanonicForms;
import com.mathpar.func.F;
import java.util.ArrayList;
import com.mathpar.number.*;
import com.mathpar.polynom.FactorPol;
import com.mathpar.polynom.Polynom;

public class Equals1 extends CanonicForms{

    static int[][] dif = null;
    static F[] func = null;
    public static Ring NewRing;//новое кольцо для ответов
    static F Ny;
    static F Qy;
    static F Mx;
    static F Px;
    static Boolean SepS;
    static Boolean DecA; //понижение дифференцирования типа а: (y^(n)^p=f(x))
    static Boolean DecB;

    static Boolean DecC;
    static F[] rres;
    public static int k=0;
    public static int n=0;
    public static int nk=-1;

    public Equals1() {
    }

    /**
     * Главный метод класса решающий дифференциальные уравнения:
     * с разделяющимися переменными вида P(x,y)(y`)^n+Q(x,y)=0;
     * понижением порядка дифференцирования уравнений вида:
     *      A)  (y^(n))^p = f(x) наиболее простое дифференциальное уравнение n-го порядка
     *      B)  F(x,y^(k),y^(k+1),...,y(n)) = 0 не содержит явно искомой функции и производных до k-1 порядка включительно

     * @param f-входное дифференциальное уравнение
     * @param ring
     * @return массив решений в виде F[] в новом кольце newRing которое является
     * дополнением исходного кольца до нового путём дополнения произвольных констант ~Ci~
     */
    public static F[] Solve(F f, Ring ring, CanonicForms cf) throws Exception{
        F[] res = Separable_variables(f, ring, cf);
        if(res != null){
            System.out.println("Это уравнение с разделяющимися переменными вида Mx)Q(y)(y`)^n + P(x)N(y) = 0:  " +Mx.multiply(Qy, ring)+"(y`)^"+dif[0][0]+" + "+Px.multiply(Ny, ring)+" = 0");
            return res;
        }

        F[] res1 = Decrease(f, ring,cf);
        if (res1 != null) {
            if (DecA) {
                System.out.println("Это уравнение вида (y^(n))^p=f(x):" +"(y^("+dif[0].length+"))^"+dif[0][dif[0].length-1]+" = "+func[1]);
                return res1;
            }
        }

        System.out.println("Не подходит для решения данными методами");
        return new F[]{};
    }
    /**
     *
     * @param d
     * @param fc
     * @param ring
     * @return
     * @throws Exception
     */
     public static F[] Solve(int[][] d, F[] fc, Ring ring, CanonicForms cf) throws Exception{


        F[] res = Separable_variables(d,fc, ring, cf);
        if(res != null){
            System.out.println("Это уравнение с разделяющимися переменными вида Mx)Q(y)(y`)^n + P(x)N(y) = 0:  " +Mx.multiply(Qy, ring)+"(y`)^"+dif[0][0]+" + "+Px.multiply(Ny, ring)+" = 0");
            return res;
        }

        F[] res1 = Decrease(d, fc, ring, cf);
        if (res1 != null) {
            if (DecA) {
                System.out.println("Это уравнение вида (y^(n))^p=f(x):" +"(y^("+dif[0].length+"))^"+dif[0][dif[0].length-1]+" = "+func[1]);
                return res1;
            }
        }


        System.out.println("Не подходит для решения данными методами");
        return new F[]{};
    }


    /**
     * понижение порядка дифференцирования
     * @param f входное диф уравнение
     * @param ring
     * @return возвращает масссив ответов
     */

    public static F[] Decrease(int[][] d,F[] gy, Ring ring, CanonicForms cf) throws Exception{
        dif =d;
        func=gy;

        F[] res = null;
        /*
         * проверяем является ли диф уравнение, типа (y^(n)))^p=f(x) и решаем если оно такое
         */
        DecA = Prov_DecA(ring);
        DecB = Prov_DecB(ring);
      //  DecC = Prov_DecC(ring);
        if (DecA) return  new F[]{((F) Simple_Dif(ring, cf).subtract(new F(new Polynom(NewRing.varNames[1], NewRing)), NewRing)).expand(NewRing)};
        if (DecB) return  bez_y(ring,cf);




        return res;
    }


      /**
     * понижение порядка дифференцирования
     * @param f входное диф уравнение
     * @param ring
     * @return возвращает масссив ответов
     */

    public static F[] Decrease(F f, Ring ring, CanonicForms cf) throws Exception{
        int[][] m = null;
        Element[] el = null;
        DiffEquation df = new DiffEquation();
        df.DifEquationToMatrix(f, m, el, ring);
        int[][] diff = df.m;
        Element[] funcf1 = df.masPol;
        F [] ffunc = new F[funcf1.length];
        for (int i = 0; i < funcf1.length; i++) {
            if (!(funcf1[i] instanceof F)) ffunc[i] = new F(funcf1[i]);
              else ffunc[i] = (F) funcf1[i];
        }
        return Decrease(diff, ffunc, ring,cf);
    }
    /**
     * решает уравнение вида  F(x,y^(k),y^(k+1),...,y(n)) = 0
     * @param ring
     * @return
     * @throws Exception
     */
    public static F[] bez_y(Ring ring, CanonicForms cf) throws Exception{
        if(n-k ==1){
            Equals1 eq1 = new Equals1();
            int[][] dif1 = new int[dif.length][1];
            F[] func1 = func;

            //делаем замену
            Element fun= func[nk].multiply(new F(ring.varNames[1],ring), ring);
            if(fun instanceof F){
                func1[nk] = (F) fun;
            }else{
                func1[nk]=new F(fun);
            }

            for(int i=0; i<dif.length; i++){
                if(dif[i][n]!=0){
                    dif1[i][0]=dif[i][n];
                }
            }
            //решаем полученное уравнение первого порядка
            F[] res = eq1.Solve(dif1,func1, ring, cf);
            if(res != null){
                F prom = zam_str(res[0], NewRing);
                F[] zres = eq1.Solve(prom, NewRing,cf);

                 if(zres!=null){
                     return zres;
                 }else{
                     return null;
                 }
            }else{
                return null;
            }


        }else{
            return null;
        }


    }

    public static F zam_str(F f, Ring ring){
        String str = f.toString(ring);
        String res ="\\systLDE(";
        Boolean flag = true;
        ArrayList<Integer> b = new ArrayList<Integer>();
        b.add(str.indexOf("y"));

        while(flag){

            b.add(str.indexOf("y",b.get(b.size()-1)+1));
            if(b.get(b.size()-1)==-1){
                flag = false;
            }

        }
        int y=-1;
        for(int i=0; i<b.size();i++){
            if(b.get(i)!=-1){
                if(i!=0){
                    res+=str.substring(b.get(i-1)+1, b.get(i)-1)+"\\d(y,t,"+(k+1)+")";
                }else{
                    res+=str.substring(0, b.get(i)-1)+"\\d(y,t,"+(k+1)+")";
                }
                y=b.get(i);
            }
        }
        res+=str.substring(y+1, str.length()-1)+")";
        System.out.println(""+res);

        return new F(res,ring);
    }
    //разрешает если это возможно относительно второй переменной кольца.
    public static F Expand(F f, Ring ring){


        return null;

    }
    /**
     * решает дифференциальные уравнения с разделящимися переменными
     *
     * @param f входное диф. уравнение
     * @param ring
     * @return возвращает массив решений
     */
    public static F[] Separable_variables(F f, Ring ring, CanonicForms cf) throws Exception {
        dif=null;
        func =null;
        int[][] m = null;
        Element[] el = null;
        DiffEquation df = new DiffEquation();
        df.DifEquationToMatrix(f, m, el, ring);
        dif = df.m;
        Element[] func1 = df.masPol;
        func = new F[func1.length];
        for (int i = 0; i < func1.length; i++) {
            if (!(func1[i] instanceof F)) {
                func[i] = new F(func1[i]);
            } else {

                func[i] = (F) func1[i];
            }
        }
        return Separable_variables(dif, func, ring, cf);
    }


    public static F[] Separable_variables(int[][] d, F[] gy, Ring ring, CanonicForms cf) throws Exception {
        dif =d;
        func = gy;
        SepS = Prov_1(ring);
        if (SepS) {
            SepS &= Prov_2(ring);
        } else {
            return null;
        }
        if (SepS) {
            F f1 = (F) Px.divide(Mx, ring);
            f1 = f1.expand(ring);
            F f2 = (F) Qy.divide(Ny, ring);
          //  f2 = f2.simplify(ring);
            F ff1 = null;
            ring.CForm=cf;
            Element bff1 = (new Integrate()).integrate(f1, ring.varPolynom[0], ring );
            if (bff1 instanceof F) {
                ff1 = (F) bff1;
            } else {
                ff1 = new F(bff1);
            }
            F fff = var_integrate(ring.varNames[1], f2, ring,cf);
            F f_Mx = null;
            F f_Ny = null;
            NewRing = addVariables(1, ring);
         //   NewRing.CForm=ring.CForm;
            F bbud = new F(new Polynom(NewRing.varNames[NewRing.varNames.length-1], NewRing));
            F[] res = null;
            if(Mx.indexsOfVars(ring)[2]!=0) f_Mx = new F(F.ALGEBRAIC_EQUATION, new F[]{Mx, new F("0", ring) });
            if(Ny.indexsOfVars(ring)[2]!=0) f_Ny = new F(F.ALGEBRAIC_EQUATION, new F[]{Ny, new F("0", ring) });
            F ress = new F(F.ALGEBRAIC_EQUATION, new F[]{(ff1.add(fff, ring)).add(bbud, NewRing), new F("0",ring)});
            ArrayList<F> ares = new ArrayList<F>();
            if(f_Mx !=null) ares.add(f_Mx);
            if(f_Ny !=null) ares.add(f_Ny);
            res = new F[ares.size()+1];
            res[0] = ress;
            for(int i=0; i<ares.size();i++){
                res[i+1]=ares.get(i);
            }
            return res;

        } else {

            return null;
        }

    }
    /**
     * решает уравнение вида  (y^(n))^p = f(x)
     * @param ring
     * @return
     * @throws Exception
     */
    public static F Simple_Dif(Ring ring, CanonicForms cf) throws Exception{
        Boolean flag = true;
        int n =dif[0].length;//счётчик кол-ва интегрирования
        int c=2;//счётчик добавления переменной
        F res = func[1];
        NewRing = addVariables(n, ring);
        while(flag){
          ring.CForm=cf;  NewRing.CForm=cf;
          Element buf =(new Integrate()).integrate(res, NewRing.varPolynom[0],  NewRing ).add(new Polynom(NewRing.varNames[c], NewRing), NewRing);

            try{
            res=(F) buf;
            }catch(Exception e){
            res = new F(buf);
        }
            c++;
            n--;
            if(n==0){
                flag=false;
            }
        }
        res=res.expand(NewRing);
        return res;
    }

    /**
     * добавляет переменные в кольцо
     * @param n сколько нужно добавить
     * @param ring исходное кольцо
     * @return
     */
    public static Ring addVariables(int n, Ring ring) {
        String s = ring.toString().substring(0, ring.toString().length() - 1);
        s += ",";
        String A = "C";
        for (int i = 0; i < n - 1; i++) {
            s += "~" + A + i + "~" + ",";
        }
        s += "~" + A + (n - 1) + "~" + "]";
        return new Ring(s);
    }

    /**
     * проверяем является ли диф уравнение, типа (y^(n))^p=f(x), и если можно то приводим к этому виду
     * @param ring
     * @return если является такого типа то true, нет false
     */
    public static Boolean Prov_DecA(Ring ring){

        int p = -1; //масимальная степень при y`
        int n = dif[0].length-1;//максимальный порядок дифференцирования
        //проверяем чтобы везде был одинаковый порядок дифференцирования
        for(int i=0; i<dif.length; i++){
            for(int j=0; j<dif[i].length-1; j++){
                if(dif[i][j]>0) return false;
            }
        }
        //проверяем чтобы все производные были в одинаковой степени
        for(int i=0; i<dif.length; i++){
            if(dif[i][n]>0) {p=dif[i][n];break;}
        }
        for(int i=0; i<dif.length; i++){
            if(dif[i][n] !=0 & dif[i][n] !=p) return false;

        }

        //ищем все номера функций при y`
        int[] k = new int[dif.length]; //лежат номера  функций при y`
        int c = 0;
        for (int i = 0; i < dif.length; i++) {
            if (dif[i][n] == p) {
                k[c] = i;
                c++;
            }
        }
        int[][] dif_buf = new int[2][n + 1];
        F[] func_buf = new F[2];//в 0 хранится при y` в 1 всё остальное.
        for (int i = 0; i < c; i++) {
            if (func_buf[0] == null) func_buf[0] = func[k[i]]; else func_buf[0] = func_buf[0].add(func[k[i]], ring);
            }
            int y = 0;

        for (int i = 0; i < func.length; i++) {
                Boolean f = false;
                for (int j = 0; j < c; j++) {
                    f |= k[j] == i;
                }

                if (!f) {
                    if (func_buf[1] == null) func_buf[1] = func[i]; else  func_buf[1] = func_buf[1].add(func[i], ring);

                }
        }

        if(!func_buf[0].isOne(ring)){
            func_buf[1]= (F) ((F) func_buf[1].divide(func_buf[0], ring)).negate(ring);
            func_buf[1]= func_buf[1].expand(ring);
            func_buf[0] = new F("1", ring);
        }else{
            func_buf[1]=(F) func_buf[1].negate(ring);
        }


        dif_buf[0][n]=1;
        dif=dif_buf;
        func = func_buf;

        //т.к. не работает корень n-ой степени то закоменчено
       // func[1] = (F) func[1].rootTheFirst(p, ring);



        if (func[1].indexsOfVars(ring)[1] != 1 && func[1].indexsOfVars(ring)[0] == 1) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Проверяет является ли диф уравнение видаF(x,y^(k),y^(k+1),...,y^(n))==0, т.е не содержит явно искомой функции
     * @param ring
     * @return
     */
    public static Boolean Prov_DecB(Ring ring){

        //проверяем наличие функции y
        for(int i=0; i<func.length; i++){
            if(func[i].indexsOfVars(ring)[1]!=0){
                return false;
            }
        }
        k=dif[0].length-1;//хранится младшее значение поярдка производной(на 1 меньше)
         n=dif[0].length-1;//хранится старшее значение порядка производной(на 1 меньше)
        //ищем минимальное значение порядка производной
        for(int i=0; i<dif.length; i++){
            for(int j=0; j<dif[i].length; j++){
                if(k>j && dif[i][j]!=0){
                    k=j;
                    nk=i;
                }
            }
        }

        int c =k;
        //проверяем наличие всех проиводных от k+1 до n+1 порядка включительно.
        while (true && c <= n) {
            Boolean flag = false;
            for (int i = 0; i < dif.length; i++) {
                if (dif[i][c] > 0) {
                    flag = true;
                }
            }
            if (!flag) {
                return false;
            }
            c++;
        }

        F[] func_buf = new F[n-k+2];
        int[][] dif_buf = new int[n-k+2][n+1];
        boolean flag = true;
        int cc=k;
        int fk=0;
        int[] pr = new int[func.length];//для проверки использовался ли эл-т из func
        //группируем относительно производных.
//        while (flag && cc <= n && fk<n-k+2) {
//            for (int i = 0; i < func.length; i++) {
//                if (dif[i][cc] > 0) {
//                    if (func_buf[fk] == null) {
//                        func_buf[fk] = func[i];
//                        dif_buf[fk][cc] = dif[i][cc];
//                        pr[i] = 1;
//                    } else {
//                        func_buf[fk] = func_buf[fk].add(func[i], ring);
//                        dif_buf[fk][cc] = dif[i][cc];
//                        pr[i] = 1;
//                    }
//                }
//            }
//            fk++;
//            cc++;
//        }
//       // fk++;
//
//        for(int i=0; i<func.length; i++){
//            if(pr[i]==0){
//                if(func_buf[fk]==null) func_buf[fk]=func[i];
//                else func_buf[fk]=func_buf[fk].add(func[i], ring);
//                fk++;
//            }
//
//        }

//        dif = dif_buf;
//        func = func_buf;
        return true;
    }



    /**
     * проверяем является ли диф. ур. 1 порядка. и разрешим относительно y`
     * приводим входное диф уравнение к виду F(x,y)y`+G(x,y)=0
     *
     * @param ring
     * @return является ли уравнение с разделяющимися перемеными
     */
    public static Boolean Prov_1(Ring ring) {
        Boolean flag = true;

        /*
         * ищем максимальную степень dy и провверям чтобы все степени были
         * одинаковыми если это не так, то это не уравнение с разделяющимися
         * переменными.
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
    /*
     * если в функции одна переменная выдаёт истину
     */
    public static Boolean var(F f, Ring ring){
        Boolean flag =  f.indexsOfVars(ring)[0]!=0 && f.indexsOfVars(ring)[1]!=0;
        return !flag;
    }


    public static F[] fraction_factor(F f, Ring ring){
        CanonicForms cf = ring.CForm;//2015  new CanonicForms(ring);
        Fraction p = null;
        try {
            p = (Fraction) cf.ElementToPolynom(f, true);
        } catch (Exception e) {
            return new F[]{f};
        }
        F num=null;
        if(p.num instanceof F) num = (F) cf.ElementToF(p.num); else  num = new F(cf.ElementToF(p.num));
        F denom=null;
        if(p.num instanceof F) denom = (F) cf.ElementToF(p.denom); else  denom = new F(cf.ElementToF(p.denom));


        F[] anum = factor(num, ring);
        F[] adenom = factor(denom, ring);

//        System.out.println("" + Array.toString(anum));
//        System.out.println("" + Array.toString(adenom));
        ArrayList<Integer> bufi = new ArrayList<Integer>();//для num
        ArrayList<Integer> bufj = new ArrayList<Integer>();//для denom
        Boolean prov = false;
        for(int i=0; i<anum.length; i++){
            for(int j=0; j<adenom.length; j++){
                if(anum[i].compareTo(adenom[j], 0, ring)){
                    prov = true;
                    bufi.add(i);
                    bufj.add(j);
                }
            }
        }

        if(prov){
            for(int i=0; i< bufi.size();i++){
                anum[bufi.get(i)] = new F("1", ring);
                adenom[bufj.get(i)] = new F("1", ring);
            }
            anum=delete_one(anum, ring);
            adenom=delete_one(adenom, ring);
        }
//        prov = true;
//        for(int i=0; i<adenom.length; i++){
//            prov &=var(adenom[i], ring);
//        }
//        //если в знаменателе т.е. в массиве adenom присутствуют функции от двух переменных
//        //то прекращаем факторизацию и выдаём null
//      //  if(!prov) return null;
//
//        prov = true;
//        for(int i=0; i<anum.length; i++){
//            prov &=var(anum[i], ring);
//        }
//
//        //если в числителе т.е. в массиве anum присутствуют функции от двух переменных
//        //то прекращаем факторизацию и выдаём null
//      //  if(!prov) return null;


         ArrayList<F> res = new ArrayList<F>();


        F nf = null;
        for(int i=0; i<anum.length; i++){
            if(anum[i].indexsOfVars(ring)[0]==0 && anum[i].indexsOfVars(ring)[1]==0){
                if(nf==null){
                    nf=anum[i];
                    anum[i] = new F("1",ring);

                }else{
                    nf=(F) nf.multiply(anum[i], ring);
                    anum[i] = new F("1",ring);
                }
            }
        }

         F dnf = null;
        for(int i=0; i<adenom.length; i++){
            if(adenom[i].indexsOfVars(ring)[0]==0 && adenom[i].indexsOfVars(ring)[1]==0){
                if(dnf==null){
                    dnf=adenom[i];
                    adenom[i] = new F("1",ring);
                }else{
                    dnf=(F) dnf.multiply(adenom[i], ring);
                    adenom[i] = new F("1",ring);
                }
            }
        }

        if(dnf!=null){
            if(nf ==null)  nf = new F("1", ring);
             nf.expand(ring);
            dnf.expand(ring);
            res.add(((F)nf.divide(dnf, ring)).expand(ring));

        }else{
            if(nf!=null) res.add(nf);
        }


        for(int i=0; i<adenom.length; i++){
            Boolean r = false;//нашлась ли хотябы одна подходящая дробь

           for(int j=0; j<anum.length;j++){
               int var1 =adenom[i].indexsOfVars(ring)[1];
               int var2 =anum[j].indexsOfVars(ring)[1];
               if(var1==var2){
                   r=true;
                   res.add((F)anum[j].divide(adenom[i], ring));
                   anum[j] = new F("1", ring);
               }
           }
           if(!r){
               res.add((F)(new F("1", ring)).divide(adenom[i], ring));

            }

           adenom[i] = new F("1",ring);
        }

        for(int i=0; i<anum.length;i++){
            if(!anum[i].isOne(ring)){
                res.add(anum[i]);
            }
        }

        F[] ress = new F[res.size()];
        for (int i = 0; i < res.size(); i++) {
            ress[i] = res.get(i);
        }



        return ress;
    }
    /**
     * удаляет единичные элементы из массива функций
     * @param f входной массив
     * @param ring
     * @return
     */
    public static F[] delete_one(F[] f,Ring ring){
        F[] p = f.clone();
        ArrayList<F> buf = new ArrayList<F>();
            for(int i=0; i<p.length; i++){
                if(!f[i].isOne(ring)){
                    buf.add(p[i]);
                }
            }
            p=new F[buf.size()];
            for(int i=0; i<buf.size(); i++){
                p[i] = buf.get(i);
            }
        return p;
    }
    /**
     *
     * Факторизует функцию и выдаёт массив функций. Раскладывает если есть корни
     * @param f2
     * @param ring
     * @return
     */


    public static F[] factor( F f2, Ring ring) {
        Boolean flag = true;

        CanonicForms cf =ring.CForm;//2015  new CanonicForms(ring);
        Element pp = cf.ElementToPolynom(f2, true);
        F[] f = null;
        if (pp instanceof Polynom) {
            FactorPol fp1 = (FactorPol) pp.factor(cf.newRing);
            f = new F[fp1.multin.length];
            for (int i = 0; i < fp1.multin.length; i++) {

                Element buf = cf.ElementToF(fp1.multin[i]);
                if (buf instanceof F) {
                    if(fp1.powers[i]>1){
                        f[i] = (F) buf;
                        f[i] = (F) f[i].pow(fp1.powers[i], ring);
                    }else{
                        f[i] = (F) buf;
                    }
                } else {
                    if (fp1.powers[i] > 1) {
                        f[i] = new F(buf);
                        f[i] = (F) f[i].pow(fp1.powers[i], ring);
                    } else {
                        f[i] = new F(buf);
                    }
                }
            }
        }else{
            f = fraction_factor(f2, ring);
            if(f==null){
                return null;
            }
        }

        System.out.println(""+Array.toString(f));
        while (flag) {
            Boolean f1 = true;
            for (int i = 0; i < f.length; i++) {

                if(f[i].name == F.EXP){//если функция экспонента

                }

                if (f[i].name == F.ROOTOF || f[i].name ==F.SQRT) {//если функция целиком корень



                    NumberZ deg=null;
                    if(f[i].name == F.SQRT){
                        deg = new NumberZ(2);
                    }else{
                        deg = new NumberZ(f[i].X[1].intValue());
                    }
                    //newRing = null;
                    //2015
                    CanonicForms cf1 =  new CanonicForms(ring,true); //2015
                    if(f[i].name == F.ROOTOF || f[i].name ==F.SQRT){

                    }
                    Element p = cf1.ElementToPolynom(f[i].X[0], true);//факторизуем функцию под корнем
                    if (p instanceof Polynom) {
                        FactorPol fp = (FactorPol) p.factor(cf1.newRing);
                        if (fp.multin.length > 1) {
                            flag = true;
                            F[] buf = new F[fp.multin.length + f.length];
                            int c = 0;

                            for (int j = 0; j < buf.length; j++) {
                                if (j < f.length) {
                                    buf[j] = f[j];
                                } else {
                                    Element buf1 = cf1.ElementToF(fp.multin[c]);
                                    if (buf1 instanceof F) buf1 = (F) buf1; else  buf1 = new F(buf1);
                                    if (deg.intValue() != 2) {
                                        buf[j] = new F(F.ROOTOF, new Element[]{buf1, deg});
                                        c++;
                                    } else {
                                        buf[j] = new F(F.SQRT, new Element[]{buf1});
                                        c++;
                                    }
                                }
                            }
                            buf[i]=new F("1", ring);
                            f = buf.clone();
                        } else {
                            flag = false;
                        }
                    }else{
                      F[] buf = fraction_factor(f[i], ring);
                      f[i]=new F("1", ring);
                      if(buf==null) return null;
                      F[] buf1 = new F[buf.length+f.length];
                      int c=0;
                      for (int j = 0; j < buf1.length; j++) {
                          if(j<f.length){
                              buf1[j] = f[j];
                          }else{
                              buf1[j]=buf[c];
                          }

                        }
                      f=buf1.clone();
                    }

                } else {
                    f1 = false;
                }
            }
            flag &= f1;
        }

        if(f.length>1){
            f=delete_one(f, ring);
        }
        return f;
    }


    /**
     * Разделяем переменные одновременно проверяя возможно ли это, и записываем
     * ур-е в виде M(x)Q(y)y`+P(x)N(y)=0 при этом записывая P(x) в поле Px и
     * т.д.
     *
     * @param ring
     * @return является ли уравнение с разделяющимися перемеными
     */
    public static Boolean Prov_2(Ring ring) {

        F[] func0 = factor(func[0], ring);
        F[] func1 = factor(func[1], ring);
        if(func0==null) return false;
        if(func1==null) return false;
        Boolean prov=true;
        for(int i=0; i<func0.length; i++){
            prov&=var(func0[i], ring);
        }
        if(!prov) return false;

        prov=true;

         for(int i=0; i<func1.length; i++){
            prov&=var(func1[i], ring);
        }
        if(!prov) return false;
        Boolean h = false;

        for(int i=0; i<func0.length;i++){
            if(func0[i].indexsOfVars(ring)[0]==1 || (func0[i].indexsOfVars(ring)[0]==0 && func0[i].indexsOfVars(ring)[1]==0)){

                if(Mx !=null){
                    Mx=(F) Mx.multiply(func0[i], ring);
                }else{
                    Mx=func0[i];
                }
            }else{
                if(Qy !=null){
                    Qy=(F) Qy.multiply(func0[i], ring);
                }else{
                    Qy=func0[i];
                }
            }
        }

           for(int i=0; i<func1.length;i++){
            if(func1[i].indexsOfVars(ring)[0]==1 || (func1[i].indexsOfVars(ring)[0]==0 && func1[i].indexsOfVars(ring)[1]==0)){
                if(Px !=null){
                    Px=(F) Px.multiply(func1[i], ring);
                }else{
                    Px=func1[i];
                }
            }else{
                if(Ny !=null){
                    Ny=(F) Ny.multiply(func1[i], ring);
                }else{
                    Ny=func1[i];
                }
            }
        }


        if (Px == null) Px = new F("1", ring);

        if (Mx == null) Mx = new F("1", ring);

        if (Ny == null) Ny = new F("1", ring);

        if (Qy == null) Qy = new F("1", ring);

        return true;
    }
    public static String zam (String str, String var, String x){

            int ky = str.indexOf(var);
            int kx = str.indexOf(x);
            ArrayList<Integer> Y = new ArrayList<Integer>();//все вхождения var
            ArrayList<Integer> X = new ArrayList<Integer>();//все вхождения x
            Y.add(ky);
            X.add(kx);
            Boolean flag = true;
            while (flag) {

                Y.add(str.indexOf(var, Y.get(Y.size() - 1) + 1));
                if (Y.get(Y.size() - 1) == -1) {
                    flag = false;
                }

            }
            flag = true;
            while (flag) {

                X.add(str.indexOf(x, X.get(X.size() - 1) + 1));
                if (X.get(X.size() - 1) == -1) {
                    flag = false;
                }

            }
            int y = -1;
            String res1 = "";
            for (int i = 0; i < X.size(); i++) {
                if (X.get(i) != -1) {
                    if (i != 0) {
                        res1 += str.substring(X.get(i - 1) + 1, X.get(i) ) + var;
                    } else {
                        res1 += str.substring(0, X.get(i) ) + var;
                    }
                    y = X.get(i);
                }
            }
            res1+=str.substring(y+1, str.length());
            String res2 = "";
            y=-1;
               for (int i = 0; i < Y.size(); i++) {
                if (Y.get(i) != -1) {
                    if (i != 0) {
                        res2 += res1.substring(Y.get(i - 1) + 1, Y.get(i) ) + x;
                    } else {
                        res2 += res1.substring(0, Y.get(i) ) + x;
                    }
                    y = Y.get(i);
                }
            }
               res2+=res1.substring(y+1, res1.length());



        return res2;

    }
    public static F var_integrate(String var, F f, Ring ring, CanonicForms cf) throws Exception {
        if (!var.equals(ring.varNames[0])) {
            String res2 = zam(f.toString(ring), var, ring.varNames[0]);
            System.out.println("Исходное" + f);
            System.out.println("Меняем переменные x и y местами:" + res2);
             ring.CForm=cf;
            Element Ineg1 = (new Integrate()).integrate(new F(res2, ring), ring.varPolynom[0], ring );
            F integ = null;
            if (Ineg1 instanceof F) {
                integ = (F) Ineg1;

            } else {
                integ = new F(Ineg1);
            }
            System.out.println("Поулченный интеграл до перемены мест" + integ);
            String int2 = integ.toString(ring);
            res2 = "";
            res2 = zam(int2, var, ring.varNames[0]);
            System.out.println("вернули переменные назад " + res2);
            return new F(res2, ring);

        } else {
            return f;
        }



    }

//    public static void main(String[] args) throws Exception {
//        Ring ring = new Ring("R64[x,y]");
//
//
////        //=========================================================================================
////        //пример на разделяющиеся переменные
////        F dif1 = new F("\\systLDE(\\d(y,t,1)+2\\exp(x)=0)", ring);
////        F[] res = Solve(dif1, ring);
////        for (int i = 0; i < res.length; i++) {
////            System.out.println("" + res[i]);
////        }
////        //   System.out.println("res"+Array.toString(res,NewRing));
////        //=========================================================================================
//
//
//        //=========================================================================================
//        //пример на понижение дифференцирования, случай когда (y^(n)^p=f(x))
//        F dif1 = new F("\\systLDE(x\\d(y,t,5)-\\exp(x)=0)", ring);
//        F[] res = Solve(dif1, ring);
//        System.out.println("res"+Array.toString(res,newRing));
//        //=========================================================================================
//
//
//    }
}
