package com.softvision.ipm.pms.acl.repo;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class SVLdapRepository {

	@Value("${app.security.ldap.context-factory}")
	private String contextFactory;

	@Value("${app.security.ldap.url}")
	private String providerUrl;

	@Value("${app.security.ldap.authentication}")
	private String authenticationUsed;

	@Value("${app.security.ldap.domain}")
	private String domain;

	public void authenticate(String userid, String password) throws NamingException {
		String principal = userid + "@" + domain;

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
		env.put(Context.PROVIDER_URL, providerUrl);
		env.put(Context.SECURITY_AUTHENTICATION, authenticationUsed);
		env.put(Context.SECURITY_PRINCIPAL, principal);
		env.put(Context.SECURITY_CREDENTIALS, password.toString());

		DirContext ctx = new InitialDirContext(env);
		System.out.println("Authentication completed.");
		// do something useful with the context...
		ctx.close();
	}

}
