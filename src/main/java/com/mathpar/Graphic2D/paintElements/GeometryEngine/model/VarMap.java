package com.mathpar.Graphic2D.paintElements.GeometryEngine.model;

import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.VarStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VarMap implements VarStorage {

    private Map<String, GeometryVar> varMap;

    public VarMap() {
        this.varMap = new HashMap<>();
    }

    @Override
    public void put(String name, GeometryVar var) {
        varMap.put(name, var);
    }

    @Override
    public GeometryVar get(String name) {
        return varMap.get(name);
    }

    @Override
    public Collection<GeometryVar> values() {
        return varMap.values();
    }

    @Override
    public boolean contains(String name) {
        return varMap.containsKey(name);
    }
}
