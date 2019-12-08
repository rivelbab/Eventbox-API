package com.eeventbox.service.user;

import com.eeventbox.exception.AppException;
import com.eeventbox.model.user.User;
import com.eeventbox.payload.user.UserRequest;
import com.eeventbox.repository.UserRepository;
import com.eeventbox.service.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
	JwtTokenProvider tokenProvider;

	/**
	 * =======================================================
	 * 	Return list of all user, utils to share event to user
	 * 	======================================================
	 */
	public List<User> findUsers() {
		return userRepository.findAll();
	}
	/**
	 * =============================================================
	 * 	Return a user of the given username, needed to build profile
	 * 	============================================================
	 */
	public Optional<User> findUserByUsername(String username) {

		return userRepository.findByUsername(username);
	}
	/**
	 * =======================================================
	 * 	Update the entire user entity using PUT Http method
	 * 	======================================================
	 */
	public Boolean updateUser(String username, UserRequest userRequest) {

		if (!userRepository.existsByUsername(username)) {
			return false;
		}

		User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException("User not exist."));

		user.setUsername(userRequest.getUsername());
		user.setLastName(userRequest.getLastName());
		user.setFirstName(userRequest.getFirstName());
		user.setInterests(userRequest.getInterests());
		user.setBirthday(userRequest.getBirthday());

		userRepository.save(user);

		return true;
	}
	/**
	 * ===================================================
	 * 	Update partially a user using PATCH Http method
	 * 	==================================================
	 */
	public void updateUser(User user) {
		userRepository.save(user);
	}
	/**
	 * ===================================================================
	 * 	Delete a user, a token is required to avoid deleting someone else.
	 * 	==================================================================
	 */
	public Boolean deleteUser(String token) {

		Long userId = tokenProvider.getUserIdFromJWT(token);
		if (!userRepository.existsById(userId)) {return false;}
		User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not exist."));
		userRepository.delete(user);
		return true;
	}
}
