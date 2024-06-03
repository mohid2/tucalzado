package com.tucalzado.validation.validator;

import com.tucalzado.repository.IUserRepository;
import com.tucalzado.service.IUserService;
import com.tucalzado.validation.anotation.ExistEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistEmailValidation implements ConstraintValidator<ExistEmail,String> {

    @Autowired
    private IUserService userService;

    public ExistEmailValidation() {
    }

    @Override
    public void initialize(ExistEmail existEmail) {
        ConstraintValidator.super.initialize(existEmail);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if(email == null){
            return false;
        }
        // Verificar si el email ya existe en el repositorio
        return !userService.findByEmail(email).isPresent();
    }
}
