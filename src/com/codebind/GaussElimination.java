package com.codebind;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;


public class GaussElimination implements LinearSolver {
    private final int order;
    private final Equation[] equations;
    private double[] ans;
    private boolean scaling;
    private long startTime;
    private long endTime;

    public GaussElimination(Equation[] equations, boolean scaling) {
        this.equations = equations;
        this.scaling = scaling;
        this.order = equations[0].getOrder();
    }

    @Override
    public double[] getSolution() {
        startTime=System.currentTimeMillis();
        int nextPivot = 0; //the curr row
        double maxPivot = Math.abs((this.equations[nextPivot].getPivot(this.scaling, nextPivot)));//max pivot in col
        int indexMaxPivot = nextPivot;
        this.ans = new double[this.order];
        while (nextPivot != this.order) {
            for (int i = nextPivot; i < this.order; i++) {
                if (Math.abs((this.equations[i].getPivot(this.scaling, nextPivot))) > maxPivot ) {
                    maxPivot = Math.abs((this.equations[i].getPivot(this.scaling, nextPivot)));
                    indexMaxPivot = i;
                }
            }

            //swap
            writeFile();
            if(indexMaxPivot!=nextPivot){
                Equation temp = equations[nextPivot];
                equations[nextPivot] = equations[indexMaxPivot];
                equations[indexMaxPivot] = temp;
                writeFile();
            }

            //mul and sub
            for (int i = nextPivot + 1; i < this.order; i++) {
                if(equations[nextPivot].getCoefficient(nextPivot)!=0){
                    double multiplier = equations[i].getCoefficient(nextPivot)/equations[nextPivot].getCoefficient(nextPivot);
                    equations[i].add(equations[nextPivot], multiplier, nextPivot);
                }
            }
            writeFile();
            if(nextPivot==this.order-1){  //check for no solution or infinite solution
                if(this.equations[nextPivot].check(equations)==-2){
                    endTime = System.currentTimeMillis();
                    throw new RuntimeException("System has no solution");
                }
                else if(this.equations[nextPivot].check(equations)!=-2 && this.equations[nextPivot].check(equations)!=-1 ){
                    int value = 1, currRow = 0, noOfFreeVar = this.equations[nextPivot].check(equations);
                    while (noOfFreeVar!=0){
                        for (int j = currRow; j < this.order; j++) {
                            if(this.equations[j].getCoefficient(j) == 0){
                                ans[j] = value; value++; currRow = j; break;
                            }
                        }
                        currRow = currRow + 1; noOfFreeVar--;
                    }
                    for (int i = this.order - 1; i >= 0; i--) {
                        if(ans[i]==0){
                            ans[i] = this.equations[i].substitute(this.ans, i + 1, this.order, i);}
                    }
                    endTime = System.currentTimeMillis();
                    return ans;
                }
            }
            maxPivot = 0; nextPivot++; indexMaxPivot = nextPivot;
        }
        //solution
        for (int i = this.order - 1; i >= 0; i--) {
            ans[i] = this.equations[i].substitute(this.ans, i + 1, this.order, i);
        }
        endTime   = System.currentTimeMillis();
        return this.ans;
    }
    @Override
    public long getTimer() {
        long totalTime = endTime - startTime;
        return totalTime;
    }
    public void writeFile(){
        try {
            FileWriter writer = new FileWriter("Gauss_Elimination.txt",true);
            int len = this.order;
            for (int f = 0; f < len; f++) {
                for (int p = 0; p < len; p++) {
                    writer.write(this.equations[f].getCoefficient(p) + " ");
                }
                writer.write(this.equations[f].getRes()+"\n");
            }
            writer.write("\n");
            writer.flush();
        }
        catch (Exception e) {
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
}