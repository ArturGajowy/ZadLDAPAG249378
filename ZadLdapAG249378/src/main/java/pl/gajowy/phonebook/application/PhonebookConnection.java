package pl.gajowy.phonebook.application;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import pl.gajowy.phonebook.application.exception.ThingThatShouldNotBeException;
import pl.gajowy.phonebook.domain.MimuwOrgPerson;
import pl.gajowy.phonebook.domain.Validator;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

public class PhonebookConnection {
    private static final String PHONEBOOK_SEARCH_TEMPLATE = "(&(objectClass=mimuwOrgPerson)(|(cn=%s*)(|(givenName=%s*))(sn=%s*)))";

    @VisibleForTesting InitialDirContext ldapContext;
    private String loggedInUsername;
    private PersonConverter personConverter;

    public PhonebookConnection(InitialDirContext ldapContext, String username) {
        this.ldapContext = ldapContext;
        this.loggedInUsername = username;
        this.personConverter = new PersonConverter();
    }

    public List<MimuwOrgPerson> findPhonebookEntries(String userName, String firstName, String lastName) {
        String[] attrIDs = {"cn", "givenName", "sn", "telephoneNumber", "nameDay"};
        SearchControls ctls = new SearchControls();
        ctls.setReturningAttributes(attrIDs);
        ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String filter = String.format(PHONEBOOK_SEARCH_TEMPLATE, userName, firstName, lastName);

        try {
            NamingEnumeration<SearchResult> foundEntries = ldapContext.search("", filter, ctls);
            return personConverter.convertToPersons(foundEntries);
        } catch (NamingException e) {
            throw new ThingThatShouldNotBeException("Could not perform phonebook search", e);
        }
    }

    public void createPhonebookEntry(MimuwOrgPerson person) {
        validateOrThrow(person);
        try {
            ldapContext.bind(getBindNameForNewEntry(person), person);
        } catch (NamingException e) {
            throw new ThingThatShouldNotBeException("Could not create phonebook entry", e);
        }
    }

    private void validateOrThrow(MimuwOrgPerson person) {
        Set<ConstraintViolation<MimuwOrgPerson>> violations = Validator.instance.get().validate(person);
        if (!violations.isEmpty()) {
            List<String> violationMessages = newArrayList();
            for (ConstraintViolation<MimuwOrgPerson> violation : violations) {
                violationMessages.add(violation.getMessage());
            }
            throw new IllegalArgumentException(Joiner.on("; ").join(violationMessages));
        }
    }

    @VisibleForTesting String getBindNameForNewEntry(MimuwOrgPerson person) {
        return "cn=" + person.getUsername() + ",ou=addressbook,cn=" + loggedInUsername + ",ou=users";
    }

    public void close() {
        try {
            ldapContext.close();
        } catch (NamingException e) {
            throw new ThingThatShouldNotBeException("Error closing phonebook connection", e);
        }
    }
}
