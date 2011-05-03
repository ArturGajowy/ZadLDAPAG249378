package pl.gajowy.phonebook;

import javax.naming.NamingException;

public class InvalidConnectionParamtersException extends PhonebookException {
    public InvalidConnectionParamtersException(String message, NamingException cause) {
        super(message, cause);
    }
}
