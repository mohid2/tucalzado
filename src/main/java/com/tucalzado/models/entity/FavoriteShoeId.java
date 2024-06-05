package com.tucalzado.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FavoriteShoeId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "shoe_id")
    private Long shoeId;
}
