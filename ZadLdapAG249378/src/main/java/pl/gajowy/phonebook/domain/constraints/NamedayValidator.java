package pl.gajowy.phonebook.domain.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NamedayValidator implements ConstraintValidator<IsValidNameday, String> {

    private static final String SOME_STEP_YEAR = "2000";

    public void initialize(IsValidNameday constraint) {
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        } else {
            return isValidNameday(value);
        }
    }

    private boolean isValidNameday(String value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(SOME_STEP_YEAR + value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
