package com.tucalzado.repository;

import com.tucalzado.models.entity.Role;
import com.tucalzado.models.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleEnum role);
}
