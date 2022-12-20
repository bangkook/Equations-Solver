package com.codebind.LUDecomposition;

import com.codebind.Equation;
import com.codebind.LinearSolver;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;

public class CroutDecomposition implements LinearSolver {

    private int precision = 7;
    boolean scaling = false;
    private final int order;
    private final Equation[] equations;
    private double[] ans;
    private double[][] lowerMatrix;
    private int o[];
    private double[][] upperMatrix;
    private final String stepsFile = "Crout.txt";
    private long startTime;
    private long endTime;

    public CroutDecomposition(Equation[] equations, boolean scaling) {
        clearFile();
        this.equations = equations;
        this.order = equations[0].getOrder();
        this.precision = equations[0].getPrecision();
        this.ans = new double[this.order];
        this.scaling = scaling;
        lowerMatrix = new double[this.order][this.order];
        upperMatrix = new double[this.order][this.order];
        for (int i = 0; i < this.order; ++i) {
            for (int j = 0; j < this.order; ++j)
                lowerMatrix[i][j] = equations[i].getCoefficient(j);
        }
        this.o = new int[this.order];
        for (int i = 0; i < this.order; i++)
            o[i] = i;
    }

    @Override
    public double[] getSolution() {
        decompose();

        for (int i = 0; i < this.order; i++) {
            for (int j = 0; j < this.order; j++) {
                System.out.print(lowerMatrix[o[i]][j] + " ");
            }
            for (int j = 0; j < this.order; j++) {
                System.out.print(upperMatrix[o[i]][j] + " ");
            }
            System.out.println();

        }
        substitute();
        return this.ans;

    }

    // decomposition
    private void decompose() {
        double sum;

        for (int j = 0; j < this.order; ++j) {
            pivot(j);
            // if pivot is 0 after partial pivoting, skip it
            if (this.lowerMatrix[o[j]][j] == 0) {
                continue;
            }

            for (int i = j; i < this.order; ++i) {
                sum = 0;
                for (int k = 0; k < j; ++k)
                    sum = round(sum + round(lowerMatrix[o[i]][k] * upperMatrix[o[k]][j]));
                lowerMatrix[o[i]][j] = round(equations[o[i]].getCoefficient(j) - sum);
            }
            for (int i = j; i < this.order; ++i) {
                sum = 0;
                for (int k = 0; k < j; ++k)
                    sum = round(sum + round(lowerMatrix[o[j]][k] * upperMatrix[o[k]][i]));
                upperMatrix[o[j]][i] = round(round(equations[o[j]].getCoefficient(i) - sum) / lowerMatrix[o[j]][j]);
            }
        }
        for (int i = 0; i < this.order; i++) {
            upperMatrix[o[i]][i] = 1;
            for (int j = i + 1; j < this.order; j++) {
                lowerMatrix[o[i]][j] = 0;
                upperMatrix[o[j]][i] = 0;
            }
        }
        writeFile();

        // check if last pivot equals 0 after pivoting, then throw exception
        if (round(lowerMatrix[o[order - 1]][order - 1]) == 0) {
            if (equations[o[order - 1]].getCoefficient(order - 1) == 0) {
                throw new RuntimeException("System has no solution");
            } else {
                throw new RuntimeException("System has infinite number of solutions");
            }
        }
    }

    private void substitute() {
        double sum;

        // forward substitution
        double[] y = new double[this.order];
        y[o[0]] = this.equations[o[0]].getRes() / lowerMatrix[o[0]][0];
        for (int i = 0; i < this.order; ++i) {
            sum = 0;
            for (int j = 0; j <= i - 1; ++j) {
                sum = round(sum + round(lowerMatrix[o[i]][j] * y[o[j]]));
            }
            y[o[i]] = round(round(this.equations[o[i]].getRes() - sum) / lowerMatrix[o[i]][i]);
        }

        //backward substitution
        this.ans[this.order - 1] = y[o[this.order - 1]] / upperMatrix[o[this.order - 1]][this.order - 1];
        for (int i = this.order - 2; i >= 0; --i) {
            sum = 0;
            for (int j = i + 1; j < this.order; ++j) {
                sum = round(sum + round(upperMatrix[o[i]][j] * this.ans[j]));
            }
            this.ans[i] = round(round(y[o[i]] - sum) / upperMatrix[o[i]][i]);
        }
    }

    private double round(double val) {
        return (new BigDecimal(Double.toString(val)).round(new MathContext(this.precision))).doubleValue();
    }

    private void pivot(int k) {
        double holder;
        int piv = k;
        double largest = Math.abs(lowerMatrix[o[k]][k]);
        if (scaling)
            largest = round(largest / findLargestInRow(k));
        for (int i = k + 1; i < this.order; ++i) {
            holder = Math.abs(lowerMatrix[o[i]][k]);
            if (scaling)
                holder = round(holder / findLargestInRow(i));
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

    // get maximum element in row for scaling
    private double findLargestInRow(int k) {
        double max = 0;
        for (int i = 0; i < this.order; i++) {
            max = Math.max(max, Math.abs(lowerMatrix[o[k]][i]));
        }
        return max;
    }

    private void writeFile() {//write steps function
        try {
            FileWriter writer = new FileWriter(stepsFile, true);
            int len = this.order;
            for (int f = 0; f < len; f++) {
                for (int p = 0; p < len; p++) {
                    // write L
                    writer.write(lowerMatrix[o[f]][p] + " ");
                }
                for (int p = 0; p < len; p++) {
                    // write U
                    writer.write(upperMatrix[o[f]][p] + " ");
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


        try {
            writer = new PrintWriter(stepsFile);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void print() {

    }

    @Override
    public String getSteps() {
        return stepsFile;
    }

    @Override
    public long getTimer() {
        long totalTime = endTime - startTime;
        return totalTime;
    }
}
