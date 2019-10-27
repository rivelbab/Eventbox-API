package com.eeventbox.model.user;
/**
 * ================================================
 * Contains all useful details for the User
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.model.security.VerificationToken;
import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private long id;

	private String username;
	private String email;
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	private boolean isActive;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private VerificationToken verificationToken;

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}
}
