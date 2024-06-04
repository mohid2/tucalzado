package com.tucalzado.service;


import com.tucalzado.models.entity.FavoriteShoe;

import java.util.List;

public interface IFavoriteShoeService {
    void save(FavoriteShoe favoriteProduct);
    void delete(Long id);
    List<FavoriteShoe> findAllByUserId(Long userId);
}
