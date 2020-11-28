/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import java.util.ArrayList;
import java.util.Vector;
import com.mathpar.number.*;
import com.mathpar.polynom.*;
import com.mathpar.web.exceptions.MathparException;

/**
 * Класс Integrate строит последовательность регулярных мономов (трансцедентные
 * функции). Трансцедентноть функций проверяется с помощью структурной теоремой
 * (класс StrucTheorem).
 *
 * @author Gagauz
 */
public class Integrate {

    public Integrate() {
    }
    
        public Element convertFnamesToPolynoms(Element func, Ring ring) {
      //  F.cleanOfRepeating(func, ring);
      if(func instanceof Fname) {
          int numOldVar = ring.CForm.newRing.varNames.length;
          Element fnm2Pol = ring.CForm.workToDefault(func, 1);
          ring.CForm.makeWorkRing(ring.CForm.index_in_Ring - numOldVar-1);
          return fnm2Pol;
      } else if(func instanceof F) {
          Element[] ARGS = new Element[((F)func).X.length];
          ARGS[0] = convertFnamesToPolynoms(((F)func).X[0], ring);
          for(int i=0; i<((F)func).X.length; i++) {
              ARGS[i] = convertFnamesToPolynoms(((F)func).X[i], ring);
          }
          return new F(((F)func).name, ARGS);
      } else {
          return func;
      }
  }

    public Element parallelPart_2(ArrayList<Fname> simbolName, Element polParts, Element arg, Ring ring ) throws Exception {
        IntPolynom intP = new IntPolynom();
        Fname nameVar = new IntPolynom().getLastRegularMonomial(polParts, simbolName, arg, ring);
        Element integrate = intP.polPartInteg(polParts, nameVar, simbolName, arg, ring );
        return integrate;
    }

    /**
     * Эта функция отмечает, что в рекурсивном дереве вычислений интеграла эта
     * ветвь независимая от parallelPart_1. Тут одна функция - интегрирование
     * дробной части интеграла
     *
     * @param simbolName введенные выше символьные имена
     * @param fracParts слагаемое подинтегрального выражения - дробная часть
     * @param ring Ring
     *
     * @return вычисленный интеграл
     *
     * @throws Exception
     */
    public Element[] parallelPart_1(ArrayList<Fname> simbolName, Fraction fracParts, Fname var, int n_var, Element arg, Ring ring ) throws Exception {
        return new IntegrateFractions().fracPartInteg(fracParts, simbolName, var, n_var, arg, ring );
    }

   
    /**
     * Процедура, для интегрирования функций вида:
     * \pow(f(x), m/n)*\D(f(x), x)
     * и функций, содержащих выражение вида \sqrt( a*x^2 + bx + c ) или вида \pow(a*x + b, m/n).
     * 
     * 
     * 
     * @param func - подинтегральное выражение.
     * @param arg - переменная интегрирования
     * @param ring - кольцо.
     * @return Массив.
     * Первый элемент массива содержит интеграл от проинтегрированной
     * части подынтегрального выражения.
     * Второй элемент массива содержит часть подынтегрального
     * выражения, интеграл от которой вычислить не удалось.
     */
    
    public Element[] integFracPows(Element func, Polynom arg, ArrayList<Fname> simbolName, Fname nameVar, Ring ring) {
        IntPolynom intp = new IntPolynom();
        Element res0 = ring.numberZERO, res1 = ring.numberZERO;
        F root = (F) nameVar.X[0];
        Element degOfRoot = null;
        if(root.name == F.SQRT) {
            degOfRoot = ring.posConst[2];
        } else if(root.name == F.CUBRT) {
            degOfRoot = ring.posConst[3];
        } else if(root.name == F.ROOTOF) {
            degOfRoot = root.X[1];
        }
        
        root = new F(F.POW, root.X[0], ring.numberONE.divide(degOfRoot, ring));
        
        Vector<Element> coeffs = new Vector<Element>();
        Vector<Element> degs = new Vector<Element>();
        

        intp.coefOfHightVar(func, nameVar, coeffs, degs, simbolName, arg, ring);
        intp.sortVector(coeffs, degs, ring);
        intp.sumCoeffOfEqualsPows(coeffs, degs, ring);
        
        
        for(int i = 0; i < coeffs.size(); i++) {
            
            if(degs.get(i).isZero(ring)) {
                Element[] elem = helpIntegFracPows(coeffs.get(i), (Polynom) arg, simbolName, ring);
                
                res0 = res0.add(elem[0], ring);
                res1 = res1.add(elem[1], ring);
                continue;
            }
            
            
            // Если подвыражение в дробной степени (радикал) стоит в знаменателе, то меняем
            // знак показателя степени на противоположный.
            if(degs.get(i).isNegative()) {
                root.X[1] = root.X[1].negate(ring);
            }
            

            Element D = root.X[0].D(((Polynom)arg).powers.length - 1, ring);
            Element div = coeffs.get(i).divide(D, ring).expand(ring).Expand(ring);
            if(!containsVar(div, (Polynom)arg)) {
                // Если рассматриваемое слагаемое имеет вид a*\pow(f(x), m/n)*\D(f(x), x), где a - не содержит x.
                // то вычисляем первообразную этого выражения по формуле
                // \pow(f(x), m/n + 1)*a/(m/n + 1)
                Element constant = root.X[1].add(ring.numberONE, ring);
                Element result = new F(F.POW, root.X[0], constant);
                constant = div.divide(constant, ring);
                res0 = (constant.isOne(ring))? res0.add(result, ring):
                        res0.add(result.multiply(constant, ring), ring);
                continue;
            }

            
            // Если под радикалом стоит многочлен, то пытаемся вычислить первообразную при помощи замены переменных.
            // На данный момент реализованы замены переменных для многочленов первой и второй степени.
            // Реализованные подстановки описаны в параграфе "Интегрирование некоторых выражений, содержащих радикалы"
            // книги "Курс дифференциального и интегрального исчисления" Г. М. Фихтенгольца.
            if(root.X[0] instanceof Polynom) {
                Element t = null;
                Element Dx = null;
                Element radical = null;
                Element x = null;
                // Если под радикалом стоит многочлен первой степени. Пусть многочлен
                // имеет вид: ax + b. Обозначаем радикал буквой t:
                // t = (ax + b)^(1/n)
                // Делаем замену переменных:
                // x = (t^n - b)/a
                // dx = n*t^{n-1}/a dt
                //
                // После замены переменных подынтегральное выражение не содержит радикалов.
                // Находим первообразную от преобразованного подынтегрального выражения.
                // В найденной первообразной производим обратную замену переменных:
                // t = (ax + b)^(1/n).
                if( ((Polynom)root.X[0]).degree(((Polynom)arg).powers.length - 1) == 1  ) {
                    x = new F(F.POW, arg, degOfRoot);
                    if(((Polynom)root.X[0]).coeffs.length == 2) {
                        x = new F(F.SUBTRACT, x, ((Polynom)root.X[0]).coeffs[1]);
                    }
                    x = new F(F.DIVIDE, x, ((Polynom)root.X[0]).coeffs[0]).expand(ring);
                    Dx = x.D(((Polynom)arg).powers.length - 1, ring).expand(ring);
                    radical = arg;
                    t = nameVar.X[0];
                // Если радикал имеет вид: sqrt(ax^2 + bx + c). В данном случае возможны три случая,
                // в зависимости от того, сколько корней имеет многочлен ax^2 + bx + c.
                } else if(((Polynom)root.X[0]).degree(((Polynom)arg).powers.length - 1) == 2  &&  ((F)nameVar.X[0]).name == F.SQRT) {
                    Polynom pol = (Polynom)root.X[0];
                    Element a = pol.coeffs[0];
                    Element b = ring.numberZERO;
                    Element c = ring.numberZERO;
                    for (int j = 1; j < pol.coeffs.length; j++) {
                        if(pol.powers[j] == 1) {
                            b = pol.coeffs[j];
                        } else if(pol.powers[j] == 0) {
                            c = pol.coeffs[j];
                        }
                    }
                    Element sqrtA = null;
                    if(a.isOne(ring)) {
                        sqrtA = a;
                    } else {
                        sqrtA = new F(F.SQRT, a);
                        pol = (Polynom)pol.divide(pol.coeffs[0], ring);
                    }
                    Element solEE = new SolveEq(ring).solvePolynomEq(pol, ((Polynom)arg).powers.length - 1, ring);
                    // Случай 1. Многочлен ax^2 + bx + c не имеет корней.
                    // В этом случае делаем замену переменных:
                    // x = (t^2 - с)/(2sqrt(a)t + b)
                    // dx = 2 (sqrt(a)t^2 + bt + sqrt(a)c)/(2sqrt(a)t + b)^2 dt
                    // sqrt(ax^2 + bx + c) = (sqrt(a)t^2 + bt + sqrt(a)c)/(2sqrt(a)t + b)
                    //
                    // После замены переменных подынтегральное выражение не содержит радикалов.
                    // Находим первообразную от преобразованного подынтегрального выражения.
                    // В найденной первообразной производим обратную замену переменных:
                    // t = sqrt(ax^2 + bx + c) + sqrt(a)x.
                    if(solEE == null  ||  solEE.isNaN()) {
                        radical = new F(F.MULTIPLY, new F(F.intPOW, arg, ring.posConst[2]), sqrtA);
                        radical = new F(F.ADD, radical, new F(F.MULTIPLY, arg, b));
                        radical = new F(F.ADD, radical, new F(F.MULTIPLY, sqrtA, c)).expand(ring);
                        radical = new F(F.DIVIDE, radical,  new F(F.ADD, new F(F.MULTIPLY, new Element[]{arg, sqrtA, ring.posConst[2]}), b)).expand(ring);
                        x = new F(F.SUBTRACT, new F(F.intPOW, arg, ring.posConst[2]), c).expand(ring);
                        x = new F(F.DIVIDE, x, new F(F.ADD, new F(F.MULTIPLY, new Element[]{arg, sqrtA, ring.posConst[2]}), b)).expand(ring);
                        Dx = x.D(((Polynom)arg).powers.length - 1, ring).expand(ring);
                        t = new F(F.ADD, nameVar.X[0], new F(F.MULTIPLY, arg, sqrtA)).expand(ring);
                    } else {
                        VectorS s = (solEE instanceof VectorS)? (VectorS)solEE: new VectorS(new Element[]{solEE});
                        // Случай 2. Многочлен ax^2 + bx + c имеет один корень кратности два.
                        // Обозначим этот корень lambda.
                        // В таком случае sqrt(ax^2 + bx + c) = sqrt(a(x - lambda)^2) = sqrt(a)(x - lambda).
                        // Поэтому в подынтегральном выражении заменяем sqrt(ax^2 + bx + c) на sqrt(a)(x - lambda).
                        if(s.V.length == 1) {
                            res1 = res1.add(coeffs.get(i).multiply(sqrtA.multiply(arg.subtract(s.V[0], ring), ring).pow(degs.get(i), ring), ring), ring);
                        } else {
                        // Случай 3. Многочлен ax^2 + bx + c имеет два различных корня.
                        // В таком случае ax^2 + bx + c = a(x - lambda)(x - mu)
                        // В этом случае делаем замену переменных:
                        // x = (-a*mu + lambda*t^2)/(t^2 - a)
                        // dx = 2*a*(lambda - mu)*t/(t^2 - a)^2 dt
                        // sqrt(ax^2 + bx + c) = a*(lambda - mu)*t/(t^2 - a)
                        //
                        // После замены переменных подынтегральное выражение не содержит радикалов.
                        // Находим первообразную от преобразованного подынтегрального выражения.
                        // В найденной первообразной производим обратную замену переменных:
                        // t = sqrt(a(x - mu)/(x - lambda)).
                        //
                        // 
                        // s.V[0] = lambda
                        // s.V[1] = mu
                            x = new F(F.ADD, new F(F.MULTIPLY, new Element[]{ring.numberMINUS_ONE, a, s.V[1]}), new F(F.MULTIPLY, new Element[]{arg, arg, s.V[0]}));
                            x = new F(F.DIVIDE, x, new F(F.SUBTRACT, new F(F.intPOW, arg, ring.posConst[2]), a)).expand(ring);
                            Dx = x.D(((Polynom)arg).powers.length - 1, ring).expand(ring);
                            radical = new F(F.MULTIPLY, new Element[]{a, new F(F.SUBTRACT, s.V[0], s.V[1]), arg});
                            radical = new F(F.DIVIDE, radical, new F(F.SUBTRACT, new F(F.intPOW, arg, ring.posConst[2]), a)).expand(ring);
                            t = new F(F.MULTIPLY, new F(F.SUBTRACT, arg, s.V[1]), a);
                            t = new F(F.DIVIDE, t, new F(F.SUBTRACT, arg, s.V[0])).expand(ring);
                            t = new F(F.SQRT, t);
                        }
                    }
                }
                
                if(x != null && Dx != null && t != null && radical != null) {
                    Element[] els = new Element[ring.varPolynom.length];
                    
                    for (int j = 0; j < els.length; j++) {
                        if(j == ((Polynom)arg).powers.length - 1) {
                            els[j] = x;
                        } else {
                            els[j] = ring.varPolynom[j];
                        }
                    }
                    // Выполняем замену переменных (меняем во всем выражении
                    // переменную x на ее выражение через t, заменяем радикал на его
                    // выражение через t, а также меняем dx на x`(t)dt).
                    Element newExpression = coeffs.get(i).value(els, ring);
                    newExpression = newExpression.multiply(Dx, ring).multiply(radical.pow(degs.get(i), ring), ring);
                    newExpression = integrate(newExpression, arg, ring);
                
                    els[((Polynom)arg).powers.length - 1] = t;
                    // Выполняем замену переменной в первообразной (т. е. заменяем переменную t на переменную x).
                    res0 = res0.add(newExpression.value(els, ring), ring);
                    continue;
                }
            }
            

            res1 = res1.add(coeffs.get(i).multiply(new F(F.intPOW, nameVar, degs.get(i)), ring), ring);
        }
        
        return new Element[]{res0, res1};
    }
    
    /**
     * Если в функции func нет выражений с дробными степенями,
     * то возвращаем null.
     * Если func содержит выражения с дробными степенями, то
     * возвращаем путь до первого попавшегося выражения с дробной степенью.
     */
    public ArrayList<Integer> containsRoots(F func, Element arg, Ring ring) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        if( (func.name == F.MULTIPLY) || (func.name == F.DIVIDE) ) {
            for (int i = 0; i < func.X.length; i++) {
                if(func.X[i] instanceof F) {
                    if(isItFracPow((F) ((F)func).X[i], arg, ring)) {
                        res.add(i);
                        return res;
                    } else {
                        res = containsRoots((F) ((F)func).X[i], arg, ring);
                        if(res != null) {
                            res.add(i);
                            return res;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Проверка, является ли func выражением вида f(x)^t, где t --- дробное число.
     * @param func
     * @param arg
     * @param ring
     * @return
     */
    public boolean isItFracPow(F func, Element arg, Ring ring) {
        if(containsVar(func, (Polynom)arg)) {
            if( (func.name == F.SQRT) || (func.name == F.CUBRT) || (func.name == F.ROOTOF)) {
                return true;
            }
            if( (func.name == F.POW) && isItFracNumber(func.X[1], ring) ) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Проверка, является ли number дробным числом.
     * @param number
     * @param ring
     * @return 
     */
    public boolean isItFracNumber(Element number, Ring ring) {
           if(number instanceof Fraction) {
               if(  ((Fraction)number).denom.abs(ring).equals(ring.numberONE, ring)  ) {
                   return false;
               }
           } else {
               if(number.ceil(ring).subtract(number, ring).isZero(ring)) {
                   return false;
               }
           }
           return true;
       }
    
        /**
     * Поиск и замена в выражении func:
     * ln(\sqrt(f(x)))   ---->   1/2 * ln(f(x))
     * ln(\cubrt(f(x)))   ---->   1/3 * ln(f(x))
     * ln(f(x)^a)   ---->   a * ln(f(x))
     * ln(f(x)/g(x))    ---->   ln(f(x)) - ln(g(x))
     * ln(f(x)*g(x)^a) -->  ln(f(x)) + a*ln(g(x))
     * 
     * \sqrt(exp(f(x)))  ---->   exp((1/2)*f(x))
     * \cubrt(exp(f(x)))  ---->   exp((1/3)*f(x))
     * exp(f(x))^(a/b)  ---->   exp((a/b)*f(x))
     * 
     * Вместо рациональной дроби (a/b) может быть десятичная дробь.
     * 
     * 
     * @param func
     * @param arg
     * @param ring
     * @return 
     */
    public Element workWithPowers(Element func, Element arg, Ring ring) {
        if(func instanceof F) {
            if( ((F)func).name == F.LN ) {
                boolean flagAbs = false;
                Element argOfLN = ((F)func).X[0];
                if(argOfLN instanceof F) {
                    F f = (F) argOfLN;
                    if(f.name == F.ABS) {
                        flagAbs = true;
                        argOfLN = f.X[0];
                    }
                }
                Element pol = ring.CForm.ElementConvertToPolynom(argOfLN);
                return toSumOfLN(pol, flagAbs, arg, ring);
            }
            if( isItFracPow( (F)func, arg, ring)  ) {
                if(  ((F)func).X[0] instanceof F && ((F)((F)func).X[0]).name == F.EXP) {
                    F exp = (F)((F)func).X[0];
                    Element coeff = null;
                    if(((F)func).name == F.SQRT) {
                        coeff = ring.numberONE.divide(ring.posConst[2], ring);
                    }
                    if(((F)func).name == F.CUBRT) {
                        coeff = ring.numberONE.divide(ring.posConst[3], ring);
                    }
                    if(((F)func).name == F.POW) {
                        coeff = ((F)func).X[1];
                    }
                    
                    return (coeff == null)? func: new F(F.EXP, new F(F.MULTIPLY, new Element[]{
                        coeff,
                        workWithPowers(exp.X[0], arg, ring)
                    }));
                }
                
            }
            Element[] ARGS = new Element[((F)func).X.length];
            for (int i = 0; i < ARGS.length; i++) {
                ARGS[i] = (((F)func).X[i] instanceof F)? workWithPowers(  ((F)func).X[i], arg, ring  ): ((F)func).X[i];
            }
            return new F(((F)func).name, ARGS);
        }
        return func;
    }
    
    
        /**
     * Процедура на входе принимает аргумент логарифма, выносит в качестве множителей
     * степенные выражения и возвращает сумму логарифмов от найденных множителей.
     * 
     * Другими словами, происходит преобразование:
     * ln(f(x)*g(x)^a) ---> ln(f(x)) + a*ln(g(x))
     * ln(g(x)^a)      ---> a*ln(g(x))
     * ln(f(x)/g(x))   ---> ln(f(x)) - ln(g(x)).
     * 
     * 
     * Например, выражение
     * ln((x^2 + 2x + 3)*sqrt[(x + 1)*cubrt(x/(x - 1))])
     * будет преобразовано к виду
     * ln(x^2 + 2x + 3) + (1/2)*ln(x + 1) + (1/6)*(ln(x) - ln(x - 1))
     * 
     * 
     * pol --- аргумент рассматриваемого логарифма, преобразованный
     * в полином (или рациональную дробь).
     * flagAbs --- должен быть true, если pol стоял в аргументе логарифма
     * под модулем (т. е. если рассматриваемый логарифм равен ln(abs(pol))   ).
     *             flagAbs должен быть false, если pol стоял в аргументе логарифма
     * без модуля (т. е. если рассматриваемый логарифм равен ln(pol)   ).
     * arg --- переменная интегрирования.
     * 
     * Процедура возвращает сумму логарифмов.
     * 
     * Например, пусть pol = (x + 1)*z, где z=sqrt[x/(x - 1)]
     * Пусть flagAbs = true.
     * Тогда процедура вернет ln(abs(x + 1)) + (1/2)*ln(abs(x)) - (1/2)*ln(abs(x-1))
     * 
    */
    public Element toSumOfLN(Element pol, boolean flagAbs, Element arg, Ring ring) {
        if(pol instanceof Fraction) {
            Element el1 = toSumOfLN(((Fraction)pol).num, flagAbs, arg, ring);
            Element el2 = toSumOfLN(((Fraction)pol).denom, flagAbs, arg, ring);
            return new F(F.SUBTRACT, el1, el2);
        }
        Element result = ring.numberZERO;
        // В переменной temp будет храниться произведение множителей,
        // вынесенных в отдельные логарифмы.
        Polynom temp = new Polynom(new int[]{}, new Element[]{ring.numberONE});
        if(pol instanceof Polynom) {
            // Находим НОД всех мономов полинома pol. Для этого достаточно
            // найти НОД от первого монома и всего полинома.
            Element[] monoms = ring.CForm.dividePolynomToMonoms((Polynom)pol);
            Element gcd = monoms[0].GCD(pol, ring);
        
            
            if(gcd instanceof Polynom) {
                Polynom polGCD = (Polynom) gcd;
                // Ищем в gcd переменные, каждая из которых обозначает некоторую функцию.
                for(int i = ring.varPolynom.length; i < polGCD.powers.length; i++) {
                    if(polGCD.powers[i] == 0) {
                        continue;
                    }
                    Element func = ring.CForm.ElementToF(ring.CForm.newRing.varPolynom[i]);
                    if(func instanceof F) {
                        // Нас интересуют только степенные функции. Другие виды
                        // функций в отдельный логарифм не выносим.
                        if( ( ((F)func).name == F.POW) ||
                            ( ((F)func).name == F.intPOW) ||
                            ( ((F)func).name == F.SQRT) ||
                            ( ((F)func).name == F.CUBRT)  ) {
                            Element coeff = null;
                            if( ((F)func).name == F.SQRT) {
                                coeff = ring.numberONE.divide(ring.posConst[2], ring);
                            } else if( ((F)func).name == F.CUBRT) {
                                coeff = ring.numberONE.divide(ring.posConst[3], ring);
                            } else {
                                coeff = ((F)func).X[1].multiply(ring.posConst[polGCD.powers[i]], ring);
                            }
                            Element funcArg = workWithPowers(((F)func).X[0], arg, ring);
                            funcArg = ring.CForm.ElementConvertToPolynom(funcArg);
                            // Выносим в отдельный логарифм найденную функцию.
                            Element el = toSumOfLN(funcArg, flagAbs, arg, ring);
                            el = el.multiply(coeff, ring);
                            result = result.add(el, ring);
                            
                            // домножаем temp на переменную, которую вынесли
                            // в отдельный логарифм.
                            temp = temp.multiply(ring.CForm.newRing.varPolynom[i], ring);
                            temp.powers[i] = polGCD.powers[i];
                        }
                    }
                }
            }
        }
        // Если ни чего не было вынесено в отдельные логарифмы, то
        // temp = 1, и поэтому возвращаем ln(pol).
        
        // Если какие-то выражения были вынесены в отдельные логарифмы, то
        // temp содержит их произведение.
        // Таким образом, pol/temp не содержит выражений, вынесенных в
        // отдельные логарифмы.
        Element argOfNewLN = pol.divide(temp, ring);
        if(argOfNewLN.isOne(ring)) {
            return result;
        }
        argOfNewLN = ring.CForm.ElementToF(argOfNewLN);
        argOfNewLN = workWithPowers(argOfNewLN, arg, ring);
        if(flagAbs) {
            argOfNewLN = new F(F.ABS, argOfNewLN);
        }
        Element newLN = new F(F.LN, argOfNewLN);
        result = result.add(newLN, ring);
        return result;
    }
    
    /**
     * Приведение показательных и логарифмических функций, содержащихся в
     * выражении func, к основанию e.
     * 
     * Т. е. поиск и замена в выражении func:
     * 
     * log_g(x) (f(x))  --->  ln(f(x))/ln(g(x))
     * f(x)^g(x)        --->  exp(g(x)*ln(f(x)))
     * 
     * @param func
     * @return 
     */
    public Element convertLOGandPOWtoLNandEXP(Element func, Ring ring) {
        if(func instanceof F) {
            if(  (((F)func).name == F.POW)  && (((F)func).X[1].isItNumber() == false) ) {
                return new F(F.EXP, 
                          new F(F.MULTIPLY, 
                               new Element[]{
                                   convertLOGandPOWtoLNandEXP(  ((F)func).X[1], ring),
                                   new F(F.LN,  convertLOGandPOWtoLNandEXP( ((F)func).X[0], ring) )
                               }
                          )
                       );
            }
            if(((F)func).name == F.LOG) {
                return new F(F.DIVIDE, 
                          new Element[]{
                            new F(F.LN, convertLOGandPOWtoLNandEXP(  ((F)func).X[1], ring)  ),
                            new F(F.LN, convertLOGandPOWtoLNandEXP(  ((F)func).X[0], ring)  )
                          }
                       );
            }
            if(((F)func).name == F.LG) {
                return new F(F.DIVIDE, 
                          new Element[]{
                            new F(F.LN, convertLOGandPOWtoLNandEXP(  ((F)func).X[0], ring)  ),
                            new F(F.LN, ring.posConst[10]  )
                          }
                       );
            }
            
            Element[] ARGS = new Element[((F)func).X.length];
            for (int i = 0; i < ARGS.length; i++) {
                ARGS[i] = (((F)func).X[i] instanceof F)? convertLOGandPOWtoLNandEXP(  ((F)func).X[i], ring  ): ((F)func).X[i];
            }
            return new F(((F)func).name, ARGS);
        }
        return func;
    }
    
    
    /**
     * Основная процедура интегрирования функции. Выделяет в подынтегральном
     * выражении последовательность подвыражений f0, ..., fn (процедура
     * partichon). С помощью структурной теоремы (класс StrucTheorem) проверяет,
     * является ли выпмсанная послед-ть f0, ..., fn последоват-тью регулярных
     * мономов. Отделяет полиномиальную и дробно-рациональю части интеграла и
     * интегрирует отдельно полиномиальную часть (класс IntPolynom) и отдельно
     * дробно-рац. часть интеграла (класс IntegrateFraction). На выходе получаем
     * готовый полином. <br><br>
     *
     * @param func -подинтегральная функция
     * @param ring Ring
     *
     * @return вычисленный неопределенный интеграл
     *
     * @throws Exception
     */
    public Element mainProcOfInteg(Element func, Element arg, Ring ring ) throws Exception {
     //   ((F)func).cleanOfRepeatingWithNewVectors(ring);
        func = convertLOGandPOWtoLNandEXP(func, ring);
        func = new LN_EXP_POW_LOG_LG().expandLog(func, ring);
        func = expandRecursive(func, ring);
        Vector<F> vLnAndExp = new Vector<F>();
        ArrayList<Fname> simbolName = new ArrayList<Fname>();
        Element simbolF = makeListOfLNandExp(func, simbolName, vLnAndExp, (Polynom)arg, ring);
        simbolName.add(new Fname("end", simbolF));
        StrucTheorem st = new StrucTheorem();
        st.makeRegularMonomialsSequence(simbolName, arg, ring);
        simbolF = simbolName.get(simbolName.size() - 1).X[0];
        simbolF = simbolF.expand(ring);
        //  System.out.println("in Integrate (54):New expression=" + simbolF);
        simbolName.remove(simbolName.size() - 1);
        
        moveRadicalsToEnd(simbolName);
        
        Element intRoots = ring.numberZERO;
        Element[] elem = helpIntegFracPows(simbolF, (Polynom) arg, simbolName, ring);
        
        if(elem[1].isZero(ring)) {
            return elem[0];
        }
        
        intRoots = elem[0];
        simbolF = elem[1];
        
        
        Element[] polAndFracParts = new Element[2];
        simbolF = new IntegrateFractions().canonicForm(simbolF, ring);
        
        Element polOrFrac = ring.CForm.ElementConvertToPolynom(simbolF);
        if(polOrFrac.isZero(ring)) return ring.numberZERO;
        
        Fname var = new IntPolynom().getLastRegularMonomial(simbolF, simbolName, arg, ring);
        int n_var = indexOfVar(var, ring);
        
        
                
        Element integrate = (intRoots.isZero(ring))? null: intRoots;
        
        
                
        if((polOrFrac instanceof Fraction) && (var.X[0] instanceof F) && isItFracPow((F)var.X[0], arg, ring)) {
            Polynom pol = eqForRoot(var, n_var, ring);
            Fraction frac = algebraicNormalForm(((Fraction)polOrFrac).num, ((Polynom) ((Fraction)polOrFrac).denom), pol, ring);
            Element algInt = new IntegrateFractions().LogarithmicPartGroebner((Polynom) frac.num, (Polynom) frac.denom, pol, var, n_var, (Polynom) arg, ring);
            if(algInt == null) {
                return new F(F.INT, new Element[] {func, arg});
            }
            integrate = (integrate == null)? algInt : integrate.add(algInt, ring);
            
        } else if(isItFraction(polOrFrac, simbolName, arg, n_var, ring)){
            if(( (((Fraction)polOrFrac).num instanceof Polynom)) &&
              ( ((Polynom) ((Fraction)polOrFrac).num).degree(n_var)>=
                ((Polynom) ((Fraction)polOrFrac).denom).degree(n_var) )) {
                        
                polAndFracParts = ((Polynom) ((Fraction)polOrFrac).num).quotientAndRemainder(
                        (Polynom) ((Fraction)polOrFrac).denom, n_var, ring.CForm.newRing);
                if(polAndFracParts[0] instanceof Fraction) {
                    polAndFracParts[0] = new F(F.DIVIDE, new Element[]{
                        ((Fraction)polAndFracParts[0]).num,
                        ((Fraction)polAndFracParts[0]).denom
                    });
                }
                polAndFracParts[0] = ring.CForm.ElementToF(polAndFracParts[0]);
                polAndFracParts[1] = new Fraction(polAndFracParts[1], ((Fraction)polOrFrac).denom);
            } else {
                polAndFracParts[1] = polOrFrac;
            }
        } else {
            polAndFracParts[0] = simbolF;
        }


        // ВНИМАНИЕ это место в параллельной программе может быть
        // переписано так, чтобы  parallelPart_2  и parallelPart_1
        // вычислялись параллельно
        //дробная часть
        if (polAndFracParts[1] != null) {
            Element[] el = parallelPart_1(simbolName, (Fraction)polAndFracParts[1], var, n_var, arg, ring);
            if (el != null) {
                if(el[0] != null) {
                    polAndFracParts[0] = (polAndFracParts[0] == null)? el[0]: polAndFracParts[0].add(el[0], ring);
                }
                if(el[1] != null) {
                    integrate = (integrate == null)? el[1]: integrate.add(el[1], ring);
                }
            }
        }
        // полиномиальная часть
        if (polAndFracParts[0] != null) {
            Element el = parallelPart_2(simbolName, polAndFracParts[0], arg, ring);
            if(el != null) {
                integrate = (integrate == null)? el: integrate.add(el, ring);
            }
        }
        return (integrate == null)? new F(F.INT, new Element[] {func, arg}) : integrate;
    }
    
    
    /**
     * Перемещение всех радикалов в конец списка.
     */
    public void moveRadicalsToEnd(ArrayList<Fname> simbolName) {
        int indexOfList = 0;
        for (int i = 0; i < simbolName.size(); i++) {
            Fname element = simbolName.get(indexOfList);
            if(element.X != null && element.X[0] instanceof F &&
              ( ((F)element.X[0]).name == F.SQRT || ((F)element.X[0]).name == F.CUBRT  || ((F)element.X[0]).name == F.ROOTOF  )) {
                
                simbolName.remove(indexOfList);
                simbolName.add(simbolName.size(), element);
                
            } else {
                indexOfList++;
            }
        }
    }





    /**
     * Приведение алгебраической функции f(x, y) к виду g(x, y)/d(x),
     * где g(x, y) и  d(x) --- многочлены. Другими словами, преобразуем
     * подынтегральную функцию f так, чтобы все радикалы перешли в числитель,
     * а в знаменателе остался многочлен переменной x.
     * 
     * Пусть подынтегральная функция является дробной алгебраической функцией, содержащей радикал.
     * Обозначим радикал буквой y. Получим дробь
     * f(x, y) = p(x, y)/q(x, y), где p, q --- многочлены переменных x и y.
     * 
     * Обозначим F(x, y) --- минимальный многочлен, задающий связь мкжду
     * переменными x и y, т. е. обладающий свойством: если
     * вместо переменной y подставить радикал, то получим 0.
     * Например, пусть буквой y обозначен радикал sqrt(x^2 + 1). Тогда F(x, y) =
     * = y^2 - x^2 - 1, так как F(x, sqrt(x^2 + 1)) = sqrt(x^2 + 1)^2 - x^2 - 1 = 0.
     * 
     * Многочлен F(x, y) является неразложимым, поэтому
     * при помощи расширенного алгоритма Евклида мы можем найти:
     * НОД(q, F) = 1 = s(x, y)*q(x, y) + t(x, y)*F(x, y)   (*), где s(x, y) и t(x, y)
     * могут быть многочленами или дробными функциями. Обозначим r(x) --- общий
     * знаменатель функций s(x, y) и t(x, y). Умножим левую и правую части равенства (*) на r(x):
     * r(x) = s_1(x, y)*q(x, y) + t_1(x, y)*F(x, y)
     * Так как F(x, y) по определению равен нулю, то мы справедливо равенство:
     * r(x) = s_1(x, y)*q(x, y)
     * Отсюда можно выразить q(x, y) = r(x)/s_1(x, y).
     * Подставим полученное выражение в f(x, y):
     * f(x, y) = p(x, y)/q(x, y) = p(x, y)*s_1(x, y)/r(x).
     * В итоге мы получили искомое представление функции f(x, y):
     * все радикалы перешли в числитель, а в знаменателе остался многочлен переменной x.
     * 
     * 
     * @param num --- числитель подынтегрального выражения
     * @param denom --- знаменатель подынтегрального выражения
     * @param m --- многочлен, задающий связь между переменными x и y.
     * @param ring
     * @return 
     */
    public Fraction algebraicNormalForm(Element num, Polynom denom, Polynom m, Ring ring) {
        Polynom polNum = (num instanceof Polynom)? (Polynom) num : new Polynom(new int[]{}, new Element[]{num});
        Element[] bezu = new Element[2];
        VectorS gcd = denom.extendedGCD(m, ring);
        if(gcd.V.length > 5) {
            bezu[0] = new F(F.DIVIDE, new Element[]{gcd.V[1],
                gcd.V[5].multiply(gcd.V[3], ring.CForm.newRing)});
            bezu[1] = new F(F.DIVIDE, new Element[]{gcd.V[2],
                gcd.V[5].multiply(gcd.V[4], ring.CForm.newRing)});
        } else {
            bezu[0] = gcd.V[1];
            bezu[1] = gcd.V[2];
        }
        
        bezu[0] = bezu[0].expand(ring);
        bezu[1] = bezu[1].expand(ring);
        
        Element bezuDenom0 = null;
        Element bezuDenom1 = null;
        Element bezuNum0 = null;
        
        if(bezu[0] instanceof F &&  ((F)bezu[0]).name == F.DIVIDE) {
            bezuDenom0 = ((F)bezu[0]).X[1];
            bezuNum0 = ((F)bezu[0]).X[0];
        } else if(bezu[0] instanceof Fraction) {
            bezuDenom0 = ((Fraction)bezu[0]).denom;
            bezuNum0 = ((Fraction)bezu[0]).num;
        } else {
            bezuDenom0 = ring.numberONE;
            bezuNum0 = ((F)bezu[0]).X[0];
        }
        
        
        if(bezu[1] instanceof F   &&  ((F)bezu[1]).name == F.DIVIDE) {
            bezuDenom1 = ((F)bezu[1]).X[1];
        } else if(bezu[1] instanceof Fraction) {
            bezuDenom1 = ((Fraction)bezu[1]).denom;
        } else {
            bezuDenom1 = ring.numberONE;
        }
        
        Polynom polDenom0 = (bezuDenom0 instanceof Polynom)? (Polynom) bezuDenom0:
                new Polynom(new int[]{}, new Element[]{bezuDenom0});
        Polynom polDenom1 = (bezuDenom1 instanceof Polynom)? (Polynom) bezuDenom1:
                new Polynom(new int[]{}, new Element[]{bezuDenom1});
        
        
        return new Fraction(polNum.multiply(bezuNum0, ring), polDenom0.LCM(polDenom1, ring));
    }
    
    /**
     * Находим минимальный многочлен, задающий связь между переменными x и y. Здесь
     * переменной y обозначен радикал, содержащийся в подынтегральной функции.
     * 
     * Другими словами находим многочлен F(x, y), обладающий свойством: если
     * вместо переменной y подставить радикал, то получим 0.
     * Например, пусть буквой y обозначен радикал sqrt(x^2 + 1). Тогда F(x, y) =
     * = y^2 - x^2 - 1, так как F(x, sqrt(x^2 + 1)) = sqrt(x^2 + 1)^2 - x^2 - 1 = 0.
     * 
     * @param nameVar --- радикал, обозначаемый переменной y.
     * @param n_var --- номер переменной y в кольце ring.CForm.newRing.
     * @param ring
     * @return 
     */
    public Polynom eqForRoot(Fname nameVar, int n_var, Ring ring) {
        int degOfRoot = 0;
        if(((F)nameVar.X[0]).name == F.SQRT) {
            degOfRoot = 2;
        } else if(((F)nameVar.X[0]).name == F.CUBRT) {
            degOfRoot = 3;
        } else if(((F)nameVar.X[0]).name == F.ROOTOF) {
            degOfRoot = ((F)nameVar.X[0]).X[1].intValue();
        }
        Polynom y = (Polynom) ring.CForm.newRing.varPolynom[n_var];
        Polynom m = (Polynom) y.pow(degOfRoot, ring).subtract((Polynom)((F)nameVar.X[0]).X[0], ring);
        return m;
    }


    /**
     * Вспомогательная процедура для интегрирования фукций содержащих радикал.
     * Проверяет, содержит ли функция радикал. Если содержит, то
     * вызывается процедура integFracPows, которая производит попытку
     * проинтегрировать функцию.
     * Если функция не содержит радикал, то процедура прекращает работу.
     * 
     * 
     * @param simbolF --- функция, возможно содержащая радикал.
     * @param arg --- переменная интегрирования.
     * @param simbolName
     * @param ring
     * @return 
     */
    public Element[] helpIntegFracPows(Element simbolF, Polynom arg, ArrayList<Fname> simbolName, Ring ring) {
        Fname nameVar = new IntPolynom().getLastRegularMonomial(simbolF, simbolName, arg, ring);
        if(nameVar.X == null || (nameVar.X[0] instanceof F == false)) {
            return new Element[]{ring.numberZERO, simbolF};
        }
            
        if(isItFracPow((F)nameVar.X[0], arg, ring) == false) {
            return new Element[]{ring.numberZERO, simbolF};
        }
        return integFracPows(simbolF, (Polynom) arg, simbolName, nameVar, ring);
    }
    
    /**
     * Проверка, является ли выражение polOrFrac дробной частью частью интеграла.
     * 
     * 
     * 
     * @param polOrFrac
     * @param simbolName
     * @param arg
     * @param n_var
     * @param ring
     * @return 
     */
    public boolean isItFraction(Element polOrFrac, ArrayList<Fname> simbolName, Element arg, int n_var, Ring ring) {
        if( (polOrFrac instanceof Fraction) && ( ((Fraction)polOrFrac).denom instanceof Polynom ) && ( ((Polynom) ((Fraction)polOrFrac).denom).degree(n_var) > 0) ) {
            boolean bool = true;
            // Если в знаменателе стоит одна экспонента - то polOrFrac относится
            // к полиномиальной части.
            if( ((Polynom)((Fraction)polOrFrac).denom).coeffs.length == 1 ) {
                Element denom = ring.CForm.ElementToF(((Fraction)polOrFrac).denom);
                Fname varDenom = new IntPolynom().getLastRegularMonomial(denom, simbolName, arg, ring);
                if( (varDenom.X[0] instanceof F)&&(((F)varDenom.X[0]).name == F.EXP) ) {
                    return false;
                }        
            }
            return true;
        }
        return false;
    }
    
    /**
     * Возвращает номер переменной var в кольце ring.
     * @param var
     * @param ring
     * @return 
     */
    public int indexOfVar(Element var, Ring ring) {
        int n_var=0;
        if(var instanceof Fname) {
            for (; n_var < ring.varNames.length; n_var++) {
               if(((Fname)var).name.equals(ring.varNames[n_var]))  return n_var;
            }
        }
        
        int ind = ring.CForm.List_of_Change.indexOf(var);
        if(ind < 0){
            for(int i = 0; i < ring.CForm.List_of_Change.size(); i++){
                Element elList = ring.CForm.List_of_Change.get(i);
                if( (elList instanceof F) &&
                    (((F) elList).name == F.POW) &&
                    (((F) elList).X[0].compareTo(var, ring) == 0) ){
                    
                    n_var = i + ring.varPolynom.length;
                    break;
                }
            }
        }else{
            n_var = ind + ring.varPolynom.length;
        }
     
        
        return n_var;
    }

    /**
     * Нахождение числового множителя функции el.
     */
    public Element numberCoeff(Element el, Ring ring) {
        if (el instanceof F) {
            F ff = (F) el;
            if (ff.name == F.ADD || ff.name == F.SUBTRACT) {
                Element n = numberCoeff(ff.X[0], ring);
                for (int j = 1; j < ff.X.length; j++) {
                    n = n.GCD(numberCoeff(ff.X[j], ring), ring);
                }
                return n;
            }
            if (ff.name == F.MULTIPLY) {
                Element n = NumberZ.ONE;
                for (int j = 0; j < ff.X.length; j++) {
                    n = numberCoeff(ff.X[j], ring);
                    if (!n.isOne(ring)) {
                        return n;
                    }
                }
                return n;
            }
            if(ff.name == F.DIVIDE) {
                return numberCoeff(ff.X[0], ring).divide(numberCoeff(ff.X[1], ring), ring);
            }
        } else {
            if (el instanceof Polynom) {
                if (((Polynom) el).coeffs.length != 1) {
                    Polynom pol = ((Polynom) el).factorOfPol_inQ(false, ring).multin[0];
                    if (pol.isItNumber()) {
                        return pol;
                    }
                } else {
                    return ((Polynom) el).coeffs[0];
                }
            }
            if (el.numbElementType() < Ring.Polynom) {
                return el;
            }
            if (el instanceof Fname && ((Fname) el).X[0] == null) {
                return el;
            }
        }
        return NumberZ.ONE;
    }
    
    /**
     * Проверка, содержит ли выражение func переменную argInt.
     * @param func
     * @param argInt
     * @return 
     */
    public boolean containsVar(Element func, Polynom argInt) {
        boolean result = false;
        if(func instanceof F) {
            for(int i=0; i<((F)func).X.length; i++) {
                result = result || containsVar(((F)func).X[i], argInt);
            }
        } else if(func instanceof Fname) {
            if(((Fname)func).X[0] != null) {
                return containsVar(((Fname)func).X[0], argInt);
            }
        } else if(func instanceof Fraction) {
            result = result || containsVar(((Fraction)func).num, argInt);
            result = result || containsVar(((Fraction)func).denom, argInt);
        } else if(func instanceof Polynom) {
            if(((Polynom)func).degree(argInt.powers.length - 1) > 0) {
                return true;
            } else {
                return false;
            }
        }
        return result;
    }

    public Fname findElementOfTypeFnameInList(ArrayList<Fname> simbolName, String s) {
        for (int i = 0; i < simbolName.size(); i++) {
            if (simbolName.get(i).name.equals(s)) {
                return simbolName.get(i);
            }
        }
        return null;
    }

    /**
     * Выделяет в подынтегральном выражении последовательность подвыражений f0,
     * ..., fn. <br><br>
     *
     * Пример: <br>
     * Пусть ln(x*exp(x))+exp(ln(x)+exp(x)) - исходное выражение, тогда получим
     * следующюю последовательность подвыражений:<br>
     * f0 = exp(x) <br>
     * f1 = ln(x*f0) <br>
     * f2 = ln(x) <br>
     * f3 = exp(f2+f0) <br>
     * f = f1+f3 <br>
     */
    public Element makeListOfLNandExp(Element func, ArrayList<Fname> simbolName, Vector<F> vLnAndExp, Polynom argInt, Ring ring) {
        func = func.ExpandFnameOrId();
        if (func instanceof F) {
            F funcF = (F) func;
            switch (funcF.name) {
                case F.SQRT:
                case F.CUBRT:
                case F.ROOTOF:
                    if (vLnAndExp.indexOf(funcF) >= 0) {
                        return findElementOfTypeFnameInList(simbolName, "f" + vLnAndExp.
                                indexOf(funcF));
                    } else {
                        int ind = simbolName.size();
                        vLnAndExp.add(funcF);
                        Element arg = makeListOfLNandExp(funcF.X[0], simbolName,
                                    vLnAndExp, argInt, ring);
                        Fname nnff=new Fname("f" + ind, new F(funcF.name, arg));
                        simbolName.add(nnff);
                        return nnff;
                    }
                case F.LN:
                case F.EXP: {
                    if(!containsVar(func, argInt)) return func; 
                    if (funcF.name == F.EXP) {
                        Element arg = funcF.X[0];
                        Element number = numberCoeff(arg, ring);
                        if ((number.isOne(ring) == false) &&
                            (number.isZero(ring) == false) &&
                            (number.isComplex(ring) == false) &&
                            isItFracNumber(number, ring) == false) {
                            func = new F(F.EXP, arg.divide(number, ring).expand(ring));
                            int n = F.intPOW;
                            return new F(n, new Element[] {makeListOfLNandExp(func,
                                simbolName, vLnAndExp, argInt, ring), number});
                        }
                    }
                    if (vLnAndExp.indexOf(funcF) >= 0) {
                        return findElementOfTypeFnameInList(simbolName, "f" + vLnAndExp.
                                indexOf(funcF));
                    } else {
                        int ind = vLnAndExp.size();
                        vLnAndExp.add(funcF);
                        Element arg = makeListOfLNandExp(funcF.X[0], simbolName,
                                vLnAndExp, argInt, ring);
                        Fname nnff=new Fname("f" + ind, new F(funcF.name, arg));
                        simbolName.add(nnff);
                        return nnff;
                    }
                }
                case F.POW:
                    if(isItFracNumber(funcF.X[1], ring) &&
                      (  (funcF.X[0] instanceof F && ((F)funcF.X[0]).name != F.EXP) || funcF.X[0] instanceof F == false )  ) {
                        
                        Ring ringQ = (ring.algebra[0] == Ring.Q)? ring : new Ring("Q[]");
                        Fraction pow = (Fraction) funcF.X[1].toNewRing(ringQ);
                        Element arg = makeListOfLNandExp(funcF.X[0], simbolName,
                                    vLnAndExp, argInt, ring);
                        
                        if (pow.num.toNewRing(ringQ).isOne(ring) == false) {
                            arg = new F(F.intPOW, new Element[]{arg, pow.num});
                        }
                        F root = null;
                        if(pow.denom.toNewRing(ringQ).equals(ringQ.posConst[2].toNewRing(ringQ), ring)) {
                            root = new F(F.SQRT, arg);
                        } else if(pow.denom.toNewRing(ringQ).equals(ringQ.posConst[3].toNewRing(ringQ), ring)) {
                            root = new F(F.CUBRT, arg);
                        } else {
                            root = new F(F.ROOTOF, arg, pow.denom.toNewRing(ringQ));
                        }
                            
                        if (vLnAndExp.indexOf(root) >= 0) {
                            Element el = findElementOfTypeFnameInList(simbolName,
                                    "f" + vLnAndExp.indexOf(root));
                            return (pow.num.toNewRing(ringQ).isOne(ringQ))?
                                    el:
                                    new F(F.intPOW, el, pow.num.toNewRing(ringQ));
                        } else {
                            int ind = simbolName.size();
                            vLnAndExp.add(root);
                            Fname nnff=new Fname("f" + ind, root);
                            simbolName.add(nnff);
                            return (pow.num.toNewRing(ringQ).isOne(ringQ))?
                                    nnff:
                                    new F(F.intPOW, nnff, pow.num.toNewRing(ringQ));
                        }
                    }
                case F.intPOW: {
                    
                    return new F(funcF.name,
                            new Element[] {makeListOfLNandExp(funcF.X[0],
                                        simbolName, vLnAndExp, argInt, ring), funcF.X[1]});
                }
                default: {
                    Element[] els = new Element[funcF.X.length];
                    if (funcF.name == F.DIVIDE && (funcF.X[1] instanceof F) && ((F) funcF.X[1]).name == F.EXP) {
                        return new F(F.MULTIPLY,
                                new Element[] {makeListOfLNandExp(funcF.X[0],
                                            simbolName, vLnAndExp, argInt, ring), new F(F.intPOW,
                                            new Element[] {makeListOfLNandExp(funcF.X[1],
                                                        simbolName, vLnAndExp, argInt, ring), NumberZ.MINUS_ONE}).
                                    expand(ring)}).expand(ring);
                    }
                    for (int i = 0; i < funcF.X.length; i++) {
                        els[i] = makeListOfLNandExp(funcF.X[i], simbolName,
                                vLnAndExp, argInt, ring);
                    }
                    return new F(funcF.name, els);
                }
            }
        } else {
            return func;
        }
    }

    public static F[] addArray(F[] f1, F[] f2) {
        if (f1 != null && f2 != null) {
            F[] vrem = new F[f1.length];
            System.arraycopy(f1, 0, vrem, 0, f1.length);
            f1 = new F[f1.length + f2.length];
            System.arraycopy(vrem, 0, f1, 0, vrem.length);
            System.arraycopy(f2, 0, f1, vrem.length, f2.length);
            return f1;
        } else {
            if (f1 == null) {
                return f2;
            }
            if (f2 == null) {
                return f1;
            }
        }
        return null;
    }
    /*
     * Отделение полиномиальной части и дробно-рац. части интеграла.
     * @param func подынтегральное выражение
     * @return массив, на 0 месте которого стоит полином. часть,
     * а на 1 - дробно-рац. часть интеграла
     */
// Рома - дай сдесь простой код от каноника...!!!

    public static Element[] razbienie(Element func) {
        Element polPart = null, fracPart = null;
        if (func instanceof F) {
            F f = (F) func;
            if (f.name == F.ADD) {
                F f1 = null, f2 = null;
                Vector<Element> el1 = new Vector<Element>(), el2 = new Vector<Element>();
                for (int i = 0; i < f.X.length; i++) {
                    F arg = f.X[i] instanceof F ? (F) f.X[i] : new F(f.X[i]);
                    if (arg.name == F.DIVIDE) {
                        el2.add(f.X[i]);
                    } else if (arg.name == F.MULTIPLY) {
                        boolean flag = false;
                        int k = 0;
                        for (int j = 0; j < arg.X.length; j++) {
                            if ((arg.X[j] instanceof F) && (((F) arg.X[j]).name == F.DIVIDE)) {
                                flag = true;
                                k = j;
                            }
                        }
                        if (flag) {
                            Element[] Chisl = new Element[arg.X.length];
                            for (int j = 0; j < arg.X.length; j++) {
                                if (j != k) {
                                    Chisl[j] = arg.X[j];
                                } else {
                                    Chisl[j] = ((F) arg.X[j]).X[0];
                                }
                            }
                            el2.add(new F(F.DIVIDE, new Element[] {new F(F.MULTIPLY, Chisl),
                                ((F) arg.X[k]).X[1]}));
                        } else {
                            el1.add(f.X[i]);
                        }
                    } else {
                        el1.add(f.X[i]);
                    }
                }
                Element[] ell1 = new Element[el1.size()], ell2 = new Element[el2.size()];
                for (int i = 0; i < el1.size(); i++) {
                    ell1[i] = el1.elementAt(i);
                }
                for (int i = 0; i < el2.size(); i++) {
                    ell2[i] = el2.elementAt(i);
                }
                if (el1.size() != 0) {
                    f1 = new F(F.ADD, ell1);
                }
                if (el2.size() != 0) {
                    if (el2.size() == 1) {
                        f2 = (F) ell2[0];
                    } else {
                        f2 = new F(F.ADD, ell2);
                    }
                }
                return new F[] {f1, f2};
            }
            if (f.name == F.DIVIDE) {
                if (f.X[1].numbElementType() < 8 || (f.X[1] instanceof Polynom && ((Polynom) f.X[1]).powers.length == 0) || f.X[1] instanceof Complex) {
                    return new Element[] {f, fracPart};
                }
                return new Element[] {null, f};
            } else {
                return new Element[] {f, null};
            }
        } else {
            if (func instanceof Fraction && ((Fraction) func).denom instanceof Polynom) {
                return new Element[] {polPart, func};
            }
            return new Element[] {func, fracPart};
        }
    }

    /**
     * Производная от трансцендентной функции. Пример: Пусть f0 = exp(x), f1 =
     * (x*f0), тогда D f1 = (f0+x*f0)/(x*f0)<br>
     */
    public Element DStructurTheorem(Element f, int varNumb, Ring ring) {
        Element arg = null, arg2 = null;
        f = f.expand(ring);//if(f instanceof F) f = f.ExpandFnameOrId();
        if (f instanceof Polynom) {
            return ((Polynom) f).D(varNumb, ring);
        }
        if (f instanceof Fraction) {
            return (((Fraction) f).num.D(varNumb, ring).multiply(((Fraction) f).denom, ring).
                    subtract(((Fraction) f).num.multiply(((Fraction) f).denom.D(varNumb, ring),
                                    ring), ring)).divide(((Fraction) f).denom.multiply(
                                    ((Fraction) f).denom, ring), ring);
        }
        if (f instanceof Fname && ((Fname) f).X != null) {
            if (((Fname) f).X[0] instanceof F) {
                F fun = (F) ((Fname) f).X[0];
                switch (fun.name) {
                    case F.LN:
                        Element Dln = DStructurTheorem(fun.X[0], varNumb, ring);
                        Element x1 = Dln;
                        Element x2 = (fun.X[0] instanceof F)&&( ((F)fun.X[0]).name == F.ABS ) ?
                                ((F) fun.X[0]).X[0] : fun.X[0];
                        return new F(F.DIVIDE, new Element[] {x1, x2});

                    case F.EXP:
                        Element DExp = DStructurTheorem(fun.X[0], varNumb, ring);
                        if (DExp.isOne(ring)) {
                            return f;
                        }
                        return new F(F.MULTIPLY, new Element[] {f, DExp}).expand(ring);
                }
            } else if(((Fname) f).X[0] instanceof Polynom) {
                Polynom p = (Polynom) ((Fname) f).X[0];
                return p.D(varNumb, ring);
            }
        }
        if (f instanceof F) {
            F fun = (F) f;
            switch (fun.name) {
                case F.ID:
                case F.ABS:
                    return DStructurTheorem(fun.X[0], varNumb, ring);
                case F.POW:
                case F.intPOW:
                    Element DintPow = DStructurTheorem(fun.X[0], varNumb, ring);
                    if (DintPow instanceof Fname && ((Fname) DintPow).X[0] instanceof F && ((F) ((Fname) DintPow).X[0]).name == F.EXP) {
                        Element argg = new F(fun.name, new Element[] {fun.X[0], fun.X[1]});
                        return new F(F.MULTIPLY, new Element[] {fun.X[1], argg}).expand(
                                ring);
                    }
                    Element argg = new F(fun.name, new Element[] {fun.X[0], fun.X[1].
                        subtract(NumberZ.ONE, ring)});
                    return new F(F.MULTIPLY, new Element[] {fun.X[1], argg, DintPow}).
                            expand(ring);

                case F.SUBTRACT:
                case F.ADD:
                    Element[] DArg = new Element[fun.X.length];
                    for (int i = 0; i < fun.X.length; i++) {
                        DArg[i] = DStructurTheorem(fun.X[i], varNumb, ring);
                    }
                    return new F(fun.name, DArg);

                case F.MULTIPLY:
                    arg = fun.X[1];
                    if (fun.X.length > 2) {
                        for (int i = 2; i < fun.X.length; i++) {
                            arg = new F(F.MULTIPLY, new Element[] {arg, fun.X[i]});
                        }
                    }
                    Element Darg = DStructurTheorem(arg, varNumb, ring);
                    Element DvU = null;
                    if (!Darg.isZero(ring)) {
                        DvU = new F(F.MULTIPLY, new Element[] {Darg, fun.X[0]});
                    }
                    F DuV = new F(F.MULTIPLY, new Element[] {DStructurTheorem(fun.X[0],
                        varNumb, ring), arg});
                    if (DvU == null) {
                        return DuV;
                    }
                    return new F(F.ADD, new Element[] {DvU, DuV});

                case F.DIVIDE:
                    F VV = new F(F.MULTIPLY, new Element[] {fun.X[1], fun.X[1]});
                    F DvMU = new F(F.MULTIPLY, new Element[] {DStructurTheorem(fun.X[1],
                        varNumb, ring), fun.X[0]});
                    F DuMV = new F(F.MULTIPLY, new Element[] {DStructurTheorem(fun.X[0],
                        varNumb, ring), fun.X[1]});
                    F TSub = new F(F.SUBTRACT, new Element[] {DuMV, DvMU});
                    return new F(F.DIVIDE, new Element[] {TSub, VV});

            }
        }
        return NumberZ.ZERO;
    }

    public static void main(String[] args) throws Exception {
        Ring ring = new Ring("R64[x,y,t]");
        System.out.println("=============="+  new Integrate().integrate(new F("\\sin(x)",ring), ring.varPolynom[0] , ring) );
        
        F[] f = new F[] {
            //      new F("((2x^8+1)\\sqrt(x^8+1))/(x^17+2x^9+x)", ring),
            new F("x", ring), //1/2x^2
            new F("x^6 + x - 2", ring), //1/7x^7+1/2x^2-2x
            new F("x^4", ring), //1/5x^5
            new F("1", ring), //x
            new F("x^2+1", ring), //1/3x^3+x
            new F("\\exp(x)+\\exp(-x)", ring), //(\exp(x)-((\exp(x))^{-1}))
            new F("\\exp(x)*\\exp(\\exp(x))", ring), //\exp(\exp(x))
            new F("\\exp(-x)", ring), //(-1*\exp(-x))
            new F("\\exp(x)", ring), //\exp(x)
            new F("\\exp(2x)", ring), //\exp(2x)/2
           // ------------------10--------------------
            new F("x*\\exp(x)", ring), //(x*\exp(x)-\exp(x))
            new F("\\exp(x^2)", ring), //\int\exp(x^2) dx
            new F("x^2*\\exp(x^2)", ring), //\intx^2*\exp(x^2) dx
            new F("x*(\\exp(x^2))", ring), //\exp(x^2)/2
            new F("x^2\\exp(x\\ln(x))", ring), //\intx^2*\exp(x*f1) dx
            new F("\\exp(x+1)", ring), //\exp(x+1)
            new F("(\\exp(x))^2+\\exp(x)+x^2", ring),//(2/3x^3+2*\exp(x)+(\exp(x))^2)/2
            new F("\\exp(x^2+2x+1)", ring), //
            new F("\\exp(\\exp(x))", ring), //\int\exp(f1) dx
            new F("\\ln(x\\exp(x))+\\exp(\\ln(x)+\\exp(x))", ring),//simplify intPol 722 line
            // ------------------20--------------------
            new F("(1+2*\\ln(x))\\exp((\\ln(x))^2)", ring),//simplify proverka 1->0 IntPolynom-->606 line
            new F("2*\\ln(x)*\\exp((\\ln(x))^2)", ring),//simplify proverka
            new F("(2x-1)\\exp(1/x)", ring),
            new F("-1/x^2\\exp(1/x)", ring),
            new F("(1/x+\\ln(x))\\exp(x)", ring),
//            new F(
//!!!            "(\\exp(x)-x^2+2x)/((\\exp(x)+x)^2*x^2)\\exp((x^2-1)/(x)+(1)/(\\exp(x)+x))",
//            ring),
            new F("(4x^2+4x-1)/(x+1)^2*(\\exp(x^2))^2", ring),
            new F("x^2\\ln(x^3+1)", ring),
            new F("\\ln(x)", ring),
            new F("\\ln(x\\exp(x))+\\ln(x)", ring),
          // ------------------30--------------------
            new F("\\ln(x*\\exp(x))", ring),
            new F("x^3\\ln(x*\\exp(x))+x^3", ring),
            new F("\\ln(x*\\exp(x))+(\\ln(x*\\exp(x)))^{3}+x^3", ring),
            new F("x\\ln(x)", ring),
            new F("\\ln(x+1)+\\ln(x+2)+\\ln(x+3)", ring),
            new F("\\ln(x+1)+\\exp(x+3)+\\ln((x+1)/(x+4))", ring),
            new F("-2*\\ln((x-1)/(x+1))", ring),//   ElementToPolynom(c)??????? in strucTeorem
            new F("\\ln(x+1)+\\ln((x+2)/(x+3))+\\ln(x+3)+\\ln((x+1)/(x+4))", ring),//   ElementToPolynom(c)??????? in strucTeorem
            new F("x^2\\ln(x)+x^3+4", ring),
            new F("1/(x+1)", ring),
            new F("(x+2)/(x^2+4x+2)", ring),
            new F("(x^5-x^4+4x^3+x^2-x+5)/(x^4-2x^3+5x^2-4x+4)", ring),
            //      new F("(8x^9+x^8-12x^7-4x^6-26x^5-6x^4+30x^3+23x^2-2x-7)/(x^10-2x^8-2x^7-4x^6+7x^4+10x^3+3x^2-4x-2)", ring),
            //      new F("(6x^7+7x^6-38x^5-53x^4+40x^3+96x^2-38x-39)/(x^8-10x^6-8x^5+23x^4+42x^3+11x^2-10x-5)", ring),

            //      new F("(6x^5+6x^4-8x^3-18x^2+8x+8)/(x^6-5x^4-8x^3-2x^2+2x+1)", ring),
            new F("(x^3+6x^2+13x+6)/(x^4+4x^3-16x-16)", ring), //\ln(x-2)-1/(2*(x+2)^2)
//            new F("(2x^2+2x+13)/((x-2)(x^2+1)^2)", ring), //
            new F(F.DIVIDE, new Element[]{new Polynom("2x^2+2x+13", ring),
                new F(F.MULTIPLY, new Element[]{new Polynom("x-2", ring),
                    new F(F.intPOW, new Polynom("x^2+1", ring), new NumberZ("2"))}
                )}
            ),
//            new F("x/((x+1)(x^2+1)^3(x+2)^4)", ring),
            new F(F.DIVIDE, new Element[]{new Polynom("x", ring),
                new F(F.MULTIPLY, new Element[]{new Polynom("x+1", ring),
                    new F(F.intPOW, new Polynom("x^2+1", ring), new NumberZ("3")),
                    new F(F.intPOW, new Polynom("x+2", ring), new NumberZ("4"))}
                )}
            ),
            new F("1/(1+\\exp(x))", ring),
//            new F("-\\exp(x)/(1+\\exp(x))^2", ring),
            new F(F.DIVIDE, new Element[]{new F("-\\exp(x)", ring), new F(F.intPOW, new F("1+\\exp(x)", ring), new NumberZ("2"))}),
            new F(
            "\\ln(x*\\exp(x))+(\\ln(x))^{3}+x^3+(x^3+6x^2+13x+6)/(x^4+4x^3-16x-16)",
            ring),
            new F("1/(x^8-2x^6+2x^2-1)", ring),
            //      new F("1/(x\\ln(x))", ring),//FactorPol_SquareFree xu = x^2u
            //      new F("1/\\ln(x)", ring),
//            new F("(x+1)^2/(1-x)^2", ring),
            new F(F.DIVIDE, new Element[]{new F(F.intPOW, new Polynom("x+1", ring), new NumberZ("2")), new F(F.intPOW, new Polynom("1-x", ring), new NumberZ("2"))}),
            new F("(x^2-1)/(x-1)", ring),
            new F("(2x^2+2x+13)/((x-2)(x^4+2x^2+1))", ring),
            new F("(\\exp(x)+\\exp(-x))/(\\exp(x)-\\exp(-x))", ring)
//      new F("x(x+1)((x^2\\exp(2x^2)-\\ln(x+1)^2)^2+2x\\exp(3x^2)(x-(2x^3+2x^2+x+1)\\ln(x+1)))/((x+1)\\ln(x+1)^2-(x^3+x^2)\\exp(2x^2))^2", ring)
        //         str1 = "(3x^2+4x+5+2x)/(x^4+1)";//not????????????????????????????
//str1 = "x(x+1)((x^2\\exp(2x^2)-\\ln(x+1)^2)^2+2x\\exp(3x^2)(x-(2x^3+2x^2+x+1)\\ln(x+1)))/((x+1)\\ln(x+1)^2-(x^3+x^2)\\exp(2x^2))^2";//????
        //         str1 = "\\ln(x*\\exp(\\ln(x)+\\exp(x)))+(\\ln(x*\\exp(x)))^{3}+x(x+1)((x^2\\exp(2x^2)-\\ln(x+1)^2)^2+2x\\exp(3x^2)(x-(2x^3+2x^2+x+1)\\ln(x+1)))/((x+1)\\ln(x+1)^2-(x^3+x^2)\\exp(2x^2))^2+\\exp(x)";
        //         str1 = "(((\\exp(x)+\\exp(-x))/2/(\\exp(x)+\\exp(-x))/(\\exp(x)-\\exp(-x))+(\\exp(x^2)-\\exp(-x^2))/2)-(\\exp(x^4 +x -8)+\\ex  p((-1)*(x^4 +x -8)))/2)";
        //         str1 = "((\\exp(x)-\\exp(-x))/(\\exp(x)+\\exp(-x))-(\\exp(x)+\\exp(-x))/2)*(\\exp((\\exp(-x^2 +x +8)-\\exp((-1)*(-x^2 +x +8)))/2)+\\exp((-1)*(\\exp(-x^2 +x +8)-\\exp((-1)*(-x^2 +x +8)))/2))/(\\exp((\\exp(-x^2 +x +8)-\\exp((-1)*(-x^2 +x +8)))/2)-\\exp((-1)*(\\exp(-x^2 +x +8)-\\exp((-1)*(-x^2 +x +8)))/2))";
        };
        F[] fcomplex = new F[] {
            new F("0", ring),
            new F("1", ring),
            new F("x^s", ring),
            new F("1/x", ring),
            new F("1/(x+x^3)", ring),//????????????????????alfa not as new var
            new F("1/(-2+x^2)", ring),
            new F("1/(1+x^2)", ring),//arct x
            new F("\\sin(x)", ring),//-cos x
            new F("\\cos(x)", ring),//sin x
            new F("1/\\sin(x)^2", ring),//-ctg x  POW  (\exp(x))^{1\i} new var in List_of_Cha...
            new F("1/\\cos(x)^2", ring),//tg x  (\exp(x))^{1\i} new var in List_of_Cha...
            new F("x\\ch(3x^2)", ring),
            new F("\\sh(x)", ring),//((\exp(x))^{-1}+\exp(x))/2 = ch x
            new F("\\ch(x)", ring),//(\exp(x)-((\exp(x))^{-1}))/2 = sh x
            //      new F("1/\\sh(x)^2", ring),//(-2*(\exp(x))^2)/((\exp(x))^2-1) = -cth x
            //      new F("1/\\ch(x)^2", ring),//th x
            //      new F("(2x^2+1)^3", ring),//8/7x^7+12/5x^5+2x^3+x
            //      new F("((x+1)(x^2-3))/(3x^2)", ring),//1/6x^2+1/3x-ln x+1/x
            //      new F("1/(x-s)", ring),//ln(x-s)
            //      new F("((\\exp(x)-1)*(\\exp(2x)+1))/\\exp(x)", ring),//1/2 exp(2x)-exp(x)+x+exp(-x)
            //      new F("1/(x^2-5x+6)", ring),//ln((x-3)/(x-2))
            //      new F("1/(4x^2+4x-3)", ring),//1/8ln((2x-1)/(2x+3))
            new F("\\sin(x)^3*\\cos(x)", ring),//sin^4 x/4
            new F("\\sin(x)^3", ring),//cos^3 x/3-cos x

            //      new F("\\sin(x)/\\cos(x)", ring),
            new F("\\sin(x)/\\cos(x)*\\cos(x)", ring),
            new F("\\sin(-x)", ring),
            new F("\\tg(x)", ring),
            new F("\\ctg(x)", ring),
            new F("\\th(x)", ring),
            new F("\\cth(x)", ring),
            //           new F("\\arccsc(x) ", ring), sqrt
            //           new F("\\arcsec(x)", ring), sqrt
            //      new F("\\arcctg(7x)", ring),
            //      new F("5\\arctg(3x)", ring),
            //           new F("\\arcsin(x)",ring), sqrt
            //           new F("\\arccos(x)",ring), sqrt
            //           new F("1/5\\arcctg(x)+3\\arccos(4x)-2\\arcsin(x-2)",ring), sqrt
            //           new F("7\\arccsc(x) - 6\\arcsec(3x)", ring), sqrt
            //           new F("1/2\\sh(x-2)+7\\cth(4x-3)", ring),//проверить ????
            new F("b*x", ring),
            new F("3\\cos(x)+7\\sin(x)", ring),
            new F("\\tg(x)*\\ctg(x)", ring), //           new F("\\tg(2x)+\\ctg(3x)",ring)проверить
        };
        Element arg = ring.varPolynom[0];
        for (int j = 0; j < fcomplex.length; j++) {
            System.out.println("INPUT1-----> " + j + " element: " + fcomplex[j].toString(ring));
            Element resultInteg = new Integrate().integrate(fcomplex[j], arg, ring );
            ring.CForm.returnFirstStatsCForm();
            Element aaaaa = resultInteg.expand(ring);
            System.out.println("OUTPUT----aaaaaaaaaaaaaaaaaa> " + aaaaa);
            //((F)resultInteg).showTree(ring);
            Element t = resultInteg.expand(ring);
            System.out.println(" thns;othosith;s ;tsr" + t.toString(ring));
            //int enteringFactorInLog = 0; // не будем вводит множитель под логарифм
            F ff=(resultInteg instanceof F)? (F)resultInteg : new F(F.ID, resultInteg);       
            Element res = ff.Factor(false, ring); //LN_EXP_POW_LOG_LG.fullFactor(resultInteg, enteringFactorInLog, ring);
            res = (res == null) ? resultInteg : res;
            System.out.println("OUTPUT----> " + res.expand(ring));
//      if(j!=11)  System.out.println(t+"res"+ res.simplify(ring));
//      if(j!=32) System.out.println("OUTPUT----> " + resultInteg.simplify(ring));
            System.out.println("");
        }
        for (int j = 0; j < f.length; j++) {
            System.out.println("INPUT-----> " + j + " element: " + f[j].toString(ring));
            Element integF = (new Integrate()).mainProcOfInteg(f[j], arg, ring );
            ring.CForm.returnFirstStatsCForm();
            System.out.println("OUTPUT----> " + integF);
            if (j != 43 &&
                j != 44 && 
                j != 47 &&
                j != 48) {
                System.out.println("OUTPUTиииии----> " + integF.expand(ring));
            }
            System.out.println("");

        }
    }

    /**
//     * Перед интегрированием делается попытка тригонометрические функции замень
//     * * на функции в комплексной области
//     *
//     * @param f
//     * @param ring
//     * @param cf
//     *
//     * @return
//     *
//     * @throws Exception
//     */
//    private Element integrate1(F f, Element arg, Ring ring ) throws Exception {
//        F g0 = f.expand(ring);
//        F g=g0.toComplex( ring);  
//        int tempAlg0=ring.algebra[0];
//        if(!(g.subtract(f, ring).expand(ring).isZero(ring))){ring.algebra[0]|=Ring.Complex;
//           ring.CForm.newRing.algebra[0]=ring.algebra[0];
//           ring.CForm.RING.algebra[0]=ring.algebra[0];
//        }
//        Element res = new Integrate().mainProcOfInteg(g, arg, ring ); 
//        ring.algebra[0]=tempAlg0;
//        ring.CForm.newRing.algebra[0]=tempAlg0;
//        ring.CForm.RING.algebra[0]=tempAlg0;
//        return res;
//    }
    
        public Element expandRecursive(Element func, Ring ring) {
        if(func instanceof F) {
            Element[] ARGS = new Element[((F)func).X.length];
            
            for(int i = 0; i < ARGS.length; i++) {
                ARGS[i] = ( ((F)func).X[i] instanceof F)?
                        expandRecursive(((F)func).X[i], ring):
                        ((F)func).X[i];
            }
            
            return new F( ((F)func).name, ARGS).expand(ring);
        }
        return func;
    }

    /**
     * Метод вычисления интеграла
     *
     * @param inputF - входная функция интегрирования
     * @param arg - переменная интегрирования
     * @param ring - кольцо
     *
     * @return - выражение - интеграл от фходной функции
     */
    public Element integrate(Element inputF, Element arg, Ring ring) {
        F inpF=new F(F.INT,inputF, arg );
        Element E1,E2,E3,E4,E5,E6;
        // put such key! Then we have to take it back!
        ring.setGROUP_NEGATIVE_TERMS(Element.FALSE); // не надо группопрвать вместе отрицательные слагаемые
        int varsForInt = ring.CForm.index_var_for_Integration;
        int oldListSize = ring.CForm.List_of_Change.size();
        inputF = convertFnamesToPolynoms(inputF, ring);
        String[] newVars = new String[ring.CForm.List_of_Change.size() - oldListSize];
        for(int i=0; i<newVars.length; i++) {
            newVars[i] = ring.CForm.List_of_Change.get(i + oldListSize).toString();
        }
        // если  чиcло то просто умножаем на переменную интегрирования
        if (inputF.numbElementType() < Ring.Polynom) {return inputF.multiply(arg, ring);}
        if (inputF instanceof F) { 
        try {
            ring.CForm.index_var_for_Integration += newVars.length;
            int tempAlg0=ring.algebra[0];
            if(inputF instanceof F){       
                F g0 = (F)inputF.expand(ring);
                F g=g0.toComplex( ring);  
                if(!(g.subtract(inputF, ring).expand(ring).isZero(ring))){
                    ring.algebra[0]|=Ring.Complex;inputF=g;
                }else inputF=g0;                   
                ring.CForm.newRing.algebra[0]=ring.algebra[0];
                ring.CForm.RING.algebra[0]=ring.algebra[0];
            }
            ring.algebra[0]=tempAlg0;
            ring.CForm.newRing.algebra[0]=tempAlg0;
            ring.CForm.RING.algebra[0]=tempAlg0;
            E1 = mainProcOfInteg((F) inputF, arg, ring.addVariables(newVars) );
            if((E1 instanceof F)&&(((F)E1).name==F.INT))  return inpF;
            } catch (Exception ex) {
                ring.exception.append("Error integrating " + inputF.toString(ring)+ex);
                ex.printStackTrace();
                return inpF;  //throw new MathparException("Error integrating " + inputF.toString(ring), ex);
            }
            ring.CForm.index_var_for_Integration = varsForInt;
            E1 = ring.CForm.ElementToF(E1);       
         //   try {
                E3 = expandRecursive(E1.ExpandFnameOrId(), ring);   
                E3 = E3.ExpandLog(ring);
//             } catch (Exception ex) { ring.exception.append("Error expand function " + E1.toString(ring)+" after integrating: "+ex);
//             if ((E1 instanceof F)&&(((F) E1).name==F.INT)){E1=new F(F.INT,((F) E1).X[0], arg);}
//             return  E1;
//             //throw new MathparException("Error integrating " + inputF.toString(ring), ex);
//            }   
    //        try {
                if (E3 instanceof F) {
                    
                    if (((F)E3).name == F.DIVIDE  &&  ((F)E3).X[1] instanceof F) {
                        Element num = ((F)E3).X[0];
                        F denom = (F) ((F)E3).X[1];
                        if ( ((F) ((F)E3).X[1]).name == F.EXP ) {
                            Element mul = new F(F.EXP, new Element[]{denom.X[0].multiply(ring.numberMINUS_ONE, ring)});
                            E3 = new F(F.MULTIPLY, new Element[]{num, mul}).simplify(ring).expand(ring);
                        } else if (((F) ((F)E3).X[1]).name == F.MULTIPLY) {
                            Element mul = null;
                
                            for (int i = 0; i < denom.X.length; i++) {
                                if (denom.X[i] instanceof F && ((F)denom.X[i]).name == F.EXP) {
                                    mul = new F(F.EXP, new Element[]{((F)denom.X[i]).X[0].multiply(ring.numberMINUS_ONE, ring)});
                                    denom.X[i] = ring.numberONE;
                                    num = new F(F.MULTIPLY, new Element[]{num, mul}).simplify(ring).expand(ring);
                                }
                            }
                        }
                    }
                    
                    
                    
                    int prevAlgebra0=ring.algebra[0];
                    ring.algebra[0]|=Ring.Complex;
                    E4=((F)E3).FACTOR(ring); 
                    ring.algebra[0]=prevAlgebra0;
                }
                else E4 = E3.Factor(true,ring);
//             } catch (Exception ex) { ring.exception.append("Error Factor of function " + E3.toString(ring)+" after integrating: "+ex);
//             if ((E3 instanceof F)&&(((F) E3).name==F.INT)){E3=new F(F.INT,((F) E3).X[0], arg);}
//             return  E3;//throw new MathparException("Error integrating " + inputF.toString(ring), ex);
//            }
                if(E4 instanceof FactorPol) {
                    E4 = ((FactorPol)E4).toPolynomOrFraction(ring);
                }
                E5=E4.ExpandLog(ring);//ring.CForm.simplify_init(E4);      
//             } catch (Exception ex) { ring.exception.append("Error simplification of function " + E4.toString(ring)+" after integrating: "+ex);      
//               if ((E4 instanceof F)&&(((F) E4).name==F.INT)){E4=new F(F.INT,((F) E4).X[0], arg);}
//             return E4;
//            }
            
            
            // Неорпеделенный интеграл определяется с точностью до аддитивной константы.
            // Поэтому, если ответ имеет вид: f(x) + c, то удаляем аддитивную
            // константу c. И в качестве ответа выдаем функцию f(x).
            
            if (E5 instanceof F){
                F F5=(F)E5;
                E5= F5.expand(ring);
                if(((F)E5).name==F.ADD){
                    Element[] XX=((F)E5).X;
                    int nu=0;
                    for (int i = 0; i < XX.length; i++) {
                        if(!XX[i].isItNumber(ring)) {
                            XX[nu]=XX[i];
                            nu++;
                        }
                    }
                    if(nu==1) E5=XX[0];
                    else if(nu<XX.length){
                        Element[] xx=new Element[nu];
                        System.arraycopy(XX, 0, xx, 0, nu);
                        ((F)E5).X=xx;
                    }
                }
            }

            // Если фукнция является дробной, то она может быть представлена в виде:
            // [p(x) + a*q(x)]/q(x), где a --- число.
            // Это выражение можно преобразовать так:
            // p(x)/q(x) + a. В качестве ответа нужно выдать выражение p(x)/q(x).
            // В таком случае делим с остатком числитель на знаменатель,
            // находим число a и вычитаем из функции число a. Получаем дробь p(x)/q(x).
            Element polOrFrac = ring.CForm.ElementConvertToPolynom(E5);
            if(polOrFrac instanceof Fraction) {
                Element[] quotRem = ((Fraction)polOrFrac).num.quotientAndRemainder(((Fraction)polOrFrac).denom, ring);
                boolean flag = false;
                Element number = null;
                if(quotRem[0].isItNumber()) {
                    flag = true;
                    number = quotRem[0];
                }
                  
                if(quotRem[0] instanceof Polynom) {
                    Polynom[] pols = ring.CForm.dividePolynomToMonoms((Polynom) quotRem[0]);
                    if(pols[pols.length - 1].isItNumber()) {
                        flag = true;
                        number = pols[pols.length - 1];
                    }
                    
                }
                  
                if(flag) {
                    E5 = new F(F.SUBTRACT, E5, number).expand(ring);
                }
            }
            
            
   //       E6= E5.Factor(false, ring);
            ring.CForm=new CanonicForms(ring,true);
            F.cleanOfRepeating(E5, ring);
            
            ring.setGROUP_NEGATIVE_TERMS(Element.TRUE);
            if ((E5 instanceof F)&&(((F) E5).name==F.INT)){
                E5=new F(F.INT,((F) E5).X[0], arg);
            }

            return E5;  
        }  
        // иначе смотрим индекс переменой интегрирования и вызываем абстрактный метод из Element
        int index_integra = ((Polynom) arg).powers.length - 1;
        return inputF.integrate(index_integra, ring);
    }
}
