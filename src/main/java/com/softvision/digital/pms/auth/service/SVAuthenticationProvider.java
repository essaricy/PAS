package com.softvision.digital.pms.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.softvision.digital.pms.auth.model.User;
import com.softvision.digital.pms.auth.model.UserToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SVAuthenticationProvider implements AuthenticationProvider {

	@Autowired private SVAuthenticationService svAuthenticationService;

	@Override
	public Authentication authenticate(Authentication authentication) {
		String principal = authentication.getPrincipal().toString();
		String credentials = authentication.getCredentials().toString();
		log.info("Authenticating user {}", principal);
		User user = svAuthenticationService.authenticate(principal.toLowerCase(), credentials);
		return new UserToken(user);
	}

	@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
