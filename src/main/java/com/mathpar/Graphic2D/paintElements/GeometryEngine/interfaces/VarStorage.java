package com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface VarStorage {

    // TODO: implement VarStorage
    // I prefer HashMap solution over List

    void put(String name, GeometryVar var);
    GeometryVar get(String name);
    Collection<GeometryVar> values();
    boolean contains(String name);

    default List<GeometryVar> get(List<String> names) {
        List<GeometryVar> vars = new ArrayList<>(names.size());
        for (String name : names) {
            vars.add(this.get(name));
        }

        return vars;
    }

}
