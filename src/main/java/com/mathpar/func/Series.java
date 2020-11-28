/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import java.util.ArrayList;
import java.util.Vector;
import com.mathpar.number.*;
import com.mathpar.polynom.Polynom;

/**
 *
 * @author ridkeim
 */
public class Series extends F {

    public static void main(String[] args) {
        Ring r = new Ring("Z[x]");
//        Polynom p = new Polynom("x^11-x^10+2x^8-4x^7+3x^5-3x^4+x^3+2x^2-x-1", r);
//        Polynom p =  new Polynom("x^5-2x^4+2x^2-x", r);
//        Polynom p0 =  new Polynom("x^5-x^4-2x^3+2x^2+x-1", r);
//       Polynom p0 = p.D(r);
//        Polynom gcd = p.gcd(p0, r);
//        Polynom Pred = p.divide(gcd, r);
//        System.out.println("p = "+p);
//        System.out.println("p0 = "+p0);
//        System.out.println("gcd = "+gcd);
//        Polynom p1 = p.divide(gcd, r);
//        Polynom p2 = p0.divide(gcd, r);
//
//        Polynom gcd1 = p1.gcd(p2, r);
//        System.out.println("gcd1 = "+gcd1);
//        System.out.println("Pred = "+Pred);
        Fname g = new Fname("a");
        Fname f = new Fname("a");
        F s1 = new F(F.SERIES, new Element[]{new F("x^a a_0", r), g, new NumberZ("6")});
        F s2 = new F(F.SERIES, new Element[]{new F("6 x^{a} \\cos( a_1 x)", r), f, new NumberZ("9")});
//        F asd = new F("\\sin(2xy*g)", r);
        //F s2 = new F(F.SERIES, new Element[]{s01.add(asd, r), new Fname("f_2"), new NumberZ("1")});
        F s3 = (F) multiplySeries(s1, s2, r);
        //F s4 = new F(F.MULTIPLY_SERIES, new Element[]{s1,s2});
        System.out.println("s1 = " + s1);
        System.out.println("s2 = " + s2);
        System.out.println("s2 = " + s3);
        //System.out.println("fs = " + s4);
        //F s5 = new F("\\sum_{a=1}^{\\infty} (2xy*a)", r);
        //System.out.println("s5 = "+s5);
        //s4.showTree(r);
        NumberZ a = new NumberZ("1");
        NumberR as = new NumberR("2");
        System.out.println(a.numbElementType());
        System.out.println(as.numbElementType());
        System.out.println(Ring.C64);

    }

    public static Fname[] getFnameFromSeries(F s) {
        Vector<F>[] vectF = new Vector[F.FUNC_ARR_LEN];
        Vector<Element>[] vectEl = new Vector[Ring.NumberOfElementTypes];
        for (int i = 0; i < Ring.NumberOfElementTypes; i++) {
            vectEl[i] = new Vector<Element>();
        }
        Ring ring =Ring.ringR64xyzt; //.cloneWithoutCFormPage(Ring.ringR64xyzt);
        ring.CForm=new CanonicForms(ring,false); //2015
        ring.CForm.vectEl=vectEl; ring.CForm.vectF=vectF;
        F.cleanOfRepeating(s, ring );
        return vectEl[Ring.Fname].toArray(new Fname[]{});
    }

    public static Fname[] getCountFnameFromSeries(F s) {
        F temp = (F) s.clone();
        int i = 0;
        ArrayList<Fname> countnames = new ArrayList();
        while ((temp.name == F.SERIES) && ((temp.X[0] instanceof F) | (temp.X[0] instanceof Fname))) {
            if (temp.X[1] instanceof Fname) {
                countnames.add((Fname) temp.X[1]);
            }
            if (temp.X[0] instanceof F) {
                temp = (F) temp.X[0];
            } else {
                break;
            }
            i++;
        }
        if ((temp.X[0] instanceof Polynom) && (temp.name == F.SERIES)) {
            i++;
        }
        Fname[] res = new Fname[]{};
        if (i == countnames.size()) {
            return countnames.toArray(res);
        } else {
            return res;
        }

    }

    public static F changeFname(F s, Fname oldfname, Fname newfname, Ring r) {
        int a = 1;
        for (int i = 0; i < s.X.length; i++) {
            switch (s.X[i].numbElementType()) {
                case Ring.F:
                    s.X[i] = changeFname((F) s.X[i], oldfname, newfname, r);
                    break;
                case Ring.Fname: {
                    if (oldfname.equals(s.X[i], r)) {
                        s.X[i] = newfname;
                    }
                }
            }
        }
        return s;
    }

    public static F valueOfSeries(F s, Element value, Ring r) {

        F temp = (F) s.clone();
        Element val = (Element) value.clone();
        Fname name = (Fname) temp.X[1];
        for (int i = 0; i < temp.X.length; i++) {
            switch (temp.X[i].numbElementType()) {
                case Ring.F:
                    temp.X[i] = valueOfSeries_((F) temp.X[i], name, value, r);
                case Ring.Fname: {
                    if (name.equals(temp.X[i], r)) {
                        temp.X[i] = value;
                    }
                }
            }
        }
        return s;
    }

    public static F valueOfSeries_(F s, Fname name, Element value, Ring r) {
        for (int i = 0; i < s.X.length; i++) {
            switch (s.X[i].numbElementType()) {
                case Ring.F:
                    s.X[i] = valueOfSeries_((F) s.X[i], name, value, r);
                case Ring.Fname: {
                    if (name.equals(s.X[i], r)) {
                        s.X[i] = value;
                    }
                }
            }
        }
        return s;
    }

    private static Element putNumberToElement(Element S, Fname name, NumberZ z) {

        switch (S.numbElementType()) {
            case Ring.F:
                for (int i = 0; i < ((F) S).X.length; i++) {
                    ((F) S).X[i] = putNumberToElement(((F) S).X[i], name, z);
                }
                S = (F) S;
                break;
            case Ring.Fname:
                if (S.equals(name, Ring.ringR64xyzt)) {
                    S = z;
                }
                break;
        }
        return S;
    }

    public static Fname[] changeFnameInMass(Fname[] a, Fname oldfname, Fname newfname, Ring r) {
        int i = 0;
        boolean flag = true;
        while ((flag) && (i < a.length)) {
            if (a[i].equals(oldfname, r)) {
                a[i] = newfname;
                flag = false;
            }
            i++;
        }
        return a;
    }

    public static int numbOfSeries(F s) {
        int i = 0;
        for (int j = 0; j < s.X.length; j++) {
            if (s.X[j] instanceof F) {
                if (((F) s.X[i]).name == F.SERIES) {
                    i++;
                }
            }
        }
        return i;

    }

    public static Element add(Element s1, Element s2, Ring r) {
        if (s1 instanceof F) {
            F f1 = (F) s1.clone();
            if (f1.name == F.SERIES) {
                if (s2 instanceof F) {
                    F f2 = (F) s2.clone();
                    if (f2.name == F.SERIES) {
                        return addSeries(f1, f2, r);
                    } else {
                        return f1.add(f2, r);
                    }
                } else {
                    return f1.add((Element) s2.clone(), r);
                }
            }
        }
        return s1.add(s2, r);
    }

    public static F addSeries(F s1, F s2, Ring r) {
        Fname count1 = (Fname) s1.X[1];
        Fname count2 = (Fname) s2.X[1];
        NumberZ s1b = (NumberZ) s1.X[2].toNumber(Ring.Z, r);
        NumberZ s2b = (NumberZ) s2.X[2].toNumber(Ring.Z, r);
        Element sum = new F();
        boolean flag = false;
        if (s1b.compareTo(s2b, -3, r)) {

            //если не совпадают начальные коэффициенты
            flag = true;
            if (s1b.compareTo(s2b, 2, r)) {
                NumberZ s1b_1 = (NumberZ) s1b.subtract(NumberZ.ONE, r);
                if (!(s2b.compareTo(s1b_1, -3, r))) {
                    sum = putNumberToElement((Element) s2.X[0].clone(), (Fname) s2.X[1], s2b);
                } else {
                    sum = new F(F.SUM, new Element[]{
                                (Element) s2.X[0].clone(),
                                (Element) s2.X[1].clone(),
                                (Element) s2b.clone(),
                                (Element) s1b_1
                            });
                }
                s2.X[2] = s1b;
            } else {
                NumberZ s2b_1 = (NumberZ) s2b.subtract(NumberZ.ONE, r);
                if (!(s1b.compareTo(s2b_1, -3, r))) {
                    sum = putNumberToElement((Element) s1.X[0].clone(), (Fname) s1.X[1], s1b);
                } else {
                    sum = new F(F.SUM, new Element[]{
                                (Element) s1.X[0].clone(),
                                (Element) s1.X[1].clone(),
                                (Element) s1b,
                                (Element) s2b_1
                            });

                }
                s1.X[2] = s2b;
            }
        }
        Fname[] names1 = getFnameFromSeries(s1);
        Fname[] names2 = getFnameFromSeries(s2);
        if (!count1.equals(count2, r)) {
            if (ifFnameInMass(count1, names2, r)) {
                Fname newfname = new Fname(getNewName(count1, true));
                while (ifFnameInMass(newfname, names2, r) | ifFnameInMass(newfname, names1, r)) {
                    newfname = new Fname(getNewName(newfname, false));
                }
                s1 = changeFname(s1, count1, newfname, r);
                s2 = changeFname(s2, count2, newfname, r);
            } else {
                s2 = changeFname(s2, count2, count1, r);
            }
        }
        s1.X[0] = s1.X[0].add(s2.X[0], r);
        if (flag) {
            s1 = (F) sum.add(s1, r);
        }
        return s1;
    }

    public static Element subtract(Element s1, Element s2, Ring r) {
        if (s1 instanceof F) {
            F f1 = (F) s1.clone();
            if (f1.name == F.SERIES) {
                if (s2 instanceof F) {
                    F f2 = (F) s2.clone();
                    if (f2.name == F.SERIES) {
                        return subtractSeries(f1, f2, r);
                    } else {
                        return f1.subtract(f2, r);
                    }
                } else {
                    return f1.subtract((Element) s2.clone(), r);
                }
            }
        }
        return s1.subtract(s2, r);
    }

    public static F subtractSeries(F s1, F s2, Ring r) {
        Fname count1 = (Fname) s1.X[1];
        Fname count2 = (Fname) s2.X[1];
        Fname[] names1 = getFnameFromSeries(s1);
        Fname[] names2 = getFnameFromSeries(s2);
        NumberZ s1b = (NumberZ) s1.X[2].toNumber(Ring.Z, r);
        NumberZ s2b = (NumberZ) s2.X[2].toNumber(Ring.Z, r);
        Element sum = new F();
        boolean flag = false;
        if (s1b.compareTo(s2b, -3, r)) {
            //если не совпадают начальные коэффициенты
            flag = true;
            int k = 0;
            if (s1b.compareTo(s2b, 2, r)) {
                NumberZ s1b_1 = (NumberZ) s1b.subtract(NumberZ.ONE, r);
                if (!(s2b.compareTo(s1b_1, -3, r))) {
                    sum = putNumberToElement((Element) s2.X[0].clone(), (Fname) s2.X[1], s2b);
                } else {
                    sum = new F(F.SUM, new Element[]{
                                (Element) s2.X[0].clone(),
                                (Element) s2.X[1].clone(),
                                (Element) s2b,
                                (Element) s1b.subtract(r.numberONE, r)
                            });
                }
                s2.X[2] = (Element) s1b;
            } else {
                s2b = (NumberZ) s2b.subtract(NumberZ.ONE, r);
                if (!(s1b.compareTo(s2b, -3, r))) {
                    sum = putNumberToElement((Element) s1.X[0].clone(), (Fname) s1.X[1], s1b);
                } else {
                    sum = new F(F.SUM, new Element[]{
                                (Element) s1.X[0].clone(),
                                (Element) s1.X[1].clone(),
                                (Element) s1b,
                                (Element) s2b
                            });

                }
                s1.X[2] = (Element) s2b.add(NumberZ.ONE, r);
            }
        }

        if (!count1.equals(count2, r)) {
            if (ifFnameInMass(count1, names2, r)) {
                Fname newfname = new Fname(getNewName(count1, true));
                while (ifFnameInMass(newfname, names2, r) | ifFnameInMass(newfname, names1, r)) {
                    newfname = new Fname(getNewName(newfname, false));
                }
                s1 = changeFname(s1, count1, newfname, r);
                s2 = changeFname(s2, count2, newfname, r);
            } else {
                s2 = changeFname(s2, count2, count1, r);
            }
        }
        s1.X[0] = s1.X[0].subtract(s2.X[0], r);
        if (flag) {
            if (s1b.compareTo(s2b, 2, r)) {
                s1 = (F) s1.subtract(sum, r);
            } else {
                s1 = (F) sum.add(s1, r);
            }
        }
        return s1;
    }

    public static F multiplySeries(F s1, F s2, Ring r) {
        Fname[] names1 = getFnameFromSeries(s1);
        Fname[] names2 = getFnameFromSeries(s2);
        Fname[] countnames1 = getCountFnameFromSeries(s1);
        Fname[] countnames2 = getCountFnameFromSeries(s2);
        for (int i = 0; i < countnames1.length; i++) {
            s1 = changeFname(s1, countnames1[i], countnames1[i], r);
            names1 = changeFnameInMass(names1, countnames1[i], countnames1[i], r);
            if (ifFnameInMass(countnames1[i], names2, r)) {
                Fname newfname = new Fname(getNewName(countnames1[i], true));
                while (ifFnameInMass(newfname, names2, r) | ifFnameInMass(newfname, names1, r)) {
                    newfname = new Fname(getNewName(newfname, false));
                }
                countnames1[i].name = newfname.name;
            }

        }
        for (int i = 0; i < countnames2.length; i++) {
            s2 = changeFname(s2, countnames2[i], countnames2[i], r);
            names2 = changeFnameInMass(names2, countnames2[i], countnames2[i], r);
            if (ifFnameInMass(countnames2[i], names1, r)) {
                Fname newfname = new Fname(getNewName(countnames2[i], true));
                while (ifFnameInMass(newfname, names2, r) | ifFnameInMass(newfname, names1, r)) {
                    newfname = new Fname(getNewName(newfname, false));
                }
                countnames2[i].name = newfname.name;
            }
        }

        F[] result = new F[countnames1.length + countnames2.length + 1];
        int i = 0;
        while (s1.name == F.SERIES) {
            result[i] = (F) s1.clone();
            s1 = (F) s1.X[0];
            i++;
        }
        while (s2.name == F.SERIES) {
            result[i] = (F) s2.clone();
            s2 = (F) s2.X[0];
            i++;
        }
        result[i] = (F) s1.multiply(s2, r);
        for (i = result.length - 1; i > 0; i--) {
            result[i - 1].X[0] = result[i];
        }
        return result[0];
    }

    public static F multiplySeriesAndElement(F f0, Element f1, Ring r) {
        switch (f1.numbElementType()) {
            case Ring.F:
                return multiplySeriesAndF(f0, (F) f1, r);
            default:
                return new F(F.MULTIPLY, new Element[]{f1, f0});
        }
    }

    public static F multiplySeriesAndF(F f0, F f1, Ring r) {
        switch (f1.name) {
            case F.SERIES:
                return multiplySeries(f0, f1, r);
            default:
                return new F(F.MULTIPLY, new Element[]{f1, f0});
        }
    }

    public static Element multiply(Element f0, Element f1, Ring r) {
        Element s1 = (Element) f0.clone();
        Element s2 = (Element) f1.clone();
        if (s1.numbElementType() == Ring.F) {
            if (((F) s1).name == F.SERIES) {
                return multiplySeriesAndElement((F) s1, s2, r);
            }
        } else {
            if (s2.numbElementType() == Ring.F) {
                if (((F) s2).name == F.SERIES) {
                    return multiplySeriesAndElement((F) s2, s1, r);
                }
            }
        }
        return s1.multiply(s2, r);
    }

    public static boolean ifFnameInMass(Fname name, Fname[] mas, Ring r) {
        for (int i = 0; i < mas.length; i++) {
            if (mas[i].equals(name, r)) {
                return true;
            }
        }
        return false;
    }

    public static String getNewName(Fname name, boolean flag) {
        String res = name.name;
        int k = (int) '0';
        if (res.lastIndexOf('_') == -1) {
            return res + "_0";
        }
        if (flag) {
            res = res.substring(0, res.lastIndexOf('_'));
        } else {
            if (res.lastIndexOf('_') == res.length() - 1) {
                return res + "0";
            }
            int j = res.lastIndexOf('_') + 1;
            String temp = res.substring(j, res.length());
            String in = "";
            for (int i = 0; i < temp.length(); i++) {
                in = ((temp.charAt(i) >= '0') && (temp.charAt(i) <= '9')) ? in + "" + temp.charAt(i) : in;
            }
            if (in.length() == 0) {
                return res.substring(0, j);
            }
            int i = new Integer(in);
            res = res.substring(0, j) + "" + (i + 1);
        }
        return res;
    }
}
