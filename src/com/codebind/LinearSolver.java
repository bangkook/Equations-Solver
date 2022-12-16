package com.codebind;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface LinearSolver {
    //void setSettings(boolean stepByStep, int precision);

    //void setSystem(Equation[] equations);

    double[] getSolution();

    void print();

    ArrayList<double[]> getSteps() throws FileNotFoundException;

}
