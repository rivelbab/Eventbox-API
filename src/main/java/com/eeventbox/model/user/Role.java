package com.eeventbox.model.user;

import com.eeventbox.model.utility.AuditModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends AuditModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(length = 60)
	private RoleName name;

	public Role() {}

	public Role(RoleName name) {
		this.name = name;
	}
}
