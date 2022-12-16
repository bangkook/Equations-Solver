package com.codebind;

import java.util.ArrayList;

public class CroutDecomposition implements LinearSolver {

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

    public CroutDecomposition(Equation[] equations, double relativeError) {
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
        double sum;
        // decomposition
        for (int i = 0; i < this.order; ++i)
            lowerMatrix[i][0] = matrixA[i][0];
        for (int j = 1; j < this.order; ++j)
            upperMatrix[0][j] = matrixA[0][j] / lowerMatrix[0][0];

        for (int j = 1; j < this.order - 1; ++j) {
            for (int i = j; i < this.order; ++i) {
                sum = 0;
                for (int k = 0; k <= j - 1; ++k)
                    sum = sum + lowerMatrix[i][k] * upperMatrix[k][j];
                lowerMatrix[i][j] = matrixA[i][j] - sum;
            }
            for (int k = j + 1; k < this.order; ++k) {
                sum = 0;
                for (int i = 0; i <= j - 1; ++i)
                    sum = sum + lowerMatrix[j][i] * upperMatrix[i][k];
                upperMatrix[j][k] = (1 / lowerMatrix[j][j]) * (matrixA[j][k] - sum);
            }
        }
        sum = 0;
        for (int k = 0; k <= this.order - 2; ++k)
            sum = sum + lowerMatrix[this.order - 1][k] * upperMatrix[k][this.order - 1];
        lowerMatrix[this.order - 1][this.order - 1] = matrixA[this.order - 1][this.order - 1] - sum;

        for (int i = 0; i < this.order; ++i) {
            for (int j = 0; j < this.order; ++j) {
                if (i == j) upperMatrix[i][i] = 1;
            }
        }

        // forward subistitution
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
    public ArrayList<double[]> getSteps() {
        return null;
    }
}
