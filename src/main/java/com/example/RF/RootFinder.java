package com.example.RF;

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
    abstract public double getRoot();


    abstract public void setPrecision(int pre);

    abstract public void setFunc(FunctionExpression func);

}
