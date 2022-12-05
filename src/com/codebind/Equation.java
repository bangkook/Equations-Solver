package com.codebind;

import java.io.Serializable;

public class Equation implements Serializable {
    private double[] coefficients;
    private int order;
    private double res;// b

    public Equation(double[] coefficients, double res) {
        this.coefficients = coefficients;
        this.order = coefficients.length;
        this.res = res;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(double[] coefficients) {
        this.coefficients = coefficients;
    }

    public double getRes() {
        return res;
    }

    public void setRes(double res) {
        this.res = res;
    }

    public double getCoefficient(int index) {
        return coefficients[index];
    }

    public void setCoefficient(int index, double coefficient) {
        this.coefficients[index] = coefficient;
    }

    public void add(Equation equation, double multiplier, int col) {
        //this.coefficients[col] = multiplier; // for LU case
        for (int i = col + 1; i < this.order; i++) {
            this.coefficients[i] -= equation.getCoefficient(i) * multiplier;
        }
        this.res -= equation.getRes() * multiplier;
    }

    // returns pivot after scaling
    public double getPivot() {
        double maxCoefficient = 0;
        for (double coefficient : coefficients) {
            maxCoefficient = Math.max(maxCoefficient, Math.abs(coefficient));
        }
        return this.coefficients[0] / maxCoefficient;
    }

    public double substitute(double[] ans, int startIndex, int endIndex, int currIndex) {
        double sum = 0;
        for (int i = startIndex; i < endIndex; i++) {
            if (i == currIndex)
                continue;
            sum = sum + this.coefficients[i] * ans[i];
        }
        //System.out.println("this.result:"+ this.res );
        return (this.res - sum) / this.coefficients[currIndex];
    }
}
