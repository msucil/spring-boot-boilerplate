package design.boilerplate.springboot.service.validator.notemptyvalue;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotEmptyValueValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyValue {

    String message() default " can not be null or blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
