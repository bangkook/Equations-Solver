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
    private int maxIterations = 50;
    private double relativeError = 0.00001;
    //private final String stepsFile = "jacobi_steps.txt";
    private ArrayList<double[]> steps;


    public Jacobi(Equation[] equations, double[] initial, int maxIterations, double relativeError) {
        this.equations = equations;
        this.order = equations[0].getOrder();
        this.ans = initial;
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
        this.reArrange();
        for (int i = 0; i < maxIterations; i++) {
            error = 0;
            for (int j = 0; j < this.order; j++) {
                tempAns[j] = this.equations[j].substitute(this.ans, 0, this.order, j, precision);

                error = Math.max(error, Math.abs((tempAns[j] - this.ans[j]) / tempAns[j]));
            }
            System.arraycopy(tempAns, 0, this.ans, 0, this.order);
            saveAns();

            if (error <= this.relativeError)
                break;
        }
        return this.ans;
    }

    // reArrange rows to get diagonally dominant matrix
    private void reArrange() {
        for (int i = 0; i < this.order; i++) {
            double sum = 0;
            for (int j = 0; j < this.order; j++) {
                if (i == j)
                    continue;
                sum = this.round(sum + Math.abs(equations[i].getCoefficient(j)));
            }

            if (Math.abs(equations[i].getCoefficient(i)) < sum)
                this.swap(i);

            // if pivot is zero after reArranging, raise error
            if (Math.abs(equations[i].getCoefficient(i)) == 0) {
                throw new RuntimeException("Pivot can not be zero");
            }
        }
    }

    // swap rows to get diagonally dominant matrix
    private void swap(int row) {
        for (int i = row + 1, j; i < this.order; i++) {
            double sum = 0;
            for (j = 0; j < this.order; j++) {
                if (row == j)
                    continue;
                sum = this.round(sum + Math.abs(equations[i].getCoefficient(j)));
            }
            // if found row with pivot greater than or equal sum of other elements in same row
            // or if pivot is zero and found row with non-zero pivot, swap both equations
            if (Math.abs(equations[i].getCoefficient(row)) >= sum ||
                    (equations[i].getCoefficient(row) != 0 && equations[row].getCoefficient(row) == 0)) {
                Equation temp = equations[i];
                equations[i] = equations[row];
                equations[row] = temp;

                return;
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
