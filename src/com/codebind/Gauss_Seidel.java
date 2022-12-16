package com.codebind;

import java.io.*;
import java.util.ArrayList;

public class Gauss_Seidel implements LinearSolver {
    //private boolean stepByStep = false;
    private int precision = 7;
    private final int order;
    private final Equation[] equations;
    private double[] ans;
    private int maxIterations = 50;
    private double relativeError = 0.00001;
    private final String stepsFile = "gauss_seidel_steps.json";


    public Gauss_Seidel(Equation[] equations, double[] initial, int maxIterations, double relativeError) {
        this.equations = equations;
        this.order = equations[0].getOrder();
        this.ans = initial;
        this.maxIterations = maxIterations;
        this.relativeError = relativeError;
    }

    public Gauss_Seidel(Equation[] equations, double[] initial) {
        this.equations = equations;
        this.order = equations[0].getOrder();
        this.ans = initial;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    @Override
    public double[] getSolution() {
        double[] tempAns = new double[this.order];
        double error;
        this.reArrange();
        for (int i = 0; i < maxIterations; i++) {
            error = 0;
            for (int j = 0; j < this.order; j++) {
                tempAns[j] = this.equations[j].substitute(this.ans, 0, this.order, j, precision);

                error = Math.max(error, Math.abs((tempAns[j] - this.ans[j]) / tempAns[j]));

                ans[j] = tempAns[j];
            }
            //System.arraycopy(tempAns, 0, this.ans, 0, this.order);
            saveAns();
            for (double a : ans) {
                System.out.print(a + " ");
            }
            System.out.println();

            if (error <= this.relativeError)
                break;
        }
        return this.ans;
    }

    // reArrange rows to get diagonally dominant matrix
    private void reArrange() {
        for (int i = 0; i < this.order; i++) {
            double sum = 0;
            for (int j = 0; j < this.order; j++) {
                if (i == j)
                    continue;
                sum += Math.abs(equations[i].getCoefficient(j));
            }

            System.out.println(equations[i].getCoefficient(i) + " " + sum);
            if (Math.abs(equations[i].getCoefficient(i)) < sum)
                this.swap(i);

            System.out.println(equations[i].getCoefficient(i));

            // if pivot is zero after reArranging, raise error
            if (Math.abs(equations[i].getCoefficient(i)) < 1E-10) {
                System.out.println(equations[i].getCoefficient(i));
                throw new RuntimeException("Pivot can not be zero");
            }
        }
    }

    // swap rows to get diagonally dominant matrix
    private void swap(int row) {
        for (int i = row + 1, j; i < this.order; i++) {
            double sum = 0;
            for (j = 0; j < this.order; j++) {
                if (row == j)
                    continue;
                sum += Math.abs(equations[i].getCoefficient(j));
            }
            if (Math.abs(equations[i].getCoefficient(row)) >= sum) {
                Equation temp = equations[i];
                equations[i] = equations[row];
                equations[row] = temp;

                return;
            }
        }
    }

    private void saveAns() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(stepsFile, true));
            oos.writeObject(this.ans);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<double[]> getSteps() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(stepsFile);
        ArrayList<double[]> steps = new ArrayList<>();

        boolean cont = true;
        while (cont) {
            try (ObjectInputStream input = new ObjectInputStream(fis)) {
                double[] obj = (double[]) input.readObject();

                if (obj != null) {
                    steps.add(obj);
                    for (double v : obj) System.out.print(v + " ");
                    System.out.println();
                } else {
                    cont = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return steps;
    }

    @Override
    public void print() {

    }
}
