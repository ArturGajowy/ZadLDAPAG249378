package pl.gajowy.phonebook.application.exception;

import javax.naming.NamingException;

public class PhonebookException extends RuntimeException{
    public PhonebookException(String message, NamingException cause) {
        super(message, cause);
    }
}
