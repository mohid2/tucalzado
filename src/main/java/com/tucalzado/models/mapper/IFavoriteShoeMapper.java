package com.tucalzado.models.mapper;

import com.tucalzado.models.dto.FavoriteShoeDTO;
import com.tucalzado.models.entity.FavoriteShoe;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IFavoriteShoeMapper {

    @Mapping(source = "shoe", target = "shoe")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "id", target = "id")
    FavoriteShoe favoriteShoeDTOToFavoriteShoe(FavoriteShoeDTO favoriteShoeDTO);

    @InheritInverseConfiguration
    FavoriteShoeDTO favoriteShoeToFavoriteShoeDTO(FavoriteShoe favoriteShoe);
}
