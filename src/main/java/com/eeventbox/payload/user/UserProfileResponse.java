package com.eeventbox.payload.user;
/**
 * ========================================================
 * This class contains all user profile infos
 * Created by Rivel Babindamana on 31/10/2019 at Nanterre U
 * ========================================================
 */
import com.eeventbox.model.user.User;
import com.eeventbox.model.utility.Interest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

	private Long id;
	private String username;
	private String name;
	private String email;
	private Boolean isActive;
	private String phone;
	private Date joinedAt;
	private Set<Interest> interests;
	private String university;

	public UserProfileResponse(User user) {

		this.id = user.getId();
		this.username = user.getUsername();
		this.name = user.getFirstName() + " " + user.getLastName();
		this.email = user.getEmail();
		this.isActive = user.isActive();
		this.phone = user.getPhone();
		this.joinedAt = user.getCreatedAt();
		this.interests = user.getInterests();
		this.university = user.getUniversity();
	}
}
