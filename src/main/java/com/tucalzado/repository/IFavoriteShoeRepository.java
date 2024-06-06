package com.tucalzado.repository;


import com.tucalzado.models.entity.FavoriteShoe;
import com.tucalzado.models.entity.FavoriteShoeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFavoriteShoeRepository extends JpaRepository<FavoriteShoe, FavoriteShoeId> {
    List<FavoriteShoe> findAllByUserId(Long userId);

    void deleteByShoeId(Long id);
}
