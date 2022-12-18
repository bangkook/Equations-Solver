package com.codebind;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class Jacobi implements LinearSolver {
    //private boolean stepByStep = false;
    private int precision = 7;
    private final int order;
    private final Equation[] equations;
    private double[] ans;
    boolean scaling = false;
    private int maxIterations = 50;
    private double relativeError = 0.00001;
    //private final String stepsFile = "jacobi_steps.txt";
    private ArrayList<double[]> steps;


    public Jacobi(Equation[] equations, double[] initial, int maxIterations, double relativeError, boolean scaling) {
        this.equations = equations;
        this.order = equations[0].getOrder();
        this.ans = initial;
        this.scaling = scaling;
        this.maxIterations = maxIterations;
        this.relativeError = relativeError;
        this.steps = new ArrayList<>();
    }

    public Jacobi(Equation[] equations, double[] initial) {
        this.equations = equations;
        this.order = equations[0].getOrder();
        this.ans = initial;
        this.steps = new ArrayList<>();
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    @Override
    public double[] getSolution() {
        double[] tempAns = new double[this.order];
        double error;
        this.pivot();// partial pivoting first

        for (int i = 0; i < maxIterations; i++) {
            error = 0;
            for (int j = 0; j < this.order; j++) {
                tempAns[j] = round(this.equations[j].substitute(this.ans, 0, this.order, j, precision));
                // System.out.println(tempAns[j]);
                error = Math.max(error, Math.abs((tempAns[j] - this.ans[j]) / tempAns[j]));
            }
            System.arraycopy(tempAns, 0, this.ans, 0, this.order);
            saveAns();

            if (error <= this.relativeError)
                break;
        }
        return this.ans;
    }

    // partial pivoting to avoid division by zero
    private void pivot() {
        for (int k = 0; k < this.order; k++) {
            if (this.equations[k].getPivot(false, k) != 0) continue;
            double holder;
            int piv = k;
            double largest = Math.abs(this.equations[k].getPivot(scaling, k));
            for (int i = k + 1; i < this.order; ++i) {
                holder = Math.abs(this.equations[i].getPivot(scaling, k));
                if (holder > largest) {
                    largest = holder;
                    piv = i;
                }
            }
            System.out.println(k + " " + piv);
            // swapping
            Equation temp = equations[piv];
            equations[piv] = equations[k];
            equations[k] = temp;

            // if pivot is zero after partial pivoting, throw runtime exception
            if (equations[k].getPivot(false, k) == 0) {
                throw new RuntimeException("Pivot can not be zero");
            }
        }
    }

    private void saveAns() {
        double[] step = new double[this.order];
        System.arraycopy(this.ans, 0, step, 0, this.order);
        this.steps.add(step);
    }

    @Override
    public ArrayList<double[]> getSteps() {
        return this.steps;
    }

    private double round(double val) {
        return (new BigDecimal(Double.toString(val)).round(new MathContext(this.precision))).doubleValue();
    }

    @Override
    public void print() {
        for (double a : ans) {
            System.out.print(a + " ");
        }
        System.out.println();
    }


}
