package pl.gajowy.phonebook.domain.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Pattern(regexp = "^\\d{4}$", message = IsValidNameday.NAMEDAY_VALIDATOR_MESSAGE_REFERENCE)
@Constraint(validatedBy = NamedayValidator.class)
@ReportAsSingleViolation
@Documented
public @interface IsValidNameday {

    String NAMEDAY_VALIDATOR_MESSAGE_REFERENCE = "{pl.gajowy.phonebook.isValidNameday.message}";

    String message() default NAMEDAY_VALIDATOR_MESSAGE_REFERENCE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
