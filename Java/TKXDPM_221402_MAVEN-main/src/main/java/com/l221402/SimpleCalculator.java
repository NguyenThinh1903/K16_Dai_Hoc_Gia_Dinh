package com.l221402;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleCalculator extends JFrame {
    private JTextField textField1, textField2;
    private JButton button;
    private JLabel resultLabel;

    public SimpleCalculator() {
        super("Simple Calculator");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Tạo các thành phần giao diện
        JLabel label1 = new JLabel("Number 1");
        label1.setBounds(20, 20, 80, 25);
        add(label1);

        textField1 = new JTextField(20);
        textField1.setBounds(100, 20, 165, 25);
        add(textField1);

        JLabel label2 = new JLabel("Number 2");
        label2.setBounds(20, 50, 80, 25);
        add(label2);

        textField2 = new JTextField(20);
        textField2.setBounds(100, 50, 165, 25);
        add(textField2);

        button = new JButton("Add");
        button.setBounds(100, 80, 80, 25);
        add(button);

        resultLabel = new JLabel();
        resultLabel.setBounds(20, 110, 200, 25);
        add(resultLabel);

        // Sự kiện khi nhấn nút Add
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int num1 = Integer.parseInt(textField1.getText());
                    int num2 = Integer.parseInt(textField2.getText());
                    int sum = num1 + num2;
                    resultLabel.setText("Result: " + sum);
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Please enter numbers only");
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new SimpleCalculator();
    }
}