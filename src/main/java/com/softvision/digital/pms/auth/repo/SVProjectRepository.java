package com.softvision.digital.pms.auth.repo;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.softvision.digital.pms.common.beans.AppRoleConfig;
import com.softvision.digital.pms.employee.model.SVEmployee;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SVProjectRepository {

	@Autowired
	private AppRoleConfig appRoleConfig;

	public SVEmployee getEmployee(String userId) {
		String email = userId + "@" + appRoleConfig.getDomain();
		log.info("Looking up for the user {} in SVProject", email);
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(appRoleConfig.getUrl()).queryParam("key", appRoleConfig.getApiKey()).queryParam("email", email);
		return restTemplate.getForObject(builder.toUriString(), SVEmployee.class, appRoleConfig.getApiKey(), email);
	}
	
	public List<SVEmployee> getAllEmployees() {
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(appRoleConfig.getUrl()).queryParam("key", appRoleConfig.getApiKey());
		SVEmployee[] list = restTemplate.getForObject(builder.toUriString(), SVEmployee[].class, appRoleConfig.getApiKey());
		return Arrays.asList(list);
	}

}
