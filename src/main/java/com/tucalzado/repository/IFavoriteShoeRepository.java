package com.tucalzado.repository;


import com.tucalzado.models.entity.FavoriteShoe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFavoriteShoeRepository extends JpaRepository<FavoriteShoe, Long> {
    List<FavoriteShoe> findAllByUserId(Long userId);
}
