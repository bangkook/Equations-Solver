package com.example.Components;

import com.example.LS.*;
import com.example.LS.LUDecomposition.*;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame
{

	JPanel deck;
	MethodSelectScreen methodScreen;
	SolutionScreen solutionScreen;

	public AppFrame(String title)
	{
		super(title);
		// setLayout(new Borde());
		deck = new JPanel();
		deck.setLayout(new CardLayout());
		add(deck);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		deck.add(new SystemEntryScreen(), "SystemEntryScreen");
		methodScreen = new MethodSelectScreen();
		deck.add(methodScreen, "MethodSelectScreen");
		solutionScreen = new SolutionScreen();
		deck.add(solutionScreen, "SolutionScreen");

	}


	public void onSystemEntryNext(Equation[] system)
	{
		((CardLayout)deck.getLayout()).next(deck);
		methodScreen.setSystem(system);
	}

	public void onMethodSelectBack()
	{
		((CardLayout)deck.getLayout()).previous(deck);
	}

	public void onMethodSelectGetSol(Equation[] system, Method method, boolean useScaling, Parameters params)
	{
		LinearSolver solver;
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


		((CardLayout)deck.getLayout()).next(deck);

		solutionScreen.setSolver(solver);


	}

	public void onSolutionScreenEnterAnotherSystem()
	{
		((CardLayout)deck.getLayout()).show(deck, "SystemEntryScreen");
	}

}
