package com.softvision.ipm.pms.assess.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softvision.ipm.pms.assess.entity.PhaseAssessHeader;

public interface PhaseAssessmentHeaderDataRepository extends CrudRepository<PhaseAssessHeader, Long>{

	List<PhaseAssessHeader> findByAssignIdOrderByStatusAsc(long assignmentId);

	PhaseAssessHeader findFirstByAssignIdOrderByStatusDesc(long assignmentId);

	PhaseAssessHeader findFirstByAssignIdAndStatus(long assignmentId, int status);

}
