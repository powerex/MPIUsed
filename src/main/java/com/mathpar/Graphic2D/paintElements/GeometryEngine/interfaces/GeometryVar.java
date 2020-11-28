package com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces;

import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;

public interface GeometryVar {

    // TODO: implement variables of different types

    boolean isDisplayed();
    void setDisplayed(boolean displayed);

    ArgType getType();

    String draw();
}
