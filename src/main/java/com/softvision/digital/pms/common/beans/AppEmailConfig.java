package com.softvision.digital.pms.common.beans;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "app.email")
@Data
public class AppEmailConfig {

	private String mode;

	private String host;

	private String domain;

	private String image;

	private String alwaysSendTo;

}
