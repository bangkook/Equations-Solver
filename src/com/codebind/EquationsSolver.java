package com.codebind;

import java.io.IOException;

public class EquationsSolver {

    public static void main(String[] args) throws IOException {
        // This is for testing only
        Equation[] equations = new Equation[3];

        equations[0] = new Equation(new double[]{-3, -5, 36}, 10);
        equations[1] = new Equation(new double[]{0, 5, 5}, 5);
        equations[2] = new Equation(new double[]{12, 3, -5}, 0);
        Gauss_Jordan g = new Gauss_Jordan(equations);
        g.getSolution();
        System.out.println("result");
        g.print();
        GaussElimination test1 = new GaussElimination(equations, false);
        double[] sol = test1.getSolution();
        test1.print();
        for (double x : sol)
            System.out.print(x + " ");
        //equations[2] = new Equation(new double[]{3, 7, 13}, 76);

        /*Equation[] equations2 = new Equation[3];
        equations2[0] = new Equation(new double[]{6, 15, 55}, -4);
        equations2[1] = new Equation(new double[]{15, 55, 225}, 9);
        equations2[2] = new Equation(new double[]{55, 225, 979}, 0);

        // testing Crout decomposition
        CroutDecomposition test4 = new CroutDecomposition(equations, 0.0001);
        double[] sol4 = test4.getSolution();
        System.out.println(Arrays.toString(sol4));

        // testing cholesky decomposition
        CholeskyDecompositioin test3 = new CholeskyDecompositioin(equations2, 0.0001);
        if(test3.getSolution() == null)
            System.out.println("you must provide symmetric matrix to apply cholesky decomposition");
        else {
            double[] sol3 = test3.getSolution();
            System.out.println(Arrays.toString(sol3));
        }
*/
        // testing LU
//        LinearSolver test2 = new Jacobi(equations, new double[]{0, 0, 0});
//        test2.setPrecision(5);
//        double[] sol2 = test2.getSolution();
//        System.out.println(Arrays.toString(sol2));
//        ArrayList<double[]> steps = test2.getSteps();
//        int i = 1;
//        for (double[] step : steps) {
//            System.out.println("iteration" + i);
//            i++;
//            for (double ans : step) {
//                System.out.print(ans + " ");
//            }
//            System.out.println();
//        }

        /*LinearSolver test = new Jacobi(equations, new double[]{1, 0, 1});
        test.setPrecision(4);
        test.getSolution();
        ArrayList<double[]> steps = test.getSteps();
        int i = 1;
        for (double[] step : steps) {
            System.out.println("iteration" + i);
            i++;
            for (double ans : step) {
                System.out.print(ans + " ");
            }
            System.out.println();
        }
        /*LinearSolver test = new Gauss_Jordan(equations);
        double[] sol = test.getSolution();
        test.print();
            for (double x : sol)
                System.out.print(x + " ");*/

    }
}
