package com.example.RF;


import java.io.FileWriter;
import java.util.function.DoubleFunction;

public class Secant extends RootFinder {
    private final double eps; //= 0.00001;
    private final int maxIters;// = 50;
    private static FunctionExpression function;
    private final static String stepsFile = "Secant.txt";
    private double oldRoot, root;

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
        for (int i = 0; i < maxIters; i++) {
            if (this.slope() == 0) {
                throw new RuntimeException("Function can not be solved - slope equals zero");
            }
            newRoot = round(this.root - round(function.evaluate(this.root) / this.slope()));
            relError = Math.abs((newRoot - this.root) / newRoot);
            System.out.println(oldRoot + " " + root + " " + newRoot);
            writeFile(oldRoot + "\t\t" + this.root + "\t\t" + function.evaluate(this.root) + "\t\t" + newRoot);
            oldRoot = this.root;
            this.root = newRoot;
            if (relError <= eps)
                break;
        }
        return this.root;
    }

    private double slope() {
        return round(round(function.evaluate(root) - function.evaluate(oldRoot)) / (root - oldRoot));
    }

    private void writeFile(String step) {//write steps function
        try {
            FileWriter writer = new FileWriter(stepsFile, true);
            writer.write(step + "\n");
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStepsFile(){
        return stepsFile;
    }

}
