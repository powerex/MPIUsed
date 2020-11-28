/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.diffEq;
import com.mathpar.func.*;
import com.mathpar.number.*;
import com.mathpar.polynom.Polynom;
 

public class Bessel {
    public static void main(String[] args) {
        Ring r = new Ring("R[x]");
        r.FLOATPOS = 23; // При печати числа на экран указывает сколько цифр числа печатать.
        r.setMachineEpsilonR(20); // Точность (количество точных цифр после запятой)
        r.setMachineEpsilonR64(10); // То же самое (точность), только для кольца R64.
        // Кольцо R отличается от кольца R64 тем, что в R числа могут быть любой
        // длинны. А в R64 длинна числа всегда постоянная и равна 15 цифрам.
        r.setAccuracy(30); // Количество знаков, из которых состоят все числа во время вычислений.
        long t = System.currentTimeMillis();
        Element x = new NumberR("20");
        Element nu = new NumberR("20");
  //      Polynom a = new Polynom("x^2*(x+1)", r);
  //      Polynom b = new Polynom("x*(x+1)", r);
  //      Polynom c = new Polynom("(x^2-1)*(x+1)", r);
        Element w = BesselY(x, nu, r);
        System.out.println("res = "+w.toString(r)+" time = "+(System.currentTimeMillis() - t));
        
        //func(new Polynom("x^2", r), new Polynom("x", r), new Polynom("x^2", r), r);
    }
    
    static Element BesselJ(Element x, Element nu, Ring ring) {
    	NumberR64 epsilon = NumberR64.ONE;
    	int acc = 0;
    	// Если кольцо - R
    	if(ring.algebra[0] == Ring.R) {
    		// Записываем в epsilon точность из старого кольца
            int eps = ring.MachineEpsilonR.scale();
    		// Задаем в новом кольце точность на 5 цифр боьше, чем
    		// в старом кольце. (на всякий случай)
    		ring.setMachineEpsilonR(eps + 5);
    	    // Задаем длинну числа в соответствии с величиной x.
    		acc = ring.getAccuracy();
    		ring.setAccuracy(eps + ((int)x.intValue()/2)+15 );
    		// Если кольцо - R64
    	} else if (ring.algebra[0] == Ring.R64) {
    		// Тогда делаем и свое новое кольцо тоже R64.
    		// И задаем точность 14 цифр.
    		epsilon = ring.MachineEpsilonR64;
    		ring.setMachineEpsilonR64(14);
    	} else {
    		// Если кольцо не является ни R, ни R64, то
    		// мы ни чего не можем посчитать, поэтому прекращаем рассчеты.
    		return null;
    	}
    	// Вычисляем значение BesselJ по формуле из твоей записки.
        F gamma = new F(F.GAMMA, new Element[]{new Polynom("x", ring)});
        Element pow = x.divide(new NumberZ("2"), ring).pow(nu, ring);
        Element pow2 = ring.numberONE;
        Element Kfactorial = ring.numberONE;
        // Считаем нулевой член ряда: ( (x/2)^nu)/nu!
        Element result = ring.numberONE;
        Element result2 = null;
        result = result.divide(gamma.valueOf(new Element[]{nu.add(ring.numberONE, ring)}, ring), ring);
        Element temp = null;
        int k = 1;
        // Далее считаем остальные члены уже по всей формуле.
        do {
        	result2 = result;
        	Kfactorial = Kfactorial.multiply(new NumberZ(""+k), ring);
        	pow2 = pow2.multiply(x.multiply(x, ring).divide(new NumberZ("4"), ring), ring);
        	temp = Kfactorial;
        	temp = temp.multiply(gamma.valueOf(
        			new Element[]{nu.add(new NumberZ(""+k), ring).add(ring.numberONE, ring)}, ring), ring);
        	temp = ring.numberONE.divide(temp, ring);
        	if( (k % 2) != 0) {
        		temp = temp.negate(ring);
        	}
            temp = temp.multiply(pow2, ring);
            result = result.add(temp, ring);
            k++;
            // Считаем до тех пор, пока очередной член ряда
            // не станет меньше погрешности.
        } while(!result.subtract(result2, ring).abs(ring).isZero(ring));
        result = result.multiply(pow, ring);
        
        if(acc != 0) {
        	ring.setAccuracy(acc);
        }
        if(!epsilon.isOne(ring)) {
        	ring.setMachineEpsilonR64(epsilon);
        }
        ring.setMachineEpsilonR(ring.MachineEpsilonR.scale() - 5);
        return result;
    }
    
    
    public static Element func(Polynom a, Polynom b, Polynom c, Ring ring) {
    	// Определяем, является ли уравнение уравнением Бесселя.
    	// На входе у этой функции три многочлена a, b и c из
    	// дифференциального уравнения y''a + y'b + yc =0
    	// Дальше проверяем каждый из многочленов a, b и c
    	// подходит ли он или нет.
    	// Если a не x^2, то это уравнение не наше, и поэтому
    	// завершаем работу.
    	Polynom p = ring.varPolynom[0];
    	p = p.multiply(p, ring);

    	Element[] pols = null;
    	if(a.coeffs.length > 1) {
    		pols = a.divAndRemToRational(p, ring);
    		if(!pols[1].isZero(ring)) {
    			return null;
    		}
    		a = p;
    		b = (Polynom) b.divide(pols[0], ring);
    		c = (Polynom) c.divide(pols[0], ring);
    	}

        if( (a.coeffs.length != 1)||(a.powers[0] != 2) ) {
            return null;
        }
        // Если b не x, то завершаем работу.
        if( (b.coeffs.length != 1)||(b.powers[0] != 1) ) {
            return null;
        }
        // Если с не x^2-a и не x^2, то завершаем работу.
        if( !( (c.coeffs.length == 1)&&(c.powers[0] == 2) ) ) {
            if( !( (c.coeffs.length == 2)&&(c.powers[0] == 2)&&(c.powers[1] == 0) ) ) {
                return null;
            }
        }
        
        // Находим nu. Полином с, который его содержит либо
        // такой: x^2-a, либо такой: x^2.
        Element nu;
        // Если полином c имеет вид x^2-a, то nu равно минус квадратному корню
        // из a.
        if(c.coeffs.length != 1) {
            nu = c.coeffs[1].negate(ring).sqrt(ring);
        } else {
         // В противном случае nu равно нулю.
            nu = NumberZ.ZERO;
        }
        // Если nu целое, то мы должны вывести функцию бесселя на экран.
        if(nu.subtract(nu.round(ring), ring).isZero(ring)) {
        	Fname alpha = new Fname("a");
        	F J = new F(F.BESSELJ, new Element[]{alpha, ring.varPolynom[0]});
        	F Y = new F(F.BESSELY, new Element[]{alpha, ring.varPolynom[0]});
        	Fname c1 = new Fname("c1");
        	Fname c2 = new Fname("c2");
            return new F(F.ADD, new Element[]{new F(F.MULTIPLY, new Element[]{c1, J}),
            		new F(F.MULTIPLY, new Element[]{c2, Y})});
        } else {
        	Fname alpha = new Fname("a");
        	Fname Malpha = new Fname("-a");
        	F J1 = new F(F.BESSELJ, new Element[]{alpha, ring.varPolynom[0]});
        	F J2 = new F(F.BESSELJ, new Element[]{Malpha, ring.varPolynom[0]});
        	Fname c1 = new Fname("c1");
        	Fname c2 = new Fname("c2");
        	return new F(F.ADD, new Element[]{new F(F.MULTIPLY, new Element[]{c1, J1}),
            		new F(F.MULTIPLY, new Element[]{c2, J2})});
        }
    }
    
    static Element BesselY(Element x, Element nu, Ring ring) {
    	int acc = 0;
    	NumberR64 epsilon = NumberR64.ONE;
    	if(ring.algebra[0] == Ring.R) {
    		int eps = ring.MachineEpsilonR.scale();
    		acc = ring.getAccuracy();
    		ring.setMachineEpsilonR(eps+5);
    		ring.setAccuracy(eps + x.divide(new NumberZ("2"), ring).intValue()
    				+ new NumberR("1.5").multiply(nu, ring).intValue() + 5);
    	} else if(ring.algebra[0] == Ring.R64) {
    		epsilon = ring.MachineEpsilonR64;
    		ring.setMachineEpsilonR64(14);
    	}
    	// Считаем BesselY по формуле.
        Element result = ring.numberZERO;
        Element result2 = ring.numberONE;
        Element temp2 = ring.numberZERO;
        // Так как в mathpar нет производной функции гамма, то
        // вычислить константу C с нужной точностью не получится. Поэтому
        // пока задаю ее вручную.
        Element C = C(ring);
        NumberZ two = new NumberZ(2);
        Element Kfactorial = ring.numberONE;
        Element KplusNfactorial = nu.factorial(ring);
        Element pow = x.divide(two, ring).pow(nu.negate(ring), ring);
        System.out.println("pow = "+pow);
        Element pow2 = ring.numberONE;
        // Считаю ту сумму, которая конечная.
        for(NumberZ i = NumberZ.ZERO; 
                !nu.subtract(ring.numberONE, ring).subtract(i, ring).isNegative(); i = i.add(NumberZ.ONE)) {
           if(!i.isZero(ring)) {
            	Kfactorial = Kfactorial.multiply(i, ring);
            }
        	temp2 = temp2.subtract(nu.subtract(i, ring).subtract(ring.numberONE, ring).factorial(ring)
                    .multiply(pow2, ring).divide(Kfactorial, ring), ring);
            pow2 = pow2.multiply(x.multiply(x, ring).divide(new NumberZ("4"), ring), ring);
        }
        temp2 = temp2.multiply(pow, ring);
        System.out.println("t2 = = "+temp2);
        
        // Считаю ту сумму из формулы, которая бесконечная.
        // Как и обычно, рассчет останавливается когда очередное
        // слагаемое становится меньше погрешности.
        Element temp = ring.numberONE;
        Kfactorial = ring.numberONE;
        Element tmp = x.divide(two, ring).ln(ring).add(C, ring).multiply(two, ring);
        Element sum = ring.numberZERO;
        pow = x.divide(two, ring).pow(nu, ring);
        pow2 = ring.numberONE;
        for(int i = 0 ;!result.subtract(result2, ring).abs(ring).isZero(ring); i++) {
        	result2 = result;
        	NumberZ k = new NumberZ(i);
        	if(i != 0){
            	Kfactorial = Kfactorial.multiply(k, ring);
                KplusNfactorial = KplusNfactorial.multiply(k.add(nu, ring), ring);
            }
            temp = tmp;
            if(i != 0) {
                sum = sum.add(two.divide(k, ring), ring);
                temp = temp.subtract(sum, ring);
            }
            for(NumberZ j = k.add(NumberZ.ONE); !nu.add(k, ring).subtract(j, ring).isNegative() ; j = j.add(NumberZ.ONE)) {
                temp = temp.subtract(ring.numberONE.divide(j, ring), ring);
            }
            temp = temp.multiply(pow2, ring);
            if(!k.divide(two, ring).subtract(k.divide(two, ring).round(ring), ring).isZero(ring)) {
                temp = temp.negate(ring);
            }
            temp = temp.divide(Kfactorial, ring);
            temp = temp.divide(KplusNfactorial, ring);
            result = result.add(temp, ring);
            pow2 = pow2.multiply(x.multiply(x, ring).divide(new NumberZ("4"), ring), ring);
        }
        result = result.multiply(pow, ring);
        result = result.add(temp2, ring);
        result = result.divide(new NumberR().pi(ring), ring);
        
        if(acc != 0) {
        	ring.setAccuracy(acc);
        }
        if(!epsilon.isOne(ring)) {
        	ring.setMachineEpsilonR64(epsilon);
        }
        ring.setMachineEpsilonR(ring.MachineEpsilonR.scale() - 5);
        return result;
    }
    

    
    static Element C(Ring ring) {
    	int acc = 0;
    	Element epsilon = null;
    	Element n = null;
    	if(ring.algebra[0] == Ring.R) {
    	    epsilon = new NumberR(ring.MachineEpsilonR.scale()+"");
            n = new NumberR().pi(ring).ln(ring)
    			.add(epsilon.multiply(new NumberR("10").ln(ring), ring), ring)
    			.divide(new NumberZ("4"), ring);
    	
    	    n = n.add(epsilon.divide(new NumberR("15"), ring), ring).round(ring);
    	    n = n.add(new NumberR("5"), ring);
    	

    	    if(ring.getAccuracy()-ring.MachineEpsilonR.scale() < 10) {
    		    acc = ring.getAccuracy();
    		    ring.setAccuracy(ring.MachineEpsilonR.scale()+10);
    	    }
    	} else if(ring.algebra[0] == Ring.R64) {
    		n = new NumberR("20");
    	}
    	
    	Element n2 = n.multiply(n, ring);
    	Element An = n.ln(ring).negate(ring);
    	Element Bn = ring.numberONE;
    	Element U = An;
    	Element V = Bn;
    	Element k;
    	Element k2;
    	Element AB = ring.numberONE;
    	for(int i=1; ( !An.isZero(ring) )||( !Bn.isZero(ring) ); i++) {
    		k = new NumberZ(""+i);
    		k2 = new NumberZ(""+i*i);
    		Bn = Bn.multiply(n2, ring).divide(k2, ring);
    		V = V.add(Bn, ring);

    		An = An.multiply(n2, ring).divide(k, ring);
    		An = An.add(Bn, ring);
    		An = An.divide(k, ring);
    		U = U.add(An, ring);
    		AB = U.divide(V, ring);
    	}
    	
    	if(acc != 0) {
    		ring.setAccuracy(acc);
    	}
    	return AB;
    }
}