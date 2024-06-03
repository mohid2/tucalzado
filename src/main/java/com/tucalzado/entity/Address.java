package com.tucalzado.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "address_db")
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String country;
    private String community;
    private String province;
    private String city;
    private String zipCode;
    private String street;
    private String number;
    private String floor;
    private String door;
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;

}
