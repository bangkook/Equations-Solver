package com.example.RF;

import java.io.FileWriter;
import java.io.IOException;

public class FixedPoint extends RootFinder{
    private double eps; //= 0.00001;
    private int maxIters;// = 50;
    private static FunctionExpression function;
    private double root;
    private final static String stepsFile = "FixedPoint.txt";
    private long startTime, endTime, writeTime;

    public FixedPoint(FunctionExpression f, double initial0, boolean applyPrecision,
                      int precision, double eps, int maxIters) {
        super(applyPrecision, precision);
        super.clearFile(stepsFile);
        this.root = initial0;
        this.eps = eps;
        this.maxIters = maxIters;
        function = f;
        String titles = String.format("%10s %20s %20s %20s", "iteration", "x", "g(x)", "relative error");
        writeFile(titles);
    }


    @Override
    public double getRoot() {
        double relError;
        double newRoot;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < maxIters; i++) {
            newRoot = round(function.evaluate(root));
            relError = Math.abs((newRoot - root) / newRoot);
            String step = String.format("%10d %20f %20f %20f", i, root, newRoot, relError);
            writeFile(step);
            startTime += writeTime;
            root = newRoot;
            if (relError <= eps)
                break;
        }
        endTime = System.currentTimeMillis();
        return this.root;
    }

    @Override
    public String getStepsFile() {
        return null;
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

    @Override
    public long getTime() {
        return endTime - startTime;
    }

//    private boolean checkConvergence(){
//        if(Math.abs(function.differentiate(root)) < 1) return true;
//        else  return false;
//    }

    public static void main(String[] args) throws IOException {//for test
        String s="3/(x-2)";
        FunctionExpression function=new FunctionExpression(s);
        IRootFinder F=new FixedPoint(function, 0,true, 5, 0.0001, 50);
        System.out.println(F.getRoot());
    }
}
