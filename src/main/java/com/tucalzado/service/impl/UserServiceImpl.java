package com.tucalzado.service.impl;



import com.tucalzado.entity.user.Address;
import com.tucalzado.entity.user.Role;
import com.tucalzado.entity.user.RoleEnum;
import com.tucalzado.entity.user.User;
import com.tucalzado.repository.IAddressRepository;
import com.tucalzado.repository.IRoleRepository;
import com.tucalzado.repository.IUserRepository;
import com.tucalzado.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IAddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;

    public UserServiceImpl(IAddressRepository addressRepository, IUserRepository userRepository, PasswordEncoder passwordEncoder, IRoleRepository roleRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User createUser(User user)  {
        Role roleAdmin = roleRepository.findByRole(RoleEnum.ADMIN).orElseThrow(() -> new RuntimeException("Role not found"));
        Role roleUser = roleRepository.findByRole(RoleEnum.USER).orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(List.of(roleAdmin, roleUser));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public User updateUser(User user) {
        Optional<User> userOptional = userRepository.findById(user.getId());
        if(userOptional.isPresent()){
            User userUpdated = userOptional.get();
            userUpdated.setFix(user.getFix());
            userUpdated.setMobile(user.getMobile());
            userUpdated.setEmail(user.getEmail());
            userUpdated.setUsername(user.getUsername());
            return userRepository.save(userUpdated);
        }
        return null;
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void updateAddressUser(Address address, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User userUpdated = userOptional.get();
            if (userUpdated.getAddress() != null) {
                Optional<Address> addressOptional = addressRepository.findById(userUpdated.getAddress().getId());
                if(addressOptional.isPresent()){
                    Address addressUpdated = getAddress(address, addressOptional);
                    userUpdated.setAddress(addressUpdated);
                    userRepository.save(userUpdated);
                }
            }else {
                userUpdated.setAddress(address);
                userRepository.save(userUpdated);
            }

        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private static Address getAddress(Address address, Optional<Address> addressOptional) {
        Address  addressUpdated = addressOptional.get();
        addressUpdated.setCountry(address.getCountry());
        addressUpdated.setCommunity(address.getCommunity());
        addressUpdated.setProvince(address.getProvince());
        addressUpdated.setCity(address.getCity());
        addressUpdated.setZipCode(address.getZipCode());
        addressUpdated.setStreet(address.getStreet());
        addressUpdated.setNumber(address.getNumber());
        addressUpdated.setFloor(address.getFloor());
        addressUpdated.setDoor(address.getDoor());
        return addressUpdated;
    }
}
