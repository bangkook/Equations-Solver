package com.codebind;

import java.io.ObjectInputStream;

public interface LinearSolver {
    //void setSettings(boolean stepByStep, int precision);

    //void setSystem(Equation[] equations);

    double[] getSolution();

    ObjectInputStream getSteps();

}
