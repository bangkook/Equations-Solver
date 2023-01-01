package com.example.Components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import com.example.Components.RFMethodSelectScreen.RFMethod;
import com.example.RF.*;


public class RFSolutionScreen extends JPanel
{
    PlotScreen plotScreen;
    SolutionScreen solutionScreen;


    RFSolutionScreen()
    {
        setLayout(new CardLayout());
    }

    public void setSolvingMode(FunctionExpression f, int pre, int iters, double err, RFMethod method, Parameters params)
    {
        clearScreen();
        RootFinder rootfinder;
        boolean usePre = pre != -1;
        switch (method)
        {
            case Bisection:
            double xl, xu;
            xl = ((BracketingParams)params).xl;
            xu = ((BracketingParams)params).xu;
            rootfinder = new Bisection(usePre, pre, xl, xu, iters, f);
            break;

            case FalsePos:
            xl = ((BracketingParams)params).xl;
            xu = ((BracketingParams)params).xu;
            rootfinder = new False_Position(usePre, pre, xl, xu, iters, f);
            break;

            case FixedPoint:
            double FPinit = ((FixedPointParams)params).initial;
            FunctionExpression g = ((FixedPointParams)params).g;
            rootfinder = new FixedPoint(g, FPinit, usePre, pre, err, iters);
            break;

            case Newton:
            double NRinit = ((FixedPointParams)params).initial;
            rootfinder = new NewtonRaphson(f, NRinit, usePre, pre, err, iters);

            default:
            double init1 = ((SecantParams)params).initial1;
            double init2 = ((SecantParams)params).initial2;
            rootfinder = new Secant(f, init1, init2, usePre, pre, err, iters);
        }


        solutionScreen = new SolutionScreen(rootfinder, method);
        add(solutionScreen, "SolutionScreen");
        plotScreen = new PlotScreen();
        add(plotScreen, "PlotScreen");

    }

    void clearScreen() {
        if (solutionScreen != null) remove(solutionScreen);
        if (plotScreen != null) remove(plotScreen);
        revalidate();
        repaint();
    }

	void onBackPressed()
	{
		((AppFrame)getTopLevelAncestor()).onRFSolutionBack();
	}

    void switchToPlot()
    {
        ((CardLayout)getLayout()).show(this, "PlotScreen");
    }
    void switchToSol()
    {

    }


    class PlotScreen extends JPanel
    {
        XChartPanel<XYChart> chartPanel;

        PlotScreen()
        {
            setLayout(new BorderLayout());
            // chartPanel = new XChartPanel<XYChart>(null);
            chartPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));

        }

        public void plotFunc(FunctionExpression f, int l, int u, double step)
        {

        }

        public void clearPlot()
        {

        }
    }

    class SolutionScreen extends JPanel implements ActionListener
    {
		JButton bBack, bPlot;

        SolutionScreen(RootFinder rootFinder, RFMethod method)
        {
            String solution;
            setLayout(new BorderLayout());
			JPanel footerPanel = new JPanel();
			add(footerPanel, BorderLayout.SOUTH);
			bPlot = new JButton("Plot");
			bBack = new JButton("Back");
            bPlot.addActionListener(this);
            bBack.addActionListener(this);
			footerPanel.add(bBack);
			footerPanel.add(bPlot);

            try
            {
                solution = String.valueOf(rootFinder.getRoot());
                add(new JLabel("Solution = " + solution), BorderLayout.NORTH);
                String stepsFile;
                switch (method)
                {
                    case Bisection:
                    stepsFile = "Bisection.txt";
                    break;
                    case FalsePos:
                    stepsFile = "False_Position.txt";
                    break;
                    case FixedPoint:
                    stepsFile = "FixedPoint.txt";
                    break;
                    case Newton:
                    stepsFile = "Newton-Raphson.txt";
                    break;
                    default:
                    stepsFile = "Secant.txt";
                    break;
                }
                JScrollPane sp = new JScrollPane();
                add(sp);
                sp.setViewportView(new stepsPanel(new File(stepsFile)));
            }
            catch (Exception e)
            {
                solution = "Couldn't find Solution";
                add(new JLabel("Coudn't converge to a solution"));
            }
        }

		public void actionPerformed(ActionEvent ae)
		{
			if (ae.getActionCommand().equals("Back"))
			{
                onBackPressed();
			}
            else
            {
                switchToPlot();
            }

		}

        class stepsPanel extends JPanel {

            stepsPanel(File file) {
                setLayout(new GridLayout(0, 1, 0, 3));
                try {
                    Scanner scn = new Scanner(file);
                    while (scn.hasNextLine()) {
                        String line = scn.nextLine();
                        add(new JLabel(line));
                    }
                    scn.close();
                } catch (FileNotFoundException e) {
                    System.out.println("steps file not found");
                }

            }
        }
    }

}
