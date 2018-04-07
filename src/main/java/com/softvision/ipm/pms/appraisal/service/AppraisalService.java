package com.softvision.ipm.pms.appraisal.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.mapper.AppraisalMapper;
import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.appraisal.repo.AppraisalRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ExceptionUtil;
import com.softvision.ipm.pms.common.util.ValidationUtil;

@Service
public class AppraisalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppraisalService.class);

	@Autowired private AppraisalRepository appraisalRepository;

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	public List<AppraisalCycleDto> getCycles() {
		return AppraisalMapper.getCycleList(appraisalCycleDataRepository.findAllByOrderByStartDateDesc());
	}

	public AppraisalCycleDto getCycle(Integer id) {
		return AppraisalMapper.getCycle(appraisalCycleDataRepository.findById(id));
	}

	public AppraisalCycleDto update(AppraisalCycleDto appraisalCycleDto) throws ServiceException {
		try {
			LOGGER.info("Updating appraisal cycle: {} ", appraisalCycleDto);
			if (appraisalCycleDto == null) {
				throw new ServiceException("Appraisal Cycle information is not provided.");
			}
			int id = appraisalCycleDto.getId();
			if (id == 0) {
				appraisalCycleDto.setStatus(AppraisalCycleStatus.DRAFT);
			} else {
				AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findById(id);
				if (appraisalCycle == null) {
					throw new ServiceException("Appraisal cycle information not found.");
				}
				AppraisalCycleStatus existingStatus = AppraisalCycleStatus.get(appraisalCycle.getStatus());
				if (existingStatus != AppraisalCycleStatus.DRAFT) {
				    throw new ServiceException("Updates are allowed only when its in DRAFT status");
				}
			}
			ValidationUtil.validate(appraisalCycleDto);
			// TODO validate dates overlap
			AppraisalCycle appraisalCycle = AppraisalMapper.getCycle(appraisalCycleDto);
			AppraisalCycle saved = appraisalCycleDataRepository.save(appraisalCycle);
			return AppraisalMapper.getCycle(saved);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

	public void changeStatus(Integer id, AppraisalCycleStatus desiredStatus) throws ServiceException {
		if (id <= 0) {
			throw new ServiceException("Invalid id");
		}
		if (desiredStatus == null) {
			throw new ServiceException("Appraisal Cycle status is not provided. Must be of " + Arrays.toString(AppraisalCycleStatus.values()));
		}
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findById(id);
		if (appraisalCycle == null) {
			throw new ServiceException("Appraisal Cycle information is not provided.");
		}
		AppraisalCycleStatus existingStatus = AppraisalCycleStatus.get(appraisalCycle.getStatus());
		LOGGER.info("Updating appraisal cycle status from {} to {}", existingStatus, desiredStatus);

		if (existingStatus == desiredStatus) {
		    throw new ServiceException("Appraisal Cycle is already in a " + existingStatus + " status");
		} else if (existingStatus != AppraisalCycleStatus.DRAFT && desiredStatus == AppraisalCycleStatus.DRAFT) {
            throw new ServiceException("Appraisal Cycle is " + existingStatus + ". Cannot change it back to DRAFT.");
        } else if (existingStatus != AppraisalCycleStatus.DRAFT && desiredStatus == AppraisalCycleStatus.READY) {
            throw new ServiceException("Cannot change an Appraisal Cycle to READY when its not a DRAFT");
        } else if (existingStatus != AppraisalCycleStatus.READY && desiredStatus == AppraisalCycleStatus.ACTIVE) {
            throw new ServiceException("Cannot change an Appraisal Cycle to ACTIVE when its not READY");
        } else if (existingStatus != AppraisalCycleStatus.ACTIVE && desiredStatus == AppraisalCycleStatus.COMPLETE) {
            throw new ServiceException("Cannot change an Appraisal Cycle to COMPLETE when its not ACTIVE");
        } else if (existingStatus == AppraisalCycleStatus.READY && desiredStatus == AppraisalCycleStatus.ACTIVE) {
            // Check if any other cycle is active. do not allow otherwise
            int count= appraisalCycleDataRepository.countOfActive(id);
            if(count > 0) {
                throw new ServiceException("There is another appraisal cycle ACTIVE already. Mark it COMPLETE to activate another appraisal cycle");
            }
        } else if (existingStatus == AppraisalCycleStatus.ACTIVE && desiredStatus == AppraisalCycleStatus.COMPLETE) {
            // TODO check if any assigned are not completed. otherwise allow.
        }
		appraisalCycle.setStatus(desiredStatus.toString());
		appraisalCycleDataRepository.save(appraisalCycle);
	}

	public void delete(Integer id) throws ServiceException {
		try {
			AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findById(id);
			if (appraisalCycle == null) {
				throw new ServiceException("Appraisal cycle information not found.");
			}
			AppraisalCycleStatus existingStatus = AppraisalCycleStatus.get(appraisalCycle.getStatus());
			if (existingStatus != AppraisalCycleStatus.DRAFT) {
				throw new ServiceException("Deleting an appraisal cycle is allowed only when its a draft.");
			}
			appraisalCycleDataRepository.delete(id);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

	public AppraisalCycleDto getActiveCycle() {
		return AppraisalMapper.getCycle(appraisalRepository.getActiveCycle());
	}

	public List<AppraisalCycleDto> getAssignableCycle() {
		List<AppraisalCycleDto> cycles = AppraisalMapper.getCycleList(appraisalRepository.getAssignableCycles());
		return cycles;
	}

}
