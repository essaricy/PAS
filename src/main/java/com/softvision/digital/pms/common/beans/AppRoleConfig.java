package com.softvision.digital.pms.common.beans;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "app.role.svproject")
@Data
public class AppRoleConfig {

	private String apiKey;

	private String domain;

	private String url;

}
