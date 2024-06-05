package com.tucalzado.service;


import com.tucalzado.models.dto.FavoriteShoeDTO;
import com.tucalzado.models.entity.FavoriteShoe;
import com.tucalzado.models.entity.FavoriteShoeId;

import java.util.List;

public interface IFavoriteShoeService {
    boolean save(FavoriteShoeDTO favoriteShoeDTO,String username);
    void delete(FavoriteShoeId id);
    List<FavoriteShoe> findAllByUserId(Long userId);
}
