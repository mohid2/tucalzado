package com.tucalzado.repository;


import com.tucalzado.models.entity.Shoe;
import com.tucalzado.models.enums.ShoeTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IShoeRepository extends JpaRepository<Shoe,Long> {
    List<Shoe> findByRatingGreaterThanEqual(Integer rating );
    List<Shoe> findAllByType(ShoeTypeEnum type);

    @Query("SELECT DISTINCT s.brand FROM Shoe s")
    List<String> findAllBrand();

    @Query("SELECT s FROM Shoe s WHERE s.name LIKE %?1%")
    List<Shoe> findShoeByNameLikeIgnoreCase(String term);
}