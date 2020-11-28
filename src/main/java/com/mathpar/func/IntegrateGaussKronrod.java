/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;

/**
 *
 * @author Корабельников Вячеслав
 */
public class IntegrateGaussKronrod {
//  Задает интегрируемую функцию.
	public static double f(double x) {
		return Math.abs(Math.sin(100*x));
	}
/*  В подпрограмме gk15() реализован метод Гаусса-Кронрода численного интегрирования
    в частном случае W(x) = 1, a = -1, b = 1, с 15 узлами.

    Рассмотрим вычисление интеграла вида:

               int_a^b(f(x)*W(x)*dx)

    где a, b и W(x) известны заранее. Существует два способа вычислить этот интеграл.
    Первый способ - это применение одного из общих алгоритмов интегрирования (метода Симпсона,
    Трапеций и т.д.). Второй способ состоит в оптимальном выборе расположения узлов и значений
    весовых коэффициентов квадратурной формулы (с учетом интервала интегрирования и функции W(x)),
    чтобы в результате добиться максимально возможной точности при заданном числе узлов N.
    Квадратурная формула, построенная таким способом, носит название квадратурной формулы Гаусса.
    В случае, если f является полиномом степени не выше 2•N-1, квадратурная формула Гаусса
    позволит получить точное значение интеграла. Квадратурная формула Гаусса-Кронрода – это
    расширение квадратурной формулы Гаусса, позволяющее одновременно получить значение интеграла
    и оценку погрешности. Этот результат достигается путем добавления к M-точечной квадратурной
    формуле Гаусса M+1 дополнительных узлов, позволяющих вычислить интеграл с более высокой
    точностью. Разность между значением интеграла, полученным на основе M-точечной формулы, и
    значением, полученным с использованием 2M+1-точечной формулы, используется в качестве оценки
    погрешности интегрирования.

    Алгоритмы построения квадратурных формул Гаусса и Гаусса-Кронрода рассматривются в статье
    "Orthogonal polynomials and quadrature", W. Gautschi, 1999.

    Подробное описание квадратурных формул Гаусса можно найти в книге А. А. Самарского и
    А. В. Гулина "Численные методы"

    Аргументы:

    a, b - концы отрезка интегрирования (a<b).

    epsilon - Требуемая точность.

    Возвращаемое значение:

    int_a^b(f(x)*dx)
*/
	public static double gk15(double a, double b, double epsilon) {
		double[] wg = new double[7];// Массив для весовых коэффициентов формулы Гаусса.
		double[] wk = new double[15];// Массив для весовых коэффициентов формулы Гаусса-Кронрода.
		double[] xg = new double[7];// Массив для узловых коэффициентов формулы Гаусса.
		double[] xk = new double[15];// Массив для узловых коэффициентов формулы Гаусса-Кронрода.
		double[] interval = new double[10]; // Хранит концы отрезков.
		double[] sum = new double[10]; // Хранит значения интеграла на каждом отрезке.
		double ResultGauss = 0,  ResultKronrod = 0;
		int k = 2, j = 0; // k - указывает на первый свободный элемент массива interval; j - указывает на разбиваемый отрезок.
		interval[0] = a; interval[1] = b;

		// Задаём заранее вычисленные коэффициенты.

		wg[0] = 0.129484966168869693270611432679082;
        	wg[1] = 0.279705391489276667901467771423780;
       	 	wg[2] = 0.381830050505118944950369775488975;
        	wg[3] = 0.417959183673469387755102040816327;
		wg[4] = 0.381830050505118944950369775488975;
		wg[5] = 0.279705391489276667901467771423780;
		wg[6] = 0.129484966168869693270611432679082;

		xg[0] = 0.949107912342758524526189684047851;
		xg[1] = 0.741531185599394439863864773280788;
		xg[2] = 0.405845151377397166906606412076961;
		xg[3] = 0.000000000000000000000000000000000;
		xg[4] = -0.405845151377397166906606412076961;
		xg[5] = -0.741531185599394439863864773280788;
		xg[6] = -0.949107912342758524526189684047851;

        	xk[0] = 0.991455371120812639206854697526329;
        	xk[1] = 0.949107912342758524526189684047851;
        	xk[2] = 0.864864423359769072789712788640926;
        	xk[3] = 0.741531185599394439863864773280788;
        	xk[4] = 0.586087235467691130294144838258730;
        	xk[5] = 0.405845151377397166906606412076961;
        	xk[6] = 0.207784955007898467600689403773245;
        	xk[7] = 0.000000000000000000000000000000000;
		xk[8] = -0.207784955007898467600689403773245;
		xk[9] = -0.405845151377397166906606412076961;
		xk[10] = -0.586087235467691130294144838258730;
		xk[11] = -0.741531185599394439863864773280788;
		xk[12] = -0.864864423359769072789712788640926;
		xk[13] = -0.949107912342758524526189684047851;
		xk[14] = -0.991455371120812639206854697526329;

        	wk[0] = 0.022935322010529224963732008058970;
        	wk[1] = 0.063092092629978553290700663189204;
        	wk[2] = 0.104790010322250183839876322541518;
        	wk[3] = 0.140653259715525918745189590510238;
        	wk[4] = 0.169004726639267902826583426598550;
        	wk[5] = 0.190350578064785409913256402421014;
        	wk[6] = 0.204432940075298892414161999234649;
        	wk[7] = 0.209482141084727828012999174891714;
		wk[8] = 0.204432940075298892414161999234649;
		wk[9] = 0.190350578064785409913256402421014;
		wk[10] = 0.169004726639267902826583426598550;
		wk[11] = 0.140653259715525918745189590510238;
		wk[12] = 0.104790010322250183839876322541518;
		wk[13] = 0.063092092629978553290700663189204;
		wk[14] = 0.022935322010529224963732008058970;
		do {
		// Если тело цикла выполняется не первый раз, значит
		// отрезок с номером j был разбит на две части:
		// первая часть имеет номер j, вторая часть имеет номер k-2.
		// В блоке if считается интеграл по отрезку с номером j,
		// затем считается интеграл по отрезку с номером k-2.

			if(k>2) {
				ResultGauss -= sum[j];
				ResultKronrod -= sum[j+1];
				sum[j] = 0;
				sum[j+1] = 0;
				compute(j, interval, sum, wg, xg, wk, xk);
				ResultGauss += sum[j];
				ResultKronrod += sum[j+1];
			}
			sum[k-2] = 0;
			sum[k-1] = 0;
			compute(k-2, interval, sum, wg, xg, wk, xk);
			ResultKronrod += sum[k-1];
			ResultGauss +=  sum[k-2];
			if(Math.abs(ResultGauss - ResultKronrod)>epsilon) {
				double temp = 0;
				j = 0;
				for(int i=0; i<k; i+=2) { // Находим номер отрезка с наибольшей погрешностью.
					if(temp < Math.abs(sum[i] - sum[i+1])) {
						j = i;
						temp = Math.abs(sum[i] - sum[i+1]);
					}
				}
				if(k==interval.length) {
					interval = resize(interval);
					sum = resize(sum);
				}
				interval[k] = (interval[j+1] + interval[j])/2;  // Разбиваем отрезок с
				interval[k+1] = interval[j+1];                  // наибольшей погрешностью пополам.
				interval[j+1] = interval[k];
				k = k + 2;
			}
		} while(Math.abs(ResultGauss - ResultKronrod)>epsilon);
		return ResultKronrod;
	}
// Метод compute() вычисляет значение интеграла на отрезке номер k.
	static void compute(int k, double[] interval, double[] sum, double[] wg, double[] xg, double[] wk, double[] xk) {
		double koef1 = (interval[k+1] - interval[k])/2;
		double koef2 = (interval[k+1] + interval[k])/2;

		// Переходим от отрезка [a, b] к отрезку [-1, 1]:
		//   int_a^b(f(x)*dx) = ( (b-a)/2 )*int_(-1)^1(f( x*(b-a)/2 + (b+a)/2)*dx)
		// и считаем значение интеграла при помощи формулы Гаусса:
		// int_(-1)^1(f(x)*dx) = sum_1^n(f(xg_i)*wg_i)

		for(int i=0; i<xg.length; i++) {
			sum[k] += wg[i] * koef1 * f(xg[i] * koef1 + koef2);
		}

		// Считаем значение интеграла при помощи формулы Гаусса-Кронрода:
		//   int_(-1)^1(f(x)*dx) = sum_1^n(f(xk_i)*wk_i) + sum_(n+1)^(2n+1)(f(xk_i)*wk_i)
		// Разность значений интеграла, полученных при помощи формул Гаусса и Гаусса-Кронрода
		// используется для оценки погрешности.

		for(int i=0; i<xk.length; i++) {
			sum[k+1] += wk[i] * koef1 * f(xk[i] * koef1 + koef2);
		}
	}
	static double[] resize(double[] a) {
		double[] b = new double[a.length*10];
		System.arraycopy(a, 0, b, 0, a.length);
		return b;
	}
}
