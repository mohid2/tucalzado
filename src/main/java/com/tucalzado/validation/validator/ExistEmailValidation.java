package com.tucalzado.validation.validator;

import com.tucalzado.repository.IUserRepository;
import com.tucalzado.validation.anotation.ExistEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistEmailValidation implements ConstraintValidator<ExistEmail,String> {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if(email == null){
            return false;
        }
        // Verificar si el email ya existe en el repositorio
        return !userRepository.findByEmail(email).isPresent();
    }
}
