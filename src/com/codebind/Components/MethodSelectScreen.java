package com.codebind.Components;


import java.awt.*;
import javax.swing.*;
import javax.swing.event.CaretEvent;
// import javax.swing.event.CaretEvent;
// import javax.swing.event.CaretListener;
import javax.swing.event.CaretListener;

import com.codebind.Equation;



public class MethodSelectScreen extends JPanel
{
	Equation[] system;


	MethodSelectScreen()
	{
		setLayout(new BorderLayout());
		add(new MethodPanel());
	}


	public void setSystem(Equation[] sys)
	{
		system = sys;
	}
}


class MethodPanel extends JPanel implements CaretListener
{
	final int defaultPrecision = 10;
	int precision;
	Methods method;
	JTextField precisionField;
	JComboBox<String> methodBox;

	MethodPanel()
	{
		setLayout(new FlowLayout(FlowLayout.LEADING, 10, 5));
		add(new JLabel("Solving Method:"));
		String[] methodBoxChoices = new String[5];
		methodBoxChoices[0] = "Gauss Elimination";
		methodBoxChoices[1] = "Gauss-Jordan";
		methodBoxChoices[2] = "LU Decomposition";
		methodBoxChoices[3] = "Gauss-Seidel";
		methodBoxChoices[4] = "Jacobi Iteration";
		methodBox = new JComboBox<String>(methodBoxChoices);
		add(methodBox);
		precisionField = new JTextField(2);
		add(new JLabel("Precision ="));
		add(precisionField);
	}

	public void caretUpdate(CaretEvent ae)
	{
		try 
		{
			String text = precisionField.getText();
			precision = Integer.parseInt(text);
		}
		catch(NumberFormatException e)
		{
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {precisionField.setText(null);}
			});
			precision = defaultPrecision;
		}

	}


}


enum Methods{GausssElimination, GaussJordan, LU, GaussSeidel, Jacobi}