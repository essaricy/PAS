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
import com.softvision.ipm.pms.appraisal.service.AppraisalService;
import com.softvision.ipm.pms.common.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppraisalCycleTest {

	private static final String CYCLE_NAME = "AppraisalCycle-Test-" + System.currentTimeMillis();
	private static Long id;

	@Autowired AppraisalService appraisalService;

	@Test()
	public void test1_getAll() {
		System.out.println("test1_getAll");
		List<AppraisalCycle> cycles = appraisalService.getCycles();
		assertNotNull(cycles);
		assertTrue(!cycles.isEmpty());
		AppraisalCycle cycle = cycles.get(0);
		assertNotNull(cycle);
		assertTrue(cycle.getId() != 0);
		assertTrue(!StringUtils.isBlank(cycle.getName()));
		List<AppraisalPhase> phases = cycle.getPhases();
		assertNotNull(phases);
		assertTrue(!phases.isEmpty());
		AppraisalPhase phase = phases.get(0);
		assertNotNull(phase);
		assertTrue(phase.getId() != 0);
		assertTrue(!StringUtils.isBlank(phase.getName()));
	}

	@Test
	public void test2_create() throws ServiceException {
		Calendar calendar = Calendar.getInstance();
		System.out.println("test2_create");
		AppraisalCycle cycle = new AppraisalCycle();
		cycle.setName(CYCLE_NAME);
		cycle.setStatus("DRAFT");
		calendar.add(Calendar.MONTH, -3);
		cycle.setStartDate(calendar.getTime());
		calendar.add(Calendar.MONTH, 3);
		cycle.setEndDate(calendar.getTime());
		cycle.setCutoffDate(new Date());
		
		List<AppraisalPhase> phases = new ArrayList<>();
		AppraisalPhase phase1 = new AppraisalPhase();
		phase1.setName("Phase 1");
		calendar.add(Calendar.MONTH, -3);
		phase1.setStartDate(calendar.getTime());
		calendar.add(Calendar.MONTH, 1);
		phase1.setEndDate(calendar.getTime());
		phases.add(phase1);
		
		AppraisalPhase phase2 = new AppraisalPhase();
		phase2.setName("Phase 2");
		calendar.add(Calendar.MONTH, 2);
		phase2.setStartDate(calendar.getTime());
		calendar.add(Calendar.MONTH, 1);
		phase2.setEndDate(calendar.getTime());
		phases.add(phase2);
		
		cycle.setPhases(phases);
		System.out.println("cycle to Create: " + cycle);

		AppraisalCycle updated = appraisalService.update(cycle);
		System.out.println("Created AppraisalCA=" + updated);
		assertNotNull(updated);
		assertTrue(updated.getId()!=0);
		assertTrue(updated.getName()==CYCLE_NAME);
		List<AppraisalPhase> updatedAppraisalPhases = updated.getPhases();
		assertTrue(updatedAppraisalPhases.size()==2);
		AppraisalPhase updatedAppraisalPhase = updatedAppraisalPhases.get(0);
		assertNotNull(updatedAppraisalPhase);
		assertTrue(updatedAppraisalPhase.getId()!=0);
		assertTrue(updatedAppraisalPhase.getName()=="Phase 1");
		
		id = updated.getId();
	}

	@Test
	public void test3_getById() {
		System.out.println("test3_getById: " + id);
		AppraisalCycle goalCa = appraisalService.getCycle(id);
		assertNotNull(goalCa);
		assertTrue(goalCa.getId() != 0);
		assertTrue(!StringUtils.isBlank(goalCa.getName()));
		List<AppraisalPhase> goalCaps = goalCa.getPhases();
		assertNotNull(goalCaps);
		assertTrue(!goalCaps.isEmpty());
		AppraisalPhase goalCap = goalCaps.get(0);
		assertNotNull(goalCap);
		assertTrue(goalCap.getId() != 0);
		assertTrue(!StringUtils.isBlank(goalCap.getName()));
	}

	@Test
	public void test4_deleteById() throws ServiceException {
		System.out.println("test4_deleteById: " + id);
		appraisalService.delete(id);
		AppraisalCycle goalCa = appraisalService.getCycle(id);
		assertNull(goalCa);
	}
}
