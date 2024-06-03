package com.tucalzado.entity.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.tucalzado.validation.anotation.ExistEmail;
import com.tucalzado.validation.anotation.ExistUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_db")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    @Column(unique = true, length = 100)
    @NotEmpty
    @Email
    @ExistEmail
    private String email;
    @Column(unique = true,length = 30)
    @NotEmpty
    @ExistUsername
    private String username;
    @Column(length = 60)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty
    @Pattern(regexp = "^.{6,}$", message = "La contrasenya ha de tener al menos 6 caracteres")
    private String password;
    @NotEmpty
    private String mobile;
    private String fix;
    @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",joinColumns = @JoinColumn(name = "user_id" ),
            inverseJoinColumns = @JoinColumn(name = "role_id")
            ,uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id","role_id"})})
    private List<Role> roles;
}
