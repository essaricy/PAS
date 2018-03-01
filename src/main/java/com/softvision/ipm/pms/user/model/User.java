package com.softvision.ipm.pms.user.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.softvision.ipm.pms.common.validator.NotContainNull;
import com.softvision.ipm.pms.role.entity.Role;

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

	@NotEmpty
	@NotContainNull
	private List<Role> roles;
	
}
