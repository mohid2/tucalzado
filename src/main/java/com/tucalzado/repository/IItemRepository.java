package com.tucalzado.repository;

import com.tucalzado.models.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IItemRepository extends JpaRepository<Item, Long> {
}
