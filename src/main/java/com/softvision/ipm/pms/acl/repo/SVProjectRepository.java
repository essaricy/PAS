package com.softvision.ipm.pms.acl.repo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.interceptor.annotations.AuditTransaction;

@Repository
public class SVProjectRepository {

	@Value("${app.role.svproject.api-key}")
	private String apiKey;

	@Value("${app.role.svproject.url}")
	private String url;

	@Value("${app.role.svproject.domain}")
	private String domain;

	@AuditTransaction
	public Employee getEmployee(String userId) {
		String email = userId + "@" + domain;

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

}
