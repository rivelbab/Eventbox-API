package com.eeventbox.model.security;
/**
 * ======================================================
 * Contains all useful details to confirm a user account
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ======================================================
 */
import com.eeventbox.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class VerificationToken {

	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_VERIFIED = "VERIFIED";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String token;
	private String status;
	private LocalDateTime expiredDateTime;
	private LocalDateTime issuedDateTime;
	private LocalDateTime confirmedDateTime;

	@OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	public VerificationToken() {
		token = UUID.randomUUID().toString();
		issuedDateTime = LocalDateTime.now();
		expiredDateTime = this.issuedDateTime.plusDays(1);
		this.status = STATUS_PENDING;
	}
}
