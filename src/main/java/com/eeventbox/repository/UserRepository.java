package com.eeventbox.repository;

import com.eeventbox.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	User findByEmail(String email);

	User findByUsername(String username);

	User findByUsernameOrEmail(String username, String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
