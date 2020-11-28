/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.probability;
import com.mathpar.number.*;
import com.mathpar.func.F;

/**
 *
 * @author kireev
 */
public class testRandomQuantity {
    public static void main(String [] args){
        Ring ring = new Ring("R64[x,y]");
        ring.setDefaultRing();

        NumberR64 X1[] = {new NumberR64(1),new NumberR64(2)};
        NumberR64 P1[] = {new NumberR64(0.2),new NumberR64(0.8)};
        RandomQuantity q = new RandomQuantity(X1,P1);

        System.out.println("Random Quantity q:");
        System.out.println(""+q.toString());

        Element a=new NumberR64(0);
        Element b=new NumberR64(4);
        F f = new F("1/4",ring);
        RandomQuantity qn = new RandomQuantity(a,b,f);
        System.out.println("qn="+qn.toString());

        Element z = q.mathExpectation(ring);
        System.out.println("mathExpectation(q)="+z);
        Element zz = qn.mathExpectation(ring);
        System.out.println("mathExpectation(qn)="+zz);
        Element z1 = q.dispersion(ring);
        System.out.println("dispersion(q)="+z1);
        Element zz1 = qn.dispersion(ring);
        System.out.println("dispersion(qn)="+zz1);
        Element z2 = q.meanSquareDeviation(ring);
        System.out.println("meanSquareDeviation(q)="+z2);
        Element zz2 = qn.meanSquareDeviation(ring);
        System.out.println("meanSquareDeviation(qn)="+zz2);
        System.out.println("");

//        NumberR64 X2[] = {new NumberR64(7), new NumberR64(5), new NumberR64(3), new NumberR64(5), new NumberR64(1)};
//        NumberR64 P2[] = {new NumberR64(0.2), new NumberR64(0.1), new NumberR64(0.3), new NumberR64(0.1), new NumberR64(0.3)};
//        RandomQuantity q2 = new RandomQuantity(X2, P2);
//        System.out.println("Random Quantity q2:");
//        System.out.println(""+q2.toString());
//        RandomQuantity q3 = q2.simplify(ring);
//        System.out.println("q3=sort(q2):");
//        System.out.println(""+q3.toString());
////
////        NumberR z4 = q3.mathExpectation(ring);
////        System.out.println("mathExpectation(q3)="+z4);
////        NumberR z5 = q3.dispersion(ring);
////        System.out.println("dispersion(q3)="+z5);
////        NumberR z6 = q3.meanSquareDeviation(ring);
////        System.out.println("meanSquareDeviation(q3)="+z6);
////        System.out.println("");
////
//        RandomQuantity z7 = q.add(q3, ring);
//        System.out.println("q+q3:");
//        System.out.println(""+z7.toString());
//        System.out.println("");
//
//        RandomQuantity z8 = q.multiply(q3, ring);
//        System.out.println("q*q3:");
//        System.out.println(""+z8.toString());
//        System.out.println("");
//
//        Element[] x={new NumberR64(51),new NumberR64(50),new NumberR64(48),new NumberR64(51),new NumberR64(46),new NumberR64(47),new NumberR64(49),new NumberR64(60),new NumberR64(51),new NumberR64(52),new NumberR64(56)};
//        Element[] y={new NumberR64(13),new NumberR64(15),new NumberR64(13),new NumberR64(16),new NumberR64(12),new NumberR64(14),new NumberR64(12),new NumberR64(10),new NumberR64(18),new NumberR64(10),new NumberR64(12)};
//        System.out.println("x[]="+Array.toString(x));
//        System.out.println("y[]="+Array.toString(y));
//
//        Element x1 = RandomQuantity.sampleMean(x, ring);
//        System.out.println("sampleMean(x)="+x1);
//        Element y1 = RandomQuantity.sampleMean(y, ring);
//        System.out.println("sampleMean(y)="+y1);
//        Element x2 = RandomQuantity.sampleDispersion(x, ring);
//        System.out.println("sampleDispersion(x)="+x2);
//        Element y2 = RandomQuantity.sampleDispersion(y, ring);
//        System.out.println("sampleDispersion(y)="+y2);
//        Element rez = RandomQuantity.covarianceCoefficient(x, y, ring);
//        Element rez1 = RandomQuantity.correlationCoefficient(x, y, ring);
//        System.out.println("covarianceCoefficient(x,y)="+rez);
//        System.out.println("correlationCoefficient(x,y)="+rez1);

//        System.out.println("param  "+Array.toString(param));
//        //q3.polygonDistribution(param);
//        Element X3[] = {new NumberR64(1), new NumberR64(3), new NumberR64(5), new NumberR64(7)};
//        Element P3[] = {new NumberR64(0.3), new NumberR64(0.1), new NumberR64(0.2), new NumberR64(0.4)};
//        RandomQuantity q4 = new RandomQuantity(X3, P3);
//        System.out.println("Random Quantity q4:");
//        System.out.println(""+q4.toString());
//        F f5 =q4.plotPolygonDistribution();
////
////        double X[] = {1,3,5,7};
////        double P[] = {0.3,0.1,0.2,0.4};
//        RandomQuantity q5 = new RandomQuantity(X3, P3);
//        F f6=q5.plotDistributionFunction();
//        F f7 = qn.plotDistributionFunction();
//
//        NumberR X4[] = {new NumberR(-7), new NumberR(-2), new NumberR(0), new NumberR(3),new NumberR(5),new NumberR(7),new NumberR(9)};
//        NumberR P4[] = {new NumberR(0.3), new NumberR(0.05), new NumberR(0.2), new NumberR(0.1),new NumberR(0.1),new NumberR(0.2),new NumberR(0.05)};
//        RandomQuantity q6 = new RandomQuantity(X4, P4);
//        System.out.println("Random Quantity q6:");
//        System.out.println(""+q6.toString());
//        //q6.polygonDistribution(param);
//
//        double X5[] = {-7,-2,0,3,5,7,9};
//        double P5[] = {0.3,0.05,0.2,0.1,0.1,0.2,0.05};
//        RandomQuantity q7 = new RandomQuantity(X5, P5);
//        //q7.distributionFunction(param);
//
//        System.out.println("");
//        NumberR ksi[] = {new NumberR(0),new NumberR(1)};
//        NumberR ksi1[] = {new NumberR(0.33333),new NumberR(0.66666)};
//        NumberR et[] = {new NumberR(1),new NumberR(2)};
//        NumberR et1[] = {new NumberR(0.25),new NumberR(0.75)};
//        RandomQuantity w1 = new RandomQuantity(ksi,ksi1);
//        System.out.println("RandomQuantity w1:");
//        System.out.println(""+w1.toString());
//        RandomQuantity w2 = new RandomQuantity(et,et1);
//        System.out.println("RandomQuantity w2:");
//        System.out.println(""+w2.toString());
//        RandomQuantity w3 = w1.multiply(w2, ring);
//        System.out.println("w1*w2:");
//        System.out.println(""+w3.toString());
//        System.out.println("");
//        System.out.println("w1+w2:");
//        RandomQuantity w4 = w1.add(w2, ring);
//        System.out.println(""+w4.toString());
//        System.out.println("");
//
//        double w5[] = {0,1};
//        double w6[] = {1,2};
//        double w7 = RandomQuantity.covarianceCoefficient(w5, w6);
//        System.out.println("cov({0,1},{1,2})="+w7);
//        double w8 = RandomQuantity.correlationCoefficient(w5, w6);
//        System.out.println("cor({0,1},{1,2})="+w8);
//
//
//        System.out.println("");
//        Element w9 = q.covariance(q, ring);
//        Element w10 = q.correlation(q, ring);
//        System.out.println("cov(q,q)="+w9);
//        System.out.println("cor(q,q)="+w10.ROUND_DOWN);

//        System.out.println("");
//        Element w11 = q.covariance(q3, ring);
//        Element w12 = q.correlation(q3, ring);
//        System.out.println("cov(q,q3)="+w11);
//        System.out.println("cor(q,q3)="+w12);
    }

}
