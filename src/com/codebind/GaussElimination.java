package com.codebind;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;


public class GaussElimination implements LinearSolver {
    private int precision = 7;
    private final int order;
    private final Equation[] equations;
    private double[] ans;
    private boolean scaling;
    private double relativeError = 0.00001;
    private long startTime;
    private long endTime;

    public GaussElimination(Equation[] equations, boolean scaling) {
        this.order = equations[0].getOrder();
        this.equations = equations;
        this.scaling = scaling;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    @Override
    public double[] getSolution() {
        int nextPivot = 0;
        double maxPivot = Math.abs((this.equations[nextPivot].getPivot(this.scaling, nextPivot)));
        int indexMaxPivot = nextPivot;
        this.ans = new double[this.order];
        while (nextPivot != this.order) {
            for (int i = nextPivot; i < this.order; i++) {
                if (Math.abs((this.equations[i].getPivot(this.scaling, nextPivot))) > maxPivot) {
                    maxPivot = Math.abs((this.equations[i].getPivot(this.scaling, nextPivot)));
                    indexMaxPivot = i;
                }
            }

            //swap
            writeFile();
            if (indexMaxPivot != nextPivot) {
                Equation temp = equations[nextPivot];
                equations[nextPivot] = equations[indexMaxPivot];
                equations[indexMaxPivot] = temp;
                writeFile();
            }


//            for (int i = 0; i < this.order; i++) {
//                for (int j = 0; j < this.order; j++) {
//                    System.out.print(this.equations[i].getCoefficient(j) + " ");
//                }
//                System.out.println("res:" + this.equations[i].getRes() + " ");
//            }System.out.println('\n');

            //mul and sub
            for (int i = nextPivot + 1; i < this.order; i++) {

                if (equations[nextPivot].getCoefficient(nextPivot) != 0) {
                    double multiplier = round(equations[i].getCoefficient(nextPivot) / equations[nextPivot].getCoefficient(nextPivot));
                    equations[i].add(equations[nextPivot], multiplier, nextPivot);
                }
            }
            writeFile();

            if (nextPivot == this.order - 1) {
                //check for no solution or infinite solution
                if (this.equations[nextPivot].check(equations) == -2) {
                    System.out.println("no solution");
                    return null;
                } else if (this.equations[nextPivot].check(equations) != -2 && this.equations[nextPivot].check(equations) != -1) {

                    int noOfFreeVar = this.equations[nextPivot].check(equations);
                    System.out.println(noOfFreeVar);
                    int value = 1;
                    int k = 0;
                    while (noOfFreeVar != 0) {
                        for (int j = k; j < this.order; j++) {
                            if (this.equations[j].getCoefficient(j) == 0) {
                                ans[j] = value;
                                value++;
                                k = j;
                                break;
                            }
                        }
                        k = k + 1;
                        noOfFreeVar--;
                    }
                    for (int m = this.order - 1; m >= 0; m--) {
                        if (ans[m] == 0) {
                            ans[m] = this.equations[m].substitute(this.ans, m + 1, this.order, m);
                        }
                    }
                    for (int j = 0; j < this.order; j++) {
                        System.out.println("the result " + ans[j]);
                    }
                    return ans;
                }
            }

            maxPivot = 0;
            nextPivot++;
            indexMaxPivot = nextPivot;

//            for (int i = 0; i < this.order; i++) {
//                for (int j = 0; j < this.order; j++) {
//                    System.out.print(this.equations[i].getCoefficient(j) + " ");
//                }
//                System.out.println("res:" + this.equations[i].getRes() + " ");
//            }System.out.println('\n');


        }

        //solution

        for (int i = this.order - 1; i >= 0; i--) {
            ans[i] = this.equations[i].substitute(this.ans, i + 1, this.order, i);
        }
        return this.ans;
    }


    private double round(double val) {
        return (new BigDecimal(Double.toString(val)).round(new MathContext(this.precision))).doubleValue();
    }

    public void writeFile() {
        try {
            FileWriter writer = new FileWriter("Gauss_Elimination.txt", true);
            int len = this.order;
            for (int f = 0; f < len; f++) {
                for (int p = 0; p < len; p++) {
                    writer.write(this.equations[f].getCoefficient(p) + " ");
                }
                writer.write(this.equations[f].getRes() + "\n");
            }
            writer.write("\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    PrintWriter writer;

    {
        try {
            writer = new PrintWriter("Gauss_Elimination.txt");
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
        return null;
    }

    @Override
    public long getTimer() {
        long totalTime = endTime - startTime;
        return totalTime;
    }
}
