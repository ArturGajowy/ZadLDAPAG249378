package pl.gajowy.phonebook;

import javax.naming.NamingException;

public class PhonebookException extends RuntimeException{
    public PhonebookException(String message, NamingException cause) {
        super(message, cause);
    }
}
