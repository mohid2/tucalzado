package com.tucalzado.service.impl;



import com.tucalzado.models.entity.Address;
import com.tucalzado.models.entity.Role;
import com.tucalzado.models.dto.AddressDTO;
import com.tucalzado.models.dto.UserDTO;
import com.tucalzado.models.enums.RoleEnum;
import com.tucalzado.models.entity.User;
import com.tucalzado.models.mapper.IAddressMapper;
import com.tucalzado.models.mapper.IUserMapper;
import com.tucalzado.repository.IAddressRepository;
import com.tucalzado.repository.IRoleRepository;
import com.tucalzado.repository.IUserRepository;
import com.tucalzado.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IAddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;
    private final IUserMapper userMapper;
    private final IAddressMapper addressMapper;


    @Override
    public User createUser(UserDTO userDTO)  {
        Role roleAdmin = roleRepository.findByRole(RoleEnum.ADMIN).orElseThrow(() -> new RuntimeException("Role not found"));
        Role roleUser = roleRepository.findByRole(RoleEnum.USER).orElseThrow(() -> new RuntimeException("Role not found"));
        userDTO.setRoles(List.of(roleUser, roleAdmin));
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userMapper.userDTOToUser(userDTO);
        return userRepository.save(user);
    }

    @Override
    public UserDTO getUserByUsername(String name) {
        Optional<User> userOptional = userRepository.findByUsername(name);
        return userOptional.map(userMapper::userToUserDTO).orElseThrow(() -> new RuntimeException("User not found"));
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
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDTO getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(userMapper::userToUserDTO).orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    @Override
    public void updateAddressUser(AddressDTO addressDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));

        Address address = user.getAddress();
        if (address != null) {
            updateExistingAddress(addressDTO, address);
        } else {
            user.setAddress(addressMapper.addressDTOToAddress(addressDTO));
        }
        userRepository.save(user);
    }
    private void updateExistingAddress(AddressDTO addressDTO, Address address) {
        address.setCountry(addressDTO.getCountry());
        address.setCommunity(addressDTO.getCommunity());
        address.setProvince(addressDTO.getProvince());
        address.setCity(addressDTO.getCity());
        address.setZipCode(addressDTO.getZipCode());
        address.setStreet(addressDTO.getStreet());
        address.setNumber(addressDTO.getNumber());
        address.setFloor(addressDTO.getFloor());
        address.setDoor(addressDTO.getDoor());
        addressRepository.save(address);
    }
}
