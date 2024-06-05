package com.tucalzado.models.mapper;

import com.tucalzado.models.dto.AddressDTO;
import com.tucalzado.models.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IAddressMapper {

    Address addressDTOToAddress(AddressDTO addressDTO);
    AddressDTO addressToAddressDTO(Address address);
}
