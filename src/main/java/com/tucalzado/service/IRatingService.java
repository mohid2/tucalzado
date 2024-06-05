package com.tucalzado.service;


import com.tucalzado.models.dto.UserDTO;
import com.tucalzado.models.entity.Shoe;
import com.tucalzado.models.entity.User;

public interface IRatingService {
    boolean saveRating(String username, Long shoeId, int ratingValue, String comment);

    public boolean hasRated(UserDTO userDTO, Shoe shoe);
}
