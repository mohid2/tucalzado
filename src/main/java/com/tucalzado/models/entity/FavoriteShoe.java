package com.tucalzado.models.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "favorite_products")
public class FavoriteShoe {

    @EmbeddedId
    private FavoriteShoeId id;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @MapsId("shoeId")
    @JoinColumn(name = "shoe_id")
    private Shoe shoe;
}
