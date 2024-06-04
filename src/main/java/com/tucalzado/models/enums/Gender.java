package com.tucalzado.models.enums;

import lombok.Getter;

@Getter
public enum Gender  {
    HOMBRE("Hombre"),
    MUJER("Mujer"),
    UNISEX("Unisex");

    private final String genderEnum;

    Gender(String gender) {
        this.genderEnum = gender;
    }
}
