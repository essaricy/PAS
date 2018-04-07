package com.softvision.ipm.pms.goal.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import com.softvision.ipm.pms.common.mapper.CustomModelMapper;
import com.softvision.ipm.pms.goal.entity.Goal;
import com.softvision.ipm.pms.goal.entity.GoalParam;
import com.softvision.ipm.pms.goal.model.GoalDto;
import com.softvision.ipm.pms.goal.model.GoalParamDto;

public class GoalMapper {

    private static ModelMapper mapper = new CustomModelMapper();

	public static List<GoalDto> getGoalList(List<Goal> all) {
	    return mapper.map(all, new TypeToken<List<GoalDto>>() {}.getType());
	}

	public static GoalDto getGoal(Goal object) {
	    return mapper.map(object, GoalDto.class);
	}

	public static List<GoalParamDto> getParamList(List<GoalParam> all) {
	    return mapper.map(all, new TypeToken<List<GoalParamDto>>() {}.getType());
	}

	public static GoalParamDto getParam(GoalParam object) {
	    return mapper.map(object, GoalParamDto.class);
	}

    public static List<Goal> getGoals(List<GoalDto> list) {
        return mapper.map(list, new TypeToken<List<Goal>>() {}.getType());
    }

	public static Goal getGoal(GoalDto dto) {
	    return mapper.map(dto, Goal.class);
	}

	public static List<GoalParam> getParams(List<GoalParamDto> params) {
	    return mapper.map(params, new TypeToken<List<GoalParam>>() {}.getType());
	}

	public static GoalParam getParam(GoalParamDto dto) {
	    return mapper.map(dto, GoalParam.class);
	}

}
