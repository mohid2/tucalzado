package com.tucalzado.models.dto;

import com.tucalzado.models.entity.ImageUrl;
import com.tucalzado.models.entity.Rating;
import com.tucalzado.models.entity.ShoeStock;
import com.tucalzado.models.enums.Gender;
import com.tucalzado.models.enums.ShoeTypeEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoeDTO {
    private Long id;
    private String imagePrimary;
    private List<ImageUrl> imageUrl;
    @NotBlank
    private String name;
    @NotBlank
    private String brand;
    private double rating;
    private double price;
    @Size(min = 50, max = 1000 , message = "La descripción debe tener entre 50 y 1000 caracteres")
    private String description;
    @NotNull(message = "El genero es requerido")
    private Gender gender;
    @NotBlank
    private String color;
    private LocalDateTime createdAt;
    @NotNull(message = "El tipo de calzado es requerido")
    private ShoeTypeEnum type;
    @ToString.Exclude
    private List<ShoeStock> shoeStocks;
    @ToString.Exclude
    private List<Rating> ratings;
    public void updateRating() {
        if (ratings == null || ratings.isEmpty()) {
            this.rating = 0;
        } else {
            this.rating = Math.round(ratings.stream().mapToInt(Rating::getRatingValue).average().orElse(0.0));
        }
    }

}
