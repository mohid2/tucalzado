package com.tucalzado.models.enums;

import lombok.Getter;

@Getter
public enum ShoeTypeEnum  {
    ZAPATOS("Zapatos"),
    ZAPATOS_CLÁSICO("Zapatos clásicos"),
    ZAPATILLAS("Zapatillas"),
    ZAPATILLAS_DEPORTIVAS("Zapatillas deportivas"),
    SANDALIAS("Sandalías"),
    BOTINES("Botines"),
    TACONES("Tacones"),
    PANTUFLAS("Pantuflas"),
    BOTAS("Botas"),
    OTROS("Otros");

    private final String typeEnum;

    ShoeTypeEnum(String type) {
        this.typeEnum = type;
    }
}
