package com.eeventbox.payload.user;
/**
 * ================================================
 * Represents all user's info required to login
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

	@NotBlank
	private String usernameOrEmail;

	@NotBlank
	private String password;
}
