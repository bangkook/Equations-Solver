package com.codebind.Components;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class DoubleTextField extends JTextField implements CaretListener
{
	double value = 0;
	
	DoubleTextField()
	{
		super();
		addCaretListener(this);
	}
	
	DoubleTextField(int cols)
	{
		super(cols);
		addCaretListener(this);
	}
	

	public void caretUpdate(CaretEvent ce)
	{
		try{
			String text = getText();
			if (text.equals("-"))
			{
				return;
			}
			value = Double.parseDouble(text);
		}
		catch (NumberFormatException e)
		{
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {setText(null);}
			});
			value = 0;
		}
	}
}
