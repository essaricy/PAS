package com.softvision.digital.pms.assess.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softvision.digital.pms.assess.entity.AssessHeader;

public interface AssessmentHeaderDataRepository extends CrudRepository<AssessHeader, Long>{

	AssessHeader findFirstByAssignIdAndStage(long assignmentId, int stage);

	List<AssessHeader> findByAssignIdOrderByStageAsc(long assignmentId);

}
