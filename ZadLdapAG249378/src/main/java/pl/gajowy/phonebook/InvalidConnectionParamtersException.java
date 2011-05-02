package pl.gajowy.phonebook;

import javax.naming.NamingException;

public class InvalidConnectionParamtersException extends RuntimeException{
    public InvalidConnectionParamtersException(String message, NamingException cause) {
        super(message, cause);
    }
}
