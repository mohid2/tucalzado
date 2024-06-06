package com.tucalzado.repository;


import com.tucalzado.models.entity.ImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IImageUrlRepository extends JpaRepository<ImageUrl, Long> {
    Optional<ImageUrl> findByUrlContaining(String filename);
}
