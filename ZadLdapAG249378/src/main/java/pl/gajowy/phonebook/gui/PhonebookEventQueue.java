package pl.gajowy.phonebook.gui;

import org.apache.commons.logging.LogFactory;
import pl.gajowy.phonebook.application.exception.ThingThatShouldNotBeException;

import javax.swing.*;
import java.awt.*;

import static com.google.common.base.Strings.isNullOrEmpty;

public class PhonebookEventQueue extends EventQueue {

    @Override
    protected void dispatchEvent(AWTEvent event) {
        try {
            super.dispatchEvent(event);
        } catch (ThingThatShouldNotBeException e) {
            LogFactory.getLog(PhonebookEventQueue.class).error("Unexpected exception", e);
            String message = e.getMessage();
            if (!isNullOrEmpty(message)) {
                JOptionPane.showMessageDialog(null, message, "Unexpected error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

