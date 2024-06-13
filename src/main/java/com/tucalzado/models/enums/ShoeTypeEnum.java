package com.tucalzado.models.enums;

import lombok.Getter;

@Getter
public enum ShoeTypeEnum  {
    ZAPATOS("Zapatos"),
    ZAPATOS_CLASICO("Zapatos clásicos"),
    ZAPATILLAS("Zapatillas"),
    ZAPATILLAS_DEPORTIVAS("Zapatillas deportivas"),
    SANDALIAS("Sandalías"),
    CHANCLAS("Chanclas"),
    TACONES("Tacones"),
    PANTUFLAS("Pantuflas"),
    MOCASINES("Mocasines"),
    BOTAS("Botas"),
    OTROS("Otros");

    private final String typeEnum;

    ShoeTypeEnum(String type) {
        this.typeEnum = type;
    }
}
