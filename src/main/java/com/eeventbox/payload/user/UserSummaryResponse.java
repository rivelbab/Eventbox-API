package com.eeventbox.payload.user;
/**
 * ==================================================
 * Contains some user's information to show in a view
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ==================================================
 */
import com.eeventbox.model.user.User;
import com.eeventbox.model.utility.Interest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryResponse {

	private Long id;
	private String username;
	private String email;
	private Boolean isActive;
	private Set<Interest> interests;
	private String accessToken;
	private String tokenType = "Bearer";


	public UserSummaryResponse(User user, String accessToken) {

		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.isActive = user.isActive();
		this.interests = user.getInterests();
		this.accessToken = accessToken;
	}
}
