package com.softvision.ipm.pms.acl.repo;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.softvision.ipm.pms.employee.model.SVEmployee;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SVProjectRepository {

	@Value("${app.role.svproject.api-key}")
	private String apiKey;

	@Value("${app.role.svproject.url}")
	private String url;

	@Value("${app.role.svproject.domain}")
	private String domain;

	public SVEmployee getEmployee(String userId) {
		String email = userId + "@" + domain;
		log.info("Looking up for the user {} in SVProject", email);
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("key", apiKey).queryParam("email", email);
		SVEmployee svEmployee = restTemplate.getForObject(builder.toUriString(), SVEmployee.class, apiKey, email);
		return svEmployee;
	}
	
	public List<SVEmployee> getAllEmployees() {
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("key", apiKey);
		SVEmployee[] list = restTemplate.getForObject(builder.toUriString(), SVEmployee[].class, apiKey);
		return Arrays.asList(list);
	}

}
