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
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.email.repo.EmailRepository;
import com.softvision.ipm.pms.employee.entity.Employee;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMailTest {

	@Autowired
	private EmailRepository emailRepository;
	

	@Test
	public void test1_sendGenericMail() {
		try {
			AppraisalCycle appraisalCycle = new AppraisalCycle();
			appraisalCycle.setName("2017-2018");
			AppraisalPhase appraisalPhase = new AppraisalPhase();
			appraisalPhase.setName("Q1");
			Employee employee = new Employee();
			employee.setLoginId("srikanth.kumar");
			employee.setFirstName("srikanth");
			Employee manager = new Employee();
			manager.setLoginId("rohith.ramesh");
			manager.setFirstName("Rohith");
			
			emailRepository.sendGenericMail(appraisalCycle, appraisalPhase, null, null, "KICK_OFF");
			emailRepository.sendGenericMail(appraisalCycle, appraisalPhase, employee, manager, "EMPLOYEE_ENABLE");
			emailRepository.sendGenericMail(appraisalCycle, appraisalPhase, employee, manager, "EMPLOYEE_SUBMITED");
			emailRepository.sendGenericMail(appraisalCycle, appraisalPhase, employee, manager, "MANAGER_REMAINDER");
			emailRepository.sendGenericMail(appraisalCycle, appraisalPhase, employee, manager, "MANAGER_FROZEN");
			emailRepository.sendGenericMail(appraisalCycle, appraisalPhase, employee, manager, "MANAGER_REVIEW");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	@Ignore
	public void test1_managerFrozen() {
		try {
			AppraisalCycle appraisalCycle = new AppraisalCycle();
			appraisalCycle.setName("2017-2018");
			AppraisalPhase appraisalPhase = new AppraisalPhase();
			appraisalPhase.setName("Q1");
			Employee employee = new Employee();
			employee.setLoginId("rohith.ramesh");
			employee.setFirstName("Rohith");
			Employee manager = new Employee();
			manager.setLoginId("prasad.pachuru");
			manager.setFirstName("Prasad");
			emailRepository.sendManagerReviewFrozen(appraisalCycle,appraisalPhase, employee, manager);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void test1_managerReview() {
		try {
			AppraisalCycle appraisalCycle = new AppraisalCycle();
			appraisalCycle.setName("2017-2018");
			AppraisalPhase appraisalPhase = new AppraisalPhase();
			appraisalPhase.setName("Q1");
			Employee employee = new Employee();
			employee.setLoginId("rohith.ramesh");
			employee.setFirstName("Rohith");
			Employee manager = new Employee();
			manager.setLoginId("prasad.pachuru");
			manager.setFirstName("Prasad");
			emailRepository.sendManagerReviewCompleted(appraisalCycle,appraisalPhase, employee, manager);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	@Ignore
	public void test1_employeeRemainder() {
		try {
			AppraisalCycle appraisalCycle = new AppraisalCycle();
			appraisalCycle.setName("2017-2018");
			AppraisalPhase appraisalPhase = new AppraisalPhase();
			appraisalPhase.setName("Q1");
			Employee employee = new Employee();
			employee.setLoginId("rohith.ramesh");
			employee.setFirstName("Rohith");
			Employee manager = new Employee();
			manager.setLoginId("prasad.pachuru");
			manager.setFirstName("Prasad");
			emailRepository.sendEmployeeRemainder(appraisalCycle, appraisalPhase,employee, manager);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void test1_ApprasialEmployeeConfirmation() {
		try {
			AppraisalCycle appraisalCycle = new AppraisalCycle();
			appraisalCycle.setName("2017-2018");
			AppraisalPhase appraisalPhase = new AppraisalPhase();
			appraisalPhase.setName("Q1");
			Employee employee = new Employee();
			employee.setLoginId("prasad.pachuru");
			employee.setFirstName("Prasad");
			Employee manager = new Employee();
			manager.setLoginId("rohith.ramesh");
			manager.setFirstName("Rohith");
			emailRepository.sendApprasialEmployeeSubmitted(appraisalCycle,appraisalPhase, employee, manager);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void test1_ApprasialEnableConfirmation() {
		try {
			AppraisalCycle appraisalCycle = new AppraisalCycle();
			appraisalCycle.setName("2017-2018");
			AppraisalPhase appraisalPhase = new AppraisalPhase();
			appraisalPhase.setName("Q1");
			Employee employee = new Employee();
			employee.setLoginId("rohith.ramesh");
			employee.setFirstName("Rohith");
			Employee manager = new Employee();
			manager.setLoginId("prasad.pachuru");
			manager.setFirstName("Prasad");
			emailRepository.sendApprasialManagerAssign(appraisalCycle,appraisalPhase, employee, manager);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void test1_ApprasialKickOfMail() {
		try {
			AppraisalCycle appraisalCycle = new AppraisalCycle();
			appraisalCycle.setName("2017-2018");
			AppraisalPhase appraisalPhase = new AppraisalPhase();
			appraisalPhase.setName("Q1");
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
