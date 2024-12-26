//package com.travelweb.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.travelweb.converter.UserConverter;
//import com.travelweb.dto.UserDTO;
//import com.travelweb.entity.RoleEntity;
//import com.travelweb.entity.UserEntity;
//import com.travelweb.repository.RoleRepository;
//import com.travelweb.repository.UserRepository;
//import com.travelweb.service.IUserService;
//
//@Service
//public class UserService implements IUserService{
//	@Autowired
//	private UserRepository userRepository;
//	@Autowired
//	private UserConverter userConverter;
//	@Autowired
//	private RoleRepository roleRepository;
//
//	@Override
//	public UserDTO createUser(UserDTO userDTO) {
//		UserEntity userEntity = new UserEntity();
//		userEntity = userConverter.toEntity(userDTO);
//		List<RoleEntity> role = roleRepository.findOneByCode(userDTO.getRoleCode());
//		userEntity.setRoles(role);
//		userEntity = userRepository.save(userEntity);
//		return userConverter.toDTO(userEntity);
//	}
//
//	@Override
//	public List<UserEntity> getUsers() {
//		return userRepository.findAll();
//	}
//
//	@Override
//	public UserEntity getUserById(Long userId) {
//		return userRepository.findById(userId).orElse(null);
//	}
//
//	@Override
//	public UserDTO updateUser(Long userId, UserDTO userDTO) {
//		UserEntity user = new UserEntity();
//		UserEntity existingUser = userRepository.findById(userId).orElse(null);
//        if (existingUser != null) {
//          user = userConverter.toEntity(userDTO, existingUser);
//            return userConverter.toDTO(user);
//        }
//		return null;
//	}
//	
//	@Override
//    public boolean existByUsername(String username) {
//        return userRepository.existsByUsername(username);
//    }
//
//    @Override
//    public boolean existByEmail(String email) {
//        return userRepository.existsByEmail(email);
//    }
//	
////	public Optional<UserEntity> findByUsername(String username) {
////        return userRepository.findByUsername(username);
////    }
//
////	@Override
////	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
////		// Kiểm tra xem user có tồn tại trong database không?
////        UserEntity user = userRepository.findUserByName(username);
////        if (user == null) {
////            throw new UsernameNotFoundException(username);
////        }
////        return new CustomUserDetails(user);
////	}
//	
//}
