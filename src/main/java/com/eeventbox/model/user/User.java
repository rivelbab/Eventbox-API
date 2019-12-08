package com.eeventbox.model.user;
/**
 * ================================================
 * Contains all useful details for the User
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.model.event.Event;
import com.eeventbox.model.utility.AuditModel;
import com.eeventbox.model.utility.Interest;
import com.eeventbox.model.security.VerificationToken;
import com.eeventbox.model.utility.TimeSetting;
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
@Table(name = "users")
public class User extends AuditModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long id;

	// =========== primary infos ========
	@NotBlank
	@Size(max = 15)
	@Column(name = "username")
	private String username;
	@NotBlank
	@Email
	@Column(name = "email")
	private String email;
	@NotBlank
	@Column(name = "password")
	private String password;

	// =========== identity infos ===========
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "birthday")
	private String birthday;
	@Column(name = "phone")
	private String phone;

	/* =========== Auth infos =========== */
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private VerificationToken verificationToken;

	@Column(name = "reset_token")
	private String resetToken;
	@Column(name = "is_active")
	private boolean isActive;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	/* =========== profile infos ========= */
	@Column(name = "avatar")
	private String avatar;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Event> adminingEvents;

	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass = Interest.class, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Interest> interests = new HashSet<>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Event> attendingEvents;

	@OneToOne
	@JoinTable(name = "time_setting")
	private TimeSetting timeAvailability;

	private String university;

	/* =========== user actions ============= */

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
}
