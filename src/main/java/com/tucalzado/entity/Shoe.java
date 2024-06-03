package com.tucalzado.entity;

import com.tucalzado.entity.enums.Gender;
import com.tucalzado.entity.enums.ShoeTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "shoes")
public  class Shoe implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String imagePrimary;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "shoe_id")
    private List<ImageUrl> imageUrl;
    @NotEmpty
    private String name;
    @NotEmpty
    private String brand;
    private double rating;
    private double price;
    @Column(length = 10000)
    @Size(min = 50, max = 1000 , message = "La descripción debe tener entre 50 y 1000 caracteres")
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
