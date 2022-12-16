package com.codebind;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.codebind.Components.*;

class AppThread implements Runnable
{
	@Override
	public void run() {
		new App();
	}
}


public class App {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new AppThread());
	}

	

	App()
	{
		JFrame frm = new JFrame("Paint demo");

		initilizeFrame(frm);
		frm.add(new SystemEntryScreen());
		
	}
	
	void initilizeFrame(JFrame frm)
	{
		frm.getContentPane().setBackground(Color.YELLOW);
		frm.setLayout(new BorderLayout());
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setVisible(true);
		frm.setSize(500, 300);

	}


	class PaintPanel extends JPanel
	{
		
		PaintPanel()
		{
			setBorder(BorderFactory.createLineBorder(Color.CYAN, 5));
		}

		public Insets getInsets()
		{
			Insets oldIns = super.getInsets();
			// return oldIns;
			return new Insets(oldIns.top + 10, oldIns.left + 10, oldIns.bottom + 10, oldIns.right + 10);
		}

		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			int x, x2, y, y2;
			// Random rand = new Random();
			int w = getWidth();
			int h = getHeight();
			Insets ins = getInsets();

			x2 = w - ins.right;
			y2 = h - ins.bottom;
			for (int i = ins.left; i < 50; i+= 5)
			{
				x = i;
				y = i;
				g.drawLine(x, y, x2, y2);
			}
		}
	}
}
