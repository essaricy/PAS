package com.sv.ipm.pms;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.softvision.ipm.pms.Application;
import com.softvision.ipm.pms.acl.service.SVAuthenticationService;
import com.softvision.ipm.pms.user.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
@Ignore
public class AuthenticationTest {

	@Autowired SVAuthenticationService authenticationService;

	@Test
	@Ignore
	public void test_ldap() {
		User user = authenticationService.authenticate("srikanth.kumar", "$oftvision$123");
		assert user!=null;
		int employeeId = user.getEmployeeId();
		assert employeeId == 1136;
	}

}
