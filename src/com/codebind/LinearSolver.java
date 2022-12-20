package com.codebind;

import java.io.IOException;
import java.util.ArrayList;

public interface LinearSolver {
    //void setSettings(boolean stepByStep, int precision);

    //void setSystem(Equation[] equations);

    double[] getSolution() throws IOException;

    void print();

    long getTimer();

    ArrayList<double[]> getSteps() throws IOException;

}
