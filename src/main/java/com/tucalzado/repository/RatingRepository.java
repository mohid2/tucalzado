package com.tucalzado.repository;


import com.tucalzado.models.entity.Rating;
import com.tucalzado.models.entity.Shoe;
import com.tucalzado.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    boolean existsByUserAndShoe(User user, Shoe shoe);
}
