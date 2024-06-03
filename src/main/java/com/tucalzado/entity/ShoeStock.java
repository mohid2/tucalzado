package com.tucalzado.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "shoe_stock")
public class ShoeStock implements Serializable {
    @EmbeddedId
    private ShoeStockId id;

    @ManyToOne
    @MapsId("shoeId")
    @JoinColumn(name = "shoe_id")
    @JsonIgnore
    private Shoe shoe;

    @ManyToOne
    @MapsId("sizeId")
    @JoinColumn(name = "size_id")
    private Size size;

    private int stock;
}
