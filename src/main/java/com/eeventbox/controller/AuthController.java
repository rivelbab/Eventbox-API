package com.eeventbox.controller;
/**
 * ===================================================
 * Contains all endpoints for the user authentication
 * Created by Rivelbab on 27/10/2019 at Nanterre U.
 * ===================================================
 */
import com.eeventbox.payload.security.ForgotPasswordRequest;
import com.eeventbox.payload.security.LoginRequest;
import com.eeventbox.payload.security.RegisterRequest;
import com.eeventbox.payload.security.ResetPasswordRequest;
import com.eeventbox.payload.user.UserAvailabilityResponse;
import com.eeventbox.service.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@RequestMapping(value="/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

		return authService.login(loginRequest);
	}

	@RequestMapping(value="/register", method = RequestMethod.POST)
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {

		return authService.register(registerRequest);
	}

	@RequestMapping(value="/verify_email", method= {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<?> confirmUserAccount(@RequestParam("code") String code) {

		return authService.verifyEmail(code);
	}

	@RequestMapping(value = "/forgot_password", method= RequestMethod.POST)
	public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest fq) {

		return  authService.forgotPassword(fq);
	}

	@RequestMapping(value = "/reset_password", method= RequestMethod.POST)
	public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest rq) {

		return authService.resetPassword(rq);
	}

	@GetMapping("/checkUsernameAvailability")
	public UserAvailabilityResponse checkUsernameAvailability(@RequestParam(value = "username") String username) {
		return authService.checkUsernameAvailability(username);
	}

	@GetMapping("/checkEmailAvailability")
	public UserAvailabilityResponse checkEmailAvailability(@RequestParam(value = "email") String email) {
		return authService.checkEmailAvailability(email);
	}
}
