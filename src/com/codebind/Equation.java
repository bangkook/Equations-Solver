package com.codebind;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

public class Equation implements Serializable {
    private double[] coefficients;
    private int order;
    private double res;// b
    private int precision = 7;

    public Equation(double[] coefficients, double res) {
        this.coefficients = this.roundArr(coefficients);
        this.order = coefficients.length;
        this.res = this.round(res);
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
        this.coefficients = this.roundArr(coefficients);
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
        this.coefficients[index] = this.round(coefficient);
    }

    public void add(Equation equation, double multiplier, int col) {
        this.coefficients[col] = 0; // for LU case
        for (int i = col + 1; i < this.order; i++) {
            this.coefficients[i] -= round(equation.getCoefficient(i) * multiplier);
            this.coefficients[i] = round(this.coefficients[i]);
        }
        this.res = round(res - round(equation.getRes() * multiplier));
    }

    // returns pivot with or without scaling
    public int check(Equation[] equation) {
        int numOfFreeVar=0;
        double sum=0;
        for (int i = 0; i < this.order; i++) {
            if(equation[i].coefficients[i]==0 && equation[i].getRes()!= 0){
                return -2;//no solution
            }
            else if(equation[i].coefficients[i]==0 && equation[i].getRes()== 0){
                for(int j =0 ;j<this.order;j++){
                    sum = sum + equation[i].coefficients[j];
                }
                if(sum==0)
                    numOfFreeVar++;
                sum=0;
            }
        }
        if(numOfFreeVar==0) return -1;
        else return numOfFreeVar;
    }
    // returns pivot with or without scaling
    public double getPivot(boolean scaling, int row) {
        if (!scaling)
            return this.coefficients[row];

        double maxCoefficient = 0;
        for (double coefficient : coefficients) {
            maxCoefficient = Math.max(maxCoefficient, Math.abs(coefficient));
        }
        if (maxCoefficient==0)
            return this.coefficients[row];
        else
            return round(this.coefficients[row] / maxCoefficient);
    }

    private double round(double val) {
        return (new BigDecimal(Double.toString(val)).round(new MathContext(this.precision))).doubleValue();
    }

    private double[] roundArr(double[] values) {
        for (int i = 0; i < values.length; i++) {
            values[i] = this.round(values[i]);
        }
        return values;
    }

    public double substitute(double[] ans, int startIndex, int endIndex, int currIndex, int precision) {
        this.precision = precision;
        double sum = 0;
        for (int i = startIndex; i < endIndex; i++) {
            if (i == currIndex)
                continue;
            sum = this.round(sum + this.coefficients[i] * ans[i]);

        }
        if( this.coefficients[currIndex]!=0)
            return this.round(this.round(this.res - sum) / this.coefficients[currIndex]);
        else  return this.round(this.res - sum);
    }
}
