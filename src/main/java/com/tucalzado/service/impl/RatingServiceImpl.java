package com.tucalzado.service.impl;


import com.tucalzado.models.entity.Rating;
import com.tucalzado.models.entity.Shoe;
import com.tucalzado.models.entity.User;
import com.tucalzado.repository.IShoeRepository;
import com.tucalzado.repository.IUserRepository;
import com.tucalzado.repository.RatingRepository;
import com.tucalzado.service.IRatingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatingServiceImpl implements IRatingService {

    private final RatingRepository ratingRepository;
    private final IUserRepository userRepository;
    private final IShoeRepository shoeRepository;

    public RatingServiceImpl(RatingRepository ratingRepository, IUserRepository userRepository, IShoeRepository shoeRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.shoeRepository = shoeRepository;
    }

    @Override
    public boolean hasRated(User user, Shoe shoe) {
        return ratingRepository.existsByUserAndShoe(user, shoe);
    }

    @Override
    @Transactional
    public boolean saveRating(String username, Long shoeId, int ratingValue, String comment) {
        Shoe shoe = shoeRepository.findById(shoeId)
                .orElseThrow(() -> new RuntimeException("Shoe not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!hasRated(user, shoe)) {
            Rating rating = Rating.builder().shoe(shoe).user(user).ratingValue(ratingValue).comment(comment).build();
            ratingRepository.save(rating);
            // Actualizar la valoración promedio del zapato
            shoe.getRatings().add(rating);
            shoe.updateRating();
            shoeRepository.save(shoe);

            return true;
        }
        return false;
    }
}
