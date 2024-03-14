package info.nahid.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UuidValidator implements ConstraintValidator<ValidUuid, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
