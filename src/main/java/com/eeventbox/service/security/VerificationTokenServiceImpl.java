package com.eeventbox.service.security;

import com.eeventbox.model.security.VerificationToken;
import com.eeventbox.model.user.User;
import com.eeventbox.payload.response.ResponseMessage;
import com.eeventbox.payload.response.ResponseType;
import com.eeventbox.repository.UserRepository;
import com.eeventbox.repository.VerificationTokenRepository;
import com.eeventbox.service.mail.SendingMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService{

	private UserRepository userRepository;
	private VerificationTokenRepository verificationTokenRepository;
	private SendingMailService sendingMailService;

	@Autowired
	public VerificationTokenServiceImpl(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, SendingMailService sendingMailService){
		this.userRepository = userRepository;
		this.verificationTokenRepository = verificationTokenRepository;
		this.sendingMailService = sendingMailService;
	}

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

	public ResponseMessage verifyEmail(String token) {

		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

		if (verificationToken == null) {
			return ResponseMessage.builder().message("Invalid token !").type(ResponseType.CLIENT_ERROR).build();
		}

		if (verificationToken.getExpiredDateTime().isBefore(LocalDateTime.now())) {
			return ResponseMessage.builder().message("expired token !").type(ResponseType.CLIENT_ERROR).build();
		}

		verificationToken.setConfirmedDateTime(LocalDateTime.now());
		verificationToken.setStatus(VerificationToken.STATUS_VERIFIED);
		verificationToken.getUser().setActive(true);
		verificationTokenRepository.save(verificationToken);

		return ResponseMessage.builder().message("You have successfully verified your mail address.").type(ResponseType.SUCCESSFUL).build();
	}
}
