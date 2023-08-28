package nandreas.ordermanagement.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
public class ValidationService
{
    private final Validator validator;

    public void validate(Object request)
    {
        Set<ConstraintViolation<Object>> constraintViolations = this.validator.validate(request);
        if (constraintViolations.size() > 0) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
