package com.tucalzado.models.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_db")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    @Column(unique = true, length = 100)
    private String email;
    @Column(unique = true,length = 30)
    private String username;
    @Column(length = 60)
    private String password;
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
