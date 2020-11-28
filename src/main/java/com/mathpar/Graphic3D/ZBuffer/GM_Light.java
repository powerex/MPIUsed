/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.ZBuffer;


/**
 *
 * <p>Title:<p>Класс предназначен для установки освещения сцены. </p>
 * <p>Description: Существует возможность
 * устанавливать как один источник света, так и несколько,задавать интенсивность источника света.
 * Поскольку коэффициент диффузного отражения света зависит от материала и длины волны,
 * для простоты расчета считается постоянным-выбирается константой [0..1].Поскольку для расчета освещения сцены распределенным источником света требуются большие вычислительные затраты,
 * то в данной работе будет рассматриваться точечный тип источника света, а соответственно коэффициент рассеянного света возможно выбирать константой.Для зеркального отражения света
 * задаются такие параметры как коэффициент отражения,зависящий от свойств поверхности освещаемого объекта  и степень n косинуса угла между вектором отражения света и вектором наблюдения.Большие значения n дают
 * сфокусированные пространственные распределения характеристик металлов и других блестящих поверхностей</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @Екатерина Гордеева
 * @version 1.0
 */
public class GM_Light {

    /**
     * массив,содержащий положение каждого из источников света
     */
    public GM_Vector[] lights;
    /**
     * массив предназначен для задания интенсивности каждого из источников света
     */
    public double[] Jl;
    /**
     *содержит интенсивность рассеянного света
     */
    private double Ja;
    /**
     * степень,аппроксимирующая пространственное распределение зеркально отраженного света
     */
    private int n;
    /**
     * переменная отвечает за расстояние от источника света до объекта
     */
    private double dist; // = 1;//(double)Math.log(1/distance*distance);//1;
    /**
     * кривая отражения представляющая отношение зеркально отраженного света
     * к падающему(выбирается константой из эстетических соображений (в силу своей сложности))
     */
    private double Ks;
    /**
     * коэффициент диффузного отражения  рассеянного света выбирается из отрезка [0,1]
     */
    private double Ka;
    /**
     * коэффициент диффузного отражения света выбирается из отрезка [0,1]
     */
    private double Kd;
    /**
     * конструктор класса отвечает за задание всех атрибутов освещения
     */
    public GM_Light(GM_Vector[] lights, double[] Jl, int n, double Ja, double Ks,
                    double Ka, double Kd) {

        this.lights = lights;
        this.n = n;
        this.Jl = Jl;
        this.Ja = Ja;
        this.Ka = Ka;
        this.Kd = Kd;
        this.Ks = Ks;

    }

    /**
     * метод находит коэффициент интенсивности отраженного света от плоскости,используя функцию закраски:
     * <p>J = Ja*Ka + 1/(dist+K)*Jl*(Kd*cosnk+ Ks*cosRS^n), где<p> cosnk-косинус угла между вектором  света и вектором
     * нормали к плоскости,cosRS-косинус угла между вектором отражения света и вектором наблюдения
     *
     * @param p0 Point-вершины треугольника
     * @param p1 Point
     * @param p2 Point
     * @param viewer Point- положение наблюдателя
     * @return double
     */
    public double Light(GM_Vector p0, GM_Vector p1, GM_Vector p2, GM_Vector dist) {
        int K = 1;

        double
                A = ((p2.z - p0.z) * (p1.y - p0.y) -
                     (p1.z - p0.z) * (p2.y - p0.y)),
                    B = -((p2.z - p0.z) * (p1.x - p0.x) -
                          (p1.z - p0.z) * (p2.x - p0.x)),
                        C = ((p1.x - p0.x) * (p2.y - p0.y) -
                             (p2.x - p0.x) * (p1.y - p0.y));
        GM_Vector R;
        double cosRS, cosnk, J, Jo = 0;
        int m = lights.length;
        for (int i = 0; i < m; i++) {
            double lightDistance;
            if (dist == null) {
                lightDistance = (double) Math.sqrt(lights[i].x *
                                                  lights[i].x +
                                                  lights[i].y * lights[i].y +
                                                  lights[i].z * lights[i].z);
            } else {
                lightDistance = (double) Math.sqrt(
                        (dist.x - lights[i].x) * (dist.x - lights[i].x) +
                        (dist.y - lights[i].y) * (dist.y - lights[i].y) +
                        (dist.z - lights[i].z) * (dist.z - lights[i].z));
            }
            //    R = new Point(2*C*A, 2*C*B,2*C*C-1,1);
            R = new GM_Vector( -1 * lights[i].x, -1 * lights[i].y, lights[i].z);
            //косинус угла между вектором отражения и  вектором наблюдателя
            cosRS = (Math.abs( R.z * 1000) /
                             (Math.sqrt(R.x * R.x + R.y * R.y + R.z * R.z) * 1000 ));

            //вычисляем косинус угла между вектором нормали и вектором падения света

            cosnk = (Math.abs(A * lights[i].x + B * lights[i].y + //abs!
                                      C * lights[i].z) /
                             (Math.sqrt(A * A + B * B + C * C) *
                              lightDistance));
            double dist1 = 0;
            //функция закраски
            Jo += 1 / (dist1 + K) * Jl[i] *
                    (Kd * cosnk + Ks * (double) Math.pow(cosRS, n));
        }
        J = Jo + Ja * Ka;
        return J;
    }

    /**
     * метод аналогичен предыдущему, предназначен для
     * нахождения коэффициента интенсивности отраженного света от линии
     * @param p0 Point-граничные точки отрезка
     * @param p1 Point
     * @param viewer Point-положение наблюдателя
     * @return double
     */
    public double IlluminationLine(GM_Vector p0, GM_Vector p1,
                                  GM_Vector viewer /*,double dist*/) {
        int K = 1;
        dist = 0;
        int m = lights.length;
        GM_Vector R;
        double cosRS, J, Jo = 0;
        for (int i = 0; i < m; i++) {
            double
                    ab = (p0.x - p1.x) * (lights[i].x - p1.x) +
                         (p0.y - p1.y) * (lights[i].y - p1.y) +
                         (p0.z - p1.z) * (lights[i].z - p1.z),

                         a = (p0.x - p1.x) * (p0.x - p1.x) +
                             (p0.y - p1.y) * (p0.y - p1.y) +
                             (p0.z - p1.z) * (p0.z - p1.z),

                             b = (lights[i].x - p1.x) * (lights[i].x - p1.x) +
                                 (lights[i].y - p1.y) * (lights[i].y - p1.y) +
                                 (lights[i].z - p1.z) * (lights[i].z - p1.z),

                                 cos = (double) ( /*Math.abs*/(ab)) /
                                       (double) (Math.sqrt(a * b));
            cos *= cos;
            double sin = (double) (Math.sqrt(1 - cos));

            R = new GM_Vector( -1 * lights[i].x, -1 * lights[i].y, lights[i].z);
            cosRS = (double) ( /*Math.abs*/(R.x * viewer.x + R.y * viewer.y +
                                           R.z * viewer.z) /
                                          (Math.sqrt(R.x * R.x + R.y * R.y +
                    R.z * R.z) *
                                           Math.sqrt(viewer.x * viewer.x +
                    viewer.y * viewer.y +
                    viewer.z * viewer.z)));
            Jo += 1 / (dist + K) * Jl[i] *
                    (Kd * sin + Ks * (double) Math.pow(cosRS, n));
        }
        J = Jo + Ja * Ka;
        return J;
    }
}
