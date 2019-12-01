package com.eeventbox.controller;
/**
 * ================================================
 * Contains all endpoints for the user resources
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.payload.user.UserProfileResponse;
import com.eeventbox.payload.user.UserSummaryResponse;
import com.eeventbox.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/user/me")
	@PreAuthorize("hasRole('ROLE_USER')")
	public UserProfileResponse getCurrentUser(HttpServletRequest request) {
		return userService.getCurrentUser(request);
	}

	@GetMapping("/users/{username}")
	public ResponseEntity<?> showUserProfile(@PathVariable(value = "username") String username) {
		return userService.showUserProfile(username);
	}


}
