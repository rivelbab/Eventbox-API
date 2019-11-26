package com.eeventbox.payload.user;
/**
 * ==================================================
 * Contains some user's information to show in a view
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ==================================================
 */
import com.eeventbox.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryResponse {

	private Integer id;
	private String username;
	private String name;

	public UserSummaryResponse(User user) {

		this.id = user.getId();
		this.username = user.getUsername();
		this.name = user.getFirstName() + " " + user.getLastName();
	}
}
