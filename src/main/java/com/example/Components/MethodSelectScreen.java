package com.example.Components;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.example.LS.Equation;



public class MethodSelectScreen extends JPanel implements ActionListener
{
	Equation[] system;
	MethodPanel headerPanel;
	ParametersPanel paramsPanel;
	JButton back;
	JButton getSol;


	MethodSelectScreen()
	{
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setLayout(new BorderLayout());
		headerPanel = new MethodPanel();
		headerPanel.myParent = this;
		headerPanel.setBackground(Color.CYAN);;
		JScrollPane headScrollPane = new JScrollPane(headerPanel);
		headScrollPane.setPreferredSize(new Dimension(200, 50));
		add(headScrollPane, BorderLayout.NORTH);
		JPanel footerPanel = new JPanel();
		add(footerPanel, BorderLayout.SOUTH);
		back = new JButton("Back");
		back.addActionListener(this);
		footerPanel.add(back);
		getSol = new JButton("Calculate Solution");
		getSol.addActionListener(this);
		footerPanel.add(getSol);
	}

	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getActionCommand() == "Back")
		{
			((AppFrame)getTopLevelAncestor()).onMethodSelectBack();
		}
		else
		{
			Parameters params = null;
			if (paramsPanel != null) params = paramsPanel.getParams();
			for (int i=0; i < system.length; i++)
			{
				system[i].setPrecision(headerPanel.precision);
			}
			((AppFrame)getTopLevelAncestor()).onMethodSelectGetSol(system, headerPanel.method, headerPanel.scaling, params);
		}
	}

	public void setSystem(Equation[] sys)
	{
		system = sys;
	}

	public void createParamsPanel(Method method)
	{
		if (paramsPanel != null)
		{
			remove(paramsPanel);
			paramsPanel = null;
		}
		switch(method)
		{
			case GaussElimination:
			case GaussJordan:
			break;

			case LU:
			paramsPanel = new LUParamsPanel();
			break;

			case GaussSeidel:
			case Jacobi:
			paramsPanel = new IndirectParamsPanel(system.length);
			break;
		}
		if (paramsPanel != null)
		{
			add(paramsPanel);
		}
		revalidate();
		repaint();
	}

}


class MethodPanel extends JPanel implements CaretListener, ActionListener
{
	public MethodSelectScreen myParent;
	final int defaultPrecision = 15;
	int precision = defaultPrecision;
	boolean scaling = false;
	Method method;
	JTextField precisionField;
	JComboBox<String> methodBox;
	JCheckBox scalingBox;


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
		methodBox.addActionListener(this);
		method = Method.GaussElimination;
		add(methodBox);
		precisionField = new JTextField(2);
		precisionField.addCaretListener(this);
		add(new JLabel("Precision ="));
		add(precisionField);
		add(new JLabel("(Default = " + defaultPrecision + ")"));
		scalingBox = new JCheckBox("Use Scaling", scaling);
		scalingBox.addActionListener(this);
		add(scalingBox);
	}

	public void caretUpdate(CaretEvent ae)
	{
		try
		{
			String text = precisionField.getText();
			precision = Integer.parseInt(text);
			if (precision == 0) throw new NumberFormatException();
		}
		catch(NumberFormatException e)
		{
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {precisionField.setText(null);}
			});
			precision = defaultPrecision;
		}
	}

	public void actionPerformed(ActionEvent ae)
	{
		String command = ae.getActionCommand();
		if (command == "comboBoxChanged")
			onMethodSelected();
		else onScalingBoxChanged();

	}

	void onMethodSelected()
	{
		String selected = (String)methodBox.getSelectedItem();
		switch(selected)
		{
			case "Gauss Elimination":
			method = Method.GaussElimination;
			break;

			case "Gauss-Jordan":
			method = Method.GaussJordan;
			break;

			case "LU Decomposition":
			method = Method.LU;
			break;

			case "Gauss-Seidel":
			method = Method.GaussSeidel;
			break;

			case "Jacobi Iteration":
			method = Method.Jacobi;
			break;
		}
		myParent.createParamsPanel(method);
	}

	void onScalingBoxChanged()
	{
		scaling = scalingBox.isSelected();
	}
}


enum Method{GaussElimination, GaussJordan, LU, GaussSeidel, Jacobi}
