package com.softvision.ipm.pms.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import com.softvision.ipm.pms.acl.repo.SVLdapRepository;
import com.softvision.ipm.pms.acl.service.SVAuthenticationService;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.employee.mapper.EmployeeMapper;
import com.softvision.ipm.pms.employee.repo.EmployeeDataRepository;
import com.softvision.ipm.pms.role.constant.Roles;
import com.softvision.ipm.pms.role.entity.Role;
import com.softvision.ipm.pms.role.repo.RoleDataRepository;
import com.softvision.ipm.pms.user.model.User;
import com.softvision.ipm.pms.web.config.MyModelMapper;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

	@Mock private SVLdapRepository svLdapRepository;

	@Mock private EmployeeDataRepository employeeRepository;

	@Mock private RoleDataRepository roleDataRepository;

	@Spy ModelMapper mapper = new MyModelMapper();

	@InjectMocks private EmployeeMapper employeeMapper;
	
	@InjectMocks private SVAuthenticationService svAuthenticationService;

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void prepare() {
		((MyModelMapper)mapper).loadMappings();
	}

	@Test
	public void validateAuthenticate() throws NamingException {
		Employee employee = mockEmployeeObject();

		Mockito.doNothing().when(svLdapRepository).authenticate(anyString(), anyString());
		Mockito.when(employeeRepository.findByLoginId(anyString())).thenReturn(employee);
		Mockito.when(employeeRepository.save((Employee)anyObject())).thenReturn(employee);
		Mockito.when(roleDataRepository.findByEmployeeId(anyObject())).thenReturn(mockAdminRoles());
		
		User user = svAuthenticationService.authenticate("srikanth.kumar", "password");
		System.out.println(user);
		/*Authentication authentication = new UsernamePasswordAuthenticationToken("srikanth.kumar", "testPassword");
		Authentication authenticate = svAuthenticationProvider.authenticate(authentication);
		assertTrue(authenticate instanceof UserToken);
		UserToken userToken = (UserToken) authenticate;
		assertEquals("srikanth.kumar", userToken.getPrincipal());*/
		
	}
	
	private List<Role> mockAdminRoles() {
		List<Role> roleList = new ArrayList<>();
		Arrays.stream(Roles.values()).forEach(roleValue -> {
			Role role = new Role();
			role.setId(roleValue.getCode());
			role.setRoleName(roleValue.getName());
		});
		return roleList;
	}

	private Employee mockEmployeeObject() {
		Employee employee = new Employee();
		return employee;
	}
	
	
}
