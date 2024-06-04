package com.tucalzado.repository;


import com.tucalzado.models.entity.ImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IImageUrlRepository extends JpaRepository<ImageUrl, Long> {
}
