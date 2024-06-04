package com.tucalzado.models.mapper;

import com.tucalzado.models.entity.User;
import com.tucalzado.models.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements com.tucalzado.models.mapper.IMapper<User, UserDTO> {

    @Override
    public UserDTO mapToDTO(User in) {
        if(in != null){
            UserDTO userDTO = new UserDTO();
            userDTO.setId(in.getId());
            userDTO.setFirstname(in.getFirstname());
            userDTO.setLastname(in.getLastname());
            userDTO.setEmail(in.getEmail());
            userDTO.setUsername(in.getUsername());
            userDTO.setMobile(in.getMobile());
            userDTO.setFix(in.getFix());
            return userDTO;
        }
        return null;
    }

    @Override
    public User mapToEntity(UserDTO in) {
        if(in != null){
            User user = new User();
            user.setId(in.getId());
            user.setFirstname(in.getFirstname());
            user.setLastname(in.getLastname());
            user.setEmail(in.getEmail());
            user.setUsername(in.getUsername());
            user.setMobile(in.getMobile());
            user.setFix(in.getFix());
            user.setPassword(in.getPassword());
            return user;
        }
        return null;
    }
}
