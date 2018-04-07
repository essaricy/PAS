package com.softvision.ipm.pms.acl.repo;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.softvision.ipm.pms.employee.entity.Employee;

@Repository
public class SVProjectRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(SVProjectRepository.class);

	@Value("${app.role.svproject.api-key}")
	private String apiKey;

	@Value("${app.role.svproject.url}")
	private String url;

	@Value("${app.role.svproject.domain}")
	private String domain;

	public Employee getEmployee(String userId) {
		String email = userId + "@" + domain;
		LOGGER.info("Looking up for the user {} in SVProject", email);

		// To enable tracing of request and response
		/*RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
			List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
			interceptors.add(new LoggingRequestInterceptor());
			restTemplate.setInterceptors(interceptors);*/
		
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("key", apiKey).queryParam("email", email);
		Employee employee = restTemplate.getForObject(builder.toUriString(), Employee.class, apiKey, email);
		return employee;
	}
	
	public List<Employee> getAllEmployees() {
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("key", apiKey);
		Employee[] list = restTemplate.getForObject(builder.toUriString(), Employee[].class, apiKey);
		return Arrays.asList(list);
	}

}
