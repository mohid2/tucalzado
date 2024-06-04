package com.tucalzado.service;



import com.tucalzado.models.entity.User;
import com.tucalzado.models.dto.AddressDTO;
import com.tucalzado.models.dto.UserDTO;

import java.util.Optional;

public interface IUserService {
    User createUser(UserDTO userDTO);
    Optional<User> getUserByUsername(String name);
    User updateUser(UserDTO  userDTO);
    void deleteUser(User user);
    Optional<User> getUserById(Long id);
    void updateAddressUser(AddressDTO addressDTO, Long userId);

}

