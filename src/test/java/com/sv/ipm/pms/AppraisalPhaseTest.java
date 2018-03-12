package com.sv.ipm.pms;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.softvision.ipm.pms.Application;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalPhaseDataRepository;
import com.softvision.ipm.pms.appraisal.service.AppraisalService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppraisalPhaseTest {

	@Autowired AppraisalService appraisalService;

	@Autowired AppraisalPhaseDataRepository appraisalPhaseDataRepository;

	@Test
	public void test1_getLastPhase() {
		System.out.println("test1_getLastPhase()");
		Integer id = 2 + 1 + 1;
		List<AppraisalPhase> nextPhases = appraisalPhaseDataRepository.findNextPhases(id);
		System.out.println(nextPhases);
	}

}