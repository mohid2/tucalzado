package com.tucalzado.repository;


import com.tucalzado.entity.Rating;
import com.tucalzado.entity.Shoe;
import com.tucalzado.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    boolean existsByUserAndShoe(User user, Shoe shoe);
}
