package pl.gajowy.phonebook;

import com.google.common.annotations.VisibleForTesting;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhonebookConnection {
    private static final String PHONEBOOK_SEARCH_TEMPLATE = "(&(objectClass=mimuwOrgPerson)(|(cn=%s*)(|(givenName=%s*))(sn=%s*)))";

    @VisibleForTesting InitialDirContext ldapContext;
    private String loggedInUsername;

    public PhonebookConnection(InitialDirContext ldapContext, String username) {
        this.ldapContext = ldapContext;
        this.loggedInUsername = username;
    }

    public List findPhonebookEntries(String userName, String firstName, String lastName) {
        String[] attrIDs = {"cn", "givenName", "sn", "telephoneNumber", "nameDay"};
        SearchControls ctls = new SearchControls();
        ctls.setReturningAttributes(attrIDs);
        ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String filter = String.format(PHONEBOOK_SEARCH_TEMPLATE, userName, firstName, lastName);

        // Search for objects using filter
        NamingEnumeration answer = null;
        try {
            answer = ldapContext.search("", filter, ctls);
        } catch (NamingException e) {
            throw new RuntimeException(e); //FIXME !!!!!
        }

        ArrayList result = newArrayList();
        try {
            while (answer.hasMore()) {
                result.add(answer.next());
            }
        } catch (NamingException e) {
            throw new RuntimeException(e); //FIXME !!!!!
        }
        return result;
    }

    public void createPhonebookEntry(MimuwOrgPerson person) {
        try {
            ldapContext.bind(getBindNameForNewEntry(person), person);
        } catch (NamingException e) {
            throw new RuntimeException(e); //FIXME
        }
    }

    @VisibleForTesting String getBindNameForNewEntry(MimuwOrgPerson person) {
        return "cn=" + person.getUsername() + ",ou=addressbook,cn=" + loggedInUsername + ",ou=users";
    }

}
