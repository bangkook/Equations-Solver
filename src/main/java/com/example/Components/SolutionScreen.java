package com.example.Components;

import com.example.LS.*;
import com.example.LS.LUDecomposition.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class SolutionScreen extends JPanel implements ActionListener {
    LinearSolver solver;
    JPanel solPanel;
    JPanel stepsPanel;
    JScrollPane sp;
    JButton enterSysButton;

    public SolutionScreen() {
        setLayout(new BorderLayout());
        sp = new JScrollPane();
        add(sp);
    }


    public void setSolvingMode(Equation[] system, Method method, boolean useScaling, Parameters params)
    {
        clearScreen();
        solPanel = new JPanel();
        add(solPanel, BorderLayout.NORTH);
        solPanel.add(new JLabel("Solution: "));
        switch (method)
		{
			case GaussElimination:
			solver = new GaussElimination(system, useScaling);
			break;
			case GaussJordan:
			solver = new Gauss_Jordan(system, useScaling);
			break;
			case LU:
			LUParams p = (LUParams) params;
			switch (p.form)
			{
				case Crout:
				solver = new CroutDecomposition(system, useScaling);
				break;
				case Cholesky:
				solver = new CholeskyDecomposition(system, useScaling);
				break;
				default:// Doolittle
				solver = new Doolittle(system, useScaling);
				break;
			}
			break;
			case GaussSeidel:
			IndirectParams sei = (IndirectParams) params;
			solver = new Gauss_Seidel(system, sei.initial, sei.maxIters, sei.relativeErr, useScaling);
			break;
			default://Jacobi
			IndirectParams ja = (IndirectParams) params;
			solver = new Jacobi(system, ja.initial, ja.maxIters, ja.relativeErr, useScaling);
			break;
		}

        try {
            double[] sol = solver.getSolution();
            for (int i = 0; i < sol.length; i++) {
                solPanel.add(new JLabel("x" + (i + 1) + " = " + sol[i]));
            }

            solPanel.add(new JLabel("Time = " + solver.getTimer() + "ms"), BorderLayout.NORTH);


            String stepsFile;
            if (solver instanceof GaussElimination) {
                stepsFile = "Gauss_Elimination.txt";
            } else if (solver instanceof Gauss_Jordan) {
                stepsFile = "Gauss_Jordan.txt";
            } else if (solver instanceof CholeskyDecomposition) {
                stepsFile = "Cholesky.txt";
            } else if (solver instanceof CroutDecomposition) {
                stepsFile = "Crout.txt";
            } else if (solver instanceof Gauss_Seidel) {
                stepsFile = "gauss_seidel_steps.txt";
            } else if (solver instanceof Jacobi) {
                stepsFile = "jacobi_steps.txt";
            } else if (solver instanceof Gauss_Jordan) {
                stepsFile = "Gauss_Jordan.txt";
            } else {
                stepsFile = "Doolittle.txt";
            }
            stepsPanel = new stepsPanel(new File(stepsFile));
            sp.setViewportView(stepsPanel);

        } catch (RuntimeException e) {
            solPanel.add(new JLabel(e.getMessage()));
        }
        enterSysButton = new JButton("Enter another system");
        add(enterSysButton, BorderLayout.SOUTH);
        enterSysButton.addActionListener(this);
    }

    void clearScreen() {
        if (solPanel != null) remove(solPanel);
        if (stepsPanel != null) remove(stepsPanel);
        revalidate();
        repaint();
    }

    public void actionPerformed(ActionEvent ae) {
        ((AppFrame) getTopLevelAncestor()).onSolutionScreenEnterAnotherSystem();
    }
}


class stepsPanel extends JPanel {

    stepsPanel(File file) {
        setLayout(new GridLayout(0, 1, 0, 3));
        try {
            Scanner scn = new Scanner(file);
            while (scn.hasNextLine()) {
                String line = scn.nextLine();
                add(new JLabel(line));
            }
            scn.close();
        } catch (FileNotFoundException e) {
            System.out.println("steps file not found");
        }

    }
}
