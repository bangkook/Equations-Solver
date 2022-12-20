package com.codebind.Components;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.*;

import com.codebind.GaussElimination;
import com.codebind.Gauss_Jordan;
import com.codebind.Gauss_Seidel;
import com.codebind.Jacobi;
import com.codebind.LinearSolver;
import com.codebind.LUDecomposition.CholeskyDecomposition;
import com.codebind.LUDecomposition.CroutDecomposition;
import com.codebind.LUDecomposition.Doolittle;


public class SolutionScreen extends JPanel implements ActionListener
{
	LinearSolver solver;
	JPanel solPanel;
	JPanel stepsPanel;
	JButton enterSysButton;

	
	public void setSolver(LinearSolver solver)
	{
		clearScreen();
		setLayout(new BorderLayout());
		solPanel = new JPanel();
		add(solPanel, BorderLayout.NORTH);
		solPanel.add(new JLabel("Solution: "));
		
		this.solver = solver;
		try
		{
			double[] sol = solver.getSolution();
			for (int i=0; i<sol.length; i++)
			{
				solPanel.add(new JLabel("x"+(i+1) + " = " + sol[i]));
			}
			long time = solver.getTimer();
			solPanel.add(new JLabel("Time = " + time + "ms"), BorderLayout.NORTH);


			String stepsFile;
			if (solver instanceof GaussElimination)
			{
				stepsFile = "Gauss_Elimination.txt";
			}
			else if (solver instanceof Gauss_Jordan)
			{
				stepsFile = "Gauss_Jordan.txt";
			}
			else if (solver instanceof CholeskyDecomposition)
			{
				stepsFile = "Chelosky.txt";
			}
			else if (solver instanceof CroutDecomposition)
			{
				stepsFile = "Crout.txt";
			}
			else if (solver instanceof Gauss_Seidel)
			{
				stepsFile = "gauss_seidel_steps.txt";
			}
			else if (solver instanceof Jacobi)
			{
				stepsFile = "jacobi_steps.txt";
			}
			else if (solver instanceof Gauss_Jordan)
			{
				stepsFile = "Gauss_Jordan.txt";
			}
			else
			{
				stepsFile = "Doolittle.txt";
			}
			stepsPanel = new stepsPanel(new File(stepsFile));
			add(stepsPanel);
		}
		catch (RuntimeException e)
		{
			solPanel.add(new JLabel(e.getMessage()));
		}
		enterSysButton = new JButton("Enter another system");
		add(enterSysButton, BorderLayout.SOUTH);
		enterSysButton.addActionListener(this);
	}

	void clearScreen()
	{
		if (solPanel != null) remove(solPanel);
		if (stepsPanel != null) remove(stepsPanel);
		revalidate();
		repaint();
	}

	public void actionPerformed(ActionEvent ae)
	{
		((AppFrame)getTopLevelAncestor()).onSolutionScreenEnterAnotherSystem();
	}
}


class stepsPanel extends JPanel
{

	stepsPanel(File file)
	{
		setLayout(new GridLayout(0,1, 0, 3));
		try
		{
			Scanner scn = new Scanner(file);
			while (scn.hasNextLine())
			{
				String line = scn.nextLine();
				add(new JLabel(line));
			}
		}
		catch (FileNotFoundException e)
		{
			System.out.println("steps file not found");
		}

	}
}
