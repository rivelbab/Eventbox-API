package com.eeventbox.repository;

import com.eeventbox.model.security.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, String> {
	VerificationToken findByToken(String token);
	VerificationToken findByUserEmail(String email);
}
