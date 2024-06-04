package com.tucalzado.validation.validator;


import com.tucalzado.repository.IUserRepository;
import com.tucalzado.validation.anotation.ExistUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistUsernameValidation implements ConstraintValidator<ExistUsername, String> {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        // Verificar si el username ya existe en el repositorio
        return !userRepository.findByUsername(username).isPresent();
    }
}
