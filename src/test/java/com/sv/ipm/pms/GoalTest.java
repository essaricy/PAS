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
import com.softvision.ipm.pms.goal.model.GoalDto;
import com.softvision.ipm.pms.goal.model.GoalParamDto;
import com.softvision.ipm.pms.goal.service.GoalService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
public class GoalTest {

	private static final String GOAL_NAME = "Goal-Test-" + System.currentTimeMillis();
	private static Long id;

	@Autowired GoalService goalService;

	@Test
	@Ignore
	public void test1_getAll() {
		System.out.println("test1_getAll");
		List<GoalDto> goals = goalService.getGoals();
		assertNotNull(goals);
		assertTrue(!goals.isEmpty());
		GoalDto goal = goals.get(0);
		assertNotNull(goal);
		assertTrue(goal.getId() != 0);
		assertTrue(!StringUtils.isBlank(goal.getName()));
		List<GoalParamDto> goalParams = goal.getParams();
		assertNotNull(goalParams);
		assertTrue(!goalParams.isEmpty());
		GoalParamDto goalParam = goalParams.get(0);
		assertNotNull(goalParam);
		assertTrue(goalParam.getId() != 0);
		assertTrue(!StringUtils.isBlank(goalParam.getName()));
	}

	@Test
	public void test5_getActiveGoals() {
		System.out.println("test5_getActiveGoals");
		List<GoalDto> goals = goalService.getActiveGoals();
		System.out.println("goals= " + goals.size());
		assertNotNull(goals);
		assertTrue(!goals.isEmpty());
		GoalDto goal = goals.get(0);
		assertNotNull(goal);
		assertTrue(goal.getId() != 0);
		assertTrue(!StringUtils.isBlank(goal.getName()));
		List<GoalParamDto> params = goal.getParams();
		assertNotNull(params);
		assertTrue(!params.isEmpty());
		GoalParamDto param = params.get(0);
		assertNotNull(param);
		assertTrue(param.getId() != 0);
		assertTrue(!StringUtils.isBlank(param.getName()));
	}

	@Test
	@Ignore
	public void test2_create() throws ServiceException {
		System.out.println("test2_create");
		GoalDto goal = new GoalDto();
		goal.setId((long)50);
		goal.setName(GOAL_NAME);
		List<GoalParamDto> params = new ArrayList<>();
		GoalParamDto parama1 = new GoalParamDto();
		parama1.setName("Goal Param 1");
		parama1.setApplicable("Y");
		params.add(parama1);

		GoalParamDto param2 = new GoalParamDto();
		param2.setName("Goal Param 2");
		param2.setApplicable("Y");
		params.add(param2);

		goal.setParams(params);

		GoalDto updated = goalService.update(goal);
		System.out.println("Created Goal=" + updated);
		assertNotNull(updated);
		assertTrue(updated.getId()!=0);
		assertTrue(updated.getName()==GOAL_NAME);
		List<GoalParamDto> updatedParams = updated.getParams();
		assertTrue(updatedParams.size()==2);
		GoalParamDto updatedParam = updatedParams.get(0);
		assertNotNull(updatedParam);
		assertTrue(updatedParam.getId()!=0);
		assertTrue(updatedParam.getName()=="Goal Param 1");
		
		id = updated.getId();
	}

	@Test
	@Ignore
	public void test3_getById() {
		System.out.println("test3_getById: " + id);
		GoalDto goal = goalService.getGoal(id);
		assertNotNull(goal);
		assertTrue(goal.getId() != 0);
		assertTrue(!StringUtils.isBlank(goal.getName()));
		List<GoalParamDto> params = goal.getParams();
		assertNotNull(params);
		assertTrue(!params.isEmpty());
		GoalParamDto param = params.get(0);
		assertNotNull(param);
		assertTrue(param.getId() != 0);
		assertTrue(!StringUtils.isBlank(param.getName()));
	}

	@Test
	@Ignore
	public void test4_deleteById() throws ServiceException {
		System.out.println("test4_deleteById: " + id);
		goalService.delete(id);
		GoalDto goal = goalService.getGoal(id);
		assertNull(goal);
	}
}
