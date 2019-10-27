package com.eeventbox.service.security;

import com.eeventbox.model.security.VerificationToken;
import com.eeventbox.model.user.User;
import com.eeventbox.payload.response.ApiResponse;
import com.eeventbox.repository.UserRepository;
import com.eeventbox.repository.VerificationTokenRepository;
import com.eeventbox.service.mail.SendingMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	@Autowired
	private SendingMailService sendingMailService;

	public void createVerification(String email) {

		User user = userRepository.findByEmail(email);

		if (user == null) {
			user = new User();
			user.setEmail(email);
			userRepository.save(user);
		}

		VerificationToken verificationToken = verificationTokenRepository.findByUserEmail(email);

		if (verificationToken == null) {
			verificationToken = new VerificationToken();
			verificationToken.setUser(user);
			verificationTokenRepository.save(verificationToken);
		}

		sendingMailService.sendVerificationMail(email, verificationToken.getToken());
	}

	public ResponseEntity<?> verifyEmail(String token) {

		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

		if (verificationToken == null) {
			return new ResponseEntity(new ApiResponse(false, "Invalid token !"), HttpStatus.BAD_REQUEST);
		}

		if (verificationToken.getExpiredDateTime().isBefore(LocalDateTime.now())) {
			return new ResponseEntity(new ApiResponse(false, "Expired token !"), HttpStatus.BAD_REQUEST);
		}

		verificationToken.setConfirmedDateTime(LocalDateTime.now());
		verificationToken.setStatus(VerificationToken.STATUS_VERIFIED);
		verificationToken.getUser().setActive(true);
		verificationTokenRepository.save(verificationToken);

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/api/users/{username}")
				.buildAndExpand(verificationToken.getUser().getUsername()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "You are successfully verify your email."));
	}
}
