package com.example.Components;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class IntTextField extends JTextField implements CaretListener {

	int value = 0;

	IntTextField()
	{
		super();
		addCaretListener(this);
	}

	IntTextField(int cols)
	{
		super(cols);
		addCaretListener(this);
	}


	public void caretUpdate(CaretEvent ce)
	{
		try{
			value = Integer.parseInt(getText());
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
