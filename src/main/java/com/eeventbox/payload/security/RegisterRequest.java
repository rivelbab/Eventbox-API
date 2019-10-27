package com.eeventbox.payload.security;
/**
 * ================================================
 * Represents all user's info required to register
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterRequest {

	@NotBlank
	@Size(min = 2, max = 40)
	private String username;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@Size(min = 6, max = 20)
	private String password;
}
