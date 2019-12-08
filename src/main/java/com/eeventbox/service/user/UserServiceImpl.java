package com.eeventbox.service.user;

import com.eeventbox.exception.AppException;
import com.eeventbox.model.user.User;
import com.eeventbox.payload.user.UserRequest;
import com.eeventbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;

	public List<User> findUsers() {
		return userRepository.findAll();
	}

	public Optional<User> findUserByUsername(String username) {

		return userRepository.findByUsername(username);
	}

	public Boolean updateUser(String username, UserRequest userRequest) {

		if (!userRepository.existsByUsername(username)) {
			//return new ResponseEntity(new ApiResponse(false, "User don't exist !"), HttpStatus.BAD_REQUEST);
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

	public void updateUser(User user) {
		userRepository.save(user);
	}

	public Boolean deleteUser(String email) {
		if (!userRepository.existsByEmail(email)) {
			return false;
		}
		User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException("User not exist."));
		userRepository.delete(user);
		return true;
	}
}
