package com.softvision.ipm.pms.web.config;

import java.util.Properties;

import javax.mail.Session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class ExternalBeanConfiguration {

	@Value("${app.security.ldap.url}") private String ldapUrl;

	@Value("${app.security.ldap.authentication}") private String authenticationUsed;

	@Value("${app.security.ldap.domain}") private String domain;

	@Value("${app.email.host}") private String emailHost;

	// TODO: 
	// 1. Create LDAP Connection source
	// 2. Free Marker template
	@Bean
	public freemarker.template.Configuration getEmailConfiguration() {
		// Freemarker configuration object
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_25);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(this.getClass(), "/templates");
		return configuration;
	}

	@Bean
    public LdapContextSource contextSource () {
        LdapContextSource contextSource= new LdapContextSource();
        contextSource.setUrl(ldapUrl);
        //contextSource.setBase(env.getRequiredProperty("ldap.base"));
        //contextSource.setUserDn(env.getRequiredProperty("ldap.user"));
        //contextSource.setPassword(env.getRequiredProperty("ldap.password"));
        return contextSource;
    }

	@Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());        
    }

	@Bean
	public Session getEmailSession() {
		Properties PROPERTIES = new Properties();
		PROPERTIES.setProperty("mail.smtp.host", emailHost);
		// Get the default Session object.
		return Session.getDefaultInstance(PROPERTIES);
	}
}
