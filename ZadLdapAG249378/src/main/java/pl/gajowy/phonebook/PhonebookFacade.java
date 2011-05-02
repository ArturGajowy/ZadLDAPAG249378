package pl.gajowy.phonebook;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.sun.jndi.ldap.LdapCtxFactory;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.spi.DirStateFactory;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Hashtable;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;

public class PhonebookFacade {

    private static final String PHONEBOOK_BASE_DN = "ou=mimuw,o=uw,l=waw,c=pl";
    private static final String AUTHENTICATION_TYPE = "simple";
    private static final String USERS_CATALOG_BASE_DN = "ou=users,ou=mimuw,o=uw,l=waw,c=pl";

    public PhonebookConnection logIn(String ldapServerAddress, int ldapServerPort, String username, String password) {
        verifyParameters(ldapServerAddress, username, password);
        Hashtable<String, Object> connectionParameters = createConnectionParameters(ldapServerAddress, ldapServerPort, username, password);
        return tryLogin(connectionParameters, username);
    }

    private Hashtable<String, Object> createConnectionParameters(String ldapServerAddress, int ldapServerPort, String username, String password) {
        Hashtable<String, Object> connectionParameters = newHashTable();
        connectionParameters.put(Context.INITIAL_CONTEXT_FACTORY, LdapCtxFactory.class.getCanonicalName());
        connectionParameters.put(Context.STATE_FACTORIES, toClassNameList(PersonStateFactory.class));
        connectionParameters.put(Context.PROVIDER_URL, "ldap://" + ldapServerAddress + ":" + ldapServerPort + "/" + PHONEBOOK_BASE_DN);
        connectionParameters.put(Context.SECURITY_AUTHENTICATION, AUTHENTICATION_TYPE);
        connectionParameters.put(Context.SECURITY_PRINCIPAL, "cn=" + username + "," + USERS_CATALOG_BASE_DN);
        connectionParameters.put(Context.SECURITY_CREDENTIALS, password);
        return connectionParameters;
    }

    private String toClassNameList(Class<? extends DirStateFactory>... stateFactoryClasses) {
        ArrayList<String> factoryClassNames = newArrayList();
        for (Class<? extends DirStateFactory> stateFactoryClass : stateFactoryClasses) {
            factoryClassNames.add(stateFactoryClass.getCanonicalName());
        }
        return Joiner.on(',').join(factoryClassNames);
    }

    private PhonebookConnection tryLogin(Hashtable<String, Object> connectionParameters, String username) {
        try {
            return new PhonebookConnection(new InitialDirContext(connectionParameters), username);
        } catch (NamingException e) {
            if (e.getCause() instanceof ConnectException || e instanceof CommunicationException) {
                throw new InvalidConnectionParamtersException("Wrong LDAP server hostname or port.", e);
            }
            throw new InvalidCredentialsException("Wrong username or password.", e);
        }
    }

    private void verifyParameters(String ldapServerAddress, String username, String password) {
        Preconditions.checkArgument(!isNullOrEmpty(ldapServerAddress));
        Preconditions.checkArgument(!isNullOrEmpty(username));
        Preconditions.checkArgument(!isNullOrEmpty(password));
    }


    //we miss you so hard, dear <> operator...
    private static <K, V> Hashtable<K, V> newHashTable() {
        return new Hashtable<K, V>();
    }
}