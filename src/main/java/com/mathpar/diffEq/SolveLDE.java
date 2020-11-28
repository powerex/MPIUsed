package com.mathpar.diffEq;

import com.mathpar.func.CanonicForms;
import com.mathpar.func.F;
import com.mathpar.func.Fname;
import com.mathpar.number.Element;
import com.mathpar.number.Fraction;
import com.mathpar.number.NumberZ;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
import com.mathpar.polynom.FactorPol;
import com.mathpar.polynom.Polynom;
import com.mathpar.webWithout.Play_On_Ground;

/**
 *
 * @author Sergey Glazkov 
 * февраль 2018
 * Класс решает дифференциальные уравнения Бернулли y'+a(x)y = b(x)y^n
 * И линейные неоднородные уравнения первого порядка y'+a(x)y = b(x)
 * Главный метод - solve
 */
public class SolveLDE {
    Ring ring = new Ring("Q[x,y]");//кольцо переменных с рациональными коэффициентами
    CanonicForms cf = new CanonicForms(ring, true);
    Element ax = ring.numberONE, bx = ring.numberONE;//будущие коэффициенты
    int n = 0;
    boolean find_der = false, find_ax = false, isLinear = false;
    Fraction N = new Fraction(ring.numberZERO, ring.numberONE);
    Element v, u, leftPart = ring.numberZERO, rightPart = ring.numberZERO;
    VectorS error = new VectorS(new Fname("ERROR: incorrect\\ equation")); 
    ProcedureSDE pr = new ProcedureSDE();
    
    public VectorS solve(Element f1, Element f2){
        //приводим входное уравнение к виду y'+a(x)y = b(x)y^n
        F f = cf.ExpandForYourChoise((F)f1.subtract(f2, ring), 1, -1, -1, -1, -1);
        
        for (int i = 0; i < f.X.length; i++) {
            Element mult = new SolveDESV().factor_d(f.X[i]);
            if(mult.isOne(ring)){//если при производной множитель 1, то нормальный вид
                break;
            }
            if(!mult.isZero(ring)){//если не 1 и не 0, то делим обе части на этот множитель
                for (int j = 0; j < f.X.length; j++)
                    f.X[j] = f.X[j].divide(mult, ring).simplify(ring);
                break;
            }
            if(i == f.X.length-1 && mult.isZero(ring)) return error;
        }
        //тут получили уравнение, приведенное к виду y'+a(x)y+b(x)y^n = 0;
        f = cf.ExpandForYourChoise(f, 1, -1, -1, -1, -1);
        
        rightPart = f;
        for (int i = 0; i < f.X.length; i++) {
            Element mult = new SolveDESV().factor_d(f.X[i]);
            if(!mult.isZero(ring)){//если встретили производную, кидаем её в левую часть
                
                leftPart = leftPart.add(f.X[i], ring);
                find_der = true;
                continue;
            }
            if(findAx(f.X[i])){//если нашли аргумент с y^1, оставляем её в левой части
                leftPart = leftPart.add(f.X[i], ring);
                find_ax = true;
                continue;
            }
        }
        if(find_der && find_ax == false) return error;
        rightPart = f.subtract(leftPart, ring).negate(ring).expand(ring);
        
        getAx(leftPart);//находим а(х) и b(x)
        getBx(rightPart);
        if(isLinear == false && n == 0 && N.isZero(ring)) return error;
        
        Element fd1 = new F("\\d(y,x)", ring).add(ring.varPolynom[1].multiply(ax, ring), ring);
        Element fd2 = ring.numberZERO;//решаем первое уравнение v'+a(x)*v = 0
        System.out.println("Eq1 = "+fd1);
        VectorS res = (VectorS)new SolveDESV().solve(fd1, fd2);
        pr.left = res.V[0];
        pr.right = res.V[1];
        if(pr.left.isNegative()){
            pr.left = pr.left.negate(ring);
            pr.right = pr.right.negate(ring);
        }
        pr.toNormalForm();
        v = pr.right.expand(ring);//нашли v
        System.out.println("v = "+v);
        
        //далее составим уравнение u'/(u^n) = b(x)*v^(n-1)
        if(n != 0 && N.num == ring.numberZERO){//если n - целое число
            fd1 = new F("\\d(y,x)", ring).divide(ring.varPolynom[1].pow(n, ring), ring);
            fd2 = bx.multiply(v.pow(n-1, ring), ring).simplify(ring);
        }
        if(n == 0 && N.num != ring.numberZERO){//если n - дробное число
            fd1 = new F(F.d, new Element[]{ring.varPolynom[1], ring.varPolynom[0]})
                    .divide(ring.varPolynom[1].pow(N, ring), ring);
            N.num = N.num.subtract(N.denom, ring).simplify(ring);
            Element mult = v.pow(N, ring).expand(ring);
            fd2 = bx.multiply(mult, ring).simplify(ring);
        }
        if(n == 0 && N.isZero(ring)){//линейное неоднородоное y'+a(x)y = b(x)
            System.out.println("Линейное неоднородное ур-е первого порядка");
            fd1 = new F("\\d(y,x)", ring).multiply(v, ring);
            fd2 = bx;
        }
        //и решим его
        res = (VectorS)new SolveDESV().solve(fd1, fd2);
        pr.left = res.V[0];
        pr.right = res.V[1].add(new Fname("C"), ring);
        if(pr.left.isNegative()){
            pr.left = pr.left.negate(ring);
            pr.right = pr.right.negate(ring);
        }
        pr.toNormalForm();
        u = pr.right;//нашли u
        try{
            u = u.expand(ring);
        } catch(Exception e){}
        System.out.println("u = "+u);
        //ответ получаем в виде y = uv
        System.out.println(">>> "+ring.varPolynom[1]+" = "+u.multiply(v, ring));
        return new VectorS(new Element[]{ring.varPolynom[1], u.multiply(v, ring)});
    }
    public void getBx(Element e){
        //когда вся правая часть e зависит от X, тогда это линейное неоднородное уравнение
        while(e instanceof F && ((F)e).name == F.ID) 
            e = ((F)e).X[0];
        if(pr.varX(e)){
            bx = e;//bx = правая часть, n по умолчанию = 0
            isLinear = true;
            System.out.println("Это линейное неоднородное уравнение");
            return;
        }
        
        //если правая часть - полином (один моном или сумма)
        if(e instanceof Polynom){
            Polynom p = (Polynom)e;
            FactorPol fp = (FactorPol) p.factor(ring);//раскладываем на простые множители
            for(int i = 0; i < fp.multin.length; i++){
                if(fp.multin[i].subtract(ring.varPolynom[1], ring).isZero(ring)){
                    //если среди них встретился y, bx = e / y^n
                    bx = p.divide(fp.multin[i].pow(fp.powers[i], ring), ring).simplify(ring);
                    n = fp.powers[i];
                    return;
                }
            }
        }
        //если правая часть - функция
        if(e instanceof F){
            F el = (F)e;
            //если правая часть - произведение
            if(el.name == F.MULTIPLY){
                //пока находятся множители, зависящие только от Х, переносим их в bx
                el = el.factor(ring);//факторизуем
                for(int i = 0; i < el.X.length; i++){
                    if(pr.varX(el.X[i])){//если множитель зависит только от Х
                        bx = bx.multiply(el.X[i], ring).simplify(ring);//bx = bx*множитель
                        el = (F)el.divide(el.X[i], ring).simplify(ring);//el = el/множитель
                        i = -1;//декрементируем индекс аргумента
                    }
                    //если на очередном шаге получили не произведение, например
                    //x*\sqrt(y)
                    if(el instanceof F && ((F)el).name != F.MULTIPLY) break;
                }
            }
            //если правая часть - деление
            if(el.name == F.DIVIDE){
                //если в числителе только Х
                if(pr.varX(el.X[0])){
                    bx = bx.multiply(el.X[0], ring).simplify(ring);
                    el.X[0] = ring.numberONE;//переносим числитель в bx
                }
                //если в числителе умножение
                if(el.X[0] instanceof F && ((F)el.X[0]).name == F.MULTIPLY){
                    F num = (F)el.X[0];//аналогично умножению выше
                    for(int i = 0; i < num.X.length; i++){
                        if(pr.varX(num.X[i])){
                            bx = bx.multiply(num.X[i], ring).simplify(ring);
                            num = (F)num.divide(num.X[i], ring).simplify(ring);
                            i = -1;
                        }
                    }
                    el.X[0] = num;
                }
                //если знаменатель зависит только от X
                if(pr.varX(el.X[1])){
                    bx = bx.divide(el.X[1], ring).simplify(ring);
                    el.X[1] = ring.numberONE;
                }
                //если в знаменателе умножение
                if(el.X[1] instanceof F && ((F)el.X[1]).name == F.MULTIPLY){
                    F denom = (F)el.X[1];
                    for(int i = 0; i < denom.X.length; i++){
                        if(pr.varX(denom.X[i])){
                            bx = bx.divide(denom.X[i], ring).simplify(ring);
                            denom = (F)denom.divide(denom.X[i], ring).simplify(ring);
                            i = -1;
                        }
                    }
                    el.X[1] = denom;
                }
                //если в знаменателе полином
                if(el.X[1] instanceof Polynom){
                    Polynom p = (Polynom) el.X[1];
                    FactorPol fp = (FactorPol) p.factor(ring);//факторизуем его
                    for(int i = 0; i < fp.multin.length; i++){
                        if(fp.multin[i].subtract(ring.varPolynom[1], ring).isZero(ring)){
                            //если находим y в какой-то степени
                            N = new Fraction(ring.numberMINUS_ONE, new NumberZ(fp.powers[i]));
                            p = (Polynom)p.divide(fp.multin[i].pow(fp.powers[i], ring), ring).simplify(ring);
                            bx = bx.divide(p, ring);
                            return;
                        }
                    }
                }
                el = el.simplify(ring);
            }

            if(el.name == F.ID) e = el.X[0];
            if(el.name == F.POW){//если правая часть - степенная функция над полиномом
                if(el.X[0] instanceof Polynom && pr.varY(el.X[0])){
                    Polynom p = (Polynom) el.X[0];
                    if(el.X[1] instanceof Fraction){
                        N = (Fraction)el.X[1].multiply(new NumberZ(p.powers[1]), ring);
                    }
                }
            }
        }
        //до сюда может дойти только e = y^n
        if(e instanceof Polynom && ((Polynom)e).powers[0] == 0)
            n = ((Polynom)e).powers[1];

        System.out.println("b(x) = "+bx);
        System.out.println("n = "+n);
        System.out.println("N = "+N);
    }
    private void getAx(Element e){
        F el = new F();
        if(e instanceof F) el = (F)e;
        //находим в левой части уравнения слагаемое без производной, делим на y,
        //получаем a(x)
        el = cf.ExpandForYourChoise(el, 1, -1, -1, -1, -1);
        if(el.name == F.ADD){
            for (int i = 0; i < el.X.length; i++) {
                if(pr.isDerivative(el.X[i])){ 
                    el = (F)el.subtract(el.X[i], ring);
                    break;
                }
            }
            ax = el.divide(ring.varPolynom[1], ring).simplify(ring);
        }
        System.out.println("a(x) = "+ax);
    }
    //определяет, явл. ли e вида a(x)y
    private boolean findAx(Element e){
        Element chastnoe = e.divide(ring.varPolynom[1], ring).expand(ring);
        return pr.varX(chastnoe);
    }
    public static void main(String[] args) throws Exception{
        String s1 = "SPACE=Q[x,y];f=\\solveDE(x\\d(y,x) + y = y^2\\ln(x));";
        String s2 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) + y/\\sqrt(1-x^2) = y^2\\arcsin(x)/\\sqrt(1-x^2));";
        String s3 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) + y = 3\\exp(-2x)y^2);";
        String s4 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) - 2y/x = 2x\\sqrt(y));";
        String s5 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) + y/(x+1) = -y^2)";
        String s6 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) - 2y/x = -x^2y^2);";
        String s7 = "SPACE=Q[x,y];f=\\solveDE(x\\d(y,x) - 4y = x^2\\sqrt(y));";
        String s8 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) + 3y/x = x^3y^3);";
        String s9 = "SPACE=Q[x,y];f=\\solveDE(2\\d(y,x) - 3y\\cos(x) = -\\exp(-2x)(2+3\\cos(x))/y);";
        
        String s10 = "SPACE=Q[x,y];f=\\solveDE(x\\d(y,x) - y = 2x^3);";
        String s11 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) + 2xy = x\\exp(-x^2));";
        String s12 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) + y\\tg(x) = 1/\\cos(x));";
        String s13 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) + y/x = 2\\exp(x^2));";
        String s14 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) - 2y/(x+1) = (x+1)^3);";
        String s15 = "SPACE=Q[x,y];f=\\solveDE(x^2\\d(y,x) - 2xy = 3);";
        String s16 = "SPACE=Q[x,y];f=\\solveDE(x\\d(y,x) + (x+1)y = 3x^2\\exp(-x));";
        String s17 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) - 2xy = 2x^3);";
        String s18 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) + 4y/(4x-3) = \\ln(x));";
        String s19 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) - y\\tg(x) = \\sin(x));";
        String s20 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x) + 3y/x = 2/x^2);";
        
        Play_On_Ground.startDebug(s4);
    }    
}
