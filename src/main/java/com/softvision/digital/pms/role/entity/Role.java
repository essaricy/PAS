package com.softvision.digital.pms.role.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@JsonProperty("id")
	private int id;

	@JsonProperty("RoleName")
	private String roleName;

}
