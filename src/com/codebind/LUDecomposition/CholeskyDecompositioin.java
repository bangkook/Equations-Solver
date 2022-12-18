package com.codebind.LUDecomposition;

import com.codebind.Equation;
import com.codebind.LinearSolver;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class CholeskyDecompositioin implements LinearSolver {

    private int precision = 7;
    boolean scaling = false;
    private final int order;
    private final Equation[] equations;
    private double[] ans;
    private double[][] lowerMatrix;

    public CholeskyDecompositioin(Equation[] equations, boolean scaling) {
        this.equations = equations;
        this.order = equations[0].getOrder();
        this.ans = new double[this.order];
        this.scaling = scaling;
        lowerMatrix = new double[this.order][this.order];
    }

    @Override
    public double[] getSolution() {
        if (!checkSymmetry())
            throw new RuntimeException("Matrix is not symmetric");

        decompose();
        // getting upper matrix by transposing lower matrix
        /*for (int i = 0; i < this.order; ++i) {
            for (int j = 0; j < this.order; ++j)
                upperMatrix[i][j] = lowerMatrix[j][i];
        }*/

        for (int i = 0; i < this.order; i++) {
            for (int j = 0; j < this.order; j++) {
                System.out.print(lowerMatrix[i][j] + " ");
            }
            /*for (int j = 0; j < this.order; j++) {
                System.out.print(upperMatrix[i][j] + " ");
            }*/
            System.out.println();

        }
        substitute();
        return this.ans;
    }

    @Override
    public void print() {

    }

    @Override
    public void setPrecision(int precision) {
        this.precision = precision;

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

    @Override
    public ArrayList<double[]> getSteps() {
        return null;
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
}
