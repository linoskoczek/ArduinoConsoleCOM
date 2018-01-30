package View;

import Model.Model;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class View extends JFrame {
    private Model model;
    private JTextField input;
    private JTextArea output;
    private JButton send;

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
    }

    private void createInputSection() {
        JPanel inputSection = new JPanel();
        inputSection.setLayout(new BorderLayout());
        input = new JTextField();
        input.setMargin(new Insets(5,5,5,5));
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
        output.setMargin(new Insets(5,5,5,5));
        JScrollPane scroll = new JScrollPane(output,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        outputSection.add(scroll, BorderLayout.CENTER);
        outputSection.setVisible(true);
        this.add(outputSection, BorderLayout.CENTER);
    }

    public void addSendListener(ActionListener sendMessage) {
        send.addActionListener(sendMessage);
    }

    public void addSendViaEnterListener(ActionListener sendMessage) {
        input.addActionListener(sendMessage);
    }

    public String getAndClearUserInput() {
        String text = input.getText();
        input.setText("");
        input.requestFocus();
        return text;
    }

    public void gotMessageEvent(String message) {
        output.append(message + "\n");
    }
}
