package com.tucalzado.models.entity;

import com.tucalzado.models.enums.Gender;
import com.tucalzado.models.enums.ShoeTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "shoes")
public  class Shoe implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imagePrimary;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "shoe_id")
    private List<ImageUrl> imageUrl;
    private String name;
    private String brand;
    private double rating;
    private double price;
    @Column(length = 10000)
    private String description;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String color;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private ShoeTypeEnum type;
    @OneToMany(mappedBy = "shoe", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ShoeStock> shoeStocks;

    @OneToMany(mappedBy = "shoe", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Rating> ratings;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    public void updateRating() {
        if (ratings == null || ratings.isEmpty()) {
            this.rating = 0;
        } else {
            this.rating = Math.round(ratings.stream().mapToInt(Rating::getRatingValue).average().orElse(0.0));
        }
    }

}
