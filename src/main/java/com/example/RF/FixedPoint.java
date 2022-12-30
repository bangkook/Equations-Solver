package com.example.RF;

import java.io.IOException;

public class FixedPoint extends RootFinder{
    private double eps; //= 0.00001;
    private int maxIters;// = 50;
    private static FunctionExpression function;
    private double root;

    public FixedPoint(FunctionExpression f, double initial0, boolean applyPrecision,
                      int precision, double eps, int maxIters) {
        super(applyPrecision, precision);
        this.root = initial0;
        this.eps = eps;
        this.maxIters = maxIters;
        function = f;
    }

    @Override
    public double getRoot() {
        double relError;
        double newRoot;
        if(checkConvergence() == false)
            throw new RuntimeException("fixed-point iteration method will diverge");
        else {
            for (int i = 0; i < maxIters; i++) {
                newRoot = round(function.evaluate(root));
                relError = Math.abs((newRoot - root) / newRoot);
                root = newRoot;
                if (relError <= eps)
                    break;
            }
        }
        return this.root;
    }

    @Override
    public String getStepsFile() {
        return null;
    }

    @Override
    public long getTime() {
        return 0;
    }

    private boolean checkConvergence(){
        if(Math.abs(function.differentiate(root)) < 1) return true;
        else  return false;
    }

    public static void main(String[] args) throws IOException {//for test
        String s="3/(x-2)";
        FunctionExpression function=new FunctionExpression(s);
        IRootFinder F=new FixedPoint(function, 0,true, 5, 0.0001, 50);
        System.out.println(F.getRoot());
    }
}
