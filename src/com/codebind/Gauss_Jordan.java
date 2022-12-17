package com.codebind;

import java.util.ArrayList;

public class Gauss_Jordan implements LinearSolver {
    private final int order;
    private double[] ans;
    private final Equation[] equations;
    private int multiplier;

    public Gauss_Jordan(Equation[] equations) {
        this.order = equations[0].getOrder();
        this.equations = equations;
        ans = new double[this.order];
    }

    @Override
    public void setPrecision(int precision) {

    }
    
    public void PartialPivoting(int index) {
        for (int i = index + 1; i < this.order; i++) {
            if (Math.abs(this.equations[index].getCoefficient(index)) < Math.abs(this.equations[i].getCoefficient(index))) {
                Equation temp = equations[index];
                equations[index] = equations[i];
                equations[i] = temp;
            }
        }
        double coff = this.equations[index].getCoefficient(index);
        for (int i = 0; i < this.order; i++) {
            this.equations[index].setCoefficient(i, this.equations[index].getCoefficient(i) / coff + 0.0);
        }
        this.equations[index].setRes(this.equations[index].getRes() / coff + 0.0);
    }

    @Override
    public double[] getSolution() {
        for (int i = 0; i < this.order; i++) {
            PartialPivoting(i);
            for (int j = i + 1; j < this.order; j++) {
                double multiplier = this.equations[j].getCoefficient(i) / this.equations[i].getCoefficient(i);
                this.equations[j].add(equations[i], multiplier, i);
            }
            for (int l = i - 1; l < this.order && l >= 0; l--) {
                double m = this.equations[l].getCoefficient(i) / this.equations[i].getCoefficient(i);
                this.equations[l].add(equations[i], m, i);
            }
                /* for (int j = i+1; j < this.order ; j++) {
                double m=this.equations[j].getCoefficient(i) / this.equations[i].getCoefficient(i);
                for (int k = 0; k < this.order ; k++) {
                    this.equations[j].setCoefficient(k,this.equations[j].getCoefficient(k)-m*this.equations[i].getCoefficient(k)+0.0);
                }
                this.equations[j].setRes(this.equations[j].getRes()-m*this.equations[i].getRes()+0.0);}
                for (int l = i-1; l < this.order &&l>=0; l--) {
                double mm=this.equations[l].getCoefficient(i) / this.equations[i].getCoefficient(i);
                for (int k = 0; k < this.order ; k++) {
                    this.equations[l].setCoefficient(k,this.equations[l].getCoefficient(k)-mm*this.equations[i].getCoefficient(k)+0.0);
                }
                this.equations[l].setRes(this.equations[l].getRes()-mm*this.equations[i].getRes()+0.0);}*/
        }
        for (int i = 0; i < this.order; i++)
            ans[i] = this.equations[i].getRes();

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
    public ArrayList<double[]> getSteps() {
        return null;
    }
}
