package com.eeventbox.controller;
/**
 * ===================================================
 * Contains all endpoints for the user authentication
 * Created by Rivelbab on 27/10/2019 at Nanterre U.
 * ===================================================
 */
import com.eeventbox.payload.security.JwtAuthResponse;
import com.eeventbox.payload.security.LoginRequest;
import com.eeventbox.payload.security.RegisterRequest;
import com.eeventbox.utils.security.JwtTokenProvider;
import com.eeventbox.service.security.VerificationTokenService;
import com.eeventbox.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private VerificationTokenService verificationTokenService;

	@RequestMapping(value="/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsernameOrEmail(),
						loginRequest.getPassword()
				)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);

		return ResponseEntity.ok(new JwtAuthResponse(jwt));
	}

	@RequestMapping(value="/register", method = RequestMethod.POST)
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
		return userService.addUser(registerRequest);
	}

	@RequestMapping(value="/verify-email", method= {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<?> confirmUserAccount(@RequestParam("code") String code) {
		return verificationTokenService.verifyEmail(code);
	}
}
