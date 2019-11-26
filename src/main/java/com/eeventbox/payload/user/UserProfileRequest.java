package com.eeventbox.payload.user;
/**
 * =========================================================
 * Contains all user profile info that can be updated
 * Created by Rivel Babindamana on 01/11/2019 at Nanterre U.
 * =========================================================
 */

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
public class UserProfileRequest {

	private Long id;
	private String username;
	private String firstName;
	private String lastName;
	private String birthday;
	private Set<Interest> interests;
}
