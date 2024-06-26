package com.tucalzado.repository;


import com.tucalzado.models.entity.ShoeStock;
import com.tucalzado.models.entity.ShoeStockId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IShoeStockRepository extends JpaRepository<ShoeStock, ShoeStockId> {
    List<ShoeStock> findAllByShoeId(Long id);
}
