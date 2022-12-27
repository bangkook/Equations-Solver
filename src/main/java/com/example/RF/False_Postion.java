package com.example.RF;

import java.io.IOException;

public class False_Postion extends RootFinder{
    private double lowerLimit;
    private double upperLimit;
    private double result;
    private double iterations;
    private FunctionExpression function;
    public False_Postion(boolean applyPrecision1, int precision1, double lowerLimit, double upperLimit, double iterations, FunctionExpression function) {
        super(applyPrecision1, precision1);
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.iterations = iterations;
        this.function = function;
    }

    @Override
    public double getRoot() {//the answer
        return 0;
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
