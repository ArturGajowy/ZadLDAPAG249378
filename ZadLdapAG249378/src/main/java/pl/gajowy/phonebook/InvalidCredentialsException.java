package pl.gajowy.phonebook;

import javax.naming.NamingException;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message, NamingException cause) {
        super(message, cause);
    }
}
