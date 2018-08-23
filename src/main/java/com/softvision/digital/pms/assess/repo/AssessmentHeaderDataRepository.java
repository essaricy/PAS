package com.softvision.digital.pms.assess.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.softvision.digital.pms.assess.entity.AssessHeader;

public interface AssessmentHeaderDataRepository extends CrudRepository<AssessHeader, Long>{

	Optional<AssessHeader> findFirstByAssignIdAndStage(long assignmentId, int stage);

	List<AssessHeader> findByAssignIdOrderByStageAsc(long assignmentId);

}
