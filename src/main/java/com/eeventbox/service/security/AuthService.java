package com.eeventbox.service.security;
/**
 * ================================================
 * Contains all useful operations for security
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.payload.api.ApiResponse;
import com.eeventbox.payload.security.ForgotPasswordRequest;
import com.eeventbox.payload.security.LoginRequest;
import com.eeventbox.payload.security.RegisterRequest;
import com.eeventbox.payload.security.ResetPasswordRequest;
import com.eeventbox.payload.security.UserAvailabilityResponse;
import com.eeventbox.payload.user.UserSummaryResponse;

public interface AuthService {

	ApiResponse register(RegisterRequest rq);

	UserSummaryResponse login(LoginRequest lq);

	ApiResponse resetPassword (ResetPasswordRequest rp);

	ApiResponse forgotPassword (ForgotPasswordRequest fq);

	Boolean verifyEmail(String token);

	UserAvailabilityResponse checkUsernameAvailability(String username);

	UserAvailabilityResponse checkEmailAvailability(String email);
}
