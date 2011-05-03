package pl.gajowy.phonebook.gui;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.*;
import pl.gajowy.phonebook.PhonebookConnection;
import pl.gajowy.phonebook.PhonebookException;
import pl.gajowy.phonebook.PhonebookFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;

public class MainForm {
    private static final String SEARCH_CARD = "SearchCard";
    private static final String CREATE_ENTRY_CARD = "CreateEntryCard";

    private JPanel contentPane;
    private JButton loginButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField textField2;
    private JButton searchButton;
    private JTable table1;
    private JButton switchToCreationButton;
    private JButton searchPhonebookButton;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton saveNewEntryButton;
    private JLabel loginValidatorLabel;
    private String ldapServerAddress;
    private int ldapServerPort;
    private PhonebookConnection phonebookConnection;

    public MainForm(String ldapServerAddress, int ldapServerPort) {
        this.ldapServerAddress = ldapServerAddress;
        this.ldapServerPort = ldapServerPort;

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logIn();
            }
        });
        switchToCreationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchDisplayedCardTo(CREATE_ENTRY_CARD);
            }
        });
        searchPhonebookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchDisplayedCardTo(SEARCH_CARD);
            }
        });
    }

    private void logIn() {
        loginValidatorLabel.setText("Logging in...");
        final String username = usernameField.getText();
        final String password = stringOfChars(passwordField.getPassword());
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(300);
                return null;
            }

            @Override
            protected void done() {
                tryLogin(username, password);
            }
        }.execute();

    }

    private void tryLogin(String username, String password) {
        try {
            phonebookConnection = new PhonebookFacade().logIn(ldapServerAddress, ldapServerPort, username, password);
            switchDisplayedCardTo(SEARCH_CARD);
        } catch (PhonebookException e) {
            loginValidatorLabel.setText(e.getMessage());
        }
    }

    private String stringOfChars(char[] passwordChars) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char passwordChar : passwordChars) {
            stringBuilder.append(passwordChar);
        }
        return stringBuilder.toString();
    }

    private void switchDisplayedCardTo(String cardName) {
        ((CardLayout) contentPane.getLayout()).show(contentPane, cardName);
    }

    public static void main(String[] args) {
        try {
            String ldapServerAddress = args[0];
            int ldapServerPort = Integer.parseInt(args[1]);
            displayMainForm(ldapServerAddress, ldapServerPort);
        } catch (ArrayIndexOutOfBoundsException e) {
            printUsage();
        } catch (NumberFormatException e) {
            printUsage();
        }
    }

    private static void printUsage() {
        System.err.println("Usage: java -jar $PHONEBOOK_JAR ldapServerAddress ldapServerPort");
    }

    private static void displayMainForm(String ldapServerAddress, int ldapServerPort) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SubstanceLookAndFeel.setSkin(new GraphiteGlassSkin());
            }
        });
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm(ldapServerAddress, ldapServerPort).contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
