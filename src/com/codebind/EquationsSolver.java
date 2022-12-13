package com.codebind;

import java.util.Arrays;

public class EquationsSolver {
    public static void main(String[] args) {
        // This is for testing only
        Equation[] equations = new Equation[3];
        equations[0] = new Equation(new double[]{2, 3, 1}, -4);
        equations[1] = new Equation(new double[]{4, 1, 4}, 9);
        equations[2] = new Equation(new double[]{3, 4, 6}, 0);

        Equation[] equations2 = new Equation[3];
        equations2[0] = new Equation(new double[]{6, 15, 55}, -4);
        equations2[1] = new Equation(new double[]{15, 55, 225}, 9);
        equations2[2] = new Equation(new double[]{55, 225, 979}, 0);

        // testing cholesky decomposition
        CholeskyDecompositioin test3 = new CholeskyDecompositioin(equations2, 0.0001);
        if(test3.getSolution() == null)
            System.out.println("you must provide symmetric matrix to apply cholesky decomposition");
        else {
            double[] sol3 = test3.getSolution();
            System.out.println(Arrays.toString(sol3));
        }

        // testing LU
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
