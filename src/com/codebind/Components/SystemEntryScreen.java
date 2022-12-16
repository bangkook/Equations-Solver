package com.codebind.Components;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import java.awt.*;
import java.awt.event.*;


public class SystemEntryScreen extends JPanel{
	
	SizeEntryPanel sizePanel;
	SystemEntryPanel systemEntryPanel = null;


	public SystemEntryScreen()
	{
		setLayout(new BorderLayout());
		sizePanel = new SizeEntryPanel();
		add(sizePanel, BorderLayout.NORTH);

	}

	public void addSystemEntryPanel(int size)
	{
		if (systemEntryPanel != null)
		{
			remove(systemEntryPanel);
		}
		if (size != 0)
		{
			systemEntryPanel = new SystemEntryPanel(size);
			add(systemEntryPanel, BorderLayout.CENTER);

		}
		revalidate();
		repaint();
	}
}



class SizeEntryPanel extends JPanel
{

	JLabel enterSize;
	JTextField sizeField;
	int SystemSize;
	
	
	SizeEntryPanel()
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
		enterSize = new JLabel("System Size = ");
		sizeField = new JTextField("", 5);
		sizeField.addCaretListener(new SizeListener());
		add(enterSize);
		add(sizeField);
	}

	private void updateSize()
	{
		int temp = SystemSize;
		try 
		{
			SystemSize = Integer.parseInt(sizeField.getText());
		}
		catch(NumberFormatException e)
		{
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {sizeField.setText(null);}
			});
			SystemSize = 0;
		}

		if (temp != SystemSize)
		{
			((SystemEntryScreen)getParent()).addSystemEntryPanel(SystemSize);
		}
	}

	class SizeListener implements CaretListener
	{
		public void caretUpdate(CaretEvent ae)
		{
			updateSize();
		}
	}
}


class SystemEntryPanel extends JPanel
{

	SystemEntryPanel(int size)
	{
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
				add(new JTextField());
			}
		}
	}
}