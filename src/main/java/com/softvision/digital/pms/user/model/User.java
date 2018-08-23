package com.softvision.digital.pms.user.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.softvision.digital.pms.role.entity.Role;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude="password")
public class User {

	@NotNull
	@NotBlank
	private int employeeId;

	@NotNull
	@NotBlank
	private String firstName;

	@NotNull
	@NotBlank
	private String lastName;

	@NotNull
	@NotBlank
	private String designation;

	@NotNull
	@NotBlank
	private String band;

	private String location;

	@NotNull
	@NotBlank
	private Date joinedDate;

	@NotNull
	@NotBlank
	private String username;

	@NotNull
	@NotBlank
	private String password;

	private String imageUrl;

	@NotEmpty
	private List<@NotNull Role> roles;
	
}
