package com.codebind.Components;

import javax.swing.*;

import com.codebind.Equation;

import java.awt.*;
// import java.awt.event.*;
// import com.codebind.Components.*;

public class AppFrame extends JFrame
{

	JPanel screenRoot;
	public AppFrame(String title)
	{
		super(title);
		// setLayout(new Borde());
		screenRoot = new JPanel();
		screenRoot.setLayout(new CardLayout());
		add(screenRoot);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screenRoot.add(new SystemEntryScreen(), "SystemEntryScreen");
		screenRoot.add(new MethodSelectScreen(), "MethodSelectScreen");

	}

	
	public void onSystemEntryNext(Equation[] system)
	{
		((CardLayout)screenRoot.getLayout()).next(screenRoot);
	}

}
