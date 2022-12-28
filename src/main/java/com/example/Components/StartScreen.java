package com.example.Components;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;




public class StartScreen extends JPanel implements ActionListener
{
    JButton toLS;
    JButton toRF;

    StartScreen()
    {
        setLayout(new GridLayout(4, 3, 5, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        toLS = new JButton("Solve a linear system of equations");
        toRF = new JButton("Find a root of a function");
        add(new JPanel());
        add(new JPanel());
        add(new JPanel());
        add(new JPanel());
        add(toLS);
        add(new JPanel());
        add(new JPanel());
        add(toRF);
        add(new JPanel());
        add(new JPanel());
        add(new JPanel());
        add(new JPanel());
        toLS.setActionCommand("To LS");
        toRF.setActionCommand("To RF");
        toLS.addActionListener(this);
        toRF.addActionListener(this);

    }

    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getActionCommand().equals("To LS"))
        {
            ((AppFrame)getTopLevelAncestor()).onStartScreenLS();
        }
        else
        {
            ((AppFrame)getTopLevelAncestor()).onStartScreenRF();
        }
    }
}



