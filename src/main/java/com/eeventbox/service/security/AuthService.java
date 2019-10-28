package com.eeventbox.service.security;
/**
 * ================================================
 * Contains all useful operations for security
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.payload.security.LoginRequest;
import com.eeventbox.payload.security.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

	ResponseEntity<?> register(RegisterRequest rq);

	ResponseEntity<?> login(LoginRequest lq);

	ResponseEntity<?> resetPassword (String resetToken, String pwd);

	ResponseEntity<?> forgotPassword (String email);

	void createVerification(String email);

	ResponseEntity<?> verifyEmail(String token);
}
