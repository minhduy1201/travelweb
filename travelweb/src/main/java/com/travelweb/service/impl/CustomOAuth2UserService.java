package com.travelweb.service.impl;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travelweb.details.CustomOAuth2User;
import com.travelweb.details.UserDetailsImpl;
import com.travelweb.entity.UserEntity;
import com.travelweb.entity.RoleEntity;
import com.travelweb.enumEntity.ERole;
import com.travelweb.repository.UserRepository;
import com.travelweb.repository.RoleRepository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
	    OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

	    // Lấy thông tin từ Google
	    Map<String, Object> attributes = oauth2User.getAttributes();
	    String email = (String) attributes.get("email");
	    String name = (String) attributes.get("name"); // Tên người dùng (nếu cần)

	    // Kiểm tra xem tài khoản đã tồn tại hay chưa
	    Optional<UserEntity> optionalUser = userRepository.findByEmail(email);

	    UserEntity user;
	    if (optionalUser.isPresent()) {
	        // Tài khoản đã tồn tại
	        user = optionalUser.get();
	    } else {
	        // Tài khoản chưa tồn tại, tạo mới
	        user = new UserEntity();
	        user.setEmail(email);
	        user.setUsername(email); // Sử dụng email làm username
	        user.setPassword(""); // OAuth không cần mật khẩu

	        // Gán vai trò mặc định
	        RoleEntity role = roleRepository.findByName(ERole.ROLE_USER)
	                .orElseThrow(() -> new RuntimeException("Error: Không tìm thấy Role"));
	        user.setRoles(Set.of(role));

	        // Lưu tài khoản vào cơ sở dữ liệu
	        user = userRepository.save(user);

	        // Gửi thông báo chào mừng hoặc yêu cầu bổ sung thông tin
	        sendWelcomeEmail(user.getEmail(), name); // Giả sử bạn có logic gửi email
	    }

	    // Trả về OAuth2User với thông tin tài khoản
	    return new CustomOAuth2User(user, attributes);
	}

	/**
	 * Gửi email chào mừng hoặc yêu cầu bổ sung thông tin
	 */
	private void sendWelcomeEmail(String email, String name) {
	    // Ví dụ logic gửi email
	    String subject = "Chào mừng bạn đến với hệ thống của chúng tôi!";
	    String body = String.format("Xin chào %s,\n\nCảm ơn bạn đã đăng nhập bằng tài khoản Google.\n" +
	            "Nếu cần hỗ trợ, vui lòng liên hệ với chúng tôi.\n\nTrân trọng,\nĐội ngũ hỗ trợ.", name);

	    // Gọi service gửi email
	    System.out.println("Gửi email tới: " + email);
	    System.out.println("Tiêu đề: " + subject);
	    System.out.println("Nội dung: " + body);
	    // Thay đoạn này bằng logic gửi email thực tế (nếu đã tích hợp).
	}

}
