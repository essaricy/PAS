package com.softvision.ipm.pms.assess.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.assign.repo.AssignmentCycleDataRepository;
import com.softvision.ipm.pms.common.exception.ServiceException;

@Service
public class CycleAssessmentService {

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	@Autowired private AssignmentCycleDataRepository assignmentCycleDataRepository;

	public void abridge(int employeeId) throws ServiceException {
		AppraisalCycle activeCycle = appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
		if (activeCycle == null) {
			throw new ServiceException("There is no appraisal cycle ACTIVE. You cannot freeze assessments");
		}
		// Validate if all phase assignments are frozen
		List<AppraisalPhase> phases = activeCycle.getPhases();
		for (AppraisalPhase appraisalPhase : phases) {
			
		}
		// Update cycle assignment
		// Create a cycle assessment header and detail
	}

}
