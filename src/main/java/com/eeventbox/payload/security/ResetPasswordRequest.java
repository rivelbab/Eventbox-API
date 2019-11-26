package com.eeventbox.payload.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * ===============================================
 * Contains user new password and a reset token.
 * Created by Rivelbab on 28/10/2019 at Nanterre U.
 * ===============================================
 */
@Getter
@Setter
public class ResetPasswordRequest {

	@NotBlank
	public String token;

	@NotBlank
	public String password;
}
