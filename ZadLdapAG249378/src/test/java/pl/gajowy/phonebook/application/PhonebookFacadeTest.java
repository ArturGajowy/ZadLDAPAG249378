package pl.gajowy.phonebook.application;

import org.junit.Test;
import pl.gajowy.phonebook.Fixture;
import pl.gajowy.phonebook.application.exception.InvalidConnectionParamtersException;
import pl.gajowy.phonebook.application.exception.InvalidCredentialsException;

import static pl.gajowy.phonebook.Fixture.ldapServerAddress;
import static pl.gajowy.phonebook.Fixture.ldapServerPort;
import static org.junit.Assert.assertNotNull;

public class PhonebookFacadeTest {

    private PhonebookFacade phonebookFacade = new PhonebookFacade();

    @Test
    public void shouldLogIn() {
        //given
        String username = Fixture.legitUsername;
        String password = Fixture.legitPassword;

        //when
        PhonebookConnection connection = phonebookFacade.logIn(ldapServerAddress, ldapServerPort, username, password);

        //then
        assertNotNull(connection);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void shouldNotLogInWithWrongPassword() {
        //given
        String username = Fixture.legitUsername;
        String password = "badPassword";

        //when
        phonebookFacade.logIn(ldapServerAddress, ldapServerPort, username, password);

        //then an exception is thrown
    }

    @Test(expected = InvalidCredentialsException.class)
    public void shouldNotLogInWithWrongUsername() {
        //given
        String username = "nonexistentUser";
        String password = "somePassword";

        //when
        phonebookFacade.logIn(ldapServerAddress, ldapServerPort, username, password);

        //then an exception is thrown
    }

    @Test(expected = InvalidConnectionParamtersException.class)
    public void shouldNotLogInOnWrongPort() {
        //given
        String username = Fixture.legitUsername;
        String password = Fixture.legitUsername;
        int badPort = 9326;

        //when
        phonebookFacade.logIn(ldapServerAddress, badPort, username, password);

        //then an exception is thrown
    }

    @Test(expected = InvalidConnectionParamtersException.class)
    public void shouldNotLogInOnWrongServerAddress() {
        //given
        String username = Fixture.legitUsername;
        String password = Fixture.legitUsername;
        String hopefullyNonexistentHost = "noSuchAddress.neverland.11235.pl";

        //when
        phonebookFacade.logIn(hopefullyNonexistentHost, ldapServerPort, username, password);

        //then an exception is thrown
    }

}
