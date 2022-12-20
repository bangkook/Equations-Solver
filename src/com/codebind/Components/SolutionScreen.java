package com.codebind.Components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class SolutionScreen extends JPanel implements ActionListener
{
	JPanel solPanel;
	JPanel stepsPanel;
	JButton enterSysButton;

	
	public void setSolution(double[] sol)
	{
		clearScreen();
		setLayout(new BorderLayout());
		solPanel = new JPanel();
		add(solPanel, BorderLayout.NORTH);
		solPanel.add(new JLabel("Solution: "));
		for (int i=0; i<sol.length; i++)
		{
			solPanel.add(new JLabel("x"+(i+1) + " = " + sol[i]));
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
