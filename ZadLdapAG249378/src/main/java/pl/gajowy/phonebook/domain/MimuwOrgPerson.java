package pl.gajowy.phonebook.domain;


import org.hibernate.validator.constraints.NotBlank;
import pl.gajowy.phonebook.domain.constraints.IsValidNameday;

import javax.validation.constraints.Pattern;

public class MimuwOrgPerson {
    public static final String LAST_NAME = "Last name";
    public static final String FIRST_NAME = "First name";
    public static final String USER_NAME = "Username";
    public static final String NAMEDAY = "Nameday";
    public static final String PHONE_NUMBER = "Phone number";

    private String firstName;

    @NotBlank(message = LAST_NAME + " is mandatory")
    private String lastName;

    @NotBlank(message = USER_NAME + " is mandatory")
    private String username;

    @IsValidNameday(message = NAMEDAY + " must be a valid day of month in MMdd format")
    private String nameDay;

    @Pattern(
            regexp = "\\s*\\+?\\s*\\d[\\s\\d]*",
            message = PHONE_NUMBER + " must have at least one digit; can contain only \"+\", numbers and whitespace"
    )
    private String telephoneNumber;

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

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getNameDay() {
        return nameDay;
    }
}
