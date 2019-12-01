package com.eeventbox.service.user;
/**
 * ================================================
 * Contains all useful operations for all Users
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.model.user.User;
import com.eeventbox.payload.user.UserProfileRequest;
import com.eeventbox.payload.user.UserProfileResponse;
import com.eeventbox.payload.user.UserSummaryResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface UserService {

    UserProfileResponse getCurrentUser(HttpServletRequest request);

    ResponseEntity<?> showUserProfile(String username);

    ResponseEntity<?> updateUserProfile(UserProfileRequest upq);

    List<User> listUsers();

    List<User> getUsersWithCommonInterest(String username);

    void sendFriendRequestTo(String sender, String receiver);

    void receiveFriendRequestFrom(String sender, String receiver);

    void acceptFriend(String sender, String receiver);

    Set<User> getPendingFriendRequests(String username);
}
