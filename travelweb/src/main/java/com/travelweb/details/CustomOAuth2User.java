package com.travelweb.details;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.travelweb.entity.UserEntity;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User extends DefaultOAuth2User {
	private final UserEntity userEntity;

    public CustomOAuth2User(UserEntity userEntity, Map<String, Object> attributes) {
        super(userEntity.getRoles().stream()
              .map(role -> (GrantedAuthority) () -> role.getName().name()).toList(), 
              attributes, "email");
        this.userEntity = userEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }
}

