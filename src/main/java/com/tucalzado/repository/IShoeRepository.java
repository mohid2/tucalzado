package com.tucalzado.repository;


import com.tucalzado.entity.Shoe;
import com.tucalzado.entity.enums.ShoeTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IShoeRepository extends JpaRepository<Shoe,Long> {
    List<Shoe> findByRatingGreaterThanEqual(Integer rating );
    List<Shoe> findAllByType(ShoeTypeEnum type);
}