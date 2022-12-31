package com.example.RF;


import java.io.FileWriter;
import java.io.IOException;
import java.util.function.DoubleFunction;

public class NewtonRaphson extends RootFinder {
    private final double eps; //= 0.00001;
    private final int maxIters;// = 50;
    private static FunctionExpression function;
    private final static String stepsFile = "Newton-Raphson.txt";
    private double root;
    private long startTime, endTime, writeTime;

    public NewtonRaphson(FunctionExpression func, double initial, boolean applyPrecision, int precision, double eps, int maxIters)
    {
        super(applyPrecision, precision);
        super.clearFile(stepsFile);
        this.root = initial;
        this.eps = eps;
        this.maxIters = maxIters;
        function = func;
        String titles = String.format("%10s %20s %20s %20s", "iteration", "Xi", "Xi+1", "relative error");
        writeFile(titles);
    }

    public double getRoot()
    {
        double relError;
        double newRoot;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < maxIters; i++) {
            // if derivative of the function equals zero, newton-raphson can't proceed
            if (round(function.differentiate(this.root)) == 0) {
                throw new RuntimeException("Function can not be solved - derivative equals zero");
            }
            newRoot = round(this.root - round(function.evaluate(this.root) / round(function.differentiate(this.root))));
            relError = Math.abs((newRoot - this.root) / newRoot);
            String step = String.format("%10d %20f %20f %20f", i, root, newRoot, relError);
            writeFile(step);
            startTime += writeTime;
            this.root = newRoot;
            if (relError <= eps)
                break;
        }
        endTime = System.currentTimeMillis();
        return this.root;
    }

    private void writeFile(String step) {//write steps function
        long currTime = System.currentTimeMillis();
        try {
            FileWriter writer = new FileWriter(stepsFile, true);
            writer.write(step + "\n");
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        writeTime = System.currentTimeMillis() - currTime;
    }

    public String getStepsFile(){
        return stepsFile;
    }

    public long getTime(){
        return endTime - startTime;
    }

    public static void main(String[] args) throws IOException {//for test
        String s="e^-x - x";
        FunctionExpression function=new FunctionExpression(s);
        IRootFinder F=new NewtonRaphson(function, 0.05, true, 5, 0.0001, 50);
        System.out.println(F.getRoot());

    }
}
