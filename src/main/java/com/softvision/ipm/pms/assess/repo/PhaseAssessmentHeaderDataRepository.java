package com.softvision.ipm.pms.assess.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softvision.ipm.pms.assess.entity.PhaseAssessHeader;

public interface PhaseAssessmentHeaderDataRepository extends CrudRepository<PhaseAssessHeader, Long>{

	List<PhaseAssessHeader> findByAssignIdOrderByStatusAsc(long assignmentId);

	/*@Query("select status from phase_assess_header where assign_id=:assign_id order by status desc")
    int getLatestStatus(long assignmentId);*/

	PhaseAssessHeader findFirstByAssignIdOrderByStatusDesc(long assignmentId);

}
