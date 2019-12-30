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
	@JsonIgnore
	@NotBlank
	@Column(name = "password")
	private String password;

	// =========== identity infos ===========
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@JsonIgnore
	@Column(name = "birthday")
	private String birthday;
	@Column(name = "phone")
	private String phone;
	private String ufr;
	private int sex;

	/* =========== Auth infos =========== */
	@JsonIgnore
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private VerificationToken verificationToken;

	@JsonIgnore
	@Column(name = "reset_token")
	private String resetToken;
	@JsonIgnore
	@Column(name = "is_active")
	private boolean isActive;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	/* =========== profile infos ========= */
	@JsonIgnore
	@Column(name = "avatar")
	private String avatar;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Event> adminingEvents;

	@JsonIgnore
	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass = Interest.class, fetch = FetchType.EAGER)
	private Set<Interest> interests = new HashSet<>();

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Event> attendingEvents;

	@JsonIgnore
	@OneToOne
	@JoinTable(name = "time_setting")
	private TimeSetting timeAvailability;

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
