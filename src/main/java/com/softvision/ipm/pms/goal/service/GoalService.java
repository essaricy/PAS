package com.softvision.ipm.pms.goal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ExceptionUtil;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.goal.assembler.GoalAssembler;
import com.softvision.ipm.pms.goal.entity.Goal;
import com.softvision.ipm.pms.goal.model.GoalDto;
import com.softvision.ipm.pms.goal.model.GoalParamDto;
import com.softvision.ipm.pms.goal.repo.GoalDataRepository;

@Service
public class GoalService {

	@Autowired
	private GoalDataRepository goalDataRepository;

	public List<GoalDto> getGoals() {
		return GoalAssembler.getGoals(goalDataRepository.findAll());
	}

	public List<GoalDto> getActiveGoals() {
		List<GoalDto> goals = getGoals();
		for (GoalDto goal : goals) {
			List<GoalParamDto> params = goal.getParams();
			for (int index = 0; index < params.size(); index++) {
				GoalParamDto param = params.get(index);
				String applicable = param.getApplicable();
				if (applicable == null || !applicable.equalsIgnoreCase("Y")) {
					params.remove(index);
					index--;
				}
			}
		}
		return goals;
	}

	public GoalDto getGoal(Long id) {
		return GoalAssembler.getGoal(goalDataRepository.findById(id));
	}

	public GoalDto update(GoalDto goalDto) throws ServiceException {
		try {
			if (goalDto == null) {
				throw new ServiceException("No Goal is provided.");
			}
			ValidationUtil.validate(goalDto);
			Goal goal = GoalAssembler.getGoal(goalDto);
			return GoalAssembler.getGoal(goalDataRepository.save(goal));
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

	public void delete(Long id) throws ServiceException {
		try {
			goalDataRepository.delete(id);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

}
