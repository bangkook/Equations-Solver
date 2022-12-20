package com.codebind.LUDecomposition;

import com.codebind.Equation;
import com.codebind.LinearSolver;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;

public class Doolittle implements LinearSolver {

    private int precision = 7;
    boolean scaling = false;
    private final int order;
    private final Equation[] equations;
    private double[] ans;
    private int o[];
    private double[][] L;
    private String stepsFile = "Doolittle.txt";
    private long startTime;
    private long endTime;

    public Doolittle(Equation[] equations, boolean scaling) {
        this.order = equations[0].getOrder();
        this.ans = new double[this.order];
        this.equations = equations;
        this.scaling = scaling;
        this.L = new double[this.order][this.order];
        this.o = new int[this.order];
        for (int i = 0; i < this.order; i++)
            o[i] = i;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    @Override
    public double[] getSolution() {
        startTime = System.currentTimeMillis();
        decompose();
        substitute();
        endTime = System.currentTimeMillis();
        return this.ans;
    }

    private void decompose() {

        for (int i = 0; i < this.order - 1; i++) {
            pivot(i);
            writeFile();

            // if pivot is 0 after partial pivoting, skip it
            if (this.equations[o[i]].getPivot(false, i) == 0) {
                continue;
            }

            L[o[i]][i] = 1;

            for (int j = i + 1; j < this.order; j++) {

                double multiplier = round(this.equations[o[j]].getPivot(false, i) /
                        this.equations[o[i]].getPivot(false, i));
                L[o[j]][i] = multiplier;
                L[o[j]][j] = 1;

                eliminate(i, j, multiplier);
                writeFile();
            }
        }

        // if U[n][n] == 0
        if (this.equations[o[order - 1]].getPivot(false, order - 1) == 0) {
            // if b[n] = 0, then infinite solutions else no solution
            if (this.equations[o[order - 1]].getRes() == 0) {
                throw new RuntimeException("System has infinite number of solution");
            } else {
                throw new RuntimeException("System has no solution");
            }
        }
    }

    private void eliminate(int i, int j, double multiplier) {
        //equations[o[j]].add(equations[o[i]], multiplier, i);
        equations[o[j]].setCoefficient(i, 0);
        for (int k = i + 1; k < this.order; k++) {
            double coeff = round(multiplier * equations[o[i]].getCoefficient(k));
            coeff = round(equations[o[j]].getCoefficient(k) - coeff);
            equations[o[j]].setCoefficient(k, coeff);
        }
    }

    private void substitute() {
        // forward substitution
        for (int i = 1; i < this.order; i++) {
            double sum = this.equations[o[i]].getRes();
            for (int j = 0; j < i; j++) {
                sum = round(sum - round(L[o[i]][j] * this.equations[o[j]].getRes()));
            }
            this.equations[o[i]].setRes(sum);
        }

        // backward substitution
        for (int i = this.order - 1; i >= 0; i--) {
            this.ans[i] = this.equations[o[i]].substitute(this.ans, i + 1, this.order, i);
        }
    }

    private double round(double val) {
        return (new BigDecimal(Double.toString(val)).round(new MathContext(this.precision))).doubleValue();
    }

    private void pivot(int k) {
        double holder;
        int piv = k;
        double largest = Math.abs(this.equations[o[k]].getPivot(scaling, k));
        for (int i = k + 1; i < this.order; ++i) {
            holder = Math.abs(this.equations[o[i]].getPivot(scaling, k));
            if (holder > largest) {
                largest = holder;
                piv = i;
            }
        }
        // swapping
        holder = o[piv];
        o[piv] = o[k];
        o[k] = (int) holder;

    }

    private void writeFile() {//write steps function
        try {
            FileWriter writer = new FileWriter(stepsFile, true);
            int len = this.order;
            for (int f = 0; f < len; f++) {
                for (int p = 0; p < len; p++) {
                    // write L
                    writer.write(L[o[f]][p] + " ");
                }
                for (int p = 0; p < len; p++) {
                    // write U
                    writer.write(this.equations[o[f]].getCoefficient(p) + " ");
                }
                writer.write(this.equations[o[f]].getRes() + "\n");
            }
            writer.write("\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearFile() {
        PrintWriter writer;//clears the text file before write

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

    @Override
    public void print() {

    }

    @Override
    public long getTimer() {
        long totalTime = endTime - startTime;
        return totalTime;
    }

    @Override
    public String getSteps() {
        return stepsFile;
    }
}
