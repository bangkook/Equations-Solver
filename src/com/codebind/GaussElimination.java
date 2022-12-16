package com.codebind;

import java.util.ArrayList;


public class GaussElimination implements LinearSolver {
    private int precision = 7;
    private final int order;
    private final Equation[] equations;
    private double[] ans;
    private double relativeError = 0.00001;

    public GaussElimination(Equation[] equations, double relativeError) {
        this.order = equations[0].getOrder();
        this.equations = equations;
        this.relativeError = relativeError;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    @Override
    public double[] getSolution() {
        double maxPivot = 0.0;
        int indexMaxPivot = 0;
        int nextpivot = 0;
        this.ans = new double[this.order];
        while (nextpivot != this.order) {
            for (int i = nextpivot; i < this.order; i++) {
                if (Math.abs(equations[i].getCoefficient(nextpivot)) > Math.abs(maxPivot)) {
                    maxPivot = equations[i].getCoefficient(nextpivot);
                    indexMaxPivot = i;
                }
            }
            //swap
            if (maxPivot != 0) {
                Equation temp = equations[nextpivot];
                equations[nextpivot] = equations[indexMaxPivot];
                equations[indexMaxPivot] = temp;
            }
            //mul and sub
            for (int i = nextpivot + 1; i < this.order; i++) {
                double multiplier = equations[i].getCoefficient(nextpivot) / equations[nextpivot].getCoefficient(nextpivot);
                equations[i].add(equations[nextpivot], multiplier, -1);
            }
            maxPivot = 0;
            indexMaxPivot = 0;
            nextpivot++;
        }
        for (int i = this.order - 1; i >= 0; i--) {
            ans[i] = this.equations[i].substitute(this.ans, i + 1, this.order, i, precision);
        }
        return this.ans;
    }

    @Override
    public void print() {

    }

    @Override
    public ArrayList<double[]> getSteps() {
        return null;
    }
}
