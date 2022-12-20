package com.codebind.Components;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import com.codebind.LinearSolver;


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

		}
		catch (IOException e)
		{

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
