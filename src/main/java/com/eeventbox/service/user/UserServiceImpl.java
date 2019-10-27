package com.eeventbox.service.user;

import com.eeventbox.model.user.User;
import com.eeventbox.payload.response.ResponseMessage;
import com.eeventbox.payload.response.ResponseType;
import com.eeventbox.payload.user.RegisterRequest;
import com.eeventbox.repository.UserRepository;
import com.eeventbox.service.security.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private VerificationTokenService verificationTokenService;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, VerificationTokenService verificationTokenService) {
		this.userRepository = userRepository;
		this.verificationTokenService = verificationTokenService;
	}

	public ResponseMessage addUser(RegisterRequest rq) {

		User user = new User(rq.getUsername(), rq.getEmail(), rq.getPassword());

		User existingUser = userRepository.findByEmail(user.getEmail());

		ResponseMessage responseMessage;

		if (existingUser != null) {

			responseMessage = ResponseMessage.builder().message("This e-mail address is already in use!").type(ResponseType.CLIENT_ERROR).build();

		} else {
			// Disable user until they click on confirmation link in mail
			user.setActive(false);

			userRepository.save(user);

			verificationTokenService.createVerification(user.getEmail());

			responseMessage = ResponseMessage.builder().message(String.format("Success, Please check your mail %s to confirm your account.", user.getEmail())).type(ResponseType.SUCCESSFUL).build();
		}

		return responseMessage;
	}
}
