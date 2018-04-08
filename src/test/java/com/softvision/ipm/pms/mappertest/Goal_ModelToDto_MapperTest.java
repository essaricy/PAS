package com.softvision.ipm.pms.mappertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.softvision.ipm.pms.goal.entity.Goal;
import com.softvision.ipm.pms.goal.entity.GoalParam;
import com.softvision.ipm.pms.goal.mapper.GoalMapper;
import com.softvision.ipm.pms.goal.model.GoalDto;
import com.softvision.ipm.pms.goal.model.GoalParamDto;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Goal_ModelToDto_MapperTest {

	@Autowired GoalMapper goalMapper;

    @Test
    public void test_goalNull() {
        assertNull(goalMapper.getGoal((Goal) null));
    }

    @Test
    public void test_paramNull() {
        assertNull(goalMapper.getParam((GoalParam) null));
    }

    @Test
    public void test_goalValid() {
        Goal goal = new Goal();
        goal.setId(10L);
        goal.setName("Some Goal Name");
        GoalDto dto = goalMapper.getGoal(goal);
        assertGoal(goal, dto);
    }

    @Test
    public void test_paramValid() {
        GoalParam param = new GoalParam();
        param.setId(10L);
        param.setName("Some Goal Param Name");
        GoalParamDto dto = goalMapper.getParam(param);
        assertParam(param, dto);
    }

    @Test
    public void test_goalList() {
        int numberOfCycles = 10;
        List<Goal> goals = new ArrayList<>();
        for (int index = 0; index < numberOfCycles; index++) {
            Goal goal = new Goal();
            goal.setId((long)index);
            goal.setName("Goal Name " + index);
            goals.add(goal);

            int numberOfPhases = 20;
            List<GoalParam> params = new ArrayList<>();
            for (int jindex = 0; jindex < numberOfPhases; jindex++) {
                GoalParam param = new GoalParam();
                param.setId((long)jindex);
                param.setName("Some Goal Param Name " + jindex);
                params.add(param);
            }
            goal.setParams(params);
        }

        List<GoalDto> goalDtoList = goalMapper.getGoalList(goals);
        assertEquals(goals.size(), goalDtoList.size());

        for (int index = 0; index < goals.size(); index++) {
            GoalDto dto = goalDtoList.get(index);
            Goal goal = goals.get(index);
            assertGoal(goal, dto);

            List<GoalParam> params = goal.getParams();
            List<GoalParamDto> paramDtoList = dto.getParams();
            assertEquals(params.size(), paramDtoList.size());
            
            for (int jindex = 0; jindex < paramDtoList.size(); jindex++) {
                GoalParamDto paramDto = paramDtoList.get(index);
                GoalParam param = params.get(index);
                assertParam(param, paramDto);
            }
        }
    }

    @Test
    public void test_paramList() {
        int numberOfPhases = 20;
        List<GoalParam> params = new ArrayList<>();
        for (int jindex = 0; jindex < numberOfPhases; jindex++) {
            GoalParam param = new GoalParam();
            param.setId((long)jindex);
            param.setName("Some Goal Param Name " + jindex);
            params.add(param);
        }

        List<GoalParamDto> paramDtoList = goalMapper.getParamList(params);
        assertEquals(params.size(), paramDtoList.size());

        for (int index = 0; index < paramDtoList.size(); index++) {
            GoalParamDto paramDto = paramDtoList.get(index);
            GoalParam param = params.get(index);
            assertParam(param, paramDto);
        }
    }

    private void assertGoal(Goal goal, GoalDto dto) {
        assertEquals((Long)goal.getId(), (Long)dto.getId());
        assertEquals(goal.getName(), dto.getName());
    }

    private void assertParam(GoalParam param, GoalParamDto dto) {
        assertEquals((Long)param.getId(), (Long)dto.getId());
        assertEquals(param.getName(), dto.getName());
    }

}
