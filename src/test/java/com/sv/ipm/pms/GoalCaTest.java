package com.sv.ipm.pms;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.goal.entity.GoalCa;
import com.softvision.ipm.pms.goal.entity.GoalCap;
import com.softvision.ipm.pms.goal.service.GoalService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
public class GoalCaTest {

	private static final String GOAL_NAME = "goalCa-Test-" + System.currentTimeMillis();
	private static Long id;

	@Autowired GoalService goalService;

	@Test()
	public void test1_getAll() {
		System.out.println("test1_getAll");
		List<GoalCa> goals = goalService.getGoals();
		assertNotNull(goals);
		assertTrue(!goals.isEmpty());
		GoalCa goalCa = goals.get(0);
		assertNotNull(goalCa);
		assertTrue(goalCa.getId() != 0);
		assertTrue(!StringUtils.isBlank(goalCa.getName()));
		List<GoalCap> goalCaps = goalCa.getGoalCaps();
		assertNotNull(goalCaps);
		assertTrue(!goalCaps.isEmpty());
		GoalCap goalCap = goalCaps.get(0);
		assertNotNull(goalCap);
		assertTrue(goalCap.getId() != 0);
		assertTrue(!StringUtils.isBlank(goalCap.getName()));
	}

	@Test
	public void test2_create() throws ServiceException {
		System.out.println("test2_create");
		GoalCa goalCa = new GoalCa();
		goalCa.setId((long)50);
		goalCa.setName(GOAL_NAME);
		List<GoalCap> goalCaps = new ArrayList<>();
		GoalCap goalCap1 = new GoalCap();
		goalCap1.setName("goalCa Param 1");
		goalCap1.setApply("Y");
		goalCaps.add(goalCap1);

		GoalCap goalCap2 = new GoalCap();
		goalCap2.setName("goalCa Param 2");
		goalCap2.setApply("Y");
		goalCaps.add(goalCap2);

		goalCa.setGoalCaps(goalCaps);

		GoalCa updated = goalService.update(goalCa);
		System.out.println("Created GoalCA=" + updated);
		assertNotNull(updated);
		assertTrue(updated.getId()!=0);
		assertTrue(updated.getName()==GOAL_NAME);
		List<GoalCap> updatedGoalCaps = updated.getGoalCaps();
		assertTrue(updatedGoalCaps.size()==2);
		GoalCap updatedGoalCap = updatedGoalCaps.get(0);
		assertNotNull(updatedGoalCap);
		assertTrue(updatedGoalCap.getId()!=0);
		assertTrue(updatedGoalCap.getName()=="goalCa Param 1");
		
		id = updated.getId();
	}

	@Test
	public void test3_getById() {
		System.out.println("test3_getById: " + id);
		GoalCa goalCa = goalService.getGoal(id);
		assertNotNull(goalCa);
		assertTrue(goalCa.getId() != 0);
		assertTrue(!StringUtils.isBlank(goalCa.getName()));
		List<GoalCap> goalCaps = goalCa.getGoalCaps();
		assertNotNull(goalCaps);
		assertTrue(!goalCaps.isEmpty());
		GoalCap goalCap = goalCaps.get(0);
		assertNotNull(goalCap);
		assertTrue(goalCap.getId() != 0);
		assertTrue(!StringUtils.isBlank(goalCap.getName()));
	}

	@Test
	public void test4_deleteById() throws ServiceException {
		System.out.println("test4_deleteById: " + id);
		goalService.delete(id);
		GoalCa goalCa = goalService.getGoal(id);
		assertNull(goalCa);
	}
}
