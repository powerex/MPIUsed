/*
 * Copyright © 2011 Mathparca Ltd. All rights reserved.
 */
package com.mathpar.Graphic2D.Element2D;

import com.mathpar.Graphic2D.Element2D.components.*;
import com.mathpar.Graphic2D.ReadFromFile;
import com.mathpar.func.F;
import com.mathpar.number.Ring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс Planimetria является общим для всех объектов пакета Element2D, т.е.
 * конструкторы print, toString работаеют сразу для нескольких планиметрических
 * объектов. ПРИМЕР p
 * =\paintElement('Многоугольник=вершины(0,0),(0,6),(6,6),(6,0)'); или
 * "Окружность =( центр( 0 , 0 ); радиус (5) ;Сектор=(0, 30); Касательная=(20 ,
 * 10));"; // "Эллипс = центр( 0 , 5 ); полуосьХ 6, полуосьY 2 ; угол( 30
 * );Сектор=(0, 90); Касательная=(90 , 10);"; // "Угол = значение 90, точки( (0,
 * 0) , (5, 0) , (0,0) )"; // "Прямая = (2 , 1);"; // "Отрезок = (начало(0 , 0),
 * конец (10, 10 ) ; деление( 2 : 1)) "; // "Точка = (1,1)"; // "Многоугольник =
 * вершины (0,0),(5,0),(5,5) ... "; // "Обозначения =( А(0 , 0), В(5, 0 ),
 * C(5,5)) "; // "Масштаб = (уменьшить) ";
 */
public class Planimetria {
    private final String path = System.getProperty("user.home") + "/image.png";
    private F elementF;
    private String title;
    private List<String> elements = new ArrayList<>();

    /**
     * Register parser components here
     * Create class that implements {@link PlanimetriaComponent} and add new instance here to enable parser to use it
     */
    private static Map<String, UnaryOperator<String>> parserComponents = Stream.of(
            new CircleComponent(),
            new DotComponent(),
            new LineSegmentComponent(),
            new PolygonComponent(),
            new TriangleComponent(),
            new HeightComponent(),
            new MedianComponent(),
            new BisectrixComponent(),
            new EllipseComponent()
    ).collect(Collectors.toMap(PlanimetriaComponent::getName, PlanimetriaComponent::parseExpression));

    /**
     *
     */
    private Planimetria() {

    }

    public interface Builder<T> {
        T build();
    }

    public static Builder<Planimetria> builder(String[] str) {
        return () -> {
            Planimetria instance = new Planimetria();
            instance.paint(str);
            return instance;
        };
    }

    public String getElements() {
        return "\\showPlots(" + elements + ");";
    }

    private void paint(final String[] str) {

        Ring r = new Ring("R64[]");
        r.setDefaultRing();
        ReadFromFile R = new ReadFromFile(str);
        title = str[0];

        elements = Arrays.stream(str)
                .map(expr -> parserComponents.getOrDefault(expr.split("=")[0].trim(), s -> s).apply(expr))
                .collect(Collectors.toList());
    }

    public String getPath() {
        return path;
    }

    public F getElementF() {
        return elementF;
    }

    public String getTitle() {
        return title;
    }
}
