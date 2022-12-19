package com.codebind.Components;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.codebind.LUDecomposition.*;

public abstract class ParametersPanel extends JPanel
{
	public abstract Parameters getParams();

	ParametersPanel()
	{
		setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
		setBackground(Color.LIGHT_GRAY);
	}
}


class LUParamsPanel extends ParametersPanel
{
	LUForm form;
	JComboBox<String> formBox;

	
	LUParamsPanel()
	{
		super();
		String[] luForms = new String[3];
		luForms[0] = "DooLittle";
		luForms[1] = "Cholesky";
		luForms[2] = "Crout";
		form = LUForm.Doolittle;
		formBox = new JComboBox<String>(luForms);
		add(formBox);
	}

	public Parameters getParams()
	{
		return new LUParams(form);
	}
}


class IndirectParamsPanel extends ParametersPanel
{
	double[] initial;
	JPanel initPanel;
	final int defaultMaxIters = 50;
	final double defaultRelativeErr = 0.001;
	int maxIters = defaultMaxIters;
	double relativeErr = defaultRelativeErr;


	public IndirectParamsPanel(int size)
	{
		super();
		setLayout(new FlowLayout(FlowLayout.LEADING));
		add(new JLabel("Initial Guess:"));
		JPanel initPanel = new JPanel(new GridLayout(2, size));

		for (int i=0; i<size; i++)
		{
			initPanel.add(new JLabel("x" + (i+1)));
		}
		for (int i=0; i<size; i++)
		{
			initPanel.add(new DoubleTextField(4));
		}
		initPanel.setBackground(Color.LIGHT_GRAY);
		initPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		JScrollPane guessScrollPane = new JScrollPane(initPanel);
		guessScrollPane.setPreferredSize(new Dimension(200, 90));
		add(guessScrollPane);
		// add(new JLabel("Max number of iterations = (Default = " + defaultMaxIters + ")"));


		// add(new JLabel("Relative Error = (Default = " + defaultRelativeErr + ")"));
	}

	public Parameters getParams()
	{
		return new IndirectParams(initial, defaultMaxIters, defaultRelativeErr);
	}
}




class Parameters
{

}

class LUParams extends Parameters
{
	LUForm form;

	LUParams(LUForm form)
	{
		this.form = form;
	}
}

class IndirectParams extends Parameters
{
	double[] initial;
	int maxIters;
	double relativeErr;

	IndirectParams(double[] initial, int maxIters, double relativeErr)
	{
		this.initial = initial;
		this.maxIters = maxIters;
		this.relativeErr = relativeErr;
	}
}
