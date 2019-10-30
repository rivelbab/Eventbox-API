package com.eeventbox.service.user;

import com.eeventbox.model.event.Event;
import com.eeventbox.model.user.User;
import com.eeventbox.model.utils.Interest;
import com.eeventbox.repository.RoleRepository;
import com.eeventbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	public void joinEvent(User user, Event event) {
		user.getAttendingEvents().add(event);
		userRepository.save(user);
	}

	public List<User> getUsersWithCommonInterest(String username) {
		return getTopMatches(username, 10);
	}

	public void sendFriendRequestTo(String senderUsername, String receiverUsername) {

		if ((userRepository.findByUsername(senderUsername) != null) && (userRepository.findByUsername(receiverUsername) != null)) {

			User sender = userRepository.findByUsername(senderUsername);
			User receiver = userRepository.findByUsername(senderUsername);

			sender.sendFriendRequestTo(receiver);

			userRepository.save(receiver);
			userRepository.save(sender);
		}
	}

	public void receiveFriendRequestFrom(String senderUsername, String receiverUsername) {

		if ((userRepository.findByUsername(senderUsername) != null) && (userRepository.findByUsername(receiverUsername) != null)) {

			User sender = userRepository.findByUsername(senderUsername);
			User receiver = userRepository.findByUsername(senderUsername);

			sender.recieveFriendRequestFrom(sender);

			userRepository.save(receiver);
			userRepository.save(sender);
		}
	}

	public void acceptFriend(String senderUsername, String receiverUsername) {

		if ((userRepository.findByUsername(senderUsername) != null) && (userRepository.findByUsername(receiverUsername) != null)) {

			User sender = userRepository.findByUsername(senderUsername);
			User receiver = userRepository.findByUsername(senderUsername);

			receiver.acceptFriend(sender);

			userRepository.save(receiver);
			userRepository.save(sender);
		}
	}

	public Set<User> getPendingFriendRequests(String username) {
		if (userRepository.findByUsername(username) != null) {

			User user = userRepository.findByUsername(username);
			return user.getPendingFriendRequests();
		}

		return null;
	}

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

	public List<User> listUsers() {
		return userRepository.findAll();
	}

	public List<Event> listUserEvents(User user) {
		return user.getAttendingEvents();
	}

	public List<Event> listUserFutureEvents(User user) {

		return user.getAttendingEvents().stream().filter(event -> event.getEndTime().isAfter(LocalDateTime.now()))
				.collect(Collectors.toList());
	}

	public List<Event> listUserPastEvents(User user) {

		return user.getAttendingEvents().stream().filter(event -> event.getEndTime().isBefore(LocalDateTime.now()))
				.collect(Collectors.toList());
	}

}
