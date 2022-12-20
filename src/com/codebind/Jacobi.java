package com.codebind;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;

public class Jacobi implements LinearSolver {
    //private boolean stepByStep = false;
    private int precision;
    private final int order;
    private final Equation[] equations;
    private double[] ans; // initial value
    boolean scaling = false;
    private int maxIterations = 50;
    private double relativeError = 0.00001;
    private final String stepsFile = "jacobi_steps.txt";
    private long startTime;
    private long endTime;


    public Jacobi(Equation[] equations, double[] initial, int maxIterations, double relativeError, boolean scaling) {
        this.equations = equations;
        this.order = equations[0].getOrder();
        this.precision = equations[0].getPrecision();
        this.ans = initial;
        this.scaling = scaling;
        this.maxIterations = maxIterations;
        this.relativeError = relativeError;
        this.clearFile();
    }

    public Jacobi(Equation[] equations, double[] initial) {
        this.equations = equations;
        this.order = equations[0].getOrder();
        this.ans = initial;
    }

    @Override
    public double[] getSolution() {
        startTime = System.currentTimeMillis();
        double[] tempAns = new double[this.order];
        double error;
        this.pivot();// pivoting first
        writeFile();

        for (int i = 0; i < maxIterations; i++) {
            error = 0;
            for (int j = 0; j < this.order; j++) {
                tempAns[j] = round(this.equations[j].substitute(this.ans, 0, this.order, j));
                // System.out.println(tempAns[j]);
                error = Math.max(error, Math.abs((tempAns[j] - this.ans[j]) / tempAns[j]));
            }
            System.arraycopy(tempAns, 0, this.ans, 0, this.order);
            writeFile();

            if (error <= this.relativeError)
                break;
        }
        endTime = System.currentTimeMillis();
        return this.ans;
    }

    // swap pivots to avoid division by zero
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

    private void writeFile() {
        try {
            FileWriter writer = new FileWriter(stepsFile, true);
            int len = this.order;
            for (int f = 0; f < this.order; f++) {
                writer.write(this.ans[f] + " ");
            }
            writer.write("\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFile() {
        PrintWriter writer;
        {
            try {
                writer = new PrintWriter(stepsFile);
                writer.print("");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private double round(double val) {
        return (new BigDecimal(Double.toString(val)).round(new MathContext(this.precision))).doubleValue();
    }

    @Override
    public String getSteps() {
        return stepsFile;
    }

    @Override
    public void print() {
        for (double a : ans) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    @Override
    public long getTimer() {
        long totalTime = endTime - startTime;
        return totalTime;
    }
}
