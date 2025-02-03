package com.travelweb.service.impl;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travelweb.details.UserDetailsImpl;
import com.travelweb.entity.RoleEntity;
import com.travelweb.entity.UserEntity;
import com.travelweb.enumEntity.ERole;
import com.travelweb.repository.RoleRepository;
import com.travelweb.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
    RoleRepository roleRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<UserEntity> userOptional = userRepository.findByEmail(email);
	    System.out.println("Kết quả truy vấn email '" + email + "': " + userOptional);

	    UserEntity user = userOptional
	            .orElseThrow(() -> new UsernameNotFoundException("không tìm được user với username: " + email));

	    return UserDetailsImpl.build(user);
	}

	@Transactional
    public UserDetails loadUserByOAuth2Authentication(OAuth2AuthenticationToken authentication) {
        OAuth2User oauth2User = authentication.getPrincipal();
        String email = (String) oauth2User.getAttributes().get("email");
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);

        if (!userEntity.isPresent()) {
            // Tạo tài khoản mới nếu không tồn tại
            UserEntity newUser = new UserEntity();
            newUser.setEmail(email);
            newUser.setUsername(email); // Sử dụng email làm username
            newUser.setPassword(""); // Bỏ trống mật khẩu vì sử dụng OAuth2

            // Gán vai trò mặc định cho người dùng mới
            RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: không tìm thấy Role."));
            newUser.setRoles(Set.of(userRole));
            
            userEntity = Optional.of(userRepository.save(newUser));
        }

        return UserDetailsImpl.build(userEntity.get());
    }

	
}
