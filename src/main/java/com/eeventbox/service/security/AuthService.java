package com.eeventbox.service.security;
/**
 * ================================================
 * Contains all useful operations for security
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.payload.security.ForgotPasswordRequest;
import com.eeventbox.payload.security.LoginRequest;
import com.eeventbox.payload.security.RegisterRequest;
import com.eeventbox.payload.security.ResetPasswordRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

	ResponseEntity<?> register(RegisterRequest rq);

	ResponseEntity<?> login(LoginRequest lq);

	ResponseEntity<?> resetPassword (ResetPasswordRequest rp);

	ResponseEntity<?> forgotPassword (ForgotPasswordRequest fq);

	ResponseEntity<?> verifyEmail(String token);
}
