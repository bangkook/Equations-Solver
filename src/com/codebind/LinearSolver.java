package com.codebind;

import java.io.IOException;

public interface LinearSolver {
    double[] getSolution() throws RuntimeException;

    void print();

    String getSteps() throws IOException;

    long getTimer();
}
