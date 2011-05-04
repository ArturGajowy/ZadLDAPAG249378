package pl.gajowy.phonebook.application.exception;

import javax.naming.NamingException;

public class ThingThatShouldNotBeException extends RuntimeException {
    public ThingThatShouldNotBeException(String message, Throwable e) {
        super(message, e);
    }
}
