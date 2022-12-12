package com.codebind;

import java.util.Arrays;

public class EquationsSolver {
    public static void main(String[] args) {
        // This is for testing only
        Equation[] equations = new Equation[3];
        equations[0] = new Equation(new double[]{2, 3, 1}, -4);
        equations[1] = new Equation(new double[]{4, 1, 4}, 9);
        equations[2] = new Equation(new double[]{3, 4, 6}, 0);

        //        ----------------
        //        testing LU
        LinearSolver test2 = new LUDecomposition(equations, 0.0001);
        double[] sol2 = test2.getSolution();
        System.out.println(Arrays.toString(sol2));
        //LinearSolver test = new Jacobi(equations, new double[]{1, 1, 1}, 50, 0.1);
        LinearSolver test = new Gauss_Jordan(equations);
        double[] sol = test.getSolution();
        test.print();
            for (double x : sol)
                System.out.print(x + " ");

    }
}
