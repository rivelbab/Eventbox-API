package com.eeventbox.service.security;

import com.eeventbox.exception.AppException;
import com.eeventbox.model.user.Role;
import com.eeventbox.model.user.RoleName;
import com.eeventbox.model.security.VerificationToken;
import com.eeventbox.model.user.User;
import com.eeventbox.payload.api.ApiResponse;
import com.eeventbox.payload.security.*;
import com.eeventbox.payload.security.UserAvailabilityResponse;
import com.eeventbox.payload.user.UserSummaryResponse;
import com.eeventbox.repository.RoleRepository;
import com.eeventbox.repository.UserRepository;
import com.eeventbox.repository.VerificationTokenRepository;
import com.eeventbox.service.mail.SendingMailService;
import com.eeventbox.utils.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

	private static final String FORGOT_PASSWORD_VERIF_URL = "http://localhost:5005/v1/auth/reset_password?code=";
	private static final String REGISTER_VERIF_URL = "http://localhost:5005/v1/auth/verify_email?code=";
	private static final  String REDIRECT_INVALID_TOKEN_URL = "http://localhost:3005/invalid_token";
	private static final String REDIRECT_TO_LOGIN_URL = "http://localhost:3005/login";

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

	/**
	 * =================================================
	 * 				register a new user
	 * =================================================
	 */
	public ResponseEntity<?> register(RegisterRequest rq) {

		if (userRepository.existsByEmail(rq.getEmail())) {
			return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"), HttpStatus.FOUND);
		}

		if (userRepository.existsByUsername(rq.getUsername())) {
			return new ResponseEntity(new ApiResponse(false, "Username is already taken!"), HttpStatus.FOUND);
		}

		Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User Role not set."));

		User user = new User();

		user.setPassword(rq.getPassword());
		user.setEmail(rq.getEmail());
		user.setUsername(rq.getUsername());
		user.setActive(false);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Collections.singleton(userRole));

		User savedUser = userRepository.save(user);

		// ============ prepare user email verification =======
		VerificationToken verificationToken = verificationTokenRepository.findByUserEmail(user.getEmail());

		if (verificationToken == null) {
			verificationToken = new VerificationToken();
			verificationToken.setUser(user);
			verificationTokenRepository.save(verificationToken);
		}

		String subject = "Verify four email";
		String verifUrl = REGISTER_VERIF_URL + verificationToken.getToken();
		String emailMsg = "Please ! Confirm your account ";

		sendingMailService.sendVerificationMail(user.getEmail(), verifUrl, subject, emailMsg);

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/v1/users/{username}")
				.buildAndExpand(savedUser.getUsername()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "Success, Please check your mail to confirm your account."));
	}
	/**
	 * =================================================
	 * 				user login
	 * =================================================
	 */
	public ResponseEntity<?> login(LoginRequest lq) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						lq.getUsernameOrEmail(),
						lq.getPassword()
				)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);
		Long userId = tokenProvider.getUserIdFromJWT(jwt);
		User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not exist."));


		return ResponseEntity.ok(new UserSummaryResponse(user, jwt));
	}
	/**
	 * =================================================
	 * 				user password forgot
	 * =================================================
	 */
	public ResponseEntity<?> forgotPassword (ForgotPasswordRequest fq) {

		if (!userRepository.existsByEmail(fq.getEmail())) {
			return new ResponseEntity(new ApiResponse(false, "We didn't find an account for that e-mail address."), HttpStatus.BAD_REQUEST);
		}

		User user = userRepository.findByEmail(fq.getEmail());

		user.setResetToken(UUID.randomUUID().toString());
		userRepository.save(user);

		// ======== prepare forgot password email =======
		String subject = "Reset your password";
		String verifUrl = FORGOT_PASSWORD_VERIF_URL + user.getResetToken();
		String emailMsg = "Please ! Reset your password";

		sendingMailService.sendVerificationMail(fq.getEmail(), verifUrl, subject, emailMsg);

		return new ResponseEntity(new ApiResponse(true, "A password reset link has been sent to your email"), HttpStatus.OK);
	}
	/**
	 * =================================================
	 * 				user reset password
	 * =================================================
	 */
	public ResponseEntity<?> resetPassword (ResetPasswordRequest rq) {

		Optional<User> user = userRepository.findByResetToken(rq.getToken());

		if (!user.isPresent()) {
			return new ResponseEntity(new ApiResponse(false, "Oops!  This is an invalid password reset link."), HttpStatus.BAD_REQUEST);
		}

		User concernedUser = user.get();

		concernedUser.setPassword(passwordEncoder.encode(rq.getPassword()));
		concernedUser.setResetToken(null);

		userRepository.save(concernedUser);

		return new ResponseEntity(new ApiResponse(true, "You have successfully reset your password.  You may now login."), HttpStatus.OK);
	}
	/**
	 * =================================================
	 * 				new user verif email
	 * =================================================
	 */
	public ResponseEntity<?> verifyEmail(String token) {

		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

		if (verificationToken == null) {
			return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION,REDIRECT_INVALID_TOKEN_URL).build();
		}

		if (verificationToken.getExpiredDateTime().isBefore(LocalDateTime.now())) {
			return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION,REDIRECT_INVALID_TOKEN_URL).build();
		}

		verificationToken.setConfirmedDateTime(LocalDateTime.now());
		verificationToken.setStatus(VerificationToken.STATUS_VERIFIED);
		verificationToken.getUser().setActive(true);
		verificationTokenRepository.save(verificationToken);

		return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION,REDIRECT_TO_LOGIN_URL).build();
	}
	/**
	 * ==================================================
	 * 			User check identity availability
	 * 	================================================
	 */
	public UserAvailabilityResponse checkUsernameAvailability(String username){

		Boolean isAvailable = !userRepository.existsByUsername(username);
		return new UserAvailabilityResponse(isAvailable);
	}

	public UserAvailabilityResponse checkEmailAvailability(String email){

		Boolean isAvailable = !userRepository.existsByEmail(email);
		return new UserAvailabilityResponse(isAvailable);
	}
}
