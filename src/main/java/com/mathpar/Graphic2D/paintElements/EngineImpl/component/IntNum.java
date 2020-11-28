package com.mathpar.Graphic2D.paintElements.EngineImpl.component;

import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import lombok.*;


@Data
@RequiredArgsConstructor
public class IntNum implements GeometryVar {


    private @NonNull int value;
    private boolean displayed;

    @Override
    public ArgType getType() {
        return ArgType.INT;
    }

    @Override
    public String draw() {
        return "";
    }

}
