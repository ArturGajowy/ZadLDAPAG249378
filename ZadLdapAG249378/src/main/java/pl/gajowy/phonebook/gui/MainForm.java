package pl.gajowy.phonebook.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm {
    private static final String SEARCH_CARD = "SearchCard";
    private static final String CREATE_ENTRY_CARD = "CreateEntryCard";

    private JPanel contentPane;
    private JButton loginButton;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JTextField textField2;
    private JButton searchButton;
    private JTable table1;
    private JButton switchToCreationButton;

    public MainForm() {
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchDisplayedCardTo(SEARCH_CARD);
            }
        });
        switchToCreationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchDisplayedCardTo(CREATE_ENTRY_CARD);
            }
        });
    }

    private void switchDisplayedCardTo(String cardName) {
        ((CardLayout) contentPane.getLayout()).show(contentPane, cardName);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
