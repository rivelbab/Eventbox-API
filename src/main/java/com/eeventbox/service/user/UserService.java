package com.eeventbox.service.user;
/**
 * ================================================
 * Contains all useful operations for all Users
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.model.user.User;
import com.eeventbox.payload.user.UserRequest;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findUsers();

    Optional<User> findUserByUsername(String username);

    Boolean updateUser(String username, UserRequest userRequest);

    void updateUser(User user);

    Boolean deleteUser(String email);
}
