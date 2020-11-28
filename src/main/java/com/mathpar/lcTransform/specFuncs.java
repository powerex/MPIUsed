/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.lcTransform;

/**
 * Класс для вычисления спец функций
 * @author White_raven
 */
public class specFuncs {

/**
 * аппроксимация гамма-функции в интервале от 1 до 2 отношением полиномов 8 степени
 * @param x
 * @return
 */
double gammaapprox(double x){
        double p[]={-1.71618513886549492533811e+0,
                        2.47656508055759199108314e+1,
                       -3.79804256470945635097577e+2,
                        6.29331155312818442661052e+2,
                        8.66966202790413211295064e+2,
                       -3.14512729688483675254357e+4,
                       -3.61444134186911729807069e+4,
                        6.64561438202405440627855e+4};

        double q[]={-3.08402300119738975254353e+1,
                        3.15350626979604161529144e+2,
                       -1.01515636749021914166146e+3,
                       -3.10777167157231109440444e+3,
                        2.25381184209801510330112e+4,
                        4.75584627752788110767815e+3,
                       -1.34659959864969306392456e+5,
                       -1.15132259675553483497211e+5};
        double z = x - 1.0;
        double a = 0.0;
        double b = 1.0;
        for(int i = 0; i<8; i++){
                a =(a+p[i])*z;
                b = b*z+q[i];
        }
        return (a/b+1.0);
}

/**
 * Гамма-функция вещественного агрумента возвращает значение гамма-функции аргумента z
 * @param  z -вещественный аргумент
 * @return
 */
public double gamma(double z){
       if((z>0.0)&&(z<1.0)) { // рекурентное соотношение для 0
        return gamma(z+1.0)/z;
    }
       if(z>2) {  // рекурентное соотношение для z>2
        return (z-1)*gamma(z-1);
    }
       if(z<=0) { // рекурентное соотношение для z<=0
        return Math.PI/(Math.sin(Math.PI*z)*gamma(1-z));
    }
       return gammaapprox(z); // 1<=z<=2 использовать аппроксимацию
}
/**
 * Функция Бета
 * @param a
 * @param b
 * @return
 */
    public double  beta(double a, double b) {
        return gamma(a) * gamma(b) / gamma(a + b);
    }



    public static void main(String[] args) {
        System.out.println("Г(6)= " + new specFuncs().gamma(6));
        System.out.println("Г(-2)= " + new specFuncs().gamma(-2));
        System.out.println("Г(0)= " + new specFuncs().gamma(0));
        System.out.println("B(-1,7)= "+ new specFuncs().beta(-1, 7));
    }



}
