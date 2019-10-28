package com.eeventbox.controller;
/**
 * ===================================================
 * Contains all endpoints for the user authentication
 * Created by Rivelbab on 27/10/2019 at Nanterre U.
 * ===================================================
 */
import com.eeventbox.payload.security.LoginRequest;
import com.eeventbox.payload.security.RegisterRequest;
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

	@RequestMapping(value="/verify-email", method= {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<?> confirmUserAccount(@RequestParam("code") String code) {

		return authService.verifyEmail(code);
	}
}
