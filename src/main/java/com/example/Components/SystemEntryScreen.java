package com.example.Components;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.example.LS.Equation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SystemEntryScreen extends JPanel implements ActionListener
{

	SizeEntryPanel sizePanel;
	SystemEntryPanel systemEntryPanel = null;
	JScrollPane scrollPane;
	JButton bNext;
    JButton bBack;
    JPanel footerPanel;



	public SystemEntryScreen()
	{
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setLayout(new BorderLayout());
		sizePanel = new SizeEntryPanel();
		add(sizePanel, BorderLayout.NORTH);
        footerPanel = new JPanel();
        add(footerPanel, BorderLayout.SOUTH);
        bBack = new JButton("Back to Start Screen");
        bBack.setActionCommand("Back");
		bBack.addActionListener(this);
        footerPanel.add(bBack);
		bNext = new JButton("Enter system size");
		bNext.setEnabled(false);
		bNext.setActionCommand("Next");
		bNext.addActionListener(this);
		footerPanel.add(bNext);
		scrollPane = new JScrollPane();
		add(scrollPane);
	}

	public void addSystemEntryPanel(int size)
	{
		if (systemEntryPanel != null)
		{
			scrollPane.setViewportView(null);
			bNext.setEnabled(false);
			bNext.setText("Enter System Size");
		}
		if (size != 0 && size != 1)
		{
			systemEntryPanel = new SystemEntryPanel(size);
			scrollPane.setViewportView(systemEntryPanel);
			bNext.setEnabled(true);
			bNext.setText("Next");
		}
		revalidate();
		repaint();
	}

	public void actionPerformed(ActionEvent ae)
	{
        if (ae.getActionCommand().equals("Next"))
        {
            Equation[] sys = systemEntryPanel.getSystem();
		    ((AppFrame)getTopLevelAncestor()).onSystemEntryNext(sys);
        }
        else ((AppFrame)getTopLevelAncestor()).onSystemEntryBack();

	}
}



class SizeEntryPanel extends JPanel implements CaretListener
{
	final int maxSystemSize = 19;
	JLabel enterSize;
	JTextField sizeField;
	int systemSize;


	SizeEntryPanel()
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
		enterSize = new JLabel("System Size = ");
		sizeField = new JTextField("", 5);
		sizeField.addCaretListener(this);
		add(enterSize);
		add(sizeField);
	}

	public void caretUpdate(CaretEvent ae)
	{
		// String txt = sizeField.getText();
		updateSize();
	}

	private void updateSize()
	{
		int temp = systemSize;
		try
		{
			String text = sizeField.getText();
			systemSize = Integer.parseInt(text);
		}
		catch(NumberFormatException e)
		{
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {sizeField.setText(null);}
			});
			systemSize = 0;
		}

		//Setting a maximum system size
		// if (systemSize > maxSystemSize)
		// {
		// 	SwingUtilities.invokeLater(new Runnable(){
		// 		public void run() {sizeField.setText(String.valueOf(maxSystemSize));}
		// 	});
		// 	systemSize = maxSystemSize;
		// }


		if (temp != systemSize)
		{
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {((SystemEntryScreen)getParent()).addSystemEntryPanel(systemSize);}
			});
		}
	}

}


class SystemEntryPanel extends JPanel
{
	int size;
	SystemEntryPanel(int size)
	{
		this.size = size;
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setLayout(new GridLayout(size+1, size+1, 30, 20));
		for (int i=1; i <= size; i++)
		{
			add(new JLabel("x"+i));
		}
		add(new JLabel("b"));

		for (int row=0; row<size; row++)
		{
			for (int col=0; col<=size; col++)
			{
				add(new DoubleTextField(5));
			}
		}
	}

	public Equation[] getSystem()
	{
		Equation[] system = new Equation[size];
		for (int row=0; row < size; row++)
		{
			double[] coeffs = new double[size];
			for (int col=0; col < size; col++)
			{
				coeffs[col] = ((DoubleTextField)getComponent((size+1)*(row+1) + col)).value;
			}
			double b = ((DoubleTextField)getComponent((size+1)*(row+1) + size)).value;
			system[row] = new Equation(coeffs, b,0);
		}

		return system;
	}
}

// class DoubleTextField extends JTextField implements CaretListener
// {
// 	double value = 0;

// 	DoubleTextField()
// 	{
// 		super();
// 		addCaretListener(this);
// 	}

// 	DoubleTextField(int cols)
// 	{
// 		super(cols);
// 	}


// 	public void caretUpdate(CaretEvent ce)
// 	{
// 		try{
// 			value = Double.parseDouble(getText());
// 		}
// 		catch (NumberFormatException e)
// 		{
// 			SwingUtilities.invokeLater(new Runnable(){
// 				public void run() {setText(null);}
// 			});
// 			value = 0;
// 		}
// 	}
// }
