package com.softvision.ipm.pms.appraisal.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.common.repo.AbstractRepository;

@Repository
public class AppraisalRepository extends AbstractRepository {

	@Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

	public AppraisalCycle getActiveCycle() {
		return appraisalCycleDataRepository.findOneByStatus(AppraisalCycleStatus.ACTIVE.toString());
	}

}
