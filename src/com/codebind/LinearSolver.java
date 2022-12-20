package com.codebind;

import java.io.IOException;

public interface LinearSolver {
    double[] getSolution() throws IOException;

    void print();

    String getSteps() throws IOException;

}
