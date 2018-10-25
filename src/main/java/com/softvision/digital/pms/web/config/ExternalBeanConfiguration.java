package com.softvision.digital.pms.web.config;

import java.util.Properties;

import javax.mail.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.softvision.digital.pms.common.beans.AppEmailConfig;
import com.softvision.digital.pms.common.beans.AppSecurityConfig;

@Configuration
public class ExternalBeanConfiguration {

	@Autowired
	private AppSecurityConfig appSecurityConfig;

	@Autowired
	private AppEmailConfig appEmailConfig;

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
        contextSource.setUrl(appSecurityConfig.getUrl());
        return contextSource;
    }

	@Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

	@Bean
	public Session getEmailSession() {
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", appEmailConfig.getHost());
		// Get the default Session object.
		return Session.getDefaultInstance(props);
	}

	@Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

	@Bean
	public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
	    return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
	}

}
