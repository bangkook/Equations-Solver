package com.example.LS;

import java.io.IOException;

public interface LinearSolver {
    double[] getSolution() throws RuntimeException;

    void print();

    String getSteps() throws IOException;

    long getTimer();
}
