package com.tucalzado.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tucalzado.models.entity.Address;
import com.tucalzado.models.entity.Role;
import com.tucalzado.validation.anotation.ExistEmail;
import com.tucalzado.validation.anotation.ExistUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    @NotEmpty
    @Email
    @ExistEmail
    private String email;
    @NotEmpty
    @ExistUsername
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty
    @Pattern(regexp = "^.{6,}$", message = "La contrasenya ha de tener al menos 6 caracteres")
    private String password;
    @NotEmpty
    private String mobile;
    private String fix;
    private Address address;
    private List<Role> roles;
}
