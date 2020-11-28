package com.mathpar.diffEq;

import com.mathpar.diffExpression.DiffEquation;
import com.mathpar.func.F;
import com.mathpar.func.CanonicForms;
import com.mathpar.func.Fname;
import com.mathpar.func.Integrate;
import com.mathpar.func.Page;
import com.mathpar.number.Element;
import com.mathpar.number.Fraction;
import com.mathpar.number.NumberZ;
import com.mathpar.number.Ring;
import com.mathpar.number.VectorS;
import com.mathpar.polynom.FactorPol;
import com.mathpar.polynom.Polynom;
import com.mathpar.webWithout.Play_On_Ground;
import java.util.ArrayList;

/** Класс для решения дифф уравнений с разделяющимися (разделенными) переменными
 * Главный метод - solve
 * @author Sergey Glazkov
 * июнь 2015
 */
public class SolveDESV {
    Ring ring = new Ring("Q[x,y]");
    //Polynom x = new Polynom("x", ring), y = new Polynom("y", ring);
    ArrayList<Element> elemsX = new ArrayList<>(), elemsY = new ArrayList<>();
    Element left = ring.numberONE, right = ring.numberONE;
    Element leftPart = new Element(), rightPart = new Element();
    CanonicForms cf = new CanonicForms(ring, true);
    String errorString =  "ERROR in SolveDESV: incorrect\\ input equation"; 
    VectorS errorVectS =  new VectorS(new Fname(errorString)); 
    boolean findder = false;
    int num_var;//кол-во переменных в кольце ring
    ProcedureSDE pr = new ProcedureSDE();
        
    public SolveDESV(){
        elemsY.add(ring.numberONE);
        elemsX.add(ring.numberONE);
        num_var = ring.varPolynom.length;
        findder = false;
    }
    public SolveDESV(Ring ring){
        elemsY.add(ring.numberONE);
        elemsX.add(ring.numberONE);
        this.ring = ring;
        cf = new CanonicForms(ring, true);
        num_var = ring.varPolynom.length;
        findder = false;
    }
    /**
     * Возвращает элемент, обратный данному.
     * @param e исходный элемент
     * @return обратная функция
     */
    public Element revers(Element e){
        return new F(F.DIVIDE, new Element[]{ring.numberONE, e});
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
                            leftPart = f.X[i].expand(ring);
                            for (int j = 0; j < f.X.length; j++)
                                if(j != i)
                                    rightPart = rightPart.subtract(f.X[j], ring);
                            return true;
                        }
                }
        }
        return false;
    }
    /**
     * Возвращает множители при производной.
     * @param e функция/произведение.
     * @return 0 - если на входе не функция;
     * 1 - если на входе производная;
     * сомножители производной, если на входе произведение функций.
     */
    public Element factor_d(Element e){
        if(e instanceof F){
            F f = (F)e;
            if(pr.isDerivative(f)) return ring.numberONE;
            if(f.name == F.MULTIPLY)
                for (int i = 0; i < f.X.length; i++)
                    if(pr.isDerivative(f.X[i]))
                        return f.divideExact(f.X[i], ring);
        }
        return ring.numberZERO;
    }
    /**
     * Группирует слагаемые с производными и выносит производную за скобки.
     * Пример: на входе: y+y'_x+1+x*y'_x. На выходе: y'_x*(1+x)+y+1.
     * @param f входное выражение.
     * @return упрощенное выражение.
     */
    public Element group_derivative(F f){
        if(pr.isDerivative(f)) return f;
        if(f.name == F.ADD || f.name == F.SUBTRACT){
            f = cf.ExpandForYourChoise(f, 1, -1, -1, -1, -1);
            Element expand = f;
            ArrayList<Element> list = new ArrayList<>();
            for (int i = 0; i < f.X.length; i++)
                if(f.X[i] instanceof F)
                    if(!factor_d(f.X[i]).isZero(ring)){
                        expand = new Page(ring).expandFnameOrId(expand.subtract(f.X[i], ring).expand(ring));
                        list.add(factor_d(f.X[i]));
                    }
            if(list.isEmpty()) return f;
            Element[] coeffs = new Element[list.size()];
            list.toArray(coeffs);
            expand = new F(F.d, new Element[]{ring.varPolynom[ring.varPolynom.length-1], ring.varPolynom[0]}).multiply(new F(F.ADD, coeffs), ring).add(expand, ring);
            return expand;
        }
        return f;
    }
    /**
     * Раскладывает экспоненту в произведение экспонент.
     * @param f входная функция.
     * @return произведение.
     */
    public Element factor_exp(F f){
        if(f.name == F.EXP){//если это - экспонента
            Element expans = ring.numberONE;
            Element arg = f.X[0];
            if(arg instanceof F) arg = cf.ExpandForYourChoise((F)arg, 1, -1, -1, -1, -1);//показатель степени экспоненты
            
            if(!varX(f) && !varY(f)){
                if(arg instanceof F && (((F)arg).name == F.ADD || ((F)arg).name == F.SUBTRACT)){
                    F ff = (F)arg;//её аргумент либо полином
                    expans = new F(F.EXP, ff.X[0]);
                    for (int i = 1; i < ff.X.length; i++)
                        expans = expans.multiply(new F(F.EXP, ff.X[i]), ring);
                }
            }
            if(arg instanceof F && (((F)arg).name == F.LN)){
                F ff = (F)arg;//её аргумент либо полином
                expans = ff.X[0];
            }
            if(arg instanceof F && (((F)arg).name == F.MULTIPLY)){
                F ff = (F)arg;
                if(ff.X[0].isItNumber(ring) && ff.X[0].isMinusOne(ring)){
                    f = new F(F.EXP, ff.X[1].negate(ring));
                    return factor_exp(f);
                }
                if(ff.X[0].isItNumber(ring) && ff.X[1] instanceof F && ((F)ff.X[1]).name == F.LN){
                    F ln = ((F)ff.X[1]);
                    if(ln.X[0] instanceof F && ((F)ln.X[0]).name == F.ABS)
                        ln.X[0] = ((F)ln.X[0]).X[0];
                    expans = ((F)ff.X[1]).X[0].pow(ff.X[0], ring);
                }
            }
            if(arg instanceof Polynom && ((Polynom)arg).coeffs.length > 1){
                Polynom[] p = cf.dividePolynomToMonoms((Polynom)f.X[0]);
                for (int i = 0; i < p.length; i++)//либо функция
                    expans = (F)expans.multiply(new F(F.EXP, p[i]), ring);
            }
            if(expans.isOne(ring)) return f;
            else return expans;
        }
        if(f.name == F.MULTIPLY)
            for (int i = 0; i < f.X.length; i++) 
                if(f.X[i] instanceof F)
                    f.X[i] = factor_exp((F)f.X[i]);
        return f;
    }
    /**
     * Разбор левой части уравнения
     * @return true в случае удачи
     */
    public boolean solve_left_part(){
        if(leftPart instanceof F){//Проверка на всякий случай
            if(pr.isDerivative(leftPart)) return true;//если производная, сразу делаем возврат true;
            leftPart = factor_exp((F)leftPart);
            Polynom p_conv = cf.PolynomialConvert((F)leftPart);//конвертируем в полином.
            if(p_conv == null) return false;
            FactorPol fp = (FactorPol) p_conv.factor(cf.newRing);
            for (int i = 0; i < fp.multin.length; i++) {
                Element e = unconvert(cf, fp.multin[i]);
                if(e.isItNumber()){ elemsY.add(e); left.multiply(e, ring); continue;}
                if(pr.isDerivative(e) || pr.isDerivative(e.negate(ring))){
                    if(fp.powers[i] > 1) return false; //если это производная в степени > 1
                    if(findder) return false; //Если производная встречается не первый раз
                    findder = true;
                    if(e.isNegative()) elemsY.add(ring.numberMINUS_ONE);
                    continue;
                }
                if(varX(e)){//Если q состоит из Х, отправляем в elemsX
                    elemsX.add(revers(e.pow(fp.powers[i], ring)));
                    continue;
                } 
                if(varY(e)){//Если q состоит из Y, отправляем в elemsY.
                    elemsY.add(e.pow(fp.powers[i], ring)); 
                    continue;
                }
                if(e instanceof F){//Если функция зависит от разных переменных
                    Element fr = factorRoot(e, false, ring);
                    if(!varX(fr) && !varY(fr))
                        fr = factor_exp((F)e);
                    F f = (F)fr;
                    for (Element el:f.X) {
                        if(el.isItNumber()){ elemsY.add(el.pow(fp.powers[i], ring)); continue;}
                        if(varX(el)){ elemsX.add(revers(el.pow(fp.powers[i], ring))); continue;} 
                        if(varY(el)){ elemsY.add(el.pow(fp.powers[i], ring)); continue;}
                        return false;
                    }
                    continue;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Ищет в выражении степенные функции POW, SQRT, CUBRT с последующей разбивкой на несколько множителей
     * @param e входное выражение
     * @param even выносить из под корня только элементы с целыми показателями степени
     * @param ring кольцо
     * @return измененное выражение
     */
    public Element findRoot(Element e, boolean even, Ring ring){
        if(e instanceof F){
            F f = (F)e;
            if(f.name == F.POW || f.name == F.SQRT || f.name == F.CUBRT) return factorRoot(e, even, ring);
            for (int i = 0; i < f.X.length; i++)
                f.X[i] = findRoot(f.X[i], even, ring);
            return f;
        }
        return e;
    }

    /**
     * Факторизует корень
     * @param e входное выражение
     * @param even выносить из под корня только элементы с целыми показателями степени
     * @param ring
     * @return измененный корень
     */
    public Element factorRoot(Element e, boolean even, Ring ring){
        if(e instanceof F){
            Element res = ring.numberONE;
            Element new_arg = ring.numberONE;
            F f = (F)e;
            if(f.name == F.SQRT) f = new F(F.POW, new Element[]{f.X[0], new Fraction(1,2)});
            if(f.name == F.CUBRT) f = new F(F.POW, new Element[]{f.X[0], new Fraction(1,3)});
            if(f.name != F.POW) return e;
            
            Element pows = f.X[1];
            Element tmp = f.X[0].factor(ring);
            tmp = simpID(tmp);
            if(tmp instanceof Fname) return e;
            if(tmp instanceof FactorPol){
                FactorPol arg = (FactorPol)tmp;//факторизовали подкоренное выражение
                for (int i = 0; i < arg.multin.length; i++){
                    if(arg.multin[i].isItNumber()){//если множитель - число
                        if(arg.multin[i].isNegative() && i+1 < arg.multin.length){
                            arg.multin[i] = arg.multin[i].negateThis(ring);
                            arg.multin[i+1] = arg.multin[i+1].negateThis(ring);
                        }
                        if(arg.multin[i].isOne(ring)) continue;
                    }
                    Element cur_pow = pows.multiply(new NumberZ(arg.powers[i]), ring);//узнали новую степень
                    Element mult = new F(F.POW, new Element[]{arg.multin[i], cur_pow});//получаем новый элемент
                    if(cur_pow.isOne(ring)) 
                        mult = arg.multin[i];
                    
                    if(!even){//если разбивать функцию полностью
                        if(cur_pow.isOne(ring)) mult = arg.multin[i];
                        res = res.multiply(mult, ring);
                        continue;
                    }
                    if(even){//если выносить из корня только целые степени
                        if(cur_pow instanceof Fraction &&
                        ((Fraction)cur_pow).num(ring).subtract(((Fraction)cur_pow), ring).isZero(ring)){
                        //если числитель равен знаменателю, то степень выносится целая
                            res = res.multiply(mult, ring);
                        }
                        else{
                            new_arg = new_arg.multiply(arg.multin[i].pow(arg.powers[i], ring), ring);
                        }
                    }
                }
                if(!new_arg.isOne(ring)) return res.multiply(new F(F.POW, new_arg, pows), ring);
                return res;
            }
            if(tmp instanceof F){
                F func = (F)tmp;
                if(func.name == F.MULTIPLY){
                    for (int i = 0; i < func.X.length; i++)
                        res = res.multiply(func.X[i].pow(pows, ring), ring);
                }
                return res;
            }
        }
        return e;
    }
    
    /**
     * Разбор правой части уравнения.
     * @return true в случае удачи.
     */
    public boolean solve_right_part(){
        //if(varX(rightPart)){elemsX.add(rightPart); return true;}
        if(varY(rightPart)){
            if(rightPart.subtract(ring.numberZERO, ring).isZero(ring)){
                elemsX.add(ring.numberZERO);
                return true;
            }
            //elemsY.add(revers(rightPart)); 
            //return true;
        }
        //if(!varX(rightPart) && !varY(rightPart)) rightPart = findRoot(rightPart, ring);
        if(rightPart.isItNumber()) {elemsX.add(rightPart); return true;}//если правая часть - число.
        if(rightPart instanceof Polynom){//если правая часть - полином.
            Polynom p = (Polynom)rightPart;
            FactorPol fp = (FactorPol)p.factor(ring);
            for (int i = 0; i < fp.multin.length; i++) {
                if(fp.multin[i].isItNumber()) {elemsX.add(fp.multin[i].pow(fp.powers[i], ring)); continue;}
                if(varX(fp.multin[i])) {elemsX.add(fp.multin[i].pow(fp.powers[i], ring)); continue;}
                if(varY(fp.multin[i])) {elemsY.add(revers(fp.multin[i].pow(fp.powers[i], ring))); continue;}
                return false;
            }
            return true;
        }
        if(rightPart instanceof F){
            cf = new CanonicForms(ring, true);
            rightPart = findRoot(rightPart, false, ring);
            rightPart = cf.ExpandForYourChoise((F)rightPart, 1, -1, -1, -1, -1);
            //Polynom p_conv = (Polynom) cf.PolynomialConvert((F)rightPart);
            Polynom p_conv = (Polynom) cf.ElementConvertToPolynom(rightPart);
            if(p_conv == null) return false;
            FactorPol fp = (FactorPol) p_conv.factor(cf.newRing);
            for (int i = 0; i < fp.multin.length; i++) {
                Element e = unconvert(cf, fp.multin[i]);
                if(e == null) return false;
                if(e.isItNumber()) {elemsX.add(e); continue;}
                if(pr.isDerivative(e)){
                    if(fp.powers[i] > 1) return false;
                    if(findder) return false; //Если производная встречается не первый раз
                    findder = true;
                    continue;
                }
                if(varX(e)){//Если e зависит только от Х, отправляем в elemsX
                    elemsX.add(e.pow(fp.powers[i], ring));
                    continue;
                } 
                if(varY(e)){//Если только от Y, отправляем в elemsY.
                    elemsY.add(revers(e.pow(fp.powers[i], ring))); 
                    continue;
                }
                if(e instanceof F){//Если во множителе встречается и X и Y
                    F f = (F)e;
                    if(f.name == F.ADD) return false;//если в сумме x и y
                    if(f.name == F.SQRT || f.name == F.POW)
                        e = findRoot(f, false, ring);
                    if(f.name == F.EXP)
                        e = factor_exp(f);
                    if(e instanceof F){
                        f = (F)e;
                        for (Element el:f.X){
                            if(el.isItNumber()) {elemsX.add(el.pow(fp.powers[i], ring)); continue;}
                            if(varX(el)) {elemsX.add(el.pow(fp.powers[i], ring)); continue;} 
                            if(varY(el)) {elemsY.add(revers(el.pow(fp.powers[i], ring))); continue;}
                            return false;
                        }
                        continue;
                    }
                }
                return false;
            }
            return true;
        }
        return false;
    }
    /**
     * Восстанавливает функцию из полинома по всем уровням. 
     * Использует List_of_Change и List_of_Polynom.
     * @param cf
     * @param p входной полином.
     * @return - восстановленная функция.
     */
    public Element unconvert(CanonicForms cf, Element p){
        Element e = cf.UnconvertToElement(p);//Восстанавливаем первый уровень вложенности.
        Element arg = new Element();//здесь будут аргументы восстановленной функции.
        if(e.isItNumber()) return e;//если восстановили число - возвращаем его же
        if(e instanceof Polynom) arg = e;//если восстановили полином
        if(e instanceof F){//если восстановили функцию
            if(pr.isDerivative(e)) return e;//если это производная - возвращаем её
            if(((F)e).name == F.d && ((F)e).X.length == 1) return e;
            if(((F)e).name == F.ID) return e;
            if(((F)e).X.length == 1) arg = ((F)e).X[0];//если функция от 1 переменной
            else arg = e;
            if(((F)e).name == F.EXP) return new F(F.EXP, new Element[]{cf.UnconvertToElement(arg)});
        }
        if(arg instanceof NumberZ){
            return e;
        }
        if(arg instanceof Polynom){
            Polynom[] monoms = cf.dividePolynomToMonoms((Polynom)arg);
            if(monoms[0].powers.length <= num_var) return e;
            return new F(((F)e).name, unconvert(cf, monoms[0]));
        }
        if(arg instanceof F){
            F f = (F)arg;
            Element res = (F)arg;
            if( (f.name == F.MULTIPLY) || (f.name == F.DIVIDE) ) {
                Element[] mass = new Element[f.X.length];
                for (int i = 0; i < f.X.length; i++)
                    mass[i] = unconvert(cf, f.X[i]);
                return new F(f.name, mass);
            }
            if(f.name == F.POW || f.name == F.intPOW){
                f.X[0] = unconvert(cf, f.X[0]);
                if(f.X[1].equals(ring.numberONE)) return f.X[0];
                if(f.X[1].equals(new Fraction(1, 2), ring)) return new F(F.SQRT, f.X[0]);//если в степени 1/2
                if(f.X[1].equals(new Fraction(1, 3), ring)) return new F(F.CUBRT, f.X[0]);
                return f;
            }
            f = cf.ExpandForYourChoise(f, 1, -1, -1, -1, -1);//поднимаем сумму
            if(f.name == F.ADD){
                Element[] mass = new Element[f.X.length];
                for (int i = 0; i < f.X.length; i++)
                    mass[i] = unconvert(cf, f.X[i]);
                return new F(F.ADD, mass);
            }
            return f;//new F(((F)e).name, unconvert(cf, res));
        }
        if(arg instanceof Fraction){
            return e.multiply(arg, ring);
        }
        return null;
    }

    /** Выносит элементы из под знака корня
     * @param f входная функция
     * @param ring кольцо
     * @return произведение функций (вынесенные элементы * корень)
     */
    /*public F factorRoot(F f, Ring ring){
        int root = 0;
        if(f.name == F.SQRT) root = 2;
        if(f.name == F.CUBRT) root = 3;
        if(root == 0) return f;
        Element e = f.X[0].factor(ring);
        if(e instanceof FactorPol){
            FactorPol f1 = (FactorPol)e;
            e = ring.numberONE;
            for (int i = 0; i < f1.multin.length; i++){//пробегаем по множителям
                if(f1.powers[i] > 0 && f1.powers[i] % root == 0){
                    int st = f1.powers[i]/root;
                    e = e.multiply(f1.multin[i].pow(st, ring), ring);
                }
                else 
                    e = e.multiply(new F(f.name, f1.multin[i].pow(f1.powers[i], ring)), ring);
            }
        }
        return (F)e;
    }*/
    
    /**
     * Проверяет, зависит ли функция/полином только от X.
     * @param e - проверяемая функция/полином.
     * @return - true, если зависит только от X.
     */
    public boolean varX(Element e){
        if(e instanceof F){
            Element[] args = ((F)e).X;
            boolean sep = true;
            for (int i = 0; i < args.length; i++)
                sep &= varX(args[i]);
            return sep;
        }
        if(e instanceof Polynom){
            Polynom p = (Polynom)e;
            int count = p.powers.length/p.coeffs.length;
            if(count == 1) return true;
            boolean sep = true;
            for (int i = 0; i < count; i++)
                for (int j = 0; j < p.powers.length; j += count){
                    if(i == 0) continue;
                    if(i == 1 && p.powers[i+j] > 0) return false;
                    if(i >= 2 && i < num_var) return false;
                    if(i >= num_var && p.powers[i+j] > 0) sep &= varX(cf.List_of_Change.get(i-num_var));
                }
            return sep;
        }
        if(e instanceof Fraction) return varX(((Fraction)e).num) && varX(((Fraction)e).denom);
        if(e instanceof Fname) return true;
        return e.isItNumber();
    }
    /**
     * Проверяет, зависит ли функция/полином только от Y.
     * @param e - проверяемая функция/полином.
     * @return - true, если зависит только от Y.
     */
    public boolean varY(Element e){
        if(e instanceof F){
            Element[] args = ((F)e).X;
            boolean sep = true;
            for (int i = 0; i < args.length; i++)
                sep &= varY(args[i]);
            return sep;
        }
        if(e instanceof Polynom){
            Polynom p = (Polynom)e;
            int count = p.powers.length/p.coeffs.length;
            boolean sep = true;
            if(p.powers.length >= 1 && p.powers[0] > 0) return false;
            for (int i = 0; i < count; i++)
                for (int j = 0; j < p.powers.length; j += count){
                    if(i == 0 && p.powers[i+j] > 0) return false;
                    if(i == 1) continue;
                    if(i >= 2 && i < num_var) continue;
                    if(i >= num_var && p.powers[i+j] > 0) sep &= varY(cf.List_of_Change.get(i-num_var));
                }
            return sep;
        }
        if(e instanceof Fraction) return varY(((Fraction)e).num) && varY(((Fraction)e).denom);
        if(e instanceof FactorPol){
            FactorPol fp = (FactorPol)e;
            boolean sep = true;
            for (int i = 0; i < fp.multin.length; i++)
                sep &= varY(fp.multin[i]);
            return sep;
        }
        return e.isItNumber();
    }
    /**
     * Прием, подготовка и решение уравнения.
     * @param f1 левая часть входного дифф. уравнения
     * @param f2 правая часть
     * @return вектор с решением в успешном случае, в противном - ошибку.
     */
    public Element solve(Element f1, Element f2){
        //System.out.println(" >>> solveDESV Уравнение: "+f1+" = "+f2+"");
        if(f1 instanceof F){//Если f1 - функция, упрощаем её
            f1 = f1.expand(ring);
            if(((F)f1).name == F.DIVIDE){//если это деление, избавляемся от него правилом пропорции
                f2 = f2.multiply(((F)f1).X[1], ring);
                f1 = ((F)f1).X[0];
            }
        }
        if(f2 instanceof F){//аналогично
            F f = (F)f2.expand(ring);
            if(f.name == F.DIVIDE){
                f1 = f1.multiply(f.X[1], ring);
                f2 = f.X[0];
            }
            if(f.name == F.intPOW && f.X[1].isMinusOne(ring)){
                f2 = ring.numberONE;
                f1 = f1.multiply(f.X[0], ring);
            }
        }
        if(f2 instanceof Fraction){//если f2 - дробь, аналогично
            f1 = f1.multiply(((Fraction)f2).denom, ring);
            f2 = ((Fraction)f2).num;
        }
        System.out.println(f1+"\n"+f2);
        f1 = f1.subtract(f2, ring).simplify(ring);
        f2 = ring.numberZERO;
        
        if(f1 instanceof F){
            f1 = f1.expand(ring);
            if(f1.isNegative()) f1 = f1.negate(ring);
            f1 = group_derivative((F)f1);
        }
        else { ring.exception.append(errorString); 
                   return errorVectS;
        }
        
        leftPart = f1; rightPart = f2;
        if((!find(f1)) ||(!solve_left_part())||(!solve_right_part())) {
            ring.exception.append(errorString);
            return errorVectS;
        }
         cf.cleanLists();
        for (int i = 0; i < elemsY.size(); i++) {
            if(elemsY.get(i).isOne(ring)) continue;
            left = left.multiply(elemsY.get(i), ring);
            try{ left = left.simplify(ring);}
            catch(Exception e){}
        }
        for (int i = 0; i < 10; i++)
            left = simpID(left);
        //cf.cleanLists();
        for (int i = 0; i < elemsX.size(); i++) {
            if(elemsX.get(i).isOne(ring)) continue;
            right = right.multiply(elemsX.get(i), ring);
            try{ right = right.simplify(ring);}
            catch(Exception e){}
        }
        for (int i = 0; i < 10; i++)
            right = simpID(right);
        
        if(left.isZero(ring)) {ring.exception.append(errorString);
            return errorVectS;}
//        System.out.println(" >>> Решение: [ "+left+" , "+right+" ]");
        //cf.cleanLists();
        
        left = new Integrate().integrate(left, new Polynom("y", ring), new Ring("Q[x,y]"));
        //System.out.println("left====="+left);
        try{ 
            if(left instanceof F && ((F)left).name == F.INT)
                left = new F(F.INT, new Element[]{((F)left).X[0].simplify(ring), ((F)left).X[1]});
            else left = left.simplify(ring);
        }
        catch(Exception e){}
        right = new Integrate().integrate(right, new Polynom("x", ring), new Ring("Q[x,y]"));
        try{ 
            if(right instanceof F && ((F)right).name == F.INT)
                right = new F(F.INT, new Element[]{((F)right).X[0].simplify(ring), ((F)right).X[1]});
            else right = right.simplify(ring);
        }
        catch(Exception e){}
        
        /*if((left instanceof F && ((F)left).name == F.INT) || (right instanceof F && ((F)right).name == F.INT)){}
        else toNormalForm();*/
        /*if(right instanceof F){
            F f = (F) right;
            if(f.name == F.LN && f.X[0] instanceof F && ((F)f.X[0]).name == F.ABS){
                ((F)f.X[0]).X[0] = ((F)f.X[0]).X[0].multiply(new Fname("C"), ring);
            }
        }*/
        VectorS result = new VectorS(new Element[]{left, right});
        //System.out.println(" >>> Интегралы: "+result);
        return result;
    }
    public Element simpID(Element e){
        if(e instanceof F){
            F f = (F)e;
            if(f.name == F.ID) return f.X[0];
            for (int i = 0; i < f.X.length; i++)
                f.X[i] = simpID(f.X[i]);
            return f;
        }
        if(e instanceof FactorPol){
            return new SolveETD().FPsimplify(e);
        }
        return e;
    }
    public static void main(String[] args) throws Exception{
        //x * (\sqrt{-y^2+1})
        //(-y) * \d_x(y)* (\sqrt{-x^2+1})
        Ring ring = new Ring("Q[x,y]");
        
        /*F f1 = new F(F.MULTIPLY, new Element[]{ring.varPolynom[0], new F(F.SQRT, new Element[]{new Polynom("-y^2+1", ring)})});
        F f2 = new F(F.MULTIPLY, new Element[]{new Polynom("-y", ring), new F(F.d, new Element[]{ring.varPolynom[1], ring.varPolynom[0]}), new F(F.SQRT, new Element[]{new Polynom("-x^2+1", ring)})});
        System.out.println(f1.subtract(f2, ring).simplify(ring));*/
        //F f = new F(F.DIVIDE, new Element[]{ring.numberONE, new F(F.MULTIPLY, new Element[]{new Polynom("y+1", ring), new F(F.LN, new Element[]{new Polynom("y+1", ring)})})});
        //new Integrate().integrate(f, new Polynom("y", ring), ring);
        //new Integrate().mainProcOfInteg(f, new Polynom("y", ring), ring);
        
        String s1 = "SPACE=Q[x,y];f=\\solveDE(\\d(y,x)(xy+2x) = \\sin(\\exp(x^4+2)-\\tg(\\cos(x)-7))\\exp(y+x));";
        String s2 = "SPACE=Q[x,y];\\solveDE((3x^3+5x^2y)\\d(y,x) = (x^3+y^3));";
        String s3 = "SPACE=Q[x,y];f=\\solveDE(y\\sqrt(4y^2+44y)\\d(y,x) = x);";
        String s4 = "SPACE=Q[x,y];\\solveDE(1 + \\d(y,x) + y + x\\d(y,x) = 0);";
        String s5 = "SPACE=Q[x,y];\\solveDE(3x\\d(y,x) = \\exp(\\sin(x)+(y^2+4)));";
        Play_On_Ground.startDebug(s5);
    }
}