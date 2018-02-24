package com.softvision.ipm.pms.user.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
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

}
