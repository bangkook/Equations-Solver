package com.codebind;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Gauss_Jordan implements LinearSolver {
    private final int order;
    private double[] ans;
    private final Equation[] equations;

    private boolean scaling = true;//scaling boolean
    private long startTime;
    private long endTime;

    public Gauss_Jordan(Equation[] equations, boolean scaling) {
        this.order = equations[0].getOrder();
        this.equations = equations;
        ans = new double[this.order];
        this.scaling = scaling;
    }

    PrintWriter writer;//clears the text file before write

    {
        try {
            writer = new PrintWriter("Gauss_Jordan.txt");
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeFile() {//write steps function
        try {
            FileWriter writer = new FileWriter("Gauss_Jordan.txt", true);
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

    public void PartialPivoting(int index) {
        int max = index;
        for (int i = index + 1; i < this.order; i++) {
            if (Math.abs((this.equations[index].getPivot(this.scaling, index))) < Math.abs((this.equations[i].getPivot(this.scaling, index)))) {
                max = i;
            }
        }
        if (max != index) {
            Equation temp = equations[index];
            equations[index] = equations[max];
            equations[max] = temp;
            writeFile();
        }
        double coff = this.equations[index].getCoefficient(index);
        for (int i = 0; i < this.order; i++) {

            if (coff != 0)
                this.equations[index].setCoefficient(i, this.equations[index].round(this.equations[index].getCoefficient(i) / coff + 0.0));
        }
        if (coff != 0)
            this.equations[index].setRes(this.equations[index].round(this.equations[index].getRes() / coff + 0.0));

    }


    @Override
    public double[] getSolution() {
        startTime = System.currentTimeMillis();
        for (int i = 0; i < this.order; i++) {
            writeFile();
            PartialPivoting(i);
            for (int j = i + 1; j < this.order; j++) {
                writeFile();
                if (this.equations[i].getCoefficient(i) != 0) {
                    double multiplier = this.equations[j].getCoefficient(i) / this.equations[i].getCoefficient(i);
                    this.equations[j].add(equations[i], multiplier, i);
                }

            }
            writeFile();
            //check for no solution or infinite solution
            if (i == this.order - 1) {
                if (this.equations[i].check(equations) == -2) {
                    throw new RuntimeException("System has no solution");
                } else if (this.equations[i].check(equations) != -2 && this.equations[i].check(equations) != -1) {
                    int noOfFreeVar = this.equations[i].check(equations);
                    int value = 1;
                    int k = 0;
                    System.out.println(noOfFreeVar);
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
                    endTime = System.currentTimeMillis();
                    return ans;
                }
            }
            for (int l = i - 1; l < this.order && l >= 0; l--) {
                writeFile();
                if (this.equations[i].getCoefficient(i) != 0) {
                    double m = this.equations[l].getCoefficient(i) / this.equations[i].getCoefficient(i);
                    this.equations[l].add(equations[i], m, i);
                }
            }

            //check for no solution or infinite solution
            if (i == this.order - 1) {
                if (this.equations[i].check(equations) == -2) {
                    throw new RuntimeException("System has no solution");
                } else if (this.equations[i].check(equations) != -2 && this.equations[i].check(equations) != -1) {
                    int noOfFreeVar = this.equations[i].check(equations);
                    int value = 1;
                    int k = 0;
                    System.out.println(noOfFreeVar);
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
                    endTime = System.currentTimeMillis();
                    return ans;
                }
            }

        }

        writeFile();
        for (int m = this.order - 1; m >= 0; m--) {
            ans[m] = this.equations[m].substitute(this.ans, m + 1, this.order, m);
        }
        endTime = System.currentTimeMillis();
        return ans;
    }

    @Override
    public void print() {
        for (int i = 0; i < this.order; i++) {
            for (int j = 0; j < this.order; j++) {
                System.out.print(this.equations[i].getCoefficient(j) + " ");
            }
            System.out.println("res:" + this.equations[i].getRes() + " ");
        }
    }

    @Override
    public String getSteps() {
        return "Gauss_Jordan.txt";
    }

    public long getTimer() {
        long totalTime = endTime - startTime;
        return totalTime;
    }

}
