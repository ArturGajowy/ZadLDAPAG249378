package pl.gajowy.phonebook;

public class MimuwOrgPerson {
    private String firstName;
    private String lastName;
    private String username;
    private String nameDay;
    private Object telephoneNumber; //FIXME make plural?

    //TODO make less stringy
    public MimuwOrgPerson(String firstName, String lastName, String username, String nameDay, String telephoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.nameDay = nameDay;
        this.telephoneNumber = telephoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Object getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getNameDay() {
        return nameDay;
    }
}
