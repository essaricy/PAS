package com.softvision.digital.pms.appraisal.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.digital.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.digital.pms.appraisal.entity.AppraisalCycle;
import com.softvision.digital.pms.appraisal.mapper.AppraisalMapper;
import com.softvision.digital.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.digital.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.digital.pms.appraisal.repo.AppraisalRepository;
import com.softvision.digital.pms.common.exception.ServiceException;
import com.softvision.digital.pms.common.util.ExceptionUtil;
import com.softvision.digital.pms.common.util.ValidationUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AppraisalService {

	private static final String APPRAISAL_CYCLE_INFORMATION_NOT_FOUND = "Appraisal cycle information not found";

	@Autowired private AppraisalRepository appraisalRepository;

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	@Autowired private AppraisalMapper appraisalMapper;

	public List<AppraisalCycleDto> getCycles() {
		return appraisalMapper.getCycleList(appraisalCycleDataRepository.findAllByOrderByStartDateDesc());
	}

	public AppraisalCycleDto getCycle(Integer id) {
		return appraisalMapper.getCycle(appraisalCycleDataRepository.findById(id).orElse(null));
	}

	public AppraisalCycleDto update(AppraisalCycleDto appraisalCycleDto) throws ServiceException {
		try {
			log.info("Updating appraisal cycle: {} ", appraisalCycleDto);
			if (appraisalCycleDto == null) {
				throw new ServiceException("Appraisal Cycle information is not provided.");
			}
			int id = appraisalCycleDto.getId();
			if (id == 0) {
				appraisalCycleDto.setStatus(AppraisalCycleStatus.DRAFT);
			} else {
				Optional<AppraisalCycle> cycleOptional = appraisalCycleDataRepository.findById(id);
				AppraisalCycle appraisalCycle = cycleOptional.orElseThrow(() -> new ServiceException(APPRAISAL_CYCLE_INFORMATION_NOT_FOUND));

				AppraisalCycleStatus existingStatus = AppraisalCycleStatus.get(appraisalCycle.getStatus());
				if (existingStatus != AppraisalCycleStatus.DRAFT) {
				    throw new ServiceException("Updates are allowed only when its in DRAFT status");
				}
			}
			ValidationUtil.validate(appraisalCycleDto);
			// validate dates overlap
			AppraisalCycle appraisalCycle = appraisalMapper.getCycle(appraisalCycleDto);
			AppraisalCycle saved = appraisalCycleDataRepository.save(appraisalCycle);
			return appraisalMapper.getCycle(saved);
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
		Optional<AppraisalCycle> cycleOptional = appraisalCycleDataRepository.findById(id);
		AppraisalCycle appraisalCycle = cycleOptional.orElseThrow(() -> new ServiceException(APPRAISAL_CYCLE_INFORMATION_NOT_FOUND));

		AppraisalCycleStatus existingStatus = AppraisalCycleStatus.get(appraisalCycle.getStatus());
		log.info("Updating appraisal cycle status from {} to {}", existingStatus, desiredStatus);

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
            int count= appraisalCycleDataRepository.countOfOtherActiveCycles(id);
            if(count > 0) {
                throw new ServiceException("There is another appraisal cycle ACTIVE already. Mark it COMPLETE to activate another appraisal cycle");
            }
        } else if (existingStatus == AppraisalCycleStatus.ACTIVE && desiredStatus == AppraisalCycleStatus.COMPLETE) {
            // check if any assigned are not completed. otherwise allow.
        }
		appraisalCycle.setStatus(desiredStatus.toString());
		appraisalCycleDataRepository.save(appraisalCycle);
	}

	public void delete(Integer id) throws ServiceException {
		try {
			Optional<AppraisalCycle> cycleOptional = appraisalCycleDataRepository.findById(id);
			AppraisalCycle appraisalCycle = cycleOptional.orElseThrow(() -> new ServiceException(APPRAISAL_CYCLE_INFORMATION_NOT_FOUND));

			AppraisalCycleStatus existingStatus = AppraisalCycleStatus.get(appraisalCycle.getStatus());
			if (existingStatus != AppraisalCycleStatus.DRAFT) {
				throw new ServiceException("Deleting an appraisal cycle is allowed only when its a draft.");
			}
			appraisalCycleDataRepository.deleteById(id);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

	public AppraisalCycleDto getActiveCycle() {
		return appraisalMapper.getCycle(appraisalRepository.getActiveCycle().orElse(null));
	}

	public List<AppraisalCycleDto> getAssignableCycle() {
		return appraisalMapper.getCycleList(appraisalRepository.getAssignableCycles());
	}

}