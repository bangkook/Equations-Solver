package com.codebind.Components;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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


class LUParamsPanel extends ParametersPanel implements ActionListener
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
		formBox.addActionListener(this);
		add(new JLabel("Decomposition Form: "));
		add(formBox);
	}

	public Parameters getParams()
	{
		return new LUParams(form);
	}

	public void actionPerformed(ActionEvent ae)
	{
		String txt = (String)formBox.getSelectedItem();
		switch (txt)
		{
			case "Doolittle":
			form = LUForm.Doolittle;
			break;
			case "Cholesky":
			form = LUForm.Cholesky;
			break;
			case "Crout":
			form = LUForm.Crout;
			break;
		}
	}
}


class IndirectParamsPanel extends ParametersPanel
{
	double[] initial;
	JPanel initPanel;
	final int defaultMaxIters = 50;
	final double defaultRelativeErr = 0.001;
	// int maxIters = defaultMaxIters;
	// double relativeErr = defaultRelativeErr;
	IntTextField maxItersField;
	DoubleTextField relativeErrField;


	public IndirectParamsPanel(int size)
	{
		super();
		setLayout(new FlowLayout(FlowLayout.LEADING));
		add(new JLabel("Initial Guess:"));
		initPanel = new JPanel(new GridLayout(2, size));
		initial = new double[size];
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
		guessScrollPane.setPreferredSize(new Dimension(300, 90));
		add(guessScrollPane);
		add(new JLabel("Max number of iterations = (Default = " + defaultMaxIters + ")"));
		maxItersField = new IntTextField(3);
		add(maxItersField);
		add(new JLabel("Relative Error = (Default = " + defaultRelativeErr + ")"));
		relativeErrField = new DoubleTextField(5);
		add(relativeErrField);
	}

	public Parameters getParams()
	{
		int maxIters = defaultMaxIters;
		double relativeErr = defaultRelativeErr;
		if (!maxItersField.getText().isEmpty())
		{
			maxIters = Integer.parseInt(maxItersField.getText());
		}
		if (!relativeErrField.getText().isEmpty())
		{
			relativeErr = Double.parseDouble(relativeErrField.getText());
		}
		for (int i=0; i<initial.length; i++)
		{
			DoubleTextField field = (DoubleTextField)initPanel.getComponent(initial.length + i);
			initial[i] = field.value;
		}

		return new IndirectParams(initial, maxIters, relativeErr);
	}
}




class Parameters
{

}

class LUParams extends Parameters
{
	public LUForm form;

	LUParams(LUForm form)
	{
		this.form = form;
	}
}

class IndirectParams extends Parameters
{
	public double[] initial;
	public int maxIters;
	public double relativeErr;

	IndirectParams(double[] initial, int maxIters, double relativeErr)
	{
		this.initial = initial;
		this.maxIters = maxIters;
		this.relativeErr = relativeErr;
	}
}
