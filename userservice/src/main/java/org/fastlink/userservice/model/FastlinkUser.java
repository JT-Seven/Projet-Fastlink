package org.fastlink.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class FastlinkUser
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UID")
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(length = 64, nullable = true)
	private String password;

	@Column(name = "first_name", nullable = false, length = 20)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 20)
	private String lastName;

	@Column(name = "username", nullable = false, length = 20, unique = true)
	private String username ;

	@Column(name = "description", length = 500, unique = false)
	private String description = "";

	@Column(name = "profile_picture_url")
	private String profilePictureUrl ;

	@Column(name = "banner_picture_url")
	private String banner_picture_url ;

	@ManyToMany(fetch = EAGER)
	private List<FastlinkRole> roles = new ArrayList<FastlinkRole>();

	@Column(name = "provider", nullable = false)
	private String provider;

	@Column(name = "active", nullable = false)
	private boolean active = false; //Only in developement TRUE

}
