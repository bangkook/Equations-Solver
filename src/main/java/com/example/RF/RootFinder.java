package com.example.RF;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;

abstract public class RootFinder implements IRootFinder{
    protected static boolean applyPrecision = false;
    protected static int precision;

    public RootFinder(boolean applyPrecision1, int precision1) {
        applyPrecision = applyPrecision1;
        precision = precision1;
    }

    public double round(double val) {
        if (!applyPrecision)
            return val;
        return (new BigDecimal(Double.toString(val)).round(new MathContext(precision))).doubleValue();
    }

    public void clearFile(String fileName){
        PrintWriter writer;//clears the text file before write
        {
            try {
                writer = new PrintWriter(fileName);
                writer.print("");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    abstract public double getRoot();
    abstract public String getStepsFile();

}
