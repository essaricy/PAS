package com.softvision.digital.pms.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.softvision.digital.pms.role.constant.Roles;

import lombok.ToString;

@ToString
public class UserToken implements Authentication, Serializable {

	private static final long serialVersionUID = 1L;

	private User user;

	private List<GrantedAuthority> authorities;

	public UserToken(@NotNull User user) {
		this.user = user;
		authorities = new ArrayList<>();
		user.getRoles().forEach(o -> {
		    Roles roles = Roles.get(o.getRoleName());
		    authorities.add(new SimpleGrantedAuthority(roles.toString()));
		});
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
	public void setAuthenticated(boolean arg0) {
		// Nothing to do
	}

}
