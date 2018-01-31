package View;

import Model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class View extends JFrame {
    private Model model;
    private JTextField input;
    private JTextArea output;
    private JButton send;
    private ActionListener sendAction = null, sendViaEnterAction = null;
    private Thread modelListener = null;

    public View(Model model) throws HeadlessException {
        super("Arduino Console");
        this.model = model;
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(600, 400));

        createInputSection();
        createOutputSection();

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                model.closeProgram();
            }
        });

        createModelListener();
    }

    private void createInputSection() {
        JPanel inputSection = new JPanel();
        inputSection.setLayout(new BorderLayout());
        input = new JTextField();
        input.setMargin(new Insets(5, 5, 5, 5));
        send = new JButton("Send");

        inputSection.add(input, BorderLayout.CENTER);
        inputSection.add(send, BorderLayout.EAST);
        inputSection.setVisible(true);
        this.add(inputSection, BorderLayout.NORTH);
        input.requestFocus();
    }

    private void createOutputSection() {
        JPanel outputSection = new JPanel();
        outputSection.setLayout(new BorderLayout());
        output = new JTextArea();
        output.setLineWrap(true);
        output.setEditable(false);
        output.setMargin(new Insets(5, 5, 5, 5));
        JScrollPane scroll = new JScrollPane(output,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        outputSection.add(scroll, BorderLayout.CENTER);
        outputSection.setVisible(true);
        this.add(outputSection, BorderLayout.CENTER);
    }

    public void addSendListener(ActionListener sendMessage) {
        sendAction = sendMessage;
        send.addActionListener(sendMessage);
    }

    public void addSendViaEnterListener(ActionListener sendMessage) {
        sendViaEnterAction = sendMessage;
        input.addActionListener(sendMessage);
    }

    public void removeActionListeners() {
        send.removeActionListener(sendAction);
        send.removeActionListener(sendViaEnterAction);
    }

    private void createModelListener() {
        modelListener = new Thread(this::startModelListener);
        modelListener.start();
    }

    public void restartModelListener() {
        modelListener.interrupt();
        while (modelListener.isAlive()) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        createModelListener();
    }

    public String getAndClearUserInput() {
        String text = input.getText();
        input.setText("");
        input.requestFocus();
        return text;
    }

    private void startModelListener() {
        String message;
        while (model.isRunning && !modelListener.isInterrupted()) {
            message = model.getMessage();
            if (message == null) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
            } else {
                gotMessageEvent(message);
            }
        }
    }

    private void gotMessageEvent(String message) {
        output.append(message + "\n");
    }

    public void updateModel(Model model) {
        this.model = model;
    }
}
