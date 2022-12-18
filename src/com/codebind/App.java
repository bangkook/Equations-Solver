package com.codebind;

import javax.swing.*;
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
		JFrame frm = new AppFrame("Linear System Solver");
		frm.setVisible(true);
		frm.setSize(600, 500);
	}

}


