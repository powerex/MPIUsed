/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

///* Вычисление транциндентных функций (NumberR)  */
//package func;
//
//import number.*;
//import number.math.MathContext;
//import number.math.RoundingMode;
//
//class funcNumberC {
//
//    public static Complex PI_2() {
//        return new Complex(FuncNumberR.pi().divide(new NumberR(2.0), Notebook.SCALE),
//                NumberR.ZERO);
//    }
//
//    /**процедура замены переменной x на t
//     * @param  x (NumberR) аргумент
//     * @return t (NumberR)
//     */
//    public static int pogresh(Complex z) {
//        //int Notebook.SCALE;
//        //Notebook.SCALE=50;
//
//        NumberR x = (NumberR) z.re.abs();
//        Integer t = 0;
//        NumberR c = NumberR.ONE;
//        if (x.compareTo(c) == 1) {
//            while (x.compareTo(c) == 1) {
//                c = c.multiply(new NumberR(10));
//                t++;
//            }
//        } else {
//            if (x.compareTo(c) == 0) {
//                t = 0;
//            } else {
//                while (x.compareTo(c) == -1) {
//                    c = c.multiply(new NumberR(0.1));
//                    t--;
//                }
//            }
//        }
//
//        NumberR y = (NumberR) z.re.abs();
//        Integer t1 = 0;
//        c = NumberR.ONE;
//        if (y.compareTo(c) == 1) {
//            while (y.compareTo(c) == 1) {
//                c = c.multiply(new NumberR(10));
//                t1++;
//            }
//        } else {
//            if (y.compareTo(c) == 0) {
//                t = 0;
//            } else {
//                while (y.compareTo(c) == -1) {
//                    c = c.multiply(new NumberR(0.1));
//                    t1--;
//                }
//            }
//        }
//        if (t > t1) {
//            return t;//возвращаем полученный результат
//        } else {
//            return t1;
//        }
//    }
//
//    /**
//     * Вычисление pi по формуле
//     * pi=16*arctg(1/5)-4*arctg(1/239)
//     *
//    public static Complex pi(int Notebook.SCALE){
//    MathContext mc=initMathContext(Notebook.SCALE+5);
//    Complex a = Complex.ONE.divide(new Complex(5), mc);
//    Complex bd = new Complex(239);
//    Complex ed = new Complex(1);
//    Complex b = ed.divide(bd, mc);
//    Complex arctg1=arctn(a,Notebook.SCALE+5);
//    Complex arctg2=arctn(b,Notebook.SCALE+5);
//    Complex y = arctg1.multiply(new Complex(16), mc).subtract(arctg2.multiply(new Complex(4), mc), mc);
//
//    return y;
//    }
//     */
//    /**
//     * Метод вычисления квадратного корня. Вычисление происходит путем построения
//     * полинома x^2-d, и нахождении корней методом хорд
//     *
//     * @param d NumberR - подкоренное число
//     * @param eps int - точность с которой это число вычисляется
//     * @return NumberR результат вычисления
//     */
//    public static Complex sqrt(Complex d) {
//        NumberR BGG = new NumberR(0.1).pow(Notebook.SCALE);
////если d равно нулю, то корень равен нулю
//        NumberR d1 = (NumberR) d.re;
//        if (d1.compareTo(new NumberR(0)) == 0) {
//            return new Complex(0);
//        }
////если d равно единице, то корень равен единице
//        if (d1.compareTo(new NumberR(1)) == 0) {
//            return new Complex(1);
//        }
////объявляем переменные - начало и конец отрезка
//        Complex A = null;
//        Complex B = null;
//// если подкоренное число > 1, то корень будет меньше d
//        if (d1.compareTo(new NumberR(1)) == 1) {
//            A = new Complex(1);
//            B = d;
//        } // если подкоренное число < 1, то корень будет больше d
//        else {
//            A = d;
//            B = new Complex(1);
//        }
////подразумевается,что мы  рассматриваем многочлен  x^2-d=0
////вычисляем значение ф-ции в точке A
//        Complex Alpha = A.multiply(A).subtract(d);
////вычисляем значение ф-ции в точке B
//        Complex Betta = B.multiply(B).subtract(d);
////объявляем переменную - приближенный корень
//        Complex C = null;
////вычисляем значение ф-ции в точке, которая есть приближенный корень
//        Complex Gamma = null;
//// выполняется до тех пор, пока погрешность не будет меньше допустимой
//        do {
//            C = B.add(A).multiply(new Complex(0.5));
//            Gamma = C.multiply(C).subtract(d);
//            Complex Gamma2 = Gamma.multiply(Alpha);
//            NumberR Gamma1 = (NumberR) Gamma.re;
//            if (Gamma1.compareTo(new NumberR(0)) == 1) {
//                A = C;
//                Alpha = Gamma;
//            } else {
//                B = C;
//                Betta = Gamma;
//            }
////формула метода хорд
//
//            C = (Betta.multiply(A).subtract(Alpha.multiply(B)));
//            Complex C1 = Betta.subtract(Alpha);
//            C = funcNumberC.divide(C1, Notebook.SCALE * 2 + 2);
////вычисляем значение ф-ции в точке приближенного корня
//            Gamma = C.multiply(C).subtract(d);
////остановка метода
//            if (Gamma1.compareTo(new NumberR(0)) == 1) {
//                A = C;
//                Alpha = Gamma;
//            } else {
//                B = C;
//                Betta = Gamma;
//            }
//
//        } while (Gamma.re.compareTo(BGG) == 1);
//        return //отсекание лишнего хвоста
//                funcNumberC.setScale(C,Notebook.SCALE, RoundingMode.HALF_UP);
//    }
//
//    /* Вычисление тригонометрических функций sin(x), cos(x), tg(x), ctg(x)
//    для  Complex */
//    /**
//     * функция вычисления синуса на отрезке [0,2pi] с помощью разложения в ряд Тейлора
//     *Sin(x)  = x-x^3/3!+ x^5/5!- x^7/7!+…+(-1)^n+1*x^2n+1/(2n+1)!
//     * @param  x Complex аргумент
//     * @param  Notebook.SCALE Integer заданное количество цифр после запятой
//     * @return Sin(x) Complex результат вычисления
//     */
//    public static Complex sin1(Complex x) {
//        NumberR n = new NumberR(2);
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        Complex y = new Complex(1, 0);                    //задаем первый член ряда
//        Complex f = new Complex(0, 0);                    //задаём начальное значение суммы ряда
//        int sign = -1;                                         //знак
//        Complex z = x;
//        Complex step = funcNumberC.multiply(z, z, mc);                    //задаём начальное значение степени
//        y = funcNumberC.multiply(y, z, mc);
//        f = funcNumberC.add(f, y, mc);
//        //NumberR fak=NumberR.ONE;
//        NumberR two = new NumberR(2);
//        //формируем необходимую точность
//        NumberR eps1 = new NumberR(0.1);
//        eps1 = eps1.pow(Notebook.SCALE);
//        NumberR eps2 = funcNumberC.pow1(new Complex(eps1),2, mc);
//        //разложение в ряд x-x^3/3!+x^5/5!-...
//        while ((eps2.compareTo(y.re) == -1) || (eps2.compareTo(y.im) == -1)) //пока y>eps2
//        {
//            NumberR zn = n.multiply(n.add(NumberR.ONE, mc), mc);
//            //Complex fakt=new Complex(fak,NumberR.ZERO);//задаём начальное значение факториала
//            y = funcNumberC.multiply(y, funcNumberC.divide(step, zn, mc), mc);                           //вычисляем один член ряда
//            //System.out.println("y = "+y+"  "+f);
//            if (sign > 0) {                                      //проверка знака члена ряда
//                f = funcNumberC.add(f, y, mc);
//            } //накапливаем сумму (возможны два варианта в зависимости от знака)
//            else {
//                f = funcNumberC.subtract(f, y, mc);
//            }
//            //fak=fak.multiply(n.add(NumberR.ONE),mc).multiply(n.add(two),mc);//формируем факториала
//            sign = -sign;                                     //смена знака
//            //step=step.multiply(z,mc).multiply(z,mc);          //формируем степень
//            n = n.add(two, mc);
//        }
//        return f;
//    }
//
//    /**
//     * Функция вычисления синуса на всей числовой оси
//     * @param  x Complex аргумент
//     * @param  Notebook.SCALE Integer заданное количество цифр после запятой
//     * @return Sin(x) Complex результат вычисления
//     */
//    public static Complex sin(Complex x) {
//        //int p = pog(x,Notebook.SCALE);
//        //int eps = Notebook.SCALE+pog(x,Notebook.SCALE);
//        int t = pogresh(x);
//        int p = 0;
//        if (t < 7) {
//            p = 1;
//        } else {
//            p = t - 5;
//        }
//        Notebook.SCALE = Notebook.SCALE + p;
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        NumberR[] pi_ = FuncNumberR.pi_2pi_pi_div2();
//        NumberR pi = pi_[0];
//        NumberR pi2 = pi_[1];
//        NumberR pi_2 = pi_[2];
//        NumberR ReZ;                                          //действительная часть числа z
//        NumberR ImZ;                                          //мнимая часть числа z
//        ReZ = (NumberR) x.re;
//        ImZ = (NumberR) x.im;
//        boolean flagS = false;                                     //флаг для определения знака sin
//        if (ReZ.compareTo(NumberR.ZERO) == -1) {                 //сравниваем z c 0
//            ReZ = (NumberR) x.re.abs();                                        //избавляемся от отрицательных в действительной части
//            flagS = true;
//        }
//        while ((ReZ.compareTo(pi2) == 1) | (ReZ.compareTo(pi2) == 0)) { //сравниваем z c 2*pi
//            ReZ = ReZ.subtract(pi2);                                 //избавляемся от периода в действительной части
//        }
//        Complex s = new Complex(NumberR.ZERO, NumberR.ZERO);
//        int k = (ReZ.divide(pi_2, 1, 1)).intValue();                 //определяем в какой четверти действительная часть
//        //работают формулы приведения
//        Complex z = new Complex(ReZ, ImZ);
//        switch (k) {
//            case 0: {                                            //первая четверть
//                s = sin1(z);
//                if (flagS) {
//                    s = s.negate();
//                }                         //если x<0
//                break;
//            }
//            case 1: {                                            //вторая четверть
//                z.re = z.re.subtract(pi_2);
//                s = cos1(z);
//                if (flagS) {
//                    s = s.negate();
//                }                         //если x<0
//                break;
//            }
//            case 2: {                                            //третья четверть
//                z.re = z.re.subtract(pi);
//                s = sin1(z).negate();
//                if (flagS) {
//                    s = s.negate();
//                }                         //если x<0
//                break;
//            }
//            case 3: {                                            //четвертая четверть
//                z.re = z.re.subtract(pi).subtract(pi_2);
//                s = cos1(z).negate();
//                if (flagS) {
//                    s = s.negate();
//                }                         //если x<0
//                break;
//            }
//        }
//        return s;
//    }
//
//    /**Функция вычисления косинуса на отрезке [0,2?] с помощью разложения в ряд Тейлора
//     * Cos(x) = 1-x2/2!+ x4/4!- x6/6!+…+(-1)n*x2n/(2n)!
//     * @param x Complex аргумент
//     * @param  Notebook.SCALE Integer заданное количество цифр после запятой
//     * @return Cos(x) Complex результат вычисления
//     */
//    public static Complex cos1(Complex x) {
//        NumberR n = new NumberR(1);
//        Complex y = new Complex(1, 0);                                //задаём первый член ряда
//        Complex f = new Complex(1, 0);                                //задаём начальное значение суммы ряда
//        int sign = -1;                                                     //знак
//        //NumberR fak=NumberR.ONE;
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        Complex zc = x;
//        NumberR two = new NumberR(2);
//        Complex step = funcNumberC.multiply(zc, zc, mc);                               //задаём начальное значение степени
//        //формируем необходимую точность
//        NumberR eps1 = new NumberR(0.1);
//        eps1 = eps1.pow(Notebook.SCALE);
//        NumberR eps2 = funcNumberC.pow1(new Complex(eps1),2, mc);
//        //разложение в ряд 1-x^2/2!+x^4/4!-...
//        while ((eps2.compareTo(y.re) == -1) || (eps2.compareTo(y.im) == -1)) //пока y>eps2
//        {
//            NumberR zn = n.multiply(n.add(NumberR.ONE, mc), mc);
//            //fak=fak.multiply(n.subtract(NumberR.ONE),mc).multiply(n,mc);//задаём начальное значение факториала
//            //Complex fakt=new Complex(fak,NumberR.ZERO);
//            y = funcNumberC.multiply(y, funcNumberC.divide(step, zn, mc), mc);                                        //вычисляем один член ряда
//            if (sign > 0) {                                                   //проверка знака члена ряда
//                f = funcNumberC.add(f, y, mc);
//            } //накапливаем сумму (возможны два варианта в зависимости от знака
//            else {
//                f = funcNumberC.subtract(f, y, mc);
//            }
//            sign = -sign;                                                //смена знака
//            //step=step.multiply(zc,mc).multiply(zc,mc);                     //формируем степень
//            n = n.add(two);
//        }
//        return f;
//    }
//
//    /**функция вычисления косинуса на всей числовой оси
//     * @param x Complex аргумент
//     * @param  Notebook.SCALE Integer заданное количество цифр после запятой
//     * @return Cos(x) Complex результат вычислений
//     */
//    public static Complex cos(Complex x) {
//        int t = pogresh(x);
//        int p;
//        if (t < 7) {
//            p = 1;
//        } else {
//            p = t - 6;
//        }
//        Notebook.SCALE = Notebook.SCALE + p;
//        System.out.println(Notebook.SCALE);
//        MathContext mc = INITmathContext(Notebook.SCALE);
//
//        NumberR[] pi_ = FuncNumberR.pi_2pi_pi_div2();
//        NumberR pi = pi_[0];
//        NumberR pi2 = pi_[1];
//        NumberR pi_2 = pi_[2];                                    //вычисляем pi
//        NumberR ReA = (NumberR) x.re;
//        NumberR ImA = (NumberR) x.im;
//        if (ReA.compareTo(NumberR.ZERO) == -1) {                    //сравниваем a c 0
//            ReA = (NumberR) x.re.abs();                                          //избавляемся от отрицательных в действительной части
//        }
//        while ((ReA.compareTo(pi2) == 1) | (ReA.compareTo(pi2) == 0)) {
//            ReA = ReA.subtract(pi2);                                   //избавляемся от периода в действительной части
//        }
//        Complex c = new Complex(NumberR.ZERO, NumberR.ZERO);
//        int k1 = (ReA.divide(pi_2, 1, 1)).intValue();                  //определяем в какой четверти действительная часть
//        //работают формулы приведения
//        Complex a = new Complex(ReA, ImA);
//        switch (k1) {
//            case 0: {                                              //первая четверть
//                c = cos1(a);
//                break;
//            }
//            case 1: {                                              //вторая четверть
//                a.re = a.re.subtract(pi_2);
//                c = sin1(a).negate();
//                break;
//            }
//            case 2: {                                              //третья четверть
//                a.re = a.re.subtract(pi);
//                c = cos1(a).negate();
//                break;
//            }
//            case 3: {                                              //четвертая четверть
//                a.re = a.re.subtract(pi).subtract(pi_2);
//                c = sin1(a);
//                break;
//            }
//        }
//
//        return c;
//    }
//
//    /**
//     *функция вычисления тангенса
//     * @param x Complex аргумент
//     * @param  Notebook.SCALE Integer заданное количество цифр после запятой
//     * @return tan(x) Complex результат вычислений
//     */
//    public static Complex tg(Complex x) {
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        Complex t = funcNumberC.divide(sin(x), cos(x), mc);
//        return t;
//    }
//
//    /**
//     *функция вычисления котангенса
//     * @param x Complex аргумент
//     * @param  Notebook.SCALE Integer заданное количество цифр после запятой
//     * @return ctg(x) Complex результат вычислений
//     */
//    public static Complex ctg(Complex x) {
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        Complex ct = funcNumberC.divide(cos(x), sin(x), mc);
//        return ct;
//    }
//
//    /**
//    Вычисление обратных тригонометрических функций
//     */
//    /** Вычисление арктангенса
//     * Метод вычисляет значение арктангенса с помощью ряда Тейлора
//     * при -1<х<1 : x-x^3/3+x^5/5-x^7/7+....
//     * при x>1 и x<-1 : П/2-(1/x-1/3*x^3+1/5*x^5-1/7*x^7+.....)
//     * @param  Notebook.SCALE Integer заданное количество цифр после запятой
//     * @param  x Complex аргумент
//     * @return ArcTan(x) Complex результат вычислений
//     */
//    public static Complex arctn(Complex x) {
//        int t = pogresh(x);
//        int pog;
//        //вычисление погрешности  с учетом количества символов несовпадения,
//        //на которую необходимо увеличить исходную точность, чтобы количество
//        //верных цифр в числе совпадало с исходной точностью.
//
//        if (t > 1) {
//            pog = 0;
//        } else {
//            pog = -t + 2;
//        }
//        Notebook.SCALE = Notebook.SCALE + pog;
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        NumberR[] pi_ = FuncNumberR.pi_2pi_pi_div2();
//        NumberR pi = pi_[0];
//        NumberR pi2 = pi_[1];
//        NumberR pi_2 = pi_[2];
//        Complex F = new Complex(0);
//        Complex k = new Complex(3);
//        Complex k1 = new Complex(1);
//        Complex eps = new Complex(1.0E-100);             //точность
//        Complex ch = new Complex(0);                    //член ряда
//        Complex odin = new Complex(1);
//        Complex odinv = new Complex(-1);
//        Complex dva = new Complex(2);
//        Complex r = new Complex(0.5);
//        Complex rm = new Complex(-0.5);
//        Complex q = new Complex(1.5);
//        Complex t1 = new Complex(0);
//        Complex y = new Complex(0);
//        Complex t2 = new Complex(1);
//        Complex w = new Complex(1);
//        Complex x2 = new Complex(0);
//        if (funcNumberC.abs(x, mc).compareTo(r) == -1) //если x < 0.5
//        {
//            x2 = funcNumberC.multiply(x, x, mc);
//            ch = funcNumberC.add(ch, x, mc);                                        //первый член ряда
//            while (funcNumberC.abs(ch, mc).compareTo(eps) == 1) //пока ch>eps
//            {
//                F = funcNumberC.add(F, ch, mc);
//                Complex y1 = funcNumberC.multiply(x2, odinv, mc);
//                Complex y2 = funcNumberC.multiply(y1, k1, mc);
//                ch = funcNumberC.multiply(ch, y2, mc);
//                ch = funcNumberC.divide(ch, k, mc);                         //общая формула ряда arctg(x)  x-x^3/3+x^5/5-x^7/7+....
//                k1 = funcNumberC.add(k1, dva, mc);
//                k = funcNumberC.add(k, dva, mc);
//            }
//        } else if (((x.compareTo(r) == 1) || (x.compareTo(r) == 0)) & (x.compareTo(odin) == -1)) //  если 0,5 <= x < 1
//        {
//            y = funcNumberC.add(y, x, mc);
//            w = funcNumberC.multiply(y, y, mc);
//            w = funcNumberC.subtract(odin, w, mc);
//            Complex xx = funcNumberC.multiply(dva, y, mc);
//            x = funcNumberC.divide(xx, w, mc);
//            x2 = funcNumberC.multiply(x, x, mc);
//            ch = funcNumberC.divide(odin, x, mc);                        //первый член ряда
//            while (funcNumberC.abs(ch, mc).compareTo(eps) == 1) //пока ch>eps
//            {
//                F = funcNumberC.add(F, ch, mc);
//                Complex y1 = funcNumberC.multiply(ch, k1, mc);
//                Complex y11 = funcNumberC.multiply(x2, odinv, mc);
//                Complex y2 = funcNumberC.multiply(y11, k, mc);
//                ch = funcNumberC.divide(y1, y2, mc);                         //общая формула ряда arctg(x)(1/x-1/3*x^3+1/5*x^5-1/7*x^7+.....)
//                k1 = funcNumberC.add(k1, dva, mc);
//                k = funcNumberC.add(k, dva, mc);
//            }
//            Complex PI = new Complex(pi, NumberR.ZERO);
//            F = funcNumberC.subtract(PI, F, mc);     // П/2-(1/x-1/3*x^3+1/5*x^5-1/7*x^7+.....)
//            F = funcNumberC.divide(F, dva, mc);
//        } else if (((x.compareTo(odin) == 1) || (x.compareTo(odin) == 0)) & (funcNumberC.abs(x, mc).compareTo(q) == -1)) //если 1 < x < 1.5
//        {
//            y = y.add(x);
//            t1 = funcNumberC.multiply(y, y, mc);
//            t2 = sqrt(odin.add(t1));
//            w = funcNumberC.add(odin.negate(), t2, mc);
//            x = funcNumberC.divide(w, y, mc);
//            ch = ch.add(x);                                        //первый член ряда
//            x2 = funcNumberC.multiply(x, x, mc);
//            while (funcNumberC.abs(ch, mc).compareTo(eps) == 1) //пока ch >eps
//            {
//                F = funcNumberC.add(F, ch, mc);
//                Complex y11 = funcNumberC.multiply(ch, x2, mc);
//                Complex y111 = funcNumberC.multiply(y11, k1, mc);
//                Complex y1 = y111.multiply(odin.negate());
//                ch = funcNumberC.divide(y1, k, mc);                         //общая формула ряда arctg(x)  x-x^3/3+x^5/5-x^7/7+....
//                k1 = funcNumberC.add(k1, dva, mc);
//                k = funcNumberC.add(k, dva, mc);
//            }
//            F = funcNumberC.multiply(F, dva, mc);
//        } else if ((x.compareTo(q) == 1) || (x.compareTo(q) == 0)) //если x >= 1.5
//        {
//            ch = funcNumberC.divide(odin, x, mc);                        //первый член ряда
//            x2 = funcNumberC.multiply(x, x, mc);
//            while (funcNumberC.abs(ch, mc).compareTo(eps) == 1) //пока ch>eps
//            {
//                F = funcNumberC.add(F, ch, mc);
//                Complex y1 = funcNumberC.multiply(ch, k1, mc);
//                Complex y2 = x2.multiply(odin.negate()).multiply(k);
//                ch = funcNumberC.divide(y1, y2, mc);                         //общая формула ряда arctg(x)(1/x-1/3*x^3+1/5*x^5-1/7*x^7+.....)
//                k1 = funcNumberC.add(k1, dva, mc);
//                k = funcNumberC.add(k, dva, mc);
//            }
//            Complex PI = new Complex(pi_2, NumberR.ZERO);
//            F = funcNumberC.subtract(PI, F, mc);     // П/2-(1/x-1/3*x^3+1/5*x^5-1/7*x^7+.....)
//        } else if (((x.compareTo(rm) == -1) || (x.compareTo(rm) == 0)) & (x.compareTo(odinv) == 1)) //если 0,5 <= x <= 1
//        {
//            x = x.negate();
//            y = funcNumberC.add(y, x, mc);
//            w = funcNumberC.multiply(y, y, mc);
//            w = funcNumberC.subtract(odin, w, mc);
//            Complex xx = funcNumberC.multiply(dva, y, mc);
//            x = funcNumberC.divide(xx, w, mc);
//            x2 = funcNumberC.multiply(x, x, mc);
//            ch = funcNumberC.divide(odin, x, mc);                        //первый член ряда
//            while (funcNumberC.abs(ch, mc).compareTo(eps) == 1) //пока ch>eps
//            {
//                F = funcNumberC.add(F, ch, mc);
//                Complex y1 = funcNumberC.multiply(ch, k1, mc);
//                Complex y11 = funcNumberC.multiply(x2, odinv, mc);
//                Complex y2 = funcNumberC.multiply(y11, k, mc);
//                ch = funcNumberC.divide(y1, y2, mc);                         //общая формула ряда arctg(x)(1/x-1/3*x^3+1/5*x^5-1/7*x^7+.....)
//                k1 = funcNumberC.add(k1, dva, mc);
//                k = funcNumberC.add(k, dva, mc);
//            }
//            Complex PI = new Complex(pi_2, NumberR.ZERO);
//            F = funcNumberC.subtract(PI, F, mc);     // П/2-(1/x-1/3*x^3+1/5*x^5-1/7*x^7+.....)
//            F = funcNumberC.divide(F, dva, mc);
//            F = F.negate();
//        } else //если x < -1.5
//        if (((x.compareTo(odinv) == 1) || (x.compareTo(odinv) == 0)) & (funcNumberC.abs(x, mc).compareTo(q) == -1)) //пїЅпїЅпїЅпїЅ 1 < x < 1.5
//        {
//            x = x.negate();
//            y = y.add(x);
//            t1 = funcNumberC.multiply(y, y, mc);
//            t2 = sqrt(odin.add(t1));
//            w = funcNumberC.add(odin.negate(), t2, mc);
//            x = funcNumberC.divide(w, y, mc);
//            ch = ch.add(x);                                        //gthdsq член ряда
//            Complex x22 = funcNumberC.multiply(x2, odinv, mc);
//            x2 = funcNumberC.multiply(x22, x, mc);
//            while (funcNumberC.abs(ch, mc).compareTo(eps) == 1) //пока ch>eps
//            {
//                F = funcNumberC.add(F, ch, mc);
//                Complex y11 = funcNumberC.multiply(ch, x2, mc);
//                Complex y1 = funcNumberC.multiply(y11, k1, mc).multiply(odin.negate());
//                ch = funcNumberC.divide(y1, k, mc);                         //общая формула ряда arctg(x)  x-x^3/3+x^5/5-x^7/7+....
//                k1 = funcNumberC.add(k1, dva, mc);
//                k = funcNumberC.add(k, dva, mc);
//            }
//            F = funcNumberC.multiply(F, dva, mc);
//            F = F.negate();
//        } else {
//            x = x.negate();                                 // если х отрицательный
//            ch = funcNumberC.divide(odin, x, mc);                      //первый член ряда
//            x2 = funcNumberC.multiply(x, x, mc);
//            while (funcNumberC.abs(ch, mc).compareTo(eps) == 1) //пока ch>eps
//            {
//                F = funcNumberC.add(F, ch, mc);
//                Complex y1 = funcNumberC.multiply(ch, k1, mc);
//                Complex y2 = x2.multiply(odin.negate()).multiply(k);
//                ch = funcNumberC.divide(y1, y2, mc);                       //общая формула ряда arctg(x)(1/x-1/3*x^3+1/5*x^5-1/7*x^7+.....)
//                k1 = funcNumberC.add(k1, dva, mc);
//                k = funcNumberC.add(k, dva, mc);
//            }
//            Complex PI = new Complex(pi_2, NumberR.ZERO);
//            F = funcNumberC.subtract(PI, F, mc);                      // П/2-(1/x-1/3*x^3+1/5*x^5-1/7*x^7+.....)
//            F = F.negate();                               //функции приваиваем противоположное значение
//        }
//        return F;
//    }
//
//    public static MathContext INITmathContext(int N) {
//        return new MathContext(N, RoundingMode.HALF_EVEN);
//    }
//
//    /** Вычисление арксинуса
//     *  Метод вычисляет значение арксинуса с помощью арктангенса
//     * arcsin(х)=arctn(x/(sqrt(1-x^2))
//     * @param  Notebook.SCALE Integer заданное количество цифр после запятой
//     * @param  x Complex аргумент
//     * @return ArcSin(x) Complex результат вычислений
//     */
//    public static Complex arcsn(Complex x) {
//        Complex x1 = new Complex(0);
//        Complex arcsin = new Complex(0);
//        Complex odin = new Complex(1);
//        Complex odinv = new Complex(-1);
//        NumberR dva = new NumberR(2);
//        int t = pogresh(x);
//        int pog;
//        //вычисление погрешности  с учетом количества символов несовпадения,
//        //на которую необходимо увеличить исходную точность, чтобы количество
//        //верных цифр в числе совпадало с исходной точностью.
//
//        if (t > 1) {
//            pog = 0;
//        } else {
//            pog = t - 1;
//        }
//        Notebook.SCALE = Notebook.SCALE + pog;
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        if (x.compareTo(odin) == 0) {                         //если х=1
//
//            arcsin = arcsin.add(PI_2());
//            System.out.println("arcsin(" + x + ")=" + arcsin);
//        } else if (x.compareTo(odinv) == 0) {                      //если х=-1
//            //   Complex PI=pi(Notebook.SCALE).divide(dva,mc);
//            arcsin = arcsin.add(PI_2());
//            arcsin = arcsin.negate();
//            System.out.println("arcsin(" + x + ")=" + arcsin);
//        } else if ((funcNumberC.abs(x, mc).compareTo(odin) == -1) || (funcNumberC.abs(x, mc).compareTo(odin) == 0)) //если -1<x<1
//        {
//            Complex x11 = funcNumberC.multiply(x, x, mc).negate();
//            Complex x111 = funcNumberC.add(x11, odin, mc);
//            x1 = funcNumberC.divide(x, sqrt(x111), mc);       //аргумент арктангенса
//            arcsin = arctn(x1);
//            System.out.println("arcsin(" + x + ")=" + arcsin);
//        } else {
//            System.out.println("arcsin(" + x + ") пїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ");
//        }
//
//        return arcsin;
//    }
//
//    /** Вычисление арккотангенса
//     * Метод вычисляет значение арккотангенса с помощью арктангенса
//     * arcctn(х)=П/2-arctn(x)
//     * @param  Notebook.SCALE Integer заданное количество цифр после запятой
//     * @param  x Complex аргумент
//     * @return ArcCtn(x) Complex результат вычислений
//     */
//    public static Complex arcctn(Complex x) {
//        // NumberR dva=new NumberR("2");
//        Complex F = new Complex(0);                //задаём начальное значение суммы
//        int t = pogresh(x);
//        int pog;
//        //вычисление погрешности  с учетом количества символов несовпадения,
//        //на которую необходимо увеличить исходную точность, чтобы количество
//        //верных цифр в числе совпадало с исходной точностью.
//
//        if (t > 1) {
//            pog = 0;
//        } else {
//            pog = t + 1;
//        }
//        Notebook.SCALE = Notebook.SCALE + pog;
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        // Complex PI=pi(Notebook.SCALE).divide(dva,mc);
//        F = funcNumberC.subtract(PI_2(), arctn(x), mc);
//        return F;
//    }
//
//    /** Вычисление арккосинуса
//     *  Метод вычисляет значение арккосинуса с помощью арккотангенса
//     * arccos(х)=arcctn(x/(sqrt(1-x^2))
//     * @param  Notebook.SCALE Integer заданное количество цифр после запятой
//     * @param  x Complex аргумент
//     * @return ArcCos(x) Complex результат вычислений
//     */
//    public static Complex arccs(Complex x) {
//        Complex x1 = new Complex(0);                 //аргумент арккотангенса
//        Complex arccos = new Complex(0);
//        Complex odin = new Complex(1);
//        Complex v = new Complex(0);
//        int t = pogresh(x);
//        int pog;
//        //вычисление погрешности  с учетом количества символов несовпадения,
//        //на которую необходимо увеличить исходную точность, чтобы количество
//        //верных цифр в числе совпадало с исходной точностью.
//
//        if (t > 1) {
//            pog = 0;
//        } else {
//            pog = -2 / 3 * t;
//        }
//        Notebook.SCALE = Notebook.SCALE + pog;
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        if (funcNumberC.abs(x, mc).compareTo(odin) == 0) {               //если /х/=1
//            arccos = arccos.add(v);
//        } else if ((funcNumberC.abs(x, mc).compareTo(odin) == -1) || (funcNumberC.abs(x, mc).compareTo(odin) == 0)) // если -1<x<1
//        {
//            Complex x11 = funcNumberC.multiply(x, x, mc).negate();
//            Complex x111 = funcNumberC.add(x11, odin, mc);
//            x1 = funcNumberC.divide(x, sqrt(x111), mc);
//            arccos = arcctn(x1);
//        } else {
//            System.out.println("arccos(" + x + ") пїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ");
//        }
//        return arccos;
//    }
//
//    /**
//     *Вычисление значения гиперболических функций sh(x), ch(x), th(x), cth(x)
//     *для Complex
//     */
//    /**
//     *Функция для вычисления e^x с помощью разложения в ряд:
//     *1 + x/1! + x^2/2! + x^3/3! + ...
//     *
//     *@param x Complex - аргумент искомой функции
//     *@param eps Integer - заданная погрешность
//     *@return f Complex - результат вычисления
//     */
//    public static Complex exp1(Complex x) {
//        MathContext mc = INITmathContext(Notebook.SCALE);//MathContect
//        int n = 1;
//        Complex f = new Complex(1);//задаем начальное значение суммы ряда
//        Complex y = new Complex(1);//задаем начальное значение члена ряда
//        //формируем необходимую точность
//        Complex eps1 = new Complex(0.1);
//        eps1 = funcNumberC.pow(Notebook.SCALE, Notebook.SCALE);
//        //пока заданная погрешность меньше члена ряда разложения накапливаем сумму
//        while (y.compareTo(eps1) == 1) {
//            //образующая формула члена ряда: y(i+1) = y(i-1) * x/n
//            Complex y1 = funcNumberC.multiply(y,x, mc);
//            Complex y2 = new Complex(n);
//            y = funcNumberC.divide(y1,y2, mc);
//            f = funcNumberC.add(f,y, mc);//накапление суммы
//            n++;
//        }
//        return f;
//    }
//
//    /**
//     *Функция для вычисления e^x. Выбор алгоритма вычисления на определенном
//     *интервале, которому принадлежит аргумент х.
//     *
//     *@param x Complex - аргумент искомой функции
//     *@param eps Integer - заданная погрешность
//     *@return f Complex - результат вычисления
//     */
//    public static Complex exp(Complex x) {
//        MathContext mc = INITmathContext(Notebook.SCALE);//MathContect
//        NumberR dv = new NumberR("2");//dv = 2
//        Complex g = new Complex(1);//задаем начальное значение промежуточной суммы ряда
//        Complex f = new Complex(1);//задаем начальное значение суммы ряда
//        if (NumberR.ZERO.compareTo(x) != 1) {
//            if (NumberR.ONE.compareTo(x) != -1) {
////вычисления e^x на отрезке [0;1]
//                f = exp1(x);
//            } else {
////вычисления e^x при x > 1 - в этом случае сводим задачу к уже решенной на отрезке [0;1]
//                //переводим x Complex в b BigInteger
//                NumberZ b = (NumberZ) x.toNumber(2);
//                //находим логарифм от х по основанию 2 через битовую длину b
//                int m = b.bitLength();
//                //преобразуем х в t, принадлежащее отрезку [0;1]
//                NumberR t1 = funcNumberC.pow1(m, Notebook.SCALE);
//                Complex t = funcNumberC.divide(x,t1, mc);
//                g = exp1(t);//вызов функции exp1(x, eps)
//                //получаем окончательное значение суммы
//                for (int i = 0; i < m; i++) {
//                    g = funcNumberC.multiply(g,g, mc);
//                }
//                f = g;
//            }
//        } else {
////вычисления e^x при x < 0; в этом случае e^(-x) = 1/(e^x)
//            g = exp1(x.negate());//вызов функции exp1(x, eps)
//            f = funcNumberC.divide(Complex.COMPLEX_ONE,g, mc);
//        }
//        return f;//возвращаем полученный результат
//    }
//
//    /**
//     *Функция для вычисления функции sh(x) - гиперболического синуса.
//     *Используется представление данной функции через e^x: (e^x - e^(-x))/2
//     *
//     *@param x Complex - аргумент искомой функции
//     *@param eps Integer - заданная погрешность
//     *@return y Complex - результат вычисления
//     */
//    public static Complex sh(Complex x) {
//        int t = pogresh(x);//вызов функции pogresh(x)
//        int pog;
//        //вычисление погрешности  с учетом количества символов несовпадения,
//        //на которую необходимо увеличить исходную точность, чтобы количество
//        //верных цифр в числе совпадало с исходной точностью.
//        if (t >= 1) {
//            pog = 2 * t + 2;
//        } else {
//            pog = 2;
//        }
//        //увеличиваем точность на величину погрешности
//        MathContext mc = INITmathContext(Notebook.SCALE + pog);//MathContect
//        Complex px = exp(x);//вызов функции exp(x,eps)
//        Complex ox = exp(x.negate());//вызов функции exp(x,eps)
//        //вычисление сth(x) по формуле (e^x - e^(-x))/2
//        Complex y1 = funcNumberC.subtract(px,ox, mc);
//        Complex y2 = new Complex(2);
//        Complex y = funcNumberC.divide(y1,y2, mc);
//        return y;//возвращаем полученный результат
//    }
//
//    /**
//     *Функция для вычисления функции ch(x) - гиперболического косинуса.
//     *Используется представление данной функции через e^x: (e^x + e^(-x))/2
//     *
//     *@param x Complex - аргумент искомой функции
//     *@param eps Integer - заданная погрешность
//     *@return y Complex - результат вычисления
//     */
//    public static Complex ch(Complex x) {
//        int t = pogresh(x);//вызов функции pogresh(x)
//        int pog;
//        //вычисление погрешности  с учетом количества символов несовпадения,
//        //на которую необходимо увеличить исходную точность, чтобы количество
//        //верных цифр в числе совпадало с исходной точностью.
//        if (t >= -1) {
//            pog = t + 4;
//        } else {
//            pog = 0;
//        }
//        //увеличиваем точность на величину погрешности
//        MathContext mc = INITmathContext(Notebook.SCALE + pog);//MathContect
//        Complex px = exp(x);//вызов функции exp(x,eps)
//        Complex ox = exp(x.negate());//вызов функции exp(x,eps)
//        //вычисление сth(x) по формуле (e^x + e^(-x))/2
//        Complex y1 = funcNumberC.add(px,ox, mc);
//        Complex y2 = new Complex(2);
//        Complex y = funcNumberC.divide(y1,y2, mc);
//        return y;//возвращаем полученный результат
//    }
//
//    /**
//     *Функция для вычисления функции th(x) - гиперболического тангенса.
//     *Используется представление данной функции через e^x:
//     *(e^x - e^(-x))/(e^x + e^(-x))
//     *
//     *@param x Complex - аргумент искомой функции
//     *@param eps Integer - заданная погрешность
//     *@return y Complex - результат вычисления
//     */
//    public static Complex th(Complex x) {
//        int t = pogresh(x);//вызов функции pogresh(x)
//        int pog;
//        //вычисление погрешности  с учетом количества символов несовпадения,
//        //на которую необходимо увеличить исходную точность, чтобы количество
//        //верных цифр в числе совпадало с исходной точностью.
//        if (t > 3) {
//            pog = 0;
//        } else {
//            pog = 2;
//        }
//        //увеличиваем точность на величину погрешности
//        MathContext mc = INITmathContext(Notebook.SCALE + pog);//MathContect
//        Complex px = exp(x);//вызов функции exp(x,eps)
//        Complex ox = exp(x.negate());//вызов функции exp(x,eps)
//        //вычисление сth(x) по формуле (e^x - e^(-x))/(e^x + e^(-x))
//        Complex y1 = funcNumberC.add(px,ox, mc);
//        Complex y2 = funcNumberC.subtract(px,ox, mc);
//        Complex y = funcNumberC.divide(y2,y1, mc);
//        return y;//возвращаем полученный результат
//    }
//
//    /**
//     *Функция для вычисления функции cth(x) - гиперболического котангенса.
//     *Используется представление данной функции через e^x:
//     *(e^x + e^(-x))/(e^x - e^(-x))
//     *
//     *@param x Complex - аргумент искомой функции
//     *@param eps Integer - заданная погрешность
//     *@return y Complex - результат вычисления
//     */
//    public static Complex cth(Complex x) {
//        int t = pogresh(x);//вызов функции pogresh(x)
//        int pog;
//        //вычисление погрешности  с учетом количества символов несовпадения,
//        //на которую необходимо увеличить исходную точность, чтобы количество
//        //верных цифр в числе совпадало с исходной точностью.
//        if (t > 1) {
//            pog = 0;
//        } else {
//            pog = -t + 2;
//        }
//        //увеличиваем точность на величину погрешности
//        MathContext mc = INITmathContext(Notebook.SCALE + pog);//MathContect
//        Complex px = exp(x);//вызов функции exp(x,eps)
//        Complex ox = exp(x.negate());//вызов функции exp(x,eps)
//        //вычисление сth(x) по формуле (e^x + e^(-x))/(e^x - e^(-x))
//        Complex y1 = funcNumberC.add(px,ox, mc);
//        Complex y2 = funcNumberC.subtract(px,ox, mc);
//        Complex y = funcNumberC.divide(y1,y2, mc);
//        return y;//возвращаем полученный результат
//    }
//
//    /* Вычисление обратно гиперболических функций arsh1(x), arch1(x), arth1(x), arcth1(x) */
//
//    /*  Процедура вычисления натурального логарифма на отрезке (0, 1.5)
//     * Вычисление происходит путем разложения логарифма в точке х=1
//     * в степенной ряд  (х-1) - (х-1)^2/2 + (x-1)^3/3 - ...
//     * и почленного суммирования полученного ряда
//     * @param x Complex - аргумент функции
//     * @param Notebook.SCALE int - точность вычисления
//     * @return F Complex результат вычисления
//     */
//    public static Complex ln2(Complex x) {
//        Complex k1 = new Complex(1);    //инициализация новых переменных
//        Complex k = new Complex(2);
//        Complex a0 = new Complex(0);
//        Complex a1 = new Complex(1.6);
//        Complex a2 = new Complex(3);
//        Complex ch = new Complex(0);
//        Complex s = new Complex(0);
//        Complex s1 = new Complex(0);
//        Complex err = new Complex(0);
//        Complex F = new Complex(0);
//        Complex odi = new Complex(-1);
//        Complex od = new Complex(1);
//        Complex dv = new Complex(2);
//        Complex zn = new Complex(1);
//        Complex L = new Complex(0);
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        Complex e = new Complex(0.1);
//        Complex eps = funcNumberC.pow(e,Notebook.SCALE, mc);
//        s = funcNumberC.add(s,x, mc);
//        s = funcNumberC.subtract(s,od, mc);
//        ch = funcNumberC.add(ch,s, mc);                    //задаем начальное значение члена ряда
//        err = funcNumberC.abs(ch,mc);
//        while (err.compareTo(eps) == 1) {      //Пока член ряда больше заданной точности, накапливаем сумму
//            F = funcNumberC.add(F,ch, mc);                 //накапливаем сумму
//            ch = funcNumberC.multiply(ch,odi, mc);         //вычисляем очередной член ряда
//            ch = funcNumberC.multiply(ch,s, mc);
//            ch = ch.multiply(k1);
//            ch = funcNumberC.divide(ch,k, mc);
//            err = funcNumberC.abs(ch,mc);
//            k = funcNumberC.add(k,od, mc);
//            k1 = funcNumberC.add(k1,od, mc);
//        }
//        return F;                                   //возвращаем полученный результат
//    }
//
//    /*  Процедура вычисления натурального логарифма на интервале (0, 3)
//     * с помощью разложения в точке х=2 в ряд
//     * Ln(2) + (х-2)/2 - ((х-2)^2)/(2*2^2) + ((х-2)^3)/(3*2^3) - ...
//     * на интервале [1.5, 3]  и почленного суммирования полученного ряда
//     * или с помощью процедуры ln(x) на интервале (0, 1.5)
//     *
//     * @param x Complex - аргумент функции
//     * @param Notebook.SCALE int - точность вычисления
//     * @return F Complex результат вычисления
//     */
//    public static Complex ln1(Complex x) {
//        Complex a0 = new Complex(0);    //инициализация новых переменных
//        Complex a1 = new Complex(1.6);
//        Complex a2 = new Complex(3);
//        Complex ch = new Complex(0);
//        Complex s = new Complex(0);
//        Complex s1 = new Complex(0);
//        Complex koff = new Complex(2);
//        Complex F = new Complex(0);
//        Complex odi = new Complex(-1);
//        Complex od = new Complex(1);
//        Complex dv = new Complex(2);
//        Complex zn = new Complex(1);
//        Complex L = new Complex(0);
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        Complex e = new Complex(0.1);
//        Complex eps = funcNumberC.pow(e,Notebook.SCALE, mc);//
//        //System.out.println(x);
//        if ((x.compareTo(a0) == 1) & ((x.compareTo(a1) == -1) | (x.compareTo(a1) == 0))) //при х из промежутка (0, 1.5) вычисляем Ln(x) с помощью процедуры ln2(x)
//        {
//            F = funcNumberC.add(F,ln2(x), mc);
//        }
//        if ((x.compareTo(a1) == 1) & ((x.compareTo(a2) == -1) | (x.compareTo(a2) == 0))) { //на интервале (1.5, 3) раскладываем Ln(x) в точке х=2 в ряд Ln(2) + (х-2)/2 - ((х-2)^2)/(2*2^2) + ((х-2)^3)/(3*2^3) - ...
//            Complex k = new Complex(2);
//            ch = funcNumberC.subtract(x,dv, mc);       //задаем начальное значение члена ряда
//            s = funcNumberC.add(s,ch, mc);
//            s1 = funcNumberC.add(s1,s, mc);
//            ch = funcNumberC.divide(ch,dv, mc);
//            int n = 1;
//            int n1 = 2;              //задаем значение первого члена ряда
//            while (funcNumberC.abs(ch,mc).compareTo(eps) == 1) { //пока член ряда больше заданной точности, накапливаем сумму
//                F = (zn.compareTo(a0) == 1) ? (funcNumberC.add(F,ch, mc)) : (funcNumberC.subtract(F,ch, mc));             //накапливаем сумму
//                s = funcNumberC.multiply(s,s1, mc);
//                zn = funcNumberC.multiply(zn,odi, mc);
//                ch = funcNumberC.divide(s,k, mc);   //вычисляем очередной член ряда
//                koff = funcNumberC.multiply(koff,dv, mc);
//                ch = funcNumberC.divide(ch,koff, mc);
//                k = funcNumberC.add(k,od, mc);
//                n++;
//                n1++;
//            }
//            L = funcNumberC.add(L,sqrt(dv), mc);  //вычисляем Ln(sqrt(2))
//            a0 = funcNumberC.add(a0,ln2(L), mc);
//            a0 = funcNumberC.multiply(a0,dv, mc);
//            F = funcNumberC.add(F,a0, mc);            //получаем окончательный результат
//        }
//        return F;                     //возвращаем полученный результат
//    }
//
//    /**
//     * Процедура вычисления натурального логарифма  на всей числовой оси
//     *
//     * @param x Complex - аргумент функции
//     * @param Notebook.SCALE int - точность вычисления
//     * @return F Complex результат вычисления
//     */
//    public static Complex ln(Complex x) {
//        Complex a0 = new Complex(0);    //задаем начальное значение пременных
//        Complex a2 = new Complex(3);
//        Complex F = new Complex(0);
//        Complex od = new Complex(1);
//        Complex dv = new Complex(2);
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        Complex y = new Complex(0);
//        Complex y1 = new Complex(0);
//        Complex y2 = new Complex(0);
//        Complex w = new Complex(0);
//        if ((x.compareTo(a0) == 1) & ((x.compareTo(a2) == -1) | (x.compareTo(a2) == 0)))//при х из промежутка (0, 3] вычисляем Ln(x) с помощью процедуры ln(x)
//        {
//            F = funcNumberC.add(F,ln1(x), mc);     //получаем окончательный результат
//        }
//        if (x.compareTo(a2) == 1) {       //при х>3 делаем замену  y=(x-1)/(x+1) и вычисляем Ln(x) по формуле Ln(x)=ln1(y+1)-ln1(1-y)
//            y1 = funcNumberC.subtract(x,od, mc);
//            y2 = funcNumberC.add(x,od, mc);
//            y = funcNumberC.divide(y1,y2, mc);
//            y1 = funcNumberC.add(y,od, mc);
//            y2 = funcNumberC.subtract(od,y, mc);
//            F = funcNumberC.subtract(ln1(y1),ln1(y2), mc);
//        }       //получаем окончательный результат
//        return F;                                              //возвращаем полученный результат
//    }
//
//    /**
//     * Процедура вычисления Arsh(x) с помощью процедуры вычисления Ln(x) по формуле Arsh(x)=Ln(x+sqrt(x^2+1))
//     *
//     * @param x Complex - аргумент функции
//     * @param Notebook.SCALE int - точность вычисления
//     */
//    public static Complex arsh(Complex x) {
//        int t = pogresh(x);//вызов функции pogresh(x)
//        int pog;
//        //вычисление погрешности  с учетом количества символов несовпадения,
//        //на которую необходимо увеличить исходную точность, чтобы количество
//        //верных цифр в числе совпадало с исходной точностью.
//        if (t >= -1) {
//            pog = t + 4;
//        } else {
//            pog = 2;
//        }
//        //увеличиваем точность на величину погрешности
//        Notebook.SCALE = Notebook.SCALE + pog;
//        Complex od = new Complex(1);        //задаем начальное значение пременных
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        Complex F = new Complex(0);
//        Complex y = new Complex(0);
//        Complex y1 = new Complex(1);
//        Complex y2 = new Complex(0);
//        Complex a1 = new Complex(0);
//        y1 = funcNumberC.multiply(y1,x, mc);          //вычисляем аргумент Ln(x) по формуле y=x+sqrt(x^2+1)
//        y1 = funcNumberC.multiply(y1,x, mc);
//        y1 = funcNumberC.add(y1,od, mc);
//        y2 = funcNumberC.add(y2,sqrt(y1), mc);
//        y = funcNumberC.add(x,y2, mc);
//        F = F.add(ln(y));  //вычисляем значение Arsh(x) с помощью процедуры ln(x)
//        if (x.compareTo(a1) == -1) {  //при отрицательном х меняем знак результата
//            F = F.negate();
//            System.out.println("arsh(" + x + ")= " + F); //выводим полученное значение на экран
//        } else {
//            System.out.println("arsh(" + x + ")=" + F); //выводим полученное значение на экран
//        }
//        return F;
//    }
//
//    /**
//     * Процедура вычисления Arch(x) с помощью процедуры вычисления Ln(x) по формуле Arch(x)=Ln(x+sqrt(x^2-1))
//     *
//     * @param x Complex - аргумент функции
//     * @param Notebook.SCALE int - точность вычисления
//     */
//    public static Complex arch(Complex x) {
//        int t = pogresh(x);//вызов функции pogresh(x)
//        int pog;
//        //вычисление погрешности  с учетом количества символов несовпадения,
//        //на которую необходимо увеличить исходную точность, чтобы количество
//        //верных цифр в числе совпадало с исходной точностью.
//        if (t >= 1) {
//            pog = t + 4;
//        } else {
//            pog = 0;
//        }
//        //увеличиваем точность на величину погрешности
//        Notebook.SCALE = Notebook.SCALE + pog;
//
//        Complex od = new Complex(1);       //задаем начальное значение пременных
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        Complex F = new Complex(0);
//        Complex y = new Complex(0);
//        Complex y1 = new Complex(1);
//        Complex y2 = new Complex(0);
//        Complex a1 = new Complex(1);
//        if (x.compareTo(a1) == -1) {
//            System.out.println("Недопустимое значение аргумента Arch(x)");  //при х<1 выводим сообщение о недопустимом значении аргумента Arch(x)
//        } else {                        // при х>=1 вычисляем значение Arsh(x) с помощью процедуры ln(x)
//            y1 = funcNumberC.multiply(y1,x, mc);     //вычисляем аргумент Ln(x) по формуле y=x+sqrt(x^2-1)
//            y1 = funcNumberC.multiply(y1,x, mc);
//            y1 = funcNumberC.subtract(y1,od, mc);
//            y2 = funcNumberC.add(y2,sqrt(y1), mc);
//            y = funcNumberC.add(y2,x, mc);
//            F = funcNumberC.add(F,ln(y), mc);
//            System.out.println("arch(" + x + ")=" + ln(y));  //выводим полученное значение на экран
//        }
//        return F;
//    }
//
//    /**
//     * Процедура вычисления Arth(x) с помощью процедуры вычисления Ln(x) по формуле Arth(x)=Ln((1+x)/(1-x))
//     *
//     * @param x NumberR - аргумент функции
//     * @param Notebook.SCALE int - точность вычисления
//     */
//    public static Complex arth(Complex x) {
//        int t = pogresh(x);//вызов функции pogresh(x)
//        int pog;
//        //вычисление погрешности  с учетом количества символов несовпадения,
//        //на которую необходимо увеличить исходную точность, чтобы количество
//        //верных цифр в числе совпадало с исходной точностью.
//        if (t >= 1) {
//            pog = 0;
//        } else {
//            pog = 3;
//        }
//        //увеличиваем точность на величину погрешности
//        Notebook.SCALE = Notebook.SCALE + pog;
//
//        Complex od = new Complex(1);   //задаем начальное значение пременных
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        Complex F = new Complex(0);
//        Complex y = new Complex(0);
//        Complex y1 = new Complex(0);
//        Complex a1 = new Complex(1);
//        Complex odi = new Complex(-1);
//        Complex dv = new Complex(2);
//        y = funcNumberC.add(od,x, mc);           //вычисляем аргумент Ln(x) по формуле y=(1+x)/(1-x)
//        y1 = funcNumberC.subtract(od,x, mc);
//        y = funcNumberC.divide(y,y1, mc);
//        F = funcNumberC.add(F,funcNumberC.divide(ln(y),dv, mc), mc);
//        if ((x.compareTo(a1) == -1) & (x.compareTo(odi) == 1))// при |х|<1 вычисляем значение Arth(x) с помощью процедуры ln(x)
//        {
//            System.out.println("arth(" + x + ")=" + funcNumberC.divide(ln(y),dv, mc));
//        } //выводим полученное значение на экран
//        else {
//            System.out.println("Недопустимое значение аргумента Arth(x)");
//        } //при |х|>=1  выводим сообщение о недопустимом значении аргумента Arth(x)
//        return F;
//    }
//
//    /**
//     * Процедура вычисления Arcth(x) с помощью процедуры вычисления Ln(x) по формуле Arcth(x)=Ln((1+x)/(x-1))
//     *
//     * @param x Complex - аргумент функции
//     * @param Notebook.SCALE int - точность вычисления
//     */
//    public static Complex arcth(Complex x) {
//        int t = pogresh(x);//вызов функции pogresh(x)
//        int pog;
//        //вычисление погрешности  с учетом количества символов несовпадения,
//        //на которую необходимо увеличить исходную точность, чтобы количество
//        //верных цифр в числе совпадало с исходной точностью.
//        if (t >= 1) {
//            pog = t + 4;
//        } else {
//            pog = 0;
//        }
//        //увеличиваем точность на величину погрешности
//        Notebook.SCALE = Notebook.SCALE + pog;
//        Complex od = new Complex(1);
//        Complex dv = new Complex(2);
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        Complex F = new Complex(0);
//        Complex y = new Complex(0);
//        Complex y1 = new Complex(0);
//        Complex a1 = new Complex(1);
//        Complex odi = new Complex(-1);
//        y = funcNumberC.add(x,od, mc);               //вычисляем аргумент Ln(x) по формуле y=(1+x)/(x-1)
//        y1 = funcNumberC.subtract(x,od, mc);
//        y = funcNumberC.divide(y,y1, mc);
//        F = funcNumberC.add(F,funcNumberC.divide(ln(y),dv, mc), mc);
//        if (((x.compareTo(od) == -1) & (x.compareTo(odi) == 1)) | (x.compareTo(odi) == 0) | (x.compareTo(od) == 0)) //при |х|<=1  выводим сообщение о недопустимом значении аргумента Arcth(x)
//        {
//            System.out.println("Недопустимое значение аргумента Arcth(x)");
//        } else {
//            System.out.println("arch(" + x + ")=" + (funcNumberC.divide(ln(y),dv, mc)));
//        } // при |х|<1 вычисляем значение Arcth(x) с помощью процедуры ln(x) и выводим его на экран
//
//        System.out.println("Ok");
//        return F;
//    }
//
//    public static Complex add(Complex c, Complex c1, MathContext mc) {
//        return new Complex(((NumberR)c.re).add(((NumberR)c1.re), mc), ((NumberR)c.im).add(((NumberR)c1.im), mc));
//    }
//
//    public static Complex subtract(Complex c, Complex c1, MathContext mc) {
//        return new Complex(((NumberR)c.re).subtract(((NumberR)c1.re), mc), ((NumberR)c.im).subtract(((NumberR)c1.im), mc));
//    }
//
//    public static Complex multiply(Complex c, Complex c1, MathContext mc) {
//        return new Complex(((NumberR)c.re).multiply(((NumberR)c1.re), mc).subtract(((NumberR)c.im).multiply(((NumberR)c1.im), mc)),
//                ((NumberR)c.im).multiply(((NumberR)c1.re), mc).add(((NumberR)c.re).multiply(((NumberR)c1.im), mc)));
//    }
//
//    public static Complex divide(Complex c, Complex c1, int SCALE, RoundingMode ROUNDING_MODE) {
//        NumberR d = ((NumberR)c1.re).multiply(((NumberR)c1.re)).add(((NumberR)c1.im).multiply(((NumberR)c1.im)));
//        return new Complex(((NumberR)c.re).multiply(((NumberR)c1.re)).add(((NumberR)c.im).multiply(((NumberR)c1.im))).divide(d,
//                SCALE, ROUNDING_MODE),
//                ((NumberR)c.im).multiply(((NumberR)c1.re)).subtract(((NumberR)c.re).multiply(((NumberR)c1.im))).divide(
//                d, SCALE, ROUNDING_MODE));
//    }
//
//    public static Complex divide(Complex c, int SCALE) {
//        return divide(c,c, SCALE, RoundingMode.HALF_UP);
//    }
//
//    public static Complex divide(Complex c, Complex c1, MathContext mc) {
//        NumberR d = ((NumberR)c1.re).multiply(((NumberR)c1.re)).add(((NumberR)c1.im).multiply(((NumberR)c1.im)));
//        return new Complex(((NumberR)c.re).multiply(((NumberR)c1.re)).add(((NumberR)c.im).multiply(((NumberR)c1.im))).divide(d,
//                mc),
//                ((NumberR)c.im).multiply(((NumberR)c1.re)).subtract(((NumberR)c.re).multiply(((NumberR)c1.im))).divide(
//                d, mc));
//    }
//
//    public static Complex divide(Complex c, Element c1, MathContext mc) {
//        Complex c2 = (Complex) c1;
//        NumberR d = ((NumberR)c2.re).multiply(((NumberR)c2.re)).add(((NumberR)c2.im).multiply(((NumberR)c2.im)));
//        return new Complex(((NumberR)c.re).multiply(((NumberR)c2.re)).add(((NumberR)c.im).multiply(((NumberR)c2.im))).divide(d,
//                mc),
//                ((NumberR)c.im).multiply(((NumberR)c2.re)).subtract(((NumberR)c.re).multiply(((NumberR)c2.im))).divide(
//                d, mc));
//    }
//
//    public static Complex setScale(Complex c ,int b) {
//        return new Complex(((NumberR)c.re).setScale(b), ((NumberR)c.im).setScale(b));
//    }
//
//    public static Complex setScale(Complex c, int b, RoundingMode rm) {
//        return new Complex(((NumberR)c.re).setScale(b, rm), ((NumberR)c.im).setScale(b, rm));
//    }
//
//    public static Complex pow(Complex c, int n, MathContext mc) {
//        Complex one = Complex.COMPLEX_ONE;
//        Complex min_one = Complex.COMPLEX_MINES_ONE;
//        //РїСЂРѕРІРµСЂРєР° С‡РёСЃР»Р° РЅР° СЂР°РІРµРЅСЃС‚РІРѕ 1 РёР»Рё -1
//        if (c.equals(min_one) || c.equals(one)) {
//            return (((n & 1) == 1) ? c : one);
//        }
//        Complex res = one;
//        Complex temp = c;
//        if ((n & 1) == 1) {
//            res = temp;
//        }
//        n >>>= 1;
//        while (n != 0) {
//            temp = (funcNumberC.multiply(temp,temp, mc));
//            if ((n & 1) == 1) {
//                res = (funcNumberC.multiply(res,temp, mc));
//            }
//            n >>>= 1;
//        }
//        return res;
//    }
//
//    public static NumberR pow1(Complex c, int n, MathContext mc) {
//        Complex one = Complex.COMPLEX_ONE;
//        Complex min_one = Complex.COMPLEX_MINES_ONE;
//        //РїСЂРѕРІРµСЂРєР° С‡РёСЃР»Р° РЅР° СЂР°РІРµРЅСЃС‚РІРѕ 1 РёР»Рё -1
//        if (c.equals(min_one) || c.equals(one)) {
//            return (((n & 1) == 1) ? ((NumberR)c.toNumber(6)) : NumberR.ONE);
//        }
//        Complex res = one;
//        Complex temp = c;
//        if ((n & 1) == 1) {
//            res = temp;
//        }
//        n >>>= 1;
//        while (n != 0) {
//            temp = (funcNumberC.multiply(temp,temp, mc));
//            if ((n & 1) == 1) {
//                res = (funcNumberC.multiply(res,temp, mc));
//            }
//            n >>>= 1;
//        }
//        return ((NumberR)res.toNumber(6));
//    }
//
//    /**
//     * Р’РѕР·РІРµРґРµРЅРёРµ РІ СЃС‚РµРїРµРЅСЊ
//     *
//     * @param factor int - РїРѕРєР°Р·Р°С‚РµР»СЊ СЃС‚РїРµРїРµРЅРё
//     * @param SCALE int - С‚РѕС‡РЅРѕСЃС‚СЊ РІРѕР·РІРµРґРµРЅРёСЏ
//     * @return ComplexBD - СЃС‚РµРїРµРЅСЊ
//     */
//    public static Complex pow(Complex c,int factor, int SCALE, RoundingMode ROUNDING_MODE) {
//        switch (factor) {
//            case 0:
//                return Complex.COMPLEX_ONE;
//            case 1:
//                return new Complex(c.re, c.im);
//            case -1:
//                return c.negate();
//            default:
//                Complex c1 = new Complex(c.re, c.im);
//                for (int i = 1; i < Math.abs(factor); i++) {
//                    c1 = c1.multiply(c1);
//                }
//                if (factor < 0) {
//                    c1 = funcNumberC.divide(Complex.COMPLEX_ONE,c1, SCALE, ROUNDING_MODE);
//                }
//                return c1;
//        }
//    }
//
////    public NumberR abs(int d) {
////        // MathContext mc = new MathContext(d);
////        return funcNumberR.sqrt((re.multiply(re)).add(im.multiply(im)));
////    }
//
//    public static Complex abs(Complex c, MathContext mc) {
//        // MathContext mc = new MathContext(d);
//        NumberR r1 = ((NumberR)c.re).multiply(((NumberR)c.re));
//        NumberR r2 = r1.add(((NumberR)c.im));
//        NumberR r3 = r2.multiply(((NumberR)c.im));
//        return new Complex(funcNumberC.sqrt(r3, mc));//!!!!!!!!!!!!!!
//    }
//
//    /**
//  * Метод вычисления квадратного корня. Вычисление происходит путем построения
//  * полинома x^2-d, и нахождении корней методом хорд
//  *
//  * @param d NumberR - подкоренное число
//  * @param eps int - точность с которой это число вычисляется
//  * @return NumberR результат вычисления
//  */
//
//    public static NumberR sqrt(NumberR d, MathContext mc) {
////если d равно нулю, то корень равен нулю
//        if (d.compareTo(new NumberR(0))==0)
//            return new NumberR(0);
////если d равно единице, то корень равен единице
//        if (d.compareTo(new NumberR(1))==0)
//            return new NumberR(1);
////объявляем переменные - начало и конец отрезка
//        NumberR A = null;
//        NumberR B = null;
//// если подкоренное число > 1, то корень будет меньше d
//        if (d.compareTo(new NumberR(1)) == 1) {
//            A = new NumberR(1);
//            B = d;
//        }
//// если подкоренное число < 1, то корень будет больше d
//        else {
//            A = d;
//            B = new NumberR(1);
//        }
////подразумевается,что мы  рассматриваем многочлен  x^2-d=0
////вычисляем значение ф-ции в точке A
//        NumberR Alpha = A.multiply(A).subtract(d);
////вычисляем значение ф-ции в точке B
//        NumberR Betta = B.multiply(B).subtract(d);
////объявляем переменную - приближенный корень
//        NumberR C = null;
////вычисляем значение ф-ции в точке, которая есть приближенный корень
//        NumberR Gamma = null;
//// выполняется до тех пор, пока погрешность не будет меньше допустимой
//        do {
//            C = B.add(A).multiply(new NumberR(0.5));
//            Gamma = C.multiply(C).subtract(d);
//            if (Gamma.multiply(Alpha).compareTo(new NumberR(0)) == 1) {
//                A = C;
//                Alpha = Gamma;
//            } else {
//                B = C;
//                Betta = Gamma;
//            }
////формула метода хорд
//            C = (Betta.multiply(A).subtract(Alpha.multiply(B))).divide(Betta.
//                    subtract(
//                    Alpha),  mc.getPrecision() * 2 + 2, 1);
////вычисляем значение ф-ции в точке приближенного корня
//            Gamma = C.multiply(C).subtract(d);
////остановка метода
//            if (Gamma.multiply(Alpha).compareTo(new NumberR(0)) == 1) {
//                A = C;
//                Alpha = Gamma;
//            } else {
//                B = C;
//                Betta = Gamma;
//            }
//
//        } while (Gamma.abs().compareTo(new NumberR(Math.pow(0.1, mc.getPrecision()))) == 1);
//        return
//                //отсекание лишнего хвоста
//                C.setScale(mc.getPrecision(), 1);
//    }
//
//
//      public static Complex pow(int exp, int m) {
//                return (m == 1)? pow(exp,Notebook.SCALE,RoundingMode.HALF_UP) : (exp == 1 & m == 2)?
//                    sqrt(Notebook.SCALE):ZERO;
//    }
//
//
//
//      public static NumberR pow1(
//            int n, int m) {
//        if (n < 0 || n > 999999999) {
//            throw new ArithmeticException("Invalid operation");
//        }
//// No need to calculate pow(n) if result will over/underflow.
//// Don't attempt to support "supernormal" numbers.
//
//        return (m == 1) ?
//            pow(n, new MathContext(Notebook.SCALE)) : (n == 1 & m == 2) ?
//                NFunctionR.sqrt(this, Notebook.SCALE) : ZERO;
//    }
//
//    /**
//     *param@ x uiu
//     *
//     **/
//    public static void main(String[] args) {
//
//        Notebook.SCALE = 20;//задаем точность
//        int ep1 = 30;
//        MathContext mc = INITmathContext(Notebook.SCALE);
//        NumberR x1 = new NumberR(0);//pi(Notebook.SCALE).divide(new NumberR(4), mc);
//        Complex x = new Complex(x1, NumberR.ZERO);
//        //вывод результатов на экран
//        System.out.println("sin1= " + sin(x));
//        Notebook.SCALE = 30;//задаем точность
//        System.out.println("sin2= " + sin(x));
//
//    }
//}