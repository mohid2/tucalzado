package com.tucalzado.service;



import com.tucalzado.entity.user.Address;
import com.tucalzado.entity.user.User;

import java.util.Optional;

public interface IUserService {
    User createUser(User user);
    Optional<User> getUserByUsername(String name);
    User updateUser(User user);
    void deleteUser(User usuario);
    Optional<User> getUserById(Long id);
    void updateAddressUser(Address address, Long userId);

    Optional<User> findByEmail(String email);

}
