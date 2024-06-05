package com.tucalzado.models.mapper;

import com.tucalzado.models.dto.UserDTO;
import com.tucalzado.models.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IUserMapper {


    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);
}
