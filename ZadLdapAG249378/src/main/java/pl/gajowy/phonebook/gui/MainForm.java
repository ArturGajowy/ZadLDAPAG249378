package pl.gajowy.phonebook.gui;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.*;
import pl.gajowy.phonebook.MimuwOrgPerson;
import pl.gajowy.phonebook.application.PhonebookConnection;
import pl.gajowy.phonebook.application.exception.PhonebookException;
import pl.gajowy.phonebook.application.PhonebookFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainForm {
    private static final String SEARCH_CARD = "SearchCard";
    private static final String CREATE_ENTRY_CARD = "CreateEntryCard";

    private JPanel contentPane;
    private JButton loginButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField searchField;
    private JButton searchButton;
    private JTable searchResultsTable;
    private JButton switchToCreationButton;
    private JButton searchPhonebookButton;
    private JTextField newUsernameField;
    private JTextField newFirstNameField;
    private JTextField newLastNameField;
    private JTextField newNameDayField;
    private JTextField newTelephoneNumberField;
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
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        saveNewEntryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewEntry();
            }
        });
        switchToCreationButton.addActionListener(new SwitchCardActionListener(CREATE_ENTRY_CARD));
        searchPhonebookButton.addActionListener(new SwitchCardActionListener(SEARCH_CARD));
    }

    private void createUIComponents() {
        searchResultsTable = new JTable();
        ReadOnlyDefaultTableModel searchResultTableModel = new ReadOnlyDefaultTableModel(
                new String[]{
                        "Surname", "Name", "Login", "Name day", "Phone number"
                },
                new Class[]{
                        String.class, String.class, String.class, String.class, String.class, String.class
                });
        searchResultsTable.setModel(searchResultTableModel);
        searchResultsTable.setEnabled(true);
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

    private void search() {
        String searchString = searchField.getText();
        List<MimuwOrgPerson> phonebookEntries = phonebookConnection.findPhonebookEntries(searchString, searchString, searchString);
        displayInSearchResultsTable(phonebookEntries);
    }

    private void displayInSearchResultsTable(List<MimuwOrgPerson> phonebookEntries) {
        clearTable(searchResultsTable);
        DefaultTableModel tableModel = (DefaultTableModel) searchResultsTable.getModel();
        for (MimuwOrgPerson phonebookEntry : phonebookEntries) {
            addRow(tableModel, phonebookEntry);
        }
    }

    private void addRow(DefaultTableModel tableModel, MimuwOrgPerson phonebookEntry) {
        int newRowNumber = tableModel.getRowCount();
        tableModel.setRowCount(newRowNumber + 1);
        setValuesAtTableRow(tableModel, newRowNumber,
                phonebookEntry.getLastName(),
                phonebookEntry.getFirstName(),
                phonebookEntry.getUsername(),
                phonebookEntry.getNameDay(),
                phonebookEntry.getTelephoneNumber()
        );
    }

    private void setValuesAtTableRow(DefaultTableModel tableModel, int rowNumber, Object... subsequentValuesInRow) {
        int column = 0;
        for (Object value : subsequentValuesInRow) {
            tableModel.setValueAt(value, rowNumber, column++);
        }
    }

    private void clearTable(JTable tableToClear) {
        ((DefaultTableModel) tableToClear.getModel()).setRowCount(0);
    }

    private void createNewEntry() {

    }

    public JPanel getContentPane() {
        return contentPane;
    }

    private class SwitchCardActionListener implements ActionListener {
        private String targetCardName;

        private SwitchCardActionListener(String targetCardName) {
            this.targetCardName = targetCardName;
        }

        public void actionPerformed(ActionEvent e) {
            switchDisplayedCardTo(targetCardName);
        }
    }

    private void switchDisplayedCardTo(String cardName) {
        ((CardLayout) contentPane.getLayout()).show(contentPane, cardName);
    }
}
