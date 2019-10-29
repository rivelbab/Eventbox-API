package com.eeventbox.model.user;
/**
 * ================================================
 * Contains all useful details for the User
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.model.event.Event;
import com.eeventbox.model.utils.Interest;
import com.eeventbox.model.role.Role;
import com.eeventbox.model.security.VerificationToken;
import com.eeventbox.model.utils.TimeSetting;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Integer id;

	@NotBlank
	@Size(max = 15)
	private String username;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	private boolean isActive;

	@Column(name = "reset_token")
	private String resetToken;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private VerificationToken verificationToken;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Event> adminingEvents;

	private String birthday;
	private String country;

	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass = Interest.class, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Interest> interests = new HashSet<>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Event> attendingEvents;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<User> friends = new HashSet<User>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<User> pendingFriendRequests = new HashSet<>();

	@OneToOne
	private TimeSetting timeAvailability;

	public void organizeNewEvent(Event event) {
		if(!adminingEvents.contains(event)){
			event.setOrganizer(this);
			this.adminingEvents.add(event);
		}
	}

	public void enrolEvent(Event event) {
		event.acceptEnrollment(this);
	}

	public void attendEvent(Event event) {
		if(!this.attendingEvents.contains(event)){
			this.attendingEvents.add(event);
		}
	}

	public void acceptFriend(User sender) {
		if ((sender != null) && pendingFriendRequests.contains(sender)) {
			pendingFriendRequests.remove(sender);
			this.friends.add(sender);
		}
	}

	public void recieveFriendRequestFrom(User user) {
		this.pendingFriendRequests.add(user);
	}

	public void sendFriendRequestTo(User user) {
		user.recieveFriendRequestFrom(this);
	}
}
