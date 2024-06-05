package com.tucalzado.models.entity;


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
    @Id
    private ShoeStockId id;
    @ManyToOne
    @MapsId("shoeId")
    @JoinColumn(name = "shoe_id" ,insertable = false, updatable = false)
    @JsonIgnore
    private Shoe shoe;
    @ManyToOne
    @MapsId("sizeId")
    @JoinColumn(name = "size_id" ,insertable = false, updatable = false)
    private Size size;
    private int stock;
}
