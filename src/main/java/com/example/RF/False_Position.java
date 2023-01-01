package com.example.RF;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class False_Position extends RootFinder{
    private double lowerLimit;
    private double upperLimit;
    private double result;
    private int iterations;
    private double toleranceError;
    private FunctionExpression function;
    private final static String stepsFile = "False_Position.txt";
    private long startTime, endTime, writeTime;

    public False_Position(boolean applyPrecision1, int precision1, double lowerLimit, double upperLimit, int iterations,double toleranceError, FunctionExpression function) {
        super(applyPrecision1, precision1);
        super.clearFile(stepsFile);
        this.lowerLimit=lowerLimit;
        this.upperLimit=upperLimit;
        this.iterations = iterations;
        this.function = function;
        this.toleranceError=toleranceError;
        String titles = String.format("%10s %20s %20s %20s %20s", "iteration", "Lower Limit (xl)","Upper Limit (xu)","Result (xr)", "relative error");
        writeFile(titles);
    }


    @Override
    public double getRoot() {
        if(this.function.evaluate(this.upperLimit)*this.function.evaluate(this.lowerLimit)>=0){
            throw new ArithmeticException ("No roots found between entered limits");
        }
        double xrOld=0;
        boolean first=true;
        double approximateError=0;
        String step="";
        startTime=System.currentTimeMillis();
        for (int i = 0; i < this.iterations; i++) {
            startTime+=writeTime;
            this.result=round(((this.lowerLimit*this.function.evaluate(this.upperLimit))-(this.upperLimit*this.function.evaluate(this.lowerLimit)))/(this.function.evaluate(this.upperLimit)-this.function.evaluate(this.lowerLimit)));
            if(round(this.function.evaluate(this.result)*this.function.evaluate(this.lowerLimit))>0){
                if(first){
                    this.lowerLimit=this.result;
                    xrOld=this.result;
                    first=false;
                    step = String.format("%10d %20f %20f %20f %20f", i, this.lowerLimit, this.upperLimit,this.result,approximateError );
                    writeFile(step);
                }
                else {
                    this.lowerLimit=this.result;
                    approximateError=Math.abs(round(((this.lowerLimit-xrOld)/this.lowerLimit)));
                    System.out.println("Errorl: "+approximateError);
                    xrOld=this.lowerLimit;
                    step = String.format("%10d %20f %20f %20f %20f", i, this.lowerLimit, this.upperLimit,this.result,approximateError );
                    writeFile(step);
                    if(approximateError<this.toleranceError){
                        startTime+=writeTime;
                        return this.result;
                    }
                }

            }
            else if(round(this.function.evaluate(this.result)*this.function.evaluate(this.lowerLimit))<0){
                if(first){
                    this.upperLimit=this.result;
                    xrOld=this.result;
                    first=false;
                    step = String.format("%10d %20f %20f %20f %20f", i, this.lowerLimit, this.upperLimit,this.result,approximateError );
                    writeFile(step);
                }
                else {
                    this.upperLimit=this.result;
                    approximateError=Math.abs(round(((this.upperLimit-xrOld)/this.upperLimit)));
                    System.out.println("Erroru: "+approximateError);
                    xrOld=this.upperLimit;
                    step = String.format("%10d %20f %20f %20f %20f", i, this.lowerLimit, this.upperLimit,this.result,approximateError );
                    writeFile(step);
                    if(approximateError<this.toleranceError){
                        startTime+=writeTime;
                        return this.result;
                    }
                }

            }
            else {
                step = String.format("%10d %20f %20f %20f %20f", i, this.lowerLimit, this.upperLimit,this.result,approximateError);
                writeFile(step);
                startTime+=writeTime;
                return this.result;
            }
        }
        step = String.format("%10d %20f %20f %20f %20f", this.iterations, this.lowerLimit, this.upperLimit,this.result,approximateError );
        writeFile(step);
        startTime+=writeTime;
        endTime=System.currentTimeMillis();
        return this.result;
    }

    public String getStepsFile() {
        return stepsFile;
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

    public long getTime(){
        return endTime - startTime;
    }


    public static void main(String[] args) throws IOException {//for test
        String s="((x-4)^2)*(x+2)";
        FunctionExpression function=new FunctionExpression(s);
        False_Position F=new False_Position(true,5,-2.5,-1.0,5,0.001,function);
        System.out.println(F.getRoot());

    }
}
