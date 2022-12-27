package com.example.RF;


import java.util.function.DoubleFunction;

public class NewtonRaphson extends RootFinder {
    private double eps; //= 0.00001;
    private int maxIters;// = 50;
    private static DoubleFunction<Double> function;
    private static DoubleFunction<Double> funcDeriv;
    private double root;

    public NewtonRaphson(DoubleFunction<Double> f, double initial, boolean applyPrecision, int precision, double eps, int maxIters)
    {
        super(applyPrecision, precision);
    }

    public void setFunc(FunctionExpression func)
    {

    }

    public void setPrecision(int pre)
    {

    }

    public double getRoot()
    {
        double relError;
        double newRoot;
        for (int i = 0; i < maxIters; i++) {
            if (round(funcDeriv.apply(this.root)) == 0) {
                throw new RuntimeException("Function can not be solved - derivative equals zero");
            }
            newRoot = round(this.root - round(function.apply(this.root) / round(funcDeriv.apply(this.root))));
            relError = Math.abs((newRoot - this.root) / newRoot);
            this.root = newRoot;
            if (relError <= eps)
                break;
        }
        return this.root;
    }
}
