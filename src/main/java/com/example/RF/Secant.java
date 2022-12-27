package com.example.RF;


import java.util.function.DoubleFunction;

public class Secant extends RootFinder {
    private double eps; //= 0.00001;
    private int maxIters;// = 50;
    private static DoubleFunction<Double> function;
    private double oldRoot, root;

    public Secant(DoubleFunction<Double> f, double initial0, double initial1, boolean applyPrecision,
                  int precision, double eps, int maxIters) {
        super(applyPrecision, precision);
        this.oldRoot = initial0;
        this.root = initial1;
        this.eps = eps;
        this.maxIters = maxIters;
        function = f;
    }

    @Override
    public double getRoot() {
        double relError;
        double newRoot;
        for (int i = 0; i < maxIters; i++) {
            if (this.slope() == 0) {
                throw new RuntimeException("Function can not be solved - slope equals zero");
            }
            newRoot = round(this.root - round(function.apply(this.root) / this.slope()));
            relError = Math.abs((newRoot - this.root) / newRoot);
            System.out.println(oldRoot + " " + root + " " + newRoot);
            oldRoot = this.root;
            this.root = newRoot;
            if (relError <= eps)
                break;
        }
        return this.root;
    }

    private double slope() {
        return round(round(function.apply(root) - function.apply(oldRoot)) / (root - oldRoot));
    }


    public void setFunc(FunctionExpression func)
    {

    }

    public void setPrecision(int pre)
    {

    }

}
