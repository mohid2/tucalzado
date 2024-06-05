package com.tucalzado.models.mapper;

import com.tucalzado.models.dto.ShoeDTO;
import com.tucalzado.models.entity.Shoe;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IShoeMapper {

    Shoe shoeDTOToShoe(ShoeDTO shoeDTO);

    ShoeDTO shoeToShoeDTO(Shoe shoe);
}
