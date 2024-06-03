package com.tucalzado.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ShoeStockId implements Serializable {
    @Column(name = "shoe_id")
    private Long shoeId;

    @Column(name = "size_id")
    private Long sizeId;
}
