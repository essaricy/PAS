package com.softvision.ipm.pms;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.softvision.ipm.pms.Application;
import com.softvision.ipm.pms.assess.model.PhaseAssessmentDto;
import com.softvision.ipm.pms.assess.service.PhaseAssessmentService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PhaseAssessmentTest {

	@Autowired PhaseAssessmentService phaseAssessmentService;

	@Test
	public void test1_getAll() {
		PhaseAssessmentDto phaseAssessment;
		try {
			phaseAssessment = phaseAssessmentService.getByAssignment(112, 4570);
			System.out.println("phaseAssessment= " + phaseAssessment);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
