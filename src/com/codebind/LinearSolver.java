package com.codebind;

import java.io.IOException;
import java.util.ArrayList;

public interface LinearSolver {
    //void setSettings(boolean stepByStep, int precision);

    //void setSystem(Equation[] equations);

    double[] getSolution() throws IOException;

    void setPrecision(int precision);
    
    void print();

    ArrayList<double[]> getSteps() throws IOException;

}
