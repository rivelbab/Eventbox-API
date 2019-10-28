package com.eeventbox.service.security;

import com.eeventbox.exception.AppException;
import com.eeventbox.model.role.Role;
import com.eeventbox.model.role.RoleName;
import com.eeventbox.model.security.VerificationToken;
import com.eeventbox.model.user.User;
import com.eeventbox.payload.response.ApiResponse;
import com.eeventbox.payload.security.JwtAuthResponse;
import com.eeventbox.payload.security.LoginRequest;
import com.eeventbox.payload.security.RegisterRequest;
import com.eeventbox.repository.RoleRepository;
import com.eeventbox.repository.UserRepository;
import com.eeventbox.repository.VerificationTokenRepository;
import com.eeventbox.service.mail.SendingMailService;
import com.eeventbox.utils.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	@Autowired
	private SendingMailService sendingMailService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider tokenProvider;

	public ResponseEntity<?> register(RegisterRequest rq) {

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

		createVerification(user.getEmail());

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/v1/users/{username}")
				.buildAndExpand(savedUser.getUsername()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "Success, Please check your mail to confirm your account."));
	}

	public ResponseEntity<?> login(LoginRequest lq) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						lq.getUsernameOrEmail(),
						lq.getPassword()
				)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);

		return ResponseEntity.ok(new JwtAuthResponse(jwt));
	}

	public ResponseEntity<?> forgotPassword (String email) {

		if (!userRepository.existsByEmail(email)) {
			return new ResponseEntity(new ApiResponse(false, "We didn't find an account for that e-mail address."), HttpStatus.BAD_REQUEST);
		}

		User user = userRepository.findByEmail(email);

		user.setResetToken(UUID.randomUUID().toString());
		userRepository.save(user);

		sendingMailService.sendVerificationMail(email, user.getResetToken());

		return new ResponseEntity(new ApiResponse(true, "A password reset link has been sent to your email"), HttpStatus.OK);
	}

	public ResponseEntity<?> resetPassword (String resetToken, String pwd) {

		Optional<User> user = userRepository.findByResetToken(resetToken);

		if (!user.isPresent()) {
			return new ResponseEntity(new ApiResponse(false, "Oops!  This is an invalid password reset link."), HttpStatus.BAD_REQUEST);
		}

		User concernedUser = user.get();

		concernedUser.setPassword(passwordEncoder.encode(pwd));
		concernedUser.setResetToken(null);

		userRepository.save(concernedUser);

		return new ResponseEntity(new ApiResponse(true, "You have successfully reset your password.  You may now login."), HttpStatus.OK);
	}

	public void createVerification(String email) {

		User user = userRepository.findByEmail(email);

		if (user == null) {
			user = new User();
			user.setEmail(email);
			userRepository.save(user);
		}

		VerificationToken verificationToken = verificationTokenRepository.findByUserEmail(email);

		if (verificationToken == null) {
			verificationToken = new VerificationToken();
			verificationToken.setUser(user);
			verificationTokenRepository.save(verificationToken);
		}

		sendingMailService.sendVerificationMail(email, verificationToken.getToken());
	}

	public ResponseEntity<?> verifyEmail(String token) {

		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

		if (verificationToken == null) {
			return new ResponseEntity(new ApiResponse(false, "Invalid token !"), HttpStatus.BAD_REQUEST);
		}

		if (verificationToken.getExpiredDateTime().isBefore(LocalDateTime.now())) {
			return new ResponseEntity(new ApiResponse(false, "Expired token !"), HttpStatus.BAD_REQUEST);
		}

		verificationToken.setConfirmedDateTime(LocalDateTime.now());
		verificationToken.setStatus(VerificationToken.STATUS_VERIFIED);
		verificationToken.getUser().setActive(true);
		verificationTokenRepository.save(verificationToken);

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/api/users/{username}")
				.buildAndExpand(verificationToken.getUser().getUsername()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "You are successfully verify your email."));
	}
}
