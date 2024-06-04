package com.tucalzado.models.mapper;

import com.tucalzado.models.entity.Address;
import com.tucalzado.models.dto.AddressDTO;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper implements IMapper<Address, AddressDTO> {
    @Override
    public AddressDTO mapToDTO(Address in) {
        if(in != null){
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setId(in.getId());
            addressDTO.setCountry(in.getCountry());
            addressDTO.setCommunity(in.getCommunity());
            addressDTO.setProvince(in.getProvince());
            addressDTO.setCity(in.getCity());
            addressDTO.setZipCode(in.getZipCode());
            addressDTO.setStreet(in.getStreet());
            addressDTO.setNumber(in.getNumber());
            addressDTO.setFloor(in.getFloor());
            addressDTO.setDoor(in.getDoor());
            return addressDTO;
        }
        return null;
    }

    @Override
    public Address mapToEntity(AddressDTO in) {
        if(in != null){
            Address address = new Address();
            address.setId(in.getId());
            address.setCountry(in.getCountry());
            address.setCommunity(in.getCommunity());
            address.setProvince(in.getProvince());
            address.setCity(in.getCity());
            address.setZipCode(in.getZipCode());
            address.setStreet(in.getStreet());
            address.setNumber(in.getNumber());
            address.setFloor(in.getFloor());
            address.setDoor(in.getDoor());
            return address;
        }
        return null;
    }
}
