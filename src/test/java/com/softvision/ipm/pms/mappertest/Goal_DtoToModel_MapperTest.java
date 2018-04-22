package com.softvision.ipm.pms.mappertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;

import com.softvision.ipm.pms.goal.entity.Goal;
import com.softvision.ipm.pms.goal.entity.GoalParam;
import com.softvision.ipm.pms.goal.mapper.GoalMapper;
import com.softvision.ipm.pms.goal.model.GoalDto;
import com.softvision.ipm.pms.goal.model.GoalParamDto;
import com.softvision.ipm.pms.web.config.MyModelMapper;

public class Goal_DtoToModel_MapperTest {

	@Spy ModelMapper mapper = new MyModelMapper();

	@InjectMocks private GoalMapper goalMapper;

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void prepare() {
		((MyModelMapper)mapper).loadMappings();
	}

    @Test
    public void test_goalNull() {
        assertNull(goalMapper.getGoal((GoalDto) null));
    }

    @Test
    public void test_paramNull() {
        assertNull(goalMapper.getParam((GoalParamDto) null));
    }

    @Test
    public void test_goalValid() {
        GoalDto dto = new GoalDto();
        dto.setId(10);
        dto.setName("Some Goal Name");

        Goal goal = goalMapper.getGoal(dto);
        assertGoal(dto, goal);
    }

    @Test
    public void test_goalParamValid() {
        GoalParamDto dto = new GoalParamDto();
        dto.setId(10);
        dto.setName("Some Goal Param Name");

        GoalParam goalParam = goalMapper.getParam(dto);
        assertPhase(dto, goalParam);
    }

    @Test
    public void test_goalList() {
        int numberOfGoals = 10;
        List<GoalDto> dtoList = new ArrayList<>();
        for (int index = 0; index < numberOfGoals; index++) {
            GoalDto dto = new GoalDto();
            dto.setId(10);
            dto.setName("Goal Name " + index);
            dtoList.add(dto);

            int numberOfParams = 50;
            List<GoalParamDto> goalParamDtoList = new ArrayList<>();
            for (int jindex = 0; jindex < numberOfParams; jindex++) {
                GoalParamDto goalParamDto = new GoalParamDto();
                goalParamDto.setId(10);
                goalParamDto.setName("Some Goal Param Name " + jindex);
                goalParamDtoList.add(goalParamDto);
            }
            dto.setParams(goalParamDtoList);
        }

        List<Goal> goals = goalMapper.getGoals(dtoList);
        assertEquals(dtoList.size(), goals.size());

        for (int index = 0; index < dtoList.size(); index++) {
            Goal goal = goals.get(index);
            GoalDto dto = dtoList.get(index);
            assertGoal(dto, goal);

            List<GoalParamDto> goalParamDtoList = dto.getParams();
            List<GoalParam> goalParams = goal.getParams();
            assertEquals(goalParamDtoList.size(), goalParams.size());
            
            for (int jindex = 0; jindex < goalParams.size(); jindex++) {
                GoalParam goalParam = goalParams.get(index);
                GoalParamDto goalParamDto = goalParamDtoList.get(index);
                assertPhase(goalParamDto, goalParam);
            }
        }
    }

    @Test
    public void test_goalParamList() {
        int numberOfPhases = 20;
        List<GoalParamDto> dtosList1 = new ArrayList<>();
        for (int jindex = 0; jindex < numberOfPhases; jindex++) {
            GoalParamDto dto = new GoalParamDto();
            dto.setId(10);
            dto.setName("Some Goal Param Name " + jindex);
            dtosList1.add(dto);
        }

        List<GoalParam> goalParams = goalMapper.getParams(dtosList1);
        assertEquals(dtosList1.size(), goalParams.size());

        for (int index = 0; index < goalParams.size(); index++) {
            GoalParam goalParam = goalParams.get(index);
            GoalParamDto goalParamDto = dtosList1.get(index);
            assertPhase(goalParamDto, goalParam);
        }
    }

    private void assertGoal(GoalDto dto, Goal goal) {
        assertEquals((Long)dto.getId(), (Long)goal.getId());
        assertEquals(dto.getName(), goal.getName());
    }

    private void assertPhase(GoalParamDto dto, GoalParam goalParam) {
        assertEquals((Long)dto.getId(), (Long)goalParam.getId());
        assertEquals(dto.getName(), goalParam.getName());
    }

}
