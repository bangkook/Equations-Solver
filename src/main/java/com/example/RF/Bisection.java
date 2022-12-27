package com.example.RF;

import java.io.IOException;

public class Bisection extends RootFinder{
    private double lowerLimit;
    private double upperLimit;
    private double result;
    private double iterations;
    private FunctionExpression function;
    public Bisection(boolean applyPrecision1, int precision1, int lowerLimit, int upperLimit, int iterations, FunctionExpression function) {
        super(applyPrecision1, precision1);
        this.lowerLimit=lowerLimit;
        this.upperLimit=upperLimit;
        this.iterations = iterations;
        this.function = function;
    }

    @Override
    public double getRoot() {
        while (this.iterations>0){
            this.result=round((this.lowerLimit+this.upperLimit)/2.0);
            if(round(this.function.evaluate(this.result)*this.function.evaluate(this.lowerLimit))>0){
                this.lowerLimit=this.result;
            }
            else if(round(this.function.evaluate(this.result)*this.function.evaluate(this.lowerLimit))<0){
                this.upperLimit=this.result;
            }
            else {
                return this.result;
            }
            this.iterations--;
        }
        return this.result;
    }

    @Override
    public void setPrecision(int pre) {

    }

    @Override
    public void setFunc(FunctionExpression func) {

    }

    public static void main(String[] args) throws IOException {//for test
        String s="x^3-25";
        FunctionExpression function=new FunctionExpression(s);
        Bisection B=new Bisection(false,5,0,4,50,function);
        System.out.println(B.getRoot());
    }
}
