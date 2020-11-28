/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.RayTracing;

/**
 *
 * @author yuri
 */
public class polynom9 {

    private double[] a;
    private int n;

    public polynom9() {
    }

    public polynom9(double[] a) {
        this.a = a;
        this.n = a.length - 1;
    }

    public polynom9(double[] a, int n) {
        this.a = a;
        this.n = n;
    }

    @Override
    protected polynom9 clone() {
        return new polynom9(a, n);
    }

    private polynom9 derivative() {

        double[] d = new double[n];
        for (int i = n; i > 0; i--) {
            d[i - 1] = a[i] * i;
        }
        return new polynom9(d, n - 1);
    }

    ;

    private void divide(polynom9 b, polynom9 res, polynom9 ost) {
        int ostn = n;
        double[] osta = a.clone();
        int resn = ostn - b.n;
        double[] resa = new double[resn + 1];
        while (ostn >= b.n) {
            double k = -osta[ostn] / b.a[b.n];
            int pow = ostn - b.n;
            resa[pow] = -k;
            for (int i = 1; i <= b.n; i++) {
                osta[ostn - i] += b.a[b.n - i] * k;
            }
            ostn--;
        }
        ost.n = ostn;
        ost.a = osta;
        res.n = resn;
        res.a = resa;
    }
    double e = 1e-5;

    private boolean isNull() {
        boolean res = true;
        for (int i = n; i >= 0 && res; i--) {
            res = a[i] < e && a[i] > -e;
        }
        return res;
    }
    double root;

    private void findAndTerminateDoubleRoots() {
        polynom9 p1 = this.clone();
        polynom9 p2 = derivative();

        polynom9 res = new polynom9();
        polynom9 ost = new polynom9();
        boolean flag = true;
        for (int i = 0; flag; i++) {
            System.out.print("p1 " + p1);
            System.out.print("p2 " + p2);
            p1.divide(p2, res, ost);
//            res.normalize();
//            ost.normalize();
            System.out.print("res " + res);
            System.out.print("ost " + ost);
            p1 = p2.clone();
            p2 = ost;
            flag = ost.n > 1;
        }
        boolean ostIsNull;
        /*        do {
        polynom9 tmpOst = new polynom9();
        divide(ost, res, tmpOst);
        ostIsNull = tmpOst.isNull();
        if (ostIsNull) {
        this.a = res.a;
        this.n = res.n;
        }
        } while (ostIsNull);*/
    }

    private polynom9 normalize() {
        while (a[n] < e && a[n] > -e) {
            n--;
        }
        double k = a[n];
        a[n] = 1.0;
        for (int i = n - 1; i >= 0; i--) {
            a[i] /= k;
        }
        return this;
    }

    @Override
    public String toString() {
        String buf = "";
        for (int i = n; i >= 0; i--) {
            buf += Math.abs(a[i]) > 1e-5 ? a[i] + (i > 0 ? "x" + (i > 1 ? "^" + i + " + " : " + ") : " ") : "";
        }

        return "polynom9{" + buf + "}\n";
    }

    public static void main(String[] args) {
        System.out.print("dsds\n");

        double[] a = {35, 94, 87, 32, 4,};
        double[] b = {1, 1,};

        polynom9 p = new polynom9(a);
        polynom9 res = new polynom9();
        polynom9 ost = new polynom9();
        polynom9 pb = new polynom9(b);

        System.out.print(p);

        System.out.print(p.derivative());

        p.divide(pb, res, ost);
        System.out.print(res);
        System.out.print(ost);

        res.divide(pb, res, ost);
        res.normalize();
        System.out.print(res);
        System.out.print(ost);


        p.findAndTerminateDoubleRoots();
        System.out.print(p);
        p.normalize();
        System.out.print(p);



//        System.out.print(p.derivative());
/*


        p.divide(p.derivative(), res, ost);
        System.out.print(p.derivative());
        System.out.print(res);
        System.out.print(ost);



        res.divide(pb, res, ost);
        System.out.print(res);
        System.out.print(ost);
         */
    }
}
