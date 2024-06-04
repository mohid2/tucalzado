package com.tucalzado.service.impl;


import com.tucalzado.entity.FavoriteShoe;
import com.tucalzado.repository.IFavoriteShoeRepository;
import com.tucalzado.service.IFavoriteShoeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IFavoriteShoeServiceImpl implements IFavoriteShoeService {

    private final IFavoriteShoeRepository iFavoriteShoeRepository;

    public IFavoriteShoeServiceImpl(IFavoriteShoeRepository iFavoriteShoeRepository) {
        this.iFavoriteShoeRepository = iFavoriteShoeRepository;
    }


    @Override
    public void save(FavoriteShoe favoriteProduct) {
        iFavoriteShoeRepository.save(favoriteProduct);
    }

    @Override
    public void delete(Long id) {
        iFavoriteShoeRepository.deleteById(id);
    }

    @Override
    public List<FavoriteShoe> findAllByUserId(Long userId) {
        return iFavoriteShoeRepository.findAllByUserId(userId);
    }
}
