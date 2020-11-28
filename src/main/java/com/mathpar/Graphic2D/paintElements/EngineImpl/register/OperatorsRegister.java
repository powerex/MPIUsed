package com.mathpar.Graphic2D.paintElements.EngineImpl.register;

import com.mathpar.Graphic2D.paintElements.EngineImpl.operator.*;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.Operator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OperatorsRegister {

    public static final List<Operator> OPERATORS = Collections.unmodifiableList(Arrays.asList(
            new CircleByRadius(),
            new CircleByRadiusAndCenter(),
            new CirclesCross(),
            new EquilateralTriangleBySideAndCentre(),
            new InfiniteLineByKAndB(),
            new InfiniteLineByTwoPoints(),
            new LineCircleCross(),
            new LineOperator(),
            new MiddleOfLine(),
            new NormalFromPointToLine(),
            new NormalFromPointToLineByLength(),
            new PointOperator(),
            new RayByTwoPoints(),
            new RegularHexagonBySideAndCenter(),
            new RegularOctagonBySideAndCentre(),
            new RegularPentagonBySideAndCenter(),
            new RegularPolygonByN(),
            new RegularPolygonByNAndSideLength(),
            new RegularPolygonByNAndSideLengthAndCenter(),
            new TextByStartPoint(),
            new TextByStartPointAndFontSize(),
            new TriangleByPoints(),
            new EllipseByWidthAndHeight(),
            new EllipseByWidthAndHeightAndCenter(),
            new Circumcircle()
    ));

}
