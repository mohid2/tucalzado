package com.tucalzado.repository;

import com.tucalzado.entity.Purchase;
import com.tucalzado.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllByUser(User user);
}

