package view;

import bll.SimulationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class SimulationFrame extends JFrame {
    private JLabel nrClientsLabel;
    private JLabel nrQueuesLabel;
    private JLabel simulationIntervalLabel;
    private JLabel minArrivalLabel;
    private JLabel maxArrivalLabel;
    private JLabel minServiceLabel;
    private JLabel maxServiceLabel;

    private JTextField nrClientsText;
    private JTextField nrQueuesText;
    private JTextField simulationIntervalText;
    private JTextField minArrivalText;
    private JTextField maxArrivalText;
    private JTextField minServiceText;
    private JTextField maxServiceText;

    private JTextArea queueTextArea;

    private JButton startButton;
    private JScrollPane scrollPane;
    public SimulationFrame() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.setSize(755, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        panel.setLayout(null);

        queueTextArea = new JTextArea();
        queueTextArea.setEditable(false);
        queueTextArea.setBounds(20, 250, 700, 300);
        panel.add(queueTextArea);
        scrollPane = new JScrollPane(queueTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(20, 200, 700, 300);
        panel.add(scrollPane);

        nrClientsLabel = new JLabel("Number of clients:");
        nrClientsLabel.setBounds(20, 20, 125, 25);
        panel.add(nrClientsLabel);

        nrClientsText = new JTextField();
        nrClientsText.setBounds(150, 24, 30, 18);
        panel.add(nrClientsText);

        nrQueuesLabel = new JLabel("Number of queues:");
        nrQueuesLabel.setBounds(20, 40, 125, 25);
        panel.add(nrQueuesLabel);

        nrQueuesText = new JTextField();
        nrQueuesText.setBounds(150, 44, 30, 18);
        panel.add(nrQueuesText);

        simulationIntervalLabel = new JLabel("Simulation interval:");
        simulationIntervalLabel.setBounds(20, 60, 125, 25);
        panel.add(simulationIntervalLabel);

        simulationIntervalText = new JTextField();
        simulationIntervalText.setBounds(150, 64, 30, 18);
        panel.add(simulationIntervalText);

        minArrivalLabel = new JLabel("Min arrival time:");
        minArrivalLabel.setBounds(20, 80, 125, 25);
        panel.add(minArrivalLabel);

        minArrivalText = new JTextField();
        minArrivalText.setBounds(150, 84, 30, 18);
        panel.add(minArrivalText);

        maxArrivalLabel = new JLabel("Max arrival time:");
        maxArrivalLabel.setBounds(20, 100, 125, 25);
        panel.add(maxArrivalLabel);

        maxArrivalText = new JTextField();
        maxArrivalText.setBounds(150, 104, 30, 18);
        panel.add(maxArrivalText);

        minServiceLabel = new JLabel("Min service time:");
        minServiceLabel.setBounds(20, 120, 125, 25);
        panel.add(minServiceLabel);

        minServiceText = new JTextField();
        minServiceText.setBounds(150, 124, 30, 18);
        panel.add(minServiceText);

        maxServiceLabel = new JLabel("Max service time:");
        maxServiceLabel.setBounds(20, 140, 125, 25);
        panel.add(maxServiceLabel);

        maxServiceText = new JTextField();
        maxServiceText.setBounds(150, 144, 30, 18);
        panel.add(maxServiceText);

        startButton = new JButton("Start");
        startButton.setBounds(200, 20, 80, 35);
        panel.add(startButton);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimulationManager simulationManager = null;
                try {
                    simulationManager = new SimulationManager(Integer.parseInt(simulationIntervalText.getText()),
                            Integer.parseInt(maxServiceText.getText()),
                            Integer.parseInt(minServiceText.getText()),
                            Integer.parseInt(nrQueuesText.getText()),
                            Integer.parseInt(nrClientsText.getText()),
                            Integer.parseInt(minArrivalText.getText()),
                            Integer.parseInt(maxArrivalText.getText())
                    );
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

                Thread thread = new Thread(simulationManager);
                thread.start();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    public void updateTextField(String clientsLog){
        queueTextArea.append(clientsLog);
    }
}
