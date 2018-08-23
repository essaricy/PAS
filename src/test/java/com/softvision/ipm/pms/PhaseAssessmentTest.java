package com.softvision.ipm.pms;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.softvision.digital.pms.Application;
import com.softvision.digital.pms.assess.model.PhaseAssessmentDto;
import com.softvision.digital.pms.assess.service.PhaseAssessmentService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
public class PhaseAssessmentTest {

	@Autowired PhaseAssessmentService phaseAssessmentService;

	@Test
	public void test1_getAll() {
		PhaseAssessmentDto phaseAssessment;
		try {
			phaseAssessment = phaseAssessmentService.getByAssignmentForEmployeeAndManager(112, 4570, null);
			System.out.println("phaseAssessment= " + phaseAssessment);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
