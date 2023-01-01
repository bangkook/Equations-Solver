package com.example.Components;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


import org.knowm.xchart.*;

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
		double plotCenter;
        FunctionExpression funcToPlot = f;
        switch (method)
        {
            case Bisection:
            double xl, xu;
            xl = ((BracketingParams)params).xl;
            xu = ((BracketingParams)params).xu;
			plotCenter = xl;

            rootfinder = new Bisection(usePre, pre, xl, xu, iters,err, f);
            break;

            case FalsePos:
            xl = ((BracketingParams)params).xl;
            xu = ((BracketingParams)params).xu;
			plotCenter = xl;
            rootfinder = new False_Position(usePre, pre, xl, xu, iters,err, f);

            break;

            case FixedPoint:
            double FPinit = ((FixedPointParams)params).initial;
			plotCenter = FPinit;
            FunctionExpression g = funcToPlot = ((FixedPointParams)params).g;
            rootfinder = new FixedPoint(g, FPinit, usePre, pre, err, iters);
            break;

            case Newton:
            double NRinit = ((NewtonRaphsonParams)params).initial;
			plotCenter = NRinit;
            rootfinder = new NewtonRaphson(f, NRinit, usePre, pre, err, iters);
			break;

            default:
            double init1 = ((SecantParams)params).initial1;
            double init2 = ((SecantParams)params).initial2;
			plotCenter = init2;
            rootfinder = new Secant(f, init1, init2, usePre, pre, err, iters);
        }


        solutionScreen = new SolutionScreen(rootfinder, method);
        add(solutionScreen, "SolutionScreen");
        plotScreen = new PlotScreen(funcToPlot, method, plotCenter);
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
        ((CardLayout)getLayout()).show(this, "SolutionScreen");
    }


    class PlotScreen extends JPanel implements ActionListener
    {
        XChartPanel<XYChart> chartPanel;
        XYChart chart;
        RFMethod method;
        JButton bSolution, bNextStep, bBack;
        double funcBounds;

        PlotScreen(FunctionExpression f, RFMethod method, double plotCenter)
        {
            this.method = method;
            setLayout(new BorderLayout());
            double[] bounds = {plotCenter - 50, plotCenter + 50};
            chart = new XYChart(WIDTH, HEIGHT);
            chart.addSeries("x-Axis", bounds, new double[2]);
            chartPanel = new XChartPanel<XYChart>(chart);
            String funcName = method == RFMethod.FixedPoint ? "g(x)" : "f(x)";
            plotFunc(f, funcName, bounds[0], bounds[1], 0.5);
            chartPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
            add(chartPanel);
            bSolution = new JButton("Result");
            bNextStep = new JButton("Next Step");
            bBack = new JButton("Back");
            JPanel footer = new JPanel();
            add(footer, BorderLayout.SOUTH);
            footer.add(bBack);
            footer.add(bSolution);
            JPanel side = new JPanel();
            add(side, BorderLayout.EAST);
            side.add(bNextStep);
            bBack.addActionListener(this);
            bNextStep.addActionListener(this);
            bSolution.addActionListener(this);

        }

        public void plotFunc(FunctionExpression f, String name, double l, double u, double step)
        {
            int n = (int) ((u-l) / step);
            double[] xes = new double[n];
            double[] ys = new double[n];

            int i = 0;
            for (double x = l; i < n; x += step)
            {
                xes[i] = x;
                ys[i] = f.evaluate(x);
                i++;
            }
            chart.addSeries(name, xes, ys);
        }


        public void actionPerformed(ActionEvent ae)
        {
            if (ae.getActionCommand().equals("Back")) onBackPressed();
            else if (ae.getActionCommand().equals("Next Step"))
            {

            }
            else switchToSol();
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
