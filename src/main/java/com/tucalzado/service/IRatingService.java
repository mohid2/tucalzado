package com.tucalzado.service;


import com.tucalzado.entity.Shoe;
import com.tucalzado.entity.User;

public interface IRatingService {
    boolean saveRating(String username, Long shoeId, int ratingValue, String comment);

    public boolean hasRated(User user, Shoe shoe);
}
