package com.softvision.ipm.pms.goal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ExceptionUtil;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.goal.entity.GoalCa;
import com.softvision.ipm.pms.goal.repo.GoalCaDataRepository;

@Service
public class GoalService {

	@Autowired
	private GoalCaDataRepository goalCaDataRepository;

	public List<GoalCa> getGoals() {
		return goalCaDataRepository.findAll();
	}

	public GoalCa getGoal(Long id) {
		return goalCaDataRepository.findById(id);
	}

	public GoalCa update(GoalCa goalCa) throws ServiceException {
		try {
			if (goalCa == null) {
				throw new ServiceException("Competency Assessment information is not provided.");
			}
			ValidationUtil.validate(goalCa);
			return goalCaDataRepository.save(goalCa);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExcceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

	public void delete(Long id) throws ServiceException {
		try {
			goalCaDataRepository.delete(id);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExcceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

}
