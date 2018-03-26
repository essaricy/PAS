package com.softvision.ipm.pms.assess.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softvision.ipm.pms.assess.entity.AssessHeader;

public interface AssessmentHeaderDataRepository extends CrudRepository<AssessHeader, Long>{

	List<AssessHeader> findByAssignIdOrderByStatusAsc(long assignmentId);

	AssessHeader findFirstByAssignIdOrderByStatusDesc(long assignmentId);

	AssessHeader findFirstByAssignIdAndStatus(long assignmentId, int status);

}
