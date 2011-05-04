package pl.gajowy.phonebook.gui;

import pl.gajowy.phonebook.domain.MimuwOrgPerson;
import pl.gajowy.phonebook.domain.Validator;
import pl.gajowy.phonebook.application.PhonebookConnection;
import pl.gajowy.phonebook.application.exception.PhonebookException;
import pl.gajowy.phonebook.application.PhonebookFacade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.validation.ConstraintViolation;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

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
    private JTextField newPhoneNumberField;
    private JButton saveNewEntryButton;
    private JLabel loginValidatorLabel;
    private JLabel createEntryValidatorMessages;
    private JLabel newUsernameLabel;
    private JLabel newFirstNameLabel;
    private JLabel newLastNameLabel;
    private JLabel newNameDayLabel;
    private JLabel newPhoneNumberLabel;
    private String ldapServerAddress;
    private int ldapServerPort;
    private PhonebookConnection phonebookConnection;

    private final ActionListener loginPerformingListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            logIn();
        }
    };
    private ActionListener searchPerformingListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            search();
        }
    };
    private final ActionListener entryCreatingListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            createNewEntry();
        }
    };

    public MainForm(String ldapServerAddress, int ldapServerPort) {
        this.ldapServerAddress = ldapServerAddress;
        this.ldapServerPort = ldapServerPort;

        loginButton.addActionListener(loginPerformingListener);
        searchButton.addActionListener(searchPerformingListener);
        saveNewEntryButton.addActionListener(entryCreatingListener);
        switchToCreationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchToCreateEntryCard();
            }
        });
        searchPhonebookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchToSearchCard();
            }
        });
        addConvenienceKeyboardActionListeners();
        setCreateNewEntryFormLabels();
    }

    private void switchToCreateEntryCard() {
        switchDisplayedCardTo(CREATE_ENTRY_CARD);
        newUsernameField.requestFocus();
    }

    private void switchToSearchCard() {
        switchDisplayedCardTo(SEARCH_CARD);
        searchField.requestFocus();
    }

    private void setCreateNewEntryFormLabels() {
        newFirstNameLabel.setText(MimuwOrgPerson.FIRST_NAME);
        newLastNameLabel.setText(MimuwOrgPerson.LAST_NAME);
        newUsernameLabel.setText(MimuwOrgPerson.USER_NAME);
        newNameDayLabel.setText(MimuwOrgPerson.NAMEDAY);
        newPhoneNumberLabel.setText(MimuwOrgPerson.PHONE_NUMBER);
    }

    private void addConvenienceKeyboardActionListeners() {
        usernameField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                passwordField.requestFocus();
            }
        });
        passwordField.addActionListener(loginPerformingListener);
        searchField.addActionListener(searchPerformingListener);
        addListenerToFields(
                entryCreatingListener,
                newUsernameField, newFirstNameField, newLastNameField, newNameDayField, newPhoneNumberField
        );
    }

    private void addListenerToFields(ActionListener listener, JTextField... fields) {
        for (JTextField field : fields) {
            field.addActionListener(listener);
        }
    }

    private void createUIComponents() {
        setUpSearchResultsTable();
    }

    private void setUpSearchResultsTable() {
        searchResultsTable = new JTable();
        String[] columnNames = {
                MimuwOrgPerson.LAST_NAME, MimuwOrgPerson.FIRST_NAME, MimuwOrgPerson.USER_NAME,
                MimuwOrgPerson.NAMEDAY, MimuwOrgPerson.PHONE_NUMBER
        };
        Class[] columnTypes = {
                String.class, String.class, String.class, String.class, String.class, String.class
        };
        searchResultsTable.setModel(new ReadOnlyDefaultTableModel(columnNames, columnTypes));
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
            switchToSearchCard();
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
        clearTable(searchResultsTable);
        List<MimuwOrgPerson> phonebookEntries = phonebookConnection.findPhonebookEntries(searchString, searchString, searchString);
        displayInSearchResultsTable(phonebookEntries);
    }

    private void clearTable(JTable tableToClear) {
        ((DefaultTableModel) tableToClear.getModel()).setRowCount(0);
    }

    private void displayInSearchResultsTable(List<MimuwOrgPerson> phonebookEntries) {
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

    private void createNewEntry() {
        clearTextOn(createEntryValidatorMessages);
        MimuwOrgPerson person = new MimuwOrgPerson(
                emptyToNull(newFirstNameField.getText()),
                emptyToNull(newLastNameField.getText()),
                emptyToNull(newUsernameField.getText()),
                emptyToNull(newNameDayField.getText()),
                emptyToNull(newPhoneNumberField.getText())
        );
        createEntryOrReportErrors(person);
    }

    private void createEntryOrReportErrors(MimuwOrgPerson person) {
        Set<ConstraintViolation<MimuwOrgPerson>> violations = Validator.instance.get().validate(person);
        if (violations.isEmpty()) {
            phonebookConnection.createPhonebookEntry(person);
            createEntryValidatorMessages.setText("Entry created.");
        } else {
            createEntryValidatorMessages.setText(toSortedHtmlList(violations));
        }
    }

    private String toSortedHtmlList(Set<ConstraintViolation<MimuwOrgPerson>> violations) {
        return renderAsHtmlList(getSortedMessages(violations));
    }

    private List<String> getSortedMessages(Set<ConstraintViolation<MimuwOrgPerson>> violations) {
        List<String> sortedViolationMessages = newArrayList();
        for (ConstraintViolation<MimuwOrgPerson> violation : violations) {
            sortedViolationMessages.add(violation.getMessage());
        }
        Collections.sort(sortedViolationMessages);
        return sortedViolationMessages;
    }

    private String renderAsHtmlList(List<String> messages) {
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        for (String message : messages) {
            html.append(format("<li>%s</li>", message));
        }
        html.append("</html>");
        return html.toString();
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    public WindowListener getCloseListener() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (phonebookConnection != null) {
                    phonebookConnection.close();
                }
            }
        };
    }

    private void switchDisplayedCardTo(String cardName) {
        ((CardLayout) contentPane.getLayout()).show(contentPane, cardName);
    }

    private void clearTextOn(JLabel label) {
        label.setText("");
    }
}
