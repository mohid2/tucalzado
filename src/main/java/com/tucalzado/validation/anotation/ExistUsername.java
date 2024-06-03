package com.tucalzado.validation.anotation;

import com.tucalzado.validation.validator.ExistUsernameValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ExistUsernameValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistUsername {
    String message() default "Ya existe un usuario con este nombre de usuario";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
