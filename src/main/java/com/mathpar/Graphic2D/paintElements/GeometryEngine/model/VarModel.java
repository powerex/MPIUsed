package com.mathpar.Graphic2D.paintElements.GeometryEngine.model;

import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VarModel {
    private String name;
    private String label;
    private GeometryVar var;
    private String processedInput;
}
