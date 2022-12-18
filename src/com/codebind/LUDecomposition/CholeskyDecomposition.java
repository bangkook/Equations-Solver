package com.codebind.LUDecomposition;

import com.codebind.Equation;
import com.codebind.LinearSolver;

import java.util.ArrayList;

public class CholeskyDecomposition implements LinearSolver {

    private int precision = 7;
    private double relativeError = 0.00001;
    private final int order;
    private final Equation[] equations;
    private double[] ans;
    private double[][] matrixA;
    private double[] matrixB;
    private double[] largestCoeffs;
    private double[][] lowerMatrix;
    private double[][] upperMatrix;

    public CholeskyDecomposition(Equation[] equations, double relativeError) {
        this.equations = equations;
        this.order = equations[0].getOrder();
        this.ans = new double[this.order];
        this.relativeError = relativeError;
        matrixA = new double[this.order][this.order];
        matrixB = new double[this.order];
        for (int i = 0; i < this.order; ++i) {
            for (int j = 0; j < this.order; ++j) matrixA[i][j] = equations[i].getCoefficient(j);
        }
        for (int i = 0; i < this.order; ++i) {
            matrixB[i] = equations[i].getRes();
        }
        lowerMatrix = new double[this.order][this.order];
        upperMatrix = new double[this.order][this.order];
    }

    @Override
    public double[] getSolution() {
        if (checkSymmetry() == false) return null;
        // calculating lower matrix
        double sum;
        for (int j = 0; j < this.order; ++j) {
            for (int i = 0; i < this.order; ++i) {
                if (i < j) lowerMatrix[i][j] = 0;
                else if (i == j) {
                    sum = 0;
                    for (int k = 0; k <= i - 1; ++k) {
                        sum = sum + Math.pow(lowerMatrix[i][k], 2);
                    }
                    lowerMatrix[i][i] = Math.sqrt(matrixA[i][i] - sum);
                } else {
                    sum = 0;
                    for (int k = 0; k <= j - 1; ++k) {
                        sum = sum + lowerMatrix[j][k] * lowerMatrix[i][k];
                    }
                    lowerMatrix[i][j] = (matrixA[i][j] - sum) / lowerMatrix[j][j];
                }
            }
        }
        // getting upper matrix by transposing lower matrix
        for (int i = 0; i < this.order; ++i) {
            for (int j = 0; j < this.order; ++j)
                upperMatrix[i][j] = lowerMatrix[j][i];
        }
        // forward substitustion
        double[] y = new double[this.order];
        y[0] = matrixB[0] / lowerMatrix[0][0];
        for (int i = 0; i < this.order; ++i) {
            sum = 0;
            for (int j = 0; j <= i - 1; ++j) {
                sum = sum + lowerMatrix[i][j] * y[j];
            }
            y[i] = (matrixB[i] - sum) / lowerMatrix[i][i];
        }
        //backward subistitution
        this.ans[this.order - 1] = y[this.order - 1] / upperMatrix[this.order - 1][this.order - 1];
        for (int i = this.order - 2; i >= 0; --i) {
            sum = 0;
            for (int j = i + 1; j < this.order; ++j) {
                sum = sum + upperMatrix[i][j] * this.ans[j];
            }
            this.ans[i] = (y[i] - sum) / upperMatrix[i][i];
        }
        return this.ans;
    }

    @Override
    public void print() {

    }

    @Override
    public void setPrecision(int precision) {

    }

    @Override
    public ArrayList<double[]> getSteps() {
        return null;
    }

    private boolean checkSymmetry() {
        for (int i = 0; i < this.order; ++i) {
            for (int j = 0; j < this.order; ++j) {
                if (matrixA[i][j] != matrixA[j][i]) return false;
            }
        }
        return true;
    }
}
