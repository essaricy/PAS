package com.softvision.digital.pms.goal.mapper;

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

import com.softvision.digital.pms.goal.entity.Goal;
import com.softvision.digital.pms.goal.entity.GoalParam;
import com.softvision.digital.pms.goal.mapper.GoalMapper;
import com.softvision.digital.pms.goal.model.GoalDto;
import com.softvision.digital.pms.goal.model.GoalParamDto;
import com.softvision.digital.pms.web.config.MyModelMapper;

public class GoalMapperTest {

	@Spy ModelMapper mapper = new MyModelMapper();

	@InjectMocks private GoalMapper goalMapper;

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void prepare() {
		((MyModelMapper)mapper).loadMappings();
	}

    @Test
    public void shouldReturn_Null_When_GoalDto_isNull() {
        assertNull(goalMapper.getGoal((GoalDto) null));
    }

    @Test
    public void shouldReturn_Null_When_GoalParamDto_isNull() {
        assertNull(goalMapper.getParam((GoalParamDto) null));
    }

    @Test
    public void shouldReturn_Goal_When_GoalDto_isValid() {
        GoalDto dto = new GoalDto();
        dto.setId(10);
        dto.setName("Some Goal Name");

        Goal goal = goalMapper.getGoal(dto);
        assertGoal(dto, goal);
    }

    @Test
    public void shouldReturn_GoalParam_When_GoalParamDto_isValid() {
        GoalParamDto dto = new GoalParamDto();
        dto.setId(10);
        dto.setName("Some Goal Param Name");

        GoalParam goalParam = goalMapper.getParam(dto);
        assertPhase(dto, goalParam);
    }

    @Test
    public void shouldReturn_GoalList_When_GoalDtoList_isValid() {
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
    public void shouldReturn_GoalParamList_When_GoalParamDtoList_isValid() {
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

    @Test
    public void shouldReturn_Null_When_Goal_isNull() {
        assertNull(goalMapper.getGoal((Goal) null));
    }

    @Test
    public void shouldReturn_Null_When_GoalParam_isNull() {
        assertNull(goalMapper.getParam((GoalParam) null));
    }

    @Test
    public void shouldReturn_GoalDto_When_Goal_isValid() {
        Goal goal = new Goal();
        goal.setId(10L);
        goal.setName("Some Goal Name");
        GoalDto dto = goalMapper.getGoal(goal);
        assertGoal(goal, dto);
    }

    @Test
    public void shouldReturn_GoalParamDto_When_GoalParam_isValid() {
        GoalParam param = new GoalParam();
        param.setId(10L);
        param.setName("Some Goal Param Name");
        GoalParamDto dto = goalMapper.getParam(param);
        assertParam(param, dto);
    }

    @Test
    public void shouldReturn_GoalDtoList_When_GoalList_isValid() {
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
    public void shouldReturn_GoalParamDtoList_When_GoalParamList_isValid() {
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


    private void assertGoal(GoalDto dto, Goal goal) {
        assertEquals((Long)dto.getId(), (Long)goal.getId());
        assertEquals(dto.getName(), goal.getName());
    }

    private void assertPhase(GoalParamDto dto, GoalParam goalParam) {
        assertEquals((Long)dto.getId(), (Long)goalParam.getId());
        assertEquals(dto.getName(), goalParam.getName());
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
