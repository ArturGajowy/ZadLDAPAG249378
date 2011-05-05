package pl.gajowy.phonebook.application;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.gajowy.phonebook.Fixture;
import pl.gajowy.phonebook.domain.MimuwOrgPerson;

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

    @After
    public void tearDown() {
        connection.close();
    }

    @Test
    public void shouldGetAllVisibleEntries() {
        //when
        List<?> phonebookEntries = connection.findPhonebookEntries("", "", "");

        //then
        assertThat(phonebookEntries.size(), Is.is(Fixture.phoneBookEntriesVisibleForLegitUser));
    }

    @Test
    public void shouldFetchUserData() {
        //when
        List<MimuwOrgPerson> phonebookEntries = connection.findPhonebookEntries(legitUsername, legitFirstName, legitLastName);

        //then
        assertThat(phonebookEntries.size(), Is.is(1));
        MimuwOrgPerson fetchedPerson = phonebookEntries.get(0);
        assertThat(fetchedPerson.getFirstName(), is(legitFirstName));
        assertThat(fetchedPerson.getLastName(), is(legitLastName));
        assertThat(fetchedPerson.getUsername(), is(legitUsername));
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
        MimuwOrgPerson person = new MimuwOrgPerson(newFirstName, newLastName, newUsername, "0101", "793111111");

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
