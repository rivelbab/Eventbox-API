package com.eeventbox.controller;
/**
 * ================================================
 * Contains all endpoints for the user resources
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */

import com.eeventbox.payload.response.Response;
import com.eeventbox.payload.user.RegisterRequest;
import com.eeventbox.service.security.VerificationTokenService;
import com.eeventbox.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private VerificationTokenService verificationTokenService;

	@RequestMapping(value="/register", method = RequestMethod.POST)
	public ResponseEntity<Response> registerUser(@Valid @RequestBody RegisterRequest rq) {
		return userService.addUser(rq).toResponseEntity();
	}

	@RequestMapping(value="/verify-email", method= {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Response> confirmUserAccount(@RequestParam("code") String code) {
		return verificationTokenService.verifyEmail(code).toResponseEntity();
	}
}
