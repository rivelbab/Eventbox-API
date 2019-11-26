package com.eeventbox.payload.security;
/**
 * ==================================================
 * Contains user email to get a reset password's link
 * Created by Rivelbab on 28/10/2019 at Nanterre U.
 * ==================================================
 */
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class ForgotPasswordRequest {

	@NotBlank
	@Email
	private String email;
}


