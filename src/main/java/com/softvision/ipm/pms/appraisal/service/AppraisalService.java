package com.softvision.ipm.pms.appraisal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ExceptionUtil;
import com.softvision.ipm.pms.common.util.ValidationUtil;

@Service
@Validated
public class AppraisalService {

	@Autowired
	private AppraisalCycleDataRepository appraisalCycleDataRepository;

	public List<AppraisalCycle> getCycles() {
		return appraisalCycleDataRepository.findAll();
	}

	public AppraisalCycle getCycle(Long id) {
		return appraisalCycleDataRepository.findById(id);
	}

	public AppraisalCycle update(AppraisalCycle appraisalCycle) throws ServiceException {
		try {
			if (appraisalCycle == null) {
				throw new ServiceException("Appraisal Cycle information is not provided.");
			}
			ValidationUtil.validate(appraisalCycle);
			return appraisalCycleDataRepository.save(appraisalCycle);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExcceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

	public void delete(Long id) throws ServiceException {
		try {
			appraisalCycleDataRepository.delete(id);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExcceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

}
