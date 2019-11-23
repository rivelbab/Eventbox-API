package com.eeventbox.controller;
/**
 * ================================================
 * Contains all endpoints for the user resources
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
public class UserController {

	@Autowired
	private UserServiceImpl userService;

}
