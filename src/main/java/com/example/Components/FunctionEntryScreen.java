package com.example.Components;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.example.RF.FunctionExpression;

import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;


public class FunctionEntryScreen extends JPanel implements ActionListener
{
    JLabel errorLabel;
    JTextField funcField;
    JButton bBack, bNext;
    FunctionExpression func;

    FunctionEntryScreen()
    {
        setLayout(new GridBagLayout());
        funcField = new JTextField(16);
        JLabel l = new JLabel("f(x) = ");
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);

        bBack = new JButton("Back to Start Screen");
        bBack.setActionCommand("Back");
        bBack.addActionListener(this);
        bNext= new JButton("Next");
        bNext.addActionListener(this);

        GridBagConstraints cons = new GridBagConstraints();
        //Empty padding
        JPanel panel = new JPanel();
        cons.gridwidth = 4;
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;
        // panel.setBackground(Color.cyan);
        add(panel, cons);
        //func label
        cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 1;
        cons.weighty = 2;
        add(l, cons);
        //Field
        cons = new GridBagConstraints();
        cons.gridx = 1;
        cons.gridy = 1;
        cons.gridwidth = 2;
        add(funcField, cons);
        //Error Label
        cons = new GridBagConstraints();
        cons.gridx = 3;
        cons.gridy = 1;
        add(errorLabel, cons);
        //Back Button
        cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.NONE;
        cons.gridx = 1;
        cons.gridy = 2;
        cons.weighty = 3;
        add(bBack, cons);
        //Next Button
        cons = new GridBagConstraints();
        cons.gridx = 2;
        cons.gridy = 2;
        add(bNext, cons);
    }


    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getActionCommand().equals("Back"))
        {
            ((AppFrame)getTopLevelAncestor()).onFuncEntryBack();
            errorLabel.setText("");
        }
        else
        {
            String exp = funcField.getText();
            try
            {
                if (exp.isEmpty()) throw new UnknownFunctionOrVariableException("y", 0, 1);
                func = new FunctionExpression(exp);
                errorLabel.setText("");
                ((AppFrame)getTopLevelAncestor()).onFuncEntryNext();
            }
            catch (UnknownFunctionOrVariableException e)
            {
                errorLabel.setText("INVALID EXPRESSION");
            }
        }
    }
}
