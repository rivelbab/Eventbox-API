package com.eeventbox.service.user;

import com.eeventbox.model.event.Event;
import com.eeventbox.model.user.User;

import java.util.List;
import java.util.Set;

/**
 * ================================================
 * Contains all useful operations for all Users
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */

public interface UserService {

    List<User> listUsers();

    List<Event> listUserEvents(User user);

    List<Event> listUserFutureEvents(User user);

    List<Event> listUserPastEvents(User user);

    void joinEvent(User user, Event event);

    List<User> getUsersWithCommonInterest(String username);

    void sendFriendRequestTo(String sender, String reciever);

    void receiveFriendRequestFrom(String sender, String reciever);

    void acceptFriend(String sender, String reciever);

    Set<User> getPendingFriendRequests(String username);
}
