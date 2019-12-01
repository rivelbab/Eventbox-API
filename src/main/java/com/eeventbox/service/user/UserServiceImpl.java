package com.eeventbox.service.user;

import com.eeventbox.exception.AppException;
import com.eeventbox.model.user.User;
import com.eeventbox.model.user.UserPrincipal;
import com.eeventbox.model.utility.Interest;
import com.eeventbox.payload.api.ApiResponse;
import com.eeventbox.payload.user.UserProfileRequest;
import com.eeventbox.payload.user.UserProfileResponse;
import com.eeventbox.payload.user.UserSummaryResponse;
import com.eeventbox.repository.UserRepository;
import com.eeventbox.service.security.JwtAuthFilter;
import com.eeventbox.utils.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtTokenProvider tokenProvider;
	@Autowired
	JwtAuthFilter authFilter;
	@Autowired
	CustomUserDetailsService customUserDetailsService;

	public UserProfileResponse getCurrentUser(HttpServletRequest request) {

		String jwt = authFilter.getJwtFromRequest(request);
		if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
			Long userId = tokenProvider.getUserIdFromJWT(jwt);
			UserDetails userDetails = customUserDetailsService.loadUserById(userId);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User not exist."));

			return new UserProfileResponse(user);
		}
		return null;
	}

	public ResponseEntity<?> showUserProfile(String username) {

		if (!userRepository.existsByUsername(username)) {
			return new ResponseEntity(new ApiResponse(false, "User don't exist !"), HttpStatus.BAD_REQUEST);
		}

		User user = userRepository.findByUsername(username);

		UserProfileResponse userProfileResponse = new UserProfileResponse(user);

		return ResponseEntity.ok(userProfileResponse);
	}

	public ResponseEntity<?> updateUserProfile(UserProfileRequest upq) {

		if (!userRepository.existsByUsername(upq.getUsername())) {
			return new ResponseEntity(new ApiResponse(false, "User don't exist !"), HttpStatus.BAD_REQUEST);
		}

		User user = userRepository.findById(upq.getId()).orElseThrow(() -> new AppException("User not exist."));

		user.setUsername(upq.getUsername());
		user.setLastName(upq.getLastName());
		user.setFirstName(upq.getFirstName());
		user.setInterests(upq.getInterests());
		user.setBirthday(upq.getBirthday());

		User updatedUser = userRepository.save(user);

		UserProfileResponse userProfileResponse = new UserProfileResponse(updatedUser);

		return ResponseEntity.ok(userProfileResponse);
	}

	public List<User> listUsers() {
		return userRepository.findAll();
	}

	public List<User> getUsersWithCommonInterest(String username) {

		return getTopMatches(username, 10);
	}

	public void sendFriendRequestTo(String senderUsername, String receiverUsername) {

		if (userRepository.existsByUsername(senderUsername) && userRepository.existsByUsername(receiverUsername)) {

			User sender = userRepository.findByUsername(senderUsername);
			User receiver = userRepository.findByUsername(senderUsername);

			sender.sendFriendRequestTo(receiver);

			userRepository.save(receiver);
			userRepository.save(sender);
		}
	}

	public void receiveFriendRequestFrom(String senderUsername, String receiverUsername) {

		if (userRepository.existsByUsername(senderUsername) && userRepository.existsByUsername(receiverUsername)) {

			User sender = userRepository.findByUsername(senderUsername);
			User receiver = userRepository.findByUsername(senderUsername);

			sender.receiveFriendRequestFrom(sender);

			userRepository.save(receiver);
			userRepository.save(sender);
		}
	}

	public void acceptFriend(String senderUsername, String receiverUsername) {

		if (userRepository.existsByUsername(senderUsername) && userRepository.existsByUsername(receiverUsername)) {

			User sender = userRepository.findByUsername(senderUsername);
			User receiver = userRepository.findByUsername(senderUsername);

			receiver.acceptFriend(sender);

			userRepository.save(receiver);
			userRepository.save(sender);
		}
	}

	public Set<User> getPendingFriendRequests(String username) {

		if (userRepository.existsByUsername(username)) {

			User user = userRepository.findByUsername(username);

			return user.getPendingFriendRequests();
		}

		return null;
	}

	//===================== private methods =========================

	private List<User> getTopMatches(String username, int numberOfTOpMatches) {

		if (numberOfTOpMatches >= getSortedList(username).size()) {
			numberOfTOpMatches = getSortedList(username).size() - 1;
		}

		List<userWithCountOfInterests> topMatchesWithCount = getSortedList(username).subList(
				getSortedList(username).size() - numberOfTOpMatches, getSortedList(username).size());

		Collections.sort(topMatchesWithCount);

		List<User> topMatches = new ArrayList<>();

		for (userWithCountOfInterests each : topMatchesWithCount) {
			topMatches.add(each.getUser());
		}

		return topMatches;
	}

	private List<userWithCountOfInterests> getSortedList(String username) {

		User currentUser = userRepository.findByUsername(username);


		List<User> allUsers = userRepository.findAll();
		List<userWithCountOfInterests> usersWithNumberOfCommonInterests = new ArrayList<>();

		for (User each : allUsers) {

			int commonInterests = getNumberOfCommonInterests(currentUser, each);

			usersWithNumberOfCommonInterests.add(new userWithCountOfInterests(each, commonInterests));
		}

		Collections.sort(usersWithNumberOfCommonInterests);

		return usersWithNumberOfCommonInterests;
	}

	private int getNumberOfCommonInterests(User user1, User user2) {

		int commonInterests = 0;
		Set<Interest> interests1 = user1.getInterests();
		Set<Interest> interests2 = user2.getInterests();

		for (Interest each : interests1) {
			if (interests2.contains(each)) {
				commonInterests++;
			}
		}
		return commonInterests;
	}

}
