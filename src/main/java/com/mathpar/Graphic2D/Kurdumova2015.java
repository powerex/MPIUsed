/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic2D;
import java.util.ArrayList;
import com.mathpar.number.*;
import com.mathpar.func.*;

/**
 *
 * @author gennadi
 */
public class Kurdumova2015 {
	// Определяю обозначения для направлений вверх, вниз, влево, вправо.
	static int right = 0;
	static int up = 2;
	static int left = 4;
	static int down = 6;
	// Направление по умолчанию вправо.
	static int move = right;
	static int nk = 1;
	
    // coo[0] - x1      (x1, y1) --- нижняя левая точка
    // coo[1] - y1
    // coo[2] - x2      (x2, y2) --- верхняя правая точка
    // coo[3] - y2
    // Нахождение стартовой точки.
    static Element[] findStartPoint(Element[] coo, Element delta, Element Delta, F f, Ring ring) {
    	NumberZ two = new NumberZ("2");
    	// stepX - шаг по у, с которым нарезаем нашу область прямыми, параллельными x.
    	// stepY - шаг по x, с которым нарезаем нашу область прямыми, параллельными y.
    	Element stepX = coo[2].subtract(coo[0], ring).divide(two, ring);
    	Element stepY = coo[3].subtract(coo[1], ring).divide(two, ring);
    	// numParts - количество вертикальных (горизонтальных) прямых вместе с границей.
    	int numParts = 2;
    	// Когда ищем точку на прямой, то одна координата меняется, а другая нет.
    	// В переменной Fcoo будет записываться неизменяемая координата.
    	Element Fcoo = ring.numberZERO;
    	// На проведенных прямых будем искать с шагом Delta промежутки, на концах которых
    	// функция f принимает значения разных знаков. В таких промежутках
    	// находятся точки искомой кривой.
    	ArrayList<Element[]> intervals = null;
    	// Если isX = true, значит пробегаем по переменной x, а переменная y неизменна.
    	// Eckb isX = false, то все наоборот.
    	boolean isX = true;
    	// Создаем массив, в который запишем координаты стартовой точки.
    	Element[] result = new Element[]{ring.numberZERO, ring.numberZERO};
    	// Если stepX > Delta или stepY > Delta
    	for(;(Delta.subtract(stepX, ring).isNegative())||
    			(Delta.subtract(stepY, ring).isNegative());) {
    		// То пробегаем по горизонтальным и вертикальным линиям,
    		// пока не найдем стартовую точку.
    		for(int i=1; i<numParts; i+=2) {
    		    intervals = findIntervals(coo[0], coo[2], coo[1].add(
    		    		new NumberZ(""+i).multiply(stepY, ring), ring), true, Delta, f, ring);
    		    if(intervals.size() > 0) {
    		    	Fcoo = coo[1].add(new NumberZ(""+i).multiply(stepY, ring), ring);
    		    	isX = true;
    		        break;
    		    }
    		    intervals = findIntervals(coo[1], coo[3], coo[0].add(
    		    		new NumberZ(""+i).multiply(stepX, ring), ring), false, Delta, f, ring);
    		    if(intervals.size() > 0) {
    		    	Fcoo = coo[0].add(new NumberZ(""+i).multiply(stepX, ring), ring);
    		    	isX = false;
    		       	break;
    		    }
    		}
    		if(intervals.size() == 0) {
    			stepX = stepX.divide(two, ring);;
    			stepY = stepY.divide(two, ring);
    			numParts *= 2;
    		} else {
    			break;
    		}
    	}
    	// В зависимости от того, по вертикальной или по горизонтальной линии
    	// был найден первый промежуток, ищем в нем соответствующую координату.
    	// Например, если нашли первый промежуток на параллельной оси x прямой,
    	// то координата y уже известна, она не меняется. Осталость в найденном
    	// промежутке найти координату x.
    	if(isX) {
    		result[0] = findPoint(intervals.get(0)[0], intervals.get(0)[1], Fcoo, isX, delta, f, ring);
    		result[1] = Fcoo;
    	} else {
    		result[0] = Fcoo;
    		result[1] = findPoint(intervals.get(0)[0], intervals.get(0)[1], Fcoo, isX, delta, f, ring);
    		
    	}
    	return result;
    }
    
    // Метод, находящий интервалы длинны Delta, содержащие точки искомой кривой.
    // [a, b] - промежуток, в котором ищем интервалы.
    // с - неизменяемая переменная.
    static ArrayList<Element[]> findIntervals(Element a, Element b, Element c, boolean isX, Element Delta, F f, Ring ring) {
    	// Переменная p используется для подстановки координат точки в функцию.
    	Element[] p;
    	// Массив для интервалов.
    	ArrayList<Element[]> intervals = new ArrayList<Element[]>();
    	// В переменные v1 и v2 записываются значения функции f с шагом Delta.
    	Element v1, v2;
    	// coo - номер координаты, которая меняется.
    	int coo;
    	if(isX) {
    		// Если бежим по x, то начало промежутка [a, b] записываем
    		// в первую координату точки p. А во вторую коодинату p записываем
    		// значение y.
    		coo = 0;
    		p = new Element[]{a, c};
    	} else {
    		// Если бежим по y, то все делаем аналогично.
    		coo = 1;
    		p = new Element[]{c, a};
    	}
    	// Считаем значение функции f в начале промежутка [a, b] и
    	// добавляем к изменяющейся координате величину Delta.
    	v1 = f.valueOf(p, ring);
    	p[coo] = p[coo].add(Delta, ring);
    	
    	for(; !b.subtract(p[coo], ring).isNegative(); p[coo]=p[coo].add(Delta, ring) ) {
    		// Записываем в v2 значение функции f в текущей точке p и
    		// проверяем знаки значений функции f. Если или v1 или v2 равно нулю,
    		// либо v1 или v2 отрицательно, то в найденном интервале есть
    		// точка искомой кривой, поэтому добавляем этот интервал в массив.
    		v2 = f.valueOf(p, ring);
    		if(v1.multiply(v2, ring).isNegative()||v1.multiply(v2, ring).isZero(ring)) {
    			intervals.add(new Element[]{p[coo].subtract(Delta, ring), p[coo]});
    		}
    		v1 = v2;
    	}
    	
    	return intervals;
    }
    
    // Нахождение точки искомой кривой в промежутке длинны Delta.
    // Точка находится методом половинного деления.
    static Element findPoint(Element a, Element b, Element c, boolean isX, Element delta, F f, Ring ring) {
    	// Начало такое же как и в предыдущем методе.
    	Element[] p;
    	Element v1, v2, v3;
    	int coo;
    	if(isX) {
    		coo = 0;
    		p = new Element[]{a, c};
    	} else {
    		coo = 1;
    		p = new Element[]{c, a};
    	}
    	// В переменную temp записываем середину отрезка [a, b]
    	Element temp = a.add(b, ring).divide(new NumberZ("2"), ring);
    	do {
    		// Считаем значение функции f в точке a
    		p[coo] = a;
    	    v1 = f.valueOf(p, ring);
    	    // Считаем значение функции f в точке b
    	    p[coo] = b;
    	    v3 = f.valueOf(p, ring);
    	    // Считаем значение функции f в середине [a, b]
    	    p[coo] = temp;
    	    v2 = f.valueOf(p, ring);
    	    // Если f(a) и значение f в середине отрезка имеют разные знаки,
    	    // то искомая точка находится между ними. В противном случае
    	    // она находится в другой половине отрезка.
    	    // В первом случае в переменную b записываем середину отрезка,
    	    // а во вором - в переменную a записываем середину отрезка.
    	    if(v1.multiply(v2, ring).isNegative() || (v1.multiply(v2, ring).isZero(ring)) ) {
    	    	b = temp;
    	    } else {
    	    	a = temp;
    	    }
    	    temp = a.add(b, ring).divide(new NumberZ("2"), ring);
    	    // Продолжаем до тех пор, пока длинна отрезка не станет меньше delta.
    	} while(delta.subtract(b.subtract(a, ring).abs(ring), ring).isNegative());
    	return temp;
    }
    
    // Метод, позволяющий найти следующую точку кривой. Следующую точку ищем так:
    // Рассматриваем квадрат со стороной Delta, в центре которого расположена текущая точка.
    // Далее, в зависимости от направления движения, выбираем соответствующую стенку квадрата
    // Если на концах выбранной стенки квадрата функция принимает разные знаки, то
    // значит кривая вересекает эту стенку, и мы ищем на ней следующую точку.
    // Если на концах выбранной стенки квадрата функция принимает однинаковые знаки,
    // то берем другую стенку.
    // x, y - координаты предыдущей точки кривой.
    // coo - границы области
    static Element[] nextPoint(Element x, Element y, Element[] coo, Element delta, Element Delta, F f, Ring ring) {
    	// В переменные a, b запишем отрезок длинны Delta, в котором будем
    	// искать точку.
    	Element a = ring.numberZERO, b = ring.numberZERO, Fcoo = ring.numberZERO;
    	Element[] p = new Element[2];
    	Element temp = new NumberR("0.5");
    	Element v1, v2;
    	// c - номер изменяющейся координаты.
    	int c = 0;
    	boolean isX = true;
    	// Если двигаемся направо, то фиксируем x=x+0.5*Delta.
    	// Координата нижней границы стенки квадрата задается: y-0.5*Delta
    	// Координата верхней границы стенки квадрата: y+0.5*Delta
    	if(move == right) {
    		a = y.subtract(temp.multiply(Delta, ring), ring);
    		b = y.add(temp.multiply(Delta, ring), ring);
    		Fcoo = x.add(temp.multiply(Delta, ring), ring);
    		c = 1;
    		isX = false;
    		// Если двигаемся вверх, все делаем аналогично.
    	} else if(move == up) {
    		a = x.subtract(temp.multiply(Delta, ring), ring);
    		b = x.add(temp.multiply(Delta, ring), ring);
    		Fcoo = y.add(temp.multiply(Delta, ring), ring);
    		c = 0;
    		isX = true;
    		// Если двигаемся налево, все делаем аналогично.
    	} else if(move == left) {
    		a = y.subtract(temp.multiply(Delta, ring), ring);
    		b = y.add(temp.multiply(Delta, ring), ring);
    		Fcoo = x.subtract(temp.multiply(Delta, ring), ring);
    		c = 1;
    		isX = false;
    		// Если двигаемся вниз, все делаем аналогично.
    	} else if(move == down) {
    		a = x.subtract(temp.multiply(Delta, ring), ring);
    		b = x.add(temp.multiply(Delta, ring), ring);
    		Fcoo = y.subtract(temp.multiply(Delta, ring), ring);
    		c = 0;
    		isX = true;
    	}
    	// Считаем значения функции f на концах стенки квадрата.
    	p[(c+1)%2] = Fcoo;
    	p[c] = a;
    	v1 = f.valueOf(p, ring);
    	p[c] = b;
    	v2 = f.valueOf(p, ring);
    	// Если функция принимает одинаковые знаки, то меняем направление.
    	if( (!v1.multiply(v2, ring).isNegative())&&(!v1.multiply(v2, ring).isZero(ring)) ) {
    		if( (nk %2) == 0) {
    		    move = nk;
    		    return nextPoint(x, y, coo, delta, Delta, f, ring);
    		} else {
    			move = (move+2)%8;
    			return nextPoint(x, y, coo, delta, Delta, f, ring);
    		}
    	}
    	// Если функция принимает одинаковые знаки, то находим пересечение
    	// кривой и стенки.
    	Element[] result = new Element[2];
        result[c] = findPoint(a, b, Fcoo, isX, delta, f, ring);
    	result[(c+1)%2] = Fcoo;
    	temp = a.add(b, ring).divide(new NumberZ("2"), ring);
    	// Если двигаемся вверх или вниз, то:
    	// если мы нашли точку в левой половине выбранной стенка квадрата,
    	// то записываем в переменную nk направление на лево.
    	// если мы нашли точку в правой половине или точно на середине отрезка,
    	// то записываем в переменную nk направление на право.
    	if( (move == up)||(move == down)) {
    		if(result[c].subtract(temp, ring).isNegative()) {
    			nk = left;
    		} else {
    			nk = right;
    		}
    		// Аналогично, если двигались влево или вправо:
    		// Если нашли точку в верхней половине отрезка или в ее середине, 
    		// в nk записываем направление вверх.
    		// если нашли точку в нижней половине, то записываем
    		// в nk напрвление вниз.
    	} else if( (move == left)||(move == right) ) {
    		if(result[c].subtract(temp, ring).isNegative()) {
    			nk = down;
    		} else {
    			nk = up;
    		}
    	}

    	return result;
    }
    
    
    // Основной метод. Находит все точки кривой.
    // coo - границы области, в которой ищем точки кривой.
    // f1, f2 - две поверхности, пересечение которых нужно найти.
    static ArrayList<Element[]> findAllPoints(Element[] coo, Element delta, Element Delta, F f1, F f2, Ring ring) {
    	// AllPoints - массив для хранения всех найденных точек.
    	ArrayList<Element[]> AllPoints = new ArrayList<Element[]>();
    	// Создаем переменную f, в которую записываем f1-f2.
    	F f = (F)f1.subtract(f2, ring);
    	// Находим стартовую точку и добавляем ее в массив.
    	AllPoints.add(findStartPoint(coo, delta, Delta, f, ring));
    	// В переменной size содержится количество найденных точек.
    	int size = 1;
    	// В переменную mv записываем направление первого шага.
    	int mv = 0;
    	// counter - счетчик найденных точек в текущем направлении.
    	// Нужен, потому что одино из условий остановки программы 
    	// когда расстояние от стартовой точки до последней найденной
    	// точки меньше Delta. Когда ты ищешь первую точку посте стартовой,
    	// ты ищешь ее на расстоянии меньшем чем Delta. Поэтому я изменил
    	// условие остановки так, чтобы, когда расстояние между последней
    	// найденной точкой и стартовой точкой меньше Delta, то программа
    	// остановится только если counter > 10.
    	int counter = 0;
    	// Если flag присваевается значение true, когда мы просмотрели кривую в
    	// одном направлении от стартовой точки и ушли за границу области.
    	// В этом случае нам нужно просмотреть кривую в противоположном направлении
    	// от стартовой точки.
    	boolean flag = false;
    	do {
    		// Находим следующую точку.
    		size = AllPoints.size();
    		AllPoints.add(nextPoint(AllPoints.get(size-1)[0],
    				AllPoints.get(size-1)[1], coo, delta, Delta, f, ring) );
    		// Если текущий шаг - первый, то сохраняем в переменную
    		// mv текущее направление.
    		if(size == 1) {
    			mv = move;
    		}
    		// Если найденная точка вышла за границы области.
    		if( (coo[2].subtract(AllPoints.get(size)[0], ring).isNegative())||(AllPoints.get(size)[0].subtract(coo[0], ring).isNegative())||
    		    (coo[3].subtract(AllPoints.get(size)[1], ring).isNegative())||(AllPoints.get(size)[1].subtract(coo[1], ring).isNegative()) ) {
    			// Удаляем последнюю точку, так как она вне области.
    			AllPoints.remove(size);
    			// Если мы рассмотрели всю кривую, то возвращаем массив точек.
    			if(flag) {
    				return AllPoints;
    			}
    			// Меняем направление движение на следующее.
    			move = (mv+2)%8;
    			// Сбрасываем счетчик.
    			counter = 0;
    			flag = true;
    			// Ищем следующую точку.
    			AllPoints.add(nextPoint(AllPoints.get(0)[0],
    				AllPoints.get(0)[1], coo, delta, Delta, f, ring) );
    			// Если направление совпало с направлением самого первого шага,
    			// то значачит мы пошли по уже пройденной части кривой, и
    			// поэтому удаляем найденную точку и возвращаем массив всех точек.
    			if(move == mv) {
    				AllPoints.remove(size);
    				return AllPoints;
    			}
    		}
    		counter++;
    	} while( Delta.subtract(distance(AllPoints.get(0), AllPoints.get(size), ring) , ring).isNegative()||(counter < 10));
    	return AllPoints;
    }
    
    // Метод, считающий расстояние между точкой a и точкой b.
    static Element distance(Element[] a, Element[] b, Ring ring) {
    	Element result;
    	result = a[0].subtract(b[0], ring).multiply(a[0].subtract(b[0], ring), ring);
    	result = result.add(a[1].subtract(b[1], ring).multiply(a[1].subtract(b[1], ring), ring), ring);
    	return result.sqrt(ring);
    }
    
        public static void main(String[] args) {
    	Ring ring = new Ring("R[x, y]");
    	F f1 = new F("x^2+y^2", ring); // Задаем две пересекающиеся поверхности
    	F f2 = new F("1", ring);
    	// Запускаем нахождение точек.
    	Object o = findAllPoints(new Element[]{new NumberR("-"+4), new NumberR("-"+1),
        new NumberR(""+1), new NumberR(""+4)},  new NumberR(""+0.001), new NumberR(""+0.1), f2, f1, ring);
    	
    }
}
