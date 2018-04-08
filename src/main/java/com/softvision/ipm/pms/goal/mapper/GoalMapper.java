package com.softvision.ipm.pms.goal.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.ipm.pms.goal.entity.Goal;
import com.softvision.ipm.pms.goal.entity.GoalParam;
import com.softvision.ipm.pms.goal.model.GoalDto;
import com.softvision.ipm.pms.goal.model.GoalParamDto;

@Component
public class GoalMapper {

	@Autowired private ModelMapper mapper;

	public List<GoalDto> getGoalList(List<Goal> all) {
	    return mapper.map(all, new TypeToken<List<GoalDto>>() {}.getType());
	}

	public GoalDto getGoal(Goal object) {
	    return mapper.map(object, GoalDto.class);
	}

	public List<GoalParamDto> getParamList(List<GoalParam> all) {
	    return mapper.map(all, new TypeToken<List<GoalParamDto>>() {}.getType());
	}

	public GoalParamDto getParam(GoalParam object) {
	    return mapper.map(object, GoalParamDto.class);
	}

    public List<Goal> getGoals(List<GoalDto> list) {
        return mapper.map(list, new TypeToken<List<Goal>>() {}.getType());
    }

	public Goal getGoal(GoalDto dto) {
	    return mapper.map(dto, Goal.class);
	}

	public List<GoalParam> getParams(List<GoalParamDto> params) {
	    return mapper.map(params, new TypeToken<List<GoalParam>>() {}.getType());
	}

	public GoalParam getParam(GoalParamDto dto) {
	    return mapper.map(dto, GoalParam.class);
	}

}
