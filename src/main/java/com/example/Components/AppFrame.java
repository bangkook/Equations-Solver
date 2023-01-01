package com.example.Components;

import com.example.Components.RFMethodSelectScreen.RFMethod;
import com.example.LS.*;
// import com.example.RF.FunctionExpression;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame
{

	JPanel deck;
	MethodSelectScreen methodScreen;
	SolutionScreen solutionScreen;
    StartScreen startScreen;
    FunctionEntryScreen funcEntryScreen;
    RFMethodSelectScreen rfMethodSelect;
    RFSolutionScreen rfSolutionScreen;


	public AppFrame(String title)
	{
		super(title);
		// setLayout(new Borde());
		deck = new JPanel();
		deck.setLayout(new CardLayout());
		add(deck);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startScreen = new StartScreen();
        deck.add(startScreen, "StartScreen");
		deck.add(new SystemEntryScreen(), "SystemEntryScreen");
		methodScreen = new MethodSelectScreen();
		deck.add(methodScreen, "MethodSelectScreen");
		solutionScreen = new SolutionScreen();
		deck.add(solutionScreen, "SolutionScreen");
        funcEntryScreen = new FunctionEntryScreen();
        deck.add(funcEntryScreen, "FunctionEntryScreen");
        rfMethodSelect = new RFMethodSelectScreen();
        deck.add(rfMethodSelect, "RFMethodSelect");
        rfSolutionScreen = new RFSolutionScreen();
        deck.add(rfSolutionScreen, "RFSolutionScreen");
	}


    public void onStartScreenLS()
    {
        ((CardLayout)deck.getLayout()).show(deck, "SystemEntryScreen");
    }

    public void onSystemEntryBack()
    {
        ((CardLayout)deck.getLayout()).show(deck, "StartScreen");
    }

    public void onSystemEntryNext(Equation[] system)
    {
        ((CardLayout)deck.getLayout()).next(deck);
        methodScreen.setSystem(system);
    }

    public void onMethodSelectBack()
	{
		((CardLayout)deck.getLayout()).previous(deck);
	}

	public void onMethodSelectGetSol(Equation[] system, Method method, boolean useScaling, Parameters params)
	{
		((CardLayout)deck.getLayout()).next(deck);
		solutionScreen.setSolvingMode(system, method, useScaling, params);
	}

	public void onSolutionScreenEnterAnotherSystem()
	{
        ((CardLayout)deck.getLayout()).show(deck, "SystemEntryScreen");
	}



    public void onStartScreenRF()
    {
        ((CardLayout)deck.getLayout()).show(deck, "FunctionEntryScreen");
    }

    public void onFuncEntryBack()
    {
        ((CardLayout)deck.getLayout()).show(deck, "StartScreen");
    }

    public void onFuncEntryNext()
    {
        ((CardLayout)deck.getLayout()).next(deck);
    }


    public void onRFMethodBack()
    {
        ((CardLayout)deck.getLayout()).previous(deck);
    }

    public void onRFMethodNext(RFMethod method, int pre, int iters, double err, Parameters params)
    {
        ((CardLayout)deck.getLayout()).next(deck);
        rfSolutionScreen.setSolvingMode(funcEntryScreen.func, pre, iters, err, method, params);
    }

    public void onRFSolutionBack()
    {
        ((CardLayout)deck.getLayout()).previous(deck);
    }
}
