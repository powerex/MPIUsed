package com.mathpar.web.entity;

import java.util.ArrayList;
import java.util.List;

public class Plots3DCollection {
    private List<List<double[]>> implicitPlots = new ArrayList<>();
    private List<List<double[]>> explicitPlots = new ArrayList<>();
    private List<List<double[]>> parametricPlots = new ArrayList<>();

    public List<List<double[]>> getImplicitPlots() {
        return implicitPlots;
    }

    public void setImplicitPlots(List<List<double[]>> implicitPlots) {
        this.implicitPlots = implicitPlots;
    }

    public List<List<double[]>> getExplicitPlots() {
        return explicitPlots;
    }

    public void setExplicitPlots(List<List<double[]>> explicitPlots) {
        this.explicitPlots = explicitPlots;
    }

    public List<List<double[]>> getParametricPlots() {
        return parametricPlots;
    }

    public void setParametricPlots(List<List<double[]>> parametricPlots) {
        this.parametricPlots = parametricPlots;
    }

    @Override
    public String toString() {
        return "Plots3DCollection{" +
                "implicitPlots=" + implicitPlots +
                ", explicitPlots=" + explicitPlots +
                ", parametricPlots=" + parametricPlots +
                '}';
    }

    public void appendImplicit(List<double[]> plot) {
        this.implicitPlots.add(plot);
    }

    public void appendExplicit(List<double[]> plot) {
        this.explicitPlots.add(plot);
    }

    public void appendParametric(List<double[]> plot) {
        this.parametricPlots.add(plot);
    }
}
