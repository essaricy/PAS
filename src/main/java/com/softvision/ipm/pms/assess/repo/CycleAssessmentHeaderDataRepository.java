package com.softvision.ipm.pms.assess.repo;

import org.springframework.data.repository.CrudRepository;

import com.softvision.ipm.pms.assess.entity.CycleAssessHeader;

public interface CycleAssessmentHeaderDataRepository extends CrudRepository<CycleAssessHeader, Long>{

	CycleAssessHeader findByAssignId(long assignmentId);

}
