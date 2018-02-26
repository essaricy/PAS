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
import com.softvision.ipm.pms.goal.repo.GoalCaDataRepository;

@Service
public class GoalService {

	@Autowired
	private GoalCaDataRepository goalCaDataRepository;

	public List<GoalDto> getGoals() {
		return GoalAssembler.getGoals(goalCaDataRepository.findAll());
	}

	public GoalDto getGoal(Long id) {
		return GoalAssembler.getGoal(goalCaDataRepository.findById(id));
	}

	public GoalDto update(GoalDto goalDto) throws ServiceException {
		try {
			if (goalDto == null) {
				throw new ServiceException("No Goal is provided.");
			}
			ValidationUtil.validate(goalDto);
			Goal goal = GoalAssembler.getGoal(goalDto);
			return GoalAssembler.getGoal(goalCaDataRepository.save(goal));
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

	public void delete(Long id) throws ServiceException {
		try {
			goalCaDataRepository.delete(id);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

}
