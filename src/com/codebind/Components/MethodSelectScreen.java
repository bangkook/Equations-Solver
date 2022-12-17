package com.codebind.Components;

import java.awt.Color;

import javax.swing.*;
// import javax.swing.event.CaretEvent;
// import javax.swing.event.CaretListener;

import com.codebind.Equation;

// import com.codebind.Equation;

// import java.awt.*;
// import java.awt.event.*;


public class MethodSelectScreen extends JPanel
{
	Equation[] system;


	MethodSelectScreen()
	{
		// setBackground(Color.RED);
		add(new JLabel("This is the screen where we choose the method."));
	}


	public void setSystem(Equation[] sys)
	{
		system = sys;
	}
}
