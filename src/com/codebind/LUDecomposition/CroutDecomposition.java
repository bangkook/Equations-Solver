package com.codebind.LUDecomposition;

import com.codebind.Equation;
import com.codebind.LinearSolver;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class CroutDecomposition implements LinearSolver {

    private int precision = 7;
    boolean scaling = false;
    private final int order;
    private final Equation[] equations;
    private double[] ans;
    private double[][] lowerMatrix;
    private int o[];
    private double[][] upperMatrix;

    public CroutDecomposition(Equation[] equations, boolean scaling) {
        this.equations = equations;
        this.order = equations[0].getOrder();
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
        // swaping
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

    @Override
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    @Override
    public void print() {

    }

    @Override
    public ArrayList<double[]> getSteps() {
        return null;
    }
}
