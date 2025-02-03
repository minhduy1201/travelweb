package com.travelweb.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.travelweb.details.UserDetailsImpl;
import com.travelweb.dto.NewDTO;
import com.travelweb.dto.UserDTO;
import com.travelweb.entity.RoleEntity;
import com.travelweb.entity.UserEntity;
import com.travelweb.enumEntity.ERole;
import com.travelweb.jwt.JwtUtils;
import com.travelweb.payload.request.LoginRequest;
import com.travelweb.payload.request.SignupRequest;
import com.travelweb.payload.response.JwtResponse;
import com.travelweb.payload.response.MessageResponse;
import com.travelweb.repository.FavoriteRepository;
import com.travelweb.repository.NewRepository;
import com.travelweb.repository.RoleRepository;
import com.travelweb.repository.UserRepository;
import com.travelweb.service.impl.UserDetailsServiceImpl;

import io.jsonwebtoken.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	FavoriteRepository favoriteRepository;
	
	@Autowired
	private NewRepository newRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
    private Cloudinary cloudinary;
	
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	private Map<String, String> verificationCodes = new HashMap<>();

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(
				new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username đã được dùng!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email đã được dùng!"));
		}

		// Create new user's account
		UserEntity user = new UserEntity(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<RoleEntity> roles = new HashSet<>();

		if (strRoles == null) {
			RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: không tìm thấy Role."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					RoleEntity adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: không tìm thấy Role Role."));
					roles.add(adminRole);

					break;
				case "mod":
					RoleEntity modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: không tìm thấy Role Role."));
					roles.add(modRole);

					break;
				default:
					RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: không tìm thấy Role Role."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("Đăng kí thành công!"));
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser(HttpServletRequest request) {
	    String authHeader = request.getHeader("Authorization");
	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        String token = authHeader.substring(7);
	        // Nếu cần, có thể lưu token vào danh sách blocklist
	    }
	    return ResponseEntity.ok(new MessageResponse("Đăng xuất thành công!"));
	}
	
	@GetMapping("/oauth2/callback")
	public ResponseEntity<?> handleGoogleLogin(OAuth2AuthenticationToken authentication) {
	    UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByOAuth2Authentication(authentication);
	    String jwt = jwtUtils.generateJwtToken(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

	    return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), 
	                                             userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())));
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
	    Optional<UserEntity> userOpt = userRepository.findById(userId);
	    if (!userOpt.isPresent()) {
	        return ResponseEntity.badRequest().body(new MessageResponse("Người dùng không tồn tại"));
	    }

	    UserEntity user = userOpt.get();
	    UserDTO userDTO = new UserDTO();
	    userDTO.setUserName(user.getUsername());
	    userDTO.setEmail(user.getEmail());
	    userDTO.setAvatar(user.getAvatar());

	    return ResponseEntity.ok(userDTO);
	}
	
	@PostMapping("/users/{userId}/avatar")
	public ResponseEntity<?> updateAvatar(@PathVariable Long userId, @RequestParam("avatar") MultipartFile avatarFile) throws java.io.IOException {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Người dùng không tồn tại"));
        }

        UserEntity user = userOpt.get();
        try {
            // Upload ảnh lên Cloudinary
            Map<?, ?> uploadResult = cloudinary.uploader().upload(avatarFile.getBytes(), ObjectUtils.emptyMap());
            String avatarUrl = uploadResult.get("secure_url").toString();

            // Cập nhật avatar cho user
            user.setAvatar(avatarUrl);
            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("Cập nhật ảnh đại diện thành công!"));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Lỗi khi tải ảnh lên Cloudinary"));
        }
    }
	
	@GetMapping("/{userId}/posts")
	public ResponseEntity<List<NewDTO>> getUserPosts(@PathVariable Long userId) {
	    List<NewDTO> posts = newRepository.findByCreatedBy(userId)
	        .stream().map(post -> new NewDTO(post)).collect(Collectors.toList());
	    return ResponseEntity.ok(posts);
	}


	@GetMapping("/{userId}/favorites")
	public ResponseEntity<List<NewDTO>> getFavoritePosts(@PathVariable Long userId) {
	    List<NewDTO> favoritePosts = favoriteRepository.findByUserId(userId)
	        .stream().map(fav -> new NewDTO(fav.getPost())).collect(Collectors.toList());
	    return ResponseEntity.ok(favoritePosts);
	}
	
	@PutMapping("/users/{userId}/update-username")
	public ResponseEntity<?> updateUserName(@PathVariable Long userId, @RequestBody Map<String, String> body) {
	    Optional<UserEntity> userOpt = userRepository.findById(userId);
	    if (!userOpt.isPresent()) {
	        return ResponseEntity.badRequest().body(new MessageResponse("Người dùng không tồn tại"));
	    }
	    UserEntity user = userOpt.get();
	    user.setUsername(body.get("username"));
	    userRepository.save(user);
	    return ResponseEntity.ok(new MessageResponse("Cập nhật tên người dùng thành công"));
	}
	
	@PutMapping("/users/{userId}/update-password")
	public ResponseEntity<?> updatePassword(@PathVariable Long userId, @RequestBody Map<String, String> body) {
	    Optional<UserEntity> userOpt = userRepository.findById(userId);
	    if (!userOpt.isPresent()) {
	        return ResponseEntity.badRequest().body(new MessageResponse("Người dùng không tồn tại"));
	    }
	    UserEntity user = userOpt.get();
	    user.setPassword(encoder.encode(body.get("password"))); // Mã hoá mật khẩu
	    userRepository.save(user);
	    return ResponseEntity.ok(new MessageResponse("Cập nhật mật khẩu thành công"));
	}




}
