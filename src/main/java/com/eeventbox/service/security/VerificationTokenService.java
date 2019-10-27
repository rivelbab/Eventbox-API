package com.eeventbox.service.security;
/**
 * ================================================
 * Contains all useful operations for security
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */

import com.eeventbox.payload.response.ResponseMessage;

public interface VerificationTokenService {

	void createVerification(String email);
	ResponseMessage verifyEmail(String token);
}
