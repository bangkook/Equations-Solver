package com.codebind;

import java.io.ObjectInputStream;

public class Jacobi implements LinearSolver {
    //private boolean stepByStep = false;
    private int precision = 7;
    private final int order;
    private final Equation[] equations;
    private double[] ans;
    private int maxIterations = 50;
    private double relativeError = 0.00001;


    public Jacobi(Equation[] equations, double[] initial, int maxIterations, double relativeError) {
        this.equations = equations;
        this.order = equations[0].getOrder();
        this.ans = initial;
        this.maxIterations = maxIterations;
        this.relativeError = relativeError;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public double[] getSolution() {
        double[] tempAns = new double[this.order];
        double error;

        for (int i = 0; i < maxIterations; i++) {
            error = 0;
            for (int j = 0; j < this.order; j++) {
                tempAns[j] = this.equations[j].substitute(this.ans, 0, this.order, j);
                error = Math.max(error, Math.abs((tempAns[j] - this.ans[j]) / tempAns[j]));
            }
            System.arraycopy(tempAns, 0, this.ans, 0, this.order);
            if (error <= this.relativeError)
                break;
        }
        return this.ans;
    }

    public ObjectInputStream getSteps() {
        return null;
    }
}
