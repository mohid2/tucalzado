package com.tucalzado.entity.enums;

import lombok.Getter;

@Getter
public enum ShoeTypeEnum  {
    ZAPATOS("Zapatos"),
    ZAPATOS_CLÁSICO("Zapatos clásicos"),
    ZAPATILLAS("Zapatillas"),
    ZAPATILLAS_CASUALES("Zapatillas casuales"),
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
