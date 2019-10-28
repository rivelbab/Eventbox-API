package com.eeventbox.service.user;

import com.eeventbox.exception.AppException;
import com.eeventbox.model.role.Role;
import com.eeventbox.model.role.RoleName;
import com.eeventbox.model.user.User;
import com.eeventbox.payload.response.ApiResponse;
import com.eeventbox.payload.security.RegisterRequest;
import com.eeventbox.repository.RoleRepository;
import com.eeventbox.repository.UserRepository;
import com.eeventbox.service.security.AuthService;
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
	private AuthService authService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
}
