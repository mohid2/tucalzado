package com.tucalzado.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tucalzado.entity.enums.ShoeSizeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sizes")
public class Size implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ShoeSizeEnum shoeSize;
    @OneToMany(mappedBy = "size", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<ShoeStock> shoeStocks;
}
