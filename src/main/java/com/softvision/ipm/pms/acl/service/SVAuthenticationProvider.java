package com.softvision.ipm.pms.acl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(SVAuthenticationProvider.class);

	//@Autowired private SVAuthenticationService authenticationService;
	@Autowired private TestAuthenticationService authenticationService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String principal = authentication.getPrincipal().toString();
		String credentials = authentication.getCredentials().toString();
		LOGGER.info("Authenticating user " + principal);
		User user = authenticationService.authenticate(principal, credentials);
		return new UserToken(user);
	}

	@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
