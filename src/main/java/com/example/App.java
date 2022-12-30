package com.example;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import com.example.Components.AppFrame;

public class App {
	public static void main(String[] args) {
        Runnable thread = () -> new App();
		SwingUtilities.invokeLater(thread);
	}

	App()
	{
		JFrame frm = new AppFrame("Linear System Solver");
		frm.setVisible(true);
		frm.setSize(600, 500);
	}

}


