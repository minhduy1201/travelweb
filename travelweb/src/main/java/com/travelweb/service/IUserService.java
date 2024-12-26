package com.travelweb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.travelweb.dto.UserDTO;
import com.travelweb.entity.UserEntity;

public interface IUserService {
	UserDTO createUser(UserDTO userDTO);

    List<UserEntity> getUsers();

    UserEntity getUserById(Long userId);

    UserDTO updateUser(Long userId, UserDTO userDTO);
    
//    Optional<UserEntity> findByUsername(String username);
    
    boolean existByUsername(String username);
    boolean existByEmail(String email);
    
//    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
