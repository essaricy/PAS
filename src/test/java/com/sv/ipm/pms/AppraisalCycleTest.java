package com.sv.ipm.pms;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.ipm.pms.appraisal.model.AppraisalPhaseDto;
import com.softvision.ipm.pms.appraisal.service.AppraisalService;
import com.softvision.ipm.pms.common.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
public class AppraisalCycleTest {

	private static final String CYCLE_NAME = "AppraisalCycle-Test-" + System.currentTimeMillis();
	private static Integer id;

	@Autowired AppraisalService appraisalService;

	@Test
	@Ignore
	public void test1_getAll() {
		System.out.println("test1_getAll");
		List<AppraisalCycleDto> cycles = appraisalService.getCycles();
		assertNotNull(cycles);
		assertTrue(!cycles.isEmpty());
		AppraisalCycleDto cycle = cycles.get(0);
		assertNotNull(cycle);
		assertTrue(cycle.getId() != 0);
		assertTrue(!StringUtils.isBlank(cycle.getName()));
		List<AppraisalPhaseDto> phases = cycle.getPhases();
		assertNotNull(phases);
		assertTrue(!phases.isEmpty());
		AppraisalPhaseDto phase = phases.get(0);
		assertNotNull(phase);
		assertTrue(phase.getId() != 0);
		assertTrue(!StringUtils.isBlank(phase.getName()));
	}

	@Test
	@Ignore
	public void test2_create() throws ServiceException {
		Calendar calendar = Calendar.getInstance();
		System.out.println("test2_create");
		AppraisalCycleDto cycle = new AppraisalCycleDto();
		cycle.setName(CYCLE_NAME);
		cycle.setStatus(AppraisalCycleStatus.DRAFT);
		calendar.add(Calendar.MONTH, -3);
		cycle.setStartDate(calendar.getTime());
		calendar.add(Calendar.MONTH, 3);
		cycle.setEndDate(calendar.getTime());
		cycle.setCutoffDate(new Date());
		
		List<AppraisalPhaseDto> phases = new ArrayList<>();
		AppraisalPhaseDto phase1 = new AppraisalPhaseDto();
		phase1.setName("Phase 1");
		calendar.add(Calendar.MONTH, -3);
		phase1.setStartDate(calendar.getTime());
		calendar.add(Calendar.MONTH, 1);
		phase1.setEndDate(calendar.getTime());
		phases.add(phase1);
		
		AppraisalPhaseDto phase2 = new AppraisalPhaseDto();
		phase2.setName("Phase 2");
		calendar.add(Calendar.MONTH, 2);
		phase2.setStartDate(calendar.getTime());
		calendar.add(Calendar.MONTH, 1);
		phase2.setEndDate(calendar.getTime());
		phases.add(phase2);
		
		cycle.setPhases(phases);
		System.out.println("cycle to Create: " + cycle);

		AppraisalCycleDto updated = appraisalService.update(cycle);
		System.out.println("Created AppraisalCA=" + updated);
		assertNotNull(updated);
		assertTrue(updated.getId()!=0);
		assertTrue(updated.getName()==CYCLE_NAME);
		List<AppraisalPhaseDto> updatedAppraisalPhases = updated.getPhases();
		assertTrue(updatedAppraisalPhases.size()==2);
		AppraisalPhaseDto updatedAppraisalPhase = updatedAppraisalPhases.get(0);
		assertNotNull(updatedAppraisalPhase);
		assertTrue(updatedAppraisalPhase.getId()!=0);
		assertTrue(updatedAppraisalPhase.getName()=="Phase 1");
		
		id = updated.getId();
	}

	@Test
	@Ignore
	public void test3_getById() {
		System.out.println("test3_getById: " + id);
		AppraisalCycleDto appraisalCycle = appraisalService.getCycle(id);
		assertNotNull(appraisalCycle);
		assertTrue(appraisalCycle.getId() != 0);
		assertTrue(!StringUtils.isBlank(appraisalCycle.getName()));
		List<AppraisalPhaseDto> appraisalPhases = appraisalCycle.getPhases();
		assertNotNull(appraisalPhases);
		assertTrue(!appraisalPhases.isEmpty());
		AppraisalPhaseDto appraisalPhase = appraisalPhases.get(0);
		assertNotNull(appraisalPhase);
		assertTrue(appraisalPhase.getId() != 0);
		assertTrue(!StringUtils.isBlank(appraisalPhase.getName()));
	}

	@Test
	@Ignore
	public void test4_deleteById() throws ServiceException {
		System.out.println("test4_deleteById: " + id);
		appraisalService.delete(id);
		AppraisalCycleDto appraisalCycle = appraisalService.getCycle(id);
		assertNull(appraisalCycle);
	}

	@Test
	public void test5_getActiveCycle() {
		System.out.println("test5_getActiveCycle: " + id);
		AppraisalCycleDto appraisalCycle = appraisalService.getActiveCycle();
		System.out.println("appraisalCycle=" + appraisalCycle);
		/*assertNotNull(appraisalCycle);
		assertTrue(appraisalCycle.getId() != 0);
		assertTrue(!StringUtils.isBlank(appraisalCycle.getName()));
		List<AppraisalPhaseDto> appraisalPhases = appraisalCycle.getPhases();
		assertNotNull(appraisalPhases);
		assertTrue(!appraisalPhases.isEmpty());
		AppraisalPhaseDto appraisalPhase = appraisalPhases.get(0);
		assertNotNull(appraisalPhase);
		assertTrue(appraisalPhase.getId() != 0);
		assertTrue(!StringUtils.isBlank(appraisalPhase.getName()));*/
	}

}
