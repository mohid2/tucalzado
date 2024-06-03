package com.tucalzado.validation.anotation;

import com.tucalzado.validation.validator.ExistEmailValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistEmailValidation.class )
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ExistEmail {
    String message() default "Ya existe un usuario con este correo electronico";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
