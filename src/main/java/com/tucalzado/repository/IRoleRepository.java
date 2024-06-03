package com.tucalzado.repository;

import com.tucalzado.entity.user.Role;
import com.tucalzado.entity.user.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleEnum role);
}
