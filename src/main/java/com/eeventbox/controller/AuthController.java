package com.eeventbox.controller;
/**
 * ===================================================
 * Contains all endpoints for the user authentication
 * Created by Rivelbab on 27/10/2019 at Nanterre U.
 * ===================================================
 */
import com.eeventbox.payload.api.ApiResponse;
import com.eeventbox.payload.security.ForgotPasswordRequest;
import com.eeventbox.payload.security.LoginRequest;
import com.eeventbox.payload.security.RegisterRequest;
import com.eeventbox.payload.security.ResetPasswordRequest;
import com.eeventbox.payload.security.UserAvailabilityResponse;
import com.eeventbox.service.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

	private static final  String REDIRECT_INVALID_TOKEN_URL = "http://localhost:3005/invalid_token";
	private static final String REDIRECT_TO_LOGIN_URL = "http://localhost:3005/login";

	@Autowired
	private AuthService authService;

	@RequestMapping(value="/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

		return ResponseEntity.ok(authService.login(loginRequest));
	}

	@RequestMapping(value="/register", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {

		ApiResponse apiResponse = authService.register(registerRequest);
		if (apiResponse.getSuccess() == false) {
			return new ResponseEntity(apiResponse, HttpStatus.CONFLICT);
		}
		return new ResponseEntity(apiResponse, HttpStatus.CREATED);
	}

	@RequestMapping(value="/verify_email", method= {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<?> confirmUserAccount(@RequestParam("code") String code) {

		if (authService.verifyEmail(code) == false) {
			return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION,REDIRECT_INVALID_TOKEN_URL).build();
		}
		return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION,REDIRECT_TO_LOGIN_URL).build();
	}

	@RequestMapping(value = "/forgot_password", method= RequestMethod.POST)
	public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest fq) {

		ApiResponse apiResponse = authService.forgotPassword(fq);
		if (apiResponse.getSuccess() == false) {
			return new ResponseEntity(apiResponse, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(apiResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/reset_password", method= RequestMethod.POST)
	public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest rq) {

		ApiResponse apiResponse = authService.resetPassword(rq);
		if (apiResponse.getSuccess() == false) {
			return new ResponseEntity(apiResponse, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(apiResponse, HttpStatus.OK);
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
