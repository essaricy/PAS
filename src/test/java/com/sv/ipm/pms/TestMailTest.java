package com.sv.ipm.pms;

import java.util.HashMap;
import java.util.Map;

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
import com.softvision.ipm.pms.email.EmailEngine;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMailTest {

	//private static final String CYCLE_NAME = "AppraisalCycle-Test-" + System.currentTimeMillis();

	//@Autowired SPIEmailUtil spiEmailUtil;

	 
	  
	  
//	@Test
//	public void test3_getById() {
//		try {
//			spiEmailUtil.sendEmail("srikanth.kumar@softvision.com", new String[] {"rohith.ramesh@softvision.com"}, "subject", "content");
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	 @Autowired
	    private EmailEngine emailEngine;
	 
	 
	  @Test
	  @Ignore
		public void test1_ApprasialKickOfMail() {
			try {
				Map<String, Object> emailData =new HashMap<String, Object>();
				emailData.put("cycle", "2017-2018");
				emailEngine.sendApprasialKickOfMail(emailData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	  
	  @Test
			public void test1_EmployeeAlert() {
			try {
				Map<String, Object> emailData =new HashMap<String, Object>();
				emailData.put("cycle", "2017-2018");
				emailData.put("employeeName", "Rohith");
				emailData.put("manager", "Murgan");
				emailEngine.sendEmployeeAlert(emailData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
