package com.codebind;

import java.io.ObjectInputStream;

public class LUDecomposition implements LinearSolver{

    private int precision = 7;
    private double relativeError = 0.00001;
    private final int order;
    private final Equation[] equations;
    private double[] ans;
    private double[][] matrixA;
    private double[] matrixB;
    private double[] largestCoeffs;

    public LUDecomposition(Equation[] equations, double relativeError) {
        this.order = equations[0].getOrder();
        this.ans = new double[this.order];
        this.equations = equations;
        this.relativeError = relativeError;
        matrixA = new double[this.order][this.order];
        matrixB = new double[this.order];
        for(int i = 0; i < this.order; ++i){
            for(int j = 0; j < this.order; ++j) matrixA[i][j] = equations[i].getCoefficient(j);
        }
        for(int i = 0; i < this.order; ++i){
            matrixB[i] = equations[i].getRes();
        }
    }

    public int getPrecision() {
        return precision;
    }
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    @Override
    public double[] getSolution() {
        double factor;
        largestCoeffs = new double[this.order];
        for(int i = 0; i < this.order; ++i){
            largestCoeffs[i] = Math.abs(matrixA[i][0]);
            for(int j = 1; j < this.order; ++j){
                if(Math.abs(matrixA[i][j]) > largestCoeffs[i])
                    largestCoeffs[i] = Math.abs(matrixA[i][j]);
            }
        }
        // decomposition
        for(int k = 0; k < this.order-1; ++k){
            pivot(k);
            for(int i = k + 1; i < this.order; ++i){
                factor = matrixA[i][k] / matrixA[k][k];
                matrixA[i][k] = factor;
                for(int j = k + 1; j < this.order; ++j){
                    matrixA[i][j] = matrixA[i][j] - factor * matrixA[k][j];
                }
//                matrixB[i] = matrixB[i] - factor * matrixB[k];
            }
        }
        // forward substitution
        double[] y = new double[this.order];
        y[0] = matrixB[0];
        for(int i = 1; i < this.order; ++i){
            double sum = matrixB[i];
            for(int j = 0; j <= i - 1; ++j){
                sum = sum - matrixA[i][j] * y[j];
            }
            y[i] = sum;
        }
        // backward subistitusion
        this.ans[this.order - 1] = y[this.order - 1] / matrixA[this.order - 1][this.order - 1];
        for(int i = this.order - 2; i >= 0; --i){
            double sum = 0;
            for (int j = i + 1; j < this.order; ++j){
                sum = sum + matrixA[i][j] * this.ans[j];
            }
            this.ans[i] = (y[i] - sum) / matrixA[i][i];
        }

        return this.ans;
    }
    private void pivot(int k){
        double holder;
        int piv = k;
        double largest = Math.abs(this.matrixA[k][k] / largestCoeffs[k]);
        for(int i = k + 1; i < this.order; ++i){
            holder = Math.abs(matrixA[i][k] / largestCoeffs[i]);
            if(holder > largest){
                largest = holder;
                piv = i;
            }
        }
        // swaping
        if(piv != k){
            for(int j = k; j < this.order; ++j){
                holder = matrixA[piv][j];
                matrixA[piv][j] = matrixA[k][j];
                matrixA[k][j] = holder;
            }
            holder = matrixB[piv];
            matrixB[piv] = matrixB[k];
            matrixB[k] = holder;

            holder = largestCoeffs[piv];
            largestCoeffs[piv] = largestCoeffs[k];
            largestCoeffs[k] = holder;
        }
    }

    @Override
    public void print() {

    }

    @Override
    public ObjectInputStream getSteps() {
        return null;
    }
}
