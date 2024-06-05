package com.tucalzado.service.impl;


import com.tucalzado.models.dto.FavoriteShoeDTO;
import com.tucalzado.models.dto.FavoriteShoeIdDTO;
import com.tucalzado.models.dto.UserDTO;
import com.tucalzado.models.entity.FavoriteShoe;
import com.tucalzado.models.entity.FavoriteShoeId;
import com.tucalzado.models.mapper.IFavoriteShoeMapper;
import com.tucalzado.repository.IFavoriteShoeRepository;
import com.tucalzado.service.IFavoriteShoeService;
import com.tucalzado.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class IFavoriteShoeServiceImpl implements IFavoriteShoeService {

    private final IFavoriteShoeRepository iFavoriteShoeRepository;
    private final IFavoriteShoeMapper iFavoriteShoeMapper;
    private final IUserService userService;


    @Override
    public boolean save(FavoriteShoeDTO favoriteShoeDTO,String username) {
        UserDTO userDTO = userService.getUserByUsername(username);
        favoriteShoeDTO.setUser(userDTO);
        favoriteShoeDTO.setId(new FavoriteShoeIdDTO(userDTO.getId(),favoriteShoeDTO.getShoe().getId()));
        List<FavoriteShoe> favoriteProducts = iFavoriteShoeRepository.findAllByUserId(userDTO.getId());
        if (favoriteProducts!=null) {
            for (FavoriteShoe favoriteProduct : favoriteProducts) {
                if (favoriteProduct.getShoe().getId().equals(favoriteShoeDTO.getShoe().getId())) {
                    return false;
                }
            }
        }
        iFavoriteShoeRepository.save(iFavoriteShoeMapper.favoriteShoeDTOToFavoriteShoe(favoriteShoeDTO));
        return true;
    }

    @Override
    public void delete(FavoriteShoeId id) {
        iFavoriteShoeRepository.deleteById(new FavoriteShoeId(id.getUserId(),id.getShoeId()));
    }

    @Override
    public List<FavoriteShoe> findAllByUserId(Long userId) {
        return iFavoriteShoeRepository.findAllByUserId(userId);
    }
}
