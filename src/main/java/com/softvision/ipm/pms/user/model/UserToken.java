package com.softvision.ipm.pms.user.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.softvision.ipm.pms.role.entity.Role;

public class UserToken implements Authentication {

	private static final long serialVersionUID = 1L;

	private User user;

	private List<GrantedAuthority> authorities;

	public UserToken(@NotNull User user) {
		this.user = user;
		authorities = new ArrayList<>();
		for (Role role : user.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
		}
	}

	@Override
	public String getName() {
		return user.getFirstName() + " " + user.getLastName();
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public Object getCredentials() {
		return user.getPassword();
	}

	@Override
	public Object getDetails() {
		return user;
	}

	@Override
	public Object getPrincipal() {
		return user.getUsername();
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean arg0) throws IllegalArgumentException {
	}

	@Override
	public String toString() {
		return "UserToken [user=" + user.toString() + ", grantedAuthorities=" + authorities.toString() + "]";
	}

}
