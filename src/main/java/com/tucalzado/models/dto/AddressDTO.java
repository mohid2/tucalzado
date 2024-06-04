package com.tucalzado.models.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
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
}
