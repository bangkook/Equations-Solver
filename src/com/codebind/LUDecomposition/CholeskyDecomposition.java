package com.codebind.LUDecomposition;

import com.codebind.Equation;
import com.codebind.LinearSolver;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;

public class CholeskyDecomposition implements LinearSolver {

    private int precision = 7;
    boolean scaling = false;
    private final int order;
    private final Equation[] equations;
    private double[] ans;
    private double[][] lowerMatrix;
    private long startTime;
    private long endTime;
    private final String stepsFile = "Cholesky.txt";

    public CholeskyDecomposition(Equation[] equations, boolean scaling) {
        clearFile();
        this.equations = equations;
        this.order = equations[0].getOrder();
        this.ans = new double[this.order];
        this.scaling = scaling;
        lowerMatrix = new double[this.order][this.order];
    }

    @Override
    public double[] getSolution() {
        startTime = System.currentTimeMillis();
        if (!checkSymmetry())
            throw new RuntimeException("Matrix is not symmetric");

        decompose();
        writeFile();
        substitute();
        endTime = System.currentTimeMillis();
        return this.ans;
    }

    @Override
    public void print() {

    }


    private double round(double val) {
        return (new BigDecimal(Double.toString(val)).round(new MathContext(this.precision))).doubleValue();
    }

    private void decompose() {
        // calculating lower matrix
        double sum;
        for (int i = 0; i < this.order; ++i) {
            for (int j = 0; j <= i; ++j) {
                //  if (i < j) lowerMatrix[i][j] = 0;
                sum = 0;
                for (int k = 0; k < j; k++) {
                    sum += round(lowerMatrix[i][k] * lowerMatrix[j][k]);
                    sum = round(sum);
                }
                if (i == j) {
                    lowerMatrix[i][i] = round(Math.sqrt(round(equations[i].getCoefficient(i) - sum)));
                } else {
                    lowerMatrix[i][j] = round(equations[i].getCoefficient(j) - sum);
                    lowerMatrix[i][j] = round(lowerMatrix[i][j] * round(1.0 / lowerMatrix[j][j]));
                }
            }
            if (lowerMatrix[i][i] == 0) {
                throw new RuntimeException("Matrix not positive definite");
            }
        }
    }

    private void substitute() {
        double sum;
        double[] y = new double[this.order];
        // forward substitution
        y[0] = round(equations[0].getRes() / lowerMatrix[0][0]);
        for (int i = 0; i < this.order; ++i) {
            sum = 0;
            for (int j = 0; j <= i - 1; ++j) {
                sum = round(sum + round(lowerMatrix[i][j] * y[j]));
            }
            y[i] = round(round(equations[i].getRes() - sum) / lowerMatrix[i][i]);
        }

        //backward substitution
        this.ans[this.order - 1] = round(y[this.order - 1] / lowerMatrix[this.order - 1][this.order - 1]);
        for (int i = this.order - 2; i >= 0; --i) {
            sum = 0;
            for (int j = i + 1; j < this.order; ++j) {
                sum = round(sum + round(lowerMatrix[j][i] * this.ans[j]));
            }
            this.ans[i] = round(round(y[i] - sum) / lowerMatrix[i][i]);
        }
    }

    private boolean checkSymmetry() {
        for (int i = 0; i < this.order; ++i) {
            for (int j = 0; j < this.order; ++j) {
                if (equations[i].getCoefficient(j) != equations[j].getCoefficient(i))
                    return false;
            }
        }
        return true;
    }

    private void writeFile() {//write steps function
        try {
            FileWriter writer = new FileWriter(stepsFile, true);
            int len = this.order;
            for (int f = 0; f < len; f++) {
                for (int p = 0; p < len; p++) {
                    // write L
                    if (f >= p)
                        writer.write(lowerMatrix[f][p] + " ");
                    else
                        writer.write("0 ");
                }
                for (int p = 0; p < len; p++) {
                    // write U
                    if (f <= p)
                        writer.write(lowerMatrix[f][p] + " ");
                    else
                        writer.write("0 ");
                }
                writer.write(this.equations[f].getRes() + "\n");
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
    public long getTimer() {
        long totalTime = endTime - startTime;
        return totalTime;
    }

    @Override
    public String getSteps() {
        return stepsFile;
    }
}

