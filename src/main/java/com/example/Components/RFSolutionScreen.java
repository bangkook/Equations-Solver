package com.example.Components;

import java.awt.*;
import java.awt.event.*;
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
        FunctionExpression f;
        JButton bSolution, bNextStep, bBack, bPrevStep;
        double funcBounds;
        Step[] steps;
        int stepPointer = -1;


        PlotScreen(FunctionExpression f, RFMethod method, double plotCenter)
        {
            this.method = method;
            this.f = f;
            buildSteps(method, f);
            setLayout(new BorderLayout());
            chart = new XYChart(WIDTH, HEIGHT);
            double[] yaxis = {-10, 10};
            chart.addSeries("y-Axis", new double[2], yaxis);
            // double[] bounds = {plotCenter - 50, plotCenter + 50};
            // chart.addSeries("x-Axis", bounds, new double[2]);
            // String funcName = method == RFMethod.FixedPoint ? "g(x)" : "f(x)";
            // plotFunc(f, funcName, bounds[0], bounds[1], 50);
            // if (method == RFMethod.FixedPoint)
            // {
            //     chart.addSeries("y = x", bounds, bounds);
            // }
            chartPanel = new XChartPanel<XYChart>(chart);
            chartPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
            add(chartPanel);
            bSolution = new JButton("Result");
            bNextStep = new JButton("Next Step");
            bPrevStep = new JButton("Previous Step");
            bPrevStep.setEnabled(false);
            bBack = new JButton("Back");
            JPanel footer = new JPanel();
            add(footer, BorderLayout.SOUTH);
            footer.add(bBack);
            footer.add(bSolution);
            JPanel side = new JPanel();
            side.setLayout(new GridLayout(0, 1, 3, 3));
            add(side, BorderLayout.EAST);
            side.add(bNextStep);
            side.add(bPrevStep);
            bBack.addActionListener(this);
            bNextStep.addActionListener(this);
            bPrevStep.addActionListener(this);
            bSolution.addActionListener(this);
            nextStep();

        }

        public void plotFunc(FunctionExpression f, String name, double l, double u, int n)
        {
            plotFunc(f, name, l, u, n, -20, 20);
        }


        public void plotFunc(FunctionExpression f, String name, double l, double u, int n, double yl, double yu)
        {
            double step = (u - l) / n;
            double[] xes = new double[n];
            double[] ys = new double[n];
            boolean filling = false;
            int i = 0;
            for (double x = l; i < n; x += step)
            {
                if (!filling && (f.evaluate(x) > 20 || f.evaluate(x) < -20)) continue;
                else filling = true;
                xes[i] = x;
                ys[i] = f.evaluate(x);
                i++;
            }
            if (i == n)
            {
                chart.addSeries(name, xes, ys);
                return;
            }
            double[] finalx = new double[i];
            double[] finaly = new double[i];
            for (int j=0; j<i; j++)
            {
                finalx[j] = xes[j];
                finaly[j] = ys[j];
            }
            chart.addSeries(name, finalx, finaly);
        }

        void buildSteps(RFMethod method, FunctionExpression f)
        {
            Scanner scn;
            try
            {
                switch (method)
                {
                    case Bisection:
                    scn = new Scanner(new File("Bisection.txt"));
                    int i = -1;
                    while (scn.hasNextLine())
                    {
                        scn.nextLine();
                        i++;
                    }
                    steps = new BisectionStep[i];
                    i = 0;
                    scn = new Scanner(new File("Bisection.txt"));
                    scn.nextLine();
                    while (i < steps.length)
                    {
                        BisectionStep step = new BisectionStep();
                        scn.nextInt();
                        step.xl = scn.nextDouble();
                        step.xu = scn.nextDouble();
                        scn.nextDouble();
                        scn.nextDouble();
                        steps[i] = step;
                        i++;
                    }
                    break;
                    case FalsePos:
                    scn = new Scanner(new File("False_Position.txt"));
                    i = -1;
                    while (scn.hasNextLine())
                    {
                        scn.nextLine();
                        i++;
                    }
                    steps = new FalsePosStep[i];
                    i = 0;
                    scn = new Scanner(new File("False_Position.txt"));
                    scn.nextLine();
                    while (i < steps.length)
                    {
                        FalsePosStep step = new FalsePosStep();
                        scn.nextInt();
                        step.xl = scn.nextDouble();
                        step.xu = scn.nextDouble();
                        step.yl = f.evaluate(step.xl);
                        step.yu = f.evaluate(step.xu);
                        scn.nextDouble();
                        scn.nextDouble();
                        steps[i] = step;
                        i++;
                    }
                    break;
                    case FixedPoint:
                    scn = new Scanner(new File("FixedPoint.txt"));
                    i = -1;
                    while (scn.hasNextLine())
                    {
                        scn.nextLine();
                        i++;
                    }
                    steps = new FixedPointStep[i];
                    i = 0;
                    scn = new Scanner(new File("FixedPoint.txt"));
                    scn.nextLine();
                    while (i < steps.length)
                    {
                        FixedPointStep step = new FixedPointStep();
                        scn.nextInt();
                        step.xr = scn.nextDouble();
                        step.xr2 = scn.nextDouble();
                        scn.nextDouble();
                        steps[i] = step;
                        i++;
                    }
                    break;
                    case Newton:
                    scn = new Scanner(new File("Newton-Raphson.txt"));
                    i = -1;
                    while (scn.hasNextLine())
                    {
                        scn.nextLine();
                        i++;
                    }
                    steps = new NewtonStep[i];
                    i = 0;
                    scn = new Scanner(new File("Newton-Raphson.txt"));
                    scn.nextLine();
                    while (i < steps.length)
                    {
                        NewtonStep step = new NewtonStep();
                        scn.nextInt();
                        step.xr = scn.nextDouble();
                        step.yr = f.evaluate(step.xr);
                        step.tangentIntercept = scn.nextDouble();
                        scn.nextDouble();
                        steps[i] = step;
                        i++;
                    }
                    break;
                    default:
                    scn = new Scanner(new File("Secant.txt"));
                    i = -1;
                    while (scn.hasNextLine())
                    {
                        scn.nextLine();
                        i++;
                    }
                    steps = new SecantStep[i];
                    i = 0;
                    scn = new Scanner(new File("Secant.txt"));
                    scn.nextLine();
                    while (i < steps.length)
                    {
                        SecantStep step = new SecantStep();
                        scn.nextInt();
                        step.x1 = scn.nextDouble();
                        step.y1 = f.evaluate(step.x1);
                        step.x2 = scn.nextDouble();
                        step.y2 = f.evaluate(step.x2);
                        step.intercept = scn.nextDouble();
                        scn.nextDouble();
                        steps[i] = step;
                        i++;
                    }
                    scn.close();
                }
            }
            catch (FileNotFoundException e)
            {
                System.out.println("Problem building steps.");
                e.printStackTrace();
            }
        }

        public void actionPerformed(ActionEvent ae)
        {
            if (ae.getActionCommand().equals("Back")) onBackPressed();
            else if (ae.getActionCommand().equals("Next Step")) nextStep();
            else if (ae.getActionCommand().equals("Previous Step")) prevStep();
            else switchToSol();
        }

        void nextStep()
        {
            stepPointer++;
            plotStep();
            bNextStep.setEnabled(stepPointer != steps.length-1);
            bPrevStep.setEnabled(stepPointer != 0);
        }
        void prevStep()
        {
            stepPointer--;
            plotStep();
            bNextStep.setEnabled(stepPointer != steps.length-1);
            bPrevStep.setEnabled(stepPointer != 0);
        }
        void plotStep()
        {
            switch (method)
            {
                case Bisection:
                chart.removeSeries("Lower Bound");
                chart.removeSeries("Upper Bound");
                chart.removeSeries("f(x)");
                chart.removeSeries("x-Axis");
                BisectionStep bistep = (BisectionStep)steps[stepPointer];
                double[] bounds = new double[2];
                bounds[0] = bistep.xl - 1;
                bounds[1] = bistep.xu + 1;
                chart.addSeries("x-Axis", bounds, new double[2]);
                plotFunc(f, "f(x)", bounds[0], bounds[1], 50);
                double[] xDataBiL = {bistep.xl, bistep.xl};
                double[] yDataBiL = {0, f.evaluate(bistep.xl)};
                chart.addSeries("Lower Bound", xDataBiL, yDataBiL);
                double[] xDataBiU = {bistep.xu, bistep.xu};
                double[] yDataBiU = {0, f.evaluate(bistep.xu)};
                chart.addSeries("Upper Bound", xDataBiU, yDataBiU);
                break;

                case FalsePos:
                chart.removeSeries("Lower Bound");
                chart.removeSeries("Upper Bound");
                chart.removeSeries("f(x)");
                chart.removeSeries("x-Axis");
                chart.removeSeries("Intercept");
                FalsePosStep fpstep = (FalsePosStep)steps[stepPointer];
                double[] fpbounds = new double[2];
                fpbounds[0] = fpstep.xl - 1;
                fpbounds[1] = fpstep.xu + 1;
                chart.addSeries("x-Axis", fpbounds, new double[2]);
                plotFunc(f, "f(x)", fpbounds[0], fpbounds[1], 50);
                double[] xDataFPL = {fpstep.xl, fpstep.xl};
                double[] yDataFPL = {0, f.evaluate(fpstep.xl)};
                chart.addSeries("Lower Bound", xDataFPL, yDataFPL);
                double[] xDataFPU = {fpstep.xu, fpstep.xu};
                double[] yDataFPU = {0, f.evaluate(fpstep.xu)};
                chart.addSeries("Upper Bound", xDataFPU, yDataFPU);
                double[] xInterceptFP = {fpstep.xl,fpstep.xu};
                double[] yInterceptFP = {fpstep.yl, fpstep.yu};
                chart.addSeries("Intercept", xInterceptFP, yInterceptFP);
                break;

                case FixedPoint:
                chart.removeSeries("f(x)");
                chart.removeSeries("x-Axis");
                chart.removeSeries("y = x");
                chart.removeSeries("Step");
                chart.removeSeries("y-Axis");
                FixedPointStep FPstep = (FixedPointStep)steps[stepPointer];
                double[] FPbounds = new double[2];
                FPbounds[0] = FPstep.xr - 10;
                FPbounds[1] = FPstep.xr + 10;
                chart.addSeries("x-Axis", FPbounds, new double[2]);
                plotFunc(f, "f(x)", FPbounds[0], FPbounds[1], 50, 15, 15);
                double[] xDataFP = new double[(stepPointer+1)*2 + 1];
                double[] yDataFP = new double[(stepPointer+1)*2 + 1];
				xDataFP[0] = ((FixedPointStep)steps[0]).xr;
				yDataFP[0] = 0;
                int temp = 1;
                while (temp < (stepPointer+1)*2)
                {
                    xDataFP[temp] = ((FixedPointStep)steps[temp/2]).xr;
                    yDataFP[temp] = ((FixedPointStep)steps[temp/2]).xr2;
                    temp++;
                    xDataFP[temp] = ((FixedPointStep)steps[temp/2]).xr;
                    yDataFP[temp] = ((FixedPointStep)steps[temp/2]).xr;
                    temp++;
                }
                chart.addSeries("Step", xDataFP, yDataFP);
                chart.addSeries("y = x", FPbounds, FPbounds);
                break;
                case Newton:
                chart.removeSeries("f(x)");
                chart.removeSeries("x-Axis");
                chart.removeSeries("Tangent");
                NewtonStep newstep = (NewtonStep)steps[stepPointer];
                double[] newBound = new double[2];
                newBound[0] = newstep.tangentIntercept < newstep.xr ? newstep.tangentIntercept : newstep.xr;
                newBound[0] -= 1;
                newBound[1] = newstep.tangentIntercept > newstep.xr ? newstep.tangentIntercept : newstep.xr;
                newBound[1] += 1;
                chart.addSeries("x-Axis", newBound, new double[2]);
                plotFunc(f, "f(x)", newBound[0], newBound[1], 50);
                double[] tangentx = {newstep.xr, newstep.tangentIntercept};
                double[] tangenty = {newstep.yr, 0};
                chart.addSeries("Tangent", tangentx, tangenty);
                break;
                default:
                chart.removeSeries("f(x)");
                chart.removeSeries("x-Axis");
                chart.removeSeries("Slope");
                SecantStep secstep = (SecantStep)steps[stepPointer];
                double[] secBounds = new double[2];
                secBounds[0] = Math.min(secstep.x1, secstep.x2);
                secBounds[0] = Math.min(secBounds[0], secstep.intercept);
                secBounds[0] -= 1;
                secBounds[1] = Math.max(secstep.x1, secstep.x2);
                secBounds[1] = Math.max(secBounds[1], secstep.intercept);
                secBounds[1] += 1;
                chart.addSeries("x-Axis", secBounds, new double[2]);
                plotFunc(f, "f(x)", secBounds[0], secBounds[1], 50);
                double[] slopex = {secstep.x1, secstep.x2, secstep.intercept};
                double[] slopey = {secstep.y1, secstep.y2, 0};
                chart.addSeries("Slope", slopex, slopey);
                break;
            }
            revalidate();
            repaint();
        }

        class Step{

        }
        class BisectionStep extends Step
        {
            public double xl, xu;
        }
        class FalsePosStep extends Step
        {
            public double xl, xu, yl, yu;
        }
        class FixedPointStep extends Step
        {
            public double xr, xr2;
        }
        class NewtonStep extends Step
        {
            public double xr, yr, tangentIntercept;
        }
        class SecantStep extends Step
        {
            public double x1, x2, y1, y2, intercept;
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
                JPanel header = new JPanel();

                solution = String.valueOf(rootFinder.getRoot());
                add(header, BorderLayout.NORTH);
                header.add(new JLabel("Solution = " + solution));
                header.add(new JLabel("Time = " + String.valueOf(rootFinder.getTime()) + "ms"));
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
