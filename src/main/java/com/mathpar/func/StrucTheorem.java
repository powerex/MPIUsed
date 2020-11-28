/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import java.util.ArrayList;
import java.util.Vector;
import com.mathpar.matrix.MatrixS;
import com.mathpar.number.*;
import com.mathpar.polynom.Polynom;

/**
 *
 * @author kamp
 */
public class StrucTheorem {

  public StrucTheorem() {
  }

  /**
   * Нахождение последовательности регулярных мономов.
   * 
   * В списке функций simbolName находятся все логарифмы и экспоненты, которые
   * содержатся в подинтегральной функции. Последним элементом этого списка
   * всегда является подинтегральная функция (даже если она не является ни логарифмом,
   * ни экспонентой).
   * 
   * Алгоритм:
   * Берем очередной элемент simbolName. Применяем к нему структкрную теорему.
   * Если он оказывается трансцендентным, то рассматриваем следующий элемент.
   * В противном случае выражаем его через предыдущие элементы simbolName,
   * в каждом следующем элементе simbolName меняем текущий элемент
   * его выражением через предыдущие элементы, и удаляем текущий элемент из simbolName.
   * Просматриваем таким образом элементы simbolName начиная с нулевого,
   * заканчивая предпоследним (саму подинтегральную функцию не рассматриваем,
   * мы только выражаем логарифмы и экспоненты, содержащиеся в ней, через элементы
   * последовательности регулярных мономов).
   * 
   * В результате работы этого алгоритма в списке simbolName будет содержаться
   * последовательность регулярных мономов, и последним элементом simbolName
   * будет подинтегральная функция, составленная из элементов последовательности
   * регулярных мономов.
   * @throws Exception
   */
  @SuppressWarnings("null")
  public void makeRegularMonomialsSequence(ArrayList<Fname> simbolName, Element argInt, Ring ring) throws Exception {
      ArrayList<Element> transcLn = new ArrayList<>();   
   //   CanonicForms cf =ring.CForm; 
    // Массив transcExp используется, когда рассматриваемый элемент списка simbolName
    // является экспонентой.
    // transcLn используется, когда рассматриваемый элемент списка simbolName
    // является логарифмом.
    // transcExp имеет следующую структуру:
    // в предпоследнем элементе содержится функция, которая является
    // правой частью линейной комбинации из структурной теоремы.
    // Эта функция будет содержать объекты Fname, не указывающие на объект типа F.
    // Эти объекты типа Fname являются неизвестными коэффициентами линейной комбинации.
    // Если эти коэффициенты можно найти, то мы можем подставить их в линейную комбинацию
    // из структурной теоремы. Если от обеих частей полученного уравнения взять экспоненту,
    // то получится, что с левой стороны уравнения будет стоять текущий элемент
    // из списка simbolName, а с правой - выражение от уже рассмотренных логарифмов и экспонент.
    // Таким образом получается, что текущий элемент списка simbolName выражен через предыдущие.
    // Именно это выражение текущего элемента через предыдущие (с неизвестными коэффициентами)
    // содержится в последнем элементе transcExp.
    // Массив transcLn устроен аналогично.
    Fname fnameC=new Fname("$c");
    transcLn.add(fnameC);
    transcLn.add(fnameC);
//    F nn0=new F(F.LN, fnameC);
//    Fname c_eq_lnOf_c=new Fname("c", nn0);            
//    transcLn.add(c_eq_lnOf_c);
    ArrayList<Element> transcExp = new ArrayList<Element>();
    transcExp.add(new Fname("$c"));
    F St = null;
    boolean flagLn = false, flagExp = false;
    for(int i = 0; i < simbolName.size() - 1; i++){
      F f = (F) simbolName.get(i).X[0];
        if(f.name == F.LN) {
          
          if(i == 0) {
              continue;
          }
          
          // Пусть мы рассматриваем ln(f(x)).
          //
          // Согласно структурной теореме, если ln(f(x)) --- не регулярный моном,
          // то его аргумент равен произведению рациональных степеней:
          // f(x) = c*g_0(x)^(m_0)*...*g_s(x)^(m_s) * h_0(x)^(n_0)*...*h_t(x)^(n_t)   (1)
          // где s + t < i, c --- произвольное число;
          // m_0, ..., m_s, n_0, ..., n_t \in Q.
          // g_0(x), ..., g_s(x) --- аргументы логарифмов из списка simbolName, имеющих номер < i.
          // h_0(x), ..., h_t(x) --- экспоненты из списка simbolName, имеющие номер < i.
          //
          // Чтоюы понять, представляется ли f(x) в виде произведения (1),
          // делим f(x) с остатком на каждый из g_0(x), ..., g_s(x), h_0(x), ..., h_t(x).
          for (int j = 0; j < i; j++) {
              if(divideArgsOfLogs(i, j, simbolName, argInt, ring)) {
                  i--;
                  break;
              }
          }
      }
      if(f.name == F.EXP){
        switch(f.name){
          case F.LN:
            if(i != 0){
              // ifLNRightF и ifLnVerify добавляют в transcLn по одной функции.
              // Вид этих функций описан в комментарии в начале метода.
              ifLNRightF(simbolName, transcLn, i);
              ifLnVerify(simbolName, transcLn, i);
            }
            boolean a = false, b = false, c = false;
            a = f.X[0] instanceof F;
            if(a) b = ((F)f.X[0]).name == F.ABS;
            if(a && b) c = ((F)f.X[0]).X[0] instanceof F;
            
            if(a && (!b || (b && c)) ){
              F arg = (b)? (F)((F)f.X[0]).X[0] : (F) f.X[0];
              St = theLogFunction(arg, simbolName, i);
//              Element rigPart = transcLn.get(transcLn.size() - 2).simplify(ring);
              //if(rigPart instanceof F && ((F)rigPart).name==F.DIVIDE) St =
              
              // Записываем в функцию St линейную комбинацию, коэффициенты которой
              // нужно найти.
              St = new F(F.SUBTRACT,
                      new Element[]{transcLn.get(transcLn.size() - 2), St});
            }else{
              Element pol = (b)? (Polynom)((F)f.X[0]).X[0] :(Polynom) f.X[0];
              if(((Polynom) pol).coeffs.length > 1){
                pol = simbolName.get(i);
              }
              St = new F(F.SUBTRACT,
                      new Element[]{transcLn.get(transcLn.size() - 2), pol});
            }
            flagLn = true;
            break; 
          case F.EXP:
            if(!flagExp){
            	transcExp.add(new F(F.EXP, new Fname("$c")));
            }
            if(i != 0){
              // ifEXPRightF и ifExpVerify добавляют в transcExp по одной функции.
              // Вид этих функций описан в комментарии в начале метода.
              ifEXPRightF(simbolName, transcExp, i);
              ifExpVerify(simbolName, transcExp, i);
            }
            Element arg = f.X[0];
            Element el = transcExp.get(transcExp.size() - 2);
            if(arg instanceof F && ((F) arg).name == F.DIVIDE){
              el = (new F(F.MULTIPLY, new Element[]{((F) arg).X[1], el})).
                      expand(ring);
              arg = ((F) arg).X[0];
            }
            // Записываем в функцию St линейную комбинацию, коэффициенты которой
            // нужно найти.
            St = new F(F.SUBTRACT, new Element[]{el, arg});
            flagExp = true;
            break;
        }

        St = St.expand(ring);
        if(St.name == F.DIVIDE){
          St = St.X[0] instanceof F ? (F) St.X[0] : new F(St.X[0]);
        }
        Element pol = ring.CForm.ElementConvertToPolynom(St);
        pol = (pol instanceof Fraction)? ((Fraction)pol).num : pol;
        // PolynomNormalVar меняет порядок переменных полинома pol
        // и кольца, относительно которого создавался полином.
        // Порядок меняется так: все переменные pol, которые являются
        // неизвестными коэффициентами из линейной комбинации St,
        // располагаются в конце списка переменных. Полученный полином
        // записывается в нулевой элемент массива newpol, а полученное
        // кольцо записывается в первый элемент newpol.
        // Во второй элемент массива newpol записывается количество
        // неизвестных коэффициентов из линейной комбинации.   
        Object[] newpol = PolynomNormalVar((Polynom) pol, ring.CForm);// polynom, ring, int        
        Vector<Element> X = new Vector<Element>();
        int kolYang =  (Integer)newpol[2] ;  //Integer.parseInt(newpol[2].toString());
        // getValueOfVeriabl находит неизвестные коэффициенты линейной
        // комбинации (которая содержится в переменной St).
        // Если решение существует, то getValueOfVeriabl возвращает
        // true, в противном случае возвращает false.
        // Если решение существует, то оно записывается в массив X.
        if(getValueOfVeriabl((Polynom) newpol[0], X, kolYang, ((Polynom)argInt).powers.length - 1, ring)){
          Element[] el = new Element[ring.CForm.newRing.varPolynom.length];
          int len = ((Ring) newpol[1]).varPolynom.length - kolYang;
          System.arraycopy(((Ring) newpol[1]).varPolynom, 0, el, 0, len);
          for(int j = 0; j < X.size(); j++){ el[len + j] = X.elementAt(j); }
          Element valEl = null;
          if(f.name == F.LN){
//            valEl = ((Polynom) cf.ElementToPolynom(transcLn.get(transcLn.size() - 1), true)).value(el, ring);
            valEl = transcLn.get(transcLn.size() - 1);
            for(int j = len; j < el.length; j++){
              valEl = containsAndReplace(valEl, el[j],
                      new Fname(((Ring) newpol[1]).varNames[j]));
            }
            transcLn.remove(transcLn.size() - 1);
            transcLn.remove(transcLn.size() - 1);
//            valEl = cf.Convert_Polynom_to_F(valEl);
          }
          if(f.name == F.EXP){
            valEl = transcExp.get(transcExp.size() - 1);
            for(int j = len; j < el.length; j++){
              valEl = containsAndReplace(valEl, el[j],
                      new Fname(((Ring) newpol[1]).varNames[j]));
            }
            transcExp.remove(transcExp.size() - 1);
            transcExp.remove(transcExp.size() - 1);
          }
          Fname fn = new Fname(simbolName.get(i).name, valEl.expand(ring));
          simbolName.remove(i);
          simbolName.add(i, fn);
          // Заменяем текущий элемент simbolName на его выражение
          // через предыдущие элементы. Такая замена производится
          // во всех элементах simbolName с индексом большим i.
          poiskZamena(simbolName, simbolName.get(i), i);
//          simbolName.remove(i);
          i--;
          //n--;
        }
      }
    }
  }
  


        /**
     * Деление аргумента логарифма (i-й элемент simbolName)
     * на j-й элемент simbolName.
     * Если j-й элемент simbolName --- экспонента, то делим на экспоненту.
     * Если j-й элемент simbolName --- логарифм, то делим на аргумент j-го логарифма.
     * 
     * Пусть ln(f(x)) ---  рассматриваемый логарифм.
     * Пусть exp(g(x)) --- j-й элемент simbolName.
     * Если f(x) без остатка поделился на exp(g(x))
     * [т. е. если  f(x) = h(x)*exp(g(x))^a ], то заменяем в подынтегральном
     * выражении и следующих элементах списка ln(f(x)) на ln(h(x)) + a*g(x).
     * 
     * Пусть ln(g(x)) --- j-й элемент simbolName.
     * Если f(x) без остатка поделился на g(x)
     * [т. е. если  f(x) = h(x)*g(x)^a ], то заменяем в подынтегральном
     * выражении и следующих элементах списка ln(f(x)) на ln(h(x)) + a*ln(g(x)).
     * 
     * 
     * @param i --- идекс рассматриваемого логарифма.
     * @param j --- индекс элемента simbolName, на который делим.
     * @param simbolName --- список логарифмов и экспонент.
     * @param argInt --- переменная интегрирования.
     * @param ring
     * @return true, если рассматриваемый логарифм (т. е. simbolName[i] )
     *               был удален из списка.
     *         false, если ничего из списка не удаляли.
     */
    public boolean divideArgsOfLogs(int i, int j, ArrayList<Fname> simbolName, Element argInt, Ring ring) {
        Element func1;
        if (((F) simbolName.get(j).X[0]).name == F.LN) {
            func1 = ((F) simbolName.get(j).X[0]).X[0];
            if(func1 instanceof F) {
                func1 = LN_EXP_POW_LOG_LG.undressAbs((F) func1, ring);
                if(func1 == null) {
                    func1 = ((F) simbolName.get(j).X[0]).X[0];
                }
            }
        } else {
            func1 = simbolName.get(j);
        }
        
        boolean flagAbs = false;
        Element func2 = ((F)simbolName.get(i).X[0]).X[0];
        if(func2 instanceof F) {
            func2 = LN_EXP_POW_LOG_LG.undressAbs((F) func2, ring);
            if(func2 != null) {
                flagAbs = true;
            } else {
                func2 = ((F)simbolName.get(i).X[0]).X[0];
            }
        }
        
        
        Element pol1 = ring.CForm.ElementConvertToPolynom(func1);
        Element pol2 = ring.CForm.ElementConvertToPolynom(func2);

        Element quotient = pol2;
        Element pol3 = pol2;
        int deg = 0;
        do {
            pol3 = pol3.divide(pol1, ring);
            if( (pol3 instanceof Fraction) == false || pol3.isItNumber()) {
                deg++;
                quotient = pol3;
            }
        } while ( (pol3 instanceof Fraction) == false || pol3.isItNumber());

        if (deg > 0) {
            Element newFunc = null;
            if (((F) simbolName.get(j).X[0]).name == F.LN) {
                newFunc = simbolName.get(j).X[0];
            } else {
                newFunc = ((F) simbolName.get(j).X[0]).X[0];
            }
            if(deg > 1) {
                newFunc = new F(F.MULTIPLY, new Element[] {newFunc, ring.posConst[deg]});
            }
            String name = simbolName.get(i).name;
            Element newLN = null;
            if(quotient.isOne(ring) == false) {
                Element argOfLN = ring.CForm.ElementToF(quotient);
                if(flagAbs) {
                    argOfLN = new F(F.ABS, argOfLN);
                }
                newLN = new F(F.LN, argOfLN);
                newLN = new Fname(name, newLN);
                newFunc = new F(F.ADD, new Element[] {newFunc, newLN});
            }
            poiskZamena(simbolName, new Fname(name, newFunc), i);
            if(quotient.isItNumber()) {
                return true;
            } else {
                simbolName.add(i, (Fname) newLN);
                return false;
            }
        }

        return false;
    }

  public static int numberYangVar(CanonicForms cf) {
    ArrayList<Element> list1 = cf.List_of_Change;
    int n = 0;
    for(int i = 0; i < list1.size(); i++){
      if(list1.get(i) instanceof Fname){
        n++;
      }
    }
    return n;
  }
/**
 * Меняем порядок переменных в полиноме, и создаем
 * кольцо с соответствующим порядком переменных.
 * 
 * @param pol polynom?
 * @param cf  - current CanonicForms
 * @return  OBJECT with 3 fields: [Polynom, NEW RING, (number of new variables??) ]
 */
  public static Object[] PolynomNormalVar(Polynom pol, CanonicForms cf) {
    ArrayList<Element> list1 = cf.List_of_Change;
//    ArrayList<Element> list2 = cf.List_of_Polynom;

    int kolVar = cf.newRing.varPolynom.length, k = 0;
    int[] newpower = new int[kolVar*pol.coeffs.length];

    int[][] massPow = new int[pol.coeffs.length][kolVar];
    for(int i = 0; i < pol.coeffs.length; i++){
      for(int j = 0; j < kolVar; j++){
    	if(j < (pol.powers.length/pol.coeffs.length)) {
    		massPow[i][j] = pol.powers[k];
    		k++;
    	} else {
    		massPow[i][j] = 0;
    	}
      }
    }
    int[][] massPow2 = new int[pol.coeffs.length][kolVar];
    for(int i = 0; i < massPow.length; i++){
      System.arraycopy(massPow[i], 0, massPow2[i], 0, cf.RING.varPolynom.length);
    }

    k = 0;
    int k2 = list1.size() - 1;
    Fname[] ring = new Fname[list1.size()];
    for(int i = 0; i < list1.size(); i++){
      if(! (list1.get(i) instanceof Fname)) {
        ring[k] = new Fname("a"+i, list1.get(i));
        for(int ii = 0; ii < massPow.length; ii++){
          massPow2[ii][k + cf.RING.varPolynom.length] = massPow[ii][i + cf.RING.varPolynom.length];
        }
        k++;
      } else if( ((Fname) list1.get(i)).X == null  
              && ((Fname)list1.get(i)).name.charAt(0) == '$' ){
        ring[k2] = (Fname) list1.get(i);
        for(int ii = 0; ii < massPow.length; ii++){
          massPow2[ii][k2 + cf.RING.varPolynom.length] = massPow[ii][i + cf.RING.varPolynom.length];
        }
        k2--;
      } else {
        ring[k] = (Fname) list1.get(i);
        for(int ii = 0; ii < massPow.length; ii++){
          massPow2[ii][k + cf.RING.varPolynom.length] = massPow[ii][i + cf.RING.varPolynom.length];
        }
        k++;
      }
    }
    k2 = 0;
    for(int i = 0; i < massPow2.length; i++){
      for(int j = 0; j < massPow2[i].length; j++){
        newpower[k2] = massPow2[i][j];
        k2++;
      }
    }
    //   ???????????????????????
    String sring = "";
    String[] s = new String[ring.length];
    for(int i = 0; i < ring.length; i++){
      if(ring[i].name.charAt(0) == 'a') {
        sring += "," + ring[i].name;
        s[i] = ring[i].name;
      }else {
        sring += "," + ring[i].toString();
        s[i] = ring[i].toString();
      }
    }
    sring += "]";
    Ring ringPol = new Ring(cf.RING.toString().replace("]", sring));
    Polynom newPol = new Polynom(newpower, pol.coeffs, ringPol);
    Object[] obj = new Object[]{newPol, ringPol, new Integer(list1.size() - k)};
    return obj;
  }

  /**
   * Подставлят выражение или значение переменной name в исходное выражение
   * @param el2 - выражение или значение кот надо подставить вместо переменной name
   * @param el1 - выражение в кот содержится переменная name
   * @param name - переменная, вместо кот надо подставить выражение или значение
   * @return
   */
  public Element containsAndReplace(Element el1, Element el2, Fname name) {
    //el1 = el1.ExpandFnameOrId();
    if(el1 instanceof F){
      switch(((F) el1).name){
        case F.POW: {
          Element elPow = containsAndReplace(((F) el1).X[0], el2, name);
          Element degPow = containsAndReplace(((F) el1).X[1], el2, name);
          if(elPow instanceof Fname && ((Fname) elPow).name.equals(name.name)){
            elPow = el2;
          }
          if(degPow instanceof Fname && ((Fname) degPow).name.equals(name.name)){
            degPow = el2;
          }
          return new F(F.POW, new Element[]{elPow, degPow});
        }
        case F.intPOW: {
          Element elPow = containsAndReplace(((F) el1).X[0], el2, name);
          if(elPow instanceof Fname && ((Fname) elPow).name.equals(name.name)){
            elPow = el2;
          }
          return new F(F.intPOW, new Element[]{elPow, ((F) el1).X[1]});
        }
        default: {
          Element[] elDefault = new Element[((F) el1).X.length];
          for(int i = 0; i < ((F) el1).X.length; i++){
            elDefault[i] = containsAndReplace(((F) el1).X[i], el2, name);
            if(((F) el1).X[i] instanceof Fname) {
              Fname fn = (Fname) ((F) el1).X[i];
              if (fn.name.equals(name.name)){
                elDefault[i] = el2;
              }
            }
          }
          return new F(((F) el1).name, elDefault);
        }
      }
    }
    if( (el1 instanceof Fname)&&( ((Fname)el1).X != null) ) {
        Element f = containsAndReplace(((Fname) el1).X[0], el2, name);
        return new Fname(((Fname) el1).name, f);
    }
    return el1;
  }

  public void poiskZamena(ArrayList<Fname> simbolName, Fname name, int i) {
    for(int k = i + 1; k < simbolName.size(); k++){
      Element elk = containsAndReplace(simbolName.get(k).X[0],
              name.X[0], simbolName.get(i));
      Fname fn = new Fname(simbolName.get(k).name, elk);
      simbolName.remove(k);
      simbolName.add(k, fn);
    }
    simbolName.remove(i);
  }

  public static int[][] removeCols(int[][] array, int firstCol, int twoCol) {
    for(int i = 0; i < array.length; i++){
      int k = array[i][firstCol];
      array[i][firstCol] = array[i][twoCol];
      array[i][twoCol] = k;
    }
    return array;
  }

  public static String[] removeCols(String[] array, int firstCol, int twoCol) {
    String k = array[firstCol];
    array[firstCol] = array[twoCol];
    array[twoCol] = k;
    return array;
  }

  public void ifLNRightF(ArrayList<Fname> simbolName, ArrayList<Element> transcLn, int end) {
    Element sln = transcLn.get(transcLn.size() - 2);
    for(int i = (transcLn.size()) / 2 - 1; i < end; i++){
      Fname name = simbolName.get(i);
      F f = (F) simbolName.get(i).X[0];
      Fname var = new Fname("$n" + i);
      if(f.name == F.EXP){
        sln = new F(F.ADD, new Element[]{sln, new F(F.MULTIPLY, new Element[]{
                    var, name})});
      }
      if(f.name == F.LN){
        if(f.X[0] instanceof F){
          F arg = (F) f.X[0];
          if(arg.name == F.MULTIPLY || arg.name == F.DIVIDE){
            if(arg.name == F.MULTIPLY){
              for(int j = 0; j < arg.X.length; j++){
                sln = new F(F.ADD, new Element[]{sln, new F(F.MULTIPLY,
                          new Element[]{var, arg.X[j]})});
              }
            }else{
              F arg0 = new F(F.MULTIPLY, new Element[]{var, arg.X[0]});
              for(int j = 1; j < arg.X.length; j++){
                arg0 = new F(F.SUBTRACT, new Element[]{arg0, new F(F.MULTIPLY,
                          new Element[]{var, arg.X[j]})});
              }
              sln = new F(F.ADD, new Element[]{sln, arg0});
            }

          }else{
            sln = new F(F.ADD, new Element[]{sln, new F(F.MULTIPLY,
                      new Element[]{var, name})});
          }
        }else{
          Element elPol = f.X[0];
          if(((Polynom) f.X[0]).coeffs.length > 1){
            elPol = simbolName.get(i);
          }
          sln = new F(F.ADD, new Element[]{sln, new F(F.MULTIPLY, new Element[]{
                      var, elPol})});
        }
      }
    }
    transcLn.add(sln);
  }

  public void ifLnVerify(ArrayList<Fname> simbolName, ArrayList<Element> transcLn, int end) {
    Element el = transcLn.get(transcLn.size() - 2);
    for(int i = (transcLn.size()) / 2 - 1; i < end; i++){
      F f = (F) simbolName.get(i).X[0];
      Fname var = new Fname("$n" + i);
      if(f.name == F.LN){
        el = new F(F.ADD, new Element[]{el, new F(F.MULTIPLY, new Element[]{var,
                    simbolName.get(i)})});
      }
      if(f.name == F.EXP){
        el = new F(F.ADD, new Element[]{el, new F(F.MULTIPLY, new Element[]{var,
                    f.X[0]})});
      }
    }
    transcLn.add(el);
  }

  public void ifEXPRightF(ArrayList<Fname> simbolName, ArrayList<Element> transcExp, int end) {
    Element el = transcExp.get(transcExp.size() - 2);
    for(int i = (transcExp.size() / 2) - 1; i < end; i++){
      F f = (F) simbolName.get(i).X[0];
      Fname var = new Fname("$n" + i);
      if(f.name == F.LN){
        el = new F(F.ADD, new Element[]{el, new F(F.MULTIPLY, new Element[]{var,
                    simbolName.get(i)})});
      }
      if(f.name == F.EXP){
        el = new F(F.ADD, new Element[]{el, new F(F.MULTIPLY, new Element[]{var,
                    f.X[0]})});
      }
    }
    transcExp.add(el);
  }

  public void ifExpVerify(ArrayList<Fname> simbolName, ArrayList<Element> transcExp, int end) {
    Element el = transcExp.get(transcExp.size() - 2);
    for(int i = (transcExp.size() / 2) - 1; i < end; i++){
      F f = (F) simbolName.get(i).X[0];
      Fname var = new Fname("$n" + i);
      if(f.name == F.LN){
        el = new F(F.MULTIPLY, new Element[]{el, new F(F.POW,
                  new Element[]{f.X[0], var})});
      }
      if(f.name == F.EXP){
        el = new F(F.MULTIPLY, new Element[]{el, new F(F.POW,
                  new Element[]{simbolName.get(i), var})});
      }
    }
    transcExp.add(el);
  }

  /*
   * логарифмируем функцию
   */
  public F theLogFunction(F arg, ArrayList<Fname> simbolName, int i) {
    F f = null;
    if(arg.name == F.MULTIPLY || arg.name == F.DIVIDE){
      f = arg.X[0] instanceof F ? (F) arg.X[0] : new F(arg.X[0]);
      if(arg.name == F.MULTIPLY){
        for(int j = 1; j < arg.X.length; j++){
          f = new F(F.ADD, new Element[]{f, arg.X[j]});
        }
      }else{
        for(int j = 1; j < arg.X.length; j++){
          f = new F(F.SUBTRACT, new Element[]{f, arg.X[j]});
        }
      }
    }else{//в противном случаии берем имя этого аргумента из контекста
      f = new F(simbolName.get(i));
    }
    return f;
  }

  /*
   * Подставляем значения некоторых переменных в полиноме
   * @param p -- входной полином
   * @param x -- массив значений переменных
   * @return измененный полином
   */
  public static Polynom setvalueOfPolynomial(Polynom p, Vector<Integer> X) {
    int k = 0, oldMon = X.size(), lenJ = p.powers.length / p.coeffs.length;
    int[][] massPow = new int[p.coeffs.length][lenJ];
    //определяем длинну массива степеней для будующего полинома
    //в этом массиве не будут учитываться степени переменных,
    //вместо которых подставляются значения
    int[] pPow = new int[p.powers.length - p.coeffs.length * oldMon];
    //длинна массива коефф. для будующего полинома останется такойже как и у
    //входного полинома
    Element[] pCoef = new Element[p.coeffs.length];
    System.arraycopy(p.coeffs, 0, pCoef, 0, p.coeffs.length);
    //представляем массив степеней входного полинома ввиде двумерного массива
    for(int i = 0; i < p.coeffs.length; i++){
      for(int j = 0; j < lenJ; j++){
        massPow[i][j] = p.powers[k];
        k++;
      }
    }
    k = 0;
    //заполняем массив коэфф будующего полинома, коэфф перед переменной
    //умножается на значение этой переменной
    for(int i = 0; i < p.coeffs.length; i++){
      for(int j = 0; j < oldMon; j++){
        if(massPow[i][j] != 0){
          if(X.elementAt(j) == 0){
            pCoef[k] = NumberZ.ZERO;
          }else{
            pCoef[k] = (new NumberZ(X.elementAt(j))).multiply(new NumberZ(p.coeffs[i].
                    intValue()));
          }
          k++;
        }
      }
    }
    k = 0;
    //заполняется массив степеней будующего полинома, массив будет равен
    //массиву степеней входного полинома без степеней тех переменных,
    //вместо которых подставили значения
    for(int i = 0; i < p.coeffs.length; i++){
      for(int j = oldMon; j < lenJ; j++){
        pPow[k] = massPow[i][j];
        k++;
      }
    }
    return new Polynom(pPow, pCoef);
  }

  public static boolean isZeroArray(Element[] el, Ring ring) {
    boolean bool = true;
    for(int i = 0; i < el.length; i++){
      if(!el[i].isZero(ring)){
        bool = false;
        break;
      }
    }
    return bool;
  }
  /* Составление матрицы коэффициентов для системы линейных уравнений, получаемой из
     равенства pol=0.
     Пример:
     Пусть a + b*x + c*exp(x) = 5*x*y*z - исходное уравнение, где
     a, b и c - неизвестные коэффициенты, которые требуется найти;
     x, y, z - переменные из кольца ring.
     a, b, c могут быть числами, либо полиномами от y и z.
     
     Данное уравнение равносильно системе уравнений:
    
     c*exp(x) = 0;
     b*x = 5*x*y*z;
     a = 0;
    
     Перенесем все слагаемые исходного уравнения в левую часть, получим уравнение:
     a + b*x + c*exp(x) - 5*x*y*z = 0
     
     Если функцию, стоящую в левой части полученного уравнения конвертировать
     в полином, то получим полином вида:
     a + b*x + c*t - 5*x*y*z
     Этот поинном и будет первым аргументом pol этой процедуры.
     Процедура в качестве ответа вернет массив:
     [[0, 0, 1, 0],             Эта запись соответствует     0*a*exp(x) + 0*b*exp(x) + 1*c*exp(x) = 0*exp(x)
      [0, 1, 0, 5*y*z],         системе уравнений:           0*a*x + 1*b*x + 0*c*x = 5*x*y*z
      [1, 0, 0, 0]]                                          1*a + 0*b + 0*c = 0
    
     В зависимости от входных параметров система уравнений может не иметь решений.
     В данном случае система имеет решение a=0, b=5*y*z, c=0.
  
     yangVar - количество неизвестных коэффициентов полинома pol
     arg - переменная, по которой считаем интеграл
  */
  private static Element[][] ArrayCoeffEquallyMonomNew(Polynom pol, int youngVar, int n_var, Ring ring) {
      // Для начала преобразуем полином pol следующим образом:
      // Выносим все неизвестные коэффициенты и переменные кольца ring кроме arg в коэффициенты полинома pol.
      // т.е. pol = t*c + x*(5*y*z + b) + a, где
      // c, 5*y*z + b, a --- коэффициенты полинома pol. Они сами являются полиномами.
      // Этот преобразованный полином обозначим p3.
      Polynom[] monoms = ring.CForm.dividePolynomToMonoms(pol);
      // deg = количество переменных полинома pol
      int deg = pol.powers.length/pol.coeffs.length;
      // Преобразование полинома pol будем выполнять над каждым мономом отдельно.
      // p1 - переменные преобразованного полинома
      // p2 - коэффициенты преобразованного полинома
      Polynom p1 = null;
      Polynom p2 = null;
      // В переменную p3 будем собирать преобразованный полином pol.
      Polynom p3 = new Polynom(new int[]{}, new Element[]{});
      for(int i=0; i<monoms.length; i++) {
          p1 = new Polynom(new int[deg], new Element[]{new NumberZ("1")});
          p2 = new Polynom(new int[deg], new Element[]{new NumberZ("1")});
          for(int j=0; j<monoms[i].powers.length; j++) {
              if( ( j == n_var )||(( j>=ring.varPolynom.length )&&(j<deg-youngVar)) ) {
                  p1.powers[j] = monoms[i].powers[j];
              } else {
                  p2.powers[j] = monoms[i].powers[j];
              }
          }
          p2.coeffs[0] = monoms[i].coeffs[0];
          p1.coeffs[0] = p2.normalNumbVar(ring);
          p3 = p3.add(p1.normalNumbVar(ring), ring);
      }
      // Каждый моном полинома p3 соостветствует строке выходной матрицы.
      // Поэтому из каждого коэффициента p3 составляем строку выходной матрицы.
      // Сначала записываем все ненулевые значения (все значения, содержащиеся в полиноме)
      // а затем записываем нули во все незаполненные элементы. 
      Element[][] el = new Element[p3.coeffs.length][youngVar + 1];
      for(int i=0; i<p3.coeffs.length; i++) {
          // Берем коэффициент полинома p3 и разбиваем его на мономы.
          Polynom[] p4 = ring.CForm.dividePolynomToMonoms((Polynom)p3.coeffs[i]);
          // Пробегаем по всем полученным мономам.
          for (int j = 0; j < p4.length; j++) {
              // Если в очередном мономе нет неизвестных коэффициентов a, b или c
              // то этот моном соответствует правой части системы уравнений.
              // Поэтому записываем его в последний столбец текущей строки.
              if(p4[j].powers.length < (deg - youngVar) ) {
                  el[i][youngVar] = p4[j].multiply(NumberZ.MINUS_ONE, ring);
              } else {
                  // В противном случае убираем из монома неизвестный
                  // коэффициент (a, b или c, причем в мономе может содержаться только
                  // один из них, и только в первой степени)
                  // Оставшуюся часть монома записываем в соответствующий столбец матрицы.
                  int temp = p4[j].powers.length;
                  p4[j].powers[p4[j].powers.length - 1] = 0;
                  el[i][temp - deg + youngVar - 1] = p4[j].normalNumbVar(ring);
              }
          }
      }
      
      // Записываем нули во все незаполненные элементы.
      for(int i = 0; i < el.length; i++) {
          for(int j = 0; j < el[i].length; j++) {
              if(el[i][j] == null) el[i][j] = NumberZ.ZERO;
          }
      }
      return el;
  }
  
  public static Element[][] ArrayCoeffEquallyMonom(Polynom pol, int yangVar, Element arg, Ring ring) {
    //определяем количество переменных
    int jj = pol.powers.length / pol.coeffs.length;
    int jjj = jj - yangVar - ring.varPolynom.length;
    int oldVar = jj - yangVar;
    Element[][] el = new Element[pol.coeffs.length][jj];
    int k = 0;
    //представляем массив степеней входного полинома в виде массива(число
    //столбцов соответствует количеству переменных, а число строк - количеству
    //мономов), причем если степнь младших мономов > 0, то записываем вместо
    //степени значение коэф. перед мономом
    for(int ii = 0; ii < pol.coeffs.length; ii++){
      for(int j = 0; j < jj; j++){
        if(pol.powers[k] != 0 && j >= oldVar){
          el[ii][j] = pol.coeffs[ii];
        }
        if(pol.powers[k] == 0 && j >= oldVar){
          el[ii][j] = NumberZ.ZERO;
        }
        if(j < oldVar){
          el[ii][j] = new NumberZ(pol.powers[k]);
        }
        k++;
      }
    }
    //создаем массив для дальнейшего вычисления значений млад мономов
    //количество строк соот. кол. старших мономов+1, а кол. столбцов -
    //кол. младших мономов.
    //если если млад. мономы соот. одному и тому же старш. моному, то складываем
    //строчки соот. млад. мон.
    Element[][] a = new Element[pol.coeffs.length][yangVar + 1];//max+1
    int row2 = 0;
    Vector<Integer> ind = new Vector<Integer>();
    Element[] m1 = new Element[yangVar];
    Element[] m2 = new Element[jjj+1];
    Element[] m3 = new Element[jjj+1];
    for(int i = 0; i < pol.coeffs.length; i++){
      if(!ind.contains(i)){
        System.arraycopy(el[i], oldVar, m1, 0, yangVar);
        if(isZeroArray(m1, ring)){
          Polynom p = new Polynom(new int[ring.varPolynom.length], new Element[]{new NumberZ("1")});
          System.arraycopy(pol.powers, jj*i, p.powers, 0, p.powers.length);
          p.powers[((Polynom)arg).powers.length-1] = 0;
          p = p.normalNumbVar(ring);
          a[row2][yangVar] = pol.coeffs[i].multiply(NumberZ.MINUS_ONE, ring).multiply(p, ring);
        }else{
          a[row2][yangVar] = NumberZ.ZERO;
        }
        m2[0] = el[i][((Polynom)arg).powers.length-1];
        System.arraycopy(el[i], ring.varPolynom.length, m2, 1, jjj);
        System.arraycopy(el[i], oldVar, a[row2], 0, yangVar);
        for(int j = i + 1; j < pol.coeffs.length; j++){
          m3[0] = el[j][((Polynom)arg).powers.length-1];
          System.arraycopy(el[j], ring.varPolynom.length, m3, 1, jjj);
          if(Array.isEqual(m2, m3, ring)){
            for(int col = 0; col < yangVar; col++){
              a[row2][col] = a[row2][col].add(el[j][col + oldVar], ring);
            }
            System.arraycopy(el[j], oldVar, m1, 0, yangVar);
            if(isZeroArray(m1, ring)){
              Polynom p = new Polynom(new int[ring.varPolynom.length], new Element[]{new NumberZ("1")});
              System.arraycopy(pol.powers, jj*j, p.powers, 0, p.powers.length);
              p.powers[((Polynom)arg).powers.length-1] = 0;
              p = p.normalNumbVar(ring);
              a[row2][yangVar] = a[row2][yangVar].add(pol.coeffs[j].multiply(
                      NumberZ.MINUS_ONE, ring).multiply(p, ring), ring);
            }
            ind.add(j);
          }
        }
        row2++;
      }
    }
    Element[][] aa = new Element[pol.coeffs.length - ind.size()][yangVar + 1];
    for(int i = 0; i < pol.coeffs.length - ind.size(); i++){
      System.arraycopy(a[i], 0, aa[i], 0, a[i].length);
    }
    return aa;
  }

  public boolean getValueOfVeriabl(Polynom pol, Vector<Element> X, int yangVar, int n_var, Ring ring) throws Exception {
    Element[][] a = ArrayCoeffEquallyMonomNew(pol, yangVar, n_var, ring);
    MatrixS ms = new MatrixS(a, ring);
    MatrixS ee = ms.toEchelonForm(ring);
    boolean bb = ee.isSysSolvable();
    Element[] els = ee.oneSysSolv_and_Det(ring);
    VectorS vs = ee.oneSysSolvForFraction(ring);
    for(int i = 0; i < vs.V.length; i++){
        X.add(vs.V[i]);
    }
    return bb;
  }

  public boolean isSolvSysOneOrManyVar(Polynom pol, int yangVar, Vector<Element> X, int nVarInR, Ring ring, ArrayList<Fname> name) throws Exception {
    //определяем количество переменных
    int jj = pol.powers.length / pol.coeffs.length;
    int oldVar = jj - yangVar;
    Element[][] el = new Element[pol.coeffs.length][yangVar + 1];
    int k = 0;
    //представляем массив степеней входного полинома в виде массива(число
    //столбцов соответствует количеству переменных, а число строк - количеству
    //мономов), причем если степнь младших мономов > 0, то записываем вместо
    //степени значение коэф. перед мономом
    for(int ii = 0; ii < pol.coeffs.length; ii++){
      Element coeffVar = NumberZ.ONE;
      for(int col = 0; col < oldVar; col++){
        if(col != nVarInR){
          Element varPow = null;
          if(name.indexOf(new Fname(ring.varNames[col])) > 0){
            varPow = new F(F.intPOW, new Element[]{new Fname(ring.varNames[col]),
                      new NumberZ(pol.powers[k + col])});
          }else{
            if(pol.powers[k + col] != 0){
              varPow = new Polynom(
                      ring.varNames[col] + "^" + pol.powers[k + col], ring);
            }
          }
          if(varPow != null){
            coeffVar = new F(F.MULTIPLY, new Element[]{coeffVar, varPow});
          }
        }
      }
      int col = 0;
      boolean bool = false;
      for(int j = 0; j < jj; j++){
        if(j >= oldVar){
          if(pol.powers[k] != 0){
            bool = true;
          }
          el[ii][col] = pol.powers[k] != 0 ? pol.coeffs[ii].multiply(coeffVar,
                  ring) : NumberZ.ZERO;
          col++;
        }
        if(j < oldVar && j == nVarInR){
          el[ii][col] = new NumberZ(pol.powers[k]);
          col++;
        }
        k++;
      }
      if(!bool){
        pol.coeffs[ii] = coeffVar;
      }
    }
    //создаем массив для дальнейшего вычисления значений млад мономов
    //количество строк соот. кол. старших мономов+1, а кол. столбцов -
    //кол. младших мономов.
    //если если млад. мономы соот. одному и тому же старш. моному, то складываем
    //строчки соот. млад. мон.
    Element[][] a = new Element[pol.coeffs.length][yangVar + 1];//max+1
    int row2 = 0;
    Vector<Integer> ind = new Vector<Integer>();
    Element[] m1 = new Element[yangVar];
    Element m2 = null;
    Element m3 = null;
    for(int i = 0; i < pol.coeffs.length; i++){
      if(!ind.contains(i)){
        System.arraycopy(el[i], 1, m1, 0, yangVar);
        if(isZeroArray(m1, ring)){
          a[row2][yangVar] = pol.coeffs[i].multiply(NumberZ.MINUS_ONE, ring).
                  expand(ring);
        }else{
          a[row2][yangVar] = NumberZ.ZERO;
        }
        m2 = el[i][0];
        System.arraycopy(el[i], 1, a[row2], 0, yangVar);
        for(int j = i + 1; j < pol.coeffs.length; j++){
          m3 = el[j][0];
          if(m2.compareTo(m3) == 0){
            for(int col = 0; col < yangVar; col++){
              a[row2][col] = a[row2][col].add(el[j][col + 1], ring).expand(
                      ring);
            }
            System.arraycopy(el[j], 1, m1, 0, yangVar);
            if(isZeroArray(m1, ring)){
              a[row2][yangVar] = a[row2][yangVar].add(pol.coeffs[j].multiply(
                      NumberZ.MINUS_ONE,
                      ring), ring).expand(ring);
            }
            ind.add(j);
          }
        }
        row2++;
      }
    }
    Element[][] aa = new Element[pol.coeffs.length - ind.size()][yangVar + 1];
    for(int i = 0; i < pol.coeffs.length - ind.size(); i++){
      System.arraycopy(a[i], 0, aa[i], 0, a[i].length);
    }
    return getValueOfVariabl(aa, X, ring);
  }

  public boolean getValueOfVariabl(Element[][] a, Vector<Element> X, Ring ring) throws Exception {
    MatrixS ms = new MatrixS(a, ring);
    MatrixS ee = ms.toEchelonForm(ring);
    boolean bb = ee.isSysSolvable();
    Element[] vsr = ee.oneSysSolv_and_Det(ring);
    for(int i = 0; i < vsr.length - 1; i++){
      X.add((vsr[i].expand(ring)).divide(vsr[vsr.length - 1].expand(ring),
              ring));
    }
    return bb;
  }
}
