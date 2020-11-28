package com.mathpar.diffEq;

import com.mathpar.func.CanonicForms;
import com.mathpar.func.F;
import com.mathpar.func.Fname;
import com.mathpar.number.Array;
import com.mathpar.number.Element;
import com.mathpar.number.Fraction;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
//import com.mathpar.polynom.FactorPol;
import com.mathpar.polynom.Polynom;
import com.mathpar.webWithout.Play_On_Ground;

//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;

public class SolveDE {
    /** Это корневой класс для решения дифференциальных уравнений
     * Главный метод - solve
     * @param f - differential equation (left part minus right part)
     * @param r - Ring
     * @return  
     */    
    Ring ring = new Ring("Q[x,y]");
    Ring HDEring = new Ring("Q[x,y,z]");
    CanonicForms cf = new CanonicForms(ring, true);
    Element P = ring.numberZERO, Q = ring.numberZERO, rightPart, leftPart;
    Element f = ring.numberZERO, Solution;
    SolveDESV sdesv = new SolveDESV();
    ProcedureSDE pr = new ProcedureSDE(); 
    Element[] v;
        
    public Element solve(Element f1, Element f2) { 
//        System.out.println("Задано уравнение: "+f1+" = "+f2);
        //если оба выражения - не F, то в них нет даже функции производной/дифференциала
        if(!(f1 instanceof F || f2 instanceof F)) return sdesv.errorVectS;
        //блок, убирающий функции деления f1/g1 = f2/g2 ===>>> f1*g2 = f2*g1
        if(f1 instanceof F){
            F f = (F)f1;//убираем деление в f1
            if(f.name == F.DIVIDE){
                f2 = f2.multiply(f.X[1], ring).expand(ring);
                f1 = f.X[0].expand(ring);
            }            
        }
        if(f2 instanceof F){
            F f = (F)f2;
            if(f.name == F.DIVIDE){
                f1 = f1.multiply(f.X[1], ring).expand(ring);
                f2 = f.X[0].expand(ring);
            }            
        }
        if(f2 instanceof Fraction){//если f2 - дробь
            f1 = f1.multiply(((Fraction)f2).denom, ring);
            f2 = ((Fraction)f2).num;
        }
        //если уравнение задано через дифференциалы
        if(containsDifferencial(f1.subtract(f2, ring).simplify(ring))){
            if(!split(f1.subtract(f2, ring).simplify(ring))) return sdesv.errorVectS;
            f = new F("\\d(y,x)", ring).multiply(Q, ring).add(P, ring);
        }
        //если задано через y'
        else{
            f = f1.subtract(f2, ring);
            toDifferencial((F)f);//приводим его к Pdx+Qdy=0
        }
// System.out.println("P = "+P+"\nQ = "+Q);
        
        //Если P'_y = Q'_x - ур-е в полных дифференциалах
        if(isETD(P, Q)){
            try{Solution = new SolveETD().solve(P, Q);}
            catch(Exception e){}
            if(Solution != null){
                v = ((VectorS) Solution).V;
                if(v.length == 2){
//                    System.out.println("======================================================="
//                                   + "\nЭто уравнение в полных дифференциалах"
//                                   + "\n=======================================================");
//                    System.out.println("Уравнение: $"+f1+" = "+f2+"$");
 //                   //save("/home/sergey/ETD.txt", f1, f2, v[0].add(new Fname("C"), ring), v[1]);
//                    System.out.println("Решение: "+v[0].add(new Fname("C"), ring)+" = "+v[1]+"$\\\\ \n");
                    return new F(F.EQUATION,  v[0].add(new Fname("C"), ring), v[1]);
                }
            }
        }
        //Если уравнение однородное
        if(isHDE(P, Q)){
            Solution = new SolveHDE().solve(f1, f2);
            v = ((VectorS) Solution).V;
            if(v.length == 2){
//                System.out.println("======================================================="
//                                + "\nЭто однородное уравнение"
//                                + "\n=======================================================");
//                System.out.println("Уравнение: $"+f1+" = "+f2+"$");
//                System.out.println(v[0]+" = "+v[1].add(new Fname("C"), ring));
                pr.left = v[0];
                pr.right = v[1].add(new Fname("C"), ring);
                pr.toNormalForm();
        //        save("/home/sergey/HDE.txt", f1, f2, pr.left, pr.right);
       //         System.out.println("Решение: $"+pr.left+" = "+pr.right+"$\\\\ \n");
                return new F(F.EQUATION,  pr.left, pr.right);
            }
        }
        
        //Решение уравнения с разделяющимися переменными
        Solution = new SolveDESV().solve(f, ring.numberZERO);
        v = ((VectorS) Solution).V;
        if(v.length == 2){
//            System.out.println("======================================================="
//                            + "\nЭто уравнение c разделяющимися переменными"
//                            + "\n=======================================================");
//            System.out.println("Уравнение: $"+f1+" = "+f2+"$");
//            System.out.println(v[0]+" = "+v[1].add(new Fname("C"), ring));
            pr.left = v[0];
            pr.right = v[1].add(new Fname("C"), ring);
            pr.toNormalForm();
     // ЭТУ команду и процедуру сохранения файла нельзя грузить на СЕРВЕР.
     //  Для отладки можно это раскомментировать. Но перед обновлением - снова закомментировать!!
    //        save("/home/sergey/DESV.txt", f1, f2, pr.left, pr.right);
   //         System.out.println("Решение: $"+pr.left+" = "+pr.right+"$\\\\ \n");
            return new F(F.EQUATION,  pr.left, pr.right);
        }
        //Неоднородное уравнение (линейное или Бернулли)
        Solution = new SolveLDE().solve(f1, f2);
        v = ((VectorS) Solution).V;
        if(v.length == 2){
  //          System.out.println("======================================================="
  //                          + "\nЭто линейное неоднородное ур-е или ур-е Бернулли"
  //                          + "\n=======================================================");
  //          System.out.println("Уравнение: $"+f1+" = "+f2+"$");
 //           System.out.println(v[0]+" = "+v[1]);
            pr.left = v[0];
            pr.right = v[1];
            pr.toNormalForm();
            //save("/home/sergey/LDE.txt", f1, f2, pr.left, pr.right);
   //         System.out.println("Решение: $"+pr.left+" = "+pr.right+"$\\\\ \n");
            return new F(F.EQUATION,  pr.left, pr.right);
        }
        if(P instanceof Polynom && Q instanceof Polynom){
            VectorS res = cf.solveAlgMatrixs(new VectorS(new Element[]{P, ring.numberZERO, Q, ring.numberZERO}), new int[]{0,1}, true);
            f1 = pr.toHDE(f1, res).factor(ring);
            f2 = pr.toHDE(f2, res);
            Solution = new SolveHDE().solve(f1, f2);
            v = ((VectorS) Solution).V;
            if(v.length == 2){
                return new F(F.EQUATION,  v[0], v[1]);
            }
        }
        return sdesv.errorVectS;
    }
    
    /**
     * Проверяет наличие дифференциалов в функции
     * @param el
     * @return true, если есть дифференциал
     */
    private boolean containsDifferencial(Element el){
        if(el instanceof F){
            F f = (F)el;
            if(f.name == F.d && f.X.length == 1) return true;
            for (int i = 0; i < f.X.length; i++)
                if(containsDifferencial(f.X[i])) return true;
        }
        return false;
    }
    /** Выражает из уравнения P и Q
     * @param el уравнение с правой частью = 0
     * @return true, если нормально задано уравнение, false, если уравнение неправильное
     */
    public boolean split(Element el){
        cf = new CanonicForms(ring, true);
        Polynom p_conv = cf.PolynomialConvert((F)el);//конвертируем в полином.
        if(p_conv == null) return false;
        Polynom[] monoms = p_conv.toMonomials(cf.newRing);
        for (int i = 0; i < monoms.length; i++) {
            System.out.println(monoms[i]);
            Element e = sdesv.unconvert(cf, monoms[i]);//восстанавливаем моном в элемент
            if(e instanceof F){//если получили функцию, а это так и должно быть, потому что f(x,y)*\d(x)
                F f = (F)e;
                if(f.name == F.d && f.X.length == 1){
                    if(f.X[0].subtract(ring.varPolynom[0], ring).isZero(ring))
                        P = P.add(ring.numberONE, ring);
                    if(f.X[0].subtract(ring.varPolynom[1], ring).isZero(ring))
                        Q = Q.add(ring.numberONE, ring);
                    continue;
                }
                if(f.name == F.MULTIPLY){
                    for (int j = 0; j < f.X.length; j++) {
                        if(f.X[j] instanceof F && ((F)f.X[j]).name == F.d && ((F)f.X[j]).X.length == 1){
                            F d = (F)f.X[j];
                            if(d.X[0].subtract(ring.varPolynom[0], ring).isZero(ring))
                                P = P.add(e.divide(d, ring), ring).simplify(ring);
                            if(d.X[0].subtract(ring.varPolynom[1], ring).isZero(ring))
                                Q = Q.add(e.divide(d, ring), ring).simplify(ring);
                            continue;
                        } 
                    }
                    continue;
                }
                return false;//если ни дифференциал, ни произведение, 
                //значит есть элемент свободный от дифференциала
            }
            return false;
        }
        return true;
    }
//    private boolean split(Element el){//
//        if(!(el instanceof F)) return false;//если на входе не функция, сразу выходим из метода
//        F f = cf.ExpandForYourChoise((F)el, 1, -1, -1, -1, -1);//поднимаем сумму
//        if(f.name != F.ADD || f.X.length != 2) return false;
//        for (int i = 0; i < f.X.length; i++) {
//            if(!(f.X[i] instanceof F)) return false;
//            if(((F)f.X[i]).name == F.d && ((F)f.X[i]).X.length == 1){
//                F d = (F)f;
//                if(d.X[0].equals(new Polynom("x", ring))){
//                    if(!P.isZero(ring)) return false;
//                    P = ring.numberONE;
//                }
//                if(d.X[0].equals(new Polynom("y", ring))){
//                    if(!Q.isZero(ring)) return false;
//                    Q = ring.numberONE;
//                }
//                continue;
//            }
//            if(((F)f.X[i]).name == F.MULTIPLY){
//                F ff = (F)f.X[i];
//                for (int j = 0; j < ff.X.length; j++) {
//                    if(ff.X[j] instanceof F && ((F)ff.X[j]).name == F.d && ((F)ff.X[j]).X.length == 1){
//                        Element arg = ff.divide(ff.X[j], ring).simplify(ring);
//                        F d = (F)ff.X[j];
//                        if(d.X[0].equals(new Polynom("x", ring))){
//                            if(!P.isZero(ring)) return false;
//                            P = arg;
//                        }
//                        if(d.X[0].equals(new Polynom("y", ring))){
//                            if(!Q.isZero(ring)) return false;
//                            Q = arg;
//                        }
//                    }
//                }
//            }
//            else return false;
//        }
//        return true;
//    }
    private boolean isETD(Element P, Element Q){
        if(P.isZero(ring) || Q.isZero(ring)) return false;
        Element Py = P.D(1, ring);
        Element Qx = Q.D(0, ring);
        return Py.subtract(Qx, ring).expand(ring).isZero(ring);
    }
    
    public void toDifferencial(F f){
        leftPart = sdesv.group_derivative(f.simplify(ring));
        rightPart = ring.numberZERO;
        find(leftPart);
        Q = sdesv.factor_d(leftPart);
        P = rightPart.negate(ring);
    }
    /**
     * Ищет место производной в части.
     * @param e - часть входного уравнения.
     * @return - true, если производная найдена. false - если не найдена.
     */
    public boolean find(Element e){
        if(e instanceof F){
            if(pr.isDerivative(e)) return true;
            F f = (F)e;
            if(f.name == F.MULTIPLY)
                for (int i = 0; i < f.X.length; i++){
                    if(pr.isDerivative(f.X[i])) return true;
                    if(f.X[i] instanceof F && ((F)f.X[i]).name == F.MULTIPLY)
                            return find(f.X[i]);
                    if(f.X[i] instanceof F && (((F)f.X[i]).name == F.ADD || ((F)f.X[i]).name == F.SUBTRACT)){
                        F f1 = (F)f.X[i];
                        for (int j = 0; j < f1.X.length; j++)
                            if(pr.isDerivative(f1.X[j])){
                                e = e.Expand(ring);
                                return find(e);
                            }
                    }
                }
            cf = new CanonicForms(ring, true);
            f = cf.ExpandForYourChoise(f, 1, -1, -1, -1, -1);
            if(f.name == F.ADD)
                for (int i = 0; i < f.X.length; i++) {
                    if(pr.isDerivative(f.X[i])){
                        leftPart = f.X[i];
                        for (int j = 0; j < f.X.length; j++)
                            if(j != i)
                                rightPart = rightPart.subtract(f.X[j], ring);
                        return true;
                    }
                    if(f.X[i] instanceof F && ((F)f.X[i]).name == F.MULTIPLY)
                        if(find(f.X[i])){
                            leftPart = f.X[i].factor(ring);
                            for (int j = 0; j < f.X.length; j++)
                                if(j != i)
                                    rightPart = rightPart.subtract(f.X[j], ring);
                            return true;
                        }
                }
        }
        return false;
    }
    
    private boolean isHDE(Element P, Element Q){
        try{
            Element temp_P = P.simplify(ring);
            Element new_P = HDE_replace(temp_P).simplify(HDEring);
            Element temp_Q = Q.simplify(ring);
            Element new_Q = HDE_replace(temp_Q).simplify(HDEring);
            new_P = sdesv.findRoot(new_P, true, HDEring);
            new_Q = sdesv.findRoot(new_Q, true, HDEring);
            
            Element multP = new_P.divide(temp_P, HDEring).simplify(HDEring);
            Element multQ = new_Q.divide(temp_Q, HDEring).simplify(HDEring);
            if(multP.subtract(multQ, HDEring).simplify(HDEring).isZero(HDEring)){
                System.out.println("isHDE: однородное уравнение");
                return true;
            }
        }
        catch(Exception e){System.out.println("Падение в isHDE");}
        System.out.println("isHDE: уравнение неоднородное");
        return false;
    }
    /**
     * Рекурсивная функция замены для метода выше
     * @param e
     * @return
     */
    private Element HDE_replace(Element e){
        if(e instanceof F){
            F f = (F)e;
            if(f.name == F.d && pr.isDerivative(f)){
                return e;
            }
            cf = new CanonicForms(ring, true);
            f = cf.ExpandForYourChoise(f, 1, 0, 0, 0, 0);
            if(f.name == F.ID) return HDE_replace(f.X[0]);
            for (int i = 0; i < f.X.length; i++)
                f.X[i] = HDE_replace(f.X[i]);
            return f;
        }
        if(e instanceof Polynom){
            Element f = ring.numberZERO;
            Polynom[] monoms = ((Polynom)e).toMonomials(ring);
            for (Polynom p : monoms){
                int c = 0;
                for (int i = 0; i < p.powers.length; i++)
                    c += p.powers[i];
                f = f.add(p.multiply(HDEring.varPolynom[2].pow(c, ring), ring), ring);
            }
            return f;
        }
        if(e instanceof Fraction){
            Fraction f = (Fraction)e;
            f.num = HDE_replace(f.num);
            f.denom = HDE_replace(f.denom);
            return f;
        }
        if(e.isItNumber()) return e;
        return ring.numberZERO;
    }
    /**
     *
     * @param f - левая часть дифференциального уравнения. Правая часть равна нулю.
     * @param r Ring
     *
     * @return null - если правая часть не является диф.ур;
     *       . NaN - если дифф. уравнение не решается. 
     *         VectorS - массив коэффициентов
     */
    public Element solve1(Element f, Ring r) {
        //массив порядков производных функций
        int[] pow = new int[0];
        //массив позиций производных функций
        int[] pos = new int[0];
        int l = 0;
        //если входное выражение - функция
        if (f instanceof F) {
            pow = new int[((F) f).X.length];
            pos = new int[((F) f).X.length];
            for (Element X : ((F) f).X) {
                //если аргумент - функция
                if (X instanceof F) {
                    //если производная
                    if (((F) X).name == F.d) {
                        pos[l] = 0;
                        pow[l] = (((F) X).X.length == 3)? 
                              ((F) X).X[2].intValue():1;
                            
                     l++;
                    } else {
                        //если сложная суперпозия
                        //счетчик количества производных функций
                        int k = 0;
                        for (int i = 0; i < ((F) X).X.length; i++) {
                            Element X1 = ((F) X).X[i];
                            if (X1 instanceof F) {
                                //если аргумент - производная
                                if (((F) X1).name == F.d) {
                                    k++;
                                    pow[l] = (((F) X1).X.length == 3)?
                                            ((F) X1).X[2].intValue():1;
                                        pos[l] = i;
                                        l++;
                                    
                                }
                            }
                        }
                        //случай когда несколько производных
                        if (k > 1) {
                            return Element.NAN;
                        }
                    }
                } else {
                    //случай когда есть все кроме производной 
                    pow[l] = 0;
                    pos[l] = -1;
                    l++;
                }
            }
        } else {
            //входное выражение не содержит функций и не является диф.ур.
            return null;
        }
        //максимальное значение порядка производной
        int maxPow = Array.max(pow);
        //создаем массив результатов
        Element[] result = new Element[maxPow+1];
        for(int i = 0; i < result.length; i++){
            result[i] = r.numberZERO;
        }
        for (int i = 0; i < ((F) f).X.length; i++) {
            //если производной нет
            if (pos[i] == -1) {
                result[pow[i]] = ((F) f).X[i];
            } else {
                Element el = ((F) ((F) f).X[i]);
                //длина аргументов
                int len = ((F) el).X.length;
                //если производная единственная функция в суперпозиции
                if (((F) el).name == F.d) {
                    result[pow[i]] = r.numberONE;
                } else {
                    //новая длина аргументов
                    int newLen = len - 1;
                    if (newLen == 1) {
                        if (pos[i] == 0) {
                            result[pow[i]] = ((F) el).X[1];
                        } else {
                            result[pow[i]] = ((F) el).X[0];
                        }
                    } else {
                        //новый массив аргументов
                        Element[] newEl = new Element[newLen];
                        int n = 0;
                        for (int j = 0; j < ((F) el).X.length; j++) {
                            if (j != pos[i]) {
                                newEl[n] = ((F) el).X[j];
                                n++;
                            }
                        }
                        result[pow[i]] = new F(((F) el).name, newEl);
                    }
                }
            }
        }
        return new VectorS(result);
    }
    public static void main(String[] args) throws Exception {
        String s1 = "SPACE=Q[x,y];f=\\solveDE(x\\d(y,x) + y = y^2\\ln(x));";
        String s2 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) + y/\\sqrt(1-x^2) = y^2\\arcsin(x)/\\sqrt(1-x^2));";
        String s3 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) + y = 3\\exp(-2x)y^2);";
        String s4 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) - 2y/x = 2x\\sqrt(y));";
        String s5 = "SPACE=Q[x,y];f=\\solveDE(6xy\\d(y,x)+4y\\d(y,x) = 3x^2-3y^2+4x);";
        String s6 = "SPACE=Q[x,y];f=\\solveDE((3x^2-3y^2+4x)\\d(x)-(6xy+4y)\\d(y) = 0);";
        String s7 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) = (x+2y-3)/(4x-y-3));";
        
        
        Play_On_Ground.startDebug(s7);
    }
    
//    private void save(String patch, Element f1, Element f2, Element left, Element right){
//        try {
//            File file = new File(patch);
//            if(!file.exists()) file.createNewFile();
//            FileReader fr = new FileReader(file);
//            int size = (int)file.length();
//            char[] cbuf = new char[size];
//            fr.read(cbuf);
//            String source = new String(cbuf);
//            source += "Уравнение: "+f1+" = "+f2+"\nРешение: "+left+" = "+right+"\n\n";
//            FileWriter fw = new FileWriter(file);
//            fw.write(source);
//            fw.flush();
//            fw.close();
//        } catch (IOException ex) {
//            Logger.getLogger(SolveDE.class.getName()).log(Level.SEVERE, null, ex);
//        }            
//    }
} 