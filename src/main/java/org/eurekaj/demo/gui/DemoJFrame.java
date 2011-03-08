package org.eurekaj.demo.gui;

import org.eurekaj.demo.app.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: jhs
 * Date: 3/8/11
 * Time: 6:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class DemoJFrame extends JFrame implements ActionListener {
    private JPanel mainPanel = new JPanel(new GridLayout(10, 1));
    private Container content = this.getContentPane();
    private Main main;
    private Timer timer;

    private JLabel numRowsLabel;

    private InputValueVerifier inputValueVerifier;
    private JFormattedTextField insertThreadTextField, numberOfInsertsPerRunTextField, blockingDelayTextField, nonBlockingDelayTextField;
    private JButton performChanges;
    private DecimalFormat decimalFormat;

    public DemoJFrame(String title, Main main) throws HeadlessException {
        super();
        this.main = main;
        inputValueVerifier = new InputValueVerifier();
        decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(0);
        decimalFormat.setGroupingSize(3);

        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        content.setLayout(new FlowLayout());
        Dimension windowSize = new Dimension();
        Toolkit theKit = this.getToolkit(); // Get the window toolkit
        windowSize = theKit.getScreenSize(); // Get the screen size
        this.setBounds(0, 0, // Position
                300, 500); // size

        setUpGui();

        this.setVisible(true);

        timer = new Timer(2500, this);
        timer.setInitialDelay(6000);
        timer.setRepeats(true);
        timer.start();
    }

    public void actionPerformed(ActionEvent event) {
        //Start loading the images in the background.
        updateGUI();
    }

    private void setUpGui() {

        JLabel insertThreadLabel = new JLabel("Number of Insert Threads:");
        mainPanel.add(insertThreadLabel);

        insertThreadTextField = new JFormattedTextField(new Integer(main.getInsertTaskSize()));
        insertThreadTextField.setInputVerifier(inputValueVerifier);
        mainPanel.add(insertThreadTextField);

        JLabel numInsertsPerRunLabel = new JLabel("Number of inserts per Run:");
        mainPanel.add(numInsertsPerRunLabel);

        numberOfInsertsPerRunTextField = new JFormattedTextField(new Integer(main.getDerbyEnvironment().getNumInsertsPerRun()));
        numberOfInsertsPerRunTextField.setInputVerifier(inputValueVerifier);
        mainPanel.add(numberOfInsertsPerRunTextField);

        JLabel blockingDelayLabel = new JLabel("Milliseconds of blocking delay:");
        mainPanel.add(blockingDelayLabel);

        blockingDelayTextField = new JFormattedTextField(new Integer(main.getDerbyEnvironment().getBlockingDelay()));
        blockingDelayTextField.setInputVerifier(inputValueVerifier);
        mainPanel.add(blockingDelayTextField);

        JLabel nonBlockingDelayLabel = new JLabel("Milliseconds of non-blocking delay:");
        mainPanel.add(nonBlockingDelayLabel);

        nonBlockingDelayTextField = new JFormattedTextField(new Integer(main.getDerbyEnvironment().getNonBlockingDelay()));
        nonBlockingDelayTextField.setInputVerifier(inputValueVerifier);
        mainPanel.add(nonBlockingDelayTextField);

        performChanges = new JButton("Perform Changes");
        mainPanel.add(performChanges);

        numRowsLabel = new JLabel("Number of rows in StatisticsTable: Loading...");
        mainPanel.add(numRowsLabel);

        content.add(mainPanel);
    }

    public void updateGUI() {
        numRowsLabel.setText("Number of rows in StatisticsTable: " + decimalFormat.format(main.getDerbyEnvironment().getNumberOfRecordsInStatisticsDatabase()));
        numRowsLabel.repaint();
        mainPanel.repaint();
        this.repaint();

    }

    class InputValueVerifier extends InputVerifier implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("actionPerformed: " + actionEvent.getSource());
        }

        @Override
        public boolean verify(JComponent jComponent) {
            System.out.println("verify: " + jComponent);
            boolean verified = false;

            if (jComponent == insertThreadTextField) {
                Integer validatedInteger = validateInteger(insertThreadTextField.getText());
                if (validatedInteger != null) {
                    main.setInsertTaskSize(validatedInteger);
                    verified = true;
                    insertThreadTextField.setValue(main.getInsertTaskSize());
                }
            } else if (jComponent == blockingDelayTextField) {
                Integer validatedInteger = validateInteger(blockingDelayTextField.getText());
                if (validatedInteger != null) {
                    main.getDerbyEnvironment().setBlockingDelay(validatedInteger);
                    verified = true;
                    blockingDelayTextField.setValue(main.getDerbyEnvironment().getBlockingDelay());
                }
            } else if (jComponent == nonBlockingDelayTextField) {
                Integer validatedInteger = validateInteger(nonBlockingDelayTextField.getText());
                if (validatedInteger != null) {
                    main.getDerbyEnvironment().setNonBlockingDelay(validatedInteger);
                    verified = true;
                    nonBlockingDelayTextField.setValue(main.getDerbyEnvironment().getNonBlockingDelay());
                }
            } else if (jComponent == numberOfInsertsPerRunTextField) {
                Integer validatedInteger = validateInteger(numberOfInsertsPerRunTextField.getText());
                if (validatedInteger != null) {
                    main.getDerbyEnvironment().setNumInsertsPerRun(validatedInteger);
                    verified = true;
                    numberOfInsertsPerRunTextField.setValue(main.getDerbyEnvironment().getNumInsertsPerRun());
                }
            }
            return verified;
        }

        private Integer validateInteger(Object input) {
            Integer verifiedInteger = null;

            try {
                verifiedInteger = Integer.parseInt(input.toString());
            } catch (NumberFormatException nfe) {
                verifiedInteger = null;
            }
            return  verifiedInteger;
        }
    }

}
