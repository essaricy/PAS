package com.softvision.digital.pms.common.beans;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "app.security.ldap")
@Data
public class AppSecurityConfig {

	private String mode;

	private String contextFactory;

	private String url;

	private String authentication;

	private String domain;

}
