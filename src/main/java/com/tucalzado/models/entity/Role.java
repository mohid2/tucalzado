package com.tucalzado.models.entity;

import com.tucalzado.models.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "roles_db")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public Role(RoleEnum role) {
        this.role = role;
    }
}
