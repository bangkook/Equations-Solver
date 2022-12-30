package com.example.RF;


import java.io.FileWriter;
import java.io.IOException;
import java.util.function.DoubleFunction;

public class Secant extends RootFinder {
    private final double eps; //= 0.00001;
    private final int maxIters;// = 50;
    private static FunctionExpression function;
    private final static String stepsFile = "Secant.txt";
    private double oldRoot, root;
    private long startTime, endTime, writeTime;

    public Secant(FunctionExpression func, double initial0, double initial1, boolean applyPrecision,
                  int precision, double eps, int maxIters) {
        super(applyPrecision, precision);
        super.clearFile(stepsFile);
        this.oldRoot = initial0;
        this.root = initial1;
        this.eps = eps;
        this.maxIters = maxIters;
        function = func;
        writeFile("Xi-1               Xi            f(Xi)              Xi+1");
    }

    @Override
    public double getRoot() {
        double relError;
        double newRoot;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < maxIters; i++) {
            if (this.slope() == 0) {
                throw new RuntimeException("Function can not be solved - slope equals zero");
            }
            newRoot = round(this.root - round(function.evaluate(this.root) / this.slope()));
            relError = Math.abs((newRoot - this.root) / newRoot);
            System.out.println(oldRoot + " " + root + " " + newRoot);
            writeFile(oldRoot + "\t\t" + this.root + "\t\t" + function.evaluate(this.root) + "\t\t" + newRoot);
            startTime += writeTime;
            oldRoot = this.root;
            this.root = newRoot;
            if (relError <= eps)
                break;
        }
        endTime = System.currentTimeMillis();
        return this.root;
    }

    private double slope() {
        return round(round(function.evaluate(root) - function.evaluate(oldRoot)) / (root - oldRoot));
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
        String s="x^3-25";
        FunctionExpression function=new FunctionExpression(s);
        IRootFinder F=new Secant(function, 0, 1,false, 0, 0.0001, 50);
        System.out.println(F.getRoot());

    }
}
