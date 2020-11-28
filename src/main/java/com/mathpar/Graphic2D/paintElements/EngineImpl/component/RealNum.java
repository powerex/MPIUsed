package com.mathpar.Graphic2D.paintElements.EngineImpl.component;

import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import lombok.*;

@Data
@RequiredArgsConstructor
public class RealNum implements GeometryVar {

    private @NonNull double value;
    private boolean displayed;

    @Override
    public ArgType getType() {
        return ArgType.REAL_NUM;
    }

    @Override
    public String draw() {
        return "";
    }
}
