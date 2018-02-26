package com.softvision.ipm.pms.goal.assembler;

import java.util.ArrayList;
import java.util.List;

import com.softvision.ipm.pms.goal.entity.Goal;
import com.softvision.ipm.pms.goal.entity.GoalParam;
import com.softvision.ipm.pms.goal.model.GoalDto;
import com.softvision.ipm.pms.goal.model.GoalParamDto;

public class GoalAssembler {

	public static List<GoalDto> getGoals(List<Goal> all) {
		List<GoalDto> list = new ArrayList<>();
		for (Goal object : all) {
			list.add(getGoal(object));
		}
		return list;
	}

	public static GoalDto getGoal(Goal object) {
		GoalDto dto = null;
		if (object != null) {
			dto = new GoalDto();
			dto.setId(object.getId());
			dto.setName(object.getName());
			dto.setParams(getParamsList(object.getParams()));
		}
		return dto;
	}

	private static List<GoalParamDto> getParamsList(List<GoalParam> all) {
		List<GoalParamDto> list = new ArrayList<>();
		for (GoalParam object : all) {
			list.add(getParam(object));
		}
		return list;
	}

	private static GoalParamDto getParam(GoalParam object) {
		GoalParamDto dto = null;
		if (object != null) {
			dto = new GoalParamDto();
			dto.setId(object.getId());
			dto.setName(object.getName());
			dto.setApplicable(object.getApply());
		}
		return dto;
	}

	public static Goal getGoal(GoalDto dto) {
		Goal object = null;
		if (dto != null) {
			object = new Goal();
			object.setId(dto.getId());
			object.setName(dto.getName());
			object.setParams(getParams(dto.getParams()));
		}
		return object;
	}

	private static List<GoalParam> getParams(List<GoalParamDto> params) {
		List<GoalParam> all = new ArrayList<>();
		for (GoalParamDto dto : params) {
			all.add(getParam(dto));
		}
		return all;
	}

	private static GoalParam getParam(GoalParamDto dto) {
		GoalParam object = null;
		if (dto != null) {
			object = new GoalParam();
			object.setId(dto.getId());
			object.setName(dto.getName());
			object.setApply(dto.getApplicable());
		}
		return object;
	}

}
