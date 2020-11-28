/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

import com.mathpar.number.Ring;
import com.mathpar.number.NumberR;
import com.mathpar.number.Element;
import com.mathpar.number.NumberZ;
import com.mathpar.number.NumberR64;
import com.mathpar.polynom.FactorPol;
import com.mathpar.polynom.Polynom;

public class NInt {
    /* класс NInt позволяет вычислять определенные и несобственные интегралы.
     * Несобственные интегралы первого рода можно посчитать только от дробно
     * рациональных функций. Для других функций корректность вычислений не гарантируется.
     * Несобственные интегралы второго рода можно посчитать только от функций,
     * принимающих максимальное значение на ограниченном конце промежутка интегрирования.
     * Для других функций корректность рассчетов не гарантируется.
     * Все виды интегралов вычисляются при помощи определенных интегралов.
     *
     */
    // Переменные h и flag используются в функции f() при вычислении
    //несобственных интегралов.
    static Element h;
    // Если flag = false, метод f() возвращает значение функции f в точке x.
    // Если flag = true, метод f() возвращает значение функции f(h+(1/x))*(1/x)^2.
    static boolean flag;
    // Переменная precision хранит количество точных десятичных знаков
    // после запятой для коэффициентов формулы Гаусса.
    static int precision = 0;
    //Если пользователь сам задаёт количество узлов интегрирования,
    //это число записявается в переменную numberOfPoints В противном случае
    //в переменной содержится число 0 и используются
    //наборы узлов, заданные по умолчанию.
    static int numberOfPoints = 0;
    // Массивы wg1 и wg2 используются для хранения весовых коэффициентов формулы Гаусса.
    static NumberR[] wg1;
    static NumberR[] wg2;
    // Массивы xg1 и xg2 используются для хранения узловых коэффициентов формулы Гаусса.
    static NumberR[] xg1;
    static NumberR[] xg2;

    static {
        xg1 = new NumberR[0];
        wg1 = new NumberR[0];
        xg2 = new NumberR[0];
        wg2 = new NumberR[0];
    }

    /**
     * Численное интегрирование методом Гаусса в частном случае W(x) = 1, a =
     * -1, b = 1, с 15 узлами.
     * </p>
     * Рассмотрим вычисление интеграла вида:
     *
     * int_a^b(f(x)*W(x)*dx)
     *
     * где a, b и W(x) известны заранее. Существует два способа вычислить этот
     * интеграл. Первый способ - это применение одного из общих алгоритмов
     * интегрирования (метода Симпсона, Трапеций и т.д.). Второй способ состоит
     * в оптимальном выборе расположения узлов и значений весовых коэффициентов
     * квадратурной формулы (с учетом интервала интегрирования и функции W(x)),
     * чтобы в результате добиться максимально возможной точности при заданном
     * числе узлов N. Квадратурная формула, построенная таким способом, носит
     * название квадратурной формулы Гаусса. В случае, если f является полиномом
     * степени не выше 2•N-1, квадратурная формула Гаусса позволит получить
     * точное значение интеграла.
     * </p>
     * Алгоритмы построения квадратурных формул Гаусса рассматривются в статье
     * "Orthogonal polynomials and quadrature", W. Gautschi, 1999.
     * </p>
     * Подробное описание квадратурных формул Гаусса можно найти в книге А. А.
     * Самарского и А. В. Гулина "Численные методы"
     *
     *
     * @param a начало промежутка интегрирования.
     *
     * @param b конец промежутка интегрирования.
     *
     * @param ring - кольцо.
     *
     * @param f - подинтегральная функция.
     */
    static Element integ1(Element a, Element b, F f, Ring ring, int eps) {
        // w1, w2 - массивы для весовых коэффициентов формулы Гаусса.
        // x1, x2 - массивы для узловых коэффициентов формулы Гаусса.
        Element[] w1 = null, w2 = null, x1 = null, x2 = null;
        Element[] interval = new Element[10]; // Хранит концы отрезков.
        Element[] sum = new Element[10]; // Хранит значения интеграла на каждом отрезке.
        Element zero = null;
        Element epsilon = null;
        Ring r = new Ring("R[x]");
        r.setMachineEpsilonR(eps + 5);
        r.setAccuracy(eps + 10);
        if (numberOfPoints == 0) {
            // Задаем наборы узлов по умолчанию.
            if ((eps <= 10) && ((precision < eps) || (xg1.length != 10))) {
                tenPoints();
                precision = eps;
            }
            if (((eps <= 45) && (eps > 10)) && ((precision < eps) || (xg1.length != 20))) {
                twentyPoints();
                precision = eps;
            }
            if ((eps > 45) && ((precision < eps) || (xg1.length != 32))) {
                r.setMachineEpsilonR(eps + eps / 5 + 5);
                try {
                    r.setAccuracy(eps + eps / 3 + 25);
                } catch (Exception e) {}
                xg1 = new NumberR[32];
                xg2 = new NumberR[48];
                wg1 = new NumberR[xg1.length];
                wg2 = new NumberR[xg2.length];
                nodes(xg1, r);
                nodes(xg2, r);
                if ((xg1[xg1.length - 1] == null) || (xg2[xg2.length - 1] == null)) {
                    return Element.NAN;
                }
                wg1 = weights(xg1, r);
                wg2 = weights(xg2, r);
                precision = eps;
                r.setMachineEpsilonR(eps + 5);
            }
        }
        if ((numberOfPoints != 0) && ((xg1.length != numberOfPoints) || (precision < eps))) {
            // Задаем наборы узлов заданные пользователем.
            if ((numberOfPoints == 10) && (eps <= 10)) {
                tenPoints();
                precision = eps;
            } else if ((numberOfPoints == 20) && (eps <= 45)) {
                twentyPoints();
                precision = eps;
            } else if ((numberOfPoints == 32)) {
                r.setMachineEpsilonR(eps + eps / 5 + 5);
                try {
                    r.setAccuracy(eps + eps / 3 + 25);
                } catch (Exception e) {}
                xg1 = new NumberR[32];
                xg2 = new NumberR[48];
                wg1 = new NumberR[xg1.length];
                wg2 = new NumberR[xg2.length];
                nodes(xg1, r);
                nodes(xg2, r);
                if ((xg1[xg1.length - 1] == null) || (xg2[xg2.length - 1] == null)) {
                    return Element.NAN;
                }
                wg1 = weights(xg1, r);
                wg2 = weights(xg2, r);
                precision = eps;
                r.setMachineEpsilonR(eps + 5);
            } else {
                r.setMachineEpsilonR(eps + eps / 5 + 15);
                try {
                    r.setAccuracy(eps + eps / 3 + 25);
                } catch (Exception e) {
                    System.out.println("Exception!");
                }
                xg1 = new NumberR[numberOfPoints];
                wg1 = new NumberR[numberOfPoints];
                xg2 = new NumberR[(int) (1.5 * numberOfPoints)];
                wg2 = new NumberR[(int) (1.5 * numberOfPoints)];
                nodes2(xg1, r);
                nodes2(xg2, r);
                if ((xg1[xg1.length - 1] == null) || (xg2[xg2.length - 1] == null)) {
                    return Element.NAN;
                }
                wg1 = weights(xg1, r);
                wg2 = weights(xg2, r);
                precision = eps;
                r.setMachineEpsilonR(eps + 5);
            }
        }
        if (ring.algebra[0] == Ring.R) {
            zero = new NumberR(0);
            epsilon = new NumberR("10").pow(-eps, r);
            x1 = xg1;
            w1 = wg1;
            x2 = xg2;
            w2 = wg2;
        } else if (ring.algebra[0] == Ring.R64) {
            if (eps > 12) {
                eps = 12;
            }
            r = new Ring("R64[x]");
            r.setMachineEpsilonR64(eps);
            zero = new NumberR64(0);
            epsilon = r.MachineEpsilonR64;
            x1 = new NumberR64[xg1.length];
            w1 = new NumberR64[wg1.length];
            x2 = new NumberR64[xg2.length];
            w2 = new NumberR64[wg2.length];
            for (int i = 0; i < xg1.length; i++) {
                x1[i] = new NumberR64(xg1[i].doubleValue());
                w1[i] = new NumberR64(wg1[i].doubleValue());
            }
            for (int i = 0; i < xg2.length; i++) {
                x2[i] = new NumberR64(xg2[i].doubleValue());
                w2[i] = new NumberR64(wg2[i].doubleValue());
            }
        }
        Element Result = zero;
        Element Result2 = zero;
        int k = 2, j = 0; // k - указывает на первый свободный элемент массива interval; j - указывает на разбиваемый отрезок.
        interval[0] = a;
        interval[1] = b;

        do {

            if (Thread.currentThread().isInterrupted()) {
                return Element.NAN;
            }
            // Если тело цикла выполняется не первый раз, значит
            // отрезок с номером j был разбит на две части:
            // первая часть имеет номер j, вторая часть имеет номер k-2.
            // В блоке if считается интеграл по отрезку с номером j,
            // затем считается интеграл по отрезку с номером k-2.

            if (k > 2) {
                Result = Result.subtract(sum[j], r);
                Result2 = Result2.subtract(sum[j + 1], r);
                sum[j] = zero;
                sum[j + 1] = zero;
                compute(j, interval, sum, w1, x1, w2, x2, r, f);
                Result = Result.add(sum[j], r);
                Result2 = Result2.add(sum[j + 1], r);
            }
            sum[k - 2] = zero;
            sum[k - 1] = zero;
            compute(k - 2, interval, sum, w1, x1, w2, x2, r, f);
            Result2 = Result2.add(sum[k - 1], r);
            Result = Result.add(sum[k - 2], r);
            if (Result.subtract(Result2, r).abs(r).compareTo(epsilon, r) > 0) {
                Element temp = zero;
                j = 0;
                for (int i = 0; i < k; i += 2) { // Находим номер отрезка с наибольшей погрешностью.
                    if (sum[i].subtract(sum[i + 1], r).abs(r).compareTo(temp, r) > 0) {
                        j = i;
                        temp = sum[i].subtract(sum[i + 1], r).abs(r);
                    }
                }
                if (k == interval.length) {
                    interval = resize(interval);
                    sum = resize(sum);
                }
                interval[k] = interval[j + 1].add(interval[j], r);           // Разбиваем отрезок с
                interval[k] = interval[k].divide(new NumberZ(2), r); // наибольшей погрешностью пополам.
                interval[k + 1] = interval[j + 1];
                interval[j + 1] = interval[k];
                k = k + 2;
            }
        } while (Result.subtract(Result2, r).abs(r).compareTo(epsilon, r) > 0);
        return Result2;
    }
// Метод compute() вычисляет значение интеграла на отрезке номер k.

    static void compute(int k, Element[] interval, Element[] sum, Element[] w1, Element[] x1, Element[] w2, Element[] x2, Ring ring, F f) {
        Element koef1 = (interval[k + 1].subtract(interval[k], ring)).divide(new NumberZ(2), ring);
        Element koef2 = (interval[k + 1].add(interval[k], ring)).divide(new NumberZ(2), ring);
        // Переходим от отрезка [a, b] к отрезку [-1, 1]:
        //   int_a^b(f(x)*dx) = ( (b-a)/2 )*int_(-1)^1(f( x*(b-a)/2 + (b+a)/2)*dx)
        // и считаем значение интеграла при помощи формулы Гаусса:
        //   int_(-1)^1(f(x)*dx) = sum_1^n(f(xg_i)*wg_i)

        for (int i = 0; i < x1.length; i++) {
            sum[k] = sum[k].add(w1[i].multiply(koef1, ring).multiply(
                    f(x1[i].multiply(koef1, ring).add(koef2, ring), f, ring), ring), ring);
        }

        // Считаем значение интеграла используя больше точек.
        // Разность полученных значений используется для оценки погрешности.
        for (int i = 0; i < x2.length; i++) {
            sum[k + 1] = sum[k + 1].add(w2[i].multiply(koef1, ring).multiply(
                    f(x2[i].multiply(koef1, ring).add(koef2, ring), f, ring), ring), ring);
        }
    }

    static Element f(Element x, F f, Ring ring) {
        Element[] t = new Element[1];
        if (!flag) {
            t[0] = x;
            return f.valueOf(t, ring);
        } else {
            t[0] = x.pow(-1, ring);
            t[0] = t[0].add(h, ring);
            return f.valueOf(t, ring).multiply(x.pow(-2, ring), ring);
        }
    }

    static Element[] resize(Element[] a) {
        Element[] b = new Element[a.length * 10];
        System.arraycopy(a, 0, b, 0, a.length);
        return b;
    }

// Возвращает int_(-1)^1(x^n)
    static NumberR integral(int n, Ring ring) {
        if (n % 2 == 0) {
            return (NumberR) new NumberR(2).divide(new NumberR(n + 1), ring);
        } else {
            return new NumberR(0);
        }
    }

    static void tenPoints() {
        xg1 = new NumberR[10];
        wg1 = new NumberR[10];
        xg2 = new NumberR[15];
        wg2 = new NumberR[15];
        xg1[0] = new NumberR("0.14887433898163121088");
        xg1[1] = new NumberR("0.43339539412924719079");
        xg1[2] = new NumberR("0.67940956829902440623");
        xg1[3] = new NumberR("0.86506336668898451073");
        xg1[4] = new NumberR("0.97390652851717172007");
        xg1[5] = new NumberR("-0.97390652851717172007");
        xg1[6] = new NumberR("-0.86506336668898451073");
        xg1[7] = new NumberR("-0.67940956829902440623");
        xg1[8] = new NumberR("-0.43339539412924719079");
        xg1[9] = new NumberR("-0.14887433898163121088");

        wg1[0] = new NumberR("0.29552422471475287017");
        wg1[1] = new NumberR("0.26926671930999635509");
        wg1[2] = new NumberR("0.21908636251598204399");
        wg1[3] = new NumberR("0.14945134915058059314");
        wg1[4] = new NumberR("0.06667134430868813759");
        wg1[5] = new NumberR("0.06667134430868813759");
        wg1[6] = new NumberR("0.14945134915058059314");
        wg1[7] = new NumberR("0.21908636251598204399");
        wg1[8] = new NumberR("0.26926671930999635509");
        wg1[9] = new NumberR("0.29552422471475287017");

        xg2[0] = new NumberR("0.20119409399743452230");
        xg2[1] = new NumberR("0.39415134707756336989");
        xg2[2] = new NumberR("0.57097217260853884753");
        xg2[3] = new NumberR("0.72441773136017004741");
        xg2[4] = new NumberR("0.84820658341042721620");
        xg2[5] = new NumberR("0.93727339240070590430");
        xg2[6] = new NumberR("0.98799251802048542848");
        xg2[7] = new NumberR("0");
        xg2[8] = new NumberR("-0.98799251802048542848");
        xg2[9] = new NumberR("-0.93727339240070590430");
        xg2[10] = new NumberR("-0.84820658341042721620");
        xg2[11] = new NumberR("-0.72441773136017004741");
        xg2[12] = new NumberR("-0.57097217260853884753");
        xg2[13] = new NumberR("-0.39415134707756336989");
        xg2[14] = new NumberR("-0.20119409399743452230");

        wg2[0] = new NumberR("0.19843148532711157645");
        wg2[1] = new NumberR("0.18616100001556221102");
        wg2[2] = new NumberR("0.16626920581699393355");
        wg2[3] = new NumberR("0.13957067792615431444");
        wg2[4] = new NumberR("0.10715922046717193501");
        wg2[5] = new NumberR("0.07036604748810812470");
        wg2[6] = new NumberR("0.03075324199611726835");
        wg2[7] = new NumberR("0.20257824192556127288");
        wg2[8] = new NumberR("0.03075324199611726835");
        wg2[9] = new NumberR("0.07036604748810812470");
        wg2[10] = new NumberR("0.10715922046717193501");
        wg2[11] = new NumberR("0.13957067792615431444");
        wg2[12] = new NumberR("0.16626920581699393355");
        wg2[13] = new NumberR("0.18616100001556221102");
        wg2[14] = new NumberR("0.19843148532711157645");
    }

    static void twentyPoints() {
        xg1 = new NumberR[20];
        wg1 = new NumberR[20];
        xg2 = new NumberR[30];
        wg2 = new NumberR[30];
        xg1[0] = new NumberR("0.0765265211334973337546404093988382110047962668134975008");
        xg1[1] = new NumberR("0.2277858511416450780804961953685746247430889376829274723");
        xg1[2] = new NumberR("0.3737060887154195606725481770249272373957463217056827118");
        xg1[3] = new NumberR("0.5108670019508270980043640509552509984254913292024268334");
        xg1[4] = new NumberR("0.6360536807265150254528366962262859367433891167993684639");
        xg1[5] = new NumberR("0.7463319064601507926143050703556415903107306795691764441");
        xg1[6] = new NumberR("0.8391169718222188233945290617015206853296293650656373732");
        xg1[7] = new NumberR("0.9122344282513259058677524412032981130491847974236917747");
        xg1[8] = new NumberR("0.9639719272779137912676661311972772219120603278061888560");
        xg1[9] = new NumberR("0.9931285991850949247861223884713202782226471309016558961");
        xg1[10] = new NumberR("-0.9931285991850949247861223884713202782226471309016558961");
        xg1[11] = new NumberR("-0.9639719272779137912676661311972772219120603278061888560");
        xg1[12] = new NumberR("-0.9122344282513259058677524412032981130491847974236917747");
        xg1[13] = new NumberR("-0.8391169718222188233945290617015206853296293650656373732");
        xg1[14] = new NumberR("-0.7463319064601507926143050703556415903107306795691764441");
        xg1[15] = new NumberR("-0.6360536807265150254528366962262859367433891167993684639");
        xg1[16] = new NumberR("-0.5108670019508270980043640509552509984254913292024268334");
        xg1[17] = new NumberR("-0.3737060887154195606725481770249272373957463217056827118");
        xg1[18] = new NumberR("-0.2277858511416450780804961953685746247430889376829274723");
        xg1[19] = new NumberR("-0.0765265211334973337546404093988382110047962668134975008");
        wg1[0] = new NumberR("0.1527533871307258506980843319550975934919486451123785972");
        wg1[1] = new NumberR("0.1491729864726037467878287370019694366926799040813683164");
        wg1[2] = new NumberR("0.1420961093183820513292983250671649330345154133920203033");
        wg1[3] = new NumberR("0.1316886384491766268984944997481631349161105111469835269");
        wg1[4] = new NumberR("0.1181945319615184173123773777113822870050412195489687754");
        wg1[5] = new NumberR("0.1019301198172404350367501354803498761666916560233925562");
        wg1[6] = new NumberR("0.0832767415767047487247581432220462061001778285831632907");
        wg1[7] = new NumberR("0.0626720483341090635695065351870416063516010765784363640");
        wg1[8] = new NumberR("0.0406014298003869413310399522749321098790906399899515368");
        wg1[9] = new NumberR("0.0176140071391521183118619623518528163621431055433367325");
        wg1[10] = new NumberR("0.0176140071391521183118619623518528163621431055433367325");
        wg1[11] = new NumberR("0.0406014298003869413310399522749321098790906399899515368");
        wg1[12] = new NumberR("0.0626720483341090635695065351870416063516010765784363640");
        wg1[13] = new NumberR("0.0832767415767047487247581432220462061001778285831632907");
        wg1[14] = new NumberR("0.1019301198172404350367501354803498761666916560233925562");
        wg1[15] = new NumberR("0.1181945319615184173123773777113822870050412195489687754");
        wg1[16] = new NumberR("0.1316886384491766268984944997481631349161105111469835269");
        wg1[17] = new NumberR("0.1420961093183820513292983250671649330345154133920203033");
        wg1[18] = new NumberR("0.1491729864726037467878287370019694366926799040813683164");
        wg1[19] = new NumberR("0.1527533871307258506980843319550975934919486451123785972");
        xg2[0] = new NumberR("0.0514718425553176958330252131667225737491414536665695642");
        xg2[1] = new NumberR("0.1538699136085835469637946727432559204185519712443384617");
        xg2[2] = new NumberR("0.2546369261678898464398051298178051078827893033025184261");
        xg2[3] = new NumberR("0.3527047255308781134710372070893738606536310080214256265");
        xg2[4] = new NumberR("0.4470337695380891767806099003228540001624075938614244097");
        xg2[5] = new NumberR("0.5366241481420198992641697933110727941641780069302971054");
        xg2[6] = new NumberR("0.6205261829892428611404775564311892992073646928295281325");
        xg2[7] = new NumberR("0.6978504947933157969322923880266400683823538006539546563");
        xg2[8] = new NumberR("0.7677774321048261949179773409745031316948836172329084532");
        xg2[9] = new NumberR("0.8295657623827683974428981197325019164390686961703416788");
        xg2[10] = new NumberR("0.8825605357920526815431164625302255900566891471464842320");
        xg2[11] = new NumberR("0.9262000474292743258793242770804740040864745368253290609");
        xg2[12] = new NumberR("0.9600218649683075122168710255817976629303592174039233994");
        xg2[13] = new NumberR("0.9836681232797472099700325816056628019403178547097113635");
        xg2[14] = new NumberR("0.9968934840746495402716300509186952833408820381177507901");
        xg2[15] = new NumberR("-0.9968934840746495402716300509186952833408820381177507901");
        xg2[16] = new NumberR("-0.9836681232797472099700325816056628019403178547097113635");
        xg2[17] = new NumberR("-0.9600218649683075122168710255817976629303592174039233994");
        xg2[18] = new NumberR("-0.9262000474292743258793242770804740040864745368253290609");
        xg2[19] = new NumberR("-0.8825605357920526815431164625302255900566891471464842320");
        xg2[20] = new NumberR("-0.8295657623827683974428981197325019164390686961703416788");
        xg2[21] = new NumberR("-0.7677774321048261949179773409745031316948836172329084532");
        xg2[22] = new NumberR("-0.6978504947933157969322923880266400683823538006539546563");
        xg2[23] = new NumberR("-0.6205261829892428611404775564311892992073646928295281325");
        xg2[24] = new NumberR("-0.5366241481420198992641697933110727941641780069302971054");
        xg2[25] = new NumberR("-0.4470337695380891767806099003228540001624075938614244097");
        xg2[26] = new NumberR("-0.3527047255308781134710372070893738606536310080214256265");
        xg2[27] = new NumberR("-0.2546369261678898464398051298178051078827893033025184261");
        xg2[28] = new NumberR("-0.1538699136085835469637946727432559204185519712443384617");
        xg2[29] = new NumberR("-0.0514718425553176958330252131667225737491414536665695642");
        wg2[0] = new NumberR("0.1028526528935588403412856367054150438683755570649282225");
        wg2[1] = new NumberR("0.1017623897484055045964289521685540446327062894871268408");
        wg2[2] = new NumberR("0.0995934205867952670627802821035694765298692636667042772");
        wg2[3] = new NumberR("0.0963687371746442596394686263518098650964064614301602459");
        wg2[4] = new NumberR("0.0921225222377861287176327070876187671969132344182341075");
        wg2[5] = new NumberR("0.0868997872010829798023875307151257025767533287435453440");
        wg2[6] = new NumberR("0.0807558952294202153546949384605297308758928037084392998");
        wg2[7] = new NumberR("0.0737559747377052062682438500221907341537705260370494389");
        wg2[8] = new NumberR("0.0659742298821804951281285151159623612374429536566603789");
        wg2[9] = new NumberR("0.0574931562176190664817216894020561287971206707217631345");
        wg2[10] = new NumberR("0.0484026728305940529029381404228075178152718091973727363");
        wg2[11] = new NumberR("0.0387991925696270495968019364463476920332009767663953521");
        wg2[12] = new NumberR("0.0287847078833233693497191796112920436395888945462874964");
        wg2[13] = new NumberR("0.0184664683110909591423021319120472690962065339681814033");
        wg2[14] = new NumberR("0.0079681924961666056154658834746736224504806965871517212");
        wg2[15] = new NumberR("0.0079681924961666056154658834746736224504806965871517212");
        wg2[16] = new NumberR("0.0184664683110909591423021319120472690962065339681814033");
        wg2[17] = new NumberR("0.0287847078833233693497191796112920436395888945462874964");
        wg2[18] = new NumberR("0.0387991925696270495968019364463476920332009767663953521");
        wg2[19] = new NumberR("0.0484026728305940529029381404228075178152718091973727363");
        wg2[20] = new NumberR("0.0574931562176190664817216894020561287971206707217631345");
        wg2[21] = new NumberR("0.0659742298821804951281285151159623612374429536566603789");
        wg2[22] = new NumberR("0.0737559747377052062682438500221907341537705260370494389");
        wg2[23] = new NumberR("0.0807558952294202153546949384605297308758928037084392998");
        wg2[24] = new NumberR("0.0868997872010829798023875307151257025767533287435453440");
        wg2[25] = new NumberR("0.0921225222377861287176327070876187671969132344182341075");
        wg2[26] = new NumberR("0.0963687371746442596394686263518098650964064614301602459");
        wg2[27] = new NumberR("0.0995934205867952670627802821035694765298692636667042772");
        wg2[28] = new NumberR("0.1017623897484055045964289521685540446327062894871268408");
        wg2[29] = new NumberR("0.1028526528935588403412856367054150438683755570649282225");
    }

    /**
     * Вычисление несобственных интегралов первого рода.
     * </p>
     * Пусть необходимо вычислить нитеграл от функции f(x) на промежутке [a,
     * +\infinity). Считаем интеграл от функции f(x) на отрезке [a, a+step],
     * затем на отрезке [a+step, a+(2*step)], и так далее (step - целое
     * положтиельное число). Когда значение интеграла на очередном отрезке
     * станет меньше значения интеграла на предыдущем отрезке, шаг увеличивается
     * в 10 раз. Вычисление интеграла останавливается, когда значение интеграла
     * на текущем отрезке становится меньше значения интеграла на предыдущем
     * отрезке и меньше машинного нуля.
     * </p>
     * Если задан промежуток [-infinity, infinity], то интеграл считается по
     * промежутку [-infinity, 0], а затем по промежутку [0, infinity] и
     * полученные значения складываются.
     *
     * @param a, b концы промежутка интегрирования.
     * @param f подинтегральная функция.
     * @param ring кольцо.
     */
    static Element integ2(Element a, Element b, F f, Ring ring, int eps) {
        Element prevIntegral; // Значение интеграла на предыдущем шаге.
        Element currIntegral; // Значение интеграла на текущем шаге.
        Element result = null;
        NumberZ step = new NumberZ(0);
        Element epsilon = new Element(1);
        Element c = null;
        Element pointLim = null;
        int st = 50;
        Ring r = null;
        if (numberOfPoints != 0) {
            st = numberOfPoints * 3;
        }
        if (a.isInfinite() && b.isInfinite()) {
            currIntegral = integ2(Element.NEGATIVE_INFINITY, new NumberZ(0), f, ring, eps);
            if (currIntegral.isNaN()) {
                return currIntegral;
            }
            result = currIntegral;
            currIntegral = integ2(new NumberZ(0), Element.POSITIVE_INFINITY, f, ring, eps);
            if (currIntegral.isNaN()) {
                return currIntegral;
            }
            result = result.add(currIntegral, ring);
            return result;
        }
        if (ring.algebra[0] == Ring.R) {
            r = new Ring("R[x]");
            r.setMachineEpsilonR(eps);
            epsilon = r.MachineEpsilonR;
            result = new NumberR(0);
            if (a.isInfinite()) {
                step = new NumberZ(-st);
                c = b;
                pointLim = Element.NEGATIVE_INFINITY;
            } else {
                step = new NumberZ(st);
                c = a;
                pointLim = Element.POSITIVE_INFINITY;
            }
        } else if (ring.algebra[0] == Ring.R64) {
            r = new Ring("R64[x]");
            r.setMachineEpsilonR64(eps);
            epsilon = r.MachineEpsilonR64;
            result = new NumberR64(0);
            if (a.isInfinite()) {
                step = new NumberZ(-st);
                c = b;
                pointLim = Element.NEGATIVE_INFINITY;
            } else {
                step = new NumberZ(st);
                c = a;
                pointLim = Element.POSITIVE_INFINITY;
            }
        }
        Element lim = new LimitOf(r).Limit(new F(F.MULTIPLY, new Element[]{f, new Polynom("x", r)}).expand(ring), pointLim);
        if(!lim.isZero(r)) {
            if(lim.isNaN()) {
                lim = new LimitOf(r).Limit(f, pointLim);
                if(!lim.isZero(r)) {
                    return Element.NAN;
                }
            } else {
                return Element.NAN;
            }
        }
        currIntegral = result;
        do {
            prevIntegral = currIntegral;
            if (step.isNegative()) {
                currIntegral = integ3(c.add(step, r), c, f, ring, eps);
            } else {
                currIntegral = integ3(c, c.add(step, r), f, ring, eps);
            }
            if (currIntegral.isNaN()) {
                return currIntegral;
            }
            c = c.add(step, r);
            result = result.add(currIntegral, r);
            if (prevIntegral.abs(r).compareTo(currIntegral.abs(r), r) >= 0) {
                step = (NumberZ) step.multiply(new NumberZ(10), r);
            }
        } while ((prevIntegral.abs(r).compareTo(currIntegral.abs(r), r) == -1) || (currIntegral.abs(r).compareTo(epsilon, r) == 1));
        return result;
    }

    /*
     * Следующие несколько методов определяют различные варианты запуска алгоритмов интегрирования.
     * Для каждого вида интегралов предусмотрено по 6 вариантов запуска:
     *      - с дополнительным параметром epsilon
     *      - с дополнительными параметрами epsilon и N
     *      - без указания этих параметров
     * (в каждом из этих трех случаев можно либо указвать точки
     * экстремума f(x) в промежутке [a, b], либо не указывать)
     */



    /**
     * Вычисление несобственого интеграла второго рода.
     * </p>
     * Промежуток [a, b] разбивается точками разрыва функции f,
     * затем каждый полученный промежуток разбивается пополам. На каждом
     * промежутке делается замена переменной x = 1/(t-v), где v -
     * точка разрыва, попавшая на границу текущего промежутка. Эта замена
     * переменной переводит интеграл второго рода в интеграл первого рода.
     *
     * @param a задает начало промежутка интегрирования
     * @param b задает конец промежутка интегрирования
     * @param breaks массив, в котором указаны все точки разрыва второго рода
     * функуии f, которые попадают в [a, b].
     */
    public static Element integrate(Element a, Element b, Element[] breaks, Element f1, int epsilon, Ring ring) {
        F f;
        if (f1 instanceof F) {
            f = (F) f1;
        } else {
            f = new F(F.ID, f1);
        }
        flag = true;
        Ring r = null;
        Element result = null;
        Element bound = null;
        Element c;
        Element one = null;
        Element currInteg = null;
        if (ring.algebra[0] == Ring.R) {
            r = new Ring("R[x]");
            r.setAccuracy(epsilon + 5);
            r.setMachineEpsilonR(epsilon);
            one = new NumberR(1);
            result = new NumberR(0);
        } else if (ring.algebra[0] == Ring.R64) {
            r = new Ring("R64[x]");
            r.setMachineEpsilonR64(epsilon);
            one = new NumberR64(1);
            result = new NumberR64(0);
        }
        if (breaks[0].compareTo(a, r) != 0) {
            bound = one.divide(a.subtract(breaks[0], r), r);
            h = breaks[0];
            result = integrate(Element.NEGATIVE_INFINITY, bound, f, epsilon, ring);
            if (result.isNaN()) {
                return result;
            }
        }
        for (int i = 0; i < breaks.length - 1; i++) {
            c = breaks[i].add(breaks[i + 1], r);
            c = c.divide(new NumberZ(2), r);
            bound = one.divide(c.subtract(breaks[i], r), r);
            h = breaks[i];
            currInteg = integrate(bound, Element.POSITIVE_INFINITY, f, epsilon, ring);
            if (currInteg.isNaN()) {
                return currInteg;
            }
            result = result.add(currInteg, r);
            bound = one.divide(c.subtract(breaks[i + 1], r), r);
            h = breaks[i + 1];
            currInteg = integrate(Element.NEGATIVE_INFINITY, bound, f, epsilon, ring);
            if (currInteg.isNaN()) {
                return currInteg;
            }
            result = result.add(currInteg, r);
        }
        if (breaks[breaks.length - 1].compareTo(b, r) != 0) {
            bound = one.divide(b.subtract(breaks[breaks.length - 1], r), r);
            h = breaks[breaks.length - 1];
            currInteg = integrate(bound, Element.POSITIVE_INFINITY, f, epsilon, ring);
            if (currInteg.isNaN()) {
                return currInteg;
            }
            result = result.add(currInteg, r);
        }
        flag = false;
        return result;
    }

    public static Element integrate(Element a, Element b, Element[] breaks, Element f1, Ring ring) {
        F f;
        if (f1 instanceof F) {
            f = (F) f1;
        } else {
            f = new F(F.ID, f1);
        }
        int i = 15;
        Element tmp = null;
        if (ring.algebra[0] == Ring.R) {
            i = 0;
            tmp = ring.MachineEpsilonR;
            NumberR ten = new NumberR(10);
            for (; tmp.compareTo(new NumberR(1), ring) == -1; i++) {
                tmp = tmp.multiply(ten, ring);
            }
        }
        return integrate(a, b, breaks, f, i, ring);
    }

    public static Element integrate(Element a, Element b, Element[] breaks, Element f1, int epsilon, int N, Ring ring) {
        F f;
        if (f1 instanceof F) {
            f = (F) f1;
        } else {
            f = new F(F.ID, f1);
        }
        numberOfPoints = N;
        Element Result = integrate(a, b, breaks, f, epsilon, ring);
        numberOfPoints = 0;
        return Result;
    }

    /*
     *  Если хотя-бы одна из границ промежутка [a, b] бесконечна, то эта функция считает
     *  несобственный интеграл. В противном случае функция считает определенный интеграл.
     */
    public static Element integrate(Element a, Element b, Element f1, int epsilon, Ring ring) {
        F f;
        if (f1 instanceof F) {
            f = (F) f1;
        } else {
            f = new F(F.ID, f1);
        }
        int i = epsilon;
        if ((ring.algebra[0] == Ring.R64) && (epsilon > 12)) {
            i = 12;
        }
        Element currIntegral = null, prevIntegral = null;
        NumberR tmp = null;
        if ((!a.isInfinite()) && (!b.isInfinite())) {
            return integ3(a, b, f, ring, i);
        }
        currIntegral = integ2(a, b, f, ring, i);
        if (currIntegral.isNaN()) {
            return currIntegral;
        }
        if (ring.algebra[0] == Ring.R) {
            tmp = ring.MachineEpsilonR;
            do {
                i += 5;
                prevIntegral = currIntegral;
                ring.setMachineEpsilonR(i);
                currIntegral = integ2(a, b, f, ring, i);
                ring.setMachineEpsilonR(epsilon);
                if (currIntegral.isNaN()) {
                    // Задаем исходное значение переменной MachineEpsilonR.
                    ring.setMachineEpsilonR(tmp);
                    return currIntegral;
                }
            } while (currIntegral.abs(ring).compareTo(prevIntegral.abs(ring), ring) != 0);
            // Возможно, в этом методе изменялось значение MachineEpsilonR
            // если оно изменилось, то задаем исходное значение.
            ring.setMachineEpsilonR(tmp);
        }
        return currIntegral;
    }

    public static Element integrate(Element a, Element b, Element f1, Ring ring) {
        F f;
        if (f1 instanceof F) {
            f = (F) f1;
        } else {
            f = new F(F.ID, f1);
        }
        int i = 15;
        Element tmp = null;
        if (ring.algebra[0] == Ring.R) {
            i = 0;
            tmp = ring.MachineEpsilonR;
            NumberR ten = new NumberR(10);
            for (; tmp.compareTo(new NumberR(1), ring) == -1; i++) {
                tmp = tmp.multiply(ten, ring);
            }
        }
        return integrate(a, b, f, i, ring);
    }

    public static Element integrate(Element a, Element b, Element f1, int epsilon, int N, Ring ring) {
        F f;
        if (f1 instanceof F) {
            f = (F) f1;
        } else {
            f = new F(F.ID, f1);
        }
        numberOfPoints = N;
        Element Result = integrate(a, b, f, epsilon, ring);
        numberOfPoints = 0;
        return Result;
    }

    /*
     *  Если хотя-бы одна из границ промежутка [a, b] бесконечна, то эта функция считает
     *  несобственный интеграл. В противном случае функция считает определенный интеграл.
     *
     *  points - массив точек экстремума функции f(x), принадлежащих [a, b].
     *  Точки экстремума должны быть отсортированы по возрастанию.
     */
    public static Element integrate(Element f, Element a, Element b, Element[] points, Ring ring) {
        int i = 15;
        Element tmp = null;
        if (ring.algebra[0] == Ring.R) {
            i = 0;
            tmp = ring.MachineEpsilonR;
            NumberR ten = new NumberR(10);
            for (; tmp.compareTo(new NumberR(1), ring) == -1; i++) {
                tmp = tmp.multiply(ten, ring);
            }
        }
    	return integrate(f, a, b, points, i, ring);
    }

    public static Element integrate(Element f, Element a, Element b, Element[] points, int epsilon, Ring ring) {
    	Element Result = new NumberZ(0);
    	Element currIntegral = null;

    	currIntegral = integrate(a, points[0], f, epsilon, ring);
    	if(currIntegral.isNaN()) {
    		return currIntegral;
    	}
    	Result = Result.add(currIntegral, ring);
    	currIntegral = integrate(points[points.length - 1], b, f, epsilon, ring);
    	if(currIntegral.isNaN()) {
    		return currIntegral;
    	}
    	Result = Result.add(currIntegral, ring);

    	for(int i=1; i<points.length; i++) {
    		currIntegral = integ3(points[i-1], points[i], (F) f, ring, epsilon);
    		if(currIntegral.isNaN()) {
    			return currIntegral;
    		}
    		Result = Result.add(currIntegral, ring);
    	}

    	return Result;
    }

    public static Element integrate(Element f, Element a, Element b, Element[] points, int epsilon, int N, Ring ring) {
    	Element Result = null;
    	numberOfPoints = N;
    	Result = integrate(f, a, b, points, epsilon, ring);
    	numberOfPoints = 0;
    	return Result;
    }

    public static Element integrate(Element f, Element a, Element b, Element[] points, Element[] breaks, int epsilon, int N, Ring ring) {
    	Element Result = null;
    	numberOfPoints = N;
    	Result = integrate(f, a, b, points, breaks, epsilon, ring);
    	numberOfPoints = 0;
    	return Result;

    }

    public static Element integrate(Element f, Element a, Element b, Element[] points, Element[] breaks, Ring ring) {
        int i = 15;
        Element tmp = null;
        if (ring.algebra[0] == Ring.R) {
            i = 0;
            tmp = ring.MachineEpsilonR;
            NumberR ten = new NumberR(10);
            for (; tmp.compareTo(new NumberR(1), ring) == -1; i++) {
                tmp = tmp.multiply(ten, ring);
            }
        }
        return integrate(f, a, b, points, breaks, i, ring);
    }

    /*
     *  Вычисление несобственного интеграла от функции f(x) на промежутке [a, b].
     *
     *  points - массив точек экстремума функции f(x), принадлежащих [a, b].
     *  Точки экстремума должны быть отсортированы по возрастанию.
     *
     * Промежуток [a, b] разбивается точками разрыва функции f,
     * затем каждый полученный промежуток разбивается пополам.
     * На каждом промежутке делается замена переменной x = 1/(t-v), где v -
     * точка разрыва, попавшая на границу текущего промежутка. Эта замена
     * переменной переводит интеграл второго рода в интеграл первого рода.
     */
    public static Element integrate(Element f, Element a, Element b, Element[] points, Element[] breaks, int epsilon, Ring ring) {
    	Element Result = null;
        flag = true;
        Ring r = null;
        Element bound = null;
        Element c;
        Element one = null;
        Element currInteg = null;
        Element[] temp = new Element[0];
        int j = 0;
        int len = 0;
        if (ring.algebra[0] == Ring.R) {
            r = new Ring("R[x]");
            r.setAccuracy(epsilon + 5);
            r.setMachineEpsilonR(epsilon);
            one = new NumberR(1);
            Result = new NumberR(0);
        } else if (ring.algebra[0] == Ring.R64) {
            r = new Ring("R64[x]");
            r.setMachineEpsilonR64(epsilon);
            one = new NumberR64(1);
            Result = new NumberR64(0);
        }
        if (breaks[0].compareTo(a, r) != 0) {
        	for(; (j < points.length)&&(points[j].compareTo(breaks[0], ring) < 0); j++) {}
        	if(j!=0) {
        	    temp = new Element[j];
        	    for(int k=0; k<temp.length; k++) {
        		    temp[k] = one.divide(points[k].subtract(breaks[0], ring), ring);
        	    }
                bound = one.divide(a.subtract(breaks[0], r), r);
                h = breaks[0];
                Result = integrate(f, Element.NEGATIVE_INFINITY, bound, temp, epsilon, ring);
        	} else {
                bound = one.divide(a.subtract(breaks[0], r), r);
                h = breaks[0];
                Result = integrate(Element.NEGATIVE_INFINITY, bound, f, epsilon, ring);
        	}
            if (Result.isNaN()) {
                return Result;
            }
        }
        for (int i = 0; i < breaks.length - 1; i++) {
            c = breaks[i].add(breaks[i + 1], r);
            c = c.divide(new NumberZ(2), r);
            len = j;
            for(; (j < points.length)&&(points[j].compareTo(c, ring) < 0); j++) {}
            if(len < j) {
                temp = new Element[j - len];
                for(int k=0; k<temp.length; k++) {
            	    temp[k] = one.divide(points[k + len].subtract(breaks[i], ring), ring);
                }
                bound = one.divide(c.subtract(breaks[i], r), r);
                h = breaks[i];
                currInteg = integrate(f, bound, Element.POSITIVE_INFINITY, temp, epsilon, ring);
            } else {
                bound = one.divide(c.subtract(breaks[i], r), r);
                h = breaks[i];
                currInteg = integrate(bound, Element.POSITIVE_INFINITY, f, epsilon, ring);
            }
            if (currInteg.isNaN()) {
                return currInteg;
            }
            Result = Result.add(currInteg, r);
            len = j;
            for(; (j < points.length)&&(points[j].compareTo(breaks[i+1], ring) < 0); j++) {}
            if(len < j) {
                temp = new Element[j - len];
                for(int k=0; k < temp.length; k++) {
            	    temp[k] = one.divide(points[k + len].subtract(breaks[i+1], ring), ring);
                }
                bound = one.divide(c.subtract(breaks[i + 1], r), r);
                h = breaks[i + 1];
                currInteg = integrate(f, Element.NEGATIVE_INFINITY, bound, temp, epsilon, ring);
            } else {
                bound = one.divide(c.subtract(breaks[i + 1], r), r);
                h = breaks[i + 1];
                currInteg = integrate(Element.NEGATIVE_INFINITY, bound, f, epsilon, ring);
            }
            if (currInteg.isNaN()) {
                return currInteg;
            }
            Result = Result.add(currInteg, r);
        }
        if (breaks[breaks.length - 1].compareTo(b, r) != 0) {
        	if(points.length > j) {
        	    temp = new Element[points.length - j];
        	    for(int k=0; k<points.length - j; k++) {
        		    temp[k] = one.divide(points[k+j].subtract(breaks[breaks.length-1], ring), ring);
        	    }
                bound = one.divide(b.subtract(breaks[breaks.length - 1], r), r);
                h = breaks[breaks.length - 1];
                currInteg = integrate(f, bound, Element.POSITIVE_INFINITY, temp, epsilon, ring);
        	} else {
                bound = one.divide(b.subtract(breaks[breaks.length - 1], r), r);
                h = breaks[breaks.length - 1];
                currInteg = integrate(bound, Element.POSITIVE_INFINITY, f, epsilon, ring);
        	}
            if (currInteg.isNaN()) {
                return currInteg;
            }
            Result = Result.add(currInteg, r);
        }
        flag = false;
    	return Result;
    }




    // Метод root() находит корень полинома методом половинного деления.
    // [a, b] - отрезок, в котором содержится корень.

    static NumberR root(Polynom p, NumberR a, NumberR b, Ring ring) {
        NumberR c, pa, pb, pc;
        pa = (NumberR) p.valueOf(a, ring);
        pb = (NumberR) p.valueOf(b, ring);
        do {
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }
            c = (NumberR) a.add(b, ring);
            c = (NumberR) c.divide(new NumberR(2),ring.MC);
            pc = (NumberR) p.valueOf(c, ring);
            if (pa.isNegative() != pc.isNegative()) {
                b = c;
                pb = pc;
            } else if (pb.isNegative() != pc.isNegative()) {
                a = c;
                pa = pc;
            } else {
                return a;
            }
        } while (b.subtract(a, ring).abs(ring).compareTo(ring.MachineEpsilonR, ring) == 1);
        c = (NumberR) a.add(b, ring);
        c = (NumberR) c.divide(new NumberR(2), ring);
        return c;
    }
    // Возвращает весовые коэффициенты для формулы Гаусса по данному набору узлов
    // по формуле: w_i = ( (x_i-x_1)*(x_i-x_2)*...*(x_i-x_(i-1))*(x_i-x_(x+1))*...*(x_i-x_n) ) *
    // *int_a^b ( p(x)/(x - x_i) ) , где p(x) - ролином Лежандра степени n; x_i - его корни.

    static NumberR[] weights(NumberR[] x, Ring ring) {
        NumberR[] w = new NumberR[x.length];
        Polynom[] p = new Polynom[x.length - 1];
        Polynom pol;
        int[] pow = new int[x.length - 1];
        for (int i = 0; i < x.length; i++) {
            w[i] = new NumberR(0);
            for (int t = 0; t < x.length - 1; t++) {
                p[t] = new Polynom(new int[] {1, 0}, new NumberR[] {
                    new NumberR(1), (NumberR) new NumberR(0).subtract(
                    x[(t + i + 1) % x.length], ring)});
                pow[t] = 1;
            }
            NumberR koef = new NumberR(1);
            for (int j = 0; j < x.length - 1; j++) {
                koef = (NumberR) koef.multiply((NumberR) x[i].subtract(
                        (NumberR) x[(j + i + 1) % x.length], ring));
            }
            koef = (NumberR) koef.pow(-1, ring);
            pol = (Polynom)(new FactorPol(pow, p).toPolynomOrFraction(ring));
            for (int j = 0; j < pol.coeffs.length; j++) {
                w[i] = (NumberR) w[i].add(koef.multiply(
                        (NumberR) pol.coeffs[j].multiply(
                                integral(pol.powers[j], ring), ring), ring), ring);
            }
        }
        return w;
    }
    // Возвращает факториал числа.

    static NumberZ factorial(NumberZ n) {
        NumberZ t = new NumberZ(1);
        if (n.equals(new NumberZ(0))) {
            return t;
        }
        NumberZ i = new NumberZ(1);
        while (!i.equals(n.add(new NumberZ(1)))) {
            t = t.multiply(i);
            i = i.add(new NumberZ(1));
        }
        return t;
    }
    //Метод soch возвращает количество сочетаний из n по k.

    static NumberZ soch(NumberZ n, NumberZ k) {
        return factorial(n).divide(factorial(k).multiply(factorial(n.subtract(k))));
    }
    // Находит корни многочлена Лежандра любой степени.

    static void nodes2(NumberR[] x, Ring ring) {
        NumberZ n = new NumberZ(x.length);
        // Находим многочлен Лежандра степени n при помощи формулы
        // sum( ( soch(n, k)^2 )*( (x-1)^(n-k) )*(x+1)^k) , k=0,...,n.
        Polynom legendre = new Polynom(new int[] {0}, new NumberZ[] {new NumberZ(0)});
        Polynom pol = new Polynom();
        Polynom[] p = new Polynom[2];
        p[0] = new Polynom(new int[] {1, 0}, new NumberR[] {new NumberR(1), new NumberR(1)});
        p[1] = new Polynom(new int[] {1, 0}, new NumberR[] {new NumberR(1), new NumberR(-1)});
        int[] pow = new int[2];
        for (int i = 0; i <= x.length; i++) {
            pow[0] = i;
            pow[1] = x.length - i;
            pol = ((Polynom)(new FactorPol(pow, p).toPolynomOrFraction(ring))).multiply(
                    new Polynom(new int[] {0}, new NumberZ[] {
                        (NumberZ) soch(n, new NumberZ(i)).multiply(
                                soch(n, new NumberZ(i)), ring)}), ring);
            legendre = legendre.add(pol, ring);
        }
        NumberR[] intervals = new NumberR[x.length];
        int k = 0;
        // Отделение корней.
        for (double i = 0.000000001; i < 1; i += 0.0001) {
            if (Thread.currentThread().isInterrupted()) {
                x[x.length - 1] = null;
                return;
            }
            if (legendre.valueOf(new NumberR(i), ring).isNegative()
                    != legendre.valueOf(new NumberR(i + 0.0001), ring).isNegative()) {
                intervals[k] = new NumberR(i);
                intervals[k + 1] = new NumberR(i + 0.0001);
                k += 2;
            }
        }
        k = 0;
        // Так как корни этого многочлена расположены симметрично относительно нуля,
        // достаточно найти только половину корней.
        for (int i = 0; i < x.length - 1; i += 2) {
            x[k] = root(legendre, intervals[i], intervals[i + 1], ring);
            k++;
        }
        if (x[k - 1] == null) {
            return;
        }
        if (x.length % 2 != 0) {
            x[k] = new NumberR(0);
            k++;
        }
        for (int i = k; i < x.length; i++) {
            x[i] = (NumberR) new NumberR(0).subtract(x[x.length - i - 1], ring);
        }
    }
    // Находит корни многочленов Лежандра 32 и 48 степени.

    static void nodes(NumberR[] x, Ring ring) {
        Polynom legendre = new Polynom();
        NumberR[] intervals = new NumberR[0];
        if (x.length == 32) {
            legendre = new Polynom("1832624140942590534.00x^32-14428278950913093728.00x^30"
                    + "+51445092980714719440.00x^28-109865791789322960160.00x^26+156606940050570009000.00x^24"
                    + "-157176419832572081760.00x^22+114175323840641983920.00x^20-60765578514627386400.00x^18"
                    + "+23717177328413240100.00x^16-6728277256287444000.00x^14+1360607178493683120.00x^12"
                    + "-189852164440979040.00x^10+17364527235455400.00x^8-958987697421600.00x^6+27769914018000.00x^4"
                    + "-317370445920.00x^2+601080390.00", ring);
            intervals = new NumberR[x.length];
            // Далее задаются промежутки, содержащие положительные корни многочлена.
            // Так как корни этого многочлена расположены симметричны относительно нуля,
            // достаточно найти только половину корней.
            intervals[0] = new NumberR("0.048307665687738316234812570440502");
            intervals[1] = new NumberR("0.048307665687738316234812570440503");
            intervals[2] = new NumberR("0.144471961582796493485186373598810");
            intervals[3] = new NumberR("0.144471961582796493485186373598811");
            intervals[4] = new NumberR("0.239287362252137074544603209165501");
            intervals[5] = new NumberR("0.239287362252137074544603209165502");
            intervals[6] = new NumberR("0.331868602282127649779916805730186");
            intervals[7] = new NumberR("0.331868602282127649779916805730189");
            intervals[8] = new NumberR("0.421351276130635345364119436172426");
            intervals[9] = new NumberR("0.421351276130635345364119436172427");
            intervals[10] = new NumberR("0.506899908932229390023747474377821");
            intervals[11] = new NumberR("0.506899908932229390023747474377823");
            intervals[12] = new NumberR("0.587715757240762329040745476401825");
            intervals[13] = new NumberR("0.587715757240762329040745476401828");
            intervals[14] = new NumberR("0.663044266930215200975115168663238");
            intervals[15] = new NumberR("0.663044266930215200975115168663239");
            intervals[16] = new NumberR("0.732182118740289680387426665091267");
            intervals[17] = new NumberR("0.732182118740289680387426665091268");
            intervals[18] = new NumberR("0.794483795967942406963097298970427");
            intervals[19] = new NumberR("0.794483795967942406963097298970429");
            intervals[20] = new NumberR("0.849367613732569970133693004967742");
            intervals[21] = new NumberR("0.849367613732569970133693004967744");
            intervals[22] = new NumberR("0.896321155766052123965307243719212");
            intervals[23] = new NumberR("0.896321155766052123965307243719213");
            intervals[24] = new NumberR("0.93490607593773968917091913483540");
            intervals[25] = new NumberR("0.93490607593773968917091913483541");
            intervals[26] = new NumberR("0.964762255587506430773811928118273");
            intervals[27] = new NumberR("0.964762255587506430773811928118277");
            intervals[28] = new NumberR("0.985611511545268335400175044630901");
            intervals[29] = new NumberR("0.985611511545268335400175044630903");
            intervals[30] = new NumberR("0.997263861849481563544981128665040");
            intervals[31] = new NumberR("0.997263861849481563544981128665042");
        } else {
            legendre = new Polynom("6435067013866298908421603100.00x^48"
                    + "-76407953596223001775784929440.00x^46+425173290172531219558803236400.00x^44"
                    + "-1473311108070382907335633192800.00x^42+3563260854069100233752753311800.00x^40"
                    + "-6389295324537696970867005938400.00x^38+8807205123823531314744127793520.00x^36"
                    + "-9549981459567684558156283149600.00x^34+8267808022866467649885300689700.00x^32"
                    + "-5767697298652275603858100059200.00x^30+3258374447939921932049705877600.00x^28"
                    + "-1492927928874291503411865238464.00x^26+553883078634868423069470550800.00x^24"
                    + "-165624842582040828566818929600.00x^22+39605940617444545961630613600.00x^20"
                    + "-7487690265984541027571956800.00x^18+1101554433361187285787028260.00x^16"
                    + "-123423465922822104850087200.00x^14+10229085062820411239852400.00x^12"
                    + "-602247648658472026610400.00x^10+23772933499676527366200.00x^8-576313539386097633120.00x^6"
                    + "+7413982067574154800.00x^4-37923181931325600.00x^2+32247603683100.00", ring);
            intervals = new NumberR[x.length];
            intervals[0] = new NumberR("0.032380170962869362033322243152134");
            intervals[1] = new NumberR("0.032380170962869362033322243152136");
            intervals[2] = new NumberR("0.097004699209462698930053955853624");
            intervals[3] = new NumberR("0.097004699209462698930053955853626");
            intervals[4] = new NumberR("0.161222356068891718056437390783497");
            intervals[5] = new NumberR("0.161222356068891718056437390783498");
            intervals[6] = new NumberR("0.224763790394689061224865440174692");
            intervals[7] = new NumberR("0.224763790394689061224865440174693");
            intervals[8] = new NumberR("0.287362487355455576735886461316797");
            intervals[9] = new NumberR("0.287362487355455576735886461316798");
            intervals[10] = new NumberR("0.348755886292160738159817937270407");
            intervals[11] = new NumberR("0.348755886292160738159817937270409");
            intervals[12] = new NumberR("0.408686481990716729916225495814633");
            intervals[13] = new NumberR("0.408686481990716729916225495814634");
            intervals[14] = new NumberR("0.466902904750958404544928861650798");
            intervals[15] = new NumberR("0.466902904750958404544928861650799");
            intervals[16] = new NumberR("0.523160974722233033678225869137508");
            intervals[17] = new NumberR("0.523160974722233033678225869137509");
            intervals[18] = new NumberR("0.577224726083972703817809238540478");
            intervals[19] = new NumberR("0.577224726083972703817809238540479");
            intervals[20] = new NumberR("0.628867396776513623995164933069994");
            intervals[21] = new NumberR("0.628867396776513623995164933069995");
            intervals[22] = new NumberR("0.67787237963266390521185128067590");
            intervals[23] = new NumberR("0.67787237963266390521185128067592");
            intervals[24] = new NumberR("0.724034130923814654674482233493665");
            intervals[25] = new NumberR("0.724034130923814654674482233493666");
            intervals[26] = new NumberR("0.76715903251574033925385543752296");
            intervals[27] = new NumberR("0.76715903251574033925385543752298");
            intervals[28] = new NumberR("0.807066204029442627082553043024538");
            intervals[29] = new NumberR("0.807066204029442627082553043024539");
            intervals[30] = new NumberR("0.843588261624393530711089844519656");
            intervals[31] = new NumberR("0.843588261624393530711089844519657");
            intervals[32] = new NumberR("0.876572020274247885905693554805096");
            intervals[33] = new NumberR("0.876572020274247885905693554805097");
            intervals[34] = new NumberR("0.905879136715569672822074835671011");
            intervals[35] = new NumberR("0.905879136715569672822074835671012");
            intervals[36] = new NumberR("0.931386690706554333114174380101601");
            intervals[37] = new NumberR("0.931386690706554333114174380101602");
            intervals[38] = new NumberR("0.952987703160430860722960666025718");
            intervals[39] = new NumberR("0.952987703160430860722960666025719");
            intervals[40] = new NumberR("0.970591592546247250461411983800660");
            intervals[41] = new NumberR("0.970591592546247250461411983800661");
            intervals[42] = new NumberR("0.984124583722826857744583600026598");
            intervals[43] = new NumberR("0.984124583722826857744583600026599");
            intervals[44] = new NumberR("0.993530172266350757547928750849074");
            intervals[45] = new NumberR("0.993530172266350757547928750849075");
            intervals[46] = new NumberR("0.998771007252426118600541491563113");
            intervals[47] = new NumberR("0.998771007252426118600541491563114");
        }
        int k = 0;
        for (int i = 0; i < x.length - 1; i += 2) {
            x[k] = root(legendre, intervals[i], intervals[i + 1], ring);
            k++;
        }
        if (x[k - 1] == null) {
            return;
        }
        for (int i = k; i < x.length; i++) {
            x[i] = (NumberR) new NumberR(0).subtract(x[x.length - i - 1], ring);
        }
    }

    /*
     * Вычисление определенного интеграла с оценкой на границах интервала [a, b].
     * Оценка выполняется следующим образом:
     * В окрестности точки a (внутри [a, b]) находим такую точку c, что f(a)*0.7 < f(c). Затем
     * считаем площадь трапеции с вершинами в точках a, c, f(a), f(c) (оцениваем
     * интеграл функции f(x) на промежутке [a, c]). Затем аналогичным образом
     * оцениваем интеграл от функции f(x) на промужутке [g, b], где g - принадлежит [a, b],
     * f(g) > f(b)*0.7 (g берем из окрестности точки b). Складываем полученные значения.
     *
     * Если полученная оценка больше интеграла по всему промежутку [a, b], то интеграл был посчитан не верно,
     * В этом случае разбиваем промежуток [a, b] пополам, и на каждой половине применяем этот же алгоритм.
     *
     * Такой алгоритм позволяет считать интегралы от быстро убывающих или возрастающих на [a, b] функций.
     * (например, exp(-1000x) на [0, 10])
     */
    static Element integ3(Element a, Element b, F f, Ring ring, int epsilon) {
        Element Result = null;
        Element evaluation = null;
        Element temp = null;
        Element L = null;
        Element R = null;
        if(ring.algebra[0] == Ring.R) {
        	temp = new NumberR("0.7");
        } else if(ring.algebra[0] == Ring.R64) {
        	temp = new NumberR64("0.7");
        } else {
        	return null;
        }
        Element[] m = new Element[1];
        if(flag) {
        	m[0] = a.pow(-1, ring);
        	m[0] = m[0].add(h, ring);
        	L = f.valueOf(m, ring).multiply(a.pow(-2, ring), ring).abs(ring);
        	m[0] = b.pow(-1, ring);
        	m[0] = m[0].add(h, ring);
        	R = f.valueOf(m, ring).multiply(b.pow(-2, ring), ring).abs(ring);
        	m[0] = b.add(a.multiply(new NumberZ(9), ring), ring).divide(new NumberZ(10), ring);
        	while(f.valueOf(new Element[]{m[0].pow(-1, ring).add(h, ring)}, ring).multiply(m[0].pow(-2, ring), ring)
        			.abs(ring).compareTo(L, ring) < 0) {
        		m[0] = m[0].add(a, ring).divide(new NumberZ(2), ring);
        	}
        	evaluation = f.valueOf(new Element[]{a.pow(-1, ring).add(h, ring)}, ring).multiply(a.pow(-2, ring), ring)
        			.add(f.valueOf(new Element[]{m[0].pow(-1, ring).add(h, ring)}, ring).multiply(m[0].pow(-2, ring), ring), ring)
        			.multiply(m[0].subtract(a, ring), ring).divide(new NumberZ(4), ring);
        	m[0] = a.add(b.multiply(new NumberZ(9), ring), ring).divide(new NumberZ(10), ring);
        	while(f.valueOf(new Element[]{m[0].pow(-1, ring).add(h, ring)}, ring).multiply(m[0].pow(-2, ring), ring)
        			.abs(ring).compareTo(R, ring) < 0) {
        		m[0] = m[0].add(b, ring).divide(new NumberZ(2), ring);
        	}
        	evaluation = evaluation.add(f.valueOf(new Element[]{m[0].pow(-1, ring).add(h, ring)}, ring).multiply(m[0].pow(-2, ring), ring)
        			.add(f.valueOf(new Element[]{b.pow(-1, ring).add(h, ring)}, ring).multiply(b.pow(-2, ring), ring), ring)
        			.multiply(b.subtract(m[0], ring), ring).divide(new NumberZ(4), ring), ring);
        } else {
            m[0] = a;
            L = f.valueOf(m, ring).abs(ring).multiply(temp, ring);
            m[0] = b;
            R = f.valueOf(m, ring).abs(ring).multiply(temp, ring);
            m[0] = b.add(a.multiply(new NumberZ(9), ring), ring).divide(new NumberZ(10), ring);
    	    while(f.valueOf(m, ring).abs(ring).compareTo(L, ring) < 0) {
    		    m[0] = m[0].add(a, ring).divide(new NumberZ(2), ring);
    	    }
    	    evaluation = f.valueOf(new Element[]{a}, ring).add(f.valueOf(m, ring), ring).divide(new NumberZ(2), ring)
    		    	.multiply(m[0].subtract(a, ring).divide(new NumberZ(2), ring), ring);
    	    m[0] = a.add(b.multiply(new NumberZ(9), ring), ring).divide(new NumberZ(10), ring);
    	    while(f.valueOf(m, ring).abs(ring).compareTo(R, ring) < 0) {
    		    m[0] = m[0].add(b, ring).divide(new NumberZ(2), ring);
    	    }
    	    evaluation = evaluation.add(f.valueOf(m, ring).add(f.valueOf(new Element[]{b}, ring), ring).divide(new NumberZ(2), ring)
    			    .multiply(b.subtract(m[0], ring).divide(new NumberZ(2), ring), ring), ring);
        }
    	Result = integ1(a, b, f, ring, epsilon);
    	if(Result.isNaN()) {
    		return Result;
    	}
    	if(Result.abs(ring).compareTo(evaluation.abs(ring), ring) < 0) {
            return integ3(a, a.add(b, ring).divide(new NumberZ(2), ring), f, ring, epsilon).add(
    				integ3(a.add(b, ring).divide(new NumberZ(2), ring), b, f, ring, epsilon), ring);
    	}
    	return Result;
    }

    public static void main(String[] args) {
        Ring ring = new Ring("R[x]");
        ring.setMachineEpsilonR(5);
        ring.setMachineEpsilonR64(10);
        ring.setAccuracy(85);
        ring.FLOATPOS = 45;
        System.out.println("ring.getAccuracy()");
        F f = new F("\\exp(x)", ring);
        Element[] m = new Element[3];
        Element[] m12 = new Element[1];
        m12[0] = new NumberR("0");
        //m12[1] = new NumberR("1");
       // Element a = f.value(m, ring);
      //  System.out.println(a.toString(ring));
        m[0] = new NumberR64("0.2");
        m[1] = new NumberR64("0.3");
        m[2] = new NumberR64("0.4");
        //System.out.println(integ3(new NumberR("0"), new NumberR("100"), f, ring, 5).toString(ring));
        System.out.println(integrate(new NumberR("-1"), new NumberR("1"), f, ring).toString(ring));
        //System.out.println(integrate(new NumberR64(0), new NumberR64().pi(ring), f, 32, 5, ring).toString(ring));
    }
}
