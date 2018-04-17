package com.softvision.ipm.pms.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.softvision.ipm.pms.acl.repo.SVLdapRepository;
import com.softvision.ipm.pms.acl.repo.SVProjectRepository;
import com.softvision.ipm.pms.acl.service.SVAuthenticationProvider;
import com.softvision.ipm.pms.acl.service.SVAuthenticationService;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.repo.EmployeeDataRepository;
import com.softvision.ipm.pms.user.model.UserToken;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class AuthenticationServiceTest {
	
	@InjectMocks
	private SVAuthenticationProvider svAuthenticationProvider;
	
	@Mock
	private SVAuthenticationService svAuthenticationService;
	
	@Mock
	private SVLdapRepository svLdapRepository;

	@Mock
	private SVProjectRepository svProjectRepository;

	@Mock
	private EmployeeDataRepository employeeRepository;
	
	@Test
	public void validateAuthenticate() {
		//Mockito.when(sVAuthenticationService.authenticate(anyString(),anyString())).thenReturn(this.mockUserObject());
		Employee employee = mockEmployeeObject();
		
		//Mockito.doNothing(svLdapRepository.authenticate(anyString(),anyString()));
		Mockito.when(employeeRepository.findByLoginId(anyString())).thenReturn(employee);
		Mockito.when(employeeRepository.save((Employee)anyObject())).thenReturn(employee);
		//Authentication userTokenAuthentication = svAuthenticationProvider.authenticate(authentication);
		//assertNotNull(userTokenAuthentication)
		
		Authentication authentication = new UsernamePasswordAuthenticationToken("srikanth.kumar", "testPassword");
		Authentication authenticate = svAuthenticationProvider.authenticate(authentication);
		assertTrue(authenticate instanceof UserToken);
		UserToken userToken = (UserToken) authenticate;
		assertEquals("srikanth.kumar", userToken.getPrincipal());
		
	}
	
	private Employee mockEmployeeObject() {
		Employee employee = new Employee();
		return employee;
	}
	
	
}
