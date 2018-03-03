package com.softvision.ipm.pms.acl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.user.model.User;
import com.softvision.ipm.pms.user.model.UserToken;

@Service
public class SVAuthenticationProvider implements AuthenticationProvider {

	@Autowired private SVAuthenticationService authenticationService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String principal = authentication.getPrincipal().toString();
		String credentials = authentication.getCredentials().toString();
		User user = authenticationService.authenticate(principal, credentials);
		return new UserToken(user);
	}

	@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
