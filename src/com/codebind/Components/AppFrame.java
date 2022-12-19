package com.codebind.Components;

import javax.swing.*;

import com.codebind.Equation;

import java.awt.*;
// import java.awt.event.*;
// import com.codebind.Components.*;

public class AppFrame extends JFrame
{

	JPanel deck;
	MethodSelectScreen methodScreen;

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

	public void onMethodSelectGetSol(Method method, boolean useScaling, int precision, Parameters params)
	{

	}

}
