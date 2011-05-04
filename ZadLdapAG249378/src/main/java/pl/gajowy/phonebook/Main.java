package pl.gajowy.phonebook;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.GraphiteGlassSkin;
import pl.gajowy.phonebook.gui.MainForm;
import pl.gajowy.phonebook.gui.PhonebookEventQueue;

import javax.swing.*;
import java.awt.*;

public class Main {

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

    private static void displayMainForm(final String ldapServerAddress, final int ldapServerPort) {
        registerGlobalErrorHandler();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SubstanceLookAndFeel.setSkin(new GraphiteGlassSkin());
                MainForm mainForm = new MainForm(ldapServerAddress, ldapServerPort);
                JFrame frame = new JFrame("MainForm");
                frame.setContentPane(mainForm.getContentPane());
                frame.addWindowListener(mainForm.getCloseListener());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    private static void registerGlobalErrorHandler() {
        EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        queue.push(new PhonebookEventQueue());
    }
}
