package com.mathpar.Graphic2D.Element2D.components;

import java.util.function.UnaryOperator;

public interface PlanimetriaComponent {
    String getName();

    UnaryOperator<String> parseExpression();
}
