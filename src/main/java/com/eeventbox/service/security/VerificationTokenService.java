package com.eeventbox.service.security;
/**
 * ================================================
 * Contains all useful operations for security
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import org.springframework.http.ResponseEntity;

public interface VerificationTokenService {

	void createVerification(String email);
	ResponseEntity<?> verifyEmail(String token);
}
