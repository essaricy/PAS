package com.softvision.digital.pms.goal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.digital.pms.common.exception.ServiceException;
import com.softvision.digital.pms.common.util.ExceptionUtil;
import com.softvision.digital.pms.common.util.ValidationUtil;
import com.softvision.digital.pms.goal.entity.Goal;
import com.softvision.digital.pms.goal.mapper.GoalMapper;
import com.softvision.digital.pms.goal.model.GoalDto;
import com.softvision.digital.pms.goal.repo.GoalDataRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoalService {

	@Autowired private GoalDataRepository goalDataRepository;

	@Autowired GoalMapper goalMapper;

	public List<GoalDto> getGoals() {
		return goalMapper.getGoalList(goalDataRepository.findAll());
	}

	public List<GoalDto> getActiveGoals() {
		List<GoalDto> goals = getGoals();
		goals.forEach(goal -> 
			goal.setParams(goal.getParams().stream().filter(param -> {
				String applicable = param.getApplicable();
				return applicable!=null && applicable.equalsIgnoreCase("Y");
			}).collect(Collectors.toList()))
		);
		return goals;
	}

	public GoalDto getGoal(Long id) {
		return goalMapper.getGoal(goalDataRepository.findById(id).orElse(null));
	}

	public GoalDto update(GoalDto goalDto) throws ServiceException {
		try {
		    log.info("update: START goalDto={}", goalDto);
			if (goalDto == null) {
				throw new ServiceException("No Goal is provided.");
			}
			ValidationUtil.validate(goalDto);
			Goal goal = goalMapper.getGoal(goalDto);
			log.info("update: END goalDto={}", goalDto);
			return goalMapper.getGoal(goalDataRepository.save(goal));
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

	public void delete(Long id) throws ServiceException {
		try {
		    log.info("delete: START id={}", id);
			goalDataRepository.deleteById(id);
			log.info("delete: END id={}", id);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

}
