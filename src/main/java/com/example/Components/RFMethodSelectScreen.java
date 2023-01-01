package com.example.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RFMethodSelectScreen extends JPanel implements ActionListener
{

    ParametersPanel paramsPanel;
    HeaderPanel headerPanel;
    JButton bBack, bNext;
    JLabel errorLabel;

    RFMethodSelectScreen()
    {
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setLayout(new BorderLayout());
        headerPanel = new HeaderPanel(this);
        headerPanel.setBackground(Color.orange);
        JScrollPane headScrollPane = new JScrollPane(headerPanel);
		headScrollPane.setPreferredSize(new Dimension(200, 70));
		add(headScrollPane, BorderLayout.NORTH);
        paramsPanel = new BracketingParamsPanel();
        add(paramsPanel);
        JPanel footerPanel = new JPanel();
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.red);
        bBack = new JButton("Back");
        bBack.addActionListener(this);
        bNext = new JButton("Find Root");
        bNext.setActionCommand("Next");
        bNext.addActionListener(this);
        footerPanel.add(bBack);
        footerPanel.add(bNext);
        footerPanel.add(errorLabel);
        add(footerPanel, BorderLayout.SOUTH);

    }

    public void createParamsPanel(RFMethod method)
    {
        remove(paramsPanel);
        switch(method)
        {
            case Bisection:
            case FalsePos:
            paramsPanel = new BracketingParamsPanel();
            break;
            case FixedPoint:
            paramsPanel = new FixedPointParamsPanel();
            break;
            case Newton:
            paramsPanel = new NewtonRaphsonParamsPanel();
            break;
            default:
            paramsPanel = new SecantParamsPanel();
            break;
        }
        add(paramsPanel);
        revalidate();
        repaint();
    }

    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getActionCommand().equals("Back"))
        {
            ((AppFrame)getTopLevelAncestor()).onRFMethodBack();
            errorLabel.setText("");
        }
        else
        {
            Parameters params = paramsPanel.getParams();
            if (params == null) errorLabel.setText("Please Enter valid parameters");
            else
            {
                int numOfIters = headerPanel.getNumOfIters();
                int pre = headerPanel.getPrecision();
                double error = headerPanel.getRelativeError();
                RFMethod method = headerPanel.getMethod();
                ((AppFrame)getTopLevelAncestor()).onRFMethodNext(method, pre, numOfIters, error, params);
            }
        }
    }


    class HeaderPanel extends JPanel implements ActionListener
    {
        JComboBox<String> methodBox;
        IntTextField preField, numOfIters;
        DoubleTextField relativeErrorField;
        final int defaultIters = 50;
        final double defaultError = 0.00001;
        RFMethodSelectScreen parent;


        HeaderPanel(RFMethodSelectScreen parent)
        {
            this.parent = parent;
            setLayout(new FlowLayout(FlowLayout.LEADING, 10, 5));
            add(new JLabel("Solving Method:"));
            String[] methodBoxChoices = new String[5];
            methodBoxChoices[0] = "Bisection";
            methodBoxChoices[1] = "False Position";
            methodBoxChoices[2] = "Fixed Point";
            methodBoxChoices[3] = "Newton-Raphson";
            methodBoxChoices[4] = "Secant";
            methodBox = new JComboBox<String>(methodBoxChoices);
            methodBox.addActionListener(this);
            add(methodBox);
            preField = new IntTextField(3);
            add(new JLabel("Precision = "));
            add(preField);
            numOfIters = new IntTextField(4);
            add(new JLabel("No. of Iterations = (Default = " + defaultIters + ") "));
            add(numOfIters);
            relativeErrorField = new DoubleTextField(8);
            add(new JLabel("Relative Error = (Default = " + defaultError + ") "));
            add(relativeErrorField);
        }

        public void actionPerformed(ActionEvent ae)
        {
            if (ae.getActionCommand().equals("comboBoxChanged"))
            {
                RFMethod method;
                remove(paramsPanel);
                switch ((String)methodBox.getSelectedItem())
                {
                    case "Bisection":
                    method = RFMethod.Bisection;
                    break;
                    case "False Position":
                    method = RFMethod.FalsePos;
                    break;
                    case "Fixed Point":
                    method = RFMethod.FixedPoint;
                    break;
                    case "Newton-Raphson":
                    method = RFMethod.Newton;
                    break;
                    default:
                    method = RFMethod.Secant;
                }
                parent.createParamsPanel(method);
            }
        }


        public int getPrecision()
        {
            if (preField.getText().isEmpty()) return -1;
            else return Integer.parseInt(preField.getText());
        }
        public int getNumOfIters()
        {
            if (numOfIters.getText().isEmpty()) return defaultIters;
            else return Integer.parseInt(numOfIters.getText());
        }
        public double getRelativeError()
        {
            if (relativeErrorField.getText().isEmpty()) return defaultError;
            else return Double.parseDouble(relativeErrorField.getText());
        }
        public RFMethod getMethod()
        {
            switch ((String)methodBox.getSelectedItem())
            {
                case "Bisection":
                return RFMethod.Bisection;
                case "False Position":
                return RFMethod.FalsePos;
                case "Fixed Point":
                return RFMethod.FixedPoint;
                case "Newton-Raphson":
                return RFMethod.Newton;
                default:
                return RFMethod.Secant;
            }
        }

    }

    public enum RFMethod {Bisection, FalsePos, FixedPoint, Newton, Secant}

}
