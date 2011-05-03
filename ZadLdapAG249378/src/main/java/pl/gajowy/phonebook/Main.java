package pl.gajowy.phonebook;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.GraphiteGlassSkin;
import pl.gajowy.phonebook.gui.MainForm;

import javax.swing.*;

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

    private static void displayMainForm(String ldapServerAddress, int ldapServerPort) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SubstanceLookAndFeel.setSkin(new GraphiteGlassSkin());
            }
        });
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm(ldapServerAddress, ldapServerPort).getContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
