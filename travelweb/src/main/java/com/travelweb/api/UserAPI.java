//package com.travelweb.api;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.travelweb.dto.UserDTO;
//import com.travelweb.entity.UserEntity;
//import com.travelweb.login.LoginRequest;
//import com.travelweb.service.IUserService;
//
//@CrossOrigin
//@RestController
//public class UserAPI {
//	@Autowired
//	private IUserService userService;
//	
////	@Autowired
////    private AuthenticationManager authenticationManager;
//	
//	@PostMapping(value = "/register")
//	public UserDTO createUser(@RequestBody UserDTO dto) {
//		return userService.createUser(dto);
//	}
//	
//	@GetMapping
//	public List<UserEntity> getUser() {
//		return userService.getUsers();
//	}
//	
//	@GetMapping(value = "/user/{id}")
//	public UserEntity getUserById(@RequestBody UserDTO dto, @PathVariable("id") long id) {
//		dto.setId(id);
//		return userService.getUserById(id);
//	}
//	
//	@PutMapping(value = "/user/{id}")
//	public UserDTO updateUser(@RequestBody UserDTO dto, @PathVariable("id") long id) {
//		dto.setId(id);
//		return userService.updateUser(id, dto);
//	}
//	
////	@PostMapping("/login")
////    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
////        try {
////            // Xác thực người dùng
////            Authentication authentication = authenticationManager.authenticate(
////                    (Authentication) new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
////            );
////            SecurityContextHolder.getContext().setAuthentication(authentication);  // Lưu thông tin xác thực
////
////            return ResponseEntity.ok("Đăng nhập thành công!");
////        } catch (BadCredentialsException e) {
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tên đăng nhập hoặc mật khẩu");
////        }
////    }
//
//}
