package com.codebind;

public class EquationsSolver {
    public static void main(String[] args) {
        // This is for testing only
        Equation[] equations = new Equation[3];
        equations[0] = new Equation(new double[]{4, 2, 1}, 11);
        equations[1] = new Equation(new double[]{-1, 2, 0}, 3);
        equations[2] = new Equation(new double[]{2, 1, 4}, 16);
        LinearSolver test = new Jacobi(equations, new double[]{1, 1, 1}, 50, 0.1);
        double[] sol = test.getSolution();
        for (double x : sol)
            System.out.print(x + " ");

        System.out.print('\n');

        LinearSolver test2 = new GaussElimination(equations, 0.1);
        double[] sol2 = test2.getSolution();
        for (double x : sol2)
            System.out.print(x + " ");

    }

}
