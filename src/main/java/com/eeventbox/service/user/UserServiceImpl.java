package com.eeventbox.service.user;

import com.eeventbox.exception.AppException;
import com.eeventbox.model.role.Role;
import com.eeventbox.model.role.RoleName;
import com.eeventbox.model.user.User;
import com.eeventbox.payload.response.ApiResponse;
import com.eeventbox.payload.security.RegisterRequest;
import com.eeventbox.repository.RoleRepository;
import com.eeventbox.repository.UserRepository;
import com.eeventbox.service.security.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private VerificationTokenService verificationTokenService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public ResponseEntity<?> addUser(RegisterRequest rq) {

		if (userRepository.existsByEmail(rq.getEmail())) {
			return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByUsername(rq.getUsername())) {
			return new ResponseEntity(new ApiResponse(false, "Username is already taken!"), HttpStatus.BAD_REQUEST);
		}

		Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User Role not set."));

		User user = new User(rq.getUsername(), rq.getEmail(), rq.getPassword());

		user.setActive(false);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Collections.singleton(userRole));

		User savedUser = userRepository.save(user);

		verificationTokenService.createVerification(user.getEmail());

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/v1/users/{username}")
				.buildAndExpand(savedUser.getUsername()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "Success, Please check your mail to confirm your account."));
	}
}
