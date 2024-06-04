package com.tucalzado.service.impl;



import com.tucalzado.models.entity.Address;
import com.tucalzado.models.entity.Role;
import com.tucalzado.models.dto.AddressDTO;
import com.tucalzado.models.dto.UserDTO;
import com.tucalzado.models.enums.RoleEnum;
import com.tucalzado.models.entity.User;
import com.tucalzado.models.mapper.AddressMapper;
import com.tucalzado.models.mapper.UserMapper;
import com.tucalzado.repository.IAddressRepository;
import com.tucalzado.repository.IRoleRepository;
import com.tucalzado.repository.IUserRepository;
import com.tucalzado.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@AllArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IAddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;


    @Override
    public User createUser(UserDTO userDTO)  {
        Role roleAdmin = roleRepository.findByRole(RoleEnum.ADMIN).orElseThrow(() -> new RuntimeException("Role not found"));
        Role roleUser = roleRepository.findByRole(RoleEnum.USER).orElseThrow(() -> new RuntimeException("Role not found"));
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userMapper.mapToEntity(userDTO);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public User updateUser(UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(userDTO.getId());
        if(userOptional.isPresent()){
            User userUpdated = userOptional.get();
            userUpdated.setFix(userDTO.getFix());
            userUpdated.setMobile(userDTO.getMobile());
            userUpdated.setEmail(userDTO.getEmail());
            userUpdated.setUsername(userDTO.getUsername());
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
    public void updateAddressUser(AddressDTO addressDTO, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User userUpdated = userOptional.get();
            if (userUpdated.getAddress() != null) {
                Optional<Address> addressOptional = addressRepository.findById(userUpdated.getAddress().getId());
                if(addressOptional.isPresent()){
                    Address addressUpdated = getAddress(addressDTO, addressOptional);
                    userUpdated.setAddress(addressUpdated);
                    userRepository.save(userUpdated);
                }
            }else {
                userUpdated.setAddress(addressMapper.mapToEntity(addressDTO));
                userRepository.save(userUpdated);
            }

        }
    }

    private static Address getAddress(AddressDTO addressDTO, Optional<Address> addressOptional) {
        Address  addressUpdated = addressOptional.get();
        addressUpdated.setCountry(addressDTO.getCountry());
        addressUpdated.setCommunity(addressDTO.getCommunity());
        addressUpdated.setProvince(addressDTO.getProvince());
        addressUpdated.setCity(addressDTO.getCity());
        addressUpdated.setZipCode(addressDTO.getZipCode());
        addressUpdated.setStreet(addressDTO.getStreet());
        addressUpdated.setNumber(addressDTO.getNumber());
        addressUpdated.setFloor(addressDTO.getFloor());
        addressUpdated.setDoor(addressDTO.getDoor());
        return addressUpdated;
    }
}
