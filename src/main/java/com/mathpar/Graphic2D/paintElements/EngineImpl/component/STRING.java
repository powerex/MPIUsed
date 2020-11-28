package com.mathpar.Graphic2D.paintElements.EngineImpl.component;

import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class STRING implements GeometryVar {

    private @NonNull String value;
    private boolean displayed;

    @Override
    public ArgType getType() {
        return ArgType.STRING;
    }

    @Override
    public String draw() {
        return "";
    }
}
