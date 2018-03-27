package com.softvision.ipm.pms.appraisal.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.assembler.AppraisalAssembler;
import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.appraisal.repo.AppraisalRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ExceptionUtil;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.email.repo.EmailRepository;

@Service
public class AppraisalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppraisalService.class);

	@Autowired private AppraisalRepository appraisalRepository;

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	@Autowired private EmailRepository emailRepository;

	public List<AppraisalCycleDto> getCycles() {
		return AppraisalAssembler.getCycles(appraisalCycleDataRepository.findAllByOrderByStartDateDesc());
	}

	public AppraisalCycleDto getCycle(Integer id) {
		return AppraisalAssembler.getCycle(appraisalCycleDataRepository.findById(id));
	}

	public AppraisalCycleDto update(AppraisalCycleDto appraisalCycleDto) throws ServiceException {
		try {
			LOGGER.info("Updating appraisal cycle: " + appraisalCycleDto);
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
				if (existingStatus == AppraisalCycleStatus.ACTIVE) {
					throw new ServiceException("Cannot update the appraisal cycle as it is ACTIVE");
				} else if (existingStatus == AppraisalCycleStatus.COMPLETE) {
					throw new ServiceException("Cannot update the appraisal cycle as it is COMPLETE");
				}
			}
			ValidationUtil.validate(appraisalCycleDto);
			// TODO validate dates overlap
			AppraisalCycle appraisalCycle = AppraisalAssembler.getCycle(appraisalCycleDto);
			AppraisalCycle saved = appraisalCycleDataRepository.save(appraisalCycle);
			return AppraisalAssembler.getCycle(saved);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

	public void changeStatus(Integer id, AppraisalCycleStatus status) throws ServiceException {
		boolean sendActivationEmail=false;
		boolean sendConcludeEmail=false;
		if (id <= 0) {
			throw new ServiceException("Invalid id");
		}
		if (status == null) {
			throw new ServiceException("Appraisal Cycle status is not provided. Must be of " + Arrays.toString(AppraisalCycleStatus.values()));
		}
		AppraisalCycle appraisalCycle = appraisalCycleDataRepository.findById(id);
		if (appraisalCycle == null) {
			throw new ServiceException("Appraisal Cycle information is not provided.");
		}
		AppraisalCycleStatus existingStatus = AppraisalCycleStatus.get(appraisalCycle.getStatus());
		LOGGER.info("Updating appraisal cycle status from " + existingStatus + " to " + status);
		if (existingStatus == AppraisalCycleStatus.DRAFT) {
			if (status == AppraisalCycleStatus.DRAFT) {
				throw new ServiceException("Appraisal Cycle is already a DRAFT");
			} else if (status == AppraisalCycleStatus.ACTIVE) {
				// Check if any other cycle is active. do not allow otherwise
				int count= appraisalCycleDataRepository.countOfActive(id);
				if(count > 0) {
					throw new ServiceException("There is already an appraisal cycle ACTIVE. Mark it COMPLETE to activate another appraisal cycle");
				}
				sendActivationEmail=true;
			} else if (status == AppraisalCycleStatus.COMPLETE) {
				throw new ServiceException("Cannot change an Appraisal Cycle to COMPLETE without making it ACTIVE");
			}
		} else if (existingStatus == AppraisalCycleStatus.ACTIVE) {
			if (status == AppraisalCycleStatus.DRAFT) {
				// TODO check if the cycle is assigned. If not, allow changing back to DRAFT
				throw new ServiceException("Appraisal Cycle is already ACTIVE. Cannot change to DRAFT.");
			} else if (status == AppraisalCycleStatus.ACTIVE) {
				throw new ServiceException("Appraisal Cycle is already ACTIVE");
			} else if (status == AppraisalCycleStatus.COMPLETE) {
				// TODO check if any assigned are not completed. otherwise allow.
				sendConcludeEmail= true;
			}
		} else if (existingStatus == AppraisalCycleStatus.COMPLETE) {
			throw new ServiceException("Cannot change the status of appraisal cycle. Its already COMPLETE.");
		}
		appraisalCycle.setStatus(status.toString());
		appraisalCycleDataRepository.save(appraisalCycle);
		if (sendActivationEmail) {
			emailRepository.sendApprasialKickOff(appraisalCycle);
		}
		if (sendConcludeEmail) {
			// Send email to whole group
			emailRepository.sendApprasialCycleConclude(appraisalCycle);
		}
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
		return AppraisalAssembler.getCycle(appraisalRepository.getActiveCycle());
	}

}
