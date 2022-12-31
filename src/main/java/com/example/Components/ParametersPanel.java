package com.example.Components;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.example.LS.LUDecomposition.*;
import com.example.RF.FunctionExpression;

import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

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

		return new IndirectParams(initial, maxIters, Math.abs(relativeErr));
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


//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------

class BracketingParamsPanel extends ParametersPanel
{

    DoubleTextField xlField, xuField;

    BracketingParamsPanel()
    {
        super();
        JLabel xlLabel = new JLabel("xl = ");
        JLabel xuLabel = new JLabel("xu = ");
        xlField = new DoubleTextField(5);
        xuField = new DoubleTextField(5);
        add(xlLabel);
        add(xlField);
        add(xuLabel);
        add(xuField);
    }

    public Parameters getParams()
    {
        double xl, xu;
        if (xlField.getText().isEmpty() || xuField.getText().isEmpty())
        {
            return null;
        }
        xl = Double.parseDouble(xlField.getText());
        xu = Double.parseDouble(xuField.getText());
        return new BracketingParams(xl, xu);
    }
}


class FixedPointParamsPanel extends ParametersPanel
{
    JTextField gField;
    DoubleTextField initialField;

    FixedPointParamsPanel()
    {
        super();
        JLabel gLabel = new JLabel("g(x) = ");
        JLabel initialLabel = new JLabel("Initial x = ");
        gField = new JTextField(15);
        initialField = new DoubleTextField(5);
        add(initialLabel);
        add(initialField);
        add(gLabel);
        add(gField);
    }

    public Parameters getParams()
    {
        FunctionExpression g;
        try
        {
            g = new FunctionExpression(gField.getText());
        }
        catch (UnknownFunctionOrVariableException e)
        {
            return null;
        }
        if (initialField.getText().isEmpty()) return null;

        double initial = Double.parseDouble(initialField.getText());
        return new FixedPointParams(initial, g);
    }
}


class NewtonRaphsonParamsPanel extends ParametersPanel
{
    DoubleTextField initialField;

    NewtonRaphsonParamsPanel()
    {
        super();
        initialField = new DoubleTextField(5);
        JLabel intialLabel = new JLabel("Initial x = ");
        add(intialLabel);
        add(initialField);
    }

    public Parameters getParams()
    {
        if (initialField.getText().isEmpty()) return null;
        double x = Double.parseDouble(initialField.getText());
        return new NewtonRaphsonParams(x);
    }
}


class SecantParamsPanel extends ParametersPanel
{
    DoubleTextField init1Field, init2Field;

    SecantParamsPanel()
    {
        super();
        init1Field = new DoubleTextField(5);
        init2Field = new DoubleTextField(5);
        JLabel init1Label = new JLabel("Initial x0 = ");
        JLabel init2Label = new JLabel("Initial x1 = ");
        add(init1Label);
        add(init1Field);
        add(init2Label);
        add(init2Field);
    }

    public Parameters getParams()
    {
        if (init1Field.getText().isEmpty() || init2Field.getText().isEmpty()) return null;
        double x0, x1;
        x0 = Double.parseDouble(init1Field.getText());
        x1 = Double.parseDouble(init2Field.getText());
        return new SecantParams(x0, x1);
    }
}



class BracketingParams extends Parameters
{
    public double xl, xu;

    BracketingParams(double xl, double xu)
    {
        this.xl = xl;
        this.xu = xu;
    }
}
class FixedPointParams extends Parameters
{
    public double initial;
    public FunctionExpression g;

    FixedPointParams(double initial, FunctionExpression g)
    {
        this.initial = initial;
        this.g = g;
    }
}
class NewtonRaphsonParams extends Parameters
{
    public double initial;
    NewtonRaphsonParams(double initial) {this.initial = initial;}
}
class SecantParams extends Parameters
{
    public double initial1, initial2;
    SecantParams(double initial1, double initial2)
    {
        this.initial1 = initial1;
        this.initial2 = initial2;
    }
}
