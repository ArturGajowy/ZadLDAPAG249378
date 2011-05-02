package pl.gajowy.phonebook;

import org.junit.Before;
import org.junit.Test;

import javax.naming.NamingException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static pl.gajowy.phonebook.Fixture.*;

public class PhonebookConnectionTest {

    private PhonebookConnection connection;

    @Before
    public void setUp() {
        connection = new PhonebookFacade().logIn(ldapServerAddress, ldapServerPort, legitUsername, legitPassword);
    }

    @Test
    public void shouldGetUserData() {
        //when
        //FIXME generic type
        List<?> phonebookEntries = connection.findPhonebookEntries("", "", "");

        //then
        assertThat(phonebookEntries.size(), is(Fixture.phoneBookEntriesVisibleForLegitUser));

        //FIXME closing the connection
    }

    @Test
    public void shouldNotFindAnyUsers() {
        //when
        List<?> phonebookEntries = connection.findPhonebookEntries(newUsername, newFirstName, newLastName);

        //then
        assertThat(phonebookEntries.size(), is(0));
    }

    @Test
    public void shouldCreateNewPhonebookEntry() throws NamingException {
        //given
        MimuwOrgPerson person = new MimuwOrgPerson(newFirstName, newLastName, newUsername, "0101", "793111111, 793222222");

        try {
            //when
            connection.createPhonebookEntry(person);

            //then
            assertThat(connection.findPhonebookEntries(newUsername, newFirstName, newUsername).size(), is(1));
        } finally {
            //revert fixture changes
            connection.ldapContext.unbind(connection.getBindNameForNewEntry(person));
        }
    }
}
