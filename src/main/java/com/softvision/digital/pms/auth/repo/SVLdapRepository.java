package com.softvision.digital.pms.auth.repo;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.softvision.digital.pms.common.beans.AppSecurityConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SVLdapRepository {

	@Autowired
	private AppSecurityConfig appSecurityConfig;

	public void authenticate(String userid, String password) throws NamingException {
		String principal = userid + "@" + appSecurityConfig.getDomain();

		Hashtable<String, String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, appSecurityConfig.getContextFactory());
		env.put(Context.PROVIDER_URL, appSecurityConfig.getUrl());
		env.put(Context.SECURITY_AUTHENTICATION, appSecurityConfig.getAuthentication());
		env.put(Context.SECURITY_PRINCIPAL, principal);
		env.put(Context.SECURITY_CREDENTIALS, password);

		DirContext ctx = new InitialDirContext(env);
		log.info("Authentication completed for user {}", userid);
		// do something useful with the context...
		ctx.close();
	}

}
