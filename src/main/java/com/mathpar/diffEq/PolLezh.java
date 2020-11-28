/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.diffEq;

import com.mathpar.func.F;
import com.mathpar.func.Fname;
import com.mathpar.number.*;
import com.mathpar.polynom.FactorPol;
import com.mathpar.polynom.Polynom;

/**
 *
 * @author katya
 */
public class PolLezh {
    
    public static void main(String[] args) {
        PolLezh poll=new PolLezh();
        int n = 5;
        Element pol;
        Element pol2;
        Element Q;
        Element Y;
        Element U;
        
        Element x=new NumberR (5);
        Element y=new NumberR (5);
        Element z=new NumberR (5);
        
        Ring ring = new Ring("R[x,y]");
        ring.setMachineEpsilonR(10);//10 знаков после запятой
        ring.FLOATPOS = 10;//сколько на печати знаков после запятой
        ring.setAccuracy(15);//длина числа, которое вычисляю
        
        
        Element phi=y.divide(x, ring).arctn(ring);
        Element r=x.pow(2, ring).multiply(y.pow(2, ring), ring).multiply(z.pow(2, ring), ring).sqrt(ring);
        Element costheta = z.divide(r, ring);
        for(int i=0;i<n;i++){
            pol = poll.nodes2(new NumberZ(""+i), new Polynom("x", ring), ring);
            pol = pol.divide(new NumberZ("2").pow(n, ring), ring);
            System.out.println("p "+i+"   ="+pol.value(new Element[]{x}, ring));
                 for(int j=1;j<=i;j++) {
                     pol2=poll.prisLeg(new NumberZ(""+i), new NumberZ(""+j), new Polynom("x", ring),ring);
  System.out.println("p "+i+" "+j+" ="+pol2.value(new Element[]{costheta}, ring));
  Q=poll.ThetaFunc((Element)pol2.value(new Element[]{costheta}, ring), new NumberZ(""+i),new NumberZ(""+j), ring);
  System.out.println("q "+i+" "+j+" ="+Q.expand(ring));
  Y=poll.YDec( new NumberZ(""+i),new NumberZ(""+j),x, y, z, ring);
                 //    for (int k = 0; k < Y.length; k++) {
                         System.out.println("Y "+i+" "+j+" ="+Y);   
                 //    }
  U = poll.UDec(new NumberZ (""+i),new NumberZ(""+j),x, y, z, ring);
                 //    for (int k = 0; k < U.length; k++) {
                         System.out.println("U"+i+" "+j+" ="+U);
                 //    }
                 }
        }

    }
//     NumberZ factorial(NumberZ n) {
//        NumberZ t = new NumberZ(1);
//        if (n.equals(new NumberZ(0))) {
//            return t;
//        }
//        NumberZ i = new NumberZ(1);
//        while (!i.equals(n.add(new NumberZ(1)))) {
//            t = t.multiply(i);
//            i = i.add(new NumberZ(1));
//        }
//        return t;
//    }
    
     
//     NumberZ soch(NumberZ n, NumberZ k) {
//        return factorial(n).divide(factorial(k).multiply(factorial(n.subtract(k))));
//    } 
   

     
    /*
     Метод нахождения полинома Лежандра
     n - степень полинома, x - переменная 
     */
            
    public  Element nodes2(Element n, Element x, Ring ring) {
       
        // 1/(2^n)sum( ( soch(n, k)^2 )*( (x-1)^(n-k) )*(x+1)^k) , k=0,...,n.
        Polynom legendre=Polynom.polynomFromNot0Number(ring.numberONE); 
        Element oneHalfInN= ring.numberONE.divide(ring.posConst[2].pow(n, ring), ring);
        Polynom pol = new Polynom();
        Polynom[] p = new Polynom[3];
        p[1] = new Polynom(new int[] {1, 0}, new Element[]  {ring.numberONE, ring.numberONE});
        p[2] = new Polynom(new int[] {1, 0}, new Element[] {ring.numberONE, ring.numberMINUS_ONE});
        int[] pow = new int[3];
        if(!n.isItNumber()){Fname k=new Fname("k");
            Element shoose=new F(F.intPOW, new F(F.BINOMIAL,n,k), NumberZ.TWO);
            Element p1=new F(F.POW, new F(F.SUBTRACT,x,NumberZ.ONE), new F(F.SUBTRACT,n,k));
            Element p2=new F(F.POW, new F(F.ADD,x,NumberZ.ONE), k);
            shoose=shoose.multiply(p1, ring).multiply(p2, ring);
            Element sum= new F(F.SUM, new Element[]{shoose,k,NumberZ.ZERO,n});
            return oneHalfInN.multiply(sum, ring);
        }
        int nInt=n.intValue();
        for (int i = 0; i <= nInt; i++) {Element shoose=Element.binomial(ring, nInt,i);
            p[0]=new Polynom(shoose);
            pow  = new int[]{2,i,nInt- i};//i;
            legendre =legendre.multiply((Polynom)(new FactorPol(pow, p).toPolynomOrFraction(ring)),ring);         
        }
        Element res =  legendre.multiply(oneHalfInN, ring);
        return (x.equals(ring.varPolynom[0],ring))? res:
                res.value(new Element[]{x}, ring);         
    }
    /*
    Метод нахождения присоединённого полинома Лежандра
    n - степень полинома, m - верхний индекс(различные полиномы одной и той же степени), x - переменная
    */
    public Element prisLeg(Element n, Element m, Element x, Ring ring){
//       Element P = new Polynom("x^2-1", ring);
//       P = P.pow(n, ring);
//       for(int i=0; i<(m.intValue()+n.intValue()); i++){
//           P = P.D(ring);
//       }
//       P = P.multiply(new Polynom("1-x^2", ring).pow(m.divide(new NumberZ("2"), ring), ring), ring);
//       P = P.divide(new NumberZ("2").pow(n.intValue()), ring);
//       P = P.divide(n.factorial(ring), ring);
        
        
        Element P = nodes2(new NumberZ(n.intValue()), new Polynom("x", ring), ring);
        for(int i=0; i<m.intValue(); i++){
           P = P.D(ring);
        }
        P = P.multiply(new Polynom("1-x^2", ring).pow(m.divide(new NumberZ("2"), ring), ring), ring);
        P = P.multiply(ring.numberMINUS_ONE.multiply(m, ring), ring);
       if (x.isItNumber()){
                return P.value(new Element[]{x}, ring);
        } else {
                return P;
        }
   } 
   
    Element ThetaFunc(Element pol,Element n,Element m, Ring ring){int mInt=m.intValue(); int mMOD2=mInt %2;
       Element Q=(mMOD2==0)?pol:pol.negate(ring); 
       Element num=  NumberZ.TWO.multiply(n, ring).subtract( NumberZ.ONE, ring).
                                 multiply(Element.factorial(ring,((NumberZ)n.subtract(m, ring)).intValue()),ring);
       Element denum=NumberZ.TWO.multiply(Element.factorial(ring,((NumberZ)n.add(m, ring)).intValue()),ring); 
       Element sqrt1 = num.divide(denum, ring);
       sqrt1= sqrt1.sqrt(ring);
       Q=Q.multiply(sqrt1, ring);
       
   return Q;
}
   //Метод нахождения сферической функции в декартовых координатах
    /*
    n- степень, m - верхний индекс; x, y, z - переменные
    */
   public Element YDec(Element n,Element m,Element x,Element y,Element z,Ring ring){
       F arctg = new F("\\arctg(x)", ring);
       F arccos = new F("\\arccos(x)", ring);
       Element r = x.multiply(x, ring).add(y.multiply(y, ring), ring).add(z.multiply(z, ring), ring);
       r = r.sqrt(ring);
       Element costheta = z.divide(r, ring);
       if(m.isZero(ring)) {
           return new VectorS(new Element[]{nodes2(new NumberZ(n.intValue()), costheta, ring)});
       }
       Element phi = y.divide(x, ring);
       phi = phi.arctn(ring);//arctg.value(new Element[]{phi}, ring);

       Element Y=prisLeg(n, m, costheta, ring);//(Element) Q.cloneWithoutCFormPage();
       
//       Complex i = new Complex(NumberR.ONE, ring);
//       i.im = new NumberR(1);
//       i.re = new NumberR(0);
       //Element fruc=new NumberZ("1").divide(new NumberZ("2").multiply(new NumberR().pi(ring), ring),ring).sqrt(ring);
//       Element exp=m.multiply(i.multiply(y.divide(x, ring).arctn(ring), ring), ring).exp(ring);//new F("\\exp(\\i*y*"+m+")/\\sqrt(2*\\pi)", ring);
//       Y=Y.multiply(exp, ring);
       if(phi.isItNumber()) {
           return new VectorS(new Element[]{Y.multiply(m.multiply(phi, ring).sin(ring), ring),
           Y.multiply(m.multiply(phi, ring).cos(ring), ring)});
       } else {
           return new VectorS(new Element[]{Y.multiply(new F(F.SIN, new Element[]{m.multiply(phi, ring)}), ring),
           Y.multiply(new F(F.COS, new Element[]{m.multiply(phi, ring)}), ring)});
       }
   }
   
   /*
   Метод нахождения сферической функции в сферических координатах
   n- степень, m - верхний индекс; theta, phi - углы в сферических координатах 
   */
   public Element Y(Element n,Element m,Element theta,Element phi,Ring ring) {
       F cos = new F("\\cos(x)", ring);
       F sin = new F("\\sin(x)", ring);
       Element costheta = cos.value(new Element[]{theta}, ring);
       if(m.isZero(ring)) {
           return new VectorS(new Element[]{nodes2(new NumberZ(n.intValue()), costheta, ring)});
       }
       Element Pnm = prisLeg(n, m, costheta, ring);
       Element mphi = m.multiply(phi, ring);
       if(mphi.isItNumber()) {
       return new VectorS(new Element[]{Pnm.multiply(cos.value(new Element[]{mphi}, ring), ring),
           Pnm.multiply(sin.value(new Element[]{mphi}, ring), ring)});
       } else {
           return new VectorS(new Element[]{Pnm.multiply(new F(F.COS, new Element[]{mphi}), ring),
           Pnm.multiply(new F(F.SIN, new Element[]{mphi}), ring)});
       }
   }
   /*
   Метод нахождения шаровой функции в декартовых координатах
    n- степень, m - верхний индекс; x, y, z - переменные
   */
    public Element UDec(Element n,Element m,Element x,Element y,Element z,Ring ring){
       Element r = x.multiply(x, ring).add(y.multiply(y, ring), ring).add(z.multiply(z, ring), ring);
       r = r.sqrt(ring);
       VectorS tmp=(VectorS)YDec(n, m, x, y, z, ring);
       Element tmp2 = r;
       for(int j=2; j<=n.intValue(); j++) {
           r = r.multiply(tmp2, ring);
       }
       for (int i = 0; i < tmp.V.length; i++) {
           tmp.V[i] = tmp.V[i].multiply(r, ring);
       }
       return new VectorS(tmp);
   }
    /*
    Метод нахождения шаровой функции в сферических координатах
    n- степень, m - верхний индекс; theta, phi - углы в сферических координатах 
    */
    public Element U(Element n,Element m, Element r, Element theta,Element phi,Ring ring){
       VectorS tmp=(VectorS)Y(n, m, theta, phi, ring);
       Element tmp2 = r;
       for(int j=2; j<=n.intValue(); j++) {
           r = r.multiply(tmp2, ring);
       }
       for (int i = 0; i < tmp.V.length; i++) {
           tmp.V[i] = tmp.V[i].multiply(r, ring);
       }
       return new VectorS(tmp);
    }
}
