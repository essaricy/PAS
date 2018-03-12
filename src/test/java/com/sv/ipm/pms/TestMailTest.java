package com.sv.ipm.pms;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.softvision.ipm.pms.Application;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.email.repo.EmailRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMailTest {

	@Autowired
	private EmailRepository emailRepository;

	@Test
	@Ignore
	public void test1_ApprasialKickOfMail() {
		try {
			AppraisalCycle appraisalCycle = new AppraisalCycle();
			emailRepository.sendApprasialKickOff(appraisalCycle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*@Test
	public void test1_EmployeeAlert() {
		try {
			Map<String, Object> emailData = new HashMap<String, Object>();
			emailData.put("cycle", "2017-2018");
			emailData.put("employeeName", "Rohith");
			emailData.put("manager", "Murgan");
			emailEngine.sendEmployeeAlert(emailData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

}
