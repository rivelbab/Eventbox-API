package com.eeventbox.model.event;
/**
 * =========================================================
 * Contains all events user's comments.
 * Created by Rivel Babindamana on 01/10/2019 at Nanterre U.
 * =========================================================
 */

import com.eeventbox.model.user.User;
import com.eeventbox.model.utility.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends AuditModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String content;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Event event;

	@ManyToOne( fetch = FetchType.LAZY)
	private User author;
}
