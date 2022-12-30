package com.example.RF;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class False_Position extends RootFinder{
    private double lowerLimit;
    private double upperLimit;
    private double result;
    private double iterations;
    private FunctionExpression function;
    private final static String stepsFile = "False_Position.txt";
    private long startTime, endTime, writeTime;

    public False_Position(boolean applyPrecision1, int precision1, int lowerLimit, int upperLimit, int iterations, FunctionExpression function) {
        super(applyPrecision1, precision1);
        super.clearFile(stepsFile);
        this.lowerLimit=lowerLimit;
        this.upperLimit=upperLimit;
        this.iterations = iterations;
        this.function = function;
    }

    public void writeFile() {//write steps function
        long currTime = System.currentTimeMillis();
        try {
            FileWriter writer = new FileWriter(stepsFile, true);
            writer.write("Lower limit: "+this.lowerLimit+","+"Upper limit: "+this.upperLimit+"\n");
            writer.write("Result: "+this.result+"\n");
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        writeTime = System.currentTimeMillis() - currTime;
    }

    @Override
    public double getRoot() {
        if(this.function.evaluate(this.upperLimit)*this.function.evaluate(this.lowerLimit)>=0){
            throw new ArithmeticException ("No roots found between entered limits");
        }
        startTime=System.currentTimeMillis();
        while (this.iterations>0){
            writeFile();
            startTime+=writeTime;
            this.result=round(((this.lowerLimit*this.function.evaluate(this.upperLimit))-(this.upperLimit*this.function.evaluate(this.lowerLimit)))/(this.function.evaluate(this.upperLimit)-this.function.evaluate(this.lowerLimit)));
            if(round(this.function.evaluate(this.result)*this.function.evaluate(this.lowerLimit))>0){
                this.lowerLimit=round(this.result);
            }
            else if(round(this.function.evaluate(this.result)*this.function.evaluate(this.lowerLimit))<0){
                this.upperLimit=round(this.result);
            }
            else {
                writeFile();
                startTime+=writeTime;
                return this.result;
            }
            this.iterations--;
        }
        writeFile();
        startTime+=writeTime;
        endTime=System.currentTimeMillis();
        return this.result;
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
        False_Position F=new False_Position(true,5,0,4,50,function);
        System.out.println(F.getRoot());
    }
}
