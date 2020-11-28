package com.mathpar.Graphic2D.paintElements.GeometryEngine.model;

import com.mathpar.Graphic2D.paintElements.EngineImpl.component.Point;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ScreenParams {
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;

    public ScreenParams() {
        this(-200, 200, -200, 200);
    }

    public boolean isInside(Point point) {
        return point.x >= xMin && point.x <= xMax
                && point.y >= yMin && point.y <= yMax;
    }

    public boolean isInside(List<Point> points) {
        return points.stream().allMatch(this::isInside);
    }

}
